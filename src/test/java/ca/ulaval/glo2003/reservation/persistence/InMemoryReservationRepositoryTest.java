package ca.ulaval.glo2003.reservation.persistence;

import static com.google.common.truth.Truth.assertThat;

import ca.ulaval.glo2003.reservation.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class InMemoryReservationRepositoryTest extends ReservationRepositoryTest {

  @Override
  protected ReservationRepository createPersistence() {
    return new InMemoryReservationRepository();
  }

  @Test
  void givenReservationNotExist_whenGetReservation_shouldReturnNoReservation() {
    InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();
    Reservation firstReservation =
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
    Reservation secondReservation =
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

    reservationRepository.save(firstReservation);
    reservationRepository.save(secondReservation);

    var foundReservation = reservationRepository.get("3");
    assertThat(foundReservation).isNull();
  }
}
