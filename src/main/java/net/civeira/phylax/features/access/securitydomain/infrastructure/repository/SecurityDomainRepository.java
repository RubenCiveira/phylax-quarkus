package net.civeira.phylax.features.access.securitydomain.infrastructure.repository;

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
import net.civeira.phylax.features.access.securitydomain.domain.SecurityDomain;
import net.civeira.phylax.features.access.securitydomain.domain.SecurityDomainRef;
import net.civeira.phylax.features.access.securitydomain.domain.gateway.SecurityDomainCursor;
import net.civeira.phylax.features.access.securitydomain.domain.gateway.SecurityDomainFilter;
import net.civeira.phylax.features.access.securitydomain.domain.gateway.SecurityDomainOrder;
import net.civeira.phylax.features.access.securitydomain.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.securitydomain.domain.valueobject.LevelVO;
import net.civeira.phylax.features.access.securitydomain.domain.valueobject.ManageAllVO;
import net.civeira.phylax.features.access.securitydomain.domain.valueobject.NameVO;
import net.civeira.phylax.features.access.securitydomain.domain.valueobject.ReadAllVO;
import net.civeira.phylax.features.access.securitydomain.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.securitydomain.domain.valueobject.VersionVO;
import net.civeira.phylax.features.access.securitydomain.domain.valueobject.WriteAllVO;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class SecurityDomainRepository {

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_ENABLED = "access_security_domain.enabled";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_LEVEL = "access_security_domain.level";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_MANAGE_ALL =
      "access_security_domain.manage_all";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_NAME = "access_security_domain.name";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_READ_ALL = "access_security_domain.read_all";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_SNAKE = "access_security_domain";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_UID = "access_security_domain.uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_VERSION = "access_security_domain.version";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_SECURITY_DOMAIN_WRITE_ALL = "access_security_domain.write_all";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ENABLED = "enabled";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String LEVEL = "level";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String MANAGE_ALL = "manageAll";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String MANAGE_ALL_SNAKE = "manage_all";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String NAME = "name";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String READ_ALL = "readAll";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String READ_ALL_SNAKE = "read_all";

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
  private static final String WRITE_ALL = "writeAll";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String WRITE_ALL_SNAKE = "write_all";

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
  public long count(SecurityDomainFilter filter) {
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
  public SecurityDomain create(SecurityDomain entity) {
    return runCreate(entity, null);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  public SecurityDomain create(SecurityDomain entity, Predicate<SecurityDomain> verifier) {
    return runCreate(entity, verifier);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   */
  public void delete(SecurityDomain entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq =
          template.createSqlCommand("delete from \"access_security_domain\" where \"uid\" = :uid");
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
  public boolean exists(String uid, Optional<SecurityDomainFilter> filter) {
    return retrieve(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean existsForUpdate(String uid, Optional<SecurityDomainFilter> filter) {
    return retrieveForUpdate(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<SecurityDomain> find(SecurityDomainFilter filter) {
    SecurityDomainCursor cursor = SecurityDomainCursor.builder().build();
    return new SecurityDomainSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<SecurityDomain> find(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<SecurityDomain> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<SecurityDomain> findForUpdate(SecurityDomainFilter filter) {
    SecurityDomainCursor cursor = SecurityDomainCursor.builder().build();
    return new SecurityDomainSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<SecurityDomain> findForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<SecurityDomain> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<SecurityDomain> list(SecurityDomainFilter filter) {
    SecurityDomainCursor cursor = SecurityDomainCursor.builder().build();
    return new SecurityDomainSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<SecurityDomain> list(final String sql, final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<SecurityDomain> list(final String sql, final Map<String, SqlParameterValue> params,
      final int limit) {
    return list(sql, params, Integer.valueOf(limit), false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<SecurityDomain> listForUpdate(SecurityDomainFilter filter) {
    SecurityDomainCursor cursor = SecurityDomainCursor.builder().build();
    return new SecurityDomainSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<SecurityDomain> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<SecurityDomain> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params, final int limit) {
    return list(sql, params, Integer.valueOf(limit), true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public SecurityDomain resolve(SecurityDomainRef reference) {
    return reference instanceof SecurityDomain securityDomain ? securityDomain
        : retrieve(reference.getUid(), Optional.empty()).orElseThrow(() -> new NotFoundException(
            "Trying to resolve inexistent SecurityDomain: " + reference.getUid()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public SecurityDomain resolveForUpdate(SecurityDomainRef reference) {
    return reference instanceof SecurityDomain securityDomain ? securityDomain
        : retrieveForUpdate(reference.getUid(), Optional.empty())
            .orElseThrow(() -> new NotFoundException(
                "Trying to resolve inexistent SecurityDomain: " + reference.getUid()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<SecurityDomain> retrieve(String uid, Optional<SecurityDomainFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SecurityDomainFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> SecurityDomainFilter.builder().uid(uid).build());
      SqlSchematicQuery<SecurityDomain> sq = filteredQuery(template, readyFilter);
      return sq.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<SecurityDomain> retrieveForUpdate(String uid,
      Optional<SecurityDomainFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SecurityDomainFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> SecurityDomainFilter.builder().uid(uid).build());
      SqlSchematicQuery<SecurityDomain> sq = filteredQuery(template, readyFilter);
      return sq.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<SecurityDomain> slide(SecurityDomainFilter filter, SecurityDomainCursor cursor) {
    return new SecurityDomainSlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<SecurityDomain> slideForUpdate(SecurityDomainFilter filter,
      SecurityDomainCursor cursor) {
    return new SecurityDomainSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @return
   */
  public SecurityDomain update(SecurityDomain entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "update \"access_security_domain\" set  \"name\" = :name, \"level\" = :level, \"read_all\" = :readAll, \"write_all\" = :writeAll, \"manage_all\" = :manageAll, \"enabled\" = :enabled, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      sq.with(NAME, SqlParameterValue.of(entity.getName()));
      sq.with(LEVEL, SqlParameterValue.of(entity.getLevel()));
      sq.with(READ_ALL, SqlParameterValue.of(entity.isReadAll()));
      sq.with(WRITE_ALL, SqlParameterValue.of(entity.isWriteAll()));
      sq.with(MANAGE_ALL, SqlParameterValue.of(entity.isManageAll()));
      sq.with(ENABLED, SqlParameterValue.of(entity.isEnabled()));
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
   * @param sq
   * @param cursor
   */
  private void attachWithOrder(SqlSchematicQuery<SecurityDomain> sq, SecurityDomainCursor cursor) {
    PartialWhere[] offset = new PartialWhere[] {PartialWhere.empty(), PartialWhere.empty()};
    for (SecurityDomainOrder order : cursor.getOrder()) {
      tryToOrderByNameAsc(sq, offset, cursor, order);
      tryToOrderByNameDesc(sq, offset, cursor, order);
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
  private SqlConverter<SecurityDomain> converter() {
    return row -> {
      try {
        return Optional.of(SecurityDomain.builder().uidValue(UidVO.from(row.getString(UID)))
            .nameValue(NameVO.from(row.getString(NAME))).levelValue(LevelVO.from(row.getInt(LEVEL)))
            .readAllValue(ReadAllVO.from(row.getBoolean(READ_ALL_SNAKE)))
            .writeAllValue(WriteAllVO.from(row.getBoolean(WRITE_ALL_SNAKE)))
            .manageAllValue(ManageAllVO.from(row.getBoolean(MANAGE_ALL_SNAKE)))
            .enabledValue(EnabledVO.from(row.getBoolean(ENABLED)))
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
  private <T> SqlSchematicQuery<T> filteredQuery(SqlTemplate template,
      SecurityDomainFilter filter) {
    SqlSchematicQuery<T> sq = template.createSqlSchematicQuery(ACCESS_SECURITY_DOMAIN_SNAKE);
    sq.selectFields(ACCESS_SECURITY_DOMAIN_UID, ACCESS_SECURITY_DOMAIN_NAME,
        ACCESS_SECURITY_DOMAIN_LEVEL, ACCESS_SECURITY_DOMAIN_READ_ALL,
        ACCESS_SECURITY_DOMAIN_WRITE_ALL, ACCESS_SECURITY_DOMAIN_MANAGE_ALL,
        ACCESS_SECURITY_DOMAIN_ENABLED, ACCESS_SECURITY_DOMAIN_VERSION);
    filter.getUid().ifPresent(uid -> sq.where(UID, SqlOperator.EQ, SqlParameterValue.of(uid)));
    if (!filter.getUids().isEmpty()) {
      sq.where(UID, SqlOperator.IN, SqlListParameterValue.strings(filter.getUids()));
    }
    filter.getSearch().ifPresent(
        search -> sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%")));
    filter.getEnabled()
        .ifPresent(enabled -> sq.where(ENABLED, SqlOperator.EQ, SqlParameterValue.of(enabled)));
    filter.getName().ifPresent(name -> sq.where(NAME, SqlOperator.EQ, SqlParameterValue.of(name)));
    return sq;
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @param forUpdate
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  private List<SecurityDomain> list(final String sql, final Map<String, SqlParameterValue> params,
      final Integer limit, final boolean forUpdate) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<SecurityDomain> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      if (forUpdate) {
        createSqlQuery = createSqlQuery.forUpdate();
      }
      SqlResult<SecurityDomain> query = createSqlQuery.query(converter());
      return null == limit ? query.all() : query.limit(limit);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  private SecurityDomain runCreate(SecurityDomain entity, Predicate<SecurityDomain> verifier) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "insert into \"access_security_domain\" ( \"uid\", \"name\", \"level\", \"read_all\", \"write_all\", \"manage_all\", \"enabled\", \"version\") values ( :uid, :name, :level, :readAll, :writeAll, :manageAll, :enabled, :version)");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      sq.with(NAME, SqlParameterValue.of(entity.getName()));
      sq.with(LEVEL, SqlParameterValue.of(entity.getLevel()));
      sq.with(READ_ALL, SqlParameterValue.of(entity.isReadAll()));
      sq.with(WRITE_ALL, SqlParameterValue.of(entity.isWriteAll()));
      sq.with(MANAGE_ALL, SqlParameterValue.of(entity.isManageAll()));
      sq.with(ENABLED, SqlParameterValue.of(entity.isEnabled()));
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
  private Iterator<SecurityDomain> runList(SecurityDomainFilter filter,
      SecurityDomainCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<SecurityDomain> sq = filteredQuery(template, filter);
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
  private Iterator<SecurityDomain> runListForUpdate(SecurityDomainFilter filter,
      SecurityDomainCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<SecurityDomain> sq = filteredQuery(template, filter);
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
   * @param sq
   * @param offset
   * @param cursor
   * @param order
   * @return
   */
  private PartialWhere[] tryToOrderByNameAsc(SqlSchematicQuery<SecurityDomain> sq,
      PartialWhere[] offset, SecurityDomainCursor cursor, SecurityDomainOrder order) {
    if (order == SecurityDomainOrder.NAME_ASC) {
      sq.addOrderAsc("name");
      Optional<String> sinceName = cursor.getSinceName();
      if (sinceName.isPresent()) {
        String sinceNameValue = sinceName.get();
        offset[0] = PartialWhere.or(offset[0], PartialWhere.and(offset[1],
            PartialWhere.where("name", SqlOperator.GT, SqlParameterValue.of(sinceNameValue))));
        offset[1] = PartialWhere.and(offset[1],
            PartialWhere.where("name", SqlOperator.EQ, SqlParameterValue.of(sinceNameValue)));
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
  private PartialWhere[] tryToOrderByNameDesc(SqlSchematicQuery<SecurityDomain> sq,
      PartialWhere[] offset, SecurityDomainCursor cursor, SecurityDomainOrder order) {
    if (order == SecurityDomainOrder.NAME_DESC) {
      sq.addOrderDesc("name");
      Optional<String> sinceName = cursor.getSinceName();
      if (sinceName.isPresent()) {
        String sinceNameValue = sinceName.get();
        offset[0] = PartialWhere.or(offset[0], PartialWhere.and(offset[1],
            PartialWhere.where("name", SqlOperator.GT, SqlParameterValue.of(sinceNameValue))));
        offset[1] = PartialWhere.and(offset[1],
            PartialWhere.where("name", SqlOperator.EQ, SqlParameterValue.of(sinceNameValue)));
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
  private SecurityDomain verified(boolean exists, SecurityDomain entity, SqlTemplate template) {
    if (exists) {
      return entity;
    } else {
      template.createSqlCommand("delete from \"access_security_domain\" where \"uid\" = :uid")
          .with("uid", SqlParameterValue.of(entity.getUid())).execute();
      throw new NotFoundException("");
    }
  }
}
