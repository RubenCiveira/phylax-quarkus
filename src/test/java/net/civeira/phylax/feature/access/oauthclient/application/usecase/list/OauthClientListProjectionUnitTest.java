package net.civeira.phylax.feature.access.oauthclient.application.usecase.list;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.feature.access.oauthclient.application.visibility.OauthClientVisibleProjection;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedAudiences;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLoginRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClient;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientSubjectTypeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientTokenEndpointAuthMethodOptions;
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

class OauthClientListProjectionUnitTest {

  /**
   * @autogenerated ListProjectionGenerator
   */
  @Test
  @DisplayName("Test the oauth client projection exposes the values of an entity")
  void test_oauth_client_list_projection_from_entity() {
    OauthClient entity = OauthClient.builder().uidValue(UidVO.from("one"))
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
    OauthClientListProjection projection = new OauthClientListProjection(entity);
    Assertions.assertEquals("one", projection.getUid(), "uid must be taken from the entity");
    Assertions.assertEquals("one", projection.getUidOrDefault("two"),
        "uid must ignore the default when has value");
    Assertions.assertEquals("one", projection.getCode(), "code must be taken from the entity");
    Assertions.assertEquals("one", projection.getCodeOrDefault("two"),
        "code must ignore the default when has value");
    Assertions.assertEquals("one", projection.getTenantReference(),
        "tenant reference must be taken from the entity");
    Assertions.assertEquals("one", projection.getTenantReferenceOrDefault("two"),
        "tenant must ignore the default when has value");
    Assertions.assertEquals(true, projection.getAllowAllScopes(),
        "allowAllScopes must be taken from the entity");
    Assertions.assertEquals(true, projection.getAllowAllScopesOrDefault(false),
        "allowAllScopes must ignore the default when has value");
    Assertions.assertEquals(OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_BASIC,
        projection.getTokenEndpointAuthMethod(),
        "tokenEndpointAuthMethod must be taken from the entity");
    Assertions.assertEquals(OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_BASIC,
        projection.getTokenEndpointAuthMethodOrDefault(
            OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_POST),
        "tokenEndpointAuthMethod must ignore the default when has value");
    Assertions.assertEquals("one", projection.getTokenEndpointAuthSigningAlg(),
        "tokenEndpointAuthSigningAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getTokenEndpointAuthSigningAlgOrDefault("two"),
        "tokenEndpointAuthSigningAlg must ignore the default when has value");
    Assertions.assertEquals(true, projection.getDpopBoundAccessTokens(),
        "dpopBoundAccessTokens must be taken from the entity");
    Assertions.assertEquals(true, projection.getDpopBoundAccessTokensOrDefault(false),
        "dpopBoundAccessTokens must ignore the default when has value");
    Assertions.assertEquals(OauthClientIdTokenSignedResponseAlgOptions.RS256,
        projection.getIdTokenSignedResponseAlg(),
        "idTokenSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals(OauthClientIdTokenSignedResponseAlgOptions.RS256,
        projection
            .getIdTokenSignedResponseAlgOrDefault(OauthClientIdTokenSignedResponseAlgOptions.ES256),
        "idTokenSignedResponseAlg must ignore the default when has value");
    Assertions.assertEquals("one", projection.getTlsClientAuthSubjectDn(),
        "tlsClientAuthSubjectDn must be taken from the entity");
    Assertions.assertEquals("one", projection.getTlsClientAuthSubjectDnOrDefault("two"),
        "tlsClientAuthSubjectDn must ignore the default when has value");
    Assertions.assertEquals(true, projection.getTlsClientCertificateBoundAccessTokens(),
        "tlsClientCertificateBoundAccessTokens must be taken from the entity");
    Assertions.assertEquals(true,
        projection.getTlsClientCertificateBoundAccessTokensOrDefault(false),
        "tlsClientCertificateBoundAccessTokens must ignore the default when has value");
    Assertions.assertEquals("one", projection.getAuthorizationSignedResponseAlg(),
        "authorizationSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getAuthorizationSignedResponseAlgOrDefault("two"),
        "authorizationSignedResponseAlg must ignore the default when has value");
    Assertions.assertEquals(OauthClientBackchannelTokenDeliveryModeOptions.POLL,
        projection.getBackchannelTokenDeliveryMode(),
        "backchannelTokenDeliveryMode must be taken from the entity");
    Assertions.assertEquals(OauthClientBackchannelTokenDeliveryModeOptions.POLL,
        projection.getBackchannelTokenDeliveryModeOrDefault(
            OauthClientBackchannelTokenDeliveryModeOptions.PING),
        "backchannelTokenDeliveryMode must ignore the default when has value");
    Assertions.assertEquals("one", projection.getBackchannelClientNotificationEndpoint(),
        "backchannelClientNotificationEndpoint must be taken from the entity");
    Assertions.assertEquals("one",
        projection.getBackchannelClientNotificationEndpointOrDefault("two"),
        "backchannelClientNotificationEndpoint must ignore the default when has value");
    Assertions.assertEquals(OauthClientSubjectTypeOptions.PUBLIC, projection.getSubjectType(),
        "subjectType must be taken from the entity");
    Assertions.assertEquals(OauthClientSubjectTypeOptions.PUBLIC,
        projection.getSubjectTypeOrDefault(OauthClientSubjectTypeOptions.PAIRWISE),
        "subjectType must ignore the default when has value");
    Assertions.assertEquals("one", projection.getIdTokenEncryptedResponseAlg(),
        "idTokenEncryptedResponseAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getIdTokenEncryptedResponseAlgOrDefault("two"),
        "idTokenEncryptedResponseAlg must ignore the default when has value");
    Assertions.assertEquals("one", projection.getIdTokenEncryptedResponseEnc(),
        "idTokenEncryptedResponseEnc must be taken from the entity");
    Assertions.assertEquals("one", projection.getIdTokenEncryptedResponseEncOrDefault("two"),
        "idTokenEncryptedResponseEnc must ignore the default when has value");
    Assertions.assertEquals("one", projection.getUserinfoEncryptedResponseAlg(),
        "userinfoEncryptedResponseAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getUserinfoEncryptedResponseAlgOrDefault("two"),
        "userinfoEncryptedResponseAlg must ignore the default when has value");
    Assertions.assertEquals("one", projection.getUserinfoEncryptedResponseEnc(),
        "userinfoEncryptedResponseEnc must be taken from the entity");
    Assertions.assertEquals("one", projection.getUserinfoEncryptedResponseEncOrDefault("two"),
        "userinfoEncryptedResponseEnc must ignore the default when has value");
    Assertions.assertEquals("one", projection.getAuthorizationDetailsTypes(),
        "authorizationDetailsTypes must be taken from the entity");
    Assertions.assertEquals("one", projection.getAuthorizationDetailsTypesOrDefault("two"),
        "authorizationDetailsTypes must ignore the default when has value");
    Assertions.assertEquals("one", projection.getUserinfoSignedResponseAlg(),
        "userinfoSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getUserinfoSignedResponseAlgOrDefault("two"),
        "userinfoSignedResponseAlg must ignore the default when has value");
    Assertions.assertEquals("****", projection.getSecretOauth(), "secretOauth must be masked");
    Assertions.assertEquals("****", projection.getRegistrationAccessToken(),
        "registrationAccessToken must be masked");
    Assertions.assertEquals("one", projection.getDefaultAcrValues(),
        "defaultAcrValues must be taken from the entity");
    Assertions.assertEquals("one", projection.getDefaultAcrValuesOrDefault("two"),
        "defaultAcrValues must ignore the default when has value");
    Assertions.assertEquals(1, projection.getDefaultMaxAge(),
        "defaultMaxAge must be taken from the entity");
    Assertions.assertEquals(1, projection.getDefaultMaxAgeOrDefault(2),
        "defaultMaxAge must ignore the default when has value");
    Assertions.assertEquals("one", projection.getSectorIdentifierUri(),
        "sectorIdentifierUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getSectorIdentifierUriOrDefault("two"),
        "sectorIdentifierUri must ignore the default when has value");
    Assertions.assertFalse(projection.getAllowedLoginRedirects().isEmpty(),
        "allowedLoginRedirects must be filled from the entity");
    Assertions.assertFalse(projection.getAllowedLogoutRedirects().isEmpty(),
        "allowedLogoutRedirects must be filled from the entity");
    Assertions.assertFalse(projection.getAllowedAudiences().isEmpty(),
        "allowedAudiences must be filled from the entity");
    Assertions.assertEquals("one", projection.getGrantTypesJson(),
        "grantTypesJson must be taken from the entity");
    Assertions.assertEquals("one", projection.getGrantTypesJsonOrDefault("two"),
        "grantTypesJson must ignore the default when has value");
    Assertions.assertEquals(true, projection.getIntrospectionAllowed(),
        "introspectionAllowed must be taken from the entity");
    Assertions.assertEquals(true, projection.getIntrospectionAllowedOrDefault(false),
        "introspectionAllowed must ignore the default when has value");
    Assertions.assertEquals(true, projection.getRequirePushedAuthorizationRequests(),
        "requirePushedAuthorizationRequests must be taken from the entity");
    Assertions.assertEquals(true, projection.getRequirePushedAuthorizationRequestsOrDefault(false),
        "requirePushedAuthorizationRequests must ignore the default when has value");
    Assertions.assertEquals(true, projection.getRequirePkce(),
        "requirePkce must be taken from the entity");
    Assertions.assertEquals(true, projection.getRequirePkceOrDefault(false),
        "requirePkce must ignore the default when has value");
    Assertions.assertEquals("one", projection.getBackchannelLogoutUri(),
        "backchannelLogoutUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getBackchannelLogoutUriOrDefault("two"),
        "backchannelLogoutUri must ignore the default when has value");
    Assertions.assertEquals(true, projection.getBackchannelLogoutSessionRequired(),
        "backchannelLogoutSessionRequired must be taken from the entity");
    Assertions.assertEquals(true, projection.getBackchannelLogoutSessionRequiredOrDefault(false),
        "backchannelLogoutSessionRequired must ignore the default when has value");
    Assertions.assertEquals("one", projection.getFrontchannelLogoutUri(),
        "frontchannelLogoutUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getFrontchannelLogoutUriOrDefault("two"),
        "frontchannelLogoutUri must ignore the default when has value");
    Assertions.assertEquals(true, projection.getFrontchannelLogoutSessionRequired(),
        "frontchannelLogoutSessionRequired must be taken from the entity");
    Assertions.assertEquals(true, projection.getFrontchannelLogoutSessionRequiredOrDefault(false),
        "frontchannelLogoutSessionRequired must ignore the default when has value");
    Assertions.assertEquals(true, projection.getEnabled(), "enabled must be taken from the entity");
    Assertions.assertEquals(true, projection.getEnabledOrDefault(false),
        "enabled must ignore the default when has value");
    Assertions.assertEquals("one", projection.getClientName(),
        "clientName must be taken from the entity");
    Assertions.assertEquals("one", projection.getClientNameOrDefault("two"),
        "clientName must ignore the default when has value");
    Assertions.assertEquals("one", projection.getLogoUri(),
        "logoUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getLogoUriOrDefault("two"),
        "logoUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getClientUri(),
        "clientUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getClientUriOrDefault("two"),
        "clientUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getPolicyUri(),
        "policyUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getPolicyUriOrDefault("two"),
        "policyUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getTosUri(), "tosUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getTosUriOrDefault("two"),
        "tosUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getResponseTypesJson(),
        "responseTypesJson must be taken from the entity");
    Assertions.assertEquals("one", projection.getResponseTypesJsonOrDefault("two"),
        "responseTypesJson must ignore the default when has value");
    Assertions.assertEquals(true, projection.getDynamicallyRegistered(),
        "dynamicallyRegistered must be taken from the entity");
    Assertions.assertEquals(true, projection.getDynamicallyRegisteredOrDefault(false),
        "dynamicallyRegistered must ignore the default when has value");
    Assertions.assertEquals(
        LocalDateTime.of(1980, 8, 20, 0, 0).atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime(),
        projection.getRegisteredAt(), "registeredAt must be taken from the entity");
    Assertions.assertEquals(
        LocalDateTime.of(1980, 8, 20, 0, 0).atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime(),
        projection.getRegisteredAtOrDefault(LocalDateTime.of(1981, 9, 6, 0, 0)
            .atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime()),
        "registeredAt must ignore the default when has value");
    Assertions.assertEquals("one", projection.getClientCredentialsScopes(),
        "clientCredentialsScopes must be taken from the entity");
    Assertions.assertEquals("one", projection.getClientCredentialsScopesOrDefault("two"),
        "clientCredentialsScopes must ignore the default when has value");
    Assertions.assertEquals(1, projection.getClientCredentialsTokenTtlSeconds(),
        "clientCredentialsTokenTtlSeconds must be taken from the entity");
    Assertions.assertEquals(1, projection.getClientCredentialsTokenTtlSecondsOrDefault(2),
        "clientCredentialsTokenTtlSeconds must ignore the default when has value");
    Assertions.assertEquals("one", projection.getRequestObjectSigningAlg(),
        "requestObjectSigningAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getRequestObjectSigningAlgOrDefault("two"),
        "requestObjectSigningAlg must ignore the default when has value");
    Assertions.assertEquals("one", projection.getJwksUri(),
        "jwksUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getJwksUriOrDefault("two"),
        "jwksUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getJwksJson(),
        "jwksJson must be taken from the entity");
    Assertions.assertEquals("one", projection.getJwksJsonOrDefault("two"),
        "jwksJson must ignore the default when has value");
    Assertions.assertEquals(1, projection.getVersion(), "version must be taken from the entity");
    Assertions.assertEquals(1, projection.getVersionOrDefault(2),
        "version must ignore the default when has value");
  }

  /**
   * @autogenerated ListProjectionGenerator
   */
  @Test
  @DisplayName("Test the oauth client projection copy the values of a visible projection")
  void test_oauth_client_list_projection_from_visible() {
    OauthClient entity = OauthClient.builder().uidValue(UidVO.from("one"))
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
    OauthClientListProjection projection =
        OauthClientListProjection.from(new OauthClientVisibleProjection(entity));
    Assertions.assertEquals("one", projection.getUid(), "uid must be taken from the entity");
    Assertions.assertEquals("one", projection.getUidOrDefault("two"),
        "uid must ignore the default when has value");
    Assertions.assertEquals("one", projection.getCode(), "code must be taken from the entity");
    Assertions.assertEquals("one", projection.getCodeOrDefault("two"),
        "code must ignore the default when has value");
    Assertions.assertEquals("one", projection.getTenantReference(),
        "tenant reference must be taken from the entity");
    Assertions.assertEquals("one", projection.getTenantReferenceOrDefault("two"),
        "tenant must ignore the default when has value");
    Assertions.assertEquals(true, projection.getAllowAllScopes(),
        "allowAllScopes must be taken from the entity");
    Assertions.assertEquals(true, projection.getAllowAllScopesOrDefault(false),
        "allowAllScopes must ignore the default when has value");
    Assertions.assertEquals(OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_BASIC,
        projection.getTokenEndpointAuthMethod(),
        "tokenEndpointAuthMethod must be taken from the entity");
    Assertions.assertEquals(OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_BASIC,
        projection.getTokenEndpointAuthMethodOrDefault(
            OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_POST),
        "tokenEndpointAuthMethod must ignore the default when has value");
    Assertions.assertEquals("one", projection.getTokenEndpointAuthSigningAlg(),
        "tokenEndpointAuthSigningAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getTokenEndpointAuthSigningAlgOrDefault("two"),
        "tokenEndpointAuthSigningAlg must ignore the default when has value");
    Assertions.assertEquals(true, projection.getDpopBoundAccessTokens(),
        "dpopBoundAccessTokens must be taken from the entity");
    Assertions.assertEquals(true, projection.getDpopBoundAccessTokensOrDefault(false),
        "dpopBoundAccessTokens must ignore the default when has value");
    Assertions.assertEquals(OauthClientIdTokenSignedResponseAlgOptions.RS256,
        projection.getIdTokenSignedResponseAlg(),
        "idTokenSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals(OauthClientIdTokenSignedResponseAlgOptions.RS256,
        projection
            .getIdTokenSignedResponseAlgOrDefault(OauthClientIdTokenSignedResponseAlgOptions.ES256),
        "idTokenSignedResponseAlg must ignore the default when has value");
    Assertions.assertEquals("one", projection.getTlsClientAuthSubjectDn(),
        "tlsClientAuthSubjectDn must be taken from the entity");
    Assertions.assertEquals("one", projection.getTlsClientAuthSubjectDnOrDefault("two"),
        "tlsClientAuthSubjectDn must ignore the default when has value");
    Assertions.assertEquals(true, projection.getTlsClientCertificateBoundAccessTokens(),
        "tlsClientCertificateBoundAccessTokens must be taken from the entity");
    Assertions.assertEquals(true,
        projection.getTlsClientCertificateBoundAccessTokensOrDefault(false),
        "tlsClientCertificateBoundAccessTokens must ignore the default when has value");
    Assertions.assertEquals("one", projection.getAuthorizationSignedResponseAlg(),
        "authorizationSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getAuthorizationSignedResponseAlgOrDefault("two"),
        "authorizationSignedResponseAlg must ignore the default when has value");
    Assertions.assertEquals(OauthClientBackchannelTokenDeliveryModeOptions.POLL,
        projection.getBackchannelTokenDeliveryMode(),
        "backchannelTokenDeliveryMode must be taken from the entity");
    Assertions.assertEquals(OauthClientBackchannelTokenDeliveryModeOptions.POLL,
        projection.getBackchannelTokenDeliveryModeOrDefault(
            OauthClientBackchannelTokenDeliveryModeOptions.PING),
        "backchannelTokenDeliveryMode must ignore the default when has value");
    Assertions.assertEquals("one", projection.getBackchannelClientNotificationEndpoint(),
        "backchannelClientNotificationEndpoint must be taken from the entity");
    Assertions.assertEquals("one",
        projection.getBackchannelClientNotificationEndpointOrDefault("two"),
        "backchannelClientNotificationEndpoint must ignore the default when has value");
    Assertions.assertEquals(OauthClientSubjectTypeOptions.PUBLIC, projection.getSubjectType(),
        "subjectType must be taken from the entity");
    Assertions.assertEquals(OauthClientSubjectTypeOptions.PUBLIC,
        projection.getSubjectTypeOrDefault(OauthClientSubjectTypeOptions.PAIRWISE),
        "subjectType must ignore the default when has value");
    Assertions.assertEquals("one", projection.getIdTokenEncryptedResponseAlg(),
        "idTokenEncryptedResponseAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getIdTokenEncryptedResponseAlgOrDefault("two"),
        "idTokenEncryptedResponseAlg must ignore the default when has value");
    Assertions.assertEquals("one", projection.getIdTokenEncryptedResponseEnc(),
        "idTokenEncryptedResponseEnc must be taken from the entity");
    Assertions.assertEquals("one", projection.getIdTokenEncryptedResponseEncOrDefault("two"),
        "idTokenEncryptedResponseEnc must ignore the default when has value");
    Assertions.assertEquals("one", projection.getUserinfoEncryptedResponseAlg(),
        "userinfoEncryptedResponseAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getUserinfoEncryptedResponseAlgOrDefault("two"),
        "userinfoEncryptedResponseAlg must ignore the default when has value");
    Assertions.assertEquals("one", projection.getUserinfoEncryptedResponseEnc(),
        "userinfoEncryptedResponseEnc must be taken from the entity");
    Assertions.assertEquals("one", projection.getUserinfoEncryptedResponseEncOrDefault("two"),
        "userinfoEncryptedResponseEnc must ignore the default when has value");
    Assertions.assertEquals("one", projection.getAuthorizationDetailsTypes(),
        "authorizationDetailsTypes must be taken from the entity");
    Assertions.assertEquals("one", projection.getAuthorizationDetailsTypesOrDefault("two"),
        "authorizationDetailsTypes must ignore the default when has value");
    Assertions.assertEquals("one", projection.getUserinfoSignedResponseAlg(),
        "userinfoSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getUserinfoSignedResponseAlgOrDefault("two"),
        "userinfoSignedResponseAlg must ignore the default when has value");
    Assertions.assertEquals("****", projection.getSecretOauth(), "secretOauth must be masked");
    Assertions.assertEquals("****", projection.getRegistrationAccessToken(),
        "registrationAccessToken must be masked");
    Assertions.assertEquals("one", projection.getDefaultAcrValues(),
        "defaultAcrValues must be taken from the entity");
    Assertions.assertEquals("one", projection.getDefaultAcrValuesOrDefault("two"),
        "defaultAcrValues must ignore the default when has value");
    Assertions.assertEquals(1, projection.getDefaultMaxAge(),
        "defaultMaxAge must be taken from the entity");
    Assertions.assertEquals(1, projection.getDefaultMaxAgeOrDefault(2),
        "defaultMaxAge must ignore the default when has value");
    Assertions.assertEquals("one", projection.getSectorIdentifierUri(),
        "sectorIdentifierUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getSectorIdentifierUriOrDefault("two"),
        "sectorIdentifierUri must ignore the default when has value");
    Assertions.assertFalse(projection.getAllowedLoginRedirects().isEmpty(),
        "allowedLoginRedirects must be filled from the entity");
    Assertions.assertFalse(projection.getAllowedLogoutRedirects().isEmpty(),
        "allowedLogoutRedirects must be filled from the entity");
    Assertions.assertFalse(projection.getAllowedAudiences().isEmpty(),
        "allowedAudiences must be filled from the entity");
    Assertions.assertEquals("one", projection.getGrantTypesJson(),
        "grantTypesJson must be taken from the entity");
    Assertions.assertEquals("one", projection.getGrantTypesJsonOrDefault("two"),
        "grantTypesJson must ignore the default when has value");
    Assertions.assertEquals(true, projection.getIntrospectionAllowed(),
        "introspectionAllowed must be taken from the entity");
    Assertions.assertEquals(true, projection.getIntrospectionAllowedOrDefault(false),
        "introspectionAllowed must ignore the default when has value");
    Assertions.assertEquals(true, projection.getRequirePushedAuthorizationRequests(),
        "requirePushedAuthorizationRequests must be taken from the entity");
    Assertions.assertEquals(true, projection.getRequirePushedAuthorizationRequestsOrDefault(false),
        "requirePushedAuthorizationRequests must ignore the default when has value");
    Assertions.assertEquals(true, projection.getRequirePkce(),
        "requirePkce must be taken from the entity");
    Assertions.assertEquals(true, projection.getRequirePkceOrDefault(false),
        "requirePkce must ignore the default when has value");
    Assertions.assertEquals("one", projection.getBackchannelLogoutUri(),
        "backchannelLogoutUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getBackchannelLogoutUriOrDefault("two"),
        "backchannelLogoutUri must ignore the default when has value");
    Assertions.assertEquals(true, projection.getBackchannelLogoutSessionRequired(),
        "backchannelLogoutSessionRequired must be taken from the entity");
    Assertions.assertEquals(true, projection.getBackchannelLogoutSessionRequiredOrDefault(false),
        "backchannelLogoutSessionRequired must ignore the default when has value");
    Assertions.assertEquals("one", projection.getFrontchannelLogoutUri(),
        "frontchannelLogoutUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getFrontchannelLogoutUriOrDefault("two"),
        "frontchannelLogoutUri must ignore the default when has value");
    Assertions.assertEquals(true, projection.getFrontchannelLogoutSessionRequired(),
        "frontchannelLogoutSessionRequired must be taken from the entity");
    Assertions.assertEquals(true, projection.getFrontchannelLogoutSessionRequiredOrDefault(false),
        "frontchannelLogoutSessionRequired must ignore the default when has value");
    Assertions.assertEquals(true, projection.getEnabled(), "enabled must be taken from the entity");
    Assertions.assertEquals(true, projection.getEnabledOrDefault(false),
        "enabled must ignore the default when has value");
    Assertions.assertEquals("one", projection.getClientName(),
        "clientName must be taken from the entity");
    Assertions.assertEquals("one", projection.getClientNameOrDefault("two"),
        "clientName must ignore the default when has value");
    Assertions.assertEquals("one", projection.getLogoUri(),
        "logoUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getLogoUriOrDefault("two"),
        "logoUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getClientUri(),
        "clientUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getClientUriOrDefault("two"),
        "clientUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getPolicyUri(),
        "policyUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getPolicyUriOrDefault("two"),
        "policyUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getTosUri(), "tosUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getTosUriOrDefault("two"),
        "tosUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getResponseTypesJson(),
        "responseTypesJson must be taken from the entity");
    Assertions.assertEquals("one", projection.getResponseTypesJsonOrDefault("two"),
        "responseTypesJson must ignore the default when has value");
    Assertions.assertEquals(true, projection.getDynamicallyRegistered(),
        "dynamicallyRegistered must be taken from the entity");
    Assertions.assertEquals(true, projection.getDynamicallyRegisteredOrDefault(false),
        "dynamicallyRegistered must ignore the default when has value");
    Assertions.assertEquals(
        LocalDateTime.of(1980, 8, 20, 0, 0).atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime(),
        projection.getRegisteredAt(), "registeredAt must be taken from the entity");
    Assertions.assertEquals(
        LocalDateTime.of(1980, 8, 20, 0, 0).atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime(),
        projection.getRegisteredAtOrDefault(LocalDateTime.of(1981, 9, 6, 0, 0)
            .atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime()),
        "registeredAt must ignore the default when has value");
    Assertions.assertEquals("one", projection.getClientCredentialsScopes(),
        "clientCredentialsScopes must be taken from the entity");
    Assertions.assertEquals("one", projection.getClientCredentialsScopesOrDefault("two"),
        "clientCredentialsScopes must ignore the default when has value");
    Assertions.assertEquals(1, projection.getClientCredentialsTokenTtlSeconds(),
        "clientCredentialsTokenTtlSeconds must be taken from the entity");
    Assertions.assertEquals(1, projection.getClientCredentialsTokenTtlSecondsOrDefault(2),
        "clientCredentialsTokenTtlSeconds must ignore the default when has value");
    Assertions.assertEquals("one", projection.getRequestObjectSigningAlg(),
        "requestObjectSigningAlg must be taken from the entity");
    Assertions.assertEquals("one", projection.getRequestObjectSigningAlgOrDefault("two"),
        "requestObjectSigningAlg must ignore the default when has value");
    Assertions.assertEquals("one", projection.getJwksUri(),
        "jwksUri must be taken from the entity");
    Assertions.assertEquals("one", projection.getJwksUriOrDefault("two"),
        "jwksUri must ignore the default when has value");
    Assertions.assertEquals("one", projection.getJwksJson(),
        "jwksJson must be taken from the entity");
    Assertions.assertEquals("one", projection.getJwksJsonOrDefault("two"),
        "jwksJson must ignore the default when has value");
    Assertions.assertEquals(1, projection.getVersion(), "version must be taken from the entity");
    Assertions.assertEquals(1, projection.getVersionOrDefault(2),
        "version must ignore the default when has value");
    OauthClientListProjection empty =
        OauthClientListProjection.from(OauthClientVisibleProjection.builder().build());
    Assertions.assertNull(empty.getUid(), "uid must be null without value");
    Assertions.assertEquals("two", empty.getUidOrDefault("two"),
        "uid must use the default without value");
    Assertions.assertNull(empty.getCode(), "code must be null without value");
    Assertions.assertEquals("two", empty.getCodeOrDefault("two"),
        "code must use the default without value");
    Assertions.assertNull(empty.getTenant(), "tenant must be null without value");
    Assertions.assertEquals("two", empty.getTenantReferenceOrDefault("two"),
        "tenant must use the default without value");
    Assertions.assertNull(empty.getAllowAllScopes(), "allowAllScopes must be null without value");
    Assertions.assertEquals(false, empty.getAllowAllScopesOrDefault(false),
        "allowAllScopes must use the default without value");
    Assertions.assertNull(empty.getTokenEndpointAuthMethod(),
        "tokenEndpointAuthMethod must be null without value");
    Assertions.assertEquals(OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_POST,
        empty.getTokenEndpointAuthMethodOrDefault(
            OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_POST),
        "tokenEndpointAuthMethod must use the default without value");
    Assertions.assertNull(empty.getTokenEndpointAuthSigningAlg(),
        "tokenEndpointAuthSigningAlg must be null without value");
    Assertions.assertEquals("two", empty.getTokenEndpointAuthSigningAlgOrDefault("two"),
        "tokenEndpointAuthSigningAlg must use the default without value");
    Assertions.assertNull(empty.getDpopBoundAccessTokens(),
        "dpopBoundAccessTokens must be null without value");
    Assertions.assertEquals(false, empty.getDpopBoundAccessTokensOrDefault(false),
        "dpopBoundAccessTokens must use the default without value");
    Assertions.assertNull(empty.getIdTokenSignedResponseAlg(),
        "idTokenSignedResponseAlg must be null without value");
    Assertions.assertEquals(OauthClientIdTokenSignedResponseAlgOptions.ES256,
        empty
            .getIdTokenSignedResponseAlgOrDefault(OauthClientIdTokenSignedResponseAlgOptions.ES256),
        "idTokenSignedResponseAlg must use the default without value");
    Assertions.assertNull(empty.getTlsClientAuthSubjectDn(),
        "tlsClientAuthSubjectDn must be null without value");
    Assertions.assertEquals("two", empty.getTlsClientAuthSubjectDnOrDefault("two"),
        "tlsClientAuthSubjectDn must use the default without value");
    Assertions.assertNull(empty.getTlsClientCertificateBoundAccessTokens(),
        "tlsClientCertificateBoundAccessTokens must be null without value");
    Assertions.assertEquals(false, empty.getTlsClientCertificateBoundAccessTokensOrDefault(false),
        "tlsClientCertificateBoundAccessTokens must use the default without value");
    Assertions.assertNull(empty.getAuthorizationSignedResponseAlg(),
        "authorizationSignedResponseAlg must be null without value");
    Assertions.assertEquals("two", empty.getAuthorizationSignedResponseAlgOrDefault("two"),
        "authorizationSignedResponseAlg must use the default without value");
    Assertions.assertNull(empty.getBackchannelTokenDeliveryMode(),
        "backchannelTokenDeliveryMode must be null without value");
    Assertions.assertEquals(OauthClientBackchannelTokenDeliveryModeOptions.PING,
        empty.getBackchannelTokenDeliveryModeOrDefault(
            OauthClientBackchannelTokenDeliveryModeOptions.PING),
        "backchannelTokenDeliveryMode must use the default without value");
    Assertions.assertNull(empty.getBackchannelClientNotificationEndpoint(),
        "backchannelClientNotificationEndpoint must be null without value");
    Assertions.assertEquals("two", empty.getBackchannelClientNotificationEndpointOrDefault("two"),
        "backchannelClientNotificationEndpoint must use the default without value");
    Assertions.assertNull(empty.getSubjectType(), "subjectType must be null without value");
    Assertions.assertEquals(OauthClientSubjectTypeOptions.PAIRWISE,
        empty.getSubjectTypeOrDefault(OauthClientSubjectTypeOptions.PAIRWISE),
        "subjectType must use the default without value");
    Assertions.assertNull(empty.getIdTokenEncryptedResponseAlg(),
        "idTokenEncryptedResponseAlg must be null without value");
    Assertions.assertEquals("two", empty.getIdTokenEncryptedResponseAlgOrDefault("two"),
        "idTokenEncryptedResponseAlg must use the default without value");
    Assertions.assertNull(empty.getIdTokenEncryptedResponseEnc(),
        "idTokenEncryptedResponseEnc must be null without value");
    Assertions.assertEquals("two", empty.getIdTokenEncryptedResponseEncOrDefault("two"),
        "idTokenEncryptedResponseEnc must use the default without value");
    Assertions.assertNull(empty.getUserinfoEncryptedResponseAlg(),
        "userinfoEncryptedResponseAlg must be null without value");
    Assertions.assertEquals("two", empty.getUserinfoEncryptedResponseAlgOrDefault("two"),
        "userinfoEncryptedResponseAlg must use the default without value");
    Assertions.assertNull(empty.getUserinfoEncryptedResponseEnc(),
        "userinfoEncryptedResponseEnc must be null without value");
    Assertions.assertEquals("two", empty.getUserinfoEncryptedResponseEncOrDefault("two"),
        "userinfoEncryptedResponseEnc must use the default without value");
    Assertions.assertNull(empty.getAuthorizationDetailsTypes(),
        "authorizationDetailsTypes must be null without value");
    Assertions.assertEquals("two", empty.getAuthorizationDetailsTypesOrDefault("two"),
        "authorizationDetailsTypes must use the default without value");
    Assertions.assertNull(empty.getUserinfoSignedResponseAlg(),
        "userinfoSignedResponseAlg must be null without value");
    Assertions.assertEquals("two", empty.getUserinfoSignedResponseAlgOrDefault("two"),
        "userinfoSignedResponseAlg must use the default without value");
    Assertions.assertNull(empty.getSecretOauth(), "secretOauth must be null without value");
    Assertions.assertNull(empty.getRegistrationAccessToken(),
        "registrationAccessToken must be null without value");
    Assertions.assertNull(empty.getDefaultAcrValues(),
        "defaultAcrValues must be null without value");
    Assertions.assertEquals("two", empty.getDefaultAcrValuesOrDefault("two"),
        "defaultAcrValues must use the default without value");
    Assertions.assertNull(empty.getDefaultMaxAge(), "defaultMaxAge must be null without value");
    Assertions.assertEquals(2, empty.getDefaultMaxAgeOrDefault(2),
        "defaultMaxAge must use the default without value");
    Assertions.assertNull(empty.getSectorIdentifierUri(),
        "sectorIdentifierUri must be null without value");
    Assertions.assertEquals("two", empty.getSectorIdentifierUriOrDefault("two"),
        "sectorIdentifierUri must use the default without value");
    Assertions.assertTrue(empty.getAllowedLoginRedirects().isEmpty(),
        "allowedLoginRedirects must be empty without value");
    Assertions.assertTrue(empty.getAllowedLogoutRedirects().isEmpty(),
        "allowedLogoutRedirects must be empty without value");
    Assertions.assertTrue(empty.getAllowedAudiences().isEmpty(),
        "allowedAudiences must be empty without value");
    Assertions.assertNull(empty.getGrantTypesJson(), "grantTypesJson must be null without value");
    Assertions.assertEquals("two", empty.getGrantTypesJsonOrDefault("two"),
        "grantTypesJson must use the default without value");
    Assertions.assertNull(empty.getIntrospectionAllowed(),
        "introspectionAllowed must be null without value");
    Assertions.assertEquals(false, empty.getIntrospectionAllowedOrDefault(false),
        "introspectionAllowed must use the default without value");
    Assertions.assertNull(empty.getRequirePushedAuthorizationRequests(),
        "requirePushedAuthorizationRequests must be null without value");
    Assertions.assertEquals(false, empty.getRequirePushedAuthorizationRequestsOrDefault(false),
        "requirePushedAuthorizationRequests must use the default without value");
    Assertions.assertNull(empty.getRequirePkce(), "requirePkce must be null without value");
    Assertions.assertEquals(false, empty.getRequirePkceOrDefault(false),
        "requirePkce must use the default without value");
    Assertions.assertNull(empty.getBackchannelLogoutUri(),
        "backchannelLogoutUri must be null without value");
    Assertions.assertEquals("two", empty.getBackchannelLogoutUriOrDefault("two"),
        "backchannelLogoutUri must use the default without value");
    Assertions.assertNull(empty.getBackchannelLogoutSessionRequired(),
        "backchannelLogoutSessionRequired must be null without value");
    Assertions.assertEquals(false, empty.getBackchannelLogoutSessionRequiredOrDefault(false),
        "backchannelLogoutSessionRequired must use the default without value");
    Assertions.assertNull(empty.getFrontchannelLogoutUri(),
        "frontchannelLogoutUri must be null without value");
    Assertions.assertEquals("two", empty.getFrontchannelLogoutUriOrDefault("two"),
        "frontchannelLogoutUri must use the default without value");
    Assertions.assertNull(empty.getFrontchannelLogoutSessionRequired(),
        "frontchannelLogoutSessionRequired must be null without value");
    Assertions.assertEquals(false, empty.getFrontchannelLogoutSessionRequiredOrDefault(false),
        "frontchannelLogoutSessionRequired must use the default without value");
    Assertions.assertNull(empty.getEnabled(), "enabled must be null without value");
    Assertions.assertEquals(false, empty.getEnabledOrDefault(false),
        "enabled must use the default without value");
    Assertions.assertNull(empty.getClientName(), "clientName must be null without value");
    Assertions.assertEquals("two", empty.getClientNameOrDefault("two"),
        "clientName must use the default without value");
    Assertions.assertNull(empty.getLogoUri(), "logoUri must be null without value");
    Assertions.assertEquals("two", empty.getLogoUriOrDefault("two"),
        "logoUri must use the default without value");
    Assertions.assertNull(empty.getClientUri(), "clientUri must be null without value");
    Assertions.assertEquals("two", empty.getClientUriOrDefault("two"),
        "clientUri must use the default without value");
    Assertions.assertNull(empty.getPolicyUri(), "policyUri must be null without value");
    Assertions.assertEquals("two", empty.getPolicyUriOrDefault("two"),
        "policyUri must use the default without value");
    Assertions.assertNull(empty.getTosUri(), "tosUri must be null without value");
    Assertions.assertEquals("two", empty.getTosUriOrDefault("two"),
        "tosUri must use the default without value");
    Assertions.assertNull(empty.getResponseTypesJson(),
        "responseTypesJson must be null without value");
    Assertions.assertEquals("two", empty.getResponseTypesJsonOrDefault("two"),
        "responseTypesJson must use the default without value");
    Assertions.assertNull(empty.getDynamicallyRegistered(),
        "dynamicallyRegistered must be null without value");
    Assertions.assertEquals(false, empty.getDynamicallyRegisteredOrDefault(false),
        "dynamicallyRegistered must use the default without value");
    Assertions.assertNull(empty.getRegisteredAt(), "registeredAt must be null without value");
    Assertions.assertEquals(
        LocalDateTime.of(1981, 9, 6, 0, 0).atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime(),
        empty.getRegisteredAtOrDefault(LocalDateTime.of(1981, 9, 6, 0, 0)
            .atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime()),
        "registeredAt must use the default without value");
    Assertions.assertNull(empty.getClientCredentialsScopes(),
        "clientCredentialsScopes must be null without value");
    Assertions.assertEquals("two", empty.getClientCredentialsScopesOrDefault("two"),
        "clientCredentialsScopes must use the default without value");
    Assertions.assertNull(empty.getClientCredentialsTokenTtlSeconds(),
        "clientCredentialsTokenTtlSeconds must be null without value");
    Assertions.assertEquals(2, empty.getClientCredentialsTokenTtlSecondsOrDefault(2),
        "clientCredentialsTokenTtlSeconds must use the default without value");
    Assertions.assertNull(empty.getRequestObjectSigningAlg(),
        "requestObjectSigningAlg must be null without value");
    Assertions.assertEquals("two", empty.getRequestObjectSigningAlgOrDefault("two"),
        "requestObjectSigningAlg must use the default without value");
    Assertions.assertNull(empty.getJwksUri(), "jwksUri must be null without value");
    Assertions.assertEquals("two", empty.getJwksUriOrDefault("two"),
        "jwksUri must use the default without value");
    Assertions.assertNull(empty.getJwksJson(), "jwksJson must be null without value");
    Assertions.assertEquals("two", empty.getJwksJsonOrDefault("two"),
        "jwksJson must use the default without value");
    Assertions.assertNull(empty.getVersion(), "version must be null without value");
    Assertions.assertEquals(2, empty.getVersionOrDefault(2),
        "version must use the default without value");
  }
}
