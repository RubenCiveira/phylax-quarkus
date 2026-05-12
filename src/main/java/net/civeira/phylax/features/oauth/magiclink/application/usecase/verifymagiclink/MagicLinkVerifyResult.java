package net.civeira.phylax.features.oauth.magiclink.application.usecase.verifymagiclink;

import java.util.Optional;

import net.civeira.phylax.features.oauth.magiclink.domain.MagicLinkSession;

public final class MagicLinkVerifyResult {

  private final String redirectUrl;
  private final MagicLinkSession session;

  private MagicLinkVerifyResult(String redirectUrl, MagicLinkSession session) {
    this.redirectUrl = redirectUrl;
    this.session = session;
  }

  public static MagicLinkVerifyResult of(String redirectUrl, MagicLinkSession session) {
    return new MagicLinkVerifyResult(redirectUrl, session);
  }

  public static MagicLinkVerifyResult of(String redirectUrl) {
    return new MagicLinkVerifyResult(redirectUrl, null);
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public Optional<MagicLinkSession> getSession() {
    return Optional.ofNullable(session);
  }
}
