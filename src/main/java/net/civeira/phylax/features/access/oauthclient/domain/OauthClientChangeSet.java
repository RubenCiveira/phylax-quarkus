package net.civeira.phylax.features.access.oauthclient.domain;

import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.AllowAllScopesValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.AllowedAudiencesValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.AllowedLoginRedirectsValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.AllowedLogoutRedirectsValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.AuthorizationDetailsTypesValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.AuthorizationSignedResponseAlgValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.BackchannelClientNotificationEndpointValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.BackchannelLogoutSessionRequiredValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.BackchannelLogoutUriValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.BackchannelTokenDeliveryModeValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.ClientCredentialsScopesValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.ClientCredentialsTokenTtlSecondsValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.ClientNameValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.ClientUriValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.CodeValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.DefaultAcrValuesValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.DefaultMaxAgeValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.DpopBoundAccessTokensValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.DynamicallyRegisteredValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.EnabledValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.FrontchannelLogoutSessionRequiredValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.FrontchannelLogoutUriValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.GrantTypesJsonValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.IdTokenEncryptedResponseAlgValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.IdTokenEncryptedResponseEncValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.IdTokenSignedResponseAlgValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.IntrospectionAllowedValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.JwksJsonValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.JwksUriValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.LogoUriValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.PolicyUriValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.RegisteredAtValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.RegistrationAccessTokenValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.RequestObjectSigningAlgValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.RequirePkceValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.RequirePushedAuthorizationRequestsValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.ResponseTypesJsonValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.SecretOauthValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.SectorIdentifierUriValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.SubjectTypeValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.TenantValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.TlsClientAuthSubjectDnValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.TlsClientCertificateBoundAccessTokensValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.TokenEndpointAuthMethodValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.TokenEndpointAuthSigningAlgValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.TosUriValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.UidValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.UserinfoEncryptedResponseAlgValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.UserinfoEncryptedResponseEncValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.UserinfoSignedResponseAlgValueHolder;
import net.civeira.phylax.features.access.oauthclient.domain.valueholder.VersionValueHolder;

/**
 * A dto transfer to hold oauth client attribute values
 */
@NoArgsConstructor
public class OauthClientChangeSet {

  /**
   * El allow all scopes de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowAllScopesValueHolder<OauthClientChangeSet> allowAllScopesValue =
      new AllowAllScopesValueHolder<>(this);

  /**
   * El allowed audiences de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedAudiencesValueHolder<OauthClientChangeSet> allowedAudiencesValue =
      new AllowedAudiencesValueHolder<>(this);

  /**
   * El allowed login redirects de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedLoginRedirectsValueHolder<OauthClientChangeSet> allowedLoginRedirectsValue =
      new AllowedLoginRedirectsValueHolder<>(this);

  /**
   * El allowed logout redirects de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedLogoutRedirectsValueHolder<OauthClientChangeSet> allowedLogoutRedirectsValue =
      new AllowedLogoutRedirectsValueHolder<>(this);

  /**
   * El authorization details types de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AuthorizationDetailsTypesValueHolder<OauthClientChangeSet> authorizationDetailsTypesValue =
      new AuthorizationDetailsTypesValueHolder<>(this);

  /**
   * El authorization signed response alg de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AuthorizationSignedResponseAlgValueHolder<OauthClientChangeSet> authorizationSignedResponseAlgValue =
      new AuthorizationSignedResponseAlgValueHolder<>(this);

  /**
   * El backchannel client notification endpoint de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelClientNotificationEndpointValueHolder<OauthClientChangeSet> backchannelClientNotificationEndpointValue =
      new BackchannelClientNotificationEndpointValueHolder<>(this);

  /**
   * El backchannel logout session required de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelLogoutSessionRequiredValueHolder<OauthClientChangeSet> backchannelLogoutSessionRequiredValue =
      new BackchannelLogoutSessionRequiredValueHolder<>(this);

  /**
   * El backchannel logout uri de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelLogoutUriValueHolder<OauthClientChangeSet> backchannelLogoutUriValue =
      new BackchannelLogoutUriValueHolder<>(this);

  /**
   * El backchannel token delivery mode de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelTokenDeliveryModeValueHolder<OauthClientChangeSet> backchannelTokenDeliveryModeValue =
      new BackchannelTokenDeliveryModeValueHolder<>(this);

  /**
   * El client credentials scopes de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientCredentialsScopesValueHolder<OauthClientChangeSet> clientCredentialsScopesValue =
      new ClientCredentialsScopesValueHolder<>(this);

  /**
   * El client credentials token ttl seconds de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientCredentialsTokenTtlSecondsValueHolder<OauthClientChangeSet> clientCredentialsTokenTtlSecondsValue =
      new ClientCredentialsTokenTtlSecondsValueHolder<>(this);

  /**
   * El client name de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientNameValueHolder<OauthClientChangeSet> clientNameValue =
      new ClientNameValueHolder<>(this);

  /**
   * El client uri de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientUriValueHolder<OauthClientChangeSet> clientUriValue =
      new ClientUriValueHolder<>(this);

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private CodeValueHolder<OauthClientChangeSet> codeValue = new CodeValueHolder<>(this);

  /**
   * El default acr values de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DefaultAcrValuesValueHolder<OauthClientChangeSet> defaultAcrValuesValue =
      new DefaultAcrValuesValueHolder<>(this);

  /**
   * El default max age de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DefaultMaxAgeValueHolder<OauthClientChangeSet> defaultMaxAgeValue =
      new DefaultMaxAgeValueHolder<>(this);

  /**
   * El dpop bound access tokens de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DpopBoundAccessTokensValueHolder<OauthClientChangeSet> dpopBoundAccessTokensValue =
      new DpopBoundAccessTokensValueHolder<>(this);

  /**
   * El dynamically registered de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DynamicallyRegisteredValueHolder<OauthClientChangeSet> dynamicallyRegisteredValue =
      new DynamicallyRegisteredValueHolder<>(this);

  /**
   * The indicator to allow the account be used without deleting it
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private EnabledValueHolder<OauthClientChangeSet> enabledValue = new EnabledValueHolder<>(this);

  /**
   * El frontchannel logout session required de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private FrontchannelLogoutSessionRequiredValueHolder<OauthClientChangeSet> frontchannelLogoutSessionRequiredValue =
      new FrontchannelLogoutSessionRequiredValueHolder<>(this);

  /**
   * El frontchannel logout uri de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private FrontchannelLogoutUriValueHolder<OauthClientChangeSet> frontchannelLogoutUriValue =
      new FrontchannelLogoutUriValueHolder<>(this);

  /**
   * El grant types json de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private GrantTypesJsonValueHolder<OauthClientChangeSet> grantTypesJsonValue =
      new GrantTypesJsonValueHolder<>(this);

  /**
   * El id token encrypted response alg de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IdTokenEncryptedResponseAlgValueHolder<OauthClientChangeSet> idTokenEncryptedResponseAlgValue =
      new IdTokenEncryptedResponseAlgValueHolder<>(this);

  /**
   * El id token encrypted response enc de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IdTokenEncryptedResponseEncValueHolder<OauthClientChangeSet> idTokenEncryptedResponseEncValue =
      new IdTokenEncryptedResponseEncValueHolder<>(this);

  /**
   * El id token signed response alg de oauth client
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
   * El jwks json de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private JwksJsonValueHolder<OauthClientChangeSet> jwksJsonValue = new JwksJsonValueHolder<>(this);

  /**
   * El jwks uri de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private JwksUriValueHolder<OauthClientChangeSet> jwksUriValue = new JwksUriValueHolder<>(this);

  /**
   * El logo uri de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private LogoUriValueHolder<OauthClientChangeSet> logoUriValue = new LogoUriValueHolder<>(this);

  /**
   * El policy uri de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private PolicyUriValueHolder<OauthClientChangeSet> policyUriValue =
      new PolicyUriValueHolder<>(this);

  /**
   * El registered at de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RegisteredAtValueHolder<OauthClientChangeSet> registeredAtValue =
      new RegisteredAtValueHolder<>(this);

  /**
   * El registration access token de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RegistrationAccessTokenValueHolder<OauthClientChangeSet> registrationAccessTokenValue =
      new RegistrationAccessTokenValueHolder<>(this);

  /**
   * El request object signing alg de oauth client
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
   * El require pushed authorization requests de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RequirePushedAuthorizationRequestsValueHolder<OauthClientChangeSet> requirePushedAuthorizationRequestsValue =
      new RequirePushedAuthorizationRequestsValueHolder<>(this);

  /**
   * El response types json de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ResponseTypesJsonValueHolder<OauthClientChangeSet> responseTypesJsonValue =
      new ResponseTypesJsonValueHolder<>(this);

  /**
   * El secret oauth de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SecretOauthValueHolder<OauthClientChangeSet> secretOauthValue =
      new SecretOauthValueHolder<>(this);

  /**
   * El sector identifier uri de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SectorIdentifierUriValueHolder<OauthClientChangeSet> sectorIdentifierUriValue =
      new SectorIdentifierUriValueHolder<>(this);

  /**
   * El subject type de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SubjectTypeValueHolder<OauthClientChangeSet> subjectTypeValue =
      new SubjectTypeValueHolder<>(this);

  /**
   * Owner tenant. Null means a global client, manageable only by root.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TenantValueHolder<OauthClientChangeSet> tenantValue = new TenantValueHolder<>(this);

  /**
   * El tls client auth subject dn de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TlsClientAuthSubjectDnValueHolder<OauthClientChangeSet> tlsClientAuthSubjectDnValue =
      new TlsClientAuthSubjectDnValueHolder<>(this);

  /**
   * El tls client certificate bound access tokens de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TlsClientCertificateBoundAccessTokensValueHolder<OauthClientChangeSet> tlsClientCertificateBoundAccessTokensValue =
      new TlsClientCertificateBoundAccessTokensValueHolder<>(this);

  /**
   * El token endpoint auth method de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TokenEndpointAuthMethodValueHolder<OauthClientChangeSet> tokenEndpointAuthMethodValue =
      new TokenEndpointAuthMethodValueHolder<>(this);

  /**
   * El token endpoint auth signing alg de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TokenEndpointAuthSigningAlgValueHolder<OauthClientChangeSet> tokenEndpointAuthSigningAlgValue =
      new TokenEndpointAuthSigningAlgValueHolder<>(this);

  /**
   * El tos uri de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TosUriValueHolder<OauthClientChangeSet> tosUriValue = new TosUriValueHolder<>(this);

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UidValueHolder<OauthClientChangeSet> uidValue = new UidValueHolder<>(this);

  /**
   * El userinfo encrypted response alg de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UserinfoEncryptedResponseAlgValueHolder<OauthClientChangeSet> userinfoEncryptedResponseAlgValue =
      new UserinfoEncryptedResponseAlgValueHolder<>(this);

  /**
   * El userinfo encrypted response enc de oauth client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UserinfoEncryptedResponseEncValueHolder<OauthClientChangeSet> userinfoEncryptedResponseEncValue =
      new UserinfoEncryptedResponseEncValueHolder<>(this);

  /**
   * El userinfo signed response alg de oauth client
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
}
