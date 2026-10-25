package net.civeira.phylax.features.oauth.client.domain;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.features.oauth.client.domain.exception.DynamicClientException;

/**
 * Metadata a client may register through DCR (task 2.1).
 */
class DcrMetadataPolicyUnitTest {

  private static final DcrRegistrationRules RULES = new DcrRegistrationRules(true,
      Set.of("authorization_code", "refresh_token", "client_credentials", "password"),
      Set.copyOf(DcrRegistrationRules.VERIFIABLE_AUTH_METHODS), false);

  private static DynamicClientRequest.DynamicClientRequestBuilder web() {
    return DynamicClientRequest.builder().redirectUris(List.of("https://app.example.com/cb"));
  }

  private static String error(DynamicClientRequest request, DcrRegistrationRules rules) {
    return Assertions.assertThrows(DynamicClientException.class,
        () -> DcrMetadataPolicy.validate(request, rules)).getErrorCode();
  }

  @Test
  @DisplayName("RFC 7591 defaults: authorization_code, code and client_secret_basic")
  void defaults_are_applied() {
    DynamicClientRequest result = DcrMetadataPolicy.validate(web().build(), RULES);
    Assertions.assertEquals(List.of("authorization_code"), result.getGrantTypes());
    Assertions.assertEquals(List.of("code"), result.getResponseTypes());
    Assertions.assertEquals("client_secret_basic", result.getTokenEndpointAuthMethod());
  }

  @Test
  @DisplayName("A grant the tenant does not list is rejected")
  void grant_not_allowed_by_tenant() {
    DcrRegistrationRules codeOnly = RULES.withGrantTypes(Set.of("authorization_code"));
    Assertions.assertEquals("invalid_client_metadata",
        error(web().grantTypes(List.of("authorization_code", "refresh_token")).build(), codeOnly));
  }

  @Test
  @DisplayName("password needs allow-password-grant even when listed")
  void password_needs_explicit_permission() {
    Assertions.assertEquals("invalid_client_metadata",
        error(web().grantTypes(List.of("password")).build(), RULES));
    DcrRegistrationRules withPassword =
        new DcrRegistrationRules(true, RULES.grantTypes(), RULES.authMethods(), true);
    Assertions.assertDoesNotThrow(() -> DcrMetadataPolicy.validate(
        web().grantTypes(List.of("password")).responseTypes(List.of()).build(), withPassword));
  }

  @Test
  @DisplayName("Implicit and unknown grants or response types are rejected")
  void unsupported_values_rejected() {
    Assertions.assertEquals("invalid_client_metadata",
        error(web().grantTypes(List.of("implicit")).build(), RULES));
    Assertions.assertEquals("invalid_client_metadata",
        error(web().responseTypes(List.of("token")).build(), RULES));
  }

  @Test
  @DisplayName("A public client cannot ask for client_credentials")
  void public_client_without_client_credentials() {
    Assertions
        .assertEquals("invalid_client_metadata",
            error(
                web().tokenEndpointAuthMethod("none")
                    .grantTypes(List.of("authorization_code", "client_credentials")).build(),
                RULES));
  }

  @Test
  @DisplayName("private_key_jwt and methods the tenant does not allow are rejected")
  void auth_method_must_be_verifiable_and_allowed() {
    Assertions.assertEquals("invalid_client_metadata",
        error(web().tokenEndpointAuthMethod("private_key_jwt").build(), RULES));
    DcrRegistrationRules basicOnly =
        new DcrRegistrationRules(true, RULES.grantTypes(), Set.of("client_secret_basic"), false);
    Assertions.assertEquals("invalid_client_metadata",
        error(web().tokenEndpointAuthMethod("none").build(), basicOnly));
  }

  @Test
  @DisplayName("Redirects: required with code, exact, HTTPS or loopback or native scheme")
  void redirect_rules() {
    Assertions.assertEquals("invalid_redirect_uri",
        error(DynamicClientRequest.builder().build(), RULES));
    Assertions.assertEquals("invalid_redirect_uri",
        error(web().redirectUris(List.of("https://app.example.com/*")).build(), RULES));
    Assertions.assertEquals("invalid_redirect_uri",
        error(web().redirectUris(List.of("http://app.example.com/cb")).build(), RULES));
    Assertions.assertEquals("invalid_redirect_uri",
        error(web().redirectUris(List.of("https://app.example.com/cb#x")).build(), RULES));
    Assertions.assertEquals("invalid_redirect_uri",
        error(web().redirectUris(List.of("javascript:alert(1)")).build(), RULES));
    Assertions.assertDoesNotThrow(() -> DcrMetadataPolicy.validate(
        web().redirectUris(List.of("http://127.0.0.1:8080/cb", "com.example.app:/oauth")).build(),
        RULES));
  }

  @Test
  @DisplayName("A client_credentials client needs no redirect and gets no response type")
  void m2m_client_without_redirects() {
    DynamicClientRequest result = DcrMetadataPolicy.validate(
        DynamicClientRequest.builder().grantTypes(List.of("client_credentials")).build(), RULES);
    Assertions.assertEquals(List.of(), result.getResponseTypes());
    Assertions.assertEquals(List.of(), result.getRedirectUris());
  }

  @Test
  @DisplayName("Post-logout and back-channel URIs are validated too")
  void logout_uris_validated() {
    Assertions.assertEquals("invalid_redirect_uri",
        error(web().postLogoutRedirectUris(List.of("https://app.example.com/*")).build(), RULES));
    Assertions.assertEquals("invalid_client_metadata",
        error(web().backchannelLogoutUri("http://app.example.com/logout").build(), RULES));
  }
}
