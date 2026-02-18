package net.civeira.phylax.common.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@DisplayName("PropertiesProposal field management")
class PropertiesProposalUnitTest {

  @Data
  @SuperBuilder(toBuilder = true)
  @EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
  static class TestPropertiesProposal extends PropertiesProposal {
    @Override
    public String resourceName() {
      return "test-resource";
    }

    @Override
    public String viewName() {
      return "test-view";
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
  @DisplayName("add(String...)")
  class AddVarargs {

    @Test
    @DisplayName("Should add fields via varargs")
    void shouldAddFieldsViaVarargs() {
      // Arrange — create a proposal with an empty mutable field set
      PropertiesProposal proposal =
          TestPropertiesProposal.builder().fields(new HashSet<>()).query(interaction).build();

      // Act — add two fields via varargs
      proposal.add("field1", "field2");

      // Assert — both fields are present in the proposal
      assertTrue(proposal.getFields().contains("field1"),
          "Fields should contain 'field1' after adding via varargs");
      assertTrue(proposal.getFields().contains("field2"),
          "Fields should contain 'field2' after adding via varargs");
    }
  }

  @Nested
  @DisplayName("add(List)")
  class AddList {

    @Test
    @DisplayName("Should add fields from a list")
    void shouldAddFieldsFromList() {
      // Arrange
      PropertiesProposal proposal =
          TestPropertiesProposal.builder().fields(new HashSet<>()).query(interaction).build();

      // Act
      proposal.add(List.of("a", "b", "c"));

      // Assert
      assertEquals(3, proposal.getFields().size(),
          "Fields should contain exactly 3 entries after adding a list of 3");
    }

    @Test
    @DisplayName("Should handle unmodifiable set by replacing it")
    void shouldHandleUnmodifiableSetByReplacingIt() {
      // Arrange
      PropertiesProposal proposal =
          TestPropertiesProposal.builder().fields(Set.of()).query(interaction).build();

      // Act
      proposal.add(List.of("newField"));

      // Assert
      assertTrue(proposal.getFields().contains("newField"),
          "Fields should contain 'newField' even if original set was unmodifiable");
    }
  }
}
