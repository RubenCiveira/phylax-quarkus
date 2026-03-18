package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.part;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import net.civeira.phylax.features.oauth.client.application.ClientScopeConsentUsecase;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;

/**
 * HTML controller part for client scope consent.
 *
 * Responsibilities: - Render scope consent form with pending scopes. - Store accepted scopes for a
 * client.
 *
 * Design notes: - Uses ClientScopeConsentUsecase for consent data. - Uses SecureHtmlBuilder for
 * CSID signing.
 */
@RequestScoped
@RequiredArgsConstructor
public class ScopeConsentControllerPart {
  /**
   * Helper to build secure HTML responses.
   */
  private final SecureHtmlBuilder securer;
  /**
   * Page decorator for HTML layout and localization.
   */
  private final DecoratePageGateway decorator;
  /**
   * Use case for client scope consent handling.
   */
  private final ClientScopeConsentUsecase clientScopeConsentUsecase;

  /**
   * Returns the challenge type for client scope consent.
   *
   * Used by flow controllers to route scope consent steps. Indicates the client consent challenge.
   *
   * @return client consent challenge identifier
   */
  public AuthenticationChallege getChallenge() {
    return AuthenticationChallege.CLIENT_CONSENT;
  }

  /**
   * Renders the scope consent form with a session cookie.
   *
   * Builds the consent content and attaches the pre-session cookie. Used when client scope consent
   * is required.
   *
   * @param locale locale for translations
   * @param request auth request context
   * @param username username
   * @param session pre-session cookie
   * @return HTML response with scope consent form
   */
  public Response doPaintScopeConsentForm(Locale locale, AuthRequest request, String username,
      NewCookie session) {
    return securer.secureHtmlResponse(
        doPaintScopeConsentContent(locale, request, username, null).cookie(session));
  }

  /**
   * Builds the scope consent form HTML content.
   *
   * Retrieves pending scopes and renders the consent page. Includes CSID signing and error message
   * handling.
   *
   * @param locale locale for translations
   * @param request auth request context
   * @param username username
   * @param msg optional error message
   * @return response builder with HTML content
   */
  private ResponseBuilder doPaintScopeConsentContent(Locale locale, AuthRequest request,
      String username, String msg) {
    String clientId = request.getClientId().orElse("");
    List<String> requestedScopes = parseScopes(request.getScope().orElse(""));
    List<String> pendingScopes = clientScopeConsentUsecase.getPendingScopes(request.getTenant(),
        username, clientId, requestedScopes);

    String js = securer.configureScripts(securer.addSign("sign"));

    String title = FrontAcessController.i18n(locale, "scopeconsent.title");
    String error = FrontAcessController.i18n(locale, "scopeconsent.error-format", msg);
    String help = FrontAcessController.i18n(locale, "scopeconsent.help");
    String accept = FrontAcessController.i18n(locale, "scopeconsent.accept");
    String backLabel = FrontAcessController.i18n(locale, "scopeconsent.back-label");
    String backText = FrontAcessController.i18n(locale, "scopeconsent.back-text",
        "<input class=\"inline\" type=\"submit\" value=\"" + backLabel + "\" />");

    String scopeList =
        pendingScopes.stream().map(s -> "<li>" + s + "</li>").collect(Collectors.joining());

    return Response.ok(decorator.getFullPage("scopeConsent",
        js + "<h1>" + title + "</h1>" + "<p>" + help + "</p>"
            + (null == msg ? "" : "<p class=\"error\">" + error + "</p>") + "<ul>" + scopeList
            + "</ul>" + "<form method=\"POST\">"
            + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />"
            + "<input type=\"hidden\" name=\"step\" value=\"scope_consent\" />"
            + "<input class=\"primary-button action-button\" type=\"submit\" value=\"" + accept
            + "\" />" + "</form>" + "<form method=\"POST\">"
            + "<input type=\"hidden\" name=\"step\" value=\"start\" />" + "<p>" + backText + "</p>"
            + "</form>",
        locale)).type(FrontAcessController.TEXT_HTML);
  }

  /**
   * Processes scope consent submissions.
   *
   * Stores accepted scopes and continues the flow. Returns an empty optional when the step does not
   * apply.
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
    if ("scope_consent".equals(step) && oUser.isPresent()) {
      return Optional
          .of(doExecScopeConsent(clientDetails, request, paramMap, oUser.get(), resolver));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Executes scope consent and routes to the next step.
   *
   * Stores accepted scopes for the client and user. Always continues the flow after storing
   * consent.
   *
   * @param clientDetails client details
   * @param request auth request context
   * @param paramMap form parameters
   * @param username username
   * @param resolver continuation handler
   * @return response for the next step
   */
  private Response doExecScopeConsent(ClientDetails clientDetails, AuthRequest request,
      MultivaluedMap<String, String> paramMap, String username,
      Function<StepResult, Response> resolver) {
    List<String> requestedScopes = parseScopes(request.getScope().orElse(""));
    clientScopeConsentUsecase.storeAcceptedScopes(request.getTenant(), username,
        clientDetails.getClientId(), requestedScopes);
    return resolver.apply(StepResult.builder().username(username).clientDetails(clientDetails)
        .request(request).build());
  }

  /**
   * Parses a scope string into a list of scopes.
   *
   * Splits by whitespace and removes empty values. Returns an empty list when input is blank.
   *
   * @param scope scope string
   * @return list of scopes
   */
  private List<String> parseScopes(String scope) {
    if (scope == null || scope.isBlank()) {
      return List.of();
    }
    return Arrays.stream(scope.split("\\s+")).filter(s -> !s.isBlank()).toList();
  }
}
