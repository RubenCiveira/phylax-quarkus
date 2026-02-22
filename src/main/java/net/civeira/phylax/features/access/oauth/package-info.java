/**
 * Concrete implementation of the OAuth/OIDC protocol layer.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Implement all gateway ports defined in {@code features/oauth/}.</li>
 * <li>Integrate with the persistence, encryption and notification infrastructure.</li>
 * <li>Provide the business logic for credential validation, MFA, consent and client
 * authorization.</li>
 * </ul>
 *
 * <p>
 * Design notes:
 * <ul>
 * <li>This module has no knowledge of HTTP or HTML — it only implements domain ports.</li>
 * <li>Entry points are the {@code *Interactor} classes in {@code infrastructure.driver.impl.*},
 * which are thin transactional adapters delegating to the usecases in
 * {@code application.usecase}.</li>
 * </ul>
 *
 * <p>
 * Dependencies:
 * <ul>
 * <li>Depends on {@code features/oauth/} domain and gateway interfaces.</li>
 * <li>Depends on the common repository and service infrastructure.</li>
 * </ul>
 *
 * @see <a href="../../../../../../../../../docs/oauth/architecture.md">Architecture overview</a>
 */
package net.civeira.phylax.features.access.oauth;
