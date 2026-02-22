/**
 * REST controllers for OpenID configuration.
 *
 * Responsibilities: - Expose .well-known OpenID configuration. - Provide metadata for client
 * discovery.
 *
 * Design notes: - Maps domain objects to HTTP responses. - Keeps OpenID configuration logic local.
 *
 * Dependencies: - Uses OIDC domain models.
 *
 * Stability: internal REST endpoints.
 */
package net.civeira.phylax.features.oauth.oidc.infrastructure.driver.rest;
