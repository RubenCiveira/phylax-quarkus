package net.civeira.phylax.features.access.user.application.usecase.create;

import java.time.OffsetDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;
import net.civeira.phylax.features.access.user.domain.UserChangeSet;
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
 * A dto transfer to hold user attribute values
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateInput {

  /**
   * @autogenerated CreateInputGenerator
   */
  public static class UserCreateInputBuilder {

    /**
     * Append BlockedUntil crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param blockedUntil The BlockedUntil value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder blockedUntil(final OffsetDateTime blockedUntil) {
      return blockedUntil(Optional.of(BlockedUntilVO.from(blockedUntil)));
    }

    /**
     * Append BlockedUntil value
     *
     * @autogenerated CreateInputGenerator
     * @param blockedUntil The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder blockedUntil(final Optional<BlockedUntilVO> blockedUntil) {
      this.blockedUntil$value = blockedUntil;
      this.blockedUntil$set = true;
      return this;
    }

    /**
     * Append Email crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param email The Email value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder email(final String email) {
      return email(Optional.of(EmailVO.from(email)));
    }

    /**
     * Append Email value
     *
     * @autogenerated CreateInputGenerator
     * @param email The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder email(final Optional<EmailVO> email) {
      this.email$value = email;
      this.email$set = true;
      return this;
    }

    /**
     * Append Enabled crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param enabled The Enabled value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder enabled(final Boolean enabled) {
      return enabled(Optional.of(EnabledVO.from(enabled)));
    }

    /**
     * Append Enabled value
     *
     * @autogenerated CreateInputGenerator
     * @param enabled The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder enabled(final Optional<EnabledVO> enabled) {
      this.enabled$value = enabled;
      this.enabled$set = true;
      return this;
    }

    /**
     * Append Language crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param language The Language value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder language(final String language) {
      return language(Optional.of(LanguageVO.from(language)));
    }

    /**
     * Append Language value
     *
     * @autogenerated CreateInputGenerator
     * @param language The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder language(final Optional<LanguageVO> language) {
      this.language$value = language;
      this.language$set = true;
      return this;
    }

    /**
     * Append Name crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param name The Name value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder name(final String name) {
      return name(Optional.of(NameVO.from(name)));
    }

    /**
     * Append Name value
     *
     * @autogenerated CreateInputGenerator
     * @param name The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder name(final Optional<NameVO> name) {
      this.name$value = name;
      this.name$set = true;
      return this;
    }

    /**
     * @autogenerated CreateInputGenerator
     * @return
     */
    public UserCreateInputBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * Append Password crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param password The Password value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder password(final String password) {
      return password(Optional.of(PasswordVO.fromPlain(password)));
    }

    /**
     * Append Password value
     *
     * @autogenerated CreateInputGenerator
     * @param password The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder password(final Optional<PasswordVO> password) {
      this.password$value = password;
      this.password$set = true;
      return this;
    }

    /**
     * Append Provider crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param provider The Provider value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder provider(final String provider) {
      return provider(Optional.of(ProviderVO.from(provider)));
    }

    /**
     * Append Provider value
     *
     * @autogenerated CreateInputGenerator
     * @param provider The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder provider(final Optional<ProviderVO> provider) {
      this.provider$value = provider;
      this.provider$set = true;
      return this;
    }

    /**
     * Append SecondFactorSeed crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param secondFactorSeed The SecondFactorSeed value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder secondFactorSeed(final String secondFactorSeed) {
      return secondFactorSeed(Optional.of(SecondFactorSeedVO.fromPlain(secondFactorSeed)));
    }

    /**
     * Append SecondFactorSeed value
     *
     * @autogenerated CreateInputGenerator
     * @param secondFactorSeed The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder secondFactorSeed(
        final Optional<SecondFactorSeedVO> secondFactorSeed) {
      this.secondFactorSeed$value = secondFactorSeed;
      this.secondFactorSeed$set = true;
      return this;
    }

    /**
     * Append TemporalPassword crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param temporalPassword The TemporalPassword value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder temporalPassword(final Boolean temporalPassword) {
      return temporalPassword(Optional.of(TemporalPasswordVO.from(temporalPassword)));
    }

    /**
     * Append TemporalPassword value
     *
     * @autogenerated CreateInputGenerator
     * @param temporalPassword The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder temporalPassword(
        final Optional<TemporalPasswordVO> temporalPassword) {
      this.temporalPassword$value = temporalPassword;
      this.temporalPassword$set = true;
      return this;
    }

    /**
     * Append Tenant crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param tenant The Tenant value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder tenant(final TenantRef tenant) {
      return tenant(Optional.of(TenantVO.from(tenant)));
    }

    /**
     * Append Tenant value
     *
     * @autogenerated CreateInputGenerator
     * @param tenant The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder tenant(final Optional<TenantVO> tenant) {
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
    public UserCreateInputBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * Append Uid value
     *
     * @autogenerated CreateInputGenerator
     * @param uid The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * Append UseSecondFactors crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param useSecondFactors The UseSecondFactors value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder useSecondFactors(final Boolean useSecondFactors) {
      return useSecondFactors(Optional.of(UseSecondFactorsVO.from(useSecondFactors)));
    }

    /**
     * Append UseSecondFactors value
     *
     * @autogenerated CreateInputGenerator
     * @param useSecondFactors The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder useSecondFactors(
        final Optional<UseSecondFactorsVO> useSecondFactors) {
      this.useSecondFactors$value = useSecondFactors;
      this.useSecondFactors$set = true;
      return this;
    }

    /**
     * Append Version crud value and convert as value object.
     *
     * @autogenerated CreateInputGenerator
     * @param version The Version value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * Append Version value
     *
     * @autogenerated CreateInputGenerator
     * @param version The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public UserCreateInputBuilder version(final Optional<VersionVO> version) {
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
  static UserCreateInput fromChangeSet(final UserChangeSet input) {
    return UserCreateInput.builder().uid(input.getUid()).tenant(input.getTenant())
        .name(input.getName()).password(input.getPassword()).email(input.getEmail())
        .enabled(input.getEnabled()).temporalPassword(input.getTemporalPassword())
        .useSecondFactors(input.getUseSecondFactors()).secondFactorSeed(input.getSecondFactorSeed())
        .blockedUntil(input.getBlockedUntil()).language(input.getLanguage())
        .provider(input.getProvider()).version(input.getVersion()).build();
  }

  /**
   * A bloqued user cant login on the system, until these date. On his login attempst, hue will
   * recive a generic not allowed message.
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<BlockedUntilVO> blockedUntil = Optional.empty();

  /**
   * An optional email used to send notifications to the user
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<EmailVO> email = Optional.empty();

  /**
   * A disabled user cant login on the system. On his login attempts, he will recive a specific warn
   * of his disabled account.
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<EnabledVO> enabled = Optional.empty();

  /**
   * El language de user
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<LanguageVO> language = Optional.empty();

  /**
   * The user name to identify on the login screen
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<NameVO> name = Optional.empty();

  /**
   * If the user is not delegated, the phrasse to identify
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<PasswordVO> password = Optional.empty();

  /**
   * Si se identificó con un proveedor, se marca con cual (aunque el provedor cambie luego)
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<ProviderVO> provider = Optional.empty();

  /**
   * the seed used to the otp login
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<SecondFactorSeedVO> secondFactorSeed = Optional.empty();

  /**
   * El temporal password de user
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<TemporalPasswordVO> temporalPassword = Optional.empty();

  /**
   * Los usuarios que no tienen tenant son roots del sistema
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
   * If is true, the user has a otp to force mfa on login
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<UseSecondFactorsVO> useSecondFactors = Optional.empty();

  /**
   * Campo con el número de version de user para controlar bloqueos optimistas
   *
   * @autogenerated CreateInputGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Inform for a possible change propolsal in BlockedUntil
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for BlockedUntil, otherwise the value for
   *         BlockedUntil
   */
  public Optional<BlockedUntilVO> getBlockedUntil() {
    return blockedUntil;
  }

  /**
   * Inform for a possible change propolsal in Email
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Email, otherwise the value for Email
   */
  public Optional<EmailVO> getEmail() {
    return email;
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Optional<EnabledVO> getEnabled() {
    return enabled;
  }

  /**
   * Inform for a possible change propolsal in Language
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Language, otherwise the value for Language
   */
  public Optional<LanguageVO> getLanguage() {
    return language;
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
   * Inform for a possible change propolsal in Password
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Password, otherwise the value for Password
   */
  public Optional<PasswordVO> getPassword() {
    return password;
  }

  /**
   * Inform for a possible change propolsal in Provider
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for Provider, otherwise the value for Provider
   */
  public Optional<ProviderVO> getProvider() {
    return provider;
  }

  /**
   * Inform for a possible change propolsal in SecondFactorSeed
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for SecondFactorSeed, otherwise the value for
   *         SecondFactorSeed
   */
  public Optional<SecondFactorSeedVO> getSecondFactorSeed() {
    return secondFactorSeed;
  }

  /**
   * Inform for a possible change propolsal in TemporalPassword
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for TemporalPassword, otherwise the value for
   *         TemporalPassword
   */
  public Optional<TemporalPasswordVO> getTemporalPassword() {
    return temporalPassword;
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
   * Inform for a possible change propolsal in UseSecondFactors
   *
   * @autogenerated CreateInputGenerator
   * @return empty if there is no change proposal for UseSecondFactors, otherwise the value for
   *         UseSecondFactors
   */
  public Optional<UseSecondFactorsVO> getUseSecondFactors() {
    return useSecondFactors;
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
   * Assigna change proposal for BlockedUntil to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setBlockedUntil(final OffsetDateTime value) {
    this.blockedUntil = Optional.of(BlockedUntilVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Email to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setEmail(final String value) {
    this.email = Optional.of(EmailVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Enabled to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Language to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setLanguage(final String value) {
    this.language = Optional.of(LanguageVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Name to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setName(final String value) {
    this.name = Optional.of(NameVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Password to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setPassword(final String value) {
    this.password = Optional.of(PasswordVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for Provider to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setProvider(final String value) {
    this.provider = Optional.of(ProviderVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for SecondFactorSeed to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setSecondFactorSeed(final String value) {
    this.secondFactorSeed = Optional.of(SecondFactorSeedVO.fromPlain(value));
    return this;
  }

  /**
   * Assigna change proposal for TemporalPassword to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setTemporalPassword(final Boolean value) {
    this.temporalPassword = Optional.of(TemporalPasswordVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Tenant to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for UseSecondFactors to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setUseSecondFactors(final Boolean value) {
    this.useSecondFactors = Optional.of(UseSecondFactorsVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated CreateInputGenerator
   * @param value The proposal value for UserCreateInput
   * @return self instance to enable a flow code
   */
  public UserCreateInput setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * @autogenerated CreateInputGenerator
   * @return
   */
  UserChangeSet toChangeSet() {
    return UserChangeSet.builder().uid(uid).tenant(tenant).name(name).password(password)
        .email(email).enabled(enabled).temporalPassword(temporalPassword)
        .useSecondFactors(useSecondFactors).secondFactorSeed(secondFactorSeed)
        .blockedUntil(blockedUntil).language(language).provider(provider).version(version).build();
  }
}
