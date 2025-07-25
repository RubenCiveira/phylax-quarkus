package net.civeira.phylax.features.access.loginprovider.application.policy;

import jakarta.enterprise.event.Observes;
import net.civeira.phylax.common.security.Actor;
import net.civeira.phylax.features.access.loginprovider.application.visibility.LoginProviderEntityEnrichment;
import net.civeira.phylax.features.access.tenant.domain.TenantReference;

public class HeritageTenant {

  /**
   * Constant for root field
   *
   * @autogenerated FormulaPolicyGenerator
   */
  private static final String CLAIM_ROOT = "root";

  /**
   * @autogenerated FormulaPolicyGenerator
   * @param builder
   */
  public void calculate(@Observes LoginProviderEntityEnrichment builder) {
    Actor actor = builder.getInteraction().getActor();
    if (!"true".equals(actor.getClaim(CLAIM_ROOT))) {
      builder.peek(dto -> dto.setTenant(TenantReference.of(actor.getTenant().orElse(null))));
    }
  }
}
