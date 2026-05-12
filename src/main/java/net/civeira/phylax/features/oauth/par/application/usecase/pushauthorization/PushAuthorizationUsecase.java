package net.civeira.phylax.features.oauth.par.application.usecase.pushauthorization;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientStoreGateway;
import net.civeira.phylax.features.oauth.par.domain.ParRequest;
import net.civeira.phylax.features.oauth.par.domain.exception.ParException;
import net.civeira.phylax.features.oauth.par.domain.gateway.ParRequestGateway;

/**
 * Pushes an authorization request and returns a {@code request_uri} — RFC 9126.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class PushAuthorizationUsecase {

  private final ClientStoreGateway clients;
  private final ParRequestGateway gateway;

  public PushAuthorizationResult push(PushAuthorizationParams params) {
    if (clients.loadPrivate(params.getTenant(), params.getClientId(), params.getClientSecret())
        .isEmpty()) {
      throw ParException.invalidClient();
    }

    validateAuthParams(params);

    ParRequest parRequest =
        ParRequest.create(params.getTenant(), params.getClientId(), params.getAuthParams());
    gateway.store(parRequest);

    return new PushAuthorizationResult(parRequest.getRequestUri(), parRequest.expiresInSeconds());
  }

  private void validateAuthParams(PushAuthorizationParams params) {
    var p = params.getAuthParams();
    if (p.getOrDefault("response_type", "").isBlank()) {
      throw ParException.invalidRequest("response_type is required");
    }
    if (p.getOrDefault("redirect_uri", "").isBlank()) {
      throw ParException.invalidRequest("redirect_uri is required");
    }
    if (p.getOrDefault("client_id", "").isBlank()) {
      throw ParException.invalidRequest("client_id is required");
    }
  }
}
