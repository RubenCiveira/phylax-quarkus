/**
 * Infrastructure adapters for OAuth users.
 *
 * Responsibilities: - Implement persistence for user data and consents. - Bridge user domain with
 * storage systems.
 *
 * Design notes: - Driven adapters encapsulate storage details. - Keeps application logic
 * storage-agnostic.
 *
 * Dependencies: - Uses user domain gateways.
 *
 * Stability: internal infrastructure layer.
 */
package net.civeira.phylax.features.oauth.user.infrastructure;
