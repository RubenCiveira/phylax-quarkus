package net.civeira.phylax.features.oauth.client.infrastructure.driven;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

/**
 * Fetches a client's remote JWKS ({@code jwks_uri}) without opening an SSRF hole (task 3.3; meant
 * to be shared with client assertions, task 3.2).
 *
 * <ul>
 * <li>HTTPS only, no redirects, no user info in the URL.</li>
 * <li>The host must not resolve to a loopback, private, link-local, unique-local, multicast or
 * unspecified address.</li>
 * <li>3 seconds to connect and to answer, at most 64 KiB.</li>
 * <li>Successful responses are cached per URL for 5 minutes; failures are not cached.</li>
 * </ul>
 * The host is resolved before connecting; a DNS answer that changes between both steps (DNS
 * rebinding) is a residual risk.
 */
@Slf4j
@ApplicationScoped
public class RemoteJwksFetcher {

  private static final Duration TIMEOUT = Duration.ofSeconds(3);
  private static final Duration CACHE_TTL = Duration.ofMinutes(5);
  private static final int MAX_BYTES = 64 * 1024;

  private record Cached(String body, Instant fetchedAt) {}

  private final Map<String, Cached> cache = new ConcurrentHashMap<>();
  private final HttpClient http = HttpClient.newBuilder().connectTimeout(TIMEOUT)
      .followRedirects(HttpClient.Redirect.NEVER).build();

  /**
   * The JWKS document of a URL.
   *
   * @param url {@code jwks_uri} of the client
   * @return its body, empty when the URL is not allowed or the fetch fails
   */
  public Optional<String> fetch(String url) {
    Cached cached = cache.get(url);
    if (cached != null && cached.fetchedAt().plus(CACHE_TTL).isAfter(Instant.now())) {
      return Optional.of(cached.body());
    }
    Optional<URI> uri = allowed(url);
    if (uri.isEmpty()) {
      return Optional.empty();
    }
    try {
      HttpResponse<InputStream> response =
          http.send(HttpRequest.newBuilder(uri.get()).timeout(TIMEOUT).GET().build(),
              HttpResponse.BodyHandlers.ofInputStream());
      try (InputStream body = response.body()) {
        if (response.statusCode() != 200) {
          log.warn("JWKS fetch from {} answered {}", url, response.statusCode());
          return Optional.empty();
        }
        byte[] bytes = body.readNBytes(MAX_BYTES + 1);
        if (bytes.length > MAX_BYTES) {
          log.warn("JWKS from {} is larger than {} bytes", url, MAX_BYTES);
          return Optional.empty();
        }
        String json = new String(bytes, StandardCharsets.UTF_8);
        cache.put(url, new Cached(json, Instant.now()));
        return Optional.of(json);
      }
    } catch (IOException ex) {
      log.warn("JWKS fetch from {} failed: {}", url, ex.getMessage());
      return Optional.empty();
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      return Optional.empty();
    }
  }

  /**
   * The URL as a URI when it may be fetched.
   *
   * @param url candidate URL
   * @return the URI, empty when it is not HTTPS or points to an internal address
   */
  static Optional<URI> allowed(String url) {
    try {
      URI uri = new URI(url);
      if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null
          || uri.getRawUserInfo() != null) {
        return Optional.empty();
      }
      for (InetAddress address : InetAddress.getAllByName(uri.getHost())) {
        if (internal(address)) {
          log.warn("JWKS URL {} resolves to an internal address", url);
          return Optional.empty();
        }
      }
      return Optional.of(uri);
    } catch (java.net.URISyntaxException | UnknownHostException ex) {
      return Optional.empty();
    }
  }

  static boolean internal(InetAddress address) {
    if (address.isLoopbackAddress() || address.isSiteLocalAddress() || address.isLinkLocalAddress()
        || address.isAnyLocalAddress() || address.isMulticastAddress()) {
      return true;
    }
    byte[] raw = address.getAddress();
    if (address instanceof Inet6Address) {
      // Unique local addresses fc00::/7.
      return (raw[0] & 0xFE) == 0xFC;
    }
    // Carrier-grade NAT 100.64.0.0/10.
    return (raw[0] & 0xFF) == 100 && (raw[1] & 0xC0) == 64;
  }
}
