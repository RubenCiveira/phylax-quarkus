package net.civeira.phylax.testing.oauth.flow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class RefreshTokenTest extends OidcIntegrationTestBase {

  @Test
  void refreshToken_valid_returnsNewToken() {
    Response auth = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD,
        "openid profile");
    String refresh = auth.jsonPath().getString("refresh_token");

    Response response = client.refreshToken(OidcTestFixtures.TENANT, refresh,
        OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET);

    Assertions.assertEquals(200, response.statusCode());
    JsonPath json = response.jsonPath();
    Assertions.assertNotNull(json.getString("access_token"));
  }

  @Test
  void refreshToken_invalid_returns401() {
    Response response = client.refreshToken(OidcTestFixtures.TENANT, "not-a-valid-token",
        OidcTestFixtures.CLIENT_ID, OidcTestFixtures.CLIENT_SECRET);

    Assertions.assertEquals(401, response.statusCode());
  }
}
