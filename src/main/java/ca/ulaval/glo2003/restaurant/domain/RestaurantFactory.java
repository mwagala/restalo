package ca.ulaval.glo2003.restaurant.domain;

import ca.ulaval.glo2003.restaurant.api.RestaurantDTO;

public class RestaurantFactory {
  public Restaurant createRestaurant(String restaurantId, RestaurantDTO restaurant) {
    return new Restaurant(
        restaurantId,
        restaurant.getOwnerId(),
        restaurant.getName(),
        restaurant.getCapacity(),
        restaurant.getHours(),
        restaurant.getReservations());
  }
}
