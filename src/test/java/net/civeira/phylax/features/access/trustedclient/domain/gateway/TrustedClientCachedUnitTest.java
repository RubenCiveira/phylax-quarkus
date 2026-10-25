package net.civeira.phylax.features.access.trustedclient.domain.gateway;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.civeira.phylax.features.access.tenant.domain.TenantReference;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedAudiences;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedRedirects;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClient;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientSubjectTypeOptions;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientTokenEndpointAuthMethodOptions;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.AllowAllScopesVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.AllowedAudiencesVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.AllowedLogoutRedirectsVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.AllowedRedirectsVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.AllowedScopesM2mVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.AuthorizationDetailsTypesVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.AuthorizationSignedResponseAlgVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.BackChannelLogoutSessionRequiredVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.BackChannelLogoutUriVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.BackchannelClientNotificationEndpointVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.BackchannelTokenDeliveryModeVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.ClientNameVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.ClientUriVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.CodeVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.DefaultAcrValuesVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.DefaultMaxAgeVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.DpopBoundAccessTokensVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.DynamicallyRegisteredVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.FrontChannelLogoutSessionRequiredVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.FrontChannelLogoutUriVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.GrantTypesJsonVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.IdTokenEncryptedResponseAlgVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.IdTokenEncryptedResponseEncVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.IdTokenSignedResponseAlgVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.IsResourceServerVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.JwksJsonVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.JwksUriVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.LogoUriVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.M2mTokenTtlSecondsVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.PolicyUriVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.PublicAllowVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.RegisteredAtVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.RegistrationAccessVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.RequestObjectSigningAlgVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.RequirePkceVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.RequirePushedAuthorizationRequestsVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.ResponseTypesJsonVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.SecretOauthVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.SectorIdentifierUriVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.SubjectTypeVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.TenantVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.TlsClientAuthSubjectDnVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.TlsClientCertificateBoundAccessTokensVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.TokenEndpointAuthMethodVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.TokenEndpointAuthSigningAlgVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.TosUriVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.UserinfoEncryptedResponseAlgVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.UserinfoEncryptedResponseEncVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.UserinfoSignedResponseAlgVO;
import net.civeira.phylax.features.access.trustedclient.domain.valueobject.VersionVO;

class TrustedClientCachedUnitTest {

  /**
   * @autogenerated CachedGenerator
   */
  @Test
  @DisplayName("Test a entity reference contruction")
  void test_trusted_client_cached_builder() {
    TrustedClient one = TrustedClient.builder().uidValue(UidVO.from("one"))
        .codeValue(CodeVO.from("one")).tenantValue(TenantVO.from(TenantReference.of("one")))
        .allowAllScopesValue(AllowAllScopesVO.from(true)).publicAllowValue(PublicAllowVO.from(true))
        .tokenEndpointAuthMethodValue(TokenEndpointAuthMethodVO
            .from(TrustedClientTokenEndpointAuthMethodOptions.CLIENT_SECRET_BASIC))
        .tokenEndpointAuthSigningAlgValue(TokenEndpointAuthSigningAlgVO.from("one"))
        .dpopBoundAccessTokensValue(DpopBoundAccessTokensVO.from(true))
        .idTokenSignedResponseAlgValue(
            IdTokenSignedResponseAlgVO.from(TrustedClientIdTokenSignedResponseAlgOptions.RS256))
        .tlsClientAuthSubjectDnValue(TlsClientAuthSubjectDnVO.from("one"))
        .tlsClientCertificateBoundAccessTokensValue(
            TlsClientCertificateBoundAccessTokensVO.from(true))
        .authorizationSignedResponseAlgValue(AuthorizationSignedResponseAlgVO.from("one"))
        .backchannelTokenDeliveryModeValue(BackchannelTokenDeliveryModeVO
            .from(TrustedClientBackchannelTokenDeliveryModeOptions.POLL))
        .backchannelClientNotificationEndpointValue(
            BackchannelClientNotificationEndpointVO.from("one"))
        .subjectTypeValue(SubjectTypeVO.from(TrustedClientSubjectTypeOptions.PUBLIC))
        .idTokenEncryptedResponseAlgValue(IdTokenEncryptedResponseAlgVO.from("one"))
        .idTokenEncryptedResponseEncValue(IdTokenEncryptedResponseEncVO.from("one"))
        .userinfoEncryptedResponseAlgValue(UserinfoEncryptedResponseAlgVO.from("one"))
        .userinfoEncryptedResponseEncValue(UserinfoEncryptedResponseEncVO.from("one"))
        .authorizationDetailsTypesValue(AuthorizationDetailsTypesVO.from("one"))
        .userinfoSignedResponseAlgValue(UserinfoSignedResponseAlgVO.from("one"))
        .secretOauthValue(SecretOauthVO.fromPlain("one"))
        .registrationAccessValue(RegistrationAccessVO.fromPlain("one"))
        .defaultAcrValuesValue(DefaultAcrValuesVO.from("one"))
        .defaultMaxAgeValue(DefaultMaxAgeVO.from(1))
        .sectorIdentifierUriValue(SectorIdentifierUriVO.from("one"))
        .allowedRedirectsValue(AllowedRedirectsVO
            .from(List.of(AllowedRedirects.builder().uid("one").url("one").version(1).build())))
        .allowedLogoutRedirectsValue(AllowedLogoutRedirectsVO.from(
            List.of(AllowedLogoutRedirects.builder().uid("one").url("one").version(1).build())))
        .allowedAudiencesValue(AllowedAudiencesVO.from(
            List.of(AllowedAudiences.builder().uid("one").audience("one").version(1).build())))
        .grantTypesJsonValue(GrantTypesJsonVO.from("one"))
        .isResourceServerValue(IsResourceServerVO.from(true))
        .requirePushedAuthorizationRequestsValue(RequirePushedAuthorizationRequestsVO.from(true))
        .requirePkceValue(RequirePkceVO.from(true))
        .backChannelLogoutUriValue(BackChannelLogoutUriVO.from("one"))
        .backChannelLogoutSessionRequiredValue(BackChannelLogoutSessionRequiredVO.from(true))
        .frontChannelLogoutUriValue(FrontChannelLogoutUriVO.from("one"))
        .frontChannelLogoutSessionRequiredValue(FrontChannelLogoutSessionRequiredVO.from(true))
        .enabledValue(EnabledVO.from(true)).clientNameValue(ClientNameVO.from("one"))
        .logoUriValue(LogoUriVO.from("one")).clientUriValue(ClientUriVO.from("one"))
        .policyUriValue(PolicyUriVO.from("one")).tosUriValue(TosUriVO.from("one"))
        .responseTypesJsonValue(ResponseTypesJsonVO.from("one"))
        .dynamicallyRegisteredValue(DynamicallyRegisteredVO.from(true))
        .registeredAtValue(RegisteredAtVO.from(LocalDateTime.of(1980, 8, 20, 0, 0)
            .atZone(ZoneId.of("Europe/Madrid")).toOffsetDateTime()))
        .allowedScopesM2mValue(AllowedScopesM2mVO.from("one"))
        .m2mTokenTtlSecondsValue(M2mTokenTtlSecondsVO.from(1))
        .requestObjectSigningAlgValue(RequestObjectSigningAlgVO.from("one"))
        .jwksUriValue(JwksUriVO.from("one")).jwksJsonValue(JwksJsonVO.from("one"))
        .versionValue(VersionVO.from(1)).build();
    OffsetDateTime now = OffsetDateTime.now();
    List<TrustedClient> empty = List.of();
    List<TrustedClient> some = List.of(one);
    TrustedClientCached emptyCursor = TrustedClientCached.builder().since(now).value(empty).build();
    Assertions.assertFalse(emptyCursor.first().isPresent(), "An empty list dont have first");
    TrustedClientCached someCursor = TrustedClientCached.builder().since(now).value(some).build();
    Assertions.assertEquals(one, someCursor.first().get(),
        "A list with value must retrieve firt value");
  }
}
