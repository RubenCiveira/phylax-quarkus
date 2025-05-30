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
import net.civeira.phylax.features.access.user.User;
import net.civeira.phylax.features.access.user.UserFacade;
import net.civeira.phylax.features.access.user.UserRef;
import net.civeira.phylax.features.access.user.application.projection.UserStateProyection;
import net.civeira.phylax.features.access.user.application.service.visibility.UsersVisibility;
import net.civeira.phylax.features.access.user.gateway.UserCacheGateway;
import net.civeira.phylax.features.access.user.gateway.UserWriteRepositoryGateway;
import net.civeira.phylax.features.access.user.query.UserFilter;

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
  private final Event<UserAllowDisableProposal> execAllowEmitter;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final Event<UserDisableProposal> execProposalEmitter;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final Event<UserDisableEvent> executedEmitter;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final UserFacade facade;

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
    UserAllowDisableProposal base = UserAllowDisableProposal.builder()
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
    UserAllowDisableProposal base = UserAllowDisableProposal.builder()
        .detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).build();
    execAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * The slide with some values
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
   * The slide with some values
   *
   * @autogenerated ActionChangeUsecaseGenerator
   * @param query
   * @param reference
   * @return The slide with some values
   */
  public UserStateProyection disable(final Interaction query, final UserRef reference) {
    Allow detail = allow(query, reference);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    User original = visibility.retrieveVisibleForUpdate(query, reference.getUidValue())
        .orElseThrow(() -> new NotFoundException(""));
    User saved = disable(query, original);
    flush();
    return visibility.copyWithHidden(query, saved);
  }

  /**
   * The slide with some values
   *
   * @autogenerated ActionChangeUsecaseGenerator
   * @param query
   * @param filter
   * @return The slide with some values
   */
  public BatchIdentificator disable(final Interaction query, final UserFilter filter) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    UserDisableAllInBatchCommand command =
        UserDisableAllInBatchCommand.builder().interaction(query).filter(filter).build();
    return batch.start(command.getInteraction().getActor().getName().orElse("-"),
        Duration.ofHours(6),
        ExecutorPlan.<UserDisableAllInBatchCommand>builder().params(command).name("disable-color")
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
    User filled = facade.disable(original);
    UserDisableProposal proposal =
        UserDisableProposal.builder().entity(filled).interaction(interaction).build();
    execProposalEmitter.fire(proposal);
    User result = proposal.getEntity();
    User saved = gateway.update(original, result);
    UserDisableEvent event =
        UserDisableEvent.builder().payload(saved).interaction(interaction).build();
    executedEmitter.fire(event);
    return event.getPayload();
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  void flush() {
    cache.evict();
  }
}
