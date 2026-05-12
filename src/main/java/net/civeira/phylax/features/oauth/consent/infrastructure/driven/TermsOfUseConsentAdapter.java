package net.civeira.phylax.features.oauth.consent.infrastructure.driven;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.Tenant;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantFilter;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantReadRepositoryGateway;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.TenantTermsOfUse;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.TenantTermsOfUseReference;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.gateway.TenantTermsOfUseFilter;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.gateway.TenantTermsOfUseReadRepositoryGateway;
import net.civeira.phylax.features.access.user.domain.User;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserReadRepositoryGateway;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.domain.UserAcceptedTermnsOfUse;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.domain.UserAcceptedTermnsOfUseChangeSet;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.domain.gateway.UserAcceptedTermnsOfUseFilter;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.domain.gateway.UserAcceptedTermnsOfUseReadRepositoryGateway;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.domain.gateway.UserAcceptedTermnsOfUseWriteRepositoryGateway;
import net.civeira.phylax.features.oauth.consent.domain.TermsOfUseAcceptance;
import net.civeira.phylax.features.oauth.consent.domain.gateway.TermsOfUseConsentGateway;

@Transactional
@ApplicationScoped
@RequiredArgsConstructor
public class TermsOfUseConsentAdapter implements TermsOfUseConsentGateway {

  private final TenantReadRepositoryGateway tenants;
  private final UserReadRepositoryGateway users;
  private final TenantTermsOfUseReadRepositoryGateway terms;
  private final UserAcceptedTermnsOfUseReadRepositoryGateway acceptedTerms;
  private final UserAcceptedTermnsOfUseWriteRepositoryGateway acceptedTermsWriter;

  @Override
  public List<TermsOfUseAcceptance> getPendingConsent(String tenant, String username,
      List<String> audiences) {
    Optional<User> userOpt = resolveUser(tenant, username);
    if (userOpt.isEmpty()) {
      return List.of();
    }
    User user = userOpt.get();
    List<TenantTermsOfUse> activeTerms =
        terms.list(TenantTermsOfUseFilter.builder().tenant(user.getTenant()).build()).stream()
            .filter(term -> term.getActivationDate().map(d -> d.isBefore(OffsetDateTime.now()))
                .orElse(false))
            .sorted(Comparator.comparing(t -> t.getActivationDate().orElse(OffsetDateTime.MIN)))
            .toList();

    return activeTerms.stream()
        .filter(term -> acceptedTerms
            .find(UserAcceptedTermnsOfUseFilter.builder().conditions(term).user(user).build())
            .isEmpty())
        .map(term -> new TermsOfUseAcceptance(term.getUid(), term.getText())).toList();
  }

  @Override
  public void storeAcceptedConsent(String tenant, String username, List<String> audiences,
      TermsOfUseAcceptance consent) {
    Optional<User> userOpt = resolveUser(tenant, username);
    if (userOpt.isEmpty()) {
      return;
    }
    User user = userOpt.get();
    Optional<TenantTermsOfUse> termsOpt = terms.retrieve(consent.id(),
        Optional.of(TenantTermsOfUseFilter.builder().tenant(user.getTenant()).build()));
    if (termsOpt.isEmpty()) {
      return;
    }
    TenantTermsOfUse currentTerms = termsOpt.get();
    boolean alreadyAccepted = acceptedTerms
        .find(UserAcceptedTermnsOfUseFilter.builder().conditions(currentTerms).user(user).build())
        .isPresent();
    if (alreadyAccepted) {
      return;
    }
    UserAcceptedTermnsOfUse entity =
        UserAcceptedTermnsOfUse.create(new UserAcceptedTermnsOfUseChangeSet().newUid().user(user)
            .conditions(TenantTermsOfUseReference.of(consent.id()))
            .acceptDate(OffsetDateTime.now()));
    acceptedTermsWriter.create(entity);
  }

  private Optional<User> resolveUser(String tenantName, String username) {
    Optional<Tenant> tenant = tenants.find(TenantFilter.builder().name(tenantName).build());
    if (tenant.isEmpty()) {
      return Optional.empty();
    }
    return users.find(UserFilter.builder().tenant(tenant.get()).name(username).build());
  }
}
