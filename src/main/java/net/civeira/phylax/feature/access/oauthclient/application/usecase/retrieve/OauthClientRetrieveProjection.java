package net.civeira.phylax.feature.access.oauthclient.application.usecase.retrieve;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import net.civeira.phylax.feature.identity.tenant.domain.TenantRef;

/**
 * A dto transfer to hold oauth client attribute values
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OauthClientRetrieveProjection {

  /**
   * Create a dto instance with all the values of an entity
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param visible The entity who provide values.
   * @return
   */
  public static OauthClientRetrieveProjection from(final OauthClientVisibleProjection visible) {
    OauthClientRetrieveProjection instance = new OauthClientRetrieveProjection();
    instance.uidValue = visible.getUid();
    instance.codeValue = visible.getCode();
    instance.tenantValue = visible.getTenant();
    instance.allowAllScopesValue = visible.getAllowAllScopes();
    instance.tokenEndpointAuthMethodValue = visible.getTokenEndpointAuthMethod();
    instance.tokenEndpointAuthSigningAlgValue = visible.getTokenEndpointAuthSigningAlg();
    instance.dpopBoundAccessTokensValue = visible.getDpopBoundAccessTokens();
    instance.idTokenSignedResponseAlgValue = visible.getIdTokenSignedResponseAlg();
    instance.tlsClientAuthSubjectDnValue = visible.getTlsClientAuthSubjectDn();
    instance.tlsClientCertificateBoundAccessTokensValue =
        visible.getTlsClientCertificateBoundAccessTokens();
    instance.authorizationSignedResponseAlgValue = visible.getAuthorizationSignedResponseAlg();
    instance.backchannelTokenDeliveryModeValue = visible.getBackchannelTokenDeliveryMode();
    instance.backchannelClientNotificationEndpointValue =
        visible.getBackchannelClientNotificationEndpoint();
    instance.subjectTypeValue = visible.getSubjectType();
    instance.idTokenEncryptedResponseAlgValue = visible.getIdTokenEncryptedResponseAlg();
    instance.idTokenEncryptedResponseEncValue = visible.getIdTokenEncryptedResponseEnc();
    instance.userinfoEncryptedResponseAlgValue = visible.getUserinfoEncryptedResponseAlg();
    instance.userinfoEncryptedResponseEncValue = visible.getUserinfoEncryptedResponseEnc();
    instance.authorizationDetailsTypesValue = visible.getAuthorizationDetailsTypes();
    instance.userinfoSignedResponseAlgValue = visible.getUserinfoSignedResponseAlg();
    instance.secretOauthValue = visible.getSecretOauth();
    instance.registrationAccessTokenValue = visible.getRegistrationAccessToken();
    instance.defaultAcrValuesValue = visible.getDefaultAcrValues();
    instance.defaultMaxAgeValue = visible.getDefaultMaxAge();
    instance.sectorIdentifierUriValue = visible.getSectorIdentifierUri();
    instance.allowedLoginRedirectsValue = visible.getAllowedLoginRedirects();
    instance.allowedLogoutRedirectsValue = visible.getAllowedLogoutRedirects();
    instance.allowedAudiencesValue = visible.getAllowedAudiences();
    instance.grantTypesJsonValue = visible.getGrantTypesJson();
    instance.introspectionAllowedValue = visible.getIntrospectionAllowed();
    instance.requirePushedAuthorizationRequestsValue =
        visible.getRequirePushedAuthorizationRequests();
    instance.requirePkceValue = visible.getRequirePkce();
    instance.backchannelLogoutUriValue = visible.getBackchannelLogoutUri();
    instance.backchannelLogoutSessionRequiredValue = visible.getBackchannelLogoutSessionRequired();
    instance.frontchannelLogoutUriValue = visible.getFrontchannelLogoutUri();
    instance.frontchannelLogoutSessionRequiredValue =
        visible.getFrontchannelLogoutSessionRequired();
    instance.enabledValue = visible.getEnabled();
    instance.clientNameValue = visible.getClientName();
    instance.logoUriValue = visible.getLogoUri();
    instance.clientUriValue = visible.getClientUri();
    instance.policyUriValue = visible.getPolicyUri();
    instance.tosUriValue = visible.getTosUri();
    instance.responseTypesJsonValue = visible.getResponseTypesJson();
    instance.dynamicallyRegisteredValue = visible.getDynamicallyRegistered();
    instance.registeredAtValue = visible.getRegisteredAt();
    instance.clientCredentialsScopesValue = visible.getClientCredentialsScopes();
    instance.clientCredentialsTokenTtlSecondsValue = visible.getClientCredentialsTokenTtlSeconds();
    instance.requestObjectSigningAlgValue = visible.getRequestObjectSigningAlg();
    instance.jwksUriValue = visible.getJwksUri();
    instance.jwksJsonValue = visible.getJwksJson();
    instance.versionValue = visible.getVersion();
    return instance;
  }

  /**
   * El allow all scopes de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<AllowAllScopesVO> allowAllScopesValue;

  /**
   * El allowed audiences de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<AllowedAudiencesVO> allowedAudiencesValue;

  /**
   * El allowed login redirects de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<AllowedLoginRedirectsVO> allowedLoginRedirectsValue;

  /**
   * El allowed logout redirects de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<AllowedLogoutRedirectsVO> allowedLogoutRedirectsValue;

  /**
   * El authorization details types de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<AuthorizationDetailsTypesVO> authorizationDetailsTypesValue;

  /**
   * El authorization signed response alg de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<AuthorizationSignedResponseAlgVO> authorizationSignedResponseAlgValue;

  /**
   * El backchannel client notification endpoint de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<BackchannelClientNotificationEndpointVO> backchannelClientNotificationEndpointValue;

  /**
   * El backchannel logout session required de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<BackchannelLogoutSessionRequiredVO> backchannelLogoutSessionRequiredValue;

  /**
   * El backchannel logout uri de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<BackchannelLogoutUriVO> backchannelLogoutUriValue;

  /**
   * El backchannel token delivery mode de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<BackchannelTokenDeliveryModeVO> backchannelTokenDeliveryModeValue;

  /**
   * El client credentials scopes de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<ClientCredentialsScopesVO> clientCredentialsScopesValue;

  /**
   * El client credentials token ttl seconds de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<ClientCredentialsTokenTtlSecondsVO> clientCredentialsTokenTtlSecondsValue;

  /**
   * El client name de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<ClientNameVO> clientNameValue;

  /**
   * El client uri de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<ClientUriVO> clientUriValue;

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<CodeVO> codeValue;

  /**
   * El default acr values de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<DefaultAcrValuesVO> defaultAcrValuesValue;

  /**
   * El default max age de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<DefaultMaxAgeVO> defaultMaxAgeValue;

  /**
   * El dpop bound access tokens de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<DpopBoundAccessTokensVO> dpopBoundAccessTokensValue;

  /**
   * El dynamically registered de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<DynamicallyRegisteredVO> dynamicallyRegisteredValue;

  /**
   * The indicator to allow the account be used without deleting it
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<EnabledVO> enabledValue;

  /**
   * El frontchannel logout session required de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<FrontchannelLogoutSessionRequiredVO> frontchannelLogoutSessionRequiredValue;

  /**
   * El frontchannel logout uri de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<FrontchannelLogoutUriVO> frontchannelLogoutUriValue;

  /**
   * El grant types json de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<GrantTypesJsonVO> grantTypesJsonValue;

  /**
   * El id token encrypted response alg de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<IdTokenEncryptedResponseAlgVO> idTokenEncryptedResponseAlgValue;

  /**
   * El id token encrypted response enc de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<IdTokenEncryptedResponseEncVO> idTokenEncryptedResponseEncValue;

  /**
   * El id token signed response alg de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<IdTokenSignedResponseAlgVO> idTokenSignedResponseAlgValue;

  /**
   * If true, this client is allowed to call the token introspection endpoint (POST /introspect) to
   * validate access tokens. Resource servers must authenticate at the introspection endpoint using
   * their client credentials.
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<IntrospectionAllowedVO> introspectionAllowedValue;

  /**
   * El jwks json de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<JwksJsonVO> jwksJsonValue;

  /**
   * El jwks uri de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<JwksUriVO> jwksUriValue;

  /**
   * El logo uri de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<LogoUriVO> logoUriValue;

  /**
   * El policy uri de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<PolicyUriVO> policyUriValue;

  /**
   * El registered at de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<RegisteredAtVO> registeredAtValue;

  /**
   * El registration access token de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<RegistrationAccessTokenVO> registrationAccessTokenValue;

  /**
   * El request object signing alg de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<RequestObjectSigningAlgVO> requestObjectSigningAlgValue;

  /**
   * If true, every authorization code request from this client must include code_challenge and
   * code_challenge_method=S256. For clients with public-allow=true, PKCE is always required
   * regardless of this flag.
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<RequirePkceVO> requirePkceValue;

  /**
   * El require pushed authorization requests de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<RequirePushedAuthorizationRequestsVO> requirePushedAuthorizationRequestsValue;

  /**
   * El response types json de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<ResponseTypesJsonVO> responseTypesJsonValue;

  /**
   * El secret oauth de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<SecretOauthVO> secretOauthValue;

  /**
   * El sector identifier uri de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<SectorIdentifierUriVO> sectorIdentifierUriValue;

  /**
   * El subject type de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<SubjectTypeVO> subjectTypeValue;

  /**
   * Owner tenant. Null means a global client, manageable only by root.
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<TenantVO> tenantValue;

  /**
   * El tls client auth subject dn de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<TlsClientAuthSubjectDnVO> tlsClientAuthSubjectDnValue;

  /**
   * El tls client certificate bound access tokens de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<TlsClientCertificateBoundAccessTokensVO> tlsClientCertificateBoundAccessTokensValue;

  /**
   * El token endpoint auth method de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<TokenEndpointAuthMethodVO> tokenEndpointAuthMethodValue;

  /**
   * El token endpoint auth signing alg de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<TokenEndpointAuthSigningAlgVO> tokenEndpointAuthSigningAlgValue;

  /**
   * El tos uri de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<TosUriVO> tosUriValue;

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<UidVO> uidValue;

  /**
   * El userinfo encrypted response alg de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<UserinfoEncryptedResponseAlgVO> userinfoEncryptedResponseAlgValue;

  /**
   * El userinfo encrypted response enc de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<UserinfoEncryptedResponseEncVO> userinfoEncryptedResponseEncValue;

  /**
   * El userinfo signed response alg de oauth client
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<UserinfoSignedResponseAlgVO> userinfoSignedResponseAlgValue;

  /**
   * Campo con el número de version de oauth client para controlar bloqueos optimistas
   *
   * @autogenerated RetrieveProjectionGenerator
   */
  private Optional<VersionVO> versionValue;

  /**
   * Create a dto instance with all the values of an entity
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param entity The entity who provide values.
   */
  public OauthClientRetrieveProjection(final OauthClient entity) {
    uidValue = Optional.of(entity.getUidValue());
    codeValue = Optional.of(entity.getCodeValue());
    tenantValue = Optional.of(entity.getTenantValue());
    allowAllScopesValue = Optional.of(entity.getAllowAllScopesValue());
    tokenEndpointAuthMethodValue = Optional.of(entity.getTokenEndpointAuthMethodValue());
    tokenEndpointAuthSigningAlgValue = Optional.of(entity.getTokenEndpointAuthSigningAlgValue());
    dpopBoundAccessTokensValue = Optional.of(entity.getDpopBoundAccessTokensValue());
    idTokenSignedResponseAlgValue = Optional.of(entity.getIdTokenSignedResponseAlgValue());
    tlsClientAuthSubjectDnValue = Optional.of(entity.getTlsClientAuthSubjectDnValue());
    tlsClientCertificateBoundAccessTokensValue =
        Optional.of(entity.getTlsClientCertificateBoundAccessTokensValue());
    authorizationSignedResponseAlgValue =
        Optional.of(entity.getAuthorizationSignedResponseAlgValue());
    backchannelTokenDeliveryModeValue = Optional.of(entity.getBackchannelTokenDeliveryModeValue());
    backchannelClientNotificationEndpointValue =
        Optional.of(entity.getBackchannelClientNotificationEndpointValue());
    subjectTypeValue = Optional.of(entity.getSubjectTypeValue());
    idTokenEncryptedResponseAlgValue = Optional.of(entity.getIdTokenEncryptedResponseAlgValue());
    idTokenEncryptedResponseEncValue = Optional.of(entity.getIdTokenEncryptedResponseEncValue());
    userinfoEncryptedResponseAlgValue = Optional.of(entity.getUserinfoEncryptedResponseAlgValue());
    userinfoEncryptedResponseEncValue = Optional.of(entity.getUserinfoEncryptedResponseEncValue());
    authorizationDetailsTypesValue = Optional.of(entity.getAuthorizationDetailsTypesValue());
    userinfoSignedResponseAlgValue = Optional.of(entity.getUserinfoSignedResponseAlgValue());
    secretOauthValue = Optional.of(entity.getSecretOauthValue());
    registrationAccessTokenValue = Optional.of(entity.getRegistrationAccessTokenValue());
    defaultAcrValuesValue = Optional.of(entity.getDefaultAcrValuesValue());
    defaultMaxAgeValue = Optional.of(entity.getDefaultMaxAgeValue());
    sectorIdentifierUriValue = Optional.of(entity.getSectorIdentifierUriValue());
    allowedLoginRedirectsValue = Optional.of(entity.getAllowedLoginRedirectsValue());
    allowedLogoutRedirectsValue = Optional.of(entity.getAllowedLogoutRedirectsValue());
    allowedAudiencesValue = Optional.of(entity.getAllowedAudiencesValue());
    grantTypesJsonValue = Optional.of(entity.getGrantTypesJsonValue());
    introspectionAllowedValue = Optional.of(entity.getIntrospectionAllowedValue());
    requirePushedAuthorizationRequestsValue =
        Optional.of(entity.getRequirePushedAuthorizationRequestsValue());
    requirePkceValue = Optional.of(entity.getRequirePkceValue());
    backchannelLogoutUriValue = Optional.of(entity.getBackchannelLogoutUriValue());
    backchannelLogoutSessionRequiredValue =
        Optional.of(entity.getBackchannelLogoutSessionRequiredValue());
    frontchannelLogoutUriValue = Optional.of(entity.getFrontchannelLogoutUriValue());
    frontchannelLogoutSessionRequiredValue =
        Optional.of(entity.getFrontchannelLogoutSessionRequiredValue());
    enabledValue = Optional.of(entity.getEnabledValue());
    clientNameValue = Optional.of(entity.getClientNameValue());
    logoUriValue = Optional.of(entity.getLogoUriValue());
    clientUriValue = Optional.of(entity.getClientUriValue());
    policyUriValue = Optional.of(entity.getPolicyUriValue());
    tosUriValue = Optional.of(entity.getTosUriValue());
    responseTypesJsonValue = Optional.of(entity.getResponseTypesJsonValue());
    dynamicallyRegisteredValue = Optional.of(entity.getDynamicallyRegisteredValue());
    registeredAtValue = Optional.of(entity.getRegisteredAtValue());
    clientCredentialsScopesValue = Optional.of(entity.getClientCredentialsScopesValue());
    clientCredentialsTokenTtlSecondsValue =
        Optional.of(entity.getClientCredentialsTokenTtlSecondsValue());
    requestObjectSigningAlgValue = Optional.of(entity.getRequestObjectSigningAlgValue());
    jwksUriValue = Optional.of(entity.getJwksUriValue());
    jwksJsonValue = Optional.of(entity.getJwksJsonValue());
    versionValue = Optional.of(entity.getVersionValue());
  }

  /**
   * Inform for a possible change propolsal in AllowAllScopes
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for AllowAllScopes, otherwise the value for
   *         AllowAllScopes
   */
  public Boolean getAllowAllScopes() {
    return getAllowAllScopesOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in AllowAllScopes
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for AllowAllScopes, otherwise the value for
   *         AllowAllScopes
   */
  public Boolean getAllowAllScopesOrDefault(final Boolean orDefault) {
    return allowAllScopesValue.map(AllowAllScopesVO::isAllowAllScopes).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in AllowedAudiences
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for AllowedAudiences, otherwise the value for
   *         AllowedAudiences
   */
  public List<AllowedAudiences> getAllowedAudiences() {
    return allowedAudiencesValue.map(AllowedAudiencesVO::getAllowedAudiences).orElseGet(List::of);
  }

  /**
   * Inform for a possible change propolsal in AllowedLoginRedirects
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for AllowedLoginRedirects, otherwise the value for
   *         AllowedLoginRedirects
   */
  public List<AllowedLoginRedirects> getAllowedLoginRedirects() {
    return allowedLoginRedirectsValue.map(AllowedLoginRedirectsVO::getAllowedLoginRedirects)
        .orElseGet(List::of);
  }

  /**
   * Inform for a possible change propolsal in AllowedLogoutRedirects
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for AllowedLogoutRedirects, otherwise the value
   *         for AllowedLogoutRedirects
   */
  public List<AllowedLogoutRedirects> getAllowedLogoutRedirects() {
    return allowedLogoutRedirectsValue.map(AllowedLogoutRedirectsVO::getAllowedLogoutRedirects)
        .orElseGet(List::of);
  }

  /**
   * Inform for a possible change propolsal in AuthorizationDetailsTypes
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for AuthorizationDetailsTypes, otherwise the value
   *         for AuthorizationDetailsTypes
   */
  public String getAuthorizationDetailsTypes() {
    return getAuthorizationDetailsTypesOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in AuthorizationDetailsTypes
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for AuthorizationDetailsTypes, otherwise the value
   *         for AuthorizationDetailsTypes
   */
  public String getAuthorizationDetailsTypesOrDefault(final String orDefault) {
    return authorizationDetailsTypesValue
        .flatMap(AuthorizationDetailsTypesVO::getAuthorizationDetailsTypes).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in AuthorizationSignedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for AuthorizationSignedResponseAlg, otherwise the
   *         value for AuthorizationSignedResponseAlg
   */
  public String getAuthorizationSignedResponseAlg() {
    return getAuthorizationSignedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in AuthorizationSignedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for AuthorizationSignedResponseAlg, otherwise the
   *         value for AuthorizationSignedResponseAlg
   */
  public String getAuthorizationSignedResponseAlgOrDefault(final String orDefault) {
    return authorizationSignedResponseAlgValue
        .flatMap(AuthorizationSignedResponseAlgVO::getAuthorizationSignedResponseAlg)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in BackchannelClientNotificationEndpoint
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for BackchannelClientNotificationEndpoint,
   *         otherwise the value for BackchannelClientNotificationEndpoint
   */
  public String getBackchannelClientNotificationEndpoint() {
    return getBackchannelClientNotificationEndpointOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in BackchannelClientNotificationEndpoint
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for BackchannelClientNotificationEndpoint,
   *         otherwise the value for BackchannelClientNotificationEndpoint
   */
  public String getBackchannelClientNotificationEndpointOrDefault(final String orDefault) {
    return backchannelClientNotificationEndpointValue
        .flatMap(BackchannelClientNotificationEndpointVO::getBackchannelClientNotificationEndpoint)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in BackchannelLogoutSessionRequired
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for BackchannelLogoutSessionRequired, otherwise
   *         the value for BackchannelLogoutSessionRequired
   */
  public Boolean getBackchannelLogoutSessionRequired() {
    return getBackchannelLogoutSessionRequiredOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in BackchannelLogoutSessionRequired
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for BackchannelLogoutSessionRequired, otherwise
   *         the value for BackchannelLogoutSessionRequired
   */
  public Boolean getBackchannelLogoutSessionRequiredOrDefault(final Boolean orDefault) {
    return backchannelLogoutSessionRequiredValue
        .map(BackchannelLogoutSessionRequiredVO::isBackchannelLogoutSessionRequired)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in BackchannelLogoutUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for BackchannelLogoutUri, otherwise the value for
   *         BackchannelLogoutUri
   */
  public String getBackchannelLogoutUri() {
    return getBackchannelLogoutUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in BackchannelLogoutUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for BackchannelLogoutUri, otherwise the value for
   *         BackchannelLogoutUri
   */
  public String getBackchannelLogoutUriOrDefault(final String orDefault) {
    return backchannelLogoutUriValue.flatMap(BackchannelLogoutUriVO::getBackchannelLogoutUri)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in BackchannelTokenDeliveryMode
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for BackchannelTokenDeliveryMode, otherwise the
   *         value for BackchannelTokenDeliveryMode
   */
  public OauthClientBackchannelTokenDeliveryModeOptions getBackchannelTokenDeliveryMode() {
    return getBackchannelTokenDeliveryModeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in BackchannelTokenDeliveryMode
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for BackchannelTokenDeliveryMode, otherwise the
   *         value for BackchannelTokenDeliveryMode
   */
  public OauthClientBackchannelTokenDeliveryModeOptions getBackchannelTokenDeliveryModeOrDefault(
      final OauthClientBackchannelTokenDeliveryModeOptions orDefault) {
    return backchannelTokenDeliveryModeValue
        .flatMap(BackchannelTokenDeliveryModeVO::getBackchannelTokenDeliveryMode).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in ClientCredentialsScopes
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for ClientCredentialsScopes, otherwise the value
   *         for ClientCredentialsScopes
   */
  public String getClientCredentialsScopes() {
    return getClientCredentialsScopesOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ClientCredentialsScopes
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for ClientCredentialsScopes, otherwise the value
   *         for ClientCredentialsScopes
   */
  public String getClientCredentialsScopesOrDefault(final String orDefault) {
    return clientCredentialsScopesValue
        .flatMap(ClientCredentialsScopesVO::getClientCredentialsScopes).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in ClientCredentialsTokenTtlSeconds
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for ClientCredentialsTokenTtlSeconds, otherwise
   *         the value for ClientCredentialsTokenTtlSeconds
   */
  public Integer getClientCredentialsTokenTtlSeconds() {
    return getClientCredentialsTokenTtlSecondsOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ClientCredentialsTokenTtlSeconds
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for ClientCredentialsTokenTtlSeconds, otherwise
   *         the value for ClientCredentialsTokenTtlSeconds
   */
  public Integer getClientCredentialsTokenTtlSecondsOrDefault(final Integer orDefault) {
    return clientCredentialsTokenTtlSecondsValue
        .map(ClientCredentialsTokenTtlSecondsVO::getClientCredentialsTokenTtlSeconds)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in ClientName
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for ClientName, otherwise the value for ClientName
   */
  public String getClientName() {
    return getClientNameOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ClientName
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for ClientName, otherwise the value for ClientName
   */
  public String getClientNameOrDefault(final String orDefault) {
    return clientNameValue.flatMap(ClientNameVO::getClientName).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in ClientUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for ClientUri, otherwise the value for ClientUri
   */
  public String getClientUri() {
    return getClientUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ClientUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for ClientUri, otherwise the value for ClientUri
   */
  public String getClientUriOrDefault(final String orDefault) {
    return clientUriValue.flatMap(ClientUriVO::getClientUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public String getCode() {
    return getCodeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public String getCodeOrDefault(final String orDefault) {
    return codeValue.map(CodeVO::getCode).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in DefaultAcrValues
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for DefaultAcrValues, otherwise the value for
   *         DefaultAcrValues
   */
  public String getDefaultAcrValues() {
    return getDefaultAcrValuesOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in DefaultAcrValues
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for DefaultAcrValues, otherwise the value for
   *         DefaultAcrValues
   */
  public String getDefaultAcrValuesOrDefault(final String orDefault) {
    return defaultAcrValuesValue.flatMap(DefaultAcrValuesVO::getDefaultAcrValues).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in DefaultMaxAge
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for DefaultMaxAge, otherwise the value for
   *         DefaultMaxAge
   */
  public Integer getDefaultMaxAge() {
    return getDefaultMaxAgeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in DefaultMaxAge
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for DefaultMaxAge, otherwise the value for
   *         DefaultMaxAge
   */
  public Integer getDefaultMaxAgeOrDefault(final Integer orDefault) {
    return defaultMaxAgeValue.flatMap(DefaultMaxAgeVO::getDefaultMaxAge).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in DpopBoundAccessTokens
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for DpopBoundAccessTokens, otherwise the value for
   *         DpopBoundAccessTokens
   */
  public Boolean getDpopBoundAccessTokens() {
    return getDpopBoundAccessTokensOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in DpopBoundAccessTokens
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for DpopBoundAccessTokens, otherwise the value for
   *         DpopBoundAccessTokens
   */
  public Boolean getDpopBoundAccessTokensOrDefault(final Boolean orDefault) {
    return dpopBoundAccessTokensValue.map(DpopBoundAccessTokensVO::isDpopBoundAccessTokens)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in DynamicallyRegistered
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for DynamicallyRegistered, otherwise the value for
   *         DynamicallyRegistered
   */
  public Boolean getDynamicallyRegistered() {
    return getDynamicallyRegisteredOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in DynamicallyRegistered
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for DynamicallyRegistered, otherwise the value for
   *         DynamicallyRegistered
   */
  public Boolean getDynamicallyRegisteredOrDefault(final Boolean orDefault) {
    return dynamicallyRegisteredValue.map(DynamicallyRegisteredVO::isDynamicallyRegistered)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Boolean getEnabled() {
    return getEnabledOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Boolean getEnabledOrDefault(final Boolean orDefault) {
    return enabledValue.map(EnabledVO::isEnabled).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in FrontchannelLogoutSessionRequired
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for FrontchannelLogoutSessionRequired, otherwise
   *         the value for FrontchannelLogoutSessionRequired
   */
  public Boolean getFrontchannelLogoutSessionRequired() {
    return getFrontchannelLogoutSessionRequiredOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in FrontchannelLogoutSessionRequired
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for FrontchannelLogoutSessionRequired, otherwise
   *         the value for FrontchannelLogoutSessionRequired
   */
  public Boolean getFrontchannelLogoutSessionRequiredOrDefault(final Boolean orDefault) {
    return frontchannelLogoutSessionRequiredValue
        .map(FrontchannelLogoutSessionRequiredVO::isFrontchannelLogoutSessionRequired)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in FrontchannelLogoutUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for FrontchannelLogoutUri, otherwise the value for
   *         FrontchannelLogoutUri
   */
  public String getFrontchannelLogoutUri() {
    return getFrontchannelLogoutUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in FrontchannelLogoutUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for FrontchannelLogoutUri, otherwise the value for
   *         FrontchannelLogoutUri
   */
  public String getFrontchannelLogoutUriOrDefault(final String orDefault) {
    return frontchannelLogoutUriValue.flatMap(FrontchannelLogoutUriVO::getFrontchannelLogoutUri)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in GrantTypesJson
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for GrantTypesJson, otherwise the value for
   *         GrantTypesJson
   */
  public String getGrantTypesJson() {
    return getGrantTypesJsonOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in GrantTypesJson
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for GrantTypesJson, otherwise the value for
   *         GrantTypesJson
   */
  public String getGrantTypesJsonOrDefault(final String orDefault) {
    return grantTypesJsonValue.flatMap(GrantTypesJsonVO::getGrantTypesJson).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseAlg, otherwise the
   *         value for IdTokenEncryptedResponseAlg
   */
  public String getIdTokenEncryptedResponseAlg() {
    return getIdTokenEncryptedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for IdTokenEncryptedResponseAlg, otherwise the
   *         value for IdTokenEncryptedResponseAlg
   */
  public String getIdTokenEncryptedResponseAlgOrDefault(final String orDefault) {
    return idTokenEncryptedResponseAlgValue
        .flatMap(IdTokenEncryptedResponseAlgVO::getIdTokenEncryptedResponseAlg).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseEnc
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseEnc, otherwise the
   *         value for IdTokenEncryptedResponseEnc
   */
  public String getIdTokenEncryptedResponseEnc() {
    return getIdTokenEncryptedResponseEncOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseEnc
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for IdTokenEncryptedResponseEnc, otherwise the
   *         value for IdTokenEncryptedResponseEnc
   */
  public String getIdTokenEncryptedResponseEncOrDefault(final String orDefault) {
    return idTokenEncryptedResponseEncValue
        .flatMap(IdTokenEncryptedResponseEncVO::getIdTokenEncryptedResponseEnc).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in IdTokenSignedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for IdTokenSignedResponseAlg, otherwise the value
   *         for IdTokenSignedResponseAlg
   */
  public OauthClientIdTokenSignedResponseAlgOptions getIdTokenSignedResponseAlg() {
    return getIdTokenSignedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in IdTokenSignedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for IdTokenSignedResponseAlg, otherwise the value
   *         for IdTokenSignedResponseAlg
   */
  public OauthClientIdTokenSignedResponseAlgOptions getIdTokenSignedResponseAlgOrDefault(
      final OauthClientIdTokenSignedResponseAlgOptions orDefault) {
    return idTokenSignedResponseAlgValue
        .flatMap(IdTokenSignedResponseAlgVO::getIdTokenSignedResponseAlg).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in IntrospectionAllowed
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for IntrospectionAllowed, otherwise the value for
   *         IntrospectionAllowed
   */
  public Boolean getIntrospectionAllowed() {
    return getIntrospectionAllowedOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in IntrospectionAllowed
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for IntrospectionAllowed, otherwise the value for
   *         IntrospectionAllowed
   */
  public Boolean getIntrospectionAllowedOrDefault(final Boolean orDefault) {
    return introspectionAllowedValue.map(IntrospectionAllowedVO::isIntrospectionAllowed)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in JwksJson
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for JwksJson, otherwise the value for JwksJson
   */
  public String getJwksJson() {
    return getJwksJsonOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in JwksJson
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for JwksJson, otherwise the value for JwksJson
   */
  public String getJwksJsonOrDefault(final String orDefault) {
    return jwksJsonValue.flatMap(JwksJsonVO::getJwksJson).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in JwksUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for JwksUri, otherwise the value for JwksUri
   */
  public String getJwksUri() {
    return getJwksUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in JwksUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for JwksUri, otherwise the value for JwksUri
   */
  public String getJwksUriOrDefault(final String orDefault) {
    return jwksUriValue.flatMap(JwksUriVO::getJwksUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in LogoUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for LogoUri, otherwise the value for LogoUri
   */
  public String getLogoUri() {
    return getLogoUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in LogoUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for LogoUri, otherwise the value for LogoUri
   */
  public String getLogoUriOrDefault(final String orDefault) {
    return logoUriValue.flatMap(LogoUriVO::getLogoUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in PolicyUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for PolicyUri, otherwise the value for PolicyUri
   */
  public String getPolicyUri() {
    return getPolicyUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in PolicyUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for PolicyUri, otherwise the value for PolicyUri
   */
  public String getPolicyUriOrDefault(final String orDefault) {
    return policyUriValue.flatMap(PolicyUriVO::getPolicyUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in RegisteredAt
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for RegisteredAt, otherwise the value for
   *         RegisteredAt
   */
  public OffsetDateTime getRegisteredAt() {
    return getRegisteredAtOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in RegisteredAt
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for RegisteredAt, otherwise the value for
   *         RegisteredAt
   */
  public OffsetDateTime getRegisteredAtOrDefault(final OffsetDateTime orDefault) {
    return registeredAtValue.flatMap(RegisteredAtVO::getRegisteredAt).orElse(orDefault);
  }

  /**
   * Masked protected value
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return
   */
  public String getRegistrationAccessToken() {
    return registrationAccessTokenValue.isPresent() ? "****" : null;
  }

  /**
   * Inform for a possible change propolsal in RequestObjectSigningAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for RequestObjectSigningAlg, otherwise the value
   *         for RequestObjectSigningAlg
   */
  public String getRequestObjectSigningAlg() {
    return getRequestObjectSigningAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in RequestObjectSigningAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for RequestObjectSigningAlg, otherwise the value
   *         for RequestObjectSigningAlg
   */
  public String getRequestObjectSigningAlgOrDefault(final String orDefault) {
    return requestObjectSigningAlgValue
        .flatMap(RequestObjectSigningAlgVO::getRequestObjectSigningAlg).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in RequirePkce
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for RequirePkce, otherwise the value for
   *         RequirePkce
   */
  public Boolean getRequirePkce() {
    return getRequirePkceOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in RequirePkce
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for RequirePkce, otherwise the value for
   *         RequirePkce
   */
  public Boolean getRequirePkceOrDefault(final Boolean orDefault) {
    return requirePkceValue.map(RequirePkceVO::isRequirePkce).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in RequirePushedAuthorizationRequests
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for RequirePushedAuthorizationRequests, otherwise
   *         the value for RequirePushedAuthorizationRequests
   */
  public Boolean getRequirePushedAuthorizationRequests() {
    return getRequirePushedAuthorizationRequestsOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in RequirePushedAuthorizationRequests
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for RequirePushedAuthorizationRequests, otherwise
   *         the value for RequirePushedAuthorizationRequests
   */
  public Boolean getRequirePushedAuthorizationRequestsOrDefault(final Boolean orDefault) {
    return requirePushedAuthorizationRequestsValue
        .map(RequirePushedAuthorizationRequestsVO::isRequirePushedAuthorizationRequests)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in ResponseTypesJson
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for ResponseTypesJson, otherwise the value for
   *         ResponseTypesJson
   */
  public String getResponseTypesJson() {
    return getResponseTypesJsonOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ResponseTypesJson
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for ResponseTypesJson, otherwise the value for
   *         ResponseTypesJson
   */
  public String getResponseTypesJsonOrDefault(final String orDefault) {
    return responseTypesJsonValue.flatMap(ResponseTypesJsonVO::getResponseTypesJson)
        .orElse(orDefault);
  }

  /**
   * Masked protected value
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return
   */
  public String getSecretOauth() {
    return secretOauthValue.isPresent() ? "****" : null;
  }

  /**
   * Inform for a possible change propolsal in SectorIdentifierUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for SectorIdentifierUri, otherwise the value for
   *         SectorIdentifierUri
   */
  public String getSectorIdentifierUri() {
    return getSectorIdentifierUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in SectorIdentifierUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for SectorIdentifierUri, otherwise the value for
   *         SectorIdentifierUri
   */
  public String getSectorIdentifierUriOrDefault(final String orDefault) {
    return sectorIdentifierUriValue.flatMap(SectorIdentifierUriVO::getSectorIdentifierUri)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in SubjectType
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for SubjectType, otherwise the value for
   *         SubjectType
   */
  public OauthClientSubjectTypeOptions getSubjectType() {
    return getSubjectTypeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in SubjectType
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for SubjectType, otherwise the value for
   *         SubjectType
   */
  public OauthClientSubjectTypeOptions getSubjectTypeOrDefault(
      final OauthClientSubjectTypeOptions orDefault) {
    return subjectTypeValue.map(SubjectTypeVO::getSubjectType).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public TenantRef getTenant() {
    return tenantValue.flatMap(TenantVO::getTenant).orElse(null);
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public String getTenantReference() {
    return getTenantReferenceOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public String getTenantReferenceOrDefault(final String orDefault) {
    return tenantValue.flatMap(TenantVO::getTenant).map(TenantRef::getUid).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in TlsClientAuthSubjectDn
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for TlsClientAuthSubjectDn, otherwise the value
   *         for TlsClientAuthSubjectDn
   */
  public String getTlsClientAuthSubjectDn() {
    return getTlsClientAuthSubjectDnOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TlsClientAuthSubjectDn
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for TlsClientAuthSubjectDn, otherwise the value
   *         for TlsClientAuthSubjectDn
   */
  public String getTlsClientAuthSubjectDnOrDefault(final String orDefault) {
    return tlsClientAuthSubjectDnValue.flatMap(TlsClientAuthSubjectDnVO::getTlsClientAuthSubjectDn)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in TlsClientCertificateBoundAccessTokens
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for TlsClientCertificateBoundAccessTokens,
   *         otherwise the value for TlsClientCertificateBoundAccessTokens
   */
  public Boolean getTlsClientCertificateBoundAccessTokens() {
    return getTlsClientCertificateBoundAccessTokensOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TlsClientCertificateBoundAccessTokens
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for TlsClientCertificateBoundAccessTokens,
   *         otherwise the value for TlsClientCertificateBoundAccessTokens
   */
  public Boolean getTlsClientCertificateBoundAccessTokensOrDefault(final Boolean orDefault) {
    return tlsClientCertificateBoundAccessTokensValue
        .map(TlsClientCertificateBoundAccessTokensVO::isTlsClientCertificateBoundAccessTokens)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthMethod
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthMethod, otherwise the value
   *         for TokenEndpointAuthMethod
   */
  public OauthClientTokenEndpointAuthMethodOptions getTokenEndpointAuthMethod() {
    return getTokenEndpointAuthMethodOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthMethod
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for TokenEndpointAuthMethod, otherwise the value
   *         for TokenEndpointAuthMethod
   */
  public OauthClientTokenEndpointAuthMethodOptions getTokenEndpointAuthMethodOrDefault(
      final OauthClientTokenEndpointAuthMethodOptions orDefault) {
    return tokenEndpointAuthMethodValue.map(TokenEndpointAuthMethodVO::getTokenEndpointAuthMethod)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthSigningAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthSigningAlg, otherwise the
   *         value for TokenEndpointAuthSigningAlg
   */
  public String getTokenEndpointAuthSigningAlg() {
    return getTokenEndpointAuthSigningAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthSigningAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for TokenEndpointAuthSigningAlg, otherwise the
   *         value for TokenEndpointAuthSigningAlg
   */
  public String getTokenEndpointAuthSigningAlgOrDefault(final String orDefault) {
    return tokenEndpointAuthSigningAlgValue
        .flatMap(TokenEndpointAuthSigningAlgVO::getTokenEndpointAuthSigningAlg).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in TosUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for TosUri, otherwise the value for TosUri
   */
  public String getTosUri() {
    return getTosUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TosUri
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for TosUri, otherwise the value for TosUri
   */
  public String getTosUriOrDefault(final String orDefault) {
    return tosUriValue.flatMap(TosUriVO::getTosUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public String getUid() {
    return getUidOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public String getUidOrDefault(final String orDefault) {
    return uidValue.map(UidVO::getUid).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseAlg, otherwise the
   *         value for UserinfoEncryptedResponseAlg
   */
  public String getUserinfoEncryptedResponseAlg() {
    return getUserinfoEncryptedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for UserinfoEncryptedResponseAlg, otherwise the
   *         value for UserinfoEncryptedResponseAlg
   */
  public String getUserinfoEncryptedResponseAlgOrDefault(final String orDefault) {
    return userinfoEncryptedResponseAlgValue
        .flatMap(UserinfoEncryptedResponseAlgVO::getUserinfoEncryptedResponseAlg).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseEnc
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseEnc, otherwise the
   *         value for UserinfoEncryptedResponseEnc
   */
  public String getUserinfoEncryptedResponseEnc() {
    return getUserinfoEncryptedResponseEncOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseEnc
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for UserinfoEncryptedResponseEnc, otherwise the
   *         value for UserinfoEncryptedResponseEnc
   */
  public String getUserinfoEncryptedResponseEncOrDefault(final String orDefault) {
    return userinfoEncryptedResponseEncValue
        .flatMap(UserinfoEncryptedResponseEncVO::getUserinfoEncryptedResponseEnc).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in UserinfoSignedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for UserinfoSignedResponseAlg, otherwise the value
   *         for UserinfoSignedResponseAlg
   */
  public String getUserinfoSignedResponseAlg() {
    return getUserinfoSignedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in UserinfoSignedResponseAlg
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for UserinfoSignedResponseAlg, otherwise the value
   *         for UserinfoSignedResponseAlg
   */
  public String getUserinfoSignedResponseAlgOrDefault(final String orDefault) {
    return userinfoSignedResponseAlgValue
        .flatMap(UserinfoSignedResponseAlgVO::getUserinfoSignedResponseAlg).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated RetrieveProjectionGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Integer getVersion() {
    return getVersionOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated RetrieveProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Integer getVersionOrDefault(final Integer orDefault) {
    return versionValue.flatMap(VersionVO::getVersion).orElse(orDefault);
  }
}
