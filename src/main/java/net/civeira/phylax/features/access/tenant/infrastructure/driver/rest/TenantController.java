package net.civeira.phylax.features.access.tenant.infrastructure.driver.rest;

import java.util.List;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.generated.openapi.api.TenantApi;
import net.civeira.phylax.generated.openapi.model.TenantApiDto;

@RequiredArgsConstructor
public class TenantController implements TenantApi {

  /**
   * TenantApiDto
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TenantCreateController createController;

  /**
   * Tenant
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TenantDeleteController deleteController;

  /**
   * Disable
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TenantDisableController disableController;

  /**
   * Enable
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TenantEnableController enableController;

  /**
   * Tenant
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TenantListController listController;

  /**
   * Tenant
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TenantRetrieveController retrieveController;

  /**
   * TenantApiDto
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TenantUpdateController updateController;

  /**
   * @autogenerated ApiControllerGenerator
   * @param uids
   * @param search
   * @param name
   * @return
   */
  @Override
  public Response tenantApiBatchDelete(final List<String> uids, final String search,
      final String name) {
    return deleteController.tenantApiBatchDelete(uids, search, name);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param batchId
   * @return
   */
  @Override
  public Response tenantApiBatchDeleteQuery(final String batchId) {
    return deleteController.tenantApiBatchDeleteQuery(batchId);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uids
   * @param search
   * @param name
   * @return
   */
  @Override
  public Response tenantApiBatchDisable(final List<String> uids, final String search,
      final String name) {
    return disableController.tenantApiBatchDisable(uids, search, name);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param batchId
   * @return
   */
  @Override
  public Response tenantApiBatchDisableQuery(final String batchId) {
    return disableController.tenantApiBatchDisableQuery(batchId);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uids
   * @param search
   * @param name
   * @return
   */
  @Override
  public Response tenantApiBatchEnable(final List<String> uids, final String search,
      final String name) {
    return enableController.tenantApiBatchEnable(uids, search, name);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param batchId
   * @return
   */
  @Override
  public Response tenantApiBatchEnableQuery(final String batchId) {
    return enableController.tenantApiBatchEnableQuery(batchId);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param tenant
   * @return
   */
  @Override
  public Response tenantApiCreate(TenantApiDto tenant) {
    return createController.tenantApiCreate(tenant);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response tenantApiDelete(final String uid) {
    return deleteController.tenantApiDelete(uid);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response tenantApiDisable(final String uid) {
    return disableController.tenantApiDisable(uid);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response tenantApiEnable(final String uid) {
    return enableController.tenantApiEnable(uid);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uids
   * @param search
   * @param name
   * @param limit
   * @param sinceUid
   * @param sinceName
   * @param order
   * @return
   */
  @Override
  public Response tenantApiList(final List<String> uids, final String search, final String name,
      final Integer limit, final String sinceUid, final String sinceName, final String order) {
    return listController.tenantApiList(uids, search, name, limit, sinceUid, sinceName, order);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response tenantApiRetrieve(final String uid) {
    return retrieveController.tenantApiRetrieve(uid);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @param tenant
   * @return
   */
  @Override
  public Response tenantApiUpdate(final String uid, TenantApiDto tenant) {
    return updateController.tenantApiUpdate(uid, tenant);
  }
}
