package net.civeira.phylax.feature.access.oauthclient.infrastructure.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.opentelemetry.api.OpenTelemetry;
import net.civeira.phylax.common.crypto.SecretDigester;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.infrastructure.sql.OptimistLockException;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedAudiences;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLoginRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClient;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientReference;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientSubjectTypeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientTokenEndpointAuthMethodOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientCursor;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientFilter;
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
import net.civeira.phylax.testing.sql.InMemoryJdbc;

class OauthClientJdbcDaoUnitTest {

  /**
   * @autogenerated RepositoryJdbcTestGenerator
   */
  @Test
  @DisplayName("Test the repository of oauth client store an entity and read the same values")
  void test_oauth_client_jdbc_dao_create_and_retrieve() {
    InMemoryJdbc jdbc = new InMemoryJdbc();
    OauthClientJdbcDao dao = new OauthClientJdbcDao(jdbc.dataSource(),
        OpenTelemetry.noop().getTracer("test"), false, new SecretDigester("p1:test-pepper", "p1"));
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
    OauthClientFilter all = OauthClientFilter.builder().build();
    OauthClient created = dao.create(item);
    Assertions.assertEquals(item.getUid(), created.getUid(),
        "The created entity must keep the identifier");
    Optional<OauthClient> found = dao.retrieve(item.getUid(), Optional.empty());
    Assertions.assertTrue(found.isPresent(), "The stored entity must be retrieved");
    Assertions.assertTrue(InMemoryJdbc.sameValues(item.toMap(), found.get().toMap()),
        () -> "The values read " + found.get().toMap() + " must be the stored " + item.toMap());
    Assertions.assertEquals(item.getAllowedLoginRedirects().size(),
        found.get().getAllowedLoginRedirects().size(),
        "The childs of allowed login redirects must be stored and read");
    Assertions.assertEquals(item.getAllowedLogoutRedirects().size(),
        found.get().getAllowedLogoutRedirects().size(),
        "The childs of allowed logout redirects must be stored and read");
    Assertions.assertEquals(item.getAllowedAudiences().size(),
        found.get().getAllowedAudiences().size(),
        "The childs of allowed audiences must be stored and read");
    Assertions.assertTrue(dao.retrieveForUpdate(item.getUid(), Optional.empty()).isPresent(),
        "The stored entity must be retrieved for update");
    Assertions.assertTrue(dao.retrieve(item.getUid(), Optional.of(all)).isPresent(),
        "The stored entity must be retrieved with a filter");
    Assertions.assertTrue(dao.exists(item.getUid(), Optional.empty()),
        "The stored entity must exists");
    Assertions.assertTrue(dao.existsForUpdate(item.getUid(), Optional.empty()),
        "The stored entity must exists for update");
    Assertions.assertFalse(dao.exists("two", Optional.empty()), "Other entity must not exists");
    Assertions.assertFalse(dao.existsForUpdate("two", Optional.empty()),
        "Other entity must not exists for update");
    Assertions.assertSame(item, dao.resolve(item), "A full entity is resolved by itself");
    Assertions.assertEquals(item.getUid(),
        dao.resolve(OauthClientReference.of(item.getUid())).getUid(),
        "A reference is resolved from the store");
    Assertions.assertEquals(item.getUid(),
        dao.resolveForUpdate(OauthClientReference.of(item.getUid())).getUid(),
        "A reference is resolved for update from the store");
    Assertions.assertThrows(NotFoundException.class,
        () -> dao.resolve(OauthClientReference.of("two")),
        "An inexistent reference is not resolved");
    Assertions.assertThrows(NotFoundException.class,
        () -> dao.resolveForUpdate(OauthClientReference.of("two")),
        "An inexistent reference is not resolved for update");
  }

  /**
   * @autogenerated RepositoryJdbcTestGenerator
   */
  @Test
  @DisplayName("Test the repository of oauth client delete the stored entity")
  void test_oauth_client_jdbc_dao_delete() {
    InMemoryJdbc jdbc = new InMemoryJdbc();
    OauthClientJdbcDao dao = new OauthClientJdbcDao(jdbc.dataSource(),
        OpenTelemetry.noop().getTracer("test"), false, new SecretDigester("p1:test-pepper", "p1"));
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
    OauthClientFilter all = OauthClientFilter.builder().build();
    dao.create(item);
    dao.delete(item);
    Assertions.assertEquals(0, dao.count(all), "The deleted entity must disappear");
    Assertions.assertThrows(IllegalArgumentException.class, () -> dao.delete(item),
        "An inexistent entity can not be deleted");
  }

  /**
   * @autogenerated RepositoryJdbcTestGenerator
   */
  @Test
  @DisplayName("Test the repository of oauth client propagate the database failures")
  void test_oauth_client_jdbc_dao_failures() {
    InMemoryJdbc jdbc = new InMemoryJdbc();
    OauthClientJdbcDao dao = new OauthClientJdbcDao(jdbc.dataSource(),
        OpenTelemetry.noop().getTracer("test"), false, new SecretDigester("p1:test-pepper", "p1"));
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
    OauthClientFilter all = OauthClientFilter.builder().build();
    jdbc.failOn(sql -> true);
    Assertions.assertThrows(RuntimeException.class, () -> dao.create(item),
        "Create failures must be propagated");
    Assertions.assertThrows(RuntimeException.class,
        () -> dao.retrieve(item.getUid(), Optional.empty()),
        "Retrieve failures must be propagated");
    Assertions.assertThrows(RuntimeException.class, () -> dao.count(all),
        "Count failures must be propagated");
    Assertions.assertThrows(RuntimeException.class, () -> dao.update(item),
        "Update failures must be propagated");
    Assertions.assertThrows(RuntimeException.class, () -> dao.delete(item),
        "Delete failures must be propagated");
  }

  /**
   * @autogenerated RepositoryJdbcTestGenerator
   */
  @Test
  @DisplayName("Test the repository of oauth client count, list and slide the stored entities")
  void test_oauth_client_jdbc_dao_list() {
    InMemoryJdbc jdbc = new InMemoryJdbc();
    OauthClientJdbcDao dao = new OauthClientJdbcDao(jdbc.dataSource(),
        OpenTelemetry.noop().getTracer("test"), false, new SecretDigester("p1:test-pepper", "p1"));
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
    OauthClientFilter all = OauthClientFilter.builder().build();
    Assertions.assertEquals(0, dao.count(all), "Without data there is nothing to count");
    Assertions.assertTrue(dao.find(all).isEmpty(), "Without data there is nothing to find");
    dao.create(item);
    Assertions.assertEquals(1, dao.count(all), "The stored entity must be counted");
    Assertions.assertEquals(1, dao.countForUpdate(all),
        "The stored entity must be counted for update");
    Assertions.assertEquals(1, dao.list(all).size(), "The stored entity must be listed");
    Assertions.assertEquals(1, dao.listForUpdate(all).size(),
        "The stored entity must be listed for update");
    Assertions.assertTrue(dao.find(all).isPresent(), "The stored entity must be found");
    Assertions.assertTrue(dao.findForUpdate(all).isPresent(),
        "The stored entity must be found for update");
    OauthClientCursor page = OauthClientCursor.builder().limit(10).build();
    Assertions.assertEquals(1, dao.slide(all, page).all().size(),
        "The stored entity must be on the slide");
    Assertions.assertEquals(1, dao.slideForUpdate(all, page).all().size(),
        "The stored entity must be on the slide for update");
    Assertions.assertTrue(dao.list(all.withUid("two")).isEmpty(),
        "The filter by identifier must be applied");
  }

  /**
   * @autogenerated RepositoryJdbcTestGenerator
   */
  @Test
  @DisplayName("Test the read only repository of oauth client reject the writes")
  void test_oauth_client_jdbc_dao_read_only() {
    InMemoryJdbc jdbc = new InMemoryJdbc();
    OauthClientJdbcDao dao = new OauthClientJdbcDao(jdbc.dataSource(),
        OpenTelemetry.noop().getTracer("test"), true, new SecretDigester("p1:test-pepper", "p1"));
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
    Assertions.assertThrows(RuntimeException.class, () -> dao.create(item),
        "A read only repository can not create");
    Assertions.assertThrows(RuntimeException.class, () -> dao.create(item, value -> true),
        "A read only repository can not create");
    Assertions.assertThrows(RuntimeException.class, () -> dao.update(item),
        "A read only repository can not update");
    Assertions.assertThrows(RuntimeException.class, () -> dao.delete(item),
        "A read only repository can not delete");
    Assertions.assertTrue(jdbc.executed().isEmpty(), "Nothing must be sent to the database");
  }

  /**
   * @autogenerated RepositoryJdbcTestGenerator
   */
  @Test
  @DisplayName("Test the repository of oauth client update the stored entity")
  void test_oauth_client_jdbc_dao_update() {
    InMemoryJdbc jdbc = new InMemoryJdbc();
    OauthClientJdbcDao dao = new OauthClientJdbcDao(jdbc.dataSource(),
        OpenTelemetry.noop().getTracer("test"), false, new SecretDigester("p1:test-pepper", "p1"));
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
    OauthClientFilter all = OauthClientFilter.builder().build();
    dao.create(item);
    OauthClient stored = dao.retrieve(item.getUid(), Optional.empty()).orElseThrow();
    OauthClient updated = dao.update(stored);
    Assertions.assertEquals(item.getUid(), updated.getUid(),
        "The updated entity must keep the identifier");
    Assertions.assertEquals(stored.getVersion().map(v -> v + 1),
        dao.retrieve(item.getUid(), Optional.empty()).orElseThrow().getVersion(),
        "The stored version must be incremented");
    Assertions.assertThrows(OptimistLockException.class, () -> dao.update(stored),
        "An outdated entity can not be updated");
  }
}
