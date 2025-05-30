/* @autogenerated */
package net.civeira.phylax.common.security.rcab;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.civeira.phylax.common.security.Actor;
import net.civeira.phylax.common.security.RbacStore;
import net.civeira.phylax.common.security.scope.FieldDescription;
import net.civeira.phylax.common.security.scope.FieldGrantList;
import net.civeira.phylax.common.security.scope.ResourceDescription;
import net.civeira.phylax.common.security.scope.ScopeAllowList;
import net.civeira.phylax.common.security.scope.ScopeDescription;

@ApplicationScoped
@IfBuildProperty(name = "mp.rcab.model", stringValue = "keycloack")
@RequiredArgsConstructor
public class Keycloack implements RbacStore {
  private final String KIND = "keycloack";
  private final @ConfigProperty(name = "mp.rcab.model") String model;

  @Override
  public boolean isActive() {
    System.out.println("MIRANDO CON " + KIND);
    return KIND.equals(model);
  }

  @Override
  public void registerResourceAction(ResourceDescription resource, ScopeDescription action) {
    System.err.println("KEYCLOACK MODE FOR SCOPE OF " + resource + " on " + action);
  }

  @Override
  public void registerResourceField(ResourceDescription resource, FieldDescription field) {
    System.err.println("KEYCLOACK MODE FOR FIELD OF " + resource + " as " + field);
  }

  @Override
  public ScopeAllowList checkRoleScopes(Actor actor) {
    return null;
  }

  @Override
  public FieldGrantList checkRoleProperties(Actor actor) {
    return null;
  }

}
