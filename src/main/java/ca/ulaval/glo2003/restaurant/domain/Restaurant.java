package ca.ulaval.glo2003.restaurant.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.time.LocalTime;
import java.util.HashMap;

@Entity
public class Restaurant {
  @Id private String restaurantId;
  private String ownerId;
  private String name;
  private Integer capacity;
  private HashMap<String, LocalTime> hours;

  public HashMap<String, Integer> reservations;

  public Restaurant() {}

  public Restaurant(
      String restaurantId,
      String ownerId,
      String name,
      Integer capacity,
      HashMap<String, LocalTime> hours,
      HashMap<String, Integer> reservations) {
    this.restaurantId = restaurantId;
    this.ownerId = ownerId;
    this.name = name;
    this.capacity = capacity;
    this.hours = hours;
    this.reservations = reservations;

    if (this.reservations != null) {
      this.reservations.putIfAbsent("duration", 60);
    }
  }

  public String getRestaurantId() {
    return restaurantId;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public String getName() {
    return name;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public HashMap<String, LocalTime> getHours() {
    return hours;
  }

  public HashMap<String, Integer> getReservations() {
    return reservations;
  }
}
