package net.civeira.phylax.features.access.useridentity.infrastructure.driver.rest;

import java.util.ArrayList;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.useridentity.application.usecase.create.UserIdentityCreateUsecase;
import net.civeira.phylax.features.access.useridentity.application.usecase.delete.UserIdentityDeleteUsecase;
import net.civeira.phylax.features.access.useridentity.application.usecase.list.UserIdentityListUsecase;
import net.civeira.phylax.features.access.useridentity.application.usecase.retrieve.UserIdentityRetrieveUsecase;
import net.civeira.phylax.features.access.useridentity.application.usecase.update.UserIdentityUpdateUsecase;
import net.civeira.phylax.features.access.useridentity.application.visibility.UserIdentitysVisibility;
import net.civeira.phylax.generated.openapi.api.UserIdentityAclApi;
import net.civeira.phylax.generated.openapi.model.CommonAllow;
import net.civeira.phylax.generated.openapi.model.UserIdentityAclFields;
import net.civeira.phylax.generated.openapi.model.UserIdentityAclGenericAllows;
import net.civeira.phylax.generated.openapi.model.UserIdentityAclSpecificAllows;
import net.civeira.phylax.generated.openapi.model.UserIdentityGenericAcl;
import net.civeira.phylax.generated.openapi.model.UserIdentitySpecificAcl;

@RequiredArgsConstructor
public class UserIdentityAclController implements UserIdentityAclApi {

  /**
   * @autogenerated AclControllerGenerator
   */
  private final UserIdentityCreateUsecase create;

  /**
   * UserIdentity
   *
   * @autogenerated AclControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final UserIdentityDeleteUsecase delete;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final UserIdentityListUsecase list;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final UserIdentityRetrieveUsecase retrieve;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final UserIdentityUpdateUsecase update;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final UserIdentitysVisibility visibility;

  /**
   * @autogenerated AclControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response userIdentityApiContextualAcl(final String uid) {
    Interaction interaction = currentRequest.interaction();
    UserIdentitySpecificAcl response = new UserIdentitySpecificAcl();
    response.setAllows(new UserIdentityAclSpecificAllows());
    response.setFields(new UserIdentityAclFields());
    response.getFields().setNoEditables(new ArrayList<>());
    response.getFields().setNoVisibles(new ArrayList<>());
    fixedFields(response.getFields(), interaction);
    hiddenFields(response.getFields(), interaction);
    updateAllows(response, interaction);
    deleteAllows(response, interaction);
    retrieveAllows(response, interaction);
    return Response.ok(response).build();
  }

  /**
   * @autogenerated AclControllerGenerator
   * @return
   */
  @Override
  public Response userIdentityApiGenericAcl() {
    Interaction interaction = currentRequest.interaction();
    UserIdentityGenericAcl response = new UserIdentityGenericAcl();
    response.setAllows(new UserIdentityAclGenericAllows());
    response.setFields(new UserIdentityAclFields());
    response.getFields().setNoEditables(new ArrayList<>());
    response.getFields().setNoVisibles(new ArrayList<>());
    fixedFields(response.getFields(), interaction);
    hiddenFields(response.getFields(), interaction);
    listAllows(response, interaction);
    createAllows(response, interaction);
    updateAllows(response, interaction);
    deleteAllows(response, interaction);
    retrieveAllows(response, interaction);
    return Response.ok(response).build();
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void createAllows(final UserIdentityGenericAcl response, final Interaction query) {
    Allow detail = create.allow(query);
    response.getAllows()
        .setCreate(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void deleteAllows(final UserIdentityGenericAcl response, final Interaction query) {
    Allow detail = delete.allow(query);
    response.getAllows()
        .setDelete(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void deleteAllows(final UserIdentitySpecificAcl response, final Interaction query) {
    Allow detail = delete.allow(query);
    response.getAllows()
        .setDelete(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void fixedFields(final UserIdentityAclFields response, final Interaction query) {
    visibility.fieldsToFix(query).forEach(field -> response.getNoEditables().add(field));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void hiddenFields(final UserIdentityAclFields response, final Interaction query) {
    visibility.fieldsToHide(query).forEach(field -> response.getNoVisibles().add(field));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void listAllows(final UserIdentityGenericAcl response, final Interaction query) {
    Allow detail = list.allow(query);
    response.getAllows()
        .setList(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void retrieveAllows(final UserIdentityGenericAcl response, final Interaction query) {
    Allow detail = retrieve.allow(query);
    response.getAllows()
        .setRetrieve(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void retrieveAllows(final UserIdentitySpecificAcl response, final Interaction query) {
    Allow detail = retrieve.allow(query);
    response.getAllows()
        .setRetrieve(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void updateAllows(final UserIdentityGenericAcl response, final Interaction query) {
    Allow detail = update.allow(query);
    response.getAllows()
        .setUpdate(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void updateAllows(final UserIdentitySpecificAcl response, final Interaction query) {
    Allow detail = update.allow(query);
    response.getAllows()
        .setUpdate(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }
}
