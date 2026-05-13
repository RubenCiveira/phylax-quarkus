package net.civeira.phylax.features.oauth.profile.infrastructure.driver.rest;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.application.SessionManager;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.OidcCookieManager;
import net.civeira.phylax.features.oauth.profile.application.ProfileService;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfile;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfileData;

@Path("")
@RequestScoped
@RequiredArgsConstructor
public class ProfileMeController {

  private final SessionManager sessionManager;
  private final ProfileService profileService;

  @Data
  public static class ProfileApiDto {
    private String givenName;
    private String familyName;
    private String middleName;
    private String nickname;
    private String preferredUsername;
    private String pictureUrl;
    private String websiteUrl;
    private String gender;
    private String birthdate;
    private String zoneinfo;
    private String locale;
    private String phoneNumber;
    private Boolean phoneNumberVerified;
    private String addressJson;
  }

  @GET
  @Path("oauth/openid/{tenant}/me/profile")
  public Response get(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie) {
    return sessionManager.loadSession(cookie)
        .map(session -> Response
            .ok(toMap(profileService.getProfile(session.getUserId()).orElse(null))).build())
        .orElseGet(() -> Response.status(401).build());
  }

  @PUT
  @Path("oauth/openid/{tenant}/me/profile")
  public Response put(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, ProfileApiDto dto) {
    return sessionManager.loadSession(cookie).map(session -> {
      OidcProfile saved = profileService.saveProfile(session.getUserId(),
          OidcProfileData.builder().givenName(dto.getGivenName()).familyName(dto.getFamilyName())
              .middleName(dto.getMiddleName()).nickname(dto.getNickname())
              .preferredUsername(dto.getPreferredUsername()).pictureUrl(dto.getPictureUrl())
              .websiteUrl(dto.getWebsiteUrl()).gender(dto.getGender()).birthdate(dto.getBirthdate())
              .zoneinfo(dto.getZoneinfo()).locale(dto.getLocale()).phoneNumber(dto.getPhoneNumber())
              .phoneNumberVerified(dto.getPhoneNumberVerified()).addressJson(dto.getAddressJson())
              .build());
      return Response.ok(toMap(saved)).build();
    }).orElseGet(() -> Response.status(401).build());
  }

  private Map<String, Object> toMap(OidcProfile profile) {
    Map<String, Object> out = new LinkedHashMap<>();
    if (profile == null) {
      return out;
    }
    out.put("uid", profile.getUid());
    out.put("userUid", profile.getUserUid());
    out.put("givenName", profile.getGivenName().orElse(null));
    out.put("familyName", profile.getFamilyName().orElse(null));
    out.put("middleName", profile.getMiddleName().orElse(null));
    out.put("nickname", profile.getNickname().orElse(null));
    out.put("preferredUsername", profile.getPreferredUsername().orElse(null));
    out.put("pictureUrl", profile.getPictureUrl().orElse(null));
    out.put("websiteUrl", profile.getWebsiteUrl().orElse(null));
    out.put("gender", profile.getGender().orElse(null));
    out.put("birthdate", profile.getBirthdate().orElse(null));
    out.put("zoneinfo", profile.getZoneinfo().orElse(null));
    out.put("locale", profile.getLocale().orElse(null));
    out.put("phoneNumber", profile.getPhoneNumber().orElse(null));
    out.put("phoneNumberVerified", profile.getPhoneNumberVerified().orElse(null));
    out.put("addressJson", profile.getAddressJson().orElse(null));
    out.put("updatedAt", profile.getUpdatedAt().orElse(null));
    out.put("version", profile.getVersion().orElse(null));
    return out;
  }
}
