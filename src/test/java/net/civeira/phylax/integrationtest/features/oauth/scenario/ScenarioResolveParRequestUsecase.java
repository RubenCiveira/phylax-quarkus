package net.civeira.phylax.integrationtest.features.oauth.scenario;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.par.application.usecase.resolveparrequest.ResolveParRequestUsecase;
import net.civeira.phylax.features.oauth.par.domain.ParRequest;
import net.civeira.phylax.features.oauth.par.domain.exception.ParException;

/**
 * Test alternative for ResolveParRequestUsecase.
 *
 * PAR flow is not exercised by the OIDC integration tests. This stub satisfies the CDI dependency
 * of AuthorizeHtml and throws if invoked unexpectedly.
 */
@IfBuildProfile("test")
@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioResolveParRequestUsecase extends ResolveParRequestUsecase {

  ScenarioResolveParRequestUsecase() {
    super(null);
  }

  @Override
  public ParRequest resolve(String requestUri, String tenant) {
    throw ParException.invalidRequestUri();
  }
}
