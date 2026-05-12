package net.civeira.phylax.features.oauth.key.domain.gateway;

import java.time.Instant;

/**
 * Domain port for JWT revocation storage.
 *
 * <p>
 * Allows individual JWTs to be revoked by JTI before they expire (logout, security events). The
 * store is tenant-scoped and must be cleaned up periodically.
 * </p>
 */
public interface TokenRevocationGateway {

  /**
   * Marks a JWT as revoked until its natural expiry.
   *
   * @param jti JWT ID claim
   * @param tenantId tenant identifier
   * @param tokenType token type (e.g. {@code access_token}, {@code refresh_token})
   * @param expiresAt token expiry — used to schedule cleanup
   */
  void revokeJti(String jti, String tenantId, String tokenType, Instant expiresAt);

  /**
   * Returns {@code true} if the JTI has been revoked.
   *
   * @param jti JWT ID claim
   * @param tenantId tenant identifier
   * @return true when the token is revoked
   */
  boolean isRevoked(String jti, String tenantId);

  /**
   * Removes expired revocation entries.
   *
   * <p>
   * Called periodically by maintenance jobs. Entries whose associated token has already expired are
   * safe to delete.
   * </p>
   */
  void cleanup();
}
