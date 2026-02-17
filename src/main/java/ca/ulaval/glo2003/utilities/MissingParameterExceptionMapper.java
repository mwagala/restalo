package ca.ulaval.glo2003.utilities;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class MissingParameterExceptionMapper implements ExceptionMapper<MissingParameterException> {
  @Override
  public Response toResponse(MissingParameterException e) {

    JsonObject body =
        Json.createObjectBuilder()
            .add("error", "MISSING_PARAMETER")
            .add("description", "Un des paramètres obligatoires est manquant")
            .build();

    return Response.status(Response.Status.BAD_REQUEST).entity(body).build();
  }
}
