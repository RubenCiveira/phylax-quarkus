package net.civeira.phylax.common.infrastructure.projection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ExecutionNode URL resolution and relations")
class ExecutionNodeUnitTest {

  @Nested
  @DisplayName("target()")
  class Target {

    @Test
    @DisplayName("Should resolve target URL with path params")
    void shouldResolveTargetUrlWithPathParams() {
      // Arrange — a node with a single path parameter placeholder in the endpoint
      ExecutionNode node = ExecutionNode.builder().server("http://localhost:8080")
          .endpoint("/api/tenants/{id}").method("GET").list(false)
          .params(Map.of("id", ParamKind.PATH)).relations(new HashMap<>()).build();

      // Act — resolve the target URL by supplying the path parameter value
      String result = node.target(Map.of("id", "123"));

      // Assert — the placeholder is replaced with the actual value in the URL
      assertEquals("http://localhost:8080/api/tenants/123", result,
          "Target URL should have path param replaced with actual value");
    }

    @Test
    @DisplayName("Should resolve target URL with multiple path params")
    void shouldResolveTargetUrlWithMultiplePathParams() {
      // Arrange — a node with two path parameter placeholders in the endpoint
      ExecutionNode node = ExecutionNode.builder().server("http://api")
          .endpoint("/tenants/{tenantId}/users/{userId}").method("GET").list(false).params(Map.of())
          .relations(new HashMap<>()).build();

      // Act — resolve the target URL by supplying both path parameter values
      String result = node.target(Map.of("tenantId", "t1", "userId", "u2"));

      // Assert — all placeholders are replaced with their actual values
      assertEquals("http://api/tenants/t1/users/u2", result,
          "Target URL should have all path params replaced");
    }

    @Test
    @DisplayName("Should return URL unchanged when no params provided")
    void shouldReturnUrlUnchangedWhenNoParamsProvided() {
      // Arrange — a node with no path parameter placeholders in the endpoint
      ExecutionNode node =
          ExecutionNode.builder().server("http://localhost").endpoint("/api/tenants").method("GET")
              .list(true).params(Map.of()).relations(new HashMap<>()).build();

      // Act — resolve the target URL with an empty parameter map
      String result = node.target(Map.of());

      // Assert — the URL is returned unchanged with no substitutions
      assertEquals("http://localhost/api/tenants", result,
          "Target URL should remain unchanged when no params are provided");
    }
  }

  @Nested
  @DisplayName("addRelation()")
  class AddRelation {

    @Test
    @DisplayName("Should add relation to the node's relation map")
    void shouldAddRelationToNodeRelationMap() {
      // Arrange — a node with an empty mutable relations map and a relationship definition
      HashMap<String, RelationshipDefinition> relations = new HashMap<>();
      ExecutionNode node =
          ExecutionNode.builder().server("http://localhost").endpoint("/api/tenants").method("GET")
              .list(true).params(Map.of()).relations(relations).build();
      RelationshipDefinition def = RelationshipDefinition.builder().id("users").url("/api/users")
          .method("GET").batchParam("tenantId").referenceField("tenantRef").list(true).build();

      // Act — add the relationship definition to the node under the key "users"
      node.addRelation("users", def);

      // Assert — the node's relations map contains exactly the added entry
      assertEquals(1, node.getRelations().size(),
          "Relations map should contain exactly 1 entry after adding a relation");
      assertEquals(def, node.getRelations().get("users"),
          "The stored relation should match the one that was added");
    }
  }

  @Nested
  @DisplayName("Builder fields")
  class BuilderFields {

    @Test
    @DisplayName("Should correctly set list flag")
    void shouldCorrectlySetListFlag() {
      // Arrange / Act — build a node with the list flag set to true
      ExecutionNode node = ExecutionNode.builder().server("http://localhost").endpoint("/api/items")
          .method("GET").list(true).params(Map.of()).relations(new HashMap<>()).build();

      // Assert — the list flag reflects the configured value
      assertTrue(node.isList(), "Node should be marked as list when built with list=true");
    }

    @Test
    @DisplayName("Should correctly set method")
    void shouldCorrectlySetMethod() {
      // Arrange / Act — build a node with the HTTP method set to POST
      ExecutionNode node = ExecutionNode.builder().server("http://localhost").endpoint("/api/items")
          .method("POST").list(false).params(Map.of()).relations(new HashMap<>()).build();

      // Assert — the method getter returns the configured HTTP method
      assertEquals("POST", node.getMethod(), "Method should match the value set in builder");
    }
  }
}
