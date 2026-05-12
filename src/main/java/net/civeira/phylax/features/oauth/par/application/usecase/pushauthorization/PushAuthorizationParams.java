package net.civeira.phylax.features.oauth.par.application.usecase.pushauthorization;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushAuthorizationParams {

  private final String tenant;
  private final String clientId;
  private final String clientSecret;
  private final Map<String, String> authParams;
}
