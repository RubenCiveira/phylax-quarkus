/**
 * Use cases for client management and consents.
 *
 * Responsibilities: - Orchestrate client consent checks and updates. - Provide application-level
 * operations for clients.
 *
 * Design notes: - Keeps workflows independent of transport layers. - Delegates storage to domain
 * gateways.
 *
 * Dependencies: - Depends on client domain gateways.
 *
 * Stability: internal application services.
 */
package net.civeira.phylax.features.oauth.client.application;
