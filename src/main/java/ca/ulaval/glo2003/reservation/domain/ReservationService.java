package ca.ulaval.glo2003.reservation.domain;

import ca.ulaval.glo2003.reservation.api.ReservationDTO;
import ca.ulaval.glo2003.reservation.persistence.ReservationRepository;
import ca.ulaval.glo2003.restaurant.api.RestaurantDTO;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.persistence.RestaurantRepository;
import java.util.Map;

public class ReservationService {
  private final ReservationRepository reservationRepository;

  private final RestaurantRepository restaurantRepository;

  private final ReservationValidator reservationValidator;

  private static Integer reservationId = 0;

  private final ReservationFactory factory;

  public ReservationService(
      ReservationRepository reservationRepository,
      RestaurantRepository restaurantRepository,
      ReservationValidator reservationValidator) {

    this.reservationRepository = reservationRepository;
    this.restaurantRepository = restaurantRepository;
    this.reservationValidator = reservationValidator;

    factory = new ReservationFactory();
  }

  public String addReservation(ReservationDTO reservation) {

    Map<String, Restaurant> restaurantsList = restaurantRepository.getRestaurantsList();
    Map<String, Reservation> reservationsList = reservationRepository.getReservationsList();

    reservationValidator.validateRestaurantId(reservation.getRestaurantId(), restaurantsList);
    Restaurant restaurant = restaurantRepository.get(reservation.getRestaurantId());
    reservationValidator.validateReservationValues(restaurant, reservation);
    reservationValidator.validateCapacity(reservationsList, reservation, restaurant);

    reservationRepository.save(
        factory.createReservation(
            String.valueOf(++reservationId),
            reservation,
            restaurant.getReservations().get("duration")));

    return ("http://localhost:8080/reservations/" + reservationId);
  }

  public ReservationDTO getByNumber(String reservationId) {

    reservationValidator.validateReservationId(
        reservationId, reservationRepository.getReservationsList());

    Reservation reservation = reservationRepository.get(reservationId);
    RestaurantDTO restaurantDTO =
        new RestaurantDTO(restaurantRepository.get(reservation.getRestaurantId()));

    return new ReservationDTO(reservation, restaurantDTO);
  }

  public void deleteReservation(String reservationId) {

    reservationValidator.validateReservationId(
        reservationId, reservationRepository.getReservationsList());

    reservationRepository.delete(reservationId);
  }
}
