package ca.ulaval.glo2003.restaurant.domain;

import ca.ulaval.glo2003.restaurant.api.RestaurantDTO;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class RestaurantValidator {
  public void validateRestaurantValues(RestaurantDTO restaurant) {
    HashMap<String, LocalTime> hours = restaurant.getHours();

    if (restaurant.getOwnerId().isEmpty()
        || restaurant.getCapacity() < 1
        || hours.get("open").isBefore(LocalTime.parse("00:00:00"))
        || hours.get("close").isAfter(LocalTime.parse("23:59:59"))
        || Duration.between(hours.get("open"), hours.get("close")).compareTo(Duration.ofHours(1))
            < 0) {
      throw new InvalidParameterException();
    }

    if (!restaurant.getReservations().isEmpty()) {
      if (restaurant.getReservations().containsKey("duration")
          && restaurant.getReservations().get("duration") < 1) {
        throw new InvalidParameterException();
      }
    }
  }

  public void validateRestaurantId(String restaurantId, Map<String, Restaurant> restaurantsList) {

    if (!restaurantsList.containsKey(restaurantId)) {
      throw new NotFoundException("Le restaurant n'existe pas");
    }
  }

  public void validateOwnerId(Restaurant restaurant, String ownerId) {

    if (!restaurant.getOwnerId().equals(ownerId)) {
      throw new NotFoundException("Le restaurant n'appartient pas au restaurateur");
    }
  }

  public void validateHours(HashMap<String, String> opened) {

    LocalTime openedFrom = null;
    LocalTime openedTo = null;

    try {
      openedFrom = LocalTime.parse(opened.get("from"));
    } catch (NullPointerException ignored) {
    }

    try {
      openedTo = LocalTime.parse(opened.get("to"));
    } catch (NullPointerException ignored) {
    }

    if ((openedFrom != null
            && (openedFrom.isBefore(LocalTime.parse("00:00:00"))
                || openedFrom.isAfter(LocalTime.parse("23:59:59"))))
        || (openedTo != null
            && (openedTo.isBefore(LocalTime.parse("00:00:00"))
                || openedTo.isAfter(LocalTime.parse("23:59:59"))))
        || (openedFrom != null && openedTo != null && openedFrom.isAfter(openedTo))) {

      throw new InvalidParameterException();
    }
  }

  public LocalDate validateDate(String date) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate parsedDate;

    try {
      parsedDate = LocalDate.parse(date, formatter);
    } catch (DateTimeParseException e) {
      throw new InvalidParameterException();
    }

    return parsedDate;
  }
}
