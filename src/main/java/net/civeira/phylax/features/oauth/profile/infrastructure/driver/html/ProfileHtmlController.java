package net.civeira.phylax.features.oauth.profile.infrastructure.driver.html;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.application.SessionManager;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.OidcCookieManager;
import net.civeira.phylax.features.oauth.profile.application.ProfileService;
import net.civeira.phylax.features.oauth.profile.domain.ActiveSession;
import net.civeira.phylax.features.oauth.profile.domain.MfaSetup;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfile;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfileData;
import net.civeira.phylax.features.oauth.theme.domain.gateway.DecoratePageGateway;

@Path("")
@RequestScoped
@RequiredArgsConstructor
public class ProfileHtmlController {
  private static final String TEXT_HTML = "text/html";

  private final SessionManager sessionManager;
  private final ProfileService profileService;
  private final DecoratePageGateway decorator;

  @GET
  @Path("oauth/openid/{tenant}/profile")
  @Produces(TEXT_HTML)
  public Response viewProfile(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    return me(tenant, cookie, headers);
  }

  @GET
  @Path("oauth/openid/{tenant}/me")
  @Produces(TEXT_HTML)
  public Response me(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    return sessionManager.loadSession(cookie).map(session -> {
      OidcProfile profile = profileService.getProfile(session.getUserId()).orElse(null);
      String base = "/oauth/openid/" + tenant + "/me";
      StringBuilder html = new StringBuilder();
      html.append("<h1>Profile</h1>");
      html.append("<p><strong>User:</strong> ")
          .append(escape(session.getValidationData().getUsername())).append("</p>");
      html.append("<p><a href=\"").append(base).append("/edit\">Edit profile</a></p>");
      html.append("<p><a href=\"").append(base).append("/password\">Change password</a></p>");
      html.append("<p><a href=\"").append(base).append("/mfa\">MFA</a></p>");
      html.append("<p><a href=\"").append(base).append("/sessions\">Sessions</a></p>");
      html.append(buildProfileSummary(profile));
      return page(tenant, headers, "Profile", html.toString());
    }).orElseGet(() -> Response.status(401).build());
  }

  @GET
  @Path("oauth/openid/{tenant}/me/edit")
  @Produces(TEXT_HTML)
  public Response edit(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    return sessionManager.loadSession(cookie).map(session -> {
      OidcProfile profile = profileService.getProfile(session.getUserId()).orElse(null);
      String action = "/oauth/openid/" + tenant + "/me/edit";
      String cancel = "/oauth/openid/" + tenant + "/me";
      String html = "<h1>Edit profile</h1>" + "<form method=\"post\" action=\"" + action + "\">"
          + input("givenName", profile == null ? "" : profile.getGivenName().orElse(""))
          + input("familyName", profile == null ? "" : profile.getFamilyName().orElse(""))
          + input("preferredUsername",
              profile == null ? "" : profile.getPreferredUsername().orElse(""))
          + input("locale", profile == null ? "" : profile.getLocale().orElse(""))
          + input("phoneNumber", profile == null ? "" : profile.getPhoneNumber().orElse(""))
          + "<button type=\"submit\">Save</button></form>" + "<p><a href=\"" + cancel
          + "\">Back</a></p>";
      return page(tenant, headers, "Edit profile", html);
    }).orElseGet(() -> Response.status(401).build());
  }

  @POST
  @Path("oauth/openid/{tenant}/me/edit")
  public Response saveEdit(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie,
      MultivaluedMap<String, String> form) {
    return sessionManager.loadSession(cookie).map(session -> {
      OidcProfileData data = OidcProfileData.builder().givenName(first(form, "givenName"))
          .familyName(first(form, "familyName")).preferredUsername(first(form, "preferredUsername"))
          .locale(first(form, "locale")).phoneNumber(first(form, "phoneNumber")).build();
      profileService.saveProfile(session.getUserId(), data);
      return Response.status(302).header("Location", "/oauth/openid/" + tenant + "/me").build();
    }).orElseGet(() -> Response.status(401).build());
  }

  @GET
  @Path("oauth/openid/{tenant}/me/password")
  @Produces(TEXT_HTML)
  public Response password(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    return sessionManager.loadSession(cookie).map(_s -> {
      String action = "/oauth/openid/" + tenant + "/me/password";
      String html = "<h1>Change password</h1><form method=\"post\" action=\"" + action + "\">"
          + input("oldPass", "") + input("newPass", "")
          + "<button type=\"submit\">Change</button></form>";
      return page(tenant, headers, "Change password", html);
    }).orElseGet(() -> Response.status(401).build());
  }

  @POST
  @Path("oauth/openid/{tenant}/me/password")
  @Produces(TEXT_HTML)
  public Response doPassword(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers,
      MultivaluedMap<String, String> form) {
    return sessionManager.loadSession(cookie).map(session -> {
      boolean ok = profileService.changePassword(session.getUserId(), tenant,
          first(form, "oldPass"), first(form, "newPass"));
      return page(tenant, headers, "Change password",
          "<h1>Change password</h1><p>Result: " + (ok ? "ok" : "not changed") + "</p>");
    }).orElseGet(() -> Response.status(401).build());
  }

  @GET
  @Path("oauth/openid/{tenant}/me/mfa")
  @Produces(TEXT_HTML)
  public Response mfa(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    return sessionManager.loadSession(cookie).map(session -> {
      boolean enabled = profileService.isMfaEnabled(session.getUserId(), tenant);
      MfaSetup setup = profileService.buildMfaSetup(session.getUserId(), tenant);
      String action = "/oauth/openid/" + tenant + "/me/mfa";
      String html = "<h1>MFA</h1><p>Enabled: " + enabled + "</p><p>Seed: " + escape(setup.seed())
          + "</p><form method=\"post\" action=\"" + action + "\">" + input("seed", setup.seed())
          + input("otp", "")
          + "<button type=\"submit\" name=\"action\" value=\"enable\">Enable</button>"
          + "<button type=\"submit\" name=\"action\" value=\"disable\">Disable</button>"
          + "</form>";
      return page(tenant, headers, "MFA", html);
    }).orElseGet(() -> Response.status(401).build());
  }

  @POST
  @Path("oauth/openid/{tenant}/me/mfa")
  public Response doMfa(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie,
      MultivaluedMap<String, String> form) {
    return sessionManager.loadSession(cookie).map(session -> {
      String action = first(form, "action");
      if ("disable".equals(action)) {
        profileService.disableMfa(session.getUserId(), tenant);
      } else {
        profileService.enableMfa(session.getUserId(), tenant, first(form, "seed"),
            first(form, "otp"));
      }
      return Response.status(302).header("Location", "/oauth/openid/" + tenant + "/me/mfa").build();
    }).orElseGet(() -> Response.status(401).build());
  }

  @GET
  @Path("oauth/openid/{tenant}/me/sessions")
  @Produces(TEXT_HTML)
  public Response sessions(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    return sessionManager.loadSession(cookie).map(session -> {
      String action = "/oauth/openid/" + tenant + "/me/sessions/revoke";
      StringBuilder html = new StringBuilder("<h1>Sessions</h1>");
      for (ActiveSession it : profileService.listSessions(session.getUserId())) {
        html.append("<div><p><strong>").append(escape(it.getSessionId())).append("</strong> ")
            .append(escape(it.getClientId())).append("</p>")
            .append("<form method=\"post\" action=\"").append(action).append("\">")
            .append("<input type=\"hidden\" name=\"sessionId\" value=\"")
            .append(escape(it.getSessionId())).append("\"/>")
            .append("<button type=\"submit\">Revoke</button></form></div>");
      }
      return page(tenant, headers, "Sessions", html.toString());
    }).orElseGet(() -> Response.status(401).build());
  }

  @POST
  @Path("oauth/openid/{tenant}/me/sessions/revoke")
  public Response revokeSession(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie,
      MultivaluedMap<String, String> form) {
    return sessionManager.loadSession(cookie).map(_s -> {
      String sessionId = first(form, "sessionId");
      if (!sessionId.isBlank()) {
        profileService.revokeSession(sessionId);
      }
      return Response.status(302).header("Location", "/oauth/openid/" + tenant + "/me/sessions")
          .build();
    }).orElseGet(() -> Response.status(401).build());
  }

  private String buildProfileSummary(OidcProfile profile) {
    StringBuilder sb = new StringBuilder();
    if (profile == null) {
      sb.append("<p>Profile data is not available yet.</p>");
      return sb.toString();
    }
    sb.append("<h2>Data</h2>");
    append(sb, "Given name", profile.getGivenName().orElse(null));
    append(sb, "Family name", profile.getFamilyName().orElse(null));
    append(sb, "Preferred username", profile.getPreferredUsername().orElse(null));
    append(sb, "Locale", profile.getLocale().orElse(null));
    append(sb, "Phone", profile.getPhoneNumber().orElse(null));
    return sb.toString();
  }

  private Response page(String tenant, HttpHeaders headers, String title, String html) {
    return Response.ok(decorator.getFullPage(tenant, title, html,
        headers.getAcceptableLanguages().isEmpty() ? java.util.Locale.ENGLISH
            : headers.getAcceptableLanguages().get(0)))
        .build();
  }

  private String input(String name, String value) {
    return "<p><label>" + name + "<br/><input name=\"" + name + "\" value=\"" + escape(value)
        + "\"/></label></p>";
  }

  private String first(MultivaluedMap<String, String> form, String key) {
    String value = form.getFirst(key);
    return value == null ? "" : value;
  }

  private void append(StringBuilder sb, String label, String value) {
    if (value != null && !value.isBlank()) {
      sb.append("<p><strong>").append(label).append(":</strong> ").append(escape(value))
          .append("</p>");
    }
  }

  private String escape(String value) {
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"",
        "&quot;");
  }
}
