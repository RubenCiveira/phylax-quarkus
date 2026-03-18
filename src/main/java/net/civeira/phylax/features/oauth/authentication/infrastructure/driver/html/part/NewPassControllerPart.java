package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.part;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.gateway.DecoratePageGateway;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.FrontAcessController;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.FrontAcessController.StepResult;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder.EncrytFieldTransfer;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;
import net.civeira.phylax.features.oauth.user.application.ChangePasswordUsecase;

/**
 * HTML controller part for forced password changes.
 *
 * Responsibilities: - Render the password change form. - Validate and apply new passwords.
 *
 * Design notes: - Uses SecureHtmlBuilder to encrypt form fields. - Delegates updates to
 * ChangePasswordUsecase.
 */
@RequestScoped
@RequiredArgsConstructor
public class NewPassControllerPart {
  /**
   * Helper to build secure HTML responses.
   */
  private final SecureHtmlBuilder securer;
  /**
   * Page decorator for HTML layout and localization.
   */
  private final DecoratePageGateway decorator;
  /**
   * Use case for changing user passwords.
   */
  private final ChangePasswordUsecase changePasswordUsecase;

  /**
   * Returns the challenge type for password refresh.
   *
   * Used by flow controllers to route password-change steps. Indicates the fresh password
   * challenge.
   *
   * @return password refresh challenge identifier
   */
  public AuthenticationChallege getChallenge() {
    return AuthenticationChallege.FRESH_PASSWORD;
  }

  /**
   * Renders the password change form with a session cookie.
   *
   * Builds the form content and attaches the pre-session cookie. Used when a user must change their
   * password.
   *
   * @param locale locale for translations
   * @param session pre-session cookie
   * @return HTML response with password form
   */
  public Response doPaintNewPassForm(Locale locale, NewCookie session) {
    return securer.secureHtmlResponse(doPaintNewPassFormContent(locale, null).cookie(session));
  }

  /**
   * Builds the password change form HTML content.
   *
   * Encrypts password fields and renders error messages. Includes a signed CSID token for form
   * submission.
   *
   * @param locale locale for translations
   * @param msg optional error message
   * @return response builder with HTML content
   */
  private ResponseBuilder doPaintNewPassFormContent(Locale locale, String msg) {
    String js = securer.configureScripts(securer.addSign("sign"),
        securer.cypher(
            Arrays.asList(
                EncrytFieldTransfer.builder().from("type_old_pass").to("old_pass").build(),
                EncrytFieldTransfer.builder().from("type_new_pass").to("new_pass").build()),
            "chpass"),
        securer.focusOn("type_old_pass"));

    String title = FrontAcessController.i18n(locale, "chpass.title");
    String error = FrontAcessController.i18n(locale, "chpass.error-format", msg);

    String help = FrontAcessController.i18n(locale, "chpass.help");
    String current = FrontAcessController.i18n(locale, "chpass.current");
    String newpass = FrontAcessController.i18n(locale, "chpass.newpass");
    String send = FrontAcessController.i18n(locale, "chpass.send");

    String backLabel = FrontAcessController.i18n(locale, "chpass.back-label");
    String backText = FrontAcessController.i18n(locale, "chpass.back-text",
        "<input class=\"inline\" type=\"submit\" value=\"" + backLabel + "\" />");

    return Response.ok(decorator.getFullPage("password",
        js + "<h1>" + title + "</h1>" + "<p>" + help + "</p>"
            + (null == msg ? "" : "<p class=\"error\">" + error + "</p>")
            + "<form id=\"chpass\" method=\"POST\">"
            + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />" + "<label>" + current
            + "<input type=\"password\" id=\"type_old_pass\" value=\"" + "\" /></label>" + "<label>"
            + newpass + "<input type=\"password\" id=\"type_new_pass\" value=\"" + "\" /></label>"
            + "<input type=\"hidden\" id=\"old_pass\" name=\"old_pass\" value=\"" + "\" />"
            + "<input type=\"hidden\" id=\"new_pass\" name=\"new_pass\" value=\"" + "\" />"
            + "<input type=\"hidden\" name=\"step\" value=\"new_pass\" />"
            + "<input class=\"primary-button action-button\" type=\"submit\" value=\"" + send
            + "\" />" + "</form>" + "<form method=\"POST\">"
            + "<input type=\"hidden\" name=\"step\" value=\"start\" />" + "<p>" + backText
            + "</form>" + "",
        locale)).type(FrontAcessController.TEXT_HTML);
  }

  /**
   * Processes password change form submissions.
   *
   * Validates the new password and continues the flow. Returns an empty optional when the step does
   * not apply.
   *
   * @param step flow step identifier
   * @param oUser optional username
   * @param clientDetails client details
   * @param request auth request context
   * @param paramMap form parameters
   * @param resolver continuation handler
   * @return optional response
   */
  public Optional<Response> process(String step, Optional<String> oUser,
      ClientDetails clientDetails, AuthRequest request, MultivaluedMap<String, String> paramMap,
      Function<StepResult, Response> resolver) {
    if ("new_pass".equals(step) && oUser.isPresent()) {
      return Optional.of(doExecNewPass(clientDetails, request, paramMap, oUser.get(), resolver));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Executes password change and routes to the next step.
   *
   * Decrypts password fields and invokes the update use case. Re-renders the form when the update
   * fails.
   *
   * @param clientDetails client details
   * @param request auth request context
   * @param paramMap form parameters
   * @param username username
   * @param resolver continuation handler
   * @return response for the next step
   */
  private Response doExecNewPass(ClientDetails clientDetails, AuthRequest request,
      MultivaluedMap<String, String> paramMap, String username,
      Function<StepResult, Response> resolver) {
    Response response;
    String oldPass = securer.decrypt(FrontAcessController.first(paramMap, "old_pass"));
    String newPass = securer.decrypt(FrontAcessController.first(paramMap, "new_pass"));
    boolean updated =
        changePasswordUsecase.forceUpdatePassword(request.getTenant(), username, oldPass, newPass);
    if (updated) {
      response = resolver.apply(StepResult.builder().username(username).clientDetails(clientDetails)
          .request(request).build());
    } else {
      response = securer.secureHtmlResponse(
          doPaintNewPassFormContent(request.getLocale(), "No se ha podido guardar"));
    }
    return response;
  }
}
