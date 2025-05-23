package net.civeira.phylax.features.access.tenant.application.request;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.tenant.RelingParties;
import net.civeira.phylax.features.access.tenant.TrustedClients;
import net.civeira.phylax.features.access.tenant.command.TenantWriteAttributes;
import net.civeira.phylax.features.access.tenant.valueobject.AccessToAllApplicationsVO;
import net.civeira.phylax.features.access.tenant.valueobject.DomainVO;
import net.civeira.phylax.features.access.tenant.valueobject.EnabledVO;
import net.civeira.phylax.features.access.tenant.valueobject.NameVO;
import net.civeira.phylax.features.access.tenant.valueobject.RelingPartiesVO;
import net.civeira.phylax.features.access.tenant.valueobject.TrustedClientsVO;
import net.civeira.phylax.features.access.tenant.valueobject.UidVO;
import net.civeira.phylax.features.access.tenant.valueobject.VersionVO;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantStateChange implements TenantWriteAttributes {

  /**
   * @autogenerated EntityStateChangeGenerator
   */
  public static class TenantStateChangeBuilder {

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param accessToAllApplications
     * @return
     */
    public TenantStateChangeBuilder accessToAllApplications(final Boolean accessToAllApplications) {
      return accessToAllApplications(
          Optional.of(AccessToAllApplicationsVO.from(accessToAllApplications)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param accessToAllApplications
     * @return
     */
    public TenantStateChangeBuilder accessToAllApplications(
        final Optional<AccessToAllApplicationsVO> accessToAllApplications) {
      this.accessToAllApplications$value = accessToAllApplications;
      this.accessToAllApplications$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param domain
     * @return
     */
    public TenantStateChangeBuilder domain(final String domain) {
      return domain(Optional.of(DomainVO.from(domain)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param domain
     * @return
     */
    public TenantStateChangeBuilder domain(final Optional<DomainVO> domain) {
      this.domain$value = domain;
      this.domain$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param enabled
     * @return
     */
    public TenantStateChangeBuilder enabled(final Boolean enabled) {
      return enabled(Optional.of(EnabledVO.from(enabled)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param enabled
     * @return
     */
    public TenantStateChangeBuilder enabled(final Optional<EnabledVO> enabled) {
      this.enabled$value = enabled;
      this.enabled$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param name
     * @return
     */
    public TenantStateChangeBuilder name(final String name) {
      return name(Optional.of(NameVO.from(name)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param name
     * @return
     */
    public TenantStateChangeBuilder name(final Optional<NameVO> name) {
      this.name$value = name;
      this.name$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @return
     */
    public TenantStateChangeBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param relingParties
     * @return
     */
    public TenantStateChangeBuilder relingParties(final List<RelingParties> relingParties) {
      return relingParties(Optional.of(RelingPartiesVO.from(relingParties)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param relingParties
     * @return
     */
    public TenantStateChangeBuilder relingParties(final Optional<RelingPartiesVO> relingParties) {
      this.relingParties$value = relingParties;
      this.relingParties$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param trustedClients
     * @return
     */
    public TenantStateChangeBuilder trustedClients(final List<TrustedClients> trustedClients) {
      return trustedClients(Optional.of(TrustedClientsVO.from(trustedClients)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param trustedClients
     * @return
     */
    public TenantStateChangeBuilder trustedClients(
        final Optional<TrustedClientsVO> trustedClients) {
      this.trustedClients$value = trustedClients;
      this.trustedClients$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public TenantStateChangeBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public TenantStateChangeBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public TenantStateChangeBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public TenantStateChangeBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * Defines if users from this tenant can access all parties and clients, or only those explicitly
   * assigned.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<AccessToAllApplicationsVO> accessToAllApplications = Optional.empty();

  /**
   * A domain suffix to identify the account
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<DomainVO> domain = Optional.empty();

  /**
   * The users of a non enabled tenant could not make login on the system
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<EnabledVO> enabled = Optional.empty();

  /**
   * The name to identify the account.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<NameVO> name = Optional.empty();

  /**
   * A list of relying parties (services or applications) associated with this tenant.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<RelingPartiesVO> relingParties = Optional.empty();

  /**
   * A list of OAuth clients marked as trusted for this tenant.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<TrustedClientsVO> trustedClients = Optional.empty();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de tenant para controlar bloqueos optimistas
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param attributes
   */
  public TenantStateChange(final TenantWriteAttributes attributes) {
    uid = attributes.getUid();
    name = attributes.getName();
    domain = attributes.getDomain();
    enabled = attributes.getEnabled();
    accessToAllApplications = attributes.getAccessToAllApplications();
    trustedClients = attributes.getTrustedClients();
    relingParties = attributes.getRelingParties();
    version = attributes.getVersion();
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<AccessToAllApplicationsVO> getAccessToAllApplications() {
    return accessToAllApplications;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<DomainVO> getDomain() {
    return domain;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<EnabledVO> getEnabled() {
    return enabled;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<NameVO> getName() {
    return name;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<RelingPartiesVO> getRelingParties() {
    return relingParties;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<TrustedClientsVO> getTrustedClients() {
    return trustedClients;
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
  public TenantStateChange setAccessToAllApplications(final Boolean value) {
    this.accessToAllApplications = Optional.of(AccessToAllApplicationsVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantStateChange setDomain(final String value) {
    this.domain = Optional.of(DomainVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantStateChange setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantStateChange setName(final String value) {
    this.name = Optional.of(NameVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantStateChange setRelingParties(final List<RelingParties> value) {
    this.relingParties = Optional.of(RelingPartiesVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantStateChange setTrustedClients(final List<TrustedClients> value) {
    this.trustedClients = Optional.of(TrustedClientsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantStateChange setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TenantStateChange setVersion(final Integer value) {
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
    if ("name".equals(field)) {
      this.unsetName();
    }
    if ("domain".equals(field)) {
      this.unsetDomain();
    }
    if ("enabled".equals(field)) {
      this.unsetEnabled();
    }
    if ("accessToAllApplications".equals(field)) {
      this.unsetAccessToAllApplications();
    }
    if ("trustedClients".equals(field)) {
      this.unsetTrustedClients();
    }
    if ("relingParties".equals(field)) {
      this.unsetRelingParties();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantStateChange unsetAccessToAllApplications() {
    this.accessToAllApplications = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantStateChange unsetDomain() {
    this.domain = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantStateChange unsetEnabled() {
    this.enabled = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantStateChange unsetName() {
    this.name = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantStateChange unsetRelingParties() {
    this.relingParties = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantStateChange unsetTrustedClients() {
    this.trustedClients = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantStateChange unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TenantStateChange unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
