package net.civeira.phylax.features.access.scopeassignation.infrastructure.repository;

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
import net.civeira.phylax.features.access.scopeassignation.ScopeAssignation;
import net.civeira.phylax.features.access.scopeassignation.ScopeAssignationRef;
import net.civeira.phylax.features.access.scopeassignation.query.ScopeAssignationCursor;
import net.civeira.phylax.features.access.scopeassignation.query.ScopeAssignationFilter;
import net.civeira.phylax.features.access.scopeassignation.valueobject.SecurityDomainVO;
import net.civeira.phylax.features.access.scopeassignation.valueobject.SecurityScopeVO;
import net.civeira.phylax.features.access.scopeassignation.valueobject.UidVO;
import net.civeira.phylax.features.access.scopeassignation.valueobject.VersionVO;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ScopeAssignationRepository {

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SCOPE_ASSIGNATION_SECURITY_DOMAIN =
      "scope_assignation.security_domain";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SCOPE_ASSIGNATION_SECURITY_SCOPE = "scope_assignation.security_scope";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SCOPE_ASSIGNATION_SNAKE = "scope_assignation";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SCOPE_ASSIGNATION_UID = "scope_assignation.uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SCOPE_ASSIGNATION_VERSION = "scope_assignation.version";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECURITY_DOMAIN = "securityDomain";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECURITY_DOMAIN_SNAKE = "security_domain";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECURITY_SCOPE = "securityScope";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECURITY_SCOPE_SNAKE = "security_scope";

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
  public long count(ScopeAssignationFilter filter) {
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
  public ScopeAssignation create(ScopeAssignation entity) {
    return runCreate(entity, null);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  public ScopeAssignation create(ScopeAssignation entity, Predicate<ScopeAssignation> verifier) {
    return runCreate(entity, verifier);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   */
  public void delete(ScopeAssignation entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq =
          template.createSqlCommand("delete from \"scope_assignation\" where \"uid\" = :uid");
      sq.with(UID, SqlParameterValue.of(entity.getUidValue()));
      int num = sq.execute();
      if (0 == num) {
        throw new IllegalArgumentException("No delete from");
      }
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public ScopeAssignation enrich(ScopeAssignationRef reference) {
    return reference instanceof ScopeAssignation scopeAssignation ? scopeAssignation
        : retrieve(reference.getUidValue(), Optional.empty())
            .orElseThrow(() -> new NotFoundException(
                "Trying to enrich inexistent ScopeAssignation: " + reference.getUidValue()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public ScopeAssignation enrichForUpdate(ScopeAssignationRef reference) {
    return reference instanceof ScopeAssignation scopeAssignation ? scopeAssignation
        : retrieveForUpdate(reference.getUidValue(), Optional.empty())
            .orElseThrow(() -> new NotFoundException(
                "Trying to enrich inexistent ScopeAssignation: " + reference.getUidValue()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean exists(String uid, Optional<ScopeAssignationFilter> filter) {
    return retrieve(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean existsForUpdate(String uid, Optional<ScopeAssignationFilter> filter) {
    return retrieveForUpdate(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<ScopeAssignation> find(ScopeAssignationFilter filter) {
    ScopeAssignationCursor cursor = ScopeAssignationCursor.builder().build();
    return new ScopeAssignationSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<ScopeAssignation> find(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<ScopeAssignation> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<ScopeAssignation> findForUpdate(ScopeAssignationFilter filter) {
    ScopeAssignationCursor cursor = ScopeAssignationCursor.builder().build();
    return new ScopeAssignationSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<ScopeAssignation> findForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<ScopeAssignation> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<ScopeAssignation> list(ScopeAssignationFilter filter) {
    ScopeAssignationCursor cursor = ScopeAssignationCursor.builder().build();
    return new ScopeAssignationSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<ScopeAssignation> list(final String sql,
      final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, false);
  }

  /**
   * sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<ScopeAssignation> list(final String sql, final Map<String, SqlParameterValue> params,
      final int limit) {
    return list(sql, params, Integer.valueOf(limit), false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<ScopeAssignation> listForUpdate(ScopeAssignationFilter filter) {
    ScopeAssignationCursor cursor = ScopeAssignationCursor.builder().build();
    return new ScopeAssignationSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<ScopeAssignation> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, true);
  }

  /**
   * sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<ScopeAssignation> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params, final int limit) {
    return list(sql, params, Integer.valueOf(limit), true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<ScopeAssignation> retrieve(String uid, Optional<ScopeAssignationFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      ScopeAssignationFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> ScopeAssignationFilter.builder().uid(uid).build());
      SqlSchematicQuery<ScopeAssignation> sq = filteredQuery(template, readyFilter);
      return sq.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<ScopeAssignation> retrieveForUpdate(String uid,
      Optional<ScopeAssignationFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      ScopeAssignationFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> ScopeAssignationFilter.builder().uid(uid).build());
      SqlSchematicQuery<ScopeAssignation> sq = filteredQuery(template, readyFilter);
      return sq.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<ScopeAssignation> slide(ScopeAssignationFilter filter,
      ScopeAssignationCursor cursor) {
    return new ScopeAssignationSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<ScopeAssignation> slideForUpdate(ScopeAssignationFilter filter,
      ScopeAssignationCursor cursor) {
    return new ScopeAssignationSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @return
   */
  public ScopeAssignation update(ScopeAssignation entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "update \"scope_assignation\" set  \"security_domain\" = :securityDomain, \"security_scope\" = :securityScope, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version");
      sq.with(UID, SqlParameterValue.of(entity.getUid().getValue()));
      sq.with(SECURITY_DOMAIN,
          SqlParameterValue.of(entity.getSecurityDomain().getReferenceValue()));
      sq.with(SECURITY_SCOPE, SqlParameterValue.of(entity.getSecurityScope().getReferenceValue()));
      sq.with(VERSION, entity.getVersion().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      int num = sq.execute();
      if (0 == num) {
        throw new OptimistLockException("No delete from");
      }
      return entity.nextVersion();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @return
   */
  private SqlConverter<ScopeAssignation> converter() {
    return row -> {
      try {
        return Optional.of(ScopeAssignation.builder().uid(UidVO.from(row.getString(UID)))
            .securityDomain(SecurityDomainVO.fromReference(row.getString(SECURITY_DOMAIN_SNAKE)))
            .securityScope(SecurityScopeVO.fromReference(row.getString(SECURITY_SCOPE_SNAKE)))
            .version(VersionVO.from(row.getInt(VERSION))).build());
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
  private <T> SqlSchematicQuery<T> filteredQuery(SqlTemplate template,
      ScopeAssignationFilter filter) {
    SqlSchematicQuery<T> sq = template.createSqlSchematicQuery(SCOPE_ASSIGNATION_SNAKE);
    sq.selectFields(SCOPE_ASSIGNATION_UID, SCOPE_ASSIGNATION_SECURITY_DOMAIN,
        SCOPE_ASSIGNATION_SECURITY_SCOPE, SCOPE_ASSIGNATION_VERSION);
    filter.getUid().ifPresent(uid -> sq.where(UID, SqlOperator.EQ, SqlParameterValue.of(uid)));
    if (!filter.getUids().isEmpty()) {
      sq.where(UID, SqlOperator.IN, SqlListParameterValue.strings(filter.getUids()));
    }
    filter.getSearch().ifPresent(
        search -> sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%")));
    filter.getSecurityDomain().ifPresent(securityDomain -> sq.where(SECURITY_DOMAIN_SNAKE,
        SqlOperator.EQ, SqlParameterValue.of(securityDomain.getUidValue())));
    if (!filter.getSecurityDomains().isEmpty()) {
      sq.where(SECURITY_DOMAIN_SNAKE, SqlOperator.IN,
          SqlListParameterValue.strings(filter.getSecurityDomains()));
    }
    filter.getSecurityScope().ifPresent(securityScope -> sq.where(SECURITY_SCOPE_SNAKE,
        SqlOperator.EQ, SqlParameterValue.of(securityScope.getUidValue())));
    if (!filter.getSecurityScopes().isEmpty()) {
      sq.where(SECURITY_SCOPE_SNAKE, SqlOperator.IN,
          SqlListParameterValue.strings(filter.getSecurityScopes()));
    }
    return sq;
  }

  /**
   * sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @param forUpdate
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  private List<ScopeAssignation> list(final String sql, final Map<String, SqlParameterValue> params,
      final Integer limit, final boolean forUpdate) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<ScopeAssignation> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      if (forUpdate) {
        createSqlQuery = createSqlQuery.forUpdate();
      }
      SqlResult<ScopeAssignation> query = createSqlQuery.query(converter());
      return null == limit ? query.all() : query.limit(limit);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  private ScopeAssignation runCreate(ScopeAssignation entity,
      Predicate<ScopeAssignation> verifier) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "insert into \"scope_assignation\" ( \"uid\", \"security_domain\", \"security_scope\", \"version\") values ( :uid, :securityDomain, :securityScope, :version)");
      sq.with(UID, SqlParameterValue.of(entity.getUid().getValue()));
      sq.with(SECURITY_DOMAIN,
          SqlParameterValue.of(entity.getSecurityDomain().getReferenceValue()));
      sq.with(SECURITY_SCOPE, SqlParameterValue.of(entity.getSecurityScope().getReferenceValue()));
      sq.with(VERSION, entity.getVersion().getValue().map(SqlParameterValue::of)
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
  private Iterator<ScopeAssignation> runList(ScopeAssignationFilter filter,
      ScopeAssignationCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<ScopeAssignation> sq = filteredQuery(template, filter);
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
  private Iterator<ScopeAssignation> runListForUpdate(ScopeAssignationFilter filter,
      ScopeAssignationCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<ScopeAssignation> sq = filteredQuery(template, filter);
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
  private ScopeAssignation verified(boolean exists, ScopeAssignation entity, SqlTemplate template) {
    if (exists) {
      return entity;
    } else {
      template.createSqlCommand("delete from \"scope_assignation\" where \"uid\" = :uid")
          .with("uid", SqlParameterValue.of(entity.getUidValue())).execute();
      throw new NotFoundException("");
    }
  }
}
