/**
 * Delegated access to external providers.
 *
 * Responsibilities: - Integrate external identity providers. - Map delegated tokens into local
 * identities.
 *
 * Design notes: - Supports multiple providers through gateways. - Encapsulates provider-specific
 * details.
 *
 * Dependencies: - Uses user login flows and token handling.
 *
 * Stability: internal delegated access module.
 */
package net.civeira.phylax.features.oauth.delegated;
