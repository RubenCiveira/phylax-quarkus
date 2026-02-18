package net.civeira.phylax.common.value;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InMemoryCache with TTL and size limits")
class InMemoryCacheUnitTest {

  /**
   * Creates a TTL Duration whose toMillis() is far in the future relative to
   * System.currentTimeMillis(), so the entry won't be considered expired. The Entry class stores
   * ttl.toMillis() as expireAtMillis and compares it directly against System.currentTimeMillis().
   */
  private static Duration futureTtl() {
    return Duration.ofMillis(System.currentTimeMillis() + 60_000);
  }

  @Nested
  @DisplayName("get with callback")
  class GetWithCallback {

    @Test
    @DisplayName("Should store and return value on cache miss")
    void shouldStoreAndReturnValueOnCacheMiss() {
      // Arrange — Create a cache with max size 10
      InMemoryCache<String, String> cache = new InMemoryCache<>(10);

      // Act — Retrieve a missing key, triggering the callback to populate it
      String result = cache.get("key1", k -> new InMemoryCache.Entry<>("value1", futureTtl()));

      // Assert — The callback-produced value should be returned on cache miss
      assertEquals("value1", result,
          "Cache should return the value produced by the callback on cache miss");
    }

    @Test
    @DisplayName("Should return cached value on cache hit")
    void shouldReturnCachedValueOnCacheHit() {
      // Arrange — Create a cache and pre-populate it with key "counter" mapped to value 1
      InMemoryCache<String, Integer> cache = new InMemoryCache<>(10);
      cache.get("counter", k -> new InMemoryCache.Entry<>(1, futureTtl()));

      // Act — Retrieve the same key again with a different callback value
      Integer result = cache.get("counter", k -> new InMemoryCache.Entry<>(999, futureTtl()));

      // Assert — The original cached value should be returned, ignoring the new callback
      assertEquals(1, result,
          "Cache should return the previously stored value on cache hit, not invoke callback again");
    }

    @Test
    @DisplayName("Should throw NullPointerException when key is null")
    void shouldThrowNullPointerExceptionWhenKeyIsNull() {
      // Arrange — Create a cache with max size 10
      InMemoryCache<String, String> cache = new InMemoryCache<>(10);

      // Act / Assert — Calling get with a null key should throw NullPointerException
      assertThrows(NullPointerException.class,
          () -> cache.get(null, k -> new InMemoryCache.Entry<>("v", futureTtl())),
          "Should throw NullPointerException when key is null");
    }
  }

  @Nested
  @DisplayName("remove")
  class Remove {

    @Test
    @DisplayName("Should remove entry from cache")
    void shouldRemoveEntryFromCache() {
      // Arrange — Create a cache and populate it with one entry
      InMemoryCache<String, String> cache = new InMemoryCache<>(10);
      cache.get("key", k -> new InMemoryCache.Entry<>("value", futureTtl()));

      // Act — Remove the previously cached entry by key
      cache.remove("key");

      // Assert — After removal, getting the key again should invoke the callback
      String result = cache.get("key", k -> new InMemoryCache.Entry<>("new-value", futureTtl()));
      assertEquals("new-value", result,
          "After removal, the callback should be invoked again producing a new value");
    }
  }

  @Nested
  @DisplayName("size")
  class Size {

    @Test
    @DisplayName("Should return current number of entries")
    void shouldReturnCurrentNumberOfEntries() {
      // Arrange — Create a cache and populate it with two entries
      InMemoryCache<String, String> cache = new InMemoryCache<>(10);
      cache.get("a", k -> new InMemoryCache.Entry<>("1", futureTtl()));
      cache.get("b", k -> new InMemoryCache.Entry<>("2", futureTtl()));

      // Act — Query the current cache size
      int size = cache.size();

      // Assert — The size should reflect the number of entries stored
      assertEquals(2, size, "Cache size should reflect the number of entries stored");
    }

    @Test
    @DisplayName("Should return zero for empty cache")
    void shouldReturnZeroForEmptyCache() {
      // Arrange — Create an empty cache with no entries
      InMemoryCache<String, String> cache = new InMemoryCache<>(10);

      // Act / Assert — An empty cache should report size zero
      assertEquals(0, cache.size(), "Empty cache should have size 0");
    }
  }

  @Nested
  @DisplayName("TTL expiration")
  class TtlExpiration {

    @Test
    @DisplayName("Should expire entries whose expireAtMillis is in the past")
    void shouldExpireEntriesWithPastExpiration() {
      // Arrange — Create a cache and insert an entry with a past expiration (TTL of 1ms)
      InMemoryCache<String, String> cache = new InMemoryCache<>(10);
      cache.get("key", k -> new InMemoryCache.Entry<>("expired", Duration.ofMillis(1)));

      // Act — Access the key again, which should detect the expired entry and invoke the callback
      String result = cache.get("key", k -> new InMemoryCache.Entry<>("fresh", futureTtl()));

      // Assert — The fresh value from the callback should replace the expired entry
      assertEquals("fresh", result,
          "Expired entry should be evicted and callback should produce a new value");
    }
  }

  @Nested
  @DisplayName("Max size enforcement")
  class MaxSizeEnforcement {

    @Test
    @DisplayName("Should enforce max size by evicting entries")
    void shouldEnforceMaxSizeByEvictingEntries() {
      // Arrange — Create a cache with a maximum size of 3
      InMemoryCache<Integer, String> cache = new InMemoryCache<>(3);

      // Act — Add 5 entries, exceeding the maximum capacity
      for (int i = 0; i < 5; i++) {
        cache.get(i, k -> new InMemoryCache.Entry<>("val-" + k, futureTtl()));
      }

      // Assert — The cache size should not exceed the configured maximum of 3
      assertTrue(cache.size() <= 3,
          "Cache size should not exceed max size of 3 after adding 5 entries");
    }
  }

  @Nested
  @DisplayName("cleanUp")
  class CleanUp {

    @Test
    @DisplayName("Should remove expired entries on cleanup")
    void shouldRemoveExpiredEntriesOnCleanup() {
      // Arrange — Create a cache with one expired entry and one alive entry
      InMemoryCache<String, String> cache = new InMemoryCache<>(10);
      cache.get("expired", k -> new InMemoryCache.Entry<>("v", Duration.ofMillis(1)));
      cache.get("alive", k -> new InMemoryCache.Entry<>("v", futureTtl()));

      // Act — Trigger cleanup to evict expired entries
      cache.cleanUp();

      // Assert — Only the non-expired entry should remain after cleanup
      assertEquals(1, cache.size(), "After cleanup, only the non-expired entry should remain");
    }
  }
}
