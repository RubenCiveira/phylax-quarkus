package net.civeira.phylax.features.access.scopeassignation.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Delegate;
import net.civeira.phylax.features.access.scopeassignation.domain.valueobject.UidVO;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ScopeAssignationReference implements ScopeAssignationRef {

  /**
   * @autogenerated EntityReferenceImplGenerator
   * @param uid
   * @return
   */
  public static ScopeAssignationReference of(final String uid) {
    return new ScopeAssignationReference(UidVO.from(uid));
  }

  /**
   * El uid de scope assignation
   *
   * @autogenerated EntityReferenceImplGenerator
   */
  @Delegate
  @NonNull
  private UidVO uidValue;
}
