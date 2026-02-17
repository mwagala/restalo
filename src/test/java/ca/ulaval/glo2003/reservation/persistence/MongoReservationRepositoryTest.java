package ca.ulaval.glo2003.reservation.persistence;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.ulaval.glo2003.reservation.domain.Reservation;
import com.mongodb.client.MongoClients;
import dev.morphia.Morphia;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class MongoReservationRepositoryTest extends ReservationRepositoryTest {
  @Container private final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

  @Override
  protected ReservationRepository createPersistence() {
    System.out.println(mongoDBContainer.getConnectionString());
    var mongoUrl = MongoClients.create(mongoDBContainer.getConnectionString());
    var dataStore = Morphia.createDatastore(mongoUrl, "test");
    return new MongoReservationRepository(dataStore);
  }

  @Test
  void givenReservationNotExist_whenGetReservation_shouldReturnNoReservation() {
    System.out.println(mongoDBContainer.getConnectionString());
    var mongoUrl = MongoClients.create(mongoDBContainer.getConnectionString());
    var dataStore = Morphia.createDatastore(mongoUrl, "test");
    MongoReservationRepository reservationRepository = new MongoReservationRepository(dataStore);

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

    assertThatThrownBy(() -> reservationRepository.get("3"))
        .isInstanceOf(NoSuchElementException.class);
  }
}
