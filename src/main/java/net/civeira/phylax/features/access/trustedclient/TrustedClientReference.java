package net.civeira.phylax.features.access.trustedclient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.With;
import net.civeira.phylax.features.access.trustedclient.valueobject.UidVO;

@Getter
@ToString
@RequiredArgsConstructor
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrustedClientReference implements TrustedClientRef {

  /**
   * @autogenerated EntityReferenceImplGenerator
   * @param uid
   * @return
   */
  public static TrustedClientReference of(final String uid) {
    return new TrustedClientReference(UidVO.from(uid));
  }

  /**
   * El identificador de la aplicacion
   *
   * @autogenerated EntityReferenceImplGenerator
   */
  @EqualsAndHashCode.Include
  @NonNull
  private UidVO uid;

  /**
   * @autogenerated EntityReferenceImplGenerator
   * @return
   */
  public String getUidValue() {
    return getUid().getValue();
  }

  /**
   * @autogenerated EntityReferenceImplGenerator
   * @param uid
   * @return
   */
  public TrustedClientReference withUidValue(final String uid) {
    return withUid(UidVO.from(uid));
  }
}
