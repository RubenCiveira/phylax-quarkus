/**
 * OAuth sessions, temporary codes, and ephemeral keys.
 *
 * Responsibilities: - Model session data and temporary auth codes. - Provide ephemeral key storage
 * interfaces.
 *
 * Design notes: - Domain objects used by auth and token flows. - Adapters persist session state.
 *
 * Dependencies: - Integrates with authentication and tokens.
 *
 * Stability: internal session module.
 */
package net.civeira.phylax.features.oauth.session;
