package net.civeira.phylax.features.access.tenant.application.usecase.update;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.tenant.domain.RelingParties;
import net.civeira.phylax.features.access.tenant.domain.TenantChangeSet;
import net.civeira.phylax.features.access.tenant.domain.TrustedClients;
import net.civeira.phylax.features.access.tenant.domain.valueobject.AccessToAllApplicationsVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.DomainVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.NameVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.RelingPartiesVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.TrustedClientsVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.VersionVO;

/**
 * A dto transfer to hold tenant attribute values
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantUpdateInput {

  /**
   * @autogenerated UpdateInputGenerator
   */
  public static class TenantUpdateInputBuilder {

    /**
     * Append AccessToAllApplications crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param accessToAllApplications The AccessToAllApplications value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder accessToAllApplications(final Boolean accessToAllApplications) {
      return accessToAllApplications(
          Optional.of(AccessToAllApplicationsVO.from(accessToAllApplications)));
    }

    /**
     * Append AccessToAllApplications value
     *
     * @autogenerated UpdateInputGenerator
     * @param accessToAllApplications The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder accessToAllApplications(
        final Optional<AccessToAllApplicationsVO> accessToAllApplications) {
      this.accessToAllApplications$value = accessToAllApplications;
      this.accessToAllApplications$set = true;
      return this;
    }

    /**
     * Append Domain crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param domain The Domain value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder domain(final String domain) {
      return domain(Optional.of(DomainVO.from(domain)));
    }

    /**
     * Append Domain value
     *
     * @autogenerated UpdateInputGenerator
     * @param domain The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder domain(final Optional<DomainVO> domain) {
      this.domain$value = domain;
      this.domain$set = true;
      return this;
    }

    /**
     * Append Enabled crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param enabled The Enabled value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder enabled(final Boolean enabled) {
      return enabled(Optional.of(EnabledVO.from(enabled)));
    }

    /**
     * Append Enabled value
     *
     * @autogenerated UpdateInputGenerator
     * @param enabled The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder enabled(final Optional<EnabledVO> enabled) {
      this.enabled$value = enabled;
      this.enabled$set = true;
      return this;
    }

    /**
     * Append Name crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param name The Name value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder name(final String name) {
      return name(Optional.of(NameVO.from(name)));
    }

    /**
     * Append Name value
     *
     * @autogenerated UpdateInputGenerator
     * @param name The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder name(final Optional<NameVO> name) {
      this.name$value = name;
      this.name$set = true;
      return this;
    }

    /**
     * @autogenerated UpdateInputGenerator
     * @return
     */
    public TenantUpdateInputBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * Append RelingParties crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param relingParties The RelingParties value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder relingParties(final List<RelingParties> relingParties) {
      return relingParties(Optional.of(RelingPartiesVO.from(relingParties)));
    }

    /**
     * Append RelingParties value
     *
     * @autogenerated UpdateInputGenerator
     * @param relingParties The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder relingParties(final Optional<RelingPartiesVO> relingParties) {
      this.relingParties$value = relingParties;
      this.relingParties$set = true;
      return this;
    }

    /**
     * Append TrustedClients crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param trustedClients The TrustedClients value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder trustedClients(final List<TrustedClients> trustedClients) {
      return trustedClients(Optional.of(TrustedClientsVO.from(trustedClients)));
    }

    /**
     * Append TrustedClients value
     *
     * @autogenerated UpdateInputGenerator
     * @param trustedClients The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder trustedClients(
        final Optional<TrustedClientsVO> trustedClients) {
      this.trustedClients$value = trustedClients;
      this.trustedClients$set = true;
      return this;
    }

    /**
     * Append Uid crud value and convert as value object.
     *
     * @autogenerated UpdateInputGenerator
     * @param uid The Uid value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * Append Uid value
     *
     * @autogenerated UpdateInputGenerator
     * @param uid The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder uid(final Optional<UidVO> uid) {
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
    public TenantUpdateInputBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * Append Version value
     *
     * @autogenerated UpdateInputGenerator
     * @param version The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantUpdateInputBuilder version(final Optional<VersionVO> version) {
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
  static TenantUpdateInput fromChangeSet(final TenantChangeSet input) {
    return TenantUpdateInput.builder().uid(input.getUid()).name(input.getName())
        .domain(input.getDomain()).enabled(input.getEnabled())
        .accessToAllApplications(input.getAccessToAllApplications())
        .trustedClients(input.getTrustedClients()).relingParties(input.getRelingParties())
        .version(input.getVersion()).build();
  }

  /**
   * Defines if users from this tenant can access all parties and clients, or only those explicitly
   * assigned.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<AccessToAllApplicationsVO> accessToAllApplications = Optional.empty();

  /**
   * A domain suffix to identify the account
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<DomainVO> domain = Optional.empty();

  /**
   * The users of a non enabled tenant could not make login on the system
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<EnabledVO> enabled = Optional.empty();

  /**
   * The name to identify the account.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<NameVO> name = Optional.empty();

  /**
   * A list of relying parties (services or applications) associated with this tenant.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<RelingPartiesVO> relingParties = Optional.empty();

  /**
   * A list of OAuth clients marked as trusted for this tenant.
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<TrustedClientsVO> trustedClients = Optional.empty();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de tenant para controlar bloqueos optimistas
   *
   * @autogenerated UpdateInputGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Inform for a possible change propolsal in AccessToAllApplications
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for AccessToAllApplications, otherwise the value
   *         for AccessToAllApplications
   */
  public Optional<AccessToAllApplicationsVO> getAccessToAllApplications() {
    return accessToAllApplications;
  }

  /**
   * Inform for a possible change propolsal in Domain
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Domain, otherwise the value for Domain
   */
  public Optional<DomainVO> getDomain() {
    return domain;
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Optional<EnabledVO> getEnabled() {
    return enabled;
  }

  /**
   * Inform for a possible change propolsal in Name
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for Name, otherwise the value for Name
   */
  public Optional<NameVO> getName() {
    return name;
  }

  /**
   * Inform for a possible change propolsal in RelingParties
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for RelingParties, otherwise the value for
   *         RelingParties
   */
  public Optional<RelingPartiesVO> getRelingParties() {
    return relingParties;
  }

  /**
   * Inform for a possible change propolsal in TrustedClients
   *
   * @autogenerated UpdateInputGenerator
   * @return empty if there is no change proposal for TrustedClients, otherwise the value for
   *         TrustedClients
   */
  public Optional<TrustedClientsVO> getTrustedClients() {
    return trustedClients;
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
   * Assigna change proposal for AccessToAllApplications to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantUpdateInput setAccessToAllApplications(final Boolean value) {
    this.accessToAllApplications = Optional.of(AccessToAllApplicationsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Domain to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantUpdateInput setDomain(final String value) {
    this.domain = Optional.of(DomainVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Enabled to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantUpdateInput setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Name to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantUpdateInput setName(final String value) {
    this.name = Optional.of(NameVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for RelingParties to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantUpdateInput setRelingParties(final List<RelingParties> value) {
    this.relingParties = Optional.of(RelingPartiesVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for TrustedClients to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantUpdateInput setTrustedClients(final List<TrustedClients> value) {
    this.trustedClients = Optional.of(TrustedClientsVO.from(null == value ? List.of() : value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantUpdateInput setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated UpdateInputGenerator
   * @param value The proposal value for TenantUpdateInput
   * @return self instance to enable a flow code
   */
  public TenantUpdateInput setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * @autogenerated UpdateInputGenerator
   * @return
   */
  TenantChangeSet toChangeSet() {
    return TenantChangeSet.builder().uid(uid).name(name).domain(domain).enabled(enabled)
        .accessToAllApplications(accessToAllApplications).trustedClients(trustedClients)
        .relingParties(relingParties).version(version).build();
  }
}
