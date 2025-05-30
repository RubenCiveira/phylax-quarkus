/* @autogenerated */
package net.civeira.phylax.common.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import net.civeira.phylax.common.batch.BatchProgress.GlobalStatus;
import net.civeira.phylax.common.batch.BatchStepProgress.Status;

class LocalizedBatchProgressUnitTest {

  @Test
  void testFromWithSingleStep() {
    // Arrange
    BatchStepProgress step = new BatchStepProgress();
    step.setName("step1");
    step.setStatus(Status.FINISHED);
    step.setStartTime(Instant.parse("2024-01-01T10:00:00Z"));
    step.setEndTime(Instant.parse("2024-01-01T10:05:00Z"));

    BatchProgress progress = BatchProgress.builder().uid("job-123").status(GlobalStatus.FINISHED)
        .startTime(Instant.parse("2024-01-01T10:00:00Z"))
        .endTime(Instant.parse("2024-01-01T10:10:00Z")).steps(List.of(step)).build();

    // Act
    LocalizedBatchProgress localized = LocalizedBatchProgress.from(progress, Locale.ENGLISH);

    // Assert
    assertNotNull(localized);
    assertEquals("job-123", localized.getUid());
    assertEquals(GlobalStatus.FINISHED, localized.getStatus());
    assertEquals(Instant.parse("2024-01-01T10:00:00Z"), localized.getStartTime());
    assertEquals(Instant.parse("2024-01-01T10:10:00Z"), localized.getEndTime());

    assertNotNull(localized.getSteps());
    assertEquals(1, localized.getSteps().size());

    LocalizedBatchStepProgress stepLocalized = localized.getSteps().get(0);
    assertEquals("step1", stepLocalized.getName());
    assertEquals(Status.FINISHED, stepLocalized.getStatus());
  }

  @Test
  void testFromWithNullEndTime() {
    // Arrange
    BatchStepProgress step = new BatchStepProgress();
    step.setName("processing-step");
    step.setStatus(Status.PROCESSING);

    BatchProgress progress = BatchProgress.builder().uid("job-456").status(GlobalStatus.PROCESSING)
        .startTime(Instant.now()).endTime(null).steps(List.of(step)).build();

    // Act
    LocalizedBatchProgress result = LocalizedBatchProgress.from(progress, Locale.FRANCE);

    // Assert
    assertNotNull(result);
    assertNull(result.getEndTime());
    assertEquals(1, result.getSteps().size());
    assertEquals("processing-step", result.getSteps().get(0).getName());
  }

  @Test
  void testFromWithEmptySteps() {
    // Arrange
    BatchProgress progress = BatchProgress.builder().uid("job-789").status(GlobalStatus.PENDING)
        .startTime(Instant.now()).endTime(null).steps(List.of()).build();

    // Act
    LocalizedBatchProgress result = LocalizedBatchProgress.from(progress, Locale.getDefault());

    // Assert
    assertNotNull(result);
    assertEquals("job-789", result.getUid());
    assertTrue(result.getSteps().isEmpty());
  }
}
