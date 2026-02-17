package ca.ulaval.glo2003.restaurant.api;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.MissingParameterException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import org.junit.jupiter.api.*;

public class CreateRestaurantBodyTest {
  private CreateRestaurantBody restaurantBody;
  String name;
  String capacity;
  HashMap<String, String> hours;
  HashMap<String, String> reservations;
  String ownerId;
  String restaurantId;

  @BeforeEach
  void setUp() {
    name = "Paul";
    capacity = "10";
    hours = new HashMap<>();
    reservations = new HashMap<>();
    ownerId = "456";
    restaurantId = "1";

    hours.put("open", "01:00:00");
    hours.put("close", "19:30:00");

    reservations.put("duration", "75");

    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);
  }

  @Test
  void givenInvalidCapacity_whenGetCapacity_shouldThrowException() {
    String capacity = "a";
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        NumberFormatException.class,
        () -> {
          restaurantBody.getCapacity();
        });
  }

  @Test
  void givenInvalidHours_whenGetHours_shouldThrowException() {
    HashMap<String, String> hours = new HashMap<>();
    hours.put("open", "01:00:00");
    hours.put("close", "25:30:00");
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        DateTimeParseException.class,
        () -> {
          restaurantBody.getHours();
        });
  }

  @Test
  void givenNullHours_whenGetHours_shouldThrowException() {
    restaurantBody = new CreateRestaurantBody(name, capacity, null, reservations);

    assertThrows(
        NullPointerException.class,
        () -> {
          restaurantBody.getHours();
        });
  }

  @Test
  void givenNullOpenHours_whenGetHours_shouldThrowException() {
    HashMap<String, String> hours = new HashMap<>();
    hours.put("close", "23:30:00");
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        NullPointerException.class,
        () -> {
          restaurantBody.getHours();
        });
  }

  @Test
  void givenValidHours_whenGetHours_shouldReturnHours() {
    HashMap<String, LocalTime> parsedHours = new HashMap<>();
    parsedHours.put("open", LocalTime.of(1, 0, 0));
    parsedHours.put("close", LocalTime.of(19, 30, 0));

    assertEquals(parsedHours, restaurantBody.getHours());
  }

  @Test
  void givenInvalidReservationDuration_whenGetReservations_shouldThrowException() {
    HashMap<String, String> reservations = new HashMap<>();
    reservations.put("duration", "b");
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        NumberFormatException.class,
        () -> {
          restaurantBody.getReservations();
        });
  }

  @Test
  void givenValidReservationDuration_whenGetReservations_shouldReturnReservationDuration() {
    HashMap<String, Integer> parsedReservations = new HashMap<>();
    parsedReservations.put("duration", 75);

    assertEquals(parsedReservations, restaurantBody.getReservations());
  }

  @Test
  void givenNullReservations_whenGetReservations_shouldReturnEmptyMap() {
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, null);

    assertEquals(new HashMap<String, Integer>(), restaurantBody.getReservations());
  }

  @Test
  void givenValidCreateRestaurantBody_whenValidateCreateRestaurantBody_shouldNotThrowException() {
    assertDoesNotThrow(
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenNullName_whenValidateCreateRestaurantBody_shouldThrowException() {
    restaurantBody = new CreateRestaurantBody(null, capacity, hours, reservations);

    assertThrows(
        MissingParameterException.class,
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenNullCapacity_whenValidateCreateRestaurantBody_shouldThrowException() {
    restaurantBody = new CreateRestaurantBody(name, null, hours, reservations);

    assertThrows(
        MissingParameterException.class,
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenNullHours_whenValidateCreateRestaurantBody_shouldThrowException() {
    restaurantBody = new CreateRestaurantBody(name, capacity, null, reservations);

    assertThrows(
        MissingParameterException.class,
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenNullOpenHours_whenValidateCreateRestaurantBody_shouldThrowException() {
    HashMap<String, String> hours = new HashMap<>();
    hours.put("close", "23:30:00");
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        MissingParameterException.class,
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenBlankName_whenValidateCreateRestaurantBody_shouldThrowException() {
    String name = "";
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenInvalidHours_whenValidateCreateRestaurantBody_shouldThrowException() {
    HashMap<String, String> hours = new HashMap<>();
    hours.put("open", "01:00:00");
    hours.put("close", "25:30:00");
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenInvalidCapacity_whenValidateCreateRestaurantBody_shouldThrowException() {
    String capacity = "r";
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenInvalidReservationDuration_whenValidateCreateRestaurantBody_shouldThrowException() {
    HashMap<String, String> reservations = new HashMap<>();
    reservations.put("duration", "b");
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }

  @Test
  void givenNullReservationDuration_whenValidateCreateRestaurantBody_shouldNotThrowException() {
    HashMap<String, String> reservations = new HashMap<>();
    restaurantBody = new CreateRestaurantBody(name, capacity, hours, reservations);

    assertDoesNotThrow(
        () -> {
          restaurantBody.validateCreateRestaurantBody();
        });
  }
}
