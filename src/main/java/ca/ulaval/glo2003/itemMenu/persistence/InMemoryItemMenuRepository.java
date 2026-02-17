package ca.ulaval.glo2003.itemMenu.persistence;

import ca.ulaval.glo2003.itemMenu.domain.ItemMenu;
import java.util.HashMap;
import java.util.Map;

public class InMemoryItemMenuRepository implements ItemMenuRepository {
  private final Map<String, ItemMenu> itemMenuList;

  public InMemoryItemMenuRepository() {
    itemMenuList = new HashMap<>();
  }

  @Override
  public ItemMenu get(String itemMenuId) {
    return itemMenuList.get(itemMenuId);
  }

  @Override
  public Map<String, ItemMenu> getItemMenuList() {
    return itemMenuList;
  }

  @Override
  public void save(ItemMenu itemMenu) {
    itemMenuList.put(itemMenu.getItemMenuId(), itemMenu);
  }

  @Override
  public void delete(String itemMenuId) {
    itemMenuList.remove(itemMenuId);
  }

  @Override
  public void deleteItemMenuByRestaurantId(String restaurantId) {
    itemMenuList
        .entrySet()
        .removeIf(entry -> entry.getValue().getRestaurantId().equals(restaurantId));
  }
}
