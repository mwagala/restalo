package ca.ulaval.glo2003.restaurant.api;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.time.LocalDateTime;

public class DispoDTO {
  LocalDateTime start;
  Integer remainingPlaces;

  public DispoDTO(LocalDateTime start, Integer remainingPlaces) {
    this.start = start;
    this.remainingPlaces = remainingPlaces;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public Integer getRemainingPlaces() {
    return remainingPlaces;
  }

  public void setRemainingPlaces(Integer value) {
    remainingPlaces = value;
  }

  public JsonObject createResponseBody() {

    JsonObjectBuilder dispoJsonBuilder =
        Json.createObjectBuilder()
            .add("start", getStart().toString() + ":00")
            .add("remainingPlaces", getRemainingPlaces());

    return dispoJsonBuilder.build();
  }
}
