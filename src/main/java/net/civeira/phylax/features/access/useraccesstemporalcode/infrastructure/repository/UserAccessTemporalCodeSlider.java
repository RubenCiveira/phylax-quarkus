package net.civeira.phylax.features.access.useraccesstemporalcode.infrastructure.repository;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.UserAccessTemporalCode;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.gateway.UserAccessTemporalCodeCursor;
import net.civeira.phylax.features.access.useraccesstemporalcode.domain.gateway.UserAccessTemporalCodeFilter;

class UserAccessTemporalCodeSlider extends Slider<UserAccessTemporalCode> {

  /**
   * @autogenerated SlideGenerator
   */
  private final UserAccessTemporalCodeCursor cursor;

  /**
   * @autogenerated SlideGenerator
   */
  private final UserAccessTemporalCodeFilter filter;

  /**
   * @autogenerated SlideGenerator
   */
  private final BiFunction<UserAccessTemporalCodeFilter, UserAccessTemporalCodeCursor, Iterator<UserAccessTemporalCode>> gateway;

  /**
   * @autogenerated SlideGenerator
   * @param multi
   * @param limit
   * @param gateway
   * @param filter
   * @param cursor
   */
  UserAccessTemporalCodeSlider(final Iterator<UserAccessTemporalCode> multi, final int limit,
      final BiFunction<UserAccessTemporalCodeFilter, UserAccessTemporalCodeCursor, Iterator<UserAccessTemporalCode>> gateway,
      final UserAccessTemporalCodeFilter filter, final UserAccessTemporalCodeCursor cursor) {
    super(multi, limit);
    this.gateway = gateway;
    this.filter = filter;
    this.cursor = cursor;
  }

  /**
   * @autogenerated SlideGenerator
   * @param userAccessTemporalCodes
   * @param limit
   * @return
   */
  @Override
  public Iterator<UserAccessTemporalCode> next(List<UserAccessTemporalCode> userAccessTemporalCodes,
      int limit) {
    UserAccessTemporalCode last = userAccessTemporalCodes.get(userAccessTemporalCodes.size() - 1);
    UserAccessTemporalCodeCursor cr = this.cursor.withSinceUid(last.getUid()).withLimit(limit);
    return gateway.apply(this.filter, cr);
  }
}
