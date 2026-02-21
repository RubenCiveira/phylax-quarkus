package net.civeira.phylax.testing.oauth.scenario;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.user.domain.gateway.ChangePasswordGateway;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioChangePasswordGateway implements ChangePasswordGateway {
  private boolean allowRecover = true;
  private boolean forceUpdatePasswordResult = true;
  private BiFunction<String, String, Optional<String>> validateBehavior =
      (code, pass) -> OidcTestFixtures.RECOVER_CODE.equals(code)
          ? Optional.of(OidcTestFixtures.USERNAME)
          : Optional.empty();

  private final AtomicInteger forceUpdateCalls = new AtomicInteger();
  private String lastTenant;
  private String lastUsername;
  private String lastOldPassword;
  private String lastNewPassword;
  private String lastRecoverUrl;
  private String lastRecoverCode;

  public void setAllowRecover(boolean allowRecover) {
    this.allowRecover = allowRecover;
  }

  public void setForceUpdatePasswordResult(boolean result) {
    this.forceUpdatePasswordResult = result;
  }

  public void whenValidateChange(BiFunction<String, String, Optional<String>> behavior) {
    this.validateBehavior = behavior;
  }

  public int getForceUpdateCalls() {
    return forceUpdateCalls.get();
  }

  public String getLastTenant() {
    return lastTenant;
  }

  public String getLastUsername() {
    return lastUsername;
  }

  public String getLastOldPassword() {
    return lastOldPassword;
  }

  public String getLastNewPassword() {
    return lastNewPassword;
  }

  public String getLastRecoverUrl() {
    return lastRecoverUrl;
  }

  public String getLastRecoverCode() {
    return lastRecoverCode;
  }

  public void reset() {
    allowRecover = true;
    forceUpdatePasswordResult = true;
    validateBehavior = (code, pass) -> OidcTestFixtures.RECOVER_CODE.equals(code)
        ? Optional.of(OidcTestFixtures.USERNAME)
        : Optional.empty();
    forceUpdateCalls.set(0);
    lastTenant = null;
    lastUsername = null;
    lastOldPassword = null;
    lastNewPassword = null;
    lastRecoverUrl = null;
    lastRecoverCode = null;
  }

  @Override
  public boolean allowRecover(String tenant) {
    return allowRecover;
  }

  @Override
  public void requestForChange(String urlBase, String tenant, String username) {
    lastRecoverUrl = urlBase;
    lastTenant = tenant;
    lastUsername = username;
  }

  @Override
  public Optional<String> validateChangeRequest(String tenant, String code, String newPassword) {
    lastTenant = tenant;
    lastRecoverCode = code;
    lastNewPassword = newPassword;
    return validateBehavior.apply(code, newPassword);
  }

  @Override
  public boolean forceUpdatePassword(String tenant, String username, String oldPassword,
      String newPassword) {
    forceUpdateCalls.incrementAndGet();
    lastTenant = tenant;
    lastUsername = username;
    lastOldPassword = oldPassword;
    lastNewPassword = newPassword;
    return forceUpdatePasswordResult;
  }
}
