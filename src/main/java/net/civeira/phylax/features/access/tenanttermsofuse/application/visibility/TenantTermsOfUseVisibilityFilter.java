package net.civeira.phylax.features.access.tenanttermsofuse.application.visibility;

import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantTermsOfUseVisibilityFilter {

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated VisibilityFilterGenerator
   */
  private String search;

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private TenantRef tenant;

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private String tenantTenantAccesible;

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private List<String> tenants;

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated VisibilityFilterGenerator
   */
  private String uid;

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated VisibilityFilterGenerator
   */
  private List<String> uids;

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<String> getSearch() {
    return Optional.ofNullable(search);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<TenantRef> getTenant() {
    return Optional.ofNullable(tenant);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<String> getTenantTenantAccesible() {
    return Optional.ofNullable(tenantTenantAccesible);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public List<String> getTenants() {
    return null == tenants ? List.of() : List.copyOf(tenants);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<String> getUid() {
    return Optional.ofNullable(uid);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public List<String> getUids() {
    return null == uids ? List.of() : List.copyOf(uids);
  }
}
