package net.civeira.phylax.features.access.securityscope.infrastructure.driver.rest;

import java.util.List;
import java.util.stream.Stream;

import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.batch.BatchIdentificator;
import net.civeira.phylax.common.batch.BatchProgress;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.features.access.relyingparty.domain.RelyingPartyReference;
import net.civeira.phylax.features.access.securityscope.application.usecase.enable.SecurityScopeEnableFilter;
import net.civeira.phylax.features.access.securityscope.application.usecase.enable.SecurityScopeEnableProjection;
import net.civeira.phylax.features.access.securityscope.application.usecase.enable.SecurityScopeEnableStatus;
import net.civeira.phylax.features.access.securityscope.application.usecase.enable.SecurityScopeEnableUsecase;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScopeKindOptions;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScopeReference;
import net.civeira.phylax.features.access.securityscope.domain.SecurityScopeVisibilityOptions;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientReference;
import net.civeira.phylax.generated.openapi.model.RelyingPartyApiRef;
import net.civeira.phylax.generated.openapi.model.SecurityScopeApiDto;
import net.civeira.phylax.generated.openapi.model.SecurityScopeApiDto.KindEnum;
import net.civeira.phylax.generated.openapi.model.SecurityScopeApiDto.VisibilityEnum;
import net.civeira.phylax.generated.openapi.model.TrustedClientApiRef;

@RequiredArgsConstructor
@RequestScoped
public class SecurityScopeEnableController {

  /**
   * @autogenerated ActionControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated ActionControllerGenerator
   */
  private final SecurityScopeEnableUsecase enable;

  /**
   * @autogenerated ActionControllerGenerator
   * @param uids
   * @param search
   * @param resource
   * @param trustedClient
   * @param trustedClients
   * @param relyingParty
   * @param relyingPartys
   * @return
   */
  public Response securityScopeApiBatchEnable(final List<String> uids, final String search,
      final String resource, final String trustedClient, final List<String> trustedClients,
      final String relyingParty, final List<String> relyingPartys) {
    SecurityScopeEnableFilter.SecurityScopeEnableFilterBuilder filterBuilder =
        SecurityScopeEnableFilter.builder();
    filterBuilder =
        filterBuilder.uids(uids.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    filterBuilder = filterBuilder.search(search);
    filterBuilder =
        filterBuilder.uids(uids.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    filterBuilder = filterBuilder.search(search);
    filterBuilder = filterBuilder.resource(resource);
    if (null != trustedClient) {
      filterBuilder = filterBuilder.trustedClient(TrustedClientReference.of(trustedClient));
    }
    filterBuilder = filterBuilder.trustedClients(
        trustedClients.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    if (null != relyingParty) {
      filterBuilder = filterBuilder.relyingParty(RelyingPartyReference.of(relyingParty));
    }
    filterBuilder = filterBuilder
        .relyingPartys(relyingPartys.stream().flatMap(part -> Stream.of(part.split(","))).toList());
    SecurityScopeEnableFilter filter = filterBuilder.build();
    BatchIdentificator task = enable.enable(currentRequest.interaction(), filter);
    /* .header("Last-Modified", value.getSince().format(DateTimeFormatter.RFC_1123_DATE_TIME)) */
    return Response.ok(task).build();
  }

  /**
   * @autogenerated ActionControllerGenerator
   * @param batchId
   * @return
   */
  public Response securityScopeApiBatchEnableQuery(final String batchId) {
    BatchProgress task = enable.checkProgress(
        SecurityScopeEnableStatus.builder().taskId(batchId).build(currentRequest.interaction()));
    return Response.ok(task).build();
  }

  /**
   * @autogenerated ActionControllerGenerator
   * @param uid
   * @return
   */
  @Transactional
  public Response securityScopeApiEnable(final String uid) {
    SecurityScopeEnableProjection updated =
        enable.enable(currentRequest.interaction(), SecurityScopeReference.of(uid));
    return Response.ok(toApiModel(updated)).build();
  }

  /**
   * @autogenerated ActionControllerGenerator
   * @param domainEnum
   * @return
   */
  private KindEnum kindEnumToApi(SecurityScopeKindOptions domainEnum) {
    KindEnum result;
    if (domainEnum == SecurityScopeKindOptions.READ) {
      result = KindEnum.READ;
    } else if (domainEnum == SecurityScopeKindOptions.WRITE) {
      result = KindEnum.WRITE;
    } else if (domainEnum == SecurityScopeKindOptions.MANAGE) {
      result = KindEnum.MANAGE;
    } else {
      result = null;
    }
    return result;
  }

  /**
   * @autogenerated ActionControllerGenerator
   * @param dto
   * @return
   */
  private SecurityScopeApiDto toApiModel(SecurityScopeEnableProjection dto) {
    SecurityScopeApiDto securityScopeApiDto = new SecurityScopeApiDto();
    securityScopeApiDto.setUid(dto.getUid());
    securityScopeApiDto
        .setTrustedClient(new TrustedClientApiRef().$ref(dto.getTrustedClientReference()));
    securityScopeApiDto
        .setRelyingParty(new RelyingPartyApiRef().$ref(dto.getRelyingPartyReference()));
    securityScopeApiDto.setResource(dto.getResource());
    securityScopeApiDto.setScope(dto.getScope());
    securityScopeApiDto.setEnabled(dto.getEnabled());
    securityScopeApiDto.setKind(kindEnumToApi(dto.getKind()));
    securityScopeApiDto.setVisibility(visibilityEnumToApi(dto.getVisibility()));
    securityScopeApiDto.setVersion(dto.getVersion());
    return securityScopeApiDto;
  }

  /**
   * @autogenerated ActionControllerGenerator
   * @param domainEnum
   * @return
   */
  private VisibilityEnum visibilityEnumToApi(SecurityScopeVisibilityOptions domainEnum) {
    VisibilityEnum result;
    if (domainEnum == SecurityScopeVisibilityOptions.PUBLIC) {
      result = VisibilityEnum.PUBLIC;
    } else if (domainEnum == SecurityScopeVisibilityOptions.AUTHORIZED) {
      result = VisibilityEnum.AUTHORIZED;
    } else if (domainEnum == SecurityScopeVisibilityOptions.EXPLICIT) {
      result = VisibilityEnum.EXPLICIT;
    } else {
      result = null;
    }
    return result;
  }
}
