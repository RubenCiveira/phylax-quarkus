package net.civeira.phylax.features.oauth.profile.domain.gateway;

import net.civeira.phylax.features.oauth.profile.domain.MfaSetup;

public interface MfaGateway {

  boolean isEnabled(String userUid, String tenant);

  MfaSetup buildSetup(String userUid, String tenant);

  void enable(String userUid, String tenant, String seed, String otp);

  void disable(String userUid, String tenant);
}
