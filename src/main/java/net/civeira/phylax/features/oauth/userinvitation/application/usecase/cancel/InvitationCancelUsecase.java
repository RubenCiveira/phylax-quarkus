package net.civeira.phylax.features.oauth.userinvitation.application.usecase.cancel;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.features.oauth.userinvitation.domain.gateway.InvitationStoreGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class InvitationCancelUsecase {

  private final InvitationStoreGateway store;

  public void cancel(String uid) {
    store.cancel(uid);
  }
}
