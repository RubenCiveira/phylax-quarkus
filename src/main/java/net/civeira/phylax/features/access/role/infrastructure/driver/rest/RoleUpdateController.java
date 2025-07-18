package net.civeira.phylax.features.access.role.infrastructure.driver.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.role.application.usecase.update.RoleUpdateInput;
import net.civeira.phylax.features.access.role.application.usecase.update.RoleUpdateProjection;
import net.civeira.phylax.features.access.role.application.usecase.update.RoleUpdateUsecase;
import net.civeira.phylax.features.access.role.domain.Domains;
import net.civeira.phylax.features.access.role.domain.Domains.DomainsBuilder;
import net.civeira.phylax.features.access.role.domain.RoleReference;
import net.civeira.phylax.features.access.securitydomain.domain.SecurityDomainReference;
import net.civeira.phylax.features.access.tenant.domain.TenantReference;
import net.civeira.phylax.generated.openapi.model.DomainsApiDto;
import net.civeira.phylax.generated.openapi.model.RoleApiDto;
import net.civeira.phylax.generated.openapi.model.SecurityDomainApiRef;
import net.civeira.phylax.generated.openapi.model.TenantApiRef;

@RequiredArgsConstructor
@RequestScoped
public class RoleUpdateController {

  /**
   * @autogenerated UpdateControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated UpdateControllerGenerator
   */
  private final RoleUpdateUsecase update;

  /**
   * @autogenerated UpdateControllerGenerator
   * @param uid
   * @param role
   * @return
   */
  @Transactional
  public Response roleApiUpdate(final String uid, final RoleApiDto role) {
    RoleUpdateProjection updated =
        update.update(currentRequest.interaction(), RoleReference.of(uid), toDomainModel(role));
    return Response.ok(toApiModel(updated)).build();
  }

  /**
   * @autogenerated UpdateControllerGenerator
   * @param dto
   * @return
   */
  private RoleApiDto toApiModel(RoleUpdateProjection dto) {
    RoleApiDto roleApiDto = new RoleApiDto();
    roleApiDto.setUid(dto.getUid());
    roleApiDto.setName(dto.getName());
    roleApiDto.setTenant(new TenantApiRef().$ref(dto.getTenantReference()));
    roleApiDto.setDomains(dto.getDomains().stream().map(this::toApiModelDomains).toList());
    roleApiDto.setVersion(dto.getVersion());
    return roleApiDto;
  }

  /**
   * @autogenerated UpdateControllerGenerator
   * @param dto
   * @return
   */
  private DomainsApiDto toApiModelDomains(Domains dto) {
    DomainsApiDto domainsApiDto = new DomainsApiDto();
    domainsApiDto.setUid(dto.getUid());
    domainsApiDto.setSecurityDomain(new SecurityDomainApiRef().$ref(dto.getSecurityDomainUid()));
    domainsApiDto.setVersion(dto.getVersion().orElse(null));
    return domainsApiDto;
  }

  /**
   * @autogenerated UpdateControllerGenerator
   * @param roleApiDto
   * @return
   */
  private RoleUpdateInput toDomainModel(RoleApiDto roleApiDto) {
    RoleUpdateInput dto = new RoleUpdateInput();
    if (null != roleApiDto.getUid()) {
      dto.setUid(roleApiDto.getUid());
    }
    if (null != roleApiDto.getName()) {
      dto.setName(roleApiDto.getName());
    }
    if (null != roleApiDto.getTenant()) {
      dto.setTenant(TenantReference.of(roleApiDto.getTenant().get$Ref()));
    }
    if (null != roleApiDto.getDomains()) {
      dto.setDomains(roleApiDto.getDomains().stream().map(this::toDomainModelDomains).toList());
    }
    if (null != roleApiDto.getVersion()) {
      dto.setVersion(roleApiDto.getVersion());
    }
    return dto;
  }

  /**
   * @autogenerated UpdateControllerGenerator
   * @param domainsApiDto
   * @return
   */
  private Domains toDomainModelDomains(DomainsApiDto domainsApiDto) {
    DomainsBuilder builder = Domains.builder();
    if (null != domainsApiDto.getUid()) {
      builder = builder.uid(domainsApiDto.getUid());
    }
    if (null != domainsApiDto.getSecurityDomain()) {
      builder = builder
          .securityDomain(SecurityDomainReference.of(domainsApiDto.getSecurityDomain().get$Ref()));
    }
    if (null != domainsApiDto.getVersion()) {
      builder = builder.version(domainsApiDto.getVersion());
    }
    return builder.build();
  }
}
