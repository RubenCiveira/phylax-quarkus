package net.civeira.phylax.features.oauth.profile.domain.gateway;

import java.util.List;

import net.civeira.phylax.features.oauth.profile.domain.ActiveSession;

public interface SessionsGateway {

  List<ActiveSession> listByUser(String userUid);

  void revoke(String sessionId);
}
