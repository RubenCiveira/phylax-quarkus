package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html;

import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.ChallengesState;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step.RegistrationStep;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientStoreGateway;

/**
 * Handles the standalone user-registration endpoints ({@code /register}).
 *
 * <p>
 * These endpoints are reached via email verification links and operate outside the main
 * {@code /auth} POST flow. Outcomes (registration verified) are routed back into the auth flow via
 * {@link StepOutcomeHandler}.
 */
@Path("")
@RequestScoped
@RequiredArgsConstructor
public class RegisterController {

  private static final String TENANT = "tenant";

  private final RegistrationStep registrationStep;
  private final StepOutcomeHandler outcomeHandler;
  private final OidcCookieManager cookieManager;
  private final ClientStoreGateway clientRetrieve;

  @GET
  @Path("oauth/openid/{tenant}/register")
  public Response showRegister(final @PathParam(TENANT) String tenant, final @Context UriInfo req,
      @Context HttpHeaders headers, @QueryParam("email") String email,
      @QueryParam("regcode") String regcode) {
    AuthRequest request = new AuthRequest(tenant, req, headers);
    return loadClient(request)
        .map(_ -> registrationStep.doPaintVerifyForm(request.getLocale(), email, regcode, null))
        .orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @POST
  @Path("oauth/openid/{tenant}/register")
  public Response checkRegister(final @PathParam(TENANT) String tenant,
      @QueryParam("email") String email, @Context UriInfo req,
      final MultivaluedMap<String, String> paramMap, @Context HttpHeaders headers,
      @CookieParam(OidcCookieManager.PRE_SESSION_ID) String cookie) {
    AuthRequest request = new AuthRequest(tenant, req, headers);
    return loadClient(request).map(clientDetails -> {
      Optional<ChallengesState> challengeState = cookieManager.readPreSession(cookie, tenant);
      StepInput input = StepInput.builder().request(request).clientDetails(clientDetails)
          .challenges(challengeState).formParams(paramMap).build();
      return registrationStep.doExecVerify(input, email)
          .map(outcome -> outcomeHandler.handle(outcome, input, paramMap))
          .orElseGet(() -> Response.status(400).build());
    }).orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  private Optional<ClientDetails> loadClient(AuthRequest request) {
    return clientRetrieve.loadPublic(request.getTenant(), request.getClientId().orElseThrow(),
        request.getRedirect().orElseThrow());
  }
}
