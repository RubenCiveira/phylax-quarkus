/* @autogenerated */
package net.civeira.phylax.features.oauth.token.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class AutorizationToken {
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("id_token")
  private String idToken;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("expires_in")
  private Long expiresIn;
  private String scope;
  @JsonProperty("grant_type")
  private String grantType;
  private String username;
}
