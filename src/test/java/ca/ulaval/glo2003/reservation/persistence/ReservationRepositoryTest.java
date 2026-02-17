package ca.ulaval.glo2003.reservation.persistence;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.reservation.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class ReservationRepositoryTest {
  private ReservationRepository reservationRepository;

  protected abstract ReservationRepository createPersistence();

  private Reservation firstReservation;
  private Reservation secondReservation;
  private Reservation thirdReservation;

  @BeforeEach
  public void setUp() {
    reservationRepository = createPersistence();
    firstReservation =
        new Reservation(
            "1",
            "1",
            LocalTime.of(1, 30, 0),
            60,
            new HashMap<>() {
              {
                put("name", "Gregroire");
              }
            },
            10,
            LocalDate.now());
    secondReservation =
        new Reservation(
            "2",
            "2",
            LocalTime.of(2, 30, 0),
            60,
            new HashMap<>() {
              {
                put("name", "Jacques");
              }
            },
            10,
            LocalDate.now());
    thirdReservation =
        new Reservation(
            "2",
            "3",
            LocalTime.of(3, 30, 0),
            60,
            new HashMap<>() {
              {
                put("name", "Paul");
              }
            },
            10,
            LocalDate.now());
  }

  @Test
  void givenSavedReservation_whenGetReservation_shouldReturnReservation() {
    reservationRepository.save(firstReservation);

    Reservation foundReservation = reservationRepository.get("1");

    assertEquals(firstReservation.getRestaurantId(), foundReservation.getRestaurantId());
    assertEquals(firstReservation.getReservationId(), foundReservation.getReservationId());
    assertEquals(firstReservation.getStartTime(), foundReservation.getStartTime());
    assertEquals(firstReservation.getGroupSize(), foundReservation.getGroupSize());
    assertEquals(firstReservation.getDate(), foundReservation.getDate());
    assertEquals(firstReservation.getEndTime(), foundReservation.getEndTime());
    assertEquals(firstReservation.getCustomer(), foundReservation.getCustomer());
  }

  @Test
  void whenSaveSameReservation_shouldOverrideExistingReservation() {
    reservationRepository.save(firstReservation);
    reservationRepository.save(firstReservation);

    var foundReservations = reservationRepository.getReservationsList();

    assertEquals(firstReservation.getRestaurantId(), foundReservations.get("1").getRestaurantId());
    assertEquals(
        firstReservation.getReservationId(), foundReservations.get("1").getReservationId());
    assertEquals(firstReservation.getStartTime(), foundReservations.get("1").getStartTime());
    assertEquals(firstReservation.getGroupSize(), foundReservations.get("1").getGroupSize());
    assertEquals(firstReservation.getDate(), foundReservations.get("1").getDate());
    assertEquals(firstReservation.getEndTime(), foundReservations.get("1").getEndTime());
    assertEquals(firstReservation.getCustomer(), foundReservations.get("1").getCustomer());
  }

  @Test
  void givenManyReservationsSaved_whenGetReservationsList_shouldReturnAllReservations() {
    reservationRepository.save(firstReservation);
    reservationRepository.save(secondReservation);

    var foundReservations = reservationRepository.getReservationsList();

    assertEquals(firstReservation.getRestaurantId(), foundReservations.get("1").getRestaurantId());
    assertEquals(
        firstReservation.getReservationId(), foundReservations.get("1").getReservationId());
    assertEquals(firstReservation.getStartTime(), foundReservations.get("1").getStartTime());
    assertEquals(firstReservation.getGroupSize(), foundReservations.get("1").getGroupSize());
    assertEquals(firstReservation.getDate(), foundReservations.get("1").getDate());
    assertEquals(firstReservation.getEndTime(), foundReservations.get("1").getEndTime());
    assertEquals(firstReservation.getCustomer(), foundReservations.get("1").getCustomer());

    assertEquals(secondReservation.getRestaurantId(), foundReservations.get("2").getRestaurantId());
    assertEquals(
        secondReservation.getReservationId(), foundReservations.get("2").getReservationId());
    assertEquals(secondReservation.getStartTime(), foundReservations.get("2").getStartTime());
    assertEquals(secondReservation.getGroupSize(), foundReservations.get("2").getGroupSize());
    assertEquals(secondReservation.getDate(), foundReservations.get("2").getDate());
    assertEquals(secondReservation.getEndTime(), foundReservations.get("2").getEndTime());
    assertEquals(secondReservation.getCustomer(), foundReservations.get("2").getCustomer());
  }

  @Test
  void givenManyReservation_whenGetReservation_shouldReturnReservation() {
    reservationRepository.save(firstReservation);
    reservationRepository.save(secondReservation);

    Reservation foundReservation = reservationRepository.get("1");

    assertEquals(firstReservation.getRestaurantId(), foundReservation.getRestaurantId());
    assertEquals(firstReservation.getReservationId(), foundReservation.getReservationId());
    assertEquals(firstReservation.getStartTime(), foundReservation.getStartTime());
    assertEquals(firstReservation.getGroupSize(), foundReservation.getGroupSize());
    assertEquals(firstReservation.getDate(), foundReservation.getDate());
    assertEquals(firstReservation.getEndTime(), foundReservation.getEndTime());
    assertEquals(firstReservation.getCustomer(), foundReservation.getCustomer());
  }

  @Test
  void givenRestaurantThatDoesNotExist_whenGetReservation_shouldReturnNoReservation() {
    var foundReservation = reservationRepository.getReservationByRestaurantId("3");

    assertThat(foundReservation).isEmpty();
  }

  @Test
  void givenNoReservation_whenGetReservationsList_shouldReturnEmptyList() {
    var foundReservations = reservationRepository.getReservationsList();

    assertThat(foundReservations).isEmpty();
  }

  @Test
  void givenValidReservation_whenDelete_shouldReturnListWithoutReservation() {
    reservationRepository.save(firstReservation);
    reservationRepository.save(secondReservation);

    reservationRepository.delete("1");
    var foundReservations = reservationRepository.getReservationsList();

    assertThat(foundReservations).doesNotContainKey(firstReservation);
  }

  @Test
  void givenNoReservation_whenDelete_shouldReturnEmptyList() {
    reservationRepository.delete("1");
    var foundReservations = reservationRepository.getReservationsList();

    assertThat(foundReservations).isEmpty();
  }

  @Test
  void givenValidRestaurant_whenDeleteByRestaurantId_shouldReturnListWithoutReservation() {
    reservationRepository.save(firstReservation);
    reservationRepository.save(secondReservation);

    reservationRepository.deleteReservationsByRestaurantId("1");
    var foundReservations = reservationRepository.getReservationsList();

    assertThat(foundReservations).doesNotContainKey(firstReservation);
  }

  @Test
  void
      givenRestaurantThatDoesNotExist_whenDeleteByRestaurantId_shouldReturnAllExistingReservations() {
    reservationRepository.save(firstReservation);
    reservationRepository.save(secondReservation);

    reservationRepository.deleteReservationsByRestaurantId("3");
    var foundReservations = reservationRepository.getReservationsList();

    assertEquals(firstReservation.getRestaurantId(), foundReservations.get("1").getRestaurantId());
    assertEquals(
        firstReservation.getReservationId(), foundReservations.get("1").getReservationId());
    assertEquals(firstReservation.getStartTime(), foundReservations.get("1").getStartTime());
    assertEquals(firstReservation.getGroupSize(), foundReservations.get("1").getGroupSize());
    assertEquals(firstReservation.getDate(), foundReservations.get("1").getDate());
    assertEquals(firstReservation.getEndTime(), foundReservations.get("1").getEndTime());
    assertEquals(firstReservation.getCustomer(), foundReservations.get("1").getCustomer());

    assertEquals(secondReservation.getRestaurantId(), foundReservations.get("2").getRestaurantId());
    assertEquals(
        secondReservation.getReservationId(), foundReservations.get("2").getReservationId());
    assertEquals(secondReservation.getStartTime(), foundReservations.get("2").getStartTime());
    assertEquals(secondReservation.getGroupSize(), foundReservations.get("2").getGroupSize());
    assertEquals(secondReservation.getDate(), foundReservations.get("2").getDate());
    assertEquals(secondReservation.getEndTime(), foundReservations.get("2").getEndTime());
    assertEquals(secondReservation.getCustomer(), foundReservations.get("2").getCustomer());
  }

  @Test
  void givenValidRestaurant_whenGetByRestaurantId_shouldReturnAllRestaurantReservationHaspMap() {
    reservationRepository.save(firstReservation);
    reservationRepository.save(secondReservation);
    reservationRepository.save(thirdReservation);

    var foundReservations = reservationRepository.getReservationByRestaurantId("2");

    assertEquals(secondReservation.getRestaurantId(), foundReservations.get("2").getRestaurantId());
    assertEquals(
        secondReservation.getReservationId(), foundReservations.get("2").getReservationId());
    assertEquals(secondReservation.getStartTime(), foundReservations.get("2").getStartTime());
    assertEquals(secondReservation.getGroupSize(), foundReservations.get("2").getGroupSize());
    assertEquals(secondReservation.getDate(), foundReservations.get("2").getDate());
    assertEquals(secondReservation.getEndTime(), foundReservations.get("2").getEndTime());
    assertEquals(secondReservation.getCustomer(), foundReservations.get("2").getCustomer());

    assertEquals(thirdReservation.getRestaurantId(), foundReservations.get("3").getRestaurantId());
    assertEquals(
        thirdReservation.getReservationId(), foundReservations.get("3").getReservationId());
    assertEquals(thirdReservation.getStartTime(), foundReservations.get("3").getStartTime());
    assertEquals(thirdReservation.getGroupSize(), foundReservations.get("3").getGroupSize());
    assertEquals(thirdReservation.getDate(), foundReservations.get("3").getDate());
    assertEquals(thirdReservation.getEndTime(), foundReservations.get("3").getEndTime());
    assertEquals(thirdReservation.getCustomer(), foundReservations.get("3").getCustomer());
  }

  @Test
  void givenRestaurantThatDoesNotExist_whenGetByRestaurantId_shouldReturnEmptyList() {
    reservationRepository.save(firstReservation);
    reservationRepository.save(secondReservation);
    reservationRepository.save(thirdReservation);

    var foundReservations = reservationRepository.getReservationByRestaurantId("3");

    assertThat(foundReservations).isEmpty();
  }
}
