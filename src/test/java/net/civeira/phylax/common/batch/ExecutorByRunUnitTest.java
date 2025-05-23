/* @autogenerated */
package net.civeira.phylax.common.batch;

import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExecutorByRunUnitTest {

  @Test
  void testRun() {
    String params = "params";
    Monitor monitor = Mockito.mock(Monitor.class);
    @SuppressWarnings("unchecked")
    BiConsumer<Monitor, String> consumer = Mockito.mock(BiConsumer.class);
    ExecutorByRun<String> run = new ExecutorByRun<>(consumer);
    run.run(null, monitor, params);
    Mockito.verify(consumer).accept(monitor, params);
  }
}
