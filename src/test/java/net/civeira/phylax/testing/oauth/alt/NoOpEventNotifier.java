package net.civeira.phylax.testing.oauth.alt;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.authentication.domain.exception.AuthenticationException;
import net.civeira.phylax.features.oauth.authentication.domain.gateway.EventNotifierGateway;
import net.civeira.phylax.features.oauth.authentication.domain.model.AuthenticationData;

@Alternative
@Priority(1)
@ApplicationScoped
public class NoOpEventNotifier implements EventNotifierGateway {
  @Override
  public void loginOk(AuthenticationData data) {}

  @Override
  public void loginFail(AuthenticationException fail) {}
}
