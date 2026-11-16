package net.civeira.phylax.feature.access.oauthclient.application.visibility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.feature.access.oauthclient.domain.AllowedAudiences;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLoginRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClient;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientReference;
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

class OauthClientVisibleProjectionUnitTest {

  /**
   * @autogenerated VisibleProjectionGenerator
   */
  @Test
  @DisplayName("Test the oauth client visible projection takes the values of an entity")
  void test_oauth_client_visible_projection_from_entity() {
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
    OauthClientVisibleProjection projection = new OauthClientVisibleProjection(entity);
    Assertions.assertEquals(Optional.of(entity.getUidValue()), projection.getUid(),
        "uid must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getCodeValue()), projection.getCode(),
        "code must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getTenantValue()), projection.getTenant(),
        "tenant must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getAllowAllScopesValue()),
        projection.getAllowAllScopes(), "allowAllScopes must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getTokenEndpointAuthMethodValue()),
        projection.getTokenEndpointAuthMethod(),
        "tokenEndpointAuthMethod must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getTokenEndpointAuthSigningAlgValue()),
        projection.getTokenEndpointAuthSigningAlg(),
        "tokenEndpointAuthSigningAlg must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getDpopBoundAccessTokensValue()),
        projection.getDpopBoundAccessTokens(),
        "dpopBoundAccessTokens must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getIdTokenSignedResponseAlgValue()),
        projection.getIdTokenSignedResponseAlg(),
        "idTokenSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getTlsClientAuthSubjectDnValue()),
        projection.getTlsClientAuthSubjectDn(),
        "tlsClientAuthSubjectDn must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getTlsClientCertificateBoundAccessTokensValue()),
        projection.getTlsClientCertificateBoundAccessTokens(),
        "tlsClientCertificateBoundAccessTokens must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getAuthorizationSignedResponseAlgValue()),
        projection.getAuthorizationSignedResponseAlg(),
        "authorizationSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getBackchannelTokenDeliveryModeValue()),
        projection.getBackchannelTokenDeliveryMode(),
        "backchannelTokenDeliveryMode must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getBackchannelClientNotificationEndpointValue()),
        projection.getBackchannelClientNotificationEndpoint(),
        "backchannelClientNotificationEndpoint must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getSubjectTypeValue()), projection.getSubjectType(),
        "subjectType must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getIdTokenEncryptedResponseAlgValue()),
        projection.getIdTokenEncryptedResponseAlg(),
        "idTokenEncryptedResponseAlg must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getIdTokenEncryptedResponseEncValue()),
        projection.getIdTokenEncryptedResponseEnc(),
        "idTokenEncryptedResponseEnc must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getUserinfoEncryptedResponseAlgValue()),
        projection.getUserinfoEncryptedResponseAlg(),
        "userinfoEncryptedResponseAlg must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getUserinfoEncryptedResponseEncValue()),
        projection.getUserinfoEncryptedResponseEnc(),
        "userinfoEncryptedResponseEnc must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getAuthorizationDetailsTypesValue()),
        projection.getAuthorizationDetailsTypes(),
        "authorizationDetailsTypes must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getUserinfoSignedResponseAlgValue()),
        projection.getUserinfoSignedResponseAlg(),
        "userinfoSignedResponseAlg must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getSecretOauthValue()), projection.getSecretOauth(),
        "secretOauth must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getRegistrationAccessTokenValue()),
        projection.getRegistrationAccessToken(),
        "registrationAccessToken must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getDefaultAcrValuesValue()),
        projection.getDefaultAcrValues(), "defaultAcrValues must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getDefaultMaxAgeValue()),
        projection.getDefaultMaxAge(), "defaultMaxAge must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getSectorIdentifierUriValue()),
        projection.getSectorIdentifierUri(), "sectorIdentifierUri must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getAllowedLoginRedirectsValue()),
        projection.getAllowedLoginRedirects(),
        "allowedLoginRedirects must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getAllowedLogoutRedirectsValue()),
        projection.getAllowedLogoutRedirects(),
        "allowedLogoutRedirects must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getAllowedAudiencesValue()),
        projection.getAllowedAudiences(), "allowedAudiences must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getGrantTypesJsonValue()),
        projection.getGrantTypesJson(), "grantTypesJson must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getIntrospectionAllowedValue()),
        projection.getIntrospectionAllowed(), "introspectionAllowed must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getRequirePushedAuthorizationRequestsValue()),
        projection.getRequirePushedAuthorizationRequests(),
        "requirePushedAuthorizationRequests must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getRequirePkceValue()), projection.getRequirePkce(),
        "requirePkce must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getBackchannelLogoutUriValue()),
        projection.getBackchannelLogoutUri(), "backchannelLogoutUri must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getBackchannelLogoutSessionRequiredValue()),
        projection.getBackchannelLogoutSessionRequired(),
        "backchannelLogoutSessionRequired must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getFrontchannelLogoutUriValue()),
        projection.getFrontchannelLogoutUri(),
        "frontchannelLogoutUri must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getFrontchannelLogoutSessionRequiredValue()),
        projection.getFrontchannelLogoutSessionRequired(),
        "frontchannelLogoutSessionRequired must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getEnabledValue()), projection.getEnabled(),
        "enabled must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getClientNameValue()), projection.getClientName(),
        "clientName must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getLogoUriValue()), projection.getLogoUri(),
        "logoUri must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getClientUriValue()), projection.getClientUri(),
        "clientUri must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getPolicyUriValue()), projection.getPolicyUri(),
        "policyUri must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getTosUriValue()), projection.getTosUri(),
        "tosUri must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getResponseTypesJsonValue()),
        projection.getResponseTypesJson(), "responseTypesJson must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getDynamicallyRegisteredValue()),
        projection.getDynamicallyRegistered(),
        "dynamicallyRegistered must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getRegisteredAtValue()),
        projection.getRegisteredAt(), "registeredAt must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getClientCredentialsScopesValue()),
        projection.getClientCredentialsScopes(),
        "clientCredentialsScopes must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getClientCredentialsTokenTtlSecondsValue()),
        projection.getClientCredentialsTokenTtlSeconds(),
        "clientCredentialsTokenTtlSeconds must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getRequestObjectSigningAlgValue()),
        projection.getRequestObjectSigningAlg(),
        "requestObjectSigningAlg must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getJwksUriValue()), projection.getJwksUri(),
        "jwksUri must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getJwksJsonValue()), projection.getJwksJson(),
        "jwksJson must be taken from the entity");
    Assertions.assertEquals(Optional.of(entity.getVersionValue()), projection.getVersion(),
        "version must be taken from the entity");
    OauthClientReference reference = projection.asReference();
    Assertions.assertEquals("one", reference.getUid(), "The reference must use the identifier");
    projection.unset("uid");
    Assertions.assertTrue(projection.getUid().isEmpty(), "uid must be removed by name");
    projection.unset("code");
    Assertions.assertTrue(projection.getCode().isEmpty(), "code must be removed by name");
    projection.unset("tenant");
    Assertions.assertTrue(projection.getTenant().isEmpty(), "tenant must be removed by name");
    projection.unset("allowAllScopes");
    Assertions.assertTrue(projection.getAllowAllScopes().isEmpty(),
        "allowAllScopes must be removed by name");
    projection.unset("tokenEndpointAuthMethod");
    Assertions.assertTrue(projection.getTokenEndpointAuthMethod().isEmpty(),
        "tokenEndpointAuthMethod must be removed by name");
    projection.unset("tokenEndpointAuthSigningAlg");
    Assertions.assertTrue(projection.getTokenEndpointAuthSigningAlg().isEmpty(),
        "tokenEndpointAuthSigningAlg must be removed by name");
    projection.unset("dpopBoundAccessTokens");
    Assertions.assertTrue(projection.getDpopBoundAccessTokens().isEmpty(),
        "dpopBoundAccessTokens must be removed by name");
    projection.unset("idTokenSignedResponseAlg");
    Assertions.assertTrue(projection.getIdTokenSignedResponseAlg().isEmpty(),
        "idTokenSignedResponseAlg must be removed by name");
    projection.unset("tlsClientAuthSubjectDn");
    Assertions.assertTrue(projection.getTlsClientAuthSubjectDn().isEmpty(),
        "tlsClientAuthSubjectDn must be removed by name");
    projection.unset("tlsClientCertificateBoundAccessTokens");
    Assertions.assertTrue(projection.getTlsClientCertificateBoundAccessTokens().isEmpty(),
        "tlsClientCertificateBoundAccessTokens must be removed by name");
    projection.unset("authorizationSignedResponseAlg");
    Assertions.assertTrue(projection.getAuthorizationSignedResponseAlg().isEmpty(),
        "authorizationSignedResponseAlg must be removed by name");
    projection.unset("backchannelTokenDeliveryMode");
    Assertions.assertTrue(projection.getBackchannelTokenDeliveryMode().isEmpty(),
        "backchannelTokenDeliveryMode must be removed by name");
    projection.unset("backchannelClientNotificationEndpoint");
    Assertions.assertTrue(projection.getBackchannelClientNotificationEndpoint().isEmpty(),
        "backchannelClientNotificationEndpoint must be removed by name");
    projection.unset("subjectType");
    Assertions.assertTrue(projection.getSubjectType().isEmpty(),
        "subjectType must be removed by name");
    projection.unset("idTokenEncryptedResponseAlg");
    Assertions.assertTrue(projection.getIdTokenEncryptedResponseAlg().isEmpty(),
        "idTokenEncryptedResponseAlg must be removed by name");
    projection.unset("idTokenEncryptedResponseEnc");
    Assertions.assertTrue(projection.getIdTokenEncryptedResponseEnc().isEmpty(),
        "idTokenEncryptedResponseEnc must be removed by name");
    projection.unset("userinfoEncryptedResponseAlg");
    Assertions.assertTrue(projection.getUserinfoEncryptedResponseAlg().isEmpty(),
        "userinfoEncryptedResponseAlg must be removed by name");
    projection.unset("userinfoEncryptedResponseEnc");
    Assertions.assertTrue(projection.getUserinfoEncryptedResponseEnc().isEmpty(),
        "userinfoEncryptedResponseEnc must be removed by name");
    projection.unset("authorizationDetailsTypes");
    Assertions.assertTrue(projection.getAuthorizationDetailsTypes().isEmpty(),
        "authorizationDetailsTypes must be removed by name");
    projection.unset("userinfoSignedResponseAlg");
    Assertions.assertTrue(projection.getUserinfoSignedResponseAlg().isEmpty(),
        "userinfoSignedResponseAlg must be removed by name");
    projection.unset("secretOauth");
    Assertions.assertTrue(projection.getSecretOauth().isEmpty(),
        "secretOauth must be removed by name");
    projection.unset("registrationAccessToken");
    Assertions.assertTrue(projection.getRegistrationAccessToken().isEmpty(),
        "registrationAccessToken must be removed by name");
    projection.unset("defaultAcrValues");
    Assertions.assertTrue(projection.getDefaultAcrValues().isEmpty(),
        "defaultAcrValues must be removed by name");
    projection.unset("defaultMaxAge");
    Assertions.assertTrue(projection.getDefaultMaxAge().isEmpty(),
        "defaultMaxAge must be removed by name");
    projection.unset("sectorIdentifierUri");
    Assertions.assertTrue(projection.getSectorIdentifierUri().isEmpty(),
        "sectorIdentifierUri must be removed by name");
    projection.unset("allowedLoginRedirects");
    Assertions.assertTrue(projection.getAllowedLoginRedirects().isEmpty(),
        "allowedLoginRedirects must be removed by name");
    projection.unset("allowedLogoutRedirects");
    Assertions.assertTrue(projection.getAllowedLogoutRedirects().isEmpty(),
        "allowedLogoutRedirects must be removed by name");
    projection.unset("allowedAudiences");
    Assertions.assertTrue(projection.getAllowedAudiences().isEmpty(),
        "allowedAudiences must be removed by name");
    projection.unset("grantTypesJson");
    Assertions.assertTrue(projection.getGrantTypesJson().isEmpty(),
        "grantTypesJson must be removed by name");
    projection.unset("introspectionAllowed");
    Assertions.assertTrue(projection.getIntrospectionAllowed().isEmpty(),
        "introspectionAllowed must be removed by name");
    projection.unset("requirePushedAuthorizationRequests");
    Assertions.assertTrue(projection.getRequirePushedAuthorizationRequests().isEmpty(),
        "requirePushedAuthorizationRequests must be removed by name");
    projection.unset("requirePkce");
    Assertions.assertTrue(projection.getRequirePkce().isEmpty(),
        "requirePkce must be removed by name");
    projection.unset("backchannelLogoutUri");
    Assertions.assertTrue(projection.getBackchannelLogoutUri().isEmpty(),
        "backchannelLogoutUri must be removed by name");
    projection.unset("backchannelLogoutSessionRequired");
    Assertions.assertTrue(projection.getBackchannelLogoutSessionRequired().isEmpty(),
        "backchannelLogoutSessionRequired must be removed by name");
    projection.unset("frontchannelLogoutUri");
    Assertions.assertTrue(projection.getFrontchannelLogoutUri().isEmpty(),
        "frontchannelLogoutUri must be removed by name");
    projection.unset("frontchannelLogoutSessionRequired");
    Assertions.assertTrue(projection.getFrontchannelLogoutSessionRequired().isEmpty(),
        "frontchannelLogoutSessionRequired must be removed by name");
    projection.unset("enabled");
    Assertions.assertTrue(projection.getEnabled().isEmpty(), "enabled must be removed by name");
    projection.unset("clientName");
    Assertions.assertTrue(projection.getClientName().isEmpty(),
        "clientName must be removed by name");
    projection.unset("logoUri");
    Assertions.assertTrue(projection.getLogoUri().isEmpty(), "logoUri must be removed by name");
    projection.unset("clientUri");
    Assertions.assertTrue(projection.getClientUri().isEmpty(), "clientUri must be removed by name");
    projection.unset("policyUri");
    Assertions.assertTrue(projection.getPolicyUri().isEmpty(), "policyUri must be removed by name");
    projection.unset("tosUri");
    Assertions.assertTrue(projection.getTosUri().isEmpty(), "tosUri must be removed by name");
    projection.unset("responseTypesJson");
    Assertions.assertTrue(projection.getResponseTypesJson().isEmpty(),
        "responseTypesJson must be removed by name");
    projection.unset("dynamicallyRegistered");
    Assertions.assertTrue(projection.getDynamicallyRegistered().isEmpty(),
        "dynamicallyRegistered must be removed by name");
    projection.unset("registeredAt");
    Assertions.assertTrue(projection.getRegisteredAt().isEmpty(),
        "registeredAt must be removed by name");
    projection.unset("clientCredentialsScopes");
    Assertions.assertTrue(projection.getClientCredentialsScopes().isEmpty(),
        "clientCredentialsScopes must be removed by name");
    projection.unset("clientCredentialsTokenTtlSeconds");
    Assertions.assertTrue(projection.getClientCredentialsTokenTtlSeconds().isEmpty(),
        "clientCredentialsTokenTtlSeconds must be removed by name");
    projection.unset("requestObjectSigningAlg");
    Assertions.assertTrue(projection.getRequestObjectSigningAlg().isEmpty(),
        "requestObjectSigningAlg must be removed by name");
    projection.unset("jwksUri");
    Assertions.assertTrue(projection.getJwksUri().isEmpty(), "jwksUri must be removed by name");
    projection.unset("jwksJson");
    Assertions.assertTrue(projection.getJwksJson().isEmpty(), "jwksJson must be removed by name");
    projection.unset("version");
    Assertions.assertTrue(projection.getVersion().isEmpty(), "version must be removed by name");
    Assertions.assertThrows(NoSuchElementException.class, projection::asReference,
        "Without identifier there is no reference");
  }

  /**
   * @autogenerated VisibleProjectionGenerator
   */
  @Test
  @DisplayName("Test the oauth client visible projection setters and unsetters")
  void test_oauth_client_visible_projection_setters() {
    OauthClientVisibleProjection empty = OauthClientVisibleProjection.builder().build();
    Assertions.assertTrue(empty.getUid().isEmpty(), "uid must be empty by default");
    Assertions.assertSame(empty, empty.setUid("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getUid().isPresent(), "uid must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetUid(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getUid().isEmpty(), "uid must be removed by the unset");
    Assertions.assertTrue(empty.getCode().isEmpty(), "code must be empty by default");
    Assertions.assertSame(empty, empty.setCode("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getCode().isPresent(), "code must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetCode(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getCode().isEmpty(), "code must be removed by the unset");
    Assertions.assertTrue(empty.getTenant().isEmpty(), "tenant must be empty by default");
    Assertions.assertSame(empty, empty.setTenant(TenantReference.of("one")),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getTenant().isPresent(), "tenant must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetTenant(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getTenant().isEmpty(), "tenant must be removed by the unset");
    Assertions.assertTrue(empty.getAllowAllScopes().isEmpty(),
        "allowAllScopes must be empty by default");
    Assertions.assertSame(empty, empty.setAllowAllScopes(true),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getAllowAllScopes().isPresent(),
        "allowAllScopes must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetAllowAllScopes(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getAllowAllScopes().isEmpty(),
        "allowAllScopes must be removed by the unset");
    Assertions.assertTrue(empty.getTokenEndpointAuthMethod().isEmpty(),
        "tokenEndpointAuthMethod must be empty by default");
    Assertions.assertSame(empty,
        empty.setTokenEndpointAuthMethod(
            OauthClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_BASIC),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getTokenEndpointAuthMethod().isPresent(),
        "tokenEndpointAuthMethod must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetTokenEndpointAuthMethod(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getTokenEndpointAuthMethod().isEmpty(),
        "tokenEndpointAuthMethod must be removed by the unset");
    Assertions.assertTrue(empty.getTokenEndpointAuthSigningAlg().isEmpty(),
        "tokenEndpointAuthSigningAlg must be empty by default");
    Assertions.assertSame(empty, empty.setTokenEndpointAuthSigningAlg("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getTokenEndpointAuthSigningAlg().isPresent(),
        "tokenEndpointAuthSigningAlg must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetTokenEndpointAuthSigningAlg(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getTokenEndpointAuthSigningAlg().isEmpty(),
        "tokenEndpointAuthSigningAlg must be removed by the unset");
    Assertions.assertTrue(empty.getDpopBoundAccessTokens().isEmpty(),
        "dpopBoundAccessTokens must be empty by default");
    Assertions.assertSame(empty, empty.setDpopBoundAccessTokens(true),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getDpopBoundAccessTokens().isPresent(),
        "dpopBoundAccessTokens must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetDpopBoundAccessTokens(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getDpopBoundAccessTokens().isEmpty(),
        "dpopBoundAccessTokens must be removed by the unset");
    Assertions.assertTrue(empty.getIdTokenSignedResponseAlg().isEmpty(),
        "idTokenSignedResponseAlg must be empty by default");
    Assertions.assertSame(empty,
        empty.setIdTokenSignedResponseAlg(OauthClientIdTokenSignedResponseAlgOptions.RS256),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getIdTokenSignedResponseAlg().isPresent(),
        "idTokenSignedResponseAlg must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetIdTokenSignedResponseAlg(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getIdTokenSignedResponseAlg().isEmpty(),
        "idTokenSignedResponseAlg must be removed by the unset");
    Assertions.assertTrue(empty.getTlsClientAuthSubjectDn().isEmpty(),
        "tlsClientAuthSubjectDn must be empty by default");
    Assertions.assertSame(empty, empty.setTlsClientAuthSubjectDn("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getTlsClientAuthSubjectDn().isPresent(),
        "tlsClientAuthSubjectDn must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetTlsClientAuthSubjectDn(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getTlsClientAuthSubjectDn().isEmpty(),
        "tlsClientAuthSubjectDn must be removed by the unset");
    Assertions.assertTrue(empty.getTlsClientCertificateBoundAccessTokens().isEmpty(),
        "tlsClientCertificateBoundAccessTokens must be empty by default");
    Assertions.assertSame(empty, empty.setTlsClientCertificateBoundAccessTokens(true),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getTlsClientCertificateBoundAccessTokens().isPresent(),
        "tlsClientCertificateBoundAccessTokens must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetTlsClientCertificateBoundAccessTokens(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getTlsClientCertificateBoundAccessTokens().isEmpty(),
        "tlsClientCertificateBoundAccessTokens must be removed by the unset");
    Assertions.assertTrue(empty.getAuthorizationSignedResponseAlg().isEmpty(),
        "authorizationSignedResponseAlg must be empty by default");
    Assertions.assertSame(empty, empty.setAuthorizationSignedResponseAlg("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getAuthorizationSignedResponseAlg().isPresent(),
        "authorizationSignedResponseAlg must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetAuthorizationSignedResponseAlg(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getAuthorizationSignedResponseAlg().isEmpty(),
        "authorizationSignedResponseAlg must be removed by the unset");
    Assertions.assertTrue(empty.getBackchannelTokenDeliveryMode().isEmpty(),
        "backchannelTokenDeliveryMode must be empty by default");
    Assertions.assertSame(empty,
        empty.setBackchannelTokenDeliveryMode(OauthClientBackchannelTokenDeliveryModeOptions.POLL),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getBackchannelTokenDeliveryMode().isPresent(),
        "backchannelTokenDeliveryMode must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetBackchannelTokenDeliveryMode(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getBackchannelTokenDeliveryMode().isEmpty(),
        "backchannelTokenDeliveryMode must be removed by the unset");
    Assertions.assertTrue(empty.getBackchannelClientNotificationEndpoint().isEmpty(),
        "backchannelClientNotificationEndpoint must be empty by default");
    Assertions.assertSame(empty, empty.setBackchannelClientNotificationEndpoint("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getBackchannelClientNotificationEndpoint().isPresent(),
        "backchannelClientNotificationEndpoint must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetBackchannelClientNotificationEndpoint(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getBackchannelClientNotificationEndpoint().isEmpty(),
        "backchannelClientNotificationEndpoint must be removed by the unset");
    Assertions.assertTrue(empty.getSubjectType().isEmpty(), "subjectType must be empty by default");
    Assertions.assertSame(empty, empty.setSubjectType(OauthClientSubjectTypeOptions.PUBLIC),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getSubjectType().isPresent(),
        "subjectType must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetSubjectType(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getSubjectType().isEmpty(),
        "subjectType must be removed by the unset");
    Assertions.assertTrue(empty.getIdTokenEncryptedResponseAlg().isEmpty(),
        "idTokenEncryptedResponseAlg must be empty by default");
    Assertions.assertSame(empty, empty.setIdTokenEncryptedResponseAlg("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getIdTokenEncryptedResponseAlg().isPresent(),
        "idTokenEncryptedResponseAlg must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetIdTokenEncryptedResponseAlg(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getIdTokenEncryptedResponseAlg().isEmpty(),
        "idTokenEncryptedResponseAlg must be removed by the unset");
    Assertions.assertTrue(empty.getIdTokenEncryptedResponseEnc().isEmpty(),
        "idTokenEncryptedResponseEnc must be empty by default");
    Assertions.assertSame(empty, empty.setIdTokenEncryptedResponseEnc("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getIdTokenEncryptedResponseEnc().isPresent(),
        "idTokenEncryptedResponseEnc must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetIdTokenEncryptedResponseEnc(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getIdTokenEncryptedResponseEnc().isEmpty(),
        "idTokenEncryptedResponseEnc must be removed by the unset");
    Assertions.assertTrue(empty.getUserinfoEncryptedResponseAlg().isEmpty(),
        "userinfoEncryptedResponseAlg must be empty by default");
    Assertions.assertSame(empty, empty.setUserinfoEncryptedResponseAlg("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getUserinfoEncryptedResponseAlg().isPresent(),
        "userinfoEncryptedResponseAlg must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetUserinfoEncryptedResponseAlg(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getUserinfoEncryptedResponseAlg().isEmpty(),
        "userinfoEncryptedResponseAlg must be removed by the unset");
    Assertions.assertTrue(empty.getUserinfoEncryptedResponseEnc().isEmpty(),
        "userinfoEncryptedResponseEnc must be empty by default");
    Assertions.assertSame(empty, empty.setUserinfoEncryptedResponseEnc("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getUserinfoEncryptedResponseEnc().isPresent(),
        "userinfoEncryptedResponseEnc must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetUserinfoEncryptedResponseEnc(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getUserinfoEncryptedResponseEnc().isEmpty(),
        "userinfoEncryptedResponseEnc must be removed by the unset");
    Assertions.assertTrue(empty.getAuthorizationDetailsTypes().isEmpty(),
        "authorizationDetailsTypes must be empty by default");
    Assertions.assertSame(empty, empty.setAuthorizationDetailsTypes("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getAuthorizationDetailsTypes().isPresent(),
        "authorizationDetailsTypes must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetAuthorizationDetailsTypes(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getAuthorizationDetailsTypes().isEmpty(),
        "authorizationDetailsTypes must be removed by the unset");
    Assertions.assertTrue(empty.getUserinfoSignedResponseAlg().isEmpty(),
        "userinfoSignedResponseAlg must be empty by default");
    Assertions.assertSame(empty, empty.setUserinfoSignedResponseAlg("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getUserinfoSignedResponseAlg().isPresent(),
        "userinfoSignedResponseAlg must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetUserinfoSignedResponseAlg(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getUserinfoSignedResponseAlg().isEmpty(),
        "userinfoSignedResponseAlg must be removed by the unset");
    Assertions.assertTrue(empty.getDefaultAcrValues().isEmpty(),
        "defaultAcrValues must be empty by default");
    Assertions.assertSame(empty, empty.setDefaultAcrValues("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getDefaultAcrValues().isPresent(),
        "defaultAcrValues must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetDefaultAcrValues(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getDefaultAcrValues().isEmpty(),
        "defaultAcrValues must be removed by the unset");
    Assertions.assertTrue(empty.getDefaultMaxAge().isEmpty(),
        "defaultMaxAge must be empty by default");
    Assertions.assertSame(empty, empty.setDefaultMaxAge(1), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getDefaultMaxAge().isPresent(),
        "defaultMaxAge must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetDefaultMaxAge(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getDefaultMaxAge().isEmpty(),
        "defaultMaxAge must be removed by the unset");
    Assertions.assertTrue(empty.getSectorIdentifierUri().isEmpty(),
        "sectorIdentifierUri must be empty by default");
    Assertions.assertSame(empty, empty.setSectorIdentifierUri("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getSectorIdentifierUri().isPresent(),
        "sectorIdentifierUri must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetSectorIdentifierUri(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getSectorIdentifierUri().isEmpty(),
        "sectorIdentifierUri must be removed by the unset");
    Assertions.assertTrue(empty.getAllowedLoginRedirects().isEmpty(),
        "allowedLoginRedirects must be empty by default");
    Assertions.assertSame(empty,
        empty.setAllowedLoginRedirects(
            List.of(AllowedLoginRedirects.builder().uid("one").url("one").version(1).build())),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getAllowedLoginRedirects().isPresent(),
        "allowedLoginRedirects must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetAllowedLoginRedirects(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getAllowedLoginRedirects().isEmpty(),
        "allowedLoginRedirects must be removed by the unset");
    Assertions.assertTrue(empty.getAllowedLogoutRedirects().isEmpty(),
        "allowedLogoutRedirects must be empty by default");
    Assertions.assertSame(empty,
        empty.setAllowedLogoutRedirects(
            List.of(AllowedLogoutRedirects.builder().uid("one").url("one").version(1).build())),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getAllowedLogoutRedirects().isPresent(),
        "allowedLogoutRedirects must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetAllowedLogoutRedirects(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getAllowedLogoutRedirects().isEmpty(),
        "allowedLogoutRedirects must be removed by the unset");
    Assertions.assertTrue(empty.getAllowedAudiences().isEmpty(),
        "allowedAudiences must be empty by default");
    Assertions.assertSame(empty,
        empty.setAllowedAudiences(
            List.of(AllowedAudiences.builder().uid("one").audience("one").version(1).build())),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getAllowedAudiences().isPresent(),
        "allowedAudiences must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetAllowedAudiences(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getAllowedAudiences().isEmpty(),
        "allowedAudiences must be removed by the unset");
    Assertions.assertTrue(empty.getGrantTypesJson().isEmpty(),
        "grantTypesJson must be empty by default");
    Assertions.assertSame(empty, empty.setGrantTypesJson("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getGrantTypesJson().isPresent(),
        "grantTypesJson must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetGrantTypesJson(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getGrantTypesJson().isEmpty(),
        "grantTypesJson must be removed by the unset");
    Assertions.assertTrue(empty.getIntrospectionAllowed().isEmpty(),
        "introspectionAllowed must be empty by default");
    Assertions.assertSame(empty, empty.setIntrospectionAllowed(true),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getIntrospectionAllowed().isPresent(),
        "introspectionAllowed must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetIntrospectionAllowed(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getIntrospectionAllowed().isEmpty(),
        "introspectionAllowed must be removed by the unset");
    Assertions.assertTrue(empty.getRequirePushedAuthorizationRequests().isEmpty(),
        "requirePushedAuthorizationRequests must be empty by default");
    Assertions.assertSame(empty, empty.setRequirePushedAuthorizationRequests(true),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getRequirePushedAuthorizationRequests().isPresent(),
        "requirePushedAuthorizationRequests must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetRequirePushedAuthorizationRequests(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getRequirePushedAuthorizationRequests().isEmpty(),
        "requirePushedAuthorizationRequests must be removed by the unset");
    Assertions.assertTrue(empty.getRequirePkce().isEmpty(), "requirePkce must be empty by default");
    Assertions.assertSame(empty, empty.setRequirePkce(true), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getRequirePkce().isPresent(),
        "requirePkce must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetRequirePkce(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getRequirePkce().isEmpty(),
        "requirePkce must be removed by the unset");
    Assertions.assertTrue(empty.getBackchannelLogoutUri().isEmpty(),
        "backchannelLogoutUri must be empty by default");
    Assertions.assertSame(empty, empty.setBackchannelLogoutUri("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getBackchannelLogoutUri().isPresent(),
        "backchannelLogoutUri must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetBackchannelLogoutUri(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getBackchannelLogoutUri().isEmpty(),
        "backchannelLogoutUri must be removed by the unset");
    Assertions.assertTrue(empty.getBackchannelLogoutSessionRequired().isEmpty(),
        "backchannelLogoutSessionRequired must be empty by default");
    Assertions.assertSame(empty, empty.setBackchannelLogoutSessionRequired(true),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getBackchannelLogoutSessionRequired().isPresent(),
        "backchannelLogoutSessionRequired must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetBackchannelLogoutSessionRequired(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getBackchannelLogoutSessionRequired().isEmpty(),
        "backchannelLogoutSessionRequired must be removed by the unset");
    Assertions.assertTrue(empty.getFrontchannelLogoutUri().isEmpty(),
        "frontchannelLogoutUri must be empty by default");
    Assertions.assertSame(empty, empty.setFrontchannelLogoutUri("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getFrontchannelLogoutUri().isPresent(),
        "frontchannelLogoutUri must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetFrontchannelLogoutUri(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getFrontchannelLogoutUri().isEmpty(),
        "frontchannelLogoutUri must be removed by the unset");
    Assertions.assertTrue(empty.getFrontchannelLogoutSessionRequired().isEmpty(),
        "frontchannelLogoutSessionRequired must be empty by default");
    Assertions.assertSame(empty, empty.setFrontchannelLogoutSessionRequired(true),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getFrontchannelLogoutSessionRequired().isPresent(),
        "frontchannelLogoutSessionRequired must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetFrontchannelLogoutSessionRequired(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getFrontchannelLogoutSessionRequired().isEmpty(),
        "frontchannelLogoutSessionRequired must be removed by the unset");
    Assertions.assertTrue(empty.getEnabled().isEmpty(), "enabled must be empty by default");
    Assertions.assertSame(empty, empty.setEnabled(true), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getEnabled().isPresent(), "enabled must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetEnabled(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getEnabled().isEmpty(), "enabled must be removed by the unset");
    Assertions.assertTrue(empty.getClientName().isEmpty(), "clientName must be empty by default");
    Assertions.assertSame(empty, empty.setClientName("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getClientName().isPresent(),
        "clientName must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetClientName(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getClientName().isEmpty(),
        "clientName must be removed by the unset");
    Assertions.assertTrue(empty.getLogoUri().isEmpty(), "logoUri must be empty by default");
    Assertions.assertSame(empty, empty.setLogoUri("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getLogoUri().isPresent(), "logoUri must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetLogoUri(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getLogoUri().isEmpty(), "logoUri must be removed by the unset");
    Assertions.assertTrue(empty.getClientUri().isEmpty(), "clientUri must be empty by default");
    Assertions.assertSame(empty, empty.setClientUri("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getClientUri().isPresent(),
        "clientUri must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetClientUri(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getClientUri().isEmpty(), "clientUri must be removed by the unset");
    Assertions.assertTrue(empty.getPolicyUri().isEmpty(), "policyUri must be empty by default");
    Assertions.assertSame(empty, empty.setPolicyUri("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getPolicyUri().isPresent(),
        "policyUri must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetPolicyUri(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getPolicyUri().isEmpty(), "policyUri must be removed by the unset");
    Assertions.assertTrue(empty.getTosUri().isEmpty(), "tosUri must be empty by default");
    Assertions.assertSame(empty, empty.setTosUri("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getTosUri().isPresent(), "tosUri must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetTosUri(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getTosUri().isEmpty(), "tosUri must be removed by the unset");
    Assertions.assertTrue(empty.getResponseTypesJson().isEmpty(),
        "responseTypesJson must be empty by default");
    Assertions.assertSame(empty, empty.setResponseTypesJson("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getResponseTypesJson().isPresent(),
        "responseTypesJson must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetResponseTypesJson(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getResponseTypesJson().isEmpty(),
        "responseTypesJson must be removed by the unset");
    Assertions.assertTrue(empty.getDynamicallyRegistered().isEmpty(),
        "dynamicallyRegistered must be empty by default");
    Assertions.assertSame(empty, empty.setDynamicallyRegistered(true),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getDynamicallyRegistered().isPresent(),
        "dynamicallyRegistered must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetDynamicallyRegistered(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getDynamicallyRegistered().isEmpty(),
        "dynamicallyRegistered must be removed by the unset");
    Assertions.assertTrue(empty.getRegisteredAt().isEmpty(),
        "registeredAt must be empty by default");
    Assertions.assertSame(
        empty, empty.setRegisteredAt(LocalDateTime.of(1980, 8, 20, 0, 0)
            .atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime()),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getRegisteredAt().isPresent(),
        "registeredAt must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetRegisteredAt(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getRegisteredAt().isEmpty(),
        "registeredAt must be removed by the unset");
    Assertions.assertTrue(empty.getClientCredentialsScopes().isEmpty(),
        "clientCredentialsScopes must be empty by default");
    Assertions.assertSame(empty, empty.setClientCredentialsScopes("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getClientCredentialsScopes().isPresent(),
        "clientCredentialsScopes must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetClientCredentialsScopes(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getClientCredentialsScopes().isEmpty(),
        "clientCredentialsScopes must be removed by the unset");
    Assertions.assertTrue(empty.getClientCredentialsTokenTtlSeconds().isEmpty(),
        "clientCredentialsTokenTtlSeconds must be empty by default");
    Assertions.assertSame(empty, empty.setClientCredentialsTokenTtlSeconds(1),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getClientCredentialsTokenTtlSeconds().isPresent(),
        "clientCredentialsTokenTtlSeconds must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetClientCredentialsTokenTtlSeconds(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getClientCredentialsTokenTtlSeconds().isEmpty(),
        "clientCredentialsTokenTtlSeconds must be removed by the unset");
    Assertions.assertTrue(empty.getRequestObjectSigningAlg().isEmpty(),
        "requestObjectSigningAlg must be empty by default");
    Assertions.assertSame(empty, empty.setRequestObjectSigningAlg("one"),
        "The setter must allow a flow code");
    Assertions.assertTrue(empty.getRequestObjectSigningAlg().isPresent(),
        "requestObjectSigningAlg must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetRequestObjectSigningAlg(),
        "The unset must allow a flow code");
    Assertions.assertTrue(empty.getRequestObjectSigningAlg().isEmpty(),
        "requestObjectSigningAlg must be removed by the unset");
    Assertions.assertTrue(empty.getJwksUri().isEmpty(), "jwksUri must be empty by default");
    Assertions.assertSame(empty, empty.setJwksUri("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getJwksUri().isPresent(), "jwksUri must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetJwksUri(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getJwksUri().isEmpty(), "jwksUri must be removed by the unset");
    Assertions.assertTrue(empty.getJwksJson().isEmpty(), "jwksJson must be empty by default");
    Assertions.assertSame(empty, empty.setJwksJson("one"), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getJwksJson().isPresent(),
        "jwksJson must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetJwksJson(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getJwksJson().isEmpty(), "jwksJson must be removed by the unset");
    Assertions.assertTrue(empty.getVersion().isEmpty(), "version must be empty by default");
    Assertions.assertSame(empty, empty.setVersion(1), "The setter must allow a flow code");
    Assertions.assertTrue(empty.getVersion().isPresent(), "version must be assigned by the setter");
    Assertions.assertSame(empty, empty.unsetVersion(), "The unset must allow a flow code");
    Assertions.assertTrue(empty.getVersion().isEmpty(), "version must be removed by the unset");
  }
}
