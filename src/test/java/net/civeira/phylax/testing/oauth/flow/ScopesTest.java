package net.civeira.phylax.testing.oauth.flow;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class ScopesTest extends OidcIntegrationTestBase {

  @Test
  void passwordGrant_requestedScopesPresentInToken() {
    Response response = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD,
        "openid profile");

    Assertions.assertEquals(200, response.statusCode());
    String accessToken = response.jsonPath().getString("access_token");
    assertTokenScopes(accessToken, List.of("openid", "profile"));
  }

  @Test
  void passwordGrant_scopeFilteredByClientConfig() {
    clientStoreGateway.setAllowedScopes(List.of("openid", "profile"));

    Response response = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD,
        "openid profile email");

    Assertions.assertEquals(200, response.statusCode());
    String accessToken = response.jsonPath().getString("access_token");
    assertTokenScopes(accessToken, List.of("openid", "profile"));
  }

  @Test
  void passwordGrant_wildcardClient_allScopesGranted() {
    clientStoreGateway.setAllowedScopes(List.of("*"));

    Response response = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD,
        "openid profile email custom.scope");

    Assertions.assertEquals(200, response.statusCode());
    String accessToken = response.jsonPath().getString("access_token");
    assertTokenScopes(accessToken, List.of("openid", "profile", "email", "custom.scope"));
  }

  @Test
  void refreshToken_preservesScopes() {
    Response response = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD,
        "openid profile");
    JsonPath json = response.jsonPath();
    String refresh = json.getString("refresh_token");

    Response refreshResponse = client.refreshTokenWithScope(OidcTestFixtures.TENANT, refresh,
        OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET, "openid profile");

    Assertions.assertEquals(200, refreshResponse.statusCode());
    String accessToken = refreshResponse.jsonPath().getString("access_token");
    assertTokenScopes(accessToken, List.of("openid", "profile"));
  }

  @Test
  void authCodeFlow_scopesFromRequest() {
    Response start = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String code = client.extractAuthCode(start);
    Assertions.assertNotNull(code);

    Response exchange = client.exchangeCode(OidcTestFixtures.TENANT, code,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.CLIENT_ID);

    Assertions.assertEquals(200, exchange.statusCode());
    String accessToken = exchange.jsonPath().getString("access_token");
    assertTokenScopes(accessToken, List.of("openid", "profile", "email"));
  }
}
