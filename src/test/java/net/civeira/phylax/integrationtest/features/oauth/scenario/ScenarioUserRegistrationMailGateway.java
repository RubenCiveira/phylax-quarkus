package net.civeira.phylax.integrationtest.features.oauth.scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.user.domain.RegistrationNotificationData;
import net.civeira.phylax.features.oauth.user.domain.gateway.UserRegistrationMailGateway;

@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioUserRegistrationMailGateway implements UserRegistrationMailGateway {

  private final List<RegistrationNotificationData> sent = new ArrayList<>();

  @Override
  public void sendRegistrationVerification(RegistrationNotificationData data) {
    sent.add(data);
  }

  public List<RegistrationNotificationData> getSent() {
    return Collections.unmodifiableList(sent);
  }

  public void reset() {
    sent.clear();
  }
}
