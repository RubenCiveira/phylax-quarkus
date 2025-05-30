package net.civeira.phylax.features.access.loginprovider.application.request;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.loginprovider.LoginProviderSourceOptions;
import net.civeira.phylax.features.access.loginprovider.command.LoginProviderWriteAttributes;
import net.civeira.phylax.features.access.loginprovider.valueobject.CertificateVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.DirectAccessVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.DisabledVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.MetadataVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.NameVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.PrivateKeyVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.PublicKeyVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.SourceVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.TenantVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.UidVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.UsersEnabledByDefaultVO;
import net.civeira.phylax.features.access.loginprovider.valueobject.VersionVO;
import net.civeira.phylax.features.access.tenant.TenantRef;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginProviderStateChange implements LoginProviderWriteAttributes {

  /**
   * @autogenerated EntityStateChangeGenerator
   */
  public static class LoginProviderStateChangeBuilder {

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param certificate
     * @return
     */
    public LoginProviderStateChangeBuilder certificate(final String certificate) {
      return certificate(Optional.of(CertificateVO.from(certificate)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param certificate
     * @return
     */
    public LoginProviderStateChangeBuilder certificate(final Optional<CertificateVO> certificate) {
      this.certificate$value = certificate;
      this.certificate$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param directAccess
     * @return
     */
    public LoginProviderStateChangeBuilder directAccess(final Boolean directAccess) {
      return directAccess(Optional.of(DirectAccessVO.from(directAccess)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param directAccess
     * @return
     */
    public LoginProviderStateChangeBuilder directAccess(
        final Optional<DirectAccessVO> directAccess) {
      this.directAccess$value = directAccess;
      this.directAccess$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param disabled
     * @return
     */
    public LoginProviderStateChangeBuilder disabled(final Boolean disabled) {
      return disabled(Optional.of(DisabledVO.from(disabled)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param disabled
     * @return
     */
    public LoginProviderStateChangeBuilder disabled(final Optional<DisabledVO> disabled) {
      this.disabled$value = disabled;
      this.disabled$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param metadata
     * @return
     */
    public LoginProviderStateChangeBuilder metadata(final String metadata) {
      return metadata(Optional.of(MetadataVO.from(metadata)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param metadata
     * @return
     */
    public LoginProviderStateChangeBuilder metadata(final Optional<MetadataVO> metadata) {
      this.metadata$value = metadata;
      this.metadata$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param name
     * @return
     */
    public LoginProviderStateChangeBuilder name(final String name) {
      return name(Optional.of(NameVO.from(name)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param name
     * @return
     */
    public LoginProviderStateChangeBuilder name(final Optional<NameVO> name) {
      this.name$value = name;
      this.name$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @return
     */
    public LoginProviderStateChangeBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param privateKey
     * @return
     */
    public LoginProviderStateChangeBuilder privateKey(final String privateKey) {
      return privateKey(Optional.of(PrivateKeyVO.from(privateKey)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param privateKey
     * @return
     */
    public LoginProviderStateChangeBuilder privateKey(final Optional<PrivateKeyVO> privateKey) {
      this.privateKey$value = privateKey;
      this.privateKey$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param publicKey
     * @return
     */
    public LoginProviderStateChangeBuilder publicKey(final String publicKey) {
      return publicKey(Optional.of(PublicKeyVO.from(publicKey)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param publicKey
     * @return
     */
    public LoginProviderStateChangeBuilder publicKey(final Optional<PublicKeyVO> publicKey) {
      this.publicKey$value = publicKey;
      this.publicKey$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param source
     * @return
     */
    public LoginProviderStateChangeBuilder source(final LoginProviderSourceOptions source) {
      return source(Optional.of(SourceVO.from(source)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param source
     * @return
     */
    public LoginProviderStateChangeBuilder source(final Optional<SourceVO> source) {
      this.source$value = source;
      this.source$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param tenant
     * @return
     */
    public LoginProviderStateChangeBuilder tenant(final TenantRef tenant) {
      return tenant(Optional.of(TenantVO.from(tenant)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param tenant
     * @return
     */
    public LoginProviderStateChangeBuilder tenant(final Optional<TenantVO> tenant) {
      this.tenant$value = tenant;
      this.tenant$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public LoginProviderStateChangeBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public LoginProviderStateChangeBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param usersEnabledByDefault
     * @return
     */
    public LoginProviderStateChangeBuilder usersEnabledByDefault(
        final Boolean usersEnabledByDefault) {
      return usersEnabledByDefault(
          Optional.of(UsersEnabledByDefaultVO.from(usersEnabledByDefault)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param usersEnabledByDefault
     * @return
     */
    public LoginProviderStateChangeBuilder usersEnabledByDefault(
        final Optional<UsersEnabledByDefaultVO> usersEnabledByDefault) {
      this.usersEnabledByDefault$value = usersEnabledByDefault;
      this.usersEnabledByDefault$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public LoginProviderStateChangeBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public LoginProviderStateChangeBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * The provider certificate used for signature verification, if required.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<CertificateVO> certificate = Optional.empty();

  /**
   * If true, the system will default to this login method without requiring selection.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<DirectAccessVO> directAccess = Optional.empty();

  /**
   * Indicates if this provider is currently disabled.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<DisabledVO> disabled = Optional.empty();

  /**
   * A metadata file required by some providers for configuration (e.g., SAML descriptor).
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<MetadataVO> metadata = Optional.empty();

  /**
   * A name that identifies this login provider within the tenant.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<NameVO> name = Optional.empty();

  /**
   * Private key used internally to validate codes returned by the identity provider.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<PrivateKeyVO> privateKey = Optional.empty();

  /**
   * A public key shared with users to interact with the identity provider.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<PublicKeyVO> publicKey = Optional.empty();

  /**
   * The source protocol or system used for authentication (e.g., GOOGLE, GITHUB, SAML).
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<SourceVO> source = Optional.empty();

  /**
   * The tenant this login provider is configured for.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<TenantVO> tenant = Optional.empty();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Defines whether the users created with this provider are enabled by default.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<UsersEnabledByDefaultVO> usersEnabledByDefault = Optional.empty();

  /**
   * Campo con el número de version de login provider para controlar bloqueos optimistas
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param attributes
   */
  public LoginProviderStateChange(final LoginProviderWriteAttributes attributes) {
    uid = attributes.getUid();
    tenant = attributes.getTenant();
    name = attributes.getName();
    source = attributes.getSource();
    disabled = attributes.getDisabled();
    directAccess = attributes.getDirectAccess();
    publicKey = attributes.getPublicKey();
    privateKey = attributes.getPrivateKey();
    certificate = attributes.getCertificate();
    metadata = attributes.getMetadata();
    usersEnabledByDefault = attributes.getUsersEnabledByDefault();
    version = attributes.getVersion();
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<CertificateVO> getCertificate() {
    return certificate;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<DirectAccessVO> getDirectAccess() {
    return directAccess;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<DisabledVO> getDisabled() {
    return disabled;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<MetadataVO> getMetadata() {
    return metadata;
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
  public Optional<PrivateKeyVO> getPrivateKey() {
    return privateKey;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<PublicKeyVO> getPublicKey() {
    return publicKey;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<SourceVO> getSource() {
    return source;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<TenantVO> getTenant() {
    return tenant;
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
  public Optional<UsersEnabledByDefaultVO> getUsersEnabledByDefault() {
    return usersEnabledByDefault;
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
  public LoginProviderStateChange setCertificate(final String value) {
    this.certificate = Optional.of(CertificateVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setDirectAccess(final Boolean value) {
    this.directAccess = Optional.of(DirectAccessVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setDisabled(final Boolean value) {
    this.disabled = Optional.of(DisabledVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setMetadata(final String value) {
    this.metadata = Optional.of(MetadataVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setName(final String value) {
    this.name = Optional.of(NameVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setPrivateKey(final String value) {
    this.privateKey = Optional.of(PrivateKeyVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setPublicKey(final String value) {
    this.publicKey = Optional.of(PublicKeyVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setSource(final LoginProviderSourceOptions value) {
    this.source = Optional.of(SourceVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setTenant(final TenantRef value) {
    this.tenant = Optional.of(TenantVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setUsersEnabledByDefault(final Boolean value) {
    this.usersEnabledByDefault = Optional.of(UsersEnabledByDefaultVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public LoginProviderStateChange setVersion(final Integer value) {
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
    if ("tenant".equals(field)) {
      this.unsetTenant();
    }
    if ("name".equals(field)) {
      this.unsetName();
    }
    if ("source".equals(field)) {
      this.unsetSource();
    }
    if ("disabled".equals(field)) {
      this.unsetDisabled();
    }
    if ("directAccess".equals(field)) {
      this.unsetDirectAccess();
    }
    if ("publicKey".equals(field)) {
      this.unsetPublicKey();
    }
    if ("privateKey".equals(field)) {
      this.unsetPrivateKey();
    }
    if ("certificate".equals(field)) {
      this.unsetCertificate();
    }
    if ("metadata".equals(field)) {
      this.unsetMetadata();
    }
    if ("usersEnabledByDefault".equals(field)) {
      this.unsetUsersEnabledByDefault();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetCertificate() {
    this.certificate = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetDirectAccess() {
    this.directAccess = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetDisabled() {
    this.disabled = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetMetadata() {
    this.metadata = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetName() {
    this.name = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetPrivateKey() {
    this.privateKey = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetPublicKey() {
    this.publicKey = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetSource() {
    this.source = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetTenant() {
    this.tenant = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetUsersEnabledByDefault() {
    this.usersEnabledByDefault = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public LoginProviderStateChange unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
