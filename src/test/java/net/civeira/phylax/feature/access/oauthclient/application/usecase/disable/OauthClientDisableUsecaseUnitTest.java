package net.civeira.phylax.feature.access.oauthclient.application.usecase.disable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.enterprise.event.Event;
import net.civeira.phylax.common.batch.BatchIdentifier;
import net.civeira.phylax.common.batch.BatchProgress;
import net.civeira.phylax.common.batch.BatchService;
import net.civeira.phylax.common.exception.NotAllowedException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.security.Actor;
import net.civeira.phylax.common.security.InvocationSource;
import net.civeira.phylax.common.security.OperationContext;
import net.civeira.phylax.feature.access.oauthclient.application.visibility.OauthClientVisibleProjection;
import net.civeira.phylax.feature.access.oauthclient.application.visibility.OauthClientsVisibility;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedAudiences;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLoginRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClient;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientRef;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientReference;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientSubjectTypeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientTokenEndpointAuthMethodOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientAuditGateway;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientCacheGateway;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientCommandRepositoryGateway;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.AllowAllScopesVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.AllowedAudiencesVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.AllowedLoginRedirectsVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.AllowedLogoutRedirectsVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.AuthorizationDetailsTypesVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.AuthorizationSignedResponseAlgVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.BackchannelClientNotificationEndpointVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.BackchannelLogoutSessionRequiredVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.BackchannelLogoutUriVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.BackchannelTokenDeliveryModeVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.ClientCredentialsScopesVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.ClientCredentialsTokenTtlSecondsVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.ClientNameVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.ClientUriVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.CodeVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.DefaultAcrValuesVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.DefaultMaxAgeVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.DpopBoundAccessTokensVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.DynamicallyRegisteredVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.EnabledVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.FrontchannelLogoutSessionRequiredVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.FrontchannelLogoutUriVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.GrantTypesJsonVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.IdTokenEncryptedResponseAlgVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.IdTokenEncryptedResponseEncVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.IdTokenSignedResponseAlgVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.IntrospectionAllowedVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.JwksJsonVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.JwksUriVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.LogoUriVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.PolicyUriVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.RegisteredAtVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.RegistrationAccessTokenVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.RequestObjectSigningAlgVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.RequirePkceVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.RequirePushedAuthorizationRequestsVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.ResponseTypesJsonVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.SecretOauthVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.SectorIdentifierUriVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.SubjectTypeVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.TenantVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.TlsClientAuthSubjectDnVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.TlsClientCertificateBoundAccessTokensVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.TokenEndpointAuthMethodVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.TokenEndpointAuthSigningAlgVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.TosUriVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.UidVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.UserinfoEncryptedResponseAlgVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.UserinfoEncryptedResponseEncVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.UserinfoSignedResponseAlgVO;
import net.civeira.phylax.feature.access.oauthclient.domain.valueobject.VersionVO;
import net.civeira.phylax.feature.identity.tenant.domain.TenantReference;

class OauthClientDisableUsecaseUnitTest {

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  @Test
  @DisplayName("Test the permission to disable of oauth client is allowed by default and can be denied")
  void test_oauth_client_disable_usecase_allow() {
    OauthClientAuditGateway audit = Mockito.mock(OauthClientAuditGateway.class);
    BatchService batch = Mockito.mock(BatchService.class);
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableAuthorizationDecision> execAllowEmitter = Mockito.mock(Event.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableCheck> execProposalEmitter = Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway gateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientsVisibility visibility = Mockito.mock(OauthClientsVisibility.class);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClientDisableUsecase usecase = new OauthClientDisableUsecase(audit, batch, cache,
        execAllowEmitter, execProposalEmitter, gateway, visibility);
    Assertions.assertTrue(usecase.allow(context).isAllowed(),
        "Without policies, the action is allowed");
    Assertions.assertTrue(usecase.allow(context, OauthClientReference.of("one")).isAllowed(),
        "Without policies, the action is allowed");
    Mockito.doAnswer(invocation -> {
      ((OauthClientDisableAuthorizationDecision) invocation.getArgument(0)).deny("not allowed");
      return null;
    }).when(execAllowEmitter).fire(Mockito.any());
    Assertions.assertFalse(usecase.allow(context).isAllowed(), "A policy can deny the action");
    Assertions.assertFalse(usecase.allow(context, OauthClientReference.of("one")).isAllowed(),
        "A policy can deny the action");
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  @Test
  @DisplayName("Test disable of oauth client in batch start a batch process")
  void test_oauth_client_disable_usecase_batch() {
    OauthClientAuditGateway audit = Mockito.mock(OauthClientAuditGateway.class);
    BatchService batch = Mockito.mock(BatchService.class);
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableAuthorizationDecision> execAllowEmitter = Mockito.mock(Event.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableCheck> execProposalEmitter = Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway gateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientsVisibility visibility = Mockito.mock(OauthClientsVisibility.class);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClientDisableUsecase usecase = new OauthClientDisableUsecase(audit, batch, cache,
        execAllowEmitter, execProposalEmitter, gateway, visibility);
    Actor actor = Mockito.mock(Actor.class);
    Mockito.when(actor.getName()).thenReturn("tester");
    InvocationSource source = Mockito.mock(InvocationSource.class);
    Mockito.when(source.getLocale()).thenReturn(Locale.ENGLISH);
    Mockito.when(context.getActor()).thenReturn(actor);
    Mockito.when(context.getSource()).thenReturn(source);
    BatchIdentifier identifier = Mockito.mock(BatchIdentifier.class);
    Mockito.when(batch.start(Mockito.eq("tester"), Mockito.any(), Mockito.any()))
        .thenReturn(identifier);
    Assertions.assertSame(identifier,
        usecase.batchDisable(context, OauthClientDisableFilter.builder().build()));
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  @Test
  @DisplayName("Test check the progress of disable of oauth client in batch")
  void test_oauth_client_disable_usecase_check_progress() {
    OauthClientAuditGateway audit = Mockito.mock(OauthClientAuditGateway.class);
    BatchService batch = Mockito.mock(BatchService.class);
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableAuthorizationDecision> execAllowEmitter = Mockito.mock(Event.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableCheck> execProposalEmitter = Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway gateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientsVisibility visibility = Mockito.mock(OauthClientsVisibility.class);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClientDisableUsecase usecase = new OauthClientDisableUsecase(audit, batch, cache,
        execAllowEmitter, execProposalEmitter, gateway, visibility);
    Actor actor = Mockito.mock(Actor.class);
    Mockito.when(actor.getName()).thenReturn("tester");
    InvocationSource source = Mockito.mock(InvocationSource.class);
    Mockito.when(source.getLocale()).thenReturn(Locale.ENGLISH);
    Mockito.when(context.getActor()).thenReturn(actor);
    Mockito.when(context.getSource()).thenReturn(source);
    BatchProgress progress = Mockito.mock(BatchProgress.class);
    Mockito.when(batch.retrieve("task", Locale.ENGLISH, "tester"))
        .thenReturn(Optional.of(progress));
    Assertions.assertSame(progress,
        usecase.checkProgress(context, OauthClientDisableStatus.builder().taskId("task").build()));
    Assertions.assertThrows(NotFoundException.class, () -> usecase.checkProgress(context,
        OauthClientDisableStatus.builder().taskId("other").build()));
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  @Test
  @DisplayName("Test disable of oauth client store, cache, audit and project the entity")
  void test_oauth_client_disable_usecase_execute() {
    OauthClientAuditGateway audit = Mockito.mock(OauthClientAuditGateway.class);
    BatchService batch = Mockito.mock(BatchService.class);
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableAuthorizationDecision> execAllowEmitter = Mockito.mock(Event.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableCheck> execProposalEmitter = Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway gateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientsVisibility visibility = Mockito.mock(OauthClientsVisibility.class);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClientDisableUsecase usecase = new OauthClientDisableUsecase(audit, batch, cache,
        execAllowEmitter, execProposalEmitter, gateway, visibility);
    OauthClient one = OauthClient.builder().uidValue(UidVO.from("one"))
        .codeValue(CodeVO.from("one")).tenantValue(TenantVO.from(TenantReference.of("one")))
        .allowAllScopesValue(AllowAllScopesVO.from(true))
        .tokenEndpointAuthMethodValue(TokenEndpointAuthMethodVO
            .from(OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_BASIC))
        .tokenEndpointAuthSigningAlgValue(TokenEndpointAuthSigningAlgVO.from("one"))
        .dpopBoundAccessTokensValue(DpopBoundAccessTokensVO.from(true))
        .idTokenSignedResponseAlgValue(
            IdTokenSignedResponseAlgVO.from(OauthClientIdTokenSignedResponseAlgOptions.RS256))
        .tlsClientAuthSubjectDnValue(TlsClientAuthSubjectDnVO.from("one"))
        .tlsClientCertificateBoundAccessTokensValue(
            TlsClientCertificateBoundAccessTokensVO.from(true))
        .authorizationSignedResponseAlgValue(AuthorizationSignedResponseAlgVO.from("one"))
        .backchannelTokenDeliveryModeValue(BackchannelTokenDeliveryModeVO
            .from(OauthClientBackchannelTokenDeliveryModeOptions.POLL))
        .backchannelClientNotificationEndpointValue(
            BackchannelClientNotificationEndpointVO.from("one"))
        .subjectTypeValue(SubjectTypeVO.from(OauthClientSubjectTypeOptions.PUBLIC))
        .idTokenEncryptedResponseAlgValue(IdTokenEncryptedResponseAlgVO.from("one"))
        .idTokenEncryptedResponseEncValue(IdTokenEncryptedResponseEncVO.from("one"))
        .userinfoEncryptedResponseAlgValue(UserinfoEncryptedResponseAlgVO.from("one"))
        .userinfoEncryptedResponseEncValue(UserinfoEncryptedResponseEncVO.from("one"))
        .authorizationDetailsTypesValue(AuthorizationDetailsTypesVO.from("one"))
        .userinfoSignedResponseAlgValue(UserinfoSignedResponseAlgVO.from("one"))
        .secretOauthValue(SecretOauthVO.fromPlain("one"))
        .registrationAccessTokenValue(RegistrationAccessTokenVO.fromPlain("one"))
        .defaultAcrValuesValue(DefaultAcrValuesVO.from("one"))
        .defaultMaxAgeValue(DefaultMaxAgeVO.from(1))
        .sectorIdentifierUriValue(SectorIdentifierUriVO.from("one"))
        .allowedLoginRedirectsValue(AllowedLoginRedirectsVO.from(
            List.of(AllowedLoginRedirects.builder().uid("one").url("one").version(1).build())))
        .allowedLogoutRedirectsValue(AllowedLogoutRedirectsVO.from(
            List.of(AllowedLogoutRedirects.builder().uid("one").url("one").version(1).build())))
        .allowedAudiencesValue(AllowedAudiencesVO.from(
            List.of(AllowedAudiences.builder().uid("one").audience("one").version(1).build())))
        .grantTypesJsonValue(GrantTypesJsonVO.from("one"))
        .introspectionAllowedValue(IntrospectionAllowedVO.from(true))
        .requirePushedAuthorizationRequestsValue(RequirePushedAuthorizationRequestsVO.from(true))
        .requirePkceValue(RequirePkceVO.from(true))
        .backchannelLogoutUriValue(BackchannelLogoutUriVO.from("one"))
        .backchannelLogoutSessionRequiredValue(BackchannelLogoutSessionRequiredVO.from(true))
        .frontchannelLogoutUriValue(FrontchannelLogoutUriVO.from("one"))
        .frontchannelLogoutSessionRequiredValue(FrontchannelLogoutSessionRequiredVO.from(true))
        .enabledValue(EnabledVO.from(true)).clientNameValue(ClientNameVO.from("one"))
        .logoUriValue(LogoUriVO.from("one")).clientUriValue(ClientUriVO.from("one"))
        .policyUriValue(PolicyUriVO.from("one")).tosUriValue(TosUriVO.from("one"))
        .responseTypesJsonValue(ResponseTypesJsonVO.from("one"))
        .dynamicallyRegisteredValue(DynamicallyRegisteredVO.from(true))
        .registeredAtValue(RegisteredAtVO.from(LocalDateTime.of(1980, 8, 20, 0, 0)
            .atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime()))
        .clientCredentialsScopesValue(ClientCredentialsScopesVO.from("one"))
        .clientCredentialsTokenTtlSecondsValue(ClientCredentialsTokenTtlSecondsVO.from(1))
        .requestObjectSigningAlgValue(RequestObjectSigningAlgVO.from("one"))
        .jwksUriValue(JwksUriVO.from("one")).jwksJsonValue(JwksJsonVO.from("one"))
        .versionValue(VersionVO.from(1)).build();
    Mockito.when(visibility.retrieveVisibleForUpdate(context, "one")).thenReturn(Optional.of(one));
    Mockito.when(gateway.update(Mockito.any(OauthClientRef.class), Mockito.any(OauthClient.class)))
        .thenAnswer(invocation -> invocation.getArgument(1));
    Mockito.when(visibility.copyWithHidden(Mockito.eq(context), Mockito.any(OauthClient.class)))
        .thenAnswer(invocation -> new OauthClientVisibleProjection(
            (OauthClient) invocation.getArgument(1)));
    OauthClientDisableProjection result = usecase.disable(context, OauthClientReference.of("one"));
    Assertions.assertNotNull(result, "The modified entity must be projected");
    Mockito.verify(execProposalEmitter).fire(Mockito.any(OauthClientDisableCheck.class));
    Mockito.verify(gateway).update(Mockito.eq(one), Mockito.any(OauthClient.class));
    Mockito.verify(cache).update(Mockito.any(OauthClient.class));
    Mockito.verify(audit).updated(Mockito.eq("disable"), Mockito.any(OauthClient.class),
        Mockito.eq(one), Mockito.eq(context));
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  @Test
  @DisplayName("Test disable of oauth client is rejected when policies deny it")
  void test_oauth_client_disable_usecase_not_allowed() {
    OauthClientAuditGateway audit = Mockito.mock(OauthClientAuditGateway.class);
    BatchService batch = Mockito.mock(BatchService.class);
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableAuthorizationDecision> execAllowEmitter = Mockito.mock(Event.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableCheck> execProposalEmitter = Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway gateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientsVisibility visibility = Mockito.mock(OauthClientsVisibility.class);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClientDisableUsecase usecase = new OauthClientDisableUsecase(audit, batch, cache,
        execAllowEmitter, execProposalEmitter, gateway, visibility);
    Mockito.doAnswer(invocation -> {
      ((OauthClientDisableAuthorizationDecision) invocation.getArgument(0)).deny("not allowed");
      return null;
    }).when(execAllowEmitter).fire(Mockito.any());
    Assertions.assertThrows(NotAllowedException.class,
        () -> usecase.disable(context, OauthClientReference.of("one")));
    Assertions.assertThrows(NotAllowedException.class,
        () -> usecase.batchDisable(context, OauthClientDisableFilter.builder().build()));
    Mockito.verifyNoInteractions(visibility, gateway, cache, audit, batch);
  }

  /**
   * @autogenerated ActionChangeUsecaseGenerator
   */
  @Test
  @DisplayName("Test disable of oauth client over a not visible entity")
  void test_oauth_client_disable_usecase_not_found() {
    OauthClientAuditGateway audit = Mockito.mock(OauthClientAuditGateway.class);
    BatchService batch = Mockito.mock(BatchService.class);
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableAuthorizationDecision> execAllowEmitter = Mockito.mock(Event.class);
    @SuppressWarnings("unchecked")
    Event<OauthClientDisableCheck> execProposalEmitter = Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway gateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientsVisibility visibility = Mockito.mock(OauthClientsVisibility.class);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClientDisableUsecase usecase = new OauthClientDisableUsecase(audit, batch, cache,
        execAllowEmitter, execProposalEmitter, gateway, visibility);
    Mockito.when(visibility.retrieveVisibleForUpdate(context, "one")).thenReturn(Optional.empty());
    Assertions.assertThrows(NotFoundException.class,
        () -> usecase.disable(context, OauthClientReference.of("one")));
    Mockito.verifyNoInteractions(gateway, cache, audit);
  }
}
