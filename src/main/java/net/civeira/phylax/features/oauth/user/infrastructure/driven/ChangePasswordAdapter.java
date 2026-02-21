package net.civeira.phylax.features.oauth.user.infrastructure.driven;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.application.interaction.command.RecoverPasswordCommand;
import net.civeira.phylax.features.oauth.authentication.application.spi.RecoverPasswordSpi;
import net.civeira.phylax.features.oauth.authentication.application.spi.UserPasswordChangeSpi;
import net.civeira.phylax.features.oauth.authentication.application.spi.UserPasswordChangeSpi.ChPassRequest;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.user.domain.gateway.ChangePasswordGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class ChangePasswordAdapter implements ChangePasswordGateway {

  private final RecoverPasswordSpi recoverSpi;
  private final UserPasswordChangeSpi passwordChangeSpi;

  @Override
  public boolean allowRecover(String tenant) {
    return recoverSpi.allowRecover(AuthRequest.builder().tenant(tenant).build());
  }

  @Override
  public void requestForChange(String urlBase, String tenant, String username) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    recoverSpi.recover(request, username, urlBase);
  }

  @Override
  public Optional<String> validateChangeRequest(String tenant, String code, String newPassword) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    boolean ok = recoverSpi.checkRecoverCode(request, null,
        RecoverPasswordCommand.builder().code(code).password(newPassword).build());
    return ok ? Optional.of(code) : Optional.empty();
  }

  @Override
  public boolean forceUpdatePassword(String tenant, String username, String oldPassword,
      String newPassword) {
    AuthRequest request = AuthRequest.builder().tenant(tenant).build();
    return passwordChangeSpi.updatePassword(request, username,
        ChPassRequest.builder().oldPassword(oldPassword).newPassword(newPassword).build());
  }
}
