package net.civeira.phylax.features.access.relyingparty.domain;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.ApiKeyVO;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.CodeVO;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.VersionVO;

/**
 * A dto transfer to hold relying party attribute values
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelyingPartyChangeSet {

  /**
   * @autogenerated EntityChangeSetGenerator
   */
  public static class RelyingPartyChangeSetBuilder {

    /**
     * Append ApiKey crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param apiKey The ApiKey value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder apiKey(final String apiKey) {
      return apiKey(Optional.of(ApiKeyVO.from(apiKey)));
    }

    /**
     * Append ApiKey value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param apiKey The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder apiKey(final Optional<ApiKeyVO> apiKey) {
      this.apiKey$value = apiKey;
      this.apiKey$set = true;
      return this;
    }

    /**
     * Append Code crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param code The Code value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder code(final String code) {
      return code(Optional.of(CodeVO.from(code)));
    }

    /**
     * Append Code value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param code The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder code(final Optional<CodeVO> code) {
      this.code$value = code;
      this.code$set = true;
      return this;
    }

    /**
     * Append Enabled crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param enabled The Enabled value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder enabled(final Boolean enabled) {
      return enabled(Optional.of(EnabledVO.from(enabled)));
    }

    /**
     * Append Enabled value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param enabled The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder enabled(final Optional<EnabledVO> enabled) {
      this.enabled$value = enabled;
      this.enabled$set = true;
      return this;
    }

    /**
     * @autogenerated EntityChangeSetGenerator
     * @return
     */
    public RelyingPartyChangeSetBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * Append Uid crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param uid The Uid value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * Append Uid value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param uid The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * Append Version crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param version The Version value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * Append Version value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param version The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public RelyingPartyChangeSetBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * A identification for the aplication
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<ApiKeyVO> apiKey = Optional.empty();

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<CodeVO> code = Optional.empty();

  /**
   * Una marca que permite quitar el acceso a una cuenta sin borrarla
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<EnabledVO> enabled = Optional.empty();

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de relying party para controlar bloqueos optimistas
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Inform for a possible change propolsal in ApiKey
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for ApiKey, otherwise the value for ApiKey
   */
  public Optional<ApiKeyVO> getApiKey() {
    return apiKey;
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public Optional<CodeVO> getCode() {
    return code;
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Optional<EnabledVO> getEnabled() {
    return enabled;
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public Optional<UidVO> getUid() {
    return uid;
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Optional<VersionVO> getVersion() {
    return version;
  }

  /**
   * Assigna change proposal for ApiKey to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for RelyingPartyChangeSet
   * @return self instance to enable a flow code
   */
  public RelyingPartyChangeSet setApiKey(final String value) {
    this.apiKey = Optional.of(ApiKeyVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Code to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for RelyingPartyChangeSet
   * @return self instance to enable a flow code
   */
  public RelyingPartyChangeSet setCode(final String value) {
    this.code = Optional.of(CodeVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Enabled to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for RelyingPartyChangeSet
   * @return self instance to enable a flow code
   */
  public RelyingPartyChangeSet setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for RelyingPartyChangeSet
   * @return self instance to enable a flow code
   */
  public RelyingPartyChangeSet setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for RelyingPartyChangeSet
   * @return self instance to enable a flow code
   */
  public RelyingPartyChangeSet setVersion(final Integer value) {
    this.version = Optional.of(VersionVO.from(value));
    return this;
  }

  /**
   * Unset a value by the property name. Unset values will no provide command change neither provide
   * information to the output context (hidden or readonly fields, or policy restrictions).
   *
   * @autogenerated EntityChangeSetGenerator
   * @param field The field name to unset.
   */
  public void unset(final String field) {
    if ("uid".equals(field)) {
      this.unsetUid();
    }
    if ("code".equals(field)) {
      this.unsetCode();
    }
    if ("apiKey".equals(field)) {
      this.unsetApiKey();
    }
    if ("enabled".equals(field)) {
      this.unsetEnabled();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * Remove the change propolsal for ApiKey
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public RelyingPartyChangeSet unsetApiKey() {
    this.apiKey = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Code
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public RelyingPartyChangeSet unsetCode() {
    this.code = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Enabled
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public RelyingPartyChangeSet unsetEnabled() {
    this.enabled = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Uid
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public RelyingPartyChangeSet unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Version
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public RelyingPartyChangeSet unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
