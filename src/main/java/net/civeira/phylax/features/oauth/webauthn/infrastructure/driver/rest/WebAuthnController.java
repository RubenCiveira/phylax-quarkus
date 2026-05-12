package net.civeira.phylax.features.oauth.webauthn.infrastructure.driver.rest;

import java.net.URI;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantFilter;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantReadRepositoryGateway;
import net.civeira.phylax.features.access.tenantconfig.domain.gateway.TenantConfigFilter;
import net.civeira.phylax.features.access.tenantconfig.domain.gateway.TenantConfigReadRepositoryGateway;
import net.civeira.phylax.features.oauth.webauthn.application.usecase.beginauthentication.BeginAuthenticationUsecase;
import net.civeira.phylax.features.oauth.webauthn.application.usecase.beginregistration.BeginRegistrationUsecase;
import net.civeira.phylax.features.oauth.webauthn.application.usecase.finishauthentication.FinishAuthenticationUsecase;
import net.civeira.phylax.features.oauth.webauthn.application.usecase.finishregistration.FinishRegistrationUsecase;

@Path("")
@RequestScoped
@RequiredArgsConstructor
public class WebAuthnController {

  private final TenantReadRepositoryGateway tenants;
  private final TenantConfigReadRepositoryGateway tenantConfigs;
  private final BeginRegistrationUsecase beginRegistration;
  private final FinishRegistrationUsecase finishRegistration;
  private final BeginAuthenticationUsecase beginAuthentication;
  private final FinishAuthenticationUsecase finishAuthentication;

  @ConfigProperty(name = "oauth.base-url")
  String baseUrl;

  @POST
  @Path("oauth/openid/{tenant}/webauthn/register/begin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response registerBegin(final @PathParam("tenant") String tenant,
      final Map<String, Object> body) {
    String userUid = str(body.get("userUid"));
    String[] rp = resolveRp(tenant);
    return Response.ok(beginRegistration.begin(userUid, tenant, rp[0], rp[1])).build();
  }

  @POST
  @Path("oauth/openid/{tenant}/webauthn/register/finish")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response registerFinish(final @PathParam("tenant") String tenant,
      final @Context HttpHeaders headers, final Map<String, Object> body) {
    String challengeId = str(body.get("challengeId"));
    @SuppressWarnings("unchecked")
    Map<String, Object> credential = body.get("credential") instanceof Map<?, ?>
        ? (Map<String, Object>) body.get("credential")
        : Map.of();
    String deviceName = str(body.get("deviceName"));

    String[] rp = resolveRp(tenant);
    finishRegistration.finish(challengeId, tenant, credential, resolveOrigin(headers), rp[0],
        deviceName);
    return Response.ok(Map.of("status", "ok")).build();
  }

  @POST
  @Path("oauth/openid/{tenant}/webauthn/authenticate/begin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response authenticateBegin(final @PathParam("tenant") String tenant) {
    String[] rp = resolveRp(tenant);
    return Response.ok(beginAuthentication.begin(tenant, rp[0])).build();
  }

  @POST
  @Path("oauth/openid/{tenant}/webauthn/authenticate/finish")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response authenticateFinish(final @PathParam("tenant") String tenant,
      final @Context HttpHeaders headers, final Map<String, Object> body) {
    String challengeId = str(body.get("challengeId"));
    @SuppressWarnings("unchecked")
    Map<String, Object> credential = body.get("credential") instanceof Map<?, ?>
        ? (Map<String, Object>) body.get("credential")
        : Map.of();

    String[] rp = resolveRp(tenant);
    finishAuthentication.finish(challengeId, tenant, credential, resolveOrigin(headers), rp[0]);
    return Response.ok(Map.of("status", "ok", "challengeId", challengeId)).build();
  }

  private String[] resolveRp(String tenantName) {
    var tenant = tenants.find(TenantFilter.builder().name(tenantName).build()).orElse(null);
    String defaultHost = URI.create(baseUrl).getHost();
    String rpId = defaultHost != null ? defaultHost : tenantName;
    String rpName = tenantName;
    if (tenant != null) {
      var cfg = tenantConfigs.find(TenantConfigFilter.builder().tenant(tenant).build());
      if (cfg.isPresent()) {
        rpId = cfg.get().getWebauthnRpId().orElse(rpId);
        rpName = cfg.get().getWebauthnRpName().orElse(rpName);
      }
    }
    return new String[] {rpId, rpName};
  }

  private String resolveOrigin(HttpHeaders headers) {
    String origin = headers.getHeaderString("Origin");
    if (origin != null && !origin.isBlank()) {
      return origin;
    }
    return baseUrl;
  }

  private static String str(Object o) {
    return o == null ? "" : String.valueOf(o);
  }
}
