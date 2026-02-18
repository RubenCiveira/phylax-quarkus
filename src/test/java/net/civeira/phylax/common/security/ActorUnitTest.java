package net.civeira.phylax.common.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Actor security model")
class ActorUnitTest {

  @Nested
  @DisplayName("Builder and getters")
  class BuilderAndGetters {

    @Test
    @DisplayName("Should build actor with all fields")
    void shouldBuildActorWithAllFields() {
      // Arrange / Act — build an Actor with all fields populated
      Actor actor = Actor.builder().name("john").tenant("acme").autenticated(true)
          .roles(List.of("admin", "user")).claims(Map.of("iss", "keycloak")).build();

      // Assert — all getter values match the builder inputs
      assertTrue(actor.getName().isPresent(), "Name should be present when set");
      assertEquals("john", actor.getName().get(), "Name should match the value set in builder");
      assertTrue(actor.getTenant().isPresent(), "Tenant should be present when set");
      assertEquals("acme", actor.getTenant().get(), "Tenant should match the value set in builder");
      assertTrue(actor.isAutenticated(), "Actor should be authenticated");
      assertEquals(2, actor.getRoles().size(), "Actor should have exactly 2 roles");
    }

    @Test
    @DisplayName("Should return empty optional when name is null")
    void shouldReturnEmptyOptionalWhenNameIsNull() {
      // Arrange / Act — build an Actor without setting a name
      Actor actor = Actor.builder().autenticated(false).roles(List.of()).claims(Map.of()).build();

      // Assert — getName() returns an empty Optional
      assertTrue(actor.getName().isEmpty(), "Name should be empty when not set");
    }

    @Test
    @DisplayName("Should return empty optional when tenant is null")
    void shouldReturnEmptyOptionalWhenTenantIsNull() {
      // Arrange / Act — build an Actor without setting a tenant
      Actor actor = Actor.builder().autenticated(false).roles(List.of()).claims(Map.of()).build();

      // Assert — getTenant() returns an empty Optional
      assertTrue(actor.getTenant().isEmpty(), "Tenant should be empty when not set");
    }
  }

  @Nested
  @DisplayName("getClaim")
  class GetClaim {

    @Test
    @DisplayName("Should return claim value when key exists")
    void shouldReturnClaimValueWhenKeyExists() {
      // Arrange — build an Actor with two claims
      Actor actor = Actor.builder().autenticated(true).roles(List.of())
          .claims(Map.of("iss", "keycloak", "tid", "tenant-1")).build();

      // Act — retrieve an existing claim by key
      String claim = actor.getClaim("tid");

      // Assert — the returned value matches the stored claim
      assertEquals("tenant-1", claim, "Claim value should match the stored value");
    }

    @Test
    @DisplayName("Should return null when claim key does not exist")
    void shouldReturnNullWhenClaimKeyDoesNotExist() {
      // Arrange — build an Actor with an empty claims map
      Actor actor = Actor.builder().autenticated(true).roles(List.of()).claims(Map.of()).build();

      // Act — request a claim key that does not exist
      String claim = actor.getClaim("nonexistent");

      // Assert — null is returned for a missing claim key
      assertNull(claim, "Claim should be null when key does not exist");
    }
  }

  @Nested
  @DisplayName("hasRole")
  class HasRole {

    @Test
    @DisplayName("Should return true when actor has the role")
    void shouldReturnTrueWhenActorHasTheRole() {
      // Arrange — build an Actor with admin and user roles
      Actor actor = Actor.builder().autenticated(true).roles(List.of("admin", "user"))
          .claims(Map.of()).build();

      // Act / Assert — hasRole returns true for an assigned role
      assertTrue(actor.hasRole("admin"), "Actor should have the 'admin' role");
    }

    @Test
    @DisplayName("Should return false when actor does not have the role")
    void shouldReturnFalseWhenActorDoesNotHaveTheRole() {
      // Arrange — build an Actor with only the user role
      Actor actor =
          Actor.builder().autenticated(true).roles(List.of("user")).claims(Map.of()).build();

      // Act / Assert — hasRole returns false for an unassigned role
      assertFalse(actor.hasRole("admin"), "Actor should NOT have the 'admin' role");
    }
  }

  @Nested
  @DisplayName("hasAnyRole")
  class HasAnyRole {

    @Test
    @DisplayName("Should return true when actor has at least one of the roles")
    void shouldReturnTrueWhenActorHasAtLeastOneRole() {
      // Arrange — build an Actor with the viewer role
      Actor actor =
          Actor.builder().autenticated(true).roles(List.of("viewer")).claims(Map.of()).build();

      // Act / Assert — hasAnyRole returns true when at least one role matches
      assertTrue(actor.hasAnyRole("admin", "viewer"),
          "Actor should match at least one of the provided roles");
    }

    @Test
    @DisplayName("Should return false when actor has none of the roles")
    void shouldReturnFalseWhenActorHasNoneOfTheRoles() {
      // Arrange — build an Actor whose role does not overlap with the checked roles
      Actor actor =
          Actor.builder().autenticated(true).roles(List.of("viewer")).claims(Map.of()).build();

      // Act / Assert — hasAnyRole returns false when no role matches
      assertFalse(actor.hasAnyRole("admin", "superadmin"),
          "Actor should NOT match any of the provided roles");
    }

    @Test
    @DisplayName("Should return false when actor has no roles")
    void shouldReturnFalseWhenActorHasNoRoles() {
      // Arrange — build an Actor with no roles at all
      Actor actor = Actor.builder().autenticated(false).roles(List.of()).claims(Map.of()).build();

      // Act / Assert — hasAnyRole returns false for an Actor with an empty role list
      assertFalse(actor.hasAnyRole("admin"), "Actor with no roles should not match any role");
    }
  }
}
