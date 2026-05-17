package net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels;

import static net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.HtmlEscape.esc;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.common.value.YamlLocaleMessages;

@ApplicationScoped
public class DataExportPanel {

  public String render(String exportUrl, String cancelUrl, boolean requested,
      YamlLocaleMessages t) {
    if (requested) {
      return "<h1>" + t.get("profile.data-export.sentTitle") + "</h1>" + "<p>"
          + t.get("profile.data-export.sentHelp") + "</p>" + "<div class=\"profile-actions\">"
          + "<a class=\"secondary-button\" href=\"" + esc(cancelUrl) + "\">"
          + t.get("profile.data-export.backToProfile") + "</a>" + "</div>";
    }

    return "<h1>" + t.get("profile.data-export.title") + "</h1>" + "<p>"
        + t.get("profile.data-export.help") + "</p>" + "<form method=\"POST\" action=\""
        + esc(exportUrl) + "\" class=\"profile-actions\">"
        + "<input type=\"submit\" class=\"primary-button\" value=\""
        + esc(t.get("profile.data-export.request")) + "\" />"
        + "<a class=\"secondary-button\" href=\"" + esc(cancelUrl) + "\">"
        + t.get("profile.data-export.backToProfile") + "</a>" + "</form>";
  }
}
