package net.civeira.phylax.testing.oauth.scenario;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.user.domain.RegistrationResult;
import net.civeira.phylax.features.oauth.user.domain.gateway.RegisterUserGateway;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioRegisterUserGateway implements RegisterUserGateway {

  private boolean allowRegister = true;
  private Supplier<RegistrationResult> requestBehavior = () -> RegistrationResult.pending();
  private BiFunction<String, String, Optional<String>> verifyBehavior =
      (tenant, code) -> OidcTestFixtures.REGISTER_CODE.equals(code)
          ? Optional.of(OidcTestFixtures.USERNAME)
          : Optional.empty();

  private final AtomicInteger requestCalls = new AtomicInteger();
  private final AtomicInteger verifyCalls = new AtomicInteger();
  private String lastTenant;
  private String lastEmail;
  private String lastPassword;
  private String lastUrlBase;
  private String lastCode;

  public void setAllowRegister(boolean allowRegister) {
    this.allowRegister = allowRegister;
  }

  public void whenRequestRegister(Supplier<RegistrationResult> behavior) {
    this.requestBehavior = behavior;
  }

  public void whenVerifyRegister(BiFunction<String, String, Optional<String>> behavior) {
    this.verifyBehavior = behavior;
  }

  public int getRequestCalls() {
    return requestCalls.get();
  }

  public int getVerifyCalls() {
    return verifyCalls.get();
  }

  public String getLastTenant() {
    return lastTenant;
  }

  public String getLastEmail() {
    return lastEmail;
  }

  public String getLastPassword() {
    return lastPassword;
  }

  public String getLastUrlBase() {
    return lastUrlBase;
  }

  public String getLastCode() {
    return lastCode;
  }

  public void reset() {
    allowRegister = true;
    requestBehavior = () -> RegistrationResult.pending();
    verifyBehavior = (tenant, code) -> OidcTestFixtures.REGISTER_CODE.equals(code)
        ? Optional.of(OidcTestFixtures.USERNAME)
        : Optional.empty();
    requestCalls.set(0);
    verifyCalls.set(0);
    lastTenant = null;
    lastEmail = null;
    lastPassword = null;
    lastUrlBase = null;
    lastCode = null;
  }

  @Override
  public boolean allowRegister(String tenant) {
    return allowRegister;
  }

  @Override
  public Optional<String> getRegisterConsent(String tenant) {
    return Optional.empty();
  }

  @Override
  public RegistrationResult requestForRegister(String urlBase, String tenant, String email,
      String password) {
    requestCalls.incrementAndGet();
    lastUrlBase = urlBase;
    lastTenant = tenant;
    lastEmail = email;
    lastPassword = password;
    return requestBehavior.get();
  }

  @Override
  public Optional<String> verifyRegister(String tenant, String code) {
    verifyCalls.incrementAndGet();
    lastTenant = tenant;
    lastCode = code;
    return verifyBehavior.apply(tenant, code);
  }
}
