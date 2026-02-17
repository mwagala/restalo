package ca.ulaval.glo2003.restaurant.domain;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo2003.itemMenu.persistence.InMemoryItemMenuRepository;
import ca.ulaval.glo2003.reservation.api.ReservationDTO;
import ca.ulaval.glo2003.reservation.domain.Reservation;
import ca.ulaval.glo2003.reservation.persistence.InMemoryReservationRepository;
import ca.ulaval.glo2003.restaurant.api.CreateRestaurantBody;
import ca.ulaval.glo2003.restaurant.api.DispoDTO;
import ca.ulaval.glo2003.restaurant.api.RestaurantDTO;
import ca.ulaval.glo2003.restaurant.api.RestaurantSearchDTO;
import ca.ulaval.glo2003.restaurant.persistence.InMemoryRestaurantRepository;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import org.glassfish.grizzly.utils.Pair;
import org.junit.Assert;
import org.junit.jupiter.api.*;

public class RestaurantServiceTest {
  InMemoryRestaurantRepository restaurantRepository;
  InMemoryReservationRepository reservationRepository;
  InMemoryItemMenuRepository itemMenuRepository;
  RestaurantValidator validator;
  RestaurantService service;
  private RestaurantDTO restaurantDTO;
  String name;
  String capacity;
  HashMap<String, String> hours;
  HashMap<String, String> reservations;
  HashMap<String, Integer> reservationsIntDuration;
  Restaurant restaurant1;
  Restaurant restaurant2;
  HashMap<String, String> customer1;
  Reservation reservation1;
  ReservationDTO reservationDTO1;
  ArrayList<ReservationDTO> reservationDTOSList;
  HashMap<String, LocalTime> savedHours;

  @BeforeEach
  void setup() {
    restaurantRepository = new InMemoryRestaurantRepository();
    reservationRepository = new InMemoryReservationRepository();
    itemMenuRepository = new InMemoryItemMenuRepository();
    validator = new RestaurantValidator();
    service =
        new RestaurantService(
            restaurantRepository, reservationRepository, itemMenuRepository, validator);

    name = "Paul";
    capacity = "10";
    hours = new HashMap<>();
    hours.put("open", "01:00:00");
    hours.put("close", "19:30:00");

    HashMap<String, LocalTime> savedHours = new HashMap<>();
    savedHours.put("open", LocalTime.parse("12:10:00"));
    savedHours.put("close", LocalTime.parse("23:10:00"));

    reservations = new HashMap<>();
    reservationsIntDuration = new HashMap<>();

    reservations.put("duration", "60");
    reservationsIntDuration.put("duration", 60);

    restaurantDTO =
        new RestaurantDTO("Tuco", new CreateRestaurantBody(name, capacity, hours, reservations));

    restaurant1 =
        new Restaurant("2", "Pablo", "Chez Pablo", 50, savedHours, reservationsIntDuration);
    restaurant2 =
        new Restaurant("3", "Pablo", "Chez Pablo 2", 45, savedHours, reservationsIntDuration);
    restaurantRepository.save(restaurant1);
    restaurantRepository.save(restaurant2);

    customer1 = new HashMap<>();
    customer1.put("name", "Henry Schrute");
    customer1.put("email", "henry@test.ca");
    customer1.put("phoneNumber", "1234567890");
    HashMap<String, String> customer2 = new HashMap<>();
    customer2.put("name", "Michael Scott");
    customer2.put("email", "scott@test.ca");
    customer2.put("phoneNumber", "1234987890");
    reservation1 =
        new Reservation(
            restaurant1.getRestaurantId(),
            "1",
            LocalTime.parse("13:15:00"),
            75,
            customer1,
            2,
            LocalDate.parse("2024-01-02"));
    Reservation reservation2 =
        new Reservation(
            restaurant1.getRestaurantId(),
            "2",
            LocalTime.parse("16:00:00"),
            75,
            customer2,
            1,
            LocalDate.parse("2024-01-18"));
    RestaurantDTO restaurantDTO1 = new RestaurantDTO(restaurant1);
    reservationDTO1 = new ReservationDTO(reservation1, restaurantDTO1);
    ReservationDTO reservationDTO2 = new ReservationDTO(reservation2, restaurantDTO1);
    reservationRepository.save(reservation1);
    reservationRepository.save(reservation2);
    reservationDTOSList = new ArrayList<>();
    reservationDTOSList.add(reservationDTO1);
    reservationDTOSList.add(reservationDTO2);
  }

  @Test
  public void givenValidArguments_whenAddRestaurant_shouldNotThrowException() {
    service.addRestaurant(restaurantDTO);
  }

  @Test
  public void givenInvalidOwnerId_whenAddRestaurant_shouldThrowException() {
    assertThrows(
        Exception.class,
        () -> {
          RestaurantDTO invalidRestaurant =
              new RestaurantDTO("", new CreateRestaurantBody(name, capacity, hours, reservations));
          service.addRestaurant(invalidRestaurant);
        });
  }

  @Test
  public void givenInvalidBody_whenAddRestaurant_shouldThrowException() {
    assertThrows(
        InvalidParameterException.class,
        () -> {
          RestaurantDTO invalidRestaurant =
              new RestaurantDTO("Pablo", new CreateRestaurantBody(name, "-1", hours, reservations));
          service.addRestaurant(invalidRestaurant);
        });
  }

  @Test
  public void
      givenValidArguments_whenGetOwnerRestaurantsList_shouldReturnListOfAllRestaurantFromOwner() {
    ArrayList<RestaurantDTO> restaurantList = service.getOwnerRestaurantsList("Pablo");

    assertEquals(restaurant1.getRestaurantId(), restaurantList.get(0).getRestaurantId());
    assertEquals(restaurant2.getRestaurantId(), restaurantList.get(1).getRestaurantId());
  }

  @Test
  public void givenValidArguments_whenGetById_shouldReturnTheRightObject() {
    RestaurantDTO result = service.getById(restaurant1.getOwnerId(), restaurant1.getRestaurantId());

    assertEquals(restaurant1.getName(), result.getName());
  }

  @Test
  public void givenInvalidOwnerId_whenGetById_shouldThrowException() {
    assertThrows(NotFoundException.class, () -> service.getById("Linguini", "1"));
  }

  @Test
  public void givenValidArguments_whenSearchRestaurants_listSizeShouldBe2() {
    HashMap<String, String> opened = new HashMap<>();
    opened.put("from", "12:10:00");
    RestaurantSearchDTO searchDTO = new RestaurantSearchDTO("", opened);

    ArrayList<RestaurantDTO> restaurantList = service.searchRestaurants(searchDTO);

    assertEquals(2, restaurantList.size());
  }

  @Test
  public void givenNotEmptyListWithBadPrompt_whenSearchRestaurants_listSizeShouldBe0() {
    HashMap<String, String> opened = new HashMap<>();
    opened.put("from", "23:20:00");
    RestaurantSearchDTO restaurantSearch = new RestaurantSearchDTO("", opened);

    ArrayList<RestaurantDTO> result = service.searchRestaurants(restaurantSearch);

    assertEquals(0, result.size());
  }

  @Test
  public void givenSearchBodyNull_whenSearchRestaurants_listSizeShouldBe2() {
    ArrayList<RestaurantDTO> result =
        service.searchRestaurants(new RestaurantSearchDTO(null, null));
    assertEquals(2, result.size());
  }

  @Test
  void givenValidOwnerIdAndRestaurantId_whenDeleteRestaurant_shouldDeleteRestaurant() {
    service.deleteRestaurant(restaurant1.getOwnerId(), restaurant1.getRestaurantId());
    service.deleteRestaurant(restaurant2.getOwnerId(), restaurant2.getRestaurantId());

    assertNull(restaurantRepository.get(restaurant1.getRestaurantId()));
    assertNull(restaurantRepository.get(restaurant2.getRestaurantId()));
  }

  @Test
  void givenInvalidOwnerId_whenDeleteRestaurant_shouldThrowException() {
    assertThrows(
        Exception.class,
        () ->
            service.deleteRestaurant(
                restaurant1.getOwnerId() + "invalid", restaurant1.getRestaurantId()));
  }

  @Test
  void givenInvalidRestaurantId_whenDeleteRestaurant_shouldThrowException() {
    assertThrows(
        Exception.class,
        () ->
            service.deleteRestaurant(
                restaurant1.getOwnerId(), restaurant1.getRestaurantId() + "invalid"));
  }

  @Test
  void
      givenReservationBelongToOwner_whenGetReservationsList_reservationShouldEqualReservationList() {
    reservationDTOSList =
        service.getReservationsList(restaurant1.getOwnerId(), restaurant1.getRestaurantId());

    assertTrue(
        reservationDTO1.getReservationId().equals(reservationDTOSList.get(0).getReservationId())
            && reservationDTO1
                .getRestaurantId()
                .equals(reservationDTOSList.get(0).getRestaurantId()));
  }

  @Test
  void givenRestaurantDoesntExist_whenGetReservationsList_shouldThrowException() {
    assertThrows(
        NotFoundException.class,
        () -> {
          service.getReservationsList(restaurant1.getOwnerId(), "10");
        });
  }

  @Test
  void givenRestaurantDoesntBelongToOwner_whenGetReservationsList_shouldThrowException() {
    assertThrows(
        NotFoundException.class,
        () -> {
          service.getReservationsList("10", restaurant1.getRestaurantId());
        });
  }

  @Test
  void givenGetReservationsList_whenRestaurantHasOneReservation_reservationListSizeShouldBe1() {
    restaurant1 =
        new Restaurant("10", "Pablo", "Chez Pablo", 50, savedHours, reservationsIntDuration);
    restaurantRepository.save(restaurant1);
    reservation1 =
        new Reservation(
            restaurant1.getRestaurantId(),
            "1",
            LocalTime.parse("13:15:00"),
            75,
            customer1,
            2,
            LocalDate.parse("2024-01-02"));
    reservationRepository.save(reservation1);
    reservationDTOSList =
        service.getReservationsList(restaurant1.getOwnerId(), restaurant1.getRestaurantId());

    assertEquals(1, reservationDTOSList.size());
  }

  @Test
  void givenRestaurantNoReservation_whenGetReservationsList_listSizeShouldBe0() {
    restaurant1 =
        new Restaurant("10", "Pablo", "Chez Pablo", 50, savedHours, reservationsIntDuration);
    restaurantRepository.save(restaurant1);
    reservationDTOSList =
        service.getReservationsList(restaurant1.getOwnerId(), restaurant1.getRestaurantId());

    assertEquals(0, reservationDTOSList.size());
  }

  @Test
  void givenNoQueryParameters_whenSearchReservations_shouldReturnTwoReservations() {
    ArrayList<ReservationDTO> result = service.searchReservations(reservationDTOSList, null, null);

    assertEquals(2, result.size());
  }

  @Test
  void givenCustomerNameIsNotNull_whenSearchReservations_shouldReturnOneReservation() {
    ArrayList<ReservationDTO> result =
        service.searchReservations(reservationDTOSList, null, "Henry");

    assertEquals(1, result.size());
  }

  @Test
  void givenCustomerNameIsNotNull_whenSearchReservations_reservationIdShouldMatch() {
    ArrayList<ReservationDTO> result =
        service.searchReservations(reservationDTOSList, null, "Henry");

    assertEquals("1", result.get(0).getReservationId());
  }

  @Test
  void givenDateIsNotNull_whenSearchReservations_shouldReturnOneReservation() {
    ArrayList<ReservationDTO> result =
        service.searchReservations(reservationDTOSList, "2024-01-02", null);

    assertEquals(1, result.size());
  }

  @Test
  void givenDateIsNotNull_whenSearchReservations_reservationIdShouldMatch() {
    ArrayList<ReservationDTO> result =
        service.searchReservations(reservationDTOSList, "2024-01-02", null);

    assertEquals("1", result.get(0).getReservationId());
  }

  @Test
  void givenDateAndCustomerNameAreNotNull_whenSearchReservations_shouldReturnOneReservation() {
    ArrayList<ReservationDTO> result =
        service.searchReservations(reservationDTOSList, "2024-01-02", "Henry");

    assertEquals(1, result.size());
  }

  @Test
  void givenDateAndCustomerNameAreNotNull_whenSearchReservations_reservationIdShouldMatch() {
    ArrayList<ReservationDTO> result =
        service.searchReservations(reservationDTOSList, "2024-01-02", "Henry");

    assertEquals("1", result.get(0).getReservationId());
  }

  @Test
  void givenDateWrongFormat_whenSearchReservations_shouldThrowException() {
    assertThrows(
        DateTimeParseException.class,
        () -> {
          service.searchReservations(reservationDTOSList, "2024", null);
        });
  }

  @Test
  void givenValidInformation_whenSearchDispo_shouldReturnRightDispo() {
    Reservation reservation =
        new Reservation(
            restaurant1.getRestaurantId(),
            "1",
            LocalTime.parse("13:00:00"),
            120,
            null,
            50,
            LocalDate.parse("2024-03-28"));
    reservationRepository.save(reservation);
    ArrayList<DispoDTO> dispoList =
        service.searchDispo(restaurant1.getRestaurantId(), reservation.getDate().toString());

    assertEquals(40, dispoList.size());

    assertEquals(
        restaurant1.getCapacity().intValue() - reservation.getGroupSize(), dispoList.get(0).getRemainingPlaces().intValue());
    assertEquals(LocalDateTime.parse("2024-03-28T12:15"), dispoList.get(0).getStart());

    assertEquals(0, dispoList.get(3).getRemainingPlaces().intValue());
    assertEquals(LocalDateTime.parse("2024-03-28T13:00"), dispoList.get(3).getStart());

    assertEquals(50, dispoList.get(35).getRemainingPlaces().intValue());
    assertEquals(LocalDateTime.parse("2024-03-28T21:00"), dispoList.get(35).getStart());
  }

  @Test
  void givenInvalidRestaurandId_whenSearchDispo_shouldThrowException() {
    assertThrows(InvalidParameterException.class, () -> service.searchDispo("9999", "2024:03:28"));
  }

  @Test
  void givenInvalidDate_whenSearchDispo_shouldThrowException() {
    assertThrows(InvalidParameterException.class, () -> service.searchDispo("1", "invalidDate"));
  }

  @Test
  void givenValidArguments_whenGenerateInitialDispoArray_shouldReturnsValidArray() {
    final Integer openHour = 15;
    final Integer closeHour = 45;
    final Integer interval = 15;

    ArrayList<DispoDTO> result =
        service.generateInitialDispoArray(openHour, closeHour, LocalDate.now(), 2, interval);
    assertEquals(LocalDate.now().atTime(0, openHour), result.get(0).getStart());
    assertEquals(LocalDate.now().atTime(0, openHour + interval), result.get(1).getStart());
    assertEquals(LocalDate.now().atTime(0, openHour + interval * 2), result.get(2).getStart());
    assertEquals(3, result.size());
  }

  @Test
  void givenRoundParameters_whenGetDispoSchedule_shouldReturnValidPair() {
    final Integer interval = 15;
    HashMap<String, LocalTime> hours1 = new HashMap<>();
    hours1.put("open", LocalTime.parse("12:00:00"));
    hours1.put("close", LocalTime.parse("23:00:00"));
    HashMap<String, Integer> reservations = new HashMap<>();

    Restaurant restaurantTest1 = new Restaurant("1", "2", "Test", 10, hours1, reservations);
    restaurantRepository.save(restaurantTest1);

    Pair<Integer, Integer> hoursInMinutes =
        service.getDispoSchedule(restaurantTest1.getRestaurantId(), interval);

    Assert.assertEquals(720, hoursInMinutes.getFirst().intValue());
    Assert.assertEquals(1320, hoursInMinutes.getSecond().intValue());
  }

  @Test
  void givenNotRoundParameters_whenGetDispoSchedule_shouldReturnValidPair() {
    final Integer interval = 15;
    HashMap<String, LocalTime> hours1 = new HashMap<>();
    hours1.put("open", LocalTime.parse("12:10:00"));
    hours1.put("close", LocalTime.parse("23:10:00"));
    HashMap<String, Integer> reservations = new HashMap<>();

    Restaurant restaurantTest1 = new Restaurant("1", "2", "Test", 10, hours1, reservations);
    restaurantRepository.save(restaurantTest1);

    Pair<Integer, Integer> hoursInMinutes =
        service.getDispoSchedule(restaurantTest1.getRestaurantId(), interval);

    Assert.assertEquals(735, hoursInMinutes.getFirst().intValue());
    Assert.assertEquals(1320, hoursInMinutes.getSecond().intValue());
  }

  @Test
  void givenManyRestaurantsExist_whenGetDispoSchedule_shouldReturnAllDispoSchedule() {
    restaurantRepository.save(restaurant1);
    restaurantRepository.save(restaurant2);

    var expected = new Pair<>(735, 1320);
    var dispo = service.getDispoSchedule(restaurant1.getRestaurantId(), 15);

    assertEquals(expected.getFirst(), dispo.getFirst());
    assertEquals(expected.getSecond(), dispo.getSecond());
  }
}
