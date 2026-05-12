package net.civeira.phylax.features.oauth.userinvitation.application.usecase.create;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class InvitationCreateParams {

  private final String tenantName;
  private final String tenantId;
  private final String email;
  private final String invitedByUserUid;
  private final String invitedByEmail;
  private final String baseUrl;
  private final String roles;
  private final String metadataJson;
  @Builder.Default
  private final String clientId = "";
  @Builder.Default
  private final String redirectUri = "";
}
