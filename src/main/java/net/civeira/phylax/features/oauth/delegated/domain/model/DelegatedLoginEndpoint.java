package net.civeira.phylax.features.oauth.delegated.domain.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents the target endpoint for a delegated (SSO) login redirection. Replaces/supersedes
 * {@link DelegatedRequestDetails}.
 */
@Getter
@Builder
public class DelegatedLoginEndpoint {
  private final String provider;
  private final String externalUrl;
  /** HTTP method to use when redirecting: "GET" or "POST". */
  private final String method;
}
