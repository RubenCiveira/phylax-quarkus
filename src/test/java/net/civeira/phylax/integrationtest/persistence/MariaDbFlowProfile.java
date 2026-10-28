package net.civeira.phylax.integrationtest.persistence;

import java.util.List;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * Flow tests through the real endpoints with the code, revocation and session stores in MariaDB.
 *
 * <p>
 * The in-memory doubles of those stores and of pushed requests are excluded, so the production SQL
 * adapters serve them, as they always serve device codes and DCR ({@link FlowTemporalKeysGateway}
 * only replaces the key handling of the form helpers). Login, clients and signing keep their
 * doubles, as in the other flow tests.
 */
public class MariaDbFlowProfile implements QuarkusTestProfile {

  /**
   * Build property that enables the test-only beans of this profile.
   */
  public static final String ENABLED = "phylax.test.mariadb-flow";

  @Override
  public Map<String, String> getConfigOverrides() {
    String doubles = "net.civeira.phylax.integrationtest.features.oauth.alt.";
    return Map.of(ENABLED, "true", "quarkus.arc.exclude-types",
        doubles + "FixedTemporalKeysGateway," + doubles + "FixedTokenRevocationGateway," + doubles
            + "FixedSessionStore," + doubles + "FixedTokenLookupGateway," + doubles
            + "FixedParRequestGateway");
  }

  @Override
  public List<TestResourceEntry> testResources() {
    return List.of(new TestResourceEntry(MariaDbFlowResource.class));
  }
}
