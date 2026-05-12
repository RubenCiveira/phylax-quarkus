package net.civeira.phylax.features.oauth.webauthn.domain.exception;

public final class WebAuthnException extends RuntimeException {
  private static final long serialVersionUID = 2603036298407972738L;

  private WebAuthnException(String message) {
    super(message);
  }

  public static WebAuthnException challengeExpired() {
    return new WebAuthnException("WebAuthn challenge has expired");
  }

  public static WebAuthnException challengeNotFound() {
    return new WebAuthnException("WebAuthn challenge not found");
  }

  public static WebAuthnException credentialNotFound() {
    return new WebAuthnException("WebAuthn credential not found");
  }

  public static WebAuthnException credentialDisabled() {
    return new WebAuthnException("WebAuthn credential is disabled");
  }

  public static WebAuthnException replayAttack() {
    return new WebAuthnException("WebAuthn replay attack detected: sign count too low");
  }

  public static WebAuthnException verificationFailed(String reason) {
    return new WebAuthnException("WebAuthn verification failed: " + reason);
  }
}
