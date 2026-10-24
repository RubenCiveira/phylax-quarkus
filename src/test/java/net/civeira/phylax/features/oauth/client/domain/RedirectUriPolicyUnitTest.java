package net.civeira.phylax.features.oauth.client.domain;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RedirectUriPolicyUnitTest {

  private static final String APP = "https://app.example.com/callback";
  private static final String DEV = "http://localhost:4200/*";

  private static boolean allows(String registered, String requested) {
    return RedirectUriPolicy.allows(List.of(registered), requested);
  }

  @Test
  @DisplayName("Exact registered URI is accepted")
  void test_exact() {
    Assertions.assertTrue(allows(APP, APP));
  }

  @Test
  @DisplayName("Prefixes, extra paths, queries and look-alike hosts are rejected")
  void test_not_exact() {
    Assertions.assertFalse(allows(APP, APP + "/other"));
    Assertions.assertFalse(allows(APP, APP + "?next=https://evil.example"));
    Assertions.assertFalse(allows(APP, "https://app.example.com.evil.example/callback"));
    Assertions.assertFalse(allows(APP, "http://app.example.com/callback"));
    Assertions.assertFalse(allows(APP, null));
  }

  @Test
  @DisplayName("Wildcards outside loopback never match")
  void test_wildcards() {
    Assertions.assertFalse(allows("*", "https://evil.example/"));
    Assertions.assertFalse(allows("*", "*"));
    Assertions.assertFalse(allows("https://app.example.com/*", "https://app.example.com/callback"));
  }

  @Test
  @DisplayName("Loopback wildcard accepts paths of the registered host and port")
  void test_loopback_accepted() {
    Assertions.assertTrue(allows(DEV, "http://localhost:4200/"));
    Assertions.assertTrue(allows(DEV, "http://localhost:4200/callback?x=1"));
    Assertions.assertTrue(allows("http://127.0.0.1/app/*", "http://127.0.0.1:51234/app/cb"));
  }

  @Test
  @DisplayName("Loopback wildcard rejects userinfo, other host, port, scheme, fragment and escapes")
  void test_loopback_rejected() {
    Assertions.assertFalse(allows(DEV, "http://localhost:4200@evil.example/"));
    Assertions.assertFalse(allows(DEV, "http://user@localhost:4200/"));
    Assertions.assertFalse(allows(DEV, "http://localhost.evil.example:4200/"));
    Assertions.assertFalse(allows(DEV, "http://localhost:4201/"));
    Assertions.assertFalse(allows(DEV, "https://localhost:4200/"));
    Assertions.assertFalse(allows(DEV, "http://localhost:4200/#frag"));
    Assertions.assertFalse(allows("http://127.0.0.1/app/*", "http://127.0.0.1/application"));
    Assertions.assertFalse(allows("http://127.0.0.1/app/*", "http://127.0.0.1/app/../admin"));
  }
}
