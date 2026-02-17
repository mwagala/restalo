package ca.ulaval.glo2003.reservation.persistence;

import ca.ulaval.glo2003.reservation.domain.Reservation;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MongoReservationRepository implements ReservationRepository {
  private final Datastore datastore;

  public MongoReservationRepository(Datastore datastore) {
    this.datastore = datastore;
  }

  public Reservation get(String id) {

    var query = this.datastore.find(Reservation.class).filter(Filters.eq("_id", id));
    var foundReservation = Optional.ofNullable(query.iterator().tryNext());

    return foundReservation.get();
  }

  public void save(Reservation reservation) {
    this.datastore.save(reservation);
  }

  public Map<String, Reservation> getReservationsList() {

    Map<String, Reservation> reservations = new HashMap<>();
    var reservationList = this.datastore.find(Reservation.class).stream().toList();

    for (Reservation reservation : reservationList) {
      reservations.put(reservation.getReservationId(), reservation);
    }

    return reservations;
  }

  public void delete(String id) {
    this.datastore.find(Reservation.class).filter(Filters.eq("_id", id)).delete();
  }

  public void deleteReservationsByRestaurantId(String restaurantId) {
    this.datastore
        .find(Reservation.class)
        .filter(Filters.eq("restaurantId", restaurantId))
        .delete();
  }

  public HashMap<String, Reservation> getReservationByRestaurantId(String restaurantId) {

    HashMap<String, Reservation> filteredReservation = new HashMap<>();

    for (Map.Entry<String, Reservation> entry : getReservationsList().entrySet()) {
      String id = entry.getKey();
      Reservation reservation = entry.getValue();

      if (reservation.getRestaurantId().equals(restaurantId)) {
        filteredReservation.put(id, reservation);
      }
    }

    return filteredReservation;
  }
}
