package ca.ulaval.glo2003.itemMenu.domain;

import ca.ulaval.glo2003.itemMenu.api.ItemMenuDTO;

public class ItemMenuFactory {
  public ItemMenu createItemMenu(String itemMenuId, ItemMenuDTO itemMenu) {
    return new ItemMenu(
        itemMenuId,
        itemMenu.getRestaurantId(),
        itemMenu.getName(),
        itemMenu.getDescription(),
        itemMenu.getPrice());
  }
}
