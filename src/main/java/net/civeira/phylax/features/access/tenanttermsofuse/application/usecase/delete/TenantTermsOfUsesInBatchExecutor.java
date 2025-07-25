package net.civeira.phylax.features.access.tenanttermsofuse.application.usecase.delete;

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
import net.civeira.phylax.features.access.tenanttermsofuse.application.visibility.TenantTermsOfUsesVisibility;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.TenantTermsOfUse;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.gateway.TenantTermsOfUseCursor;

@Unremovable
@ApplicationScoped
@RequiredArgsConstructor
class TenantTermsOfUsesInBatchExecutor implements
    StepCounter<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsesInBatchExecutor.TenantTermsOfUsePaginableBatch>,
    StepFinalizer<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsesInBatchExecutor.TenantTermsOfUsePaginableBatch>,
    StepInitializer<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsesInBatchExecutor.TenantTermsOfUsePaginableBatch>,
    ItemReader<TenantTermsOfUse, TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsesInBatchExecutor.TenantTermsOfUsePaginableBatch>,
    ItemWriter<TenantTermsOfUse, TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsesInBatchExecutor.TenantTermsOfUsePaginableBatch>,
    ItemProcessor<TenantTermsOfUse, TenantTermsOfUse, TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsesInBatchExecutor.TenantTermsOfUsePaginableBatch>,
    ItemDescriptor<TenantTermsOfUse, TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsesInBatchExecutor.TenantTermsOfUsePaginableBatch> {

  /**
   * @autogenerated EntityGenerator
   */
  @Data
  public static class TenantTermsOfUsePaginableBatch {

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
  private final TenantTermsOfUseDeleteUsecase usecase;

  /**
   * @autogenerated EntityGenerator
   */
  private final TenantTermsOfUsesVisibility visibility;

  /**
   * @autogenerated EntityGenerator
   * @param context
   * @return
   */
  @Override
  public long approximatedItems(
      final StepContext<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsePaginableBatch> context) {
    return visibility.countVisibles(context.getParams().getInteraction(),
        context.getParams().getFilter());
  }

  /**
   * @autogenerated EntityGenerator
   * @param context
   */
  @Override
  public void finish(
      final StepContext<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsePaginableBatch> context) {
    usecase.flush();
  }

  /**
   * @autogenerated EntityGenerator
   * @param context
   */
  @Override
  public void init(
      final StepContext<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsePaginableBatch> context) {
    context.setState(new TenantTermsOfUsePaginableBatch());
  }

  /**
   * @autogenerated EntityGenerator
   * @param item
   * @param context
   * @return
   */
  @Override
  public String itemDescription(final TenantTermsOfUse item,
      final StepContext<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsePaginableBatch> context) {
    return item.getUid();
  }

  /**
   * @autogenerated EntityGenerator
   * @param item
   * @param context
   * @return
   */
  @Override
  public TenantTermsOfUse process(final TenantTermsOfUse item,
      final StepContext<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsePaginableBatch> context) {
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
  public List<TenantTermsOfUse> read(
      final StepContext<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsePaginableBatch> context) {
    List<TenantTermsOfUse> page =
        visibility.listVisiblesForUpdate(context.getParams().getInteraction(),
            context.getParams().getFilter(), TenantTermsOfUseCursor.builder().limit(size)
                .sinceUid(context.getState().getSince()).build());
    context.getState().setSince(page.isEmpty() ? null : page.get(page.size() - 1).getUid());
    return page;
  }

  /**
   * @autogenerated EntityGenerator
   * @param items
   * @param context
   */
  @Override
  public void write(final List<TenantTermsOfUse> items,
      final StepContext<TenantTermsOfUseDeleteAllInBatchCommand, TenantTermsOfUsePaginableBatch> context) {
    items.forEach(item -> usecase.delete(context.getParams().getInteraction(), item));
  }
}
