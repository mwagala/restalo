package ca.ulaval.glo2003.restaurant.persistence;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mongodb.client.MongoClients;
import dev.morphia.Morphia;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class MongoRestaurantRepositoryTest extends RestaurantRepositoryTest {
  @Container private final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

  @Override
  protected RestaurantRepository createPersistence() {
    System.out.println(mongoDBContainer.getConnectionString());
    var mongoUrl = MongoClients.create(mongoDBContainer.getConnectionString());
    var dataStore = Morphia.createDatastore(mongoUrl, "test");
    return new MongoRestaurantRepository(dataStore);
  }

  @Test
  void givenRestaurantThatDoesNotExist_whenGetRestaurant_shouldNotReturnRestaurant() {
    System.out.println(mongoDBContainer.getConnectionString());
    var mongoUrl = MongoClients.create(mongoDBContainer.getConnectionString());
    var dataStore = Morphia.createDatastore(mongoUrl, "test");

    MongoRestaurantRepository restaurantRepository = new MongoRestaurantRepository(dataStore);

    assertThatThrownBy(() -> restaurantRepository.get("3"))
        .isInstanceOf(NoSuchElementException.class);
  }
}
