package net.civeira.phylax.features.access.user.application.usecase.disable;

import java.time.Duration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.batch.BatchIdentificator;
import net.civeira.phylax.common.batch.BatchProgress;
import net.civeira.phylax.common.batch.BatchService;
import net.civeira.phylax.common.batch.ExecutorByDeferSteps;
import net.civeira.phylax.common.batch.ExecutorPlan;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.user.application.visibility.UserVisibilityFilter;
import net.civeira.phylax.features.access.user.application.visibility.UsersVisibility;
import net.civeira.phylax.features.access.user.domain.User;
import net.civeira.phylax.features.access.user.domain.UserRef;
import net.civeira.phylax.features.access.user.domain.gateway.UserCacheGateway;
import net.civeira.phylax.features.access.user.domain.gateway.UserWriteRepositoryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class UserDisableUsecase {

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final BatchService batch;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final UserCacheGateway cache;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final Event<UserDisableAllowDecision> execAllowEmitter;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final Event<UserDisableCheck> execProposalEmitter;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final UserWriteRepositoryGateway gateway;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final UsersVisibility visibility;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   * @param query
   * @param reference
   * @return
   */
  public Allow allow(final Interaction query, final UserRef reference) {
    UserDisableAllowDecision base = UserDisableAllowDecision.builder()
        .detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).reference(reference).build();
    execAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    UserDisableAllowDecision base = UserDisableAllowDecision.builder()
        .detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).build();
    execAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * Recover a slide of data.
   *
   * @autogenerated ActionChangeUsecaseGenerator
   * @param query a filter to retrieve only matching values
   * @return The slide with some values
   */
  public BatchProgress checkProgress(final UserDisableStatus query) {
    return query.getActor().getName()
        .flatMap(name -> batch.retrieve(query.getTaskId(), query.getConnection().getLocale(), name))
        .orElseThrow(() -> new NotFoundException());
  }

  /**
   * Recover a slide of data.
   *
   * @autogenerated ActionChangeUsecaseGenerator
   * @param query
   * @param reference
   * @return The slide with some values
   */
  public UserDisableProjection disable(final Interaction query, final UserRef reference) {
    Allow detail = allow(query, reference);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    User original = visibility.retrieveVisibleForUpdate(query, reference.getUid())
        .orElseThrow(() -> new NotFoundException(""));
    User saved = disable(query, original);
    flush();
    return UserDisableProjection.from(visibility.copyWithHidden(query, saved));
  }

  /**
   * Recover a slide of data.
   *
   * @autogenerated ActionChangeUsecaseGenerator
   * @param query
   * @param filter
   * @return The slide with some values
   */
  public BatchIdentificator disable(final Interaction query, final UserDisableFilter filter) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    UserVisibilityFilter filterWithVisibility = UserVisibilityFilter.builder()
        .uid(filter.getUid().orElse(null)).uids(filter.getUids().stream().toList())
        .search(filter.getSearch().orElse(null)).root(filter.getRoot().orElse(null))
        .nameOrEmail(filter.getNameOrEmail().orElse(null)).name(filter.getName().orElse(null))
        .tenant(filter.getTenant().orElse(null)).tenants(filter.getTenants())
        .tenantTenantAccesible(filter.getTenantTenantAccesible().orElse(null)).build();
    UserDisableAllInBatchCommand command = UserDisableAllInBatchCommand.builder().interaction(query)
        .filter(filterWithVisibility).build();
    return batch.start(command.getInteraction().getActor().getName().orElse("-"),
        Duration.ofHours(6),
        ExecutorPlan.<UserDisableAllInBatchCommand>builder().params(command).name("disable-user")
            .executor(
                ExecutorByDeferSteps.<User, User, UserDisableAllInBatchCommand, UserDisablesInBatchExecutor.UserPaginableBatch>builder()
                    .initializer(UserDisablesInBatchExecutor.class)
                    .counter(UserDisablesInBatchExecutor.class)
                    .descriptor(UserDisablesInBatchExecutor.class)
                    .reader(UserDisablesInBatchExecutor.class)
                    .processor(UserDisablesInBatchExecutor.class)
                    .writer(UserDisablesInBatchExecutor.class)
                    .finalizer(UserDisablesInBatchExecutor.class).build())
            .build());
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   * @param interaction
   * @param original
   * @return
   */
  User disable(final Interaction interaction, final User original) {
    UserDisableCheck proposal =
        UserDisableCheck.builder().reference(original).interaction(interaction).build();
    execProposalEmitter.fire(proposal);
    User result = original.disable();
    User saved = gateway.update(original, result);
    return saved;
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  void flush() {
    cache.evict();
  }
}
