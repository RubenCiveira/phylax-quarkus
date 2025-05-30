package net.civeira.phylax.features.access.useracceptedtermnsofuse.gateway;

import java.util.List;
import java.util.Optional;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.UserAcceptedTermnsOfUse;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.UserAcceptedTermnsOfUseRef;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.query.UserAcceptedTermnsOfUseCursor;
import net.civeira.phylax.features.access.useracceptedtermnsofuse.query.UserAcceptedTermnsOfUseFilter;

public interface UserAcceptedTermnsOfUseReadRepositoryGateway {

  /**
   * The items that would be returned by the query
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The items that would be returned by the query
   */
  long count(UserAcceptedTermnsOfUseFilter filter);

  /**
   * Retrieve one single value
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  UserAcceptedTermnsOfUse enrich(UserAcceptedTermnsOfUseRef reference);

  /**
   * Retrieve one single value
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  boolean exists(String uid, Optional<UserAcceptedTermnsOfUseFilter> filter);

  /**
   * The slide with some values
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  Optional<UserAcceptedTermnsOfUse> find(UserAcceptedTermnsOfUseFilter filter);

  /**
   * The slide with some values
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  List<UserAcceptedTermnsOfUse> list(UserAcceptedTermnsOfUseFilter filter);

  /**
   * Retrieve one single value
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Optional<UserAcceptedTermnsOfUse> retrieve(String uid,
      Optional<UserAcceptedTermnsOfUseFilter> filter);

  /**
   * The slide with some values
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @param cursor a cursor to order and skip
   * @return The slide with some values
   */
  Slider<UserAcceptedTermnsOfUse> slide(UserAcceptedTermnsOfUseFilter filter,
      UserAcceptedTermnsOfUseCursor cursor);
}
