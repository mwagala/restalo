package ca.ulaval.glo2003.restaurant.persistence;

import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.domain.RestaurantSearch;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface RestaurantRepository {
  Restaurant get(String id);

  void save(Restaurant restaurant);

  Map<String, Restaurant> getRestaurantsList();

  ArrayList<Restaurant> getOwnerRestaurantsList(String ownerId);

  ArrayList<Restaurant> getSearchedRestaurants(RestaurantSearch restaurantSearch);

  ArrayList<Restaurant> searchedRestaurants(
      RestaurantSearch restaurantSearch, HashMap<String, LocalTime> openedHours);

  void delete(String id);
}
