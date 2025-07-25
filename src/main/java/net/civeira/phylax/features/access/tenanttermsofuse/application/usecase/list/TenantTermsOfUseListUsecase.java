package net.civeira.phylax.features.access.tenanttermsofuse.application.usecase.list;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.algorithms.metadata.TimestampedList;
import net.civeira.phylax.common.algorithms.metadata.WrapMetadata;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.tenanttermsofuse.application.visibility.TenantTermsOfUseVisibilityFilter;
import net.civeira.phylax.features.access.tenanttermsofuse.application.visibility.TenantTermsOfUsesVisibility;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.gateway.TenantTermsOfUseCached;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.gateway.TenantTermsOfUseCursor;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantTermsOfUseListUsecase {

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final Event<TenantTermsOfUseListAllowDecision> listAllowEmitter;

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final TenantTermsOfUsesVisibility visibility;

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    TenantTermsOfUseListAllowDecision proposal = TenantTermsOfUseListAllowDecision.builder()
        .detail(Allow.builder().allowed(true).description("Allow by default").build()).query(query)
        .build();
    listAllowEmitter.fire(proposal);
    return proposal.getDetail();
  }

  /**
   * Recover a slide of data.
   *
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @param filter
   * @param cursor
   * @return The slide with some values
   */
  public List<TenantTermsOfUseListProjection> list(final Interaction query,
      final TenantTermsOfUseListFilter filter, final TenantTermsOfUseListCursor cursor) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    TenantTermsOfUseVisibilityFilter visibleFilter =
        TenantTermsOfUseVisibilityFilter.builder().uid(filter.getUid().orElse(null))
            .uids(filter.getUids().stream().toList()).search(filter.getSearch().orElse(null))
            .tenant(filter.getTenant().orElse(null)).tenants(filter.getTenants())
            .tenantTenantAccesible(filter.getTenantTenantAccesible().orElse(null)).build();
    TenantTermsOfUseCursor gatewayCursor = TenantTermsOfUseCursor.builder()
        .limit(cursor.getLimit().orElse(null)).sinceUid(cursor.getSinceUid().orElse(null)).build();
    TenantTermsOfUseCached values =
        visibility.listCachedVisibles(query, visibleFilter, gatewayCursor);
    List<TenantTermsOfUseListProjection> list =
        values.getValue().stream().map(value -> visibility.copyWithHidden(query, value))
            .map(TenantTermsOfUseListProjection::from).toList();
    return new TimestampedList<>(WrapMetadata.<List<TenantTermsOfUseListProjection>>builder()
        .data(list).since(values.getSince().toInstant()).build());
  }
}
