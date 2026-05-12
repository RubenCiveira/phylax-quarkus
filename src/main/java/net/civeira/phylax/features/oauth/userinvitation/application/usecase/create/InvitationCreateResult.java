package net.civeira.phylax.features.oauth.userinvitation.application.usecase.create;

import java.time.OffsetDateTime;

public record InvitationCreateResult(String uid, OffsetDateTime expiresAt) {
}
