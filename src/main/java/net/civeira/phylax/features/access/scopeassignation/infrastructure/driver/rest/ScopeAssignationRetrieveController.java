package net.civeira.phylax.features.access.scopeassignation.infrastructure.driver.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.scopeassignation.application.usecase.retrieve.ScopeAssignationRetrieveProjection;
import net.civeira.phylax.features.access.scopeassignation.application.usecase.retrieve.ScopeAssignationRetrieveUsecase;
import net.civeira.phylax.features.access.scopeassignation.domain.ScopeAssignationReference;
import net.civeira.phylax.generated.openapi.model.ScopeAssignationApiDto;
import net.civeira.phylax.generated.openapi.model.SecurityDomainApiRef;
import net.civeira.phylax.generated.openapi.model.SecurityScopeApiRef;

@RequiredArgsConstructor
@RequestScoped
public class ScopeAssignationRetrieveController {

  /**
   * @autogenerated RetrieveControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated RetrieveControllerGenerator
   */
  private final ScopeAssignationRetrieveUsecase retrieve;

  /**
   * @autogenerated RetrieveControllerGenerator
   * @param uid
   * @return
   */
  public Response scopeAssignationApiRetrieve(final String uid) {
    ScopeAssignationRetrieveProjection retrieved =
        retrieve.retrieve(currentRequest.interaction(), ScopeAssignationReference.of(uid));
    /* .header("Last-Modified", value.getSince().format(DateTimeFormatter.RFC_1123_DATE_TIME))) */
    return Response.ok(toApiModel(retrieved)).build();
  }

  /**
   * @autogenerated RetrieveControllerGenerator
   * @param dto
   * @return
   */
  private ScopeAssignationApiDto toApiModel(ScopeAssignationRetrieveProjection dto) {
    ScopeAssignationApiDto scopeAssignationApiDto = new ScopeAssignationApiDto();
    scopeAssignationApiDto.setUid(dto.getUid());
    scopeAssignationApiDto
        .setSecurityDomain(new SecurityDomainApiRef().$ref(dto.getSecurityDomainReference()));
    scopeAssignationApiDto
        .setSecurityScope(new SecurityScopeApiRef().$ref(dto.getSecurityScopeReference()));
    scopeAssignationApiDto.setVersion(dto.getVersion());
    return scopeAssignationApiDto;
  }
}
