package net.civeira.phylax.features.oauth.authentication.application.token;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;
import net.civeira.phylax.features.oauth.session.domain.gateway.SessionStoreGateway;
import net.civeira.phylax.features.oauth.tokensecurity.application.JwtTokenBuilder;
import net.civeira.phylax.features.oauth.tokensecurity.application.RefreshFamily;
import net.civeira.phylax.features.oauth.tokensecurity.domain.AutorizationToken;

/**
 * Issues the access, refresh and ID tokens of a user grant and binds them to its session.
 *
 * <p>
 * The claims and signature come from {@link JwtTokenBuilder} (and its {@code ClaimsMapper}), the
 * single emission point per token type. This service only adds the session bookkeeping: the JTIs of
 * a code exchange are registered on the authorization session, and those of a rotation replace the
 * previous refresh JTI, so logout and session revocation reach every token of the grant.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class TokenIssuer {

  private final JwtTokenBuilder tokenBuilder;
  private final SessionStoreGateway sessionStore;

  /**
   * Issues the tokens of a grant.
   *
   * @param tenant tenant
   * @param client authenticated client
   * @param grantType grant type recorded in the token
   * @param auth authenticated user and granted scopes
   * @param request request context (scope, nonce, state, audiences)
   * @param sessionId authorization session of a code exchange
   * @param previousRefreshToken refresh token being rotated
   * @param family refresh family of the grant
   * @return the issued tokens
   */
  public AutorizationToken issue(String tenant, ClientDetails client, String grantType,
      AuthenticationData auth, AuthRequest request, Optional<String> sessionId,
      Optional<String> previousRefreshToken, RefreshFamily family) {
    AutorizationToken token =
        tokenBuilder.buildToken(tenant, client, grantType, auth, request, family);
    Optional<String> accessJti = tokenBuilder.tokenJti(token.getAccessToken(), tenant);
    Optional<String> refreshJti = tokenBuilder.tokenJti(token.getRefreshToken(), tenant);
    if (accessJti.isPresent() && refreshJti.isPresent()) {
      if (sessionId.isPresent() && !sessionId.get().isBlank()) {
        sessionStore.updateTokenJtis(sessionId.get(), accessJti.get(), refreshJti.get());
      } else if (previousRefreshToken.isPresent()) {
        tokenBuilder.tokenJti(previousRefreshToken.get(), tenant)
            .ifPresent(oldRefreshJti -> sessionStore.updateTokenJtisByRefreshJti(oldRefreshJti,
                accessJti.get(), refreshJti.get()));
      }
    }
    return token;
  }
}
