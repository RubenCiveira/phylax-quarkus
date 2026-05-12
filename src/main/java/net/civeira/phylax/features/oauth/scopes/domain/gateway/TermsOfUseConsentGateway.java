package net.civeira.phylax.features.oauth.scopes.domain.gateway;

import java.util.List;

import net.civeira.phylax.features.oauth.scopes.domain.TermsOfUseAcceptance;

/**
 * Domain port for terms-of-use consent management.
 *
 * <p>
 * Returns terms versions the user has not yet accepted, and persists acceptances once given.
 * </p>
 */
public interface TermsOfUseConsentGateway {

  /**
   * Returns terms-of-use versions the user has not yet accepted for the given audiences.
   *
   * @param tenant tenant identifier
   * @param username username
   * @param audiences audience identifiers scoping which terms to check
   * @return list of pending terms-of-use items
   */
  List<TermsOfUseAcceptance> getPendingConsent(String tenant, String username,
      List<String> audiences);

  /**
   * Stores the user's acceptance of the given terms version for all specified audiences.
   *
   * @param tenant tenant identifier
   * @param username username
   * @param audiences audience identifiers
   * @param consent the terms version being accepted
   */
  void storeAcceptedConsent(String tenant, String username, List<String> audiences,
      TermsOfUseAcceptance consent);
}
