package net.civeira.phylax.common.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@DisplayName("RbacAllowListener CDI event observer")
class RbacAllowListenerUnitTest {

  @Mock
  private Rbac rbac;

  private RbacAllowListener listener;
  private Interaction interaction;
  private AutoCloseable mocks;

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

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    listener = new RbacAllowListener(rbac);
    Actor actor =
        Actor.builder().autenticated(true).roles(List.of("admin")).claims(Map.of()).build();
    Connection conn = Connection.builder().request("/test").build();
    interaction = new Interaction(Interaction.builder().actor(actor).connection(conn)) {};
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  @DisplayName("checkAllow for AllowDecision")
  class CheckAllowDecision {

    @Test
    @DisplayName("Should delegate to rbac when already allowed")
    void shouldDelegateToRbacWhenAlreadyAllowed() {
      // Arrange — create an allowed decision and stub Rbac to also allow
      Allow allow = Allow.builder().allowed(true).build();
      AllowDecision decision = TestAllowDecision.builder().detail(allow).query(interaction).build();
      Allow rbacResult = Allow.builder().allowed(true).build();
      when(rbac.checkAllow(interaction.getActor(), "test-resource", "test-action"))
          .thenReturn(rbacResult);

      // Act — invoke the listener on the decision
      listener.checkAllow(decision);

      // Assert — the decision remains allowed
      assertTrue(decision.getDetail().isAllowed(),
          "Decision should remain allowed when rbac also allows");
    }

    @Test
    @DisplayName("Should not delegate to rbac when already denied")
    void shouldNotDelegateToRbacWhenAlreadyDenied() {
      // Arrange
      Allow allow = Allow.builder().allowed(false).description("denied").build();
      AllowDecision decision = TestAllowDecision.builder().detail(allow).query(interaction).build();

      // Act
      listener.checkAllow(decision);

      // Assert
      verify(rbac, never().description("Rbac should not be consulted when already denied"))
          .checkAllow(any(), anyString(), anyString());
      assertFalse(decision.getDetail().isAllowed(), "Decision should remain denied");
    }
  }

  @Nested
  @DisplayName("checkAllow for PropertiesProposal")
  class CheckAllowProperties {

    @Test
    @DisplayName("Should add inaccessible fields to proposal")
    void shouldAddInaccessibleFieldsToProposal() {
      // Arrange
      PropertiesProposal proposal =
          TestPropertiesProposal.builder().fields(new HashSet<>()).query(interaction).build();
      when(rbac.inaccesibleFileds(interaction.getActor(), "test-resource", "test-view"))
          .thenReturn(List.of("secret-field", "hidden-field"));

      // Act
      listener.checkAllow(proposal);

      // Assert
      assertTrue(proposal.getFields().contains("secret-field"),
          "Proposal should contain 'secret-field' after listener processes it");
      assertTrue(proposal.getFields().contains("hidden-field"),
          "Proposal should contain 'hidden-field' after listener processes it");
    }
  }
}
