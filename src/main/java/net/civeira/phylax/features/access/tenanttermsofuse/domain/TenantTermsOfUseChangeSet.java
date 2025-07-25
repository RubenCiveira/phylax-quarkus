package net.civeira.phylax.features.access.tenanttermsofuse.domain;

import java.time.OffsetDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.tenant.domain.TenantRef;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.valueobject.ActivationDateVO;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.valueobject.AttachedVO;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.valueobject.TenantVO;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.valueobject.TextVO;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.tenanttermsofuse.domain.valueobject.VersionVO;

/**
 * A dto transfer to hold tenant terms of use attribute values
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantTermsOfUseChangeSet {

  /**
   * @autogenerated EntityChangeSetGenerator
   */
  public static class TenantTermsOfUseChangeSetBuilder {

    /**
     * Append ActivationDate crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param activationDate The ActivationDate value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder activationDate(final OffsetDateTime activationDate) {
      return activationDate(Optional.of(ActivationDateVO.from(activationDate)));
    }

    /**
     * Append ActivationDate value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param activationDate The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder activationDate(
        final Optional<ActivationDateVO> activationDate) {
      this.activationDate$value = activationDate;
      this.activationDate$set = true;
      return this;
    }

    /**
     * Append Attached crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param attached The Attached value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder attached(final String attached) {
      return attached(Optional.of(AttachedVO.from(attached)));
    }

    /**
     * Append Attached value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param attached The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder attached(final Optional<AttachedVO> attached) {
      this.attached$value = attached;
      this.attached$set = true;
      return this;
    }

    /**
     * @autogenerated EntityChangeSetGenerator
     * @return
     */
    public TenantTermsOfUseChangeSetBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * Append Tenant crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param tenant The Tenant value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder tenant(final TenantRef tenant) {
      return tenant(Optional.of(TenantVO.from(tenant)));
    }

    /**
     * Append Tenant value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param tenant The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder tenant(final Optional<TenantVO> tenant) {
      this.tenant$value = tenant;
      this.tenant$set = true;
      return this;
    }

    /**
     * Append Text crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param text The Text value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder text(final String text) {
      return text(Optional.of(TextVO.from(text)));
    }

    /**
     * Append Text value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param text The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder text(final Optional<TextVO> text) {
      this.text$value = text;
      this.text$set = true;
      return this;
    }

    /**
     * Append Uid crud value and convert as value object.
     *
     * @autogenerated EntityChangeSetGenerator
     * @param uid The Uid value to wrap in a value object.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * Append Uid value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param uid The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder uid(final Optional<UidVO> uid) {
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
    public TenantTermsOfUseChangeSetBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * Append Version value
     *
     * @autogenerated EntityChangeSetGenerator
     * @param version The value object with the information.
     * @return Self instance to continue with a flow build
     */
    public TenantTermsOfUseChangeSetBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * Date when the Terms of Use become active.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<ActivationDateVO> activationDate = Optional.empty();

  /**
   * An optional file attachment (e.g., PDF or signed document).
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<AttachedVO> attached = Optional.empty();

  /**
   * The tenant this Terms of Use document belongs to.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<TenantVO> tenant = Optional.empty();

  /**
   * The content of the Terms of Use document.
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<TextVO> text = Optional.empty();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de tenant terms of use para controlar bloqueos optimistas
   *
   * @autogenerated EntityChangeSetGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * Inform for a possible change propolsal in ActivationDate
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for ActivationDate, otherwise the value for
   *         ActivationDate
   */
  public Optional<ActivationDateVO> getActivationDate() {
    return activationDate;
  }

  /**
   * Inform for a possible change propolsal in Attached
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for Attached, otherwise the value for Attached
   */
  public Optional<AttachedVO> getAttached() {
    return attached;
  }

  /**
   * Inform for a possible change propolsal in Tenant
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for Tenant, otherwise the value for Tenant
   */
  public Optional<TenantVO> getTenant() {
    return tenant;
  }

  /**
   * Inform for a possible change propolsal in Text
   *
   * @autogenerated EntityChangeSetGenerator
   * @return empty if there is no change proposal for Text, otherwise the value for Text
   */
  public Optional<TextVO> getText() {
    return text;
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
   * Assigna change proposal for ActivationDate to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for TenantTermsOfUseChangeSet
   * @return self instance to enable a flow code
   */
  public TenantTermsOfUseChangeSet setActivationDate(final OffsetDateTime value) {
    this.activationDate = Optional.of(ActivationDateVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Attached to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for TenantTermsOfUseChangeSet
   * @return self instance to enable a flow code
   */
  public TenantTermsOfUseChangeSet setAttached(final String value) {
    this.attached = Optional.of(AttachedVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Tenant to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for TenantTermsOfUseChangeSet
   * @return self instance to enable a flow code
   */
  public TenantTermsOfUseChangeSet setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Text to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for TenantTermsOfUseChangeSet
   * @return self instance to enable a flow code
   */
  public TenantTermsOfUseChangeSet setText(final String value) {
    this.text = Optional.of(TextVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Uid to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for TenantTermsOfUseChangeSet
   * @return self instance to enable a flow code
   */
  public TenantTermsOfUseChangeSet setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * Assigna change proposal for Version to be apply for the aggregate.
   *
   * @autogenerated EntityChangeSetGenerator
   * @param value The proposal value for TenantTermsOfUseChangeSet
   * @return self instance to enable a flow code
   */
  public TenantTermsOfUseChangeSet setVersion(final Integer value) {
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
    if ("tenant".equals(field)) {
      this.unsetTenant();
    }
    if ("text".equals(field)) {
      this.unsetText();
    }
    if ("attached".equals(field)) {
      this.unsetAttached();
    }
    if ("activationDate".equals(field)) {
      this.unsetActivationDate();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * Remove the change propolsal for ActivationDate
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public TenantTermsOfUseChangeSet unsetActivationDate() {
    this.activationDate = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Attached
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public TenantTermsOfUseChangeSet unsetAttached() {
    this.attached = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Tenant
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public TenantTermsOfUseChangeSet unsetTenant() {
    this.tenant = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Text
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public TenantTermsOfUseChangeSet unsetText() {
    this.text = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Uid
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public TenantTermsOfUseChangeSet unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * Remove the change propolsal for Version
   *
   * @autogenerated EntityChangeSetGenerator
   * @return self instance to enable a flow code.
   */
  public TenantTermsOfUseChangeSet unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
