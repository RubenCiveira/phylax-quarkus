/**
 * Domain model for keys, JWK, and configuration.
 *
 * Responsibilities: - Represent key pairs, public keys, and configs. - Model JWKS and JKS
 * structures.
 *
 * Design notes: - Focused on immutable, transport-neutral types. - Used by signing and token
 * building flows.
 *
 * Dependencies: - Minimal dependencies on other modules.
 *
 * Stability: core crypto domain.
 */
package net.civeira.phylax.features.oauth.key.domain;
