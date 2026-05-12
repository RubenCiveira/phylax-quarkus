package net.civeira.phylax.features.oauth.magiclink.domain.gateway;

/**
 * Generates an OAuth authorization code after a magic link is verified.
 *
 * <p>
 * The code is stored with the user's identity and OAuth parameters so the token endpoint can
 * exchange it for tokens without re-authenticating.
 * </p>
 */
public interface MagicLinkCodeGateway {

  String createAuthCode(String userUid, String clientId, String scope, String redirectUri,
      String tenant, String nonce);
}
