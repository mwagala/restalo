package ca.ulaval.glo2003.reservation.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.reservation.domain.Reservation;
import ca.ulaval.glo2003.restaurant.api.RestaurantDTO;
import jakarta.json.JsonObject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationDTOTest {
  private ReservationDTO reservationDTO;
  String restaurantId;
  String reservationId;
  HashMap<String, String> customer;
  LocalTime startTime;
  LocalTime endTime;
  Integer groupSize;
  LocalDate date;
  Reservation reservation;
  RestaurantDTO restaurantDTO;

  @BeforeEach
  void setUp() {
    restaurantId = "1";
    reservationId = "4";
    customer = new HashMap<>();
    customer.put("name", "alice");
    customer.put("phoneNumber", "4389252369");
    customer.put("email", "test@test.com");
    startTime = LocalTime.of(21, 4, 0);
    endTime = LocalTime.of(22, 15, 0);
    groupSize = 10;
    date = LocalDate.of(2024, 4, 14);

    reservation = mock(Reservation.class);
    restaurantDTO = mock(RestaurantDTO.class);
  }

  @Test
  void givenCreateReservationBodyToConstructor_shouldReturnTrue() {
    CreateReservationBody reservationBody = mock(CreateReservationBody.class);
    when(reservationBody.getStartTime()).thenReturn(startTime);
    when(reservationBody.getGroupSize()).thenReturn(groupSize);
    when(reservationBody.getDate()).thenReturn(date);
    when(reservationBody.getCustomer()).thenReturn(customer);

    reservationDTO = new ReservationDTO(restaurantId, reservationBody);

    assertTrue(
        reservationDTO.getRestaurantId() == restaurantId
            && reservationDTO.getReservationId() == null
            && reservationDTO.getStartTime() == startTime
            && reservationDTO.getGroupSize() == groupSize
            && reservationDTO.getDate() == date
            && reservationDTO.getEndTime() == null
            && reservationDTO.getCustomer() == customer
            && reservationDTO.getAssociatedRestaurant() == null);
  }

  @Test
  void givenReservationAndRestaurantDTOToConstructor_shouldReturnTrue() {
    when(reservation.getRestaurantId()).thenReturn(restaurantId);
    when(reservation.getStartTime()).thenReturn(startTime);
    when(reservation.getReservationId()).thenReturn(reservationId);
    when(reservation.getGroupSize()).thenReturn(groupSize);
    when(reservation.getDate()).thenReturn(date);
    when(reservation.getCustomer()).thenReturn(customer);
    when(reservation.getEndTime()).thenReturn(endTime);

    reservationDTO = new ReservationDTO(reservation, restaurantDTO);

    assertTrue(
        reservationDTO.getRestaurantId() == restaurantId
            && reservationDTO.getReservationId() == reservationId
            && reservationDTO.getStartTime() == startTime
            && reservationDTO.getGroupSize() == groupSize
            && reservationDTO.getDate() == date
            && reservationDTO.getEndTime() == endTime
            && reservationDTO.getCustomer() == customer
            && reservationDTO.getAssociatedRestaurant() == restaurantDTO);
  }

  @Test
  void givenValidReservationDTO_whenCreateResponseBodyWithRestaurant_JsonObjectShouldBeValid() {
    when(reservation.getRestaurantId()).thenReturn(restaurantId);
    when(reservation.getStartTime()).thenReturn(startTime);
    when(reservation.getReservationId()).thenReturn(reservationId);
    when(reservation.getGroupSize()).thenReturn(groupSize);
    when(reservation.getDate()).thenReturn(date);
    when(reservation.getCustomer()).thenReturn(customer);
    when(reservation.getEndTime()).thenReturn(endTime);
    HashMap<String, LocalTime> hours = new HashMap<>();
    hours.put("open", LocalTime.NOON);
    hours.put("close", LocalTime.NOON.plusHours(11));
    when(restaurantDTO.getRestaurantId()).thenReturn(restaurantId);
    when(restaurantDTO.getHours()).thenReturn(hours);
    when(restaurantDTO.getName()).thenReturn("La Botega");
    when(restaurantDTO.getCapacity()).thenReturn(12);
    reservationDTO = new ReservationDTO(reservation, restaurantDTO);

    JsonObject responseBody = reservationDTO.createResponseBody(true);

    assertEquals("\"4\"", responseBody.get("number").toString());
    assertEquals("\"2024-04-14\"", responseBody.get("date").toString());
    assertEquals(
        "{\"start\":\"21:04:00\",\"end\":\"22:15:00\"}", responseBody.get("time").toString());
    assertEquals("10", responseBody.get("groupSize").toString());
    assertEquals(
        "{\"name\":\"alice\",\"email\":\"test@test.com\",\"phoneNumber\":\"4389252369\"}",
        responseBody.get("customer").toString());
    assertEquals(
        "{\"id\":\"1\",\"name\":\"La Botega\",\"capacity\":12,\"hours\":{\"open\":\"12:00:00\",\"close\":\"23:00:00\"}}",
        responseBody.get("restaurant").toString());
  }
}
