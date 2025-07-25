package net.civeira.phylax.features.access.tenantconfig.domain.gateway;

import java.util.List;
import java.util.Optional;

import net.civeira.phylax.common.algorithms.Slider;
import net.civeira.phylax.features.access.tenantconfig.domain.TenantConfig;
import net.civeira.phylax.features.access.tenantconfig.domain.TenantConfigRef;

public interface TenantConfigReadRepositoryGateway {

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The items that would be returned by the query
   */
  long count(TenantConfigFilter filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  boolean exists(String uid, Optional<TenantConfigFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  Optional<TenantConfig> find(TenantConfigFilter filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @return The slide with some values
   */
  List<TenantConfig> list(TenantConfigFilter filter);

  /**
   * Retrieve full data for a reference.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param reference a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  TenantConfig resolve(TenantConfigRef reference);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param uid
   * @param filter a filter to retrieve only matching values
   * @return Retrieve one single value
   */
  Optional<TenantConfig> retrieve(String uid, Optional<TenantConfigFilter> filter);

  /**
   * Recover a slide of data.
   *
   * @autogenerated ReadGatewayRepositoryGenerator
   * @param filter a filter to retrieve only matching values
   * @param cursor a cursor to order and skip
   * @return The slide with some values
   */
  Slider<TenantConfig> slide(TenantConfigFilter filter, TenantConfigCursor cursor);
}
