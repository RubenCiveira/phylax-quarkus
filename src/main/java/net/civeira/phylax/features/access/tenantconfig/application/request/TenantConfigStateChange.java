package net.civeira.phylax.features.access.tenantconfig.application.request;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.tenant.TenantRef;
import net.civeira.phylax.features.access.tenantconfig.command.TenantConfigWriteAttributes;
import net.civeira.phylax.features.access.tenantconfig.valueobject.ForceMfaVO;
import net.civeira.phylax.features.access.tenantconfig.valueobject.InnerLabelVO;
import net.civeira.phylax.features.access.tenantconfig.valueobject.TenantVO;
import net.civeira.phylax.features.access.tenantconfig.valueobject.UidVO;
import net.civeira.phylax.features.access.tenantconfig.valueobject.VersionVO;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantConfigStateChange implements TenantConfigWriteAttributes {

  /**
   * @autogenerated EntityStateChangeGenerator
   */
  public static class TenantConfigStateChangeBuilder {

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param forceMfa
     * @return
     */
    public TenantConfigStateChangeBuilder forceMfa(final Boolean forceMfa) {
      return forceMfa(Optional.of(ForceMfaVO.from(forceMfa)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param forceMfa
     * @return
     */
    public TenantConfigStateChangeBuilder forceMfa(final Optional<ForceMfaVO> forceMfa) {
      this.forceMfa$value = forceMfa;
      this.forceMfa$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param innerLabel
     * @return
     */
    public TenantConfigStateChangeBuilder innerLabel(final String innerLabel) {
      return innerLabel(Optional.of(InnerLabelVO.from(innerLabel)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param innerLabel
     * @return
     */
    public TenantConfigStateChangeBuilder innerLabel(final Optional<InnerLabelVO> innerLabel) {
      this.innerLabel$value = innerLabel;
      this.innerLabel$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @return
     */
    public TenantConfigStateChangeBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param tenant
     * @return
     */
    public TenantConfigStateChangeBuilder tenant(final TenantRef tenant) {
      return tenant(Optional.of(TenantVO.from(tenant)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param tenant
     * @return
     */
    public TenantConfigStateChangeBuilder tenant(final Optional<TenantVO> tenant) {
      this.tenant$value = tenant;
      this.tenant$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public TenantConfigStateChangeBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public TenantConfigStateChangeBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public TenantConfigStateChangeBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public TenantConfigStateChangeBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * Indicates if Multi-Factor Authentication (MFA) is enforced for the tenant.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<ForceMfaVO> forceMfa = Optional.empty();

  /**
   * A customizable label that tenants can use to personalize the application UI.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<InnerLabelVO> innerLabel = Optional.empty();

  /**
   * The tenant this configuration applies to.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<TenantVO> tenant = Optional.empty();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de tenant config para controlar bloqueos optimistas
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param attributes
   */
  public TenantConfigStateChange(final TenantConfigWriteAttributes attributes) {
    uid = attributes.getUid();
    tenant = attributes.getTenant();
    innerLabel = attributes.getInnerLabel();
    forceMfa = attributes.getForceMfa();
    version = attributes.getVersion();
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<ForceMfaVO> getForceMfa() {
    return forceMfa;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<InnerLabelVO> getInnerLabel() {
    return innerLabel;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<TenantVO> getTenant() {
    return tenant;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<UidVO> getUid() {
    return uid;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<VersionVO> getVersion() {
    return version;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantConfigStateChange setForceMfa(final Boolean value) {
    this.forceMfa = Optional.of(ForceMfaVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantConfigStateChange setInnerLabel(final String value) {
    this.innerLabel = Optional.of(InnerLabelVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantConfigStateChange setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantConfigStateChange setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantConfigStateChange setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param field
   */
  public void unset(final String field) {
    if ("uid".equals(field)) {
      this.unsetUid();
    }
    if ("tenant".equals(field)) {
      this.unsetTenant();
    }
    if ("innerLabel".equals(field)) {
      this.unsetInnerLabel();
    }
    if ("forceMfa".equals(field)) {
      this.unsetForceMfa();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantConfigStateChange unsetForceMfa() {
    this.forceMfa = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantConfigStateChange unsetInnerLabel() {
    this.innerLabel = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantConfigStateChange unsetTenant() {
    this.tenant = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantConfigStateChange unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantConfigStateChange unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
