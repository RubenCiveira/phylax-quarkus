package net.civeira.phylax.features.access.tenantconfig.application.usecase.create;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenantconfig.application.visibility.TenantConfigsVisibility;
import net.civeira.phylax.features.access.tenantconfig.domain.TenantConfig;
import net.civeira.phylax.features.access.tenantconfig.domain.TenantConfigChangeSet;
import net.civeira.phylax.features.access.tenantconfig.domain.gateway.TenantConfigCacheGateway;
import net.civeira.phylax.features.access.tenantconfig.domain.gateway.TenantConfigWriteRepositoryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantConfigCreateUsecase {

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final TenantConfigCacheGateway cache;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<TenantConfigCreateAllowDecision> createAllowEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<TenantConfigCreateCheck> createCheckEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<TenantConfigCreateEnrich> createEnrichEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final TenantConfigWriteRepositoryGateway gateway;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final TenantConfigsVisibility visibility;

  /**
   * @autogenerated CreateUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    TenantConfigCreateAllowDecision proposal = TenantConfigCreateAllowDecision.builder()
        .detail(Allow.builder().allowed(true).description("Allow by default").build()).query(query)
        .build();
    createAllowEmitter.fire(proposal);
    return proposal.getDetail();
  }

  /**
   * Recover a slide of data.
   *
   * @autogenerated CreateUsecaseGenerator
   * @param query
   * @param input
   * @return The slide with some values
   */
  public TenantConfigCreateProjection create(final Interaction query,
      final TenantConfigCreateInput input) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    TenantConfigCreateInput filled =
        TenantConfigCreateInput.fromChangeSet(visibility.copyWithFixed(query, input.toChangeSet()));
    TenantConfigCreateCheck check =
        TenantConfigCreateCheck.builder().interaction(query).input(filled).build();
    createCheckEmitter.fire(check);
    TenantConfigCreateEnrich proposal =
        TenantConfigCreateEnrich.builder().interaction(query).input(filled).build();
    createEnrichEmitter.fire(proposal);
    TenantConfigChangeSet dto = proposal.getInput().toChangeSet();
    TenantConfig entity = TenantConfig.create(dto);
    TenantConfig created =
        gateway.create(entity, attempt -> visibility.checkVisibility(query, attempt.getUid()));
    cache.update(created);
    return TenantConfigCreateProjection.from(visibility.copyWithHidden(query, created));
  }
}
