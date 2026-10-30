package net.civeira.phylax.feature.access.oauthclient.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

class OauthClientUnitTest {

  /**
   * @autogenerated CachedGenerator
   */
  @Test
  @DisplayName("Test a entity reference contruction")
  void test_oauth_client_builder() {
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
    OauthClient withOne = one.withVersionValue(VersionVO.from(1));
    Assertions.assertEquals(2, withOne.withNextVersion().getVersion().get());
    OauthClient deleted = one.delete();
    Assertions.assertEquals(1, deleted.getRecordedEvents().size(),
        "After delete, one event must be stored");
    Assertions.assertTrue(one.getRecordedEvents().isEmpty(),
        "No events can affect the original entity");
  }
}
