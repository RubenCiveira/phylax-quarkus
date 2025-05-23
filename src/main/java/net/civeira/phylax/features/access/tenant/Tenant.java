package net.civeira.phylax.features.access.tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;
import net.civeira.phylax.features.access.tenant.command.TenantChangeProposal;
import net.civeira.phylax.features.access.tenant.command.TenantWriteAttributes;
import net.civeira.phylax.features.access.tenant.event.TenantCreateEvent;
import net.civeira.phylax.features.access.tenant.event.TenantDeleteEvent;
import net.civeira.phylax.features.access.tenant.event.TenantDisableEvent;
import net.civeira.phylax.features.access.tenant.event.TenantEnableEvent;
import net.civeira.phylax.features.access.tenant.event.TenantEvent;
import net.civeira.phylax.features.access.tenant.event.TenantUpdateEvent;
import net.civeira.phylax.features.access.tenant.valueobject.AccessToAllApplicationsVO;
import net.civeira.phylax.features.access.tenant.valueobject.DomainVO;
import net.civeira.phylax.features.access.tenant.valueobject.EnabledVO;
import net.civeira.phylax.features.access.tenant.valueobject.NameVO;
import net.civeira.phylax.features.access.tenant.valueobject.RelingPartiesVO;
import net.civeira.phylax.features.access.tenant.valueobject.TrustedClientsVO;
import net.civeira.phylax.features.access.tenant.valueobject.UidVO;
import net.civeira.phylax.features.access.tenant.valueobject.VersionVO;

@AllArgsConstructor
@Getter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tenant implements TenantRef {

  /**
   * @autogenerated EntityGenerator
   * @param attributes
   * @return
   */
  public static Tenant create(final TenantWriteAttributes attributes) {
    TenantChangeProposal proposal = new TenantChangeProposal(attributes);
    proposal.setEnabled(false);
    Tenant instance = new Tenant(proposal, Optional.empty());
    instance.addEvent(TenantCreateEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public static Set<String> readonlyFields() {
    return Set.of("enabled");
  }

  /**
   * Defines if users from this tenant can access all parties and clients, or only those explicitly
   * assigned.
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  @Builder.Default
  private AccessToAllApplicationsVO accessToAllApplications = AccessToAllApplicationsVO.nullValue();

  /**
   * A domain suffix to identify the account
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  private DomainVO domain;

  /**
   * The users of a non enabled tenant could not make login on the system
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  private EnabledVO enabled;

  /**
   * The name to identify the account.
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  private NameVO name;

  /**
   * @autogenerated EntityGenerator
   */
  @NonNull
  @Builder.Default
  private List<TenantEvent> recordedEvents = List.of();

  /**
   * A list of relying parties (services or applications) associated with this tenant.
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  private RelingPartiesVO relingParties;

  /**
   * A list of OAuth clients marked as trusted for this tenant.
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  private TrustedClientsVO trustedClients;

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  private UidVO uid;

  /**
   * Campo con el número de version de tenant para controlar bloqueos optimistas
   *
   * @autogenerated EntityGenerator
   */
  @NonNull
  @Builder.Default
  private VersionVO version = VersionVO.nullValue();

  /**
   * @autogenerated EntityGenerator
   * @param attribute
   * @param previous
   */
  private Tenant(final TenantWriteAttributes attribute, final Optional<Tenant> previous) {
    ConstraintFailList list = new ConstraintFailList();
    this.uid = attribute.getUid().orElse(previous.map(Tenant::getUid).orElse(null));
    this.name = attribute.getName().orElse(previous.map(Tenant::getName).orElse(null));
    this.domain = attribute.getDomain().orElse(previous.map(Tenant::getDomain).orElse(null));
    this.enabled = attribute.getEnabled().orElse(previous.map(Tenant::getEnabled).orElse(null));
    this.accessToAllApplications = attribute.getAccessToAllApplications().orElse(previous
        .map(Tenant::getAccessToAllApplications).orElseGet(AccessToAllApplicationsVO::nullValue));
    this.trustedClients = attribute.getTrustedClients().orElse(
        previous.map(Tenant::getTrustedClients).orElseGet(() -> TrustedClientsVO.from(List.of())));
    this.relingParties = attribute.getRelingParties().orElse(
        previous.map(Tenant::getRelingParties).orElseGet(() -> RelingPartiesVO.from(List.of())));
    this.version = attribute.getVersion()
        .orElse(previous.map(Tenant::getVersion).orElseGet(VersionVO::nullValue));
    if (null == uid) {
      list.add(new ConstraintFail("REQUIRED", "uid", null));
    }
    if (null == name) {
      list.add(new ConstraintFail("REQUIRED", "name", null));
    }
    if (null == domain) {
      list.add(new ConstraintFail("REQUIRED", "domain", null));
    }
    if (null == enabled) {
      list.add(new ConstraintFail("REQUIRED", "enabled", null));
    }
    if (null == trustedClients) {
      list.add(new ConstraintFail("REQUIRED", "trustedClients", null));
    }
    if (null == relingParties) {
      list.add(new ConstraintFail("REQUIRED", "relingParties", null));
    }
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    this.recordedEvents = previous.map(Tenant::getRecordedEvents).orElseGet(List::of);
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public Tenant delete() {
    Tenant instance = this;
    instance.addEvent(TenantDeleteEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public Tenant disable() {
    TenantChangeProposal attr = new TenantChangeProposal(this);
    attr.setEnabled(false);
    Tenant instance = new Tenant(attr, Optional.of(this));
    instance.addEvent(TenantDisableEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public Tenant enable() {
    TenantChangeProposal attr = new TenantChangeProposal(this);
    attr.setEnabled(true);
    Tenant instance = new Tenant(attr, Optional.of(this));
    instance.addEvent(TenantEnableEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public String getDomainValue() {
    return getDomain().getValue();
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public String getNameValue() {
    return getName().getValue();
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public List<RelingParties> getRelingPartiesValue() {
    return getRelingParties().getValue();
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public List<TrustedClients> getTrustedClientsValue() {
    return getTrustedClients().getValue();
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  @Override
  public String getUidValue() {
    return this.uid.getValue();
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public Optional<Integer> getVersionValue() {
    return getVersion().getValue();
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public boolean isAccessToAllApplications() {
    return Boolean.TRUE.equals(getAccessToAllApplications().getValue().orElse(Boolean.FALSE));
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public boolean isEnabled() {
    return Boolean.TRUE.equals(getEnabled().getValue());
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public Tenant nextVersion() {
    return Tenant.builder().uid(this.uid).name(this.name).domain(this.domain).enabled(this.enabled)
        .accessToAllApplications(this.accessToAllApplications).trustedClients(this.trustedClients)
        .relingParties(this.relingParties).version(VersionVO.from(this.version() + 1)).build();
  }

  /**
   * @autogenerated EntityGenerator
   * @param attributes
   * @return
   */
  public Tenant update(final TenantWriteAttributes attributes) {
    TenantChangeProposal proposal = new TenantChangeProposal(attributes);
    proposal.unsetEnabled();
    Tenant instance = new Tenant(proposal, Optional.of(this));
    instance.addEvent(TenantUpdateEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public Integer version() {
    return this.getVersion().getValue().orElse(0);
  }

  /**
   * @autogenerated EntityGenerator
   * @param event
   */
  private void addEvent(final TenantEvent event) {
    List<TenantEvent> events = new ArrayList<>(this.recordedEvents);
    events.add(event);
    this.recordedEvents = List.copyOf(events);
  }
}
