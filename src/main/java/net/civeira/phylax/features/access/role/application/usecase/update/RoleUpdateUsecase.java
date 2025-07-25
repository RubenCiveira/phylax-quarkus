package net.civeira.phylax.features.access.role.application.usecase.update;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.role.application.visibility.RolesVisibility;
import net.civeira.phylax.features.access.role.domain.Role;
import net.civeira.phylax.features.access.role.domain.RoleChangeSet;
import net.civeira.phylax.features.access.role.domain.RoleRef;
import net.civeira.phylax.features.access.role.domain.gateway.RoleCacheGateway;
import net.civeira.phylax.features.access.role.domain.gateway.RoleWriteRepositoryGateway;

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
  private final RoleWriteRepositoryGateway gateway;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final Event<RoleUpdateAllowDecision> updateAllowEmitter;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final Event<RoleUpdateCheck> updateCheckEmitter;

  /**
   * @autogenerated UpdateUsecaseGenerator
   */
  private final Event<RoleUpdateEnrich> updateEnrichEmitter;

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
    RoleUpdateAllowDecision base = RoleUpdateAllowDecision.builder()
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
    RoleUpdateAllowDecision base = RoleUpdateAllowDecision.builder()
        .detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).build();
    updateAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * Recover a slide of data.
   *
   * @autogenerated UpdateUsecaseGenerator
   * @param query
   * @param reference
   * @param input
   * @return The slide with some values
   */
  public RoleUpdateProjection update(final Interaction query, final RoleRef reference,
      final RoleUpdateInput input) {
    Allow detail = allow(query, reference);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    Role original = visibility.retrieveVisibleForUpdate(query, reference.getUid())
        .orElseThrow(() -> new NotFoundException(""));
    RoleUpdateInput filled =
        RoleUpdateInput.fromChangeSet(visibility.copyWithFixed(query, input.toChangeSet()));
    RoleUpdateCheck check =
        RoleUpdateCheck.builder().input(filled).interaction(query).reference(reference).build();
    updateCheckEmitter.fire(check);
    RoleUpdateEnrich proposal =
        RoleUpdateEnrich.builder().input(filled).interaction(query).reference(reference).build();
    updateEnrichEmitter.fire(proposal);
    RoleChangeSet dto = proposal.getInput().toChangeSet();
    Role saved = gateway.update(original, original.update(dto));
    cache.update(saved);
    return RoleUpdateProjection.from(visibility.copyWithHidden(query, saved));
  }
}
