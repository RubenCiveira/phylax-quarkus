package net.civeira.phylax.features.oauth.profile.domain;

import java.util.Optional;

public final class ActiveSession {

  private final String sessionId;
  private final String clientId;
  private final String clientName;
  private final String ipAddress;
  private final String userAgent;
  private final String lastUsedAt;
  private final String expiration;

  public ActiveSession(String sessionId, String clientId, String clientName, String ipAddress,
      String userAgent, String lastUsedAt, String expiration) {
    this.sessionId = sessionId;
    this.clientId = clientId;
    this.clientName = clientName;
    this.ipAddress = ipAddress;
    this.userAgent = userAgent;
    this.lastUsedAt = lastUsedAt;
    this.expiration = expiration;
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getClientId() {
    return clientId;
  }

  public Optional<String> getClientName() {
    return Optional.ofNullable(clientName);
  }

  public Optional<String> getIpAddress() {
    return Optional.ofNullable(ipAddress);
  }

  public Optional<String> getUserAgent() {
    return Optional.ofNullable(userAgent);
  }

  public Optional<String> getLastUsedAt() {
    return Optional.ofNullable(lastUsedAt);
  }

  public String getExpiration() {
    return expiration;
  }
}
