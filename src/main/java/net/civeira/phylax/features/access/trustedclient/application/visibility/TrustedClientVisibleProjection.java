package net.civeira.phylax.features.access.trustedclient.application.visibility;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedAudiences;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedRedirects;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClient;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientReference;
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
 * Visible projection events happen when a dto is filled throught an existint entity when the
 * visibility service is preparing to send at user.The programmers could add policies to fill dto
 * values to show to user at the application level.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrustedClientVisibleProjection {

  /**
   * El allow all scopes de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowAllScopesVO> allowAllScopes = Optional.empty();

  /**
   * El allowed audiences de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowedAudiencesVO> allowedAudiences = Optional.empty();

  /**
   * El allowed logout redirects de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowedLogoutRedirectsVO> allowedLogoutRedirects = Optional.empty();

  /**
   * El allowed redirects de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowedRedirectsVO> allowedRedirects = Optional.empty();

  /**
   * El allowed scopes m 2m de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowedScopesM2mVO> allowedScopesM2m = Optional.empty();

  /**
   * El authorization details types de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AuthorizationDetailsTypesVO> authorizationDetailsTypes = Optional.empty();

  /**
   * El authorization signed response alg de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AuthorizationSignedResponseAlgVO> authorizationSignedResponseAlg =
      Optional.empty();

  /**
   * El back channel logout session required de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<BackChannelLogoutSessionRequiredVO> backChannelLogoutSessionRequired =
      Optional.empty();

  /**
   * El back channel logout uri de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<BackChannelLogoutUriVO> backChannelLogoutUri = Optional.empty();

  /**
   * El backchannel client notification endpoint de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<BackchannelClientNotificationEndpointVO> backchannelClientNotificationEndpoint =
      Optional.empty();

  /**
   * El backchannel token delivery mode de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<BackchannelTokenDeliveryModeVO> backchannelTokenDeliveryMode = Optional.empty();

  /**
   * El client name de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<ClientNameVO> clientName = Optional.empty();

  /**
   * El client uri de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<ClientUriVO> clientUri = Optional.empty();

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<CodeVO> code = Optional.empty();

  /**
   * El default acr values de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<DefaultAcrValuesVO> defaultAcrValues = Optional.empty();

  /**
   * El default max age de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<DefaultMaxAgeVO> defaultMaxAge = Optional.empty();

  /**
   * El dpop bound access tokens de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<DpopBoundAccessTokensVO> dpopBoundAccessTokens = Optional.empty();

  /**
   * El dynamically registered de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<DynamicallyRegisteredVO> dynamicallyRegistered = Optional.empty();

  /**
   * The indicator to allow the account be used without deleting it
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<EnabledVO> enabled = Optional.empty();

  /**
   * El front channel logout session required de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<FrontChannelLogoutSessionRequiredVO> frontChannelLogoutSessionRequired =
      Optional.empty();

  /**
   * El front channel logout uri de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<FrontChannelLogoutUriVO> frontChannelLogoutUri = Optional.empty();

  /**
   * El grant types json de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<GrantTypesJsonVO> grantTypesJson = Optional.empty();

  /**
   * El id token encrypted response alg de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<IdTokenEncryptedResponseAlgVO> idTokenEncryptedResponseAlg = Optional.empty();

  /**
   * El id token encrypted response enc de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<IdTokenEncryptedResponseEncVO> idTokenEncryptedResponseEnc = Optional.empty();

  /**
   * El id token signed response alg de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<IdTokenSignedResponseAlgVO> idTokenSignedResponseAlg = Optional.empty();

  /**
   * If true, this client is allowed to call the token introspection endpoint (POST /introspect) to
   * validate access tokens. Resource servers must authenticate at the introspection endpoint using
   * their client credentials.
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<IsResourceServerVO> isResourceServer = Optional.empty();

  /**
   * El jwks json de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<JwksJsonVO> jwksJson = Optional.empty();

  /**
   * El jwks uri de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<JwksUriVO> jwksUri = Optional.empty();

  /**
   * El logo uri de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<LogoUriVO> logoUri = Optional.empty();

  /**
   * El m 2m token ttl seconds de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<M2mTokenTtlSecondsVO> m2mTokenTtlSeconds = Optional.empty();

  /**
   * El policy uri de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<PolicyUriVO> policyUri = Optional.empty();

  /**
   * If true, users can use these client to access with public code flow
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<PublicAllowVO> publicAllow = Optional.empty();

  /**
   * El registered at de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<RegisteredAtVO> registeredAt = Optional.empty();

  /**
   * El registration access de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<RegistrationAccessVO> registrationAccess = Optional.empty();

  /**
   * El request object signing alg de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<RequestObjectSigningAlgVO> requestObjectSigningAlg = Optional.empty();

  /**
   * If true, every authorization code request from this client must include code_challenge and
   * code_challenge_method=S256. For clients with public-allow=true, PKCE is always required
   * regardless of this flag.
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<RequirePkceVO> requirePkce = Optional.empty();

  /**
   * El require pushed authorization requests de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<RequirePushedAuthorizationRequestsVO> requirePushedAuthorizationRequests =
      Optional.empty();

  /**
   * El response types json de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<ResponseTypesJsonVO> responseTypesJson = Optional.empty();

  /**
   * El secret oauth de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<SecretOauthVO> secretOauth = Optional.empty();

  /**
   * El sector identifier uri de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<SectorIdentifierUriVO> sectorIdentifierUri = Optional.empty();

  /**
   * El subject type de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<SubjectTypeVO> subjectType = Optional.empty();

  /**
   * Owner tenant. Null means a global client, manageable only by root.
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TenantVO> tenant = Optional.empty();

  /**
   * El tls client auth subject dn de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TlsClientAuthSubjectDnVO> tlsClientAuthSubjectDn = Optional.empty();

  /**
   * El tls client certificate bound access tokens de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TlsClientCertificateBoundAccessTokensVO> tlsClientCertificateBoundAccessTokens =
      Optional.empty();

  /**
   * El token endpoint auth method de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TokenEndpointAuthMethodVO> tokenEndpointAuthMethod = Optional.empty();

  /**
   * El token endpoint auth signing alg de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TokenEndpointAuthSigningAlgVO> tokenEndpointAuthSigningAlg = Optional.empty();

  /**
   * El tos uri de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TosUriVO> tosUri = Optional.empty();

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * El userinfo encrypted response alg de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<UserinfoEncryptedResponseAlgVO> userinfoEncryptedResponseAlg = Optional.empty();

  /**
   * El userinfo encrypted response enc de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<UserinfoEncryptedResponseEncVO> userinfoEncryptedResponseEnc = Optional.empty();

  /**
   * El userinfo signed response alg de trusted client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<UserinfoSignedResponseAlgVO> userinfoSignedResponseAlg = Optional.empty();

  /**
   * Campo con el número de version de trusted client para controlar bloqueos optimistas
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Create a dto instance with all the values of an entity
   *
   * @autogenerated VisibleProjectionGenerator
   * @param entity The entity who provide values.
   */
  public TrustedClientVisibleProjection(final TrustedClient entity) {
    uid = Optional.of(entity.getUidValue());
    code = Optional.of(entity.getCodeValue());
    tenant = Optional.of(entity.getTenantValue());
    allowAllScopes = Optional.of(entity.getAllowAllScopesValue());
    publicAllow = Optional.of(entity.getPublicAllowValue());
    tokenEndpointAuthMethod = Optional.of(entity.getTokenEndpointAuthMethodValue());
    tokenEndpointAuthSigningAlg = Optional.of(entity.getTokenEndpointAuthSigningAlgValue());
    dpopBoundAccessTokens = Optional.of(entity.getDpopBoundAccessTokensValue());
    idTokenSignedResponseAlg = Optional.of(entity.getIdTokenSignedResponseAlgValue());
    tlsClientAuthSubjectDn = Optional.of(entity.getTlsClientAuthSubjectDnValue());
    tlsClientCertificateBoundAccessTokens =
        Optional.of(entity.getTlsClientCertificateBoundAccessTokensValue());
    authorizationSignedResponseAlg = Optional.of(entity.getAuthorizationSignedResponseAlgValue());
    backchannelTokenDeliveryMode = Optional.of(entity.getBackchannelTokenDeliveryModeValue());
    backchannelClientNotificationEndpoint =
        Optional.of(entity.getBackchannelClientNotificationEndpointValue());
    subjectType = Optional.of(entity.getSubjectTypeValue());
    idTokenEncryptedResponseAlg = Optional.of(entity.getIdTokenEncryptedResponseAlgValue());
    idTokenEncryptedResponseEnc = Optional.of(entity.getIdTokenEncryptedResponseEncValue());
    userinfoEncryptedResponseAlg = Optional.of(entity.getUserinfoEncryptedResponseAlgValue());
    userinfoEncryptedResponseEnc = Optional.of(entity.getUserinfoEncryptedResponseEncValue());
    authorizationDetailsTypes = Optional.of(entity.getAuthorizationDetailsTypesValue());
    userinfoSignedResponseAlg = Optional.of(entity.getUserinfoSignedResponseAlgValue());
    secretOauth = Optional.of(entity.getSecretOauthValue());
    registrationAccess = Optional.of(entity.getRegistrationAccessValue());
    defaultAcrValues = Optional.of(entity.getDefaultAcrValuesValue());
    defaultMaxAge = Optional.of(entity.getDefaultMaxAgeValue());
    sectorIdentifierUri = Optional.of(entity.getSectorIdentifierUriValue());
    allowedRedirects = Optional.of(entity.getAllowedRedirectsValue());
    allowedLogoutRedirects = Optional.of(entity.getAllowedLogoutRedirectsValue());
    allowedAudiences = Optional.of(entity.getAllowedAudiencesValue());
    grantTypesJson = Optional.of(entity.getGrantTypesJsonValue());
    isResourceServer = Optional.of(entity.getIsResourceServerValue());
    requirePushedAuthorizationRequests =
        Optional.of(entity.getRequirePushedAuthorizationRequestsValue());
    requirePkce = Optional.of(entity.getRequirePkceValue());
    backChannelLogoutUri = Optional.of(entity.getBackChannelLogoutUriValue());
    backChannelLogoutSessionRequired =
        Optional.of(entity.getBackChannelLogoutSessionRequiredValue());
    frontChannelLogoutUri = Optional.of(entity.getFrontChannelLogoutUriValue());
    frontChannelLogoutSessionRequired =
        Optional.of(entity.getFrontChannelLogoutSessionRequiredValue());
    enabled = Optional.of(entity.getEnabledValue());
    clientName = Optional.of(entity.getClientNameValue());
    logoUri = Optional.of(entity.getLogoUriValue());
    clientUri = Optional.of(entity.getClientUriValue());
    policyUri = Optional.of(entity.getPolicyUriValue());
    tosUri = Optional.of(entity.getTosUriValue());
    responseTypesJson = Optional.of(entity.getResponseTypesJsonValue());
    dynamicallyRegistered = Optional.of(entity.getDynamicallyRegisteredValue());
    registeredAt = Optional.of(entity.getRegisteredAtValue());
    allowedScopesM2m = Optional.of(entity.getAllowedScopesM2mValue());
    m2mTokenTtlSeconds = Optional.of(entity.getM2mTokenTtlSecondsValue());
    requestObjectSigningAlg = Optional.of(entity.getRequestObjectSigningAlgValue());
    jwksUri = Optional.of(entity.getJwksUriValue());
    jwksJson = Optional.of(entity.getJwksJsonValue());
    version = Optional.of(entity.getVersionValue());
  }

  /**
   * Create a reference for trusted client with his inner assigned value.
   *
   * @autogenerated VisibleProjectionGenerator
   * @throws NoSuchElementException If no uid is set with a value
   * @return A reference if the direct reference build from stored value, otherwise an exception
   *         will be thrown.
   */
  public TrustedClientReference asReference() throws NoSuchElementException {
    return uid.map(UidVO::getUid).map(TrustedClientReference::of).orElseThrow();
  }

  /**
   * Inform for a possible change propolsal in AllowAllScopes
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for AllowAllScopes, otherwise the value for
   *         AllowAllScopes
   */
  public Optional<AllowAllScopesVO> getAllowAllScopes() {
    return allowAllScopes;
  }

  /**
   * Inform for a possible change propolsal in AllowedAudiences
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for AllowedAudiences, otherwise the value for
   *         AllowedAudiences
   */
  public Optional<AllowedAudiencesVO> getAllowedAudiences() {
    return allowedAudiences;
  }

  /**
   * Inform for a possible change propolsal in AllowedLogoutRedirects
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for AllowedLogoutRedirects, otherwise the value
   *         for AllowedLogoutRedirects
   */
  public Optional<AllowedLogoutRedirectsVO> getAllowedLogoutRedirects() {
    return allowedLogoutRedirects;
  }

  /**
   * Inform for a possible change propolsal in AllowedRedirects
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for AllowedRedirects, otherwise the value for
   *         AllowedRedirects
   */
  public Optional<AllowedRedirectsVO> getAllowedRedirects() {
    return allowedRedirects;
  }

  /**
   * Inform for a possible change propolsal in AllowedScopesM2m
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for AllowedScopesM2m, otherwise the value for
   *         AllowedScopesM2m
   */
  public Optional<AllowedScopesM2mVO> getAllowedScopesM2m() {
    return allowedScopesM2m;
  }

  /**
   * Inform for a possible change propolsal in AuthorizationDetailsTypes
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for AuthorizationDetailsTypes, otherwise the value
   *         for AuthorizationDetailsTypes
   */
  public Optional<AuthorizationDetailsTypesVO> getAuthorizationDetailsTypes() {
    return authorizationDetailsTypes;
  }

  /**
   * Inform for a possible change propolsal in AuthorizationSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for AuthorizationSignedResponseAlg, otherwise the
   *         value for AuthorizationSignedResponseAlg
   */
  public Optional<AuthorizationSignedResponseAlgVO> getAuthorizationSignedResponseAlg() {
    return authorizationSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in BackChannelLogoutSessionRequired
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for BackChannelLogoutSessionRequired, otherwise
   *         the value for BackChannelLogoutSessionRequired
   */
  public Optional<BackChannelLogoutSessionRequiredVO> getBackChannelLogoutSessionRequired() {
    return backChannelLogoutSessionRequired;
  }

  /**
   * Inform for a possible change propolsal in BackChannelLogoutUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for BackChannelLogoutUri, otherwise the value for
   *         BackChannelLogoutUri
   */
  public Optional<BackChannelLogoutUriVO> getBackChannelLogoutUri() {
    return backChannelLogoutUri;
  }

  /**
   * Inform for a possible change propolsal in BackchannelClientNotificationEndpoint
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for BackchannelClientNotificationEndpoint,
   *         otherwise the value for BackchannelClientNotificationEndpoint
   */
  public Optional<BackchannelClientNotificationEndpointVO> getBackchannelClientNotificationEndpoint() {
    return backchannelClientNotificationEndpoint;
  }

  /**
   * Inform for a possible change propolsal in BackchannelTokenDeliveryMode
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for BackchannelTokenDeliveryMode, otherwise the
   *         value for BackchannelTokenDeliveryMode
   */
  public Optional<BackchannelTokenDeliveryModeVO> getBackchannelTokenDeliveryMode() {
    return backchannelTokenDeliveryMode;
  }

  /**
   * Inform for a possible change propolsal in ClientName
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for ClientName, otherwise the value for ClientName
   */
  public Optional<ClientNameVO> getClientName() {
    return clientName;
  }

  /**
   * Inform for a possible change propolsal in ClientUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for ClientUri, otherwise the value for ClientUri
   */
  public Optional<ClientUriVO> getClientUri() {
    return clientUri;
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public Optional<CodeVO> getCode() {
    return code;
  }

  /**
   * Inform for a possible change propolsal in DefaultAcrValues
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for DefaultAcrValues, otherwise the value for
   *         DefaultAcrValues
   */
  public Optional<DefaultAcrValuesVO> getDefaultAcrValues() {
    return defaultAcrValues;
  }

  /**
   * Inform for a possible change propolsal in DefaultMaxAge
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for DefaultMaxAge, otherwise the value for
   *         DefaultMaxAge
   */
  public Optional<DefaultMaxAgeVO> getDefaultMaxAge() {
    return defaultMaxAge;
  }

  /**
   * Inform for a possible change propolsal in DpopBoundAccessTokens
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for DpopBoundAccessTokens, otherwise the value for
   *         DpopBoundAccessTokens
   */
  public Optional<DpopBoundAccessTokensVO> getDpopBoundAccessTokens() {
    return dpopBoundAccessTokens;
  }

  /**
   * Inform for a possible change propolsal in DynamicallyRegistered
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for DynamicallyRegistered, otherwise the value for
   *         DynamicallyRegistered
   */
  public Optional<DynamicallyRegisteredVO> getDynamicallyRegistered() {
    return dynamicallyRegistered;
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Optional<EnabledVO> getEnabled() {
    return enabled;
  }

  /**
   * Inform for a possible change propolsal in FrontChannelLogoutSessionRequired
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for FrontChannelLogoutSessionRequired, otherwise
   *         the value for FrontChannelLogoutSessionRequired
   */
  public Optional<FrontChannelLogoutSessionRequiredVO> getFrontChannelLogoutSessionRequired() {
    return frontChannelLogoutSessionRequired;
  }

  /**
   * Inform for a possible change propolsal in FrontChannelLogoutUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for FrontChannelLogoutUri, otherwise the value for
   *         FrontChannelLogoutUri
   */
  public Optional<FrontChannelLogoutUriVO> getFrontChannelLogoutUri() {
    return frontChannelLogoutUri;
  }

  /**
   * Inform for a possible change propolsal in GrantTypesJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for GrantTypesJson, otherwise the value for
   *         GrantTypesJson
   */
  public Optional<GrantTypesJsonVO> getGrantTypesJson() {
    return grantTypesJson;
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseAlg, otherwise the
   *         value for IdTokenEncryptedResponseAlg
   */
  public Optional<IdTokenEncryptedResponseAlgVO> getIdTokenEncryptedResponseAlg() {
    return idTokenEncryptedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseEnc
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseEnc, otherwise the
   *         value for IdTokenEncryptedResponseEnc
   */
  public Optional<IdTokenEncryptedResponseEncVO> getIdTokenEncryptedResponseEnc() {
    return idTokenEncryptedResponseEnc;
  }

  /**
   * Inform for a possible change propolsal in IdTokenSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for IdTokenSignedResponseAlg, otherwise the value
   *         for IdTokenSignedResponseAlg
   */
  public Optional<IdTokenSignedResponseAlgVO> getIdTokenSignedResponseAlg() {
    return idTokenSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in IsResourceServer
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for IsResourceServer, otherwise the value for
   *         IsResourceServer
   */
  public Optional<IsResourceServerVO> getIsResourceServer() {
    return isResourceServer;
  }

  /**
   * Inform for a possible change propolsal in JwksJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for JwksJson, otherwise the value for JwksJson
   */
  public Optional<JwksJsonVO> getJwksJson() {
    return jwksJson;
  }

  /**
   * Inform for a possible change propolsal in JwksUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for JwksUri, otherwise the value for JwksUri
   */
  public Optional<JwksUriVO> getJwksUri() {
    return jwksUri;
  }

  /**
   * Inform for a possible change propolsal in LogoUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for LogoUri, otherwise the value for LogoUri
   */
  public Optional<LogoUriVO> getLogoUri() {
    return logoUri;
  }

  /**
   * Inform for a possible change propolsal in M2mTokenTtlSeconds
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for M2mTokenTtlSeconds, otherwise the value for
   *         M2mTokenTtlSeconds
   */
  public Optional<M2mTokenTtlSecondsVO> getM2mTokenTtlSeconds() {
    return m2mTokenTtlSeconds;
  }

  /**
   * Inform for a possible change propolsal in PolicyUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for PolicyUri, otherwise the value for PolicyUri
   */
  public Optional<PolicyUriVO> getPolicyUri() {
    return policyUri;
  }

  /**
   * Inform for a possible change propolsal in PublicAllow
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for PublicAllow, otherwise the value for
   *         PublicAllow
   */
  public Optional<PublicAllowVO> getPublicAllow() {
    return publicAllow;
  }

  /**
   * Inform for a possible change propolsal in RegisteredAt
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for RegisteredAt, otherwise the value for
   *         RegisteredAt
   */
  public Optional<RegisteredAtVO> getRegisteredAt() {
    return registeredAt;
  }

  /**
   * Inform for a possible change propolsal in RegistrationAccess
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for RegistrationAccess, otherwise the value for
   *         RegistrationAccess
   */
  public Optional<RegistrationAccessVO> getRegistrationAccess() {
    return registrationAccess;
  }

  /**
   * Inform for a possible change propolsal in RequestObjectSigningAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for RequestObjectSigningAlg, otherwise the value
   *         for RequestObjectSigningAlg
   */
  public Optional<RequestObjectSigningAlgVO> getRequestObjectSigningAlg() {
    return requestObjectSigningAlg;
  }

  /**
   * Inform for a possible change propolsal in RequirePkce
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for RequirePkce, otherwise the value for
   *         RequirePkce
   */
  public Optional<RequirePkceVO> getRequirePkce() {
    return requirePkce;
  }

  /**
   * Inform for a possible change propolsal in RequirePushedAuthorizationRequests
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for RequirePushedAuthorizationRequests, otherwise
   *         the value for RequirePushedAuthorizationRequests
   */
  public Optional<RequirePushedAuthorizationRequestsVO> getRequirePushedAuthorizationRequests() {
    return requirePushedAuthorizationRequests;
  }

  /**
   * Inform for a possible change propolsal in ResponseTypesJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for ResponseTypesJson, otherwise the value for
   *         ResponseTypesJson
   */
  public Optional<ResponseTypesJsonVO> getResponseTypesJson() {
    return responseTypesJson;
  }

  /**
   * Inform for a possible change propolsal in SecretOauth
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for SecretOauth, otherwise the value for
   *         SecretOauth
   */
  public Optional<SecretOauthVO> getSecretOauth() {
    return secretOauth;
  }

  /**
   * Inform for a possible change propolsal in SectorIdentifierUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for SectorIdentifierUri, otherwise the value for
   *         SectorIdentifierUri
   */
  public Optional<SectorIdentifierUriVO> getSectorIdentifierUri() {
    return sectorIdentifierUri;
  }

  /**
   * Inform for a possible change propolsal in SubjectType
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for SubjectType, otherwise the value for
   *         SubjectType
   */
  public Optional<SubjectTypeVO> getSubjectType() {
    return subjectType;
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public Optional<TenantVO> getTenant() {
    return tenant;
  }

  /**
   * Inform for a possible change propolsal in TlsClientAuthSubjectDn
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for TlsClientAuthSubjectDn, otherwise the value
   *         for TlsClientAuthSubjectDn
   */
  public Optional<TlsClientAuthSubjectDnVO> getTlsClientAuthSubjectDn() {
    return tlsClientAuthSubjectDn;
  }

  /**
   * Inform for a possible change propolsal in TlsClientCertificateBoundAccessTokens
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for TlsClientCertificateBoundAccessTokens,
   *         otherwise the value for TlsClientCertificateBoundAccessTokens
   */
  public Optional<TlsClientCertificateBoundAccessTokensVO> getTlsClientCertificateBoundAccessTokens() {
    return tlsClientCertificateBoundAccessTokens;
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthMethod
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthMethod, otherwise the value
   *         for TokenEndpointAuthMethod
   */
  public Optional<TokenEndpointAuthMethodVO> getTokenEndpointAuthMethod() {
    return tokenEndpointAuthMethod;
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthSigningAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthSigningAlg, otherwise the
   *         value for TokenEndpointAuthSigningAlg
   */
  public Optional<TokenEndpointAuthSigningAlgVO> getTokenEndpointAuthSigningAlg() {
    return tokenEndpointAuthSigningAlg;
  }

  /**
   * Inform for a possible change propolsal in TosUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for TosUri, otherwise the value for TosUri
   */
  public Optional<TosUriVO> getTosUri() {
    return tosUri;
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public Optional<UidVO> getUid() {
    return uid;
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseAlg, otherwise the
   *         value for UserinfoEncryptedResponseAlg
   */
  public Optional<UserinfoEncryptedResponseAlgVO> getUserinfoEncryptedResponseAlg() {
    return userinfoEncryptedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseEnc
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseEnc, otherwise the
   *         value for UserinfoEncryptedResponseEnc
   */
  public Optional<UserinfoEncryptedResponseEncVO> getUserinfoEncryptedResponseEnc() {
    return userinfoEncryptedResponseEnc;
  }

  /**
   * Inform for a possible change propolsal in UserinfoSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for UserinfoSignedResponseAlg, otherwise the value
   *         for UserinfoSignedResponseAlg
   */
  public Optional<UserinfoSignedResponseAlgVO> getUserinfoSignedResponseAlg() {
    return userinfoSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Optional<VersionVO> getVersion() {
    return version;
  }

  /**
   * Assigna change proposal for AllowAllScopes to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setAllowAllScopes(final Boolean value) {
    this.allowAllScopes = Optional.of(AllowAllScopesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedAudiences to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setAllowedAudiences(final List<AllowedAudiences> value) {
    this.allowedAudiences = Optional.of(AllowedAudiencesVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedLogoutRedirects to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setAllowedLogoutRedirects(
      final List<AllowedLogoutRedirects> value) {
    this.allowedLogoutRedirects =
        Optional.of(AllowedLogoutRedirectsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedRedirects to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setAllowedRedirects(final List<AllowedRedirects> value) {
    this.allowedRedirects = Optional.of(AllowedRedirectsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedScopesM2m to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setAllowedScopesM2m(final String value) {
    this.allowedScopesM2m = Optional.of(AllowedScopesM2mVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AuthorizationDetailsTypes to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setAuthorizationDetailsTypes(final String value) {
    this.authorizationDetailsTypes = Optional.of(AuthorizationDetailsTypesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AuthorizationSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setAuthorizationSignedResponseAlg(final String value) {
    this.authorizationSignedResponseAlg = Optional.of(AuthorizationSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackChannelLogoutSessionRequired to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setBackChannelLogoutSessionRequired(final Boolean value) {
    this.backChannelLogoutSessionRequired =
        Optional.of(BackChannelLogoutSessionRequiredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackChannelLogoutUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setBackChannelLogoutUri(final String value) {
    this.backChannelLogoutUri = Optional.of(BackChannelLogoutUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelClientNotificationEndpoint to be apply for the
   * aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setBackchannelClientNotificationEndpoint(
      final String value) {
    this.backchannelClientNotificationEndpoint =
        Optional.of(BackchannelClientNotificationEndpointVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelTokenDeliveryMode to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setBackchannelTokenDeliveryMode(
      final TrustedClientBackchannelTokenDeliveryModeOptions value) {
    this.backchannelTokenDeliveryMode = Optional.of(BackchannelTokenDeliveryModeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientName to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setClientName(final String value) {
    this.clientName = Optional.of(ClientNameVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setClientUri(final String value) {
    this.clientUri = Optional.of(ClientUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Code to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setCode(final String value) {
    this.code = Optional.of(CodeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DefaultAcrValues to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setDefaultAcrValues(final String value) {
    this.defaultAcrValues = Optional.of(DefaultAcrValuesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DefaultMaxAge to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setDefaultMaxAge(final Integer value) {
    this.defaultMaxAge = Optional.of(DefaultMaxAgeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DpopBoundAccessTokens to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setDpopBoundAccessTokens(final Boolean value) {
    this.dpopBoundAccessTokens = Optional.of(DpopBoundAccessTokensVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DynamicallyRegistered to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setDynamicallyRegistered(final Boolean value) {
    this.dynamicallyRegistered = Optional.of(DynamicallyRegisteredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Enabled to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for FrontChannelLogoutSessionRequired to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setFrontChannelLogoutSessionRequired(final Boolean value) {
    this.frontChannelLogoutSessionRequired =
        Optional.of(FrontChannelLogoutSessionRequiredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for FrontChannelLogoutUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setFrontChannelLogoutUri(final String value) {
    this.frontChannelLogoutUri = Optional.of(FrontChannelLogoutUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for GrantTypesJson to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setGrantTypesJson(final String value) {
    this.grantTypesJson = Optional.of(GrantTypesJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenEncryptedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setIdTokenEncryptedResponseAlg(final String value) {
    this.idTokenEncryptedResponseAlg = Optional.of(IdTokenEncryptedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenEncryptedResponseEnc to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setIdTokenEncryptedResponseEnc(final String value) {
    this.idTokenEncryptedResponseEnc = Optional.of(IdTokenEncryptedResponseEncVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setIdTokenSignedResponseAlg(
      final TrustedClientIdTokenSignedResponseAlgOptions value) {
    this.idTokenSignedResponseAlg = Optional.of(IdTokenSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IsResourceServer to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setIsResourceServer(final Boolean value) {
    this.isResourceServer = Optional.of(IsResourceServerVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for JwksJson to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setJwksJson(final String value) {
    this.jwksJson = Optional.of(JwksJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for JwksUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setJwksUri(final String value) {
    this.jwksUri = Optional.of(JwksUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for LogoUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setLogoUri(final String value) {
    this.logoUri = Optional.of(LogoUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for M2mTokenTtlSeconds to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setM2mTokenTtlSeconds(final Integer value) {
    this.m2mTokenTtlSeconds = Optional.of(M2mTokenTtlSecondsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for PolicyUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setPolicyUri(final String value) {
    this.policyUri = Optional.of(PolicyUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for PublicAllow to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setPublicAllow(final Boolean value) {
    this.publicAllow = Optional.of(PublicAllowVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RegisteredAt to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setRegisteredAt(final OffsetDateTime value) {
    this.registeredAt = Optional.of(RegisteredAtVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RegistrationAccess to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setRegistrationAccess(final String value) {
    this.registrationAccess = Optional.of(RegistrationAccessVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for RequestObjectSigningAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setRequestObjectSigningAlg(final String value) {
    this.requestObjectSigningAlg = Optional.of(RequestObjectSigningAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RequirePkce to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setRequirePkce(final Boolean value) {
    this.requirePkce = Optional.of(RequirePkceVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RequirePushedAuthorizationRequests to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setRequirePushedAuthorizationRequests(final Boolean value) {
    this.requirePushedAuthorizationRequests =
        Optional.of(RequirePushedAuthorizationRequestsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ResponseTypesJson to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setResponseTypesJson(final String value) {
    this.responseTypesJson = Optional.of(ResponseTypesJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SecretOauth to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setSecretOauth(final String value) {
    this.secretOauth = Optional.of(SecretOauthVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for SectorIdentifierUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setSectorIdentifierUri(final String value) {
    this.sectorIdentifierUri = Optional.of(SectorIdentifierUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SubjectType to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setSubjectType(
      final TrustedClientSubjectTypeOptions value) {
    this.subjectType = Optional.of(SubjectTypeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Tenant to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TlsClientAuthSubjectDn to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setTlsClientAuthSubjectDn(final String value) {
    this.tlsClientAuthSubjectDn = Optional.of(TlsClientAuthSubjectDnVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TlsClientCertificateBoundAccessTokens to be apply for the
   * aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setTlsClientCertificateBoundAccessTokens(
      final Boolean value) {
    this.tlsClientCertificateBoundAccessTokens =
        Optional.of(TlsClientCertificateBoundAccessTokensVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TokenEndpointAuthMethod to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setTokenEndpointAuthMethod(
      final TrustedClientTokenEndpointAuthMethodOptions value) {
    this.tokenEndpointAuthMethod = Optional.of(TokenEndpointAuthMethodVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TokenEndpointAuthSigningAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setTokenEndpointAuthSigningAlg(final String value) {
    this.tokenEndpointAuthSigningAlg = Optional.of(TokenEndpointAuthSigningAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TosUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setTosUri(final String value) {
    this.tosUri = Optional.of(TosUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoEncryptedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setUserinfoEncryptedResponseAlg(final String value) {
    this.userinfoEncryptedResponseAlg = Optional.of(UserinfoEncryptedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoEncryptedResponseEnc to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setUserinfoEncryptedResponseEnc(final String value) {
    this.userinfoEncryptedResponseEnc = Optional.of(UserinfoEncryptedResponseEncVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setUserinfoSignedResponseAlg(final String value) {
    this.userinfoSignedResponseAlg = Optional.of(UserinfoSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for TrustedClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public TrustedClientVisibleProjection setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * Unset a value by the property name. Unset values will no provide command change neither provide
   * information to the output context (hidden or readonly fields, or policy restrictions).
   *
   * @autogenerated VisibleProjectionGenerator
   * @param field The field name to unset.
   */
  public void unset(final String field) {
    if ("uid".equals(field)) {
      this.unsetUid();
    }
    if ("code".equals(field)) {
      this.unsetCode();
    }
    if ("tenant".equals(field)) {
      this.unsetTenant();
    }
    if ("allowAllScopes".equals(field)) {
      this.unsetAllowAllScopes();
    }
    if ("publicAllow".equals(field)) {
      this.unsetPublicAllow();
    }
    if ("tokenEndpointAuthMethod".equals(field)) {
      this.unsetTokenEndpointAuthMethod();
    }
    if ("tokenEndpointAuthSigningAlg".equals(field)) {
      this.unsetTokenEndpointAuthSigningAlg();
    }
    if ("dpopBoundAccessTokens".equals(field)) {
      this.unsetDpopBoundAccessTokens();
    }
    if ("idTokenSignedResponseAlg".equals(field)) {
      this.unsetIdTokenSignedResponseAlg();
    }
    if ("tlsClientAuthSubjectDn".equals(field)) {
      this.unsetTlsClientAuthSubjectDn();
    }
    if ("tlsClientCertificateBoundAccessTokens".equals(field)) {
      this.unsetTlsClientCertificateBoundAccessTokens();
    }
    if ("authorizationSignedResponseAlg".equals(field)) {
      this.unsetAuthorizationSignedResponseAlg();
    }
    if ("backchannelTokenDeliveryMode".equals(field)) {
      this.unsetBackchannelTokenDeliveryMode();
    }
    if ("backchannelClientNotificationEndpoint".equals(field)) {
      this.unsetBackchannelClientNotificationEndpoint();
    }
    if ("subjectType".equals(field)) {
      this.unsetSubjectType();
    }
    if ("idTokenEncryptedResponseAlg".equals(field)) {
      this.unsetIdTokenEncryptedResponseAlg();
    }
    if ("idTokenEncryptedResponseEnc".equals(field)) {
      this.unsetIdTokenEncryptedResponseEnc();
    }
    if ("userinfoEncryptedResponseAlg".equals(field)) {
      this.unsetUserinfoEncryptedResponseAlg();
    }
    if ("userinfoEncryptedResponseEnc".equals(field)) {
      this.unsetUserinfoEncryptedResponseEnc();
    }
    if ("authorizationDetailsTypes".equals(field)) {
      this.unsetAuthorizationDetailsTypes();
    }
    if ("userinfoSignedResponseAlg".equals(field)) {
      this.unsetUserinfoSignedResponseAlg();
    }
    if ("secretOauth".equals(field)) {
      this.unsetSecretOauth();
    }
    if ("registrationAccess".equals(field)) {
      this.unsetRegistrationAccess();
    }
    if ("defaultAcrValues".equals(field)) {
      this.unsetDefaultAcrValues();
    }
    if ("defaultMaxAge".equals(field)) {
      this.unsetDefaultMaxAge();
    }
    if ("sectorIdentifierUri".equals(field)) {
      this.unsetSectorIdentifierUri();
    }
    if ("allowedRedirects".equals(field)) {
      this.unsetAllowedRedirects();
    }
    if ("allowedLogoutRedirects".equals(field)) {
      this.unsetAllowedLogoutRedirects();
    }
    if ("allowedAudiences".equals(field)) {
      this.unsetAllowedAudiences();
    }
    if ("grantTypesJson".equals(field)) {
      this.unsetGrantTypesJson();
    }
    if ("isResourceServer".equals(field)) {
      this.unsetIsResourceServer();
    }
    if ("requirePushedAuthorizationRequests".equals(field)) {
      this.unsetRequirePushedAuthorizationRequests();
    }
    if ("requirePkce".equals(field)) {
      this.unsetRequirePkce();
    }
    if ("backChannelLogoutUri".equals(field)) {
      this.unsetBackChannelLogoutUri();
    }
    if ("backChannelLogoutSessionRequired".equals(field)) {
      this.unsetBackChannelLogoutSessionRequired();
    }
    if ("frontChannelLogoutUri".equals(field)) {
      this.unsetFrontChannelLogoutUri();
    }
    if ("frontChannelLogoutSessionRequired".equals(field)) {
      this.unsetFrontChannelLogoutSessionRequired();
    }
    if ("enabled".equals(field)) {
      this.unsetEnabled();
    }
    if ("clientName".equals(field)) {
      this.unsetClientName();
    }
    if ("logoUri".equals(field)) {
      this.unsetLogoUri();
    }
    if ("clientUri".equals(field)) {
      this.unsetClientUri();
    }
    if ("policyUri".equals(field)) {
      this.unsetPolicyUri();
    }
    if ("tosUri".equals(field)) {
      this.unsetTosUri();
    }
    if ("responseTypesJson".equals(field)) {
      this.unsetResponseTypesJson();
    }
    if ("dynamicallyRegistered".equals(field)) {
      this.unsetDynamicallyRegistered();
    }
    if ("registeredAt".equals(field)) {
      this.unsetRegisteredAt();
    }
    if ("allowedScopesM2m".equals(field)) {
      this.unsetAllowedScopesM2m();
    }
    if ("m2mTokenTtlSeconds".equals(field)) {
      this.unsetM2mTokenTtlSeconds();
    }
    if ("requestObjectSigningAlg".equals(field)) {
      this.unsetRequestObjectSigningAlg();
    }
    if ("jwksUri".equals(field)) {
      this.unsetJwksUri();
    }
    if ("jwksJson".equals(field)) {
      this.unsetJwksJson();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * Remove the change propolsal for AllowAllScopes
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetAllowAllScopes() {
    this.allowAllScopes = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AllowedAudiences
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetAllowedAudiences() {
    this.allowedAudiences = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AllowedLogoutRedirects
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetAllowedLogoutRedirects() {
    this.allowedLogoutRedirects = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AllowedRedirects
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetAllowedRedirects() {
    this.allowedRedirects = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AllowedScopesM2m
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetAllowedScopesM2m() {
    this.allowedScopesM2m = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AuthorizationDetailsTypes
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetAuthorizationDetailsTypes() {
    this.authorizationDetailsTypes = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AuthorizationSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetAuthorizationSignedResponseAlg() {
    this.authorizationSignedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for BackChannelLogoutSessionRequired
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetBackChannelLogoutSessionRequired() {
    this.backChannelLogoutSessionRequired = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for BackChannelLogoutUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetBackChannelLogoutUri() {
    this.backChannelLogoutUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for BackchannelClientNotificationEndpoint
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetBackchannelClientNotificationEndpoint() {
    this.backchannelClientNotificationEndpoint = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for BackchannelTokenDeliveryMode
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetBackchannelTokenDeliveryMode() {
    this.backchannelTokenDeliveryMode = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for ClientName
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetClientName() {
    this.clientName = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for ClientUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetClientUri() {
    this.clientUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Code
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetCode() {
    this.code = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for DefaultAcrValues
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetDefaultAcrValues() {
    this.defaultAcrValues = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for DefaultMaxAge
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetDefaultMaxAge() {
    this.defaultMaxAge = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for DpopBoundAccessTokens
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetDpopBoundAccessTokens() {
    this.dpopBoundAccessTokens = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for DynamicallyRegistered
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetDynamicallyRegistered() {
    this.dynamicallyRegistered = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Enabled
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetEnabled() {
    this.enabled = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for FrontChannelLogoutSessionRequired
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetFrontChannelLogoutSessionRequired() {
    this.frontChannelLogoutSessionRequired = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for FrontChannelLogoutUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetFrontChannelLogoutUri() {
    this.frontChannelLogoutUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for GrantTypesJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetGrantTypesJson() {
    this.grantTypesJson = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for IdTokenEncryptedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetIdTokenEncryptedResponseAlg() {
    this.idTokenEncryptedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for IdTokenEncryptedResponseEnc
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetIdTokenEncryptedResponseEnc() {
    this.idTokenEncryptedResponseEnc = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for IdTokenSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetIdTokenSignedResponseAlg() {
    this.idTokenSignedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for IsResourceServer
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetIsResourceServer() {
    this.isResourceServer = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for JwksJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetJwksJson() {
    this.jwksJson = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for JwksUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetJwksUri() {
    this.jwksUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for LogoUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetLogoUri() {
    this.logoUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for M2mTokenTtlSeconds
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetM2mTokenTtlSeconds() {
    this.m2mTokenTtlSeconds = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for PolicyUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetPolicyUri() {
    this.policyUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for PublicAllow
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetPublicAllow() {
    this.publicAllow = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RegisteredAt
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetRegisteredAt() {
    this.registeredAt = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RegistrationAccess
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetRegistrationAccess() {
    this.registrationAccess = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RequestObjectSigningAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetRequestObjectSigningAlg() {
    this.requestObjectSigningAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RequirePkce
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetRequirePkce() {
    this.requirePkce = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RequirePushedAuthorizationRequests
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetRequirePushedAuthorizationRequests() {
    this.requirePushedAuthorizationRequests = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for ResponseTypesJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetResponseTypesJson() {
    this.responseTypesJson = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for SecretOauth
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetSecretOauth() {
    this.secretOauth = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for SectorIdentifierUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetSectorIdentifierUri() {
    this.sectorIdentifierUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for SubjectType
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetSubjectType() {
    this.subjectType = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Tenant
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetTenant() {
    this.tenant = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TlsClientAuthSubjectDn
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetTlsClientAuthSubjectDn() {
    this.tlsClientAuthSubjectDn = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TlsClientCertificateBoundAccessTokens
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetTlsClientCertificateBoundAccessTokens() {
    this.tlsClientCertificateBoundAccessTokens = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TokenEndpointAuthMethod
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetTokenEndpointAuthMethod() {
    this.tokenEndpointAuthMethod = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TokenEndpointAuthSigningAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetTokenEndpointAuthSigningAlg() {
    this.tokenEndpointAuthSigningAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TosUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetTosUri() {
    this.tosUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Uid
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for UserinfoEncryptedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetUserinfoEncryptedResponseAlg() {
    this.userinfoEncryptedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for UserinfoEncryptedResponseEnc
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetUserinfoEncryptedResponseEnc() {
    this.userinfoEncryptedResponseEnc = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for UserinfoSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetUserinfoSignedResponseAlg() {
    this.userinfoSignedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Version
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public TrustedClientVisibleProjection unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
