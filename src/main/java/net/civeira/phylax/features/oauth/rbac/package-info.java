/**
 * RBAC and scope-based authorization.
 *
 * Responsibilities: - Model resources, roles, and scope permissions. - Provide permission checks
 * and registrations.
 *
 * Design notes: - Domain keeps authorization rules centralized. - REST adapters expose registration
 * endpoints.
 *
 * Dependencies: - Integrates with token and scope modules.
 *
 * Stability: internal authorization module.
 */
package net.civeira.phylax.features.oauth.rbac;
