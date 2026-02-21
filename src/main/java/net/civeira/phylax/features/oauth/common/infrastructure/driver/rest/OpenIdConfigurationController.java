package net.civeira.phylax.features.oauth.common.infrastructure.driver.rest;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;

/**
 * OpenID Connect Discovery endpoint (RFC 8414 / OpenID Connect Discovery 1.0). Moved from
 * authentication/infrastructure/driver/rest/OpenIdInformationController.
 */
@Path("")
@RequestScoped
@RequiredArgsConstructor
public class OpenIdConfigurationController {

  @Data
  @Builder
  @RegisterForReflection
  public static class MtlsEndpointAliases {
    @JsonProperty("token_endpoint")
    private final String tokenEndpoint;
    @JsonProperty("revocation_endpoint")
    private final String revocationEndpoint;
    @JsonProperty("introspection_endpoint")
    private final String introspectionEndpoint;
    @JsonProperty("device_authorization_endpoint")
    private final String deviceAuthorizationEndpoint;
    @JsonProperty("registration_endpoint")
    private final String registrationEndpoint;
    @JsonProperty("userinfo_endpoint")
    private final String userinfoEndpoint;
    @JsonProperty("pushed_authorization_request_endpoint")
    private final String pushedAuthorizationRequestEndpoint;
    @JsonProperty("backchannel_authentication_endpoint")
    private final String backchannelAuthenticationEndpoint;
  }

  @Data
  @Builder
  @RegisterForReflection
  public static class OpenIdConfiguration {
    private final String issuer;
    @JsonProperty("authorization_endpoint")
    private final String authorizationEndpoint;
    @JsonProperty("token_endpoint")
    private final String tokenEndpoint;
    @JsonProperty("introspection_endpoint")
    private final String introspectionEndpoint;
    @JsonProperty("userinfo_endpoint")
    private final String userinfoEndpoint;
    @JsonProperty("end_session_endpoint")
    private final String endSessionEndpoint;
    @JsonProperty("frontchannel_logout_session_supported")
    private final boolean frontchannelLogoutSessionSupported;
    @JsonProperty("frontchannel_logout_supported")
    private final boolean frontchannelLogoutSupported;
    @JsonProperty("jwks_uri")
    private final String jwksUri;
    @JsonProperty("check_session_iframe")
    private final String checkSessionIframe;
    @JsonProperty("grant_types_supported")
    private final List<String> grantTypesSupported;
    @JsonProperty("acr_values_supported")
    private final List<String> acrValuesSupported;
    @JsonProperty("response_types_supported")
    private final List<String> responseTypesSupported;
    @JsonProperty("subject_types_supported")
    private final List<String> subjectTypesSupported;
    @JsonProperty("id_token_signing_alg_values_supported")
    private final List<String> idTokenSigningAlgValuesSupported;
    @JsonProperty("id_token_encryption_alg_values_supported")
    private final List<String> idTokenEncryptionAlgValuesSupported;
    @JsonProperty("id_token_encryption_enc_values_supported")
    private final List<String> idTokenEncryptionEncValuesSupported;
    @JsonProperty("userinfo_signing_alg_values_supported")
    private final List<String> userinfoSigningAlgValuesSupported;
    @JsonProperty("userinfo_encryption_alg_values_supported")
    private final List<String> userinfoEncryptionAlgValuesSupported;
    @JsonProperty("userinfo_encryption_enc_values_supported")
    private final List<String> userinfoEncryptionEncValuesSupported;
    @JsonProperty("request_object_signing_alg_values_supported")
    private final List<String> requestObjectSigningAlgValuesSupported;
    @JsonProperty("request_object_encryption_alg_values_supported")
    private final List<String> requestObjectEncryptionAlgValuesSupported;
    @JsonProperty("request_object_encryption_enc_values_supported")
    private final List<String> requestObjectEncryptionEncValuesSupported;
    @JsonProperty("response_modes_supported")
    private final List<String> responseModesSupported;
    @JsonProperty("registration_endpoint")
    private final String registrationEndpoint;
    @JsonProperty("token_endpoint_auth_methods_supported")
    private final List<String> tokenEndpointAuthMethodsSupported;
    @JsonProperty("token_endpoint_auth_signing_alg_values_supported")
    private final List<String> tokenEndpointAuthSigningAlgValuesSupported;
    @JsonProperty("introspection_endpoint_auth_methods_supported")
    private final List<String> introspectionEndpointAuthMethodsSupported;
    @JsonProperty("introspection_endpoint_auth_signing_alg_values_supported")
    private final List<String> introspectionEndpointAuthSigningAlgValuesSupported;
    @JsonProperty("authorization_signing_alg_values_supported")
    private final List<String> authorizationSigningAlgValuesSupported;
    @JsonProperty("authorization_encryption_alg_values_supported")
    private final List<String> authorizationEncryptionAlgValuesSupported;
    @JsonProperty("authorization_encryption_enc_values_supported")
    private final List<String> authorizationEncryptionEncValuesSupported;
    @JsonProperty("claims_supported")
    private final List<String> claimsSupported;
    @JsonProperty("claim_types_supported")
    private final List<String> claimTypesSupported;
    @JsonProperty("claims_parameter_supported")
    private final boolean claimsParameterSupported;
    @JsonProperty("scopes_supported")
    private final List<String> scopesSupported;
    @JsonProperty("request_parameter_supported")
    private final boolean requestParameterSupported;
    @JsonProperty("request_uri_parameter_supported")
    private final boolean requestUriParameterSupported;
    @JsonProperty("require_request_uri_registration")
    private final boolean requireRequestUriRegistration;
    @JsonProperty("code_challenge_methods_supported")
    private final List<String> codeChallengeMethodsSupported;
    @JsonProperty("tls_client_certificate_bound_access_tokens")
    private final boolean tlsClientCertificateBoundAccessTokens;
    @JsonProperty("revocation_endpoint")
    private final String revocationEndpoint;
    @JsonProperty("revocation_endpoint_auth_methods_supported")
    private final List<String> revocationEndpointAuthMethodsSupported;
    @JsonProperty("revocation_endpoint_auth_signing_alg_values_supported")
    private final List<String> revocationEndpointAuthSigningAlgValuesSupported;
    @JsonProperty("backchannel_logout_supported")
    private final boolean backchannelLogoutSupported;
    @JsonProperty("backchannel_logout_session_supported")
    private final boolean backchannelLogoutSessionSupported;
    @JsonProperty("device_authorization_endpoint")
    private final String deviceAuthorizationEndpoint;
    @JsonProperty("backchannel_token_delivery_modes_supported")
    private final List<String> backchannelTokenDeliveryModesSupported;
    @JsonProperty("backchannel_authentication_endpoint")
    private final String backchannelAuthenticationEndpoint;
    @JsonProperty("backchannel_authentication_request_signing_alg_values_supported")
    private final List<String> backchannelAuthenticationRequestSigningAlgValuesSupported;
    @JsonProperty("require_pushed_authorization_requests")
    private final boolean requirePushedAuthorizationRequests;
    @JsonProperty("pushed_authorization_request_endpoint")
    private final String pushedAuthorizationRequestEndpoint;
    @JsonProperty("mtls_endpoint_aliases")
    private final MtlsEndpointAliases mtlsEndpointAliases;
    @JsonProperty("authorization_response_iss_parameter_supported")
    private final boolean authorizationResponseIssParameterSupported;
  }

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
