package net.civeira.phylax.features.access.useridentity.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Delegate;
import net.civeira.phylax.features.access.useridentity.domain.valueobject.UidVO;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserIdentityReference implements UserIdentityRef {

  /**
   * @autogenerated EntityReferenceImplGenerator
   * @param uid
   * @return
   */
  public static UserIdentityReference of(final String uid) {
    return new UserIdentityReference(UidVO.from(uid));
  }

  /**
   * A uid string to identify the entity
   *
   * @autogenerated EntityReferenceImplGenerator
   */
  @Delegate
  @NonNull
  private UidVO uidValue;
}
