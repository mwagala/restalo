package ca.ulaval.glo2003.reservation.domain;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.reservation.api.CreateReservationBody;
import ca.ulaval.glo2003.reservation.api.ReservationDTO;
import ca.ulaval.glo2003.reservation.persistence.InMemoryReservationRepository;
import ca.ulaval.glo2003.reservation.persistence.ReservationRepository;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.persistence.InMemoryRestaurantRepository;
import ca.ulaval.glo2003.restaurant.persistence.RestaurantRepository;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;

class ReservationServiceTest {

  private ReservationService reservationService;

  private ReservationRepository reservationRepository;

  private RestaurantRepository restaurantRepository;

  private ReservationValidator reservationValidator;

  Restaurant restaurant;

  CreateReservationBody createReservationBody;
  String groupSize;

  String date;

  String startTime;

  HashMap<String, String> customer;

  String reservationId;

  ReservationDTO reservationDTO;

  @BeforeEach
  void setUp() {
    restaurantRepository = new InMemoryRestaurantRepository();
    HashMap<String, LocalTime> hours = new HashMap<>();
    hours.put("open", LocalTime.parse("10:00:00"));
    hours.put("close", LocalTime.parse("20:00:00"));
    HashMap<String, Integer> reservations = new HashMap<>();
    restaurant = new Restaurant("1", "2", "Test", 10, hours, reservations);
    reservationRepository = new InMemoryReservationRepository();
    restaurantRepository = new InMemoryRestaurantRepository();
    restaurantRepository.save(restaurant);
    reservationValidator = new ReservationValidator();
    reservationService =
        new ReservationService(reservationRepository, restaurantRepository, reservationValidator);
    date = "2024-02-21";
    startTime = "11:00:00";
    groupSize = "8";
    customer = new HashMap<>();
    customer.put("name", "Catherine");
    customer.put("phoneNumber", "1234567890");
    customer.put("email", "abc@xyz.com");
    createReservationBody = new CreateReservationBody(startTime, customer, groupSize, date);
    reservationId = "1";

    reservationDTO = new ReservationDTO(restaurant.getRestaurantId(), createReservationBody);

    reservationService.addReservation(reservationDTO);
  }

  @Test
  void givenReservation_whenAddReservation_reservationShouldBeAdded() {
    assertEquals(1, reservationRepository.getReservationsList().values().size());
  }

  @Test
  void givenReservationId_whenGetByNumber_shouldFindRightReservation() {
    Reservation reservation = reservationRepository.get(reservationId);

    assertEquals(reservationDTO.getGroupSize(), reservation.getGroupSize());
    assertEquals(reservationDTO.getDate(), reservation.getDate());
  }

  @Test
  void givenReservationId_whenDeleteReservation_reservationShouldBeDeleted() {
    String newReservationId = "1";

    for (Map.Entry<String, Reservation> entry :
        reservationRepository.getReservationsList().entrySet()) {
      newReservationId = entry.getValue().getReservationId();
      reservationService.deleteReservation(newReservationId);
    }

    assertNull(reservationRepository.get(newReservationId));
  }
}
