package net.civeira.phylax.features.access.loginprovider.domain.gateway;

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
public class LoginProviderCursor {

  /**
   * max number of result
   *
   * @autogenerated CursorGenerator
   */
  private Integer limit;

  /**
   * @autogenerated CursorGenerator
   */
  private List<LoginProviderOrder> order;

  /**
   * @autogenerated CursorGenerator
   */
  private String sinceName;

  /**
   * If these value is set start retrieving for those whos uid is greater than these value
   *
   * @autogenerated CursorGenerator
   */
  private String sinceUid;

  /**
   * @autogenerated CursorGenerator
   * @return
   */
  public Optional<Integer> getLimit() {
    return Optional.ofNullable(limit);
  }

  /**
   * @autogenerated CursorGenerator
   * @return
   */
  public List<LoginProviderOrder> getOrder() {
    return null == order ? List.of() : List.copyOf(order);
  }

  /**
   * @autogenerated CursorGenerator
   * @return
   */
  public Optional<String> getSinceName() {
    return Optional.ofNullable(sinceName);
  }

  /**
   * @autogenerated CursorGenerator
   * @return
   */
  public Optional<String> getSinceUid() {
    return Optional.ofNullable(sinceUid);
  }
}
