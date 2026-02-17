package ca.ulaval.glo2003.itemMenu.domain;

import ca.ulaval.glo2003.itemMenu.api.ItemMenuDTO;
import ca.ulaval.glo2003.itemMenu.persistence.ItemMenuRepository;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.persistence.RestaurantRepository;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.util.ArrayList;
import java.util.Map;

public class ItemMenuService {
  private final ItemMenuRepository itemMenuRepository;

  private final RestaurantRepository restaurantRepository;

  private final ItemMenuValidator itemMenuValidator;

  private static Integer itemMenuId = 0;

  private final ItemMenuFactory factory;

  public ItemMenuService(
      ItemMenuRepository itemMenuRepository,
      RestaurantRepository restaurantRepository,
      ItemMenuValidator itemMenuValidator) {

    this.itemMenuRepository = itemMenuRepository;
    this.restaurantRepository = restaurantRepository;
    this.itemMenuValidator = itemMenuValidator;

    factory = new ItemMenuFactory();
  }

  public String addItemMenu(String ownerId, ItemMenuDTO itemMenu) {

    Map<String, Restaurant> restaurantsList = restaurantRepository.getRestaurantsList();

    itemMenuValidator.validateRestaurantId(itemMenu.getRestaurantId(), restaurantsList);
    itemMenuValidator.validateOwnerId(
        restaurantRepository.get(itemMenu.getRestaurantId()), ownerId);
    itemMenuValidator.validateItemMenuValues(itemMenu);

    itemMenuRepository.save(factory.createItemMenu(String.valueOf(++itemMenuId), itemMenu));

    return ("http://localhost:8080/itemMenus/" + itemMenuId);
  }

  public ArrayList<ItemMenuDTO> getMenu(String restaurantId) {

    Map<String, Restaurant> restaurantsList = restaurantRepository.getRestaurantsList();
    itemMenuValidator.validateRestaurantId(restaurantId, restaurantsList);
    Map<String, ItemMenu> itemMenuList = itemMenuRepository.getItemMenuList();

    ArrayList<ItemMenuDTO> itemMenuDTOList = new ArrayList<>();
    for (ItemMenu entry : itemMenuList.values()) {
      if (entry.getRestaurantId().equals(restaurantId)) {
        itemMenuDTOList.add(new ItemMenuDTO(entry));
      }
    }

    return itemMenuDTOList;
  }

  public void deleteItemMenu(String ownerId, String restaurantId, String itemMenuId) {

    itemMenuValidator.validateRestaurantId(restaurantId, restaurantRepository.getRestaurantsList());
    itemMenuValidator.validateOwnerId(restaurantRepository.get(restaurantId), ownerId);
    itemMenuValidator.validateItemMenuId(itemMenuId, itemMenuRepository.getItemMenuList());

    if (!itemMenuRepository.get(itemMenuId).getRestaurantId().equals(restaurantId)) {
      throw new NotFoundException("L'item n'appartient pas à ce restaurant");
    }

    itemMenuRepository.delete(itemMenuId);
  }
}
