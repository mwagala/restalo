package ca.ulaval.glo2003.reservation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationTest {
  private Reservation reservation;
  String restaurantId;
  String reservationId;
  LocalTime startTime;
  Integer reservationDuration;
  HashMap<String, String> customer;
  Integer groupSize;
  LocalDate date;
  LocalTime endTime;
  LocalTime roundMinutesStartTime;

  @BeforeEach
  void setUp() {
    restaurantId = "1";
    reservationId = "2";
    startTime = LocalTime.parse("21:04");
    reservationDuration = 60;
    customer = new HashMap<>();
    customer.put("name", "alice");
    customer.put("phoneNumber", "1234567890");
    customer.put("email", "abc@xyz.com");
    groupSize = 10;
    date = LocalDate.of(2024, 2, 1);
    endTime = LocalTime.parse("22:15");
    roundMinutesStartTime = LocalTime.of(21, 15, 0);

    reservation =
        new Reservation(
            restaurantId, reservationId, startTime, reservationDuration, customer, groupSize, date);
  }

  @Test
  void givenValidParametersAndRoundMinutesStartTime_constructorShouldBeValid() {
    Reservation reservation =
        new Reservation(
            restaurantId,
            reservationId,
            roundMinutesStartTime,
            reservationDuration,
            customer,
            groupSize,
            date);
    assertTrue(
        reservation.getRestaurantId() == restaurantId
            && reservation.getReservationId() == reservationId
            && reservation.getStartTime() == roundMinutesStartTime
            && reservation.getCustomer() == customer
            && reservation.getGroupSize() == groupSize
            && reservation.getDate() == date
            && reservation.getEndTime().equals(endTime));
  }

  @Test
  void givenNotRoundMinutesStartTime_constructorShouldChangeStartAndEndTimeAutomatically() {
    assertTrue(
        reservation.getRestaurantId() == restaurantId
            && reservation.getReservationId() == reservationId
            && reservation.getStartTime().equals(roundMinutesStartTime)
            && reservation.getCustomer() == customer
            && reservation.getGroupSize() == groupSize
            && reservation.getDate() == date
            && reservation.getEndTime().equals(endTime));
  }

  @Test
  void givenNotRoundMinutesStartTime_whenGetStartTime_shouldReturnRoundMinutesStartTime() {
    assertEquals(reservation.getStartTime(), roundMinutesStartTime);
  }

  @Test
  void givenRoundMinutesStartTime_whenGetStartTime_shouldReturnRoundMinutesStartTime() {
    reservation =
        new Reservation(
            restaurantId,
            reservationId,
            roundMinutesStartTime,
            reservationDuration,
            customer,
            groupSize,
            date);

    assertEquals(roundMinutesStartTime, reservation.getStartTime());
  }

  @Test
  void givenNotRoundMinutesStartTime_whenGetEndTime_shouldReturnRoundMinutes() {
    assertEquals(endTime, reservation.getEndTime());
  }
}
