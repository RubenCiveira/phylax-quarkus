package net.civeira.phylax.features.access.trustedclient.domain;

import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.AllowAllScopesValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.AllowedAudiencesValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.AllowedRedirectsValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.AllowedScopesM2mValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.AuthorizationDetailsTypesValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.AuthorizationSignedResponseAlgValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.BackChannelLogoutSessionRequiredValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.BackChannelLogoutUriValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.BackchannelClientNotificationEndpointValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.BackchannelTokenDeliveryModeValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.ClientNameValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.ClientUriValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.CodeValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.DefaultAcrValuesValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.DefaultMaxAgeValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.DpopBoundAccessTokensValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.DynamicallyRegisteredValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.EnabledValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.FrontChannelLogoutSessionRequiredValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.FrontChannelLogoutUriValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.GrantTypesJsonValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.IdTokenEncryptedResponseAlgValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.IdTokenEncryptedResponseEncValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.IdTokenSignedResponseAlgValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.IsResourceServerValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.JwksJsonValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.JwksUriValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.LogoUriValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.M2mTokenTtlSecondsValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.PolicyUriValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.PublicAllowValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.RegisteredAtValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.RegistrationAccessValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.RequestObjectSigningAlgValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.RequirePkceValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.RequirePushedAuthorizationRequestsValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.ResponseTypesJsonValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.SecretOauthValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.SectorIdentifierUriValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.SubjectTypeValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.TenantValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.TlsClientAuthSubjectDnValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.TlsClientCertificateBoundAccessTokensValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.TokenEndpointAuthMethodValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.TokenEndpointAuthSigningAlgValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.TosUriValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.UidValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.UserinfoEncryptedResponseAlgValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.UserinfoEncryptedResponseEncValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.UserinfoSignedResponseAlgValueHolder;
import net.civeira.phylax.features.access.trustedclient.domain.valueholder.VersionValueHolder;

/**
 * A dto transfer to hold trusted client attribute values
 */
@NoArgsConstructor
public class TrustedClientChangeSet {

  /**
   * El allow all scopes de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowAllScopesValueHolder<TrustedClientChangeSet> allowAllScopesValue =
      new AllowAllScopesValueHolder<>(this);

  /**
   * El allowed audiences de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedAudiencesValueHolder<TrustedClientChangeSet> allowedAudiencesValue =
      new AllowedAudiencesValueHolder<>(this);

  /**
   * El allowed redirects de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedRedirectsValueHolder<TrustedClientChangeSet> allowedRedirectsValue =
      new AllowedRedirectsValueHolder<>(this);

  /**
   * El allowed scopes m 2m de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AllowedScopesM2mValueHolder<TrustedClientChangeSet> allowedScopesM2mValue =
      new AllowedScopesM2mValueHolder<>(this);

  /**
   * El authorization details types de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AuthorizationDetailsTypesValueHolder<TrustedClientChangeSet> authorizationDetailsTypesValue =
      new AuthorizationDetailsTypesValueHolder<>(this);

  /**
   * El authorization signed response alg de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private AuthorizationSignedResponseAlgValueHolder<TrustedClientChangeSet> authorizationSignedResponseAlgValue =
      new AuthorizationSignedResponseAlgValueHolder<>(this);

  /**
   * El back channel logout session required de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackChannelLogoutSessionRequiredValueHolder<TrustedClientChangeSet> backChannelLogoutSessionRequiredValue =
      new BackChannelLogoutSessionRequiredValueHolder<>(this);

  /**
   * El back channel logout uri de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackChannelLogoutUriValueHolder<TrustedClientChangeSet> backChannelLogoutUriValue =
      new BackChannelLogoutUriValueHolder<>(this);

  /**
   * El backchannel client notification endpoint de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelClientNotificationEndpointValueHolder<TrustedClientChangeSet> backchannelClientNotificationEndpointValue =
      new BackchannelClientNotificationEndpointValueHolder<>(this);

  /**
   * El backchannel token delivery mode de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private BackchannelTokenDeliveryModeValueHolder<TrustedClientChangeSet> backchannelTokenDeliveryModeValue =
      new BackchannelTokenDeliveryModeValueHolder<>(this);

  /**
   * El client name de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientNameValueHolder<TrustedClientChangeSet> clientNameValue =
      new ClientNameValueHolder<>(this);

  /**
   * El client uri de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ClientUriValueHolder<TrustedClientChangeSet> clientUriValue =
      new ClientUriValueHolder<>(this);

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private CodeValueHolder<TrustedClientChangeSet> codeValue = new CodeValueHolder<>(this);

  /**
   * El default acr values de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DefaultAcrValuesValueHolder<TrustedClientChangeSet> defaultAcrValuesValue =
      new DefaultAcrValuesValueHolder<>(this);

  /**
   * El default max age de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DefaultMaxAgeValueHolder<TrustedClientChangeSet> defaultMaxAgeValue =
      new DefaultMaxAgeValueHolder<>(this);

  /**
   * El dpop bound access tokens de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DpopBoundAccessTokensValueHolder<TrustedClientChangeSet> dpopBoundAccessTokensValue =
      new DpopBoundAccessTokensValueHolder<>(this);

  /**
   * El dynamically registered de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private DynamicallyRegisteredValueHolder<TrustedClientChangeSet> dynamicallyRegisteredValue =
      new DynamicallyRegisteredValueHolder<>(this);

  /**
   * The indicator to allow the account be used without deleting it
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private EnabledValueHolder<TrustedClientChangeSet> enabledValue = new EnabledValueHolder<>(this);

  /**
   * El front channel logout session required de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private FrontChannelLogoutSessionRequiredValueHolder<TrustedClientChangeSet> frontChannelLogoutSessionRequiredValue =
      new FrontChannelLogoutSessionRequiredValueHolder<>(this);

  /**
   * El front channel logout uri de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private FrontChannelLogoutUriValueHolder<TrustedClientChangeSet> frontChannelLogoutUriValue =
      new FrontChannelLogoutUriValueHolder<>(this);

  /**
   * El grant types json de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private GrantTypesJsonValueHolder<TrustedClientChangeSet> grantTypesJsonValue =
      new GrantTypesJsonValueHolder<>(this);

  /**
   * El id token encrypted response alg de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IdTokenEncryptedResponseAlgValueHolder<TrustedClientChangeSet> idTokenEncryptedResponseAlgValue =
      new IdTokenEncryptedResponseAlgValueHolder<>(this);

  /**
   * El id token encrypted response enc de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IdTokenEncryptedResponseEncValueHolder<TrustedClientChangeSet> idTokenEncryptedResponseEncValue =
      new IdTokenEncryptedResponseEncValueHolder<>(this);

  /**
   * El id token signed response alg de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IdTokenSignedResponseAlgValueHolder<TrustedClientChangeSet> idTokenSignedResponseAlgValue =
      new IdTokenSignedResponseAlgValueHolder<>(this);

  /**
   * If true, this client is allowed to call the token introspection endpoint (POST /introspect) to
   * validate access tokens. Resource servers must authenticate at the introspection endpoint using
   * their client credentials.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private IsResourceServerValueHolder<TrustedClientChangeSet> isResourceServerValue =
      new IsResourceServerValueHolder<>(this);

  /**
   * El jwks json de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private JwksJsonValueHolder<TrustedClientChangeSet> jwksJsonValue =
      new JwksJsonValueHolder<>(this);

  /**
   * El jwks uri de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private JwksUriValueHolder<TrustedClientChangeSet> jwksUriValue = new JwksUriValueHolder<>(this);

  /**
   * El logo uri de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private LogoUriValueHolder<TrustedClientChangeSet> logoUriValue = new LogoUriValueHolder<>(this);

  /**
   * El m 2m token ttl seconds de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private M2mTokenTtlSecondsValueHolder<TrustedClientChangeSet> m2mTokenTtlSecondsValue =
      new M2mTokenTtlSecondsValueHolder<>(this);

  /**
   * El policy uri de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private PolicyUriValueHolder<TrustedClientChangeSet> policyUriValue =
      new PolicyUriValueHolder<>(this);

  /**
   * If true, users can use these client to access with public code flow
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private PublicAllowValueHolder<TrustedClientChangeSet> publicAllowValue =
      new PublicAllowValueHolder<>(this);

  /**
   * El registered at de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RegisteredAtValueHolder<TrustedClientChangeSet> registeredAtValue =
      new RegisteredAtValueHolder<>(this);

  /**
   * El registration access de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RegistrationAccessValueHolder<TrustedClientChangeSet> registrationAccessValue =
      new RegistrationAccessValueHolder<>(this);

  /**
   * El request object signing alg de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RequestObjectSigningAlgValueHolder<TrustedClientChangeSet> requestObjectSigningAlgValue =
      new RequestObjectSigningAlgValueHolder<>(this);

  /**
   * If true, every authorization code request from this client must include code_challenge and
   * code_challenge_method=S256. For clients with public-allow=true, PKCE is always required
   * regardless of this flag.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RequirePkceValueHolder<TrustedClientChangeSet> requirePkceValue =
      new RequirePkceValueHolder<>(this);

  /**
   * El require pushed authorization requests de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private RequirePushedAuthorizationRequestsValueHolder<TrustedClientChangeSet> requirePushedAuthorizationRequestsValue =
      new RequirePushedAuthorizationRequestsValueHolder<>(this);

  /**
   * El response types json de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private ResponseTypesJsonValueHolder<TrustedClientChangeSet> responseTypesJsonValue =
      new ResponseTypesJsonValueHolder<>(this);

  /**
   * El secret oauth de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SecretOauthValueHolder<TrustedClientChangeSet> secretOauthValue =
      new SecretOauthValueHolder<>(this);

  /**
   * El sector identifier uri de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SectorIdentifierUriValueHolder<TrustedClientChangeSet> sectorIdentifierUriValue =
      new SectorIdentifierUriValueHolder<>(this);

  /**
   * El subject type de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private SubjectTypeValueHolder<TrustedClientChangeSet> subjectTypeValue =
      new SubjectTypeValueHolder<>(this);

  /**
   * Owner tenant. Null means a global client, manageable only by root.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TenantValueHolder<TrustedClientChangeSet> tenantValue = new TenantValueHolder<>(this);

  /**
   * El tls client auth subject dn de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TlsClientAuthSubjectDnValueHolder<TrustedClientChangeSet> tlsClientAuthSubjectDnValue =
      new TlsClientAuthSubjectDnValueHolder<>(this);

  /**
   * El tls client certificate bound access tokens de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TlsClientCertificateBoundAccessTokensValueHolder<TrustedClientChangeSet> tlsClientCertificateBoundAccessTokensValue =
      new TlsClientCertificateBoundAccessTokensValueHolder<>(this);

  /**
   * El token endpoint auth method de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TokenEndpointAuthMethodValueHolder<TrustedClientChangeSet> tokenEndpointAuthMethodValue =
      new TokenEndpointAuthMethodValueHolder<>(this);

  /**
   * El token endpoint auth signing alg de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TokenEndpointAuthSigningAlgValueHolder<TrustedClientChangeSet> tokenEndpointAuthSigningAlgValue =
      new TokenEndpointAuthSigningAlgValueHolder<>(this);

  /**
   * El tos uri de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private TosUriValueHolder<TrustedClientChangeSet> tosUriValue = new TosUriValueHolder<>(this);

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UidValueHolder<TrustedClientChangeSet> uidValue = new UidValueHolder<>(this);

  /**
   * El userinfo encrypted response alg de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UserinfoEncryptedResponseAlgValueHolder<TrustedClientChangeSet> userinfoEncryptedResponseAlgValue =
      new UserinfoEncryptedResponseAlgValueHolder<>(this);

  /**
   * El userinfo encrypted response enc de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UserinfoEncryptedResponseEncValueHolder<TrustedClientChangeSet> userinfoEncryptedResponseEncValue =
      new UserinfoEncryptedResponseEncValueHolder<>(this);

  /**
   * El userinfo signed response alg de trusted client
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private UserinfoSignedResponseAlgValueHolder<TrustedClientChangeSet> userinfoSignedResponseAlgValue =
      new UserinfoSignedResponseAlgValueHolder<>(this);

  /**
   * Campo con el número de version de trusted client para controlar bloqueos optimistas
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Delegate
  private VersionValueHolder<TrustedClientChangeSet> versionValue = new VersionValueHolder<>(this);

  /**
   * @autogenerated EntityChangeSetGenerator
   * @return
   */
  public TrustedClientChangeSet newUid() {
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
    publicAllowValue.unsetPublicAllowIfMatches(field);
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
    registrationAccessValue.unsetRegistrationAccessIfMatches(field);
    defaultAcrValuesValue.unsetDefaultAcrValuesIfMatches(field);
    defaultMaxAgeValue.unsetDefaultMaxAgeIfMatches(field);
    sectorIdentifierUriValue.unsetSectorIdentifierUriIfMatches(field);
    allowedRedirectsValue.unsetAllowedRedirectsIfMatches(field);
    allowedAudiencesValue.unsetAllowedAudiencesIfMatches(field);
    grantTypesJsonValue.unsetGrantTypesJsonIfMatches(field);
    isResourceServerValue.unsetIsResourceServerIfMatches(field);
    requirePushedAuthorizationRequestsValue.unsetRequirePushedAuthorizationRequestsIfMatches(field);
    requirePkceValue.unsetRequirePkceIfMatches(field);
    backChannelLogoutUriValue.unsetBackChannelLogoutUriIfMatches(field);
    backChannelLogoutSessionRequiredValue.unsetBackChannelLogoutSessionRequiredIfMatches(field);
    frontChannelLogoutUriValue.unsetFrontChannelLogoutUriIfMatches(field);
    frontChannelLogoutSessionRequiredValue.unsetFrontChannelLogoutSessionRequiredIfMatches(field);
    enabledValue.unsetEnabledIfMatches(field);
    clientNameValue.unsetClientNameIfMatches(field);
    logoUriValue.unsetLogoUriIfMatches(field);
    clientUriValue.unsetClientUriIfMatches(field);
    policyUriValue.unsetPolicyUriIfMatches(field);
    tosUriValue.unsetTosUriIfMatches(field);
    responseTypesJsonValue.unsetResponseTypesJsonIfMatches(field);
    dynamicallyRegisteredValue.unsetDynamicallyRegisteredIfMatches(field);
    registeredAtValue.unsetRegisteredAtIfMatches(field);
    allowedScopesM2mValue.unsetAllowedScopesM2mIfMatches(field);
    m2mTokenTtlSecondsValue.unsetM2mTokenTtlSecondsIfMatches(field);
    requestObjectSigningAlgValue.unsetRequestObjectSigningAlgIfMatches(field);
    jwksUriValue.unsetJwksUriIfMatches(field);
    jwksJsonValue.unsetJwksJsonIfMatches(field);
    versionValue.unsetVersionIfMatches(field);
  }
}
