package net.civeira.phylax.features.oauth.scopes.application;

import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.scopes.domain.GdprConsentPurposeItem;
import net.civeira.phylax.features.oauth.scopes.domain.gateway.GdprConsentGateway;

/**
 * Application service for GDPR consent management.
 *
 * <p>
 * Orchestrates retrieval of pending GDPR processing purposes and persistence of user decisions.
 * </p>
 */
@ApplicationScoped
@RequiredArgsConstructor
public class GdprConsentUsecase {

  private final GdprConsentGateway gateway;

  /**
   * Returns GDPR processing purposes that still require a decision from the user.
   *
   * @param tenant tenant identifier
   * @param username username
   * @return list of pending purposes
   */
  public List<GdprConsentPurposeItem> pendingPurposes(String tenant, String username) {
    return gateway.pendingPurposes(tenant, username);
  }

  /**
   * Stores the user's decisions for each GDPR purpose.
   *
   * @param tenant tenant identifier
   * @param username username
   * @param decisionsByPurposeUid map of purpose uid → accepted/rejected
   * @param ipAddress client IP for audit
   * @param userAgent client user-agent for audit
   */
  public void storePurposeDecisions(String tenant, String username,
      Map<String, Boolean> decisionsByPurposeUid, String ipAddress, String userAgent) {
    gateway.storePurposeDecisions(tenant, username, decisionsByPurposeUid, ipAddress, userAgent);
  }
}
