package net.civeira.phylax.features.access.relyingparty.application.usecase.enable;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.civeira.phylax.features.access.relyingparty.application.visibility.RelyingPartyVisibleProjection;
import net.civeira.phylax.features.access.relyingparty.domain.RelyingParty;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.ApiKeyVO;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.CodeVO;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.EnabledVO;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.UidVO;
import net.civeira.phylax.features.access.relyingparty.domain.valueobject.VersionVO;

/**
 * A dto transfer to hold relying party attribute values
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RelyingPartyEnableProjection {

  /**
   * Create a dto instance with all the values of an entity
   *
   * @autogenerated ActionProjectionGenerator
   * @param visible The entity who provide values.
   * @return
   */
  public static RelyingPartyEnableProjection from(final RelyingPartyVisibleProjection visible) {
    RelyingPartyEnableProjection instance = new RelyingPartyEnableProjection();
    instance.uidValue = visible.getUid();
    instance.codeValue = visible.getCode();
    instance.apiKeyValue = visible.getApiKey();
    instance.enabledValue = visible.getEnabled();
    instance.versionValue = visible.getVersion();
    return instance;
  }

  /**
   * A identification for the aplication
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<ApiKeyVO> apiKeyValue;

  /**
   * El código identificativo de la aplicación
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<CodeVO> codeValue;

  /**
   * Una marca que permite quitar el acceso a una cuenta sin borrarla
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<EnabledVO> enabledValue;

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<UidVO> uidValue;

  /**
   * Campo con el número de version de relying party para controlar bloqueos optimistas
   *
   * @autogenerated ActionProjectionGenerator
   */
  private Optional<VersionVO> versionValue;

  /**
   * Create a dto instance with all the values of an entity
   *
   * @autogenerated ActionProjectionGenerator
   * @param entity The entity who provide values.
   */
  public RelyingPartyEnableProjection(final RelyingParty entity) {
    uidValue = Optional.of(entity.getUidValue());
    codeValue = Optional.of(entity.getCodeValue());
    apiKeyValue = Optional.of(entity.getApiKeyValue());
    enabledValue = Optional.of(entity.getEnabledValue());
    versionValue = Optional.of(entity.getVersionValue());
  }

  /**
   * Inform for a possible change propolsal in ApiKey
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for ApiKey, otherwise the value for ApiKey
   */
  public String getApiKey() {
    return getApiKeyOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in ApiKey
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for ApiKey, otherwise the value for ApiKey
   */
  public String getApiKeyOrDefault(final String orDefault) {
    return apiKeyValue.map(ApiKeyVO::getApiKey).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public String getCode() {
    return getCodeOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Code
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Code, otherwise the value for Code
   */
  public String getCodeOrDefault(final String orDefault) {
    return codeValue.map(CodeVO::getCode).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Boolean getEnabled() {
    return getEnabledOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Enabled
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Enabled, otherwise the value for Enabled
   */
  public Boolean getEnabledOrDefault(final Boolean orDefault) {
    return enabledValue.map(EnabledVO::isEnabled).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public String getUid() {
    return getUidOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Uid
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Uid, otherwise the value for Uid
   */
  public String getUidOrDefault(final String orDefault) {
    return uidValue.map(UidVO::getUid).orElse(orDefault);
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated ActionProjectionGenerator
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Integer getVersion() {
    return getVersionOrDefault(null);
  }

  /**
   * Inform for a possible change propolsal in Version
   *
   * @autogenerated ActionProjectionGenerator
   * @param orDefault Default value if is null
   * @return empty if there is no change proposal for Version, otherwise the value for Version
   */
  public Integer getVersionOrDefault(final Integer orDefault) {
    return versionValue.flatMap(VersionVO::getVersion).orElse(orDefault);
  }
}
