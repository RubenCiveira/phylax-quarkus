package net.civeira.phylax.features.access.role.application.usecase.update;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.role.Role;
import net.civeira.phylax.features.access.role.RoleFacade;
import net.civeira.phylax.features.access.role.RoleRef;
import net.civeira.phylax.features.access.role.application.projection.RoleStateProyection;
import net.civeira.phylax.features.access.role.application.request.RoleStateChange;
import net.civeira.phylax.features.access.role.application.service.visibility.RolesVisibility;
import net.civeira.phylax.features.access.role.gateway.RoleCacheGateway;
import net.civeira.phylax.features.access.role.gateway.RoleWriteRepositoryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class RoleUpdateUsecase {

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final RoleCacheGateway cache;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final RoleFacade facade;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final RoleWriteRepositoryGateway gateway;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final Event<RoleAllowUpdateProposal> updateAllowEmitter;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final Event<RoleUpdateProposal> updateProposalEmitter;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final Event<RoleUpdateEvent> updatedEmitter;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final RolesVisibility visibility;

  /**
   * @autogenerated UpdateUsecaseGenerator
   * @param query
   * @param reference
   * @return
   */
  public Allow allow(final Interaction query, final RoleRef reference) {
    RoleAllowUpdateProposal base = RoleAllowUpdateProposal.builder()
        .detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).reference(reference).build();
    updateAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * @autogenerated UpdateUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    RoleAllowUpdateProposal base = RoleAllowUpdateProposal.builder()
        .detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).build();
    updateAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * The slide with some values
   *
   * @autogenerated UpdateUsecaseGenerator
   * @param query
   * @param reference
   * @param input
   * @return The slide with some values
   */
  public RoleStateProyection update(final Interaction query, final RoleRef reference,
      final RoleStateChange input) {
    Allow detail = allow(query, reference);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    Role original = visibility.retrieveVisibleForUpdate(query, reference.getUidValue())
        .orElseThrow(() -> new NotFoundException(""));
    RoleStateChange filled = visibility.copyWithFixed(query, input);
    RoleUpdateProposal proposal =
        RoleUpdateProposal.builder().dto(filled).interaction(query).reference(reference).build();
    updateProposalEmitter.fire(proposal);
    RoleStateChange dto = proposal.getDto();;
    Role saved = gateway.update(original, facade.update(original, dto));
    cache.update(saved);
    RoleUpdateEvent event = RoleUpdateEvent.builder().payload(saved).interaction(query).build();
    updatedEmitter.fire(event);
    return visibility.copyWithHidden(query, event.getPayload());
  }
}
