/* @autogenerated */
package net.civeira.phylax.features.access.oauth.infrastructure.driver.impl.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.oauth.application.usecase.PasswordRecoverUsecase;
import net.civeira.phylax.features.oauth.authentication.application.interaction.command.RecoverPasswordCommand;
import net.civeira.phylax.features.oauth.authentication.application.spi.RecoverPasswordSpi;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;

@Transactional
@ApplicationScoped
@RequiredArgsConstructor
public class RecoverPasswordInteractor implements RecoverPasswordSpi {
  private final PasswordRecoverUsecase usecase;

  @Override
  public boolean allowRecover(AuthRequest request) {
    return true;
  }

  @Override
  public boolean checkRecoverCode(AuthRequest request, String email,
      RecoverPasswordCommand chrequest) {
    return usecase.checkRecoverCode(request, email, chrequest.getCode(), chrequest.getPassword());
  }

  @Override
  public void recover(AuthRequest request, String email, String urlWithParams) {
    usecase.recover(request, email, urlWithParams);
  }
}
