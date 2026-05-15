package net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels;

import static net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.HtmlEscape.esc;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.common.value.YamlLocaleMessages;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfile;

@ApplicationScoped
public class ProfileViewPanel {

  public String render(OidcProfile profile, String editUrl, String passwordUrl, String mfaUrl,
      String sessionsUrl, String recoveryCodesUrl, YamlLocaleMessages t) {

    String givenName = esc(profile == null ? null : profile.getGivenName().orElse(null));
    String familyName = esc(profile == null ? null : profile.getFamilyName().orElse(null));
    String nickname = esc(profile == null ? null : profile.getNickname().orElse(null));
    String preferredUsername =
        esc(profile == null ? null : profile.getPreferredUsername().orElse(null));
    String pictureUrl = esc(profile == null ? null : profile.getPictureUrl().orElse(null));
    String websiteUrl = esc(profile == null ? null : profile.getWebsiteUrl().orElse(null));
    String phoneNumber = esc(profile == null ? null : profile.getPhoneNumber().orElse(null));
    String locale = esc(profile == null ? null : profile.getLocale().orElse(null));
    String zoneinfo = esc(profile == null ? null : profile.getZoneinfo().orElse(null));
    String birthdate = esc(profile == null ? null : profile.getBirthdate().orElse(null));
    String gender = esc(profile == null ? null : profile.getGender().orElse(null));

    String avatar = !pictureUrl.isEmpty()
        ? "<img src=\"" + pictureUrl + "\" alt=\"Profile picture\" class=\"profile-avatar\" />"
        : "<div class=\"profile-avatar-placeholder\"></div>";

    String name = (givenName + " " + familyName).trim();
    String displayName =
        !name.isEmpty() ? name : (!nickname.isEmpty() ? nickname : preferredUsername);

    return "<h1>" + t.get("profile.view.title") + "</h1>" + "<div class=\"profile-view\">"
        + "<div class=\"profile-header\">" + avatar + "<div class=\"profile-display-name\">"
        + displayName + "</div>" + "</div>" + "<dl class=\"profile-fields\">"
        + dt(t.get("profile.view.givenName"), givenName)
        + dt(t.get("profile.view.familyName"), familyName)
        + dt(t.get("profile.view.nickname"), nickname)
        + dt(t.get("profile.view.username"), preferredUsername)
        + dt(t.get("profile.view.phone"), phoneNumber) + dt(t.get("profile.view.locale"), locale)
        + dt(t.get("profile.view.timezone"), zoneinfo)
        + dt(t.get("profile.view.birthdate"), birthdate) + dt(t.get("profile.view.gender"), gender)
        + dt(t.get("profile.view.website"), websiteUrl) + "</dl>"
        + "<div class=\"profile-actions\">" + "<a class=\"primary-button\" href=\"" + esc(editUrl)
        + "\">" + t.get("profile.view.editProfile") + "</a>"
        + "<a class=\"secondary-button\" href=\"" + esc(passwordUrl) + "\">"
        + t.get("profile.view.changePassword") + "</a>" + "<a class=\"secondary-button\" href=\""
        + esc(mfaUrl) + "\">" + t.get("profile.view.configureMfa") + "</a>"
        + "<a class=\"secondary-button\" href=\"" + esc(sessionsUrl) + "\">"
        + t.get("profile.view.manageSessions") + "</a>" + "<a class=\"secondary-button\" href=\""
        + esc(recoveryCodesUrl) + "\">" + t.get("profile.view.recoveryCodes") + "</a>" + "</div>"
        + "</div>";
  }

  private String dt(String label, String value) {
    return "<dt>" + label + "</dt><dd>" + value + "</dd>";
  }
}
