/**
 * MFA support for OAuth flows.
 *
 * Responsibilities: - Provide MFA verification and enrollment. - Build public MFA response
 * payloads.
 *
 * Design notes: - Integrates with authentication flows. - Keeps MFA-specific logic in this module.
 *
 * Dependencies: - Uses user and token features.
 *
 * Stability: internal MFA feature set.
 */
package net.civeira.phylax.features.oauth.mfa;
