/**
 * Driver adapters for OIDC endpoints.
 *
 * Responsibilities: - Serve discovery metadata over HTTP. - Provide configuration endpoints for
 * clients.
 *
 * Design notes: - REST controllers encapsulate HTTP details. - Delegates metadata building to
 * services.
 *
 * Dependencies: - Uses OIDC domain types.
 *
 * Stability: internal REST delivery.
 */
package net.civeira.phylax.features.oauth.oidc.infrastructure.driver;
