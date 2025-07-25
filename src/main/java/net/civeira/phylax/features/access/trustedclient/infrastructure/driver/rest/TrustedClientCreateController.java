package net.civeira.phylax.features.access.trustedclient.infrastructure.driver.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.trustedclient.application.usecase.create.TrustedClientCreateInput;
import net.civeira.phylax.features.access.trustedclient.application.usecase.create.TrustedClientCreateProjection;
import net.civeira.phylax.features.access.trustedclient.application.usecase.create.TrustedClientCreateUsecase;
import net.civeira.phylax.generated.openapi.model.TrustedClientApiDto;

@RequiredArgsConstructor
@RequestScoped
public class TrustedClientCreateController {

  /**
   * @autogenerated CreateControllerGenerator
   */
  private final TrustedClientCreateUsecase create;

  /**
   * @autogenerated CreateControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated CreateControllerGenerator
   * @param trustedClient
   * @return
   */
  @Transactional
  public Response trustedClientApiCreate(TrustedClientApiDto trustedClient) {
    TrustedClientCreateProjection created =
        create.create(currentRequest.interaction(), toDomainModel(trustedClient));
    return Response.ok(toApiModel(created)).build();
  }

  /**
   * @autogenerated CreateControllerGenerator
   * @param dto
   * @return
   */
  private TrustedClientApiDto toApiModel(TrustedClientCreateProjection dto) {
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

  /**
   * @autogenerated CreateControllerGenerator
   * @param trustedClientApiDto
   * @return
   */
  private TrustedClientCreateInput toDomainModel(TrustedClientApiDto trustedClientApiDto) {
    TrustedClientCreateInput dto = new TrustedClientCreateInput();
    if (null != trustedClientApiDto.getUid()) {
      dto.setUid(trustedClientApiDto.getUid());
    }
    if (null != trustedClientApiDto.getCode()) {
      dto.setCode(trustedClientApiDto.getCode());
    }
    if (null != trustedClientApiDto.getPublicAllow()) {
      dto.setPublicAllow(trustedClientApiDto.getPublicAllow());
    }
    if (null != trustedClientApiDto.getSecretOauth()
        && !"*****".equals(trustedClientApiDto.getSecretOauth())) {
      dto.setSecretOauth(trustedClientApiDto.getSecretOauth());
    }
    if (null != trustedClientApiDto.getAllowedRedirects()) {
      dto.setAllowedRedirects(trustedClientApiDto.getAllowedRedirects());
    }
    if (null != trustedClientApiDto.getEnabled()) {
      dto.setEnabled(trustedClientApiDto.getEnabled());
    }
    if (null != trustedClientApiDto.getVersion()) {
      dto.setVersion(trustedClientApiDto.getVersion());
    }
    return dto;
  }
}
