package net.civeira.phylax.features.oauth.consent.domain;

/**
 * A terms-of-use version pending user acceptance.
 *
 * @param id unique identifier of the terms version
 * @param text full text of the terms to display
 */
public record TermsOfUseAcceptance(String id, String text) {
}

