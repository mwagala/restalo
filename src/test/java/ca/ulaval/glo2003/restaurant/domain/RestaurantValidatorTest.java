package ca.ulaval.glo2003.restaurant.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ca.ulaval.glo2003.restaurant.api.RestaurantDTO;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;

public class RestaurantValidatorTest {
  private RestaurantValidator restaurantValidator;
  HashMap<String, LocalTime> hours;
  String ownerId;
  Integer capacity;
  HashMap<String, Integer> reservations;
  String restaurantId;
  String otherRestaurantId;
  String otherOwnerId;

  @BeforeEach
  void setup() {
    restaurantValidator = new RestaurantValidator();
    hours = new HashMap<>();
    hours.put("open", LocalTime.of(12, 0, 0));
    hours.put("close", LocalTime.of(23, 0, 0));
    ownerId = "456";
    capacity = 12;
    reservations = new HashMap<>();
    reservations.put("duration", 60);
    restaurantId = "1";
    otherRestaurantId = "2";
    otherOwnerId = "345";
  }

  @Test
  void givenValidParameters_whenValidateRestaurantValues_shouldNotThrowException() {
    RestaurantDTO restaurantDTO = mock(RestaurantDTO.class);
    when(restaurantDTO.getHours()).thenReturn(hours);
    when(restaurantDTO.getOwnerId()).thenReturn(ownerId);
    when(restaurantDTO.getReservations()).thenReturn(reservations);
    when(restaurantDTO.getCapacity()).thenReturn(capacity);

    assertDoesNotThrow(
        () -> {
          restaurantValidator.validateRestaurantValues(restaurantDTO);
        });
  }

  @Test
  void givenEmptyOwnerId_whenValidateRestaurantValues_shouldThrowException() {
    String ownerId = "";
    RestaurantDTO restaurantDTO = mock(RestaurantDTO.class);
    when(restaurantDTO.getHours()).thenReturn(hours);
    when(restaurantDTO.getOwnerId()).thenReturn(ownerId);
    when(restaurantDTO.getReservations()).thenReturn(reservations);
    when(restaurantDTO.getCapacity()).thenReturn(capacity);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantValidator.validateRestaurantValues(restaurantDTO);
        });
  }

  @Test
  void givenNegativeCapacity_whenValidateRestaurantValues_shouldThrowException() {
    Integer capacity = -1;
    RestaurantDTO restaurantDTO = mock(RestaurantDTO.class);
    when(restaurantDTO.getHours()).thenReturn(hours);
    when(restaurantDTO.getOwnerId()).thenReturn(ownerId);
    when(restaurantDTO.getReservations()).thenReturn(reservations);
    when(restaurantDTO.getCapacity()).thenReturn(capacity);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantValidator.validateRestaurantValues(restaurantDTO);
        });
  }

  @Test
  void givenNotEnoughOpenTime_whenValidateRestaurantValues_shouldThrowException() {
    HashMap<String, LocalTime> hours = new HashMap<>();
    hours.put("open", LocalTime.of(12, 0, 0));
    hours.put("close", LocalTime.of(12, 58, 0));
    RestaurantDTO restaurantDTO = mock(RestaurantDTO.class);
    when(restaurantDTO.getHours()).thenReturn(hours);
    when(restaurantDTO.getOwnerId()).thenReturn(ownerId);
    when(restaurantDTO.getReservations()).thenReturn(reservations);
    when(restaurantDTO.getCapacity()).thenReturn(capacity);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantValidator.validateRestaurantValues(restaurantDTO);
        });
  }

  @Test
  void givenNegativeReservationDuration_whenValidateRestaurantValues_shouldThrowException() {
    HashMap<String, Integer> reservations = new HashMap<>();
    reservations.put("duration", -1);
    RestaurantDTO restaurantDTO = mock(RestaurantDTO.class);
    when(restaurantDTO.getHours()).thenReturn(hours);
    when(restaurantDTO.getOwnerId()).thenReturn(ownerId);
    when(restaurantDTO.getReservations()).thenReturn(reservations);
    when(restaurantDTO.getCapacity()).thenReturn(capacity);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantValidator.validateRestaurantValues(restaurantDTO);
        });
  }

  @Test
  void givenNullReservationDuration_whenValidateRestaurantValues_shouldNotThrowException() {
    HashMap<String, Integer> reservations = new HashMap<>();
    RestaurantDTO restaurantDTO = mock(RestaurantDTO.class);
    when(restaurantDTO.getHours()).thenReturn(hours);
    when(restaurantDTO.getOwnerId()).thenReturn(ownerId);
    when(restaurantDTO.getReservations()).thenReturn(reservations);
    when(restaurantDTO.getCapacity()).thenReturn(capacity);

    assertDoesNotThrow(
        () -> {
          restaurantValidator.validateRestaurantValues(restaurantDTO);
        });
  }

  @Test
  void givenNonExistingId_whenValidateRestaurantId_shouldThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getRestaurantId()).thenReturn(otherRestaurantId);
    Map<String, Restaurant> restaurantsList = new HashMap<>();
    restaurantsList.put(restaurant.getRestaurantId(), restaurant);

    assertThrows(
        NotFoundException.class,
        () -> {
          restaurantValidator.validateRestaurantId(restaurantId, restaurantsList);
        });
  }

  @Test
  void givenExistingId_whenValidateRestaurantId_shouldNotThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getRestaurantId()).thenReturn(restaurantId);
    Map<String, Restaurant> restaurantsList = new HashMap<>();
    restaurantsList.put(restaurant.getRestaurantId(), restaurant);

    assertDoesNotThrow(
        () -> {
          restaurantValidator.validateRestaurantId(restaurantId, restaurantsList);
        });
  }

  @Test
  void givenInvalidId_whenValidateOwnerId_shouldThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getOwnerId()).thenReturn(otherOwnerId);

    assertThrows(
        NotFoundException.class,
        () -> {
          restaurantValidator.validateOwnerId(restaurant, ownerId);
        });
  }

  @Test
  void givenValidId_whenValidateOwnerId_shouldNotThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getOwnerId()).thenReturn(ownerId);

    assertDoesNotThrow(
        () -> {
          restaurantValidator.validateOwnerId(restaurant, ownerId);
        });
  }

  @Test
  void givenInvalidOpenHours_whenValidateHours_shouldThrowException() {
    HashMap<String, String> opened = new HashMap<>();
    opened.put("to", "11:00:00");
    opened.put("from", "12:00:00");

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantValidator.validateHours(opened);
        });
  }

  @Test
  void givenValidOpenHours_whenValidateHours_shouldNotThrowException() {
    HashMap<String, String> opened = new HashMap<>();
    opened.put("to", "12:00:00");
    opened.put("from", "11:00:00");

    assertDoesNotThrow(
        () -> {
          restaurantValidator.validateHours(opened);
        });
  }

  @Test
  void givenInvalidFormattedDate_whenValidateDate_shouldThrowException() {
    String date = "20240302";

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantValidator.validateDate(date);
        });
  }

  @Test
  void givenInvalidDate_whenValidateDate_shouldThrowException() {
    String date = "2024-13-02";

    assertThrows(
        InvalidParameterException.class,
        () -> {
          restaurantValidator.validateDate(date);
        });
  }

  @Test
  void givenValidDate_whenValidateDate_shouldNotThrowException() {
    String date = "2024-11-02";

    assertDoesNotThrow(
        () -> {
          restaurantValidator.validateDate(date);
        });
  }
}
