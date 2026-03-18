package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.ChallengesState;
import net.civeira.phylax.features.oauth.authentication.domain.exception.AuthenticationException;
import net.civeira.phylax.features.oauth.authentication.domain.exception.ClientScopeConsentRequiredException;
import net.civeira.phylax.features.oauth.authentication.domain.gateway.DecoratePageGateway;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.FrontAcessController;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.OidcStep;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.StepInput;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.StepOutcome;
import net.civeira.phylax.features.oauth.client.application.ClientScopeConsentUsecase;

/**
 * OIDC step for client scope consent.
 *
 * Handles the {@code step=scope_consent} form submission and renders the scope consent form when
 * {@link ClientScopeConsentRequiredException} is thrown by the login use case.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ScopeConsentStep implements OidcStep {

  private final SecureHtmlBuilder securer;
  private final DecoratePageGateway decorator;
  private final ClientScopeConsentUsecase clientScopeConsentUsecase;

  @Override
  public AuthenticationChallege challenge() {
    return AuthenticationChallege.CLIENT_CONSENT;
  }

  @Override
  public Class<? extends AuthenticationException> challengeException() {
    return ClientScopeConsentRequiredException.class;
  }

  @Override
  public Set<String> stepNames() {
    return Set.of("scope_consent");
  }

  @Override
  public Response paint(StepInput input, NewCookie preSession) {
    String username = input.currentUser().orElse("");
    return securer.secureHtmlResponse(buildForm(input, username, null).cookie(preSession));
  }

  @Override
  public Optional<StepOutcome> process(StepInput input) {
    if (!"scope_consent".equals(input.step()) || input.currentUser().isEmpty()) {
      return Optional.empty();
    }
    String username = input.currentUser().get();
    List<String> requestedScopes = parseScopes(input.getRequest().getScope().orElse(""));
    clientScopeConsentUsecase.storeAcceptedScopes(input.getRequest().getTenant(), username,
        input.getClientDetails().getClientId(), requestedScopes);
    ChallengesState state =
        input.getChallenges().orElseGet(() -> ChallengesState.empty(username));
    return Optional.of(new StepOutcome.Proceed(username, input.getClientDetails(),
        input.getRequest(), state));
  }

  private ResponseBuilder buildForm(StepInput input, String username, String msg) {
    String clientId = input.getRequest().getClientId().orElse("");
    List<String> requestedScopes = parseScopes(input.getRequest().getScope().orElse(""));
    List<String> pendingScopes = clientScopeConsentUsecase.getPendingScopes(
        input.getRequest().getTenant(), username, clientId, requestedScopes);

    String js = securer.configureScripts(securer.addSign("sign"));

    String title = FrontAcessController.i18n(input.locale(), "scopeconsent.title");
    String error = FrontAcessController.i18n(input.locale(), "scopeconsent.error-format", msg);
    String help = FrontAcessController.i18n(input.locale(), "scopeconsent.help");
    String accept = FrontAcessController.i18n(input.locale(), "scopeconsent.accept");
    String backLabel = FrontAcessController.i18n(input.locale(), "scopeconsent.back-label");
    String backText = FrontAcessController.i18n(input.locale(), "scopeconsent.back-text",
        "<input class=\"inline\" type=\"submit\" value=\"" + backLabel + "\" />");

    String scopeList =
        pendingScopes.stream().map(s -> "<li>" + s + "</li>").collect(Collectors.joining());

    return Response
        .ok(decorator.getFullPage("scopeConsent",
            js + "<h1>" + title + "</h1>"
                + "<p>" + help + "</p>"
                + (null == msg ? "" : "<p class=\"error\">" + error + "</p>")
                + "<ul>" + scopeList + "</ul>"
                + "<form method=\"POST\">"
                + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />"
                + "<input type=\"hidden\" name=\"step\" value=\"scope_consent\" />"
                + "<input class=\"primary-button action-button\" type=\"submit\" value=\""
                + accept + "\" />"
                + "</form>"
                + "<form method=\"POST\">"
                + "<input type=\"hidden\" name=\"step\" value=\"start\" />"
                + "<p>" + backText + "</p>"
                + "</form>",
            input.locale()))
        .type(FrontAcessController.TEXT_HTML);
  }

  private List<String> parseScopes(String scope) {
    if (scope == null || scope.isBlank()) {
      return List.of();
    }
    return Arrays.stream(scope.split("\\s+")).filter(s -> !s.isBlank()).toList();
  }
}
