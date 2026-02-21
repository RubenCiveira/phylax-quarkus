package net.civeira.phylax.testing.oauth.alt;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.key.domain.gateway.TokenSigner;
import net.civeira.phylax.features.oauth.key.domain.model.Jks;
import net.civeira.phylax.features.oauth.key.domain.model.JwkSet;

@Alternative
@Priority(1)
@ApplicationScoped
public class FixedTokenSigner implements TokenSigner {
  private static final String KEY_ID = "test-key";
  private static final String KEYPASS = "test-keypass";

  private RSAPublicKey publicKey;
  private RSAPrivateKey privateKey;

  @PostConstruct
  void init() {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
      keyGen.initialize(2048);
      KeyPair pair = keyGen.generateKeyPair();
      publicKey = (RSAPublicKey) pair.getPublic();
      privateKey = (RSAPrivateKey) pair.getPrivate();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Unable to generate RSA key pair", e);
    }
  }

  @Override
  public String sign(String tenant, Builder data, Instant expiration) {
    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
    return data.withKeyId(KEY_ID).withExpiresAt(java.util.Date.from(expiration)).sign(algorithm);
  }

  @Override
  public JwkSet keysAsJwks(String tenant) {
    Jks jwk = Jks.builder().kty("RSA").kid(KEY_ID).n(base64Url(publicKey.getModulus()))
        .e(base64Url(publicKey.getPublicExponent())).alg("RS256").use("sig").build();
    return JwkSet.builder().keys(List.of(jwk)).build();
  }

  @Override
  public String signKeypass(String tenant, Builder data, Instant expiration) {
    Algorithm algorithm = Algorithm.HMAC256(KEYPASS);
    return data.withKeyId(KEYPASS).withExpiresAt(java.util.Date.from(expiration)).sign(algorithm);
  }

  @Override
  public Map<String, Object> verifyTokenPayload(String tenant, String token) {
    try {
      Algorithm algorithm = Algorithm.RSA256(publicKey, null);
      DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
      Map<String, Object> payload = new HashMap<>();
      decoded.getClaims().forEach((key, claim) -> payload.put(key, claim.as(Object.class)));
      return payload;
    } catch (JWTVerificationException ex) {
      try {
        Algorithm algorithm = Algorithm.HMAC256(KEYPASS);
        DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
        Map<String, Object> payload = new HashMap<>();
        decoded.getClaims().forEach((key, claim) -> payload.put(key, claim.as(Object.class)));
        return payload;
      } catch (JWTVerificationException hmacEx) {
        return Map.of();
      }
    }
  }

  @Override
  public String verifiedKeypass(String tenant, String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(KEYPASS);
      DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
      return decoded.getKeyId();
    } catch (JWTVerificationException ex) {
      return "";
    }
  }

  private String base64Url(BigInteger value) {
    byte[] bytes = value.toByteArray();
    if (bytes.length > 1 && bytes[0] == 0) {
      byte[] trimmed = new byte[bytes.length - 1];
      System.arraycopy(bytes, 1, trimmed, 0, trimmed.length);
      bytes = trimmed;
    }
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
