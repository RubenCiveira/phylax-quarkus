/**
 * Authentication flows and credential issuance.
 *
 * Responsibilities: - Coordinate UI and API entry points for login. - Enforce required challenges
 * before issuing tokens.
 *
 * Design notes: - Separates domain rules from delivery adapters. - Supports OIDC authorization and
 * token flows.
 *
 * Dependencies: - Relies on user, session, and token features.
 *
 * Stability: internal application module.
 */
package net.civeira.phylax.features.oauth.authentication;
