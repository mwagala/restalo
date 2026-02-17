package ca.ulaval.glo2003.reservation.domain;

import ca.ulaval.glo2003.reservation.api.ReservationDTO;

public class ReservationFactory {
  public Reservation createReservation(
      String reservationId, ReservationDTO reservationDTO, Integer reservationDuration) {
    return new Reservation(
        reservationDTO.getRestaurantId(),
        reservationId,
        reservationDTO.getStartTime(),
        reservationDuration,
        reservationDTO.getCustomer(),
        reservationDTO.getGroupSize(),
        reservationDTO.getDate());
  }
}
