package net.civeira.phylax.features.access.trustedclient.application.request;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.civeira.phylax.common.value.Uuid;
import net.civeira.phylax.features.access.trustedclient.command.TrustedClientWriteAttributes;
import net.civeira.phylax.features.access.trustedclient.valueobject.AllowedRedirectsVO;
import net.civeira.phylax.features.access.trustedclient.valueobject.CodeVO;
import net.civeira.phylax.features.access.trustedclient.valueobject.EnabledVO;
import net.civeira.phylax.features.access.trustedclient.valueobject.PublicAllowVO;
import net.civeira.phylax.features.access.trustedclient.valueobject.SecretOauthVO;
import net.civeira.phylax.features.access.trustedclient.valueobject.UidVO;
import net.civeira.phylax.features.access.trustedclient.valueobject.VersionVO;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrustedClientStateChange implements TrustedClientWriteAttributes {

  /**
   * @autogenerated EntityStateChangeGenerator
   */
  public static class TrustedClientStateChangeBuilder {

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param allowedRedirects
     * @return
     */
    public TrustedClientStateChangeBuilder allowedRedirects(final String allowedRedirects) {
      return allowedRedirects(Optional.of(AllowedRedirectsVO.from(allowedRedirects)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param allowedRedirects
     * @return
     */
    public TrustedClientStateChangeBuilder allowedRedirects(
        final Optional<AllowedRedirectsVO> allowedRedirects) {
      this.allowedRedirects$value = allowedRedirects;
      this.allowedRedirects$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param code
     * @return
     */
    public TrustedClientStateChangeBuilder code(final String code) {
      return code(Optional.of(CodeVO.from(code)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param code
     * @return
     */
    public TrustedClientStateChangeBuilder code(final Optional<CodeVO> code) {
      this.code$value = code;
      this.code$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param enabled
     * @return
     */
    public TrustedClientStateChangeBuilder enabled(final Boolean enabled) {
      return enabled(Optional.of(EnabledVO.from(enabled)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param enabled
     * @return
     */
    public TrustedClientStateChangeBuilder enabled(final Optional<EnabledVO> enabled) {
      this.enabled$value = enabled;
      this.enabled$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @return
     */
    public TrustedClientStateChangeBuilder newUid() {
      return version(0).uid(Uuid.comb().toString());
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param publicAllow
     * @return
     */
    public TrustedClientStateChangeBuilder publicAllow(final Boolean publicAllow) {
      return publicAllow(Optional.of(PublicAllowVO.from(publicAllow)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param publicAllow
     * @return
     */
    public TrustedClientStateChangeBuilder publicAllow(final Optional<PublicAllowVO> publicAllow) {
      this.publicAllow$value = publicAllow;
      this.publicAllow$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param secretOauth
     * @return
     */
    public TrustedClientStateChangeBuilder secretOauth(final String secretOauth) {
      return secretOauth(Optional.of(SecretOauthVO.fromPlain(secretOauth)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param secretOauth
     * @return
     */
    public TrustedClientStateChangeBuilder secretOauth(final Optional<SecretOauthVO> secretOauth) {
      this.secretOauth$value = secretOauth;
      this.secretOauth$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public TrustedClientStateChangeBuilder uid(final String uid) {
      return uid(Optional.of(UidVO.from(uid)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param uid
     * @return
     */
    public TrustedClientStateChangeBuilder uid(final Optional<UidVO> uid) {
      this.uid$value = uid;
      this.uid$set = true;
      return this;
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public TrustedClientStateChangeBuilder version(final Integer version) {
      return version(Optional.of(VersionVO.from(version)));
    }

    /**
     * @autogenerated EntityStateChangeGenerator
     * @param version
     * @return
     */
    public TrustedClientStateChangeBuilder version(final Optional<VersionVO> version) {
      this.version$value = version;
      this.version$set = true;
      return this;
    }
  }

  /**
   * Si permitimos login directo de la app, obligamos a indicar un secreto.
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<AllowedRedirectsVO> allowedRedirects = Optional.empty();

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<CodeVO> code = Optional.empty();

  /**
   * Una marca que permite quitar el acceso a una cuenta sin borrarla
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<EnabledVO> enabled = Optional.empty();

  /**
   * If true, users can use these client to access with public code flow
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<PublicAllowVO> publicAllow = Optional.empty();

  /**
   * If the user is not delegated, the phrasse to identify
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<SecretOauthVO> secretOauth = Optional.empty();

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<UidVO> uid = Optional.empty();

  /**
   * Campo con el número de version de trusted client para controlar bloqueos optimistas
   *
   * @autogenerated EntityStateChangeGenerator
   */
  @Builder.Default
  private Optional<VersionVO> version = Optional.empty();

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param attributes
   */
  public TrustedClientStateChange(final TrustedClientWriteAttributes attributes) {
    uid = attributes.getUid();
    code = attributes.getCode();
    publicAllow = attributes.getPublicAllow();
    secretOauth = attributes.getSecretOauth();
    allowedRedirects = attributes.getAllowedRedirects();
    enabled = attributes.getEnabled();
    version = attributes.getVersion();
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<AllowedRedirectsVO> getAllowedRedirects() {
    return allowedRedirects;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<CodeVO> getCode() {
    return code;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<EnabledVO> getEnabled() {
    return enabled;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<PublicAllowVO> getPublicAllow() {
    return publicAllow;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  @Override
  public Optional<SecretOauthVO> getSecretOauth() {
    return secretOauth;
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
  public TrustedClientStateChange setAllowedRedirects(final String value) {
    this.allowedRedirects = Optional.of(AllowedRedirectsVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TrustedClientStateChange setCode(final String value) {
    this.code = Optional.of(CodeVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TrustedClientStateChange setEnabled(final Boolean value) {
    this.enabled = Optional.of(EnabledVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TrustedClientStateChange setPublicAllow(final Boolean value) {
    this.publicAllow = Optional.of(PublicAllowVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TrustedClientStateChange setSecretOauth(final String value) {
    this.secretOauth = Optional.of(SecretOauthVO.fromPlain(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TrustedClientStateChange setUid(final String value) {
    this.uid = Optional.of(UidVO.from(value));
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @param value
   * @return
   */
  public TrustedClientStateChange setVersion(final Integer value) {
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
    if ("code".equals(field)) {
      this.unsetCode();
    }
    if ("publicAllow".equals(field)) {
      this.unsetPublicAllow();
    }
    if ("secretOauth".equals(field)) {
      this.unsetSecretOauth();
    }
    if ("allowedRedirects".equals(field)) {
      this.unsetAllowedRedirects();
    }
    if ("enabled".equals(field)) {
      this.unsetEnabled();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TrustedClientStateChange unsetAllowedRedirects() {
    this.allowedRedirects = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TrustedClientStateChange unsetCode() {
    this.code = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TrustedClientStateChange unsetEnabled() {
    this.enabled = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TrustedClientStateChange unsetPublicAllow() {
    this.publicAllow = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TrustedClientStateChange unsetSecretOauth() {
    this.secretOauth = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TrustedClientStateChange unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateChangeGenerator
   * @return
   */
  public TrustedClientStateChange unsetVersion() {
    this.version = Optional.empty();
    return this;
  }
}
