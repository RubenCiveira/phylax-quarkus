package net.civeira.phylax.features.oauth.userinvitation.application.usecase.accept;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class InvitationAcceptParams {

  private final String rawToken;
  private final String tenantName;
  private final String password;
  private final String clientId;
  private final String redirectUri;
}
