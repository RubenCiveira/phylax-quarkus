/* @autogenerated */
package net.civeira.phylax.features.access.oauth.infrastructure.bootstrap;

import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.migration.Migrations;
import net.civeira.phylax.features.access.relyingparty.domain.RelyingParty;
import net.civeira.phylax.features.access.relyingparty.domain.gateway.RelyingPartyWriteRepositoryGateway;
import net.civeira.phylax.features.access.role.domain.Role;
import net.civeira.phylax.features.access.role.domain.gateway.RoleWriteRepositoryGateway;
import net.civeira.phylax.features.access.securitydomain.domain.SecurityDomain;
import net.civeira.phylax.features.access.securitydomain.domain.gateway.SecurityDomainWriteRepositoryGateway;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClient;
import net.civeira.phylax.features.access.trustedclient.domain.gateway.TrustedClientWriteRepositoryGateway;
import net.civeira.phylax.features.access.user.domain.User;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserWriteRepositoryGateway;
import net.civeira.phylax.features.access.useridentity.domain.UserIdentity;
import net.civeira.phylax.features.access.useridentity.domain.gateway.UserIdentityWriteRepositoryGateway;

@RequiredArgsConstructor
public class InitialFillService {
  private final UserWriteRepositoryGateway users;
  private final SecurityDomainWriteRepositoryGateway domains;
  private final RoleWriteRepositoryGateway roles;
  private final TrustedClientWriteRepositoryGateway clients;
  private final RelyingPartyWriteRepositoryGateway parties;
  private final UserIdentityWriteRepositoryGateway identities;

  @Transactional
  void registerResource(
      @Observes @Priority(Migrations.POST_MIGRATION_PHASE_PRIORITY) final StartupEvent ev) {
    long count = users.countForUpdate(UserFilter.builder().build());
    if (0 == count) {
      InitialConfigBean bean = new InitialConfigBean("sesamo");
      bean.getParties().forEach(proposal -> {
        RelyingParty created = RelyingParty.create(proposal);
        parties.create(created.enable());
      });
      bean.getClients().forEach(proposal -> {
        TrustedClient created = TrustedClient.create(proposal);
        clients.create(created.enable());
      });
      bean.getDomains().forEach(proposal -> {
        SecurityDomain created = SecurityDomain.create(proposal);
        domains.create(created.enable());
      });
      bean.getRoles().forEach(proposal -> {
        Role created = Role.create(proposal);
        roles.create(created);
      });
      bean.getUsers().forEach(proposal -> {
        users.create(User.create(proposal));
      });
      bean.getIdentities().forEach(proposal -> {
        identities.create(UserIdentity.create(proposal));
      });
    }
  }
}
