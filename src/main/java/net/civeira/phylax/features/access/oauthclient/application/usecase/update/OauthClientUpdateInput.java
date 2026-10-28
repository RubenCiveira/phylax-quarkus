package net.civeira.phylax.features.access.oauthclient.application.usecase.update;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.oauthclient.domain.AllowedAudiences;
import net.civeira.phylax.features.access.oauthclient.domain.AllowedLoginRedirects;
import net.civeira.phylax.features.access.oauthclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClientChangeSet;
import net.civeira.phylax.features.access.oauthclient.domain.OauthClientIdTokenSignedResponseAlgOptions;
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
 * A dto transfer to hold oauth client attribute values
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OauthClientUpdateInput {

  /**
   * @autogenerated UpdateInputGenerator
   */
  public static class OauthClientUpdateInputBuilder {

    /**
     * Append AllowAllScopes crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param allowAllScopes The AllowAllScopes value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder allowAllScopes(final Boolean allowAllScopes) {
      return allowAllScopes(Optional.of(AllowAllScopesVO.from(allowAllScopes)));
    }

    /**
     * Append AllowAllScopes value
     *
     * @autogenerated UpdateInputGenerator
     * @param allowAllScopes The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder allowAllScopes(
        final Optional<AllowAllScopesVO> allowAllScopes) {
      this.allowAllScopes$value = allowAllScopes;
      this.allowAllScopes$set = true;
      return this;
    }

    /**
     * Append AllowedAudiences crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param allowedAudiences The AllowedAudiences value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder allowedAudiences(
        final List<AllowedAudiences> allowedAudiences) {
      return allowedAudiences(Optional.of(AllowedAudiencesVO.from(allowedAudiences)));
    }

    /**
     * Append AllowedAudiences value
     *
     * @autogenerated UpdateInputGenerator
     * @param allowedAudiences The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder allowedAudiences(
        final Optional<AllowedAudiencesVO> allowedAudiences) {
      this.allowedAudiences$value = allowedAudiences;
      this.allowedAudiences$set = true;
      return this;
    }

    /**
     * Append AllowedLoginRedirects crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param allowedLoginRedirects The AllowedLoginRedirects value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder allowedLoginRedirects(
        final List<AllowedLoginRedirects> allowedLoginRedirects) {
      return allowedLoginRedirects(
          Optional.of(AllowedLoginRedirectsVO.from(allowedLoginRedirects)));
    }

    /**
     * Append AllowedLoginRedirects value
     *
     * @autogenerated UpdateInputGenerator
     * @param allowedLoginRedirects The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder allowedLoginRedirects(
        final Optional<AllowedLoginRedirectsVO> allowedLoginRedirects) {
      this.allowedLoginRedirects$value = allowedLoginRedirects;
      this.allowedLoginRedirects$set = true;
      return this;
    }

    /**
     * Append AllowedLogoutRedirects crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param allowedLogoutRedirects The AllowedLogoutRedirects value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder allowedLogoutRedirects(
        final List<AllowedLogoutRedirects> allowedLogoutRedirects) {
      return allowedLogoutRedirects(
          Optional.of(AllowedLogoutRedirectsVO.from(allowedLogoutRedirects)));
    }

    /**
     * Append AllowedLogoutRedirects value
     *
     * @autogenerated UpdateInputGenerator
     * @param allowedLogoutRedirects The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder allowedLogoutRedirects(
        final Optional<AllowedLogoutRedirectsVO> allowedLogoutRedirects) {
      this.allowedLogoutRedirects$value = allowedLogoutRedirects;
      this.allowedLogoutRedirects$set = true;
      return this;
    }

    /**
     * Append AuthorizationDetailsTypes crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param authorizationDetailsTypes The AuthorizationDetailsTypes value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder authorizationDetailsTypes(
        final String authorizationDetailsTypes) {
      return authorizationDetailsTypes(
          Optional.of(AuthorizationDetailsTypesVO.from(authorizationDetailsTypes)));
    }

    /**
     * Append AuthorizationDetailsTypes value
     *
     * @autogenerated UpdateInputGenerator
     * @param authorizationDetailsTypes The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder authorizationDetailsTypes(
        final Optional<AuthorizationDetailsTypesVO> authorizationDetailsTypes) {
      this.authorizationDetailsTypes$value = authorizationDetailsTypes;
      this.authorizationDetailsTypes$set = true;
      return this;
    }

    /**
     * Append AuthorizationSignedResponseAlg crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param authorizationSignedResponseAlg The AuthorizationSignedResponseAlg value to wrap in a
     *        value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder authorizationSignedResponseAlg(
        final String authorizationSignedResponseAlg) {
      return authorizationSignedResponseAlg(
          Optional.of(AuthorizationSignedResponseAlgVO.from(authorizationSignedResponseAlg)));
    }

    /**
     * Append AuthorizationSignedResponseAlg value
     *
     * @autogenerated UpdateInputGenerator
     * @param authorizationSignedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder authorizationSignedResponseAlg(
        final Optional<AuthorizationSignedResponseAlgVO> authorizationSignedResponseAlg) {
      this.authorizationSignedResponseAlg$value = authorizationSignedResponseAlg;
      this.authorizationSignedResponseAlg$set = true;
      return this;
    }

    /**
     * Append BackchannelClientNotificationEndpoint crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param backchannelClientNotificationEndpoint The BackchannelClientNotificationEndpoint value
     *        to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder backchannelClientNotificationEndpoint(
        final String backchannelClientNotificationEndpoint) {
      return backchannelClientNotificationEndpoint(Optional
          .of(BackchannelClientNotificationEndpointVO.from(backchannelClientNotificationEndpoint)));
    }

    /**
     * Append BackchannelClientNotificationEndpoint value
     *
     * @autogenerated UpdateInputGenerator
     * @param backchannelClientNotificationEndpoint The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder backchannelClientNotificationEndpoint(
        final Optional<BackchannelClientNotificationEndpointVO> backchannelClientNotificationEndpoint) {
      this.backchannelClientNotificationEndpoint$value = backchannelClientNotificationEndpoint;
      this.backchannelClientNotificationEndpoint$set = true;
      return this;
    }

    /**
     * Append BackchannelLogoutSessionRequired crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param backchannelLogoutSessionRequired The BackchannelLogoutSessionRequired value to wrap in
     *        a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder backchannelLogoutSessionRequired(
        final Boolean backchannelLogoutSessionRequired) {
      return backchannelLogoutSessionRequired(
          Optional.of(BackchannelLogoutSessionRequiredVO.from(backchannelLogoutSessionRequired)));
    }

    /**
     * Append BackchannelLogoutSessionRequired value
     *
     * @autogenerated UpdateInputGenerator
     * @param backchannelLogoutSessionRequired The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder backchannelLogoutSessionRequired(
        final Optional<BackchannelLogoutSessionRequiredVO> backchannelLogoutSessionRequired) {
      this.backchannelLogoutSessionRequired$value = backchannelLogoutSessionRequired;
      this.backchannelLogoutSessionRequired$set = true;
      return this;
    }

    /**
     * Append BackchannelLogoutUri crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param backchannelLogoutUri The BackchannelLogoutUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder backchannelLogoutUri(final String backchannelLogoutUri) {
      return backchannelLogoutUri(Optional.of(BackchannelLogoutUriVO.from(backchannelLogoutUri)));
    }

    /**
     * Append BackchannelLogoutUri value
     *
     * @autogenerated UpdateInputGenerator
     * @param backchannelLogoutUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder backchannelLogoutUri(
        final Optional<BackchannelLogoutUriVO> backchannelLogoutUri) {
      this.backchannelLogoutUri$value = backchannelLogoutUri;
      this.backchannelLogoutUri$set = true;
      return this;
    }

    /**
     * Append BackchannelTokenDeliveryMode crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param backchannelTokenDeliveryMode The BackchannelTokenDeliveryMode value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder backchannelTokenDeliveryMode(
        final OauthClientBackchannelTokenDeliveryModeOptions backchannelTokenDeliveryMode) {
      return backchannelTokenDeliveryMode(
          Optional.of(BackchannelTokenDeliveryModeVO.from(backchannelTokenDeliveryMode)));
    }

    /**
     * Append BackchannelTokenDeliveryMode value
     *
     * @autogenerated UpdateInputGenerator
     * @param backchannelTokenDeliveryMode The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder backchannelTokenDeliveryMode(
        final Optional<BackchannelTokenDeliveryModeVO> backchannelTokenDeliveryMode) {
      this.backchannelTokenDeliveryMode$value = backchannelTokenDeliveryMode;
      this.backchannelTokenDeliveryMode$set = true;
      return this;
    }

    /**
     * Append ClientCredentialsScopes crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param clientCredentialsScopes The ClientCredentialsScopes value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder clientCredentialsScopes(
        final String clientCredentialsScopes) {
      return clientCredentialsScopes(
          Optional.of(ClientCredentialsScopesVO.from(clientCredentialsScopes)));
    }

    /**
     * Append ClientCredentialsScopes value
     *
     * @autogenerated UpdateInputGenerator
     * @param clientCredentialsScopes The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder clientCredentialsScopes(
        final Optional<ClientCredentialsScopesVO> clientCredentialsScopes) {
      this.clientCredentialsScopes$value = clientCredentialsScopes;
      this.clientCredentialsScopes$set = true;
      return this;
    }

    /**
     * Append ClientCredentialsTokenTtlSeconds crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param clientCredentialsTokenTtlSeconds The ClientCredentialsTokenTtlSeconds value to wrap in
     *        a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder clientCredentialsTokenTtlSeconds(
        final Integer clientCredentialsTokenTtlSeconds) {
      return clientCredentialsTokenTtlSeconds(
          Optional.of(ClientCredentialsTokenTtlSecondsVO.from(clientCredentialsTokenTtlSeconds)));
    }

    /**
     * Append ClientCredentialsTokenTtlSeconds value
     *
     * @autogenerated UpdateInputGenerator
     * @param clientCredentialsTokenTtlSeconds The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder clientCredentialsTokenTtlSeconds(
        final Optional<ClientCredentialsTokenTtlSecondsVO> clientCredentialsTokenTtlSeconds) {
      this.clientCredentialsTokenTtlSeconds$value = clientCredentialsTokenTtlSeconds;
      this.clientCredentialsTokenTtlSeconds$set = true;
      return this;
    }

    /**
     * Append ClientName crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param clientName The ClientName value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder clientName(final String clientName) {
      return clientName(Optional.of(ClientNameVO.from(clientName)));
    }

    /**
     * Append ClientName value
     *
     * @autogenerated UpdateInputGenerator
     * @param clientName The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder clientName(final Optional<ClientNameVO> clientName) {
      this.clientName$value = clientName;
      this.clientName$set = true;
      return this;
    }

    /**
     * Append ClientUri crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param clientUri The ClientUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder clientUri(final String clientUri) {
      return clientUri(Optional.of(ClientUriVO.from(clientUri)));
    }

    /**
     * Append ClientUri value
     *
     * @autogenerated UpdateInputGenerator
     * @param clientUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder clientUri(final Optional<ClientUriVO> clientUri) {
      this.clientUri$value = clientUri;
      this.clientUri$set = true;
      return this;
    }

    /**
     * Append Code crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param code The Code value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder code(final String code) {
      return code(Optional.of(CodeVO.from(code)));
    }

    /**
     * Append Code value
     *
     * @autogenerated UpdateInputGenerator
     * @param code The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder code(final Optional<CodeVO> code) {
      this.code$value = code;
      this.code$set = true;
      return this;
    }

    /**
     * Append DefaultAcrValues crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param defaultAcrValues The DefaultAcrValues value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder defaultAcrValues(final String defaultAcrValues) {
      return defaultAcrValues(Optional.of(DefaultAcrValuesVO.from(defaultAcrValues)));
    }

    /**
     * Append DefaultAcrValues value
     *
     * @autogenerated UpdateInputGenerator
     * @param defaultAcrValues The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder defaultAcrValues(
        final Optional<DefaultAcrValuesVO> defaultAcrValues) {
      this.defaultAcrValues$value = defaultAcrValues;
      this.defaultAcrValues$set = true;
      return this;
    }

    /**
     * Append DefaultMaxAge crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param defaultMaxAge The DefaultMaxAge value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder defaultMaxAge(final Integer defaultMaxAge) {
      return defaultMaxAge(Optional.of(DefaultMaxAgeVO.from(defaultMaxAge)));
    }

    /**
     * Append DefaultMaxAge value
     *
     * @autogenerated UpdateInputGenerator
     * @param defaultMaxAge The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder defaultMaxAge(
        final Optional<DefaultMaxAgeVO> defaultMaxAge) {
      this.defaultMaxAge$value = defaultMaxAge;
      this.defaultMaxAge$set = true;
      return this;
    }

    /**
     * Append DpopBoundAccessTokens crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param dpopBoundAccessTokens The DpopBoundAccessTokens value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder dpopBoundAccessTokens(
        final Boolean dpopBoundAccessTokens) {
      return dpopBoundAccessTokens(
          Optional.of(DpopBoundAccessTokensVO.from(dpopBoundAccessTokens)));
    }

    /**
     * Append DpopBoundAccessTokens value
     *
     * @autogenerated UpdateInputGenerator
     * @param dpopBoundAccessTokens The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder dpopBoundAccessTokens(
        final Optional<DpopBoundAccessTokensVO> dpopBoundAccessTokens) {
      this.dpopBoundAccessTokens$value = dpopBoundAccessTokens;
      this.dpopBoundAccessTokens$set = true;
      return this;
    }

    /**
     * Append DynamicallyRegistered crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param dynamicallyRegistered The DynamicallyRegistered value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder dynamicallyRegistered(
        final Boolean dynamicallyRegistered) {
      return dynamicallyRegistered(
          Optional.of(DynamicallyRegisteredVO.from(dynamicallyRegistered)));
    }

    /**
     * Append DynamicallyRegistered value
     *
     * @autogenerated UpdateInputGenerator
     * @param dynamicallyRegistered The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder dynamicallyRegistered(
        final Optional<DynamicallyRegisteredVO> dynamicallyRegistered) {
      this.dynamicallyRegistered$value = dynamicallyRegistered;
      this.dynamicallyRegistered$set = true;
      return this;
    }

    /**
     * Append Enabled crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param enabled The Enabled value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder enabled(final Boolean enabled) {
      return enabled(Optional.of(EnabledVO.from(enabled)));
    }

    /**
     * Append Enabled value
     *
     * @autogenerated UpdateInputGenerator
     * @param enabled The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder enabled(final Optional<EnabledVO> enabled) {
      this.enabled$value = enabled;
      this.enabled$set = true;
      return this;
    }

    /**
     * Append FrontchannelLogoutSessionRequired crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param frontchannelLogoutSessionRequired The FrontchannelLogoutSessionRequired value to wrap
     *        in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder frontchannelLogoutSessionRequired(
        final Boolean frontchannelLogoutSessionRequired) {
      return frontchannelLogoutSessionRequired(
          Optional.of(FrontchannelLogoutSessionRequiredVO.from(frontchannelLogoutSessionRequired)));
    }

    /**
     * Append FrontchannelLogoutSessionRequired value
     *
     * @autogenerated UpdateInputGenerator
     * @param frontchannelLogoutSessionRequired The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder frontchannelLogoutSessionRequired(
        final Optional<FrontchannelLogoutSessionRequiredVO> frontchannelLogoutSessionRequired) {
      this.frontchannelLogoutSessionRequired$value = frontchannelLogoutSessionRequired;
      this.frontchannelLogoutSessionRequired$set = true;
      return this;
    }

    /**
     * Append FrontchannelLogoutUri crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param frontchannelLogoutUri The FrontchannelLogoutUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder frontchannelLogoutUri(final String frontchannelLogoutUri) {
      return frontchannelLogoutUri(
          Optional.of(FrontchannelLogoutUriVO.from(frontchannelLogoutUri)));
    }

    /**
     * Append FrontchannelLogoutUri value
     *
     * @autogenerated UpdateInputGenerator
     * @param frontchannelLogoutUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder frontchannelLogoutUri(
        final Optional<FrontchannelLogoutUriVO> frontchannelLogoutUri) {
      this.frontchannelLogoutUri$value = frontchannelLogoutUri;
      this.frontchannelLogoutUri$set = true;
      return this;
    }

    /**
     * Append GrantTypesJson crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param grantTypesJson The GrantTypesJson value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder grantTypesJson(final String grantTypesJson) {
      return grantTypesJson(Optional.of(GrantTypesJsonVO.from(grantTypesJson)));
    }

    /**
     * Append GrantTypesJson value
     *
     * @autogenerated UpdateInputGenerator
     * @param grantTypesJson The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder grantTypesJson(
        final Optional<GrantTypesJsonVO> grantTypesJson) {
      this.grantTypesJson$value = grantTypesJson;
      this.grantTypesJson$set = true;
      return this;
    }

    /**
     * Append IdTokenEncryptedResponseAlg crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param idTokenEncryptedResponseAlg The IdTokenEncryptedResponseAlg value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder idTokenEncryptedResponseAlg(
        final String idTokenEncryptedResponseAlg) {
      return idTokenEncryptedResponseAlg(
          Optional.of(IdTokenEncryptedResponseAlgVO.from(idTokenEncryptedResponseAlg)));
    }

    /**
     * Append IdTokenEncryptedResponseAlg value
     *
     * @autogenerated UpdateInputGenerator
     * @param idTokenEncryptedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder idTokenEncryptedResponseAlg(
        final Optional<IdTokenEncryptedResponseAlgVO> idTokenEncryptedResponseAlg) {
      this.idTokenEncryptedResponseAlg$value = idTokenEncryptedResponseAlg;
      this.idTokenEncryptedResponseAlg$set = true;
      return this;
    }

    /**
     * Append IdTokenEncryptedResponseEnc crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param idTokenEncryptedResponseEnc The IdTokenEncryptedResponseEnc value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder idTokenEncryptedResponseEnc(
        final String idTokenEncryptedResponseEnc) {
      return idTokenEncryptedResponseEnc(
          Optional.of(IdTokenEncryptedResponseEncVO.from(idTokenEncryptedResponseEnc)));
    }

    /**
     * Append IdTokenEncryptedResponseEnc value
     *
     * @autogenerated UpdateInputGenerator
     * @param idTokenEncryptedResponseEnc The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder idTokenEncryptedResponseEnc(
        final Optional<IdTokenEncryptedResponseEncVO> idTokenEncryptedResponseEnc) {
      this.idTokenEncryptedResponseEnc$value = idTokenEncryptedResponseEnc;
      this.idTokenEncryptedResponseEnc$set = true;
      return this;
    }

    /**
     * Append IdTokenSignedResponseAlg crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param idTokenSignedResponseAlg The IdTokenSignedResponseAlg value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder idTokenSignedResponseAlg(
        final OauthClientIdTokenSignedResponseAlgOptions idTokenSignedResponseAlg) {
      return idTokenSignedResponseAlg(
          Optional.of(IdTokenSignedResponseAlgVO.from(idTokenSignedResponseAlg)));
    }

    /**
     * Append IdTokenSignedResponseAlg value
     *
     * @autogenerated UpdateInputGenerator
     * @param idTokenSignedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder idTokenSignedResponseAlg(
        final Optional<IdTokenSignedResponseAlgVO> idTokenSignedResponseAlg) {
      this.idTokenSignedResponseAlg$value = idTokenSignedResponseAlg;
      this.idTokenSignedResponseAlg$set = true;
      return this;
    }

    /**
     * Append IntrospectionAllowed crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param introspectionAllowed The IntrospectionAllowed value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder introspectionAllowed(final Boolean introspectionAllowed) {
      return introspectionAllowed(Optional.of(IntrospectionAllowedVO.from(introspectionAllowed)));
    }

    /**
     * Append IntrospectionAllowed value
     *
     * @autogenerated UpdateInputGenerator
     * @param introspectionAllowed The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder introspectionAllowed(
        final Optional<IntrospectionAllowedVO> introspectionAllowed) {
      this.introspectionAllowed$value = introspectionAllowed;
      this.introspectionAllowed$set = true;
      return this;
    }

    /**
     * Append JwksJson crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param jwksJson The JwksJson value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder jwksJson(final String jwksJson) {
      return jwksJson(Optional.of(JwksJsonVO.from(jwksJson)));
    }

    /**
     * Append JwksJson value
     *
     * @autogenerated UpdateInputGenerator
     * @param jwksJson The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder jwksJson(final Optional<JwksJsonVO> jwksJson) {
      this.jwksJson$value = jwksJson;
      this.jwksJson$set = true;
      return this;
    }

    /**
     * Append JwksUri crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param jwksUri The JwksUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder jwksUri(final String jwksUri) {
      return jwksUri(Optional.of(JwksUriVO.from(jwksUri)));
    }

    /**
     * Append JwksUri value
     *
     * @autogenerated UpdateInputGenerator
     * @param jwksUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder jwksUri(final Optional<JwksUriVO> jwksUri) {
      this.jwksUri$value = jwksUri;
      this.jwksUri$set = true;
      return this;
    }

    /**
     * Append LogoUri crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param logoUri The LogoUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder logoUri(final String logoUri) {
      return logoUri(Optional.of(LogoUriVO.from(logoUri)));
    }

    /**
     * Append LogoUri value
     *
     * @autogenerated UpdateInputGenerator
     * @param logoUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder logoUri(final Optional<LogoUriVO> logoUri) {
      this.logoUri$value = logoUri;
      this.logoUri$set = true;
      return this;
    }

    /**
     * @autogenerated UpdateInputGenerator
     * @return
     */
    public OauthClientUpdateInputBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * Append PolicyUri crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param policyUri The PolicyUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder policyUri(final String policyUri) {
      return policyUri(Optional.of(PolicyUriVO.from(policyUri)));
    }

    /**
     * Append PolicyUri value
     *
     * @autogenerated UpdateInputGenerator
     * @param policyUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder policyUri(final Optional<PolicyUriVO> policyUri) {
      this.policyUri$value = policyUri;
      this.policyUri$set = true;
      return this;
    }

    /**
     * Append RegisteredAt crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param registeredAt The RegisteredAt value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder registeredAt(final OffsetDateTime registeredAt) {
      return registeredAt(Optional.of(RegisteredAtVO.from(registeredAt)));
    }

    /**
     * Append RegisteredAt value
     *
     * @autogenerated UpdateInputGenerator
     * @param registeredAt The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder registeredAt(final Optional<RegisteredAtVO> registeredAt) {
      this.registeredAt$value = registeredAt;
      this.registeredAt$set = true;
      return this;
    }

    /**
     * Append RegistrationAccessToken crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param registrationAccessToken The RegistrationAccessToken value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder registrationAccessToken(
        final String registrationAccessToken) {
      return registrationAccessToken(
          Optional.of(RegistrationAccessTokenVO.fromPlain(registrationAccessToken)));
    }

    /**
     * Append RegistrationAccessToken value
     *
     * @autogenerated UpdateInputGenerator
     * @param registrationAccessToken The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder registrationAccessToken(
        final Optional<RegistrationAccessTokenVO> registrationAccessToken) {
      this.registrationAccessToken$value = registrationAccessToken;
      this.registrationAccessToken$set = true;
      return this;
    }

    /**
     * Append RequestObjectSigningAlg crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param requestObjectSigningAlg The RequestObjectSigningAlg value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder requestObjectSigningAlg(
        final String requestObjectSigningAlg) {
      return requestObjectSigningAlg(
          Optional.of(RequestObjectSigningAlgVO.from(requestObjectSigningAlg)));
    }

    /**
     * Append RequestObjectSigningAlg value
     *
     * @autogenerated UpdateInputGenerator
     * @param requestObjectSigningAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder requestObjectSigningAlg(
        final Optional<RequestObjectSigningAlgVO> requestObjectSigningAlg) {
      this.requestObjectSigningAlg$value = requestObjectSigningAlg;
      this.requestObjectSigningAlg$set = true;
      return this;
    }

    /**
     * Append RequirePkce crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param requirePkce The RequirePkce value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder requirePkce(final Boolean requirePkce) {
      return requirePkce(Optional.of(RequirePkceVO.from(requirePkce)));
    }

    /**
     * Append RequirePkce value
     *
     * @autogenerated UpdateInputGenerator
     * @param requirePkce The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder requirePkce(final Optional<RequirePkceVO> requirePkce) {
      this.requirePkce$value = requirePkce;
      this.requirePkce$set = true;
      return this;
    }

    /**
     * Append RequirePushedAuthorizationRequests crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param requirePushedAuthorizationRequests The RequirePushedAuthorizationRequests value to
     *        wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder requirePushedAuthorizationRequests(
        final Boolean requirePushedAuthorizationRequests) {
      return requirePushedAuthorizationRequests(Optional
          .of(RequirePushedAuthorizationRequestsVO.from(requirePushedAuthorizationRequests)));
    }

    /**
     * Append RequirePushedAuthorizationRequests value
     *
     * @autogenerated UpdateInputGenerator
     * @param requirePushedAuthorizationRequests The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder requirePushedAuthorizationRequests(
        final Optional<RequirePushedAuthorizationRequestsVO> requirePushedAuthorizationRequests) {
      this.requirePushedAuthorizationRequests$value = requirePushedAuthorizationRequests;
      this.requirePushedAuthorizationRequests$set = true;
      return this;
    }

    /**
     * Append ResponseTypesJson crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param responseTypesJson The ResponseTypesJson value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder responseTypesJson(final String responseTypesJson) {
      return responseTypesJson(Optional.of(ResponseTypesJsonVO.from(responseTypesJson)));
    }

    /**
     * Append ResponseTypesJson value
     *
     * @autogenerated UpdateInputGenerator
     * @param responseTypesJson The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder responseTypesJson(
        final Optional<ResponseTypesJsonVO> responseTypesJson) {
      this.responseTypesJson$value = responseTypesJson;
      this.responseTypesJson$set = true;
      return this;
    }

    /**
     * Append SecretOauth crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param secretOauth The SecretOauth value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder secretOauth(final String secretOauth) {
      return secretOauth(Optional.of(SecretOauthVO.fromPlain(secretOauth)));
    }

    /**
     * Append SecretOauth value
     *
     * @autogenerated UpdateInputGenerator
     * @param secretOauth The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder secretOauth(final Optional<SecretOauthVO> secretOauth) {
      this.secretOauth$value = secretOauth;
      this.secretOauth$set = true;
      return this;
    }

    /**
     * Append SectorIdentifierUri crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param sectorIdentifierUri The SectorIdentifierUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder sectorIdentifierUri(final String sectorIdentifierUri) {
      return sectorIdentifierUri(Optional.of(SectorIdentifierUriVO.from(sectorIdentifierUri)));
    }

    /**
     * Append SectorIdentifierUri value
     *
     * @autogenerated UpdateInputGenerator
     * @param sectorIdentifierUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder sectorIdentifierUri(
        final Optional<SectorIdentifierUriVO> sectorIdentifierUri) {
      this.sectorIdentifierUri$value = sectorIdentifierUri;
      this.sectorIdentifierUri$set = true;
      return this;
    }

    /**
     * Append SubjectType crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param subjectType The SubjectType value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder subjectType(
        final OauthClientSubjectTypeOptions subjectType) {
      return subjectType(Optional.of(SubjectTypeVO.from(subjectType)));
    }

    /**
     * Append SubjectType value
     *
     * @autogenerated UpdateInputGenerator
     * @param subjectType The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder subjectType(final Optional<SubjectTypeVO> subjectType) {
      this.subjectType$value = subjectType;
      this.subjectType$set = true;
      return this;
    }

    /**
     * Append Tenant crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param tenant The Tenant value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tenant(final TenantRef tenant) {
      return tenant(Optional.of(TenantVO.from(tenant)));
    }

    /**
     * Append Tenant value
     *
     * @autogenerated UpdateInputGenerator
     * @param tenant The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tenant(final Optional<TenantVO> tenant) {
      this.tenant$value = tenant;
      this.tenant$set = true;
      return this;
    }

    /**
     * Append TlsClientAuthSubjectDn crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param tlsClientAuthSubjectDn The TlsClientAuthSubjectDn value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tlsClientAuthSubjectDn(
        final String tlsClientAuthSubjectDn) {
      return tlsClientAuthSubjectDn(
          Optional.of(TlsClientAuthSubjectDnVO.from(tlsClientAuthSubjectDn)));
    }

    /**
     * Append TlsClientAuthSubjectDn value
     *
     * @autogenerated UpdateInputGenerator
     * @param tlsClientAuthSubjectDn The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tlsClientAuthSubjectDn(
        final Optional<TlsClientAuthSubjectDnVO> tlsClientAuthSubjectDn) {
      this.tlsClientAuthSubjectDn$value = tlsClientAuthSubjectDn;
      this.tlsClientAuthSubjectDn$set = true;
      return this;
    }

    /**
     * Append TlsClientCertificateBoundAccessTokens crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param tlsClientCertificateBoundAccessTokens The TlsClientCertificateBoundAccessTokens value
     *        to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tlsClientCertificateBoundAccessTokens(
        final Boolean tlsClientCertificateBoundAccessTokens) {
      return tlsClientCertificateBoundAccessTokens(Optional
          .of(TlsClientCertificateBoundAccessTokensVO.from(tlsClientCertificateBoundAccessTokens)));
    }

    /**
     * Append TlsClientCertificateBoundAccessTokens value
     *
     * @autogenerated UpdateInputGenerator
     * @param tlsClientCertificateBoundAccessTokens The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tlsClientCertificateBoundAccessTokens(
        final Optional<TlsClientCertificateBoundAccessTokensVO> tlsClientCertificateBoundAccessTokens) {
      this.tlsClientCertificateBoundAccessTokens$value = tlsClientCertificateBoundAccessTokens;
      this.tlsClientCertificateBoundAccessTokens$set = true;
      return this;
    }

    /**
     * Append TokenEndpointAuthMethod crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param tokenEndpointAuthMethod The TokenEndpointAuthMethod value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tokenEndpointAuthMethod(
        final OauthClientTokenEndpointAuthMethodOptions tokenEndpointAuthMethod) {
      return tokenEndpointAuthMethod(
          Optional.of(TokenEndpointAuthMethodVO.from(tokenEndpointAuthMethod)));
    }

    /**
     * Append TokenEndpointAuthMethod value
     *
     * @autogenerated UpdateInputGenerator
     * @param tokenEndpointAuthMethod The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tokenEndpointAuthMethod(
        final Optional<TokenEndpointAuthMethodVO> tokenEndpointAuthMethod) {
      this.tokenEndpointAuthMethod$value = tokenEndpointAuthMethod;
      this.tokenEndpointAuthMethod$set = true;
      return this;
    }

    /**
     * Append TokenEndpointAuthSigningAlg crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param tokenEndpointAuthSigningAlg The TokenEndpointAuthSigningAlg value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tokenEndpointAuthSigningAlg(
        final String tokenEndpointAuthSigningAlg) {
      return tokenEndpointAuthSigningAlg(
          Optional.of(TokenEndpointAuthSigningAlgVO.from(tokenEndpointAuthSigningAlg)));
    }

    /**
     * Append TokenEndpointAuthSigningAlg value
     *
     * @autogenerated UpdateInputGenerator
     * @param tokenEndpointAuthSigningAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tokenEndpointAuthSigningAlg(
        final Optional<TokenEndpointAuthSigningAlgVO> tokenEndpointAuthSigningAlg) {
      this.tokenEndpointAuthSigningAlg$value = tokenEndpointAuthSigningAlg;
      this.tokenEndpointAuthSigningAlg$set = true;
      return this;
    }

    /**
     * Append TosUri crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param tosUri The TosUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tosUri(final String tosUri) {
      return tosUri(Optional.of(TosUriVO.from(tosUri)));
    }

    /**
     * Append TosUri value
     *
     * @autogenerated UpdateInputGenerator
     * @param tosUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder tosUri(final Optional<TosUriVO> tosUri) {
      this.tosUri$value = tosUri;
      this.tosUri$set = true;
      return this;
    }

    /**
     * Append Uid crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param uid The Uid value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * Append Uid value
     *
     * @autogenerated UpdateInputGenerator
     * @param uid The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * Append UserinfoEncryptedResponseAlg crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param userinfoEncryptedResponseAlg The UserinfoEncryptedResponseAlg value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder userinfoEncryptedResponseAlg(
        final String userinfoEncryptedResponseAlg) {
      return userinfoEncryptedResponseAlg(
          Optional.of(UserinfoEncryptedResponseAlgVO.from(userinfoEncryptedResponseAlg)));
    }

    /**
     * Append UserinfoEncryptedResponseAlg value
     *
     * @autogenerated UpdateInputGenerator
     * @param userinfoEncryptedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder userinfoEncryptedResponseAlg(
        final Optional<UserinfoEncryptedResponseAlgVO> userinfoEncryptedResponseAlg) {
      this.userinfoEncryptedResponseAlg$value = userinfoEncryptedResponseAlg;
      this.userinfoEncryptedResponseAlg$set = true;
      return this;
    }

    /**
     * Append UserinfoEncryptedResponseEnc crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param userinfoEncryptedResponseEnc The UserinfoEncryptedResponseEnc value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder userinfoEncryptedResponseEnc(
        final String userinfoEncryptedResponseEnc) {
      return userinfoEncryptedResponseEnc(
          Optional.of(UserinfoEncryptedResponseEncVO.from(userinfoEncryptedResponseEnc)));
    }

    /**
     * Append UserinfoEncryptedResponseEnc value
     *
     * @autogenerated UpdateInputGenerator
     * @param userinfoEncryptedResponseEnc The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder userinfoEncryptedResponseEnc(
        final Optional<UserinfoEncryptedResponseEncVO> userinfoEncryptedResponseEnc) {
      this.userinfoEncryptedResponseEnc$value = userinfoEncryptedResponseEnc;
      this.userinfoEncryptedResponseEnc$set = true;
      return this;
    }

    /**
     * Append UserinfoSignedResponseAlg crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param userinfoSignedResponseAlg The UserinfoSignedResponseAlg value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder userinfoSignedResponseAlg(
        final String userinfoSignedResponseAlg) {
      return userinfoSignedResponseAlg(
          Optional.of(UserinfoSignedResponseAlgVO.from(userinfoSignedResponseAlg)));
    }

    /**
     * Append UserinfoSignedResponseAlg value
     *
     * @autogenerated UpdateInputGenerator
     * @param userinfoSignedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder userinfoSignedResponseAlg(
        final Optional<UserinfoSignedResponseAlgVO> userinfoSignedResponseAlg) {
      this.userinfoSignedResponseAlg$value = userinfoSignedResponseAlg;
      this.userinfoSignedResponseAlg$set = true;
      return this;
    }

    /**
     * Append Version crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param version The Version value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * Append Version value
     *
     * @autogenerated UpdateInputGenerator
     * @param version The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public OauthClientUpdateInputBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * @autogenerated UpdateInputGenerator
   * @param input
   * @return
   */
  static OauthClientUpdateInput fromChangeSet(final OauthClientChangeSet input) {
    return OauthClientUpdateInput.builder().uid(input.getUidValue()).code(input.getCodeValue())
        .tenant(input.getTenantValue()).allowAllScopes(input.getAllowAllScopesValue())
        .tokenEndpointAuthMethod(input.getTokenEndpointAuthMethodValue())
        .tokenEndpointAuthSigningAlg(input.getTokenEndpointAuthSigningAlgValue())
        .dpopBoundAccessTokens(input.getDpopBoundAccessTokensValue())
        .idTokenSignedResponseAlg(input.getIdTokenSignedResponseAlgValue())
        .tlsClientAuthSubjectDn(input.getTlsClientAuthSubjectDnValue())
        .tlsClientCertificateBoundAccessTokens(
            input.getTlsClientCertificateBoundAccessTokensValue())
        .authorizationSignedResponseAlg(input.getAuthorizationSignedResponseAlgValue())
        .backchannelTokenDeliveryMode(input.getBackchannelTokenDeliveryModeValue())
        .backchannelClientNotificationEndpoint(
            input.getBackchannelClientNotificationEndpointValue())
        .subjectType(input.getSubjectTypeValue())
        .idTokenEncryptedResponseAlg(input.getIdTokenEncryptedResponseAlgValue())
        .idTokenEncryptedResponseEnc(input.getIdTokenEncryptedResponseEncValue())
        .userinfoEncryptedResponseAlg(input.getUserinfoEncryptedResponseAlgValue())
        .userinfoEncryptedResponseEnc(input.getUserinfoEncryptedResponseEncValue())
        .authorizationDetailsTypes(input.getAuthorizationDetailsTypesValue())
        .userinfoSignedResponseAlg(input.getUserinfoSignedResponseAlgValue())
        .secretOauth(input.getSecretOauthValue())
        .registrationAccessToken(input.getRegistrationAccessTokenValue())
        .defaultAcrValues(input.getDefaultAcrValuesValue())
        .defaultMaxAge(input.getDefaultMaxAgeValue())
        .sectorIdentifierUri(input.getSectorIdentifierUriValue())
        .allowedLoginRedirects(input.getAllowedLoginRedirectsValue())
        .allowedLogoutRedirects(input.getAllowedLogoutRedirectsValue())
        .allowedAudiences(input.getAllowedAudiencesValue())
        .grantTypesJson(input.getGrantTypesJsonValue())
        .introspectionAllowed(input.getIntrospectionAllowedValue())
        .requirePushedAuthorizationRequests(input.getRequirePushedAuthorizationRequestsValue())
        .requirePkce(input.getRequirePkceValue())
        .backchannelLogoutUri(input.getBackchannelLogoutUriValue())
        .backchannelLogoutSessionRequired(input.getBackchannelLogoutSessionRequiredValue())
        .frontchannelLogoutUri(input.getFrontchannelLogoutUriValue())
        .frontchannelLogoutSessionRequired(input.getFrontchannelLogoutSessionRequiredValue())
        .enabled(input.getEnabledValue()).clientName(input.getClientNameValue())
        .logoUri(input.getLogoUriValue()).clientUri(input.getClientUriValue())
        .policyUri(input.getPolicyUriValue()).tosUri(input.getTosUriValue())
        .responseTypesJson(input.getResponseTypesJsonValue())
        .dynamicallyRegistered(input.getDynamicallyRegisteredValue())
        .registeredAt(input.getRegisteredAtValue())
        .clientCredentialsScopes(input.getClientCredentialsScopesValue())
        .clientCredentialsTokenTtlSeconds(input.getClientCredentialsTokenTtlSecondsValue())
        .requestObjectSigningAlg(input.getRequestObjectSigningAlgValue())
        .jwksUri(input.getJwksUriValue()).jwksJson(input.getJwksJsonValue())
        .version(input.getVersionValue()).build();
  }

  /**
   * El allow all scopes de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<AllowAllScopesVO> allowAllScopes = Optional.empty();

  /**
   * El allowed audiences de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<AllowedAudiencesVO> allowedAudiences = Optional.empty();

  /**
   * El allowed login redirects de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<AllowedLoginRedirectsVO> allowedLoginRedirects = Optional.empty();

  /**
   * El allowed logout redirects de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<AllowedLogoutRedirectsVO> allowedLogoutRedirects = Optional.empty();

  /**
   * El authorization details types de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<AuthorizationDetailsTypesVO> authorizationDetailsTypes = Optional.empty();

  /**
   * El authorization signed response alg de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<AuthorizationSignedResponseAlgVO> authorizationSignedResponseAlg =
      Optional.empty();

  /**
   * El backchannel client notification endpoint de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<BackchannelClientNotificationEndpointVO> backchannelClientNotificationEndpoint =
      Optional.empty();

  /**
   * El backchannel logout session required de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<BackchannelLogoutSessionRequiredVO> backchannelLogoutSessionRequired =
      Optional.empty();

  /**
   * El backchannel logout uri de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<BackchannelLogoutUriVO> backchannelLogoutUri = Optional.empty();

  /**
   * El backchannel token delivery mode de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<BackchannelTokenDeliveryModeVO> backchannelTokenDeliveryMode = Optional.empty();

  /**
   * El client credentials scopes de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<ClientCredentialsScopesVO> clientCredentialsScopes = Optional.empty();

  /**
   * El client credentials token ttl seconds de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<ClientCredentialsTokenTtlSecondsVO> clientCredentialsTokenTtlSeconds =
      Optional.empty();

  /**
   * El client name de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<ClientNameVO> clientName = Optional.empty();

  /**
   * El client uri de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<ClientUriVO> clientUri = Optional.empty();

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<CodeVO> code = Optional.empty();

  /**
   * El default acr values de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<DefaultAcrValuesVO> defaultAcrValues = Optional.empty();

  /**
   * El default max age de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<DefaultMaxAgeVO> defaultMaxAge = Optional.empty();

  /**
   * El dpop bound access tokens de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<DpopBoundAccessTokensVO> dpopBoundAccessTokens = Optional.empty();

  /**
   * El dynamically registered de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<DynamicallyRegisteredVO> dynamicallyRegistered = Optional.empty();

  /**
   * The indicator to allow the account be used without deleting it
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<EnabledVO> enabled = Optional.empty();

  /**
   * El frontchannel logout session required de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<FrontchannelLogoutSessionRequiredVO> frontchannelLogoutSessionRequired =
      Optional.empty();

  /**
   * El frontchannel logout uri de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<FrontchannelLogoutUriVO> frontchannelLogoutUri = Optional.empty();

  /**
   * El grant types json de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<GrantTypesJsonVO> grantTypesJson = Optional.empty();

  /**
   * El id token encrypted response alg de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<IdTokenEncryptedResponseAlgVO> idTokenEncryptedResponseAlg = Optional.empty();

  /**
   * El id token encrypted response enc de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<IdTokenEncryptedResponseEncVO> idTokenEncryptedResponseEnc = Optional.empty();

  /**
   * El id token signed response alg de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<IdTokenSignedResponseAlgVO> idTokenSignedResponseAlg = Optional.empty();

  /**
   * If true, this client is allowed to call the token introspection endpoint (POST /introspect) to
   * validate access tokens. Resource servers must authenticate at the introspection endpoint using
   * their client credentials.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<IntrospectionAllowedVO> introspectionAllowed = Optional.empty();

  /**
   * El jwks json de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<JwksJsonVO> jwksJson = Optional.empty();

  /**
   * El jwks uri de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<JwksUriVO> jwksUri = Optional.empty();

  /**
   * El logo uri de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<LogoUriVO> logoUri = Optional.empty();

  /**
   * El policy uri de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<PolicyUriVO> policyUri = Optional.empty();

  /**
   * El registered at de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<RegisteredAtVO> registeredAt = Optional.empty();

  /**
   * El registration access token de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<RegistrationAccessTokenVO> registrationAccessToken = Optional.empty();

  /**
   * El request object signing alg de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<RequestObjectSigningAlgVO> requestObjectSigningAlg = Optional.empty();

  /**
   * If true, every authorization code request from this client must include code_challenge and
   * code_challenge_method=S256. For clients with public-allow=true, PKCE is always required
   * regardless of this flag.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<RequirePkceVO> requirePkce = Optional.empty();

  /**
   * El require pushed authorization requests de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<RequirePushedAuthorizationRequestsVO> requirePushedAuthorizationRequests =
      Optional.empty();

  /**
   * El response types json de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<ResponseTypesJsonVO> responseTypesJson = Optional.empty();

  /**
   * El secret oauth de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<SecretOauthVO> secretOauth = Optional.empty();

  /**
   * El sector identifier uri de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<SectorIdentifierUriVO> sectorIdentifierUri = Optional.empty();

  /**
   * El subject type de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<SubjectTypeVO> subjectType = Optional.empty();

  /**
   * Owner tenant. Null means a global client, manageable only by root.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<TenantVO> tenant = Optional.empty();

  /**
   * El tls client auth subject dn de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<TlsClientAuthSubjectDnVO> tlsClientAuthSubjectDn = Optional.empty();

  /**
   * El tls client certificate bound access tokens de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<TlsClientCertificateBoundAccessTokensVO> tlsClientCertificateBoundAccessTokens =
      Optional.empty();

  /**
   * El token endpoint auth method de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<TokenEndpointAuthMethodVO> tokenEndpointAuthMethod = Optional.empty();

  /**
   * El token endpoint auth signing alg de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<TokenEndpointAuthSigningAlgVO> tokenEndpointAuthSigningAlg = Optional.empty();

  /**
   * El tos uri de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<TosUriVO> tosUri = Optional.empty();

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * El userinfo encrypted response alg de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<UserinfoEncryptedResponseAlgVO> userinfoEncryptedResponseAlg = Optional.empty();

  /**
   * El userinfo encrypted response enc de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<UserinfoEncryptedResponseEncVO> userinfoEncryptedResponseEnc = Optional.empty();

  /**
   * El userinfo signed response alg de oauth client
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<UserinfoSignedResponseAlgVO> userinfoSignedResponseAlg = Optional.empty();

  /**
   * Campo con el número de version de oauth client para controlar bloqueos optimistas
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Inform for a possible change propolsal in AllowAllScopes
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for AllowAllScopes, otherwise the value for
   *         AllowAllScopes
   */
  public Optional<AllowAllScopesVO> getAllowAllScopes() {
    return allowAllScopes;
  }

  /**
   * Inform for a possible change propolsal in AllowedAudiences
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for AllowedAudiences, otherwise the value for
   *         AllowedAudiences
   */
  public Optional<AllowedAudiencesVO> getAllowedAudiences() {
    return allowedAudiences;
  }

  /**
   * Inform for a possible change propolsal in AllowedLoginRedirects
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for AllowedLoginRedirects, otherwise the value for
   *         AllowedLoginRedirects
   */
  public Optional<AllowedLoginRedirectsVO> getAllowedLoginRedirects() {
    return allowedLoginRedirects;
  }

  /**
   * Inform for a possible change propolsal in AllowedLogoutRedirects
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for AllowedLogoutRedirects, otherwise the value
   *         for AllowedLogoutRedirects
   */
  public Optional<AllowedLogoutRedirectsVO> getAllowedLogoutRedirects() {
    return allowedLogoutRedirects;
  }

  /**
   * Inform for a possible change propolsal in AuthorizationDetailsTypes
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for AuthorizationDetailsTypes, otherwise the value
   *         for AuthorizationDetailsTypes
   */
  public Optional<AuthorizationDetailsTypesVO> getAuthorizationDetailsTypes() {
    return authorizationDetailsTypes;
  }

  /**
   * Inform for a possible change propolsal in AuthorizationSignedResponseAlg
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for AuthorizationSignedResponseAlg, otherwise the
   *         value for AuthorizationSignedResponseAlg
   */
  public Optional<AuthorizationSignedResponseAlgVO> getAuthorizationSignedResponseAlg() {
    return authorizationSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in BackchannelClientNotificationEndpoint
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for BackchannelClientNotificationEndpoint,
   *         otherwise the value for BackchannelClientNotificationEndpoint
   */
  public Optional<BackchannelClientNotificationEndpointVO> getBackchannelClientNotificationEndpoint() {
    return backchannelClientNotificationEndpoint;
  }

  /**
   * Inform for a possible change propolsal in BackchannelLogoutSessionRequired
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for BackchannelLogoutSessionRequired, otherwise
   *         the value for BackchannelLogoutSessionRequired
   */
  public Optional<BackchannelLogoutSessionRequiredVO> getBackchannelLogoutSessionRequired() {
    return backchannelLogoutSessionRequired;
  }

  /**
   * Inform for a possible change propolsal in BackchannelLogoutUri
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for BackchannelLogoutUri, otherwise the value for
   *         BackchannelLogoutUri
   */
  public Optional<BackchannelLogoutUriVO> getBackchannelLogoutUri() {
    return backchannelLogoutUri;
  }

  /**
   * Inform for a possible change propolsal in BackchannelTokenDeliveryMode
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for BackchannelTokenDeliveryMode, otherwise the
   *         value for BackchannelTokenDeliveryMode
   */
  public Optional<BackchannelTokenDeliveryModeVO> getBackchannelTokenDeliveryMode() {
    return backchannelTokenDeliveryMode;
  }

  /**
   * Inform for a possible change propolsal in ClientCredentialsScopes
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for ClientCredentialsScopes, otherwise the value
   *         for ClientCredentialsScopes
   */
  public Optional<ClientCredentialsScopesVO> getClientCredentialsScopes() {
    return clientCredentialsScopes;
  }

  /**
   * Inform for a possible change propolsal in ClientCredentialsTokenTtlSeconds
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for ClientCredentialsTokenTtlSeconds, otherwise
   *         the value for ClientCredentialsTokenTtlSeconds
   */
  public Optional<ClientCredentialsTokenTtlSecondsVO> getClientCredentialsTokenTtlSeconds() {
    return clientCredentialsTokenTtlSeconds;
  }

  /**
   * Inform for a possible change propolsal in ClientName
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for ClientName, otherwise the value for ClientName
   */
  public Optional<ClientNameVO> getClientName() {
    return clientName;
  }

  /**
   * Inform for a possible change propolsal in ClientUri
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for ClientUri, otherwise the value for ClientUri
   */
  public Optional<ClientUriVO> getClientUri() {
    return clientUri;
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public Optional<CodeVO> getCode() {
    return code;
  }

  /**
   * Inform for a possible change propolsal in DefaultAcrValues
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for DefaultAcrValues, otherwise the value for
   *         DefaultAcrValues
   */
  public Optional<DefaultAcrValuesVO> getDefaultAcrValues() {
    return defaultAcrValues;
  }

  /**
   * Inform for a possible change propolsal in DefaultMaxAge
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for DefaultMaxAge, otherwise the value for
   *         DefaultMaxAge
   */
  public Optional<DefaultMaxAgeVO> getDefaultMaxAge() {
    return defaultMaxAge;
  }

  /**
   * Inform for a possible change propolsal in DpopBoundAccessTokens
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for DpopBoundAccessTokens, otherwise the value for
   *         DpopBoundAccessTokens
   */
  public Optional<DpopBoundAccessTokensVO> getDpopBoundAccessTokens() {
    return dpopBoundAccessTokens;
  }

  /**
   * Inform for a possible change propolsal in DynamicallyRegistered
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for DynamicallyRegistered, otherwise the value for
   *         DynamicallyRegistered
   */
  public Optional<DynamicallyRegisteredVO> getDynamicallyRegistered() {
    return dynamicallyRegistered;
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Optional<EnabledVO> getEnabled() {
    return enabled;
  }

  /**
   * Inform for a possible change propolsal in FrontchannelLogoutSessionRequired
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for FrontchannelLogoutSessionRequired, otherwise
   *         the value for FrontchannelLogoutSessionRequired
   */
  public Optional<FrontchannelLogoutSessionRequiredVO> getFrontchannelLogoutSessionRequired() {
    return frontchannelLogoutSessionRequired;
  }

  /**
   * Inform for a possible change propolsal in FrontchannelLogoutUri
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for FrontchannelLogoutUri, otherwise the value for
   *         FrontchannelLogoutUri
   */
  public Optional<FrontchannelLogoutUriVO> getFrontchannelLogoutUri() {
    return frontchannelLogoutUri;
  }

  /**
   * Inform for a possible change propolsal in GrantTypesJson
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for GrantTypesJson, otherwise the value for
   *         GrantTypesJson
   */
  public Optional<GrantTypesJsonVO> getGrantTypesJson() {
    return grantTypesJson;
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseAlg
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseAlg, otherwise the
   *         value for IdTokenEncryptedResponseAlg
   */
  public Optional<IdTokenEncryptedResponseAlgVO> getIdTokenEncryptedResponseAlg() {
    return idTokenEncryptedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseEnc
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseEnc, otherwise the
   *         value for IdTokenEncryptedResponseEnc
   */
  public Optional<IdTokenEncryptedResponseEncVO> getIdTokenEncryptedResponseEnc() {
    return idTokenEncryptedResponseEnc;
  }

  /**
   * Inform for a possible change propolsal in IdTokenSignedResponseAlg
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for IdTokenSignedResponseAlg, otherwise the value
   *         for IdTokenSignedResponseAlg
   */
  public Optional<IdTokenSignedResponseAlgVO> getIdTokenSignedResponseAlg() {
    return idTokenSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in IntrospectionAllowed
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for IntrospectionAllowed, otherwise the value for
   *         IntrospectionAllowed
   */
  public Optional<IntrospectionAllowedVO> getIntrospectionAllowed() {
    return introspectionAllowed;
  }

  /**
   * Inform for a possible change propolsal in JwksJson
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for JwksJson, otherwise the value for JwksJson
   */
  public Optional<JwksJsonVO> getJwksJson() {
    return jwksJson;
  }

  /**
   * Inform for a possible change propolsal in JwksUri
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for JwksUri, otherwise the value for JwksUri
   */
  public Optional<JwksUriVO> getJwksUri() {
    return jwksUri;
  }

  /**
   * Inform for a possible change propolsal in LogoUri
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for LogoUri, otherwise the value for LogoUri
   */
  public Optional<LogoUriVO> getLogoUri() {
    return logoUri;
  }

  /**
   * Inform for a possible change propolsal in PolicyUri
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for PolicyUri, otherwise the value for PolicyUri
   */
  public Optional<PolicyUriVO> getPolicyUri() {
    return policyUri;
  }

  /**
   * Inform for a possible change propolsal in RegisteredAt
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for RegisteredAt, otherwise the value for
   *         RegisteredAt
   */
  public Optional<RegisteredAtVO> getRegisteredAt() {
    return registeredAt;
  }

  /**
   * Inform for a possible change propolsal in RegistrationAccessToken
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for RegistrationAccessToken, otherwise the value
   *         for RegistrationAccessToken
   */
  public Optional<RegistrationAccessTokenVO> getRegistrationAccessToken() {
    return registrationAccessToken;
  }

  /**
   * Inform for a possible change propolsal in RequestObjectSigningAlg
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for RequestObjectSigningAlg, otherwise the value
   *         for RequestObjectSigningAlg
   */
  public Optional<RequestObjectSigningAlgVO> getRequestObjectSigningAlg() {
    return requestObjectSigningAlg;
  }

  /**
   * Inform for a possible change propolsal in RequirePkce
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for RequirePkce, otherwise the value for
   *         RequirePkce
   */
  public Optional<RequirePkceVO> getRequirePkce() {
    return requirePkce;
  }

  /**
   * Inform for a possible change propolsal in RequirePushedAuthorizationRequests
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for RequirePushedAuthorizationRequests, otherwise
   *         the value for RequirePushedAuthorizationRequests
   */
  public Optional<RequirePushedAuthorizationRequestsVO> getRequirePushedAuthorizationRequests() {
    return requirePushedAuthorizationRequests;
  }

  /**
   * Inform for a possible change propolsal in ResponseTypesJson
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for ResponseTypesJson, otherwise the value for
   *         ResponseTypesJson
   */
  public Optional<ResponseTypesJsonVO> getResponseTypesJson() {
    return responseTypesJson;
  }

  /**
   * Inform for a possible change propolsal in SecretOauth
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for SecretOauth, otherwise the value for
   *         SecretOauth
   */
  public Optional<SecretOauthVO> getSecretOauth() {
    return secretOauth;
  }

  /**
   * Inform for a possible change propolsal in SectorIdentifierUri
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for SectorIdentifierUri, otherwise the value for
   *         SectorIdentifierUri
   */
  public Optional<SectorIdentifierUriVO> getSectorIdentifierUri() {
    return sectorIdentifierUri;
  }

  /**
   * Inform for a possible change propolsal in SubjectType
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for SubjectType, otherwise the value for
   *         SubjectType
   */
  public Optional<SubjectTypeVO> getSubjectType() {
    return subjectType;
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public Optional<TenantVO> getTenant() {
    return tenant;
  }

  /**
   * Inform for a possible change propolsal in TlsClientAuthSubjectDn
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for TlsClientAuthSubjectDn, otherwise the value
   *         for TlsClientAuthSubjectDn
   */
  public Optional<TlsClientAuthSubjectDnVO> getTlsClientAuthSubjectDn() {
    return tlsClientAuthSubjectDn;
  }

  /**
   * Inform for a possible change propolsal in TlsClientCertificateBoundAccessTokens
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for TlsClientCertificateBoundAccessTokens,
   *         otherwise the value for TlsClientCertificateBoundAccessTokens
   */
  public Optional<TlsClientCertificateBoundAccessTokensVO> getTlsClientCertificateBoundAccessTokens() {
    return tlsClientCertificateBoundAccessTokens;
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthMethod
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthMethod, otherwise the value
   *         for TokenEndpointAuthMethod
   */
  public Optional<TokenEndpointAuthMethodVO> getTokenEndpointAuthMethod() {
    return tokenEndpointAuthMethod;
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthSigningAlg
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthSigningAlg, otherwise the
   *         value for TokenEndpointAuthSigningAlg
   */
  public Optional<TokenEndpointAuthSigningAlgVO> getTokenEndpointAuthSigningAlg() {
    return tokenEndpointAuthSigningAlg;
  }

  /**
   * Inform for a possible change propolsal in TosUri
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for TosUri, otherwise the value for TosUri
   */
  public Optional<TosUriVO> getTosUri() {
    return tosUri;
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public Optional<UidVO> getUid() {
    return uid;
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseAlg
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseAlg, otherwise the
   *         value for UserinfoEncryptedResponseAlg
   */
  public Optional<UserinfoEncryptedResponseAlgVO> getUserinfoEncryptedResponseAlg() {
    return userinfoEncryptedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseEnc
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseEnc, otherwise the
   *         value for UserinfoEncryptedResponseEnc
   */
  public Optional<UserinfoEncryptedResponseEncVO> getUserinfoEncryptedResponseEnc() {
    return userinfoEncryptedResponseEnc;
  }

  /**
   * Inform for a possible change propolsal in UserinfoSignedResponseAlg
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for UserinfoSignedResponseAlg, otherwise the value
   *         for UserinfoSignedResponseAlg
   */
  public Optional<UserinfoSignedResponseAlgVO> getUserinfoSignedResponseAlg() {
    return userinfoSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Optional<VersionVO> getVersion() {
    return version;
  }

  /**
   * Assigna change proposal for AllowAllScopes to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setAllowAllScopes(final Boolean value) {
    this.allowAllScopes = Optional.of(AllowAllScopesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedAudiences to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setAllowedAudiences(final List<AllowedAudiences> value) {
    this.allowedAudiences = Optional.of(AllowedAudiencesVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedLoginRedirects to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setAllowedLoginRedirects(final List<AllowedLoginRedirects> value) {
    this.allowedLoginRedirects =
        Optional.of(AllowedLoginRedirectsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedLogoutRedirects to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setAllowedLogoutRedirects(
      final List<AllowedLogoutRedirects> value) {
    this.allowedLogoutRedirects =
        Optional.of(AllowedLogoutRedirectsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AuthorizationDetailsTypes to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setAuthorizationDetailsTypes(final String value) {
    this.authorizationDetailsTypes = Optional.of(AuthorizationDetailsTypesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AuthorizationSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setAuthorizationSignedResponseAlg(final String value) {
    this.authorizationSignedResponseAlg = Optional.of(AuthorizationSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelClientNotificationEndpoint to be apply for the
   * aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setBackchannelClientNotificationEndpoint(final String value) {
    this.backchannelClientNotificationEndpoint =
        Optional.of(BackchannelClientNotificationEndpointVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelLogoutSessionRequired to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setBackchannelLogoutSessionRequired(final Boolean value) {
    this.backchannelLogoutSessionRequired =
        Optional.of(BackchannelLogoutSessionRequiredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelLogoutUri to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setBackchannelLogoutUri(final String value) {
    this.backchannelLogoutUri = Optional.of(BackchannelLogoutUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelTokenDeliveryMode to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setBackchannelTokenDeliveryMode(
      final OauthClientBackchannelTokenDeliveryModeOptions value) {
    this.backchannelTokenDeliveryMode = Optional.of(BackchannelTokenDeliveryModeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientCredentialsScopes to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setClientCredentialsScopes(final String value) {
    this.clientCredentialsScopes = Optional.of(ClientCredentialsScopesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientCredentialsTokenTtlSeconds to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setClientCredentialsTokenTtlSeconds(final Integer value) {
    this.clientCredentialsTokenTtlSeconds =
        Optional.of(ClientCredentialsTokenTtlSecondsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientName to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setClientName(final String value) {
    this.clientName = Optional.of(ClientNameVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientUri to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setClientUri(final String value) {
    this.clientUri = Optional.of(ClientUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Code to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setCode(final String value) {
    this.code = Optional.of(CodeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DefaultAcrValues to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setDefaultAcrValues(final String value) {
    this.defaultAcrValues = Optional.of(DefaultAcrValuesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DefaultMaxAge to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setDefaultMaxAge(final Integer value) {
    this.defaultMaxAge = Optional.of(DefaultMaxAgeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DpopBoundAccessTokens to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setDpopBoundAccessTokens(final Boolean value) {
    this.dpopBoundAccessTokens = Optional.of(DpopBoundAccessTokensVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DynamicallyRegistered to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setDynamicallyRegistered(final Boolean value) {
    this.dynamicallyRegistered = Optional.of(DynamicallyRegisteredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Enabled to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for FrontchannelLogoutSessionRequired to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setFrontchannelLogoutSessionRequired(final Boolean value) {
    this.frontchannelLogoutSessionRequired =
        Optional.of(FrontchannelLogoutSessionRequiredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for FrontchannelLogoutUri to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setFrontchannelLogoutUri(final String value) {
    this.frontchannelLogoutUri = Optional.of(FrontchannelLogoutUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for GrantTypesJson to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setGrantTypesJson(final String value) {
    this.grantTypesJson = Optional.of(GrantTypesJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenEncryptedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setIdTokenEncryptedResponseAlg(final String value) {
    this.idTokenEncryptedResponseAlg = Optional.of(IdTokenEncryptedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenEncryptedResponseEnc to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setIdTokenEncryptedResponseEnc(final String value) {
    this.idTokenEncryptedResponseEnc = Optional.of(IdTokenEncryptedResponseEncVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setIdTokenSignedResponseAlg(
      final OauthClientIdTokenSignedResponseAlgOptions value) {
    this.idTokenSignedResponseAlg = Optional.of(IdTokenSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IntrospectionAllowed to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setIntrospectionAllowed(final Boolean value) {
    this.introspectionAllowed = Optional.of(IntrospectionAllowedVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for JwksJson to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setJwksJson(final String value) {
    this.jwksJson = Optional.of(JwksJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for JwksUri to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setJwksUri(final String value) {
    this.jwksUri = Optional.of(JwksUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for LogoUri to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setLogoUri(final String value) {
    this.logoUri = Optional.of(LogoUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for PolicyUri to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setPolicyUri(final String value) {
    this.policyUri = Optional.of(PolicyUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RegisteredAt to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setRegisteredAt(final OffsetDateTime value) {
    this.registeredAt = Optional.of(RegisteredAtVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RegistrationAccessToken to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setRegistrationAccessToken(final String value) {
    this.registrationAccessToken = Optional.of(RegistrationAccessTokenVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for RequestObjectSigningAlg to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setRequestObjectSigningAlg(final String value) {
    this.requestObjectSigningAlg = Optional.of(RequestObjectSigningAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RequirePkce to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setRequirePkce(final Boolean value) {
    this.requirePkce = Optional.of(RequirePkceVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RequirePushedAuthorizationRequests to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setRequirePushedAuthorizationRequests(final Boolean value) {
    this.requirePushedAuthorizationRequests =
        Optional.of(RequirePushedAuthorizationRequestsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ResponseTypesJson to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setResponseTypesJson(final String value) {
    this.responseTypesJson = Optional.of(ResponseTypesJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SecretOauth to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setSecretOauth(final String value) {
    this.secretOauth = Optional.of(SecretOauthVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for SectorIdentifierUri to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setSectorIdentifierUri(final String value) {
    this.sectorIdentifierUri = Optional.of(SectorIdentifierUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SubjectType to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setSubjectType(final OauthClientSubjectTypeOptions value) {
    this.subjectType = Optional.of(SubjectTypeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Tenant to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TlsClientAuthSubjectDn to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setTlsClientAuthSubjectDn(final String value) {
    this.tlsClientAuthSubjectDn = Optional.of(TlsClientAuthSubjectDnVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TlsClientCertificateBoundAccessTokens to be apply for the
   * aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setTlsClientCertificateBoundAccessTokens(final Boolean value) {
    this.tlsClientCertificateBoundAccessTokens =
        Optional.of(TlsClientCertificateBoundAccessTokensVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TokenEndpointAuthMethod to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setTokenEndpointAuthMethod(
      final OauthClientTokenEndpointAuthMethodOptions value) {
    this.tokenEndpointAuthMethod = Optional.of(TokenEndpointAuthMethodVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TokenEndpointAuthSigningAlg to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setTokenEndpointAuthSigningAlg(final String value) {
    this.tokenEndpointAuthSigningAlg = Optional.of(TokenEndpointAuthSigningAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TosUri to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setTosUri(final String value) {
    this.tosUri = Optional.of(TosUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoEncryptedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setUserinfoEncryptedResponseAlg(final String value) {
    this.userinfoEncryptedResponseAlg = Optional.of(UserinfoEncryptedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoEncryptedResponseEnc to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setUserinfoEncryptedResponseEnc(final String value) {
    this.userinfoEncryptedResponseEnc = Optional.of(UserinfoEncryptedResponseEncVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setUserinfoSignedResponseAlg(final String value) {
    this.userinfoSignedResponseAlg = Optional.of(UserinfoSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for OauthClientUpdateInput
   * @return self instance to enable a flow code
   */
  public OauthClientUpdateInput setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * @autogenerated UpdateInputGenerator
   * @return
   */
  OauthClientChangeSet toChangeSet() {
    return new OauthClientChangeSet().uid(uid).code(code).tenant(tenant)
        .allowAllScopes(allowAllScopes).tokenEndpointAuthMethod(tokenEndpointAuthMethod)
        .tokenEndpointAuthSigningAlg(tokenEndpointAuthSigningAlg)
        .dpopBoundAccessTokens(dpopBoundAccessTokens)
        .idTokenSignedResponseAlg(idTokenSignedResponseAlg)
        .tlsClientAuthSubjectDn(tlsClientAuthSubjectDn)
        .tlsClientCertificateBoundAccessTokens(tlsClientCertificateBoundAccessTokens)
        .authorizationSignedResponseAlg(authorizationSignedResponseAlg)
        .backchannelTokenDeliveryMode(backchannelTokenDeliveryMode)
        .backchannelClientNotificationEndpoint(backchannelClientNotificationEndpoint)
        .subjectType(subjectType).idTokenEncryptedResponseAlg(idTokenEncryptedResponseAlg)
        .idTokenEncryptedResponseEnc(idTokenEncryptedResponseEnc)
        .userinfoEncryptedResponseAlg(userinfoEncryptedResponseAlg)
        .userinfoEncryptedResponseEnc(userinfoEncryptedResponseEnc)
        .authorizationDetailsTypes(authorizationDetailsTypes)
        .userinfoSignedResponseAlg(userinfoSignedResponseAlg).secretOauth(secretOauth)
        .registrationAccessToken(registrationAccessToken).defaultAcrValues(defaultAcrValues)
        .defaultMaxAge(defaultMaxAge).sectorIdentifierUri(sectorIdentifierUri)
        .allowedLoginRedirects(allowedLoginRedirects).allowedLogoutRedirects(allowedLogoutRedirects)
        .allowedAudiences(allowedAudiences).grantTypesJson(grantTypesJson)
        .introspectionAllowed(introspectionAllowed)
        .requirePushedAuthorizationRequests(requirePushedAuthorizationRequests)
        .requirePkce(requirePkce).backchannelLogoutUri(backchannelLogoutUri)
        .backchannelLogoutSessionRequired(backchannelLogoutSessionRequired)
        .frontchannelLogoutUri(frontchannelLogoutUri)
        .frontchannelLogoutSessionRequired(frontchannelLogoutSessionRequired).enabled(enabled)
        .clientName(clientName).logoUri(logoUri).clientUri(clientUri).policyUri(policyUri)
        .tosUri(tosUri).responseTypesJson(responseTypesJson)
        .dynamicallyRegistered(dynamicallyRegistered).registeredAt(registeredAt)
        .clientCredentialsScopes(clientCredentialsScopes)
        .clientCredentialsTokenTtlSeconds(clientCredentialsTokenTtlSeconds)
        .requestObjectSigningAlg(requestObjectSigningAlg).jwksUri(jwksUri).jwksJson(jwksJson)
        .version(version);
  }
}
