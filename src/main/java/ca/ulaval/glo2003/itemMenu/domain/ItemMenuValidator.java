package ca.ulaval.glo2003.itemMenu.domain;

import ca.ulaval.glo2003.itemMenu.api.ItemMenuDTO;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.util.Map;

public class ItemMenuValidator {
  public void validateItemMenuId(String itemMenuId, Map<String, ItemMenu> itemMenuList) {
    if (!itemMenuList.containsKey(itemMenuId)) {
      throw new NotFoundException("L'item n'est pas présent dans ce menu");
    }
  }

  public void validateRestaurantId(String restaurantId, Map<String, Restaurant> restaurantsList) {
    if (!restaurantsList.containsKey(restaurantId)) {
      throw new NotFoundException("Le restaurant n'existe pas");
    }
  }

  public void validateItemMenuValues(ItemMenuDTO itemMenu) {
    if (itemMenu.getPrice() < 1) throw new InvalidParameterException();
  }

  public void validateOwnerId(Restaurant restaurant, String ownerId) {
    if (!restaurant.getOwnerId().equals(ownerId)) {
      throw new NotFoundException("Le restaurant n'appartient pas au restaurateur");
    }
  }
}
