package net.civeira.phylax.features.oauth.magiclink.domain.gateway;

import java.util.Optional;

import net.civeira.phylax.features.oauth.magiclink.domain.MagicLink;

public interface MagicLinkGateway {

  void store(MagicLink magicLink);

  Optional<MagicLink> findByHash(String tokenHash, String tenantId);

  void markUsed(String uid);
}
