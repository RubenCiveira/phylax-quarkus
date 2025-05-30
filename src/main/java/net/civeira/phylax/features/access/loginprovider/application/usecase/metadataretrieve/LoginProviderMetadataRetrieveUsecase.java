package net.civeira.phylax.features.access.loginprovider.application.usecase.metadataretrieve;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.infrastructure.store.BinaryContent;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.loginprovider.LoginProvider;
import net.civeira.phylax.features.access.loginprovider.LoginProviderRef;
import net.civeira.phylax.features.access.loginprovider.application.service.visibility.LoginProvidersVisibility;
import net.civeira.phylax.features.access.loginprovider.gateway.LoginProviderMetadataUploadGateway;

@ApplicationScoped
@RequiredArgsConstructor
public class LoginProviderMetadataRetrieveUsecase {

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final Event<LoginProviderAllowMetadataRetrieveProposal> retrieveAllowEmitter;

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final LoginProviderMetadataUploadGateway store;

  /**
   * @autogenerated ListUsecaseGenerator
   */
  private final LoginProvidersVisibility visibility;

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @param reference
   * @return
   */
  public Allow allow(final Interaction query, final LoginProviderRef reference) {
    LoginProviderAllowMetadataRetrieveProposal base = LoginProviderAllowMetadataRetrieveProposal
        .builder().detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).reference(reference).build();
    retrieveAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @return
   */
  public Allow allow(final Interaction query) {
    LoginProviderAllowMetadataRetrieveProposal base = LoginProviderAllowMetadataRetrieveProposal
        .builder().detail(Allow.builder().allowed(true).description("Allowed by default").build())
        .query(query).build();
    retrieveAllowEmitter.fire(base);
    return base.getDetail();
  }

  /**
   * The slide with some values
   *
   * @autogenerated ListUsecaseGenerator
   * @param query
   * @param reference
   * @return The slide with some values
   */
  public BinaryContent read(final Interaction query, final LoginProviderRef reference) {
    Allow detail = allow(query, reference);
    if (!detail.isAllowed()) {
      throw new NotAllowedException(detail.getDescription());
    }
    LoginProvider entity = visibility.retrieveVisible(query, reference.getUidValue())
        .orElseThrow(() -> new NotFoundException(""));
    return store.readMetadata(entity).orElseThrow(() -> new NotFoundException(""));
  }
}
