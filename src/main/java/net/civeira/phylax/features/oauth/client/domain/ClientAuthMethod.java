package net.civeira.phylax.features.oauth.client.domain;

import java.util.Arrays;
import java.util.Optional;

/**
 * Client authentication methods at the token endpoint (RFC 7591 §2, token_endpoint_auth_method).
 *
 * <p>
 * Only the secret methods and {@code none} are verified today; {@code private_key_jwt} can be
 * registered but is rejected until client assertions are supported (task 3.2).
 */
public enum ClientAuthMethod {
  CLIENT_SECRET_BASIC("client_secret_basic"), CLIENT_SECRET_POST(
      "client_secret_post"), PRIVATE_KEY_JWT("private_key_jwt"), NONE("none");

  private final String value;

  ClientAuthMethod(String value) {
    this.value = value;
  }

  /**
   * Protocol name of the method.
   *
   * @return the RFC 7591 value
   */
  public String value() {
    return value;
  }

  /**
   * Whether the method authenticates with the client secret.
   *
   * @return true for client_secret_basic and client_secret_post
   */
  public boolean usesSecret() {
    return this == CLIENT_SECRET_BASIC || this == CLIENT_SECRET_POST;
  }

  /**
   * Resolves a method from its enum name or protocol value, ignoring case.
   *
   * @param name enum name (CLIENT_SECRET_POST) or protocol value (client_secret_post)
   * @return the method, empty when unknown
   */
  public static Optional<ClientAuthMethod> from(String name) {
    return name == null ? Optional.empty()
        : Arrays.stream(values())
            .filter(m -> m.name().equalsIgnoreCase(name) || m.value.equalsIgnoreCase(name))
            .findFirst();
  }
}
