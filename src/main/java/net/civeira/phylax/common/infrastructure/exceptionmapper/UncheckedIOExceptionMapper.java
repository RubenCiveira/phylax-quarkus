/* @autogenerated */
package net.civeira.phylax.common.infrastructure.exceptionmapper;

import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class UncheckedIOExceptionMapper implements ExceptionMapper<UncheckedIOException> {

  @Override
  public Response toResponse(UncheckedIOException exception) {
    if (log.isDebugEnabled()) {
      log.warn("Uncheked io exception", exception);
    } else if (log.isWarnEnabled()) {
      log.warn("Uncheked io exception");
    }
    Map<String, String> error = new HashMap<>();
    error.put("reason", exception.getMessage());
    return Response.status(502).entity(error).build();
  }
}
