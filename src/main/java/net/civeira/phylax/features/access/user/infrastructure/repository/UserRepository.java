package net.civeira.phylax.features.access.user.infrastructure.repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
import net.civeira.phylax.common.crypto.AesCipherService;
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
import net.civeira.phylax.features.access.user.User;
import net.civeira.phylax.features.access.user.UserRef;
import net.civeira.phylax.features.access.user.query.UserCursor;
import net.civeira.phylax.features.access.user.query.UserFilter;
import net.civeira.phylax.features.access.user.query.UserOrder;
import net.civeira.phylax.features.access.user.valueobject.BlockedUntilVO;
import net.civeira.phylax.features.access.user.valueobject.EmailVO;
import net.civeira.phylax.features.access.user.valueobject.EnabledVO;
import net.civeira.phylax.features.access.user.valueobject.LanguageVO;
import net.civeira.phylax.features.access.user.valueobject.NameVO;
import net.civeira.phylax.features.access.user.valueobject.PasswordVO;
import net.civeira.phylax.features.access.user.valueobject.ProviderVO;
import net.civeira.phylax.features.access.user.valueobject.SecondFactorSeedVO;
import net.civeira.phylax.features.access.user.valueobject.TemporalPasswordVO;
import net.civeira.phylax.features.access.user.valueobject.TenantVO;
import net.civeira.phylax.features.access.user.valueobject.UidVO;
import net.civeira.phylax.features.access.user.valueobject.UseSecondFactorsVO;
import net.civeira.phylax.features.access.user.valueobject.VersionVO;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class UserRepository {

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BLOCKED_UNTIL = "blockedUntil";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String BLOCKED_UNTIL_SNAKE = "blocked_until";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String EMAIL = "email";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ENABLED = "enabled";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String LANGUAGE = "language";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String NAME = "name";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String PASSWORD = "password";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String PROVIDER = "provider";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECOND_FACTOR_SEED = "secondFactorSeed";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String SECOND_FACTOR_SEED_SNAKE = "second_factor_seed";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TEMPORAL_PASSWORD = "temporalPassword";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TEMPORAL_PASSWORD_SNAKE = "temporal_password";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TENANT = "tenant";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String UID = "uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER = "user";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_BLOCKED_UNTIL = "user.blocked_until";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_EMAIL = "user.email";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_ENABLED = "user.enabled";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_LANGUAGE = "user.language";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_NAME = "user.name";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_PASSWORD = "user.password";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_PROVIDER = "user.provider";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_SECOND_FACTOR_SEED = "user.second_factor_seed";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_TEMPORAL_PASSWORD = "user.temporal_password";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_TENANT = "user.tenant";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_UID = "user.uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_USE_SECOND_FACTORS = "user.use_second_factors";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_VERSION = "user.version";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USE_SECOND_FACTORS = "useSecondFactors";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USE_SECOND_FACTORS_SNAKE = "use_second_factors";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String VERSION = "version";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private final AesCipherService cypher;

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
  public long count(UserFilter filter) {
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
  public User create(User entity) {
    return runCreate(entity, null);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  public User create(User entity, Predicate<User> verifier) {
    return runCreate(entity, verifier);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   */
  public void delete(User entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand("delete from \"user\" where \"uid\" = :uid");
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
  public User enrich(UserRef reference) {
    return reference instanceof User user ? user
        : retrieve(reference.getUidValue(), Optional.empty())
            .orElseThrow(() -> new NotFoundException(
                "Trying to enrich inexistent User: " + reference.getUidValue()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public User enrichForUpdate(UserRef reference) {
    return reference instanceof User user ? user
        : retrieveForUpdate(reference.getUidValue(), Optional.empty())
            .orElseThrow(() -> new NotFoundException(
                "Trying to enrich inexistent User: " + reference.getUidValue()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean exists(String uid, Optional<UserFilter> filter) {
    return retrieve(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean existsForUpdate(String uid, Optional<UserFilter> filter) {
    return retrieveForUpdate(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<User> find(UserFilter filter) {
    UserCursor cursor = UserCursor.builder().build();
    return new UserSlider(runList(filter, cursor), cursor.getLimit().orElse(0), this::runList,
        filter, cursor).one();
  }

  /**
   * sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<User> find(final String sql, final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<User> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<User> findForUpdate(UserFilter filter) {
    UserCursor cursor = UserCursor.builder().build();
    return new UserSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<User> findForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<User> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      return createSqlQuery.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<User> list(UserFilter filter) {
    UserCursor cursor = UserCursor.builder().build();
    return new UserSlider(runList(filter, cursor), cursor.getLimit().orElse(0), this::runList,
        filter, cursor).all();
  }

  /**
   * sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<User> list(final String sql, final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, false);
  }

  /**
   * sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<User> list(final String sql, final Map<String, SqlParameterValue> params,
      final int limit) {
    return list(sql, params, Integer.valueOf(limit), false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<User> listForUpdate(UserFilter filter) {
    UserCursor cursor = UserCursor.builder().build();
    return new UserSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<User> listForUpdate(final String sql, final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, true);
  }

  /**
   * sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<User> listForUpdate(final String sql, final Map<String, SqlParameterValue> params,
      final int limit) {
    return list(sql, params, Integer.valueOf(limit), true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<User> retrieve(String uid, Optional<UserFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      UserFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> UserFilter.builder().uid(uid).build());
      SqlSchematicQuery<User> sq = filteredQuery(template, readyFilter);
      return sq.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<User> retrieveForUpdate(String uid, Optional<UserFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      UserFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> UserFilter.builder().uid(uid).build());
      SqlSchematicQuery<User> sq = filteredQuery(template, readyFilter);
      return sq.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<User> slide(UserFilter filter, UserCursor cursor) {
    return new UserSlider(runList(filter, cursor), cursor.getLimit().orElse(0), this::runList,
        filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<User> slideForUpdate(UserFilter filter, UserCursor cursor) {
    return new UserSlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @return
   */
  public User update(User entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "update \"user\" set  \"tenant\" = :tenant, \"name\" = :name, \"password\" = :password, \"email\" = :email, \"enabled\" = :enabled, \"temporal_password\" = :temporalPassword, \"use_second_factors\" = :useSecondFactors, \"second_factor_seed\" = :secondFactorSeed, \"blocked_until\" = :blockedUntil, \"language\" = :language, \"provider\" = :provider, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version");
      sq.with(UID, SqlParameterValue.of(entity.getUid().getValue()));
      sq.with(TENANT, entity.getTenant().getReferenceValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(NAME, SqlParameterValue.of(entity.getName().getValue()));
      sq.with(PASSWORD, SqlParameterValue.of(entity.getPassword().getCypheredValue(cypher)));
      sq.with(EMAIL, entity.getEmail().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(ENABLED, entity.getEnabled().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullBoolean));
      sq.with(TEMPORAL_PASSWORD, entity.getTemporalPassword().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullBoolean));
      sq.with(USE_SECOND_FACTORS, entity.getUseSecondFactors().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullBoolean));
      sq.with(SECOND_FACTOR_SEED, entity.getSecondFactorSeed().getCypheredValue(cypher)
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(BLOCKED_UNTIL, entity.getBlockedUntil().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullOffsetDateTime));
      sq.with(LANGUAGE, entity.getLanguage().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(PROVIDER, entity.getProvider().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
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
   * @param sq
   * @param cursor
   */
  private void attachWithOrder(SqlSchematicQuery<User> sq, UserCursor cursor) {
    PartialWhere[] offset = new PartialWhere[] {PartialWhere.empty(), PartialWhere.empty()};
    for (UserOrder order : cursor.getOrder()) {
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
  private SqlConverter<User> converter() {
    return row -> {
      try {
        return Optional.of(User.builder().uid(UidVO.from(row.getString(UID)))
            .tenant(TenantVO.fromReference(row.getString(TENANT)))
            .name(NameVO.from(row.getString(NAME)))
            .password(PasswordVO.fromCyphered(row.getString(PASSWORD)))
            .email(EmailVO.from(row.getString(EMAIL)))
            .enabled(EnabledVO.from(row.getBoolean(ENABLED)))
            .temporalPassword(TemporalPasswordVO.from(row.getBoolean(TEMPORAL_PASSWORD_SNAKE)))
            .useSecondFactors(UseSecondFactorsVO.from(row.getBoolean(USE_SECOND_FACTORS_SNAKE)))
            .secondFactorSeed(
                SecondFactorSeedVO.fromCyphered(row.getString(SECOND_FACTOR_SEED_SNAKE)))
            .blockedUntil(BlockedUntilVO.from(null == row.getTimestamp(BLOCKED_UNTIL_SNAKE) ? null
                : OffsetDateTime.ofInstant(
                    Instant.ofEpochMilli(row.getTimestamp(BLOCKED_UNTIL_SNAKE).getTime()),
                    ZoneId.systemDefault())))
            .language(LanguageVO.from(row.getString(LANGUAGE)))
            .provider(ProviderVO.from(row.getString(PROVIDER)))
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
  private <T> SqlSchematicQuery<T> filteredQuery(SqlTemplate template, UserFilter filter) {
    SqlSchematicQuery<T> sq = template.createSqlSchematicQuery(USER);
    sq.selectFields(USER_UID, USER_TENANT, USER_NAME, USER_PASSWORD, USER_EMAIL, USER_ENABLED,
        USER_TEMPORAL_PASSWORD, USER_USE_SECOND_FACTORS, USER_SECOND_FACTOR_SEED,
        USER_BLOCKED_UNTIL, USER_LANGUAGE, USER_PROVIDER, USER_VERSION);
    filter.getUid().ifPresent(uid -> sq.where(UID, SqlOperator.EQ, SqlParameterValue.of(uid)));
    if (!filter.getUids().isEmpty()) {
      sq.where(UID, SqlOperator.IN, SqlListParameterValue.strings(filter.getUids()));
    }
    filter.getSearch().ifPresent(
        search -> sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%")));
    filter.getRoot()
        .ifPresent(root -> sq.where(TENANT, SqlOperator.IS_NULL, SqlParameterValue.ofNullString()));
    sq.where(PartialWhere.or(
        filter.getNameOrEmail()
            .map(nameOrEmail -> PartialWhere.where("email", SqlOperator.EQ,
                SqlParameterValue.of(nameOrEmail))),
        filter.getNameOrEmail().map(nameOrEmail -> PartialWhere.where("name", SqlOperator.EQ,
            SqlParameterValue.of(nameOrEmail)))));
    filter.getName().ifPresent(name -> sq.where(NAME, SqlOperator.EQ, SqlParameterValue.of(name)));
    filter.getTenant().ifPresent(
        tenant -> sq.where(TENANT, SqlOperator.EQ, SqlParameterValue.of(tenant.getUidValue())));
    if (!filter.getTenants().isEmpty()) {
      sq.where(TENANT, SqlOperator.IN, SqlListParameterValue.strings(filter.getTenants()));
    }
    filter.getTenantTenantAccesible().ifPresent(tenantTenantAccesible -> sq.where(USER_TENANT,
        SqlOperator.EQ, SqlParameterValue.of(tenantTenantAccesible)));
    return sq;
  }

  /**
   * sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @param forUpdate
   * @return sq.where("name", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  private List<User> list(final String sql, final Map<String, SqlParameterValue> params,
      final Integer limit, final boolean forUpdate) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<User> createSqlQuery = template.createSqlQuery(sql);
      params.forEach(createSqlQuery::with);
      if (forUpdate) {
        createSqlQuery = createSqlQuery.forUpdate();
      }
      SqlResult<User> query = createSqlQuery.query(converter());
      return null == limit ? query.all() : query.limit(limit);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  private User runCreate(User entity, Predicate<User> verifier) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "insert into \"user\" ( \"uid\", \"tenant\", \"name\", \"password\", \"email\", \"enabled\", \"temporal_password\", \"use_second_factors\", \"second_factor_seed\", \"blocked_until\", \"language\", \"provider\", \"version\") values ( :uid, :tenant, :name, :password, :email, :enabled, :temporalPassword, :useSecondFactors, :secondFactorSeed, :blockedUntil, :language, :provider, :version)");
      sq.with(UID, SqlParameterValue.of(entity.getUid().getValue()));
      sq.with(TENANT, entity.getTenant().getReferenceValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(NAME, SqlParameterValue.of(entity.getName().getValue()));
      sq.with(PASSWORD, SqlParameterValue.of(entity.getPassword().getCypheredValue(cypher)));
      sq.with(EMAIL, entity.getEmail().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(ENABLED, entity.getEnabled().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullBoolean));
      sq.with(TEMPORAL_PASSWORD, entity.getTemporalPassword().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullBoolean));
      sq.with(USE_SECOND_FACTORS, entity.getUseSecondFactors().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullBoolean));
      sq.with(SECOND_FACTOR_SEED, entity.getSecondFactorSeed().getCypheredValue(cypher)
          .map(SqlParameterValue::of).orElseGet(SqlParameterValue::ofNullString));
      sq.with(BLOCKED_UNTIL, entity.getBlockedUntil().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullOffsetDateTime));
      sq.with(LANGUAGE, entity.getLanguage().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(PROVIDER, entity.getProvider().getValue().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
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
  private Iterator<User> runList(UserFilter filter, UserCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<User> sq = filteredQuery(template, filter);
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
  private Iterator<User> runListForUpdate(UserFilter filter, UserCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<User> sq = filteredQuery(template, filter);
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
  private PartialWhere[] tryToOrderByNameAsc(SqlSchematicQuery<User> sq, PartialWhere[] offset,
      UserCursor cursor, UserOrder order) {
    if (order == UserOrder.NAME_ASC) {
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
  private PartialWhere[] tryToOrderByNameDesc(SqlSchematicQuery<User> sq, PartialWhere[] offset,
      UserCursor cursor, UserOrder order) {
    if (order == UserOrder.NAME_DESC) {
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
  private User verified(boolean exists, User entity, SqlTemplate template) {
    if (exists) {
      return entity;
    } else {
      template.createSqlCommand("delete from \"user\" where \"uid\" = :uid")
          .with("uid", SqlParameterValue.of(entity.getUidValue())).execute();
      throw new NotFoundException("");
    }
  }
}
