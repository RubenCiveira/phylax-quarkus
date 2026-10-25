package net.civeira.phylax.features.oauth.client.infrastructure.driven;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.civeira.phylax.features.oauth.client.domain.ClientDetails;

/**
 * Signature of client JWTs with the client's own keys (task 3.3).
 */
class JwksSignatureAdapterUnitTest {

  private final JwksSignatureAdapter adapter =
      new JwksSignatureAdapter(Mockito.mock(RemoteJwksFetcher.class), new ObjectMapper());

  private static String b64(BigInteger value, int length) {
    byte[] bytes = value.toByteArray();
    if (bytes.length > 1 && bytes[0] == 0) {
      bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
    }
    if (length > 0 && bytes.length < length) {
      byte[] padded = new byte[length];
      System.arraycopy(bytes, 0, padded, length - bytes.length, bytes.length);
      bytes = padded;
    }
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private static ClientDetails client(String jwks, String alg) {
    return ClientDetails.builder().clientId("web").allowedScopes(List.of()).allowedGrants(List.of())
        .jwksJson(jwks).requestObjectSigningAlg(alg).build();
  }

  private static String token(Algorithm algorithm) {
    return JWT.create().withIssuer("web").withKeyId("k1")
        .withExpiresAt(Date.from(Instant.now().plusSeconds(60))).sign(algorithm);
  }

  @Test
  @DisplayName("ES256 with a P-256 key of the client verifies")
  void es256_verifies() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
    generator.initialize(new ECGenParameterSpec("secp256r1"));
    KeyPair pair = generator.generateKeyPair();
    ECPublicKey pub = (ECPublicKey) pair.getPublic();
    String jwks = "{\"keys\":[{\"kty\":\"EC\",\"crv\":\"P-256\",\"kid\":\"k1\",\"x\":\""
        + b64(pub.getW().getAffineX(), 32) + "\",\"y\":\"" + b64(pub.getW().getAffineY(), 32)
        + "\"}]}";

    String jwt = token(Algorithm.ECDSA256(null, (ECPrivateKey) pair.getPrivate()));

    Assertions.assertEquals("web",
        adapter.verify(client(jwks, null), jwt).orElseThrow().get("iss"));
    Assertions.assertTrue(adapter.verify(client(jwks, "RS256"), jwt).isEmpty(),
        "Another algorithm than the registered one is refused");
  }

  @Test
  @DisplayName("Symmetric, unsigned or foreign-key tokens are refused")
  void refused_tokens() throws Exception {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    KeyPair registered = generator.generateKeyPair();
    KeyPair other = generator.generateKeyPair();
    RSAPublicKey pub = (RSAPublicKey) registered.getPublic();
    String jwks = "{\"keys\":[{\"kty\":\"RSA\",\"kid\":\"k1\",\"n\":\"" + b64(pub.getModulus(), 0)
        + "\",\"e\":\"" + b64(pub.getPublicExponent(), 0) + "\"}]}";
    ClientDetails client = client(jwks, null);

    Assertions.assertTrue(adapter
        .verify(client, token(Algorithm.RSA256(null, (RSAPrivateKey) registered.getPrivate())))
        .isPresent());
    Assertions.assertTrue(
        adapter.verify(client, token(Algorithm.RSA256(null, (RSAPrivateKey) other.getPrivate())))
            .isEmpty());
    Assertions.assertTrue(adapter.verify(client, token(Algorithm.HMAC256("secret"))).isEmpty());
    Assertions.assertTrue(adapter.verify(client, token(Algorithm.none())).isEmpty());
  }
}
