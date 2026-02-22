package net.civeira.phylax.features.oauth.client.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents an API Key credential with its associated allowed scopes.
 */
@Getter
@Builder
public class ApiKeyData {
  private final String id;
  private final List<String> scopes;
}
