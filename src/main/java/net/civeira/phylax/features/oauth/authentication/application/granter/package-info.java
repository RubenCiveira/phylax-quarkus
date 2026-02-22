/**
 * Grant strategies for authentication and refresh flows.
 *
 * Responsibilities: - Implement grant-type specific authentication steps. - Translate parameters
 * into domain authentication requests.
 *
 * Design notes: - Each strategy handles a single grant type. - Shared contract enables pluggable
 * granters.
 *
 * Dependencies: - Uses LoginUsecase and token verifiers.
 *
 * Stability: internal grant handling.
 */
package net.civeira.phylax.features.oauth.authentication.application.granter;
