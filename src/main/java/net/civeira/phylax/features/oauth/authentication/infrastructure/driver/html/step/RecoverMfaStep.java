package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step;

import java.util.Optional;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.access.user.domain.gateway.UserFilter;
import net.civeira.phylax.features.access.user.domain.gateway.UserReadRepositoryGateway;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationChallenge;
import net.civeira.phylax.features.oauth.authentication.domain.ChallengesState;
import net.civeira.phylax.features.oauth.authentication.domain.exception.AuthenticationException;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.AuthorizeHtml;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.OidcStep;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.StepInput;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.StepOutcome;
import net.civeira.phylax.features.oauth.mfa.application.MfaRecoveryService;
import net.civeira.phylax.features.oauth.theme.domain.gateway.DecoratePageGateway;

/**
 * OIDC step for MFA recovery using single-use backup codes.
 *
 * Accessible from the MFA challenge form via the "Lost your device?" link. A valid recovery code
 * satisfies the MFA challenge and continues the flow.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class RecoverMfaStep implements OidcStep {

  private final SecureHtmlBuilder securer;
  private final DecoratePageGateway decorator;
  private final MfaRecoveryService mfaRecoveryService;
  private final UserReadRepositoryGateway users;

  @Override
  public AuthenticationChallenge challenge() {
    return null;
  }

  @Override
  public Class<? extends AuthenticationException> challengeException() {
    return null;
  }

  @Override
  public Set<String> stepNames() {
    return Set.of("recover-mfa");
  }

  @Override
  public Set<String> initialStepNames() {
    return Set.of("show-recover-mfa");
  }

  @Override
  public Optional<Response> paintInitial(StepInput input, String error) {
    return Optional.of(buildForm(input, error));
  }

  @Override
  public Response paint(StepInput input, NewCookie preSession) {
    throw new IllegalStateException("RecoverMfaStep is not triggered by a challenge exception");
  }

  @Override
  public Optional<StepOutcome> process(StepInput input) {
    if (!"recover-mfa".equals(input.step()) || input.currentUser().isEmpty()) {
      return Optional.empty();
    }
    String username = input.currentUser().get();
    String rawCode = AuthorizeHtml.first(input.getFormParams(), "recovery_code");

    String userUid = resolveUid(username);
    boolean valid = mfaRecoveryService.consumeCode(userUid, rawCode);

    if (valid) {
      ChallengesState state =
          input.getChallenges().orElseGet(() -> ChallengesState.empty(username));
      return Optional.of(
          new StepOutcome.Proceed(username, input.getClientDetails(), input.getRequest(), state));
    } else {
      String msg = AuthorizeHtml.i18n(input.locale(), "mfa.recovery.invalid");
      return Optional.of(new StepOutcome.Render(buildForm(input, msg)));
    }
  }

  private Response buildForm(StepInput input, String msg) {
    String js = securer.configureScripts(securer.addSign("sign"), securer.focusOn("recovery_code"));

    String title = AuthorizeHtml.i18n(input.locale(), "mfa.recovery.title");
    String error = AuthorizeHtml.i18n(input.locale(), "mfa.recovery.error-format", msg);
    String help = AuthorizeHtml.i18n(input.locale(), "mfa.recovery.help");
    String codeLabel = AuthorizeHtml.i18n(input.locale(), "mfa.recovery.code");
    String send = AuthorizeHtml.i18n(input.locale(), "mfa.recovery.send");
    String backLabel = AuthorizeHtml.i18n(input.locale(), "mfa.recovery.back-label");
    String backText = AuthorizeHtml.i18n(input.locale(), "mfa.recovery.back-text",
        "<input class=\"inline\" type=\"submit\" value=\"" + backLabel + "\" />");

    ResponseBuilder builder = Response.ok(decorator.getFullPage(input.tenant(), "MFA Recovery", js
        + "<h1>" + title + "</h1>" + "<p>" + help + "</p>"
        + (null == msg ? "" : "<p class=\"error\">" + error + "</p>") + "<form method=\"POST\">"
        + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />" + "<label>" + codeLabel
        + "<input type=\"text\" name=\"recovery_code\" id=\"recovery_code\" value=\"\" autocomplete=\"off\" /></label>"
        + "<input type=\"hidden\" name=\"step\" value=\"recover-mfa\" />"
        + "<input class=\"primary-button action-button\" type=\"submit\" value=\"" + send + "\" />"
        + "</form>" + "<form method=\"POST\">"
        + "<input type=\"hidden\" name=\"step\" value=\"mfa\" />" + "<p>" + backText + "</p>"
        + "</form>", input.locale())).type(AuthorizeHtml.TEXT_HTML);

    return securer.secureHtmlResponse(builder);
  }

  private String resolveUid(String username) {
    return users.find(UserFilter.builder().nameOrEmail(username).build()).map(u -> u.getUid())
        .orElse(username);
  }
}
