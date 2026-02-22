/**
 * OIDC functionality and provider metadata.
 *
 * Responsibilities: - Expose OpenID provider configuration. - Represent metadata for discovery
 * endpoints.
 *
 * Design notes: - Keeps OIDC metadata independent from token flows. - Supports mtls alias
 * representations.
 *
 * Dependencies: - Uses key and token modules for metadata.
 *
 * Stability: internal OIDC module.
 */
package net.civeira.phylax.features.oauth.oidc;
