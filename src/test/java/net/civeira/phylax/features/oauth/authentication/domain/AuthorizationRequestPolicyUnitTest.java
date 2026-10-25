package net.civeira.phylax.features.oauth.authentication.domain;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.MultivaluedHashMap;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;

/**
 * Authorization request policy shared by authorize and PAR (tasks 0.4, 1.11 and 2.3).
 */
class AuthorizationRequestPolicyUnitTest {

  private static final String CHALLENGE = "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM";

  private static ClientDetails client(boolean requirePkce, boolean requirePar) {
    return ClientDetails.builder().clientId("app").allowedScopes(List.of("*"))
        .allowedGrants(List.of("authorization_code")).requirePkce(requirePkce)
        .requirePar(requirePar).build();
  }

  private static AuthRequest request(String... pairs) {
    MultivaluedHashMap<String, String> params = new MultivaluedHashMap<>();
    params.putSingle("client_id", "app");
    params.putSingle("response_type", "code");
    for (int i = 0; i < pairs.length; i += 2) {
      params.putSingle(pairs[i], pairs[i + 1]);
    }
    return AuthRequest.fromParams("acme", params, Locale.ENGLISH);
  }

  private static Optional<String> error(AuthRequest request, ClientDetails client) {
    return AuthorizationRequestPolicy.violation(request, client)
        .map(AuthorizationRequestPolicy.Violation::error);
  }

  @Test
  @DisplayName("Only S256: plain or a challenge without method are rejected")
  void only_s256() {
    Assertions.assertEquals(Optional.empty(),
        error(request("code_challenge", CHALLENGE, "code_challenge_method", "S256"),
            client(true, false)));
    Assertions.assertEquals(Optional.of("invalid_request"),
        error(request("code_challenge", CHALLENGE), client(false, false)));
    Assertions.assertEquals(Optional.of("invalid_request"),
        error(request("code_challenge", CHALLENGE, "code_challenge_method", "plain"),
            client(false, false)));
    Assertions.assertEquals(Optional.of("invalid_request"), error(
        request("code_challenge", "short", "code_challenge_method", "S256"), client(false, false)));
  }

  @Test
  @DisplayName("PKCE required for clients that need it; optional for the others")
  void pkce_required_by_client() {
    Assertions.assertEquals(Optional.of("invalid_request"), error(request(), client(true, false)));
    Assertions.assertEquals(Optional.empty(), error(request(), client(false, false)));
  }

  @Test
  @DisplayName("A client that requires PAR only accepts pushed requests")
  void par_required() {
    Assertions.assertEquals(Optional.of("invalid_request"), error(request(), client(false, true)));
    Assertions.assertEquals(Optional.empty(), error(request().asPushed(), client(false, true)));
  }

  @Test
  @DisplayName("ui_locales picks the first supported language, else the fallback")
  void ui_locales() {
    Assertions.assertEquals("en", request("ui_locales", "fr-FR en").getLocale().getLanguage());
    Assertions.assertEquals(Locale.GERMAN,
        AuthRequest.fromParams("acme", new MultivaluedHashMap<>(), Locale.GERMAN).getLocale());
  }
}
