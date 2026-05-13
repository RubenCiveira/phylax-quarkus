package net.civeira.phylax.features.oauth.profile.infrastructure.driver.html;

import java.util.Locale;

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
import net.civeira.phylax.common.value.YamlLocaleMessages;
import net.civeira.phylax.features.oauth.authentication.application.SessionManager;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.OidcCookieManager;
import net.civeira.phylax.features.oauth.profile.application.ProfileService;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfile;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfileData;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.ChangePasswordPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.MfaPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.ProfileEditPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.ProfileViewPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.SessionsPanel;
import net.civeira.phylax.features.oauth.theme.domain.gateway.DecoratePageGateway;

@Path("")
@RequestScoped
@RequiredArgsConstructor
public class ProfileHtmlController {
  private static final String TEXT_HTML = "text/html";

  private final SessionManager sessionManager;
  private final ProfileService profileService;
  private final DecoratePageGateway decorator;
  private final ProfileViewPanel viewPanel;
  private final ProfileEditPanel editPanel;
  private final ChangePasswordPanel changePasswordPanel;
  private final MfaPanel mfaPanel;
  private final SessionsPanel sessionsPanel;

  @GET
  @Path("oauth/openid/{tenant}/profile")
  @Produces(TEXT_HTML)
  public Response viewProfile(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    return me(tenant, cookie, headers);
  }

  @GET
  @Path("oauth/openid/{tenant}/me")
  @Produces(TEXT_HTML)
  public Response me(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      OidcProfile profile = profileService.getProfile(session.getUserId()).orElse(null);
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">" + viewPanel.render(profile, base + "/edit",
          base + "/password", base + "/mfa", base + "/sessions", t) + "</div>";
      return page(tenant, headers, t.get("profile.view.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @GET
  @Path("oauth/openid/{tenant}/me/edit")
  @Produces(TEXT_HTML)
  public Response edit(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      OidcProfile profile = profileService.getProfile(session.getUserId()).orElse(null);
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">"
          + editPanel.render(profile, base + "/edit", base, null, t) + "</div>";
      return page(tenant, headers, t.get("profile.edit.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/edit")
  public Response saveEdit(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie,
      MultivaluedMap<String, String> form) {
    return sessionManager.loadSession(cookie).map(session -> {
      OidcProfileData data = OidcProfileData.builder().givenName(first(form, "givenName"))
          .familyName(first(form, "familyName")).middleName(first(form, "middleName"))
          .nickname(first(form, "nickname")).preferredUsername(first(form, "preferredUsername"))
          .pictureUrl(first(form, "pictureUrl")).websiteUrl(first(form, "websiteUrl"))
          .gender(first(form, "gender")).birthdate(first(form, "birthdate"))
          .zoneinfo(first(form, "zoneinfo")).locale(first(form, "locale"))
          .phoneNumber(first(form, "phoneNumber")).build();
      profileService.saveProfile(session.getUserId(), data);
      return Response.status(302).header("Location", "/oauth/openid/" + tenant + "/me").build();
    }).orElseGet(() -> Response.status(401).build());
  }

  @GET
  @Path("oauth/openid/{tenant}/me/password")
  @Produces(TEXT_HTML)
  public Response password(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(_ -> {
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">"
          + changePasswordPanel.render(base + "/password", base, null, false, t) + "</div>";
      return page(tenant, headers, t.get("profile.password.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/password")
  @Produces(TEXT_HTML)
  public Response doPassword(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers,
      MultivaluedMap<String, String> form) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String base = "/oauth/openid/" + tenant + "/me";
      String newPass = first(form, "newPassword");
      String confirmPass = first(form, "confirmPassword");
      if (!newPass.equals(confirmPass)) {
        String html =
            "<div class=\"section-card\">" + changePasswordPanel.render(base + "/password", base,
                t.get("profile.password.errorMismatch"), false, t) + "</div>";
        return Response.status(422).entity(
            decorator.getFullPage(tenant, t.get("profile.password.title"), html, locale, "full"))
            .type(TEXT_HTML).build();
      }
      boolean ok = profileService.changePassword(session.getUserId(), tenant,
          first(form, "currentPassword"), newPass);
      String html = "<div class=\"section-card\">" + changePasswordPanel.render(base + "/password",
          base, ok ? null : t.get("profile.password.errorGeneric"), ok, t) + "</div>";
      return page(tenant, headers, t.get("profile.password.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @GET
  @Path("oauth/openid/{tenant}/me/mfa")
  @Produces(TEXT_HTML)
  public Response mfa(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      boolean enabled = profileService.isMfaEnabled(session.getUserId(), tenant);
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">" + mfaPanel.render(enabled, base + "/mfa", base,
          enabled ? null : profileService.buildMfaSetup(session.getUserId(), tenant), null, false,
          t) + "</div>";
      return page(tenant, headers, t.get("profile.mfa.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/mfa")
  @Produces(TEXT_HTML)
  public Response doMfa(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers,
      MultivaluedMap<String, String> form) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String base = "/oauth/openid/" + tenant + "/me";
      String action = first(form, "action");
      if ("disable".equals(action)) {
        profileService.disableMfa(session.getUserId(), tenant);
      } else {
        profileService.enableMfa(session.getUserId(), tenant, first(form, "seed"),
            first(form, "otp"));
      }
      boolean enabled = profileService.isMfaEnabled(session.getUserId(), tenant);
      String html = "<div class=\"section-card\">" + mfaPanel.render(enabled, base + "/mfa", base,
          enabled ? null : profileService.buildMfaSetup(session.getUserId(), tenant), null, true, t)
          + "</div>";
      return page(tenant, headers, t.get("profile.mfa.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @GET
  @Path("oauth/openid/{tenant}/me/sessions")
  @Produces(TEXT_HTML)
  public Response sessions(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String base = "/oauth/openid/" + tenant + "/me";
      String html = sessionsPanel.render(profileService.listSessions(session.getUserId()),
          base + "/sessions/revoke", base, cookie, t);
      return page(tenant, headers, t.get("profile.sessions.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/sessions/revoke/{sessionId}")
  public Response revokeSession(@PathParam("tenant") String tenant,
      @PathParam("sessionId") String sessionId,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie) {
    return sessionManager.loadSession(cookie).map(_ -> {
      if (sessionId != null && !sessionId.isBlank() && !sessionId.equals(cookie)) {
        profileService.revokeSession(sessionId);
      }
      return Response.status(302).header("Location", "/oauth/openid/" + tenant + "/me/sessions")
          .build();
    }).orElseGet(() -> Response.status(401).build());
  }

  private Response page(String tenant, HttpHeaders headers, String title, String html,
      Locale locale) {
    return Response.ok(decorator.getFullPage(tenant, title, html, locale, "full")).type(TEXT_HTML)
        .build();
  }

  private Response unauthenticated(String tenant, HttpHeaders headers, Locale locale,
      YamlLocaleMessages t) {
    String loginUrl = "/oauth/openid/" + tenant + "/authorize";
    String html = "<div class=\"section-card\">" + "<h1>" + t.get("profile.auth.title") + "</h1>"
        + "<p>" + t.get("profile.auth.help") + "</p>" + "<p><a class=\"primary-button\" href=\""
        + loginUrl + "\">" + t.get("profile.auth.login") + "</a></p>" + "</div>";
    return Response.status(401)
        .entity(decorator.getFullPage(tenant, t.get("profile.view.title"), html, locale, "full"))
        .type(TEXT_HTML).build();
  }

  private Locale resolveLocale(HttpHeaders headers) {
    return headers.getAcceptableLanguages().isEmpty() ? Locale.ENGLISH
        : headers.getAcceptableLanguages().get(0);
  }

  private YamlLocaleMessages messages(Locale locale) {
    return YamlLocaleMessages.load("messages/oauth", locale);
  }

  private String first(MultivaluedMap<String, String> form, String key) {
    String value = form.getFirst(key);
    return value == null ? "" : value;
  }
}
