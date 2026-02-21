package net.civeira.phylax.testing.oauth.flow;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class AuthCodeExchangeTest extends OidcIntegrationTestBase {

  @Test
  void authCode_exchange_returnsToken() {
    Response login = client.submitLogin(OidcTestFixtures.TENANT, OidcTestFixtures.USERNAME,
        OidcTestFixtures.PASSWORD, null);
    String code = client.extractAuthCode(login);
    Assertions.assertNotNull(code);

    Response exchange = client.exchangeCode(OidcTestFixtures.TENANT, code,
        OidcTestFixtures.REDIRECT_URI, OidcTestFixtures.CLIENT_ID);

    Assertions.assertEquals(200, exchange.statusCode());
    JsonPath json = exchange.jsonPath();
    Assertions.assertNotNull(json.getString("access_token"));
    Assertions.assertNotNull(json.getString("refresh_token"));
    Assertions.assertNotNull(json.getString("id_token"));

    Map<String, Object> claims = decodeToken(json.getString("id_token"));
    Assertions.assertEquals(OidcTestFixtures.USERNAME, String.valueOf(claims.get("sub")));
    Assertions.assertEquals(OidcTestFixtures.CLIENT_ID, String.valueOf(claims.get("azp")));
  }
}
