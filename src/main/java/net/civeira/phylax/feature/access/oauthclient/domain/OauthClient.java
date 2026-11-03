package net.civeira.phylax.feature.access.oauthclient.domain;

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
import net.civeira.phylax.feature.access.oauthclient.domain.event.OauthClientCreateEvent;
import net.civeira.phylax.feature.access.oauthclient.domain.event.OauthClientDeleteEvent;
import net.civeira.phylax.feature.access.oauthclient.domain.event.OauthClientDisableEvent;
import net.civeira.phylax.feature.access.oauthclient.domain.event.OauthClientEnableEvent;
import net.civeira.phylax.feature.access.oauthclient.domain.event.OauthClientEvent;
import net.civeira.phylax.feature.access.oauthclient.domain.event.OauthClientUpdateEvent;
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

/**
 * Application that signs users in or requests tokens through OAuth 2.0 / OpenID Connect. The client
 * is sent in every authorization request, so the tenant is resolved from it.
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
   * Lets the client request any scope instead of only the standard OpenID Connect ones.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private AllowAllScopesVO allowAllScopesValue = AllowAllScopesVO.nullValue();

  /**
   * Audiences (resource servers or resource URIs) the client may request tokens for.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private AllowedAudiencesVO allowedAudiencesValue;

  /**
   * Redirect URIs the client may use after sign-in.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private AllowedLoginRedirectsVO allowedLoginRedirectsValue;

  /**
   * Redirect URIs the client may use after sign-out.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private AllowedLogoutRedirectsVO allowedLogoutRedirectsValue;

  /**
   * JSON list of the authorization_details types the client can request (RFC 9396).
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private AuthorizationDetailsTypesVO authorizationDetailsTypesValue =
      AuthorizationDetailsTypesVO.nullValue();

  /**
   * Algorithm used to sign JWT-secured authorization responses (JARM) for this client.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private AuthorizationSignedResponseAlgVO authorizationSignedResponseAlgValue =
      AuthorizationSignedResponseAlgVO.nullValue();

  /**
   * Endpoint of the client that receives the CIBA ping notifications.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BackchannelClientNotificationEndpointVO backchannelClientNotificationEndpointValue =
      BackchannelClientNotificationEndpointVO.nullValue();

  /**
   * Includes the session identifier (sid) in the back-channel logout token.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BackchannelLogoutSessionRequiredVO backchannelLogoutSessionRequiredValue =
      BackchannelLogoutSessionRequiredVO.nullValue();

  /**
   * Endpoint of the client that receives back-channel logout notifications.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BackchannelLogoutUriVO backchannelLogoutUriValue = BackchannelLogoutUriVO.nullValue();

  /**
   * How tokens are delivered in the CIBA flow: the client polls the token endpoint, or receives a
   * ping.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BackchannelTokenDeliveryModeVO backchannelTokenDeliveryModeValue =
      BackchannelTokenDeliveryModeVO.nullValue();

  /**
   * Scopes granted to the client in the client credentials flow.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ClientCredentialsScopesVO clientCredentialsScopesValue =
      ClientCredentialsScopesVO.nullValue();

  /**
   * Lifetime, in seconds, of the access tokens issued in the client credentials flow.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private ClientCredentialsTokenTtlSecondsVO clientCredentialsTokenTtlSecondsValue;

  /**
   * Name of the client shown to users, for example on the consent screen.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ClientNameVO clientNameValue = ClientNameVO.nullValue();

  /**
   * URL of the home page of the client.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ClientUriVO clientUriValue = ClientUriVO.nullValue();

  /**
   * Client identifier (client_id) used in the OAuth requests.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private CodeVO codeValue;

  /**
   * Authentication context class (ACR) values requested by default when the client does not send
   * acr_values.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private DefaultAcrValuesVO defaultAcrValuesValue = DefaultAcrValuesVO.nullValue();

  /**
   * Maximum authentication age, in seconds, applied when the client does not send max_age.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private DefaultMaxAgeVO defaultMaxAgeValue = DefaultMaxAgeVO.nullValue();

  /**
   * Requires the access tokens of this client to be bound to a DPoP key (RFC 9449).
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private DpopBoundAccessTokensVO dpopBoundAccessTokensValue;

  /**
   * The client was created through dynamic client registration (RFC 7591).
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private DynamicallyRegisteredVO dynamicallyRegisteredValue = DynamicallyRegisteredVO.nullValue();

  /**
   * A disabled client cannot sign in users or obtain tokens, without deleting it.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private EnabledVO enabledValue;

  /**
   * Adds the issuer and session identifier to the front-channel logout URL.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private FrontchannelLogoutSessionRequiredVO frontchannelLogoutSessionRequiredValue =
      FrontchannelLogoutSessionRequiredVO.nullValue();

  /**
   * URL of the client loaded in the browser to sign the user out (front-channel logout).
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private FrontchannelLogoutUriVO frontchannelLogoutUriValue = FrontchannelLogoutUriVO.nullValue();

  /**
   * JSON list of the grant types the client may use.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private GrantTypesJsonVO grantTypesJsonValue = GrantTypesJsonVO.nullValue();

  /**
   * Key management algorithm used to encrypt the ID tokens of this client; empty sends them
   * unencrypted.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private IdTokenEncryptedResponseAlgVO idTokenEncryptedResponseAlgValue =
      IdTokenEncryptedResponseAlgVO.nullValue();

  /**
   * Content encryption algorithm used to encrypt the ID tokens of this client.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private IdTokenEncryptedResponseEncVO idTokenEncryptedResponseEncValue =
      IdTokenEncryptedResponseEncVO.nullValue();

  /**
   * Algorithm used to sign the ID tokens issued to this client.
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
   * JSON Web Key Set with the public keys of the client, when it does not publish a jwks-uri.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private JwksJsonVO jwksJsonValue = JwksJsonVO.nullValue();

  /**
   * URL of the JSON Web Key Set with the public keys of the client.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private JwksUriVO jwksUriValue = JwksUriVO.nullValue();

  /**
   * URL of the client logo shown to users.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private LogoUriVO logoUriValue = LogoUriVO.nullValue();

  /**
   * URL of the privacy policy of the client.
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
   * When the client was registered.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private RegisteredAtVO registeredAtValue = RegisteredAtVO.nullValue();

  /**
   * Token the client uses to manage its own registration (RFC 7592); only its digest is stored.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private RegistrationAccessTokenVO registrationAccessTokenValue =
      RegistrationAccessTokenVO.nullValue();

  /**
   * Algorithm the client must use to sign request objects.
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
   * Requires the client to send its authorization requests through PAR (RFC 9126).
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private RequirePushedAuthorizationRequestsVO requirePushedAuthorizationRequestsValue;

  /**
   * JSON list of the response types the client may request.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ResponseTypesJsonVO responseTypesJsonValue = ResponseTypesJsonVO.nullValue();

  /**
   * Client secret; only its digest is stored.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private SecretOauthVO secretOauthValue = SecretOauthVO.nullValue();

  /**
   * URL listing the redirect URIs of the clients that share pairwise subject identifiers.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private SectorIdentifierUriVO sectorIdentifierUriValue = SectorIdentifierUriVO.nullValue();

  /**
   * Subject identifier sent to the client: the same for every client (public) or a different one
   * per sector (pairwise).
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private SubjectTypeVO subjectTypeValue;

  /**
   * Owner tenant. Empty for a global client, manageable only by the root tenant.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TenantVO tenantValue = TenantVO.nullValue();

  /**
   * Expected subject DN of the client certificate for mutual-TLS client authentication (RFC 8705).
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TlsClientAuthSubjectDnVO tlsClientAuthSubjectDnValue =
      TlsClientAuthSubjectDnVO.nullValue();

  /**
   * Binds the access tokens of this client to its TLS client certificate (RFC 8705).
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private TlsClientCertificateBoundAccessTokensVO tlsClientCertificateBoundAccessTokensValue;

  /**
   * How the client authenticates at the token endpoint: client secret in the Authorization header
   * or in the body, a signed JWT (private_key_jwt), or none for public clients.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private TokenEndpointAuthMethodVO tokenEndpointAuthMethodValue;

  /**
   * Algorithm the client uses to sign the JWT with which it authenticates at the token endpoint.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TokenEndpointAuthSigningAlgVO tokenEndpointAuthSigningAlgValue =
      TokenEndpointAuthSigningAlgVO.nullValue();

  /**
   * URL of the terms of service of the client.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TosUriVO tosUriValue = TosUriVO.nullValue();

  /**
   * Unique identifier of the client.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @EqualsAndHashCode.Include
  @NonNull
  private UidVO uidValue;

  /**
   * Key management algorithm used to encrypt the userinfo responses of this client; empty sends
   * them unencrypted.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private UserinfoEncryptedResponseAlgVO userinfoEncryptedResponseAlgValue =
      UserinfoEncryptedResponseAlgVO.nullValue();

  /**
   * Content encryption algorithm used to encrypt the userinfo responses of this client.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private UserinfoEncryptedResponseEncVO userinfoEncryptedResponseEncValue =
      UserinfoEncryptedResponseEncVO.nullValue();

  /**
   * Algorithm used to sign the userinfo responses; empty returns them as plain JSON.
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
