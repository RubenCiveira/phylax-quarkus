package net.civeira.phylax.testing.oauth.scenario;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.mfa.domain.PublicLoginMfaBuildResponse;
import net.civeira.phylax.features.oauth.mfa.domain.gateway.UserMfaGateway;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioMfaGateway implements UserMfaGateway {
  private String validOtp = OidcTestFixtures.MFA_CODE;
  private String validNewOtp = OidcTestFixtures.MFA_CODE;
  private PublicLoginMfaBuildResponse config = defaultConfig();
  private final AtomicInteger verifyCount = new AtomicInteger();
  private String lastTenant;
  private String lastUsername;
  private String lastOtp;
  private String lastSeed;

  public void setValidOtp(String otp) {
    this.validOtp = otp;
  }

  public void setValidNewOtp(String otp) {
    this.validNewOtp = otp;
  }

  public void setConfig(PublicLoginMfaBuildResponse config) {
    this.config = config;
  }

  public int getVerifyCount() {
    return verifyCount.get();
  }

  public String getLastTenant() {
    return lastTenant;
  }

  public String getLastUsername() {
    return lastUsername;
  }

  public String getLastOtp() {
    return lastOtp;
  }

  public String getLastSeed() {
    return lastSeed;
  }

  public void reset() {
    validOtp = OidcTestFixtures.MFA_CODE;
    validNewOtp = OidcTestFixtures.MFA_CODE;
    config = defaultConfig();
    verifyCount.set(0);
    lastTenant = null;
    lastUsername = null;
    lastOtp = null;
    lastSeed = null;
  }

  @Override
  public PublicLoginMfaBuildResponse configurationForNewMfa(String tenant, String username,
      Locale locale) {
    return config;
  }

  @Override
  public boolean verifyOtp(String tenant, String username, String otp) {
    verifyCount.incrementAndGet();
    lastTenant = tenant;
    lastUsername = username;
    lastOtp = otp;
    return validOtp.equals(otp);
  }

  @Override
  public boolean verifyNewOtp(String tenant, String username, String otp) {
    verifyCount.incrementAndGet();
    lastTenant = tenant;
    lastUsername = username;
    lastOtp = otp;
    return validNewOtp.equals(otp);
  }

  @Override
  public void storeSeed(String tenant, String username, String seed) {
    lastTenant = tenant;
    lastUsername = username;
    lastSeed = seed;
  }

  private PublicLoginMfaBuildResponse defaultConfig() {
    return PublicLoginMfaBuildResponse.builder().seed("seed").message("Setup MFA")
        .image("data:image/png;base64,iVBORw0KGgo=").url("otpauth://totp/test").requiresImage(true)
        .build();
  }
}
