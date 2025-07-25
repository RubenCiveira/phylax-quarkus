/* @autogenerated */
package net.civeira.phylax.features.access.oauth.application.usecase;

import java.util.Locale;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.oauth.application.service.ActiveUserFindService;
import net.civeira.phylax.features.access.oauth.application.service.RequiredConsentService;
import net.civeira.phylax.features.oauth.authentication.application.spi.UserConsentSpi.Consent;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;

@ApplicationScoped
@RequiredArgsConstructor
public class PendingConsentUsecase {
  private final RequiredConsentService terms;
  private final ActiveUserFindService activeUser;

  public Optional<Consent> userRequiredConsent(AuthRequest request, Locale locale,
      String username) {
    return activeUser.findEnabledUser(request.getTenant(), username, request.getAudiences())
        .flatMap(user -> terms.findPendingTerms(user))
        .map(terms -> Consent.builder().version(terms.getUid()).fullText(terms.getText()).build());
  }

  public boolean confirmConsent(AuthRequest request, String username, String version) {
    return activeUser.findEnabledUser(request.getTenant(), username, request.getAudiences())
        .map(user -> {
          terms.acceptPendingTerms(user, version);
          return true;
        }).orElse(false);
  }
}
