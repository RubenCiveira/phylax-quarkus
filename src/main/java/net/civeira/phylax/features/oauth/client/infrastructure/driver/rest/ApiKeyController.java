package net.civeira.phylax.features.oauth.client.infrastructure.driver.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.client.domain.gateway.ApiKeyStoreGateway;

/**
 * REST controller for API Key authentication. Validates an API key and returns an access token.
 *
 * TODO: complete implementation — validate the key via ApiKeyStoreGateway, build an access token
 * with the key's scopes, and return it as a standard token response.
 */
@Path("")
@RequestScoped
@RequiredArgsConstructor
public class ApiKeyController {

  private final ApiKeyStoreGateway apiKeyStore;

  @POST
  @Path("oauth/api-key")
  public Response exchangeApiKey(MultivaluedMap<String, String> paramMap) {
    String key = paramMap.getFirst("api_key");
    return apiKeyStore.apiKey(key)
        .map(data -> Response.status(501, "API Key token exchange not yet implemented").build())
        .orElseGet(() -> Response.status(401, "Invalid API key").build());
  }
}
