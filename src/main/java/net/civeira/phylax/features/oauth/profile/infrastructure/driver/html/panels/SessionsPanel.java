package net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels;

import static net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.HtmlEscape.esc;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import net.civeira.phylax.common.value.YamlLocaleMessages;
import net.civeira.phylax.features.oauth.profile.domain.ActiveSession;

@ApplicationScoped
public class SessionsPanel {

  public String render(List<ActiveSession> sessions, String revokeBaseUrl, String cancelUrl,
      String currentSessionId, YamlLocaleMessages t) {

    StringBuilder cards = new StringBuilder();
    for (ActiveSession session : sessions) {
//      String sessionId = esc(session.getSessionId());
      String client =
          esc(session.getClientName().filter(s -> !s.isBlank()).orElseGet(session::getClientId));
      String ip = esc(session.getIpAddress().orElse(t.get("profile.sessions.unknown")));
      String agent = esc(session.getUserAgent().orElse(t.get("profile.sessions.unknown")));
      String lastUsed = esc(session.getLastUsedAt().orElse(t.get("profile.sessions.unknown")));
      String expiration = esc(session.getExpiration());
      boolean isCurrent =
          currentSessionId != null && currentSessionId.equals(session.getSessionId());
      String badge =
          isCurrent ? "<strong>" + t.get("profile.sessions.currentSession") + "</strong>" : "";

      String actions;
      if (isCurrent) {
        actions = "<div class=\"profile-actions\"><span class=\"secondary-button\">"
            + t.get("profile.sessions.thisDevice") + "</span></div>";
      } else {
        actions = "<form method=\"POST\" action=\""
            + esc(revokeBaseUrl + "/" + session.getSessionId()) + "\" class=\"profile-actions\">"
            + "<input class=\"secondary-button\" type=\"submit\" value=\""
            + esc(t.get("profile.sessions.closeSession")) + "\" />" + "</form>";
      }

      cards.append("<div class=\"section-card\" style=\"margin-bottom: 1rem;\">")
          .append("<p><strong>").append(t.get("profile.sessions.client")).append(":</strong> ")
          .append(client).append(" ").append(badge).append("</p>").append("<p><strong>")
          .append(t.get("profile.sessions.ip")).append(":</strong> ").append(ip).append("</p>")
          .append("<p><strong>").append(t.get("profile.sessions.userAgent")).append(":</strong> ")
          .append(agent).append("</p>").append("<p><strong>")
          .append(t.get("profile.sessions.lastUsed")).append(":</strong> ").append(lastUsed)
          .append("</p>").append("<p><strong>").append(t.get("profile.sessions.expires"))
          .append(":</strong> ").append(expiration).append("</p>").append(actions).append("</div>");
    }

    if (cards.isEmpty()) {
      cards.append("<p>").append(t.get("profile.sessions.empty")).append("</p>");
    }

    return "<h1>" + t.get("profile.sessions.title") + "</h1>" + "<p>"
        + t.get("profile.sessions.help") + "</p>" + cards + "<div class=\"profile-actions\">"
        + "<a class=\"secondary-button\" href=\"" + esc(cancelUrl) + "\">"
        + t.get("profile.sessions.backToProfile") + "</a>" + "</div>";
  }
}
