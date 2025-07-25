package net.civeira.phylax.features.access.trustedclient.infrastructure.driver.rest;

import java.util.List;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.generated.openapi.api.TrustedClientApi;
import net.civeira.phylax.generated.openapi.model.TrustedClientApiDto;

@RequiredArgsConstructor
public class TrustedClientController implements TrustedClientApi {

  /**
   * TrustedClientApiDto
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TrustedClientCreateController createController;

  /**
   * TrustedClient
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TrustedClientDeleteController deleteController;

  /**
   * Disable
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TrustedClientDisableController disableController;

  /**
   * Enable
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TrustedClientEnableController enableController;

  /**
   * TrustedClient
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TrustedClientListController listController;

  /**
   * TrustedClient
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TrustedClientRetrieveController retrieveController;

  /**
   * TrustedClientApiDto
   *
   * @autogenerated ApiControllerGenerator
   */
  private final TrustedClientUpdateController updateController;

  /**
   * @autogenerated ApiControllerGenerator
   * @param uids
   * @param search
   * @param code
   * @return
   */
  @Override
  public Response trustedClientApiBatchDelete(final List<String> uids, final String search,
      final String code) {
    return deleteController.trustedClientApiBatchDelete(uids, search, code);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param batchId
   * @return
   */
  @Override
  public Response trustedClientApiBatchDeleteQuery(final String batchId) {
    return deleteController.trustedClientApiBatchDeleteQuery(batchId);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uids
   * @param search
   * @param code
   * @return
   */
  @Override
  public Response trustedClientApiBatchDisable(final List<String> uids, final String search,
      final String code) {
    return disableController.trustedClientApiBatchDisable(uids, search, code);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param batchId
   * @return
   */
  @Override
  public Response trustedClientApiBatchDisableQuery(final String batchId) {
    return disableController.trustedClientApiBatchDisableQuery(batchId);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uids
   * @param search
   * @param code
   * @return
   */
  @Override
  public Response trustedClientApiBatchEnable(final List<String> uids, final String search,
      final String code) {
    return enableController.trustedClientApiBatchEnable(uids, search, code);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param batchId
   * @return
   */
  @Override
  public Response trustedClientApiBatchEnableQuery(final String batchId) {
    return enableController.trustedClientApiBatchEnableQuery(batchId);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param trustedClient
   * @return
   */
  @Override
  public Response trustedClientApiCreate(TrustedClientApiDto trustedClient) {
    return createController.trustedClientApiCreate(trustedClient);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response trustedClientApiDelete(final String uid) {
    return deleteController.trustedClientApiDelete(uid);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response trustedClientApiDisable(final String uid) {
    return disableController.trustedClientApiDisable(uid);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response trustedClientApiEnable(final String uid) {
    return enableController.trustedClientApiEnable(uid);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uids
   * @param search
   * @param code
   * @param limit
   * @param sinceUid
   * @param sinceCode
   * @param order
   * @return
   */
  @Override
  public Response trustedClientApiList(final List<String> uids, final String search,
      final String code, final Integer limit, final String sinceUid, final String sinceCode,
      final String order) {
    return listController.trustedClientApiList(uids, search, code, limit, sinceUid, sinceCode,
        order);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response trustedClientApiRetrieve(final String uid) {
    return retrieveController.trustedClientApiRetrieve(uid);
  }

  /**
   * @autogenerated ApiControllerGenerator
   * @param uid
   * @param trustedClient
   * @return
   */
  @Override
  public Response trustedClientApiUpdate(final String uid, TrustedClientApiDto trustedClient) {
    return updateController.trustedClientApiUpdate(uid, trustedClient);
  }
}
