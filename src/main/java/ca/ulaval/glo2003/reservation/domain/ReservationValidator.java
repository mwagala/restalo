package ca.ulaval.glo2003.reservation.domain;

import ca.ulaval.glo2003.reservation.api.ReservationDTO;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.MissingParameterException;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReservationValidator {

  public void validateReservationValues(Restaurant restaurant, ReservationDTO reservation) {
    try {
      LocalTime startTime = getStartTime(reservation);

      if (startTime
              .plusMinutes(restaurant.getReservations().get("duration"))
              .isAfter(restaurant.getHours().get("close"))
          || startTime.plusMinutes(restaurant.getReservations().get("duration")).isBefore(startTime)
          || reservation.getGroupSize() < 1
          || startTime.isBefore(restaurant.getHours().get("open"))) {
        throw new InvalidParameterException();
      }

    } catch (DateTimeParseException | NumberFormatException e) {
      throw new InvalidParameterException();

    } catch (NullPointerException e) {
      throw new MissingParameterException();
    }
  }

  public void validateCapacity(
      Map<String, Reservation> reservationsList,
      ReservationDTO reservation,
      Restaurant restaurant) {

    Map<String, Reservation> currentReservations = new HashMap<>();

    for (Map.Entry<String, Reservation> entry : reservationsList.entrySet()) {
      if (reservation.getDate().equals(entry.getValue().getDate())) {
        if (Objects.equals(entry.getValue().getRestaurantId(), reservation.getRestaurantId())) {
          currentReservations.put(entry.getKey(), entry.getValue());
        }
      }
    }

    validateCapacityWithReservations(currentReservations, reservation, restaurant);
  }

  private void validateCapacityWithReservations(
      Map<String, Reservation> currentReservations,
      ReservationDTO reservation,
      Restaurant restaurant) {

    Integer reservationDuration = restaurant.getReservations().get("duration");
    LocalTime startTime = getStartTime(reservation);

    for (int i = 0; i < reservationDuration; i = i + 15) {
      Integer currentCapacity = 0;

      for (Map.Entry<String, Reservation> entry : currentReservations.entrySet()) {
        if (!entry.getValue().getStartTime().isAfter(startTime.plusMinutes(i))
            && !entry.getValue().getEndTime().isBefore(startTime.plusMinutes(i))) {
          currentCapacity += entry.getValue().getGroupSize();
        }
      }

      if (currentCapacity + reservation.getGroupSize() > restaurant.getCapacity()) {
        throw new InvalidParameterException();
      }
    }
  }

  public void validateReservationId(
      String reservationId, Map<String, Reservation> reservationsList) {

    if (!reservationsList.containsKey(reservationId)) {
      throw new NotFoundException("La reservation n'existe pas");
    }
  }

  public void validateRestaurantId(String restaurantId, Map<String, Restaurant> restaurantsList) {

    if (!restaurantsList.containsKey(restaurantId)) {
      throw new NotFoundException("Le restaurant n'existe pas");
    }
  }

  private LocalTime getStartTime(ReservationDTO reservation) {

    LocalTime startTime = reservation.getStartTime();

    if (startTime.getSecond() != 0) {
      startTime = LocalTime.of(startTime.getHour(), startTime.getMinute() + 1);
    }

    int startMod15Minutes = startTime.getMinute() % 15;

    if (startMod15Minutes != 0) {
      startTime = startTime.plusMinutes(15 - startMod15Minutes);
    }

    return startTime;
  }
}
