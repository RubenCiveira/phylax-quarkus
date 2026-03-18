package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html;

import java.util.Optional;
import java.util.Set;

import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.exception.AuthenticationException;

/**
 * Common contract for all steps in the OIDC authentication flow.
 *
 * Replaces the inconsistent {@code process()} signatures across the {@code *ControllerPart} classes
 * and the hardcoded dispatch table inside {@code AuthorizeHtml.doExecStep()}.
 *
 * Each implementation handles one logical step of the flow (MFA, consent, recover, etc.) and
 * self-declares which form step names it owns and which authentication challenge it satisfies.
 * {@link OidcStepRouter} discovers all implementations via CDI {@code Instance<OidcStep>} and
 * routes requests to the right step without any hardcoded if/else.
 *
 * <p>
 * <b>CDI scope:</b> implementations must be {@code @ApplicationScoped} (or {@code @Dependent}). All
 * per-request data travels through {@link StepInput} — no mutable instance state is allowed.
 */
public interface OidcStep {

  /**
   * The {@link AuthenticationChallege} enum value that this step satisfies.
   *
   * Used by {@link OidcStepRouter} to build the challenge → step mapping so that when
   * {@code fillPreAuthenticated()} throws a challenge-related exception, the router knows which
   * step to render.
   *
   * @return the challenge this step satisfies, or {@code null} if this step is not associated with
   *         any authentication challenge (e.g. recover password, registration)
   */
  AuthenticationChallege challenge();

  /**
   * The exception class that triggers the {@link #paint} of this step.
   *
   * When {@code fillPreAuthenticated()} throws this exception, {@link OidcStepRouter#paint} will
   * delegate to this step's {@link #paint} method.
   *
   * @return the exception class, or {@code null} if this step is never triggered by an exception
   */
  default Class<? extends AuthenticationException> challengeException() {
    return null;
  }

  /**
   * The set of {@code step} form-parameter values this step can handle.
   *
   * A POST to the authorize endpoint with {@code step=mfa} will be routed to the step that returns
   * {@code Set.of("mfa")} here. Steps that expose a "show" page and a "process" page (e.g. recover)
   * declare both names: {@code Set.of("show-recover", "send-recover")}.
   *
   * @return non-null, non-empty set of step names
   */
  Set<String> stepNames();

  /**
   * Renders the initial HTML form for this step.
   *
   * Called when an authentication challenge requires this step to be shown, or when a form POST
   * with a {@code show-xxx} step name is received.
   *
   * @param input the current request context
   * @param preSession the pre-session cookie already built by the controller, ready to attach to
   *        the response
   * @return the JAX-RS response containing the rendered HTML
   */
  Response paint(StepInput input, NewCookie preSession);

  /**
   * Processes a form submission for this step.
   *
   * @param input the current request context including form parameters
   * @return {@code Optional.empty()} if this step does not handle the current {@code step} value;
   *         {@code Optional.of(Render(...))} if there is a validation error and the form must be
   *         redisplayed; {@code Optional.of(Proceed(...))} if the step completed successfully and
   *         the flow may continue
   */
  Optional<StepOutcome> process(StepInput input);
}
