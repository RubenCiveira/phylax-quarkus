package net.civeira.phylax.features.oauth.user.domain;

import lombok.Getter;

@Getter
public class PendingConsent {

  private final String relyingParty;
  private final String consentText;

  public static PendingConsent of(String relyingParty, String consentText) {
    return new PendingConsent(relyingParty, consentText);
  }

  private PendingConsent(String relyingParty, String consentText) {
    this.relyingParty = relyingParty;
    this.consentText = consentText;
  }
}
