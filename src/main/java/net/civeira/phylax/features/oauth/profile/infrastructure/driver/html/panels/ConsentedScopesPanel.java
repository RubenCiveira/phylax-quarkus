package net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels;

import static net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.HtmlEscape.esc;

import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.common.value.YamlLocaleMessages;
import net.civeira.phylax.features.oauth.consent.domain.ClientConsentSummary;

@ApplicationScoped
public class ConsentedScopesPanel {

  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public String render(List<ClientConsentSummary> consents, String revokeBaseUrl, String cancelUrl,
      YamlLocaleMessages t) {

    StringBuilder cards = new StringBuilder();
    for (ClientConsentSummary summary : consents) {
      String name = esc(summary.clientName().orElse(summary.clientId()));
      String scopes =
          String.join(", ", summary.grantedScopes().stream().map(HtmlEscape::esc).toList());
      String grantedAt = summary.lastGrantedAt().map(dt -> esc(dt.format(DATE_FMT)))
          .orElse(esc(t.get("profile.consents.unknown")));

      String revokeAction = esc(revokeBaseUrl + "/" + summary.clientUid());

      cards.append("<div class=\"section-card\" style=\"margin-bottom: 1rem;\">")
          .append("<p><strong>").append(t.get("profile.consents.client")).append(":</strong> ")
          .append(name).append("</p>").append("<p><strong>")
          .append(t.get("profile.consents.scopes")).append(":</strong> ").append(scopes)
          .append("</p>").append("<p><strong>").append(t.get("profile.consents.grantedAt"))
          .append(":</strong> ").append(grantedAt).append("</p>")
          .append("<form method=\"POST\" action=\"").append(revokeAction)
          .append("\" class=\"profile-actions\">")
          .append("<input class=\"secondary-button\" type=\"submit\" value=\"")
          .append(esc(t.get("profile.consents.revoke"))).append("\" />").append("</form>")
          .append("</div>");
    }

    if (cards.isEmpty()) {
      cards.append("<p>").append(t.get("profile.consents.empty")).append("</p>");
    }

    return "<h1>" + t.get("profile.consents.title") + "</h1>" + "<p>"
        + t.get("profile.consents.help") + "</p>" + cards + "<div class=\"profile-actions\">"
        + "<a class=\"secondary-button\" href=\"" + esc(cancelUrl) + "\">"
        + t.get("profile.consents.backToProfile") + "</a>" + "</div>";
  }
}
