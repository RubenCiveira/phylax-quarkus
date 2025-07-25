package net.civeira.phylax.features.access.useridentity.application.visibility;

import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import net.civeira.phylax.features.access.relyingparty.domain.RelyingPartyRef;
import net.civeira.phylax.features.access.trustedclient.domain.TrustedClientRef;
import net.civeira.phylax.features.access.user.domain.UserRef;

@Data
@Builder(toBuilder = true)
@With
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserIdentityVisibilityFilter {

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private RelyingPartyRef relyingParty;

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private List<String> relyingPartys;

  /**
   * The filter to select the results to retrieve
   *
   * @autogenerated VisibilityFilterGenerator
   */
  private String search;

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private TrustedClientRef trustedClient;

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private List<String> trustedClients;

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
   */
  private UserRef user;

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private String userTenantTenantAccesible;

  /**
   * @autogenerated VisibilityFilterGenerator
   */
  private List<String> users;

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<RelyingPartyRef> getRelyingParty() {
    return Optional.ofNullable(relyingParty);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public List<String> getRelyingPartys() {
    return null == relyingPartys ? List.of() : List.copyOf(relyingPartys);
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
  public Optional<TrustedClientRef> getTrustedClient() {
    return Optional.ofNullable(trustedClient);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public List<String> getTrustedClients() {
    return null == trustedClients ? List.of() : List.copyOf(trustedClients);
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

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<UserRef> getUser() {
    return Optional.ofNullable(user);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public Optional<String> getUserTenantTenantAccesible() {
    return Optional.ofNullable(userTenantTenantAccesible);
  }

  /**
   * @autogenerated VisibilityFilterGenerator
   * @return
   */
  public List<String> getUsers() {
    return null == users ? List.of() : List.copyOf(users);
  }
}
