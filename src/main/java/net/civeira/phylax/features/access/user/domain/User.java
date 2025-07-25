package net.civeira.phylax.features.access.user.domain;

import java.time.OffsetDateTime;
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
import net.civeira.phylax.features.access.user.domain.event.UserBlockEvent;
import net.civeira.phylax.features.access.user.domain.event.UserChangePasswordEvent;
import net.civeira.phylax.features.access.user.domain.event.UserCreateEvent;
import net.civeira.phylax.features.access.user.domain.event.UserDeleteEvent;
import net.civeira.phylax.features.access.user.domain.event.UserDisableEvent;
import net.civeira.phylax.features.access.user.domain.event.UserEnableEvent;
import net.civeira.phylax.features.access.user.domain.event.UserEvent;
import net.civeira.phylax.features.access.user.domain.event.UserSetMfaSeedEvent;
import net.civeira.phylax.features.access.user.domain.event.UserUnlockEvent;
import net.civeira.phylax.features.access.user.domain.event.UserUpdateEvent;
import net.civeira.phylax.features.access.user.domain.valueobject.BlockedUntilVO;
import net.civeira.phylax.features.access.user.domain.valueobject.EmailVO;
import net.civeira.phylax.features.access.user.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.user.domain.valueobject.LanguageVO;
import net.civeira.phylax.features.access.user.domain.valueobject.NameVO;
import net.civeira.phylax.features.access.user.domain.valueobject.PasswordVO;
import net.civeira.phylax.features.access.user.domain.valueobject.ProviderVO;
import net.civeira.phylax.features.access.user.domain.valueobject.SecondFactorSeedVO;
import net.civeira.phylax.features.access.user.domain.valueobject.TemporalPasswordVO;
import net.civeira.phylax.features.access.user.domain.valueobject.TenantVO;
import net.civeira.phylax.features.access.user.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.user.domain.valueobject.UseSecondFactorsVO;
import net.civeira.phylax.features.access.user.domain.valueobject.VersionVO;

/**
 * user
 */
@Builder
@Getter
@With
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class User implements UserRef {

  /**
   * Prepare a new user with the provided values
   *
   * @autogenerated EntityGenerator
   * @param change A set of values to create a new user
   * @throws ConstraintException If there are some constraints fails on the changes.
   * @return A well formed user.
   */
  public static User create(final UserChangeSet change) throws ConstraintException {
    change.setEnabled(true);
    change.setBlockedUntil(null);
    change.setProvider(null);
    User instance = new User(change, Optional.empty());
    instance.addEvent(UserCreateEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * A bloqued user cant login on the system, until these date. On his login attempst, hue will
   * recive a generic not allowed message.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private BlockedUntilVO blockedUntilValue = BlockedUntilVO.nullValue();

  /**
   * An optional email used to send notifications to the user
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private EmailVO emailValue = EmailVO.nullValue();

  /**
   * A disabled user cant login on the system. On his login attempts, he will recive a specific warn
   * of his disabled account.
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private EnabledVO enabledValue = EnabledVO.nullValue();

  /**
   * El language de user
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private LanguageVO languageValue = LanguageVO.nullValue();

  /**
   * The user name to identify on the login screen
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private NameVO nameValue;

  /**
   * If the user is not delegated, the phrasse to identify
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private PasswordVO passwordValue;

  /**
   * Si se identificó con un proveedor, se marca con cual (aunque el provedor cambie luego)
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private ProviderVO providerValue = ProviderVO.nullValue();

  /**
   * List of events
   *
   * @autogenerated EntityGenerator
   */
  @Builder.Default
  private List<UserEvent> recordedEvents = List.of();

  /**
   * the seed used to the otp login
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private SecondFactorSeedVO secondFactorSeedValue = SecondFactorSeedVO.nullValue();

  /**
   * El temporal password de user
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TemporalPasswordVO temporalPasswordValue = TemporalPasswordVO.nullValue();

  /**
   * Los usuarios que no tienen tenant son roots del sistema
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private TenantVO tenantValue = TenantVO.nullValue();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @NonNull
  private UidVO uidValue;

  /**
   * If is true, the user has a otp to force mfa on login
   *
   * @autogenerated EntityGenerator
   */
  @Delegate
  @Builder.Default
  private UseSecondFactorsVO useSecondFactorsValue = UseSecondFactorsVO.nullValue();

  /**
   * Campo con el número de version de user para controlar bloqueos optimistas
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
  private User(final UserChangeSet attribute, final Optional<User> previous) {
    ConstraintFailList list = new ConstraintFailList();
    this.uidValue = attribute.getUid().orElse(previous.map(User::getUidValue).orElse(null));
    this.tenantValue = attribute.getTenant()
        .orElse(previous.map(User::getTenantValue).orElseGet(TenantVO::nullValue));
    this.nameValue = attribute.getName().orElse(previous.map(User::getNameValue).orElse(null));
    this.passwordValue =
        attribute.getPassword().orElse(previous.map(User::getPasswordValue).orElse(null));
    this.emailValue = attribute.getEmail()
        .orElse(previous.map(User::getEmailValue).orElseGet(EmailVO::nullValue));
    this.enabledValue = attribute.getEnabled()
        .orElse(previous.map(User::getEnabledValue).orElseGet(EnabledVO::nullValue));
    this.temporalPasswordValue = attribute.getTemporalPassword().orElse(
        previous.map(User::getTemporalPasswordValue).orElseGet(TemporalPasswordVO::nullValue));
    this.useSecondFactorsValue = attribute.getUseSecondFactors().orElse(
        previous.map(User::getUseSecondFactorsValue).orElseGet(UseSecondFactorsVO::nullValue));
    this.secondFactorSeedValue = attribute.getSecondFactorSeed().orElse(
        previous.map(User::getSecondFactorSeedValue).orElseGet(SecondFactorSeedVO::nullValue));
    this.blockedUntilValue = attribute.getBlockedUntil()
        .orElse(previous.map(User::getBlockedUntilValue).orElseGet(BlockedUntilVO::nullValue));
    this.languageValue = attribute.getLanguage()
        .orElse(previous.map(User::getLanguageValue).orElseGet(LanguageVO::nullValue));
    this.providerValue = attribute.getProvider()
        .orElse(previous.map(User::getProviderValue).orElseGet(ProviderVO::nullValue));
    this.versionValue = attribute.getVersion()
        .orElse(previous.map(User::getVersionValue).orElseGet(VersionVO::nullValue));
    if (null == uidValue) {
      list.add(new ConstraintFail("REQUIRED", "uid", null));
    }
    if (null == nameValue) {
      list.add(new ConstraintFail("REQUIRED", "name", null));
    }
    if (null == passwordValue) {
      list.add(new ConstraintFail("REQUIRED", "password", null));
    }
    if (list.hasErrors()) {
      throw new ConstraintException(list);
    }
    this.recordedEvents = previous.map(User::getRecordedEvents).orElseGet(List::of);
  }

  /**
   * Apply block on user.
   *
   * @autogenerated EntityGenerator
   * @param blockedUntil
   * @return A modified instance of user
   */
  public User block(final OffsetDateTime blockedUntil) {
    UserChangeSet attr = new UserChangeSet();
    attr.setBlockedUntil(blockedUntil);
    User instance = new User(attr, Optional.of(this));
    instance.addEvent(UserBlockEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply change password on user.
   *
   * @autogenerated EntityGenerator
   * @param password
   * @return A modified instance of user
   */
  public User changePassword(final String password) {
    UserChangeSet attr = new UserChangeSet();
    attr.setPassword(password);
    attr.setTemporalPassword(false);
    User instance = new User(attr, Optional.of(this));
    instance.addEvent(UserChangePasswordEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply changes to delete a user
   *
   * @autogenerated EntityGenerator
   * @return A instance of user ready to be deleted
   */
  public User delete() {
    User instance = this;
    instance.addEvent(UserDeleteEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply disable on user.
   *
   * @autogenerated EntityGenerator
   * @return A modified instance of user
   */
  public User disable() {
    UserChangeSet attr = new UserChangeSet();
    attr.setEnabled(false);
    User instance = new User(attr, Optional.of(this));
    instance.addEvent(UserDisableEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply enable on user.
   *
   * @autogenerated EntityGenerator
   * @return A modified instance of user
   */
  public User enable() {
    UserChangeSet attr = new UserChangeSet();
    attr.setEnabled(true);
    User instance = new User(attr, Optional.of(this));
    instance.addEvent(UserEnableEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply set mfa seed on user.
   *
   * @autogenerated EntityGenerator
   * @param secondFactorSeed
   * @return A modified instance of user
   */
  public User setMfaSeed(final String secondFactorSeed) {
    UserChangeSet attr = new UserChangeSet();
    attr.setSecondFactorSeed(secondFactorSeed);
    attr.setUseSecondFactors(true);
    User instance = new User(attr, Optional.of(this));
    instance.addEvent(UserSetMfaSeedEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Apply unlock on user.
   *
   * @autogenerated EntityGenerator
   * @return A modified instance of user
   */
  public User unlock() {
    UserChangeSet attr = new UserChangeSet();
    attr.setBlockedUntil(null);
    User instance = new User(attr, Optional.of(this));
    instance.addEvent(UserUnlockEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * Modify the values for some of the properties of a user
   *
   * @autogenerated EntityGenerator
   * @param change The properties to be modified
   * @throws ConstraintException If there are some constraints fails on the changes.
   * @return A modified instance of user
   */
  public User update(final UserChangeSet change) throws ConstraintException {
    change.unsetEnabled();
    change.unsetBlockedUntil();
    change.unsetProvider();
    User instance = new User(change, Optional.of(this));
    instance.addEvent(UserUpdateEvent.builder().payload(instance).build());
    return instance;
  }

  /**
   * @autogenerated EntityGenerator
   * @return
   */
  public User withNextVersion() {
    return withVersionValue(VersionVO.from(nextVersion()));
  }

  /**
   * @autogenerated EntityGenerator
   * @param event
   */
  private void addEvent(final UserEvent event) {
    List<UserEvent> events = new ArrayList<>(this.recordedEvents);
    events.add(event);
    this.recordedEvents = List.copyOf(events);
  }
}
