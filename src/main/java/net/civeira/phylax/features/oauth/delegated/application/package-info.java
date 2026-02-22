/**
 * Use cases for delegated login.
 *
 * Responsibilities: - Orchestrate delegated provider flows. - Resolve external tokens into
 * usernames.
 *
 * Design notes: - Keeps flow logic transport-agnostic. - Delegates provider calls to gateways.
 *
 * Dependencies: - Depends on delegated domain gateways.
 *
 * Stability: internal application services.
 */
package net.civeira.phylax.features.oauth.delegated.application;
