package ca.ulaval.glo2003.itemMenu.domain;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.itemMenu.api.ItemMenuDTO;
import ca.ulaval.glo2003.itemMenu.persistence.InMemoryItemMenuRepository;
import ca.ulaval.glo2003.itemMenu.persistence.ItemMenuRepository;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.persistence.InMemoryRestaurantRepository;
import ca.ulaval.glo2003.restaurant.persistence.RestaurantRepository;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemMenuServiceTest {
  ItemMenuDTO itemMenuDTO;
  ItemMenuService service;
  ItemMenuRepository itemMenuRepository;
  RestaurantRepository restaurantRepository;
  ItemMenuValidator validator;
  Restaurant restaurant;
  ItemMenuFactory factory;
  final String INVALID_RESTAURANT_ID = "999";
  final String INVALID_OWNER_ID = "Not Pablo";

  @BeforeEach
  public void setup() {
    itemMenuDTO = new ItemMenuDTO(new ItemMenu("1", "1", "Pasta", "Tomato pasta", 4.99f));
    itemMenuRepository = new InMemoryItemMenuRepository();
    restaurantRepository = new InMemoryRestaurantRepository();
    validator = new ItemMenuValidator();
    service = new ItemMenuService(itemMenuRepository, restaurantRepository, validator);
    factory = new ItemMenuFactory();
    HashMap<String, LocalTime> hours = new HashMap<>();
    hours.put("open", LocalTime.parse("10:00:00"));
    hours.put("close", LocalTime.parse("20:00:00"));
    HashMap<String, Integer> reservations = new HashMap<>();
    restaurant = new Restaurant("1", "Pablo", "Test", 10, hours, reservations);
  }

  @Test
  public void givenValidArguments_whenAddItemMenu_shouldAddItemMenu() {
    restaurantRepository.save(restaurant);
    service.addItemMenu(restaurant.getOwnerId(), itemMenuDTO);

    assertEquals(1, itemMenuRepository.getItemMenuList().size());
  }

  @Test
  public void givenInvalidPrice_whenAddItemMenu_shouldThrowException() {
    restaurantRepository.save(restaurant);
    ItemMenuDTO invalidItemMenu =
        new ItemMenuDTO(new ItemMenu("1", "1", "Pasta", "Tomato pasta", -4.99f));

    assertThrows(
        InvalidParameterException.class,
        () -> service.addItemMenu(restaurant.getOwnerId(), invalidItemMenu));
  }

  @Test
  public void givenRestaurantThatDoesNotExist_whenAddItemMenu_shouldThrowException() {
    assertThrows(
        NotFoundException.class, () -> service.addItemMenu(restaurant.getOwnerId(), itemMenuDTO));
  }

  @Test
  public void givenValidArguments_whenGetMenu_shouldReturnValidMenu() {
    restaurantRepository.save(restaurant);
    itemMenuRepository.save(factory.createItemMenu(itemMenuDTO.getItemMenuId(), itemMenuDTO));
    ArrayList<ItemMenuDTO> menu = service.getMenu(restaurant.getRestaurantId());

    assertEquals(1, menu.size());
    assertEquals(itemMenuDTO.getItemMenuId(), menu.get(0).getItemMenuId());
    assertEquals(itemMenuDTO.getName(), menu.get(0).getName());
    assertEquals(itemMenuDTO.getDescription(), menu.get(0).getDescription());
    assertEquals(itemMenuDTO.getPrice(), menu.get(0).getPrice());
  }

  @Test
  public void givenInvalidRestaurantId_whenGetMenu_shouldThrowException() {
    restaurantRepository.save(restaurant);

    assertThrows(NotFoundException.class, () -> service.getMenu(INVALID_RESTAURANT_ID));
  }

  @Test
  public void givenValidArguments_whenDeleteItemMenu_shouldDeleteItemMenu() {
    restaurantRepository.save(restaurant);
    itemMenuRepository.save(factory.createItemMenu(itemMenuDTO.getItemMenuId(), itemMenuDTO));
    service.deleteItemMenu(
        restaurant.getOwnerId(), itemMenuDTO.getRestaurantId(), itemMenuDTO.getItemMenuId());

    assertEquals(0, itemMenuRepository.getItemMenuList().size());
  }

  @Test
  public void givenInvalidOwner_whenDeleteItemMenu_shouldThrowException() {
    restaurantRepository.save(restaurant);
    itemMenuRepository.save(factory.createItemMenu(itemMenuDTO.getItemMenuId(), itemMenuDTO));

    assertThrows(
        NotFoundException.class,
        () ->
            service.deleteItemMenu(
                INVALID_OWNER_ID, itemMenuDTO.getRestaurantId(), itemMenuDTO.getItemMenuId()));
    assertEquals(1, itemMenuRepository.getItemMenuList().size());
  }

  @Test
  public void givenInvalidRestaurantId_whenDeleteItemMenu_shouldThrowException() {
    restaurantRepository.save(restaurant);
    itemMenuRepository.save(factory.createItemMenu(itemMenuDTO.getItemMenuId(), itemMenuDTO));

    assertThrows(
        NotFoundException.class,
        () ->
            service.deleteItemMenu(
                restaurant.getOwnerId(), INVALID_RESTAURANT_ID, itemMenuDTO.getItemMenuId()));
    assertEquals(1, itemMenuRepository.getItemMenuList().size());
  }

  @Test
  public void givenInvalidItemMenu_whenDeleteItemMenu_shouldThrowException() {
    restaurantRepository.save(restaurant);
    itemMenuRepository.save(factory.createItemMenu(itemMenuDTO.getItemMenuId(), itemMenuDTO));

    assertThrows(
        NotFoundException.class,
        () ->
            service.deleteItemMenu(
                restaurant.getOwnerId(),
                itemMenuDTO.getRestaurantId(),
                itemMenuDTO.getItemMenuId() + "INVALID"));
    assertEquals(1, itemMenuRepository.getItemMenuList().size());
  }
}
