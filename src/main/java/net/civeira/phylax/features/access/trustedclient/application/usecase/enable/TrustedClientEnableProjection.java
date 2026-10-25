package net.civeira.phylax.features.access.trustedclient.application.usecase.enable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;
import net.civeira.phylax.features.access.trustedclient.application.visibility.TrustedClientVisibleProjection;
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

/**
 * A dto transfer to hold trusted client attribute values
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TrustedClientEnableProjection {

  /**
   * Create a dto instance with all the values of an entity
   *
   * @autogenerated ActionProjectionGenerator
   * @param visible The entity who provide values.
   * @return
   */
  public static TrustedClientEnableProjection from(final TrustedClientVisibleProjection visible) {
    TrustedClientEnableProjection instance = new TrustedClientEnableProjection();
    instance.uidValue = visible.getUid();
    instance.codeValue = visible.getCode();
    instance.tenantValue = visible.getTenant();
    instance.allowAllScopesValue = visible.getAllowAllScopes();
    instance.publicAllowValue = visible.getPublicAllow();
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
    instance.registrationAccessValue = visible.getRegistrationAccess();
    instance.defaultAcrValuesValue = visible.getDefaultAcrValues();
    instance.defaultMaxAgeValue = visible.getDefaultMaxAge();
    instance.sectorIdentifierUriValue = visible.getSectorIdentifierUri();
    instance.allowedRedirectsValue = visible.getAllowedRedirects();
    instance.allowedLogoutRedirectsValue = visible.getAllowedLogoutRedirects();
    instance.allowedAudiencesValue = visible.getAllowedAudiences();
    instance.grantTypesJsonValue = visible.getGrantTypesJson();
    instance.isResourceServerValue = visible.getIsResourceServer();
    instance.requirePushedAuthorizationRequestsValue =
        visible.getRequirePushedAuthorizationRequests();
    instance.requirePkceValue = visible.getRequirePkce();
    instance.backChannelLogoutUriValue = visible.getBackChannelLogoutUri();
    instance.backChannelLogoutSessionRequiredValue = visible.getBackChannelLogoutSessionRequired();
    instance.frontChannelLogoutUriValue = visible.getFrontChannelLogoutUri();
    instance.frontChannelLogoutSessionRequiredValue =
        visible.getFrontChannelLogoutSessionRequired();
    instance.enabledValue = visible.getEnabled();
    instance.clientNameValue = visible.getClientName();
    instance.logoUriValue = visible.getLogoUri();
    instance.clientUriValue = visible.getClientUri();
    instance.policyUriValue = visible.getPolicyUri();
    instance.tosUriValue = visible.getTosUri();
    instance.responseTypesJsonValue = visible.getResponseTypesJson();
    instance.dynamicallyRegisteredValue = visible.getDynamicallyRegistered();
    instance.registeredAtValue = visible.getRegisteredAt();
    instance.allowedScopesM2mValue = visible.getAllowedScopesM2m();
    instance.m2mTokenTtlSecondsValue = visible.getM2mTokenTtlSeconds();
    instance.requestObjectSigningAlgValue = visible.getRequestObjectSigningAlg();
    instance.jwksUriValue = visible.getJwksUri();
    instance.jwksJsonValue = visible.getJwksJson();
    instance.versionValue = visible.getVersion();
    return instance;
  }

  /**
   * El allow all scopes de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<AllowAllScopesVO> allowAllScopesValue;

  /**
   * El allowed audiences de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<AllowedAudiencesVO> allowedAudiencesValue;

  /**
   * El allowed logout redirects de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<AllowedLogoutRedirectsVO> allowedLogoutRedirectsValue;

  /**
   * El allowed redirects de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<AllowedRedirectsVO> allowedRedirectsValue;

  /**
   * El allowed scopes m 2m de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<AllowedScopesM2mVO> allowedScopesM2mValue;

  /**
   * El authorization details types de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<AuthorizationDetailsTypesVO> authorizationDetailsTypesValue;

  /**
   * El authorization signed response alg de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<AuthorizationSignedResponseAlgVO> authorizationSignedResponseAlgValue;

  /**
   * El back channel logout session required de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<BackChannelLogoutSessionRequiredVO> backChannelLogoutSessionRequiredValue;

  /**
   * El back channel logout uri de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<BackChannelLogoutUriVO> backChannelLogoutUriValue;

  /**
   * El backchannel client notification endpoint de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<BackchannelClientNotificationEndpointVO> backchannelClientNotificationEndpointValue;

  /**
   * El backchannel token delivery mode de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<BackchannelTokenDeliveryModeVO> backchannelTokenDeliveryModeValue;

  /**
   * El client name de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<ClientNameVO> clientNameValue;

  /**
   * El client uri de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<ClientUriVO> clientUriValue;

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<CodeVO> codeValue;

  /**
   * El default acr values de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<DefaultAcrValuesVO> defaultAcrValuesValue;

  /**
   * El default max age de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<DefaultMaxAgeVO> defaultMaxAgeValue;

  /**
   * El dpop bound access tokens de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<DpopBoundAccessTokensVO> dpopBoundAccessTokensValue;

  /**
   * El dynamically registered de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<DynamicallyRegisteredVO> dynamicallyRegisteredValue;

  /**
   * The indicator to allow the account be used without deleting it
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<EnabledVO> enabledValue;

  /**
   * El front channel logout session required de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<FrontChannelLogoutSessionRequiredVO> frontChannelLogoutSessionRequiredValue;

  /**
   * El front channel logout uri de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<FrontChannelLogoutUriVO> frontChannelLogoutUriValue;

  /**
   * El grant types json de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<GrantTypesJsonVO> grantTypesJsonValue;

  /**
   * El id token encrypted response alg de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<IdTokenEncryptedResponseAlgVO> idTokenEncryptedResponseAlgValue;

  /**
   * El id token encrypted response enc de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<IdTokenEncryptedResponseEncVO> idTokenEncryptedResponseEncValue;

  /**
   * El id token signed response alg de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<IdTokenSignedResponseAlgVO> idTokenSignedResponseAlgValue;

  /**
   * If true, this client is allowed to call the token introspection endpoint (POST /introspect) to
   * validate access tokens. Resource servers must authenticate at the introspection endpoint using
   * their client credentials.
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<IsResourceServerVO> isResourceServerValue;

  /**
   * El jwks json de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<JwksJsonVO> jwksJsonValue;

  /**
   * El jwks uri de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<JwksUriVO> jwksUriValue;

  /**
   * El logo uri de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<LogoUriVO> logoUriValue;

  /**
   * El m 2m token ttl seconds de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<M2mTokenTtlSecondsVO> m2mTokenTtlSecondsValue;

  /**
   * El policy uri de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<PolicyUriVO> policyUriValue;

  /**
   * If true, users can use these client to access with public code flow
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<PublicAllowVO> publicAllowValue;

  /**
   * El registered at de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<RegisteredAtVO> registeredAtValue;

  /**
   * El registration access de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<RegistrationAccessVO> registrationAccessValue;

  /**
   * El request object signing alg de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<RequestObjectSigningAlgVO> requestObjectSigningAlgValue;

  /**
   * If true, every authorization code request from this client must include code_challenge and
   * code_challenge_method=S256. For clients with public-allow=true, PKCE is always required
   * regardless of this flag.
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<RequirePkceVO> requirePkceValue;

  /**
   * El require pushed authorization requests de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<RequirePushedAuthorizationRequestsVO> requirePushedAuthorizationRequestsValue;

  /**
   * El response types json de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<ResponseTypesJsonVO> responseTypesJsonValue;

  /**
   * El secret oauth de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<SecretOauthVO> secretOauthValue;

  /**
   * El sector identifier uri de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<SectorIdentifierUriVO> sectorIdentifierUriValue;

  /**
   * El subject type de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<SubjectTypeVO> subjectTypeValue;

  /**
   * Owner tenant. Null means a global client, manageable only by root.
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<TenantVO> tenantValue;

  /**
   * El tls client auth subject dn de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<TlsClientAuthSubjectDnVO> tlsClientAuthSubjectDnValue;

  /**
   * El tls client certificate bound access tokens de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<TlsClientCertificateBoundAccessTokensVO> tlsClientCertificateBoundAccessTokensValue;

  /**
   * El token endpoint auth method de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<TokenEndpointAuthMethodVO> tokenEndpointAuthMethodValue;

  /**
   * El token endpoint auth signing alg de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<TokenEndpointAuthSigningAlgVO> tokenEndpointAuthSigningAlgValue;

  /**
   * El tos uri de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<TosUriVO> tosUriValue;

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<UidVO> uidValue;

  /**
   * El userinfo encrypted response alg de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<UserinfoEncryptedResponseAlgVO> userinfoEncryptedResponseAlgValue;

  /**
   * El userinfo encrypted response enc de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<UserinfoEncryptedResponseEncVO> userinfoEncryptedResponseEncValue;

  /**
   * El userinfo signed response alg de trusted client
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<UserinfoSignedResponseAlgVO> userinfoSignedResponseAlgValue;

  /**
   * Campo con el número de version de trusted client para controlar bloqueos optimistas
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<VersionVO> versionValue;

  /**
   * Create a dto instance with all the values of an entity
   *
   * @autogenerated ActionProjectionGenerator
   * @param entity The entity who provide values.
   */
  public TrustedClientEnableProjection(final TrustedClient entity) {
    uidValue = Optional.of(entity.getUidValue());
    codeValue = Optional.of(entity.getCodeValue());
    tenantValue = Optional.of(entity.getTenantValue());
    allowAllScopesValue = Optional.of(entity.getAllowAllScopesValue());
    publicAllowValue = Optional.of(entity.getPublicAllowValue());
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
    registrationAccessValue = Optional.of(entity.getRegistrationAccessValue());
    defaultAcrValuesValue = Optional.of(entity.getDefaultAcrValuesValue());
    defaultMaxAgeValue = Optional.of(entity.getDefaultMaxAgeValue());
    sectorIdentifierUriValue = Optional.of(entity.getSectorIdentifierUriValue());
    allowedRedirectsValue = Optional.of(entity.getAllowedRedirectsValue());
    allowedLogoutRedirectsValue = Optional.of(entity.getAllowedLogoutRedirectsValue());
    allowedAudiencesValue = Optional.of(entity.getAllowedAudiencesValue());
    grantTypesJsonValue = Optional.of(entity.getGrantTypesJsonValue());
    isResourceServerValue = Optional.of(entity.getIsResourceServerValue());
    requirePushedAuthorizationRequestsValue =
        Optional.of(entity.getRequirePushedAuthorizationRequestsValue());
    requirePkceValue = Optional.of(entity.getRequirePkceValue());
    backChannelLogoutUriValue = Optional.of(entity.getBackChannelLogoutUriValue());
    backChannelLogoutSessionRequiredValue =
        Optional.of(entity.getBackChannelLogoutSessionRequiredValue());
    frontChannelLogoutUriValue = Optional.of(entity.getFrontChannelLogoutUriValue());
    frontChannelLogoutSessionRequiredValue =
        Optional.of(entity.getFrontChannelLogoutSessionRequiredValue());
    enabledValue = Optional.of(entity.getEnabledValue());
    clientNameValue = Optional.of(entity.getClientNameValue());
    logoUriValue = Optional.of(entity.getLogoUriValue());
    clientUriValue = Optional.of(entity.getClientUriValue());
    policyUriValue = Optional.of(entity.getPolicyUriValue());
    tosUriValue = Optional.of(entity.getTosUriValue());
    responseTypesJsonValue = Optional.of(entity.getResponseTypesJsonValue());
    dynamicallyRegisteredValue = Optional.of(entity.getDynamicallyRegisteredValue());
    registeredAtValue = Optional.of(entity.getRegisteredAtValue());
    allowedScopesM2mValue = Optional.of(entity.getAllowedScopesM2mValue());
    m2mTokenTtlSecondsValue = Optional.of(entity.getM2mTokenTtlSecondsValue());
    requestObjectSigningAlgValue = Optional.of(entity.getRequestObjectSigningAlgValue());
    jwksUriValue = Optional.of(entity.getJwksUriValue());
    jwksJsonValue = Optional.of(entity.getJwksJsonValue());
    versionValue = Optional.of(entity.getVersionValue());
  }

  /**
   * Inform for a possible change propolsal in AllowAllScopes
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for AllowAllScopes, otherwise the value for
   *         AllowAllScopes
   */
  public Boolean getAllowAllScopes() {
    return getAllowAllScopesOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in AllowAllScopes
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for AllowedAudiences, otherwise the value for
   *         AllowedAudiences
   */
  public List<AllowedAudiences> getAllowedAudiences() {
    return allowedAudiencesValue.map(AllowedAudiencesVO::getAllowedAudiences).orElseGet(List::of);
  }

  /**
   * Inform for a possible change propolsal in AllowedLogoutRedirects
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for AllowedLogoutRedirects, otherwise the value
   *         for AllowedLogoutRedirects
   */
  public List<AllowedLogoutRedirects> getAllowedLogoutRedirects() {
    return allowedLogoutRedirectsValue.map(AllowedLogoutRedirectsVO::getAllowedLogoutRedirects)
        .orElseGet(List::of);
  }

  /**
   * Inform for a possible change propolsal in AllowedRedirects
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for AllowedRedirects, otherwise the value for
   *         AllowedRedirects
   */
  public List<AllowedRedirects> getAllowedRedirects() {
    return allowedRedirectsValue.map(AllowedRedirectsVO::getAllowedRedirects).orElseGet(List::of);
  }

  /**
   * Inform for a possible change propolsal in AllowedScopesM2m
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for AllowedScopesM2m, otherwise the value for
   *         AllowedScopesM2m
   */
  public String getAllowedScopesM2m() {
    return getAllowedScopesM2mOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in AllowedScopesM2m
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for AllowedScopesM2m, otherwise the value for
   *         AllowedScopesM2m
   */
  public String getAllowedScopesM2mOrDefault(final String orDefault) {
    return allowedScopesM2mValue.flatMap(AllowedScopesM2mVO::getAllowedScopesM2m).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in AuthorizationDetailsTypes
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for AuthorizationDetailsTypes, otherwise the value
   *         for AuthorizationDetailsTypes
   */
  public String getAuthorizationDetailsTypes() {
    return getAuthorizationDetailsTypesOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in AuthorizationDetailsTypes
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for AuthorizationSignedResponseAlg, otherwise the
   *         value for AuthorizationSignedResponseAlg
   */
  public String getAuthorizationSignedResponseAlg() {
    return getAuthorizationSignedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in AuthorizationSignedResponseAlg
   *
   * @autogenerated ActionProjectionGenerator
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
   * Inform for a possible change propolsal in BackChannelLogoutSessionRequired
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for BackChannelLogoutSessionRequired, otherwise
   *         the value for BackChannelLogoutSessionRequired
   */
  public Boolean getBackChannelLogoutSessionRequired() {
    return getBackChannelLogoutSessionRequiredOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in BackChannelLogoutSessionRequired
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for BackChannelLogoutSessionRequired, otherwise
   *         the value for BackChannelLogoutSessionRequired
   */
  public Boolean getBackChannelLogoutSessionRequiredOrDefault(final Boolean orDefault) {
    return backChannelLogoutSessionRequiredValue
        .map(BackChannelLogoutSessionRequiredVO::isBackChannelLogoutSessionRequired)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in BackChannelLogoutUri
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for BackChannelLogoutUri, otherwise the value for
   *         BackChannelLogoutUri
   */
  public String getBackChannelLogoutUri() {
    return getBackChannelLogoutUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in BackChannelLogoutUri
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for BackChannelLogoutUri, otherwise the value for
   *         BackChannelLogoutUri
   */
  public String getBackChannelLogoutUriOrDefault(final String orDefault) {
    return backChannelLogoutUriValue.flatMap(BackChannelLogoutUriVO::getBackChannelLogoutUri)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in BackchannelClientNotificationEndpoint
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for BackchannelClientNotificationEndpoint,
   *         otherwise the value for BackchannelClientNotificationEndpoint
   */
  public String getBackchannelClientNotificationEndpoint() {
    return getBackchannelClientNotificationEndpointOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in BackchannelClientNotificationEndpoint
   *
   * @autogenerated ActionProjectionGenerator
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
   * Inform for a possible change propolsal in BackchannelTokenDeliveryMode
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for BackchannelTokenDeliveryMode, otherwise the
   *         value for BackchannelTokenDeliveryMode
   */
  public TrustedClientBackchannelTokenDeliveryModeOptions getBackchannelTokenDeliveryMode() {
    return getBackchannelTokenDeliveryModeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in BackchannelTokenDeliveryMode
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for BackchannelTokenDeliveryMode, otherwise the
   *         value for BackchannelTokenDeliveryMode
   */
  public TrustedClientBackchannelTokenDeliveryModeOptions getBackchannelTokenDeliveryModeOrDefault(
      final TrustedClientBackchannelTokenDeliveryModeOptions orDefault) {
    return backchannelTokenDeliveryModeValue
        .flatMap(BackchannelTokenDeliveryModeVO::getBackchannelTokenDeliveryMode).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in ClientName
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for ClientName, otherwise the value for ClientName
   */
  public String getClientName() {
    return getClientNameOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ClientName
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for ClientName, otherwise the value for ClientName
   */
  public String getClientNameOrDefault(final String orDefault) {
    return clientNameValue.flatMap(ClientNameVO::getClientName).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in ClientUri
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for ClientUri, otherwise the value for ClientUri
   */
  public String getClientUri() {
    return getClientUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ClientUri
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for ClientUri, otherwise the value for ClientUri
   */
  public String getClientUriOrDefault(final String orDefault) {
    return clientUriValue.flatMap(ClientUriVO::getClientUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public String getCode() {
    return getCodeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public String getCodeOrDefault(final String orDefault) {
    return codeValue.map(CodeVO::getCode).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in DefaultAcrValues
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for DefaultAcrValues, otherwise the value for
   *         DefaultAcrValues
   */
  public String getDefaultAcrValues() {
    return getDefaultAcrValuesOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in DefaultAcrValues
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for DefaultMaxAge, otherwise the value for
   *         DefaultMaxAge
   */
  public Integer getDefaultMaxAge() {
    return getDefaultMaxAgeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in DefaultMaxAge
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for DpopBoundAccessTokens, otherwise the value for
   *         DpopBoundAccessTokens
   */
  public Boolean getDpopBoundAccessTokens() {
    return getDpopBoundAccessTokensOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in DpopBoundAccessTokens
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for DynamicallyRegistered, otherwise the value for
   *         DynamicallyRegistered
   */
  public Boolean getDynamicallyRegistered() {
    return getDynamicallyRegisteredOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in DynamicallyRegistered
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Boolean getEnabled() {
    return getEnabledOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Boolean getEnabledOrDefault(final Boolean orDefault) {
    return enabledValue.map(EnabledVO::isEnabled).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in FrontChannelLogoutSessionRequired
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for FrontChannelLogoutSessionRequired, otherwise
   *         the value for FrontChannelLogoutSessionRequired
   */
  public Boolean getFrontChannelLogoutSessionRequired() {
    return getFrontChannelLogoutSessionRequiredOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in FrontChannelLogoutSessionRequired
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for FrontChannelLogoutSessionRequired, otherwise
   *         the value for FrontChannelLogoutSessionRequired
   */
  public Boolean getFrontChannelLogoutSessionRequiredOrDefault(final Boolean orDefault) {
    return frontChannelLogoutSessionRequiredValue
        .map(FrontChannelLogoutSessionRequiredVO::isFrontChannelLogoutSessionRequired)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in FrontChannelLogoutUri
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for FrontChannelLogoutUri, otherwise the value for
   *         FrontChannelLogoutUri
   */
  public String getFrontChannelLogoutUri() {
    return getFrontChannelLogoutUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in FrontChannelLogoutUri
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for FrontChannelLogoutUri, otherwise the value for
   *         FrontChannelLogoutUri
   */
  public String getFrontChannelLogoutUriOrDefault(final String orDefault) {
    return frontChannelLogoutUriValue.flatMap(FrontChannelLogoutUriVO::getFrontChannelLogoutUri)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in GrantTypesJson
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for GrantTypesJson, otherwise the value for
   *         GrantTypesJson
   */
  public String getGrantTypesJson() {
    return getGrantTypesJsonOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in GrantTypesJson
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseAlg, otherwise the
   *         value for IdTokenEncryptedResponseAlg
   */
  public String getIdTokenEncryptedResponseAlg() {
    return getIdTokenEncryptedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseAlg
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseEnc, otherwise the
   *         value for IdTokenEncryptedResponseEnc
   */
  public String getIdTokenEncryptedResponseEnc() {
    return getIdTokenEncryptedResponseEncOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseEnc
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for IdTokenSignedResponseAlg, otherwise the value
   *         for IdTokenSignedResponseAlg
   */
  public TrustedClientIdTokenSignedResponseAlgOptions getIdTokenSignedResponseAlg() {
    return getIdTokenSignedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in IdTokenSignedResponseAlg
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for IdTokenSignedResponseAlg, otherwise the value
   *         for IdTokenSignedResponseAlg
   */
  public TrustedClientIdTokenSignedResponseAlgOptions getIdTokenSignedResponseAlgOrDefault(
      final TrustedClientIdTokenSignedResponseAlgOptions orDefault) {
    return idTokenSignedResponseAlgValue
        .flatMap(IdTokenSignedResponseAlgVO::getIdTokenSignedResponseAlg).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in IsResourceServer
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for IsResourceServer, otherwise the value for
   *         IsResourceServer
   */
  public Boolean getIsResourceServer() {
    return getIsResourceServerOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in IsResourceServer
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for IsResourceServer, otherwise the value for
   *         IsResourceServer
   */
  public Boolean getIsResourceServerOrDefault(final Boolean orDefault) {
    return isResourceServerValue.map(IsResourceServerVO::isIsResourceServer).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in JwksJson
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for JwksJson, otherwise the value for JwksJson
   */
  public String getJwksJson() {
    return getJwksJsonOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in JwksJson
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for JwksJson, otherwise the value for JwksJson
   */
  public String getJwksJsonOrDefault(final String orDefault) {
    return jwksJsonValue.flatMap(JwksJsonVO::getJwksJson).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in JwksUri
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for JwksUri, otherwise the value for JwksUri
   */
  public String getJwksUri() {
    return getJwksUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in JwksUri
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for JwksUri, otherwise the value for JwksUri
   */
  public String getJwksUriOrDefault(final String orDefault) {
    return jwksUriValue.flatMap(JwksUriVO::getJwksUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in LogoUri
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for LogoUri, otherwise the value for LogoUri
   */
  public String getLogoUri() {
    return getLogoUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in LogoUri
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for LogoUri, otherwise the value for LogoUri
   */
  public String getLogoUriOrDefault(final String orDefault) {
    return logoUriValue.flatMap(LogoUriVO::getLogoUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in M2mTokenTtlSeconds
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for M2mTokenTtlSeconds, otherwise the value for
   *         M2mTokenTtlSeconds
   */
  public Integer getM2mTokenTtlSeconds() {
    return getM2mTokenTtlSecondsOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in M2mTokenTtlSeconds
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for M2mTokenTtlSeconds, otherwise the value for
   *         M2mTokenTtlSeconds
   */
  public Integer getM2mTokenTtlSecondsOrDefault(final Integer orDefault) {
    return m2mTokenTtlSecondsValue.map(M2mTokenTtlSecondsVO::getM2mTokenTtlSeconds)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in PolicyUri
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for PolicyUri, otherwise the value for PolicyUri
   */
  public String getPolicyUri() {
    return getPolicyUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in PolicyUri
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for PolicyUri, otherwise the value for PolicyUri
   */
  public String getPolicyUriOrDefault(final String orDefault) {
    return policyUriValue.flatMap(PolicyUriVO::getPolicyUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in PublicAllow
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for PublicAllow, otherwise the value for
   *         PublicAllow
   */
  public Boolean getPublicAllow() {
    return getPublicAllowOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in PublicAllow
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for PublicAllow, otherwise the value for
   *         PublicAllow
   */
  public Boolean getPublicAllowOrDefault(final Boolean orDefault) {
    return publicAllowValue.map(PublicAllowVO::isPublicAllow).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in RegisteredAt
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for RegisteredAt, otherwise the value for
   *         RegisteredAt
   */
  public OffsetDateTime getRegisteredAt() {
    return getRegisteredAtOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in RegisteredAt
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return
   */
  public String getRegistrationAccess() {
    return registrationAccessValue.isPresent() ? "****" : null;
  }

  /**
   * Inform for a possible change propolsal in RequestObjectSigningAlg
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for RequestObjectSigningAlg, otherwise the value
   *         for RequestObjectSigningAlg
   */
  public String getRequestObjectSigningAlg() {
    return getRequestObjectSigningAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in RequestObjectSigningAlg
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for RequirePkce, otherwise the value for
   *         RequirePkce
   */
  public Boolean getRequirePkce() {
    return getRequirePkceOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in RequirePkce
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for RequirePushedAuthorizationRequests, otherwise
   *         the value for RequirePushedAuthorizationRequests
   */
  public Boolean getRequirePushedAuthorizationRequests() {
    return getRequirePushedAuthorizationRequestsOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in RequirePushedAuthorizationRequests
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for ResponseTypesJson, otherwise the value for
   *         ResponseTypesJson
   */
  public String getResponseTypesJson() {
    return getResponseTypesJsonOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ResponseTypesJson
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return
   */
  public String getSecretOauth() {
    return secretOauthValue.isPresent() ? "****" : null;
  }

  /**
   * Inform for a possible change propolsal in SectorIdentifierUri
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for SectorIdentifierUri, otherwise the value for
   *         SectorIdentifierUri
   */
  public String getSectorIdentifierUri() {
    return getSectorIdentifierUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in SectorIdentifierUri
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for SubjectType, otherwise the value for
   *         SubjectType
   */
  public TrustedClientSubjectTypeOptions getSubjectType() {
    return getSubjectTypeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in SubjectType
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for SubjectType, otherwise the value for
   *         SubjectType
   */
  public TrustedClientSubjectTypeOptions getSubjectTypeOrDefault(
      final TrustedClientSubjectTypeOptions orDefault) {
    return subjectTypeValue.map(SubjectTypeVO::getSubjectType).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public TenantRef getTenant() {
    return tenantValue.flatMap(TenantVO::getTenant).orElse(null);
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public String getTenantReference() {
    return getTenantReferenceOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public String getTenantReferenceOrDefault(final String orDefault) {
    return tenantValue.flatMap(TenantVO::getTenant).map(TenantRef::getUid).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in TlsClientAuthSubjectDn
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for TlsClientAuthSubjectDn, otherwise the value
   *         for TlsClientAuthSubjectDn
   */
  public String getTlsClientAuthSubjectDn() {
    return getTlsClientAuthSubjectDnOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TlsClientAuthSubjectDn
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for TlsClientCertificateBoundAccessTokens,
   *         otherwise the value for TlsClientCertificateBoundAccessTokens
   */
  public Boolean getTlsClientCertificateBoundAccessTokens() {
    return getTlsClientCertificateBoundAccessTokensOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TlsClientCertificateBoundAccessTokens
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthMethod, otherwise the value
   *         for TokenEndpointAuthMethod
   */
  public TrustedClientTokenEndpointAuthMethodOptions getTokenEndpointAuthMethod() {
    return getTokenEndpointAuthMethodOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthMethod
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for TokenEndpointAuthMethod, otherwise the value
   *         for TokenEndpointAuthMethod
   */
  public TrustedClientTokenEndpointAuthMethodOptions getTokenEndpointAuthMethodOrDefault(
      final TrustedClientTokenEndpointAuthMethodOptions orDefault) {
    return tokenEndpointAuthMethodValue.map(TokenEndpointAuthMethodVO::getTokenEndpointAuthMethod)
        .orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthSigningAlg
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthSigningAlg, otherwise the
   *         value for TokenEndpointAuthSigningAlg
   */
  public String getTokenEndpointAuthSigningAlg() {
    return getTokenEndpointAuthSigningAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthSigningAlg
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for TosUri, otherwise the value for TosUri
   */
  public String getTosUri() {
    return getTosUriOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in TosUri
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for TosUri, otherwise the value for TosUri
   */
  public String getTosUriOrDefault(final String orDefault) {
    return tosUriValue.flatMap(TosUriVO::getTosUri).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public String getUid() {
    return getUidOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public String getUidOrDefault(final String orDefault) {
    return uidValue.map(UidVO::getUid).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseAlg
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseAlg, otherwise the
   *         value for UserinfoEncryptedResponseAlg
   */
  public String getUserinfoEncryptedResponseAlg() {
    return getUserinfoEncryptedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseAlg
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseEnc, otherwise the
   *         value for UserinfoEncryptedResponseEnc
   */
  public String getUserinfoEncryptedResponseEnc() {
    return getUserinfoEncryptedResponseEncOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseEnc
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for UserinfoSignedResponseAlg, otherwise the value
   *         for UserinfoSignedResponseAlg
   */
  public String getUserinfoSignedResponseAlg() {
    return getUserinfoSignedResponseAlgOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in UserinfoSignedResponseAlg
   *
   * @autogenerated ActionProjectionGenerator
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
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Integer getVersion() {
    return getVersionOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Integer getVersionOrDefault(final Integer orDefault) {
    return versionValue.flatMap(VersionVO::getVersion).orElse(orDefault);
  }
}
