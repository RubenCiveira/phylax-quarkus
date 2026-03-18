package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

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
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.NewCookie.SameSite;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.value.YamlLocaleMessages;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.features.oauth.authentication.domain.ChallengesState;
import net.civeira.phylax.features.oauth.authentication.domain.exception.AuthenticationException;
import net.civeira.phylax.features.oauth.authentication.domain.gateway.DecoratePageGateway;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder.EncrytFieldTransfer;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step.DelegatedStep;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step.NewMfaStep;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step.RecoverStep;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.step.RegistrationStep;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;
import net.civeira.phylax.features.oauth.client.domain.gateway.ClientStoreGateway;
import net.civeira.phylax.features.oauth.delegated.application.DelegateLogin;
import net.civeira.phylax.features.oauth.delegated.domain.DelegatedAccessExternalProvider;
import net.civeira.phylax.features.oauth.delegated.domain.DelegatedProviderDescription;
import net.civeira.phylax.features.oauth.session.domain.SessionInfo;
import net.civeira.phylax.features.oauth.session.domain.TemporalAuthCode;
import net.civeira.phylax.features.oauth.session.domain.gateway.SessionStoreGateway;
import net.civeira.phylax.features.oauth.session.domain.gateway.TemporalKeysGateway;
import net.civeira.phylax.features.oauth.token.application.JwtTokenBuilder;
import net.civeira.phylax.features.oauth.token.domain.IdToken;
import net.civeira.phylax.features.oauth.user.application.LoginUsecase;

/**
 * Main HTML controller for the OAuth/OIDC authorization user interface.
 *
 * Responsibilities: - Render login, consent, MFA, and registration flows. - Manage session and
 * pre-session cookie handling.
 *
 * Design notes: - Routes form steps through {@link OidcStepRouter}. - Uses SecureHtmlBuilder for
 * signing and encryption.
 */
@Slf4j
@Path("")
@RequestScoped
@RequiredArgsConstructor
public class FrontAcessController {
  /**
   * Path parameter name for tenant identifiers.
   */
  private static final String TENANT = "tenant";
  /**
   * Parameter name used for usernames in forms.
   */
  private static final String USERNAME = "username";
  /**
   * Base OAuth path used for cookies and routing.
   */
  private static final String OAUTH_OPENID = "/oauth/openid/";
  /**
   * Cookie name for authenticated sessions.
   */
  private static final String AUTH_SESSION_ID = "AUTH_SESSION_ID";
  /**
   * Cookie name for pre-authenticated sessions.
   */
  private static final String PRE_SESSION_ID = "PRE_SESSION_ID";
  /**
   * Cached translations indexed by locale.
   */
  private static Map<Locale, YamlLocaleMessages> TRANSLATIONS = new HashMap<>();
  /**
   * HTML content type value used by this controller.
   */
  public static final String TEXT_HTML = "text/html";


  /**
   * Use case for credential validation and pre-authenticated flows.
   */
  private final LoginUsecase loginUsecase;
  /**
   * Helper to build secure HTML responses and scripts.
   */
  private final SecureHtmlBuilder securer;
  /**
   * Page decorator for HTML layout and localization.
   */
  private final DecoratePageGateway decorator;

  /**
   * Router that dispatches form steps to the correct {@link OidcStep} implementation.
   */
  private final OidcStepRouter router;

  /**
   * Step for password recovery flows (also exposes direct controller endpoints).
   */
  private final RecoverStep recoverStep;
  /**
   * Step for user registration flows (also exposes direct controller endpoints).
   */
  private final RegistrationStep registrationStep;
  /**
   * Step for new MFA enrollment (also exposes the QR-image endpoint).
   */
  private final NewMfaStep newMfaStep;
  /**
   * Step for delegated login callbacks (also exposes back-login form and token parsing).
   */
  private final DelegatedStep delegatedStep;

  /**
   * Gateway to retrieve client details.
   */
  private final ClientStoreGateway clientRetrieve;
  /**
   * Token builder used for session tokens and ID tokens.
   */
  private final JwtTokenBuilder tokenBuilder;
  /**
   * Session store gateway for authenticated sessions.
   */
  private final SessionStoreGateway sessionStore;
  /**
   * Temporal store for auth codes and CSID tokens.
   */
  private final TemporalKeysGateway temporalStore;
  /**
   * Use case for delegated login providers.
   */
  private final DelegateLogin delegateLogin;

  @ServerExceptionMapper
  @Produces(TEXT_HTML)
  /**
   * Maps server exceptions to a friendly HTML error response.
   */
  public Response showError(Exception ex, @Context HttpHeaders headers) {
    Locale locale = headers.getAcceptableLanguages().get(0);
    log.error("There was an error with son outh step", ex);
    return Response.ok(decorator.getFullPage("Error",
        "" + "<h1>There was an error</h1>"
            + "<p>Please, <a href=\"#\" click=\"window.history.back();\">go back</a> and again</p>"
            + "",
        locale)).header("Content-Type", "text/html").build();
  }

  @GET
  @Produces(TEXT_HTML)
  @Path("oauth/openid/{tenant}/me")
  /**
   * Returns the current session information as HTML.
   */
  public Response showInfo(final @PathParam(TENANT) String tenant,
      @CookieParam(AUTH_SESSION_ID) String cookie, final @Context UriInfo req,
      @Context HttpHeaders headers) {
    AuthRequest request = new AuthRequest(tenant, req, headers);
    return loadClient(request).flatMap(_ -> sessionStore.loadSession(cookie))
        .map(sessionInfo -> securer.secureHtmlResponse(Response.ok(decorator.getFullPage("Data",
            "<h1>Ficha de " + sessionInfo.getValidationData().getUsername() + "</h1>",
            request.getLocale()))))
        .orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @GET
  @Produces(TEXT_HTML)
  @Path("oauth/openid/{tenant}/delegated-auth")
  /**
   * Handles delegated login return from external providers.
   */
  public Response processDelegated(final @PathParam(TENANT) String tenant,
      @QueryParam("provider") String provider, @QueryParam("code") String code,
      final @Context UriInfo req, @Context HttpHeaders headers) {
    Locale locale = headers.getAcceptableLanguages().get(0);
    return delegatedStep.doBackLoginForm(tenant, provider, code, locale, null);
  }

  @GET
  @Path("oauth/openid/{tenant}/auth")
  /**
   * Displays the login form or validates an existing session.
   */
  public Response showForm(final @PathParam(TENANT) String tenant,
      @CookieParam(AUTH_SESSION_ID) String session, final @Context UriInfo req,
      @Context HttpHeaders headers) {
    AuthRequest request = new AuthRequest(tenant, req, headers);
    return loadClient(request).map(loadClient -> {
      return sessionStore.loadSession(session)
          .map(sessionInfo -> doPaintVerifySession(sessionInfo, loadClient, request, session))
          .orElseGet(() -> {
            return "none".equals(request.getPrompt().orElse(""))
                ? redirectError(request, "no session")
                : doPaintLoginForm(request, null, null);
          });
    }).orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @POST
  @Path("oauth/openid/{tenant}/auth")
  /**
   * Processes login form submissions and flow steps.
   */
  public Response processForm(final @PathParam(TENANT) String tenant, final @Context UriInfo req,
      @Context HttpHeaders headers, final MultivaluedMap<String, String> paramMap,
      @CookieParam(AUTH_SESSION_ID) String session, @CookieParam(PRE_SESSION_ID) String cookie) {
    AuthRequest request = new AuthRequest(tenant, req, headers);

    return loadClient(request)
        .map(loadClient -> sessionStore.loadSession(session)
            .map(sessionInfo -> doCheckSession(sessionInfo, loadClient, request, paramMap, session))
            .orElseGet(() -> doExecStep(loadClient, request, paramMap, cookie)))
        .orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @GET
  @Path("oauth/openid/{tenant}/mfa-setup")
  /**
   * Returns the MFA enrollment image when required.
   */
  public Response showMfaSelector(final @PathParam(TENANT) String tenant,
      final @Context UriInfo req, @Context HttpHeaders headers,
      @CookieParam(PRE_SESSION_ID) String cookie) {
    AuthRequest request = new AuthRequest(tenant, req, headers);

    return preSessionChallengeState(cookie, tenant).map(ChallengesState::getUsername)
        .flatMap(user -> newMfaStep.mfaImage(tenant, user))
        .orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @GET
  @Path("oauth/openid/{tenant}/recover")
  /**
   * Displays the password recovery form for a user.
   */
  public Response showRecover(final @PathParam(TENANT) String tenant, final @Context UriInfo req,
      @Context HttpHeaders headers, @QueryParam(USERNAME) String username,
      @QueryParam("recovercode") String recovercode) {
    AuthRequest request = new AuthRequest(tenant, req, headers);

    return loadClient(request)
        .map(_ -> recoverStep.doPaintWaitRecover(request.getLocale(), null, username, recovercode))
        .orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @POST
  @Path("oauth/openid/{tenant}/recover")
  /**
   * Processes recovery form submissions.
   */
  public Response checkRecover(final @PathParam(TENANT) String tenant,
      @QueryParam(USERNAME) String username, @Context UriInfo req,
      final MultivaluedMap<String, String> paramMap, @Context HttpHeaders headers,
      @CookieParam(PRE_SESSION_ID) String cookie) {
    AuthRequest request = new AuthRequest(tenant, req, headers);

    return loadClient(request).map(clientDetails -> {
      Optional<ChallengesState> challengeState = preSessionChallengeState(cookie, tenant);
      StepInput input = StepInput.builder().request(request).clientDetails(clientDetails)
          .challenges(challengeState).formParams(paramMap).build();
      return recoverStep.doExecFinal(input).map(outcome -> handleOutcome(outcome, input, paramMap))
          .orElseGet(() -> Response.status(400).build());
    }).orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @GET
  @Path("oauth/openid/{tenant}/register")
  /**
   * Displays the registration verification form.
   */
  public Response showRegister(final @PathParam(TENANT) String tenant, final @Context UriInfo req,
      @Context HttpHeaders headers, @QueryParam("email") String email,
      @QueryParam("regcode") String regcode) {
    AuthRequest request = new AuthRequest(tenant, req, headers);
    return loadClient(request)
        .map(_ -> registrationStep.doPaintVerifyForm(request.getLocale(), email, regcode, null))
        .orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @POST
  @Path("oauth/openid/{tenant}/register")
  /**
   * Processes registration verification submissions.
   */
  public Response checkRegister(final @PathParam(TENANT) String tenant,
      @QueryParam("email") String email, @Context UriInfo req,
      final MultivaluedMap<String, String> paramMap, @Context HttpHeaders headers,
      @CookieParam(PRE_SESSION_ID) String cookie) {
    AuthRequest request = new AuthRequest(tenant, req, headers);
    return loadClient(request).map(clientDetails -> {
      Optional<ChallengesState> challengeState = preSessionChallengeState(cookie, tenant);
      StepInput input = StepInput.builder().request(request).clientDetails(clientDetails)
          .challenges(challengeState).formParams(paramMap).build();
      return registrationStep.doExecVerify(input, email)
          .map(outcome -> handleOutcome(outcome, input, paramMap))
          .orElseGet(() -> Response.status(400).build());
    }).orElseGet(() -> Response.status(403, "Client not allowed.").build());
  }

  @POST
  @Path("oauth/openid/{tenant}/revocation")
  /**
   * Revokes a pre-session by deleting its cookie session.
   */
  public Response revoke(final @PathParam(TENANT) String tenant,
      @CookieParam(PRE_SESSION_ID) String cookie, final MultivaluedMap<String, String> paramMap,
      final @Context HttpHeaders headers) {
    sessionStore.deleteSession(cookie);
    return Response.ok().build();
  }

  @GET
  @Path("oauth/openid/{tenant}/logout")
  /**
   * Logs out the current session and redirects to the post-logout URL.
   */
  public Response logout(final @PathParam(TENANT) String tenant,
      @CookieParam("AUTH_SESSION_ID") String cookie,
      @QueryParam("post_logout_redirect_uri") String redirect) {
    sessionStore.deleteSession(cookie);
    String to = redirect;
    int onClean = to.indexOf('?');
    if (-1 != onClean) {
      to = to.substring(0, onClean);
    }
    return Response.status(302).cookie(new NewCookie.Builder(AUTH_SESSION_ID).value("").build())
        .location(buildUrl(to)).build();
  }

  @GET
  @Path("oauth/openid/{tenant}/login-status-iframe")
  @Produces(TEXT_HTML)
  /**
   * Returns a placeholder HTML for login status iframe checks.
   */
  public String checkSession(final @PathParam(TENANT) String tenant) {
    return "<h1>Check</h1>";
  }

  /**
   * Validates an existing session and decides next step.
   */
  private Response doCheckSession(SessionInfo sessionInfo, ClientDetails loadClient,
      AuthRequest request, final MultivaluedMap<String, String> paramMap, String cookie) {
    String prompt = request.getPrompt().orElse("");

    String csid = securer.verifyToken(first(paramMap, "csid")).orElse("-");
    if (sessionInfo.getCsid().equals(csid)) {
      return redirect(Optional.of(cookie), loadClient, sessionInfo.getGrant(), request,
          sessionInfo.getValidationData(), paramMap);
    } else if ("none".equals(prompt)) {
      return redirectError(request, "invalid client");
    } else {
      return doPaintLoginForm(request, null, null);
    }
  }

  /**
   * Renders a verification form for an existing session.
   */
  private Response doPaintVerifySession(SessionInfo sDDessionInfo, ClientDetails loadClient,
      AuthRequest request, String cookie) {
    String js = securer.configureScripts(securer.addSignAndSend("sign", "enter"));
    return securer.secureHtmlResponse(Response
        .ok(decorator.getFullPage("Login",
            js + "<form id=\"enter\" method=\"POST\">"
                + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />"
                + "<input type=\"submit\" />" + "</form>" + "",
            request.getLocale()))
        .type(TEXT_HTML));
  }

  /**
   * Renders the primary login form.
   */
  private Response doPaintLoginForm(AuthRequest request, String msg, String chagenlle) {
    Locale locale = request.getLocale();
    String js = securer.configureScripts(securer.focusOn(USERNAME), securer.addSign("sign"),
        securer.cypher(
            Arrays
                .asList(EncrytFieldTransfer.builder().from("type_password").to("password").build()),
            "login"));
    String title = i18n(locale, "login.title");
    String error = i18n(locale, "login.error-format", msg);
    String help = i18n(locale, "login.help", request.getTenant());
    String username = i18n(locale, "login.username");
    String password = i18n(locale, "login.password");
    String recoverLabel = i18n(locale, "login.recover-label");
    String recoverText = "<input class=\"inline\" type=\"submit\" value=\"" + recoverLabel + "\"/>";
    String enter = i18n(locale, "login.enter");

    StringBuilder delegatedLogins = new StringBuilder();
    StringBuilder execAuto = new StringBuilder();

    for (DelegatedAccessExternalProvider provider : delegateLogin.providers(request)) {
      DelegatedProviderDescription info = provider.info(request);
      delegatedLogins.append("                    <form method=\"POST\" id=\"social-form-"
          + info.getId() + "\" class=\"social-form\">\r\n"
          + "                        <input type=\"hidden\" name=\"step\" value=\"delegated-login\" />"
          + "                        <input type=\"hidden\" name=\"delegated-provider\" value=\""
          + info.getId() + "\" />"
          + "                        <button type=\"submit\" class=\"social-button\">\r\n"
          + "                            <img src=\"" + info.getLogo() + "\" alt=\""
          + info.getName() + "\">\r\n" + "                        </button>\r\n"
          + "                    </form>\r\n");

      if (info.isAutomatic()) {
        execAuto.append(
            "<script>" + "document.addEventListener('DOMContentLoaded', function(event) {\r\n"
                + "document.getElementById(\"social-form-" + info.getId() + "\").submit();" + "});"
                + "</script>");
      }
    }

    if (!delegatedLogins.isEmpty()) {
      delegatedLogins.append(
          " <div class=\"social-login-buttons\">\r\n" + "" + delegatedLogins + "" + "</div>");
    }

    return securer
        .secureHtmlResponse(
            Response
                .ok(decorator.getFullPage("Login",
                    js + "<h1>" + title + "</h1>" + "<p>" + help + "</p>"
                        + (null == msg ? "" : "<p class=\"error\">" + error + "</p>")
                        + "<form id=\"login\" method=\"POST\">"
                        + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />" + "<label>"
                        + username
                        + " <input type=\"text\" id=\"username\" name=\"username\" value=\""
                        + "\" /></label>" + "<label>" + password
                        + " <input type=\"password\" id=\"type_password\" value=\""
                        + "\" /></label>"
                        + "<input type=\"hidden\" id=\"password\" name=\"password\" value=\""
                        + "\" />"
                        + "<input class=\"primary-button action-button\" type=\"submit\" value=\""
                        + enter + "\" />" + "</form>" + delegatedLogins + execAuto
                        + (recoverStep.allowRecover(request.getTenant())
                            ? "<form method=\"POST\">"
                                + "<input type=\"hidden\" name=\"step\" value=\"show-recover\" />"
                                + "<p>" + recoverText + "</p></form>"
                            : "")
                        + (registrationStep.allowRegister(request.getTenant())
                            ? "<form method=\"POST\">"
                                + "<input type=\"hidden\" name=\"step\" value=\"show-register\" />"
                                + "<p>" + "<input class=\"inline\" type=\"submit\" value=\""
                                + i18n(locale, "login.register-label") + "\"/>" + "</p></form>"
                            : ""),
                    locale))
                .type(TEXT_HTML)
                .cookie(new NewCookie.Builder(AUTH_SESSION_ID).value(null).sameSite(SameSite.NONE)
                    .path(OAUTH_OPENID + request.getTenant()).secure(true).httpOnly(true).build())
                .cookie(new NewCookie.Builder(PRE_SESSION_ID).value(null).sameSite(SameSite.NONE)
                    .path(OAUTH_OPENID + request.getTenant()).secure(true).httpOnly(true).build()));
  }

  /**
   * Executes the next step in the form-based flow via the OidcStepRouter.
   */
  private Response doExecStep(ClientDetails clientDetails, AuthRequest request,
      MultivaluedMap<String, String> paramMap, String cookie) {
    String step = first(paramMap, "step");
    Optional<ChallengesState> challengeState =
        preSessionChallengeState(cookie, request.getTenant());

    // Direct steps not routed through OidcStepRouter
    if ("delegated-login".equals(step)) {
      return delegatedStep.doPaintLoginForm(request.getTenant(),
          first(paramMap, "delegated-provider"), request.getLocale(), null);
    }
    if ("show-recover".equals(step)) {
      return recoverStep.doPaintRecoverForm(request.getLocale(), null);
    }
    if ("show-register".equals(step)) {
      return registrationStep.allowRegister(request.getTenant())
          ? registrationStep.doPaintRegisterForm(request.getLocale(), null)
          : doPaintLoginForm(request, null, null);
    }
    if ("start".equals(step) && challengeState.isPresent()) {
      return doPaintLoginForm(request, null, null);
    }

    StepInput input = StepInput.builder().request(request).clientDetails(clientDetails)
        .challenges(challengeState).formParams(paramMap).build();

    Optional<StepOutcome> outcome = router.process(input);
    if (outcome.isPresent()) {
      return handleOutcome(outcome.get(), input, paramMap);
    }

    List<AuthenticationChallege> challenges =
        challengeState.map(ChallengesState::getCompleted).orElseGet(List::of);
    return doExecLogin(clientDetails, request, paramMap, challenges);
  }

  /**
   * Dispatches a {@link StepOutcome} to either render a response or continue the flow.
   */
  private Response handleOutcome(StepOutcome outcome, StepInput input,
      MultivaluedMap<String, String> paramMap) {
    return switch (outcome) {
      case StepOutcome.Render r -> r.response();
      case StepOutcome.Proceed p -> handleProceed(p, paramMap);
    };
  }

  /**
   * Continues the auth flow after a step signals proceed.
   *
   * Calls {@code fillPreAuthenticated} with the already-completed challenges. On success, redirects
   * with an auth code. On a new challenge, writes the updated pre-session cookie and paints the
   * next step form.
   */
  private Response handleProceed(StepOutcome.Proceed proceed,
      MultivaluedMap<String, String> paramMap) {
    List<AuthenticationChallege> alreadyDone = proceed.challengesState().getCompleted();
    AuthenticationResult result = loginUsecase.fillPreAuthenticated(proceed.request(),
        proceed.username(), proceed.clientDetails(), alreadyDone);

    if (result.isRight()) {
      return redirect(Optional.empty(), proceed.clientDetails(), "form", proceed.request(),
          result.getData(), paramMap);
    }

    AuthenticationException ex = result.getFail();
    Optional<AuthenticationChallege> ch = router.challengeFor(ex.getClass());
    if (ch.isEmpty()) {
      return doPaintLoginForm(proceed.request(), "Credenciales incorrectas", null);
    }
    ChallengesState next = proceed.challengesState().withCompleted(ch.get());
    NewCookie preSession = buildPreSessionCookie(next, proceed.request().getTenant());
    StepInput nextInput =
        StepInput.builder().request(proceed.request()).clientDetails(proceed.clientDetails())
            .challenges(Optional.of(next)).formParams(paramMap).build();
    return router.paint(ex.getClass(), nextInput, preSession)
        .orElseGet(() -> doPaintLoginForm(proceed.request(), "Credenciales incorrectas", null));
  }

  /**
   * Executes a username/password login and routes results.
   */
  private Response doExecLogin(ClientDetails clientDetails, AuthRequest request,
      MultivaluedMap<String, String> paramMap, List<AuthenticationChallege> challenges) {
    String grant = "form";
    String password = securer.decrypt(first(paramMap, "password"));
    AuthenticationResult authenticate = loginUsecase.validatedUserData(request,
        first(paramMap, USERNAME), password, clientDetails, challenges);

    if (authenticate.isRight()) {
      return redirect(Optional.empty(), clientDetails, grant, request, authenticate.getData(),
          paramMap);
    }

    AuthenticationException ex = authenticate.getFail();
    Optional<AuthenticationChallege> ch = router.challengeFor(ex.getClass());
    if (ch.isEmpty()) {
      return doPaintLoginForm(request, "Credenciales incorrectas", null);
    }
    String username = first(paramMap, USERNAME);
    String tenant = request.getTenant();
    ChallengesState state = ChallengesState.empty(username).withCompleted(ch.get());
    NewCookie preSession = buildPreSessionCookie(state, tenant);
    StepInput input = StepInput.builder().request(request).clientDetails(clientDetails)
        .challenges(Optional.of(state)).formParams(paramMap).build();
    return router.paint(ex.getClass(), input, preSession)
        .orElseGet(() -> doPaintLoginForm(request, "Credenciales incorrectas", null));
  }

  /**
   * Redirects to the client with an error fragment.
   */
  private Response redirectError(AuthRequest request, String message) {
    String to = request.getRedirect().orElse("") + "#error="
        + URLEncoder.encode(message, StandardCharsets.UTF_8);
    return securer.secureRedirectResponse(Response.status(302).location(buildUrl(to))
        .cookie(new NewCookie.Builder(AUTH_SESSION_ID).value(null).sameSite(SameSite.NONE)
            .path(OAUTH_OPENID + request.getTenant()).secure(true).httpOnly(true).build()));
  }

  /**
   * Redirects to the client after successful authentication.
   */
  private Response redirect(Optional<String> cookie, ClientDetails clientDetails, String grant,
      AuthRequest request, AuthenticationData validationData,
      MultivaluedMap<String, String> paramMap) {
    String uid = UUID.randomUUID().toString();
    if (cookie.isPresent()) {
      sessionStore.updateSession(uid, cookie.get());
    } else {
      String csid = securer.verifyToken(first(paramMap, "csid")).orElseThrow();
      sessionStore.saveSession(uid, clientDetails, grant, validationData, csid);
    }
    validationData.setAudiences(request.getAudiences());
    String type = request.getResponseType().orElse("code");
    String to;
    if ("code".equals(type)) {
      String redirectUri = request.getRedirect().orElseThrow();
      String separator = "?";
      if (redirectUri.endsWith("?")) {
        separator = "";
      } else if (redirectUri.contains("?")) {
        separator = "&";
      }
      String code = temporalStore.registerTemporalAuthCode(
          TemporalAuthCode.builder().client(clientDetails).request(request)
              .nonce(request.getNonce().orElse(null)).data(validationData).build());
      to = redirectUri + separator + "code=" + code + "&state=" + request.getState().orElseThrow();
    } else {
      IdToken buildIdToken =
          tokenBuilder.buildIdToken(request.getTenant(), request.getState().orElseThrow(),
              request.getNonce().orElse(null), clientDetails, grant, validationData);
      to = request.getRedirect().orElseThrow() + "#state=" + request.getState().orElseThrow()
          + "&session_state=" + buildIdToken.getSessionState() + "" + "&iss="
          + URLEncoder.encode(buildIdToken.getIss(), StandardCharsets.UTF_8) + "&id_token="
          + buildIdToken.getIdToken() + "&access_token=" + buildIdToken.getAccessToken()
          + "&token_type=" + buildIdToken.getTokenType() + "&expires_in="
          + buildIdToken.getExpiresIn();
    }
    return securer.secureRedirectResponse(Response.status(302).location(buildUrl(to))
        .cookie(new NewCookie.Builder(AUTH_SESSION_ID).value(uid).sameSite(SameSite.NONE)
            .path(OAUTH_OPENID + request.getTenant()).secure(true).httpOnly(true).build()));
  }

  /**
   * Builds a pre-session cookie carrying the current {@link ChallengesState}.
   */
  private NewCookie buildPreSessionCookie(ChallengesState state, String tenant) {
    String token = tokenBuilder.buildChallengerToken(tenant, state, Duration.ofHours(1));
    return new NewCookie.Builder(PRE_SESSION_ID).value(token).sameSite(SameSite.NONE)
        .path(OAUTH_OPENID + tenant).secure(true).httpOnly(true).build();
  }

  /**
   * Loads and verifies the pre-session cookie into a {@link ChallengesState}.
   *
   * Returns empty when the cookie is absent or invalid.
   */
  private Optional<ChallengesState> preSessionChallengeState(String cookie, String tenant) {
    if (StringUtils.isEmpty(cookie)) {
      return Optional.empty();
    }
    return tokenBuilder.verifyChalleger(ChallengesState.class, cookie, tenant);
  }

  /**
   * Loads the client details for the request.
   */
  private Optional<ClientDetails> loadClient(final AuthRequest request) {
    return clientRetrieve.loadPublic(request.getTenant(), request.getClientId().orElseThrow(),
        request.getRedirect().orElseThrow());
  }

  /**
   * Retrieves a localized message for the given key.
   */
  public static String i18n(Locale locale, String key, Object... arguments) {
    synchronized (TRANSLATIONS) {
      TRANSLATIONS.computeIfAbsent(locale, k -> YamlLocaleMessages.load("/messages/oauth", k));
    }
    return TRANSLATIONS.get(locale).get(key, arguments);
  }

  /**
   * Returns the first value for a parameter key.
   */
  public static String first(Map<String, List<String>> paramMap, String key) {
    List<String> list = paramMap.get(key);
    return null == list || list.isEmpty() ? "" : list.get(0);
  }

  /**
   * Builds a URI from a raw string.
   */
  public static URI buildUrl(String url) {
    try {
      return new URI(url);
    } catch (URISyntaxException ex) {
      throw new IllegalArgumentException("Source url " + url + " is illegal", ex);
    }
  }
}
