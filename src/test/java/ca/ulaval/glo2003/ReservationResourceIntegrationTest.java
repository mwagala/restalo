package ca.ulaval.glo2003;

import static com.google.common.truth.Truth.assertThat;

import ca.ulaval.glo2003.reservation.api.ReservationResource;
import ca.ulaval.glo2003.reservation.domain.Reservation;
import ca.ulaval.glo2003.reservation.domain.ReservationService;
import ca.ulaval.glo2003.reservation.domain.ReservationValidator;
import ca.ulaval.glo2003.reservation.persistence.InMemoryReservationRepository;
import ca.ulaval.glo2003.reservation.persistence.ReservationRepository;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.persistence.InMemoryRestaurantRepository;
import ca.ulaval.glo2003.restaurant.persistence.RestaurantRepository;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationResourceIntegrationTest extends JerseyTest {
  public static final String NAME = "La Botega";
  public static final Integer CAPACITY = 12;
  public static final String OWNER_ID = "1";
  public static final String RESTAURANT_ID = "1";
  public static final String RESERVATION_ID = "1";
  public static final LocalTime START_TIME = LocalTime.parse("10:30:00");
  public static final Integer RESERVATION_DURATION = 120;
  public static final Integer GROUPE_SIZE = 4;
  public static final LocalDate DATE = LocalDate.parse("2024-06-12");

  HashMap<String, LocalTime> hours = new HashMap<>();
  HashMap<String, Integer> reservationsDuration = new HashMap<>();
  HashMap<String, String> customer = new HashMap<>();

  RestaurantRepository restaurantRepository;
  ReservationRepository reservationRepository;
  ReservationValidator validator;

  @Override
  protected Application configure() {
    restaurantRepository = new InMemoryRestaurantRepository();
    reservationRepository = new InMemoryReservationRepository();
    validator = new ReservationValidator();
    return new ResourceConfig()
        .register(
            new ReservationResource(
                new ReservationService(reservationRepository, restaurantRepository, validator)));
  }

  @BeforeEach
  void setup() {
    hours.put("open", LocalTime.parse("19:30:00"));
    hours.put("close", LocalTime.parse("19:30:00"));
    reservationsDuration.put("duration", 120);
    customer.put("name", "John Deer");
    customer.put("email", "john.deer@gmail.com");
    customer.put("phoneNumber", "1234567890");

    Restaurant newResto =
        new Restaurant(RESTAURANT_ID, OWNER_ID, NAME, CAPACITY, hours, reservationsDuration);
    Reservation newReservation =
        new Reservation(
            RESTAURANT_ID,
            RESERVATION_ID,
            START_TIME,
            RESERVATION_DURATION,
            customer,
            GROUPE_SIZE,
            DATE);

    restaurantRepository.save(newResto);
    reservationRepository.save(newReservation);
  }

  @Test
  public void whenDeleteAValidRestaurant_shouldReturnStatus204() {
    Response response = target("reservations/" + RESERVATION_ID).request().delete();

    assertThat(response.getStatus()).isEqualTo(204);
  }
}
