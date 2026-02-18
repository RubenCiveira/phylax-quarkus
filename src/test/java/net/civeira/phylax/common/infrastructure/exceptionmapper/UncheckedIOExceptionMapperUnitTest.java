package net.civeira.phylax.common.infrastructure.exceptionmapper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;

@DisplayName("UncheckedIOExceptionMapper IO exception mapper")
class UncheckedIOExceptionMapperUnitTest {

  @Test
  @DisplayName("Should return 502 status code")
  void shouldReturn502StatusCode() {
    // Arrange — an IO exception mapper and an UncheckedIOException wrapping a connection reset are
    // instantiated
    UncheckedIOExceptionMapper mapper = new UncheckedIOExceptionMapper();
    UncheckedIOException ex = new UncheckedIOException(new IOException("connection reset"));

    // Act — the mapper converts the IO exception into a REST response
    Response response = mapper.toResponse(ex);

    // Assert — the response status code should be 502
    assertEquals(502, response.getStatus(),
        "Response status should be 502 Bad Gateway for IO errors");
  }

  @Test
  @DisplayName("Should include exception message as reason")
  @SuppressWarnings("unchecked")
  void shouldIncludeExceptionMessageAsReason() {
    // Arrange — an IO exception mapper and an UncheckedIOException wrapping a timeout are
    // instantiated
    UncheckedIOExceptionMapper mapper = new UncheckedIOExceptionMapper();
    UncheckedIOException ex = new UncheckedIOException(new IOException("timeout"));

    // Act — the mapper converts the IO exception into a REST response and the entity is extracted
    Response response = mapper.toResponse(ex);
    Map<String, String> entity = (Map<String, String>) response.getEntity();

    // Assert — the response entity should contain a non-null reason with the exception message
    assertNotNull(entity, "Response entity should not be null");
    assertNotNull(entity.get("reason"), "Entity 'reason' should contain the exception message");
  }
}
