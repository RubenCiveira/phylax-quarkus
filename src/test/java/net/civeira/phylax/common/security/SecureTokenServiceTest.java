package net.civeira.phylax.common.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SecureTokenService")
class SecureTokenServiceTest {

  private SecureTokenService service;

  @BeforeEach
  void setUp() {
    service = new SecureTokenService();
  }

  @Nested
  @DisplayName("generate()")
  class Generate {

    @Test
    @DisplayName("returns a non-blank token")
    void returnsNonBlankToken() {
      assertFalse(service.generate().isBlank());
    }

    @Test
    @DisplayName("returns unique tokens on each call")
    void returnsUniqueTokens() {
      String t1 = service.generate();
      String t2 = service.generate();
      assertNotEquals(t1, t2);
    }

    @Test
    @DisplayName("returns Base64URL-safe characters only (no padding)")
    void returnsBase64UrlChars() {
      String token = service.generate();
      assertTrue(token.matches("[A-Za-z0-9_-]+"), "Token must be Base64URL without padding: " + token);
    }
  }

  @Nested
  @DisplayName("hash()")
  class Hash {

    @Test
    @DisplayName("is deterministic for the same input")
    void isDeterministic() {
      String raw = "test-token-value";
      assertEquals(service.hash(raw), service.hash(raw));
    }

    @Test
    @DisplayName("returns different hashes for different inputs")
    void differentInputsDifferentHashes() {
      assertNotEquals(service.hash("token-a"), service.hash("token-b"));
    }

    @Test
    @DisplayName("returns a 64-character hex string (SHA-256)")
    void returnsHexString() {
      String hash = service.hash("some-token");
      assertEquals(64, hash.length());
      assertTrue(hash.matches("[0-9a-f]+"));
    }
  }

  @Nested
  @DisplayName("verify()")
  class Verify {

    @Test
    @DisplayName("returns true when rawToken matches storedHash")
    void returnsTrueForMatch() {
      String raw = service.generate();
      String stored = service.hash(raw);
      assertTrue(service.verify(raw, stored));
    }

    @Test
    @DisplayName("returns false for wrong token")
    void returnsFalseForWrongToken() {
      String stored = service.hash("correct-token");
      assertFalse(service.verify("wrong-token", stored));
    }

    @Test
    @DisplayName("returns false for tampered hash")
    void returnsFalseForTamperedHash() {
      String raw = "my-token";
      String stored = service.hash(raw);
      String tampered = stored.replace(stored.charAt(0), stored.charAt(0) == 'a' ? 'b' : 'a');
      assertFalse(service.verify(raw, tampered));
    }
  }
}
