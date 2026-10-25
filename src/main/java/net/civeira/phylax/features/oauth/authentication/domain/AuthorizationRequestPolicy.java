package net.civeira.phylax.features.oauth.authentication.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import net.civeira.phylax.features.oauth.client.domain.ClientDetails;

/**
 * What a client may ask for in an authorization request, checked before any login and again when
 * the request is pushed (tasks 0.4, 0.3, 1.11 and 2.3).
 *
 * <ul>
 * <li>{@code response_type} is required ({@code invalid_request}) and registered for the client
 * ({@code unauthorized_client});</li>
 * <li>every audience is the client or one registered for it ({@code invalid_target});</li>
 * <li>PKCE, for a response with a code: only {@code S256}, so a challenge without method (which
 * means {@code plain}) is rejected; a public client, or one that requires PKCE, must send it
 * ({@code invalid_request});</li>
 * <li>a client that requires PAR only accepts pushed requests ({@code invalid_request}).</li>
 * </ul>
 */
public final class AuthorizationRequestPolicy {

  /**
   * RFC 7636 §4.2: base64url of a SHA-256, 43 characters; up to 128 for other lengths.
   */
  private static final Pattern CHALLENGE = Pattern.compile("[A-Za-z0-9._~-]{43,128}");

  /**
   * A rejected request.
   *
   * @param error OAuth error code
   * @param description human readable reason
   */
  public record Violation(String error, String description) {}

  private AuthorizationRequestPolicy() {}

  /**
   * Checks a request against the client policy.
   *
   * @param request authorization request
   * @param client client that makes it
   * @return the first violation, empty when the request may go on
   */
  public static Optional<Violation> violation(AuthRequest request, ClientDetails client) {
    Optional<String> responseType = request.getResponseType().filter(t -> !t.isBlank());
    if (responseType.isEmpty()) {
      return reject("invalid_request", "response_type is required");
    }
    if (!client.allowsResponseType(responseType.get())) {
      return reject("unauthorized_client", "The client may not use this response_type");
    }
    if (!client.allowsAudiences(request.getAudiences())) {
      return reject("invalid_target", "The audience is not registered for the client");
    }
    if (client.isRequirePar() && !request.isPushed()) {
      return reject("invalid_request", "The client must push its authorization requests");
    }
    boolean code = Arrays.asList(responseType.get().trim().split("\\s+")).contains("code");
    return code ? pkceViolation(request, client) : Optional.empty();
  }

  private static Optional<Violation> pkceViolation(AuthRequest request, ClientDetails client) {
    Optional<String> challenge = request.getCodeChallenge().filter(c -> !c.isBlank());
    if (challenge.isEmpty()) {
      return client.isRequirePkce()
          ? reject("invalid_request", "code_challenge is required for this client")
          : Optional.empty();
    }
    if (!"S256".equals(request.getCodeChallengeMethod().orElse(null))) {
      return reject("invalid_request", "code_challenge_method must be S256");
    }
    if (!CHALLENGE.matcher(challenge.get()).matches()) {
      return reject("invalid_request", "Malformed code_challenge");
    }
    return Optional.empty();
  }

  private static Optional<Violation> reject(String error, String description) {
    return Optional.of(new Violation(error, description));
  }
}
