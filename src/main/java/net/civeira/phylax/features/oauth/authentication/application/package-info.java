/**
 * OAuth authentication use cases.
 *
 * Responsibilities: - Orchestrate authentication flows and decisions. - Invoke domain gateways for
 * user validation.
 *
 * Design notes: - Keeps use case logic free of transport details. - Returns domain results for
 * downstream adapters.
 *
 * Dependencies: - Depends on user, session, and token domains.
 *
 * Stability: internal application services.
 */
package net.civeira.phylax.features.oauth.authentication.application;
