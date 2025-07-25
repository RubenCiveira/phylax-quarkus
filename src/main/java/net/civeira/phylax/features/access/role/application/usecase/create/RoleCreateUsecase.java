package net.civeira.phylax.features.access.role.application.usecase.create;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.role.application.visibility.RolesVisibility;
import net.civeira.phylax.features.access.role.domain.Role;
import net.civeira.phylax.features.access.role.domain.RoleChangeSet;
import net.civeira.phylax.features.access.role.domain.gateway.RoleCacheGateway;
import net.civeira.phylax.features.access.role.domain.gateway.RoleWriteRepositoryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class RoleCreateUsecase {

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final RoleCacheGateway cache;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<RoleCreateAllowDecision> createAllowEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<RoleCreateCheck> createCheckEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<RoleCreateEnrich> createEnrichEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final RoleWriteRepositoryGateway gateway;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final RolesVisibility visibility;

  /**
   * @autogenerated CreateUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    RoleCreateAllowDecision proposal = RoleCreateAllowDecision.builder()
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
  public RoleCreateProjection create(final Interaction query, final RoleCreateInput input) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    RoleCreateInput filled =
        RoleCreateInput.fromChangeSet(visibility.copyWithFixed(query, input.toChangeSet()));
    RoleCreateCheck check = RoleCreateCheck.builder().interaction(query).input(filled).build();
    createCheckEmitter.fire(check);
    RoleCreateEnrich proposal = RoleCreateEnrich.builder().interaction(query).input(filled).build();
    createEnrichEmitter.fire(proposal);
    RoleChangeSet dto = proposal.getInput().toChangeSet();
    Role entity = Role.create(dto);
    Role created =
        gateway.create(entity, attempt -> visibility.checkVisibility(query, attempt.getUid()));
    cache.update(created);
    return RoleCreateProjection.from(visibility.copyWithHidden(query, created));
  }
}
