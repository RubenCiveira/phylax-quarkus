package net.civeira.phylax.features.access.useridentity.infrastructure.repository;

import java.util.ArrayList;
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
import net.civeira.phylax.common.infrastructure.sql.AbstractSqlParametrized;
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
import net.civeira.phylax.features.access.role.domain.Role;
import net.civeira.phylax.features.access.role.infrastructure.repository.RoleRepository;
import net.civeira.phylax.features.access.useridentity.domain.Roles;
import net.civeira.phylax.features.access.useridentity.domain.UserIdentity;
import net.civeira.phylax.features.access.useridentity.domain.UserIdentityRef;
import net.civeira.phylax.features.access.useridentity.domain.gateway.UserIdentityCursor;
import net.civeira.phylax.features.access.useridentity.domain.gateway.UserIdentityFilter;
import net.civeira.phylax.features.access.useridentity.domain.valueobject.RelyingPartyVO;
import net.civeira.phylax.features.access.useridentity.domain.valueobject.RolesVO;
import net.civeira.phylax.features.access.useridentity.domain.valueobject.TrustedClientVO;
import net.civeira.phylax.features.access.useridentity.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.useridentity.domain.valueobject.UserVO;
import net.civeira.phylax.features.access.useridentity.domain.valueobject.VersionVO;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class UserIdentityRepository {

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_USER_IDENTITY_RELYING_PARTY =
      "access_user_identity.relying_party";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_USER_IDENTITY_SNAKE = "access_user_identity";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_USER_IDENTITY_TRUSTED_CLIENT =
      "access_user_identity.trusted_client";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_USER_IDENTITY_UID = "access_user_identity.uid";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_USER_IDENTITY_USER = "access_user_identity.user";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_USER_IDENTITY_USER_TENANT = "access_user_identity_user.tenant";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String ACCESS_USER_IDENTITY_VERSION = "access_user_identity.version";

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
  private static final String ROLE = "role";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String TOMAP = "tomap";

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
  private static final String USER = "user";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_IDENTITY = "userIdentity";

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private static final String USER_IDENTITY_KEBAB = "user-identity";

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
  private final RoleRepository roleRepository;

  /**
   * @autogenerated RepositoryJdbcGenerator
   */
  private final Tracer tracer;

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public long count(UserIdentityFilter filter) {
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
  public UserIdentity create(UserIdentity entity) {
    return runCreate(entity, null);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  public UserIdentity create(UserIdentity entity, Predicate<UserIdentity> verifier) {
    return runCreate(entity, verifier);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   */
  public void delete(UserIdentity entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      deleteRoles(entity, template);
      SqlCommand sq =
          template.createSqlCommand("delete from \"access_user_identity\" where \"uid\" = :uid");
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
  public boolean exists(String uid, Optional<UserIdentityFilter> filter) {
    return retrieve(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public boolean existsForUpdate(String uid, Optional<UserIdentityFilter> filter) {
    return retrieveForUpdate(uid, filter).isPresent();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public Optional<UserIdentity> find(UserIdentityFilter filter) {
    UserIdentityCursor cursor = UserIdentityCursor.builder().build();
    return new UserIdentitySlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<UserIdentity> find(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<UserIdentity> createSqlQuery = template.createSqlQuery(sql);
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
  public Optional<UserIdentity> findForUpdate(UserIdentityFilter filter) {
    UserIdentityCursor cursor = UserIdentityCursor.builder().build();
    return new UserIdentitySlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).one();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public Optional<UserIdentity> findForUpdate(final String sql,
      final Map<String, SqlParameterValue> params) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<UserIdentity> createSqlQuery = template.createSqlQuery(sql);
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
  public List<UserIdentity> list(UserIdentityFilter filter) {
    UserIdentityCursor cursor = UserIdentityCursor.builder().build();
    return new UserIdentitySlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<UserIdentity> list(final String sql, final Map<String, SqlParameterValue> params) {
    return list(sql, params, null, false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @param limit
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<UserIdentity> list(final String sql, final Map<String, SqlParameterValue> params,
      final int limit) {
    return list(sql, params, Integer.valueOf(limit), false);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @return
   */
  public List<UserIdentity> listForUpdate(UserIdentityFilter filter) {
    UserIdentityCursor cursor = UserIdentityCursor.builder().build();
    return new UserIdentitySlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor).all();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sql
   * @param params
   * @return sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%"))
   */
  public List<UserIdentity> listForUpdate(final String sql,
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
  public List<UserIdentity> listForUpdate(final String sql,
      final Map<String, SqlParameterValue> params, final int limit) {
    return list(sql, params, Integer.valueOf(limit), true);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public UserIdentity resolve(UserIdentityRef reference) {
    return reference instanceof UserIdentity userIdentity ? userIdentity
        : retrieve(reference.getUid(), Optional.empty()).orElseThrow(() -> new NotFoundException(
            "Trying to resolve inexistent UserIdentity: " + reference.getUid()));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param reference
   * @return
   */
  public UserIdentity resolveForUpdate(UserIdentityRef reference) {
    return reference instanceof UserIdentity userIdentity ? userIdentity
        : retrieveForUpdate(reference.getUid(), Optional.empty())
            .orElseThrow(() -> new NotFoundException(
                "Trying to resolve inexistent UserIdentity: " + reference.getUid()));
  }

  /**
   * Retrieve full data for a reference.
   *
   * @autogenerated RepositoryJdbcGenerator
   * @param childs
   * @return Retrieve one single value
   */
  public List<Role> resolveRoles(final List<Roles> childs) {
    return roleRepository.list(
        "select \"access_role\".* from \"access_role\" join \"access_user_identity_role\" on \"access_role\".\"uid\" = \"access_user_identity_role\".\"role\" "
            + " where \"access_user_identity_role\".\"uid\" in ( :availables )",
        Map.of("availables",
            SqlListParameterValue.strings(childs.stream().map(Roles::getUid).toList())));
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<UserIdentity> retrieve(String uid, Optional<UserIdentityFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      UserIdentityFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> UserIdentityFilter.builder().uid(uid).build());
      SqlSchematicQuery<UserIdentity> sq = filteredQuery(template, readyFilter);
      return sq.query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param uid
   * @param filter
   * @return
   */
  public Optional<UserIdentity> retrieveForUpdate(String uid, Optional<UserIdentityFilter> filter) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      UserIdentityFilter readyFilter = filter.map(val -> val.withUid(uid))
          .orElseGet(() -> UserIdentityFilter.builder().uid(uid).build());
      SqlSchematicQuery<UserIdentity> sq = filteredQuery(template, readyFilter);
      return sq.forUpdate().query(converter()).one();
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<UserIdentity> slide(UserIdentityFilter filter, UserIdentityCursor cursor) {
    return new UserIdentitySlider(runList(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  public Slider<UserIdentity> slideForUpdate(UserIdentityFilter filter, UserIdentityCursor cursor) {
    return new UserIdentitySlider(runListForUpdate(filter, cursor), cursor.getLimit().orElse(0),
        this::runList, filter, cursor);
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @return
   */
  public UserIdentity update(UserIdentity entity) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "update \"access_user_identity\" set  \"user\" = :user, \"relying_party\" = :relyingParty, \"trusted_client\" = :trustedClient, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      sq.with(USER, SqlParameterValue.of(entity.getUserUid()));
      sq.with(RELYING_PARTY, entity.getRelyingPartyUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(TRUSTED_CLIENT, entity.getTrustedClientUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(VERSION, entity.getVersion().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      int num = sq.execute();
      if (0 == num) {
        throw new OptimistLockException("No delete from");
      }
      UserIdentity result = entity.withNextVersion();
      saveRoles(result, result.getRoles());
      return result;
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @return
   */
  private SqlConverter<UserIdentity> converter() {
    return row -> {
      try {
        List<Roles> rolesFromChilds = new ArrayList<>();
        row.<Roles>getChilds("roles")
            .whenComplete((childlist, fail) -> rolesFromChilds.addAll(childlist));
        return Optional.of(UserIdentity.builder().uidValue(UidVO.from(row.getString(UID)))
            .userValue(UserVO.fromReference(row.getString(USER)))
            .relyingPartyValue(RelyingPartyVO.fromReference(row.getString(RELYING_PARTY_SNAKE)))
            .trustedClientValue(TrustedClientVO.fromReference(row.getString(TRUSTED_CLIENT_SNAKE)))
            .rolesValue(RolesVO.from(rolesFromChilds))
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
  private void deleteRoles(UserIdentity entity, SqlTemplate template) {
    SqlCommand sq = template.createSqlCommand(
        "delete from \"access_user_identity_role\" where \"user-identity\" = :user-identity");
    sq.with(USER_IDENTITY_KEBAB, SqlParameterValue.of(entity.getUid()));
    sq.execute();
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param template
   * @param filter
   * @return
   */
  private <T> SqlSchematicQuery<T> filteredQuery(SqlTemplate template, UserIdentityFilter filter) {
    SqlSchematicQuery<T> sq = template.createSqlSchematicQuery(ACCESS_USER_IDENTITY_SNAKE);
    sq.selectFields(ACCESS_USER_IDENTITY_UID, ACCESS_USER_IDENTITY_USER,
        ACCESS_USER_IDENTITY_RELYING_PARTY, ACCESS_USER_IDENTITY_TRUSTED_CLIENT,
        ACCESS_USER_IDENTITY_VERSION);
    withChilds(sq);
    filter.getUid().ifPresent(uid -> sq.where(UID, SqlOperator.EQ, SqlParameterValue.of(uid)));
    if (!filter.getUids().isEmpty()) {
      sq.where(UID, SqlOperator.IN, SqlListParameterValue.strings(filter.getUids()));
    }
    filter.getSearch().ifPresent(
        search -> sq.where("uid", SqlOperator.LIKE, SqlParameterValue.of("%" + search + "%")));
    filter.getUser()
        .ifPresent(user -> sq.where(USER, SqlOperator.EQ, SqlParameterValue.of(user.getUid())));
    if (!filter.getUsers().isEmpty()) {
      sq.where(USER, SqlOperator.IN, SqlListParameterValue.strings(filter.getUsers()));
    }
    filter.getRelyingParty().ifPresent(relyingParty -> sq.where(RELYING_PARTY_SNAKE, SqlOperator.EQ,
        SqlParameterValue.of(relyingParty.getUid())));
    if (!filter.getRelyingPartys().isEmpty()) {
      sq.where(RELYING_PARTY_SNAKE, SqlOperator.IN,
          SqlListParameterValue.strings(filter.getRelyingPartys()));
    }
    filter.getTrustedClient().ifPresent(trustedClient -> sq.where(TRUSTED_CLIENT_SNAKE,
        SqlOperator.EQ, SqlParameterValue.of(trustedClient.getUid())));
    if (!filter.getTrustedClients().isEmpty()) {
      sq.where(TRUSTED_CLIENT_SNAKE, SqlOperator.IN,
          SqlListParameterValue.strings(filter.getTrustedClients()));
    }
    filter.getUserTenantTenantAccesible().ifPresent(userTenantTenantAccesible -> {
      sq.join("access_user", "access_user_identity_user", "access_user_identity.user",
          "access_user_identity_user.uid");
      sq.where(ACCESS_USER_IDENTITY_USER_TENANT, SqlOperator.EQ,
          SqlParameterValue.of(userTenantTenantAccesible));
    });
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
  private List<UserIdentity> list(final String sql, final Map<String, SqlParameterValue> params,
      final Integer limit, final boolean forUpdate) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<UserIdentity> createSqlQuery = template.createSqlQuery(sql);
      withChilds(createSqlQuery);
      params.forEach(createSqlQuery::with);
      if (forUpdate) {
        createSqlQuery = createSqlQuery.forUpdate();
      }
      SqlResult<UserIdentity> query = createSqlQuery.query(converter());
      return null == limit ? query.all() : query.limit(limit);
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param entity
   * @param verifier
   * @return
   */
  private UserIdentity runCreate(UserIdentity entity, Predicate<UserIdentity> verifier) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlCommand sq = template.createSqlCommand(
          "insert into \"access_user_identity\" ( \"uid\", \"user\", \"relying_party\", \"trusted_client\", \"version\") values ( :uid, :user, :relyingParty, :trustedClient, :version)");
      sq.with(UID, SqlParameterValue.of(entity.getUid()));
      sq.with(USER, SqlParameterValue.of(entity.getUserUid()));
      sq.with(RELYING_PARTY, entity.getRelyingPartyUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(TRUSTED_CLIENT, entity.getTrustedClientUid().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullString));
      sq.with(VERSION, entity.getVersion().map(SqlParameterValue::of)
          .orElseGet(SqlParameterValue::ofNullInteger));
      int num = sq.execute();
      if (0 == num) {
        throw new IllegalArgumentException("No insert into");
      }
      UserIdentity result =
          verifier == null ? entity : verified(verifier.test(entity), entity, template);
      result = saveRoles(result, result.getRoles());
      return result;
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param filter
   * @param cursor
   * @return
   */
  private Iterator<UserIdentity> runList(UserIdentityFilter filter, UserIdentityCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<UserIdentity> sq = filteredQuery(template, filter);
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
  private Iterator<UserIdentity> runListForUpdate(UserIdentityFilter filter,
      UserIdentityCursor cursor) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlSchematicQuery<UserIdentity> sq = filteredQuery(template, filter);
      cursor.getSinceUid()
          .ifPresent(since -> sq.where(UID, SqlOperator.GT, SqlParameterValue.of(since)));
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
  private UserIdentity saveRoles(UserIdentity entity, List<Roles> childs) {
    try (SqlTemplate template = new SqlTemplate(datasource, tracer)) {
      SqlQuery<Object[]> sq = template.createSqlQuery(
          "select \"uid\", \"user_identity\" from \"access_user_identity_role\" where (\"user_identity\" = :userIdentity)"
              + " or ( \"uid\" in (:tomap) and \"user_identity\" != :userIdentity )");
      sq.with(USER_IDENTITY, SqlParameterValue.of(entity.getUid()));
      sq.with(TOMAP, SqlListParameterValue.strings(childs.stream().map(Roles::getUid).toList()));
      List<Object[]> list =
          sq.query(row -> Optional.of(new Object[] {row.getString(1), row.getString(2)})).all();
      if (list.stream().anyMatch(arr -> !entity.getUid().equals(arr[1]))) {
        throw new IllegalArgumentException("Try to move from another parent");
      }
      List<Roles> response = new ArrayList<>();
      List<String> codes = new ArrayList<>(list.stream().map(arr -> (String) arr[0]).toList());
      childs.forEach(child -> codes.remove(child.getUid()));
      if (!codes.isEmpty()) {
        SqlCommand deleted = template
            .createSqlCommand("delete from\"access_user_identity_role\" where \"uid\" in (:codes)");
        deleted.with("codes", SqlListParameterValue.strings(codes));
        deleted.execute();
      }
      childs.forEach(child -> {
        SqlCommand command = template.createSqlCommand(codes.contains(child.getUid())
            ? "update \"access_user_identity_role\" set \"verify\" = :verify, \"medal\" = :medal, \"version\" = \"version\" + 1 where \"uid\" = :uid and \"version\" = :version"
            : "insert into \"access_user_identity_role\" ( \"uid\", \"user_identity\", \"role\", \"version\") values ( :uid, :userIdentity, :role, :version)");
        command.with(UID, SqlParameterValue.of(child.getUid()));
        command.with(USER_IDENTITY, SqlParameterValue.of(entity.getUid()));
        command.with(ROLE, SqlParameterValue.of(child.getRoleUid()));
        command.with(VERSION, child.getVersion().map(SqlParameterValue::of)
            .orElseGet(SqlParameterValue::ofNullInteger));
        int val = command.execute();
        if (val != 1) {
          throw new OptimistLockException("No child processed");
        } else {
          response.add(child.withNextVersion());
        }
      });
      // TODO: need to save througn entity Roles => RolesVO::from[ result ] ;
      return entity;
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param exists
   * @param entity
   * @param template
   * @return
   */
  private UserIdentity verified(boolean exists, UserIdentity entity, SqlTemplate template) {
    if (exists) {
      return entity;
    } else {
      template.createSqlCommand("delete from \"access_user_identity\" where \"uid\" = :uid")
          .with("uid", SqlParameterValue.of(entity.getUid())).execute();
      throw new NotFoundException("");
    }
  }

  /**
   * @autogenerated RepositoryJdbcGenerator
   * @param sq
   */
  private void withChilds(AbstractSqlParametrized<?> sq) {
    sq.child(100, "roles",
        "select \"user_identity\",\"uid\",\"role\",\"version\" from \"access_user_identity_role\"  where \"user_identity\" in (:userIdentity)",
        "uid", "user_identity", row -> {
          try {
            return Optional.of(Roles.builder().uid(row.getString(UID)).role(row.getString(ROLE))
                .version(row.getInt(VERSION)).build());
          } catch (ConstraintException ce) {
            log.error("Unable to map roles child data for {}", row.getString(2), ce);
            return Optional.empty();
          }
        });
  }
}
