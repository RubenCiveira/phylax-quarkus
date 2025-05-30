/* @autogenerated */
package net.civeira.phylax.common.algorithms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Priority;

class PriorityComparatorUnitTest {

  @Test
  void testOrderCompartion() {
    MockPriorityComparatorOne a = new MockPriorityComparatorOne();
    MockPriorityComparatorOne b = new MockPriorityComparatorOne();
    MockPriorityComparatorTwo c = new MockPriorityComparatorTwo();

    PriorityComparator<Object> comp = new PriorityComparator<>();
    Assertions.assertEquals(0, comp.compare(a, b));
    Assertions.assertEquals(-1, comp.compare(a, c));
    Assertions.assertEquals(1, comp.compare(c, a));

    Assertions.assertEquals(1, comp.compare(a, ""));
    Assertions.assertEquals(-1, comp.compare("", a));
  }
}


@Priority(1)
class MockPriorityComparatorOne {
}


@Priority(2)
class MockPriorityComparatorTwo {
}
