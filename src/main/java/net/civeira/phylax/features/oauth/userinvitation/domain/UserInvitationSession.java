package net.civeira.phylax.features.oauth.userinvitation.domain;

import java.time.Duration;

public record UserInvitationSession(String sessionId, Duration expiration) {
}
