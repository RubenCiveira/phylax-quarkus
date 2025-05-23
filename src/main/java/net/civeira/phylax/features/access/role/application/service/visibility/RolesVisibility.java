package net.civeira.phylax.features.access.role.application.service.visibility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.exception.NotFoundException;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.role.Role;
import net.civeira.phylax.features.access.role.RoleFacade;
import net.civeira.phylax.features.access.role.RoleRef;
import net.civeira.phylax.features.access.role.application.projection.RoleStateProyection;
import net.civeira.phylax.features.access.role.application.request.RoleStateChange;
import net.civeira.phylax.features.access.role.gateway.RoleCacheGateway;
import net.civeira.phylax.features.access.role.gateway.RoleCached;
import net.civeira.phylax.features.access.role.gateway.RoleReadRepositoryGateway;
import net.civeira.phylax.features.access.role.gateway.RoleWriteRepositoryGateway;
import net.civeira.phylax.features.access.role.query.RoleCursor;
import net.civeira.phylax.features.access.role.query.RoleFilter;
import net.civeira.phylax.features.access.role.valueobject.DomainsVO;
import net.civeira.phylax.features.access.role.valueobject.TenantVO;
import net.civeira.phylax.features.access.securitydomain.application.service.visibility.SecurityDomainsVisibility;
import net.civeira.phylax.features.access.tenant.application.service.visibility.TenantsVisibility;

@ApplicationScoped
@RequiredArgsConstructor
public class RolesVisibility {

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final RoleFacade aggregate;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final RoleCacheGateway cache;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final Event<RoleFilterProposal> filterProposalEmitter;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final Event<RoleFixedFieldsProposal> fixedFieldsEmitter;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final Event<RoleVisibleContentProposal> guardEmitter;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final Event<RoleHiddenFieldsProposal> hiddenFieldsEmitter;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final Event<RolePresetProposal> presetEmitter;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final RoleReadRepositoryGateway roleReadRepositoryGateway;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final RoleWriteRepositoryGateway roleWriteRepositoryGateway;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final SecurityDomainsVisibility securityDomainsVisibility;

  /**
   * @autogenerated VisibilityServiceGenerator
   */
  private final TenantsVisibility tenantsVisibility;

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param uid
   * @return The input entity with the copy values without hidden
   */
  public boolean checkVisibility(Interaction prev, String uid) {
    return retrieveVisible(prev, uid).isPresent();
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param uids
   * @return The input entity with the copy values without hidden
   */
  public boolean checkVisibility(Interaction prev, List<String> uids) {
    return uids.size() == listVisibles(prev, RoleFilter.builder().uids(uids).build(),
        RoleCursor.builder().build()).size();
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param source The source interaction
   * @param original The source interaction
   * @return The input entity with the copy values without hidden
   */
  public RoleStateChange copyWithFixed(Interaction prev, RoleStateChange source, Role original) {
    RoleStateChange withReferences = visiblesReferences(prev, source);
    fieldsToFix(prev, original).forEach(withReferences::unset);
    RolePresetProposal proposal = RolePresetProposal.builder().dto(withReferences).interaction(prev)
        .original(original).build();
    presetEmitter.fire(proposal);
    return proposal.getDto();
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param source The source interaction
   * @return The input entity with the copy values without hidden
   */
  public RoleStateChange copyWithFixed(Interaction prev, RoleStateChange source) {
    RoleStateChange withReferences = visiblesReferences(prev, source);
    fieldsToFix(prev).forEach(withReferences::unset);
    RolePresetProposal proposal =
        RolePresetProposal.builder().dto(withReferences).interaction(prev).build();
    presetEmitter.fire(proposal);
    return proposal.getDto();
  }

  /**
   * The input dto with hidden values
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param role
   * @return The input dto with hidden values
   */
  public RoleStateProyection copyWithHidden(Interaction prev, Role role) {
    RoleStateProyection target = new RoleStateProyection(role);
    fieldsToHide(prev, role).forEach(target::unset);
    return target;
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param filter The filter to retrieve values
   * @return The input entity with the copy values without hidden
   */
  public long countVisibles(Interaction prev, RoleFilter filter) {
    return roleReadRepositoryGateway.count(applyPreVisibilityFilter(prev, filter));
  }

  /**
   * initialsFixFields
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @return initialsFixFields
   */
  public Set<String> fieldsToFix(Interaction prev) {
    Set<String> fields = new HashSet<>(fieldsToHide(prev));
    fields.addAll(aggregate.readonlyFields());
    RoleFixedFieldsProposal proposal =
        RoleFixedFieldsProposal.builder().fields(fields).query(prev).build();
    fixedFieldsEmitter.fire(proposal);
    return proposal.getFields();
  }

  /**
   * initialsFixFields
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param role
   * @return initialsFixFields
   */
  public Set<String> fieldsToFix(Interaction prev, RoleRef role) {
    Set<String> fields = new HashSet<>(fieldsToHide(prev, role));
    fields.addAll(aggregate.readonlyFields());
    RoleFixedFieldsProposal proposal =
        RoleFixedFieldsProposal.builder().fields(fields).role(role).query(prev).build();
    fixedFieldsEmitter.fire(proposal);
    return proposal.getFields();
  }

  /**
   * initialsHideFields
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @return initialsHideFields
   */
  public Set<String> fieldsToHide(Interaction prev) {
    Set<String> fields = new HashSet<>();
    RoleHiddenFieldsProposal proposal =
        RoleHiddenFieldsProposal.builder().fields(fields).query(prev).build();
    hiddenFieldsEmitter.fire(proposal);
    return proposal.getFields();
  }

  /**
   * initialsHideFields
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param role
   * @return initialsHideFields
   */
  public Set<String> fieldsToHide(Interaction prev, RoleRef role) {
    Set<String> fields = new HashSet<>(fieldsToHide(prev));
    RoleHiddenFieldsProposal proposal =
        RoleHiddenFieldsProposal.builder().fields(fields).role(role).query(prev).build();
    hiddenFieldsEmitter.fire(proposal);
    return proposal.getFields();
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param filter The filter to retrieve values
   * @param cursor The filter to retrieve values
   * @return The input entity with the copy values without hidden
   */
  public RoleCached listCachedVisibles(Interaction prev, RoleFilter filter, RoleCursor cursor) {
    RoleFilter visibleFilter = applyPreVisibilityFilter(prev, filter);
    return cache.retrieve(visibleFilter, cursor)
        .orElseGet(() -> cache.store(visibleFilter, cursor, listVisibles(prev, filter, cursor)));
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param filter The filter to retrieve values
   * @param cursor The filter to retrieve values
   * @return The input entity with the copy values without hidden
   */
  public List<Role> listVisibles(Interaction prev, RoleFilter filter, RoleCursor cursor) {
    return queryItems(prev, applyPreVisibilityFilter(prev, filter), cursor);
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param filter The filter to retrieve values
   * @param cursor The filter to retrieve values
   * @return The input entity with the copy values without hidden
   */
  public List<Role> listVisiblesForUpdate(Interaction prev, RoleFilter filter, RoleCursor cursor) {
    return queryItemsForUpdate(prev, applyPreVisibilityFilter(prev, filter), cursor);
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param uid
   * @return The input entity with the copy values without hidden
   */
  public RoleCached retrieveCachedVisible(Interaction prev, String uid) {
    RoleCursor cursor = RoleCursor.builder().limit(1).build();
    RoleFilter visibleFilter =
        applyPreVisibilityFilter(prev, RoleFilter.builder().uid(uid).build());
    return cache.retrieve(visibleFilter, cursor).orElseGet(() -> {
      List<Role> list = retrieveVisible(prev, uid).<List<Role>>map(List::of).orElseGet(List::of);
      return cache.store(visibleFilter, cursor, list);
    });
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param uid
   * @return The input entity with the copy values without hidden
   */
  public Optional<Role> retrieveVisible(Interaction prev, String uid) {
    RoleFilter filter = applyPreVisibilityFilter(prev, RoleFilter.builder().uid(uid).build());
    return queryItem(prev, uid, filter);
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param uid
   * @return The input entity with the copy values without hidden
   */
  public Optional<Role> retrieveVisibleForUpdate(Interaction prev, String uid) {
    RoleFilter filter = applyPreVisibilityFilter(prev, RoleFilter.builder().uid(uid).build());
    return queryItemForUpdate(prev, uid, filter);
  }

  /**
   * The self filter modified with the prepared values.
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param filter The filter to retrieve values
   * @return The self filter modified with the prepared values.
   */
  private RoleFilter applyPreVisibilityFilter(Interaction prev, RoleFilter filter) {
    RoleFilterProposal proposal =
        RoleFilterProposal.builder().filter(filter).interaction(prev).build();
    filterProposalEmitter.fire(proposal);
    return proposal.getFilter();
  }

  /**
   * The input dto with hidden values
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param roleRef The source interaction
   * @return The input dto with hidden values
   */
  private boolean evaluatePostVisibility(Interaction prev, Role roleRef) {
    RoleVisibleContentProposal proposal = RoleVisibleContentProposal.builder().visible(true)
        .entity(roleRef).interaction(prev).build();
    guardEmitter.fire(proposal);
    return Boolean.TRUE.equals(proposal.getVisible());
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param uid
   * @param filter The filter to retrieve values
   * @return The input entity with the copy values without hidden
   */
  private Optional<Role> queryItem(Interaction prev, String uid, RoleFilter filter) {
    return roleReadRepositoryGateway.retrieve(uid, Optional.of(filter))
        .filter(values -> evaluatePostVisibility(prev, values));
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param uid
   * @param filter The filter to retrieve values
   * @return The input entity with the copy values without hidden
   */
  private Optional<Role> queryItemForUpdate(Interaction prev, String uid, RoleFilter filter) {
    return roleWriteRepositoryGateway.retrieveForUpdate(uid, Optional.of(filter))
        .filter(values -> evaluatePostVisibility(prev, values));
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param filter The filter to retrieve values
   * @param cursor The filter to retrieve values
   * @return The input entity with the copy values without hidden
   */
  private List<Role> queryItems(Interaction prev, RoleFilter filter, RoleCursor cursor) {
    List<Role> list = new ArrayList<>();
    Iterator<Role> slide = roleReadRepositoryGateway.slide(filter, cursor)
        .slide(values -> evaluatePostVisibility(prev, values));
    while (slide.hasNext()) {
      list.add(slide.next());
    }
    return list;
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param filter The filter to retrieve values
   * @param cursor The filter to retrieve values
   * @return The input entity with the copy values without hidden
   */
  private List<Role> queryItemsForUpdate(Interaction prev, RoleFilter filter, RoleCursor cursor) {
    List<Role> list = new ArrayList<>();
    Iterator<Role> slide = roleWriteRepositoryGateway.slideForUpdate(filter, cursor)
        .slide(values -> evaluatePostVisibility(prev, values));
    while (slide.hasNext()) {
      list.add(slide.next());
    }
    return list;
  }

  /**
   * The input entity with the copy values without hidden
   *
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param source The source interaction
   * @return The input entity with the copy values without hidden
   */
  private RoleStateChange visiblesReferences(Interaction prev, RoleStateChange source) {
    source.getTenant().flatMap(TenantVO::getReferenceValue).ifPresent(ref -> {
      boolean visible = tenantsVisibility.checkVisibility(prev, ref);
      if (!visible) {
        throw new NotFoundException("No tenant - " + ref);
      }
    });
    visiblesReferencesFromDomains(prev, source);
    return source;
  }

  /**
   * @autogenerated VisibilityServiceGenerator
   * @param prev The source interaction
   * @param source The source interaction
   */
  private void visiblesReferencesFromDomains(Interaction prev, RoleStateChange source) {
    source.getDomains().map(DomainsVO::getValue).ifPresent(collector -> {
      List<String> collectSecurityDomain = new ArrayList<>();
      collector.forEach(item -> {
        if (null != item.getSecurityDomain()) {
          String idSecurityDomain = item.getSecurityDomainReferenceValue();
          if (!collectSecurityDomain.contains(idSecurityDomain)) {
            collectSecurityDomain.add(idSecurityDomain);
          }
        }
      });
      if (!collectSecurityDomain.isEmpty()) {
        boolean visible = securityDomainsVisibility.checkVisibility(prev, collectSecurityDomain);
        if (!visible) {
          throw new NotFoundException("No all security domain exists: " + collectSecurityDomain);
        }
      }
    });
  }
}
