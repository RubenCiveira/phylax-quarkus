package net.civeira.phylax.features.access.loginprovider.application.usecase.enable;

import java.util.List;

import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.batch.stepper.ItemDescriptor;
import net.civeira.phylax.common.batch.stepper.ItemProcessor;
import net.civeira.phylax.common.batch.stepper.ItemReader;
import net.civeira.phylax.common.batch.stepper.ItemWriter;
import net.civeira.phylax.common.batch.stepper.StepContext;
import net.civeira.phylax.common.batch.stepper.StepCounter;
import net.civeira.phylax.common.batch.stepper.StepFinalizer;
import net.civeira.phylax.common.batch.stepper.StepInitializer;
import net.civeira.phylax.common.exception.ExecutionException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.features.access.loginprovider.application.visibility.LoginProvidersVisibility;
import net.civeira.phylax.features.access.loginprovider.domain.LoginProvider;
import net.civeira.phylax.features.access.loginprovider.domain.gateway.LoginProviderCursor;

@Unremovable
@ApplicationScoped
@RequiredArgsConstructor
class LoginProviderEnablesInBatchExecutor implements
    StepCounter<LoginProviderEnableAllInBatchCommand, LoginProviderEnablesInBatchExecutor.LoginProviderPaginableBatch>,
    StepFinalizer<LoginProviderEnableAllInBatchCommand, LoginProviderEnablesInBatchExecutor.LoginProviderPaginableBatch>,
    StepInitializer<LoginProviderEnableAllInBatchCommand, LoginProviderEnablesInBatchExecutor.LoginProviderPaginableBatch>,
    ItemReader<LoginProvider, LoginProviderEnableAllInBatchCommand, LoginProviderEnablesInBatchExecutor.LoginProviderPaginableBatch>,
    ItemWriter<LoginProvider, LoginProviderEnableAllInBatchCommand, LoginProviderEnablesInBatchExecutor.LoginProviderPaginableBatch>,
    ItemProcessor<LoginProvider, LoginProvider, LoginProviderEnableAllInBatchCommand, LoginProviderEnablesInBatchExecutor.LoginProviderPaginableBatch>,
    ItemDescriptor<LoginProvider, LoginProviderEnableAllInBatchCommand, LoginProviderEnablesInBatchExecutor.LoginProviderPaginableBatch> {

  /**
   * @autogenerated EntityGenerator
   */
  @Data
  public static class LoginProviderPaginableBatch {

    /**
     * @autogenerated EntityGenerator
     */
    private String since;
  }

  /**
   * @autogenerated EntityGenerator
   */
  private final int size = 10;

  /**
   * @autogenerated EntityGenerator
   */
  private final LoginProviderEnableUsecase usecase;

  /**
   * @autogenerated EntityGenerator
   */
  private final LoginProvidersVisibility visibility;

  /**
   * @autogenerated EntityGenerator
   * @param context
   * @return
   */
  @Override
  public long approximatedItems(
      final StepContext<LoginProviderEnableAllInBatchCommand, LoginProviderPaginableBatch> context) {
    return visibility.countVisibles(context.getParams().getInteraction(),
        context.getParams().getFilter());
  }

  /**
   * @autogenerated EntityGenerator
   * @param context
   */
  @Override
  public void finish(
      final StepContext<LoginProviderEnableAllInBatchCommand, LoginProviderPaginableBatch> context) {
    usecase.flush();
  }

  /**
   * @autogenerated EntityGenerator
   * @param context
   */
  @Override
  public void init(
      final StepContext<LoginProviderEnableAllInBatchCommand, LoginProviderPaginableBatch> context) {
    context.setState(new LoginProviderPaginableBatch());
  }

  /**
   * @autogenerated EntityGenerator
   * @param item
   * @param context
   * @return
   */
  @Override
  public String itemDescription(final LoginProvider item,
      final StepContext<LoginProviderEnableAllInBatchCommand, LoginProviderPaginableBatch> context) {
    return item.getUid();
  }

  /**
   * @autogenerated EntityGenerator
   * @param item
   * @param context
   * @return
   */
  @Override
  public LoginProvider process(final LoginProvider item,
      final StepContext<LoginProviderEnableAllInBatchCommand, LoginProviderPaginableBatch> context) {
    Allow allowed = usecase.allow(context.getParams().getInteraction(), item);
    if (!allowed.isAllowed()) {
      throw new ExecutionException("not-allowed", null);
    }
    return item;
  }

  /**
   * @autogenerated EntityGenerator
   * @param context
   * @return
   */
  @Override
  public List<LoginProvider> read(
      final StepContext<LoginProviderEnableAllInBatchCommand, LoginProviderPaginableBatch> context) {
    List<LoginProvider> page = visibility.listVisiblesForUpdate(
        context.getParams().getInteraction(), context.getParams().getFilter(),
        LoginProviderCursor.builder().limit(size).sinceUid(context.getState().getSince()).build());
    context.getState().setSince(page.isEmpty() ? null : page.get(page.size() - 1).getUid());
    return page;
  }

  /**
   * @autogenerated EntityGenerator
   * @param items
   * @param context
   */
  @Override
  public void write(final List<LoginProvider> items,
      final StepContext<LoginProviderEnableAllInBatchCommand, LoginProviderPaginableBatch> context) {
    items.forEach(item -> usecase.enable(context.getParams().getInteraction(), item));
  }
}
