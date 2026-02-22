package net.civeira.phylax.features.access.oauth.infrastructure.driver.impl.user;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.oauth.application.usecase.PasswordRecoverUsecase;
import net.civeira.phylax.features.access.oauth.application.usecase.UserLoginUsecase;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.user.domain.gateway.ChangePasswordGateway;

@Transactional
@ApplicationScoped
@RequiredArgsConstructor
public class ChangePasswordGatewayInteractor implements ChangePasswordGateway {
  private final PasswordRecoverUsecase recoverUsecase;
  private final UserLoginUsecase loginUsecase;

  @Override
  public boolean allowRecover(String tenant) {
    return true;
  }

  @Override
  public void requestForChange(String urlBase, String tenant, String username) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    recoverUsecase.recover(request, username, urlBase);
  }

  @Override
  public Optional<String> validateChangeRequest(String tenant, String code, String newPassword) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    boolean ok = recoverUsecase.checkRecoverCode(request, null, code, newPassword);
    return ok ? Optional.of(code) : Optional.empty();
  }

  @Override
  public boolean forceUpdatePassword(String tenant, String username, String oldPassword,
      String newPassword) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    return loginUsecase.updatePassword(request, username, oldPassword, newPassword);
  }
}
