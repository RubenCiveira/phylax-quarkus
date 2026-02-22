/**
 * REST endpoints for RBAC registration and checks.
 *
 * Responsibilities: - Expose permission checks over HTTP. - Allow resource registration for
 * clients.
 *
 * Design notes: - Keeps HTTP binding localized to this package. - Delegates to RBAC gateways and
 * services.
 *
 * Dependencies: - Uses RBAC domain ports and models.
 *
 * Stability: internal REST delivery.
 */
package net.civeira.phylax.features.oauth.rbac.infrastructure.driven.rest;
