package ca.ulaval.glo2003.itemMenu.persistence;

import ca.ulaval.glo2003.itemMenu.domain.ItemMenu;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MongoItemMenuRepository implements ItemMenuRepository {
  private final Datastore datastore;

  public MongoItemMenuRepository(Datastore datastore) {
    this.datastore = datastore;
  }

  @Override
  public ItemMenu get(String id) {
    var query = this.datastore.find(ItemMenu.class).filter(Filters.eq("_id", id));
    var foundItemMenu = Optional.ofNullable(query.iterator().tryNext());
    return foundItemMenu.get();
  }

  @Override
  public Map<String, ItemMenu> getItemMenuList() {

    Map<String, ItemMenu> itemsMenu = new HashMap<>();
    var itemMenuList = this.datastore.find(ItemMenu.class).stream().toList();

    for (ItemMenu itemMenu : itemMenuList) {
      itemsMenu.put(itemMenu.getItemMenuId(), itemMenu);
    }

    return itemsMenu;
  }

  @Override
  public void save(ItemMenu itemMenu) {
    this.datastore.save(itemMenu);
  }

  @Override
  public void delete(String id) {
    this.datastore.find(ItemMenu.class).filter(Filters.eq("_id", id)).delete();
  }

  @Override
  public void deleteItemMenuByRestaurantId(String restaurantId) {
    this.datastore.find(ItemMenu.class).filter(Filters.eq("restaurantId", restaurantId)).delete();
  }
}
