package net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels;

import static net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.HtmlEscape.esc;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.common.value.YamlLocaleMessages;

@ApplicationScoped
public class ChangePasswordPanel {

  public String render(String saveUrl, String cancelUrl, String error, boolean success,
      YamlLocaleMessages t) {
    String title = "<h1>" + t.get("profile.password.title") + "</h1>";

    if (success) {
      return title + "<p class=\"change-password-success\">" + t.get("profile.password.success")
          + "</p>" + "<div class=\"profile-actions\">" + "<a class=\"secondary-button\" href=\""
          + esc(cancelUrl) + "\">" + t.get("profile.password.backToProfile") + "</a>" + "</div>";
    }

    String errorHtml =
        error != null && !error.isBlank() ? "<p class=\"error\">" + esc(error) + "</p>" : "";

    return title + errorHtml + "<form method=\"POST\" action=\"" + esc(saveUrl)
        + "\" class=\"change-password-form\">"
        + passField(t.get("profile.password.currentPassword"), "currentPassword",
            "current-password")
        + passField(t.get("profile.password.newPassword"), "newPassword", "new-password")
        + passField(t.get("profile.password.confirmPassword"), "confirmPassword", "new-password")
        + "<div class=\"profile-actions\">"
        + "<input class=\"primary-button\" type=\"submit\" value=\""
        + esc(t.get("profile.password.submit")) + "\" />" + "<a class=\"secondary-button\" href=\""
        + esc(cancelUrl) + "\">" + t.get("profile.password.cancel") + "</a>" + "</div>" + "</form>";
  }

  private String passField(String label, String name, String autocomplete) {
    return "<label>" + label + "<input type=\"password\" name=\"" + name + "\" autocomplete=\""
        + autocomplete + "\" required />" + "</label>";
  }
}
