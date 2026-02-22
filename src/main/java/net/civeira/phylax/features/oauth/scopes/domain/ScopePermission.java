package net.civeira.phylax.features.oauth.scopes.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScopePermission {
  private final String scope;
  private final boolean required;
  private final String label;
  private final String description;
}
