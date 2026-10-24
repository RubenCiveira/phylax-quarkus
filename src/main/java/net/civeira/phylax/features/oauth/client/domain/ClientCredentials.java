package net.civeira.phylax.features.oauth.client.domain;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Client credentials presented to the token, device, PAR, introspection or revocation endpoints,
 * before they are verified by {@code ClientAuthenticator}.
 *
 * <p>
 * They come from HTTP Basic (client_secret_basic), from the {@code client_secret} body parameter
 * (client_secret_post), from a JWT client assertion (private_key_jwt) or from the
 * {@code client_id} alone (none). A request uses exactly one method (RFC 6749 §2.3):
 * {@link #method()} is empty when it mixes them, has a malformed Basic header or names two
 * different clients.
 *
 * @param bodyClientId the {@code client_id} body parameter
 * @param basicId client id of the Basic header
 * @param basicSecret secret of the Basic header
 * @param bodySecret the {@code client_secret} body parameter
 * @param assertion whether the body carries a client assertion
 * @param malformed the Authorization header uses Basic but cannot be decoded
 */
public record ClientCredentials(String bodyClientId, String basicId, String basicSecret,
    String bodySecret, boolean assertion, boolean malformed) {

  /**
   * Reads the credentials from the Authorization header and the form body. Other Authorization
   * schemes are not client credentials and are ignored.
   *
   * @param authorization Authorization header, may be null
   * @param body form parameters
   * @return the presented credentials
   */
  public static ClientCredentials from(String authorization, Map<String, List<String>> body) {
    String basicId = null;
    String basicSecret = null;
    boolean malformed = false;
    if (authorization != null && authorization.regionMatches(true, 0, "Basic ", 0, 6)) {
      try {
        String decoded = new String(Base64.getDecoder().decode(authorization.substring(6).trim()),
            StandardCharsets.UTF_8);
        int colon = decoded.indexOf(':');
        if (colon < 0) {
          malformed = true;
        } else {
          // Taken verbatim: RFC 6749 §2.3.1 form-encodes both parts, but existing clients send
          // them raw and decoding would alter secrets with '+' or '%'.
          basicId = decoded.substring(0, colon);
          basicSecret = decoded.substring(colon + 1);
        }
      } catch (IllegalArgumentException ex) {
        malformed = true;
      }
    }
    return new ClientCredentials(param(body, "client_id"), basicId, basicSecret,
        param(body, "client_secret"),
        param(body, "client_assertion") != null || param(body, "client_assertion_type") != null,
        malformed);
  }

  /**
   * The method these credentials use.
   *
   * @return the method, empty when malformed, mixed or without client
   */
  public Optional<ClientAuthMethod> method() {
    List<ClientAuthMethod> methods = new ArrayList<>();
    if (basicId != null) {
      methods.add(ClientAuthMethod.CLIENT_SECRET_BASIC);
    }
    if (bodySecret != null) {
      methods.add(ClientAuthMethod.CLIENT_SECRET_POST);
    }
    if (assertion) {
      methods.add(ClientAuthMethod.PRIVATE_KEY_JWT);
    }
    if (malformed || methods.size() > 1 || clientId().isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(methods.isEmpty() ? ClientAuthMethod.NONE : methods.get(0));
  }

  /**
   * The client the credentials name: the Basic user or the {@code client_id} parameter.
   *
   * @return the client id, empty when there is none or they differ
   */
  public Optional<String> clientId() {
    if (basicId != null && bodyClientId != null && !basicId.equals(bodyClientId)) {
      return Optional.empty();
    }
    return Optional.ofNullable(basicId != null ? basicId : bodyClientId).filter(s -> !s.isBlank());
  }

  /**
   * The secret of client_secret_basic or client_secret_post.
   *
   * @return the secret, empty string when there is none
   */
  public String secret() {
    return basicSecret != null ? basicSecret : bodySecret != null ? bodySecret : "";
  }

  @Override
  public String toString() {
    return "ClientCredentials[" + clientId().orElse("?") + ", " + method().map(Enum::name).orElse("-")
        + "]";
  }

  private static String param(Map<String, List<String>> body, String name) {
    List<String> values = body == null ? null : body.get(name);
    return values == null || values.isEmpty() || values.get(0) == null || values.get(0).isEmpty()
        ? null
        : values.get(0);
  }
}
