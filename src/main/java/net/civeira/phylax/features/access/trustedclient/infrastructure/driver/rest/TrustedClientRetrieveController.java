package net.civeira.phylax.features.access.trustedclient.infrastructure.driver.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.trustedclient.application.usecase.retrieve.TrustedClientRetrieveProjection;
import net.civeira.phylax.features.access.trustedclient.application.usecase.retrieve.TrustedClientRetrieveUsecase;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientReference;
import net.civeira.phylax.generated.openapi.model.TrustedClientApiDto;

@RequiredArgsConstructor
@RequestScoped
public class TrustedClientRetrieveController {

  /**
   * @autogenerated RetrieveControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated RetrieveControllerGenerator
   */
  private final TrustedClientRetrieveUsecase retrieve;

  /**
   * @autogenerated RetrieveControllerGenerator
   * @param uid
   * @return
   */
  public Response trustedClientApiRetrieve(final String uid) {
    TrustedClientRetrieveProjection retrieved =
        retrieve.retrieve(currentRequest.interaction(), TrustedClientReference.of(uid));
    /* .header("Last-Modified", value.getSince().format(DateTimeFormatter.RFC_1123_DATE_TIME))) */
    return Response.ok(toApiModel(retrieved)).build();
  }

  /**
   * @autogenerated RetrieveControllerGenerator
   * @param dto
   * @return
   */
  private TrustedClientApiDto toApiModel(TrustedClientRetrieveProjection dto) {
    TrustedClientApiDto trustedClientApiDto = new TrustedClientApiDto();
    trustedClientApiDto.setUid(dto.getUid());
    trustedClientApiDto.setCode(dto.getCode());
    trustedClientApiDto.setPublicAllow(dto.getPublicAllow());
    trustedClientApiDto.setSecretOauth("*****");
    trustedClientApiDto.setAllowedRedirects(dto.getAllowedRedirects());
    trustedClientApiDto.setEnabled(dto.getEnabled());
    trustedClientApiDto.setVersion(dto.getVersion());
    return trustedClientApiDto;
  }
}
