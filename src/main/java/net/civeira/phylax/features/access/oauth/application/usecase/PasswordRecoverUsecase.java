/* @autogenerated */
package net.civeira.phylax.features.access.oauth.application.usecase;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.oauth.application.service.ActiveUserFindService;
import net.civeira.phylax.features.access.oauth.application.service.SecureCodeGenerator;
import net.civeira.phylax.features.access.user.domain.gateway.UserWriteRepositoryGateway;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.UserAccessTemporalCode;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.UserAccessTemporalCodeChangeSet;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.gateway.UserAccessTemporalCodeFilter;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.gateway.UserAccessTemporalCodeWriteRepositoryGateway;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;

@ApplicationScoped
@RequiredArgsConstructor
public class PasswordRecoverUsecase {
  private final static TemporalAmount RECOVER_TIME = Duration.ofHours(12);

  private final ActiveUserFindService activeUser;
  private final UserWriteRepositoryGateway users;
  private final UserAccessTemporalCodeWriteRepositoryGateway codes;

  public boolean checkRecoverCode(AuthRequest request, String email, String code,
      String newPassoword) {
    return activeUser
        .findEnabledUserByNameOrEmail(request.getTenant(), email, request.getAudiences())
        .map(user -> {
          if (user.getProvider().isPresent()) {
            return false;
          }
          Optional<UserAccessTemporalCode> find =
              codes.findForUpdate(UserAccessTemporalCodeFilter.builder().user(user).build());
          if (!find.isPresent()) {
            return false;
          }
          UserAccessTemporalCode tempCode = find.get();
          if (!"".equals(code) && code.equals(tempCode.getRecoveryCode().orElse(""))) {
            codes.update(tempCode, tempCode.resetPasswordRecover());
            // codes.update(tempCode, tempCode.withNullRecoveryCode());
            users.update(user, user.changePassword(newPassoword));
            // users.update(user, user.withPlainPassword(newPassoword));
            return true;
          } else {
            return false;
          }
        }).orElse(false);
  }

  public void recover(AuthRequest request, String email, String urlWithParams) {
    activeUser.findEnabledUserByNameOrEmail(request.getTenant(), email, request.getAudiences())
        .ifPresent(user -> {
          if (user.getProvider().isPresent()) {
            return;
          }
          String code = SecureCodeGenerator.generate();
          Optional<UserAccessTemporalCode> find =
              codes.findForUpdate(UserAccessTemporalCodeFilter.builder().user(user).build());
          UserAccessTemporalCode userCode;
          if (find.isPresent()) {
            userCode = find.get();
          } else {
            userCode = codes.create(UserAccessTemporalCode
                .create(UserAccessTemporalCodeChangeSet.builder().newUid().build()));
          }
          codes.update(userCode,
              userCode.generatePasswordRecover(code, OffsetDateTime.now().plus(RECOVER_TIME)));
        });
  }
}
