package net.civeira.phylax.features.oauth.user.infrastructure.driven;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.features.oauth.user.domain.gateway.RegisterUserGateway;

/**
 * Adaptador stub para el registro de usuarios. La implementación real requiere integración con el
 * feature de acceso (access/) para crear usuarios y gestionar códigos de verificación.
 *
 * TODO: implementar con acceso a UserApproveOptions, TenantConfig.enableRegisterUsers y el sistema
 * de códigos temporales.
 */
@ApplicationScoped
public class RegisterUserAdapter implements RegisterUserGateway {

  @Override
  public boolean allowRegister(String tenant) {
    return false;
  }

  @Override
  public Optional<String> getRegisterConsent(String tenant) {
    return Optional.empty();
  }

  @Override
  public void requestForRegister(String urlBase, String tenant, String email, String password) {
    throw new UnsupportedOperationException(
        "Register user not yet implemented. TODO: integrate with access feature.");
  }

  @Override
  public Optional<String> verifyRegister(String tenant, String code) {
    return Optional.empty();
  }
}
