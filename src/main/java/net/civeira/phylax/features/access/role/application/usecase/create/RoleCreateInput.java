package net.civeira.phylax.features.access.role.application.usecase.create;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.role.domain.Domains;
import net.civeira.phylax.features.access.role.domain.RoleChangeSet;
import net.civeira.phylax.features.access.role.domain.valueobject.DomainsVO;
import net.civeira.phylax.features.access.role.domain.valueobject.NameVO;
import net.civeira.phylax.features.access.role.domain.valueobject.TenantVO;
import net.civeira.phylax.features.access.role.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.role.domain.valueobject.VersionVO;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;

/**
 * A dto transfer to hold role attribute values
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreateInput {

  /**
   * @autogenerated CreateInputGenerator
   */
  public static class RoleCreateInputBuilder {

    /**
     * Append Domains crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param domains The Domains value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder domains(final List<Domains> domains) {
      return domains(Optional.of(DomainsVO.from(domains)));
    }

    /**
     * Append Domains value
     *
     * @autogenerated CreateInputGenerator
     * @param domains The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder domains(final Optional<DomainsVO> domains) {
      this.domains$value = domains;
      this.domains$set = true;
      return this;
    }

    /**
     * Append Name crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param name The Name value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder name(final String name) {
      return name(Optional.of(NameVO.from(name)));
    }

    /**
     * Append Name value
     *
     * @autogenerated CreateInputGenerator
     * @param name The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder name(final Optional<NameVO> name) {
      this.name$value = name;
      this.name$set = true;
      return this;
    }

    /**
     * @autogenerated CreateInputGenerator
     * @return
     */
    public RoleCreateInputBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * Append Tenant crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param tenant The Tenant value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder tenant(final TenantRef tenant) {
      return tenant(Optional.of(TenantVO.from(tenant)));
    }

    /**
     * Append Tenant value
     *
     * @autogenerated CreateInputGenerator
     * @param tenant The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder tenant(final Optional<TenantVO> tenant) {
      this.tenant$value = tenant;
      this.tenant$set = true;
      return this;
    }

    /**
     * Append Uid crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param uid The Uid value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * Append Uid value
     *
     * @autogenerated CreateInputGenerator
     * @param uid The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * Append Version crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param version The Version value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * Append Version value
     *
     * @autogenerated CreateInputGenerator
     * @param version The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RoleCreateInputBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * @autogenerated CreateInputGenerator
   * @param input
   * @return
   */
  static RoleCreateInput fromChangeSet(final RoleChangeSet input) {
    return RoleCreateInput.builder().uid(input.getUid()).name(input.getName())
        .tenant(input.getTenant()).domains(input.getDomains()).version(input.getVersion()).build();
  }

  /**
   * El domains de role
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<DomainsVO> domains = Optional.empty();

  /**
   * The user name to identify on the login screen
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<NameVO> name = Optional.empty();

  /**
   * Domains without a tenant could be used only for admins
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<TenantVO> tenant = Optional.empty();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de role para controlar bloqueos optimistas
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Inform for a possible change propolsal in Domains
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Domains, otherwise the value for Domains
   */
  public Optional<DomainsVO> getDomains() {
    return domains;
  }

  /**
   * Inform for a possible change propolsal in Name
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Name, otherwise the value for Name
   */
  public Optional<NameVO> getName() {
    return name;
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public Optional<TenantVO> getTenant() {
    return tenant;
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public Optional<UidVO> getUid() {
    return uid;
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Optional<VersionVO> getVersion() {
    return version;
  }

  /**
   * Assigna change proposal for Domains to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for RoleCreateInput
   * @return self instance to enable a flow code
   */
  public RoleCreateInput setDomains(final List<Domains> value) {
    this.domains = Optional.of(DomainsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for Name to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for RoleCreateInput
   * @return self instance to enable a flow code
   */
  public RoleCreateInput setName(final String value) {
    this.name = Optional.of(NameVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Tenant to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for RoleCreateInput
   * @return self instance to enable a flow code
   */
  public RoleCreateInput setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for RoleCreateInput
   * @return self instance to enable a flow code
   */
  public RoleCreateInput setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for RoleCreateInput
   * @return self instance to enable a flow code
   */
  public RoleCreateInput setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * @autogenerated CreateInputGenerator
   * @return
   */
  RoleChangeSet toChangeSet() {
    return RoleChangeSet.builder().uid(uid).name(name).tenant(tenant).domains(domains)
        .version(version).build();
  }
}
