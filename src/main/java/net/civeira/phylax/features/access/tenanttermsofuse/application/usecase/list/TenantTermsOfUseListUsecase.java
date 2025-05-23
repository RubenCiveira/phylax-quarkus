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
import net.civeira.phylax.features.access.tenanttermsofuse.application.projection.TenantTermsOfUseStateProyection;
import net.civeira.phylax.features.access.tenanttermsofuse.application.service.visibility.TenantTermsOfUsesVisibility;
import net.civeira.phylax.features.access.tenanttermsofuse.gateway.TenantTermsOfUseCached;
import net.civeira.phylax.features.access.tenanttermsofuse.query.TenantTermsOfUseCursor;
import net.civeira.phylax.features.access.tenanttermsofuse.query.TenantTermsOfUseFilter;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantTermsOfUseListUsecase {

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final Event<TenantTermsOfUseAllowListProposal> listAllowEmitter;

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
    TenantTermsOfUseAllowListProposal proposal = TenantTermsOfUseAllowListProposal.builder()
        .detail(Allow.builder().allowed(true).description("Allow by default").build()).query(query)
        .build();
    listAllowEmitter.fire(proposal);
    return proposal.getDetail();
  }

  /**
   * The slide with some values
   *
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @param filter
   * @param cursor
   * @return The slide with some values
   */
  public List<TenantTermsOfUseStateProyection> list(final Interaction query,
      final TenantTermsOfUseFilter filter, final TenantTermsOfUseCursor cursor) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    TenantTermsOfUseCached values = visibility.listCachedVisibles(query, filter, cursor);
    List<TenantTermsOfUseStateProyection> list =
        values.getValue().stream().map(value -> visibility.copyWithHidden(query, value)).toList();
    return new TimestampedList<>(WrapMetadata.<List<TenantTermsOfUseStateProyection>>builder()
        .data(list).since(values.getSince().toInstant()).build());
  }
}
