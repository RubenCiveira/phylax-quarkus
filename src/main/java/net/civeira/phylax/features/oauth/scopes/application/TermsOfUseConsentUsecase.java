package net.civeira.phylax.features.oauth.scopes.application;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.scopes.domain.TermsOfUseAcceptance;
import net.civeira.phylax.features.oauth.scopes.domain.gateway.TermsOfUseConsentGateway;

/**
 * Application service for terms-of-use consent management.
 *
 * <p>
 * Orchestrates retrieval of pending terms versions and persistence of user acceptances.
 * </p>
 */
@ApplicationScoped
@RequiredArgsConstructor
public class TermsOfUseConsentUsecase {

  private final TermsOfUseConsentGateway gateway;

  /**
   * Returns terms-of-use versions the user has not yet accepted.
   *
   * @param tenant tenant identifier
   * @param username username
   * @param audiences audiences scoping which terms to check
   * @return list of pending terms items
   */
  public List<TermsOfUseAcceptance> getPendingConsent(String tenant, String username,
      List<String> audiences) {
    return gateway.getPendingConsent(tenant, username, audiences);
  }

  /**
   * Stores the user's acceptance of a terms version for all specified audiences.
   *
   * @param tenant tenant identifier
   * @param username username
   * @param audiences audience identifiers
   * @param consent the terms version being accepted
   */
  public void storeAcceptedConsent(String tenant, String username, List<String> audiences,
      TermsOfUseAcceptance consent) {
    gateway.storeAcceptedConsent(tenant, username, audiences, consent);
  }
}
