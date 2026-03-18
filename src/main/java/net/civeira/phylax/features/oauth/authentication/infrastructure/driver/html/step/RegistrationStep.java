package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.ChallengesState;
import net.civeira.phylax.features.oauth.authentication.domain.exception.AuthenticationException;
import net.civeira.phylax.features.oauth.authentication.domain.gateway.DecoratePageGateway;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.AuthorizeHtml;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.OidcStep;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder.EncrytFieldTransfer;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.StepInput;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.StepOutcome;
import net.civeira.phylax.features.oauth.user.application.RegisterUserUsecase;
import net.civeira.phylax.features.oauth.user.domain.RegistrationResult;

/**
 * OIDC step for user registration.
 *
 * Handles the {@code step=do_register} form submission (submits registration request). Not
 * triggered by an authentication exception — registration is user-initiated.
 *
 * Also exposes form-rendering and verification methods for direct use by the controller's
 * registration endpoints.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class RegistrationStep implements OidcStep {

  private final SecureHtmlBuilder securer;
  private final DecoratePageGateway decorator;
  private final RegisterUserUsecase registerUserUsecase;
  private final CurrentRequest current;

  @Override
  public AuthenticationChallege challenge() {
    return null;
  }

  @Override
  public Class<? extends AuthenticationException> challengeException() {
    return null;
  }

  @Override
  public Set<String> stepNames() {
    return Set.of("do_register");
  }

  @Override
  public Response paint(StepInput input, NewCookie preSession) {
    throw new IllegalStateException("RegistrationStep is not triggered by a challenge exception");
  }

  @Override
  public Optional<StepOutcome> process(StepInput input) {
    if (!"do_register".equals(input.step())) {
      return Optional.empty();
    }
    if (!registerUserUsecase.allowRegister(input.getRequest().getTenant())) {
      return Optional.of(new StepOutcome.Render(doPaintRegisterForm(input.locale(), AuthorizeHtml
          .i18n(input.locale(), "register.error-format", "Registration not allowed"))));
    }
    String email = AuthorizeHtml.first(input.getFormParams(), "reg_email");
    String password = securer.decrypt(AuthorizeHtml.first(input.getFormParams(), "reg_password"));
    String urlBase = issuer(input.getRequest().getTenant()) + "/register" + "?email="
        + URLEncoder.encode(email, StandardCharsets.UTF_8) + "&client_id="
        + URLEncoder.encode(input.getClientDetails().getClientId(), StandardCharsets.UTF_8)
        + input.getRequest().encodeInUrl() + "&regcode=";
    RegistrationResult result = registerUserUsecase.requestForRegister(urlBase,
        input.getRequest().getTenant(), email, password);
    if (RegistrationResult.Status.OK.equals(result.getStatus())) {
      String username = result.getUsername();
      ChallengesState state =
          input.getChallenges().orElseGet(() -> ChallengesState.empty(username));
      return Optional.of(
          new StepOutcome.Proceed(username, input.getClientDetails(), input.getRequest(), state));
    } else if (RegistrationResult.Status.PENDING.equals(result.getStatus())) {
      return Optional.of(new StepOutcome.Render(doPaintPendingPage(input.locale(), email)));
    } else {
      return Optional.of(new StepOutcome.Render(doPaintRegisterForm(input.locale(),
          AuthorizeHtml.i18n(input.locale(), "register.error-cancel"))));
    }
  }

  /**
   * Returns whether registration is allowed for the given tenant.
   */
  public boolean allowRegister(String tenant) {
    return registerUserUsecase.allowRegister(tenant);
  }

  /**
   * Renders the registration form.
   */
  public Response doPaintRegisterForm(Locale locale, String msg) {
    String js =
        securer.configureScripts(securer.addSign("sign"),
            securer.cypher(Arrays.asList(
                EncrytFieldTransfer.builder().from("type_reg_password").to("reg_password").build()),
                "register"),
            securer.focusOn("reg_email"));

    String title = AuthorizeHtml.i18n(locale, "register.title");
    String error = AuthorizeHtml.i18n(locale, "register.error-format", msg);
    String help = AuthorizeHtml.i18n(locale, "register.help");
    String emailLabel = AuthorizeHtml.i18n(locale, "register.email");
    String passwordLabel = AuthorizeHtml.i18n(locale, "register.password");
    String send = AuthorizeHtml.i18n(locale, "register.send");
    String backLabel = AuthorizeHtml.i18n(locale, "register.back-label");
    String backText = AuthorizeHtml.i18n(locale, "register.back-text",
        "<input class=\"inline\" type=\"submit\" value=\"" + backLabel + "\" />");

    return securer.secureHtmlResponse(Response.ok(decorator.getFullPage("Register",
        js + "<h1>" + title + "</h1>" + "<p>" + help + "</p>"
            + (null == msg ? "" : "<p class=\"error\">" + error + "</p>")
            + "<form id=\"register\" method=\"POST\">"
            + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />"
            + "<input type=\"hidden\" name=\"step\" value=\"do_register\" />" + "<label>"
            + emailLabel
            + " <input type=\"text\" id=\"reg_email\" name=\"reg_email\" value=\"\" /></label>"
            + "<label>" + passwordLabel
            + " <input type=\"password\" id=\"type_reg_password\" value=\"\" /></label>"
            + "<input type=\"hidden\" id=\"reg_password\" name=\"reg_password\" value=\"\" />"
            + "<input class=\"primary-button action-button\" type=\"submit\" value=\"" + send
            + "\" />" + "</form>" + "<form method=\"POST\">"
            + "<input type=\"hidden\" name=\"step\" value=\"start\" />" + "<p>" + backText + "</p>"
            + "</form>",
        locale)).type(AuthorizeHtml.TEXT_HTML));
  }

  /**
   * Renders the pending registration confirmation page.
   */
  public Response doPaintPendingPage(Locale locale, String email) {
    String title = AuthorizeHtml.i18n(locale, "register.pending-title");
    String help = AuthorizeHtml.i18n(locale, "register.pending-help", email);

    return securer
        .secureHtmlResponse(
            Response
                .ok(decorator.getFullPage("Register",
                    "<h1>" + title + "</h1>" + "<p>" + help + "</p>", locale))
                .type(AuthorizeHtml.TEXT_HTML));
  }

  /**
   * Renders the registration verification form (user enters code from email).
   */
  public Response doPaintVerifyForm(Locale locale, String email, String regcode, String msg) {
    String js = securer.configureScripts(securer.addSign("sign"));

    String title = AuthorizeHtml.i18n(locale, "register-verify.title");
    String error = AuthorizeHtml.i18n(locale, "register-verify.error-format", msg);
    String help = AuthorizeHtml.i18n(locale, "register-verify.help");
    String codeLabel = AuthorizeHtml.i18n(locale, "register-verify.code");
    String send = AuthorizeHtml.i18n(locale, "register-verify.send");
    String backLabel = AuthorizeHtml.i18n(locale, "register-verify.back-label");
    String backText = AuthorizeHtml.i18n(locale, "register-verify.back-text",
        "<input class=\"inline\" type=\"submit\" value=\"" + backLabel + "\" />");

    return securer.secureHtmlResponse(Response.ok(decorator.getFullPage("Verify Registration",
        js + "<h1>" + title + "</h1>" + "<p>" + help + "</p>"
            + (null == msg ? "" : "<p class=\"error\">" + error + "</p>") + "<form method=\"POST\">"
            + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />" + "<label>" + codeLabel
            + " <input type=\"text\" name=\"regcode\" value=\"" + nullToEmpty(regcode)
            + "\" /></label>"
            + "<input class=\"primary-button action-button\" type=\"submit\" value=\"" + send
            + "\" />" + "</form>" + "<form method=\"POST\">"
            + "<input type=\"hidden\" name=\"step\" value=\"start\" />" + "<p>" + backText + "</p>"
            + "</form>",
        locale)).type(AuthorizeHtml.TEXT_HTML));
  }

  /**
   * Executes registration code verification and resumes the auth flow.
   */
  public Optional<StepOutcome> doExecVerify(StepInput input, String email) {
    String code = AuthorizeHtml.first(input.getFormParams(), "regcode");
    Optional<String> verifiedUsername =
        registerUserUsecase.verifyRegister(input.getRequest().getTenant(), code);
    if (verifiedUsername.isPresent()) {
      String username = verifiedUsername.get();
      ChallengesState state =
          input.getChallenges().orElseGet(() -> ChallengesState.empty(username));
      return Optional.of(
          new StepOutcome.Proceed(username, input.getClientDetails(), input.getRequest(), state));
    } else {
      return Optional.of(new StepOutcome.Render(doPaintVerifyForm(input.locale(), email, code,
          AuthorizeHtml.i18n(input.locale(), "register-verify.error-format", "Invalid code"))));
    }
  }

  private String issuer(String tenant) {
    return current.getPublicHost() + "/oauth/openid/" + tenant;
  }

  private String nullToEmpty(String value) {
    return value == null ? "" : value;
  }
}
