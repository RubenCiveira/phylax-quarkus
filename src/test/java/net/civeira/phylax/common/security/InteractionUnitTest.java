package net.civeira.phylax.common.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Interaction security context holder")
class InteractionUnitTest {

  @Nested
  @DisplayName("Construction")
  class Construction {

    @Test
    @DisplayName("Should hold actor and connection")
    void shouldHoldActorAndConnection() {
      // Arrange — create an Actor and a Connection to compose the Interaction
      Actor actor = Actor.builder().autenticated(true).roles(List.of("admin")).claims(Map.of())
          .name("john").build();
      Connection conn = Connection.builder().request("/api/test").build();

      // Act — construct an Interaction from the actor and connection
      Interaction interaction =
          new Interaction(Interaction.builder().actor(actor).connection(conn)) {};

      // Assert — the Interaction exposes the same actor and connection instances
      assertEquals(actor, interaction.getActor(),
          "Interaction should hold the actor that was passed in");
      assertEquals(conn, interaction.getConnection(),
          "Interaction should hold the connection that was passed in");
    }

    @Test
    @DisplayName("Should throw when actor is null")
    void shouldThrowWhenActorIsNull() {
      // Arrange — create a valid Connection but no Actor
      Connection conn = Connection.builder().request("/api").build();

      // Act / Assert — constructing an Interaction with null actor throws NullPointerException
      assertThrows(NullPointerException.class,
          () -> new Interaction(Interaction.builder().actor(null).connection(conn)) {},
          "Should throw NullPointerException when actor is null");
    }

    @Test
    @DisplayName("Should throw when connection is null")
    void shouldThrowWhenConnectionIsNull() {
      // Arrange — create a valid Actor but no Connection
      Actor actor = Actor.builder().autenticated(false).roles(List.of()).claims(Map.of()).build();

      // Act / Assert — constructing an Interaction with null connection throws NullPointerException
      assertThrows(NullPointerException.class,
          () -> new Interaction(Interaction.builder().actor(actor).connection(null)) {},
          "Should throw NullPointerException when connection is null");
    }
  }
}
