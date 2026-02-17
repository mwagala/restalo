package ca.ulaval.glo2003.reservation.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.reservation.api.ReservationDTO;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationValidatorTest {
  private ReservationValidator reservationValidator;
  HashMap<String, Integer> reservations;
  HashMap<String, LocalTime> hours;
  LocalTime startTime;
  Integer groupSize;
  Integer otherGroupSize;
  String reservationId;
  LocalDate date;
  String restaurantId;
  Integer capacity;
  String otherRestaurantId;
  String otherReservationId;

  @BeforeEach
  void setUp() {
    reservationValidator = new ReservationValidator();
    reservations = new HashMap<>();
    reservations.put("duration", 75);
    hours = new HashMap<>();
    hours.put("open", LocalTime.of(12, 0, 0));
    hours.put("close", LocalTime.of(23, 0, 0));
    startTime = LocalTime.of(13, 1, 0);
    groupSize = 10;
    otherGroupSize = 3;
    reservationId = "4";
    date = LocalDate.of(2024, 11, 2);
    restaurantId = "1";
    capacity = 12;
    otherRestaurantId = "2";
    otherReservationId = "5";
  }

  @Test
  void givenTooLateReservationStartTime_whenValidateReservationValues_shouldThrowException() {
    LocalTime startTime = LocalTime.of(22, 0, 0);
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getHours()).thenReturn(hours);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationValidator.validateReservationValues(restaurant, reservationDTO);
        });
  }

  @Test
  void givenChangingDayReservationStartTime_whenValidateReservationValues_shouldThrowException() {
    LocalTime startTime = LocalTime.of(23, 0, 0);
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getHours()).thenReturn(hours);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationValidator.validateReservationValues(restaurant, reservationDTO);
        });
  }

  @Test
  void givenNegativeGroupSize_whenValidateReservationValues_shouldThrowException() {
    Integer groupSize = -1;
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getHours()).thenReturn(hours);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);
    when(reservationDTO.getGroupSize()).thenReturn(groupSize);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationValidator.validateReservationValues(restaurant, reservationDTO);
        });
  }

  @Test
  void givenTooEarlyReservationStartTime_whenValidateReservationValues_shouldThrowException() {
    LocalTime startTime = LocalTime.of(11, 45, 0);
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getHours()).thenReturn(hours);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);
    when(reservationDTO.getGroupSize()).thenReturn(groupSize);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationValidator.validateReservationValues(restaurant, reservationDTO);
        });
  }

  @Test
  void givenValidParameters_whenValidateReservationValues_shouldNotThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getHours()).thenReturn(hours);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);
    when(reservationDTO.getGroupSize()).thenReturn(groupSize);

    assertDoesNotThrow(
        () -> {
          reservationValidator.validateReservationValues(restaurant, reservationDTO);
        });
  }

  @Test
  void givenTooBigGroupSize_whenValidateCapacity_shouldThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getCapacity()).thenReturn(capacity);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);
    when(reservationDTO.getGroupSize()).thenReturn(groupSize);
    when(reservationDTO.getDate()).thenReturn(date);
    when(reservationDTO.getRestaurantId()).thenReturn(restaurantId);
    Reservation reservation = mock(Reservation.class);
    when(reservation.getReservationId()).thenReturn(reservationId);
    when(reservation.getDate()).thenReturn(date);
    when(reservation.getRestaurantId()).thenReturn(restaurantId);
    when(reservation.getStartTime()).thenReturn(startTime.plusMinutes(29));
    when(reservation.getEndTime()).thenReturn(startTime.plusMinutes(104));
    when(reservation.getGroupSize()).thenReturn(otherGroupSize);
    Map<String, Reservation> reservationsList = new HashMap<>();
    reservationsList.put(reservation.getReservationId(), reservation);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationValidator.validateCapacity(reservationsList, reservationDTO, restaurant);
        });
  }

  @Test
  void givenValidGroupSize_whenValidateCapacity_shouldNotThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getCapacity()).thenReturn(capacity);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);
    when(reservationDTO.getGroupSize()).thenReturn(otherGroupSize);
    when(reservationDTO.getDate()).thenReturn(date);
    when(reservationDTO.getRestaurantId()).thenReturn(restaurantId);
    Reservation reservation = mock(Reservation.class);
    when(reservation.getReservationId()).thenReturn(reservationId);
    when(reservation.getDate()).thenReturn(date);
    when(reservation.getRestaurantId()).thenReturn(restaurantId);
    when(reservation.getStartTime()).thenReturn(startTime.plusMinutes(29));
    when(reservation.getEndTime()).thenReturn(startTime.plusMinutes(104));
    when(reservation.getGroupSize()).thenReturn(otherGroupSize);
    Map<String, Reservation> reservationsList = new HashMap<>();
    reservationsList.put(reservation.getReservationId(), reservation);

    assertDoesNotThrow(
        () -> {
          reservationValidator.validateCapacity(reservationsList, reservationDTO, restaurant);
        });
  }

  @Test
  void givenReservationNotAtSameTime_whenValidateCapacity_shouldNotThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getCapacity()).thenReturn(capacity);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);
    when(reservationDTO.getGroupSize()).thenReturn(groupSize);
    when(reservationDTO.getDate()).thenReturn(date);
    when(reservationDTO.getRestaurantId()).thenReturn(restaurantId);
    Reservation reservation = mock(Reservation.class);
    when(reservation.getReservationId()).thenReturn(reservationId);
    when(reservation.getDate()).thenReturn(date);
    when(reservation.getRestaurantId()).thenReturn(restaurantId);
    when(reservation.getStartTime()).thenReturn(startTime.plusMinutes(80));
    Map<String, Reservation> reservationsList = new HashMap<>();
    reservationsList.put(reservation.getReservationId(), reservation);

    assertDoesNotThrow(
        () -> {
          reservationValidator.validateCapacity(reservationsList, reservationDTO, restaurant);
        });
  }

  @Test
  void givenReservationNotAtSameDate_whenValidateCapacity_shouldNotThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getCapacity()).thenReturn(capacity);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);
    when(reservationDTO.getGroupSize()).thenReturn(groupSize);
    when(reservationDTO.getDate()).thenReturn(date);
    Reservation reservation = mock(Reservation.class);
    when(reservation.getReservationId()).thenReturn(reservationId);
    when(reservation.getDate()).thenReturn(date.plusDays(1));
    Map<String, Reservation> reservationsList = new HashMap<>();
    reservationsList.put(reservation.getReservationId(), reservation);

    assertDoesNotThrow(
        () -> {
          reservationValidator.validateCapacity(reservationsList, reservationDTO, restaurant);
        });
  }

  @Test
  void givenReservationNotAtSameRestaurant_whenValidateCapacity_shouldNotThrowException() {
    Restaurant restaurant = mock(Restaurant.class);
    when(restaurant.getReservations()).thenReturn(reservations);
    when(restaurant.getCapacity()).thenReturn(capacity);
    ReservationDTO reservationDTO = mock(ReservationDTO.class);
    when(reservationDTO.getStartTime()).thenReturn(startTime);
    when(reservationDTO.getGroupSize()).thenReturn(groupSize);
    when(reservationDTO.getDate()).thenReturn(date);
    when(reservationDTO.getRestaurantId()).thenReturn(restaurantId);
    Reservation reservation = mock(Reservation.class);
    when(reservation.getReservationId()).thenReturn(reservationId);
    when(reservation.getDate()).thenReturn(date);
    when(reservation.getRestaurantId()).thenReturn(otherRestaurantId);
    Map<String, Reservation> reservationsList = new HashMap<>();
    reservationsList.put(reservation.getReservationId(), reservation);

    assertDoesNotThrow(
        () -> {
          reservationValidator.validateCapacity(reservationsList, reservationDTO, restaurant);
        });
  }

  @Test
  void givenNonExistingId_whenValidateReservationId_shouldThrowException() {
    Reservation reservation = mock(Reservation.class);
    when(reservation.getReservationId()).thenReturn(otherReservationId);
    Map<String, Reservation> reservationsList = new HashMap<>();
    reservationsList.put(reservation.getReservationId(), reservation);

    assertThrows(
        NotFoundException.class,
        () -> {
          reservationValidator.validateReservationId(reservationId, reservationsList);
        });
  }

  @Test
  void givenExistingId_whenValidateReservationId_shouldNotThrowException() {
    Reservation reservation = mock(Reservation.class);
    when(reservation.getReservationId()).thenReturn(reservationId);
    Map<String, Reservation> reservationsList = new HashMap<>();
    reservationsList.put(reservation.getReservationId(), reservation);

    assertDoesNotThrow(
        () -> {
          reservationValidator.validateReservationId(reservationId, reservationsList);
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
          reservationValidator.validateRestaurantId(restaurantId, restaurantsList);
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
          reservationValidator.validateRestaurantId(restaurantId, restaurantsList);
        });
  }
}
