package ca.ulaval.glo2003.utilities;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class InvalidParameterExceptionMapper implements ExceptionMapper<InvalidParameterException> {
  @Override
  public Response toResponse(InvalidParameterException e) {

    JsonObject body =
        Json.createObjectBuilder()
            .add("error", "INVALID_PARAMETER")
            .add("description", "Un des paramètres n'est pas valide")
            .build();

    return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
  }
}
