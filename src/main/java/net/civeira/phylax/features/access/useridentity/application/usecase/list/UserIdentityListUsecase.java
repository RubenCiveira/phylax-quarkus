package net.civeira.phylax.features.access.useridentity.application.usecase.list;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.algorithms.metadata.TimestampedList;
import net.civeira.phylax.common.algorithms.metadata.WrapMetadata;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.useridentity.application.projection.UserIdentityStateProyection;
import net.civeira.phylax.features.access.useridentity.application.service.visibility.UserIdentitysVisibility;
import net.civeira.phylax.features.access.useridentity.gateway.UserIdentityCached;
import net.civeira.phylax.features.access.useridentity.query.UserIdentityCursor;
import net.civeira.phylax.features.access.useridentity.query.UserIdentityFilter;

@ApplicationScoped
@RequiredArgsConstructor
public class UserIdentityListUsecase {

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final Event<UserIdentityAllowListProposal> listAllowEmitter;

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final UserIdentitysVisibility visibility;

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    UserIdentityAllowListProposal proposal = UserIdentityAllowListProposal.builder()
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
  public List<UserIdentityStateProyection> list(final Interaction query,
      final UserIdentityFilter filter, final UserIdentityCursor cursor) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    UserIdentityCached values = visibility.listCachedVisibles(query, filter, cursor);
    List<UserIdentityStateProyection> list =
        values.getValue().stream().map(value -> visibility.copyWithHidden(query, value)).toList();
    return new TimestampedList<>(WrapMetadata.<List<UserIdentityStateProyection>>builder()
        .data(list).since(values.getSince().toInstant()).build());
  }
}
