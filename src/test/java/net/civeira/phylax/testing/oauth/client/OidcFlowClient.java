package net.civeira.phylax.testing.oauth.client;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

public class OidcFlowClient {
  public Response startAuthFlow(String tenant, String clientId, String redirect, String scope) {
    return RestAssured.given().redirects().follow(false).header("Accept-Language", "es-ES")
        .queryParam("client_id", clientId).queryParam("redirect_uri", redirect)
        .queryParam("response_type", "code").queryParam("scope", scope)
        .queryParam("state", OidcTestFixtures.STATE)
        .queryParam("code_challenge", OidcTestFixtures.CODE_CHALLENGE)
        .get("/oauth/openid/" + tenant + "/auth");
  }

  public Response startAuthFlowWithSession(String tenant, String clientId, String redirect,
      String scope, String authSessionCookie) {
    return RestAssured.given().redirects().follow(false).header("Accept-Language", "es-ES")
        .queryParam("client_id", clientId).queryParam("redirect_uri", redirect)
        .queryParam("response_type", "code").queryParam("scope", scope)
        .queryParam("state", OidcTestFixtures.STATE)
        .queryParam("code_challenge", OidcTestFixtures.CODE_CHALLENGE)
        .cookie("AUTH_SESSION_ID", nullToEmpty(authSessionCookie))
        .get("/oauth/openid/" + tenant + "/auth");
  }

  public Response submitLogin(String tenant, String username, String password,
      String preSessionCookie) {
    return baseAuthPost(tenant).contentType(ContentType.URLENC).formParam("username", username)
        .formParam("password", password).formParam("csid", OidcTestFixtures.CSID)
        .cookie("PRE_SESSION_ID", nullToEmpty(preSessionCookie))
        .post("/oauth/openid/" + tenant + "/auth");
  }

  public Response submitSessionConfirm(String tenant, String authSessionCookie) {
    return baseAuthPost(tenant).contentType(ContentType.URLENC)
        .formParam("csid", OidcTestFixtures.CSID)
        .cookie("AUTH_SESSION_ID", nullToEmpty(authSessionCookie))
        .post("/oauth/openid/" + tenant + "/auth");
  }

  public Response submitMfa(String tenant, String mfaCode, String preSessionCookie) {
    return baseAuthPost(tenant).contentType(ContentType.URLENC).formParam("step", "mfa")
        .formParam("mfa_code", mfaCode).formParam("csid", OidcTestFixtures.CSID)
        .cookie("PRE_SESSION_ID", nullToEmpty(preSessionCookie))
        .post("/oauth/openid/" + tenant + "/auth");
  }

  public Response submitConsent(String tenant, String consentValue, String preSessionCookie) {
    return baseAuthPost(tenant).contentType(ContentType.URLENC).formParam("step", "consent")
        .formParam("consent", consentValue).formParam("csid", OidcTestFixtures.CSID)
        .cookie("PRE_SESSION_ID", nullToEmpty(preSessionCookie))
        .post("/oauth/openid/" + tenant + "/auth");
  }

  public Response submitNewPass(String tenant, String oldPass, String newPass,
      String preSessionCookie) {
    return baseAuthPost(tenant).contentType(ContentType.URLENC).formParam("step", "new_pass")
        .formParam("old_pass", oldPass).formParam("new_pass", newPass)
        .formParam("csid", OidcTestFixtures.CSID)
        .cookie("PRE_SESSION_ID", nullToEmpty(preSessionCookie))
        .post("/oauth/openid/" + tenant + "/auth");
  }

  public Response submitNewMfaVerify(String tenant, String mfaCode, String preSessionCookie) {
    return baseAuthPost(tenant).contentType(ContentType.URLENC).formParam("step", "valid_new_mfa")
        .formParam("otp_code", mfaCode).formParam("csid", OidcTestFixtures.CSID)
        .cookie("PRE_SESSION_ID", nullToEmpty(preSessionCookie))
        .post("/oauth/openid/" + tenant + "/auth");
  }

  public Response showRecoverForm(String tenant) {
    return baseAuthPost(tenant).contentType(ContentType.URLENC).formParam("step", "show-recover")
        .post("/oauth/openid/" + tenant + "/auth");
  }

  public Response requestRecover(String tenant, String username) {
    return baseAuthPost(tenant).contentType(ContentType.URLENC).formParam("step", "send-recover")
        .formParam("username", username).post("/oauth/openid/" + tenant + "/auth");
  }

  public Response fetchMfaSetup(String tenant, String preSessionCookie) {
    return RestAssured.given().redirects().follow(false).header("Accept-Language", "es-ES")
        .cookie("PRE_SESSION_ID", nullToEmpty(preSessionCookie))
        .get("/oauth/openid/" + tenant + "/mfa-setup");
  }

  public String extractAuthCode(Response response) {
    String location = response.getHeader("Location");
    if (location == null || !location.contains("code=")) {
      return null;
    }
    String query = location.contains("?") ? location.substring(location.indexOf('?') + 1) : "";
    for (String part : query.split("&")) {
      if (part.startsWith("code=")) {
        return part.substring("code=".length());
      }
    }
    return null;
  }

  public String extractPreSessionCookie(Response response) {
    String cookie = response.getCookie("PRE_SESSION_ID");
    return emptyToNull(cookie);
  }

  public String extractAuthSessionCookie(Response response) {
    String cookie = response.getCookie("AUTH_SESSION_ID");
    return emptyToNull(cookie);
  }

  public Response exchangeCode(String tenant, String code, String redirectUri, String clientId) {
    return RestAssured.given().redirects().follow(false).contentType(ContentType.URLENC)
        .formParam("grant_type", "authorization_code").formParam("code", code)
        .formParam("code_verifier", OidcTestFixtures.CODE_CHALLENGE)
        .formParam("redirect_uri", redirectUri).formParam("client_id", clientId)
        .post("/oauth/openid/" + tenant + "/token");
  }

  public Response passwordGrant(String tenant, String clientId, String secret, String username,
      String password, String scope) {
    String basic = Base64.getEncoder()
        .encodeToString((clientId + ":" + secret).getBytes(StandardCharsets.UTF_8));
    return RestAssured.given().redirects().follow(false).contentType(ContentType.URLENC)
        .header("Authorization", "Basic " + basic).formParam("grant_type", "password")
        .formParam("username", username).formParam("password", password).formParam("scope", scope)
        .post("/oauth/openid/" + tenant + "/token");
  }

  public Response passwordGrantWithClientId(String tenant, String clientId, String username,
      String password, String scope) {
    return RestAssured.given().redirects().follow(false).contentType(ContentType.URLENC)
        .formParam("grant_type", "password").formParam("client_id", clientId)
        .formParam("username", username).formParam("password", password).formParam("scope", scope)
        .post("/oauth/openid/" + tenant + "/token");
  }

  public Response refreshToken(String tenant, String refreshToken, String clientId, String secret) {
    String basic = Base64.getEncoder()
        .encodeToString((clientId + ":" + secret).getBytes(StandardCharsets.UTF_8));
    return RestAssured.given().redirects().follow(false).contentType(ContentType.URLENC)
        .header("Authorization", "Basic " + basic).formParam("grant_type", "refresh_token")
        .formParam("refresh_token", refreshToken).post("/oauth/openid/" + tenant + "/token");
  }

  public Response refreshTokenWithScope(String tenant, String refreshToken, String clientId,
      String secret, String scope) {
    String basic = Base64.getEncoder()
        .encodeToString((clientId + ":" + secret).getBytes(StandardCharsets.UTF_8));
    return RestAssured.given().redirects().follow(false).contentType(ContentType.URLENC)
        .header("Authorization", "Basic " + basic).formParam("grant_type", "refresh_token")
        .formParam("refresh_token", refreshToken).formParam("scope", scope)
        .post("/oauth/openid/" + tenant + "/token");
  }

  public Response showRecoverWithCode(String tenant, String username, String recoverCode) {
    return RestAssured.given().redirects().follow(false).header("Accept-Language", "es-ES")
        .queryParam("client_id", OidcTestFixtures.CLIENT_ID)
        .queryParam("redirect_uri", OidcTestFixtures.REDIRECT_URI)
        .queryParam("response_type", "code").queryParam("state", OidcTestFixtures.STATE)
        .queryParam("code_challenge", OidcTestFixtures.CODE_CHALLENGE)
        .queryParam("username", username).queryParam("recovercode", recoverCode)
        .get("/oauth/openid/" + tenant + "/recover");
  }

  public Response submitRecover(String tenant, String username, String recoverCode,
      String newPassword) {
    return RestAssured.given().redirects().follow(false).contentType(ContentType.URLENC)
        .queryParam("client_id", OidcTestFixtures.CLIENT_ID)
        .queryParam("redirect_uri", OidcTestFixtures.REDIRECT_URI)
        .queryParam("response_type", "code").queryParam("state", OidcTestFixtures.STATE)
        .queryParam("code_challenge", OidcTestFixtures.CODE_CHALLENGE)
        .queryParam("username", username).formParam("recovercode", recoverCode)
        .formParam("code", recoverCode).formParam("password", newPassword)
        .formParam("csid", OidcTestFixtures.CSID).post("/oauth/openid/" + tenant + "/recover");
  }

  private io.restassured.specification.RequestSpecification baseAuthPost(String tenant) {
    return RestAssured.given().redirects().follow(false).header("Accept-Language", "es-ES")
        .queryParam("client_id", OidcTestFixtures.CLIENT_ID)
        .queryParam("redirect_uri", OidcTestFixtures.REDIRECT_URI)
        .queryParam("response_type", "code").queryParam("scope", OidcTestFixtures.SCOPE)
        .queryParam("state", OidcTestFixtures.STATE)
        .queryParam("code_challenge", OidcTestFixtures.CODE_CHALLENGE);
  }

  private String nullToEmpty(String value) {
    return Optional.ofNullable(value).orElse("");
  }

  private String emptyToNull(String value) {
    return value == null || value.isBlank() ? null : value;
  }
}
