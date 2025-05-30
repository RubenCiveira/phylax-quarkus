/* @autogenerated */
package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.part;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.application.interaction.command.RecoverPasswordCommand;
import net.civeira.phylax.features.oauth.authentication.application.spi.DecoratePageSpi;
import net.civeira.phylax.features.oauth.authentication.application.spi.RecoverPasswordSpi;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.FrontAcessController;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.FrontAcessController.StepResult;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.SecureHtmlBuilder.EncrytFieldTransfer;
import net.civeira.phylax.features.oauth.client.domain.model.ClientDetails;
import net.civeira.phylax.features.oauth.token.domain.JwtTokenManager;

@RequestScoped
@RequiredArgsConstructor
public class RecoverControllerPart {
  private final SecureHtmlBuilder securer;
  private final DecoratePageSpi decorator;
  private final RecoverPasswordSpi recoverApi;
  private final JwtTokenManager tokenManager;

  public boolean allowRecover(AuthRequest request) {
    return recoverApi.allowRecover(request);
  }

  public Response doPaintWaitRecover(Locale locale, String msg, String username,
      String recoverCode) {
    String js = securer.configureScripts(securer.addSign("sign"),
        securer.cypher(
            Arrays
                .asList(EncrytFieldTransfer.builder().from("type_password").to("password").build()),
            "recover"),
        securer.focusOn("password"));
    // entry point of GET from public url
    return securer.secureHtmlResponse(Response
        .ok(decorator.getFullPage("Recover",
            js + "<h1>Code for Recover</h1>" + (null == msg ? "" : "<p>Error: " + msg + "</p>")
                + "<form id=\"recover\" method=\"POST\">"
                + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />"
                + "<label>The username: <input type=\"text\" name=\"username\" value=\"" + username
                + "\" /></label>" + "<label>The code: <input type=\"text\" name=\"code\" value=\""
                + recoverCode + "\" /></label>"
                + "<label>New pass: <input type=\"password\" id=\"type_password\" value=\""
                + "\" /></label>" + ""
                + "<input type=\"hidden\" id=\"password\" name=\"password\" value=\"" + "\" />"
                + "<input type=\"submit\" />" + "</form>",
            locale))
        .type(FrontAcessController.TEXT_HTML));
  }

  public Response doExecFinal(ClientDetails clientDetails, AuthRequest request, String username,
      MultivaluedMap<String, String> paramMap, Function<StepResult, Response> resolver) {
    // entry point of POST from doPaintWaitRecover
    String pass = securer.decrypt(FrontAcessController.first(paramMap, "password"));
    String recoverCode = FrontAcessController.first(paramMap, "recovercode");
    boolean checkRecoberCode = recoverApi.checkRecoverCode(request, username, RecoverPasswordCommand
        .builder().code(FrontAcessController.first(paramMap, "code")).password(pass).build());
    if (checkRecoberCode) {
      return resolver.apply(StepResult.builder().username(username).clientDetails(clientDetails)
          .request(request).build());
    } else {
      return doPaintWaitRecover(request.getLocale(), "Wrong code", username, recoverCode);
    }
  }

  public Optional<Response> process(String step, Optional<String> oUser,
      ClientDetails clientDetails, AuthRequest request, MultivaluedMap<String, String> paramMap,
      Function<StepResult, Response> resolver) {
    if ("send-recover".equals(step)) {
      return Optional.of(doExecSendeRecover(clientDetails, request, paramMap));
    } else {
      return Optional.empty();
    }
  }

  public Response doPaintRecoverForm(Locale locale, String msg) {
    String js = securer.configureScripts(securer.addSign("sign"), securer.focusOn("username"));

    String title = FrontAcessController.i18n(locale, "recover.title");
    String error = FrontAcessController.i18n(locale, "recover.error-format", msg);

    String help = FrontAcessController.i18n(locale, "recover.help");
    String email = FrontAcessController.i18n(locale, "recover.email");
    String send = FrontAcessController.i18n(locale, "recover.send");

    String backLabel = FrontAcessController.i18n(locale, "recover.back-label");
    String backText = FrontAcessController.i18n(locale, "recover.back-text",
        "<input class=\"inline\" type=\"submit\" value=\"" + backLabel + "\" />");

    return securer.secureHtmlResponse(Response.ok(decorator.getFullPage("Recover",
        js + "<h1>" + title + "</h1>" + "<p>" + help + "</p>"
            + (null == msg ? "" : "<p class=\"error\">" + error + "</p>") + "<form method=\"POST\">"
            + "<input type=\"hidden\" name=\"csid\" id=\"sign\" />" + "<label>" + email
            + "<input type=\"text\" name=\"username\" id=\"username\" value=\"" + "\" /></label>"
            + "<input type=\"hidden\" name=\"step\" value=\"send-recover\" />"
            + "<input class=\"primary-button action-button\" type=\"submit\" value=\"" + send
            + "\" />" + "</form>" + "<form method=\"POST\">"
            + "<input class=\"inline\" type=\"hidden\" name=\"step\" />" + "<p>" + backText + "</p>"
            + "</form>",
        locale)).type(FrontAcessController.TEXT_HTML));
  }

  private Response doExecSendeRecover(ClientDetails clientDetails, AuthRequest request,
      MultivaluedMap<String, String> paramMap) {
    if (recoverApi.allowRecover(request)) {
      String username = FrontAcessController.first(paramMap, "username");
      String url = tokenManager.getIssuer(request.getTenant()) + "/recover" + "?username="
          + URLEncoder.encode(username, StandardCharsets.UTF_8) + "&client_id="
          + URLEncoder.encode(clientDetails.getClientId(), StandardCharsets.UTF_8)
          + request.encodeInUrl() + "&recovercode=";
      recoverApi.recover(request, username, url);
      return securer.secureRedirectResponse(
          Response.status(302).location(FrontAcessController.buildUrl(url)));
    } else {
      return doPaintRecoverForm(request.getLocale(), "No se ha podido guardar");
    }
  }

}
