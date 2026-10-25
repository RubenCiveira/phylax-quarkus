package net.civeira.phylax.integrationtest.features.oauth.alt;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.par.domain.ParRequest;
import net.civeira.phylax.features.oauth.par.domain.gateway.ParRequestGateway;

/**
 * Pushed requests in memory, with the atomic single use of {@code ParRequestSqlAdapter}.
 */
@IfBuildProfile("test")
@Alternative
@Priority(1)
@ApplicationScoped
public class FixedParRequestGateway implements ParRequestGateway {

  private final Map<String, ParRequest> requests = new ConcurrentHashMap<>();

  public void clear() {
    requests.clear();
  }

  @Override
  public void store(ParRequest request) {
    requests.put(key(request.getRequestUri(), request.getTenant()), request);
  }

  @Override
  public Optional<ParRequest> findByUri(String requestUri, String tenant) {
    return Optional.ofNullable(requests.get(key(requestUri, tenant)));
  }

  @Override
  public synchronized boolean consume(String requestUri, String tenant, OffsetDateTime until) {
    ParRequest current = requests.get(key(requestUri, tenant));
    if (current == null || current.isUsed() || current.isExpired()) {
      return false;
    }
    requests.put(key(requestUri, tenant), ParRequest.reconstitute(requestUri, tenant,
        current.getClientId(), current.getParams(), until, OffsetDateTime.now(ZoneOffset.UTC)));
    return true;
  }

  /**
   * Makes a stored request expire, as time passing would.
   */
  public void expire(String requestUri, String tenant) {
    ParRequest current = requests.get(key(requestUri, tenant));
    requests.put(key(requestUri, tenant),
        ParRequest.reconstitute(requestUri, tenant, current.getClientId(), current.getParams(),
            OffsetDateTime.now(ZoneOffset.UTC).minusSeconds(1), current.getUsedAt().orElse(null)));
  }

  private static String key(String requestUri, String tenant) {
    return tenant + "|" + requestUri;
  }
}
