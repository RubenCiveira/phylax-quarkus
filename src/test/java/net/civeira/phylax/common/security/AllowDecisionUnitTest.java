package net.civeira.phylax.common.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@DisplayName("AllowDecision abstract security decision")
class AllowDecisionUnitTest {

  @Data
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
  static class TestAllowDecision extends AllowDecision {
    @Override
    public String resourceName() {
      return "test-resource";
    }

    @Override
    public String actionName() {
      return "test-action";
    }
  }

  private Interaction interaction;

  @BeforeEach
  void setUp() {
    Actor actor = Actor.builder().autenticated(true).roles(List.of()).claims(Map.of()).build();
    Connection conn = Connection.builder().request("/test").build();
    interaction = new Interaction(Interaction.builder().actor(actor).connection(conn)) {};
  }

  @Nested
  @DisplayName("deny()")
  class Deny {

    @Test
    @DisplayName("Should set allowed to false when deny is called")
    void shouldSetAllowedToFalseWhenDenyIsCalled() {
      // Arrange — create an initially-allowed decision
      Allow allow = Allow.builder().allowed(true).build();
      AllowDecision decision = TestAllowDecision.builder().detail(allow).query(interaction).build();

      // Act — invoke deny() without a reason
      decision.deny();

      // Assert — the decision detail is now denied
      assertFalse(decision.getDetail().isAllowed(), "Allow should be false after deny() is called");
    }
  }

  @Nested
  @DisplayName("deny(reason)")
  class DenyWithReason {

    @Test
    @DisplayName("Should set allowed to false and description when deny with reason is called")
    void shouldSetAllowedToFalseAndDescriptionWhenDenyWithReasonIsCalled() {
      // Arrange — create an initially-allowed decision
      Allow allow = Allow.builder().allowed(true).build();
      AllowDecision decision = TestAllowDecision.builder().detail(allow).query(interaction).build();

      // Act — invoke deny() with a specific reason message
      decision.deny("Insufficient permissions");

      // Assert — the decision is denied and carries the provided reason
      assertFalse(decision.getDetail().isAllowed(),
          "Allow should be false after deny(reason) is called");
      assertEquals("Insufficient permissions", decision.getDetail().getDescription(),
          "Description should contain the denial reason");
    }
  }

  @Nested
  @DisplayName("map()")
  class MapFunction {

    @Test
    @DisplayName("Should transform the allow detail using the provided function")
    void shouldTransformAllowDetailUsingProvidedFunction() {
      // Arrange — create an initially-allowed decision
      Allow allow = Allow.builder().allowed(true).build();
      AllowDecision decision = TestAllowDecision.builder().detail(allow).query(interaction).build();

      // Act — apply a mapping function that replaces the Allow detail
      decision.map(a -> Allow.builder().allowed(false).description("mapped").build());

      // Assert — the decision detail reflects the mapped values
      assertFalse(decision.getDetail().isAllowed(),
          "Allow should be false after map transforms it");
      assertEquals("mapped", decision.getDetail().getDescription(),
          "Description should be set by the mapping function");
    }
  }

  @Nested
  @DisplayName("abstract methods")
  class AbstractMethods {

    @Test
    @DisplayName("Should return correct resource name")
    void shouldReturnCorrectResourceName() {
      // Arrange — create a TestAllowDecision with a known resource name
      Allow allow = Allow.builder().allowed(true).build();
      AllowDecision decision = TestAllowDecision.builder().detail(allow).query(interaction).build();

      // Act / Assert — resourceName() returns the value from the concrete implementation
      assertEquals("test-resource", decision.resourceName(),
          "Resource name should match the concrete implementation");
    }

    @Test
    @DisplayName("Should return correct action name")
    void shouldReturnCorrectActionName() {
      // Arrange — create a TestAllowDecision with a known action name
      Allow allow = Allow.builder().allowed(true).build();
      AllowDecision decision = TestAllowDecision.builder().detail(allow).query(interaction).build();

      // Act / Assert — actionName() returns the value from the concrete implementation
      assertEquals("test-action", decision.actionName(),
          "Action name should match the concrete implementation");
    }
  }
}
