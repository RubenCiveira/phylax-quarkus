package net.civeira.phylax.features.access.user.infrastructure.driver.rest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.tenant.domain.TenantReference;
import net.civeira.phylax.features.access.user.application.usecase.list.UserListCursor;
import net.civeira.phylax.features.access.user.application.usecase.list.UserListFilter;
import net.civeira.phylax.features.access.user.application.usecase.list.UserListOrder;
import net.civeira.phylax.features.access.user.application.usecase.list.UserListProjection;
import net.civeira.phylax.features.access.user.application.usecase.list.UserListUsecase;
import net.civeira.phylax.generated.openapi.model.TenantApiRef;
import net.civeira.phylax.generated.openapi.model.UserApiDto;
import net.civeira.phylax.generated.openapi.model.UserApiDtoList;

@RequiredArgsConstructor
@RequestScoped
public class UserListController {

  /**
   * @autogenerated ListControllerGenerator
   */
  private static final String LIMIT_APPEND = "&limit=";

  /**
   * @autogenerated ListControllerGenerator
   */
  private static final String NAME_APPEND = "&name=";

  /**
   * @autogenerated ListControllerGenerator
   */
  private static final String ROOT_APPEND = "&root=";

  /**
   * @autogenerated ListControllerGenerator
   */
  private static final String SEARCH_APPEND = "&search=";

  /**
   * @autogenerated ListControllerGenerator
   */
  private static final String SINCE_APPEND = "&since-uid=";

  /**
   * @autogenerated ListControllerGenerator
   */
  private static final String TENANTS_APPEND = "&tenants=";

  /**
   * @autogenerated ListControllerGenerator
   */
  private static final String TENANT_APPEND = "&tenant=";

  /**
   * @autogenerated ListControllerGenerator
   */
  private static final String UID_APPEND = "&uid=";

  /**
   * User
   *
   * @autogenerated ListControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated ListControllerGenerator
   */
  private final UserListUsecase list;

  /**
   * @autogenerated ListControllerGenerator
   * @param uids
   * @param search
   * @param root
   * @param name
   * @param tenant
   * @param tenants
   * @param limit
   * @param sinceUid
   * @param sinceName
   * @param order
   * @return
   */
  public Response userApiList(final List<String> uids, final String search, final Boolean root,
      final String name, final String tenant, final List<String> tenants, final Integer limit,
      final String sinceUid, final String sinceName, final String order) {
    List<UserListOrder> orderSteps = null == order ? List.of()
        : Arrays.asList(order.split(",")).stream().map(this::mapOrder).filter(Objects::nonNull)
            .toList();
    UserListFilter.UserListFilterBuilder filterBuilder = UserListFilter.builder();
    UserListCursor.UserListCursorBuilder cursorBuilder = UserListCursor.builder();
    filterBuilder =
        filterBuilder.uids(uids.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    filterBuilder = filterBuilder.search(search);
    filterBuilder = filterBuilder.root(root);
    filterBuilder = filterBuilder.name(name);
    if (null != tenant) {
      filterBuilder = filterBuilder.tenant(TenantReference.of(tenant));
    }
    filterBuilder = filterBuilder
        .tenants(tenants.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    cursorBuilder = cursorBuilder.limit(limit);
    cursorBuilder = cursorBuilder.sinceUid(sinceUid);
    cursorBuilder = cursorBuilder.sinceName(sinceName);
    cursorBuilder = cursorBuilder.order(orderSteps);
    UserListFilter filter = filterBuilder.build();
    UserListCursor cursor = cursorBuilder.build();
    List<UserListProjection> listed = list.list(currentRequest.interaction(), filter, cursor);
    return currentRequest.cacheableResponse(listed, toListApiModel(listed, filter, cursor),
        "user-" + ("" + filter + cursor).hashCode());
  }

  /**
   * @autogenerated ListControllerGenerator
   * @param label
   * @return
   */
  private UserListOrder mapOrder(final String label) {
    if (null == label) {
      return null;
    } else if (label.trim().equals("name-asc")) {
      return UserListOrder.NAME_ASC;
    } else if (label.trim().equals("name-desc")) {
      return UserListOrder.NAME_DESC;
    } else {
      return null;
    }
  }

  /**
   * @autogenerated ListControllerGenerator
   * @param dto
   * @return
   */
  private UserApiDto toApiModel(UserListProjection dto) {
    UserApiDto userApiDto = new UserApiDto();
    userApiDto.setUid(dto.getUid());
    userApiDto.setTenant(new TenantApiRef().$ref(dto.getTenantReference()));
    userApiDto.setName(dto.getName());
    userApiDto.setPassword("*****");
    userApiDto.setEmail(dto.getEmail());
    userApiDto.setEnabled(dto.getEnabled());
    userApiDto.setTemporalPassword(dto.getTemporalPassword());
    userApiDto.setUseSecondFactors(dto.getUseSecondFactors());
    userApiDto.setSecondFactorSeed("*****");
    userApiDto.setBlockedUntil(dto.getBlockedUntil());
    userApiDto.setLanguage(dto.getLanguage());
    userApiDto.setProvider(dto.getProvider());
    userApiDto.setVersion(dto.getVersion());
    return userApiDto;
  }

  /**
   * @autogenerated ListControllerGenerator
   * @param users
   * @param filter
   * @param cursor
   * @return
   */
  private UserApiDtoList toListApiModel(List<UserListProjection> users, UserListFilter filter,
      UserListCursor cursor) {
    Optional<UserListProjection> last =
        users.isEmpty() ? Optional.empty() : Optional.of(users.get(users.size() - 1));
    StringBuilder self = new StringBuilder();
    StringBuilder next = new StringBuilder();
    StringBuilder first = new StringBuilder();
    if (!filter.getUids().isEmpty()) {
      String uidsValue =
          URLEncoder.encode(String.join(",", filter.getUids()), StandardCharsets.UTF_8);
      self.append(UID_APPEND + uidsValue);
      next.append(UID_APPEND + uidsValue);
      first.append(UID_APPEND + uidsValue);
    }
    filter.getSearch().ifPresent(search -> {
      String searchValue = URLEncoder.encode(search, StandardCharsets.UTF_8);
      self.append(SEARCH_APPEND + searchValue);
      next.append(SEARCH_APPEND + searchValue);
      first.append(SEARCH_APPEND + searchValue);
    });
    filter.getRoot().ifPresent(filterRoot -> {
      String rootValue = URLEncoder.encode(Boolean.TRUE.equals(filterRoot) ? "true" : "false",
          StandardCharsets.UTF_8);
      self.append(ROOT_APPEND + rootValue);
      next.append(ROOT_APPEND + rootValue);
      first.append(ROOT_APPEND + rootValue);
    });
    filter.getName().ifPresent(filterName -> {
      String nameValue = URLEncoder.encode(String.valueOf(filterName), StandardCharsets.UTF_8);
      self.append(NAME_APPEND + nameValue);
      next.append(NAME_APPEND + nameValue);
      first.append(NAME_APPEND + nameValue);
    });
    filter.getTenant().ifPresent(filterTenant -> {
      String tenantValue =
          URLEncoder.encode(String.valueOf(filterTenant.getUid()), StandardCharsets.UTF_8);
      self.append(TENANT_APPEND + tenantValue);
      next.append(TENANT_APPEND + tenantValue);
      first.append(TENANT_APPEND + tenantValue);
    });
    if (!filter.getTenants().isEmpty()) {
      String tenantsValue =
          URLEncoder.encode(String.join(",", filter.getTenants()), StandardCharsets.UTF_8);
      self.append(TENANTS_APPEND + tenantsValue);
      next.append(TENANTS_APPEND + tenantsValue);
      first.append(TENANTS_APPEND + tenantsValue);
    }
    cursor.getLimit().ifPresent(limit -> {
      self.append(LIMIT_APPEND + limit);
      first.append(LIMIT_APPEND + limit);
      next.append(LIMIT_APPEND + limit);
    });
    cursor.getSinceUid().ifPresent(
        since -> self.append(SINCE_APPEND + URLEncoder.encode(since, StandardCharsets.UTF_8)));
    cursor.getSinceName().ifPresent(sinceName -> self
        .append("&since-name=" + URLEncoder.encode(sinceName, StandardCharsets.UTF_8)));
    if (!cursor.getOrder().isEmpty()) {
      String urlOrder = URLEncoder.encode(
          String.join(",",
              cursor.getOrder().stream().map(this::writeOrder).filter(Objects::nonNull).toList()),
          StandardCharsets.UTF_8);
      self.append("&order=" + urlOrder);
      next.append("&order=" + urlOrder);
      first.append("&order=" + urlOrder);
    }
    last.ifPresent(lastDto -> {
      next.append(SINCE_APPEND + URLEncoder.encode(lastDto.getUid(), StandardCharsets.UTF_8));;
      cursor.getSinceName().ifPresent(sinceName -> next
          .append("&since-name=" + URLEncoder.encode(sinceName, StandardCharsets.UTF_8)));
    });
    return new UserApiDtoList().items(users.stream().map(this::toApiModel).toList())
        .next(next.length() > 1 ? "?" + next.substring(1) : "")
        .self(self.length() > 1 ? "?" + self.substring(1) : "")
        .first(first.length() > 1 ? "?" + first.substring(1) : "");
  }

  /**
   * @autogenerated ListControllerGenerator
   * @param order
   * @return
   */
  private String writeOrder(final UserListOrder order) {
    if (null == order) {
      return null;
    } else if (order == UserListOrder.NAME_ASC) {
      return "name-asc";
    } else if (order == UserListOrder.NAME_DESC) {
      return "name-desc";
    } else {
      return null;
    }
  }
}
