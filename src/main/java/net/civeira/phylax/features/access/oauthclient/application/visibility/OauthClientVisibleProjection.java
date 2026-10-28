package net.civeira.phylax.features.access.oauthclient.application.visibility;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.features.access.oauthclient.domain.AllowedAudiences;
import net.civeira.phylax.features.access.oauthclient.domain.AllowedLoginRedirects;
import net.civeira.phylax.features.access.oauthclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClient;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClientReference;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClientSubjectTypeOptions;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClientTokenEndpointAuthMethodOptions;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.AllowAllScopesVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.AllowedAudiencesVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.AllowedLoginRedirectsVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.AllowedLogoutRedirectsVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.AuthorizationDetailsTypesVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.AuthorizationSignedResponseAlgVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.BackchannelClientNotificationEndpointVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.BackchannelLogoutSessionRequiredVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.BackchannelLogoutUriVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.BackchannelTokenDeliveryModeVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.ClientCredentialsScopesVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.ClientCredentialsTokenTtlSecondsVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.ClientNameVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.ClientUriVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.CodeVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.DefaultAcrValuesVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.DefaultMaxAgeVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.DpopBoundAccessTokensVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.DynamicallyRegisteredVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.FrontchannelLogoutSessionRequiredVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.FrontchannelLogoutUriVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.GrantTypesJsonVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.IdTokenEncryptedResponseAlgVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.IdTokenEncryptedResponseEncVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.IdTokenSignedResponseAlgVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.IntrospectionAllowedVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.JwksJsonVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.JwksUriVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.LogoUriVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.PolicyUriVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.RegisteredAtVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.RegistrationAccessTokenVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.RequestObjectSigningAlgVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.RequirePkceVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.RequirePushedAuthorizationRequestsVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.ResponseTypesJsonVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.SecretOauthVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.SectorIdentifierUriVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.SubjectTypeVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.TenantVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.TlsClientAuthSubjectDnVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.TlsClientCertificateBoundAccessTokensVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.TokenEndpointAuthMethodVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.TokenEndpointAuthSigningAlgVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.TosUriVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.UserinfoEncryptedResponseAlgVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.UserinfoEncryptedResponseEncVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.UserinfoSignedResponseAlgVO;
import net.civeira.phylax.features.access.oauthclient.domain.valueobject.VersionVO;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;

/**
 * Visible projection events happen when a dto is filled throught an existint entity when the
 * visibility service is preparing to send at user.The programmers could add policies to fill dto
 * values to show to user at the application level.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OauthClientVisibleProjection {

  /**
   * El allow all scopes de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowAllScopesVO> allowAllScopes = Optional.empty();

  /**
   * El allowed audiences de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowedAudiencesVO> allowedAudiences = Optional.empty();

  /**
   * El allowed login redirects de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowedLoginRedirectsVO> allowedLoginRedirects = Optional.empty();

  /**
   * El allowed logout redirects de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AllowedLogoutRedirectsVO> allowedLogoutRedirects = Optional.empty();

  /**
   * El authorization details types de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AuthorizationDetailsTypesVO> authorizationDetailsTypes = Optional.empty();

  /**
   * El authorization signed response alg de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<AuthorizationSignedResponseAlgVO> authorizationSignedResponseAlg =
      Optional.empty();

  /**
   * El backchannel client notification endpoint de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<BackchannelClientNotificationEndpointVO> backchannelClientNotificationEndpoint =
      Optional.empty();

  /**
   * El backchannel logout session required de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<BackchannelLogoutSessionRequiredVO> backchannelLogoutSessionRequired =
      Optional.empty();

  /**
   * El backchannel logout uri de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<BackchannelLogoutUriVO> backchannelLogoutUri = Optional.empty();

  /**
   * El backchannel token delivery mode de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<BackchannelTokenDeliveryModeVO> backchannelTokenDeliveryMode = Optional.empty();

  /**
   * El client credentials scopes de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<ClientCredentialsScopesVO> clientCredentialsScopes = Optional.empty();

  /**
   * El client credentials token ttl seconds de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<ClientCredentialsTokenTtlSecondsVO> clientCredentialsTokenTtlSeconds =
      Optional.empty();

  /**
   * El client name de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<ClientNameVO> clientName = Optional.empty();

  /**
   * El client uri de oauth client
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
   * El default acr values de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<DefaultAcrValuesVO> defaultAcrValues = Optional.empty();

  /**
   * El default max age de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<DefaultMaxAgeVO> defaultMaxAge = Optional.empty();

  /**
   * El dpop bound access tokens de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<DpopBoundAccessTokensVO> dpopBoundAccessTokens = Optional.empty();

  /**
   * El dynamically registered de oauth client
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
   * El frontchannel logout session required de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<FrontchannelLogoutSessionRequiredVO> frontchannelLogoutSessionRequired =
      Optional.empty();

  /**
   * El frontchannel logout uri de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<FrontchannelLogoutUriVO> frontchannelLogoutUri = Optional.empty();

  /**
   * El grant types json de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<GrantTypesJsonVO> grantTypesJson = Optional.empty();

  /**
   * El id token encrypted response alg de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<IdTokenEncryptedResponseAlgVO> idTokenEncryptedResponseAlg = Optional.empty();

  /**
   * El id token encrypted response enc de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<IdTokenEncryptedResponseEncVO> idTokenEncryptedResponseEnc = Optional.empty();

  /**
   * El id token signed response alg de oauth client
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
  private Optional<IntrospectionAllowedVO> introspectionAllowed = Optional.empty();

  /**
   * El jwks json de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<JwksJsonVO> jwksJson = Optional.empty();

  /**
   * El jwks uri de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<JwksUriVO> jwksUri = Optional.empty();

  /**
   * El logo uri de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<LogoUriVO> logoUri = Optional.empty();

  /**
   * El policy uri de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<PolicyUriVO> policyUri = Optional.empty();

  /**
   * El registered at de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<RegisteredAtVO> registeredAt = Optional.empty();

  /**
   * El registration access token de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<RegistrationAccessTokenVO> registrationAccessToken = Optional.empty();

  /**
   * El request object signing alg de oauth client
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
   * El require pushed authorization requests de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<RequirePushedAuthorizationRequestsVO> requirePushedAuthorizationRequests =
      Optional.empty();

  /**
   * El response types json de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<ResponseTypesJsonVO> responseTypesJson = Optional.empty();

  /**
   * El secret oauth de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<SecretOauthVO> secretOauth = Optional.empty();

  /**
   * El sector identifier uri de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<SectorIdentifierUriVO> sectorIdentifierUri = Optional.empty();

  /**
   * El subject type de oauth client
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
   * El tls client auth subject dn de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TlsClientAuthSubjectDnVO> tlsClientAuthSubjectDn = Optional.empty();

  /**
   * El tls client certificate bound access tokens de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TlsClientCertificateBoundAccessTokensVO> tlsClientCertificateBoundAccessTokens =
      Optional.empty();

  /**
   * El token endpoint auth method de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TokenEndpointAuthMethodVO> tokenEndpointAuthMethod = Optional.empty();

  /**
   * El token endpoint auth signing alg de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<TokenEndpointAuthSigningAlgVO> tokenEndpointAuthSigningAlg = Optional.empty();

  /**
   * El tos uri de oauth client
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
   * El userinfo encrypted response alg de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<UserinfoEncryptedResponseAlgVO> userinfoEncryptedResponseAlg = Optional.empty();

  /**
   * El userinfo encrypted response enc de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<UserinfoEncryptedResponseEncVO> userinfoEncryptedResponseEnc = Optional.empty();

  /**
   * El userinfo signed response alg de oauth client
   *
   * @autogenerated VisibleProjectionGenerator
   */
  @Builder.Default
  private Optional<UserinfoSignedResponseAlgVO> userinfoSignedResponseAlg = Optional.empty();

  /**
   * Campo con el número de version de oauth client para controlar bloqueos optimistas
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
  public OauthClientVisibleProjection(final OauthClient entity) {
    uid = Optional.of(entity.getUidValue());
    code = Optional.of(entity.getCodeValue());
    tenant = Optional.of(entity.getTenantValue());
    allowAllScopes = Optional.of(entity.getAllowAllScopesValue());
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
    registrationAccessToken = Optional.of(entity.getRegistrationAccessTokenValue());
    defaultAcrValues = Optional.of(entity.getDefaultAcrValuesValue());
    defaultMaxAge = Optional.of(entity.getDefaultMaxAgeValue());
    sectorIdentifierUri = Optional.of(entity.getSectorIdentifierUriValue());
    allowedLoginRedirects = Optional.of(entity.getAllowedLoginRedirectsValue());
    allowedLogoutRedirects = Optional.of(entity.getAllowedLogoutRedirectsValue());
    allowedAudiences = Optional.of(entity.getAllowedAudiencesValue());
    grantTypesJson = Optional.of(entity.getGrantTypesJsonValue());
    introspectionAllowed = Optional.of(entity.getIntrospectionAllowedValue());
    requirePushedAuthorizationRequests =
        Optional.of(entity.getRequirePushedAuthorizationRequestsValue());
    requirePkce = Optional.of(entity.getRequirePkceValue());
    backchannelLogoutUri = Optional.of(entity.getBackchannelLogoutUriValue());
    backchannelLogoutSessionRequired =
        Optional.of(entity.getBackchannelLogoutSessionRequiredValue());
    frontchannelLogoutUri = Optional.of(entity.getFrontchannelLogoutUriValue());
    frontchannelLogoutSessionRequired =
        Optional.of(entity.getFrontchannelLogoutSessionRequiredValue());
    enabled = Optional.of(entity.getEnabledValue());
    clientName = Optional.of(entity.getClientNameValue());
    logoUri = Optional.of(entity.getLogoUriValue());
    clientUri = Optional.of(entity.getClientUriValue());
    policyUri = Optional.of(entity.getPolicyUriValue());
    tosUri = Optional.of(entity.getTosUriValue());
    responseTypesJson = Optional.of(entity.getResponseTypesJsonValue());
    dynamicallyRegistered = Optional.of(entity.getDynamicallyRegisteredValue());
    registeredAt = Optional.of(entity.getRegisteredAtValue());
    clientCredentialsScopes = Optional.of(entity.getClientCredentialsScopesValue());
    clientCredentialsTokenTtlSeconds =
        Optional.of(entity.getClientCredentialsTokenTtlSecondsValue());
    requestObjectSigningAlg = Optional.of(entity.getRequestObjectSigningAlgValue());
    jwksUri = Optional.of(entity.getJwksUriValue());
    jwksJson = Optional.of(entity.getJwksJsonValue());
    version = Optional.of(entity.getVersionValue());
  }

  /**
   * Create a reference for oauth client with his inner assigned value.
   *
   * @autogenerated VisibleProjectionGenerator
   * @throws NoSuchElementException If no uid is set with a value
   * @return A reference if the direct reference build from stored value, otherwise an exception
   *         will be thrown.
   */
  public OauthClientReference asReference() throws NoSuchElementException {
    return uid.map(UidVO::getUid).map(OauthClientReference::of).orElseThrow();
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
   * Inform for a possible change propolsal in AllowedLoginRedirects
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for AllowedLoginRedirects, otherwise the value for
   *         AllowedLoginRedirects
   */
  public Optional<AllowedLoginRedirectsVO> getAllowedLoginRedirects() {
    return allowedLoginRedirects;
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
   * Inform for a possible change propolsal in BackchannelLogoutSessionRequired
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for BackchannelLogoutSessionRequired, otherwise
   *         the value for BackchannelLogoutSessionRequired
   */
  public Optional<BackchannelLogoutSessionRequiredVO> getBackchannelLogoutSessionRequired() {
    return backchannelLogoutSessionRequired;
  }

  /**
   * Inform for a possible change propolsal in BackchannelLogoutUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for BackchannelLogoutUri, otherwise the value for
   *         BackchannelLogoutUri
   */
  public Optional<BackchannelLogoutUriVO> getBackchannelLogoutUri() {
    return backchannelLogoutUri;
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
   * Inform for a possible change propolsal in ClientCredentialsScopes
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for ClientCredentialsScopes, otherwise the value
   *         for ClientCredentialsScopes
   */
  public Optional<ClientCredentialsScopesVO> getClientCredentialsScopes() {
    return clientCredentialsScopes;
  }

  /**
   * Inform for a possible change propolsal in ClientCredentialsTokenTtlSeconds
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for ClientCredentialsTokenTtlSeconds, otherwise
   *         the value for ClientCredentialsTokenTtlSeconds
   */
  public Optional<ClientCredentialsTokenTtlSecondsVO> getClientCredentialsTokenTtlSeconds() {
    return clientCredentialsTokenTtlSeconds;
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
   * Inform for a possible change propolsal in FrontchannelLogoutSessionRequired
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for FrontchannelLogoutSessionRequired, otherwise
   *         the value for FrontchannelLogoutSessionRequired
   */
  public Optional<FrontchannelLogoutSessionRequiredVO> getFrontchannelLogoutSessionRequired() {
    return frontchannelLogoutSessionRequired;
  }

  /**
   * Inform for a possible change propolsal in FrontchannelLogoutUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for FrontchannelLogoutUri, otherwise the value for
   *         FrontchannelLogoutUri
   */
  public Optional<FrontchannelLogoutUriVO> getFrontchannelLogoutUri() {
    return frontchannelLogoutUri;
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
   * Inform for a possible change propolsal in IntrospectionAllowed
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for IntrospectionAllowed, otherwise the value for
   *         IntrospectionAllowed
   */
  public Optional<IntrospectionAllowedVO> getIntrospectionAllowed() {
    return introspectionAllowed;
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
   * Inform for a possible change propolsal in PolicyUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for PolicyUri, otherwise the value for PolicyUri
   */
  public Optional<PolicyUriVO> getPolicyUri() {
    return policyUri;
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
   * Inform for a possible change propolsal in RegistrationAccessToken
   *
   * @autogenerated VisibleProjectionGenerator
   * @return empty if there is no change proposal for RegistrationAccessToken, otherwise the value
   *         for RegistrationAccessToken
   */
  public Optional<RegistrationAccessTokenVO> getRegistrationAccessToken() {
    return registrationAccessToken;
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
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setAllowAllScopes(final Boolean value) {
    this.allowAllScopes = Optional.of(AllowAllScopesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedAudiences to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setAllowedAudiences(final List<AllowedAudiences> value) {
    this.allowedAudiences = Optional.of(AllowedAudiencesVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedLoginRedirects to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setAllowedLoginRedirects(
      final List<AllowedLoginRedirects> value) {
    this.allowedLoginRedirects =
        Optional.of(AllowedLoginRedirectsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedLogoutRedirects to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setAllowedLogoutRedirects(
      final List<AllowedLogoutRedirects> value) {
    this.allowedLogoutRedirects =
        Optional.of(AllowedLogoutRedirectsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AuthorizationDetailsTypes to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setAuthorizationDetailsTypes(final String value) {
    this.authorizationDetailsTypes = Optional.of(AuthorizationDetailsTypesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AuthorizationSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setAuthorizationSignedResponseAlg(final String value) {
    this.authorizationSignedResponseAlg = Optional.of(AuthorizationSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelClientNotificationEndpoint to be apply for the
   * aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setBackchannelClientNotificationEndpoint(final String value) {
    this.backchannelClientNotificationEndpoint =
        Optional.of(BackchannelClientNotificationEndpointVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelLogoutSessionRequired to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setBackchannelLogoutSessionRequired(final Boolean value) {
    this.backchannelLogoutSessionRequired =
        Optional.of(BackchannelLogoutSessionRequiredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelLogoutUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setBackchannelLogoutUri(final String value) {
    this.backchannelLogoutUri = Optional.of(BackchannelLogoutUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelTokenDeliveryMode to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setBackchannelTokenDeliveryMode(
      final OauthClientBackchannelTokenDeliveryModeOptions value) {
    this.backchannelTokenDeliveryMode = Optional.of(BackchannelTokenDeliveryModeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientCredentialsScopes to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setClientCredentialsScopes(final String value) {
    this.clientCredentialsScopes = Optional.of(ClientCredentialsScopesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientCredentialsTokenTtlSeconds to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setClientCredentialsTokenTtlSeconds(final Integer value) {
    this.clientCredentialsTokenTtlSeconds =
        Optional.of(ClientCredentialsTokenTtlSecondsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientName to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setClientName(final String value) {
    this.clientName = Optional.of(ClientNameVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setClientUri(final String value) {
    this.clientUri = Optional.of(ClientUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Code to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setCode(final String value) {
    this.code = Optional.of(CodeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DefaultAcrValues to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setDefaultAcrValues(final String value) {
    this.defaultAcrValues = Optional.of(DefaultAcrValuesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DefaultMaxAge to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setDefaultMaxAge(final Integer value) {
    this.defaultMaxAge = Optional.of(DefaultMaxAgeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DpopBoundAccessTokens to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setDpopBoundAccessTokens(final Boolean value) {
    this.dpopBoundAccessTokens = Optional.of(DpopBoundAccessTokensVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DynamicallyRegistered to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setDynamicallyRegistered(final Boolean value) {
    this.dynamicallyRegistered = Optional.of(DynamicallyRegisteredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Enabled to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for FrontchannelLogoutSessionRequired to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setFrontchannelLogoutSessionRequired(final Boolean value) {
    this.frontchannelLogoutSessionRequired =
        Optional.of(FrontchannelLogoutSessionRequiredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for FrontchannelLogoutUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setFrontchannelLogoutUri(final String value) {
    this.frontchannelLogoutUri = Optional.of(FrontchannelLogoutUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for GrantTypesJson to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setGrantTypesJson(final String value) {
    this.grantTypesJson = Optional.of(GrantTypesJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenEncryptedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setIdTokenEncryptedResponseAlg(final String value) {
    this.idTokenEncryptedResponseAlg = Optional.of(IdTokenEncryptedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenEncryptedResponseEnc to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setIdTokenEncryptedResponseEnc(final String value) {
    this.idTokenEncryptedResponseEnc = Optional.of(IdTokenEncryptedResponseEncVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setIdTokenSignedResponseAlg(
      final OauthClientIdTokenSignedResponseAlgOptions value) {
    this.idTokenSignedResponseAlg = Optional.of(IdTokenSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IntrospectionAllowed to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setIntrospectionAllowed(final Boolean value) {
    this.introspectionAllowed = Optional.of(IntrospectionAllowedVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for JwksJson to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setJwksJson(final String value) {
    this.jwksJson = Optional.of(JwksJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for JwksUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setJwksUri(final String value) {
    this.jwksUri = Optional.of(JwksUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for LogoUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setLogoUri(final String value) {
    this.logoUri = Optional.of(LogoUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for PolicyUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setPolicyUri(final String value) {
    this.policyUri = Optional.of(PolicyUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RegisteredAt to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setRegisteredAt(final OffsetDateTime value) {
    this.registeredAt = Optional.of(RegisteredAtVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RegistrationAccessToken to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setRegistrationAccessToken(final String value) {
    this.registrationAccessToken = Optional.of(RegistrationAccessTokenVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for RequestObjectSigningAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setRequestObjectSigningAlg(final String value) {
    this.requestObjectSigningAlg = Optional.of(RequestObjectSigningAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RequirePkce to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setRequirePkce(final Boolean value) {
    this.requirePkce = Optional.of(RequirePkceVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RequirePushedAuthorizationRequests to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setRequirePushedAuthorizationRequests(final Boolean value) {
    this.requirePushedAuthorizationRequests =
        Optional.of(RequirePushedAuthorizationRequestsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ResponseTypesJson to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setResponseTypesJson(final String value) {
    this.responseTypesJson = Optional.of(ResponseTypesJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SecretOauth to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setSecretOauth(final String value) {
    this.secretOauth = Optional.of(SecretOauthVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for SectorIdentifierUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setSectorIdentifierUri(final String value) {
    this.sectorIdentifierUri = Optional.of(SectorIdentifierUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SubjectType to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setSubjectType(final OauthClientSubjectTypeOptions value) {
    this.subjectType = Optional.of(SubjectTypeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Tenant to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TlsClientAuthSubjectDn to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setTlsClientAuthSubjectDn(final String value) {
    this.tlsClientAuthSubjectDn = Optional.of(TlsClientAuthSubjectDnVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TlsClientCertificateBoundAccessTokens to be apply for the
   * aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setTlsClientCertificateBoundAccessTokens(
      final Boolean value) {
    this.tlsClientCertificateBoundAccessTokens =
        Optional.of(TlsClientCertificateBoundAccessTokensVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TokenEndpointAuthMethod to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setTokenEndpointAuthMethod(
      final OauthClientTokenEndpointAuthMethodOptions value) {
    this.tokenEndpointAuthMethod = Optional.of(TokenEndpointAuthMethodVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TokenEndpointAuthSigningAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setTokenEndpointAuthSigningAlg(final String value) {
    this.tokenEndpointAuthSigningAlg = Optional.of(TokenEndpointAuthSigningAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TosUri to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setTosUri(final String value) {
    this.tosUri = Optional.of(TosUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoEncryptedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setUserinfoEncryptedResponseAlg(final String value) {
    this.userinfoEncryptedResponseAlg = Optional.of(UserinfoEncryptedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoEncryptedResponseEnc to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setUserinfoEncryptedResponseEnc(final String value) {
    this.userinfoEncryptedResponseEnc = Optional.of(UserinfoEncryptedResponseEncVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setUserinfoSignedResponseAlg(final String value) {
    this.userinfoSignedResponseAlg = Optional.of(UserinfoSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated VisibleProjectionGenerator
   * @param value The proposal value for OauthClientVisibleProjection
   * @return self instance to enable a flow code
   */
  public OauthClientVisibleProjection setVersion(final Integer value) {
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
    if ("registrationAccessToken".equals(field)) {
      this.unsetRegistrationAccessToken();
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
    if ("allowedLoginRedirects".equals(field)) {
      this.unsetAllowedLoginRedirects();
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
    if ("introspectionAllowed".equals(field)) {
      this.unsetIntrospectionAllowed();
    }
    if ("requirePushedAuthorizationRequests".equals(field)) {
      this.unsetRequirePushedAuthorizationRequests();
    }
    if ("requirePkce".equals(field)) {
      this.unsetRequirePkce();
    }
    if ("backchannelLogoutUri".equals(field)) {
      this.unsetBackchannelLogoutUri();
    }
    if ("backchannelLogoutSessionRequired".equals(field)) {
      this.unsetBackchannelLogoutSessionRequired();
    }
    if ("frontchannelLogoutUri".equals(field)) {
      this.unsetFrontchannelLogoutUri();
    }
    if ("frontchannelLogoutSessionRequired".equals(field)) {
      this.unsetFrontchannelLogoutSessionRequired();
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
    if ("clientCredentialsScopes".equals(field)) {
      this.unsetClientCredentialsScopes();
    }
    if ("clientCredentialsTokenTtlSeconds".equals(field)) {
      this.unsetClientCredentialsTokenTtlSeconds();
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
  public OauthClientVisibleProjection unsetAllowAllScopes() {
    this.allowAllScopes = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AllowedAudiences
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetAllowedAudiences() {
    this.allowedAudiences = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AllowedLoginRedirects
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetAllowedLoginRedirects() {
    this.allowedLoginRedirects = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AllowedLogoutRedirects
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetAllowedLogoutRedirects() {
    this.allowedLogoutRedirects = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AuthorizationDetailsTypes
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetAuthorizationDetailsTypes() {
    this.authorizationDetailsTypes = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for AuthorizationSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetAuthorizationSignedResponseAlg() {
    this.authorizationSignedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for BackchannelClientNotificationEndpoint
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetBackchannelClientNotificationEndpoint() {
    this.backchannelClientNotificationEndpoint = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for BackchannelLogoutSessionRequired
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetBackchannelLogoutSessionRequired() {
    this.backchannelLogoutSessionRequired = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for BackchannelLogoutUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetBackchannelLogoutUri() {
    this.backchannelLogoutUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for BackchannelTokenDeliveryMode
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetBackchannelTokenDeliveryMode() {
    this.backchannelTokenDeliveryMode = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for ClientCredentialsScopes
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetClientCredentialsScopes() {
    this.clientCredentialsScopes = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for ClientCredentialsTokenTtlSeconds
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetClientCredentialsTokenTtlSeconds() {
    this.clientCredentialsTokenTtlSeconds = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for ClientName
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetClientName() {
    this.clientName = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for ClientUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetClientUri() {
    this.clientUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Code
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetCode() {
    this.code = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for DefaultAcrValues
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetDefaultAcrValues() {
    this.defaultAcrValues = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for DefaultMaxAge
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetDefaultMaxAge() {
    this.defaultMaxAge = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for DpopBoundAccessTokens
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetDpopBoundAccessTokens() {
    this.dpopBoundAccessTokens = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for DynamicallyRegistered
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetDynamicallyRegistered() {
    this.dynamicallyRegistered = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Enabled
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetEnabled() {
    this.enabled = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for FrontchannelLogoutSessionRequired
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetFrontchannelLogoutSessionRequired() {
    this.frontchannelLogoutSessionRequired = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for FrontchannelLogoutUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetFrontchannelLogoutUri() {
    this.frontchannelLogoutUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for GrantTypesJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetGrantTypesJson() {
    this.grantTypesJson = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for IdTokenEncryptedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetIdTokenEncryptedResponseAlg() {
    this.idTokenEncryptedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for IdTokenEncryptedResponseEnc
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetIdTokenEncryptedResponseEnc() {
    this.idTokenEncryptedResponseEnc = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for IdTokenSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetIdTokenSignedResponseAlg() {
    this.idTokenSignedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for IntrospectionAllowed
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetIntrospectionAllowed() {
    this.introspectionAllowed = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for JwksJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetJwksJson() {
    this.jwksJson = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for JwksUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetJwksUri() {
    this.jwksUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for LogoUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetLogoUri() {
    this.logoUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for PolicyUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetPolicyUri() {
    this.policyUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RegisteredAt
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetRegisteredAt() {
    this.registeredAt = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RegistrationAccessToken
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetRegistrationAccessToken() {
    this.registrationAccessToken = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RequestObjectSigningAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetRequestObjectSigningAlg() {
    this.requestObjectSigningAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RequirePkce
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetRequirePkce() {
    this.requirePkce = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for RequirePushedAuthorizationRequests
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetRequirePushedAuthorizationRequests() {
    this.requirePushedAuthorizationRequests = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for ResponseTypesJson
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetResponseTypesJson() {
    this.responseTypesJson = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for SecretOauth
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetSecretOauth() {
    this.secretOauth = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for SectorIdentifierUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetSectorIdentifierUri() {
    this.sectorIdentifierUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for SubjectType
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetSubjectType() {
    this.subjectType = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Tenant
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetTenant() {
    this.tenant = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TlsClientAuthSubjectDn
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetTlsClientAuthSubjectDn() {
    this.tlsClientAuthSubjectDn = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TlsClientCertificateBoundAccessTokens
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetTlsClientCertificateBoundAccessTokens() {
    this.tlsClientCertificateBoundAccessTokens = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TokenEndpointAuthMethod
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetTokenEndpointAuthMethod() {
    this.tokenEndpointAuthMethod = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TokenEndpointAuthSigningAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetTokenEndpointAuthSigningAlg() {
    this.tokenEndpointAuthSigningAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for TosUri
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetTosUri() {
    this.tosUri = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Uid
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for UserinfoEncryptedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetUserinfoEncryptedResponseAlg() {
    this.userinfoEncryptedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for UserinfoEncryptedResponseEnc
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetUserinfoEncryptedResponseEnc() {
    this.userinfoEncryptedResponseEnc = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for UserinfoSignedResponseAlg
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetUserinfoSignedResponseAlg() {
    this.userinfoSignedResponseAlg = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Version
   *
   * @autogenerated VisibleProjectionGenerator
   * @return self instance to enable a flow code.
   */
  public OauthClientVisibleProjection unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
