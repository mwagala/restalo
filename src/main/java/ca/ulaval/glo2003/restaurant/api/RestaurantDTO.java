package ca.ulaval.glo2003.restaurant.api;

import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RestaurantDTO {
  private String restaurantId;
  private final String ownerId;
  private final String name;
  private final Integer capacity;
  private final HashMap<String, LocalTime> hours;
  private final HashMap<String, Integer> reservations;

  public RestaurantDTO(String ownerId, CreateRestaurantBody source) {
    this.ownerId = ownerId;
    this.name = source.getName();
    this.capacity = source.getCapacity();
    this.hours = source.getHours();
    this.reservations = source.getReservations();
  }

  public RestaurantDTO(Restaurant original) {
    this.restaurantId = original.getRestaurantId();
    this.ownerId = original.getOwnerId();
    this.name = original.getName();
    this.capacity = original.getCapacity();
    this.hours = original.getHours();
    this.reservations = original.getReservations();
  }

  public String getRestaurantId() {
    return restaurantId;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public String getName() {
    return name;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public HashMap<String, LocalTime> getHours() {
    return hours;
  }

  public HashMap<String, Integer> getReservations() {
    return reservations;
  }

  public JsonObject createResponseBody(boolean showReservations) {

    JsonObjectBuilder restaurantJsonBuilder =
        Json.createObjectBuilder()
            .add("id", getRestaurantId())
            .add("name", getName())
            .add("capacity", getCapacity())
            .add(
                "hours",
                Json.createObjectBuilder()
                    .add(
                        "open",
                        getHours().get("open").format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                    .add(
                        "close",
                        getHours().get("close").format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

    return addReservationsToResponseBody(restaurantJsonBuilder, showReservations).build();
  }

  private JsonObjectBuilder addReservationsToResponseBody(
      JsonObjectBuilder restaurantJsonBuilder, boolean showReservations) {

    HashMap<String, Integer> reservations = getReservations();

    if (reservations != null && showReservations) {
      JsonObjectBuilder reservationJsonBuilder = Json.createObjectBuilder();

      for (Map.Entry<String, Integer> entry : reservations.entrySet())
        reservationJsonBuilder.add(entry.getKey(), entry.getValue());

      restaurantJsonBuilder.add("reservations", reservationJsonBuilder);
    }

    return restaurantJsonBuilder;
  }
}
