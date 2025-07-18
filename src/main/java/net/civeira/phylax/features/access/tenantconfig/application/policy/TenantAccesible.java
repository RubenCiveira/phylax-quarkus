package net.civeira.phylax.features.access.tenantconfig.application.policy;

import jakarta.enterprise.event.Observes;
import net.civeira.phylax.common.security.Actor;
import net.civeira.phylax.features.access.tenantconfig.application.visibility.TenantConfigVisibilityCheck;

public class TenantAccesible {

  /**
   * Constant for root field
   *
   * @autogenerated VisibilityPolicyGenerator
   */
  private static final String CLAIM_ROOT = "root";

  /**
   * @autogenerated VisibilityPolicyGenerator
   * @param filter
   */
  public void filterVisibles(@Observes TenantConfigVisibilityCheck filter) {
    Actor actor = filter.getInteraction().getActor();
    if (!"true".equals(actor.getClaim(CLAIM_ROOT))) {
      filter.peek(fl -> fl.setTenantTenantAccesible(actor.getTenant().orElse(null)));
    }
  }
}
