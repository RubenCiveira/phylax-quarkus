package net.civeira.phylax.features.access.role.infrastructure.driver.rest;

import java.util.List;
import java.util.stream.Stream;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.batch.BatchIdentificator;
import net.civeira.phylax.common.batch.BatchProgress;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.role.application.usecase.delete.RoleCheckBatchDeleteStatus;
import net.civeira.phylax.features.access.role.application.usecase.delete.RoleDeleteFilter;
import net.civeira.phylax.features.access.role.application.usecase.delete.RoleDeleteUsecase;
import net.civeira.phylax.features.access.role.domain.RoleReference;
import net.civeira.phylax.features.access.tenant.domain.TenantReference;

@RequiredArgsConstructor
@RequestScoped
public class RoleDeleteController {

  /**
   * @autogenerated DeleteControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated DeleteControllerGenerator
   */
  private final RoleDeleteUsecase delete;

  /**
   * @autogenerated DeleteControllerGenerator
   * @param uids
   * @param search
   * @param name
   * @param tenant
   * @param tenants
   * @return
   */
  public Response roleApiBatchDelete(final List<String> uids, final String search,
      final String name, final String tenant, final List<String> tenants) {
    RoleDeleteFilter.RoleDeleteFilterBuilder filterBuilder = RoleDeleteFilter.builder();
    filterBuilder =
        filterBuilder.uids(uids.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    filterBuilder = filterBuilder.search(search);
    filterBuilder =
        filterBuilder.uids(uids.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    filterBuilder = filterBuilder.search(search);
    filterBuilder = filterBuilder.name(name);
    if (null != tenant) {
      filterBuilder = filterBuilder.tenant(TenantReference.of(tenant));
    }
    filterBuilder = filterBuilder
        .tenants(tenants.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    RoleDeleteFilter filter = filterBuilder.build();
    BatchIdentificator task = delete.delete(currentRequest.interaction(), filter);
    /* .header("Last-Modified", value.getSince().format(DateTimeFormatter.RFC_1123_DATE_TIME)) */
    return Response.ok(task).build();
  }

  /**
   * @autogenerated DeleteControllerGenerator
   * @param batchId
   * @return
   */
  public Response roleApiBatchDeleteQuery(final String batchId) {
    BatchProgress task = delete.checkProgress(
        RoleCheckBatchDeleteStatus.builder().taskId(batchId).build(currentRequest.interaction()));
    return Response.ok(task).build();
  }

  /**
   * @autogenerated DeleteControllerGenerator
   * @param uid
   * @return
   */
  @Transactional
  public Response roleApiDelete(final String uid) {
    delete.delete(currentRequest.interaction(), RoleReference.of(uid));
    return Response.noContent().build();
  }
}
