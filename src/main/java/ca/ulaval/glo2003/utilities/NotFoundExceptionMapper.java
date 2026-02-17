package ca.ulaval.glo2003.utilities;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
  @Override
  public Response toResponse(NotFoundException e) {

    JsonObject body =
        Json.createObjectBuilder()
            .add("error", "NOT_FOUND")
            .add("description", e.description)
            .build();

    return Response.status(Response.Status.NOT_FOUND).entity(body).build();
  }
}
