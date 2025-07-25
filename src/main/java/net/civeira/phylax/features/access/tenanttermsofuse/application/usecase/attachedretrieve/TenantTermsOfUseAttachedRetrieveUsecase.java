package net.civeira.phylax.features.access.tenanttermsofuse.application.usecase.attachedretrieve;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.infrastructure.store.BinaryContent;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenanttermsofuse.application.visibility.TenantTermsOfUsesVisibility;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.TenantTermsOfUse;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.TenantTermsOfUseRef;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.gateway.TenantTermsOfUseAttachedUploadGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantTermsOfUseAttachedRetrieveUsecase {

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final Event<TenantTermsOfUseAttachedRetrieveAllowDecision> retrieveAllowEmitter;

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final TenantTermsOfUseAttachedUploadGateway store;

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final TenantTermsOfUsesVisibility visibility;

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @param reference
   * @return
   */
  public Allow allow(final Interaction query, final TenantTermsOfUseRef reference) {
    TenantTermsOfUseAttachedRetrieveAllowDecision base =
        TenantTermsOfUseAttachedRetrieveAllowDecision.builder()
            .detail(Allow.builder().allowed(true).description("Allowed by default").build())
            .query(query).reference(reference).build();
    retrieveAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    TenantTermsOfUseAttachedRetrieveAllowDecision base =
        TenantTermsOfUseAttachedRetrieveAllowDecision.builder()
            .detail(Allow.builder().allowed(true).description("Allowed by default").build())
            .query(query).build();
    retrieveAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @param reference
   * @return The slide with some values
   */
  public BinaryContent read(final Interaction query, final TenantTermsOfUseRef reference) {
    Allow detail = allow(query, reference);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    TenantTermsOfUse entity = visibility.retrieveVisible(query, reference.getUid())
        .orElseThrow(() -> new NotFoundException(""));
    return store.readAttached(entity).orElseThrow(() -> new NotFoundException(""));
  }
}
