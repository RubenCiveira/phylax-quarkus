package net.civeira.phylax.features.oauth.scopes.domain.gateway;

import java.util.List;
import java.util.Map;

import net.civeira.phylax.features.oauth.scopes.domain.GdprConsentPurposeItem;

/**
 * Domain port for GDPR consent management.
 *
 * <p>
 * Returns processing purposes that still need a decision from the user, and stores the decisions
 * once they are given.
 * </p>
 */
public interface GdprConsentGateway {

  /**
   * Returns the GDPR processing purposes that the user has not yet decided on.
   *
   * @param tenant tenant identifier
   * @param username username
   * @return list of pending purposes
   */
  List<GdprConsentPurposeItem> pendingPurposes(String tenant, String username);

  /**
   * Stores the user's decisions for each purpose.
   *
   * @param tenant tenant identifier
   * @param username username
   * @param decisionsByPurposeUid map of purpose uid → accepted/rejected
   * @param ipAddress client IP address for audit
   * @param userAgent client user-agent for audit
   */
  void storePurposeDecisions(String tenant, String username,
      Map<String, Boolean> decisionsByPurposeUid, String ipAddress, String userAgent);
}
