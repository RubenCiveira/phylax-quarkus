package net.civeira.phylax.common.batch;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.context.ManagedExecutor;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.batch.BatchProgress.GlobalStatus;
import net.civeira.phylax.common.batch.BatchStepProgress.Status;
import net.civeira.phylax.common.batch.storage.MassiveOperationStorage;

/**
 * Manages asynchronous execution and tracking of multi-step batch jobs.
 *
 * <p>
 * The service initializes batch steps, persists progress, and runs them in a background executor.
 * Each step is executed via an {@link ExecutorPlan} and persisted through
 * {@link MassiveOperationStorage}. Progress is saved between steps so clients can monitor
 * long-running work. When the job finishes, it is marked for expiration based on the preservation
 * duration.
 * </p>
 *
 * <p>
 * This bean is {@code @ApplicationScoped} so that its injected dependencies and internal state
 * remain valid for the full lifetime of any asynchronous task submitted via
 * {@link #start(String, Duration, ExecutorPlan[])}. Asynchronous tasks outlive the HTTP request
 * that triggered them; using {@code @RequestScoped} would cause CDI proxies to become invalid once
 * the originating request context is destroyed.
 * </p>
 */
@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class BatchService {

  /** The storage backend used to persist and restore batch progress. */
  private final MassiveOperationStorage storage;

  /** Managed executor service used to run batch jobs asynchronously. */
  private final ManagedExecutor executor;

  /**
   * Starts the asynchronous execution of a batch job composed of multiple steps.
   *
   * <p>
   * This initializes step progress, stores the initial state, and launches a background task. Steps
   * run sequentially using their {@link Executor} implementations with progress persisted.
   * Exceptions are recorded per step without stopping the overall job execution. The job is
   * finalized and scheduled for expiration after the preservation window.
   * </p>
   *
   * @param actor the identifier of the user or system initiating the batch job
   * @param preservation how long the batch result should be preserved after completion
   * @param plans ordered set of execution plans that compose the batch
   * @return a {@link BatchIdentificator} containing the batch UID and step names
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public BatchIdentificator start(String actor, Duration preservation, ExecutorPlan<?>... plans) {
    String taskUid = UUID.randomUUID().toString();
    List<BatchStepProgress> map = new ArrayList<>();
    for (int i = 0; i < plans.length; i++) {
      ExecutorPlan<?> plan = plans[i];
      BatchStepProgress step = new BatchStepProgress();
      step.setName(plan.getName());
      step.setStatus(Status.PENDING);
      map.add(step);
    }
    BatchProgress progress = BatchProgress.builder().uid(taskUid).steps(map)
        .startTime(Instant.now()).status(GlobalStatus.PENDING).build();

    storage.save(taskUid, actor, progress);
    executor.runAsync(() -> {
      try {
        for (int i = 0; i < plans.length; i++) {
          ExecutorPlan<?> plan = plans[i];
          Executor task = plan.getExecutor();
          BatchStepProgress massiveOperationResult = map.get(i);
          try {
            massiveOperationResult.setStatus(Status.PROCESSING);
            storage.save(taskUid, actor, progress);
            task.run(massiveOperationResult, t -> {
              storage.save(taskUid, actor, progress);
            }, plan.getParams());
            massiveOperationResult.setStatus(Status.FINISHED);
            storage.save(taskUid, actor, progress);
          } catch (RuntimeException ex) {
            log.error("Step '{}' failed with error: {}", massiveOperationResult.getName(),
                ex.getMessage(), ex);
            massiveOperationResult.setError(ex.getMessage());
            massiveOperationResult.setStatus(Status.FAILED);
            storage.save(taskUid, actor, progress);
          }
        }
      } finally {
        storage.finish(taskUid, Instant.now().plus(preservation), actor);
      }
    });
    return BatchIdentificator.builder()
        .steps(progress.getSteps().stream().map(BatchStepProgress::getName).toList()).uid(taskUid)
        .build();
  }

  /**
   * Retrieves the current progress of a batch job.
   *
   * <p>
   * This delegates to the storage layer to restore the last persisted state. The locale parameter
   * is reserved for future localization of messages. Callers should provide the actor to enforce
   * ownership constraints. Returns empty when no batch with the given uid is found for the actor.
   * </p>
   *
   * @param uid unique batch identifier
   * @param locale locale to apply for future localization
   * @param actor actor or user who owns the batch
   * @return optional containing the {@link BatchProgress} if found
   */
  public Optional<BatchProgress> retrieve(String uid, Locale locale, String actor) {
    return storage.restores(uid, actor);
  }
}
