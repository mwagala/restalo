package ca.ulaval.glo2003.utilities;

import io.sentry.Sentry;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
  @Override
  public Response toResponse(RuntimeException e) {

    Sentry.captureException(e);
    JsonObject body = Json.createObjectBuilder().add("error", e.getMessage()).build();

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(body).build();
  }
}
