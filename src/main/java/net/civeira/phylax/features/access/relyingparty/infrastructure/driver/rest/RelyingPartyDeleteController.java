package net.civeira.phylax.features.access.relyingparty.infrastructure.driver.rest;

import java.util.List;
import java.util.stream.Stream;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.batch.BatchIdentificator;
import net.civeira.phylax.common.batch.BatchProgress;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.relyingparty.application.usecase.delete.RelyingPartyCheckBatchDeleteStatus;
import net.civeira.phylax.features.access.relyingparty.application.usecase.delete.RelyingPartyDeleteFilter;
import net.civeira.phylax.features.access.relyingparty.application.usecase.delete.RelyingPartyDeleteUsecase;
import net.civeira.phylax.features.access.relyingparty.domain.RelyingPartyReference;

@RequiredArgsConstructor
@RequestScoped
public class RelyingPartyDeleteController {

  /**
   * @autogenerated DeleteControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated DeleteControllerGenerator
   */
  private final RelyingPartyDeleteUsecase delete;

  /**
   * @autogenerated DeleteControllerGenerator
   * @param uids
   * @param search
   * @param apiKey
   * @param code
   * @return
   */
  public Response relyingPartyApiBatchDelete(final List<String> uids, final String search,
      final String apiKey, final String code) {
    RelyingPartyDeleteFilter.RelyingPartyDeleteFilterBuilder filterBuilder =
        RelyingPartyDeleteFilter.builder();
    filterBuilder =
        filterBuilder.uids(uids.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    filterBuilder = filterBuilder.search(search);
    filterBuilder =
        filterBuilder.uids(uids.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    filterBuilder = filterBuilder.search(search);
    filterBuilder = filterBuilder.apiKey(apiKey);
    filterBuilder = filterBuilder.code(code);
    RelyingPartyDeleteFilter filter = filterBuilder.build();
    BatchIdentificator task = delete.delete(currentRequest.interaction(), filter);
    /* .header("Last-Modified", value.getSince().format(DateTimeFormatter.RFC_1123_DATE_TIME)) */
    return Response.ok(task).build();
  }

  /**
   * @autogenerated DeleteControllerGenerator
   * @param batchId
   * @return
   */
  public Response relyingPartyApiBatchDeleteQuery(final String batchId) {
    BatchProgress task = delete.checkProgress(RelyingPartyCheckBatchDeleteStatus.builder()
        .taskId(batchId).build(currentRequest.interaction()));
    return Response.ok(task).build();
  }

  /**
   * @autogenerated DeleteControllerGenerator
   * @param uid
   * @return
   */
  @Transactional
  public Response relyingPartyApiDelete(final String uid) {
    delete.delete(currentRequest.interaction(), RelyingPartyReference.of(uid));
    return Response.noContent().build();
  }
}
