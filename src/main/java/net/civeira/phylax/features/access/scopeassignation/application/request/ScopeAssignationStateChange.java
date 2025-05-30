package net.civeira.phylax.features.access.scopeassignation.application.request;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.scopeassignation.command.ScopeAssignationWriteAttributes;
import net.civeira.phylax.features.access.scopeassignation.valueobject.SecurityDomainVO;
import net.civeira.phylax.features.access.scopeassignation.valueobject.SecurityScopeVO;
import net.civeira.phylax.features.access.scopeassignation.valueobject.UidVO;
import net.civeira.phylax.features.access.scopeassignation.valueobject.VersionVO;
import net.civeira.phylax.features.access.securitydomain.SecurityDomainRef;
import net.civeira.phylax.features.access.securityscope.SecurityScopeRef;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScopeAssignationStateChange implements ScopeAssignationWriteAttributes {

  /**
   * @autogenerated EntityStateChangeGenerator
   */
  public static class ScopeAssignationStateChangeBuilder {

    /**
     * @autogenerated EntityStateChangeGenerator
     * @return
     */
    public ScopeAssignationStateChangeBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param securityDomain
     * @return
     */
    public ScopeAssignationStateChangeBuilder securityDomain(
        final SecurityDomainRef securityDomain) {
      return securityDomain(Optional.of(SecurityDomainVO.from(securityDomain)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param securityDomain
     * @return
     */
    public ScopeAssignationStateChangeBuilder securityDomain(
        final Optional<SecurityDomainVO> securityDomain) {
      this.securityDomain$value = securityDomain;
      this.securityDomain$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param securityScope
     * @return
     */
    public ScopeAssignationStateChangeBuilder securityScope(final SecurityScopeRef securityScope) {
      return securityScope(Optional.of(SecurityScopeVO.from(securityScope)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param securityScope
     * @return
     */
    public ScopeAssignationStateChangeBuilder securityScope(
        final Optional<SecurityScopeVO> securityScope) {
      this.securityScope$value = securityScope;
      this.securityScope$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public ScopeAssignationStateChangeBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public ScopeAssignationStateChangeBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public ScopeAssignationStateChangeBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public ScopeAssignationStateChangeBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * El security domain de scope assignation
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<SecurityDomainVO> securityDomain = Optional.empty();

  /**
   * El security scope de scope assignation
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<SecurityScopeVO> securityScope = Optional.empty();

  /**
   * El uid de scope assignation
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de scope assignation para controlar bloqueos optimistas
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param attributes
   */
  public ScopeAssignationStateChange(final ScopeAssignationWriteAttributes attributes) {
    uid = attributes.getUid();
    securityDomain = attributes.getSecurityDomain();
    securityScope = attributes.getSecurityScope();
    version = attributes.getVersion();
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<SecurityDomainVO> getSecurityDomain() {
    return securityDomain;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<SecurityScopeVO> getSecurityScope() {
    return securityScope;
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
  public ScopeAssignationStateChange setSecurityDomain(final SecurityDomainRef value) {
    this.securityDomain = Optional.of(SecurityDomainVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public ScopeAssignationStateChange setSecurityScope(final SecurityScopeRef value) {
    this.securityScope = Optional.of(SecurityScopeVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public ScopeAssignationStateChange setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public ScopeAssignationStateChange setVersion(final Integer value) {
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
    if ("securityDomain".equals(field)) {
      this.unsetSecurityDomain();
    }
    if ("securityScope".equals(field)) {
      this.unsetSecurityScope();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public ScopeAssignationStateChange unsetSecurityDomain() {
    this.securityDomain = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public ScopeAssignationStateChange unsetSecurityScope() {
    this.securityScope = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public ScopeAssignationStateChange unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public ScopeAssignationStateChange unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
