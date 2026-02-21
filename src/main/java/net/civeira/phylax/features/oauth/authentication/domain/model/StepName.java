package net.civeira.phylax.features.oauth.authentication.domain.model;

/**
 * Enumerates all the authentication steps supported by the OIDC authorize flow.
 */
public enum StepName {
  LOGIN, CONSENT, SCOPES_CONSENT, MFA, NEW_MFA, RECOVER_PASS, NEW_PASS, REGISTER_USER, DELEGATED_LOGIN
}
