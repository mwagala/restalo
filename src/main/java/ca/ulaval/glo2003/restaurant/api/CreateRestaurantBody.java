package ca.ulaval.glo2003.restaurant.api;

import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.MissingParameterException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class CreateRestaurantBody {
  public String name;
  public String capacity;
  public HashMap<String, String> hours;
  public HashMap<String, String> reservations;

  public CreateRestaurantBody() {}

  public CreateRestaurantBody(
      String name,
      String capacity,
      HashMap<String, String> hours,
      HashMap<String, String> reservations) {
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservations = reservations;
  }

  public String getName() {
    return name;
  }

  public Integer getCapacity() {
    return Integer.parseInt(capacity);
  }

  public HashMap<String, LocalTime> getHours() {

    HashMap<String, LocalTime> parsedHours = new HashMap<>();

    LocalTime openHour = LocalTime.parse(hours.get("open"));
    LocalTime closeHour = LocalTime.parse(hours.get("close"));

    parsedHours.put("open", openHour);
    parsedHours.put("close", closeHour);

    return parsedHours;
  }

  public HashMap<String, Integer> getReservations() {

    HashMap<String, Integer> reservationsIntDuration = new HashMap<>();

    if (reservations != null && reservations.containsKey("duration")) {
      Integer duration = Integer.parseInt(reservations.get("duration"));
      reservationsIntDuration.put("duration", duration);
    }

    return reservationsIntDuration;
  }

  public void validateCreateRestaurantBody() {

    if (name == null || capacity == null) {
      throw new MissingParameterException();
    }

    if (name.isBlank()) {
      throw new InvalidParameterException();
    }

    try {
      getHours();
      getCapacity();

    } catch (DateTimeParseException | NumberFormatException e) {
      throw new InvalidParameterException();

    } catch (NullPointerException e) {
      throw new MissingParameterException();
    }

    if (reservations != null) {
      if (reservations.containsKey("duration")) {

        try {
          getReservations();

        } catch (NumberFormatException e) {
          throw new InvalidParameterException();
        }
      }
    }
  }
}
