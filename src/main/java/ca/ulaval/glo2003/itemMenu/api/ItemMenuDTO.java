package ca.ulaval.glo2003.itemMenu.api;

import ca.ulaval.glo2003.itemMenu.domain.ItemMenu;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class ItemMenuDTO {
  private final String restaurantId;
  private final String itemMenuId;
  private final String name;
  private final String description;
  private final float price;

  public ItemMenuDTO(String restaurantId, CreateItemMenuBody body) {
    this.itemMenuId = null;
    this.restaurantId = restaurantId;
    this.name = body.getName();
    this.description = body.getDescription();
    this.price = body.getPrice();
  }

  public ItemMenuDTO(ItemMenu original) {
    this.itemMenuId = original.getItemMenuId();
    this.restaurantId = original.getRestaurantId();
    this.name = original.getName();
    this.description = original.getDescription();
    this.price = original.getPrice();
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

  public JsonObject createResponseBody() {
    JsonObjectBuilder itemMenuJsonBuilder =
        Json.createObjectBuilder()
            .add("id", getItemMenuId())
            .add("name", getName())
            .add("description", getDescription())
            .add("price", Float.toString(getPrice()));
    return itemMenuJsonBuilder.build();
  }
}
