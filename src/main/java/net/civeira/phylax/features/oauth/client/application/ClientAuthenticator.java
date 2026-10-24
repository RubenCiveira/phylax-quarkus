package net.civeira.phylax.features.oauth.client.application;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.client.domain.ClientAuthMethod;
import net.civeira.phylax.features.oauth.client.domain.ClientAuthPolicy;
import net.civeira.phylax.features.oauth.client.domain.ClientCredentials;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientStoreGateway;

/**
 * Single point of client authentication for the token, device, PAR, introspection and revocation
 * endpoints (task 0.9).
 *
 * <p>
 * The credentials must use exactly one method ({@link ClientCredentials#method()}). A secret
 * (client_secret_basic or client_secret_post) is verified by {@link ClientStoreGateway#loadPrivate}
 * with a constant-time comparison of its digest; the {@code client_id} alone authenticates a client
 * allowed public access ({@code none}). The {@link ClientAuthPolicy} decides which methods the
 * endpoint accepts, and a client never authenticates with a method it did not register:
 * <ul>
 * <li>registered {@code none}: only as public, never with a secret;</li>
 * <li>registered {@code private_key_jwt}: rejected until client assertions exist (task 3.2);</li>
 * <li>registered secret method: under {@link ClientAuthPolicy#REGISTERED_METHOD} only that one
 * (Basic or body); public access only if the client also allows it.</li>
 * </ul>
 * A client authenticated as public is returned with {@code protectedWithSecret = false}, so the
 * grants that require a confidential client (code without PKCE, password, client_credentials) keep
 * rejecting it.
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ClientAuthenticator {

  private final ClientStoreGateway clients;

  /**
   * Authenticates the client of a request.
   *
   * @param tenant tenant of the endpoint
   * @param credentials credentials presented in the request
   * @param policy methods accepted by the endpoint
   * @return the authenticated client, empty when the request must fail with invalid_client
   */
  public Optional<ClientDetails> authenticate(String tenant, ClientCredentials credentials,
      ClientAuthPolicy policy) {
    Optional<ClientAuthMethod> method = credentials.method();
    Optional<String> clientId = credentials.clientId();
    if (method.isEmpty() || clientId.isEmpty()) {
      return rejected(tenant, credentials, "malformed or mixed credentials");
    }
    ClientAuthMethod used = method.get();
    if (used == ClientAuthMethod.PRIVATE_KEY_JWT) {
      return rejected(tenant, credentials, "client assertions are not supported");
    }
    if (used == ClientAuthMethod.NONE) {
      return policy == ClientAuthPolicy.SECRET ? rejected(tenant, credentials, "secret required")
          : clients.loadPublicById(tenant, clientId.get())
              .filter(
                  client -> client.getTokenEndpointAuthMethod() != ClientAuthMethod.PRIVATE_KEY_JWT)
              .or(() -> rejected(tenant, credentials, "not a public client"));
    }
    return clients.loadPrivate(tenant, clientId.get(), credentials.secret())
        .filter(client -> allowsSecretMethod(client.getTokenEndpointAuthMethod(), used, policy))
        .or(() -> rejected(tenant, credentials, "invalid secret or unregistered method"));
  }

  /**
   * Whether a client registered for {@code registered} may authenticate with the secret method
   * {@code used} under {@code policy}. A client without registered method accepts both.
   */
  private static boolean allowsSecretMethod(ClientAuthMethod registered, ClientAuthMethod used,
      ClientAuthPolicy policy) {
    if (registered == null) {
      return true;
    }
    if (!registered.usesSecret()) {
      return false;
    }
    return policy != ClientAuthPolicy.REGISTERED_METHOD || registered == used;
  }

  private static Optional<ClientDetails> rejected(String tenant, ClientCredentials credentials,
      String reason) {
    log.info("Client authentication failed in tenant {} for {}: {}", tenant, credentials, reason);
    return Optional.empty();
  }
}
