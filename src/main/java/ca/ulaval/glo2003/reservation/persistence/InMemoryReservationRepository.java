package ca.ulaval.glo2003.reservation.persistence;

import ca.ulaval.glo2003.reservation.domain.Reservation;
import java.util.HashMap;
import java.util.Map;

public class InMemoryReservationRepository implements ReservationRepository {
  private final Map<String, Reservation> reservationsList;

  public InMemoryReservationRepository() {
    reservationsList = new HashMap<>();
  }

  public Reservation get(String id) {
    return reservationsList.get(id);
  }

  public void save(Reservation reservation) {
    reservationsList.put(reservation.getReservationId(), reservation);
  }

  public Map<String, Reservation> getReservationsList() {
    return reservationsList;
  }

  public void delete(String id) {
    reservationsList.remove(id);
  }

  public void deleteReservationsByRestaurantId(String restaurantId) {
    reservationsList
        .entrySet()
        .removeIf(entry -> entry.getValue().getRestaurantId().equals(restaurantId));
  }

  public HashMap<String, Reservation> getReservationByRestaurantId(String restaurantId) {

    HashMap<String, Reservation> filteredReservation = new HashMap<>();

    for (Map.Entry<String, Reservation> entry : reservationsList.entrySet()) {
      String id = entry.getKey();
      Reservation reservation = entry.getValue();

      if (reservation.getRestaurantId().equals(restaurantId)) {
        filteredReservation.put(id, reservation);
      }
    }

    return filteredReservation;
  }
}
