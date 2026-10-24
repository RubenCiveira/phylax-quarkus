package net.civeira.phylax.features.oauth.authentication.application.token;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.application.granter.TokenGranter;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.features.oauth.authentication.domain.exception.AuthenticationException;
import net.civeira.phylax.features.oauth.authentication.domain.exception.MfaRequiredException;
import net.civeira.phylax.features.oauth.authentication.domain.exception.OAuthTokenException;
import net.civeira.phylax.features.oauth.client.application.ClientAuthenticator;
import net.civeira.phylax.features.oauth.client.domain.ClientAuthPolicy;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;
import net.civeira.phylax.features.oauth.tokensecurity.application.JwtTokenBuilder;
import net.civeira.phylax.features.oauth.tokensecurity.domain.AutorizationToken;

/**
 * Use case of the token endpoint: authenticates the client and runs the grant (task 1.16).
 *
 * <p>
 * Order of checks, each with its RFC 6749 §5.2 error:
 * <ol>
 * <li>{@code grant_type} present ({@code invalid_request}) and known
 * ({@code unsupported_grant_type});</li>
 * <li>client authentication with the methods the grant accepts ({@code invalid_client}): the
 * registered one for authorization_code and refresh_token, also public for the device grant, a
 * secret for the rest;</li>
 * <li>authorization_code and refresh_token run their own use cases ({@link AuthorizationCodeGrant},
 * {@link RefreshTokenGrant});</li>
 * <li>the other grants must be allowed to the client ({@code unauthorized_client}) and are resolved
 * by their {@link TokenGranter}; a rejected resource owner is {@code invalid_grant}, except a
 * pending second factor ({@code mfa_required}).</li>
 * </ol>
 * Every failure is an {@link OAuthTokenException}; the driver only turns it into HTTP.
 */
@RequestScoped
@RequiredArgsConstructor
public class IssueTokenUsecase {

  private static final String CLIENT_CREDENTIALS = "client_credentials";
  private static final String DEVICE_CODE = "urn:ietf:params:oauth:grant-type:device_code";

  private final Instance<TokenGranter> granters;
  private final ClientAuthenticator clientAuthenticator;
  private final AuthorizationCodeGrant codeGrant;
  private final RefreshTokenGrant refreshGrant;
  private final TokenIssuer issuer;
  private final JwtTokenBuilder tokenBuilder;

  /**
   * Issues the tokens of a token request.
   *
   * @param request parsed token request
   * @return issued tokens
   * @throws OAuthTokenException when the request must be rejected
   */
  public AutorizationToken issue(TokenGrantRequest request) {
    String grant = request.grantType();
    if (grant == null || grant.isBlank()) {
      throw OAuthTokenException.invalidRequest("grant_type is required");
    }
    boolean code = AuthorizationCodeGrant.GRANT_TYPE.equals(grant);
    boolean refresh = RefreshTokenGrant.GRANT_TYPE.equals(grant);
    Optional<TokenGranter> granter = code || refresh ? Optional.empty() : granter(grant);
    if (!code && !refresh && granter.isEmpty()) {
      throw OAuthTokenException.unsupportedGrantType();
    }
    ClientAuthPolicy policy = code || refresh ? ClientAuthPolicy.REGISTERED_METHOD
        : DEVICE_CODE.equals(grant) ? ClientAuthPolicy.SECRET_OR_PUBLIC : ClientAuthPolicy.SECRET;
    ClientDetails client =
        clientAuthenticator.authenticate(request.tenant(), request.credentials(), policy)
            .orElseThrow(OAuthTokenException::invalidClient);
    if (code) {
      return codeGrant.exchange(request, client);
    }
    if (refresh) {
      return refreshGrant.rotate(request, client);
    }
    if (!client.allowedGrant(grant)) {
      throw OAuthTokenException.unauthorizedClient("The client is not allowed to use " + grant);
    }
    AuthenticationResult result =
        granter.get().authenticate(request.request(), client, request.params());
    if (!result.isRight()) {
      throw rejected(result.getFail());
    }
    AuthenticationData data = result.getData();
    if (data.getScopes().isEmpty()) {
      // The scope of a POST token request comes in the body: grant the allowed ones.
      List<String> requested = TokenParams.scopes(request.param("scope"));
      boolean wildcard = client.getAllowedScopes().contains("*");
      data.setScopes(wildcard ? requested
          : requested.stream().filter(client.getAllowedScopes()::contains).toList());
    }
    if (CLIENT_CREDENTIALS.equals(grant)) {
      return tokenBuilder.buildClientCredentialsToken(request.tenant(), client, data.getScopes());
    }
    return issuer.issue(request.tenant(), client, grant, data, request.request(), Optional.empty(),
        Optional.empty(), tokenBuilder.newRefreshFamily(request.tenant(), false));
  }

  private Optional<TokenGranter> granter(String grant) {
    return StreamSupport.stream(granters.spliterator(), false).filter(g -> g.canHandle(grant))
        .findFirst();
  }

  private OAuthTokenException rejected(AuthenticationException fail) {
    if (fail instanceof MfaRequiredException) {
      return OAuthTokenException.mfaRequired(fail.entity(tokenBuilder));
    }
    String reason = fail.getMessage();
    return OAuthTokenException
        .invalidGrant(reason == null || reason.isBlank() ? "Invalid grant" : reason);
  }
}
