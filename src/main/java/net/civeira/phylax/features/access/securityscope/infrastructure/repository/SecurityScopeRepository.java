package net.civeira.phylax.features.access.securityscope.infrastructure.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.sql.DataSource;

import io.opentelemetry.api.trace.Tracer;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.infrastructure.sql.OptimistLockException;
import net.civeira.phylax.common.infrastructure.sql.SqlCommand;
import net.civeira.phylax.common.infrastructure.sql.SqlConverter;
import net.civeira.phylax.common.infrastructure.sql.SqlListParameterValue;
import net.civeira.phylax.common.infrastructure.sql.SqlOperator;
import net.civeira.phylax.common.infrastructure.sql.SqlParameterValue;
import net.civeira.phylax.common.infrastructure.sql.SqlQuery;
import net.civeira.phylax.common.infrastructure.sql.SqlResult;
import net.civeira.phylax.common.infrastructure.sql.SqlSchematicQuery;
import net.civeira.phylax.common.infrastructure.sql.SqlTemplate;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScope;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScopeKindOptions;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScopeRef;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScopeVisibilityOptions;
import net.civeira.phylax.features.access.securityscope.domain.gateway.SecurityScopeCursor;
import net.civeira.phylax.features.access.securityscope.domain.gateway.SecurityScopeFilter;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.KindVO;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.RelyingPartyVO;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.ResourceVO;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.ScopeVO;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.TrustedClientVO;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.VersionVO;
import net.civeira.phylax.features.access.securityscope.domain.valueobject.VisibilityVO;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class SecurityScopeRepository {

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_ENABLED = "access_security_scope.enabled";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_KIND = "access_security_scope.kind";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_RELYING_PARTY =
      "access_security_scope.relying_party";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_RESOURCE = "access_security_scope.resource";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_SCOPE = "access_security_scope.scope";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_SNAKE = "access_security_scope";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_TRUSTED_CLIENT =
      "access_security_scope.trusted_client";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_UID = "access_security_scope.uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_VERSION = "access_security_scope.version";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_SCOPE_VISIBILITY = "access_security_scope.visibility";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ENABLED = "enabled";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String KIND = "kind";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String RELYING_PARTY = "relyingParty";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String RELYING_PARTY_SNAKE = "relying_party";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String RESOURCE = "resource";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SCOPE = "scope";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TRUSTED_CLIENT = "trustedClient";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TRUSTED_CLIENT_SNAKE = "trusted_client";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String UID = "uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String VERSION = "version";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String VISIBILITY = "visibility";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private final DataSource datasource;

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private final Tracer tracer;

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public long count(SecurityScopeFilter filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<Long> sq = filteredQuery(template, filter);
      sq.select("count(uid) as uid");
      return sq.query(row -> Optional.of(row.getLong(1))).one().orElse(0l);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @return
   */
  public SecurityScope create(SecurityScope entity) {
    return runCreate(entity, null);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  public SecurityScope create(SecurityScope entity, Predicate<SecurityScope> verifier) {
    return runCreate(entity, verifier);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   */
  public void delete(SecurityScope entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq =
          template.createSqlCommand("delete from \"access_security_scope\" where \"uid\" = :uid");
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
  public boolean exists(String uid, Optional<SecurityScopeFilter> filter) {
    return retrieve(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean existsForUpdate(String uid, Optional<SecurityScopeFilter> filter) {
    return retrieveForUpdate(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<SecurityScope> find(SecurityScopeFilter filter) {
    SecurityScopeCursor cursor = SecurityScopeCursor.builder().build();
    return new SecurityScopeSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<SecurityScope> find(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<SecurityScope> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<SecurityScope> findForUpdate(SecurityScopeFilter filter) {
    SecurityScopeCursor cursor = SecurityScopeCursor.builder().build();
    return new SecurityScopeSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<SecurityScope> findForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<SecurityScope> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<SecurityScope> list(SecurityScopeFilter filter) {
    SecurityScopeCursor cursor = SecurityScopeCursor.builder().build();
    return new SecurityScopeSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<SecurityScope> list(final String sql, final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<SecurityScope> list(final String sql, final Map<String, SqlParameterValue> params,
      final int limit) {
    return list(sql, params, Integer.valueOf(limit), false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<SecurityScope> listForUpdate(SecurityScopeFilter filter) {
    SecurityScopeCursor cursor = SecurityScopeCursor.builder().build();
    return new SecurityScopeSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<SecurityScope> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<SecurityScope> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params, final int limit) {
    return list(sql, params, Integer.valueOf(limit), true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public SecurityScope resolve(SecurityScopeRef reference) {
    return reference instanceof SecurityScope securityScope ? securityScope
        : retrieve(reference.getUid(), Optional.empty()).orElseThrow(() -> new NotFoundException(
            "Trying to resolve inexistent SecurityScope: " + reference.getUid()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public SecurityScope resolveForUpdate(SecurityScopeRef reference) {
    return reference instanceof SecurityScope securityScope ? securityScope
        : retrieveForUpdate(reference.getUid(), Optional.empty())
            .orElseThrow(() -> new NotFoundException(
                "Trying to resolve inexistent SecurityScope: " + reference.getUid()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<SecurityScope> retrieve(String uid, Optional<SecurityScopeFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SecurityScopeFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> SecurityScopeFilter.builder().uid(uid).build());
      SqlSchematicQuery<SecurityScope> sq = filteredQuery(template, readyFilter);
      return sq.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<SecurityScope> retrieveForUpdate(String uid,
      Optional<SecurityScopeFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SecurityScopeFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> SecurityScopeFilter.builder().uid(uid).build());
      SqlSchematicQuery<SecurityScope> sq = filteredQuery(template, readyFilter);
      return sq.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<SecurityScope> slide(SecurityScopeFilter filter, SecurityScopeCursor cursor) {
    return new SecurityScopeSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<SecurityScope> slideForUpdate(SecurityScopeFilter filter,
      SecurityScopeCursor cursor) {
    return new SecurityScopeSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @return
   */
  public SecurityScope update(SecurityScope entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "update \"access_security_scope\" set  \"trusted_client\" = :trustedClient, \"relying_party\" = :relyingParty, \"resource\" = :resource, \"scope\" = :scope, \"enabled\" = :enabled, \"kind\" = :kind, \"visibility\" = :visibility, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      sq.with(TRUSTED_CLIENT, entity.getTrustedClientUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(RELYING_PARTY, entity.getRelyingPartyUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(RESOURCE, SqlParameterValue.of(entity.getResource()));
      sq.with(SCOPE, SqlParameterValue.of(entity.getScope()));
      sq.with(ENABLED, SqlParameterValue.of(entity.isEnabled()));
      sq.with(KIND, entity.getKind().map(SecurityScopeKindOptions::toString)
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(VISIBILITY, entity.getVisibility().map(SecurityScopeVisibilityOptions::toString)
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(VERSION, entity.getVersion().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      int num = sq.execute();
      if (0 == num) {
        throw new OptimistLockException("No delete from");
      }
      return entity.withNextVersion();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @return
   */
  private SqlConverter<SecurityScope> converter() {
    return row -> {
      try {
        return Optional.of(SecurityScope.builder().uidValue(UidVO.from(row.getString(UID)))
            .trustedClientValue(TrustedClientVO.fromReference(row.getString(TRUSTED_CLIENT_SNAKE)))
            .relyingPartyValue(RelyingPartyVO.fromReference(row.getString(RELYING_PARTY_SNAKE)))
            .resourceValue(ResourceVO.from(row.getString(RESOURCE)))
            .scopeValue(ScopeVO.from(row.getString(SCOPE)))
            .enabledValue(EnabledVO.from(row.getBoolean(ENABLED)))
            .kindValue(KindVO.tryFrom(row.getString(KIND)))
            .visibilityValue(VisibilityVO.tryFrom(row.getString(VISIBILITY)))
            .versionValue(VersionVO.from(row.getInt(VERSION))).build());
      } catch (ConstraintException ce) {
        log.error("Unable to map data for {}", row.getString(1), ce);
        return Optional.empty();
      }
    };
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param template
   * @param filter
   * @return
   */
  private <T> SqlSchematicQuery<T> filteredQuery(SqlTemplate template, SecurityScopeFilter filter) {
    SqlSchematicQuery<T> sq = template.createSqlSchematicQuery(ACCESS_SECURITY_SCOPE_SNAKE);
    sq.selectFields(ACCESS_SECURITY_SCOPE_UID, ACCESS_SECURITY_SCOPE_TRUSTED_CLIENT,
        ACCESS_SECURITY_SCOPE_RELYING_PARTY, ACCESS_SECURITY_SCOPE_RESOURCE,
        ACCESS_SECURITY_SCOPE_SCOPE, ACCESS_SECURITY_SCOPE_ENABLED, ACCESS_SECURITY_SCOPE_KIND,
        ACCESS_SECURITY_SCOPE_VISIBILITY, ACCESS_SECURITY_SCOPE_VERSION);
    filter.getUid().ifPresent(uid -> sq.where(UID, SqlOperator.EQ, SqlParameterValue.of(uid)));
    if (!filter.getUids().isEmpty()) {
      sq.where(UID, SqlOperator.IN, SqlListParameterValue.strings(filter.getUids()));
    }
    filter.getSearch().ifPresent(
        search -> sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%")));
    filter.getResource()
        .ifPresent(resource -> sq.where(RESOURCE, SqlOperator.EQ, SqlParameterValue.of(resource)));
    filter.getTrustedClient().ifPresent(trustedClient -> sq.where(TRUSTED_CLIENT_SNAKE,
        SqlOperator.EQ, SqlParameterValue.of(trustedClient.getUid())));
    if (!filter.getTrustedClients().isEmpty()) {
      sq.where(TRUSTED_CLIENT_SNAKE, SqlOperator.IN,
          SqlListParameterValue.strings(filter.getTrustedClients()));
    }
    filter.getRelyingParty().ifPresent(relyingParty -> sq.where(RELYING_PARTY_SNAKE, SqlOperator.EQ,
        SqlParameterValue.of(relyingParty.getUid())));
    if (!filter.getRelyingPartys().isEmpty()) {
      sq.where(RELYING_PARTY_SNAKE, SqlOperator.IN,
          SqlListParameterValue.strings(filter.getRelyingPartys()));
    }
    return sq;
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @param forUpdate
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  private List<SecurityScope> list(final String sql, final Map<String, SqlParameterValue> params,
      final Integer limit, final boolean forUpdate) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<SecurityScope> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      if (forUpdate) {
        createSqlQuery = createSqlQuery.forUpdate();
      }
      SqlResult<SecurityScope> query = createSqlQuery.query(converter());
      return null == limit ? query.all() : query.limit(limit);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  private SecurityScope runCreate(SecurityScope entity, Predicate<SecurityScope> verifier) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "insert into \"access_security_scope\" ( \"uid\", \"trusted_client\", \"relying_party\", \"resource\", \"scope\", \"enabled\", \"kind\", \"visibility\", \"version\") values ( :uid, :trustedClient, :relyingParty, :resource, :scope, :enabled, :kind, :visibility, :version)");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      sq.with(TRUSTED_CLIENT, entity.getTrustedClientUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(RELYING_PARTY, entity.getRelyingPartyUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(RESOURCE, SqlParameterValue.of(entity.getResource()));
      sq.with(SCOPE, SqlParameterValue.of(entity.getScope()));
      sq.with(ENABLED, SqlParameterValue.of(entity.isEnabled()));
      sq.with(KIND, entity.getKind().map(SecurityScopeKindOptions::toString)
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(VISIBILITY, entity.getVisibility().map(SecurityScopeVisibilityOptions::toString)
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(VERSION, entity.getVersion().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      int num = sq.execute();
      if (0 == num) {
        throw new IllegalArgumentException("No insert into");
      }
      return verifier == null ? entity : verified(verifier.test(entity), entity, template);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  private Iterator<SecurityScope> runList(SecurityScopeFilter filter, SecurityScopeCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<SecurityScope> sq = filteredQuery(template, filter);
      cursor.getSinceUid()
          .ifPresent(since -> sq.where(UID, SqlOperator.GT, SqlParameterValue.of(since)));
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
  private Iterator<SecurityScope> runListForUpdate(SecurityScopeFilter filter,
      SecurityScopeCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<SecurityScope> sq = filteredQuery(template, filter);
      cursor.getSinceUid()
          .ifPresent(since -> sq.where(UID, SqlOperator.GT, SqlParameterValue.of(since)));
      sq.orderAsc("uid");
      return sq.forUpdate().query(converter()).limit(cursor.getLimit()).iterator();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param exists
   * @param entity
   * @param template
   * @return
   */
  private SecurityScope verified(boolean exists, SecurityScope entity, SqlTemplate template) {
    if (exists) {
      return entity;
    } else {
      template.createSqlCommand("delete from \"access_security_scope\" where \"uid\" = :uid")
          .with("uid", SqlParameterValue.of(entity.getUid())).execute();
      throw new NotFoundException("");
    }
  }
}
