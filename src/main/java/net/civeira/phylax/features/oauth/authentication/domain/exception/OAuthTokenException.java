package net.civeira.phylax.features.oauth.authentication.domain.exception;

import java.util.Map;
import java.util.Optional;

import lombok.Getter;

/**
 * Error response of the token endpoint (RFC 6749 §5.2).
 *
 * <p>
 * Carries the machine-readable {@code error}, a {@code error_description} and the HTTP status:
 * {@code invalid_client} is 401 (the driver adds {@code WWW-Authenticate}), the rest 400. The only
 * non-RFC error is {@code mfa_required} (403), whose body is the MFA token that lets the client
 * continue with the {@code mfa} grant.
 */
@Getter
public class OAuthTokenException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private final String error;
  private final int status;
  /**
   * Body that replaces the standard error object, only for {@code mfa_required}.
   */
  private final transient Object body;

  private OAuthTokenException(String error, String description, int status, Object body) {
    super(description);
    this.error = error;
    this.status = status;
    this.body = body;
  }

  /**
   * Error with an explicit status, for errors defined by grant extensions (RFC 8628 §3.5).
   *
   * @param error error code
   * @param description human-readable description
   * @param status HTTP status
   * @return the exception
   */
  public static OAuthTokenException of(String error, String description, int status) {
    return new OAuthTokenException(error, description, status, null);
  }

  public static OAuthTokenException invalidRequest(String description) {
    return of("invalid_request", description, 400);
  }

  public static OAuthTokenException invalidClient() {
    return of("invalid_client", "Client authentication failed", 401);
  }

  public static OAuthTokenException invalidGrant(String description) {
    return of("invalid_grant", description, 400);
  }

  public static OAuthTokenException unauthorizedClient(String description) {
    return of("unauthorized_client", description, 400);
  }

  public static OAuthTokenException unsupportedGrantType() {
    return of("unsupported_grant_type", "The grant type is not supported", 400);
  }

  public static OAuthTokenException invalidScope(String description) {
    return of("invalid_scope", description, 400);
  }

  /**
   * The resource owner must complete a second factor.
   *
   * @param mfaToken body with {@code error=mfa_required} and the {@code mfa_token}
   * @return the exception
   */
  public static OAuthTokenException mfaRequired(Object mfaToken) {
    return new OAuthTokenException("mfa_required", "Second factor required", 403, mfaToken);
  }

  /**
   * Body of the HTTP response.
   *
   * @return the replacement body or the standard {@code error}/{@code error_description} object
   */
  public Object entity() {
    return Optional.ofNullable(body)
        .orElseGet(() -> Map.of("error", error, "error_description", getMessage()));
  }
}
