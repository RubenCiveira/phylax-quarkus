package net.civeira.phylax.features.access.trustedclient.application.visibility;

import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrustedClientVisibilityFilter {

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private String code;

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated VisibilityFilterGenerator
   */
  private String search;

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated VisibilityFilterGenerator
   */
  private String uid;

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated VisibilityFilterGenerator
   */
  private List<String> uids;

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<String> getCode() {
    return Optional.ofNullable(code);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<String> getSearch() {
    return Optional.ofNullable(search);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<String> getUid() {
    return Optional.ofNullable(uid);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public List<String> getUids() {
    return null == uids ? List.of() : List.copyOf(uids);
  }
}
