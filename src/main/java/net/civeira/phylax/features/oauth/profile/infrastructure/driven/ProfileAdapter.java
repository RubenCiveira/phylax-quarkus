package net.civeira.phylax.features.oauth.profile.infrastructure.driven;

import java.util.Optional;
import java.util.UUID;

import jakarta.enterprise.inject.Vetoed;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfile;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfileData;
import net.civeira.phylax.features.oauth.profile.domain.gateway.ProfileGateway;

/**
 * Stub adapter for OIDC profile persistence.
 *
 * <p>
 * Marked {@code @Vetoed} — replace with a real implementation backed by a user-profile table or
 * read-model.
 * </p>
 */
@Vetoed
@Slf4j
public class ProfileAdapter implements ProfileGateway {

  @Override
  public Optional<OidcProfile> findByUser(String userUid) {
    return Optional.empty();
  }

  @Override
  public OidcProfile save(String userUid, OidcProfileData data) {
    log.warn("ProfileAdapter is a stub — profile not saved for user {}", userUid);
    return OidcProfile.builder().uid(UUID.randomUUID().toString()).userUid(userUid).build();
  }
}
