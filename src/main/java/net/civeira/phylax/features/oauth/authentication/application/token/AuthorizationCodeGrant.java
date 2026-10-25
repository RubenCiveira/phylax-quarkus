package net.civeira.phylax.features.oauth.authentication.application.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationMode;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.features.oauth.authentication.domain.exception.OAuthTokenException;
import net.civeira.phylax.features.oauth.authentication.domain.valueobject.PkceChallenge;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;
import net.civeira.phylax.features.oauth.session.domain.TemporalAuthCode;
import net.civeira.phylax.features.oauth.session.domain.gateway.TemporalKeysGateway;
import net.civeira.phylax.features.oauth.tokensecurity.application.JwtTokenBuilder;
import net.civeira.phylax.features.oauth.tokensecurity.domain.AutorizationToken;
import net.civeira.phylax.features.oauth.user.application.usecase.LoginUsecase;

/**
 * The authorization_code grant (tasks 0.1 and 1.8).
 *
 * <p>
 * The client has already authenticated with its registered method. The code must have been issued
 * to that client, in the same tenant and for the same {@code redirect_uri}, with a valid PKCE
 * verifier; without a challenge, only a confidential client that authenticated with its secret may
 * redeem it, and never with a verifier (PKCE downgrade, RFC 9700 §2.1.1). A request that fails
 * these checks does not spend the code; the consumption is atomic, so of two valid concurrent
 * requests only one gets tokens.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class AuthorizationCodeGrant {

  public static final String GRANT_TYPE = "authorization_code";

  private final TemporalKeysGateway temporalStore;
  private final LoginUsecase loginUsecase;
  private final JwtTokenBuilder tokenBuilder;
  private final TokenIssuer issuer;

  /**
   * Redeems the code of a token request.
   *
   * @param request token request
   * @param client authenticated client
   * @return issued tokens
   * @throws OAuthTokenException invalid_request without code, invalid_grant when it cannot be
   *         redeemed
   */
  public AutorizationToken exchange(TokenGrantRequest request, ClientDetails client) {
    String tenant = request.tenant();
    String codeValue = request.param("code");
    if (codeValue == null) {
      throw OAuthTokenException.invalidRequest("code is required");
    }
    String redirect = request.param("redirect_uri");
    String verifier = request.param("code_verifier");
    TemporalAuthCode code = temporalStore
        .consumeTemporalAuthCode(codeValue,
            stored -> codeMatches(stored, tenant, client, redirect, verifier))
        .orElseThrow(() -> OAuthTokenException.invalidGrant("Invalid authorization code"));
    AuthenticationResult auth = loginUsecase.fillPreAuthenticated(code.request,
        code.data.getUsername(), client, List.of(), AuthenticationMode.CODE_EXCHANGE);
    if (!auth.isRight()) {
      throw OAuthTokenException.invalidGrant("The user can not be authenticated");
    }
    // The tokens describe the login that produced the code, not this exchange (task 1.13).
    auth.getData().setSid(code.data.getSid());
    auth.getData().setTime(code.data.getTime());
    auth.getData().setAmr(
        code.data.getAmr() == null ? new ArrayList<>() : new ArrayList<>(code.data.getAmr()));
    boolean withSession = code.sessionId != null && !code.sessionId.isBlank();
    return issuer.issue(tenant, client, GRANT_TYPE, auth.getData(), code.request,
        Optional.ofNullable(code.sessionId), Optional.empty(),
        tokenBuilder.newRefreshFamily(tenant, withSession));
  }

  /**
   * Checks that a stored code belongs to the redeeming request.
   *
   * @param code stored code
   * @param tenant tenant of the token request
   * @param client authenticated client
   * @param redirect {@code redirect_uri} of the token request
   * @param verifier {@code code_verifier} of the token request
   * @return true when the code may be redeemed
   */
  static boolean codeMatches(TemporalAuthCode code, String tenant, ClientDetails client,
      String redirect, String verifier) {
    if (code.client == null || !client.getClientId().equals(code.client.getClientId())) {
      return false;
    }
    if (code.request == null || !tenant.equals(code.request.getTenant())) {
      return false;
    }
    if (code.request.getRedirect().isPresent()
        && !code.request.getRedirect().get().equals(redirect)) {
      return false;
    }
    String challenge = code.request.getCodeChallenge().orElse(null);
    if (challenge == null) {
      // Without PKCE only an authenticated confidential client that does not require it may redeem
      // (task 1.11), and a verifier for a code without challenge is a downgrade attempt (RFC 9700
      // §2.1.1).
      return client.isProtectedWithSecret() && !client.isRequirePkce()
          && (verifier == null || verifier.isBlank());
    }
    return verifier != null && !verifier.isBlank()
        && "S256".equals(code.request.getCodeChallengeMethod().orElse(""))
        && PkceChallenge.fromRequest(challenge, "S256").verify(verifier);
  }
}
