package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.html;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;

/**
 * Centralises the construction of callback URLs used in the OIDC authentication flow.
 *
 * Eliminates the duplicated {@code issuer()} private method that existed independently in both
 * {@code RecoverControllerPart} and {@code RegistrationControllerPart}. Any structural change to
 * the URL scheme now only requires editing this single class.
 */
@jakarta.enterprise.context.ApplicationScoped
@RequiredArgsConstructor
public class OidcUrlBuilder {

  private final CurrentRequest current;

  /**
   * Returns the OpenID Connect issuer base URL for the given tenant.
   *
   * @param tenant the tenant slug/ID
   * @return issuer URL, e.g. {@code https://host/oauth/openid/mytenant}
   */
  public String issuer(String tenant) {
    return current.getPublicHost() + "/oauth/openid/" + tenant;
  }

  /**
   * Builds the password-recovery callback URL.
   *
   * The URL is passed to the mailer so that the recovery link in the email points back to the
   * correct authorization flow. A {@code recovercode=} parameter is left intentionally empty at
   * the end so the caller can append the one-time code.
   *
   * @param input    the current step context
   * @param username the username requesting password recovery
   * @return the callback URL with a trailing {@code recovercode=}
   */
  public String recoverCallbackUrl(StepInput input, String username) {
    return issuer(input.tenant()) + "/recover"
        + "?username=" + encode(username)
        + "&client_id=" + encode(input.getClientDetails().getClientId())
        + input.getRequest().encodeInUrl()
        + "&recovercode=";
  }

  /**
   * Builds the user-registration callback URL.
   *
   * The URL is passed to the mailer so that the verification link in the email points back to the
   * correct authorization flow. A {@code regcode=} parameter is left intentionally empty at the
   * end so the caller can append the one-time code.
   *
   * @param input the current step context
   * @param email the email address being registered
   * @return the callback URL with a trailing {@code regcode=}
   */
  public String registerCallbackUrl(StepInput input, String email) {
    return issuer(input.tenant()) + "/register"
        + "?email=" + encode(email)
        + "&client_id=" + encode(input.getClientDetails().getClientId())
        + input.getRequest().encodeInUrl()
        + "&regcode=";
  }

  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
