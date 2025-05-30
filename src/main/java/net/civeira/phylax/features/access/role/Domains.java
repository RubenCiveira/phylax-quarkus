package net.civeira.phylax.features.access.role;

import java.util.Optional;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.role.valueobject.SecurityDomainVO;
import net.civeira.phylax.features.access.role.valueobject.UidVO;
import net.civeira.phylax.features.access.role.valueobject.VersionVO;
import net.civeira.phylax.features.access.securitydomain.SecurityDomainRef;

@Getter
@ToString
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Domains {

  /**
   * @autogenerated UnionGenerator
   */
  public static class DomainsBuilder {

    /**
     * @autogenerated UnionGenerator
     * @return
     */
    public DomainsBuilder newUid() {
      version(0);
      return uid(Uuid.comb().toString());
    }

    /**
     * @autogenerated UnionGenerator
     * @param securityDomain
     * @return
     */
    public DomainsBuilder securityDomain(final SecurityDomainRef securityDomain) {
      return securityDomain(SecurityDomainVO.from(securityDomain));
    }

    /**
     * @autogenerated UnionGenerator
     * @param securityDomain
     * @return
     */
    public DomainsBuilder securityDomain(final SecurityDomainVO securityDomain) {
      this.securityDomain = securityDomain;
      return this;
    }

    /**
     * @autogenerated UnionGenerator
     * @param securityDomain
     * @return
     */
    public DomainsBuilder securityDomain(final String securityDomain) {
      return securityDomain(SecurityDomainVO.fromReference(securityDomain));
    }

    /**
     * @autogenerated UnionGenerator
     * @param uid
     * @return
     */
    public DomainsBuilder uid(final String uid) {
      return uid(UidVO.from(uid));
    }

    /**
     * @autogenerated UnionGenerator
     * @param uid
     * @return
     */
    public DomainsBuilder uid(final UidVO uid) {
      this.uid = uid;
      return this;
    }

    /**
     * @autogenerated UnionGenerator
     * @param version
     * @return
     */
    public DomainsBuilder version(final Integer version) {
      return version(VersionVO.from(version));
    }

    /**
     * @autogenerated UnionGenerator
     * @param version
     * @return
     */
    public DomainsBuilder version(final VersionVO version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * El security domain de role domain
   *
   * @autogenerated UnionGenerator
   */
  @NonNull
  private SecurityDomainVO securityDomain;

  /**
   * A uid string to identify the entity
   *
   * @autogenerated UnionGenerator
   */
  @NonNull
  private UidVO uid;

  /**
   * Campo con el número de version de role domain para controlar bloqueos optimistas
   *
   * @autogenerated UnionGenerator
   */
  @NonNull
  @Builder.Default
  private VersionVO version = VersionVO.nullValue();

  /**
   * @autogenerated UnionGenerator
   * @return
   */
  public String getSecurityDomainReferenceValue() {
    return getSecurityDomain().getReferenceValue();
  }

  /**
   * @autogenerated UnionGenerator
   * @return
   */
  public SecurityDomainRef getSecurityDomainValue() {
    return getSecurityDomain().getValue();
  }

  /**
   * @autogenerated UnionGenerator
   * @return
   */
  public String getUidValue() {
    return getUid().getValue();
  }

  /**
   * @autogenerated UnionGenerator
   * @return
   */
  public Optional<Integer> getVersionValue() {
    return getVersion().getValue();
  }

  /**
   * @autogenerated UnionGenerator
   * @return
   */
  public Domains withNullVersion() {
    return withVersion(VersionVO.nullValue());
  }

  /**
   * @autogenerated UnionGenerator
   * @param securityDomain
   * @return
   */
  public Domains withSecurityDomain(final SecurityDomainRef securityDomain) {
    return withSecurityDomain(SecurityDomainVO.from(securityDomain));
  }

  /**
   * @autogenerated UnionGenerator
   * @param securityDomain
   * @return
   */
  public Domains withSecurityDomain(final SecurityDomainVO securityDomain) {
    return toBuilder().securityDomain(securityDomain).build();
  }

  /**
   * @autogenerated UnionGenerator
   * @param securityDomain
   * @return
   */
  public Domains withSecurityDomainReferenceValue(final String securityDomain) {
    return withSecurityDomain(SecurityDomainVO.fromReference(securityDomain));
  }

  /**
   * @autogenerated UnionGenerator
   * @param uid
   * @return
   */
  public Domains withUid(final String uid) {
    return withUid(UidVO.from(uid));
  }

  /**
   * @autogenerated UnionGenerator
   * @param uid
   * @return
   */
  public Domains withUid(final UidVO uid) {
    return toBuilder().uid(uid).build();
  }

  /**
   * @autogenerated UnionGenerator
   * @param version
   * @return
   */
  public Domains withVersion(final Integer version) {
    return withVersion(VersionVO.from(version));
  }

  /**
   * @autogenerated UnionGenerator
   * @param version
   * @return
   */
  public Domains withVersion(final VersionVO version) {
    return toBuilder().version(version).build();
  }
}
