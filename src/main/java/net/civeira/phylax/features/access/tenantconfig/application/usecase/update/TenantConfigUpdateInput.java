package net.civeira.phylax.features.access.tenantconfig.application.usecase.update;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;
import net.civeira.phylax.features.access.tenantconfig.domain.TenantConfigChangeSet;
import net.civeira.phylax.features.access.tenantconfig.domain.valueobject.ForceMfaVO;
import net.civeira.phylax.features.access.tenantconfig.domain.valueobject.InnerLabelVO;
import net.civeira.phylax.features.access.tenantconfig.domain.valueobject.TenantVO;
import net.civeira.phylax.features.access.tenantconfig.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.tenantconfig.domain.valueobject.VersionVO;

/**
 * A dto transfer to hold tenant config attribute values
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantConfigUpdateInput {

  /**
   * @autogenerated UpdateInputGenerator
   */
  public static class TenantConfigUpdateInputBuilder {

    /**
     * Append ForceMfa crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param forceMfa The ForceMfa value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder forceMfa(final Boolean forceMfa) {
      return forceMfa(Optional.of(ForceMfaVO.from(forceMfa)));
    }

    /**
     * Append ForceMfa value
     *
     * @autogenerated UpdateInputGenerator
     * @param forceMfa The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder forceMfa(final Optional<ForceMfaVO> forceMfa) {
      this.forceMfa$value = forceMfa;
      this.forceMfa$set = true;
      return this;
    }

    /**
     * Append InnerLabel crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param innerLabel The InnerLabel value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder innerLabel(final String innerLabel) {
      return innerLabel(Optional.of(InnerLabelVO.from(innerLabel)));
    }

    /**
     * Append InnerLabel value
     *
     * @autogenerated UpdateInputGenerator
     * @param innerLabel The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder innerLabel(final Optional<InnerLabelVO> innerLabel) {
      this.innerLabel$value = innerLabel;
      this.innerLabel$set = true;
      return this;
    }

    /**
     * @autogenerated UpdateInputGenerator
     * @return
     */
    public TenantConfigUpdateInputBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * Append Tenant crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param tenant The Tenant value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder tenant(final TenantRef tenant) {
      return tenant(Optional.of(TenantVO.from(tenant)));
    }

    /**
     * Append Tenant value
     *
     * @autogenerated UpdateInputGenerator
     * @param tenant The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder tenant(final Optional<TenantVO> tenant) {
      this.tenant$value = tenant;
      this.tenant$set = true;
      return this;
    }

    /**
     * Append Uid crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param uid The Uid value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * Append Uid value
     *
     * @autogenerated UpdateInputGenerator
     * @param uid The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * Append Version crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param version The Version value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * Append Version value
     *
     * @autogenerated UpdateInputGenerator
     * @param version The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantConfigUpdateInputBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * @autogenerated UpdateInputGenerator
   * @param input
   * @return
   */
  static TenantConfigUpdateInput fromChangeSet(final TenantConfigChangeSet input) {
    return TenantConfigUpdateInput.builder().uid(input.getUid()).tenant(input.getTenant())
        .innerLabel(input.getInnerLabel()).forceMfa(input.getForceMfa()).version(input.getVersion())
        .build();
  }

  /**
   * Indicates if Multi-Factor Authentication (MFA) is enforced for the tenant.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<ForceMfaVO> forceMfa = Optional.empty();

  /**
   * A customizable label that tenants can use to personalize the application UI.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<InnerLabelVO> innerLabel = Optional.empty();

  /**
   * The tenant this configuration applies to.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<TenantVO> tenant = Optional.empty();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de tenant config para controlar bloqueos optimistas
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Inform for a possible change propolsal in ForceMfa
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for ForceMfa, otherwise the value for ForceMfa
   */
  public Optional<ForceMfaVO> getForceMfa() {
    return forceMfa;
  }

  /**
   * Inform for a possible change propolsal in InnerLabel
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for InnerLabel, otherwise the value for InnerLabel
   */
  public Optional<InnerLabelVO> getInnerLabel() {
    return innerLabel;
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public Optional<TenantVO> getTenant() {
    return tenant;
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public Optional<UidVO> getUid() {
    return uid;
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Optional<VersionVO> getVersion() {
    return version;
  }

  /**
   * Assigna change proposal for ForceMfa to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantConfigUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantConfigUpdateInput setForceMfa(final Boolean value) {
    this.forceMfa = Optional.of(ForceMfaVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for InnerLabel to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantConfigUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantConfigUpdateInput setInnerLabel(final String value) {
    this.innerLabel = Optional.of(InnerLabelVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Tenant to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantConfigUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantConfigUpdateInput setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantConfigUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantConfigUpdateInput setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantConfigUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantConfigUpdateInput setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * @autogenerated UpdateInputGenerator
   * @return
   */
  TenantConfigChangeSet toChangeSet() {
    return TenantConfigChangeSet.builder().uid(uid).tenant(tenant).innerLabel(innerLabel)
        .forceMfa(forceMfa).version(version).build();
  }
}
