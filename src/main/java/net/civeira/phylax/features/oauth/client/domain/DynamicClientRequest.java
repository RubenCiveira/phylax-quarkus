package net.civeira.phylax.features.oauth.client.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * Client metadata provided in a Dynamic Client Registration request (RFC 7591).
 */
@Builder
@Getter
public class DynamicClientRequest {

  private final List<String> redirectUris;
  private final String clientName;

  @Builder.Default
  private final List<String> grantTypes = List.of("authorization_code");

  @Builder.Default
  private final List<String> responseTypes = List.of("code");

  private final String scope;

  @Builder.Default
  private final String tokenEndpointAuthMethod = "client_secret_basic";

  private final String logoUri;
  private final String clientUri;
  private final String policyUri;
  private final String tosUri;
  private final String backchannelLogoutUri;
  private final String frontchannelLogoutUri;
}
