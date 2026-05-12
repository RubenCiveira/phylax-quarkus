package net.civeira.phylax.features.oauth.profile.domain;

import java.util.Optional;

public record MfaSetup(String seed, String qrImage) {

  public Optional<String> getQrImage() {
    return Optional.ofNullable(qrImage);
  }
}
