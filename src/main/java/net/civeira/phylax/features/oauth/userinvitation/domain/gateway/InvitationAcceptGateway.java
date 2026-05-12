package net.civeira.phylax.features.oauth.userinvitation.domain.gateway;

import net.civeira.phylax.features.access.userinvitation.domain.UserInvitation;

/**
 * Encapsulates the cross-BC accept operation: creates a user from the invitation and marks the
 * invitation as accepted in a single transaction.
 */
public interface InvitationAcceptGateway {

  /**
   * Creates a user with the given password, marks the invitation as accepted, and returns the
   * resulting user UID.
   */
  String acceptAndCreateUser(UserInvitation invitation, String password, String tenantName);
}
