package net.civeira.phylax.features.oauth.authentication.application;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthRequest;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationChallege;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationResult;
import net.civeira.phylax.features.oauth.client.domain.model.ClientDetails;
import net.civeira.phylax.features.oauth.user.application.LoginUsecase;

/**
 * Application service that orchestrates user authentication within an OIDC flow. Provides the two
 * main authentication entry-points:
 * <ul>
 * <li>{@link #authenticate} — full credential check (username + password)</li>
 * <li>{@link #preAuthenticate} — re-authentication of an already-identified user (e.g. after MFA,
 * consent, or password-change step)</li>
 * </ul>
 */
@ApplicationScoped
@RequiredArgsConstructor
public class AuthenticateUser {

  private final LoginUsecase loginUsecase;

  /**
   * Authenticates a user with username and password credentials.
   *
   * @param request the current OIDC auth request context
   * @param challenges challenges already satisfied in this flow (e.g. MFA_DONE)
   * @param username the submitted username
   * @param password the submitted (decrypted) password
   * @param client the OAuth client being used
   * @return the authentication result (right = success, left = failure with exception)
   */
  public AuthenticationResult authenticate(AuthRequest request,
      List<AuthenticationChallege> challenges, String username, String password,
      ClientDetails client) {
    return loginUsecase.validatedUserData(request, username, password, client, challenges);
  }

  /**
   * Pre-authenticates a user that has already been identified (e.g. after MFA or consent).
   *
   * @param request the current OIDC auth request context
   * @param challenges challenges already satisfied in this flow
   * @param username the already-identified username
   * @param client the OAuth client being used
   * @return the authentication result
   */
  public AuthenticationResult preAuthenticate(AuthRequest request,
      List<AuthenticationChallege> challenges, String username, ClientDetails client) {
    return loginUsecase.fillPreAuthenticated(request, username, client, challenges);
  }
}
