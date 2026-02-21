package net.civeira.phylax.testing.oauth.alt;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationData;
import net.civeira.phylax.features.oauth.client.domain.model.ClientDetails;
import net.civeira.phylax.features.oauth.session.domain.gateway.SessionStoreGateway;
import net.civeira.phylax.features.oauth.session.domain.model.SessionInfo;

@Alternative
@Priority(1)
@ApplicationScoped
public class FixedSessionStore implements SessionStoreGateway {
  private final ConcurrentMap<String, SessionInfo> sessions = new ConcurrentHashMap<>();

  @Override
  public Optional<SessionInfo> loadSession(String state) {
    return Optional.ofNullable(sessions.get(state));
  }

  @Override
  public void saveSession(String state, ClientDetails clientDetails, String grant,
      AuthenticationData validationData, String csid) {
    SessionInfo info = SessionInfo.builder().csid(csid).clientId(clientDetails.getClientId())
        .grant(grant).issuer(null).userId(validationData.getUsername()).withMfa(false)
        .validationData(validationData).build();
    sessions.put(state, info);
  }

  @Override
  public void updateSession(String newState, String oldState) {
    SessionInfo existing = sessions.remove(oldState);
    if (existing != null) {
      sessions.put(newState, existing);
    }
  }

  @Override
  public void deleteSession(String state) {
    sessions.remove(state);
  }

  public void clear() {
    sessions.clear();
  }
}
