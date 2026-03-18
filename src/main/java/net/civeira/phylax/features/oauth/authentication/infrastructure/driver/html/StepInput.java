package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html;

import java.util.Locale;
import java.util.Optional;

import jakarta.ws.rs.core.MultivaluedMap;
import lombok.Builder;
import lombok.Value;
import net.civeira.phylax.features.oauth.authentication.domain.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.ChallengesState;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;

/**
 * Aggregates all data that a step needs to paint or process a form.
 *
 * Replaces the individual parameters {@code step}, {@code oUser}, {@code clientDetails},
 * {@code request}, {@code paramMap} that each ControllerPart currently receives separately.
 * Constructed once per request in {@code FrontAcessController} and passed down to
 * {@code OidcStepRouter} and then to each {@link OidcStep}.
 */
@Value
@Builder(toBuilder = true)
public class StepInput {

  /** Parsed OAuth/OIDC request parameters (tenant, scopes, redirect URI, etc.). */
  AuthRequest request;

  /** Validated OAuth client details for this authorization request. */
  ClientDetails clientDetails;

  /**
   * Challenge state loaded from the {@code PRE_SESSION_ID} cookie.
   * Empty when no pre-session cookie is present (first step in the flow).
   */
  Optional<ChallengesState> challenges;

  /** Form parameters submitted with the POST request. */
  MultivaluedMap<String, String> formParams;

  // -------------------------------------------------------------------------
  // Convenience helpers
  // -------------------------------------------------------------------------

  /**
   * Returns the username from the current challenge state, if any.
   *
   * @return the username the user already authenticated as in a previous step
   */
  public Optional<String> currentUser() {
    return challenges.map(ChallengesState::getUsername);
  }

  /**
   * Returns the value of the {@code step} form parameter, or an empty string if absent.
   *
   * @return the step name submitted in the form
   */
  public String step() {
    String s = formParams.getFirst("step");
    return s != null ? s : "";
  }

  /**
   * Returns the request locale resolved by {@link AuthRequest}.
   *
   * @return the locale for rendering i18n messages
   */
  public Locale locale() {
    return request.getLocale();
  }

  /**
   * Returns the tenant identifier from the request.
   *
   * @return the tenant slug/ID
   */
  public String tenant() {
    return request.getTenant();
  }
}
