package net.civeira.phylax.integrationtest.features.oauth.scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import net.civeira.phylax.features.oauth.user.domain.RecoveryNotificationData;
import net.civeira.phylax.features.oauth.user.domain.gateway.PasswordRecoveryMailGateway;

@IfBuildProfile("test")
@Alternative
@Priority(1)
@ApplicationScoped
public class ScenarioPasswordRecoveryMailGateway implements PasswordRecoveryMailGateway {

  private final List<RecoveryNotificationData> sent = new ArrayList<>();

  @Override
  public void sendPasswordRecovery(RecoveryNotificationData data) {
    sent.add(data);
  }

  public List<RecoveryNotificationData> getSent() {
    return Collections.unmodifiableList(sent);
  }

  public void reset() {
    sent.clear();
  }
}
