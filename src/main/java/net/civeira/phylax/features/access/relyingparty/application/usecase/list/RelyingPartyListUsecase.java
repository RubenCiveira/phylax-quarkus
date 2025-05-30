package net.civeira.phylax.features.access.relyingparty.application.usecase.list;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.algorithms.metadata.TimestampedList;
import net.civeira.phylax.common.algorithms.metadata.WrapMetadata;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.relyingparty.application.projection.RelyingPartyStateProyection;
import net.civeira.phylax.features.access.relyingparty.application.service.visibility.RelyingPartysVisibility;
import net.civeira.phylax.features.access.relyingparty.gateway.RelyingPartyCached;
import net.civeira.phylax.features.access.relyingparty.query.RelyingPartyCursor;
import net.civeira.phylax.features.access.relyingparty.query.RelyingPartyFilter;

@ApplicationScoped
@RequiredArgsConstructor
public class RelyingPartyListUsecase {

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final Event<RelyingPartyAllowListProposal> listAllowEmitter;

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final RelyingPartysVisibility visibility;

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    RelyingPartyAllowListProposal proposal = RelyingPartyAllowListProposal.builder()
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
  public List<RelyingPartyStateProyection> list(final Interaction query,
      final RelyingPartyFilter filter, final RelyingPartyCursor cursor) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    RelyingPartyCached values = visibility.listCachedVisibles(query, filter, cursor);
    List<RelyingPartyStateProyection> list =
        values.getValue().stream().map(value -> visibility.copyWithHidden(query, value)).toList();
    return new TimestampedList<>(WrapMetadata.<List<RelyingPartyStateProyection>>builder()
        .data(list).since(values.getSince().toInstant()).build());
  }
}
