package ca.ulaval.glo2003.itemMenu.persistence;

import ca.ulaval.glo2003.itemMenu.domain.ItemMenu;
import java.util.Map;

public interface ItemMenuRepository {
  ItemMenu get(String id);

  Map<String, ItemMenu> getItemMenuList();

  void save(ItemMenu itemMenu);

  void delete(String id);

  void deleteItemMenuByRestaurantId(String restaurantId);
}
