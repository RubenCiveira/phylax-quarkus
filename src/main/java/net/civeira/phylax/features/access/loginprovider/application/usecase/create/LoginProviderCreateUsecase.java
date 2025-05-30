package net.civeira.phylax.features.access.loginprovider.application.usecase.create;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.loginprovider.LoginProvider;
import net.civeira.phylax.features.access.loginprovider.LoginProviderFacade;
import net.civeira.phylax.features.access.loginprovider.application.projection.LoginProviderStateProyection;
import net.civeira.phylax.features.access.loginprovider.application.request.LoginProviderStateChange;
import net.civeira.phylax.features.access.loginprovider.application.service.visibility.LoginProvidersVisibility;
import net.civeira.phylax.features.access.loginprovider.gateway.LoginProviderCacheGateway;
import net.civeira.phylax.features.access.loginprovider.gateway.LoginProviderWriteRepositoryGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class LoginProviderCreateUsecase {

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final LoginProviderCacheGateway cache;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<LoginProviderAllowCreateProposal> createAllowEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<LoginProviderCreateProposal> createProposalEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final Event<LoginProviderCreateEvent> createdEmitter;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final LoginProviderFacade facade;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final LoginProviderWriteRepositoryGateway gateway;

  /**
   * @autogenerated CreateUsecaseGenerator
   */
  private final LoginProvidersVisibility visibility;

  /**
   * @autogenerated CreateUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    LoginProviderAllowCreateProposal proposal = LoginProviderAllowCreateProposal.builder()
        .detail(Allow.builder().allowed(true).description("Allow by default").build()).query(query)
        .build();
    createAllowEmitter.fire(proposal);
    return proposal.getDetail();
  }

  /**
   * The slide with some values
   *
   * @autogenerated CreateUsecaseGenerator
   * @param query
   * @param input
   * @return The slide with some values
   */
  public LoginProviderStateProyection create(final Interaction query,
      final LoginProviderStateChange input) {
    Allow detail = allow(query);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    LoginProviderStateChange filled = visibility.copyWithFixed(query, input);
    LoginProviderCreateProposal proposal =
        LoginProviderCreateProposal.builder().interaction(query).dto(filled).build();
    createProposalEmitter.fire(proposal);
    LoginProviderStateChange dto = proposal.getDto();
    LoginProvider entity = facade.create(dto);
    LoginProvider created =
        gateway.create(entity, attempt -> visibility.checkVisibility(query, attempt.getUidValue()));
    cache.update(created);
    LoginProviderCreateEvent event =
        LoginProviderCreateEvent.builder().payload(created).interaction(query).build();
    createdEmitter.fire(event);
    return visibility.copyWithHidden(query, event.getPayload());
  }
}
