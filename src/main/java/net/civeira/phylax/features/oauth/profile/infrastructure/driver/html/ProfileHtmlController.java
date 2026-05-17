package net.civeira.phylax.features.oauth.profile.infrastructure.driver.html;

import java.util.List;
import java.util.Locale;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.value.YamlLocaleMessages;
import net.civeira.phylax.features.access.user.domain.User;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserReadRepositoryGateway;
import net.civeira.phylax.features.oauth.authentication.application.SessionManager;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.OidcCookieManager;
import net.civeira.phylax.features.oauth.consent.domain.ClientConsentSummary;
import net.civeira.phylax.features.oauth.consent.domain.gateway.ScopesConsentGateway;
import net.civeira.phylax.features.oauth.gdpr.application.GdprService;
import net.civeira.phylax.features.oauth.mfa.application.MfaRecoveryService;
import net.civeira.phylax.features.oauth.profile.application.ProfileService;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfile;
import net.civeira.phylax.features.oauth.profile.domain.OidcProfileData;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.AccountDeletionPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.ChangePasswordPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.ConsentedScopesPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.DataExportPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.MfaPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.PasskeysPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.ProfileEditPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.ProfileViewPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.RecoveryCodesPanel;
import net.civeira.phylax.features.oauth.profile.infrastructure.driver.html.panels.SessionsPanel;
import net.civeira.phylax.features.oauth.session.domain.SessionInfo;
import net.civeira.phylax.features.oauth.theme.domain.gateway.DecoratePageGateway;

@Path("")
@RequestScoped
@RequiredArgsConstructor
public class ProfileHtmlController {
  private static final String TEXT_HTML = "text/html";

  private final SessionManager sessionManager;
  private final ProfileService profileService;
  private final UserReadRepositoryGateway users;
  private final DecoratePageGateway decorator;
  private final ProfileViewPanel viewPanel;
  private final ProfileEditPanel editPanel;
  private final ChangePasswordPanel changePasswordPanel;
  private final MfaPanel mfaPanel;
  private final SessionsPanel sessionsPanel;
  private final MfaRecoveryService mfaRecoveryService;
  private final RecoveryCodesPanel recoveryCodesPanel;
  private final ScopesConsentGateway scopesConsentGateway;
  private final ConsentedScopesPanel consentedScopesPanel;
  private final PasskeysPanel passkeysPanel;
  private final GdprService gdprService;
  private final DataExportPanel dataExportPanel;
  private final AccountDeletionPanel accountDeletionPanel;

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
      OidcProfile profile = profileService.getProfile(resolveUserUid(session)).orElse(null);
      String base = "/oauth/openid/" + tenant + "/me";
      String html =
          "<div class=\"section-card\">"
              + viewPanel.render(profile, base + "/edit", base + "/password", base + "/mfa",
                  base + "/sessions", base + "/recovery-codes", base + "/consents",
                  base + "/passkeys", base + "/data-export", base + "/delete-account", t)
              + "</div>";
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
      OidcProfile profile = profileService.getProfile(resolveUserUid(session)).orElse(null);
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
      profileService.saveProfile(resolveUserUid(session), data);
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
      boolean ok = profileService.changePassword(resolveUserUid(session), tenant,
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
      boolean enabled = profileService.isMfaEnabled(resolveUserUid(session), tenant);
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">" + mfaPanel.render(enabled, base + "/mfa", base,
          enabled ? null : profileService.buildMfaSetup(resolveUserUid(session), tenant), null,
          false, t) + "</div>";
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
        profileService.disableMfa(resolveUserUid(session), tenant);
      } else {
        profileService.enableMfa(resolveUserUid(session), tenant, first(form, "seed"),
            first(form, "otp"));
      }
      boolean enabled = profileService.isMfaEnabled(resolveUserUid(session), tenant);
      String html = "<div class=\"section-card\">" + mfaPanel.render(enabled, base + "/mfa", base,
          enabled ? null : profileService.buildMfaSetup(resolveUserUid(session), tenant), null,
          true, t) + "</div>";
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
      String html = sessionsPanel.render(
          profileService.listSessions(resolveUserUid(session),
              session.getValidationData().getUsername()),
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

  @GET
  @Path("oauth/openid/{tenant}/me/recovery-codes")
  @Produces(TEXT_HTML)
  public Response recoveryCodes(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String base = "/oauth/openid/" + tenant + "/me";
      long unusedCount = mfaRecoveryService.countUnused(resolveUserUid(session));
      String html = "<div class=\"section-card\">" + recoveryCodesPanel.render(unusedCount,
          base + "/recovery-codes/regenerate", base, null, t) + "</div>";
      return page(tenant, headers, t.get("profile.recovery-codes.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/recovery-codes/regenerate")
  @Produces(TEXT_HTML)
  public Response regenerateRecoveryCodes(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String base = "/oauth/openid/" + tenant + "/me";
      String userUid = resolveUserUid(session);
      List<String> codes = mfaRecoveryService.generateCodes(userUid, 8);
      long unusedCount = mfaRecoveryService.countUnused(userUid);
      String html = "<div class=\"section-card\">" + recoveryCodesPanel.render(unusedCount,
          base + "/recovery-codes/regenerate", base, codes, t) + "</div>";
      return page(tenant, headers, t.get("profile.recovery-codes.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @GET
  @Path("oauth/openid/{tenant}/me/consents")
  @Produces(TEXT_HTML)
  public Response consents(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String username = session.getValidationData().getUsername();
      String base = "/oauth/openid/" + tenant + "/me";
      List<ClientConsentSummary> granted = scopesConsentGateway.listGrantedByUser(tenant, username);
      String html = "<div class=\"section-card\">"
          + consentedScopesPanel.render(granted, base + "/consents/revoke", base, t) + "</div>";
      return page(tenant, headers, t.get("profile.consents.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/consents/revoke/{clientUid}")
  public Response revokeConsent(@PathParam("tenant") String tenant,
      @PathParam("clientUid") String clientUid,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie) {
    return sessionManager.loadSession(cookie).map(session -> {
      String username = session.getValidationData().getUsername();
      if (clientUid != null && !clientUid.isBlank()) {
        scopesConsentGateway.revokeClientConsent(tenant, username, clientUid);
      }
      return Response.status(302).header("Location", "/oauth/openid/" + tenant + "/me/consents")
          .build();
    }).orElseGet(() -> Response.status(401).build());
  }

  @GET
  @Path("oauth/openid/{tenant}/me/passkeys")
  @Produces(TEXT_HTML)
  public Response passkeys(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String userUid = resolveUserUid(session);
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">"
          + passkeysPanel.render(profileService.listPasskeys(userUid, tenant), base + "/passkeys",
              base + "/passkeys", base, null, t)
          + "</div>";
      return page(tenant, headers, t.get("profile.passkeys.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/passkeys/{credentialId}/rename")
  public Response renamePasskey(@PathParam("tenant") String tenant,
      @PathParam("credentialId") String credentialId,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie,
      MultivaluedMap<String, String> form) {
    return sessionManager.loadSession(cookie).map(session -> {
      String newName = first(form, "name");
      if (credentialId != null && !credentialId.isBlank() && !newName.isBlank()) {
        profileService.renamePasskey(credentialId, resolveUserUid(session), tenant, newName);
      }
      return Response.status(302).header("Location", "/oauth/openid/" + tenant + "/me/passkeys")
          .build();
    }).orElseGet(() -> Response.status(401).build());
  }

  @POST
  @Path("oauth/openid/{tenant}/me/passkeys/{credentialId}/delete")
  public Response deletePasskey(@PathParam("tenant") String tenant,
      @PathParam("credentialId") String credentialId,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie) {
    return sessionManager.loadSession(cookie).map(session -> {
      if (credentialId != null && !credentialId.isBlank()) {
        profileService.deletePasskey(credentialId, resolveUserUid(session), tenant);
      }
      return Response.status(302).header("Location", "/oauth/openid/" + tenant + "/me/passkeys")
          .build();
    }).orElseGet(() -> Response.status(401).build());
  }

  @GET
  @Path("oauth/openid/{tenant}/me/data-export")
  @Produces(TEXT_HTML)
  public Response dataExport(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(_ -> {
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">"
          + dataExportPanel.render(base + "/data-export", base, false, t) + "</div>";
      return page(tenant, headers, t.get("profile.data-export.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/data-export")
  @Produces(TEXT_HTML)
  public Response requestDataExport(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String userUid = resolveUserUid(session);
      String username = session.getValidationData().getUsername();
      User user = users.find(UserFilter.builder().uid(userUid).build()).orElse(null);
      String email = user != null ? user.getEmail().orElse(null) : null;
      if (email != null && !email.isBlank()) {
        gdprService.requestDataExport(userUid, username, email, tenant);
      }
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">"
          + dataExportPanel.render(base + "/data-export", base, true, t) + "</div>";
      return page(tenant, headers, t.get("profile.data-export.sentTitle"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @GET
  @Path("oauth/openid/{tenant}/me/delete-account")
  @Produces(TEXT_HTML)
  public Response deleteAccount(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(_ -> {
      String base = "/oauth/openid/" + tenant + "/me";
      String html = "<div class=\"section-card\">"
          + accountDeletionPanel.renderRequest(base + "/delete-account", base, null, t) + "</div>";
      return page(tenant, headers, t.get("profile.delete-account.title"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @POST
  @Path("oauth/openid/{tenant}/me/delete-account")
  @Produces(TEXT_HTML)
  public Response requestDeleteAccount(@PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String cookie, @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    return sessionManager.loadSession(cookie).map(session -> {
      String userUid = resolveUserUid(session);
      String username = session.getValidationData().getUsername();
      User user = users.find(UserFilter.builder().uid(userUid).build()).orElse(null);
      String email = user != null ? user.getEmail().orElse(null) : null;
      String base = "/oauth/openid/" + tenant + "/me";
      if (email != null && !email.isBlank()) {
        String confirmUrl = "/oauth/openid/" + tenant + "/me/delete-account/confirm";
        gdprService.requestAccountDeletion(userUid, username, email, tenant, confirmUrl);
      }
      String html =
          "<div class=\"section-card\">" + accountDeletionPanel.renderSent(base, t) + "</div>";
      return page(tenant, headers, t.get("profile.delete-account.sentTitle"), html, locale);
    }).orElseGet(() -> unauthenticated(tenant, headers, locale, t));
  }

  @GET
  @Path("oauth/openid/{tenant}/me/delete-account/confirm")
  @Produces(TEXT_HTML)
  public Response confirmDeleteAccount(@PathParam("tenant") String tenant,
      @QueryParam("uid") String uid, @QueryParam("code") String code,
      @Context HttpHeaders headers) {
    Locale locale = resolveLocale(headers);
    YamlLocaleMessages t = messages(locale);
    String base = "/oauth/openid/" + tenant + "/me";
    if (uid == null || uid.isBlank() || code == null || code.isBlank()) {
      String html = "<div class=\"section-card\">" + accountDeletionPanel.renderTokenError(base, t)
          + "</div>";
      return Response.status(400).entity(decorator.getFullPage(tenant,
          t.get("profile.delete-account.title"), html, locale, "full")).type(TEXT_HTML).build();
    }
    boolean deleted = gdprService.confirmAccountDeletion(uid, code, tenant);
    String html;
    if (deleted) {
      html = "<div class=\"section-card\">" + accountDeletionPanel.renderConfirmed(t) + "</div>";
      return Response.ok(decorator.getFullPage(tenant,
          t.get("profile.delete-account.confirmedTitle"), html, locale, "full")).type(TEXT_HTML)
          .build();
    } else {
      html = "<div class=\"section-card\">" + accountDeletionPanel.renderTokenError(base, t)
          + "</div>";
      return Response.status(400).entity(decorator.getFullPage(tenant,
          t.get("profile.delete-account.title"), html, locale, "full")).type(TEXT_HTML).build();
    }
  }

  private String resolveUserUid(SessionInfo session) {
    String username = session.getValidationData().getUsername();
    return users.find(UserFilter.builder().nameOrEmail(username).build()).map(u -> u.getUid())
        .orElse(session.getUserId());
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
