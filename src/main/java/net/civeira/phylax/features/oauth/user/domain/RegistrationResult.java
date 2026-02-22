package net.civeira.phylax.features.oauth.user.domain;

import lombok.Getter;

@Getter
public class RegistrationResult {

  public enum Status {
    OK, PENDING, CANCEL
  }

  private final Status status;
  private final String username;

  public static RegistrationResult ok(String username) {
    return new RegistrationResult(Status.OK, username);
  }

  public static RegistrationResult pending() {
    return new RegistrationResult(Status.PENDING, null);
  }

  public static RegistrationResult cancel() {
    return new RegistrationResult(Status.CANCEL, null);
  }

  private RegistrationResult(Status status, String username) {
    this.status = status;
    this.username = username;
  }
}
