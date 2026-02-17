package ca.ulaval.glo2003.restaurant.persistence;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

class InMemoryRestaurantRepositoryTest extends RestaurantRepositoryTest {

  @Override
  protected RestaurantRepository createPersistence() {
    return new InMemoryRestaurantRepository();
  }

  @Test
  void givenRestaurantThatDoesNotExist_whenGetRestaurant_shouldNotReturnRestaurant() {
    InMemoryRestaurantRepository restaurantRepository = new InMemoryRestaurantRepository();
    var foundRestaurant = restaurantRepository.get("3");

    assertThat(foundRestaurant).isNull();
  }
}
