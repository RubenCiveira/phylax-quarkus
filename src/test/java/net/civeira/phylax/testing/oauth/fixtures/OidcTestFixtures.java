package net.civeira.phylax.testing.oauth.fixtures;

public final class OidcTestFixtures {
  public static final String TENANT = "test-tenant";
  public static final String CLIENT_ID = "test-client";
  public static final String CLIENT_SECRET = "test-secret";
  public static final String REDIRECT_URI = "http://localhost/callback";
  public static final String USERNAME = "alice@example.com";
  public static final String PASSWORD = "correct-password";
  public static final String WRONG_PASSWORD = "wrong-password";
  public static final String SCOPE = "openid profile email";
  public static final String MFA_CODE = "123456";
  public static final String RECOVER_CODE = "valid-recover-code";
  public static final String REGISTER_CODE = "valid-register-code";
  public static final String REGISTER_EMAIL = "newuser@example.com";
  public static final String STATE = "test-state";
  public static final String CODE_CHALLENGE = "test-verifier";
  public static final String CSID = "test-csid";

  private OidcTestFixtures() {}
}
