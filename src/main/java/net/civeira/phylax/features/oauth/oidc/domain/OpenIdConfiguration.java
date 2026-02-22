package net.civeira.phylax.features.oauth.oidc.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RegisterForReflection
public class OpenIdConfiguration {
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