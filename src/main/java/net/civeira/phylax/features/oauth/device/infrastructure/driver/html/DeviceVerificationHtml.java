package net.civeira.phylax.features.oauth.device.infrastructure.driver.html;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.application.SessionManager;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.AuthorizeHtml;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.OidcCookieManager;
import net.civeira.phylax.features.oauth.device.application.DeviceAuthorizationService;
import net.civeira.phylax.features.oauth.device.domain.DeviceAuthorizationStatus;
import net.civeira.phylax.features.oauth.theme.domain.gateway.DecoratePageGateway;

@Path("")
@RequestScoped
@RequiredArgsConstructor
public class DeviceVerificationHtml {

  private final DeviceAuthorizationService deviceAuthorization;
  private final SessionManager sessions;
  private final DecoratePageGateway decorator;

  @GET
  @Path("oauth/openid/{tenant}/device/verify")
  public Response verify(final @PathParam("tenant") String tenant,
      @QueryParam("user_code") String userCode, @Context HttpHeaders headers) {
    var locale = headers.getAcceptableLanguages().isEmpty() ? java.util.Locale.getDefault()
        : headers.getAcceptableLanguages().get(0);
    String normalized = normalize(userCode);
    if (normalized.isEmpty()) {
      return Response
          .ok(decorator.getFullPage(tenant, AuthorizeHtml.i18n(locale, "device.title"),
              "<h1>" + AuthorizeHtml.i18n(locale, "device.title") + "</h1><p>"
                  + AuthorizeHtml.i18n(locale, "device.ask-help") + "</p>"
                  + "<form method=\"POST\"><label>" + AuthorizeHtml.i18n(locale, "device.user-code")
                  + "<input type=\"text\" name=\"user_code\" value=\"\" /></label>"
                  + "<input class=\"primary-button action-button\" type=\"submit\" value=\""
                  + AuthorizeHtml.i18n(locale, "device.continue") + "\" />" + "</form>",
              locale))
          .type("text/html").build();
    }
    var record = deviceAuthorization.findByUserCode(tenant, normalized);
    if (record == null) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.invalid-code"), locale);
    }
    if (record.isExpired()) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.expired-code"), locale);
    }
    if (record.getStatus() == DeviceAuthorizationStatus.APPROVED) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.already-approved"), locale);
    }
    if (record.getStatus() == DeviceAuthorizationStatus.DENIED) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.denied"), locale);
    }
    return message(tenant, AuthorizeHtml.i18n(locale, "device.pending-help"), locale);
  }

  @POST
  @Path("oauth/openid/{tenant}/device/verify")
  public Response formVerify(final @PathParam("tenant") String tenant,
      @CookieParam(OidcCookieManager.AUTH_SESSION_ID) String sessionId,
      final MultivaluedMap<String, String> form, @Context HttpHeaders headers) {
    var locale = headers.getAcceptableLanguages().isEmpty() ? java.util.Locale.getDefault()
        : headers.getAcceptableLanguages().get(0);
    String normalized = normalize(form.getFirst("user_code"));
    if (normalized.isEmpty()) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.code-required"), locale);
    }
    var record = deviceAuthorization.findByUserCode(tenant, normalized);
    if (record == null) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.invalid-code"), locale);
    }
    if (record.isExpired()) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.expired-code"), locale);
    }
    if (record.getStatus() != DeviceAuthorizationStatus.PENDING) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.not-pending"), locale);
    }
    if (sessionId == null || sessionId.isBlank()) {
      return message(tenant, AuthorizeHtml.i18n(locale, "device.auth-required"), locale);
    }
    return sessions.loadSession(sessionId).map(session -> {
      boolean approved =
          deviceAuthorization.approveByUserCode(tenant, normalized, session.getValidationData());
      return approved ? message(tenant, AuthorizeHtml.i18n(locale, "device.approved"), locale)
          : message(tenant, AuthorizeHtml.i18n(locale, "device.approval-failed"), locale);
    }).orElseGet(() -> message(tenant, AuthorizeHtml.i18n(locale, "device.auth-required"), locale));
  }

  private Response message(String tenant, String message, java.util.Locale locale) {
    return Response
        .ok(decorator.getFullPage(tenant, AuthorizeHtml.i18n(locale, "device.title"),
            "<h1>" + AuthorizeHtml.i18n(locale, "device.title") + "</h1><p>" + message + "</p>"
                + "<form method=\"GET\">" + "<input class=\"inline\" type=\"submit\" value=\""
                + AuthorizeHtml.i18n(locale, "device.back") + "\" />" + "</form>",
            locale))
        .type("text/html").build();
  }

  private String normalize(String value) {
    if (value == null) {
      return "";
    }
    String code = value.trim().toUpperCase().replace(" ", "").replace("-", "");
    if (code.length() == 8) {
      return code.substring(0, 4) + "-" + code.substring(4);
    }
    return code;
  }
}
