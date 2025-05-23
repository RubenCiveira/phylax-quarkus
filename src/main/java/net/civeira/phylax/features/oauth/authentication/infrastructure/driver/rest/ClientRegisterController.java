/* @autogenerated */
package net.civeira.phylax.features.oauth.authentication.infrastructure.driver.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("")
@RequestScoped
@RequiredArgsConstructor
public class ClientRegisterController {
  @POST
  @Path("oauth/openid/{tenant}/connect")
  public Response device(final @PathParam("tenant") String tenant,
      final MultivaluedMap<String, String> paramMap) {
    return Response.status(403, "Client not allowed.").build();
  }
}
