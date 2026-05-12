package net.civeira.phylax.features.oauth.magiclink.domain.gateway;

import java.time.OffsetDateTime;

import net.civeira.phylax.features.access.tenant.domain.TenantRef;

public interface MagicLinkMailGateway {

  void sendMagicLink(String toEmail, String userName, TenantRef tenant, String magicUrl,
      OffsetDateTime expiresAt);
}
