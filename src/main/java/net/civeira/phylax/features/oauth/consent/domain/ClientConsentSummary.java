package net.civeira.phylax.features.oauth.consent.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Summary of scopes granted by a user to a specific OAuth client.
 */
public record ClientConsentSummary(
    String clientUid,
    String clientId,
    Optional<String> clientName,
    List<String> grantedScopes,
    Optional<OffsetDateTime> lastGrantedAt) {
}
