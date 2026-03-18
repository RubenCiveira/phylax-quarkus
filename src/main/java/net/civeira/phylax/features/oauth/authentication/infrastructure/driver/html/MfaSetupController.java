package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.domain.ChallengesState;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step.NewMfaStep;

/**
 * Serves the MFA enrollment QR image at {@code /mfa-setup}.
 *
 * <p>
 * This endpoint is called by the browser while the {@code NewMfa} form is rendered, to load the QR
 * code image inline.
 */
@Path("")
@RequestScoped
@RequiredArgsConstructor
public class MfaSetupController {

  private static final String TENANT = "tenant";

  private final NewMfaStep newMfaStep;
  private final OidcCookieManager cookieManager;

  @GET
  @Path("oauth/openid/{tenant}/mfa-setup")
  public Response showMfaImage(final @PathParam(TENANT) String tenant, final @Context UriInfo req,
      @Context HttpHeaders headers, @CookieParam(OidcCookieManager.PRE_SESSION_ID) String cookie) {
    return cookieManager.readPreSession(cookie, tenant).map(ChallengesState::getUsername)
        .flatMap(user -> newMfaStep.mfaImage(tenant, user))
        .orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }
}
