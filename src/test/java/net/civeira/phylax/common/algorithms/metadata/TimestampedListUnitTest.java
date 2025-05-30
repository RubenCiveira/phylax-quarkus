/* @autogenerated */
package net.civeira.phylax.common.algorithms.metadata;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimestampedListUnitTest {

  @Test
  void testBuild() {
    Instant instant = Instant.now();
    WrapMetadata<List<String>> wrapped =
        WrapMetadata.<List<String>>builder().data(List.of("verde", "azul")).since(instant).build();
    TimestampedList<String> list = new TimestampedList<>(wrapped);
    Assertions.assertEquals("verde", list.get(0));
    Assertions.assertEquals("azul", list.get(1));
    Assertions.assertEquals(instant, list.getGeneratedAt().get());
  }
}
