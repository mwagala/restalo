package ca.ulaval.glo2003.reservation.persistence;

import ca.ulaval.glo2003.reservation.domain.Reservation;
import java.util.HashMap;
import java.util.Map;

public interface ReservationRepository {
  Reservation get(String id);

  void save(Reservation reservation);

  Map<String, Reservation> getReservationsList();

  void delete(String id);

  void deleteReservationsByRestaurantId(String restaurantId);

  HashMap<String, Reservation> getReservationByRestaurantId(String restaurantId);
}
