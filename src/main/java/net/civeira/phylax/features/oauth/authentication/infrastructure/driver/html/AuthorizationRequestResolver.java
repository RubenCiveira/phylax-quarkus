package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.client.application.RequestObjectVerifier;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;
import net.civeira.phylax.features.oauth.client.domain.exception.RequestObjectException;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientStoreGateway;
import net.civeira.phylax.features.oauth.par.application.usecase.resolveparrequest.ResolveParRequestUsecase;
import net.civeira.phylax.features.oauth.par.domain.exception.ParException;

/**
 * Where the parameters of an authorization request come from (tasks 0.14 and 3.3).
 *
 * <p>
 * Precedence, and only one source per request:
 * <ol>
 * <li>{@code request_uri} of a pushed request ({@code urn:ietf:params:oauth:request_uri:}): only
 * the stored parameters; the outer {@code client_id} must match and the rest of the query is
 * ignored. Single use, bound to the browser that consumed it (cookie {@code PAR_BINDING}).</li>
 * <li>{@code request} (a signed request object, JAR by value): only its claims, verified by
 * {@link RequestObjectVerifier}. It is kept as a consumed pushed request and the browser is sent to
 * it, so the login forms that follow do not present the request object again.</li>
 * <li>Any other {@code request_uri}: {@code request_uri_not_supported} (request objects by
 * reference are not fetched).</li>
 * <li>Otherwise, the query.</li>
 * </ol>
 */
@ApplicationScoped
@RequiredArgsConstructor
public class AuthorizationRequestResolver {

  private static final String PAR_URN = "urn:ietf:params:oauth:request_uri:";

  private final ResolveParRequestUsecase par;
  private final RequestObjectVerifier requestObjects;
  private final ClientStoreGateway clients;
  private final OidcCookieManager cookies;
  private final CurrentRequest current;

  /**
   * A resolved request: either its parameters, or a redirection to the pushed request that now
   * holds them.
   *
   * @param request parsed request, null when redirecting
   * @param cookie binding cookie to set, if any
   * @param redirect where to send the browser, if any
   */
  public record Resolution(AuthRequest request, Optional<NewCookie> cookie,
      Optional<URI> redirect) {}

  /**
   * Resolves the request of an authorization endpoint call.
   *
   * @param tenant tenant of the endpoint
   * @param req request URI
   * @param headers request headers
   * @param parBinding {@code PAR_BINDING} cookie, if any
   * @return the resolution
   * @throws ParException for an invalid pushed request
   * @throws RequestObjectException for an invalid request object
   */
  public Resolution resolve(String tenant, UriInfo req, HttpHeaders headers, String parBinding) {
    MultivaluedMap<String, String> query = req.getQueryParameters();
    String clientId = query.getFirst("client_id");
    String requestUri = query.getFirst("request_uri");
    String requestObject = query.getFirst("request");
    if (requestUri != null && !requestUri.isBlank()) {
      if (requestObject != null || !requestUri.startsWith(PAR_URN)) {
        throw RequestObjectException.requestUriNotSupported();
      }
      ResolveParRequestUsecase.Resolution resolved =
          par.resolve(requestUri, tenant, clientId, Optional.ofNullable(parBinding));
      return new Resolution(new AuthRequest(tenant, asQuery(resolved.params()), headers).asPushed(),
          resolved.binding().map(binding -> bindingCookie(binding, tenant)), Optional.empty());
    }
    if (requestObject != null && !requestObject.isBlank()) {
      ClientDetails client =
          Optional.ofNullable(clientId).flatMap(id -> clients.loadPreautorized(tenant, id))
              .orElseThrow(() -> RequestObjectException.invalid("Unknown client"));
      Map<String, String> params =
          requestObjects.verify(tenant, issuer(tenant), client, requestObject);
      ResolveParRequestUsecase.Adopted adopted = par.adopt(tenant, client.getClientId(), params);
      URI to = req.getAbsolutePathBuilder().replaceQuery(null)
          .queryParam("client_id", client.getClientId())
          .queryParam("request_uri", adopted.requestUri()).build();
      return new Resolution(null, Optional.of(bindingCookie(adopted.binding(), tenant)),
          Optional.of(to));
    }
    return new Resolution(new AuthRequest(tenant, req, headers), Optional.empty(),
        Optional.empty());
  }

  private NewCookie bindingCookie(String binding, String tenant) {
    return cookies.writeParBinding(binding, tenant, (int) par.flowTtl().toSeconds());
  }

  private String issuer(String tenant) {
    return current.getPublicHost() + "/oauth/openid/" + tenant;
  }

  private static MultivaluedMap<String, String> asQuery(Map<String, String> params) {
    MultivaluedHashMap<String, String> query = new MultivaluedHashMap<>();
    params.forEach(query::putSingle);
    return query;
  }
}
