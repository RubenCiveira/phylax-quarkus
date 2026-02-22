package net.civeira.phylax.features.access.oauth.infrastructure.driver.impl.user;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import net.civeira.phylax.features.oauth.user.domain.RegistrationResult;
import net.civeira.phylax.features.oauth.user.domain.gateway.RegisterUserGateway;

/**
 * Interactor for user self-registration. Disabled (allowRegister=false) until real persistence is
 * integrated with TenantConfig.enableRegisterUsers and the temporal code system.
 *
 * TODO: integrate with UserApproveOptions, TenantConfig and temporal auth codes.
 */
@Transactional
@ApplicationScoped
public class RegisterUserInteractor implements RegisterUserGateway {

  @Override
  public boolean allowRegister(String tenant) {
    return true;
  }

  @Override
  public Optional<String> getRegisterConsent(String tenant) {
    return Optional.empty();
  }

  @Override
  public RegistrationResult requestForRegister(String urlBase, String tenant, String email,
      String password) {
    return RegistrationResult.cancel();
  }

  @Override
  public Optional<String> verifyRegister(String tenant, String code) {
    return Optional.empty();
  }
}
