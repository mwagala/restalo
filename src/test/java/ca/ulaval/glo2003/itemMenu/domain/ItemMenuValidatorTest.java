package ca.ulaval.glo2003.itemMenu.domain;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.itemMenu.api.CreateItemMenuBody;
import ca.ulaval.glo2003.itemMenu.api.ItemMenuDTO;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.NotFoundException;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemMenuValidatorTest {
  ItemMenuValidator validator;
  HashMap<String, ItemMenu> itemMenuList;
  HashMap<String, Restaurant> restaurantList;
  ItemMenuDTO itemMenuDTO;
  Restaurant restaurant;

  @BeforeEach
  public void setup() {
    validator = new ItemMenuValidator();
    itemMenuList = new HashMap<>();
    restaurantList = new HashMap<>();
    itemMenuDTO = new ItemMenuDTO("1", new CreateItemMenuBody("Pasta", "Tomato pasta", "4.99"));
    restaurant = new Restaurant("1", "Pablo", "Chez Pablo", 10, new HashMap<>(), new HashMap<>());
  }

  @Test
  public void givenValidId_whenValidateItemMenuId_shouldNotThrowException() {
    itemMenuList.put(
        itemMenuDTO.getItemMenuId(),
        new ItemMenu(
            itemMenuDTO.getItemMenuId(),
            restaurant.getRestaurantId(),
            itemMenuDTO.getName(),
            itemMenuDTO.getDescription(),
            itemMenuDTO.getPrice()));

    assertDoesNotThrow(
        () -> validator.validateItemMenuId(itemMenuDTO.getItemMenuId(), itemMenuList));
  }

  @Test
  public void givenInvalidId_whenValidateItemMenuId_shouldThrowException() {
    assertThrows(
        NotFoundException.class,
        () -> validator.validateItemMenuId(itemMenuDTO.getItemMenuId(), itemMenuList));
  }

  @Test
  public void givenValidId_whenValidateRestaurantId_shouldNotThrowException() {
    restaurantList.put(restaurant.getRestaurantId(), restaurant);
    assertDoesNotThrow(
        () -> validator.validateRestaurantId(restaurant.getRestaurantId(), restaurantList));
  }

  @Test
  public void givenInvalidId_whenValidateRestaurantId_shouldThrowException() {
    assertThrows(
        NotFoundException.class,
        () -> validator.validateRestaurantId(itemMenuDTO.getItemMenuId(), restaurantList));
  }

  @Test
  public void givenValidArguments_whenValidateItemMenuValues_shouldNotThrowException() {
    assertDoesNotThrow(() -> validator.validateItemMenuValues(itemMenuDTO));
  }

  @Test
  public void givenInvalidArguments_whenValidateItemMenuValues_shouldThrowException() {
    assertThrows(
        InvalidParameterException.class,
        () ->
            validator.validateItemMenuValues(
                new ItemMenuDTO("1", new CreateItemMenuBody("Pasta", "Tomato pasta", "0.99"))));
  }

  @Test
  public void givenValidArguments_whenValidateOwnerId_shouldNotThrowException() {
    assertDoesNotThrow(() -> validator.validateOwnerId(restaurant, restaurant.getOwnerId()));
  }

  @Test
  public void givenInvalidOwnerId_whenValidateOwnerId_shouldThrowException() {
    assertThrows(NotFoundException.class, () -> validator.validateOwnerId(restaurant, "Not Pablo"));
  }
}
