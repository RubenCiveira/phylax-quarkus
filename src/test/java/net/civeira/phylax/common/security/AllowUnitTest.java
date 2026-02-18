package net.civeira.phylax.common.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Allow security permission data")
class AllowUnitTest {

  @Nested
  @DisplayName("Builder and getters")
  class BuilderAndGetters {

    @Test
    @DisplayName("Should build allowed permit")
    void shouldBuildAllowedPermit() {
      // Arrange / Act — build an Allow with allowed set to true
      Allow allow = Allow.builder().allowed(true).build();

      // Assert — the allow flag reflects the builder value
      assertTrue(allow.isAllowed(), "Allow should be true when built with allowed=true");
    }

    @Test
    @DisplayName("Should build denied permit with description")
    void shouldBuildDeniedPermitWithDescription() {
      // Arrange / Act — build an Allow denied with a description
      Allow allow = Allow.builder().allowed(false).description("Insufficient permissions").build();

      // Assert — the deny flag and description match the builder values
      assertFalse(allow.isAllowed(), "Allow should be false when built with allowed=false");
      assertEquals("Insufficient permissions", allow.getDescription(),
          "Description should match the value set in builder");
    }
  }

  @Nested
  @DisplayName("Mutability")
  class Mutability {

    @Test
    @DisplayName("Should allow changing allowed state via setter")
    void shouldAllowChangingAllowedStateViaSetter() {
      // Arrange — build an initially-allowed Allow instance
      Allow allow = Allow.builder().allowed(true).build();

      // Act — change the allowed state to false via setter
      allow.setAllowed(false);

      // Assert — the allow flag reflects the updated value
      assertFalse(allow.isAllowed(), "Allow should be false after setting allowed to false");
    }

    @Test
    @DisplayName("Should allow changing description via setter")
    void shouldAllowChangingDescriptionViaSetter() {
      // Arrange — build an Allow without a description
      Allow allow = Allow.builder().allowed(false).build();

      // Act — set a new description via setter
      allow.setDescription("New reason");

      // Assert — the description reflects the updated value
      assertEquals("New reason", allow.getDescription(),
          "Description should be updated after setting via setter");
    }
  }
}
