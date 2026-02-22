/**
 * Infrastructure adapters for OAuth sessions.
 *
 * Responsibilities: - Implement session and temporal key persistence. - Bridge domain models with
 * storage.
 *
 * Design notes: - Driven adapters handle SQL persistence. - Keeps application logic
 * storage-agnostic.
 *
 * Dependencies: - Uses session domain gateways.
 *
 * Stability: internal infrastructure layer.
 */
package net.civeira.phylax.features.oauth.session.infrastructure;
