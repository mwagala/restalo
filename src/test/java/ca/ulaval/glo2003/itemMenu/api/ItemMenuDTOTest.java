package ca.ulaval.glo2003.itemMenu.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.itemMenu.domain.ItemMenu;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.Test;

public class ItemMenuDTOTest {
  String ITEMMENU_ID = "1";
  String RESTAURANT_ID = "1";
  String NAME = "Pasta";
  String DESCRIPTION = "Tomato pasta";
  Float PRICE = 4.99f;

  @Test
  public void givenValidItemMenuDTO_whenCreateResponseBody_shouldReturnValidJsonObject() {
    ItemMenu itemMenu = mock(ItemMenu.class);
    when(itemMenu.getItemMenuId()).thenReturn(ITEMMENU_ID);
    when(itemMenu.getRestaurantId()).thenReturn(RESTAURANT_ID);
    when(itemMenu.getName()).thenReturn(NAME);
    when(itemMenu.getDescription()).thenReturn(DESCRIPTION);
    when(itemMenu.getPrice()).thenReturn(PRICE);
    ItemMenuDTO itemMenuDTO = new ItemMenuDTO(itemMenu);

    JsonObject responseBody = itemMenuDTO.createResponseBody();

    assertEquals("\"" + ITEMMENU_ID + "\"", responseBody.get("id").toString());
    assertEquals("\"" + NAME + "\"", responseBody.get("name").toString());
    assertEquals("\"" + DESCRIPTION + "\"", responseBody.get("description").toString());
    assertEquals("\"" + PRICE.toString() + "\"", responseBody.get("price").toString());
  }
}
