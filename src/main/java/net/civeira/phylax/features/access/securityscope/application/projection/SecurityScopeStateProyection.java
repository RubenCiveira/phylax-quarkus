package net.civeira.phylax.features.access.securityscope.application.projection;

import java.util.Optional;

import net.civeira.phylax.features.access.relyingparty.RelyingPartyRef;
import net.civeira.phylax.features.access.securityscope.SecurityScope;
import net.civeira.phylax.features.access.securityscope.SecurityScopeKindOptions;
import net.civeira.phylax.features.access.securityscope.SecurityScopeVisibilityOptions;
import net.civeira.phylax.features.access.trustedclient.TrustedClientRef;

public class SecurityScopeStateProyection {

  /**
   * El enabled de security scope
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<Boolean> enabled = Optional.empty();

  /**
   * El kind de security scope
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<SecurityScopeKindOptions> kind = Optional.empty();

  /**
   * El relying party de security scope
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<RelyingPartyRef> relyingParty = Optional.empty();

  /**
   * A label group key to show the scope
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<String> resource = Optional.empty();

  /**
   * A label to show the scope
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<String> scope = Optional.empty();

  /**
   * El trusted client de security scope
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<TrustedClientRef> trustedClient = Optional.empty();

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<String> uid = Optional.empty();

  /**
   * Campo con el número de version de security scope para controlar bloqueos optimistas
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<Integer> version = Optional.empty();

  /**
   * El visibility de security scope
   *
   * @autogenerated EntityStateProyectionGenerator
   */
  private Optional<SecurityScopeVisibilityOptions> visibility = Optional.empty();

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param entity
   */
  public SecurityScopeStateProyection(final SecurityScope entity) {
    uid = Optional.of(entity.getUid().getValue());
    trustedClient = entity.getTrustedClient().getValue();
    relyingParty = entity.getRelyingParty().getValue();
    resource = Optional.of(entity.getResource().getValue());
    scope = Optional.of(entity.getScope().getValue());
    enabled = entity.getEnabled().getValue();
    kind = entity.getKind().getValue();
    visibility = entity.getVisibility().getValue();
    version = entity.getVersion().getValue();
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<Boolean> getEnabled() {
    return enabled;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<SecurityScopeKindOptions> getKind() {
    return kind;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<RelyingPartyRef> getRelyingParty() {
    return relyingParty;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<String> getResource() {
    return resource;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<String> getScope() {
    return scope;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<TrustedClientRef> getTrustedClient() {
    return trustedClient;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<String> getUid() {
    return uid;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<Integer> getVersion() {
    return version;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public Optional<SecurityScopeVisibilityOptions> getVisibility() {
    return visibility;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setEnabled(final Optional<Boolean> value) {
    this.enabled = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setKind(final Optional<SecurityScopeKindOptions> value) {
    this.kind = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setRelyingParty(final Optional<RelyingPartyRef> value) {
    this.relyingParty = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setResource(final Optional<String> value) {
    this.resource = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setScope(final Optional<String> value) {
    this.scope = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setTrustedClient(final Optional<TrustedClientRef> value) {
    this.trustedClient = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setUid(final Optional<String> value) {
    this.uid = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setVersion(final Optional<Integer> value) {
    this.version = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param value
   * @return
   */
  public SecurityScopeStateProyection setVisibility(
      final Optional<SecurityScopeVisibilityOptions> value) {
    this.visibility = value;
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @param field
   */
  public void unset(final String field) {
    if ("uid".equals(field)) {
      this.unsetUid();
    }
    if ("trustedClient".equals(field)) {
      this.unsetTrustedClient();
    }
    if ("relyingParty".equals(field)) {
      this.unsetRelyingParty();
    }
    if ("resource".equals(field)) {
      this.unsetResource();
    }
    if ("scope".equals(field)) {
      this.unsetScope();
    }
    if ("enabled".equals(field)) {
      this.unsetEnabled();
    }
    if ("kind".equals(field)) {
      this.unsetKind();
    }
    if ("visibility".equals(field)) {
      this.unsetVisibility();
    }
    if ("version".equals(field)) {
      this.unsetVersion();
    }
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetEnabled() {
    this.enabled = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetKind() {
    this.kind = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetRelyingParty() {
    this.relyingParty = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetResource() {
    this.resource = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetScope() {
    this.scope = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetTrustedClient() {
    this.trustedClient = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetUid() {
    this.uid = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetVersion() {
    this.version = Optional.empty();
    return this;
  }

  /**
   * @autogenerated EntityStateProyectionGenerator
   * @return
   */
  public SecurityScopeStateProyection unsetVisibility() {
    this.visibility = Optional.empty();
    return this;
  }
}
