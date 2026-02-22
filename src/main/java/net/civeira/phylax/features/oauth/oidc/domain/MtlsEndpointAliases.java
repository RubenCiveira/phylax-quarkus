package net.civeira.phylax.features.oauth.oidc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RegisterForReflection
public class MtlsEndpointAliases {
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