package ca.ulaval.glo2003.itemMenu.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity
public class ItemMenu {
  @Id private String restaurantId;
  private String itemMenuId;
  private String name;
  private String description;
  private float price;

  public ItemMenu() {}

  public ItemMenu(String id, String restaurantId, String name, String description, float price) {
    this.itemMenuId = id;
    this.restaurantId = restaurantId;
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public String getItemMenuId() {
    return itemMenuId;
  }

  public String getRestaurantId() {
    return restaurantId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public float getPrice() {
    return price;
  }
}
