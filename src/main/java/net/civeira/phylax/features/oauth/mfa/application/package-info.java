/**
 * Use cases for user MFA.
 *
 * Responsibilities: - Orchestrate MFA verification steps. - Provide MFA-related flow helpers.
 *
 * Design notes: - Use cases are transport-independent. - Delegates persistence to gateways.
 *
 * Dependencies: - Depends on MFA domain gateways.
 *
 * Stability: internal application services.
 */
package net.civeira.phylax.features.oauth.mfa.application;
