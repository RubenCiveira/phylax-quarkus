package net.civeira.phylax.features.access.relyingparty.infrastructure.driver.rest;

import java.util.ArrayList;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.infrastructure.CurrentRequest;
import net.civeira.phylax.common.security.Allow;
import net.civeira.phylax.common.security.Interaction;
import net.civeira.phylax.features.access.relyingparty.application.usecase.create.RelyingPartyCreateUsecase;
import net.civeira.phylax.features.access.relyingparty.application.usecase.delete.RelyingPartyDeleteUsecase;
import net.civeira.phylax.features.access.relyingparty.application.usecase.disable.RelyingPartyDisableUsecase;
import net.civeira.phylax.features.access.relyingparty.application.usecase.enable.RelyingPartyEnableUsecase;
import net.civeira.phylax.features.access.relyingparty.application.usecase.list.RelyingPartyListUsecase;
import net.civeira.phylax.features.access.relyingparty.application.usecase.retrieve.RelyingPartyRetrieveUsecase;
import net.civeira.phylax.features.access.relyingparty.application.usecase.update.RelyingPartyUpdateUsecase;
import net.civeira.phylax.features.access.relyingparty.application.visibility.RelyingPartysVisibility;
import net.civeira.phylax.generated.openapi.api.RelyingPartyAclApi;
import net.civeira.phylax.generated.openapi.model.CommonAllow;
import net.civeira.phylax.generated.openapi.model.RelyingPartyAclFields;
import net.civeira.phylax.generated.openapi.model.RelyingPartyAclGenericAllows;
import net.civeira.phylax.generated.openapi.model.RelyingPartyAclSpecificAllows;
import net.civeira.phylax.generated.openapi.model.RelyingPartyGenericAcl;
import net.civeira.phylax.generated.openapi.model.RelyingPartySpecificAcl;

@RequiredArgsConstructor
public class RelyingPartyAclController implements RelyingPartyAclApi {

  /**
   * @autogenerated AclControllerGenerator
   */
  private final RelyingPartyCreateUsecase create;

  /**
   * RelyingParty
   *
   * @autogenerated AclControllerGenerator
   */
  private final CurrentRequest currentRequest;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final RelyingPartyDeleteUsecase delete;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final RelyingPartyDisableUsecase disable;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final RelyingPartyEnableUsecase enable;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final RelyingPartyListUsecase list;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final RelyingPartyRetrieveUsecase retrieve;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final RelyingPartyUpdateUsecase update;

  /**
   * @autogenerated AclControllerGenerator
   */
  private final RelyingPartysVisibility visibility;

  /**
   * @autogenerated AclControllerGenerator
   * @param uid
   * @return
   */
  @Override
  public Response relyingPartyApiContextualAcl(final String uid) {
    Interaction interaction = currentRequest.interaction();
    RelyingPartySpecificAcl response = new RelyingPartySpecificAcl();
    response.setAllows(new RelyingPartyAclSpecificAllows());
    response.setFields(new RelyingPartyAclFields());
    response.getFields().setNoEditables(new ArrayList<>());
    response.getFields().setNoVisibles(new ArrayList<>());
    fixedFields(response.getFields(), interaction);
    hiddenFields(response.getFields(), interaction);
    updateAllows(response, interaction);
    deleteAllows(response, interaction);
    retrieveAllows(response, interaction);
    enableAllows(response, interaction);
    disableAllows(response, interaction);
    return Response.ok(response).build();
  }

  /**
   * @autogenerated AclControllerGenerator
   * @return
   */
  @Override
  public Response relyingPartyApiGenericAcl() {
    Interaction interaction = currentRequest.interaction();
    RelyingPartyGenericAcl response = new RelyingPartyGenericAcl();
    response.setAllows(new RelyingPartyAclGenericAllows());
    response.setFields(new RelyingPartyAclFields());
    response.getFields().setNoEditables(new ArrayList<>());
    response.getFields().setNoVisibles(new ArrayList<>());
    fixedFields(response.getFields(), interaction);
    hiddenFields(response.getFields(), interaction);
    listAllows(response, interaction);
    createAllows(response, interaction);
    updateAllows(response, interaction);
    deleteAllows(response, interaction);
    retrieveAllows(response, interaction);
    enableAllows(response, interaction);
    disableAllows(response, interaction);
    return Response.ok(response).build();
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void createAllows(final RelyingPartyGenericAcl response, final Interaction query) {
    Allow detail = create.allow(query);
    response.getAllows()
        .setCreate(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void deleteAllows(final RelyingPartyGenericAcl response, final Interaction query) {
    Allow detail = delete.allow(query);
    response.getAllows()
        .setDelete(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void deleteAllows(final RelyingPartySpecificAcl response, final Interaction query) {
    Allow detail = delete.allow(query);
    response.getAllows()
        .setDelete(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void disableAllows(final RelyingPartyGenericAcl response, final Interaction query) {
    Allow detail = disable.allow(query);
    response.getAllows()
        .setDisable(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void disableAllows(final RelyingPartySpecificAcl response, final Interaction query) {
    Allow detail = disable.allow(query);
    response.getAllows()
        .setDisable(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void enableAllows(final RelyingPartyGenericAcl response, final Interaction query) {
    Allow detail = enable.allow(query);
    response.getAllows()
        .setEnable(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void enableAllows(final RelyingPartySpecificAcl response, final Interaction query) {
    Allow detail = enable.allow(query);
    response.getAllows()
        .setEnable(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void fixedFields(final RelyingPartyAclFields response, final Interaction query) {
    visibility.fieldsToFix(query).forEach(field -> response.getNoEditables().add(field));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void hiddenFields(final RelyingPartyAclFields response, final Interaction query) {
    visibility.fieldsToHide(query).forEach(field -> response.getNoVisibles().add(field));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void listAllows(final RelyingPartyGenericAcl response, final Interaction query) {
    Allow detail = list.allow(query);
    response.getAllows()
        .setList(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void retrieveAllows(final RelyingPartyGenericAcl response, final Interaction query) {
    Allow detail = retrieve.allow(query);
    response.getAllows()
        .setRetrieve(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void retrieveAllows(final RelyingPartySpecificAcl response, final Interaction query) {
    Allow detail = retrieve.allow(query);
    response.getAllows()
        .setRetrieve(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void updateAllows(final RelyingPartyGenericAcl response, final Interaction query) {
    Allow detail = update.allow(query);
    response.getAllows()
        .setUpdate(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }

  /**
   * @autogenerated AclControllerGenerator
   * @param response
   * @param query
   */
  private void updateAllows(final RelyingPartySpecificAcl response, final Interaction query) {
    Allow detail = update.allow(query);
    response.getAllows()
        .setUpdate(new CommonAllow().allowed(detail.isAllowed()).reason(detail.getDescription()));
  }
}
