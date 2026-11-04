package net.civeira.phylax.feature.access.oauthclient.domain;

import java.util.Optional;

import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.common.value.validation.ConstraintFailList;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.AllowAllScopesValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.AllowedAudiencesValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.AllowedLoginRedirectsValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.AllowedLogoutRedirectsValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.AuthorizationDetailsTypesValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.AuthorizationSignedResponseAlgValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.BackchannelClientNotificationEndpointValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.BackchannelLogoutSessionRequiredValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.BackchannelLogoutUriValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.BackchannelTokenDeliveryModeValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.ClientCredentialsScopesValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.ClientCredentialsTokenTtlSecondsValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.ClientNameValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.ClientUriValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.CodeValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.DefaultAcrValuesValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.DefaultMaxAgeValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.DpopBoundAccessTokensValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.DynamicallyRegisteredValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.EnabledValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.FrontchannelLogoutSessionRequiredValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.FrontchannelLogoutUriValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.GrantTypesJsonValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.IdTokenEncryptedResponseAlgValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.IdTokenEncryptedResponseEncValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.IdTokenSignedResponseAlgValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.IntrospectionAllowedValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.JwksJsonValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.JwksUriValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.LogoUriValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.PolicyUriValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.RegisteredAtValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.RegistrationAccessTokenValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.RequestObjectSigningAlgValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.RequirePkceValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.RequirePushedAuthorizationRequestsValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.ResponseTypesJsonValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.SecretOauthValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.SectorIdentifierUriValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.SubjectTypeValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.TenantValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.TlsClientAuthSubjectDnValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.TlsClientCertificateBoundAccessTokensValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.TokenEndpointAuthMethodValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.TokenEndpointAuthSigningAlgValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.TosUriValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.UidValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.UserinfoEncryptedResponseAlgValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.UserinfoEncryptedResponseEncValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.UserinfoSignedResponseAlgValueHolder;
import net.civeira.phylax.feature.access.oauthclient.domain.valueholder.VersionValueHolder;

/**
 * A dto transfer to hold oauth client attribute values
 */
@NoArgsConstructor
public class OauthClientChangeSet {

  /**
   * Lets the client request any scope instead of only the standard OpenID Connect ones.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowAllScopesValueHolder<OauthClientChangeSet> allowAllScopesValue =
      new AllowAllScopesValueHolder<>(this);

  /**
   * Audiences (resource servers or resource URIs) the client may request tokens for.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedAudiencesValueHolder<OauthClientChangeSet> allowedAudiencesValue =
      new AllowedAudiencesValueHolder<>(this);

  /**
   * Redirect URIs the client may use after sign-in.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedLoginRedirectsValueHolder<OauthClientChangeSet> allowedLoginRedirectsValue =
      new AllowedLoginRedirectsValueHolder<>(this);

  /**
   * Redirect URIs the client may use after sign-out.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedLogoutRedirectsValueHolder<OauthClientChangeSet> allowedLogoutRedirectsValue =
      new AllowedLogoutRedirectsValueHolder<>(this);

  /**
   * JSON list of the authorization_details types the client can request (RFC 9396).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AuthorizationDetailsTypesValueHolder<OauthClientChangeSet> authorizationDetailsTypesValue =
      new AuthorizationDetailsTypesValueHolder<>(this);

  /**
   * Algorithm used to sign JWT-secured authorization responses (JARM) for this client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AuthorizationSignedResponseAlgValueHolder<OauthClientChangeSet> authorizationSignedResponseAlgValue =
      new AuthorizationSignedResponseAlgValueHolder<>(this);

  /**
   * Endpoint of the client that receives the CIBA ping notifications.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelClientNotificationEndpointValueHolder<OauthClientChangeSet> backchannelClientNotificationEndpointValue =
      new BackchannelClientNotificationEndpointValueHolder<>(this);

  /**
   * Includes the session identifier (sid) in the back-channel logout token.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelLogoutSessionRequiredValueHolder<OauthClientChangeSet> backchannelLogoutSessionRequiredValue =
      new BackchannelLogoutSessionRequiredValueHolder<>(this);

  /**
   * Endpoint of the client that receives back-channel logout notifications.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelLogoutUriValueHolder<OauthClientChangeSet> backchannelLogoutUriValue =
      new BackchannelLogoutUriValueHolder<>(this);

  /**
   * How tokens are delivered in the CIBA flow: the client polls the token endpoint, or receives a
   * ping.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelTokenDeliveryModeValueHolder<OauthClientChangeSet> backchannelTokenDeliveryModeValue =
      new BackchannelTokenDeliveryModeValueHolder<>(this);

  /**
   * Scopes granted to the client in the client credentials flow.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientCredentialsScopesValueHolder<OauthClientChangeSet> clientCredentialsScopesValue =
      new ClientCredentialsScopesValueHolder<>(this);

  /**
   * Lifetime, in seconds, of the access tokens issued in the client credentials flow.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientCredentialsTokenTtlSecondsValueHolder<OauthClientChangeSet> clientCredentialsTokenTtlSecondsValue =
      new ClientCredentialsTokenTtlSecondsValueHolder<>(this);

  /**
   * Name of the client shown to users, for example on the consent screen.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientNameValueHolder<OauthClientChangeSet> clientNameValue =
      new ClientNameValueHolder<>(this);

  /**
   * URL of the home page of the client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientUriValueHolder<OauthClientChangeSet> clientUriValue =
      new ClientUriValueHolder<>(this);

  /**
   * Client identifier (client_id) used in the OAuth requests.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private CodeValueHolder<OauthClientChangeSet> codeValue = new CodeValueHolder<>(this);

  /**
   * Authentication context class (ACR) values requested by default when the client does not send
   * acr_values.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DefaultAcrValuesValueHolder<OauthClientChangeSet> defaultAcrValuesValue =
      new DefaultAcrValuesValueHolder<>(this);

  /**
   * Maximum authentication age, in seconds, applied when the client does not send max_age.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DefaultMaxAgeValueHolder<OauthClientChangeSet> defaultMaxAgeValue =
      new DefaultMaxAgeValueHolder<>(this);

  /**
   * Requires the access tokens of this client to be bound to a DPoP key (RFC 9449).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DpopBoundAccessTokensValueHolder<OauthClientChangeSet> dpopBoundAccessTokensValue =
      new DpopBoundAccessTokensValueHolder<>(this);

  /**
   * The client was created through dynamic client registration (RFC 7591).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DynamicallyRegisteredValueHolder<OauthClientChangeSet> dynamicallyRegisteredValue =
      new DynamicallyRegisteredValueHolder<>(this);

  /**
   * A disabled client cannot sign in users or obtain tokens, without deleting it.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private EnabledValueHolder<OauthClientChangeSet> enabledValue = new EnabledValueHolder<>(this);

  /**
   * Adds the issuer and session identifier to the front-channel logout URL.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private FrontchannelLogoutSessionRequiredValueHolder<OauthClientChangeSet> frontchannelLogoutSessionRequiredValue =
      new FrontchannelLogoutSessionRequiredValueHolder<>(this);

  /**
   * URL of the client loaded in the browser to sign the user out (front-channel logout).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private FrontchannelLogoutUriValueHolder<OauthClientChangeSet> frontchannelLogoutUriValue =
      new FrontchannelLogoutUriValueHolder<>(this);

  /**
   * JSON list of the grant types the client may use.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private GrantTypesJsonValueHolder<OauthClientChangeSet> grantTypesJsonValue =
      new GrantTypesJsonValueHolder<>(this);

  /**
   * Key management algorithm used to encrypt the ID tokens of this client; empty sends them
   * unencrypted.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IdTokenEncryptedResponseAlgValueHolder<OauthClientChangeSet> idTokenEncryptedResponseAlgValue =
      new IdTokenEncryptedResponseAlgValueHolder<>(this);

  /**
   * Content encryption algorithm used to encrypt the ID tokens of this client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IdTokenEncryptedResponseEncValueHolder<OauthClientChangeSet> idTokenEncryptedResponseEncValue =
      new IdTokenEncryptedResponseEncValueHolder<>(this);

  /**
   * Algorithm used to sign the ID tokens issued to this client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IdTokenSignedResponseAlgValueHolder<OauthClientChangeSet> idTokenSignedResponseAlgValue =
      new IdTokenSignedResponseAlgValueHolder<>(this);

  /**
   * If true, this client is allowed to call the token introspection endpoint (POST /introspect) to
   * validate access tokens. Resource servers must authenticate at the introspection endpoint using
   * their client credentials.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IntrospectionAllowedValueHolder<OauthClientChangeSet> introspectionAllowedValue =
      new IntrospectionAllowedValueHolder<>(this);

  /**
   * JSON Web Key Set with the public keys of the client, when it does not publish a jwks-uri.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private JwksJsonValueHolder<OauthClientChangeSet> jwksJsonValue = new JwksJsonValueHolder<>(this);

  /**
   * URL of the JSON Web Key Set with the public keys of the client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private JwksUriValueHolder<OauthClientChangeSet> jwksUriValue = new JwksUriValueHolder<>(this);

  /**
   * URL of the client logo shown to users.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private LogoUriValueHolder<OauthClientChangeSet> logoUriValue = new LogoUriValueHolder<>(this);

  /**
   * URL of the privacy policy of the client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private PolicyUriValueHolder<OauthClientChangeSet> policyUriValue =
      new PolicyUriValueHolder<>(this);

  /**
   * When the client was registered.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RegisteredAtValueHolder<OauthClientChangeSet> registeredAtValue =
      new RegisteredAtValueHolder<>(this);

  /**
   * Token the client uses to manage its own registration (RFC 7592); only its digest is stored.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RegistrationAccessTokenValueHolder<OauthClientChangeSet> registrationAccessTokenValue =
      new RegistrationAccessTokenValueHolder<>(this);

  /**
   * Algorithm the client must use to sign request objects.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RequestObjectSigningAlgValueHolder<OauthClientChangeSet> requestObjectSigningAlgValue =
      new RequestObjectSigningAlgValueHolder<>(this);

  /**
   * If true, every authorization code request from this client must include code_challenge and
   * code_challenge_method=S256. For clients with public-allow=true, PKCE is always required
   * regardless of this flag.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RequirePkceValueHolder<OauthClientChangeSet> requirePkceValue =
      new RequirePkceValueHolder<>(this);

  /**
   * Requires the client to send its authorization requests through PAR (RFC 9126).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RequirePushedAuthorizationRequestsValueHolder<OauthClientChangeSet> requirePushedAuthorizationRequestsValue =
      new RequirePushedAuthorizationRequestsValueHolder<>(this);

  /**
   * JSON list of the response types the client may request.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ResponseTypesJsonValueHolder<OauthClientChangeSet> responseTypesJsonValue =
      new ResponseTypesJsonValueHolder<>(this);

  /**
   * Client secret; only its digest is stored.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SecretOauthValueHolder<OauthClientChangeSet> secretOauthValue =
      new SecretOauthValueHolder<>(this);

  /**
   * URL listing the redirect URIs of the clients that share pairwise subject identifiers.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SectorIdentifierUriValueHolder<OauthClientChangeSet> sectorIdentifierUriValue =
      new SectorIdentifierUriValueHolder<>(this);

  /**
   * Subject identifier sent to the client: the same for every client (public) or a different one
   * per sector (pairwise).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SubjectTypeValueHolder<OauthClientChangeSet> subjectTypeValue =
      new SubjectTypeValueHolder<>(this);

  /**
   * Owner tenant. Empty for a global client, manageable only by the root tenant.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TenantValueHolder<OauthClientChangeSet> tenantValue = new TenantValueHolder<>(this);

  /**
   * Expected subject DN of the client certificate for mutual-TLS client authentication (RFC 8705).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TlsClientAuthSubjectDnValueHolder<OauthClientChangeSet> tlsClientAuthSubjectDnValue =
      new TlsClientAuthSubjectDnValueHolder<>(this);

  /**
   * Binds the access tokens of this client to its TLS client certificate (RFC 8705).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TlsClientCertificateBoundAccessTokensValueHolder<OauthClientChangeSet> tlsClientCertificateBoundAccessTokensValue =
      new TlsClientCertificateBoundAccessTokensValueHolder<>(this);

  /**
   * How the client authenticates at the token endpoint: client secret in the Authorization header
   * or in the body, a signed JWT (private_key_jwt), or none for public clients.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TokenEndpointAuthMethodValueHolder<OauthClientChangeSet> tokenEndpointAuthMethodValue =
      new TokenEndpointAuthMethodValueHolder<>(this);

  /**
   * Algorithm the client uses to sign the JWT with which it authenticates at the token endpoint.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TokenEndpointAuthSigningAlgValueHolder<OauthClientChangeSet> tokenEndpointAuthSigningAlgValue =
      new TokenEndpointAuthSigningAlgValueHolder<>(this);

  /**
   * URL of the terms of service of the client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TosUriValueHolder<OauthClientChangeSet> tosUriValue = new TosUriValueHolder<>(this);

  /**
   * Unique identifier of the client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UidValueHolder<OauthClientChangeSet> uidValue = new UidValueHolder<>(this);

  /**
   * Key management algorithm used to encrypt the userinfo responses of this client; empty sends
   * them unencrypted.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UserinfoEncryptedResponseAlgValueHolder<OauthClientChangeSet> userinfoEncryptedResponseAlgValue =
      new UserinfoEncryptedResponseAlgValueHolder<>(this);

  /**
   * Content encryption algorithm used to encrypt the userinfo responses of this client.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UserinfoEncryptedResponseEncValueHolder<OauthClientChangeSet> userinfoEncryptedResponseEncValue =
      new UserinfoEncryptedResponseEncValueHolder<>(this);

  /**
   * Algorithm used to sign the userinfo responses; empty returns them as plain JSON.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UserinfoSignedResponseAlgValueHolder<OauthClientChangeSet> userinfoSignedResponseAlgValue =
      new UserinfoSignedResponseAlgValueHolder<>(this);

  /**
   * Campo con el número de version de oauth client para controlar bloqueos optimistas
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private VersionValueHolder<OauthClientChangeSet> versionValue = new VersionValueHolder<>(this);

  /**
   * Check the preconditions and fail if any is not met.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param original The current entity, empty on create.
   * @throws ConstraintException If some precondition is not met.
   */
  public void checkValidate(final Optional<OauthClient> original) throws ConstraintException {
    ConstraintFailList fails = new ConstraintFailList();
    validate(original, fails);
    if (fails.hasErrors()) {
      throw new ConstraintException("Invalid values on OauthClientChangeSet", fails);
    }
  }

  /**
   * Check the preconditions for a new oauth client and fail if any is not met.
   *
   * @autogenerated EntityChangeSetGenerator
   * @throws ConstraintException If some precondition is not met.
   */
  public void checkValidate() throws ConstraintException {
    checkValidate(Optional.empty());
  }

  /**
   * @autogenerated EntityChangeSetGenerator
   * @return
   */
  public OauthClientChangeSet newUid() {
    return version(0).uid(Uuid.comb().toString());
  }

  /**
   * Unset a value by the property name. Unset values will no provide command change neither provide
   * information to the output context (hidden or readonly fields, or policy restrictions).
   *
   * @autogenerated EntityChangeSetGenerator
   * @param field The field name to unset.
   */
  public void unset(final String field) {
    uidValue.unsetUidIfMatches(field);
    codeValue.unsetCodeIfMatches(field);
    tenantValue.unsetTenantIfMatches(field);
    allowAllScopesValue.unsetAllowAllScopesIfMatches(field);
    tokenEndpointAuthMethodValue.unsetTokenEndpointAuthMethodIfMatches(field);
    tokenEndpointAuthSigningAlgValue.unsetTokenEndpointAuthSigningAlgIfMatches(field);
    dpopBoundAccessTokensValue.unsetDpopBoundAccessTokensIfMatches(field);
    idTokenSignedResponseAlgValue.unsetIdTokenSignedResponseAlgIfMatches(field);
    tlsClientAuthSubjectDnValue.unsetTlsClientAuthSubjectDnIfMatches(field);
    tlsClientCertificateBoundAccessTokensValue
        .unsetTlsClientCertificateBoundAccessTokensIfMatches(field);
    authorizationSignedResponseAlgValue.unsetAuthorizationSignedResponseAlgIfMatches(field);
    backchannelTokenDeliveryModeValue.unsetBackchannelTokenDeliveryModeIfMatches(field);
    backchannelClientNotificationEndpointValue
        .unsetBackchannelClientNotificationEndpointIfMatches(field);
    subjectTypeValue.unsetSubjectTypeIfMatches(field);
    idTokenEncryptedResponseAlgValue.unsetIdTokenEncryptedResponseAlgIfMatches(field);
    idTokenEncryptedResponseEncValue.unsetIdTokenEncryptedResponseEncIfMatches(field);
    userinfoEncryptedResponseAlgValue.unsetUserinfoEncryptedResponseAlgIfMatches(field);
    userinfoEncryptedResponseEncValue.unsetUserinfoEncryptedResponseEncIfMatches(field);
    authorizationDetailsTypesValue.unsetAuthorizationDetailsTypesIfMatches(field);
    userinfoSignedResponseAlgValue.unsetUserinfoSignedResponseAlgIfMatches(field);
    secretOauthValue.unsetSecretOauthIfMatches(field);
    registrationAccessTokenValue.unsetRegistrationAccessTokenIfMatches(field);
    defaultAcrValuesValue.unsetDefaultAcrValuesIfMatches(field);
    defaultMaxAgeValue.unsetDefaultMaxAgeIfMatches(field);
    sectorIdentifierUriValue.unsetSectorIdentifierUriIfMatches(field);
    allowedLoginRedirectsValue.unsetAllowedLoginRedirectsIfMatches(field);
    allowedLogoutRedirectsValue.unsetAllowedLogoutRedirectsIfMatches(field);
    allowedAudiencesValue.unsetAllowedAudiencesIfMatches(field);
    grantTypesJsonValue.unsetGrantTypesJsonIfMatches(field);
    introspectionAllowedValue.unsetIntrospectionAllowedIfMatches(field);
    requirePushedAuthorizationRequestsValue.unsetRequirePushedAuthorizationRequestsIfMatches(field);
    requirePkceValue.unsetRequirePkceIfMatches(field);
    backchannelLogoutUriValue.unsetBackchannelLogoutUriIfMatches(field);
    backchannelLogoutSessionRequiredValue.unsetBackchannelLogoutSessionRequiredIfMatches(field);
    frontchannelLogoutUriValue.unsetFrontchannelLogoutUriIfMatches(field);
    frontchannelLogoutSessionRequiredValue.unsetFrontchannelLogoutSessionRequiredIfMatches(field);
    enabledValue.unsetEnabledIfMatches(field);
    clientNameValue.unsetClientNameIfMatches(field);
    logoUriValue.unsetLogoUriIfMatches(field);
    clientUriValue.unsetClientUriIfMatches(field);
    policyUriValue.unsetPolicyUriIfMatches(field);
    tosUriValue.unsetTosUriIfMatches(field);
    responseTypesJsonValue.unsetResponseTypesJsonIfMatches(field);
    dynamicallyRegisteredValue.unsetDynamicallyRegisteredIfMatches(field);
    registeredAtValue.unsetRegisteredAtIfMatches(field);
    clientCredentialsScopesValue.unsetClientCredentialsScopesIfMatches(field);
    clientCredentialsTokenTtlSecondsValue.unsetClientCredentialsTokenTtlSecondsIfMatches(field);
    requestObjectSigningAlgValue.unsetRequestObjectSigningAlgIfMatches(field);
    jwksUriValue.unsetJwksUriIfMatches(field);
    jwksJsonValue.unsetJwksJsonIfMatches(field);
    versionValue.unsetVersionIfMatches(field);
  }

  /**
   * Check the preconditions of oauth client over the resulting values: the ones of this change set
   * or, if unset, the ones of the original.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param original The current entity, empty on create.
   * @param fails List to add the failed preconditions.
   */
  public void validate(final Optional<OauthClient> original, final ConstraintFailList fails) {
    // No preconditions for oauth client
  }

  /**
   * Check the preconditions for a new oauth client.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param fails List to add the failed preconditions.
   */
  public void validate(final ConstraintFailList fails) {
    validate(Optional.empty(), fails);
  }
}
