package net.civeira.phylax.feature.access.oauthclient.infrastructure.repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.sql.DataSource;

import io.opentelemetry.api.trace.Tracer;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.common.crypto.SecretDigester;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.infrastructure.sql.AbstractSqlParametrized;
import net.civeira.phylax.common.infrastructure.sql.OptimistLockException;
import net.civeira.phylax.common.infrastructure.sql.PartialWhere;
import net.civeira.phylax.common.infrastructure.sql.SqlCommand;
import net.civeira.phylax.common.infrastructure.sql.SqlConverter;
import net.civeira.phylax.common.infrastructure.sql.SqlListParameterValue;
import net.civeira.phylax.common.infrastructure.sql.SqlOperator;
import net.civeira.phylax.common.infrastructure.sql.SqlParameterValue;
import net.civeira.phylax.common.infrastructure.sql.SqlQuery;
import net.civeira.phylax.common.infrastructure.sql.SqlResult;
import net.civeira.phylax.common.infrastructure.sql.SqlSchematicQuery;
import net.civeira.phylax.common.infrastructure.sql.SqlTemplate;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedAudiences;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLoginRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.AllowedLogoutRedirects;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClient;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientBackchannelTokenDeliveryModeOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientIdTokenSignedResponseAlgOptions;
import net.civeira.phylax.feature.access.oauthclient.domain.OauthClientRef;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientCursor;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientFilter;
import net.civeira.phylax.feature.access.oauthclient.domain.gateway.OauthClientOrder;
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

@Slf4j
public class OauthClientJdbcDao {

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_ALLOW_ALL_SCOPES =
      "access_oauth_client.allow_all_scopes";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_AUTHORIZATION_DETAILS_TYPES =
      "access_oauth_client.authorization_details_types";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_AUTHORIZATION_SIGNED_RESPONSE_ALG =
      "access_oauth_client.authorization_signed_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_BACKCHANNEL_CLIENT_NOTIFICATION_ENDPOINT =
      "access_oauth_client.backchannel_client_notification_endpoint";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_BACKCHANNEL_LOGOUT_SESSION_REQUIRED =
      "access_oauth_client.backchannel_logout_session_required";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_BACKCHANNEL_LOGOUT_URI =
      "access_oauth_client.backchannel_logout_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_BACKCHANNEL_TOKEN_DELIVERY_MODE =
      "access_oauth_client.backchannel_token_delivery_mode";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_CLIENT_CREDENTIALS_SCOPES =
      "access_oauth_client.client_credentials_scopes";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_CLIENT_CREDENTIALS_TOKEN_TTL_SECONDS =
      "access_oauth_client.client_credentials_token_ttl_seconds";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_CLIENT_NAME = "access_oauth_client.client_name";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_CLIENT_URI = "access_oauth_client.client_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_CODE = "access_oauth_client.code";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_DEFAULT_ACR_VALUES =
      "access_oauth_client.default_acr_values";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_DEFAULT_MAX_AGE =
      "access_oauth_client.default_max_age";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_DPOP_BOUND_ACCESS_TOKENS =
      "access_oauth_client.dpop_bound_access_tokens";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_DYNAMICALLY_REGISTERED =
      "access_oauth_client.dynamically_registered";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_ENABLED = "access_oauth_client.enabled";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_FRONTCHANNEL_LOGOUT_SESSION_REQUIRED =
      "access_oauth_client.frontchannel_logout_session_required";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_FRONTCHANNEL_LOGOUT_URI =
      "access_oauth_client.frontchannel_logout_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_GRANT_TYPES_JSON =
      "access_oauth_client.grant_types_json";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_ID_TOKEN_ENCRYPTED_RESPONSE_ALG =
      "access_oauth_client.id_token_encrypted_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_ID_TOKEN_ENCRYPTED_RESPONSE_ENC =
      "access_oauth_client.id_token_encrypted_response_enc";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_ID_TOKEN_SIGNED_RESPONSE_ALG =
      "access_oauth_client.id_token_signed_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_INTROSPECTION_ALLOWED =
      "access_oauth_client.introspection_allowed";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_JWKS_JSON = "access_oauth_client.jwks_json";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_JWKS_URI = "access_oauth_client.jwks_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_LOGO_URI = "access_oauth_client.logo_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_POLICY_URI = "access_oauth_client.policy_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_REGISTERED_AT =
      "access_oauth_client.registered_at";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_REGISTRATION_ACCESS_TOKEN =
      "access_oauth_client.registration_access_token";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_REQUEST_OBJECT_SIGNING_ALG =
      "access_oauth_client.request_object_signing_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_REQUIRE_PKCE = "access_oauth_client.require_pkce";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_REQUIRE_PUSHED_AUTHORIZATION_REQUESTS =
      "access_oauth_client.require_pushed_authorization_requests";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_RESPONSE_TYPES_JSON =
      "access_oauth_client.response_types_json";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_SECRET_OAUTH = "access_oauth_client.secret_oauth";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_SECTOR_IDENTIFIER_URI =
      "access_oauth_client.sector_identifier_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_SNAKE = "access_oauth_client";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_SUBJECT_TYPE = "access_oauth_client.subject_type";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_TENANT = "access_oauth_client.tenant";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_TLS_CLIENT_AUTH_SUBJECT_DN =
      "access_oauth_client.tls_client_auth_subject_dn";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS =
      "access_oauth_client.tls_client_certificate_bound_access_tokens";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_TOKEN_ENDPOINT_AUTH_METHOD =
      "access_oauth_client.token_endpoint_auth_method";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_TOKEN_ENDPOINT_AUTH_SIGNING_ALG =
      "access_oauth_client.token_endpoint_auth_signing_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_TOS_URI = "access_oauth_client.tos_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_UID = "access_oauth_client.uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_USERINFO_ENCRYPTED_RESPONSE_ALG =
      "access_oauth_client.userinfo_encrypted_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_USERINFO_ENCRYPTED_RESPONSE_ENC =
      "access_oauth_client.userinfo_encrypted_response_enc";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_USERINFO_SIGNED_RESPONSE_ALG =
      "access_oauth_client.userinfo_signed_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_OAUTH_CLIENT_VERSION = "access_oauth_client.version";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ALLOW_ALL_SCOPES = "allowAllScopes";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ALLOW_ALL_SCOPES_SNAKE = "allow_all_scopes";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String AUDIENCE = "audience";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String AUTHORIZATION_DETAILS_TYPES = "authorizationDetailsTypes";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String AUTHORIZATION_DETAILS_TYPES_SNAKE = "authorization_details_types";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String AUTHORIZATION_SIGNED_RESPONSE_ALG = "authorizationSignedResponseAlg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String AUTHORIZATION_SIGNED_RESPONSE_ALG_SNAKE =
      "authorization_signed_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BACKCHANNEL_CLIENT_NOTIFICATION_ENDPOINT =
      "backchannelClientNotificationEndpoint";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BACKCHANNEL_CLIENT_NOTIFICATION_ENDPOINT_SNAKE =
      "backchannel_client_notification_endpoint";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BACKCHANNEL_LOGOUT_SESSION_REQUIRED =
      "backchannelLogoutSessionRequired";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BACKCHANNEL_LOGOUT_SESSION_REQUIRED_SNAKE =
      "backchannel_logout_session_required";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BACKCHANNEL_LOGOUT_URI = "backchannelLogoutUri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BACKCHANNEL_LOGOUT_URI_SNAKE = "backchannel_logout_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BACKCHANNEL_TOKEN_DELIVERY_MODE = "backchannelTokenDeliveryMode";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BACKCHANNEL_TOKEN_DELIVERY_MODE_SNAKE =
      "backchannel_token_delivery_mode";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT = "client";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT_CREDENTIALS_SCOPES = "clientCredentialsScopes";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT_CREDENTIALS_SCOPES_SNAKE = "client_credentials_scopes";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT_CREDENTIALS_TOKEN_TTL_SECONDS =
      "clientCredentialsTokenTtlSeconds";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT_CREDENTIALS_TOKEN_TTL_SECONDS_SNAKE =
      "client_credentials_token_ttl_seconds";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT_NAME = "clientName";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT_NAME_SNAKE = "client_name";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT_URI = "clientUri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CLIENT_URI_SNAKE = "client_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String CODE = "code";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String DEFAULT_ACR_VALUES = "defaultAcrValues";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String DEFAULT_ACR_VALUES_SNAKE = "default_acr_values";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String DEFAULT_MAX_AGE = "defaultMaxAge";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String DEFAULT_MAX_AGE_SNAKE = "default_max_age";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String DPOP_BOUND_ACCESS_TOKENS = "dpopBoundAccessTokens";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String DPOP_BOUND_ACCESS_TOKENS_SNAKE = "dpop_bound_access_tokens";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String DYNAMICALLY_REGISTERED = "dynamicallyRegistered";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String DYNAMICALLY_REGISTERED_SNAKE = "dynamically_registered";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ENABLED = "enabled";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String FRONTCHANNEL_LOGOUT_SESSION_REQUIRED =
      "frontchannelLogoutSessionRequired";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String FRONTCHANNEL_LOGOUT_SESSION_REQUIRED_SNAKE =
      "frontchannel_logout_session_required";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String FRONTCHANNEL_LOGOUT_URI = "frontchannelLogoutUri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String FRONTCHANNEL_LOGOUT_URI_SNAKE = "frontchannel_logout_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String GRANT_TYPES_JSON = "grantTypesJson";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String GRANT_TYPES_JSON_SNAKE = "grant_types_json";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ID_TOKEN_ENCRYPTED_RESPONSE_ALG = "idTokenEncryptedResponseAlg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ID_TOKEN_ENCRYPTED_RESPONSE_ALG_SNAKE =
      "id_token_encrypted_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ID_TOKEN_ENCRYPTED_RESPONSE_ENC = "idTokenEncryptedResponseEnc";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ID_TOKEN_ENCRYPTED_RESPONSE_ENC_SNAKE =
      "id_token_encrypted_response_enc";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ID_TOKEN_SIGNED_RESPONSE_ALG = "idTokenSignedResponseAlg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ID_TOKEN_SIGNED_RESPONSE_ALG_SNAKE = "id_token_signed_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String INTROSPECTION_ALLOWED = "introspectionAllowed";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String INTROSPECTION_ALLOWED_SNAKE = "introspection_allowed";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String JWKS_JSON = "jwksJson";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String JWKS_JSON_SNAKE = "jwks_json";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String JWKS_URI = "jwksUri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String JWKS_URI_SNAKE = "jwks_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String LOGO_URI = "logoUri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String LOGO_URI_SNAKE = "logo_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String POLICY_URI = "policyUri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String POLICY_URI_SNAKE = "policy_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REGISTERED_AT = "registeredAt";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REGISTERED_AT_SNAKE = "registered_at";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REGISTRATION_ACCESS_TOKEN = "registrationAccessToken";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REGISTRATION_ACCESS_TOKEN_SNAKE = "registration_access_token";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REQUEST_OBJECT_SIGNING_ALG = "requestObjectSigningAlg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REQUEST_OBJECT_SIGNING_ALG_SNAKE = "request_object_signing_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REQUIRE_PKCE = "requirePkce";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REQUIRE_PKCE_SNAKE = "require_pkce";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REQUIRE_PUSHED_AUTHORIZATION_REQUESTS =
      "requirePushedAuthorizationRequests";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String REQUIRE_PUSHED_AUTHORIZATION_REQUESTS_SNAKE =
      "require_pushed_authorization_requests";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String RESPONSE_TYPES_JSON = "responseTypesJson";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String RESPONSE_TYPES_JSON_SNAKE = "response_types_json";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECRET_OAUTH = "secretOauth";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECRET_OAUTH_SNAKE = "secret_oauth";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECTOR_IDENTIFIER_URI = "sectorIdentifierUri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECTOR_IDENTIFIER_URI_SNAKE = "sector_identifier_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SUBJECT_TYPE = "subjectType";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SUBJECT_TYPE_SNAKE = "subject_type";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TENANT = "tenant";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TLS_CLIENT_AUTH_SUBJECT_DN = "tlsClientAuthSubjectDn";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TLS_CLIENT_AUTH_SUBJECT_DN_SNAKE = "tls_client_auth_subject_dn";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS =
      "tlsClientCertificateBoundAccessTokens";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS_SNAKE =
      "tls_client_certificate_bound_access_tokens";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TOKEN_ENDPOINT_AUTH_METHOD = "tokenEndpointAuthMethod";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TOKEN_ENDPOINT_AUTH_METHOD_SNAKE = "token_endpoint_auth_method";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TOKEN_ENDPOINT_AUTH_SIGNING_ALG = "tokenEndpointAuthSigningAlg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TOKEN_ENDPOINT_AUTH_SIGNING_ALG_SNAKE =
      "token_endpoint_auth_signing_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TOMAP = "tomap";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TOS_URI = "tosUri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TOS_URI_SNAKE = "tos_uri";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String UID = "uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String URL = "url";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USERINFO_ENCRYPTED_RESPONSE_ALG = "userinfoEncryptedResponseAlg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USERINFO_ENCRYPTED_RESPONSE_ALG_SNAKE =
      "userinfo_encrypted_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USERINFO_ENCRYPTED_RESPONSE_ENC = "userinfoEncryptedResponseEnc";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USERINFO_ENCRYPTED_RESPONSE_ENC_SNAKE =
      "userinfo_encrypted_response_enc";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USERINFO_SIGNED_RESPONSE_ALG = "userinfoSignedResponseAlg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USERINFO_SIGNED_RESPONSE_ALG_SNAKE = "userinfo_signed_response_alg";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String VERSION = "version";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private final DataSource datasource;

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private final boolean readOnly;

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private final SecretDigester secretDigester;

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private final Tracer tracer;

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param datasource
   * @param tracer
   * @param readOnly
   * @param secretDigester
   */
  public OauthClientJdbcDao(final DataSource datasource, final Tracer tracer,
      final boolean readOnly, SecretDigester secretDigester) {
    this.datasource = datasource;
    this.tracer = tracer;
    this.readOnly = readOnly;
    this.secretDigester = secretDigester;
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public long count(OauthClientFilter filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<Long> sq = filteredQuery(template, filter);
      sq.select("count(uid) as uid");
      return sq.query(row -> Optional.of(row.getLong(1))).one().orElse(0l);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public long countForUpdate(OauthClientFilter filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<Long> sq = filteredQuery(template, filter);
      sq.select("count(uid) as uid");
      return sq.forUpdate().query(row -> Optional.of(row.getLong(1))).one().orElse(0l);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @return
   */
  public OauthClient create(OauthClient entity) {
    if (readOnly) {
      throw new RuntimeException("Write operation on read only dao");
    }
    return runCreate(entity, null);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  public OauthClient create(OauthClient entity, Predicate<OauthClient> verifier) {
    if (readOnly) {
      throw new RuntimeException("Write operation on read only dao");
    }
    return runCreate(entity, verifier);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   */
  public void delete(OauthClient entity) {
    if (readOnly) {
      throw new RuntimeException("Write operation on read only dao");
    }
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      deleteAllowedLoginRedirects(entity, template);
      deleteAllowedLogoutRedirects(entity, template);
      deleteAllowedAudiences(entity, template);
      SqlCommand sq =
          template.createSqlCommand("delete from \"access_oauth_client\" where \"uid\" = :uid");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      int num = sq.execute();
      if (0 == num) {
        throw new IllegalArgumentException("No delete from");
      }
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean exists(String uid, Optional<OauthClientFilter> filter) {
    return retrieve(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean existsForUpdate(String uid, Optional<OauthClientFilter> filter) {
    return retrieveForUpdate(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<OauthClient> find(OauthClientFilter filter) {
    OauthClientCursor cursor = OauthClientCursor.builder().build();
    return new OauthClientSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("code", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<OauthClient> find(final String sql, final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<OauthClient> createSqlQuery = template.createSqlQuery(sql);
      withChilds(createSqlQuery);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<OauthClient> findForUpdate(OauthClientFilter filter) {
    OauthClientCursor cursor = OauthClientCursor.builder().build();
    return new OauthClientSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("code", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<OauthClient> findForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<OauthClient> createSqlQuery = template.createSqlQuery(sql);
      withChilds(createSqlQuery);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<OauthClient> list(OauthClientFilter filter) {
    OauthClientCursor cursor = OauthClientCursor.builder().build();
    return new OauthClientSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("code", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<OauthClient> list(final String sql, final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("code", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<OauthClient> list(final String sql, final Map<String, SqlParameterValue> params,
      final int limit) {
    return list(sql, params, Integer.valueOf(limit), false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<OauthClient> listForUpdate(OauthClientFilter filter) {
    OauthClientCursor cursor = OauthClientCursor.builder().build();
    return new OauthClientSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("code", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<OauthClient> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("code", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<OauthClient> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params, final int limit) {
    return list(sql, params, Integer.valueOf(limit), true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public OauthClient resolve(OauthClientRef reference) {
    return reference instanceof OauthClient oauthClient ? oauthClient
        : retrieve(reference.getUid(), Optional.empty()).orElseThrow(() -> new NotFoundException(
            "Trying to resolve inexistent OauthClient: " + reference.getUid()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public OauthClient resolveForUpdate(OauthClientRef reference) {
    return reference instanceof OauthClient oauthClient ? oauthClient
        : retrieveForUpdate(reference.getUid(), Optional.empty())
            .orElseThrow(() -> new NotFoundException(
                "Trying to resolve inexistent OauthClient: " + reference.getUid()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<OauthClient> retrieve(String uid, Optional<OauthClientFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      OauthClientFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> OauthClientFilter.builder().uid(uid).build());
      SqlSchematicQuery<OauthClient> sq = filteredQuery(template, readyFilter);
      return sq.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<OauthClient> retrieveForUpdate(String uid, Optional<OauthClientFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      OauthClientFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> OauthClientFilter.builder().uid(uid).build());
      SqlSchematicQuery<OauthClient> sq = filteredQuery(template, readyFilter);
      return sq.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<OauthClient> slide(OauthClientFilter filter, OauthClientCursor cursor) {
    return new OauthClientSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<OauthClient> slideForUpdate(OauthClientFilter filter, OauthClientCursor cursor) {
    return new OauthClientSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @return
   */
  public OauthClient update(OauthClient entity) {
    if (readOnly) {
      throw new RuntimeException("Write operation on read only dao");
    }
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "update \"access_oauth_client\" set  \"code\" = :code, \"tenant\" = :tenant, \"allow_all_scopes\" = :allowAllScopes, \"token_endpoint_auth_method\" = :tokenEndpointAuthMethod, \"token_endpoint_auth_signing_alg\" = :tokenEndpointAuthSigningAlg, \"dpop_bound_access_tokens\" = :dpopBoundAccessTokens, \"id_token_signed_response_alg\" = :idTokenSignedResponseAlg, \"tls_client_auth_subject_dn\" = :tlsClientAuthSubjectDn, \"tls_client_certificate_bound_access_tokens\" = :tlsClientCertificateBoundAccessTokens, \"authorization_signed_response_alg\" = :authorizationSignedResponseAlg, \"backchannel_token_delivery_mode\" = :backchannelTokenDeliveryMode, \"backchannel_client_notification_endpoint\" = :backchannelClientNotificationEndpoint, \"subject_type\" = :subjectType, \"id_token_encrypted_response_alg\" = :idTokenEncryptedResponseAlg, \"id_token_encrypted_response_enc\" = :idTokenEncryptedResponseEnc, \"userinfo_encrypted_response_alg\" = :userinfoEncryptedResponseAlg, \"userinfo_encrypted_response_enc\" = :userinfoEncryptedResponseEnc, \"authorization_details_types\" = :authorizationDetailsTypes, \"userinfo_signed_response_alg\" = :userinfoSignedResponseAlg, \"secret_oauth\" = :secretOauth, \"registration_access_token\" = :registrationAccessToken, \"default_acr_values\" = :defaultAcrValues, \"default_max_age\" = :defaultMaxAge, \"sector_identifier_uri\" = :sectorIdentifierUri, \"grant_types_json\" = :grantTypesJson, \"introspection_allowed\" = :introspectionAllowed, \"require_pushed_authorization_requests\" = :requirePushedAuthorizationRequests, \"require_pkce\" = :requirePkce, \"backchannel_logout_uri\" = :backchannelLogoutUri, \"backchannel_logout_session_required\" = :backchannelLogoutSessionRequired, \"frontchannel_logout_uri\" = :frontchannelLogoutUri, \"frontchannel_logout_session_required\" = :frontchannelLogoutSessionRequired, \"enabled\" = :enabled, \"client_name\" = :clientName, \"logo_uri\" = :logoUri, \"client_uri\" = :clientUri, \"policy_uri\" = :policyUri, \"tos_uri\" = :tosUri, \"response_types_json\" = :responseTypesJson, \"dynamically_registered\" = :dynamicallyRegistered, \"registered_at\" = :registeredAt, \"client_credentials_scopes\" = :clientCredentialsScopes, \"client_credentials_token_ttl_seconds\" = :clientCredentialsTokenTtlSeconds, \"request_object_signing_alg\" = :requestObjectSigningAlg, \"jwks_uri\" = :jwksUri, \"jwks_json\" = :jwksJson, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      sq.with(CODE, SqlParameterValue.of(entity.getCode()));
      sq.with(TENANT, entity.getTenantUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(ALLOW_ALL_SCOPES, SqlParameterValue.of(entity.isAllowAllScopes()));
      sq.with(TOKEN_ENDPOINT_AUTH_METHOD,
          SqlParameterValue.of(entity.getTokenEndpointAuthMethod().toString()));
      sq.with(TOKEN_ENDPOINT_AUTH_SIGNING_ALG, entity.getTokenEndpointAuthSigningAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(DPOP_BOUND_ACCESS_TOKENS, SqlParameterValue.of(entity.isDpopBoundAccessTokens()));
      sq.with(ID_TOKEN_SIGNED_RESPONSE_ALG,
          entity.getIdTokenSignedResponseAlg()
              .map(OauthClientIdTokenSignedResponseAlgOptions::toString).map(SqlParameterValue::of)
              .orElseGet(SqlParameterValue::ofNullString));
      sq.with(TLS_CLIENT_AUTH_SUBJECT_DN, entity.getTlsClientAuthSubjectDn()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS,
          SqlParameterValue.of(entity.isTlsClientCertificateBoundAccessTokens()));
      sq.with(AUTHORIZATION_SIGNED_RESPONSE_ALG, entity.getAuthorizationSignedResponseAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(BACKCHANNEL_TOKEN_DELIVERY_MODE,
          entity.getBackchannelTokenDeliveryMode()
              .map(OauthClientBackchannelTokenDeliveryModeOptions::toString)
              .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(BACKCHANNEL_CLIENT_NOTIFICATION_ENDPOINT,
          entity.getBackchannelClientNotificationEndpoint().map(SqlParameterValue::of)
              .orElseGet(SqlParameterValue::ofNullString));
      sq.with(SUBJECT_TYPE, SqlParameterValue.of(entity.getSubjectType().toString()));
      sq.with(ID_TOKEN_ENCRYPTED_RESPONSE_ALG, entity.getIdTokenEncryptedResponseAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(ID_TOKEN_ENCRYPTED_RESPONSE_ENC, entity.getIdTokenEncryptedResponseEnc()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(USERINFO_ENCRYPTED_RESPONSE_ALG, entity.getUserinfoEncryptedResponseAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(USERINFO_ENCRYPTED_RESPONSE_ENC, entity.getUserinfoEncryptedResponseEnc()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(AUTHORIZATION_DETAILS_TYPES, entity.getAuthorizationDetailsTypes()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(USERINFO_SIGNED_RESPONSE_ALG, entity.getUserinfoSignedResponseAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(SECRET_OAUTH, entity.getSecretOauthStored(secretDigester).map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(REGISTRATION_ACCESS_TOKEN, entity.getRegistrationAccessTokenStored(secretDigester)
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(DEFAULT_ACR_VALUES, entity.getDefaultAcrValues().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(DEFAULT_MAX_AGE, entity.getDefaultMaxAge().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      sq.with(SECTOR_IDENTIFIER_URI, entity.getSectorIdentifierUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(GRANT_TYPES_JSON, entity.getGrantTypesJson().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(INTROSPECTION_ALLOWED, SqlParameterValue.of(entity.isIntrospectionAllowed()));
      sq.with(REQUIRE_PUSHED_AUTHORIZATION_REQUESTS,
          SqlParameterValue.of(entity.isRequirePushedAuthorizationRequests()));
      sq.with(REQUIRE_PKCE, SqlParameterValue.of(entity.isRequirePkce()));
      sq.with(BACKCHANNEL_LOGOUT_URI, entity.getBackchannelLogoutUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(BACKCHANNEL_LOGOUT_SESSION_REQUIRED,
          SqlParameterValue.of(entity.isBackchannelLogoutSessionRequired()));
      sq.with(FRONTCHANNEL_LOGOUT_URI, entity.getFrontchannelLogoutUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(FRONTCHANNEL_LOGOUT_SESSION_REQUIRED,
          SqlParameterValue.of(entity.isFrontchannelLogoutSessionRequired()));
      sq.with(ENABLED, SqlParameterValue.of(entity.isEnabled()));
      sq.with(CLIENT_NAME, entity.getClientName().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(LOGO_URI, entity.getLogoUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(CLIENT_URI, entity.getClientUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(POLICY_URI, entity.getPolicyUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(TOS_URI,
          entity.getTosUri().map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(RESPONSE_TYPES_JSON, entity.getResponseTypesJson().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(DYNAMICALLY_REGISTERED, SqlParameterValue.of(entity.isDynamicallyRegistered()));
      sq.with(REGISTERED_AT, entity.getRegisteredAt().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullOffsetDateTime));
      sq.with(CLIENT_CREDENTIALS_SCOPES, entity.getClientCredentialsScopes()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(CLIENT_CREDENTIALS_TOKEN_TTL_SECONDS,
          SqlParameterValue.of(entity.getClientCredentialsTokenTtlSeconds()));
      sq.with(REQUEST_OBJECT_SIGNING_ALG, entity.getRequestObjectSigningAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(JWKS_URI, entity.getJwksUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(JWKS_JSON, entity.getJwksJson().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(VERSION, entity.getVersion().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      int num = sq.execute();
      if (0 == num) {
        throw new OptimistLockException("No delete from");
      }
      OauthClient result = entity.withNextVersion();
      saveAllowedLoginRedirects(result, result.getAllowedLoginRedirects());
      saveAllowedLogoutRedirects(result, result.getAllowedLogoutRedirects());
      saveAllowedAudiences(result, result.getAllowedAudiences());
      return result;
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sq
   * @param cursor
   */
  private void attachWithOrder(SqlSchematicQuery<OauthClient> sq, OauthClientCursor cursor) {
    PartialWhere[] offset = new PartialWhere[] {PartialWhere.empty(), PartialWhere.empty()};
    for (OauthClientOrder order : cursor.getOrder()) {
      tryToOrderByCodeAsc(sq, offset, cursor, order);
      tryToOrderByCodeDesc(sq, offset, cursor, order);
    }
    Optional<String> sinceUid = cursor.getSinceUid();
    if (sinceUid.isPresent()) {
      offset[0] = PartialWhere.or(offset[0], PartialWhere.and(offset[1],
          PartialWhere.where("uid", SqlOperator.GT, SqlParameterValue.of(sinceUid.get()))));
    }
    sq.where(offset[0]);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @return
   */
  private SqlConverter<OauthClient> converter() {
    return row -> {
      try {
        List<AllowedLoginRedirects> allowedLoginRedirectsFromChilds = new ArrayList<>();
        row.<AllowedLoginRedirects>getChilds("allowedLoginRedirects")
            .whenComplete((childlist, _) -> allowedLoginRedirectsFromChilds.addAll(childlist));
        List<AllowedLogoutRedirects> allowedLogoutRedirectsFromChilds = new ArrayList<>();
        row.<AllowedLogoutRedirects>getChilds("allowedLogoutRedirects")
            .whenComplete((childlist, _) -> allowedLogoutRedirectsFromChilds.addAll(childlist));
        List<AllowedAudiences> allowedAudiencesFromChilds = new ArrayList<>();
        row.<AllowedAudiences>getChilds("allowedAudiences")
            .whenComplete((childlist, _) -> allowedAudiencesFromChilds.addAll(childlist));
        return Optional.of(OauthClient.builder().uidValue(UidVO.from(row.getString(UID)))
            .codeValue(CodeVO.from(row.getString(CODE)))
            .tenantValue(TenantVO.fromReference(row.getString(TENANT)))
            .allowAllScopesValue(AllowAllScopesVO.from(row.getBoolean(ALLOW_ALL_SCOPES_SNAKE)))
            .tokenEndpointAuthMethodValue(
                TokenEndpointAuthMethodVO.tryFrom(row.getString(TOKEN_ENDPOINT_AUTH_METHOD_SNAKE)))
            .tokenEndpointAuthSigningAlgValue(TokenEndpointAuthSigningAlgVO
                .from(row.getString(TOKEN_ENDPOINT_AUTH_SIGNING_ALG_SNAKE)))
            .dpopBoundAccessTokensValue(
                DpopBoundAccessTokensVO.from(row.getBoolean(DPOP_BOUND_ACCESS_TOKENS_SNAKE)))
            .idTokenSignedResponseAlgValue(IdTokenSignedResponseAlgVO
                .tryFrom(row.getString(ID_TOKEN_SIGNED_RESPONSE_ALG_SNAKE)))
            .tlsClientAuthSubjectDnValue(
                TlsClientAuthSubjectDnVO.from(row.getString(TLS_CLIENT_AUTH_SUBJECT_DN_SNAKE)))
            .tlsClientCertificateBoundAccessTokensValue(TlsClientCertificateBoundAccessTokensVO
                .from(row.getBoolean(TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS_SNAKE)))
            .authorizationSignedResponseAlgValue(AuthorizationSignedResponseAlgVO
                .from(row.getString(AUTHORIZATION_SIGNED_RESPONSE_ALG_SNAKE)))
            .backchannelTokenDeliveryModeValue(BackchannelTokenDeliveryModeVO
                .tryFrom(row.getString(BACKCHANNEL_TOKEN_DELIVERY_MODE_SNAKE)))
            .backchannelClientNotificationEndpointValue(BackchannelClientNotificationEndpointVO
                .from(row.getString(BACKCHANNEL_CLIENT_NOTIFICATION_ENDPOINT_SNAKE)))
            .subjectTypeValue(SubjectTypeVO.tryFrom(row.getString(SUBJECT_TYPE_SNAKE)))
            .idTokenEncryptedResponseAlgValue(IdTokenEncryptedResponseAlgVO
                .from(row.getString(ID_TOKEN_ENCRYPTED_RESPONSE_ALG_SNAKE)))
            .idTokenEncryptedResponseEncValue(IdTokenEncryptedResponseEncVO
                .from(row.getString(ID_TOKEN_ENCRYPTED_RESPONSE_ENC_SNAKE)))
            .userinfoEncryptedResponseAlgValue(UserinfoEncryptedResponseAlgVO
                .from(row.getString(USERINFO_ENCRYPTED_RESPONSE_ALG_SNAKE)))
            .userinfoEncryptedResponseEncValue(UserinfoEncryptedResponseEncVO
                .from(row.getString(USERINFO_ENCRYPTED_RESPONSE_ENC_SNAKE)))
            .authorizationDetailsTypesValue(
                AuthorizationDetailsTypesVO.from(row.getString(AUTHORIZATION_DETAILS_TYPES_SNAKE)))
            .userinfoSignedResponseAlgValue(
                UserinfoSignedResponseAlgVO.from(row.getString(USERINFO_SIGNED_RESPONSE_ALG_SNAKE)))
            .secretOauthValue(SecretOauthVO.fromStored(row.getString(SECRET_OAUTH_SNAKE)))
            .registrationAccessTokenValue(RegistrationAccessTokenVO
                .fromStored(row.getString(REGISTRATION_ACCESS_TOKEN_SNAKE)))
            .defaultAcrValuesValue(DefaultAcrValuesVO.from(row.getString(DEFAULT_ACR_VALUES_SNAKE)))
            .defaultMaxAgeValue(DefaultMaxAgeVO.from(row.getInt(DEFAULT_MAX_AGE_SNAKE)))
            .sectorIdentifierUriValue(
                SectorIdentifierUriVO.from(row.getString(SECTOR_IDENTIFIER_URI_SNAKE)))
            .allowedLoginRedirectsValue(
                AllowedLoginRedirectsVO.from(allowedLoginRedirectsFromChilds))
            .allowedLogoutRedirectsValue(
                AllowedLogoutRedirectsVO.from(allowedLogoutRedirectsFromChilds))
            .allowedAudiencesValue(AllowedAudiencesVO.from(allowedAudiencesFromChilds))
            .grantTypesJsonValue(GrantTypesJsonVO.from(row.getString(GRANT_TYPES_JSON_SNAKE)))
            .introspectionAllowedValue(
                IntrospectionAllowedVO.from(row.getBoolean(INTROSPECTION_ALLOWED_SNAKE)))
            .requirePushedAuthorizationRequestsValue(RequirePushedAuthorizationRequestsVO
                .from(row.getBoolean(REQUIRE_PUSHED_AUTHORIZATION_REQUESTS_SNAKE)))
            .requirePkceValue(RequirePkceVO.from(row.getBoolean(REQUIRE_PKCE_SNAKE)))
            .backchannelLogoutUriValue(
                BackchannelLogoutUriVO.from(row.getString(BACKCHANNEL_LOGOUT_URI_SNAKE)))
            .backchannelLogoutSessionRequiredValue(BackchannelLogoutSessionRequiredVO
                .from(row.getBoolean(BACKCHANNEL_LOGOUT_SESSION_REQUIRED_SNAKE)))
            .frontchannelLogoutUriValue(
                FrontchannelLogoutUriVO.from(row.getString(FRONTCHANNEL_LOGOUT_URI_SNAKE)))
            .frontchannelLogoutSessionRequiredValue(FrontchannelLogoutSessionRequiredVO
                .from(row.getBoolean(FRONTCHANNEL_LOGOUT_SESSION_REQUIRED_SNAKE)))
            .enabledValue(EnabledVO.from(row.getBoolean(ENABLED)))
            .clientNameValue(ClientNameVO.from(row.getString(CLIENT_NAME_SNAKE)))
            .logoUriValue(LogoUriVO.from(row.getString(LOGO_URI_SNAKE)))
            .clientUriValue(ClientUriVO.from(row.getString(CLIENT_URI_SNAKE)))
            .policyUriValue(PolicyUriVO.from(row.getString(POLICY_URI_SNAKE)))
            .tosUriValue(TosUriVO.from(row.getString(TOS_URI_SNAKE)))
            .responseTypesJsonValue(
                ResponseTypesJsonVO.from(row.getString(RESPONSE_TYPES_JSON_SNAKE)))
            .dynamicallyRegisteredValue(
                DynamicallyRegisteredVO.from(row.getBoolean(DYNAMICALLY_REGISTERED_SNAKE)))
            .registeredAtValue(
                RegisteredAtVO.from(null == row.getTimestamp(REGISTERED_AT_SNAKE) ? null
                    : OffsetDateTime.ofInstant(
                        Instant.ofEpochMilli(row.getTimestamp(REGISTERED_AT_SNAKE).getTime()),
                        ZoneId.systemDefault())))
            .clientCredentialsScopesValue(
                ClientCredentialsScopesVO.from(row.getString(CLIENT_CREDENTIALS_SCOPES_SNAKE)))
            .clientCredentialsTokenTtlSecondsValue(ClientCredentialsTokenTtlSecondsVO
                .from(row.getInt(CLIENT_CREDENTIALS_TOKEN_TTL_SECONDS_SNAKE)))
            .requestObjectSigningAlgValue(
                RequestObjectSigningAlgVO.from(row.getString(REQUEST_OBJECT_SIGNING_ALG_SNAKE)))
            .jwksUriValue(JwksUriVO.from(row.getString(JWKS_URI_SNAKE)))
            .jwksJsonValue(JwksJsonVO.from(row.getString(JWKS_JSON_SNAKE)))
            .versionValue(VersionVO.from(row.getInt(VERSION))).build());
      } catch (ConstraintException ce) {
        log.error("Unable to map data for {}", row.getString(1), ce);
        return Optional.empty();
      }
    };
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param template
   */
  private void deleteAllowedAudiences(OauthClient entity, SqlTemplate template) {
    SqlCommand sq = template.createSqlCommand(
        "delete from \"access_oauth_client_allowed_audience\" where \"client\" = :client");
    sq.with(CLIENT, SqlParameterValue.of(entity.getUid()));
    sq.execute();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param template
   */
  private void deleteAllowedLoginRedirects(OauthClient entity, SqlTemplate template) {
    SqlCommand sq = template.createSqlCommand(
        "delete from \"access_oauth_client_allowed_login_redirect\" where \"client\" = :client");
    sq.with(CLIENT, SqlParameterValue.of(entity.getUid()));
    sq.execute();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param template
   */
  private void deleteAllowedLogoutRedirects(OauthClient entity, SqlTemplate template) {
    SqlCommand sq = template.createSqlCommand(
        "delete from \"access_oauth_client_allowed_logout_redirect\" where \"client\" = :client");
    sq.with(CLIENT, SqlParameterValue.of(entity.getUid()));
    sq.execute();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param template
   * @param filter
   * @return
   */
  private <T> SqlSchematicQuery<T> filteredQuery(SqlTemplate template, OauthClientFilter filter) {
    SqlSchematicQuery<T> sq = template.createSqlSchematicQuery(ACCESS_OAUTH_CLIENT_SNAKE);
    sq.selectFields(ACCESS_OAUTH_CLIENT_UID, ACCESS_OAUTH_CLIENT_CODE, ACCESS_OAUTH_CLIENT_TENANT,
        ACCESS_OAUTH_CLIENT_ALLOW_ALL_SCOPES, ACCESS_OAUTH_CLIENT_TOKEN_ENDPOINT_AUTH_METHOD,
        ACCESS_OAUTH_CLIENT_TOKEN_ENDPOINT_AUTH_SIGNING_ALG,
        ACCESS_OAUTH_CLIENT_DPOP_BOUND_ACCESS_TOKENS,
        ACCESS_OAUTH_CLIENT_ID_TOKEN_SIGNED_RESPONSE_ALG,
        ACCESS_OAUTH_CLIENT_TLS_CLIENT_AUTH_SUBJECT_DN,
        ACCESS_OAUTH_CLIENT_TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS,
        ACCESS_OAUTH_CLIENT_AUTHORIZATION_SIGNED_RESPONSE_ALG,
        ACCESS_OAUTH_CLIENT_BACKCHANNEL_TOKEN_DELIVERY_MODE,
        ACCESS_OAUTH_CLIENT_BACKCHANNEL_CLIENT_NOTIFICATION_ENDPOINT,
        ACCESS_OAUTH_CLIENT_SUBJECT_TYPE, ACCESS_OAUTH_CLIENT_ID_TOKEN_ENCRYPTED_RESPONSE_ALG,
        ACCESS_OAUTH_CLIENT_ID_TOKEN_ENCRYPTED_RESPONSE_ENC,
        ACCESS_OAUTH_CLIENT_USERINFO_ENCRYPTED_RESPONSE_ALG,
        ACCESS_OAUTH_CLIENT_USERINFO_ENCRYPTED_RESPONSE_ENC,
        ACCESS_OAUTH_CLIENT_AUTHORIZATION_DETAILS_TYPES,
        ACCESS_OAUTH_CLIENT_USERINFO_SIGNED_RESPONSE_ALG, ACCESS_OAUTH_CLIENT_SECRET_OAUTH,
        ACCESS_OAUTH_CLIENT_REGISTRATION_ACCESS_TOKEN, ACCESS_OAUTH_CLIENT_DEFAULT_ACR_VALUES,
        ACCESS_OAUTH_CLIENT_DEFAULT_MAX_AGE, ACCESS_OAUTH_CLIENT_SECTOR_IDENTIFIER_URI,
        ACCESS_OAUTH_CLIENT_GRANT_TYPES_JSON, ACCESS_OAUTH_CLIENT_INTROSPECTION_ALLOWED,
        ACCESS_OAUTH_CLIENT_REQUIRE_PUSHED_AUTHORIZATION_REQUESTS, ACCESS_OAUTH_CLIENT_REQUIRE_PKCE,
        ACCESS_OAUTH_CLIENT_BACKCHANNEL_LOGOUT_URI,
        ACCESS_OAUTH_CLIENT_BACKCHANNEL_LOGOUT_SESSION_REQUIRED,
        ACCESS_OAUTH_CLIENT_FRONTCHANNEL_LOGOUT_URI,
        ACCESS_OAUTH_CLIENT_FRONTCHANNEL_LOGOUT_SESSION_REQUIRED, ACCESS_OAUTH_CLIENT_ENABLED,
        ACCESS_OAUTH_CLIENT_CLIENT_NAME, ACCESS_OAUTH_CLIENT_LOGO_URI,
        ACCESS_OAUTH_CLIENT_CLIENT_URI, ACCESS_OAUTH_CLIENT_POLICY_URI, ACCESS_OAUTH_CLIENT_TOS_URI,
        ACCESS_OAUTH_CLIENT_RESPONSE_TYPES_JSON, ACCESS_OAUTH_CLIENT_DYNAMICALLY_REGISTERED,
        ACCESS_OAUTH_CLIENT_REGISTERED_AT, ACCESS_OAUTH_CLIENT_CLIENT_CREDENTIALS_SCOPES,
        ACCESS_OAUTH_CLIENT_CLIENT_CREDENTIALS_TOKEN_TTL_SECONDS,
        ACCESS_OAUTH_CLIENT_REQUEST_OBJECT_SIGNING_ALG, ACCESS_OAUTH_CLIENT_JWKS_URI,
        ACCESS_OAUTH_CLIENT_JWKS_JSON, ACCESS_OAUTH_CLIENT_VERSION);
    withChilds(sq);
    filter.getUid().ifPresent(uid -> sq.where(UID, SqlOperator.EQ, SqlParameterValue.of(uid)));
    if (!filter.getUids().isEmpty()) {
      sq.where(UID, SqlOperator.IN, SqlListParameterValue.strings(filter.getUids()));
    }
    filter.getSearch().ifPresent(
        search -> sq.where("code", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%")));
    filter.getWithBackchannelLogoutUrl().filter(Boolean.TRUE::equals)
        .ifPresent(_ -> sq.where(BACKCHANNEL_LOGOUT_URI_SNAKE, SqlOperator.IS_NULL,
            SqlParameterValue.ofNullString()));
    filter.getWithFrontchannelLogoutUrl().filter(Boolean.TRUE::equals)
        .ifPresent(_ -> sq.where(FRONTCHANNEL_LOGOUT_URI_SNAKE, SqlOperator.IS_NULL,
            SqlParameterValue.ofNullString()));
    filter.getCode()
        .ifPresent(codeParam -> sq.where(CODE, SqlOperator.EQ, SqlParameterValue.of(codeParam)));
    filter.getTenant().ifPresent(tenantParam -> sq.where(TENANT, SqlOperator.EQ,
        SqlParameterValue.of(tenantParam.getUid())));
    if (!filter.getTenants().isEmpty()) {
      sq.where(TENANT, SqlOperator.IN, SqlListParameterValue.strings(filter.getTenants()));
    }
    filter.getTenantAccessible()
        .ifPresent(tenantAccessibleParam -> sq.where(ACCESS_OAUTH_CLIENT_TENANT, SqlOperator.EQ,
            SqlParameterValue.of(tenantAccessibleParam)));
    return sq;
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @param forUpdate
   * @return sq.where("code", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  private List<OauthClient> list(final String sql, final Map<String, SqlParameterValue> params,
      final Integer limit, final boolean forUpdate) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<OauthClient> createSqlQuery = template.createSqlQuery(sql);
      withChilds(createSqlQuery);
      params.forEach(createSqlQuery::with);
      if (forUpdate) {
        createSqlQuery = createSqlQuery.forUpdate();
      }
      SqlResult<OauthClient> query = createSqlQuery.query(converter());
      return null == limit ? query.all() : query.limit(limit);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  private OauthClient runCreate(OauthClient entity, Predicate<OauthClient> verifier) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "insert into \"access_oauth_client\" ( \"uid\", \"code\", \"tenant\", \"allow_all_scopes\", \"token_endpoint_auth_method\", \"token_endpoint_auth_signing_alg\", \"dpop_bound_access_tokens\", \"id_token_signed_response_alg\", \"tls_client_auth_subject_dn\", \"tls_client_certificate_bound_access_tokens\", \"authorization_signed_response_alg\", \"backchannel_token_delivery_mode\", \"backchannel_client_notification_endpoint\", \"subject_type\", \"id_token_encrypted_response_alg\", \"id_token_encrypted_response_enc\", \"userinfo_encrypted_response_alg\", \"userinfo_encrypted_response_enc\", \"authorization_details_types\", \"userinfo_signed_response_alg\", \"secret_oauth\", \"registration_access_token\", \"default_acr_values\", \"default_max_age\", \"sector_identifier_uri\", \"grant_types_json\", \"introspection_allowed\", \"require_pushed_authorization_requests\", \"require_pkce\", \"backchannel_logout_uri\", \"backchannel_logout_session_required\", \"frontchannel_logout_uri\", \"frontchannel_logout_session_required\", \"enabled\", \"client_name\", \"logo_uri\", \"client_uri\", \"policy_uri\", \"tos_uri\", \"response_types_json\", \"dynamically_registered\", \"registered_at\", \"client_credentials_scopes\", \"client_credentials_token_ttl_seconds\", \"request_object_signing_alg\", \"jwks_uri\", \"jwks_json\", \"version\") values ( :uid, :code, :tenant, :allowAllScopes, :tokenEndpointAuthMethod, :tokenEndpointAuthSigningAlg, :dpopBoundAccessTokens, :idTokenSignedResponseAlg, :tlsClientAuthSubjectDn, :tlsClientCertificateBoundAccessTokens, :authorizationSignedResponseAlg, :backchannelTokenDeliveryMode, :backchannelClientNotificationEndpoint, :subjectType, :idTokenEncryptedResponseAlg, :idTokenEncryptedResponseEnc, :userinfoEncryptedResponseAlg, :userinfoEncryptedResponseEnc, :authorizationDetailsTypes, :userinfoSignedResponseAlg, :secretOauth, :registrationAccessToken, :defaultAcrValues, :defaultMaxAge, :sectorIdentifierUri, :grantTypesJson, :introspectionAllowed, :requirePushedAuthorizationRequests, :requirePkce, :backchannelLogoutUri, :backchannelLogoutSessionRequired, :frontchannelLogoutUri, :frontchannelLogoutSessionRequired, :enabled, :clientName, :logoUri, :clientUri, :policyUri, :tosUri, :responseTypesJson, :dynamicallyRegistered, :registeredAt, :clientCredentialsScopes, :clientCredentialsTokenTtlSeconds, :requestObjectSigningAlg, :jwksUri, :jwksJson, :version)");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      sq.with(CODE, SqlParameterValue.of(entity.getCode()));
      sq.with(TENANT, entity.getTenantUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(ALLOW_ALL_SCOPES, SqlParameterValue.of(entity.isAllowAllScopes()));
      sq.with(TOKEN_ENDPOINT_AUTH_METHOD,
          SqlParameterValue.of(entity.getTokenEndpointAuthMethod().toString()));
      sq.with(TOKEN_ENDPOINT_AUTH_SIGNING_ALG, entity.getTokenEndpointAuthSigningAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(DPOP_BOUND_ACCESS_TOKENS, SqlParameterValue.of(entity.isDpopBoundAccessTokens()));
      sq.with(ID_TOKEN_SIGNED_RESPONSE_ALG,
          entity.getIdTokenSignedResponseAlg()
              .map(OauthClientIdTokenSignedResponseAlgOptions::toString).map(SqlParameterValue::of)
              .orElseGet(SqlParameterValue::ofNullString));
      sq.with(TLS_CLIENT_AUTH_SUBJECT_DN, entity.getTlsClientAuthSubjectDn()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS,
          SqlParameterValue.of(entity.isTlsClientCertificateBoundAccessTokens()));
      sq.with(AUTHORIZATION_SIGNED_RESPONSE_ALG, entity.getAuthorizationSignedResponseAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(BACKCHANNEL_TOKEN_DELIVERY_MODE,
          entity.getBackchannelTokenDeliveryMode()
              .map(OauthClientBackchannelTokenDeliveryModeOptions::toString)
              .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(BACKCHANNEL_CLIENT_NOTIFICATION_ENDPOINT,
          entity.getBackchannelClientNotificationEndpoint().map(SqlParameterValue::of)
              .orElseGet(SqlParameterValue::ofNullString));
      sq.with(SUBJECT_TYPE, SqlParameterValue.of(entity.getSubjectType().toString()));
      sq.with(ID_TOKEN_ENCRYPTED_RESPONSE_ALG, entity.getIdTokenEncryptedResponseAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(ID_TOKEN_ENCRYPTED_RESPONSE_ENC, entity.getIdTokenEncryptedResponseEnc()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(USERINFO_ENCRYPTED_RESPONSE_ALG, entity.getUserinfoEncryptedResponseAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(USERINFO_ENCRYPTED_RESPONSE_ENC, entity.getUserinfoEncryptedResponseEnc()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(AUTHORIZATION_DETAILS_TYPES, entity.getAuthorizationDetailsTypes()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(USERINFO_SIGNED_RESPONSE_ALG, entity.getUserinfoSignedResponseAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(SECRET_OAUTH, entity.getSecretOauthStored(secretDigester).map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(REGISTRATION_ACCESS_TOKEN, entity.getRegistrationAccessTokenStored(secretDigester)
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(DEFAULT_ACR_VALUES, entity.getDefaultAcrValues().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(DEFAULT_MAX_AGE, entity.getDefaultMaxAge().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      sq.with(SECTOR_IDENTIFIER_URI, entity.getSectorIdentifierUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(GRANT_TYPES_JSON, entity.getGrantTypesJson().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(INTROSPECTION_ALLOWED, SqlParameterValue.of(entity.isIntrospectionAllowed()));
      sq.with(REQUIRE_PUSHED_AUTHORIZATION_REQUESTS,
          SqlParameterValue.of(entity.isRequirePushedAuthorizationRequests()));
      sq.with(REQUIRE_PKCE, SqlParameterValue.of(entity.isRequirePkce()));
      sq.with(BACKCHANNEL_LOGOUT_URI, entity.getBackchannelLogoutUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(BACKCHANNEL_LOGOUT_SESSION_REQUIRED,
          SqlParameterValue.of(entity.isBackchannelLogoutSessionRequired()));
      sq.with(FRONTCHANNEL_LOGOUT_URI, entity.getFrontchannelLogoutUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(FRONTCHANNEL_LOGOUT_SESSION_REQUIRED,
          SqlParameterValue.of(entity.isFrontchannelLogoutSessionRequired()));
      sq.with(ENABLED, SqlParameterValue.of(entity.isEnabled()));
      sq.with(CLIENT_NAME, entity.getClientName().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(LOGO_URI, entity.getLogoUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(CLIENT_URI, entity.getClientUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(POLICY_URI, entity.getPolicyUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(TOS_URI,
          entity.getTosUri().map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(RESPONSE_TYPES_JSON, entity.getResponseTypesJson().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(DYNAMICALLY_REGISTERED, SqlParameterValue.of(entity.isDynamicallyRegistered()));
      sq.with(REGISTERED_AT, entity.getRegisteredAt().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullOffsetDateTime));
      sq.with(CLIENT_CREDENTIALS_SCOPES, entity.getClientCredentialsScopes()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(CLIENT_CREDENTIALS_TOKEN_TTL_SECONDS,
          SqlParameterValue.of(entity.getClientCredentialsTokenTtlSeconds()));
      sq.with(REQUEST_OBJECT_SIGNING_ALG, entity.getRequestObjectSigningAlg()
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(JWKS_URI, entity.getJwksUri().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(JWKS_JSON, entity.getJwksJson().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(VERSION, entity.getVersion().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      int num = sq.execute();
      if (0 == num) {
        throw new IllegalArgumentException("No insert into");
      }
      OauthClient result =
          verifier == null ? entity : verified(verifier.test(entity), entity, template);
      result = saveAllowedLoginRedirects(result, result.getAllowedLoginRedirects());
      result = saveAllowedLogoutRedirects(result, result.getAllowedLogoutRedirects());
      result = saveAllowedAudiences(result, result.getAllowedAudiences());
      return result;
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  private Iterator<OauthClient> runList(OauthClientFilter filter, OauthClientCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<OauthClient> sq = filteredQuery(template, filter);
      if (null != cursor.getOrder()) {
        attachWithOrder(sq, cursor);
      } else {
        cursor.getSinceUid()
            .ifPresent(since -> sq.where(UID, SqlOperator.GT, SqlParameterValue.of(since)));
      }
      sq.orderAsc("uid");
      return sq.query(converter()).limit(cursor.getLimit()).iterator();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  private Iterator<OauthClient> runListForUpdate(OauthClientFilter filter,
      OauthClientCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<OauthClient> sq = filteredQuery(template, filter);
      if (null != cursor.getOrder()) {
        attachWithOrder(sq, cursor);
      } else {
        cursor.getSinceUid()
            .ifPresent(since -> sq.where(UID, SqlOperator.GT, SqlParameterValue.of(since)));
      }
      sq.orderAsc("uid");
      return sq.forUpdate().query(converter()).limit(cursor.getLimit()).iterator();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param childs
   * @return
   */
  private OauthClient saveAllowedAudiences(OauthClient entity, List<AllowedAudiences> childs) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<Object[]> sq = template.createSqlQuery(
          "select \"uid\", \"client\" from \"access_oauth_client_allowed_audience\" where (\"client\" = :client)"
              + " or ( \"uid\" in (:tomap) and \"client\" != :client )");
      sq.with(CLIENT, SqlParameterValue.of(entity.getUid()));
      sq.with(TOMAP,
          SqlListParameterValue.strings(childs.stream().map(AllowedAudiences::getUid).toList()));
      List<Object[]> list =
          sq.query(row -> Optional.of(new Object[] {row.getString(1), row.getString(2)})).all();
      if (list.stream().anyMatch(arr -> !entity.getUid().equals(arr[1]))) {
        throw new IllegalArgumentException("Try to move from another parent");
      }
      List<AllowedAudiences> response = new ArrayList<>();
      List<String> codes = new ArrayList<>(list.stream().map(arr -> (String) arr[0]).toList());
      childs.forEach(child -> codes.remove(child.getUid()));
      if (!codes.isEmpty()) {
        SqlCommand deleted = template.createSqlCommand(
            "delete from\"access_oauth_client_allowed_audience\" where \"uid\" in (:codes)");
        deleted.with("codes", SqlListParameterValue.strings(codes));
        deleted.execute();
      }
      childs.forEach(child -> {
        SqlCommand command = template.createSqlCommand(codes.contains(child.getUid())
            ? "update \"access_oauth_client_allowed_audience\" set \"verify\" = :verify, \"medal\" = :medal, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version"
            : "insert into \"access_oauth_client_allowed_audience\" ( \"uid\", \"client\", \"audience\", \"version\") values ( :uid, :client, :audience, :version)");
        command.with(UID, SqlParameterValue.of(child.getUid()));
        command.with(CLIENT, SqlParameterValue.of(entity.getUid()));
        command.with(AUDIENCE, SqlParameterValue.of(child.getAudience()));
        command.with(VERSION, child.getVersion().map(SqlParameterValue::of)
            .orElseGet(SqlParameterValue::ofNullInteger));
        int val = command.execute();
        if (val != 1) {
          throw new OptimistLockException("No child processed");
        } else {
          response.add(child.withNextVersion());
        }
      });
      // TODO: need to save througn entity AllowedAudiences => AllowedAudiencesVO::from[ result ] ;
      return entity;
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param childs
   * @return
   */
  private OauthClient saveAllowedLoginRedirects(OauthClient entity,
      List<AllowedLoginRedirects> childs) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<Object[]> sq = template.createSqlQuery(
          "select \"uid\", \"client\" from \"access_oauth_client_allowed_login_redirect\" where (\"client\" = :client)"
              + " or ( \"uid\" in (:tomap) and \"client\" != :client )");
      sq.with(CLIENT, SqlParameterValue.of(entity.getUid()));
      sq.with(TOMAP, SqlListParameterValue
          .strings(childs.stream().map(AllowedLoginRedirects::getUid).toList()));
      List<Object[]> list =
          sq.query(row -> Optional.of(new Object[] {row.getString(1), row.getString(2)})).all();
      if (list.stream().anyMatch(arr -> !entity.getUid().equals(arr[1]))) {
        throw new IllegalArgumentException("Try to move from another parent");
      }
      List<AllowedLoginRedirects> response = new ArrayList<>();
      List<String> codes = new ArrayList<>(list.stream().map(arr -> (String) arr[0]).toList());
      childs.forEach(child -> codes.remove(child.getUid()));
      if (!codes.isEmpty()) {
        SqlCommand deleted = template.createSqlCommand(
            "delete from\"access_oauth_client_allowed_login_redirect\" where \"uid\" in (:codes)");
        deleted.with("codes", SqlListParameterValue.strings(codes));
        deleted.execute();
      }
      childs.forEach(child -> {
        SqlCommand command = template.createSqlCommand(codes.contains(child.getUid())
            ? "update \"access_oauth_client_allowed_login_redirect\" set \"verify\" = :verify, \"medal\" = :medal, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version"
            : "insert into \"access_oauth_client_allowed_login_redirect\" ( \"uid\", \"client\", \"url\", \"version\") values ( :uid, :client, :url, :version)");
        command.with(UID, SqlParameterValue.of(child.getUid()));
        command.with(CLIENT, SqlParameterValue.of(entity.getUid()));
        command.with(URL, SqlParameterValue.of(child.getUrl()));
        command.with(VERSION, child.getVersion().map(SqlParameterValue::of)
            .orElseGet(SqlParameterValue::ofNullInteger));
        int val = command.execute();
        if (val != 1) {
          throw new OptimistLockException("No child processed");
        } else {
          response.add(child.withNextVersion());
        }
      });
      // TODO: need to save througn entity AllowedLoginRedirects => AllowedLoginRedirectsVO::from[
      // result ] ;
      return entity;
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param childs
   * @return
   */
  private OauthClient saveAllowedLogoutRedirects(OauthClient entity,
      List<AllowedLogoutRedirects> childs) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<Object[]> sq = template.createSqlQuery(
          "select \"uid\", \"client\" from \"access_oauth_client_allowed_logout_redirect\" where (\"client\" = :client)"
              + " or ( \"uid\" in (:tomap) and \"client\" != :client )");
      sq.with(CLIENT, SqlParameterValue.of(entity.getUid()));
      sq.with(TOMAP, SqlListParameterValue
          .strings(childs.stream().map(AllowedLogoutRedirects::getUid).toList()));
      List<Object[]> list =
          sq.query(row -> Optional.of(new Object[] {row.getString(1), row.getString(2)})).all();
      if (list.stream().anyMatch(arr -> !entity.getUid().equals(arr[1]))) {
        throw new IllegalArgumentException("Try to move from another parent");
      }
      List<AllowedLogoutRedirects> response = new ArrayList<>();
      List<String> codes = new ArrayList<>(list.stream().map(arr -> (String) arr[0]).toList());
      childs.forEach(child -> codes.remove(child.getUid()));
      if (!codes.isEmpty()) {
        SqlCommand deleted = template.createSqlCommand(
            "delete from\"access_oauth_client_allowed_logout_redirect\" where \"uid\" in (:codes)");
        deleted.with("codes", SqlListParameterValue.strings(codes));
        deleted.execute();
      }
      childs.forEach(child -> {
        SqlCommand command = template.createSqlCommand(codes.contains(child.getUid())
            ? "update \"access_oauth_client_allowed_logout_redirect\" set \"verify\" = :verify, \"medal\" = :medal, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version"
            : "insert into \"access_oauth_client_allowed_logout_redirect\" ( \"uid\", \"client\", \"url\", \"version\") values ( :uid, :client, :url, :version)");
        command.with(UID, SqlParameterValue.of(child.getUid()));
        command.with(CLIENT, SqlParameterValue.of(entity.getUid()));
        command.with(URL, SqlParameterValue.of(child.getUrl()));
        command.with(VERSION, child.getVersion().map(SqlParameterValue::of)
            .orElseGet(SqlParameterValue::ofNullInteger));
        int val = command.execute();
        if (val != 1) {
          throw new OptimistLockException("No child processed");
        } else {
          response.add(child.withNextVersion());
        }
      });
      // TODO: need to save througn entity AllowedLogoutRedirects => AllowedLogoutRedirectsVO::from[
      // result ] ;
      return entity;
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sq
   * @param offset
   * @param cursor
   * @param order
   * @return
   */
  private PartialWhere[] tryToOrderByCodeAsc(SqlSchematicQuery<OauthClient> sq,
      PartialWhere[] offset, OauthClientCursor cursor, OauthClientOrder order) {
    if (order == OauthClientOrder.CODE_ASC) {
      sq.addOrderAsc("code");
      Optional<String> sinceCode = cursor.getSinceCode();
      if (sinceCode.isPresent()) {
        String sinceCodeValue = sinceCode.get();
        offset[0] = PartialWhere.or(offset[0], PartialWhere.and(offset[1],
            PartialWhere.where("code", SqlOperator.GT, SqlParameterValue.of(sinceCodeValue))));
        offset[1] = PartialWhere.and(offset[1],
            PartialWhere.where("code", SqlOperator.EQ, SqlParameterValue.of(sinceCodeValue)));
      }
    }
    return offset;
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sq
   * @param offset
   * @param cursor
   * @param order
   * @return
   */
  private PartialWhere[] tryToOrderByCodeDesc(SqlSchematicQuery<OauthClient> sq,
      PartialWhere[] offset, OauthClientCursor cursor, OauthClientOrder order) {
    if (order == OauthClientOrder.CODE_DESC) {
      sq.addOrderDesc("code");
      Optional<String> sinceCode = cursor.getSinceCode();
      if (sinceCode.isPresent()) {
        String sinceCodeValue = sinceCode.get();
        offset[0] = PartialWhere.or(offset[0], PartialWhere.and(offset[1],
            PartialWhere.where("code", SqlOperator.GT, SqlParameterValue.of(sinceCodeValue))));
        offset[1] = PartialWhere.and(offset[1],
            PartialWhere.where("code", SqlOperator.EQ, SqlParameterValue.of(sinceCodeValue)));
      }
    }
    return offset;
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param exists
   * @param entity
   * @param template
   * @return
   */
  private OauthClient verified(boolean exists, OauthClient entity, SqlTemplate template) {
    if (exists) {
      return entity;
    } else {
      template.createSqlCommand("delete from \"access_oauth_client\" where \"uid\" = :uid")
          .with("uid", SqlParameterValue.of(entity.getUid())).execute();
      throw new NotFoundException("");
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sq
   */
  private void withChilds(AbstractSqlParametrized<?> sq) {
    sq.child(100, "allowedLoginRedirects",
        "select \"client\",\"uid\",\"url\",\"version\" from \"access_oauth_client_allowed_login_redirect\"  where \"client\" in (:client)",
        "uid", "client", row -> {
          try {
            return Optional.of(AllowedLoginRedirects.builder().uid(row.getString(UID))
                .url(row.getString(URL)).version(row.getInt(VERSION)).build());
          } catch (ConstraintException ce) {
            log.error("Unable to map allowedLoginRedirects child data for {}", row.getString(2),
                ce);
            return Optional.empty();
          }
        });
    sq.child(100, "allowedLogoutRedirects",
        "select \"client\",\"uid\",\"url\",\"version\" from \"access_oauth_client_allowed_logout_redirect\"  where \"client\" in (:client)",
        "uid", "client", row -> {
          try {
            return Optional.of(AllowedLogoutRedirects.builder().uid(row.getString(UID))
                .url(row.getString(URL)).version(row.getInt(VERSION)).build());
          } catch (ConstraintException ce) {
            log.error("Unable to map allowedLogoutRedirects child data for {}", row.getString(2),
                ce);
            return Optional.empty();
          }
        });
    sq.child(100, "allowedAudiences",
        "select \"client\",\"uid\",\"audience\",\"version\" from \"access_oauth_client_allowed_audience\"  where \"client\" in (:client)",
        "uid", "client", row -> {
          try {
            return Optional.of(AllowedAudiences.builder().uid(row.getString(UID))
                .audience(row.getString(AUDIENCE)).version(row.getInt(VERSION)).build());
          } catch (ConstraintException ce) {
            log.error("Unable to map allowedAudiences child data for {}", row.getString(2), ce);
            return Optional.empty();
          }
        });
  }
}
