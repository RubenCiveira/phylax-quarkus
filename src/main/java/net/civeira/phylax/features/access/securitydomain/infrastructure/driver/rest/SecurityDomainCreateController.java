package net.civeira.phylax.features.access.securitydomain.infrastructure.driver.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.securitydomain.application.usecase.create.SecurityDomainCreateInput;
import net.civeira.phylax.features.access.securitydomain.application.usecase.create.SecurityDomainCreateProjection;
import net.civeira.phylax.features.access.securitydomain.application.usecase.create.SecurityDomainCreateUsecase;
import net.civeira.phylax.generated.openapi.model.SecurityDomainApiDto;

@RequiredArgsConstructor
@RequestScoped
public class SecurityDomainCreateController {

  /**
   * @autogenerated CreateControllerGenerator
   */
  private final SecurityDomainCreateUsecase create;

  /**
   * @autogenerated CreateControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated CreateControllerGenerator
   * @param securityDomain
   * @return
   */
  @Transactional
  public Response securityDomainApiCreate(SecurityDomainApiDto securityDomain) {
    SecurityDomainCreateProjection created =
        create.create(currentRequest.interaction(), toDomainModel(securityDomain));
    return Response.ok(toApiModel(created)).build();
  }

  /**
   * @autogenerated CreateControllerGenerator
   * @param dto
   * @return
   */
  private SecurityDomainApiDto toApiModel(SecurityDomainCreateProjection dto) {
    SecurityDomainApiDto securityDomainApiDto = new SecurityDomainApiDto();
    securityDomainApiDto.setUid(dto.getUid());
    securityDomainApiDto.setName(dto.getName());
    securityDomainApiDto.setLevel(dto.getLevel());
    securityDomainApiDto.setReadAll(dto.getReadAll());
    securityDomainApiDto.setWriteAll(dto.getWriteAll());
    securityDomainApiDto.setManageAll(dto.getManageAll());
    securityDomainApiDto.setEnabled(dto.getEnabled());
    securityDomainApiDto.setVersion(dto.getVersion());
    return securityDomainApiDto;
  }

  /**
   * @autogenerated CreateControllerGenerator
   * @param securityDomainApiDto
   * @return
   */
  private SecurityDomainCreateInput toDomainModel(SecurityDomainApiDto securityDomainApiDto) {
    SecurityDomainCreateInput dto = new SecurityDomainCreateInput();
    if (null != securityDomainApiDto.getUid()) {
      dto.setUid(securityDomainApiDto.getUid());
    }
    if (null != securityDomainApiDto.getName()) {
      dto.setName(securityDomainApiDto.getName());
    }
    if (null != securityDomainApiDto.getLevel()) {
      dto.setLevel(securityDomainApiDto.getLevel());
    }
    if (null != securityDomainApiDto.getReadAll()) {
      dto.setReadAll(securityDomainApiDto.getReadAll());
    }
    if (null != securityDomainApiDto.getWriteAll()) {
      dto.setWriteAll(securityDomainApiDto.getWriteAll());
    }
    if (null != securityDomainApiDto.getManageAll()) {
      dto.setManageAll(securityDomainApiDto.getManageAll());
    }
    if (null != securityDomainApiDto.getEnabled()) {
      dto.setEnabled(securityDomainApiDto.getEnabled());
    }
    if (null != securityDomainApiDto.getVersion()) {
      dto.setVersion(securityDomainApiDto.getVersion());
    }
    return dto;
  }
}
