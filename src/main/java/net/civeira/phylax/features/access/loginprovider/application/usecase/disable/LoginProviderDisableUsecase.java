package net.civeira.phylax.features.access.loginprovider.application.usecase.disable;

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
import net.civeira.phylax.features.access.loginprovider.LoginProvider;
import net.civeira.phylax.features.access.loginprovider.LoginProviderFacade;
import net.civeira.phylax.features.access.loginprovider.LoginProviderRef;
import net.civeira.phylax.features.access.loginprovider.application.projection.LoginProviderStateProyection;
import net.civeira.phylax.features.access.loginprovider.application.service.visibility.LoginProvidersVisibility;
import net.civeira.phylax.features.access.loginprovider.gateway.LoginProviderCacheGateway;
import net.civeira.phylax.features.access.loginprovider.gateway.LoginProviderWriteRepositoryGateway;
import net.civeira.phylax.features.access.loginprovider.query.LoginProviderFilter;

@ApplicationScoped
@RequiredArgsConstructor
public class LoginProviderDisableUsecase {

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final BatchService batch;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final LoginProviderCacheGateway cache;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final Event<LoginProviderAllowDisableProposal> execAllowEmitter;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final Event<LoginProviderDisableProposal> execProposalEmitter;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final Event<LoginProviderDisableEvent> executedEmitter;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final LoginProviderFacade facade;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final LoginProviderWriteRepositoryGateway gateway;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  private final LoginProvidersVisibility visibility;

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   * @param query
   * @param reference
   * @return
   */
  public Allow allow(final Interaction query, final LoginProviderRef reference) {
    LoginProviderAllowDisableProposal base = LoginProviderAllowDisableProposal.builder()
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
    LoginProviderAllowDisableProposal base = LoginProviderAllowDisableProposal.builder()
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
  public BatchProgress checkProgress(final LoginProviderDisableStatus query) {
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
  public LoginProviderStateProyection disable(final Interaction query,
      final LoginProviderRef reference) {
    Allow detail = allow(query, reference);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    LoginProvider original = visibility.retrieveVisibleForUpdate(query, reference.getUidValue())
        .orElseThrow(() -> new NotFoundException(""));
    LoginProvider saved = disable(query, original);
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
  public BatchIdentificator disable(final Interaction query, final LoginProviderFilter filter) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    LoginProviderDisableAllInBatchCommand command =
        LoginProviderDisableAllInBatchCommand.builder().interaction(query).filter(filter).build();
    return batch.start(command.getInteraction().getActor().getName().orElse("-"),
        Duration.ofHours(6),
        ExecutorPlan.<LoginProviderDisableAllInBatchCommand>builder().params(command)
            .name("disable-color")
            .executor(
                ExecutorByDeferSteps.<LoginProvider, LoginProvider, LoginProviderDisableAllInBatchCommand, LoginProviderDisablesInBatchExecutor.LoginProviderPaginableBatch>builder()
                    .initializer(LoginProviderDisablesInBatchExecutor.class)
                    .counter(LoginProviderDisablesInBatchExecutor.class)
                    .descriptor(LoginProviderDisablesInBatchExecutor.class)
                    .reader(LoginProviderDisablesInBatchExecutor.class)
                    .processor(LoginProviderDisablesInBatchExecutor.class)
                    .writer(LoginProviderDisablesInBatchExecutor.class)
                    .finalizer(LoginProviderDisablesInBatchExecutor.class).build())
            .build());
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   * @param interaction
   * @param original
   * @return
   */
  LoginProvider disable(final Interaction interaction, final LoginProvider original) {
    LoginProvider filled = facade.disable(original);
    LoginProviderDisableProposal proposal =
        LoginProviderDisableProposal.builder().entity(filled).interaction(interaction).build();
    execProposalEmitter.fire(proposal);
    LoginProvider result = proposal.getEntity();
    LoginProvider saved = gateway.update(original, result);
    LoginProviderDisableEvent event =
        LoginProviderDisableEvent.builder().payload(saved).interaction(interaction).build();
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
