package ca.ulaval.glo2003;

import static com.google.common.truth.Truth.assertThat;

import ca.ulaval.glo2003.itemMenu.persistence.InMemoryItemMenuRepository;
import ca.ulaval.glo2003.reservation.domain.Reservation;
import ca.ulaval.glo2003.reservation.persistence.InMemoryReservationRepository;
import ca.ulaval.glo2003.restaurant.api.CreateRestaurantBody;
import ca.ulaval.glo2003.restaurant.api.RestaurantResource;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.domain.RestaurantService;
import ca.ulaval.glo2003.restaurant.domain.RestaurantValidator;
import ca.ulaval.glo2003.restaurant.persistence.InMemoryRestaurantRepository;
import ca.ulaval.glo2003.utilities.InvalidParameterExceptionMapper;
import ca.ulaval.glo2003.utilities.MissingParameterExceptionMapper;
import ca.ulaval.glo2003.utilities.NotFoundExceptionMapper;
import ca.ulaval.glo2003.utilities.RuntimeExceptionMapper;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestaurantResourceIntegrationTest extends JerseyTest {

  public static final String NAME = "La Botega";
  public static final String CAPACITY = "12";
  public static final String OWNER = "1";

  public static final String RESTAURANT = "1";

  HashMap<String, String> hours = new HashMap<>();
  HashMap<String, LocalTime> hoursLocalTime = new HashMap<>();
  HashMap<String, String> reservations = new HashMap<>();
  HashMap<String, Integer> reservationsIntDuration = new HashMap<>();
  CreateRestaurantBody createRestaurantBody;
  InMemoryRestaurantRepository restaurantRepository;
  InMemoryReservationRepository reservationRepository;
  InMemoryItemMenuRepository itemMenuRepository;
  RestaurantValidator validator;
  public static final String RESERVATION_ID = "1";
  public static final int GROUP_SIZE = 2;
  public static final int RESERVATION_DURATION = 75;
  public static final LocalTime START_TIME = LocalTime.parse("13:15:00");
  public static final LocalDate DATE = LocalDate.parse("2024-01-02");

  HashMap<String, String> customer = new HashMap<>();

  JsonObject invalidBody = null;
  JsonObject validBody = null;

  @Override
  protected Application configure() {
    restaurantRepository = new InMemoryRestaurantRepository();
    reservationRepository = new InMemoryReservationRepository();
    itemMenuRepository = new InMemoryItemMenuRepository();
    validator = new RestaurantValidator();
    return new ResourceConfig()
        .register(
            new RestaurantResource(
                new RestaurantService(
                    restaurantRepository, reservationRepository, itemMenuRepository, validator)))
        .register(new InvalidParameterExceptionMapper())
        .register(new RuntimeExceptionMapper())
        .register(new MissingParameterExceptionMapper())
        .register(new NotFoundExceptionMapper());
  }

  @BeforeEach
  void setup() {
    hours.put("open", "11:00:00");
    hours.put("close", "19:30:00");
    hoursLocalTime.put("open", LocalTime.parse("19:30:00"));
    hoursLocalTime.put("close", LocalTime.parse("19:30:00"));
    reservations.put("duration", "180");
    reservationsIntDuration.put("duration", 180);
    customer.put("name", "Henry Schrute");
    customer.put("email", "henry@test.ca");
    customer.put("phoneNumber", "1234567890");

    createRestaurantBody = new CreateRestaurantBody(NAME, CAPACITY, hours, reservations);
  }

  @Test
  public void whenCreateValidRestaurantBody_shouldReturnStatus201() {
    Response response =
        target("restaurants")
            .request()
            .header("Owner", OWNER)
            .post(Entity.entity(createRestaurantBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(201);
    assertThat(response.getHeaders().get("Location")).isNotNull();
  }

  @Test
  public void givenMissingAParameter_whenCreateRestaurantBody_shouldThrowException() {
    invalidBody = Json.createObjectBuilder().add("name", "La Botega").add("capacity", "11").build();
    Response response =
        target("restaurants")
            .request()
            .header("Owner", OWNER)
            .post(Entity.entity(invalidBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.readEntity(JsonObject.class)).containsKey("error");
  }

  @Test
  public void givenInvalidParameter_whenCreateRestaurantBody_shouldThrowException() {
    String name = null;
    createRestaurantBody = new CreateRestaurantBody(name, CAPACITY, hours, reservations);

    Response response =
        target("restaurants")
            .request()
            .header("Owner", OWNER)
            .post(Entity.entity(createRestaurantBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.readEntity(JsonObject.class)).containsKey("error");
  }

  @Test
  public void whenGetAValidRestaurant_shouldReturnStatus200() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response = target("restaurants").request().header("Owner", OWNER).get();

    assertThat(response.getStatus()).isEqualTo(200);
  }

  @Test
  public void givenMissingAParameter_whenGetRestaurants_shouldThrowException() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response = target("restaurants").request().get();

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.readEntity(JsonObject.class)).containsKey("error");
  }

  @Test
  public void whenGetAValidRestaurantList_shouldReturnStatus200() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response = target("restaurants/" + OWNER).request().header("Owner", OWNER).get();

    assertThat(response.getStatus()).isEqualTo(200);
  }

  @Test
  public void givenMissingAParameterForId_whenGetRestaurants_shouldThrowException() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response = target("restaurants/" + OWNER).request().get();

    assertThat(response.getStatus()).isEqualTo(400);
  }

  @Test
  public void givenIdNotInList_whenGetRestaurant_shouldThrowException() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            "2",
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response = target("restaurants/" + RESTAURANT).request().header("Owner", OWNER).get();

    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void whenDeleteAValidRestaurant_shouldReturnStatus204() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response =
        target("restaurants/" + RESTAURANT).request().header("Owner", OWNER).delete();

    assertThat(response.getStatus()).isEqualTo(204);
  }

  @Test
  public void givenMissingOwnerId_whenDeleteRestaurant_shouldThrowException() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response = target("restaurants/" + RESTAURANT).request().delete();

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.readEntity(JsonObject.class)).containsKey("error");
  }

  @Test
  public void givenValidArguments_whenSearchDispo_shouldReturnStatus200() {
    HashMap<String, LocalTime> validHours = new HashMap<>();
    validHours.put("open", LocalTime.parse("11:00:00"));
    validHours.put("close", LocalTime.parse("19:30:00"));

    Restaurant newResto =
        new Restaurant(
            RESTAURANT, "2", NAME, Integer.valueOf(CAPACITY), validHours, reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response =
        target("restaurants/1/availabilities").queryParam("date", "2024-03-28").request().get();
    assertThat(response.getStatus()).isEqualTo(200);
  }

  @Test
  public void givenMissingParam_whenGetDispoList_shouldThrowException() {
    HashMap<String, LocalTime> validHours = new HashMap<>();
    validHours.put("open", LocalTime.parse("11:00:00"));
    validHours.put("close", LocalTime.parse("19:30:00"));

    Restaurant newResto =
        new Restaurant(
            RESTAURANT, "2", NAME, Integer.valueOf(CAPACITY), validHours, reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response = target("restaurants/1/availabilities").request().get();
    assertThat(response.getStatus()).isEqualTo(400);
  }

  @Test
  public void givenInvalidId_whenGetDispoList_shouldThrowException() {
    HashMap<String, LocalTime> validHours = new HashMap<>();
    validHours.put("open", LocalTime.parse("11:00:00"));
    validHours.put("close", LocalTime.parse("19:30:00"));

    Restaurant newResto =
        new Restaurant(
            RESTAURANT, "2", NAME, Integer.valueOf(CAPACITY), validHours, reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response =
        target("restaurants/999999999/availabilities")
            .queryParam("date", "2024-03-28")
            .request()
            .get();
    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void givenMissingOwner_whenGetReservations_shouldThrowException() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response = target("restaurants/" + RESTAURANT + "/reservations").request().get();

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.readEntity(JsonObject.class)).containsKey("error");
  }

  @Test
  public void givenInvalidCustomerNameQueryParam_whenGetReservation_shouldThrowException() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response =
        target("restaurants/" + RESTAURANT + "/reservations")
            .queryParam("customerName", "\"")
            .request()
            .header("owner", OWNER)
            .get();

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.readEntity(JsonObject.class)).containsKey("error");
  }

  @Test
  public void givenRestaurantThatDoesNotExist_whenGetReservation_shouldThrowException() {
    Response response =
        target("restaurants/" + RESTAURANT + "/reservations")
            .request()
            .header("owner", OWNER)
            .get();

    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.readEntity(JsonObject.class)).containsKey("error");
  }

  @Test
  public void givenRestaurantOwnerThatDoesntMatchOwner_whenGetReservation_shouldThrowException() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            "2",
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Response response =
        target("restaurants/" + RESTAURANT + "/reservations")
            .request()
            .header("owner", OWNER)
            .get();

    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.readEntity(JsonObject.class)).containsKey("error");
  }

  @Test
  public void givenNoQueryParams_whenGetReservation_shouldReturnStatus200() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Reservation newReservation =
        new Reservation(
            RESTAURANT,
            RESERVATION_ID,
            START_TIME,
            RESERVATION_DURATION,
            customer,
            GROUP_SIZE,
            DATE);
    reservationRepository.save(newReservation);

    Response response =
        target("restaurants/" + RESTAURANT + "/reservations")
            .request()
            .header("owner", OWNER)
            .get();

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(ArrayList.class).size()).isEqualTo(1);
  }

  @Test
  public void givenValidDateQueryParam_whenGetReservation_shouldReturnStatus200() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Reservation newReservation =
        new Reservation(
            RESTAURANT,
            RESERVATION_ID,
            START_TIME,
            RESERVATION_DURATION,
            customer,
            GROUP_SIZE,
            DATE);
    reservationRepository.save(newReservation);

    Reservation newReservation2 =
        new Reservation(
            RESTAURANT,
            "2",
            START_TIME,
            RESERVATION_DURATION,
            customer,
            GROUP_SIZE,
            LocalDate.parse("2024-01-03"));
    reservationRepository.save(newReservation2);

    Response response =
        target("restaurants/" + RESTAURANT + "/reservations")
            .queryParam("date", "2024-01-02")
            .request()
            .header("owner", OWNER)
            .get();

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(ArrayList.class).size()).isEqualTo(1);
  }

  @Test
  public void givenValidCustomerNameQueryParam_whenGetReservation_shouldReturnStatus200() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Reservation newReservation =
        new Reservation(
            RESTAURANT,
            RESERVATION_ID,
            START_TIME,
            RESERVATION_DURATION,
            customer,
            GROUP_SIZE,
            DATE);
    reservationRepository.save(newReservation);

    Response response =
        target("restaurants/" + RESTAURANT + "/reservations")
            .queryParam("customerName", "Henry Schrute")
            .request()
            .header("owner", OWNER)
            .get();

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(ArrayList.class).size()).isEqualTo(1);
  }

  @Test
  public void givenValidCustomerNameAndDateQueryParam_whenGetReservation_shouldReturnStatus200() {
    Restaurant newResto =
        new Restaurant(
            RESTAURANT,
            OWNER,
            NAME,
            Integer.valueOf(CAPACITY),
            hoursLocalTime,
            reservationsIntDuration);
    restaurantRepository.save(newResto);

    Reservation newReservation =
        new Reservation(
            RESTAURANT,
            RESERVATION_ID,
            START_TIME,
            RESERVATION_DURATION,
            customer,
            GROUP_SIZE,
            DATE);
    reservationRepository.save(newReservation);

    Reservation newReservation2 =
        new Reservation(
            RESTAURANT,
            "2",
            START_TIME,
            RESERVATION_DURATION,
            customer,
            GROUP_SIZE,
            LocalDate.parse("2024-01-03"));
    reservationRepository.save(newReservation2);

    Response response =
        target("restaurants/" + RESTAURANT + "/reservations")
            .queryParam("date", "2024-01-02")
            .queryParam("customerName", "Henry Schrute")
            .request()
            .header("owner", OWNER)
            .get();

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(ArrayList.class).size()).isEqualTo(1);
  }
}
