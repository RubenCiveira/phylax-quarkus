package net.civeira.phylax.features.access.securitydomain.infrastructure.driver.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.securitydomain.application.usecase.retrieve.SecurityDomainRetrieveProjection;
import net.civeira.phylax.features.access.securitydomain.application.usecase.retrieve.SecurityDomainRetrieveUsecase;
import net.civeira.phylax.features.access.securitydomain.domain.SecurityDomainReference;
import net.civeira.phylax.generated.openapi.model.SecurityDomainApiDto;

@RequiredArgsConstructor
@RequestScoped
public class SecurityDomainRetrieveController {

  /**
   * @autogenerated RetrieveControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated RetrieveControllerGenerator
   */
  private final SecurityDomainRetrieveUsecase retrieve;

  /**
   * @autogenerated RetrieveControllerGenerator
   * @param uid
   * @return
   */
  public Response securityDomainApiRetrieve(final String uid) {
    SecurityDomainRetrieveProjection retrieved =
        retrieve.retrieve(currentRequest.interaction(), SecurityDomainReference.of(uid));
    /* .header("Last-Modified", value.getSince().format(DateTimeFormatter.RFC_1123_DATE_TIME))) */
    return Response.ok(toApiModel(retrieved)).build();
  }

  /**
   * @autogenerated RetrieveControllerGenerator
   * @param dto
   * @return
   */
  private SecurityDomainApiDto toApiModel(SecurityDomainRetrieveProjection dto) {
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
}
