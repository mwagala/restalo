package ca.ulaval.glo2003.restaurant.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import jakarta.json.JsonObject;
import java.time.LocalTime;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestaurantDTOTest {
  String restaurantId;
  String ownerId;
  String name;
  Integer capacity;
  HashMap<String, LocalTime> hours;
  HashMap<String, Integer> reservations;

  @BeforeEach
  void setUp() {
    restaurantId = "1";
    ownerId = "456";
    name = "La Botega";
    capacity = 12;
    hours = new HashMap<>();
    hours.put("open", LocalTime.of(12, 10, 0));
    hours.put("close", LocalTime.of(20, 0, 0));
    reservations = new HashMap<>();
    reservations.put("duration", 75);
  }

  @Test
  void givenCreateRestaurantBodyToConstructor_shouldReturnTrue() {
    CreateRestaurantBody createRestaurantBody = mock(CreateRestaurantBody.class);
    when(createRestaurantBody.getReservations()).thenReturn(reservations);
    when(createRestaurantBody.getHours()).thenReturn(hours);
    when(createRestaurantBody.getCapacity()).thenReturn(capacity);
    when(createRestaurantBody.getName()).thenReturn(name);

    RestaurantDTO restaurantDTO = new RestaurantDTO(ownerId, createRestaurantBody);

    assertTrue(
        restaurantDTO.getRestaurantId() == null
            && restaurantDTO.getOwnerId() == ownerId
            && restaurantDTO.getName() == name
            && restaurantDTO.getCapacity() == capacity
            && restaurantDTO.getHours() == hours
            && restaurantDTO.getReservations() == reservations);
  }

  @Test
  void givenRestaurantToConstructor_shouldReturnTrue() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getRestaurantId()).thenReturn(restaurantId);
    when(restaurant.getOwnerId()).thenReturn(ownerId);
    when(restaurant.getName()).thenReturn(name);
    when(restaurant.getCapacity()).thenReturn(capacity);
    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getReservations()).thenReturn(reservations);

    RestaurantDTO restaurantDTO = new RestaurantDTO(restaurant);

    assertTrue(
        restaurantDTO.getRestaurantId() == restaurantId
            && restaurantDTO.getOwnerId() == ownerId
            && restaurantDTO.getName() == name
            && restaurantDTO.getCapacity() == capacity
            && restaurantDTO.getHours() == hours
            && restaurantDTO.getReservations() == reservations);
  }

  @Test
  void givenValidRestaurantDTO_whenCreateResponseBodyWithReservations_JsonObjectShouldBeValid() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getRestaurantId()).thenReturn(restaurantId);
    when(restaurant.getOwnerId()).thenReturn(ownerId);
    when(restaurant.getName()).thenReturn(name);
    when(restaurant.getCapacity()).thenReturn(capacity);
    when(restaurant.getHours()).thenReturn(hours);
    when(restaurant.getReservations()).thenReturn(reservations);
    RestaurantDTO restaurantDTO = new RestaurantDTO(restaurant);

    JsonObject responseBody = restaurantDTO.createResponseBody(true);

    assertEquals("\"1\"", responseBody.get("id").toString());
    assertEquals("\"La Botega\"", responseBody.get("name").toString());
    assertEquals("12", responseBody.get("capacity").toString());
    assertEquals(
        "{\"open\":\"12:10:00\",\"close\":\"20:00:00\"}", responseBody.get("hours").toString());
    assertEquals("{\"duration\":75}", responseBody.get("reservations").toString());
  }
}
