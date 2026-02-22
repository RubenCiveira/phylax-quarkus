package net.civeira.phylax.testing.oauth.flow;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationResult;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;

@Tag("oidc-flow")
@QuarkusTest
class PasswordGrantTest extends OidcIntegrationTestBase {

  @Test
  void passwordGrant_correctCredentials_returnsToken() {
    Response response = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD,
        "openid profile");

    Assertions.assertEquals(200, response.statusCode());
    JsonPath json = response.jsonPath();
    Assertions.assertNotNull(json.getString("access_token"));
    Assertions.assertNotNull(json.getString("refresh_token"));
    Assertions.assertEquals("Bearer", json.getString("token_type"));

    Map<String, Object> claims = decodeToken(json.getString("access_token"));
    Assertions.assertEquals(OidcTestFixtures.USERNAME, String.valueOf(claims.get("sub")));
  }

  @Test
  void passwordGrant_wrongCredentials_returns401() {
    loginGateway.whenValidate(() -> AuthenticationResult.wrongCredential(OidcTestFixtures.TENANT,
        OidcTestFixtures.USERNAME));

    Response response = client.passwordGrant(OidcTestFixtures.TENANT, OidcTestFixtures.CLIENT_ID,
        OidcTestFixtures.CLIENT_SECRET, OidcTestFixtures.USERNAME, OidcTestFixtures.WRONG_PASSWORD,
        "openid profile");

    Assertions.assertEquals(401, response.statusCode());
  }

  @Test
  void passwordGrant_unknownClient_returns401() {
    Response response = client.passwordGrantWithClientId(OidcTestFixtures.TENANT, "unknown-client",
        OidcTestFixtures.USERNAME, OidcTestFixtures.PASSWORD, "openid profile");

    Assertions.assertEquals(401, response.statusCode());
  }
}
