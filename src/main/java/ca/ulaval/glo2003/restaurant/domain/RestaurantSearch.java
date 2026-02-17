package ca.ulaval.glo2003.restaurant.domain;

import java.util.HashMap;

public class RestaurantSearch {
  private String name;
  private HashMap<String, String> opened;

  public RestaurantSearch() {}

  public RestaurantSearch(String name, HashMap<String, String> opened) {
    this.name = name;
    this.opened = opened;
  }

  public String getName() {
    return this.name;
  }

  public HashMap<String, String> getOpened() {
    return this.opened;
  }
}
