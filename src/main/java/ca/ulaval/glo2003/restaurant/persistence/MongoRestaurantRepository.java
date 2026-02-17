package ca.ulaval.glo2003.restaurant.persistence;

import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.domain.RestaurantSearch;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MongoRestaurantRepository implements RestaurantRepository {
  private final Datastore datastore;

  public MongoRestaurantRepository(Datastore datastore) {
    this.datastore = datastore;
  }

  public Restaurant get(String id) {

    var query = this.datastore.find(Restaurant.class).filter(Filters.eq("_id", id));
    var foundRestaurant = Optional.ofNullable(query.iterator().tryNext());

    return foundRestaurant.get();
  }

  public void save(Restaurant restaurant) {
    this.datastore.save(restaurant);
  }

  public Map<String, Restaurant> getRestaurantsList() {

    Map<String, Restaurant> restaurants = new HashMap<>();
    var restaurantList = this.datastore.find(Restaurant.class).stream().toList();

    for (Restaurant restaurant : restaurantList) {
      restaurants.put(restaurant.getRestaurantId(), restaurant);
    }

    return restaurants;
  }

  public ArrayList<Restaurant> getOwnerRestaurantsList(String ownerId) {

    ArrayList<Restaurant> ownerRestaurants = new ArrayList<>();

    for (Restaurant restaurant : getRestaurantsList().values()) {
      if (restaurant.getOwnerId().equals(ownerId)) {
        ownerRestaurants.add(restaurant);
      }
    }

    return ownerRestaurants;
  }

  public ArrayList<Restaurant> getSearchedRestaurants(RestaurantSearch restaurantSearch) {

    HashMap<String, LocalTime> openedHours = new HashMap<>();

    if (restaurantSearch != null) {

      try {
        openedHours.put("from", LocalTime.parse(restaurantSearch.getOpened().get("from")));
      } catch (NullPointerException ignored) {
      }

      try {
        openedHours.put("to", LocalTime.parse(restaurantSearch.getOpened().get("to")));
      } catch (NullPointerException ignored) {
      }
    }

    return searchedRestaurants(restaurantSearch, openedHours);
  }

  public ArrayList<Restaurant> searchedRestaurants(
      RestaurantSearch restaurantSearch, HashMap<String, LocalTime> openedHours) {

    ArrayList<Restaurant> searchedRestaurantsList = new ArrayList<>();

    for (Restaurant entry : getRestaurantsList().values()) {
      if (restaurantSearch != null
          && !(filterRestaurantName(restaurantSearch, entry.getName())
              || !openedHours.isEmpty() && !filterRestaurantHours(entry.getHours(), openedHours))) {
        searchedRestaurantsList.add(entry);
      }
    }

    return searchedRestaurantsList;
  }

  private boolean filterRestaurantName(RestaurantSearch restaurantSearch, String restaurantName) {

    return restaurantSearch.getName() != null
        && !restaurantName
            .toLowerCase()
            .replaceAll("\\s", "")
            .contains(restaurantSearch.getName().toLowerCase().replaceAll("\\s", ""));
  }

  private boolean filterRestaurantHours(
      HashMap<String, LocalTime> restaurantHours, HashMap<String, LocalTime> openedHours) {

    LocalTime filterFrom = openedHours.get("from");
    LocalTime filterTo = openedHours.get("to");

    if (filterFrom != null) {
      if (!(!restaurantHours.get("open").isAfter(filterFrom)
          && restaurantHours.get("close").isAfter(filterFrom))) {
        return false;
      }
    }

    if (filterTo != null) {
      if (!(restaurantHours.get("open").isBefore(filterTo)
          && !restaurantHours.get("close").isBefore(filterTo))) {
        return false;
      }
    }

    return true;
  }

  public void delete(String id) {
    this.datastore.find(Restaurant.class).filter(Filters.eq("_id", id)).delete();
  }
}
