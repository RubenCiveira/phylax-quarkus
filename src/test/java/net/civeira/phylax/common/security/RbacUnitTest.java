package net.civeira.phylax.common.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.enterprise.inject.Instance;
import net.civeira.phylax.common.security.scope.FieldDescription;
import net.civeira.phylax.common.security.scope.FieldGrant;
import net.civeira.phylax.common.security.scope.FieldGrantList;
import net.civeira.phylax.common.security.scope.Kind;
import net.civeira.phylax.common.security.scope.ResourceDescription;
import net.civeira.phylax.common.security.scope.ScopeAllow;
import net.civeira.phylax.common.security.scope.ScopeAllowList;
import net.civeira.phylax.common.security.scope.ScopeDescription;

@DisplayName("Rbac role-based access control")
class RbacUnitTest {

  @Mock
  private Instance<RbacStore> managerInstance;

  @Mock
  private RbacStore store;

  private Rbac rbac;
  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    rbac = new Rbac(managerInstance);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  @DisplayName("registerResourceField")
  class RegisterResourceField {

    @Test
    @DisplayName("Should delegate to active stores when registering resource field")
    void shouldDelegateToActiveStoresWhenRegisteringResourceField() {
      // Arrange — prepare a resource and field description with an active store mock
      ResourceDescription resource =
          ResourceDescription.builder().name("tenant").description("Tenant resource").build();
      FieldDescription field =
          FieldDescription.builder().name("name").description("Tenant name").build();
      when(store.isActive()).thenReturn(true);
      doAnswer(invocation -> {
        java.util.function.Consumer<RbacStore> action = invocation.getArgument(0);
        action.accept(store);
        return null;
      }).when(managerInstance).forEach(any());

      // Act — register the field on the resource through Rbac
      rbac.registerResourceField(resource, field);

      // Assert — the active store received the registration call
      verify(store, description("Active store should receive the field registration"))
          .registerResourceField(resource, field);
    }

    @Test
    @DisplayName("Should not delegate to inactive stores")
    void shouldNotDelegateToInactiveStores() {
      // Arrange
      ResourceDescription resource =
          ResourceDescription.builder().name("tenant").description("Tenant resource").build();
      FieldDescription field =
          FieldDescription.builder().name("name").description("Tenant name").build();
      when(store.isActive()).thenReturn(false);
      doAnswer(invocation -> {
        java.util.function.Consumer<RbacStore> action = invocation.getArgument(0);
        action.accept(store);
        return null;
      }).when(managerInstance).forEach(any());

      // Act
      rbac.registerResourceField(resource, field);

      // Assert
      verify(store, never().description("Inactive store should NOT receive registration"))
          .registerResourceField(any(), any());
    }
  }

  @Nested
  @DisplayName("registerResourceAction")
  class RegisterResourceAction {

    @Test
    @DisplayName("Should delegate to active stores when registering resource action")
    void shouldDelegateToActiveStores() {
      // Arrange
      ResourceDescription resource =
          ResourceDescription.builder().name("tenant").description("desc").build();
      ScopeDescription action = ScopeDescription.builder().name("create").kind(Kind.WRITE)
          .description("Create tenant").build();
      when(store.isActive()).thenReturn(true);
      doAnswer(invocation -> {
        java.util.function.Consumer<RbacStore> c = invocation.getArgument(0);
        c.accept(store);
        return null;
      }).when(managerInstance).forEach(any());

      // Act
      rbac.registerResourceAction(resource, action);

      // Assert
      verify(store, description("Active store should receive the action registration"))
          .registerResourceAction(resource, action);
    }
  }

  @Nested
  @DisplayName("checkAllow")
  class CheckAllow {

    @Test
    @DisplayName("Should return allowed when store grants the scope")
    void shouldReturnAllowedWhenStoreGrantsScope() {
      // Arrange
      Actor actor =
          Actor.builder().autenticated(true).roles(List.of("admin")).claims(Map.of()).build();
      when(store.isActive()).thenReturn(true);
      ScopeAllowList scopeList = new ScopeAllowList();
      scopeList.add(ScopeAllow.builder().resource("tenant").name("create").build());
      when(store.checkRoleScopes(actor)).thenReturn(scopeList);
      doAnswer(invocation -> {
        java.util.function.Consumer<RbacStore> c = invocation.getArgument(0);
        c.accept(store);
        return null;
      }).when(managerInstance).forEach(any());

      // Act
      Allow result = rbac.checkAllow(actor, "tenant", "create");

      // Assert
      assertTrue(result.isAllowed(), "Allow should be true when the scope is granted");
    }

    @Test
    @DisplayName("Should return allowed true when no active stores exist")
    void shouldReturnAllowedTrueWhenNoActiveStoresExist() {
      // Arrange
      Actor actor = Actor.builder().autenticated(true).roles(List.of()).claims(Map.of()).build();
      doAnswer(invocation -> null).when(managerInstance).forEach(any());

      // Act
      Allow result = rbac.checkAllow(actor, "tenant", "create");

      // Assert
      assertTrue(result.isAllowed(),
          "Allow should default to true when no active stores are available");
    }

    @Test
    @DisplayName("Should return not allowed when store denies the scope")
    void shouldReturnNotAllowedWhenStoreDeniesScope() {
      // Arrange
      Actor actor =
          Actor.builder().autenticated(true).roles(List.of("viewer")).claims(Map.of()).build();
      when(store.isActive()).thenReturn(true);
      ScopeAllowList scopeList = new ScopeAllowList();
      when(store.checkRoleScopes(actor)).thenReturn(scopeList);
      doAnswer(invocation -> {
        java.util.function.Consumer<RbacStore> c = invocation.getArgument(0);
        c.accept(store);
        return null;
      }).when(managerInstance).forEach(any());

      // Act
      Allow result = rbac.checkAllow(actor, "tenant", "create");

      // Assert
      assertFalse(result.isAllowed(), "Allow should be false when the scope is not granted");
    }
  }

  @Nested
  @DisplayName("inaccesibleFileds")
  class InaccesibleFields {

    @Test
    @DisplayName("Should return inaccessible fields from store")
    void shouldReturnInaccessibleFieldsFromStore() {
      // Arrange
      Actor actor =
          Actor.builder().autenticated(true).roles(List.of("viewer")).claims(Map.of()).build();
      when(store.isActive()).thenReturn(true);
      FieldGrantList fieldList = new FieldGrantList();
      fieldList.add(FieldGrant.builder().resource("tenant").view("list").name("secret").build());
      when(store.checkRoleProperties(actor)).thenReturn(fieldList);
      doAnswer(invocation -> {
        java.util.function.Consumer<RbacStore> c = invocation.getArgument(0);
        c.accept(store);
        return null;
      }).when(managerInstance).forEach(any());

      // Act
      List<String> result = rbac.inaccesibleFileds(actor, "tenant", "list");

      // Assert
      assertEquals(1, result.size(), "Should return exactly one inaccessible field");
      assertEquals("secret", result.get(0), "The inaccessible field name should match");
    }

    @Test
    @DisplayName("Should return empty list when no active stores exist")
    void shouldReturnEmptyListWhenNoActiveStoresExist() {
      // Arrange
      Actor actor = Actor.builder().autenticated(true).roles(List.of()).claims(Map.of()).build();
      doAnswer(invocation -> null).when(managerInstance).forEach(any());

      // Act
      List<String> result = rbac.inaccesibleFileds(actor, "tenant", "list");

      // Assert
      assertTrue(result.isEmpty(), "Should return empty list when no active stores are available");
    }
  }
}
