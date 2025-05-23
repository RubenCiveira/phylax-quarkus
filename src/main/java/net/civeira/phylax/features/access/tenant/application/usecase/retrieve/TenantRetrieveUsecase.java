package net.civeira.phylax.features.access.tenant.application.usecase.retrieve;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenant.TenantRef;
import net.civeira.phylax.features.access.tenant.application.projection.TenantStateProyection;
import net.civeira.phylax.features.access.tenant.application.service.visibility.TenantsVisibility;
import net.civeira.phylax.features.access.tenant.gateway.TenantCached;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantRetrieveUsecase {

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final Event<TenantAllowRetrieveProposal> retrieveAllowEmitter;

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final TenantsVisibility visibility;

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @param reference
   * @return
   */
  public Allow allow(final Interaction query, final TenantRef reference) {
    TenantAllowRetrieveProposal base = TenantAllowRetrieveProposal.builder()
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
    TenantAllowRetrieveProposal base = TenantAllowRetrieveProposal.builder()
        .detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).build();
    retrieveAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * The slide with some values
   *
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @param reference
   * @return The slide with some values
   */
  public TenantStateProyection retrieve(final Interaction query, final TenantRef reference) {
    Allow detail = allow(query, reference);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    TenantCached retrieveCachedVisible =
        visibility.retrieveCachedVisible(query, reference.getUidValue());
    return retrieveCachedVisible.first().map(first -> visibility.copyWithHidden(query, first))
        .orElseThrow(() -> new NotFoundException(""));
  }
}
