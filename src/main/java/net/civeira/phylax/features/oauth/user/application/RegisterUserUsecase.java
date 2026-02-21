package net.civeira.phylax.features.oauth.user.application;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.user.domain.gateway.RegisterUserGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class RegisterUserUsecase {

  private final RegisterUserGateway gateway;

  public boolean allowRegister(String tenant) {
    return gateway.allowRegister(tenant);
  }

  public Optional<String> getRegisterConsent(String tenant) {
    return gateway.getRegisterConsent(tenant);
  }

  public void requestForRegister(String urlBase, String tenant, String email, String password) {
    gateway.requestForRegister(urlBase, tenant, email, password);
  }

  public Optional<String> verifyRegister(String tenant, String code) {
    return gateway.verifyRegister(tenant, code);
  }
}
