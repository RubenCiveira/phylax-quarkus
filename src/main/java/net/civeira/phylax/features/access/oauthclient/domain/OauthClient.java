package net.civeira.phylax.features.access.oauthclient.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;
import lombok.experimental.Delegate;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.ConstraintFailList;
import net.civeira.phylax.features.access.oauthclient.domain.event.OauthClientCreateEvent;
import net.civeira.phylax.features.access.oauthclient.domain.event.OauthClientDeleteEvent;
import net.civeira.phylax.features.access.oauthclient.domain.event.OauthClientDisableEvent;
import net.civeira.phylax.features.access.oauthclient.domain.event.OauthClientEnableEvent;
import net.civeira.phylax.features.access.oauthclient.domain.event.OauthClientEvent;
import net.civeira.phylax.features.access.oauthclient.domain.event.OauthClientUpdateEvent;
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

/**
 * Las diferentes aplicaciones que pueden usar el sistema de acceso. Dado qua la app va en la
 * petición de login, podemos obtener la cuenta asociado a traves del proyecto
 */
@Builder
@Getter
@With
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OauthClient implements OauthClientRef {

  /**
   * Prepare a new oauth client with the provided values
   *
   * @autogenerated EntityGenerator
   * @param change A set of values to create a new oauth client
   * @throws ConstraintException If there are some constraints fails on the changes.
   * @return A well formed oauth client.
   */
  public static OauthClient create(final OauthClientChangeSet change) throws ConstraintException {
    change.enabled(false);
    OauthClient instance = new OauthClient(change, Optional.empty());
    instance.addEvent(OauthClientCreateEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * El allow all scopes de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private AllowAllScopesVO allowAllScopesValue = AllowAllScopesVO.nullValue();

  /**
   * El allowed audiences de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private AllowedAudiencesVO allowedAudiencesValue;

  /**
   * El allowed login redirects de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private AllowedLoginRedirectsVO allowedLoginRedirectsValue;

  /**
   * El allowed logout redirects de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private AllowedLogoutRedirectsVO allowedLogoutRedirectsValue;

  /**
   * El authorization details types de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private AuthorizationDetailsTypesVO authorizationDetailsTypesValue =
      AuthorizationDetailsTypesVO.nullValue();

  /**
   * El authorization signed response alg de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private AuthorizationSignedResponseAlgVO authorizationSignedResponseAlgValue =
      AuthorizationSignedResponseAlgVO.nullValue();

  /**
   * El backchannel client notification endpoint de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BackchannelClientNotificationEndpointVO backchannelClientNotificationEndpointValue =
      BackchannelClientNotificationEndpointVO.nullValue();

  /**
   * El backchannel logout session required de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BackchannelLogoutSessionRequiredVO backchannelLogoutSessionRequiredValue =
      BackchannelLogoutSessionRequiredVO.nullValue();

  /**
   * El backchannel logout uri de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BackchannelLogoutUriVO backchannelLogoutUriValue = BackchannelLogoutUriVO.nullValue();

  /**
   * El backchannel token delivery mode de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BackchannelTokenDeliveryModeVO backchannelTokenDeliveryModeValue =
      BackchannelTokenDeliveryModeVO.nullValue();

  /**
   * El client credentials scopes de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ClientCredentialsScopesVO clientCredentialsScopesValue =
      ClientCredentialsScopesVO.nullValue();

  /**
   * El client credentials token ttl seconds de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private ClientCredentialsTokenTtlSecondsVO clientCredentialsTokenTtlSecondsValue;

  /**
   * El client name de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ClientNameVO clientNameValue = ClientNameVO.nullValue();

  /**
   * El client uri de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ClientUriVO clientUriValue = ClientUriVO.nullValue();

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private CodeVO codeValue;

  /**
   * El default acr values de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private DefaultAcrValuesVO defaultAcrValuesValue = DefaultAcrValuesVO.nullValue();

  /**
   * El default max age de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private DefaultMaxAgeVO defaultMaxAgeValue = DefaultMaxAgeVO.nullValue();

  /**
   * El dpop bound access tokens de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private DpopBoundAccessTokensVO dpopBoundAccessTokensValue;

  /**
   * El dynamically registered de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private DynamicallyRegisteredVO dynamicallyRegisteredValue = DynamicallyRegisteredVO.nullValue();

  /**
   * The indicator to allow the account be used without deleting it
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private EnabledVO enabledValue;

  /**
   * El frontchannel logout session required de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private FrontchannelLogoutSessionRequiredVO frontchannelLogoutSessionRequiredValue =
      FrontchannelLogoutSessionRequiredVO.nullValue();

  /**
   * El frontchannel logout uri de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private FrontchannelLogoutUriVO frontchannelLogoutUriValue = FrontchannelLogoutUriVO.nullValue();

  /**
   * El grant types json de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private GrantTypesJsonVO grantTypesJsonValue = GrantTypesJsonVO.nullValue();

  /**
   * El id token encrypted response alg de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private IdTokenEncryptedResponseAlgVO idTokenEncryptedResponseAlgValue =
      IdTokenEncryptedResponseAlgVO.nullValue();

  /**
   * El id token encrypted response enc de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private IdTokenEncryptedResponseEncVO idTokenEncryptedResponseEncValue =
      IdTokenEncryptedResponseEncVO.nullValue();

  /**
   * El id token signed response alg de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private IdTokenSignedResponseAlgVO idTokenSignedResponseAlgValue =
      IdTokenSignedResponseAlgVO.nullValue();

  /**
   * If true, this client is allowed to call the token introspection endpoint (POST /introspect) to
   * validate access tokens. Resource servers must authenticate at the introspection endpoint using
   * their client credentials.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private IntrospectionAllowedVO introspectionAllowedValue;

  /**
   * El jwks json de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private JwksJsonVO jwksJsonValue = JwksJsonVO.nullValue();

  /**
   * El jwks uri de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private JwksUriVO jwksUriValue = JwksUriVO.nullValue();

  /**
   * El logo uri de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private LogoUriVO logoUriValue = LogoUriVO.nullValue();

  /**
   * El policy uri de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private PolicyUriVO policyUriValue = PolicyUriVO.nullValue();

  /**
   * List of events
   *
   * @autogenerated EntityGenerator
   */
  @Builder.Default
  private List<OauthClientEvent> recordedEvents = List.of();

  /**
   * El registered at de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private RegisteredAtVO registeredAtValue = RegisteredAtVO.nullValue();

  /**
   * El registration access token de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private RegistrationAccessTokenVO registrationAccessTokenValue =
      RegistrationAccessTokenVO.nullValue();

  /**
   * El request object signing alg de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private RequestObjectSigningAlgVO requestObjectSigningAlgValue =
      RequestObjectSigningAlgVO.nullValue();

  /**
   * If true, every authorization code request from this client must include code_challenge and
   * code_challenge_method=S256. For clients with public-allow=true, PKCE is always required
   * regardless of this flag.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private RequirePkceVO requirePkceValue;

  /**
   * El require pushed authorization requests de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private RequirePushedAuthorizationRequestsVO requirePushedAuthorizationRequestsValue;

  /**
   * El response types json de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ResponseTypesJsonVO responseTypesJsonValue = ResponseTypesJsonVO.nullValue();

  /**
   * El secret oauth de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private SecretOauthVO secretOauthValue = SecretOauthVO.nullValue();

  /**
   * El sector identifier uri de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private SectorIdentifierUriVO sectorIdentifierUriValue = SectorIdentifierUriVO.nullValue();

  /**
   * El subject type de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private SubjectTypeVO subjectTypeValue;

  /**
   * Owner tenant. Null means a global client, manageable only by root.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TenantVO tenantValue = TenantVO.nullValue();

  /**
   * El tls client auth subject dn de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TlsClientAuthSubjectDnVO tlsClientAuthSubjectDnValue =
      TlsClientAuthSubjectDnVO.nullValue();

  /**
   * El tls client certificate bound access tokens de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private TlsClientCertificateBoundAccessTokensVO tlsClientCertificateBoundAccessTokensValue;

  /**
   * El token endpoint auth method de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private TokenEndpointAuthMethodVO tokenEndpointAuthMethodValue;

  /**
   * El token endpoint auth signing alg de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TokenEndpointAuthSigningAlgVO tokenEndpointAuthSigningAlgValue =
      TokenEndpointAuthSigningAlgVO.nullValue();

  /**
   * El tos uri de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TosUriVO tosUriValue = TosUriVO.nullValue();

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @EqualsAndHashCode.Include
  @NonNull
  private UidVO uidValue;

  /**
   * El userinfo encrypted response alg de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private UserinfoEncryptedResponseAlgVO userinfoEncryptedResponseAlgValue =
      UserinfoEncryptedResponseAlgVO.nullValue();

  /**
   * El userinfo encrypted response enc de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private UserinfoEncryptedResponseEncVO userinfoEncryptedResponseEncValue =
      UserinfoEncryptedResponseEncVO.nullValue();

  /**
   * El userinfo signed response alg de oauth client
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private UserinfoSignedResponseAlgVO userinfoSignedResponseAlgValue =
      UserinfoSignedResponseAlgVO.nullValue();

  /**
   * Campo con el número de version de oauth client para controlar bloqueos optimistas
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private VersionVO versionValue = VersionVO.nullValue();

  /**
   * @autogenerated EntityGenerator
   * @param attribute
   * @param previous
   */
  private OauthClient(final OauthClientChangeSet attribute, final Optional<OauthClient> previous) {
    ConstraintFailList list = new ConstraintFailList();
    this.uidValue = attribute.readUid(previous, list);
    this.codeValue = attribute.readCode(previous, list);
    this.tenantValue = attribute.readTenant(previous, list);
    this.allowAllScopesValue = attribute.readAllowAllScopes(previous, list);
    this.tokenEndpointAuthMethodValue = attribute.readTokenEndpointAuthMethod(previous, list);
    this.tokenEndpointAuthSigningAlgValue =
        attribute.readTokenEndpointAuthSigningAlg(previous, list);
    this.dpopBoundAccessTokensValue = attribute.readDpopBoundAccessTokens(previous, list);
    this.idTokenSignedResponseAlgValue = attribute.readIdTokenSignedResponseAlg(previous, list);
    this.tlsClientAuthSubjectDnValue = attribute.readTlsClientAuthSubjectDn(previous, list);
    this.tlsClientCertificateBoundAccessTokensValue =
        attribute.readTlsClientCertificateBoundAccessTokens(previous, list);
    this.authorizationSignedResponseAlgValue =
        attribute.readAuthorizationSignedResponseAlg(previous, list);
    this.backchannelTokenDeliveryModeValue =
        attribute.readBackchannelTokenDeliveryMode(previous, list);
    this.backchannelClientNotificationEndpointValue =
        attribute.readBackchannelClientNotificationEndpoint(previous, list);
    this.subjectTypeValue = attribute.readSubjectType(previous, list);
    this.idTokenEncryptedResponseAlgValue =
        attribute.readIdTokenEncryptedResponseAlg(previous, list);
    this.idTokenEncryptedResponseEncValue =
        attribute.readIdTokenEncryptedResponseEnc(previous, list);
    this.userinfoEncryptedResponseAlgValue =
        attribute.readUserinfoEncryptedResponseAlg(previous, list);
    this.userinfoEncryptedResponseEncValue =
        attribute.readUserinfoEncryptedResponseEnc(previous, list);
    this.authorizationDetailsTypesValue = attribute.readAuthorizationDetailsTypes(previous, list);
    this.userinfoSignedResponseAlgValue = attribute.readUserinfoSignedResponseAlg(previous, list);
    this.secretOauthValue = attribute.readSecretOauth(previous, list);
    this.registrationAccessTokenValue = attribute.readRegistrationAccessToken(previous, list);
    this.defaultAcrValuesValue = attribute.readDefaultAcrValues(previous, list);
    this.defaultMaxAgeValue = attribute.readDefaultMaxAge(previous, list);
    this.sectorIdentifierUriValue = attribute.readSectorIdentifierUri(previous, list);
    this.allowedLoginRedirectsValue = attribute.readAllowedLoginRedirects(previous, list);
    this.allowedLogoutRedirectsValue = attribute.readAllowedLogoutRedirects(previous, list);
    this.allowedAudiencesValue = attribute.readAllowedAudiences(previous, list);
    this.grantTypesJsonValue = attribute.readGrantTypesJson(previous, list);
    this.introspectionAllowedValue = attribute.readIntrospectionAllowed(previous, list);
    this.requirePushedAuthorizationRequestsValue =
        attribute.readRequirePushedAuthorizationRequests(previous, list);
    this.requirePkceValue = attribute.readRequirePkce(previous, list);
    this.backchannelLogoutUriValue = attribute.readBackchannelLogoutUri(previous, list);
    this.backchannelLogoutSessionRequiredValue =
        attribute.readBackchannelLogoutSessionRequired(previous, list);
    this.frontchannelLogoutUriValue = attribute.readFrontchannelLogoutUri(previous, list);
    this.frontchannelLogoutSessionRequiredValue =
        attribute.readFrontchannelLogoutSessionRequired(previous, list);
    this.enabledValue = attribute.readEnabled(previous, list);
    this.clientNameValue = attribute.readClientName(previous, list);
    this.logoUriValue = attribute.readLogoUri(previous, list);
    this.clientUriValue = attribute.readClientUri(previous, list);
    this.policyUriValue = attribute.readPolicyUri(previous, list);
    this.tosUriValue = attribute.readTosUri(previous, list);
    this.responseTypesJsonValue = attribute.readResponseTypesJson(previous, list);
    this.dynamicallyRegisteredValue = attribute.readDynamicallyRegistered(previous, list);
    this.registeredAtValue = attribute.readRegisteredAt(previous, list);
    this.clientCredentialsScopesValue = attribute.readClientCredentialsScopes(previous, list);
    this.clientCredentialsTokenTtlSecondsValue =
        attribute.readClientCredentialsTokenTtlSeconds(previous, list);
    this.requestObjectSigningAlgValue = attribute.readRequestObjectSigningAlg(previous, list);
    this.jwksUriValue = attribute.readJwksUri(previous, list);
    this.jwksJsonValue = attribute.readJwksJson(previous, list);
    this.versionValue = attribute.readVersion(previous, list);
    if (list.hasErrors()) {
      throw new ConstraintException("Invalid values on OauthClient", list);
    }
    this.recordedEvents = previous.map(OauthClient::getRecordedEvents).orElseGet(List::of);
  }

  /**
   * Apply changes to delete a oauth client
   *
   * @autogenerated EntityGenerator
   * @return A instance of oauth client ready to be deleted
   */
  public OauthClient delete() {
    OauthClient instance = new OauthClient(new OauthClientChangeSet(), Optional.of(this));
    instance.addEvent(OauthClientDeleteEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply disable on oauth client.
   *
   * @autogenerated EntityGenerator
   * @return A modified instance of oauth client
   */
  public OauthClient disable() {
    OauthClientChangeSet attr = new OauthClientChangeSet();
    attr.enabled(false);
    OauthClient instance = new OauthClient(attr, Optional.of(this));
    instance.addEvent(OauthClientDisableEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply enable on oauth client.
   *
   * @autogenerated EntityGenerator
   * @return A modified instance of oauth client
   */
  public OauthClient enable() {
    OauthClientChangeSet attr = new OauthClientChangeSet();
    attr.enabled(true);
    OauthClient instance = new OauthClient(attr, Optional.of(this));
    instance.addEvent(OauthClientEnableEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public Map<String, String> toMap() {
    Map<String, String> map = new HashMap<>(51);
    uidToMap(map);
    codeToMap(map);
    tenantToMap(map);
    allowAllScopesToMap(map);
    tokenEndpointAuthMethodToMap(map);
    tokenEndpointAuthSigningAlgToMap(map);
    dpopBoundAccessTokensToMap(map);
    idTokenSignedResponseAlgToMap(map);
    tlsClientAuthSubjectDnToMap(map);
    tlsClientCertificateBoundAccessTokensToMap(map);
    authorizationSignedResponseAlgToMap(map);
    backchannelTokenDeliveryModeToMap(map);
    backchannelClientNotificationEndpointToMap(map);
    subjectTypeToMap(map);
    idTokenEncryptedResponseAlgToMap(map);
    idTokenEncryptedResponseEncToMap(map);
    userinfoEncryptedResponseAlgToMap(map);
    userinfoEncryptedResponseEncToMap(map);
    authorizationDetailsTypesToMap(map);
    userinfoSignedResponseAlgToMap(map);
    secretOauthToMap(map);
    registrationAccessTokenToMap(map);
    defaultAcrValuesToMap(map);
    defaultMaxAgeToMap(map);
    sectorIdentifierUriToMap(map);
    allowedLoginRedirectsToMap(map);
    allowedLogoutRedirectsToMap(map);
    allowedAudiencesToMap(map);
    grantTypesJsonToMap(map);
    introspectionAllowedToMap(map);
    requirePushedAuthorizationRequestsToMap(map);
    requirePkceToMap(map);
    backchannelLogoutUriToMap(map);
    backchannelLogoutSessionRequiredToMap(map);
    frontchannelLogoutUriToMap(map);
    frontchannelLogoutSessionRequiredToMap(map);
    enabledToMap(map);
    clientNameToMap(map);
    logoUriToMap(map);
    clientUriToMap(map);
    policyUriToMap(map);
    tosUriToMap(map);
    responseTypesJsonToMap(map);
    dynamicallyRegisteredToMap(map);
    registeredAtToMap(map);
    clientCredentialsScopesToMap(map);
    clientCredentialsTokenTtlSecondsToMap(map);
    requestObjectSigningAlgToMap(map);
    jwksUriToMap(map);
    jwksJsonToMap(map);
    versionToMap(map);
    return map;
  }

  /**
   * Modify the values for some of the properties of a oauth client
   *
   * @autogenerated EntityGenerator
   * @param change The properties to be modified
   * @throws ConstraintException If there are some constraints fails on the changes.
   * @return A modified instance of oauth client
   */
  public OauthClient update(final OauthClientChangeSet change) throws ConstraintException {
    change.unsetEnabled();
    OauthClient instance = new OauthClient(change, Optional.of(this));
    instance.addEvent(OauthClientUpdateEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public OauthClient withNextVersion() {
    return withVersionValue(VersionVO.from(nextVersion()));
  }

  /**
   * @autogenerated EntityGenerator
   * @param event
   */
  private void addEvent(final OauthClientEvent event) {
    List<OauthClientEvent> events = new ArrayList<>(this.recordedEvents);
    events.add(event);
    this.recordedEvents = List.copyOf(events);
  }
}
