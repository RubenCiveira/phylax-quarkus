package net.civeira.phylax.features.oauth.device.domain.gateway;

import java.time.OffsetDateTime;
import java.util.Optional;

import net.civeira.phylax.features.oauth.authentication.domain.AuthenticationData;
import net.civeira.phylax.features.oauth.device.domain.DeviceAuthorization;

/**
 * Domain port for Device Authorization storage — RFC 8628.
 */
public interface DeviceAuthorizationGateway {

  void create(DeviceAuthorization authorization);

  Optional<DeviceAuthorization> findByDeviceCode(String deviceCode);

  Optional<DeviceAuthorization> findByUserCode(String tenant, String userCode);

  boolean approve(String deviceCode, AuthenticationData auth);

  boolean deny(String deviceCode);

  void touchPoll(String deviceCode, OffsetDateTime now);

  /** Marks the record as consumed after token exchange. */
  void consume(String deviceCode);
}
