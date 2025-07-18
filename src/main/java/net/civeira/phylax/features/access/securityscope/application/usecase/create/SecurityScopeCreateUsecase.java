package net.civeira.phylax.features.access.securityscope.application.usecase.create;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.securityscope.application.visibility.SecurityScopesVisibility;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScope;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScopeChangeSet;
import net.civeira.phylax.features.access.securityscope.domain.gateway.SecurityScopeCacheGateway;
import net.civeira.phylax.features.access.securityscope.domain.gateway.SecurityScopeWriteRepositoryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class SecurityScopeCreateUsecase {

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final SecurityScopeCacheGateway cache;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<SecurityScopeCreateAllowDecision> createAllowEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<SecurityScopeCreateCheck> createCheckEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<SecurityScopeCreateEnrich> createEnrichEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final SecurityScopeWriteRepositoryGateway gateway;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final SecurityScopesVisibility visibility;

  /**
   * @autogenerated CreateUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    SecurityScopeCreateAllowDecision proposal = SecurityScopeCreateAllowDecision.builder()
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
  public SecurityScopeCreateProjection create(final Interaction query,
      final SecurityScopeCreateInput input) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    SecurityScopeCreateInput filled = SecurityScopeCreateInput
        .fromChangeSet(visibility.copyWithFixed(query, input.toChangeSet()));
    SecurityScopeCreateCheck check =
        SecurityScopeCreateCheck.builder().interaction(query).input(filled).build();
    createCheckEmitter.fire(check);
    SecurityScopeCreateEnrich proposal =
        SecurityScopeCreateEnrich.builder().interaction(query).input(filled).build();
    createEnrichEmitter.fire(proposal);
    SecurityScopeChangeSet dto = proposal.getInput().toChangeSet();
    SecurityScope entity = SecurityScope.create(dto);
    SecurityScope created =
        gateway.create(entity, attempt -> visibility.checkVisibility(query, attempt.getUid()));
    cache.update(created);
    return SecurityScopeCreateProjection.from(visibility.copyWithHidden(query, created));
  }
}
