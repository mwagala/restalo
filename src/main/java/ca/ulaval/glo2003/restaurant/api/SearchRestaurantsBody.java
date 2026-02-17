package ca.ulaval.glo2003.restaurant.api;

import ca.ulaval.glo2003.utilities.InvalidParameterException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class SearchRestaurantsBody {
  public String name;
  public HashMap<String, String> opened;

  public SearchRestaurantsBody() {}

  public String getName() {
    return name;
  }

  public HashMap<String, String> getOpened() {
    return opened;
  }

  public void validateSearchRestaurantBody() {

    try {

      try {
        LocalTime.parse(opened.get("from"));
      } catch (NullPointerException ignored) {
      }

      try {
        LocalTime.parse(opened.get("to"));
      } catch (NullPointerException ignored) {
      }

    } catch (DateTimeParseException e) {
      throw new InvalidParameterException();
    }
  }
}
