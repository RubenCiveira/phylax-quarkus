package net.civeira.phylax.features.oauth.oidc.infrastructure.driver.rest;

import java.util.Arrays;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.oauth.oidc.domain.MtlsEndpointAliases;
import net.civeira.phylax.features.oauth.oidc.domain.OpenIdConfiguration;

/**
 * OpenID Connect Discovery endpoint (RFC 8414 / OpenID Connect Discovery 1.0). Moved from
 * authentication/infrastructure/driver/rest/OpenIdInformationController.
 */
@Path("")
@RequestScoped
@RequiredArgsConstructor
public class OpenIdConfigurationController {

  private final CurrentRequest current;

  @GET
  @Path("oauth/openid/{tenant}/configuration")
  public Response tenantType(final @PathParam("tenant") String tenant, @Context UriInfo request) {
    return Response.ok(info(current.getPublicHost() + "/oauth/openid/" + tenant + "/", tenant))
        .build();
  }

  @GET
  @Path("oauth/openid/{tenant}/.well-known/openid-configuration")
  public Response wellKnowTenantType(final @PathParam("tenant") String tenant,
      @Context HttpHeaders headers, @Context UriInfo request) {
    return Response.ok(info(current.getPublicHost() + "/oauth/openid/" + tenant + "/", tenant))
        .build();
  }

  private OpenIdConfiguration info(String base, String tenant) {
    return OpenIdConfiguration.builder().issuer(issuer(tenant)).authorizationEndpoint(base + "auth")
        .tokenEndpoint(base + "token").introspectionEndpoint(base + "introspect")
        .userinfoEndpoint(base + "userinfo").endSessionEndpoint(base + "logout")
        .frontchannelLogoutSessionSupported(false).frontchannelLogoutSupported(false)
        .jwksUri(base + "jwks").checkSessionIframe(base + "login-status-iframe")
        .grantTypesSupported(Arrays.asList("refresh_token", "password", "mfa"))
        .acrValuesSupported(Arrays.asList("0", "1"))
        .responseTypesSupported(Arrays.asList("code", "none", "id_token", "token", "id_token token",
            "code id_token", "code token", "code id_token token"))
        .subjectTypesSupported(Arrays.asList("public", "pairwise"))
        .idTokenSigningAlgValuesSupported(Arrays.asList("RS256"))
        .idTokenEncryptionAlgValuesSupported(Arrays.asList("RSA-OAEP"))
        .idTokenEncryptionEncValuesSupported(Arrays.asList("A256GCM"))
        .userinfoSigningAlgValuesSupported(Arrays.asList("RS256"))
        .userinfoEncryptionAlgValuesSupported(Arrays.asList("RSA-OAEP"))
        .userinfoEncryptionEncValuesSupported(Arrays.asList("A256GCM"))
        .requestObjectSigningAlgValuesSupported(Arrays.asList("RS256"))
        .requestObjectEncryptionAlgValuesSupported(Arrays.asList("RSA-OAEP"))
        .requestObjectEncryptionEncValuesSupported(Arrays.asList("A256GCM"))
        .responseModesSupported(Arrays.asList("query", "fragment"))
        .registrationEndpoint(base + "connect")
        .tokenEndpointAuthSigningAlgValuesSupported(Arrays.asList("RS256"))
        .introspectionEndpointAuthMethodsSupported(Arrays.asList("private_key_jwt",
            "client_secret_basic", "client_secret_post", "tls_client_auth", "client_secret_jwt"))
        .introspectionEndpointAuthSigningAlgValuesSupported(Arrays.asList("RS256"))
        .authorizationSigningAlgValuesSupported(Arrays.asList("RS256"))
        .authorizationEncryptionAlgValuesSupported(Arrays.asList("RSA-OAEP"))
        .authorizationEncryptionEncValuesSupported(Arrays.asList("A256GCM"))
        .claimsSupported(Arrays.asList("aud", "sub", "iss", "auth_time", "name", "given_name",
            "family_name", "preferred_username", "email", "acr"))
        .claimTypesSupported(Arrays.asList("normal")).claimsParameterSupported(true)
        .scopesSupported(Arrays.asList("roles", "profile", "email", "phone"))
        .requestParameterSupported(true).requestUriParameterSupported(true)
        .requireRequestUriRegistration(true).tlsClientCertificateBoundAccessTokens(true)
        .revocationEndpoint(base + "revocation")
        .revocationEndpointAuthMethodsSupported(Arrays.asList("private_key_jwt",
            "client_secret_basic", "client_secret_post", "tls_client_auth", "client_secret_jwt"))
        .revocationEndpointAuthSigningAlgValuesSupported(Arrays.asList("RS256"))
        .backchannelLogoutSessionSupported(true).backchannelLogoutSupported(true)
        .deviceAuthorizationEndpoint(base + "device")
        .backchannelTokenDeliveryModesSupported(Arrays.asList("poll", "ping"))
        .backchannelAuthenticationEndpoint(base + "ciba-auth")
        .backchannelAuthenticationRequestSigningAlgValuesSupported(Arrays.asList("RS256"))
        .requirePushedAuthorizationRequests(true)
        .pushedAuthorizationRequestEndpoint(base + "par-request")
        .mtlsEndpointAliases(MtlsEndpointAliases.builder().tokenEndpoint(base + "token")
            .revocationEndpoint(base + "revocation").introspectionEndpoint(base + "introspect")
            .deviceAuthorizationEndpoint(base + "device")
            .registrationEndpoint(base + "registration").userinfoEndpoint(base + "userinfo")
            .pushedAuthorizationRequestEndpoint(base + "par-request")
            .backchannelAuthenticationEndpoint(base + "ciba-auth").build())
        .authorizationResponseIssParameterSupported(true).build();
  }

  private String issuer(String tenant) {
    return current.getPublicHost() + "/oauth/openid/" + tenant;
  }
}
