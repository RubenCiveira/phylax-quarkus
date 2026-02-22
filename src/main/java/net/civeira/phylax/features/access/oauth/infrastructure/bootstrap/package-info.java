/**
 * Startup initialization for the access/OAuth module.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Configure default trusted clients, scopes and RBAC on first startup.</li>
 * <li>Register resource scopes exposed by relying parties.</li>
 * </ul>
 *
 * <p>
 * Stability: low — evolves as bootstrapping requirements change.
 */
package net.civeira.phylax.features.access.oauth.infrastructure.bootstrap;
