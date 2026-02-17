package ca.ulaval.glo2003.reservation.api;

import ca.ulaval.glo2003.reservation.domain.Reservation;
import ca.ulaval.glo2003.restaurant.api.RestaurantDTO;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ReservationDTO {
  private final String restaurantId;
  private final String reservationId;
  private final LocalTime startTime;
  private final Integer groupSize;
  private final LocalDate date;
  private final LocalTime endTime;
  private final HashMap<String, String> customer;
  private final RestaurantDTO associatedRestaurant;

  public ReservationDTO(String restaurantId, CreateReservationBody body) {
    this.restaurantId = restaurantId;
    this.startTime = body.getStartTime();
    this.reservationId = null;
    this.groupSize = body.getGroupSize();
    this.date = body.getDate();
    this.customer = body.getCustomer();
    this.endTime = null;
    this.associatedRestaurant = null;
  }

  public ReservationDTO(Reservation original, RestaurantDTO restaurant) {
    this.restaurantId = original.getRestaurantId();
    this.startTime = original.getStartTime();
    this.reservationId = original.getReservationId();
    this.groupSize = original.getGroupSize();
    this.date = original.getDate();
    this.customer = original.getCustomer();
    this.endTime = original.getEndTime();
    this.associatedRestaurant = restaurant;
  }

  public String getRestaurantId() {
    return restaurantId;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public String getReservationId() {
    return reservationId;
  }

  public Integer getGroupSize() {
    return groupSize;
  }

  public LocalDate getDate() {
    return date;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public HashMap<String, String> getCustomer() {
    return customer;
  }

  public RestaurantDTO getAssociatedRestaurant() {
    return associatedRestaurant;
  }

  public JsonObject createResponseBody(boolean withRestaurant) {

    JsonObjectBuilder responseBody =
        Json.createObjectBuilder()
            .add("number", getReservationId())
            .add("date", getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .add(
                "time",
                Json.createObjectBuilder()
                    .add("start", getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                    .add("end", getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
            .add("groupSize", getGroupSize())
            .add(
                "customer",
                Json.createObjectBuilder()
                    .add("name", getCustomer().get("name"))
                    .add("email", getCustomer().get("email"))
                    .add("phoneNumber", getCustomer().get("phoneNumber")));

    if (withRestaurant && associatedRestaurant != null) {
      return addAssociatedRestaurantToResponseBody(responseBody).build();
    }

    return responseBody.build();
  }

  private JsonObjectBuilder addAssociatedRestaurantToResponseBody(JsonObjectBuilder responseBody) {
    responseBody.add(
        "restaurant",
        Json.createObjectBuilder()
            .add("id", associatedRestaurant.getRestaurantId())
            .add("name", associatedRestaurant.getName())
            .add("capacity", associatedRestaurant.getCapacity())
            .add(
                "hours",
                Json.createObjectBuilder()
                    .add(
                        "open",
                        associatedRestaurant
                            .getHours()
                            .get("open")
                            .format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                    .add(
                        "close",
                        associatedRestaurant
                            .getHours()
                            .get("close")
                            .format(DateTimeFormatter.ofPattern("HH:mm:ss")))));
    return responseBody;
  }
}
