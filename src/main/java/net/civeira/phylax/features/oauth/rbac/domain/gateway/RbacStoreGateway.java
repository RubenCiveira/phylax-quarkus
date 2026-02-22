package net.civeira.phylax.features.oauth.rbac.domain.gateway;

import java.util.List;

import net.civeira.phylax.features.oauth.rbac.domain.PropertyList;
import net.civeira.phylax.features.oauth.rbac.domain.RoleGrant;
import net.civeira.phylax.features.oauth.rbac.domain.ScopeList;

public interface RbacStoreGateway {
  void registerScopes(String relayParty, List<ScopeList> paramMap);

  void registerSchema(String relayParty, List<PropertyList> paramMap);

  List<RoleGrant> granted(String relayParty);
}
