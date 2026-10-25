package net.civeira.phylax.features.oauth.session.domain;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Public identifier of an SSO session, the OIDC {@code sid} (tasks 1.2 and 1.14).
 *
 * <p>
 * Random and unrelated to the session cookie: knowing it does not let anyone use the session. It is
 * created with the session and kept, inside the session data, while the cookie value rotates.
 */
public final class PublicSessionId {

  private static final SecureRandom RANDOM = new SecureRandom();

  private PublicSessionId() {}

  /**
   * A new public session id.
   *
   * @return 32 random bytes in base64url
   */
  public static String random() {
    byte[] bytes = new byte[32];
    RANDOM.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
