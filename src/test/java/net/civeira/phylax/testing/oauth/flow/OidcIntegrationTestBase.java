package net.civeira.phylax.testing.oauth.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html.FrontAcessController.Challenge;
import net.civeira.phylax.features.oauth.key.domain.gateway.TokenSigner;
import net.civeira.phylax.features.oauth.token.domain.JwtTokenBuilder;
import net.civeira.phylax.testing.oauth.alt.FixedClientStoreGateway;
import net.civeira.phylax.testing.oauth.alt.FixedSessionStore;
import net.civeira.phylax.testing.oauth.alt.FixedTemporalKeysGateway;
import net.civeira.phylax.testing.oauth.client.OidcFlowClient;
import net.civeira.phylax.testing.oauth.fixtures.OidcTestFixtures;
import net.civeira.phylax.testing.oauth.scenario.ScenarioChangePasswordGateway;
import net.civeira.phylax.testing.oauth.scenario.ScenarioConsentGateway;
import net.civeira.phylax.testing.oauth.scenario.ScenarioLoginGateway;
import net.civeira.phylax.testing.oauth.scenario.ScenarioMfaGateway;

@QuarkusTest
public abstract class OidcIntegrationTestBase {
  protected final OidcFlowClient client = new OidcFlowClient();

  @Inject
  FixedSessionStore sessionStore;
  @Inject
  FixedTemporalKeysGateway temporalKeysGateway;
  @Inject
  FixedClientStoreGateway clientStoreGateway;
  @Inject
  ScenarioLoginGateway loginGateway;
  @Inject
  ScenarioConsentGateway consentGateway;
  @Inject
  ScenarioMfaGateway mfaGateway;
  @Inject
  ScenarioChangePasswordGateway changePasswordGateway;
  @Inject
  TokenSigner tokenSigner;
  @Inject
  JwtTokenBuilder tokenBuilder;

  @BeforeEach
  void resetState() {
    sessionStore.clear();
    temporalKeysGateway.clear();
    clientStoreGateway.reset();
    loginGateway.reset();
    consentGateway.reset();
    mfaGateway.reset();
    changePasswordGateway.reset();
  }

  protected Map<String, Object> decodeToken(String token) {
    return tokenSigner.verifyTokenPayload(OidcTestFixtures.TENANT, token);
  }

  protected List<String> claimAsList(Object value) {
    if (value == null) {
      return List.of();
    }
    if (value instanceof List<?>) {
      List<?> list = (List<?>) value;
      List<String> result = new ArrayList<>();
      for (Object item : list) {
        result.add(String.valueOf(item));
      }
      return result;
    }
    if (value instanceof String[]) {
      return List.of((String[]) value);
    }
    String str = String.valueOf(value);
    return str.isBlank() ? List.of() : List.of(str);
  }

  protected void assertTokenScopes(String token, List<String> expected) {
    Map<String, Object> payload = decodeToken(token);
    Assertions.assertFalse(payload.isEmpty());
    List<String> scopes = claimAsList(payload.get("scope"));
    Assertions.assertEquals(expected, scopes);
  }

  protected Challenge decodeChallenge(String cookie) {
    Optional<Challenge> challenge =
        tokenBuilder.verifyChalleger(Challenge.class, cookie, OidcTestFixtures.TENANT);
    Assertions.assertTrue(challenge.isPresent());
    return challenge.get();
  }
}
