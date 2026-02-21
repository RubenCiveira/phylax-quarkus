package net.civeira.phylax.features.oauth.authentication.application;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.session.domain.gateway.SessionStoreGateway;
import net.civeira.phylax.features.oauth.session.domain.model.SessionInfo;

/**
 * Application service for managing authenticated sessions. Wraps {@link SessionStoreGateway} to
 * provide session lifecycle operations.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class SessionManager {

  private final SessionStoreGateway sessionStore;

  public Optional<SessionInfo> loadSession(String sessionId) {
    return sessionStore.loadSession(sessionId);
  }

  public void removeSession(String sessionId) {
    sessionStore.deleteSession(sessionId);
  }
}
