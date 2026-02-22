/**
 * Transactional adapters implementing user-related gateway ports.
 *
 * <p>
 * Each class in this package is a {@code @Transactional @ApplicationScoped} CDI bean that
 * implements one gateway interface and delegates to an application usecase.
 *
 * <ul>
 * <li>{@code UserLoginInteractor} → implements {@code LoginGateway}</li>
 * <li>{@code UserConsentInteractor} → implements {@code ConsentGateway}</li>
 * <li>{@code ChangePasswordGatewayInteractor} → implements {@code ChangePasswordGateway}</li>
 * <li>{@code RegisterUserInteractor} → implements {@code RegisterUserGateway}</li>
 * </ul>
 *
 * <p>
 * Stability: stable; changes only when gateway port signatures change.
 */
package net.civeira.phylax.features.access.oauth.infrastructure.driver.impl.user;
