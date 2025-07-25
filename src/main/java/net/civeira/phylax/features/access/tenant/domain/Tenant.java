package net.civeira.phylax.features.access.tenant.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;
import lombok.experimental.Delegate;
import net.civeira.phylax.common.exception.ConstraintException;
import net.civeira.phylax.common.value.validation.ConstraintFail;
import net.civeira.phylax.common.value.validation.ConstraintFailList;
import net.civeira.phylax.features.access.tenant.domain.event.TenantCreateEvent;
import net.civeira.phylax.features.access.tenant.domain.event.TenantDeleteEvent;
import net.civeira.phylax.features.access.tenant.domain.event.TenantDisableEvent;
import net.civeira.phylax.features.access.tenant.domain.event.TenantEnableEvent;
import net.civeira.phylax.features.access.tenant.domain.event.TenantEvent;
import net.civeira.phylax.features.access.tenant.domain.event.TenantUpdateEvent;
import net.civeira.phylax.features.access.tenant.domain.valueobject.AccessToAllApplicationsVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.DomainVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.NameVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.RelingPartiesVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.TrustedClientsVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.tenant.domain.valueobject.VersionVO;

/**
 * Represents a tenant or account in the system. Each tenant defines an isolated scope for users and
 * applications, with optional access to global resources or restricted visibility. Tenants allow
 * multi-tenancy features in the platform.
 */
@Builder
@Getter
@With
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Tenant implements TenantRef {

  /**
   * Prepare a new tenant with the provided values
   *
   * @autogenerated EntityGenerator
   * @param change A set of values to create a new tenant
   * @throws ConstraintException If there are some constraints fails on the changes.
   * @return A well formed tenant.
   */
  public static Tenant create(final TenantChangeSet change) throws ConstraintException {
    change.setEnabled(false);
    Tenant instance = new Tenant(change, Optional.empty());
    instance.addEvent(TenantCreateEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Defines if users from this tenant can access all parties and clients, or only those explicitly
   * assigned.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private AccessToAllApplicationsVO accessToAllApplicationsValue =
      AccessToAllApplicationsVO.nullValue();

  /**
   * A domain suffix to identify the account
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private DomainVO domainValue;

  /**
   * The users of a non enabled tenant could not make login on the system
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private EnabledVO enabledValue;

  /**
   * The name to identify the account.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private NameVO nameValue;

  /**
   * List of events
   *
   * @autogenerated EntityGenerator
   */
  @Builder.Default
  private List<TenantEvent> recordedEvents = List.of();

  /**
   * A list of relying parties (services or applications) associated with this tenant.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private RelingPartiesVO relingPartiesValue;

  /**
   * A list of OAuth clients marked as trusted for this tenant.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private TrustedClientsVO trustedClientsValue;

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private UidVO uidValue;

  /**
   * Campo con el número de version de tenant para controlar bloqueos optimistas
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private VersionVO versionValue = VersionVO.nullValue();

  /**
   * @autogenerated EntityGenerator
   * @param attribute
   * @param previous
   */
  private Tenant(final TenantChangeSet attribute, final Optional<Tenant> previous) {
    ConstraintFailList list = new ConstraintFailList();
    this.uidValue = attribute.getUid().orElse(previous.map(Tenant::getUidValue).orElse(null));
    this.nameValue = attribute.getName().orElse(previous.map(Tenant::getNameValue).orElse(null));
    this.domainValue =
        attribute.getDomain().orElse(previous.map(Tenant::getDomainValue).orElse(null));
    this.enabledValue =
        attribute.getEnabled().orElse(previous.map(Tenant::getEnabledValue).orElse(null));
    this.accessToAllApplicationsValue = attribute.getAccessToAllApplications()
        .orElse(previous.map(Tenant::getAccessToAllApplicationsValue)
            .orElseGet(AccessToAllApplicationsVO::nullValue));
    this.trustedClientsValue = attribute.getTrustedClients().orElse(previous
        .map(Tenant::getTrustedClientsValue).orElseGet(() -> TrustedClientsVO.from(List.of())));
    this.relingPartiesValue = attribute.getRelingParties().orElse(previous
        .map(Tenant::getRelingPartiesValue).orElseGet(() -> RelingPartiesVO.from(List.of())));
    this.versionValue = attribute.getVersion()
        .orElse(previous.map(Tenant::getVersionValue).orElseGet(VersionVO::nullValue));
    if (null == uidValue) {
      list.add(new ConstraintFail("REQUIRED", "uid", null));
    }
    if (null == nameValue) {
      list.add(new ConstraintFail("REQUIRED", "name", null));
    }
    if (null == domainValue) {
      list.add(new ConstraintFail("REQUIRED", "domain", null));
    }
    if (null == enabledValue) {
      list.add(new ConstraintFail("REQUIRED", "enabled", null));
    }
    if (null == trustedClientsValue) {
      list.add(new ConstraintFail("REQUIRED", "trustedClients", null));
    }
    if (null == relingPartiesValue) {
      list.add(new ConstraintFail("REQUIRED", "relingParties", null));
    }
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    this.recordedEvents = previous.map(Tenant::getRecordedEvents).orElseGet(List::of);
  }

  /**
   * Apply changes to delete a tenant
   *
   * @autogenerated EntityGenerator
   * @return A instance of tenant ready to be deleted
   */
  public Tenant delete() {
    Tenant instance = this;
    instance.addEvent(TenantDeleteEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply disable on tenant. Disables the tenant, preventing access without removing the record.
   *
   * @autogenerated EntityGenerator
   * @return A modified instance of tenant
   */
  public Tenant disable() {
    TenantChangeSet attr = new TenantChangeSet();
    attr.setEnabled(false);
    Tenant instance = new Tenant(attr, Optional.of(this));
    instance.addEvent(TenantDisableEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply enable on tenant. Enables the tenant, allowing its users to access the system.
   *
   * @autogenerated EntityGenerator
   * @return A modified instance of tenant
   */
  public Tenant enable() {
    TenantChangeSet attr = new TenantChangeSet();
    attr.setEnabled(true);
    Tenant instance = new Tenant(attr, Optional.of(this));
    instance.addEvent(TenantEnableEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Modify the values for some of the properties of a tenant
   *
   * @autogenerated EntityGenerator
   * @param change The properties to be modified
   * @throws ConstraintException If there are some constraints fails on the changes.
   * @return A modified instance of tenant
   */
  public Tenant update(final TenantChangeSet change) throws ConstraintException {
    change.unsetEnabled();
    Tenant instance = new Tenant(change, Optional.of(this));
    instance.addEvent(TenantUpdateEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public Tenant withNextVersion() {
    return withVersionValue(VersionVO.from(nextVersion()));
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
