package net.civeira.phylax.common.batch.storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.batch.BatchProgress;
import net.civeira.phylax.common.batch.BatchProgress.GlobalStatus;
import net.civeira.phylax.common.batch.BatchStepProgress;
import net.civeira.phylax.common.batch.BatchStepProgress.Status;

/**
 * JDBC-based implementation of {@link MassiveOperationStorage} that persists and restores
 * {@link BatchProgress} records using a relational database table.
 *
 * <p>
 * This implementation uses a table named <code>_long_tasks</code> to store task metadata, progress
 * steps, and expiration information. It supports resumable and user-scoped batch operations by
 * allowing progress to be saved, restored, and finalized.
 * </p>
 *
 * <p>
 * Progress data is serialized as JSON using Jackson, and expiration is handled by automatically
 * cleaning up expired tasks on every access. Each task is uniquely identified by a combination of
 * task code and actor (user identifier).
 * </p>
 *
 * <p>
 * This class is marked as {@code @ApplicationScoped} and designed for use in CDI-managed
 * environments.
 * </p>
 *
 * @see BatchProgress
 * @see BatchStepProgress
 * @see MassiveOperationStorage
 */
@ApplicationScoped
@RequiredArgsConstructor
public class MapMassiveOperationStorage implements MassiveOperationStorage {
  private final ObjectMapper mapper;
  private final DataSource datasource;

  /**
   * Saves or updates the current batch progress for a given task and actor.
   *
   * <p>
   * If a record does not exist, it is inserted. Otherwise, it is updated with the current list of
   * progress steps serialized as JSON.
   * </p>
   *
   * @param task the unique task identifier
   * @param actor the user or system actor responsible for the task
   * @param result the progress state to save
   * @throws IllegalArgumentException if the operation cannot be persisted
   */
  @Override
  public void save(String task, String actor, BatchProgress result) {
    try (Connection connection = datasource.getConnection()) {
      cleanTemp(connection);
      try (PreparedStatement prepareStatement = connection
          .prepareStatement("SELECT count(code)  FROM _long_tasks WHERE code = ? and actor=?")) {
        prepareStatement.setString(1, task);
        prepareStatement.setString(2, actor);
        long count = 0;
        try (ResultSet executeQuery = prepareStatement.executeQuery()) {
          if (executeQuery.next()) {
            count = executeQuery.getLong(1);
          }
        }
        if (count == 0) {
          try (PreparedStatement updateStatement = connection.prepareStatement(
              "INSERT INTO _long_tasks (code, actor, creation, progress) VALUES(?, ?, ?, ?)")) {
            updateStatement.setString(1, task);
            updateStatement.setString(2, actor);
            updateStatement.setTimestamp(3, new Timestamp(result.getStartTime().toEpochMilli()));
            updateStatement.setString(4, mapper.writeValueAsString(result.getSteps()));
            if (updateStatement.executeUpdate() != 1) {
              throw new IllegalArgumentException("Failed to save batch progress for task: " + task);
            }
          }
        } else {
          try (PreparedStatement updateStatement = connection
              .prepareStatement("UPDATE _long_tasks set progress = ? where code=? and actor=?")) {
            updateStatement.setString(1, mapper.writeValueAsString(result.getSteps()));
            updateStatement.setString(2, task);
            updateStatement.setString(3, actor);
            if (updateStatement.executeUpdate() != 1) {
              throw new IllegalArgumentException("Failed to save batch progress for task: " + task);
            }
          }
        }
      }
    } catch (JsonProcessingException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Restores previously saved progress for a given task and actor.
   *
   * @param task the task identifier
   * @param actor the user or actor who executed the task
   * @return an {@link Optional} containing the restored {@link BatchProgress}, or empty if none
   *         found
   * @throws IllegalArgumentException if deserialization or database access fails
   */
  @Override
  public Optional<BatchProgress> restores(String task, String actor) {
    Optional<BatchProgress> result = Optional.empty();
    try (Connection connection = datasource.getConnection()) {
      cleanTemp(connection);
      try (PreparedStatement prepareStatement = connection.prepareStatement(
          "SELECT progress, creation, completion  FROM _long_tasks WHERE code = ? and actor=?")) {
        prepareStatement.setString(1, task);
        prepareStatement.setString(2, actor);
        try (ResultSet executeQuery = prepareStatement.executeQuery()) {
          if (executeQuery.next()) {
            String json = executeQuery.getString(1);
            List<BatchStepProgress> steps =
                mapper.readValue(json, new TypeReference<List<BatchStepProgress>>() {});
            result = Optional.of(BatchProgress.builder().uid(task).status(globalStatus(steps))
                .startTime(Instant.ofEpochMilli(executeQuery.getTimestamp(2).getTime()))
                .endTime(null == executeQuery.getTimestamp(3) ? null
                    : Instant.ofEpochMilli(executeQuery.getTimestamp(3).getTime()))
                .steps(steps).build());
          }
        }
      }
    } catch (IOException | SQLException e) {
      throw new IllegalArgumentException(e);
    }
    return result;
  }

  /**
   * Marks a task as completed and sets its expiration timestamp.
   *
   * @param taskUid the task identifier
   * @param expiration the expiration time after which the record may be cleaned
   * @param actor the user or actor associated with the task
   * @throws IllegalArgumentException if the update fails
   */
  @Override
  public void finish(String taskUid, Instant expiration, String actor) {
    try (Connection connection = datasource.getConnection()) {
      cleanTemp(connection);
      try (PreparedStatement updateStatement = connection.prepareStatement(
          "UPDATE _long_tasks SET completion = ?, expiration = ? where code = ? and actor = ?")) {
        updateStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        updateStatement.setTimestamp(2, new Timestamp(expiration.toEpochMilli()));
        updateStatement.setString(3, taskUid);
        updateStatement.setString(4, actor);
        if (updateStatement.executeUpdate() != 1) {
          throw new IllegalArgumentException("Failed to finalize batch task: " + taskUid);
        }
      }
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Computes the overall status of a batch based on its individual step results.
   *
   * @param steps the list of step progress entries
   * @return the computed {@link GlobalStatus}
   */
  private GlobalStatus globalStatus(List<BatchStepProgress> steps) {
    GlobalStatus status = GlobalStatus.PENDING;
    boolean someProcesing = false;
    boolean someError = false;
    boolean allPending = true;

    for (BatchStepProgress massiveOperationResultDetail : steps) {
      if (massiveOperationResultDetail.getStatus() == Status.PROCESSING) {
        someProcesing = true;
        allPending = false;
      } else if (massiveOperationResultDetail.getStatus() == Status.FAILED) {
        someError = true;
        allPending = false;
      } else if (massiveOperationResultDetail.getStatus() == Status.FINISHED) {
        allPending = false;
      }
    }
    if (allPending) {
      status = GlobalStatus.PENDING;
    } else if (someProcesing) {
      status = GlobalStatus.PROCESSING;
    } else if (someError) {
      status = GlobalStatus.FAILED;
    } else {
      status = GlobalStatus.FINISHED;
    }
    return status;
  }

  /**
   * Removes expired task entries from the storage.
   *
   * @param connection the JDBC connection to use
   * @throws SQLException if an SQL error occurs
   */
  private void cleanTemp(Connection connection) throws SQLException {
    try (PreparedStatement updateStatement =
        connection.prepareStatement("DELETE FROM _long_tasks where expiration < ?")) {
      updateStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
      updateStatement.executeUpdate();
    }
  }
}
