package net.civeira.phylax.features.access.trustedclient.application.usecase.create;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedAudiences;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.features.access.trustedclient.domain.AllowedRedirects;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientChangeSet;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrustedClientCreateInput {

  /**
   * @autogenerated CreateInputGenerator
   */
  public static class TrustedClientCreateInputBuilder {

    /**
     * Append AllowAllScopes crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param allowAllScopes The AllowAllScopes value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowAllScopes(final Boolean allowAllScopes) {
      return allowAllScopes(Optional.of(AllowAllScopesVO.from(allowAllScopes)));
    }

    /**
     * Append AllowAllScopes value
     *
     * @autogenerated CreateInputGenerator
     * @param allowAllScopes The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowAllScopes(
        final Optional<AllowAllScopesVO> allowAllScopes) {
      this.allowAllScopes$value = allowAllScopes;
      this.allowAllScopes$set = true;
      return this;
    }

    /**
     * Append AllowedAudiences crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param allowedAudiences The AllowedAudiences value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowedAudiences(
        final List<AllowedAudiences> allowedAudiences) {
      return allowedAudiences(Optional.of(AllowedAudiencesVO.from(allowedAudiences)));
    }

    /**
     * Append AllowedAudiences value
     *
     * @autogenerated CreateInputGenerator
     * @param allowedAudiences The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowedAudiences(
        final Optional<AllowedAudiencesVO> allowedAudiences) {
      this.allowedAudiences$value = allowedAudiences;
      this.allowedAudiences$set = true;
      return this;
    }

    /**
     * Append AllowedLogoutRedirects crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param allowedLogoutRedirects The AllowedLogoutRedirects value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowedLogoutRedirects(
        final List<AllowedLogoutRedirects> allowedLogoutRedirects) {
      return allowedLogoutRedirects(
          Optional.of(AllowedLogoutRedirectsVO.from(allowedLogoutRedirects)));
    }

    /**
     * Append AllowedLogoutRedirects value
     *
     * @autogenerated CreateInputGenerator
     * @param allowedLogoutRedirects The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowedLogoutRedirects(
        final Optional<AllowedLogoutRedirectsVO> allowedLogoutRedirects) {
      this.allowedLogoutRedirects$value = allowedLogoutRedirects;
      this.allowedLogoutRedirects$set = true;
      return this;
    }

    /**
     * Append AllowedRedirects crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param allowedRedirects The AllowedRedirects value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowedRedirects(
        final List<AllowedRedirects> allowedRedirects) {
      return allowedRedirects(Optional.of(AllowedRedirectsVO.from(allowedRedirects)));
    }

    /**
     * Append AllowedRedirects value
     *
     * @autogenerated CreateInputGenerator
     * @param allowedRedirects The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowedRedirects(
        final Optional<AllowedRedirectsVO> allowedRedirects) {
      this.allowedRedirects$value = allowedRedirects;
      this.allowedRedirects$set = true;
      return this;
    }

    /**
     * Append AllowedScopesM2m crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param allowedScopesM2m The AllowedScopesM2m value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowedScopesM2m(final String allowedScopesM2m) {
      return allowedScopesM2m(Optional.of(AllowedScopesM2mVO.from(allowedScopesM2m)));
    }

    /**
     * Append AllowedScopesM2m value
     *
     * @autogenerated CreateInputGenerator
     * @param allowedScopesM2m The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder allowedScopesM2m(
        final Optional<AllowedScopesM2mVO> allowedScopesM2m) {
      this.allowedScopesM2m$value = allowedScopesM2m;
      this.allowedScopesM2m$set = true;
      return this;
    }

    /**
     * Append AuthorizationDetailsTypes crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param authorizationDetailsTypes The AuthorizationDetailsTypes value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder authorizationDetailsTypes(
        final String authorizationDetailsTypes) {
      return authorizationDetailsTypes(
          Optional.of(AuthorizationDetailsTypesVO.from(authorizationDetailsTypes)));
    }

    /**
     * Append AuthorizationDetailsTypes value
     *
     * @autogenerated CreateInputGenerator
     * @param authorizationDetailsTypes The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder authorizationDetailsTypes(
        final Optional<AuthorizationDetailsTypesVO> authorizationDetailsTypes) {
      this.authorizationDetailsTypes$value = authorizationDetailsTypes;
      this.authorizationDetailsTypes$set = true;
      return this;
    }

    /**
     * Append AuthorizationSignedResponseAlg crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param authorizationSignedResponseAlg The AuthorizationSignedResponseAlg value to wrap in a
     *        value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder authorizationSignedResponseAlg(
        final String authorizationSignedResponseAlg) {
      return authorizationSignedResponseAlg(
          Optional.of(AuthorizationSignedResponseAlgVO.from(authorizationSignedResponseAlg)));
    }

    /**
     * Append AuthorizationSignedResponseAlg value
     *
     * @autogenerated CreateInputGenerator
     * @param authorizationSignedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder authorizationSignedResponseAlg(
        final Optional<AuthorizationSignedResponseAlgVO> authorizationSignedResponseAlg) {
      this.authorizationSignedResponseAlg$value = authorizationSignedResponseAlg;
      this.authorizationSignedResponseAlg$set = true;
      return this;
    }

    /**
     * Append BackChannelLogoutSessionRequired crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param backChannelLogoutSessionRequired The BackChannelLogoutSessionRequired value to wrap in
     *        a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder backChannelLogoutSessionRequired(
        final Boolean backChannelLogoutSessionRequired) {
      return backChannelLogoutSessionRequired(
          Optional.of(BackChannelLogoutSessionRequiredVO.from(backChannelLogoutSessionRequired)));
    }

    /**
     * Append BackChannelLogoutSessionRequired value
     *
     * @autogenerated CreateInputGenerator
     * @param backChannelLogoutSessionRequired The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder backChannelLogoutSessionRequired(
        final Optional<BackChannelLogoutSessionRequiredVO> backChannelLogoutSessionRequired) {
      this.backChannelLogoutSessionRequired$value = backChannelLogoutSessionRequired;
      this.backChannelLogoutSessionRequired$set = true;
      return this;
    }

    /**
     * Append BackChannelLogoutUri crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param backChannelLogoutUri The BackChannelLogoutUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder backChannelLogoutUri(final String backChannelLogoutUri) {
      return backChannelLogoutUri(Optional.of(BackChannelLogoutUriVO.from(backChannelLogoutUri)));
    }

    /**
     * Append BackChannelLogoutUri value
     *
     * @autogenerated CreateInputGenerator
     * @param backChannelLogoutUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder backChannelLogoutUri(
        final Optional<BackChannelLogoutUriVO> backChannelLogoutUri) {
      this.backChannelLogoutUri$value = backChannelLogoutUri;
      this.backChannelLogoutUri$set = true;
      return this;
    }

    /**
     * Append BackchannelClientNotificationEndpoint crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param backchannelClientNotificationEndpoint The BackchannelClientNotificationEndpoint value
     *        to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder backchannelClientNotificationEndpoint(
        final String backchannelClientNotificationEndpoint) {
      return backchannelClientNotificationEndpoint(Optional
          .of(BackchannelClientNotificationEndpointVO.from(backchannelClientNotificationEndpoint)));
    }

    /**
     * Append BackchannelClientNotificationEndpoint value
     *
     * @autogenerated CreateInputGenerator
     * @param backchannelClientNotificationEndpoint The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder backchannelClientNotificationEndpoint(
        final Optional<BackchannelClientNotificationEndpointVO> backchannelClientNotificationEndpoint) {
      this.backchannelClientNotificationEndpoint$value = backchannelClientNotificationEndpoint;
      this.backchannelClientNotificationEndpoint$set = true;
      return this;
    }

    /**
     * Append BackchannelTokenDeliveryMode crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param backchannelTokenDeliveryMode The BackchannelTokenDeliveryMode value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder backchannelTokenDeliveryMode(
        final TrustedClientBackchannelTokenDeliveryModeOptions backchannelTokenDeliveryMode) {
      return backchannelTokenDeliveryMode(
          Optional.of(BackchannelTokenDeliveryModeVO.from(backchannelTokenDeliveryMode)));
    }

    /**
     * Append BackchannelTokenDeliveryMode value
     *
     * @autogenerated CreateInputGenerator
     * @param backchannelTokenDeliveryMode The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder backchannelTokenDeliveryMode(
        final Optional<BackchannelTokenDeliveryModeVO> backchannelTokenDeliveryMode) {
      this.backchannelTokenDeliveryMode$value = backchannelTokenDeliveryMode;
      this.backchannelTokenDeliveryMode$set = true;
      return this;
    }

    /**
     * Append ClientName crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param clientName The ClientName value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder clientName(final String clientName) {
      return clientName(Optional.of(ClientNameVO.from(clientName)));
    }

    /**
     * Append ClientName value
     *
     * @autogenerated CreateInputGenerator
     * @param clientName The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder clientName(final Optional<ClientNameVO> clientName) {
      this.clientName$value = clientName;
      this.clientName$set = true;
      return this;
    }

    /**
     * Append ClientUri crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param clientUri The ClientUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder clientUri(final String clientUri) {
      return clientUri(Optional.of(ClientUriVO.from(clientUri)));
    }

    /**
     * Append ClientUri value
     *
     * @autogenerated CreateInputGenerator
     * @param clientUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder clientUri(final Optional<ClientUriVO> clientUri) {
      this.clientUri$value = clientUri;
      this.clientUri$set = true;
      return this;
    }

    /**
     * Append Code crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param code The Code value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder code(final String code) {
      return code(Optional.of(CodeVO.from(code)));
    }

    /**
     * Append Code value
     *
     * @autogenerated CreateInputGenerator
     * @param code The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder code(final Optional<CodeVO> code) {
      this.code$value = code;
      this.code$set = true;
      return this;
    }

    /**
     * Append DefaultAcrValues crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param defaultAcrValues The DefaultAcrValues value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder defaultAcrValues(final String defaultAcrValues) {
      return defaultAcrValues(Optional.of(DefaultAcrValuesVO.from(defaultAcrValues)));
    }

    /**
     * Append DefaultAcrValues value
     *
     * @autogenerated CreateInputGenerator
     * @param defaultAcrValues The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder defaultAcrValues(
        final Optional<DefaultAcrValuesVO> defaultAcrValues) {
      this.defaultAcrValues$value = defaultAcrValues;
      this.defaultAcrValues$set = true;
      return this;
    }

    /**
     * Append DefaultMaxAge crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param defaultMaxAge The DefaultMaxAge value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder defaultMaxAge(final Integer defaultMaxAge) {
      return defaultMaxAge(Optional.of(DefaultMaxAgeVO.from(defaultMaxAge)));
    }

    /**
     * Append DefaultMaxAge value
     *
     * @autogenerated CreateInputGenerator
     * @param defaultMaxAge The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder defaultMaxAge(
        final Optional<DefaultMaxAgeVO> defaultMaxAge) {
      this.defaultMaxAge$value = defaultMaxAge;
      this.defaultMaxAge$set = true;
      return this;
    }

    /**
     * Append DpopBoundAccessTokens crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param dpopBoundAccessTokens The DpopBoundAccessTokens value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder dpopBoundAccessTokens(
        final Boolean dpopBoundAccessTokens) {
      return dpopBoundAccessTokens(
          Optional.of(DpopBoundAccessTokensVO.from(dpopBoundAccessTokens)));
    }

    /**
     * Append DpopBoundAccessTokens value
     *
     * @autogenerated CreateInputGenerator
     * @param dpopBoundAccessTokens The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder dpopBoundAccessTokens(
        final Optional<DpopBoundAccessTokensVO> dpopBoundAccessTokens) {
      this.dpopBoundAccessTokens$value = dpopBoundAccessTokens;
      this.dpopBoundAccessTokens$set = true;
      return this;
    }

    /**
     * Append DynamicallyRegistered crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param dynamicallyRegistered The DynamicallyRegistered value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder dynamicallyRegistered(
        final Boolean dynamicallyRegistered) {
      return dynamicallyRegistered(
          Optional.of(DynamicallyRegisteredVO.from(dynamicallyRegistered)));
    }

    /**
     * Append DynamicallyRegistered value
     *
     * @autogenerated CreateInputGenerator
     * @param dynamicallyRegistered The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder dynamicallyRegistered(
        final Optional<DynamicallyRegisteredVO> dynamicallyRegistered) {
      this.dynamicallyRegistered$value = dynamicallyRegistered;
      this.dynamicallyRegistered$set = true;
      return this;
    }

    /**
     * Append Enabled crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param enabled The Enabled value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder enabled(final Boolean enabled) {
      return enabled(Optional.of(EnabledVO.from(enabled)));
    }

    /**
     * Append Enabled value
     *
     * @autogenerated CreateInputGenerator
     * @param enabled The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder enabled(final Optional<EnabledVO> enabled) {
      this.enabled$value = enabled;
      this.enabled$set = true;
      return this;
    }

    /**
     * Append FrontChannelLogoutSessionRequired crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param frontChannelLogoutSessionRequired The FrontChannelLogoutSessionRequired value to wrap
     *        in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder frontChannelLogoutSessionRequired(
        final Boolean frontChannelLogoutSessionRequired) {
      return frontChannelLogoutSessionRequired(
          Optional.of(FrontChannelLogoutSessionRequiredVO.from(frontChannelLogoutSessionRequired)));
    }

    /**
     * Append FrontChannelLogoutSessionRequired value
     *
     * @autogenerated CreateInputGenerator
     * @param frontChannelLogoutSessionRequired The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder frontChannelLogoutSessionRequired(
        final Optional<FrontChannelLogoutSessionRequiredVO> frontChannelLogoutSessionRequired) {
      this.frontChannelLogoutSessionRequired$value = frontChannelLogoutSessionRequired;
      this.frontChannelLogoutSessionRequired$set = true;
      return this;
    }

    /**
     * Append FrontChannelLogoutUri crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param frontChannelLogoutUri The FrontChannelLogoutUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder frontChannelLogoutUri(
        final String frontChannelLogoutUri) {
      return frontChannelLogoutUri(
          Optional.of(FrontChannelLogoutUriVO.from(frontChannelLogoutUri)));
    }

    /**
     * Append FrontChannelLogoutUri value
     *
     * @autogenerated CreateInputGenerator
     * @param frontChannelLogoutUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder frontChannelLogoutUri(
        final Optional<FrontChannelLogoutUriVO> frontChannelLogoutUri) {
      this.frontChannelLogoutUri$value = frontChannelLogoutUri;
      this.frontChannelLogoutUri$set = true;
      return this;
    }

    /**
     * Append GrantTypesJson crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param grantTypesJson The GrantTypesJson value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder grantTypesJson(final String grantTypesJson) {
      return grantTypesJson(Optional.of(GrantTypesJsonVO.from(grantTypesJson)));
    }

    /**
     * Append GrantTypesJson value
     *
     * @autogenerated CreateInputGenerator
     * @param grantTypesJson The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder grantTypesJson(
        final Optional<GrantTypesJsonVO> grantTypesJson) {
      this.grantTypesJson$value = grantTypesJson;
      this.grantTypesJson$set = true;
      return this;
    }

    /**
     * Append IdTokenEncryptedResponseAlg crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param idTokenEncryptedResponseAlg The IdTokenEncryptedResponseAlg value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder idTokenEncryptedResponseAlg(
        final String idTokenEncryptedResponseAlg) {
      return idTokenEncryptedResponseAlg(
          Optional.of(IdTokenEncryptedResponseAlgVO.from(idTokenEncryptedResponseAlg)));
    }

    /**
     * Append IdTokenEncryptedResponseAlg value
     *
     * @autogenerated CreateInputGenerator
     * @param idTokenEncryptedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder idTokenEncryptedResponseAlg(
        final Optional<IdTokenEncryptedResponseAlgVO> idTokenEncryptedResponseAlg) {
      this.idTokenEncryptedResponseAlg$value = idTokenEncryptedResponseAlg;
      this.idTokenEncryptedResponseAlg$set = true;
      return this;
    }

    /**
     * Append IdTokenEncryptedResponseEnc crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param idTokenEncryptedResponseEnc The IdTokenEncryptedResponseEnc value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder idTokenEncryptedResponseEnc(
        final String idTokenEncryptedResponseEnc) {
      return idTokenEncryptedResponseEnc(
          Optional.of(IdTokenEncryptedResponseEncVO.from(idTokenEncryptedResponseEnc)));
    }

    /**
     * Append IdTokenEncryptedResponseEnc value
     *
     * @autogenerated CreateInputGenerator
     * @param idTokenEncryptedResponseEnc The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder idTokenEncryptedResponseEnc(
        final Optional<IdTokenEncryptedResponseEncVO> idTokenEncryptedResponseEnc) {
      this.idTokenEncryptedResponseEnc$value = idTokenEncryptedResponseEnc;
      this.idTokenEncryptedResponseEnc$set = true;
      return this;
    }

    /**
     * Append IdTokenSignedResponseAlg crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param idTokenSignedResponseAlg The IdTokenSignedResponseAlg value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder idTokenSignedResponseAlg(
        final TrustedClientIdTokenSignedResponseAlgOptions idTokenSignedResponseAlg) {
      return idTokenSignedResponseAlg(
          Optional.of(IdTokenSignedResponseAlgVO.from(idTokenSignedResponseAlg)));
    }

    /**
     * Append IdTokenSignedResponseAlg value
     *
     * @autogenerated CreateInputGenerator
     * @param idTokenSignedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder idTokenSignedResponseAlg(
        final Optional<IdTokenSignedResponseAlgVO> idTokenSignedResponseAlg) {
      this.idTokenSignedResponseAlg$value = idTokenSignedResponseAlg;
      this.idTokenSignedResponseAlg$set = true;
      return this;
    }

    /**
     * Append IsResourceServer crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param isResourceServer The IsResourceServer value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder isResourceServer(final Boolean isResourceServer) {
      return isResourceServer(Optional.of(IsResourceServerVO.from(isResourceServer)));
    }

    /**
     * Append IsResourceServer value
     *
     * @autogenerated CreateInputGenerator
     * @param isResourceServer The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder isResourceServer(
        final Optional<IsResourceServerVO> isResourceServer) {
      this.isResourceServer$value = isResourceServer;
      this.isResourceServer$set = true;
      return this;
    }

    /**
     * Append JwksJson crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param jwksJson The JwksJson value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder jwksJson(final String jwksJson) {
      return jwksJson(Optional.of(JwksJsonVO.from(jwksJson)));
    }

    /**
     * Append JwksJson value
     *
     * @autogenerated CreateInputGenerator
     * @param jwksJson The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder jwksJson(final Optional<JwksJsonVO> jwksJson) {
      this.jwksJson$value = jwksJson;
      this.jwksJson$set = true;
      return this;
    }

    /**
     * Append JwksUri crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param jwksUri The JwksUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder jwksUri(final String jwksUri) {
      return jwksUri(Optional.of(JwksUriVO.from(jwksUri)));
    }

    /**
     * Append JwksUri value
     *
     * @autogenerated CreateInputGenerator
     * @param jwksUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder jwksUri(final Optional<JwksUriVO> jwksUri) {
      this.jwksUri$value = jwksUri;
      this.jwksUri$set = true;
      return this;
    }

    /**
     * Append LogoUri crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param logoUri The LogoUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder logoUri(final String logoUri) {
      return logoUri(Optional.of(LogoUriVO.from(logoUri)));
    }

    /**
     * Append LogoUri value
     *
     * @autogenerated CreateInputGenerator
     * @param logoUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder logoUri(final Optional<LogoUriVO> logoUri) {
      this.logoUri$value = logoUri;
      this.logoUri$set = true;
      return this;
    }

    /**
     * Append M2mTokenTtlSeconds crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param m2mTokenTtlSeconds The M2mTokenTtlSeconds value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder m2mTokenTtlSeconds(final Integer m2mTokenTtlSeconds) {
      return m2mTokenTtlSeconds(Optional.of(M2mTokenTtlSecondsVO.from(m2mTokenTtlSeconds)));
    }

    /**
     * Append M2mTokenTtlSeconds value
     *
     * @autogenerated CreateInputGenerator
     * @param m2mTokenTtlSeconds The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder m2mTokenTtlSeconds(
        final Optional<M2mTokenTtlSecondsVO> m2mTokenTtlSeconds) {
      this.m2mTokenTtlSeconds$value = m2mTokenTtlSeconds;
      this.m2mTokenTtlSeconds$set = true;
      return this;
    }

    /**
     * @autogenerated CreateInputGenerator
     * @return
     */
    public TrustedClientCreateInputBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * Append PolicyUri crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param policyUri The PolicyUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder policyUri(final String policyUri) {
      return policyUri(Optional.of(PolicyUriVO.from(policyUri)));
    }

    /**
     * Append PolicyUri value
     *
     * @autogenerated CreateInputGenerator
     * @param policyUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder policyUri(final Optional<PolicyUriVO> policyUri) {
      this.policyUri$value = policyUri;
      this.policyUri$set = true;
      return this;
    }

    /**
     * Append PublicAllow crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param publicAllow The PublicAllow value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder publicAllow(final Boolean publicAllow) {
      return publicAllow(Optional.of(PublicAllowVO.from(publicAllow)));
    }

    /**
     * Append PublicAllow value
     *
     * @autogenerated CreateInputGenerator
     * @param publicAllow The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder publicAllow(final Optional<PublicAllowVO> publicAllow) {
      this.publicAllow$value = publicAllow;
      this.publicAllow$set = true;
      return this;
    }

    /**
     * Append RegisteredAt crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param registeredAt The RegisteredAt value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder registeredAt(final OffsetDateTime registeredAt) {
      return registeredAt(Optional.of(RegisteredAtVO.from(registeredAt)));
    }

    /**
     * Append RegisteredAt value
     *
     * @autogenerated CreateInputGenerator
     * @param registeredAt The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder registeredAt(
        final Optional<RegisteredAtVO> registeredAt) {
      this.registeredAt$value = registeredAt;
      this.registeredAt$set = true;
      return this;
    }

    /**
     * Append RegistrationAccess crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param registrationAccess The RegistrationAccess value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder registrationAccess(final String registrationAccess) {
      return registrationAccess(Optional.of(RegistrationAccessVO.fromPlain(registrationAccess)));
    }

    /**
     * Append RegistrationAccess value
     *
     * @autogenerated CreateInputGenerator
     * @param registrationAccess The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder registrationAccess(
        final Optional<RegistrationAccessVO> registrationAccess) {
      this.registrationAccess$value = registrationAccess;
      this.registrationAccess$set = true;
      return this;
    }

    /**
     * Append RequestObjectSigningAlg crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param requestObjectSigningAlg The RequestObjectSigningAlg value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder requestObjectSigningAlg(
        final String requestObjectSigningAlg) {
      return requestObjectSigningAlg(
          Optional.of(RequestObjectSigningAlgVO.from(requestObjectSigningAlg)));
    }

    /**
     * Append RequestObjectSigningAlg value
     *
     * @autogenerated CreateInputGenerator
     * @param requestObjectSigningAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder requestObjectSigningAlg(
        final Optional<RequestObjectSigningAlgVO> requestObjectSigningAlg) {
      this.requestObjectSigningAlg$value = requestObjectSigningAlg;
      this.requestObjectSigningAlg$set = true;
      return this;
    }

    /**
     * Append RequirePkce crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param requirePkce The RequirePkce value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder requirePkce(final Boolean requirePkce) {
      return requirePkce(Optional.of(RequirePkceVO.from(requirePkce)));
    }

    /**
     * Append RequirePkce value
     *
     * @autogenerated CreateInputGenerator
     * @param requirePkce The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder requirePkce(final Optional<RequirePkceVO> requirePkce) {
      this.requirePkce$value = requirePkce;
      this.requirePkce$set = true;
      return this;
    }

    /**
     * Append RequirePushedAuthorizationRequests crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param requirePushedAuthorizationRequests The RequirePushedAuthorizationRequests value to
     *        wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder requirePushedAuthorizationRequests(
        final Boolean requirePushedAuthorizationRequests) {
      return requirePushedAuthorizationRequests(Optional
          .of(RequirePushedAuthorizationRequestsVO.from(requirePushedAuthorizationRequests)));
    }

    /**
     * Append RequirePushedAuthorizationRequests value
     *
     * @autogenerated CreateInputGenerator
     * @param requirePushedAuthorizationRequests The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder requirePushedAuthorizationRequests(
        final Optional<RequirePushedAuthorizationRequestsVO> requirePushedAuthorizationRequests) {
      this.requirePushedAuthorizationRequests$value = requirePushedAuthorizationRequests;
      this.requirePushedAuthorizationRequests$set = true;
      return this;
    }

    /**
     * Append ResponseTypesJson crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param responseTypesJson The ResponseTypesJson value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder responseTypesJson(final String responseTypesJson) {
      return responseTypesJson(Optional.of(ResponseTypesJsonVO.from(responseTypesJson)));
    }

    /**
     * Append ResponseTypesJson value
     *
     * @autogenerated CreateInputGenerator
     * @param responseTypesJson The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder responseTypesJson(
        final Optional<ResponseTypesJsonVO> responseTypesJson) {
      this.responseTypesJson$value = responseTypesJson;
      this.responseTypesJson$set = true;
      return this;
    }

    /**
     * Append SecretOauth crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param secretOauth The SecretOauth value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder secretOauth(final String secretOauth) {
      return secretOauth(Optional.of(SecretOauthVO.fromPlain(secretOauth)));
    }

    /**
     * Append SecretOauth value
     *
     * @autogenerated CreateInputGenerator
     * @param secretOauth The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder secretOauth(final Optional<SecretOauthVO> secretOauth) {
      this.secretOauth$value = secretOauth;
      this.secretOauth$set = true;
      return this;
    }

    /**
     * Append SectorIdentifierUri crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param sectorIdentifierUri The SectorIdentifierUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder sectorIdentifierUri(final String sectorIdentifierUri) {
      return sectorIdentifierUri(Optional.of(SectorIdentifierUriVO.from(sectorIdentifierUri)));
    }

    /**
     * Append SectorIdentifierUri value
     *
     * @autogenerated CreateInputGenerator
     * @param sectorIdentifierUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder sectorIdentifierUri(
        final Optional<SectorIdentifierUriVO> sectorIdentifierUri) {
      this.sectorIdentifierUri$value = sectorIdentifierUri;
      this.sectorIdentifierUri$set = true;
      return this;
    }

    /**
     * Append SubjectType crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param subjectType The SubjectType value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder subjectType(
        final TrustedClientSubjectTypeOptions subjectType) {
      return subjectType(Optional.of(SubjectTypeVO.from(subjectType)));
    }

    /**
     * Append SubjectType value
     *
     * @autogenerated CreateInputGenerator
     * @param subjectType The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder subjectType(final Optional<SubjectTypeVO> subjectType) {
      this.subjectType$value = subjectType;
      this.subjectType$set = true;
      return this;
    }

    /**
     * Append Tenant crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param tenant The Tenant value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tenant(final TenantRef tenant) {
      return tenant(Optional.of(TenantVO.from(tenant)));
    }

    /**
     * Append Tenant value
     *
     * @autogenerated CreateInputGenerator
     * @param tenant The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tenant(final Optional<TenantVO> tenant) {
      this.tenant$value = tenant;
      this.tenant$set = true;
      return this;
    }

    /**
     * Append TlsClientAuthSubjectDn crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param tlsClientAuthSubjectDn The TlsClientAuthSubjectDn value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tlsClientAuthSubjectDn(
        final String tlsClientAuthSubjectDn) {
      return tlsClientAuthSubjectDn(
          Optional.of(TlsClientAuthSubjectDnVO.from(tlsClientAuthSubjectDn)));
    }

    /**
     * Append TlsClientAuthSubjectDn value
     *
     * @autogenerated CreateInputGenerator
     * @param tlsClientAuthSubjectDn The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tlsClientAuthSubjectDn(
        final Optional<TlsClientAuthSubjectDnVO> tlsClientAuthSubjectDn) {
      this.tlsClientAuthSubjectDn$value = tlsClientAuthSubjectDn;
      this.tlsClientAuthSubjectDn$set = true;
      return this;
    }

    /**
     * Append TlsClientCertificateBoundAccessTokens crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param tlsClientCertificateBoundAccessTokens The TlsClientCertificateBoundAccessTokens value
     *        to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tlsClientCertificateBoundAccessTokens(
        final Boolean tlsClientCertificateBoundAccessTokens) {
      return tlsClientCertificateBoundAccessTokens(Optional
          .of(TlsClientCertificateBoundAccessTokensVO.from(tlsClientCertificateBoundAccessTokens)));
    }

    /**
     * Append TlsClientCertificateBoundAccessTokens value
     *
     * @autogenerated CreateInputGenerator
     * @param tlsClientCertificateBoundAccessTokens The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tlsClientCertificateBoundAccessTokens(
        final Optional<TlsClientCertificateBoundAccessTokensVO> tlsClientCertificateBoundAccessTokens) {
      this.tlsClientCertificateBoundAccessTokens$value = tlsClientCertificateBoundAccessTokens;
      this.tlsClientCertificateBoundAccessTokens$set = true;
      return this;
    }

    /**
     * Append TokenEndpointAuthMethod crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param tokenEndpointAuthMethod The TokenEndpointAuthMethod value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tokenEndpointAuthMethod(
        final TrustedClientTokenEndpointAuthMethodOptions tokenEndpointAuthMethod) {
      return tokenEndpointAuthMethod(
          Optional.of(TokenEndpointAuthMethodVO.from(tokenEndpointAuthMethod)));
    }

    /**
     * Append TokenEndpointAuthMethod value
     *
     * @autogenerated CreateInputGenerator
     * @param tokenEndpointAuthMethod The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tokenEndpointAuthMethod(
        final Optional<TokenEndpointAuthMethodVO> tokenEndpointAuthMethod) {
      this.tokenEndpointAuthMethod$value = tokenEndpointAuthMethod;
      this.tokenEndpointAuthMethod$set = true;
      return this;
    }

    /**
     * Append TokenEndpointAuthSigningAlg crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param tokenEndpointAuthSigningAlg The TokenEndpointAuthSigningAlg value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tokenEndpointAuthSigningAlg(
        final String tokenEndpointAuthSigningAlg) {
      return tokenEndpointAuthSigningAlg(
          Optional.of(TokenEndpointAuthSigningAlgVO.from(tokenEndpointAuthSigningAlg)));
    }

    /**
     * Append TokenEndpointAuthSigningAlg value
     *
     * @autogenerated CreateInputGenerator
     * @param tokenEndpointAuthSigningAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tokenEndpointAuthSigningAlg(
        final Optional<TokenEndpointAuthSigningAlgVO> tokenEndpointAuthSigningAlg) {
      this.tokenEndpointAuthSigningAlg$value = tokenEndpointAuthSigningAlg;
      this.tokenEndpointAuthSigningAlg$set = true;
      return this;
    }

    /**
     * Append TosUri crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param tosUri The TosUri value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tosUri(final String tosUri) {
      return tosUri(Optional.of(TosUriVO.from(tosUri)));
    }

    /**
     * Append TosUri value
     *
     * @autogenerated CreateInputGenerator
     * @param tosUri The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder tosUri(final Optional<TosUriVO> tosUri) {
      this.tosUri$value = tosUri;
      this.tosUri$set = true;
      return this;
    }

    /**
     * Append Uid crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param uid The Uid value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * Append Uid value
     *
     * @autogenerated CreateInputGenerator
     * @param uid The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * Append UserinfoEncryptedResponseAlg crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param userinfoEncryptedResponseAlg The UserinfoEncryptedResponseAlg value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder userinfoEncryptedResponseAlg(
        final String userinfoEncryptedResponseAlg) {
      return userinfoEncryptedResponseAlg(
          Optional.of(UserinfoEncryptedResponseAlgVO.from(userinfoEncryptedResponseAlg)));
    }

    /**
     * Append UserinfoEncryptedResponseAlg value
     *
     * @autogenerated CreateInputGenerator
     * @param userinfoEncryptedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder userinfoEncryptedResponseAlg(
        final Optional<UserinfoEncryptedResponseAlgVO> userinfoEncryptedResponseAlg) {
      this.userinfoEncryptedResponseAlg$value = userinfoEncryptedResponseAlg;
      this.userinfoEncryptedResponseAlg$set = true;
      return this;
    }

    /**
     * Append UserinfoEncryptedResponseEnc crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param userinfoEncryptedResponseEnc The UserinfoEncryptedResponseEnc value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder userinfoEncryptedResponseEnc(
        final String userinfoEncryptedResponseEnc) {
      return userinfoEncryptedResponseEnc(
          Optional.of(UserinfoEncryptedResponseEncVO.from(userinfoEncryptedResponseEnc)));
    }

    /**
     * Append UserinfoEncryptedResponseEnc value
     *
     * @autogenerated CreateInputGenerator
     * @param userinfoEncryptedResponseEnc The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder userinfoEncryptedResponseEnc(
        final Optional<UserinfoEncryptedResponseEncVO> userinfoEncryptedResponseEnc) {
      this.userinfoEncryptedResponseEnc$value = userinfoEncryptedResponseEnc;
      this.userinfoEncryptedResponseEnc$set = true;
      return this;
    }

    /**
     * Append UserinfoSignedResponseAlg crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param userinfoSignedResponseAlg The UserinfoSignedResponseAlg value to wrap in a value
     *        object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder userinfoSignedResponseAlg(
        final String userinfoSignedResponseAlg) {
      return userinfoSignedResponseAlg(
          Optional.of(UserinfoSignedResponseAlgVO.from(userinfoSignedResponseAlg)));
    }

    /**
     * Append UserinfoSignedResponseAlg value
     *
     * @autogenerated CreateInputGenerator
     * @param userinfoSignedResponseAlg The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder userinfoSignedResponseAlg(
        final Optional<UserinfoSignedResponseAlgVO> userinfoSignedResponseAlg) {
      this.userinfoSignedResponseAlg$value = userinfoSignedResponseAlg;
      this.userinfoSignedResponseAlg$set = true;
      return this;
    }

    /**
     * Append Version crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param version The Version value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * Append Version value
     *
     * @autogenerated CreateInputGenerator
     * @param version The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TrustedClientCreateInputBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * @autogenerated CreateInputGenerator
   * @param input
   * @return
   */
  static TrustedClientCreateInput fromChangeSet(final TrustedClientChangeSet input) {
    return TrustedClientCreateInput.builder().uid(input.getUidValue()).code(input.getCodeValue())
        .tenant(input.getTenantValue()).allowAllScopes(input.getAllowAllScopesValue())
        .publicAllow(input.getPublicAllowValue())
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
        .registrationAccess(input.getRegistrationAccessValue())
        .defaultAcrValues(input.getDefaultAcrValuesValue())
        .defaultMaxAge(input.getDefaultMaxAgeValue())
        .sectorIdentifierUri(input.getSectorIdentifierUriValue())
        .allowedRedirects(input.getAllowedRedirectsValue())
        .allowedLogoutRedirects(input.getAllowedLogoutRedirectsValue())
        .allowedAudiences(input.getAllowedAudiencesValue())
        .grantTypesJson(input.getGrantTypesJsonValue())
        .isResourceServer(input.getIsResourceServerValue())
        .requirePushedAuthorizationRequests(input.getRequirePushedAuthorizationRequestsValue())
        .requirePkce(input.getRequirePkceValue())
        .backChannelLogoutUri(input.getBackChannelLogoutUriValue())
        .backChannelLogoutSessionRequired(input.getBackChannelLogoutSessionRequiredValue())
        .frontChannelLogoutUri(input.getFrontChannelLogoutUriValue())
        .frontChannelLogoutSessionRequired(input.getFrontChannelLogoutSessionRequiredValue())
        .enabled(input.getEnabledValue()).clientName(input.getClientNameValue())
        .logoUri(input.getLogoUriValue()).clientUri(input.getClientUriValue())
        .policyUri(input.getPolicyUriValue()).tosUri(input.getTosUriValue())
        .responseTypesJson(input.getResponseTypesJsonValue())
        .dynamicallyRegistered(input.getDynamicallyRegisteredValue())
        .registeredAt(input.getRegisteredAtValue())
        .allowedScopesM2m(input.getAllowedScopesM2mValue())
        .m2mTokenTtlSeconds(input.getM2mTokenTtlSecondsValue())
        .requestObjectSigningAlg(input.getRequestObjectSigningAlgValue())
        .jwksUri(input.getJwksUriValue()).jwksJson(input.getJwksJsonValue())
        .version(input.getVersionValue()).build();
  }

  /**
   * El allow all scopes de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<AllowAllScopesVO> allowAllScopes = Optional.empty();

  /**
   * El allowed audiences de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<AllowedAudiencesVO> allowedAudiences = Optional.empty();

  /**
   * El allowed logout redirects de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<AllowedLogoutRedirectsVO> allowedLogoutRedirects = Optional.empty();

  /**
   * El allowed redirects de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<AllowedRedirectsVO> allowedRedirects = Optional.empty();

  /**
   * El allowed scopes m 2m de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<AllowedScopesM2mVO> allowedScopesM2m = Optional.empty();

  /**
   * El authorization details types de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<AuthorizationDetailsTypesVO> authorizationDetailsTypes = Optional.empty();

  /**
   * El authorization signed response alg de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<AuthorizationSignedResponseAlgVO> authorizationSignedResponseAlg =
      Optional.empty();

  /**
   * El back channel logout session required de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<BackChannelLogoutSessionRequiredVO> backChannelLogoutSessionRequired =
      Optional.empty();

  /**
   * El back channel logout uri de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<BackChannelLogoutUriVO> backChannelLogoutUri = Optional.empty();

  /**
   * El backchannel client notification endpoint de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<BackchannelClientNotificationEndpointVO> backchannelClientNotificationEndpoint =
      Optional.empty();

  /**
   * El backchannel token delivery mode de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<BackchannelTokenDeliveryModeVO> backchannelTokenDeliveryMode = Optional.empty();

  /**
   * El client name de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<ClientNameVO> clientName = Optional.empty();

  /**
   * El client uri de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<ClientUriVO> clientUri = Optional.empty();

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<CodeVO> code = Optional.empty();

  /**
   * El default acr values de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<DefaultAcrValuesVO> defaultAcrValues = Optional.empty();

  /**
   * El default max age de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<DefaultMaxAgeVO> defaultMaxAge = Optional.empty();

  /**
   * El dpop bound access tokens de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<DpopBoundAccessTokensVO> dpopBoundAccessTokens = Optional.empty();

  /**
   * El dynamically registered de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<DynamicallyRegisteredVO> dynamicallyRegistered = Optional.empty();

  /**
   * The indicator to allow the account be used without deleting it
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<EnabledVO> enabled = Optional.empty();

  /**
   * El front channel logout session required de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<FrontChannelLogoutSessionRequiredVO> frontChannelLogoutSessionRequired =
      Optional.empty();

  /**
   * El front channel logout uri de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<FrontChannelLogoutUriVO> frontChannelLogoutUri = Optional.empty();

  /**
   * El grant types json de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<GrantTypesJsonVO> grantTypesJson = Optional.empty();

  /**
   * El id token encrypted response alg de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<IdTokenEncryptedResponseAlgVO> idTokenEncryptedResponseAlg = Optional.empty();

  /**
   * El id token encrypted response enc de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<IdTokenEncryptedResponseEncVO> idTokenEncryptedResponseEnc = Optional.empty();

  /**
   * El id token signed response alg de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<IdTokenSignedResponseAlgVO> idTokenSignedResponseAlg = Optional.empty();

  /**
   * If true, this client is allowed to call the token introspection endpoint (POST /introspect) to
   * validate access tokens. Resource servers must authenticate at the introspection endpoint using
   * their client credentials.
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<IsResourceServerVO> isResourceServer = Optional.empty();

  /**
   * El jwks json de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<JwksJsonVO> jwksJson = Optional.empty();

  /**
   * El jwks uri de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<JwksUriVO> jwksUri = Optional.empty();

  /**
   * El logo uri de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<LogoUriVO> logoUri = Optional.empty();

  /**
   * El m 2m token ttl seconds de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<M2mTokenTtlSecondsVO> m2mTokenTtlSeconds = Optional.empty();

  /**
   * El policy uri de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<PolicyUriVO> policyUri = Optional.empty();

  /**
   * If true, users can use these client to access with public code flow
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<PublicAllowVO> publicAllow = Optional.empty();

  /**
   * El registered at de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<RegisteredAtVO> registeredAt = Optional.empty();

  /**
   * El registration access de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<RegistrationAccessVO> registrationAccess = Optional.empty();

  /**
   * El request object signing alg de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<RequestObjectSigningAlgVO> requestObjectSigningAlg = Optional.empty();

  /**
   * If true, every authorization code request from this client must include code_challenge and
   * code_challenge_method=S256. For clients with public-allow=true, PKCE is always required
   * regardless of this flag.
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<RequirePkceVO> requirePkce = Optional.empty();

  /**
   * El require pushed authorization requests de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<RequirePushedAuthorizationRequestsVO> requirePushedAuthorizationRequests =
      Optional.empty();

  /**
   * El response types json de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<ResponseTypesJsonVO> responseTypesJson = Optional.empty();

  /**
   * El secret oauth de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<SecretOauthVO> secretOauth = Optional.empty();

  /**
   * El sector identifier uri de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<SectorIdentifierUriVO> sectorIdentifierUri = Optional.empty();

  /**
   * El subject type de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<SubjectTypeVO> subjectType = Optional.empty();

  /**
   * Owner tenant. Null means a global client, manageable only by root.
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<TenantVO> tenant = Optional.empty();

  /**
   * El tls client auth subject dn de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<TlsClientAuthSubjectDnVO> tlsClientAuthSubjectDn = Optional.empty();

  /**
   * El tls client certificate bound access tokens de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<TlsClientCertificateBoundAccessTokensVO> tlsClientCertificateBoundAccessTokens =
      Optional.empty();

  /**
   * El token endpoint auth method de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<TokenEndpointAuthMethodVO> tokenEndpointAuthMethod = Optional.empty();

  /**
   * El token endpoint auth signing alg de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<TokenEndpointAuthSigningAlgVO> tokenEndpointAuthSigningAlg = Optional.empty();

  /**
   * El tos uri de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<TosUriVO> tosUri = Optional.empty();

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * El userinfo encrypted response alg de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<UserinfoEncryptedResponseAlgVO> userinfoEncryptedResponseAlg = Optional.empty();

  /**
   * El userinfo encrypted response enc de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<UserinfoEncryptedResponseEncVO> userinfoEncryptedResponseEnc = Optional.empty();

  /**
   * El userinfo signed response alg de trusted client
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<UserinfoSignedResponseAlgVO> userinfoSignedResponseAlg = Optional.empty();

  /**
   * Campo con el número de version de trusted client para controlar bloqueos optimistas
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Inform for a possible change propolsal in AllowAllScopes
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for AllowAllScopes, otherwise the value for
   *         AllowAllScopes
   */
  public Optional<AllowAllScopesVO> getAllowAllScopes() {
    return allowAllScopes;
  }

  /**
   * Inform for a possible change propolsal in AllowedAudiences
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for AllowedAudiences, otherwise the value for
   *         AllowedAudiences
   */
  public Optional<AllowedAudiencesVO> getAllowedAudiences() {
    return allowedAudiences;
  }

  /**
   * Inform for a possible change propolsal in AllowedLogoutRedirects
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for AllowedLogoutRedirects, otherwise the value
   *         for AllowedLogoutRedirects
   */
  public Optional<AllowedLogoutRedirectsVO> getAllowedLogoutRedirects() {
    return allowedLogoutRedirects;
  }

  /**
   * Inform for a possible change propolsal in AllowedRedirects
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for AllowedRedirects, otherwise the value for
   *         AllowedRedirects
   */
  public Optional<AllowedRedirectsVO> getAllowedRedirects() {
    return allowedRedirects;
  }

  /**
   * Inform for a possible change propolsal in AllowedScopesM2m
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for AllowedScopesM2m, otherwise the value for
   *         AllowedScopesM2m
   */
  public Optional<AllowedScopesM2mVO> getAllowedScopesM2m() {
    return allowedScopesM2m;
  }

  /**
   * Inform for a possible change propolsal in AuthorizationDetailsTypes
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for AuthorizationDetailsTypes, otherwise the value
   *         for AuthorizationDetailsTypes
   */
  public Optional<AuthorizationDetailsTypesVO> getAuthorizationDetailsTypes() {
    return authorizationDetailsTypes;
  }

  /**
   * Inform for a possible change propolsal in AuthorizationSignedResponseAlg
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for AuthorizationSignedResponseAlg, otherwise the
   *         value for AuthorizationSignedResponseAlg
   */
  public Optional<AuthorizationSignedResponseAlgVO> getAuthorizationSignedResponseAlg() {
    return authorizationSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in BackChannelLogoutSessionRequired
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for BackChannelLogoutSessionRequired, otherwise
   *         the value for BackChannelLogoutSessionRequired
   */
  public Optional<BackChannelLogoutSessionRequiredVO> getBackChannelLogoutSessionRequired() {
    return backChannelLogoutSessionRequired;
  }

  /**
   * Inform for a possible change propolsal in BackChannelLogoutUri
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for BackChannelLogoutUri, otherwise the value for
   *         BackChannelLogoutUri
   */
  public Optional<BackChannelLogoutUriVO> getBackChannelLogoutUri() {
    return backChannelLogoutUri;
  }

  /**
   * Inform for a possible change propolsal in BackchannelClientNotificationEndpoint
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for BackchannelClientNotificationEndpoint,
   *         otherwise the value for BackchannelClientNotificationEndpoint
   */
  public Optional<BackchannelClientNotificationEndpointVO> getBackchannelClientNotificationEndpoint() {
    return backchannelClientNotificationEndpoint;
  }

  /**
   * Inform for a possible change propolsal in BackchannelTokenDeliveryMode
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for BackchannelTokenDeliveryMode, otherwise the
   *         value for BackchannelTokenDeliveryMode
   */
  public Optional<BackchannelTokenDeliveryModeVO> getBackchannelTokenDeliveryMode() {
    return backchannelTokenDeliveryMode;
  }

  /**
   * Inform for a possible change propolsal in ClientName
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for ClientName, otherwise the value for ClientName
   */
  public Optional<ClientNameVO> getClientName() {
    return clientName;
  }

  /**
   * Inform for a possible change propolsal in ClientUri
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for ClientUri, otherwise the value for ClientUri
   */
  public Optional<ClientUriVO> getClientUri() {
    return clientUri;
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public Optional<CodeVO> getCode() {
    return code;
  }

  /**
   * Inform for a possible change propolsal in DefaultAcrValues
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for DefaultAcrValues, otherwise the value for
   *         DefaultAcrValues
   */
  public Optional<DefaultAcrValuesVO> getDefaultAcrValues() {
    return defaultAcrValues;
  }

  /**
   * Inform for a possible change propolsal in DefaultMaxAge
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for DefaultMaxAge, otherwise the value for
   *         DefaultMaxAge
   */
  public Optional<DefaultMaxAgeVO> getDefaultMaxAge() {
    return defaultMaxAge;
  }

  /**
   * Inform for a possible change propolsal in DpopBoundAccessTokens
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for DpopBoundAccessTokens, otherwise the value for
   *         DpopBoundAccessTokens
   */
  public Optional<DpopBoundAccessTokensVO> getDpopBoundAccessTokens() {
    return dpopBoundAccessTokens;
  }

  /**
   * Inform for a possible change propolsal in DynamicallyRegistered
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for DynamicallyRegistered, otherwise the value for
   *         DynamicallyRegistered
   */
  public Optional<DynamicallyRegisteredVO> getDynamicallyRegistered() {
    return dynamicallyRegistered;
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Optional<EnabledVO> getEnabled() {
    return enabled;
  }

  /**
   * Inform for a possible change propolsal in FrontChannelLogoutSessionRequired
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for FrontChannelLogoutSessionRequired, otherwise
   *         the value for FrontChannelLogoutSessionRequired
   */
  public Optional<FrontChannelLogoutSessionRequiredVO> getFrontChannelLogoutSessionRequired() {
    return frontChannelLogoutSessionRequired;
  }

  /**
   * Inform for a possible change propolsal in FrontChannelLogoutUri
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for FrontChannelLogoutUri, otherwise the value for
   *         FrontChannelLogoutUri
   */
  public Optional<FrontChannelLogoutUriVO> getFrontChannelLogoutUri() {
    return frontChannelLogoutUri;
  }

  /**
   * Inform for a possible change propolsal in GrantTypesJson
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for GrantTypesJson, otherwise the value for
   *         GrantTypesJson
   */
  public Optional<GrantTypesJsonVO> getGrantTypesJson() {
    return grantTypesJson;
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseAlg
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseAlg, otherwise the
   *         value for IdTokenEncryptedResponseAlg
   */
  public Optional<IdTokenEncryptedResponseAlgVO> getIdTokenEncryptedResponseAlg() {
    return idTokenEncryptedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in IdTokenEncryptedResponseEnc
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for IdTokenEncryptedResponseEnc, otherwise the
   *         value for IdTokenEncryptedResponseEnc
   */
  public Optional<IdTokenEncryptedResponseEncVO> getIdTokenEncryptedResponseEnc() {
    return idTokenEncryptedResponseEnc;
  }

  /**
   * Inform for a possible change propolsal in IdTokenSignedResponseAlg
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for IdTokenSignedResponseAlg, otherwise the value
   *         for IdTokenSignedResponseAlg
   */
  public Optional<IdTokenSignedResponseAlgVO> getIdTokenSignedResponseAlg() {
    return idTokenSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in IsResourceServer
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for IsResourceServer, otherwise the value for
   *         IsResourceServer
   */
  public Optional<IsResourceServerVO> getIsResourceServer() {
    return isResourceServer;
  }

  /**
   * Inform for a possible change propolsal in JwksJson
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for JwksJson, otherwise the value for JwksJson
   */
  public Optional<JwksJsonVO> getJwksJson() {
    return jwksJson;
  }

  /**
   * Inform for a possible change propolsal in JwksUri
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for JwksUri, otherwise the value for JwksUri
   */
  public Optional<JwksUriVO> getJwksUri() {
    return jwksUri;
  }

  /**
   * Inform for a possible change propolsal in LogoUri
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for LogoUri, otherwise the value for LogoUri
   */
  public Optional<LogoUriVO> getLogoUri() {
    return logoUri;
  }

  /**
   * Inform for a possible change propolsal in M2mTokenTtlSeconds
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for M2mTokenTtlSeconds, otherwise the value for
   *         M2mTokenTtlSeconds
   */
  public Optional<M2mTokenTtlSecondsVO> getM2mTokenTtlSeconds() {
    return m2mTokenTtlSeconds;
  }

  /**
   * Inform for a possible change propolsal in PolicyUri
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for PolicyUri, otherwise the value for PolicyUri
   */
  public Optional<PolicyUriVO> getPolicyUri() {
    return policyUri;
  }

  /**
   * Inform for a possible change propolsal in PublicAllow
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for PublicAllow, otherwise the value for
   *         PublicAllow
   */
  public Optional<PublicAllowVO> getPublicAllow() {
    return publicAllow;
  }

  /**
   * Inform for a possible change propolsal in RegisteredAt
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for RegisteredAt, otherwise the value for
   *         RegisteredAt
   */
  public Optional<RegisteredAtVO> getRegisteredAt() {
    return registeredAt;
  }

  /**
   * Inform for a possible change propolsal in RegistrationAccess
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for RegistrationAccess, otherwise the value for
   *         RegistrationAccess
   */
  public Optional<RegistrationAccessVO> getRegistrationAccess() {
    return registrationAccess;
  }

  /**
   * Inform for a possible change propolsal in RequestObjectSigningAlg
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for RequestObjectSigningAlg, otherwise the value
   *         for RequestObjectSigningAlg
   */
  public Optional<RequestObjectSigningAlgVO> getRequestObjectSigningAlg() {
    return requestObjectSigningAlg;
  }

  /**
   * Inform for a possible change propolsal in RequirePkce
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for RequirePkce, otherwise the value for
   *         RequirePkce
   */
  public Optional<RequirePkceVO> getRequirePkce() {
    return requirePkce;
  }

  /**
   * Inform for a possible change propolsal in RequirePushedAuthorizationRequests
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for RequirePushedAuthorizationRequests, otherwise
   *         the value for RequirePushedAuthorizationRequests
   */
  public Optional<RequirePushedAuthorizationRequestsVO> getRequirePushedAuthorizationRequests() {
    return requirePushedAuthorizationRequests;
  }

  /**
   * Inform for a possible change propolsal in ResponseTypesJson
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for ResponseTypesJson, otherwise the value for
   *         ResponseTypesJson
   */
  public Optional<ResponseTypesJsonVO> getResponseTypesJson() {
    return responseTypesJson;
  }

  /**
   * Inform for a possible change propolsal in SecretOauth
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for SecretOauth, otherwise the value for
   *         SecretOauth
   */
  public Optional<SecretOauthVO> getSecretOauth() {
    return secretOauth;
  }

  /**
   * Inform for a possible change propolsal in SectorIdentifierUri
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for SectorIdentifierUri, otherwise the value for
   *         SectorIdentifierUri
   */
  public Optional<SectorIdentifierUriVO> getSectorIdentifierUri() {
    return sectorIdentifierUri;
  }

  /**
   * Inform for a possible change propolsal in SubjectType
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for SubjectType, otherwise the value for
   *         SubjectType
   */
  public Optional<SubjectTypeVO> getSubjectType() {
    return subjectType;
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public Optional<TenantVO> getTenant() {
    return tenant;
  }

  /**
   * Inform for a possible change propolsal in TlsClientAuthSubjectDn
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for TlsClientAuthSubjectDn, otherwise the value
   *         for TlsClientAuthSubjectDn
   */
  public Optional<TlsClientAuthSubjectDnVO> getTlsClientAuthSubjectDn() {
    return tlsClientAuthSubjectDn;
  }

  /**
   * Inform for a possible change propolsal in TlsClientCertificateBoundAccessTokens
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for TlsClientCertificateBoundAccessTokens,
   *         otherwise the value for TlsClientCertificateBoundAccessTokens
   */
  public Optional<TlsClientCertificateBoundAccessTokensVO> getTlsClientCertificateBoundAccessTokens() {
    return tlsClientCertificateBoundAccessTokens;
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthMethod
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthMethod, otherwise the value
   *         for TokenEndpointAuthMethod
   */
  public Optional<TokenEndpointAuthMethodVO> getTokenEndpointAuthMethod() {
    return tokenEndpointAuthMethod;
  }

  /**
   * Inform for a possible change propolsal in TokenEndpointAuthSigningAlg
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for TokenEndpointAuthSigningAlg, otherwise the
   *         value for TokenEndpointAuthSigningAlg
   */
  public Optional<TokenEndpointAuthSigningAlgVO> getTokenEndpointAuthSigningAlg() {
    return tokenEndpointAuthSigningAlg;
  }

  /**
   * Inform for a possible change propolsal in TosUri
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for TosUri, otherwise the value for TosUri
   */
  public Optional<TosUriVO> getTosUri() {
    return tosUri;
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public Optional<UidVO> getUid() {
    return uid;
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseAlg
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseAlg, otherwise the
   *         value for UserinfoEncryptedResponseAlg
   */
  public Optional<UserinfoEncryptedResponseAlgVO> getUserinfoEncryptedResponseAlg() {
    return userinfoEncryptedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in UserinfoEncryptedResponseEnc
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for UserinfoEncryptedResponseEnc, otherwise the
   *         value for UserinfoEncryptedResponseEnc
   */
  public Optional<UserinfoEncryptedResponseEncVO> getUserinfoEncryptedResponseEnc() {
    return userinfoEncryptedResponseEnc;
  }

  /**
   * Inform for a possible change propolsal in UserinfoSignedResponseAlg
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for UserinfoSignedResponseAlg, otherwise the value
   *         for UserinfoSignedResponseAlg
   */
  public Optional<UserinfoSignedResponseAlgVO> getUserinfoSignedResponseAlg() {
    return userinfoSignedResponseAlg;
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Optional<VersionVO> getVersion() {
    return version;
  }

  /**
   * Assigna change proposal for AllowAllScopes to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setAllowAllScopes(final Boolean value) {
    this.allowAllScopes = Optional.of(AllowAllScopesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedAudiences to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setAllowedAudiences(final List<AllowedAudiences> value) {
    this.allowedAudiences = Optional.of(AllowedAudiencesVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedLogoutRedirects to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setAllowedLogoutRedirects(
      final List<AllowedLogoutRedirects> value) {
    this.allowedLogoutRedirects =
        Optional.of(AllowedLogoutRedirectsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedRedirects to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setAllowedRedirects(final List<AllowedRedirects> value) {
    this.allowedRedirects = Optional.of(AllowedRedirectsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for AllowedScopesM2m to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setAllowedScopesM2m(final String value) {
    this.allowedScopesM2m = Optional.of(AllowedScopesM2mVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AuthorizationDetailsTypes to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setAuthorizationDetailsTypes(final String value) {
    this.authorizationDetailsTypes = Optional.of(AuthorizationDetailsTypesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for AuthorizationSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setAuthorizationSignedResponseAlg(final String value) {
    this.authorizationSignedResponseAlg = Optional.of(AuthorizationSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackChannelLogoutSessionRequired to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setBackChannelLogoutSessionRequired(final Boolean value) {
    this.backChannelLogoutSessionRequired =
        Optional.of(BackChannelLogoutSessionRequiredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackChannelLogoutUri to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setBackChannelLogoutUri(final String value) {
    this.backChannelLogoutUri = Optional.of(BackChannelLogoutUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelClientNotificationEndpoint to be apply for the
   * aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setBackchannelClientNotificationEndpoint(final String value) {
    this.backchannelClientNotificationEndpoint =
        Optional.of(BackchannelClientNotificationEndpointVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for BackchannelTokenDeliveryMode to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setBackchannelTokenDeliveryMode(
      final TrustedClientBackchannelTokenDeliveryModeOptions value) {
    this.backchannelTokenDeliveryMode = Optional.of(BackchannelTokenDeliveryModeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientName to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setClientName(final String value) {
    this.clientName = Optional.of(ClientNameVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ClientUri to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setClientUri(final String value) {
    this.clientUri = Optional.of(ClientUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Code to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setCode(final String value) {
    this.code = Optional.of(CodeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DefaultAcrValues to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setDefaultAcrValues(final String value) {
    this.defaultAcrValues = Optional.of(DefaultAcrValuesVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DefaultMaxAge to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setDefaultMaxAge(final Integer value) {
    this.defaultMaxAge = Optional.of(DefaultMaxAgeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DpopBoundAccessTokens to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setDpopBoundAccessTokens(final Boolean value) {
    this.dpopBoundAccessTokens = Optional.of(DpopBoundAccessTokensVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for DynamicallyRegistered to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setDynamicallyRegistered(final Boolean value) {
    this.dynamicallyRegistered = Optional.of(DynamicallyRegisteredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Enabled to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for FrontChannelLogoutSessionRequired to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setFrontChannelLogoutSessionRequired(final Boolean value) {
    this.frontChannelLogoutSessionRequired =
        Optional.of(FrontChannelLogoutSessionRequiredVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for FrontChannelLogoutUri to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setFrontChannelLogoutUri(final String value) {
    this.frontChannelLogoutUri = Optional.of(FrontChannelLogoutUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for GrantTypesJson to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setGrantTypesJson(final String value) {
    this.grantTypesJson = Optional.of(GrantTypesJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenEncryptedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setIdTokenEncryptedResponseAlg(final String value) {
    this.idTokenEncryptedResponseAlg = Optional.of(IdTokenEncryptedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenEncryptedResponseEnc to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setIdTokenEncryptedResponseEnc(final String value) {
    this.idTokenEncryptedResponseEnc = Optional.of(IdTokenEncryptedResponseEncVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IdTokenSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setIdTokenSignedResponseAlg(
      final TrustedClientIdTokenSignedResponseAlgOptions value) {
    this.idTokenSignedResponseAlg = Optional.of(IdTokenSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for IsResourceServer to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setIsResourceServer(final Boolean value) {
    this.isResourceServer = Optional.of(IsResourceServerVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for JwksJson to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setJwksJson(final String value) {
    this.jwksJson = Optional.of(JwksJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for JwksUri to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setJwksUri(final String value) {
    this.jwksUri = Optional.of(JwksUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for LogoUri to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setLogoUri(final String value) {
    this.logoUri = Optional.of(LogoUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for M2mTokenTtlSeconds to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setM2mTokenTtlSeconds(final Integer value) {
    this.m2mTokenTtlSeconds = Optional.of(M2mTokenTtlSecondsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for PolicyUri to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setPolicyUri(final String value) {
    this.policyUri = Optional.of(PolicyUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for PublicAllow to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setPublicAllow(final Boolean value) {
    this.publicAllow = Optional.of(PublicAllowVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RegisteredAt to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setRegisteredAt(final OffsetDateTime value) {
    this.registeredAt = Optional.of(RegisteredAtVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RegistrationAccess to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setRegistrationAccess(final String value) {
    this.registrationAccess = Optional.of(RegistrationAccessVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for RequestObjectSigningAlg to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setRequestObjectSigningAlg(final String value) {
    this.requestObjectSigningAlg = Optional.of(RequestObjectSigningAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RequirePkce to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setRequirePkce(final Boolean value) {
    this.requirePkce = Optional.of(RequirePkceVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RequirePushedAuthorizationRequests to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setRequirePushedAuthorizationRequests(final Boolean value) {
    this.requirePushedAuthorizationRequests =
        Optional.of(RequirePushedAuthorizationRequestsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for ResponseTypesJson to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setResponseTypesJson(final String value) {
    this.responseTypesJson = Optional.of(ResponseTypesJsonVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SecretOauth to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setSecretOauth(final String value) {
    this.secretOauth = Optional.of(SecretOauthVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for SectorIdentifierUri to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setSectorIdentifierUri(final String value) {
    this.sectorIdentifierUri = Optional.of(SectorIdentifierUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SubjectType to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setSubjectType(final TrustedClientSubjectTypeOptions value) {
    this.subjectType = Optional.of(SubjectTypeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Tenant to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TlsClientAuthSubjectDn to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setTlsClientAuthSubjectDn(final String value) {
    this.tlsClientAuthSubjectDn = Optional.of(TlsClientAuthSubjectDnVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TlsClientCertificateBoundAccessTokens to be apply for the
   * aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setTlsClientCertificateBoundAccessTokens(final Boolean value) {
    this.tlsClientCertificateBoundAccessTokens =
        Optional.of(TlsClientCertificateBoundAccessTokensVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TokenEndpointAuthMethod to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setTokenEndpointAuthMethod(
      final TrustedClientTokenEndpointAuthMethodOptions value) {
    this.tokenEndpointAuthMethod = Optional.of(TokenEndpointAuthMethodVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TokenEndpointAuthSigningAlg to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setTokenEndpointAuthSigningAlg(final String value) {
    this.tokenEndpointAuthSigningAlg = Optional.of(TokenEndpointAuthSigningAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for TosUri to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setTosUri(final String value) {
    this.tosUri = Optional.of(TosUriVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoEncryptedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setUserinfoEncryptedResponseAlg(final String value) {
    this.userinfoEncryptedResponseAlg = Optional.of(UserinfoEncryptedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoEncryptedResponseEnc to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setUserinfoEncryptedResponseEnc(final String value) {
    this.userinfoEncryptedResponseEnc = Optional.of(UserinfoEncryptedResponseEncVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UserinfoSignedResponseAlg to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setUserinfoSignedResponseAlg(final String value) {
    this.userinfoSignedResponseAlg = Optional.of(UserinfoSignedResponseAlgVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for TrustedClientCreateInput
   * @return self instance to enable a flow code
   */
  public TrustedClientCreateInput setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * @autogenerated CreateInputGenerator
   * @return
   */
  TrustedClientChangeSet toChangeSet() {
    return new TrustedClientChangeSet().uid(uid).code(code).tenant(tenant)
        .allowAllScopes(allowAllScopes).publicAllow(publicAllow)
        .tokenEndpointAuthMethod(tokenEndpointAuthMethod)
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
        .registrationAccess(registrationAccess).defaultAcrValues(defaultAcrValues)
        .defaultMaxAge(defaultMaxAge).sectorIdentifierUri(sectorIdentifierUri)
        .allowedRedirects(allowedRedirects).allowedLogoutRedirects(allowedLogoutRedirects)
        .allowedAudiences(allowedAudiences).grantTypesJson(grantTypesJson)
        .isResourceServer(isResourceServer)
        .requirePushedAuthorizationRequests(requirePushedAuthorizationRequests)
        .requirePkce(requirePkce).backChannelLogoutUri(backChannelLogoutUri)
        .backChannelLogoutSessionRequired(backChannelLogoutSessionRequired)
        .frontChannelLogoutUri(frontChannelLogoutUri)
        .frontChannelLogoutSessionRequired(frontChannelLogoutSessionRequired).enabled(enabled)
        .clientName(clientName).logoUri(logoUri).clientUri(clientUri).policyUri(policyUri)
        .tosUri(tosUri).responseTypesJson(responseTypesJson)
        .dynamicallyRegistered(dynamicallyRegistered).registeredAt(registeredAt)
        .allowedScopesM2m(allowedScopesM2m).m2mTokenTtlSeconds(m2mTokenTtlSeconds)
        .requestObjectSigningAlg(requestObjectSigningAlg).jwksUri(jwksUri).jwksJson(jwksJson)
        .version(version);
  }
}
