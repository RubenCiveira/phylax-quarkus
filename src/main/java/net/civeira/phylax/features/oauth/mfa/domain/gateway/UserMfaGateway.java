package net.civeira.phylax.features.oauth.mfa.domain.gateway;

import java.util.Locale;
import net.civeira.phylax.features.oauth.mfa.domain.PublicLoginMfaBuildResponse;

public interface UserMfaGateway {

  PublicLoginMfaBuildResponse configurationForNewMfa(String tenant, String username, Locale locale);

  boolean verifyOtp(String tenant, String username, String otp);

  boolean verifyNewOtp(String tenant, String username, String otp);

  void storeSeed(String tenant, String username, String seed);
}
