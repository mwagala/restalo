package ca.ulaval.glo2003.restaurant.persistence;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.domain.RestaurantSearch;
import java.time.LocalTime;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class RestaurantRepositoryTest {
  private RestaurantRepository restaurantRepository;

  protected abstract RestaurantRepository createPersistence();

  private Restaurant firstRestaurant;
  private Restaurant secondRestaurant;
  private Restaurant thirdRestaurant;

  @BeforeEach
  public void setUp() {
    restaurantRepository = createPersistence();
    firstRestaurant =
        new Restaurant(
            "1",
            "1",
            "La Botega",
            100,
            new HashMap<>() {
              {
                put("open", LocalTime.parse("10:00"));
                put("close", LocalTime.parse("22:00"));
              }
            },
            new HashMap<>() {
              {
                put("1", 1);
              }
            });
    secondRestaurant =
        new Restaurant(
            "2",
            "2",
            "La Montreala",
            100,
            new HashMap<>() {
              {
                put("open", LocalTime.parse("10:00"));
                put("close", LocalTime.parse("22:00"));
              }
            },
            new HashMap<>() {
              {
                put("1", 2);
              }
            });
    thirdRestaurant =
        new Restaurant(
            "3",
            "1",
            "La Abidjana",
            100,
            new HashMap<>() {
              {
                put("open", LocalTime.parse("10:00"));
                put("close", LocalTime.parse("22:00"));
              }
            },
            new HashMap<>() {
              {
                put("1", 1);
              }
            });
  }

  @Test
  void givenSavedRestaurant_whenGet_shouldReturnRestaurant() {
    restaurantRepository.save(firstRestaurant);

    Restaurant foundRestaurant = restaurantRepository.get("1");

    assertEquals(firstRestaurant.getRestaurantId(), foundRestaurant.getRestaurantId());
    assertEquals(firstRestaurant.getOwnerId(), foundRestaurant.getOwnerId());
    assertEquals(firstRestaurant.getName(), foundRestaurant.getName());
    assertEquals(firstRestaurant.getCapacity(), foundRestaurant.getCapacity());
    assertEquals(firstRestaurant.getHours(), foundRestaurant.getHours());
    assertEquals(firstRestaurant.getReservations(), foundRestaurant.getReservations());
  }

  @Test
  void givenSameRestaurant_whenGet_shouldOverrideExistingRestaurant() {
    restaurantRepository.save(firstRestaurant);
    restaurantRepository.save(firstRestaurant);

    Restaurant foundRestaurant = restaurantRepository.get("1");

    assertEquals(firstRestaurant.getRestaurantId(), foundRestaurant.getRestaurantId());
    assertEquals(firstRestaurant.getOwnerId(), foundRestaurant.getOwnerId());
    assertEquals(firstRestaurant.getName(), foundRestaurant.getName());
    assertEquals(firstRestaurant.getCapacity(), foundRestaurant.getCapacity());
    assertEquals(firstRestaurant.getHours(), foundRestaurant.getHours());
    assertEquals(firstRestaurant.getReservations(), foundRestaurant.getReservations());
  }

  @Test
  void givenManyRestaurants_whenGetRestaurantsList_shouldReturnAllRestaurants() {
    restaurantRepository.save(firstRestaurant);
    restaurantRepository.save(secondRestaurant);

    var foundRestaurant = restaurantRepository.getRestaurantsList();

    assertEquals(firstRestaurant.getRestaurantId(), foundRestaurant.get("1").getRestaurantId());
    assertEquals(firstRestaurant.getOwnerId(), foundRestaurant.get("1").getOwnerId());
    assertEquals(firstRestaurant.getName(), foundRestaurant.get("1").getName());
    assertEquals(firstRestaurant.getCapacity(), foundRestaurant.get("1").getCapacity());
    assertEquals(firstRestaurant.getHours(), foundRestaurant.get("1").getHours());
    assertEquals(firstRestaurant.getReservations(), foundRestaurant.get("1").getReservations());

    assertEquals(secondRestaurant.getRestaurantId(), foundRestaurant.get("2").getRestaurantId());
    assertEquals(secondRestaurant.getOwnerId(), foundRestaurant.get("2").getOwnerId());
    assertEquals(secondRestaurant.getName(), foundRestaurant.get("2").getName());
    assertEquals(secondRestaurant.getCapacity(), foundRestaurant.get("2").getCapacity());
    assertEquals(secondRestaurant.getHours(), foundRestaurant.get("2").getHours());
    assertEquals(secondRestaurant.getReservations(), foundRestaurant.get("2").getReservations());
  }

  @Test
  void givenNoRestaurant_whenGetRestaurantsList_shouldReturnEmptyList() {
    var foundRestaurant = restaurantRepository.getRestaurantsList();

    assertThat(foundRestaurant).isEmpty();
  }

  @Test
  void givenRestaurant_whenDelete_shouldReturnListWithoutRestaurant() {
    restaurantRepository.save(firstRestaurant);
    restaurantRepository.save(secondRestaurant);

    restaurantRepository.delete("1");
    var foundRestaurants = restaurantRepository.getRestaurantsList();

    assertThat(foundRestaurants).doesNotContainKey(firstRestaurant);
  }

  @Test
  void givenNoRestaurants_whenDelete_shouldReturnEmptyList() {
    restaurantRepository.delete("1");

    var foundRestaurants = restaurantRepository.getRestaurantsList();

    assertThat(foundRestaurants).isEmpty();
  }

  @Test
  void givenManyRestaurants_whenGetOwnerRestaurant_shouldReturnAllOwnerRestaurants() {
    restaurantRepository.save(firstRestaurant);
    restaurantRepository.save(thirdRestaurant);

    var foundRestaurant = restaurantRepository.getOwnerRestaurantsList("1");

    assertEquals(firstRestaurant.getRestaurantId(), foundRestaurant.get(0).getRestaurantId());
    assertEquals(firstRestaurant.getOwnerId(), foundRestaurant.get(0).getOwnerId());
    assertEquals(firstRestaurant.getName(), foundRestaurant.get(0).getName());
    assertEquals(firstRestaurant.getCapacity(), foundRestaurant.get(0).getCapacity());
    assertEquals(firstRestaurant.getHours(), foundRestaurant.get(0).getHours());
    assertEquals(firstRestaurant.getReservations(), foundRestaurant.get(0).getReservations());

    assertEquals(thirdRestaurant.getRestaurantId(), foundRestaurant.get(1).getRestaurantId());
    assertEquals(thirdRestaurant.getOwnerId(), foundRestaurant.get(1).getOwnerId());
    assertEquals(thirdRestaurant.getName(), foundRestaurant.get(1).getName());
    assertEquals(thirdRestaurant.getCapacity(), foundRestaurant.get(1).getCapacity());
    assertEquals(thirdRestaurant.getHours(), foundRestaurant.get(1).getHours());
    assertEquals(thirdRestaurant.getReservations(), foundRestaurant.get(1).getReservations());
  }

  @Test
  void givenNoRestaurants_whenGetOwnerRestaurant_shouldReturnNull() {
    var foundRestaurant = restaurantRepository.getOwnerRestaurantsList("3");

    assertThat(foundRestaurant).isEmpty();
  }

  @Test
  void givenManyRestaurants_whenGetSearchedRestaurants_shouldReturnAllSearchedRestaurants() {
    restaurantRepository.save(firstRestaurant);
    restaurantRepository.save(thirdRestaurant);

    var foundRestaurant =
        restaurantRepository.getSearchedRestaurants(
            new RestaurantSearch(
                firstRestaurant.getName(),
                new HashMap<>() {
                  {
                    put("from", "10:00");
                    put("to", "22:00");
                  }
                }));

    assertEquals(firstRestaurant.getRestaurantId(), foundRestaurant.get(0).getRestaurantId());
    assertEquals(firstRestaurant.getOwnerId(), foundRestaurant.get(0).getOwnerId());
    assertEquals(firstRestaurant.getName(), foundRestaurant.get(0).getName());
    assertEquals(firstRestaurant.getCapacity(), foundRestaurant.get(0).getCapacity());
    assertEquals(firstRestaurant.getHours(), foundRestaurant.get(0).getHours());
    assertEquals(firstRestaurant.getReservations(), foundRestaurant.get(0).getReservations());
  }

  @Test
  void givenNoRestaurants_whenGetSearchedRestaurants_shouldReturnNull() {
    var foundRestaurant =
        restaurantRepository.getSearchedRestaurants(
            new RestaurantSearch(
                firstRestaurant.getName(),
                new HashMap<>() {
                  {
                    put("from", "10:00");
                    put("to", "22:00");
                  }
                }));

    assertThat(foundRestaurant).isEmpty();
  }

  @Test
  void givenManyRestaurants_whenSearchedRestaurants_shouldReturnAllSearchedRestaurants() {
    restaurantRepository.save(firstRestaurant);
    restaurantRepository.save(thirdRestaurant);

    var foundRestaurant =
        restaurantRepository.searchedRestaurants(
            new RestaurantSearch(
                firstRestaurant.getName(),
                new HashMap<>() {
                  {
                    put("from", "10:00");
                    put("to", "22:00");
                  }
                }),
            new HashMap<>() {
              {
                put("from", LocalTime.parse("10:00"));
                put("to", LocalTime.parse("22:00"));
              }
            });

    assertEquals(firstRestaurant.getRestaurantId(), foundRestaurant.get(0).getRestaurantId());
    assertEquals(firstRestaurant.getOwnerId(), foundRestaurant.get(0).getOwnerId());
    assertEquals(firstRestaurant.getName(), foundRestaurant.get(0).getName());
    assertEquals(firstRestaurant.getCapacity(), foundRestaurant.get(0).getCapacity());
    assertEquals(firstRestaurant.getHours(), foundRestaurant.get(0).getHours());
    assertEquals(firstRestaurant.getReservations(), foundRestaurant.get(0).getReservations());
  }

  @Test
  void givenNoRestaurants_whenSearchedRestaurants_shouldReturnNull() {
    var foundRestaurant =
        restaurantRepository.searchedRestaurants(
            new RestaurantSearch(
                firstRestaurant.getName(),
                new HashMap<>() {
                  {
                    put("from", "10:00");
                    put("to", "22:00");
                  }
                }),
            new HashMap<>() {
              {
                put("from", LocalTime.parse("10:00"));
                put("to", LocalTime.parse("22:00"));
              }
            });

    assertThat(foundRestaurant).isEmpty();
  }
}
