package ca.ulaval.glo2003.restaurant.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Objects;
import org.junit.jupiter.api.*;

public class RestaurantTest {
  private Restaurant restaurant;
  String restaurantId;
  String ownerId;
  String name;
  Integer capacity;
  HashMap<String, LocalTime> hours;
  HashMap<String, Integer> reservationsIntDuration;

  @BeforeEach
  void setUp() {
    restaurantId = "1";
    ownerId = "Mr. Patate";
    name = "La patate";
    capacity = 10;
    hours = new HashMap<>();
    reservationsIntDuration = new HashMap<>();

    hours.put("open", LocalTime.parse("01:00:00"));
    hours.put("close", LocalTime.parse("19:30:00"));

    reservationsIntDuration.put("duration", 75);

    restaurant =
        new Restaurant(restaurantId, ownerId, name, capacity, hours, reservationsIntDuration);
  }

  @Test
  void givenReservationDuration_whenCreateConstructor_constructorShouldUseReservationDuration() {
    assertTrue(
        restaurant.getRestaurantId() == restaurantId
            && restaurant.getOwnerId() == ownerId
            && restaurant.getName() == name
            && restaurant.getCapacity() == capacity
            && restaurant.getHours() == hours
            && restaurant.getReservations().get("duration")
                == reservationsIntDuration.get("duration"));
  }

  @Test
  void givenNoReservationDuration_whenCreateConstructor_constructorShouldPut60Minutes() {
    HashMap<String, Integer> emptyReservationsIntDuration = new HashMap<>();
    HashMap<String, Integer> expectedResult = new HashMap<>();
    expectedResult.put("duration", 60);

    restaurant =
        new Restaurant(restaurantId, ownerId, name, capacity, hours, emptyReservationsIntDuration);

    assertTrue(
        restaurant.getRestaurantId() == restaurantId
            && restaurant.getOwnerId() == ownerId
            && restaurant.getName() == name
            && restaurant.getCapacity() == capacity
            && restaurant.getHours() == hours
            && Objects.equals(
                restaurant.getReservations().get("duration"), expectedResult.get("duration")));
  }
}
