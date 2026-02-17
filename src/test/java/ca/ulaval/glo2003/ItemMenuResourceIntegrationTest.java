package ca.ulaval.glo2003;

import static com.google.common.truth.Truth.assertThat;

import ca.ulaval.glo2003.itemMenu.api.CreateItemMenuBody;
import ca.ulaval.glo2003.itemMenu.api.ItemMenuResource;
import ca.ulaval.glo2003.itemMenu.domain.ItemMenu;
import ca.ulaval.glo2003.itemMenu.domain.ItemMenuService;
import ca.ulaval.glo2003.itemMenu.domain.ItemMenuValidator;
import ca.ulaval.glo2003.itemMenu.persistence.InMemoryItemMenuRepository;
import ca.ulaval.glo2003.itemMenu.persistence.ItemMenuRepository;
import ca.ulaval.glo2003.restaurant.domain.Restaurant;
import ca.ulaval.glo2003.restaurant.persistence.InMemoryRestaurantRepository;
import ca.ulaval.glo2003.restaurant.persistence.RestaurantRepository;
import ca.ulaval.glo2003.utilities.InvalidParameterExceptionMapper;
import ca.ulaval.glo2003.utilities.MissingParameterExceptionMapper;
import ca.ulaval.glo2003.utilities.NotFoundExceptionMapper;
import ca.ulaval.glo2003.utilities.RuntimeExceptionMapper;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

public class ItemMenuResourceIntegrationTest extends JerseyTest {
  ItemMenuRepository itemMenuRepository;
  RestaurantRepository restaurantRepository;
  ItemMenuValidator validator;
  public static final String RESTAURANT_ID = "1";
  public static final String INVALID_RESTAURANT_ID = "999";
  public static final String RESTAURANT_NAME = "La Botega";
  public static final Integer RESTAURANT_CAPACITY = 12;
  public static final String RESTAURANT_OWNER_ID = "Pablo";
  public static final String INVALID_RESTAURANT_OWNER_ID = "Not Pablo";
  public static final String ITEMMENU_NAME = "Pasta";
  public static final String ITEMMENU_DESCRIPTION = "Tomato pasta";
  public static final float ITEMMENU_PRICE = 4.99F;

  @Override
  protected Application configure() {
    restaurantRepository = new InMemoryRestaurantRepository();
    itemMenuRepository = new InMemoryItemMenuRepository();
    validator = new ItemMenuValidator();
    Restaurant newResto =
        new Restaurant(
            RESTAURANT_ID,
            RESTAURANT_OWNER_ID,
            RESTAURANT_NAME,
            RESTAURANT_CAPACITY,
            new HashMap<>(),
            new HashMap<>());
    restaurantRepository.save(newResto);
    return new ResourceConfig()
        .register(
            new ItemMenuResource(
                new ItemMenuService(itemMenuRepository, restaurantRepository, validator)))
        .register(new InvalidParameterExceptionMapper())
        .register(new RuntimeExceptionMapper())
        .register(new MissingParameterExceptionMapper())
        .register(new NotFoundExceptionMapper());
  }

  @Test
  public void whenCreateValidItemMenu_shouldReturnStatus201() {
    CreateItemMenuBody createItemMenuBody =
        new CreateItemMenuBody(ITEMMENU_NAME, ITEMMENU_DESCRIPTION, Float.toString(ITEMMENU_PRICE));

    Response response =
        target("restaurants/" + RESTAURANT_ID + "/itemMenu")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .post(Entity.entity(createItemMenuBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(201);
    assertThat(response.getHeaders().get("Location")).isNotNull();
  }

  @Test
  public void whenCreateItemMenuWithMissingParam_shouldThrowException() {
    JsonObject invalidBody =
        Json.createObjectBuilder().add("name", "Pasta").add("price", "4.99").build();
    Response response =
        target("restaurants/" + RESTAURANT_ID + "/itemMenu")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .post(Entity.entity(invalidBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.getHeaders().get("Location")).isNull();
  }

  @Test
  public void whenCreateInvalidItemMenu_shouldThrowException() {
    CreateItemMenuBody createItemMenuBody =
        new CreateItemMenuBody("", ITEMMENU_DESCRIPTION, Float.toString(ITEMMENU_PRICE));

    Response response =
        target("restaurants/" + RESTAURANT_ID + "/itemMenu")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .post(Entity.entity(createItemMenuBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.getHeaders().get("Location")).isNull();
  }

  @Test
  public void whenCreateItemMenuWithInvalidRestaurantId_shouldThrowException() {
    CreateItemMenuBody createItemMenuBody =
        new CreateItemMenuBody(ITEMMENU_NAME, ITEMMENU_DESCRIPTION, Float.toString(ITEMMENU_PRICE));

    Response response =
        target("restaurants/" + INVALID_RESTAURANT_ID + "/itemMenu")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .post(Entity.entity(createItemMenuBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getHeaders().get("Location")).isNull();
  }

  @Test
  public void whenCreateItemMenuWithInvalidOwnerId_shouldThrowException() {
    CreateItemMenuBody createItemMenuBody =
        new CreateItemMenuBody(ITEMMENU_NAME, ITEMMENU_DESCRIPTION, Float.toString(ITEMMENU_PRICE));

    Response response =
        target("restaurants/" + RESTAURANT_ID + "/itemMenu")
            .request()
            .header("Owner", INVALID_RESTAURANT_OWNER_ID)
            .post(Entity.entity(createItemMenuBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getHeaders().get("Location")).isNull();
  }

  @Test
  public void whenCreateItemMenuWithMissingOwnerId_shouldThrowException() {
    CreateItemMenuBody createItemMenuBody =
        new CreateItemMenuBody(ITEMMENU_NAME, ITEMMENU_DESCRIPTION, Float.toString(ITEMMENU_PRICE));

    Response response =
        target("restaurants/" + RESTAURANT_ID + "/itemMenu")
            .request()
            .post(Entity.entity(createItemMenuBody, MediaType.APPLICATION_JSON));

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.getHeaders().get("Location")).isNull();
  }

  @Test
  public void whenGetMenuFromValidParameters_shouldReturnStatus200() {
    Response response =
        target("restaurants/" + RESTAURANT_ID + "/menu")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .get();

    assertThat(response.getStatus()).isEqualTo(200);
  }

  @Test
  public void whenGetMenuFromInvalidRestaurantId_shouldThrowException() {
    Response response =
        target("restaurants/" + INVALID_RESTAURANT_ID + "/menu")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .get();

    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void whenDeleteValidItemMenu_shouldReturnStatus204() {
    itemMenuRepository.save(
        new ItemMenu("1", RESTAURANT_ID, ITEMMENU_NAME, ITEMMENU_DESCRIPTION, ITEMMENU_PRICE));
    Response response =
        target("restaurants/" + RESTAURANT_ID + "/itemMenu/1")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .delete();

    assertThat(response.getStatus()).isEqualTo(204);
  }

  @Test
  public void whenDeleteItemMenuWithInvalidOwnerId_shouldThrowException() {
    itemMenuRepository.save(
        new ItemMenu("1", RESTAURANT_ID, ITEMMENU_NAME, ITEMMENU_DESCRIPTION, ITEMMENU_PRICE));
    Response response =
        target("restaurants/" + RESTAURANT_ID + "/itemMenu/1")
            .request()
            .header("Owner", INVALID_RESTAURANT_OWNER_ID)
            .delete();

    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void whenDeleteItemMenuWithInvalidRestaurantId_shouldThrowException() {
    itemMenuRepository.save(
        new ItemMenu("1", RESTAURANT_ID, ITEMMENU_NAME, ITEMMENU_DESCRIPTION, ITEMMENU_PRICE));
    Response response =
        target("restaurants/" + INVALID_RESTAURANT_ID + "/itemMenu/1")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .delete();

    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void whenDeleteItemMenuWithInvalidItemMenuId_shouldThrowException() {
    itemMenuRepository.save(
        new ItemMenu("1", RESTAURANT_ID, ITEMMENU_NAME, ITEMMENU_DESCRIPTION, ITEMMENU_PRICE));
    Response response =
        target("restaurants/" + RESTAURANT_ID + "/itemMenu/999")
            .request()
            .header("Owner", RESTAURANT_OWNER_ID)
            .delete();

    assertThat(response.getStatus()).isEqualTo(404);
  }
}
