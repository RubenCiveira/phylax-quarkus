package net.civeira.phylax.features.oauth.client.domain;

import java.util.List;
import java.util.Set;

import lombok.Value;
import lombok.experimental.Accessors;

/**
 * What a tenant lets clients register for themselves through DCR (task 2.1).
 *
 * <p>
 * Read from the tenant configuration: {@code dynamic-registration-policy} ({@code disabled} or
 * absent turns DCR off; any other value turns it on, always behind an initial access token),
 * {@code dcr-allowed-grant-types}, {@code dcr-allowed-auth-methods} and
 * {@code allow-password-grant}.
 */
@Value
@Accessors(fluent = true)
public class DcrRegistrationRules {

  /**
   * Grants of a tenant that does not list its own.
   */
  public static final List<String> DEFAULT_GRANT_TYPES = ClientDetails.DEFAULT_GRANTS;
  /**
   * Auth methods the token endpoint verifies; {@code private_key_jwt} waits for task 3.2.
   */
  public static final List<String> VERIFIABLE_AUTH_METHODS =
      List.of("client_secret_basic", "client_secret_post", "none");

  /**
   * Whether the tenant accepts registrations.
   */
  boolean enabled;
  /**
   * Grants a registered client may ask for.
   */
  Set<String> grantTypes;
  /**
   * Token endpoint auth methods a registered client may ask for.
   */
  Set<String> authMethods;
  /**
   * Whether the password grant may be registered at all.
   */
  boolean passwordGrantAllowed;

  /**
   * Same rules with other grants: those of an initial access token, which prevail over the tenant
   * ones.
   *
   * @param grants grants to allow
   * @return rules with those grants
   */
  public DcrRegistrationRules withGrantTypes(Set<String> grants) {
    return new DcrRegistrationRules(enabled, Set.copyOf(grants), authMethods, passwordGrantAllowed);
  }

  /**
   * Rules of a tenant without DCR.
   *
   * @return disabled rules
   */
  public static DcrRegistrationRules disabled() {
    return new DcrRegistrationRules(false, Set.of(), Set.of(), false);
  }
}
