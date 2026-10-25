package net.civeira.phylax.features.oauth.client.domain.exception;

/**
 * Rejected request object (RFC 9101 §6.3, task 3.3).
 */
public class RequestObjectException extends RuntimeException {
  private static final long serialVersionUID = 4021750939012473196L;

  private final String errorCode;

  private RequestObjectException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public static RequestObjectException invalid(String reason) {
    return new RequestObjectException("invalid_request_object", reason);
  }

  /**
   * A {@code request_uri} that is not a pushed request: fetching request objects by reference is
   * not supported.
   */
  public static RequestObjectException requestUriNotSupported() {
    return new RequestObjectException("request_uri_not_supported",
        "Only request_uri values of pushed authorization requests are supported");
  }
}
