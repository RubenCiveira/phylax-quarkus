package net.civeira.phylax.features.oauth.client.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import net.civeira.phylax.features.oauth.client.domain.exception.DynamicClientException;

/**
 * Validates and normalizes the metadata of a DCR request (RFC 7591 §2, task 2.1).
 *
 * <p>
 * Only metadata that the server enforces is accepted, so a registered client never believes it has
 * something it does not:
 * <ul>
 * <li>{@code grant_types} (default {@code authorization_code}): known grants allowed by the tenant
 * (or by the initial access token); {@code password} also needs {@code allow-password-grant};</li>
 * <li>{@code response_types} (default {@code code} when the client has authorization_code, none
 * otherwise): only {@code code}, which needs the authorization_code grant;</li>
 * <li>{@code token_endpoint_auth_method} (default {@code client_secret_basic}): allowed by the
 * tenant and verified by the token endpoint; a public client ({@code none}) cannot ask for
 * client_credentials nor password;</li>
 * <li>{@code redirect_uris} (required with authorization_code) and
 * {@code post_logout_redirect_uris}: absolute, without fragment or wildcard, HTTPS, HTTP only on
 * loopback, or a private-use scheme of a native app (RFC 8252 §7.1);</li>
 * <li>logout and information URIs: absolute HTTPS (HTTP only on loopback).</li>
 * </ul>
 */
public final class DcrMetadataPolicy {

  /**
   * Grants the server can issue to a registered client.
   */
  static final Set<String> KNOWN_GRANTS = Set.of("authorization_code", "refresh_token",
      "client_credentials", "password", "urn:ietf:params:oauth:grant-type:device_code");
  private static final String CODE_GRANT = "authorization_code";
  private static final String PASSWORD_GRANT = "password";
  private static final String CLIENT_CREDENTIALS = "client_credentials";
  private static final String NONE = "none";
  private static final int MAX_URI_LENGTH = 255;

  private DcrMetadataPolicy() {}

  /**
   * Validates a request and fills in the RFC 7591 defaults.
   *
   * @param request registration or update request
   * @param rules what the tenant (or the initial access token) allows
   * @return the normalized request to store
   * @throws DynamicClientException invalid_client_metadata or invalid_redirect_uri
   */
  public static DynamicClientRequest validate(DynamicClientRequest request,
      DcrRegistrationRules rules) {
    List<String> grants = distinct(request.getGrantTypes(), List.of(CODE_GRANT));
    for (String grant : grants) {
      if (!KNOWN_GRANTS.contains(grant)) {
        throw DynamicClientException.invalidClientMetadata("Unsupported grant type: " + grant);
      }
      if (!rules.grantTypes().contains(grant)
          || PASSWORD_GRANT.equals(grant) && !rules.passwordGrantAllowed()) {
        throw DynamicClientException.invalidClientMetadata("Grant type not allowed: " + grant);
      }
    }
    boolean code = grants.contains(CODE_GRANT);
    List<String> responseTypes =
        distinct(request.getResponseTypes(), code ? List.of("code") : List.of());
    for (String responseType : responseTypes) {
      if (!"code".equals(responseType)) {
        throw DynamicClientException
            .invalidClientMetadata("Unsupported response type: " + responseType);
      }
      if (!code) {
        throw DynamicClientException
            .invalidClientMetadata("response type code needs the authorization_code grant");
      }
    }
    String method = request.getTokenEndpointAuthMethod() == null ? "client_secret_basic"
        : request.getTokenEndpointAuthMethod();
    if (!DcrRegistrationRules.VERIFIABLE_AUTH_METHODS.contains(method)
        || !rules.authMethods().contains(method)) {
      throw DynamicClientException.invalidClientMetadata("Auth method not allowed: " + method);
    }
    if (NONE.equals(method)
        && (grants.contains(CLIENT_CREDENTIALS) || grants.contains(PASSWORD_GRANT))) {
      throw DynamicClientException
          .invalidClientMetadata("A public client cannot use client_credentials nor password");
    }
    List<String> redirects = distinct(request.getRedirectUris(), List.of());
    if (code && redirects.isEmpty()) {
      throw DynamicClientException.invalidRedirectUri("redirect_uris is required");
    }
    redirects.forEach(DcrMetadataPolicy::requireRedirect);
    List<String> postLogout = distinct(request.getPostLogoutRedirectUris(), List.of());
    postLogout.forEach(DcrMetadataPolicy::requireRedirect);
    requireWebUri("backchannel_logout_uri", request.getBackchannelLogoutUri());
    requireWebUri("frontchannel_logout_uri", request.getFrontchannelLogoutUri());
    requireWebUri("client_uri", request.getClientUri());
    requireWebUri("logo_uri", request.getLogoUri());
    requireWebUri("policy_uri", request.getPolicyUri());
    requireWebUri("tos_uri", request.getTosUri());
    if (request.getClientName() != null && request.getClientName().length() > MAX_URI_LENGTH) {
      throw DynamicClientException.invalidClientMetadata("client_name is too long");
    }
    return request.toBuilder().grantTypes(grants).responseTypes(responseTypes)
        .tokenEndpointAuthMethod(method).redirectUris(redirects).postLogoutRedirectUris(postLogout)
        .build();
  }

  private static List<String> distinct(List<String> values, List<String> fallback) {
    if (values == null) {
      return fallback;
    }
    Set<String> result = new LinkedHashSet<>();
    for (String value : values) {
      if (value == null || value.isBlank()) {
        throw DynamicClientException.invalidClientMetadata("Blank value in a list");
      }
      result.add(value.trim());
    }
    return new ArrayList<>(result);
  }

  /**
   * A redirect URI a client may register: exact, so no fragment and no wildcard.
   */
  private static void requireRedirect(String value) {
    URI uri = parse(value).orElseThrow(() -> DynamicClientException.invalidRedirectUri(value));
    String scheme = uri.getScheme().toLowerCase(Locale.ROOT);
    boolean allowed = switch (scheme) {
      case "https" -> uri.getHost() != null;
      case "http" -> isLoopback(uri.getHost());
      case "javascript", "data", "vbscript", "file" -> false;
      // Private-use scheme of a native app: reverse domain name (RFC 8252 §7.1).
      default -> scheme.contains(".");
    };
    if (!allowed || value.contains("*") || uri.getRawFragment() != null
        || value.length() > MAX_URI_LENGTH) {
      throw DynamicClientException.invalidRedirectUri(value);
    }
  }

  private static void requireWebUri(String field, String value) {
    if (value == null) {
      return;
    }
    boolean allowed =
        parse(value).filter(uri -> uri.getHost() != null && uri.getRawFragment() == null)
            .map(uri -> "https".equalsIgnoreCase(uri.getScheme())
                || "http".equalsIgnoreCase(uri.getScheme()) && isLoopback(uri.getHost()))
            .orElse(false);
    if (!allowed || value.length() > MAX_URI_LENGTH) {
      throw DynamicClientException.invalidClientMetadata("Invalid " + field);
    }
  }

  private static Optional<URI> parse(String value) {
    try {
      URI uri = new URI(value);
      return uri.isAbsolute() ? Optional.of(uri) : Optional.empty();
    } catch (URISyntaxException ex) {
      return Optional.empty();
    }
  }

  private static boolean isLoopback(String host) {
    return "localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host) || "[::1]".equals(host);
  }
}
