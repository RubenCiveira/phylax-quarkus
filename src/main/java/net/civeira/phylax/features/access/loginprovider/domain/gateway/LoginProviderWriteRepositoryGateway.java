package net.civeira.phylax.features.access.loginprovider.domain.gateway;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.loginprovider.domain.LoginProvider;
import net.civeira.phylax.features.access.loginprovider.domain.LoginProviderRef;

public interface LoginProviderWriteRepositoryGateway {

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The items that would be returned by the query
   */
  long countForUpdate(LoginProviderFilter filter);

  /**
   * Create a new record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   * @param verifier a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  LoginProvider create(LoginProvider entity, Predicate<LoginProvider> verifier);

  /**
   * Create a new record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  LoginProvider create(LoginProvider entity);

  /**
   * Delete an existing record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   */
  void delete(LoginProvider entity);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  boolean existsForUpdate(String uid, Optional<LoginProviderFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  Optional<LoginProvider> findForUpdate(LoginProviderFilter filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  List<LoginProvider> listForUpdate(LoginProviderFilter filter);

  /**
   * Retrieve full data for a reference.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  LoginProvider resolveForUpdate(LoginProviderRef reference);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Optional<LoginProvider> retrieveForUpdate(String uid, Optional<LoginProviderFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @param cursor a cursor to order and skip
   * @return The slide with some values
   */
  Slider<LoginProvider> slideForUpdate(LoginProviderFilter filter, LoginProviderCursor cursor);

  /**
   * Create a new record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @param entity a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  LoginProvider update(LoginProviderRef reference, LoginProvider entity);
}
