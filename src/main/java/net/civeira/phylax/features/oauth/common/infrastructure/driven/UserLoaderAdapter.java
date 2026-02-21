package net.civeira.phylax.features.oauth.common.infrastructure.driven;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Centralizes tenant and user validation logic shared across sub-modules (login, password change,
 * register, etc.).
 *
 * TODO: implement with access to: - TenantConfig / TenantStore for tenant existence + enabled
 * checks - UserApproveOptions / UserStore for user existence + enabled + approved + blocked checks
 * - UserAccessTemporalCode for temporary code management
 *
 * Methods to implement: - checkTenant(tenant): verifies tenant exists and is enabled -
 * checkUser(tenant, username): verifies user existence, enabled, not blocked, approved -
 * checkUserByRecoveryCode(tenant, code): finds user by recovery code -
 * checkUserByRegisterCode(tenant, code): finds user by register verification code -
 * userCodeForUpdate(tenant, username): obtains/creates a UserAccessTemporalCode -
 * loadTenantTerms(tenant): returns the tenant's terms-and-conditions text (if any)
 */
@ApplicationScoped
public class UserLoaderAdapter {
  // stub — to be implemented when access feature integration is ready
}
