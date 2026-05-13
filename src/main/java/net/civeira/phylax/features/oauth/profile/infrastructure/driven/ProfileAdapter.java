package net.civeira.phylax.features.oauth.profile.infrastructure.driven;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.user.domain.UserReference;
import net.civeira.phylax.features.access.userprofile.domain.UserProfile;
import net.civeira.phylax.features.access.userprofile.domain.UserProfileChangeSet;
import net.civeira.phylax.features.access.userprofile.domain.gateway.UserProfileFilter;
import net.civeira.phylax.features.access.userprofile.domain.gateway.UserProfileReadRepositoryGateway;
import net.civeira.phylax.features.access.userprofile.domain.gateway.UserProfileWriteRepositoryGateway;
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
@ApplicationScoped
@RequiredArgsConstructor
public class ProfileAdapter implements ProfileGateway {

  private final UserProfileReadRepositoryGateway reader;
  private final UserProfileWriteRepositoryGateway writer;

  @Override
  public Optional<OidcProfile> findByUser(String userUid) {
    UserProfileFilter filter = UserProfileFilter.builder().user(UserReference.of(userUid)).build();
    return reader.find(filter).map(this::toOidcProfile);
  }

  @Override
  public OidcProfile save(String userUid, OidcProfileData data) {
    UserProfileFilter filter = UserProfileFilter.builder().user(UserReference.of(userUid)).build();
    Optional<UserProfile> prev = writer.findForUpdate(filter);
    UserProfileChangeSet changes = new UserProfileChangeSet().givenName(data.getGivenName())
        .familyName(data.getFamilyName()).middleName(data.getMiddleName())
        .nickname(data.getNickname()).preferredUsername(data.getPreferredUsername())
        .pictureUrl(data.getPictureUrl()).websiteUrl(data.getWebsiteUrl()).gender(data.getGender())
        .birthdate(data.getBirthdate()).zoneinfo(data.getZoneinfo()).locale(data.getLocale())
        .phoneNumber(data.getPhoneNumber()).phoneNumberVerified(data.getPhoneNumberVerified())
        .addressJson(data.getAddressJson());
    UserProfile profile;
    if (prev.isPresent()) {
      profile = writer.update(prev.get(), prev.get().update(changes));
    } else {
      profile = writer.create(UserProfile.create(changes.newUid().user(UserReference.of(userUid))));
    }
    return toOidcProfile(profile);
  }

  private OidcProfile toOidcProfile(UserProfile profile) {
    return OidcProfile.builder().uid(profile.getUid()).userUid(profile.getUserUid())
        .givenName(profile.getGivenName().orElse(null))
        .familyName(profile.getFamilyName().orElse(null))
        .middleName(profile.getMiddleName().orElse(null))
        .nickname(profile.getNickname().orElse(null))
        .preferredUsername(profile.getPreferredUsername().orElse(null))
        .pictureUrl(profile.getPictureUrl().orElse(null))
        .websiteUrl(profile.getWebsiteUrl().orElse(null)).gender(profile.getGender().orElse(null))
        .birthdate(profile.getBirthdate().orElse(null)).zoneinfo(profile.getZoneinfo().orElse(null))
        .locale(profile.getLocale().orElse(null)).phoneNumber(profile.getPhoneNumber().orElse(null))
        .phoneNumberVerified(profile.isPhoneNumberVerified())
        .addressJson(profile.getAddressJson().orElse(null))
        .updatedAt(profile.getUpdatedAt().orElse(null)).version(profile.getVersion().orElse(null))
        .build();
  }
}
