package net.civeira.phylax.features.oauth.profile.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class OidcProfileData {

  private final String givenName;
  private final String familyName;
  private final String middleName;
  private final String nickname;
  private final String preferredUsername;
  private final String pictureUrl;
  private final String websiteUrl;
  private final String gender;
  private final String birthdate;
  private final String zoneinfo;
  private final String locale;
  private final String phoneNumber;
  private final Boolean phoneNumberVerified;
  private final String addressJson;
}
