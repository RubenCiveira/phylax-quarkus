package net.civeira.phylax.feature.access.oauthclient.application.visibility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.enterprise.event.Event;
import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.common.security.OperationContext;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedAudiences;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLoginRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClient;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientChangeSet;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientSubjectTypeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientTokenEndpointAuthMethodOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientCacheGateway;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientCached;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientCommandRepositoryGateway;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientCursor;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientQueryRepositoryGateway;
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
import net.civeira.phylax.feature.identity.tenant.application.visibility.TenantsVisibility;
import net.civeira.phylax.feature.identity.tenant.domain.TenantReference;

class OauthClientsVisibilityUnitTest {

  /**
   * @autogenerated VisibilityServiceTestGenerator
   */
  @Test
  @SuppressWarnings("unchecked")
  @DisplayName("Test the visibility of oauth client use the cache")
  void test_oauth_clients_visibility_cache() {
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    Event<OauthClientVisibilityCheck> filterProposalEmitter =
        (Event<OauthClientVisibilityCheck>) Mockito.mock(Event.class);
    Event<OauthClientFixedPropertiesProposal> fixedFieldsEmitter =
        (Event<OauthClientFixedPropertiesProposal>) Mockito.mock(Event.class);
    Event<OauthClientAccessibleCheck> guardEmitter =
        (Event<OauthClientAccessibleCheck>) Mockito.mock(Event.class);
    Event<OauthClientHiddenPropertiesProposal> hiddenFieldsEmitter =
        (Event<OauthClientHiddenPropertiesProposal>) Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway oauthClientCommandRepositoryGateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientQueryRepositoryGateway oauthClientQueryRepositoryGateway =
        Mockito.mock(OauthClientQueryRepositoryGateway.class);
    Event<OauthClientEntityEnrichment> presetEmitter =
        (Event<OauthClientEntityEnrichment>) Mockito.mock(Event.class);
    TenantsVisibility tenantsVisibility = Mockito.mock(TenantsVisibility.class);
    OauthClientsVisibility service = new OauthClientsVisibility(cache, filterProposalEmitter,
        fixedFieldsEmitter, guardEmitter, hiddenFieldsEmitter, oauthClientCommandRepositoryGateway,
        oauthClientQueryRepositoryGateway, presetEmitter, tenantsVisibility);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClient item = OauthClient.builder().uidValue(UidVO.from("one"))
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
    var uid = item.getUid();
    OauthClientVisibilityFilter filter = OauthClientVisibilityFilter.builder().build();
    OauthClientCursor cursor = OauthClientCursor.builder().build();
    Slider<OauthClient> slider = (Slider<OauthClient>) Mockito.mock(Slider.class);
    Mockito.when(slider.slide(Mockito.any())).thenAnswer(invocation -> List.of(item).iterator());
    Mockito.when(oauthClientQueryRepositoryGateway.slide(Mockito.any(), Mockito.any()))
        .thenReturn(slider);
    Mockito.when(oauthClientCommandRepositoryGateway.slideForUpdate(Mockito.any(), Mockito.any()))
        .thenReturn(slider);
    OauthClientCached cached = Mockito.mock(OauthClientCached.class);
    Mockito.when(cache.retrieve(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
    Mockito.when(cache.store(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(cached);
    Assertions.assertSame(cached, service.listCachedVisibles(context, filter, cursor),
        "Without cache the list must be stored");
    Assertions.assertSame(cached, service.retrieveCachedVisible(context, uid),
        "Without cache the item must be stored");
    Mockito.verify(cache, Mockito.times(2)).store(Mockito.any(), Mockito.any(), Mockito.any());
    OauthClientCached stored = Mockito.mock(OauthClientCached.class);
    Mockito.when(cache.retrieve(Mockito.any(), Mockito.any())).thenReturn(Optional.of(stored));
    Assertions.assertSame(stored, service.listCachedVisibles(context, filter, cursor),
        "The cached list must be used");
    Assertions.assertSame(stored, service.retrieveCachedVisible(context, uid),
        "The cached item must be used");
  }

  /**
   * @autogenerated VisibilityServiceTestGenerator
   */
  @Test
  @DisplayName("Test the visibility of oauth client hide and fix fields")
  void test_oauth_clients_visibility_fields() {
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    Event<OauthClientVisibilityCheck> filterProposalEmitter =
        (Event<OauthClientVisibilityCheck>) Mockito.mock(Event.class);
    Event<OauthClientFixedPropertiesProposal> fixedFieldsEmitter =
        (Event<OauthClientFixedPropertiesProposal>) Mockito.mock(Event.class);
    Event<OauthClientAccessibleCheck> guardEmitter =
        (Event<OauthClientAccessibleCheck>) Mockito.mock(Event.class);
    Event<OauthClientHiddenPropertiesProposal> hiddenFieldsEmitter =
        (Event<OauthClientHiddenPropertiesProposal>) Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway oauthClientCommandRepositoryGateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientQueryRepositoryGateway oauthClientQueryRepositoryGateway =
        Mockito.mock(OauthClientQueryRepositoryGateway.class);
    Event<OauthClientEntityEnrichment> presetEmitter =
        (Event<OauthClientEntityEnrichment>) Mockito.mock(Event.class);
    TenantsVisibility tenantsVisibility = Mockito.mock(TenantsVisibility.class);
    OauthClientsVisibility service = new OauthClientsVisibility(cache, filterProposalEmitter,
        fixedFieldsEmitter, guardEmitter, hiddenFieldsEmitter, oauthClientCommandRepositoryGateway,
        oauthClientQueryRepositoryGateway, presetEmitter, tenantsVisibility);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClient item = OauthClient.builder().uidValue(UidVO.from("one"))
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
    var uid = item.getUid();
    OauthClientVisibilityFilter filter = OauthClientVisibilityFilter.builder().build();
    OauthClientCursor cursor = OauthClientCursor.builder().build();
    Assertions.assertTrue(service.fieldsToHide(context).isEmpty(),
        "Without policies no field is hidden");
    Assertions.assertTrue(service.fieldsToHide(context, item).isEmpty(),
        "Without policies no field is hidden");
    Assertions.assertNotNull(service.fieldsToFix(context), "The fixed fields must be informed");
    Assertions.assertNotNull(service.fieldsToFix(context, item),
        "The fixed fields must be informed");
    OauthClientVisibleProjection projection = service.copyWithHidden(context, item);
    Assertions.assertEquals(Optional.of(item.getUidValue()), projection.getUid(),
        "The visible projection must keep the identifier");
    OauthClientChangeSet changes = new OauthClientChangeSet();
    Assertions.assertNotNull(service.copyWithFixed(context, changes),
        "The change set must be enriched");
    Assertions.assertNotNull(service.copyWithFixed(context, changes, item),
        "The change set must be enriched");
  }

  /**
   * @autogenerated VisibilityServiceTestGenerator
   */
  @Test
  @SuppressWarnings("unchecked")
  @DisplayName("Test the visibility of oauth client list and count the visible entities")
  void test_oauth_clients_visibility_list() {
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    Event<OauthClientVisibilityCheck> filterProposalEmitter =
        (Event<OauthClientVisibilityCheck>) Mockito.mock(Event.class);
    Event<OauthClientFixedPropertiesProposal> fixedFieldsEmitter =
        (Event<OauthClientFixedPropertiesProposal>) Mockito.mock(Event.class);
    Event<OauthClientAccessibleCheck> guardEmitter =
        (Event<OauthClientAccessibleCheck>) Mockito.mock(Event.class);
    Event<OauthClientHiddenPropertiesProposal> hiddenFieldsEmitter =
        (Event<OauthClientHiddenPropertiesProposal>) Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway oauthClientCommandRepositoryGateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientQueryRepositoryGateway oauthClientQueryRepositoryGateway =
        Mockito.mock(OauthClientQueryRepositoryGateway.class);
    Event<OauthClientEntityEnrichment> presetEmitter =
        (Event<OauthClientEntityEnrichment>) Mockito.mock(Event.class);
    TenantsVisibility tenantsVisibility = Mockito.mock(TenantsVisibility.class);
    OauthClientsVisibility service = new OauthClientsVisibility(cache, filterProposalEmitter,
        fixedFieldsEmitter, guardEmitter, hiddenFieldsEmitter, oauthClientCommandRepositoryGateway,
        oauthClientQueryRepositoryGateway, presetEmitter, tenantsVisibility);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClient item = OauthClient.builder().uidValue(UidVO.from("one"))
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
    var uid = item.getUid();
    OauthClientVisibilityFilter filter = OauthClientVisibilityFilter.builder().build();
    OauthClientCursor cursor = OauthClientCursor.builder().build();
    Slider<OauthClient> slider = (Slider<OauthClient>) Mockito.mock(Slider.class);
    Mockito.when(slider.slide(Mockito.any())).thenAnswer(invocation -> List.of(item).iterator());
    Mockito.when(oauthClientQueryRepositoryGateway.slide(Mockito.any(), Mockito.any()))
        .thenReturn(slider);
    Mockito.when(oauthClientCommandRepositoryGateway.slideForUpdate(Mockito.any(), Mockito.any()))
        .thenReturn(slider);
    Mockito.when(oauthClientQueryRepositoryGateway.count(Mockito.any())).thenReturn(1L);
    Assertions.assertEquals(List.of(item), service.listVisibles(context, filter, cursor),
        "The listed items must be visibles");
    Assertions.assertEquals(List.of(item), service.listVisiblesForUpdate(context, filter, cursor),
        "The listed items must be visibles for update");
    Assertions.assertEquals(1L, service.countVisibles(context, filter),
        "The count must be delegated to the repository");
    Assertions.assertTrue(service.checkVisibility(context, List.of(uid)),
        "All the listed items must be visibles");
    Assertions.assertTrue(service.checkVisibilityForUpdate(context, List.of(uid)),
        "All the listed items must be visibles for update");
  }

  /**
   * @autogenerated VisibilityServiceTestGenerator
   */
  @Test
  @DisplayName("Test the visibility of oauth client retrieve the visible entities")
  void test_oauth_clients_visibility_retrieve() {
    OauthClientCacheGateway cache = Mockito.mock(OauthClientCacheGateway.class);
    Event<OauthClientVisibilityCheck> filterProposalEmitter =
        (Event<OauthClientVisibilityCheck>) Mockito.mock(Event.class);
    Event<OauthClientFixedPropertiesProposal> fixedFieldsEmitter =
        (Event<OauthClientFixedPropertiesProposal>) Mockito.mock(Event.class);
    Event<OauthClientAccessibleCheck> guardEmitter =
        (Event<OauthClientAccessibleCheck>) Mockito.mock(Event.class);
    Event<OauthClientHiddenPropertiesProposal> hiddenFieldsEmitter =
        (Event<OauthClientHiddenPropertiesProposal>) Mockito.mock(Event.class);
    OauthClientCommandRepositoryGateway oauthClientCommandRepositoryGateway =
        Mockito.mock(OauthClientCommandRepositoryGateway.class);
    OauthClientQueryRepositoryGateway oauthClientQueryRepositoryGateway =
        Mockito.mock(OauthClientQueryRepositoryGateway.class);
    Event<OauthClientEntityEnrichment> presetEmitter =
        (Event<OauthClientEntityEnrichment>) Mockito.mock(Event.class);
    TenantsVisibility tenantsVisibility = Mockito.mock(TenantsVisibility.class);
    OauthClientsVisibility service = new OauthClientsVisibility(cache, filterProposalEmitter,
        fixedFieldsEmitter, guardEmitter, hiddenFieldsEmitter, oauthClientCommandRepositoryGateway,
        oauthClientQueryRepositoryGateway, presetEmitter, tenantsVisibility);
    OperationContext context = Mockito.mock(OperationContext.class);
    OauthClient item = OauthClient.builder().uidValue(UidVO.from("one"))
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
    var uid = item.getUid();
    OauthClientVisibilityFilter filter = OauthClientVisibilityFilter.builder().build();
    OauthClientCursor cursor = OauthClientCursor.builder().build();
    Mockito.when(oauthClientQueryRepositoryGateway.retrieve(Mockito.eq(uid), Mockito.any()))
        .thenReturn(Optional.of(item));
    Mockito
        .when(oauthClientCommandRepositoryGateway.retrieveForUpdate(Mockito.eq(uid), Mockito.any()))
        .thenReturn(Optional.of(item));
    Assertions.assertEquals(Optional.of(item), service.retrieveVisible(context, uid),
        "A stored entity must be visible");
    Assertions.assertTrue(service.checkVisibility(context, uid), "A stored entity must be visible");
    Assertions.assertEquals(Optional.of(item), service.retrieveVisibleForUpdate(context, uid),
        "A stored entity must be visible for update");
    Assertions.assertTrue(service.checkVisibilityForUpdate(context, uid),
        "A stored entity must be visible for update");
    Assertions.assertFalse(service.checkVisibility(context, "two"),
        "A missing entity is not visible");
    Assertions.assertFalse(service.checkVisibilityForUpdate(context, "two"),
        "A missing entity is not visible for update");
  }
}
