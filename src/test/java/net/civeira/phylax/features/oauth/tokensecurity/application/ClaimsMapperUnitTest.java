package net.civeira.phylax.features.oauth.tokensecurity.application;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.tenant.domain.gateway.TenantQueryRepositoryGateway;
import net.civeira.phylax.features.access.tenantconfig.domain.gateway.TenantConfigQueryRepositoryGateway;
import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData;
import net.civeira.phylax.features.oauth.client.domain.ClientDetails;

/**
 * Claims by token purpose (task 1.13) and the RFC 9068 access token profile (task 3.5).
 */
class ClaimsMapperUnitTest {

  private static final Algorithm HMAC = Algorithm.HMAC256("test");
  private static final ClientDetails CLIENT = ClientDetails.builder().clientId("web")
      .allowedScopes(List.of("*")).allowedGrants(List.of("authorization_code")).build();

  private static ClaimsMapper mapper(boolean rfc9068) {
    CurrentRequest current = Mockito.mock(CurrentRequest.class);
    Mockito.when(current.getPublicHost()).thenReturn("https://auth.example.com");
    return new ClaimsMapper(current, Mockito.mock(TenantQueryRepositoryGateway.class),
        Mockito.mock(TenantConfigQueryRepositoryGateway.class),
        new AcrPolicy("urn:lugh:acr:1fa", "urn:lugh:acr:mfa"), rfc9068, Duration.ofMinutes(5));
  }

  private static AuthenticationData user() {
    AuthenticationData data = new AuthenticationData();
    data.setUsername("alice");
    data.setScopes(List.of("openid", "orders:read"));
    data.setAudiences(List.of("web", "orders-api"));
    data.setAmr(List.of("pwd", "otp", "mfa"));
    data.setTime(Instant.ofEpochSecond(1_700_000_000));
    data.setSid("sid-1");
    return data;
  }

  private static DecodedJWT signed(com.auth0.jwt.JWTCreator.Builder builder) {
    return JWT.decode(builder.sign(HMAC));
  }

  @Test
  @DisplayName("Legacy access token: scope array, typ claim and the real login facts")
  void legacy_access_token() {
    DecodedJWT access =
        signed(mapper(false).accessToken("acme", CLIENT, "authorization_code", user()));

    Assertions.assertEquals("JWT", access.getType());
    Assertions.assertEquals(List.of("openid", "orders:read"),
        access.getClaim("scope").asList(String.class));
    Assertions.assertEquals("urn:lugh:acr:mfa", access.getClaim("acr").asString());
    Assertions.assertEquals(1_700_000_000L, access.getClaim("auth_time").asLong());
    Assertions.assertEquals("web", access.getClaim("client_id").asString());
  }

  @Test
  @DisplayName("RFC 9068 access token: typ at+jwt header and scope string")
  void rfc9068_access_token() {
    DecodedJWT access =
        signed(mapper(true).accessToken("acme", CLIENT, "authorization_code", user()));

    Assertions.assertEquals("at+jwt", access.getType());
    Assertions.assertEquals("openid orders:read", access.getClaim("scope").asString());
    Assertions.assertTrue(access.getClaim("typ").isMissing());
    Assertions.assertEquals(List.of("web", "orders-api"), access.getAudience());
  }

  @Test
  @DisplayName("client_credentials tokens carry no user authentication facts")
  void client_credentials_without_login_facts() {
    AuthenticationData m2m = new AuthenticationData();
    m2m.setUsername("web");
    DecodedJWT access =
        signed(mapper(false).accessToken("acme", CLIENT, "client_credentials", m2m));

    Assertions.assertTrue(access.getClaim("acr").isMissing());
    Assertions.assertTrue(access.getClaim("amr").isMissing());
    Assertions.assertTrue(access.getClaim("auth_time").isMissing());
  }

  @Test
  @DisplayName("ID token: only for the client, hashes only for what the response carries")
  void id_token_claims() {
    DecodedJWT id = signed(mapper(false).idToken("acme", CLIENT, user(), "", null, "code-1"));

    Assertions.assertEquals(List.of("web"), id.getAudience());
    Assertions.assertEquals("web", id.getClaim("azp").asString());
    Assertions.assertEquals("sid-1", id.getClaim("sid").asString());
    Assertions.assertTrue(id.getClaim("nonce").isMissing(), "A blank nonce is not emitted");
    Assertions.assertTrue(id.getClaim("at_hash").isMissing());
    Assertions.assertFalse(id.getClaim("c_hash").isMissing());
    Assertions.assertTrue(id.getClaim("roles").isMissing());
    Assertions.assertTrue(id.getClaim("scope").isMissing());
  }
}
