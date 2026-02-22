/**
 * Use cases for registration, login, and password changes.
 *
 * Responsibilities: - Orchestrate user login and registration flows. - Coordinate consent and
 * password updates.
 *
 * Design notes: - Application services are transport-agnostic. - Delegates data access to domain
 * gateways.
 *
 * Dependencies: - Depends on user domain gateways.
 *
 * Stability: internal application services.
 */
package net.civeira.phylax.features.oauth.user.application;
