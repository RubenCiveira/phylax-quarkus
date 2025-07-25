package net.civeira.phylax.features.access.tenantconfig.domain.gateway;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.tenantconfig.domain.TenantConfig;
import net.civeira.phylax.features.access.tenantconfig.domain.TenantConfigRef;

public interface TenantConfigWriteRepositoryGateway {

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The items that would be returned by the query
   */
  long countForUpdate(TenantConfigFilter filter);

  /**
   * Create a new record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   * @param verifier a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  TenantConfig create(TenantConfig entity, Predicate<TenantConfig> verifier);

  /**
   * Create a new record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  TenantConfig create(TenantConfig entity);

  /**
   * Delete an existing record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param entity a filter to retrieve only matching values
   */
  void delete(TenantConfig entity);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  boolean existsForUpdate(String uid, Optional<TenantConfigFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  Optional<TenantConfig> findForUpdate(TenantConfigFilter filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  List<TenantConfig> listForUpdate(TenantConfigFilter filter);

  /**
   * Retrieve full data for a reference.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  TenantConfig resolveForUpdate(TenantConfigRef reference);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Optional<TenantConfig> retrieveForUpdate(String uid, Optional<TenantConfigFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @param cursor a cursor to order and skip
   * @return The slide with some values
   */
  Slider<TenantConfig> slideForUpdate(TenantConfigFilter filter, TenantConfigCursor cursor);

  /**
   * Create a new record.
   *
   * @autogenerated WriteGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @param entity a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  TenantConfig update(TenantConfigRef reference, TenantConfig entity);
}
