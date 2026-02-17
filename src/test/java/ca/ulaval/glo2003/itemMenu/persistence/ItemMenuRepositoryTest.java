package ca.ulaval.glo2003.itemMenu.persistence;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.itemMenu.domain.ItemMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class ItemMenuRepositoryTest {
  ItemMenu itemMenu;
  ItemMenuRepository itemMenuRepository;

  protected abstract ItemMenuRepository createPersistence();

  @BeforeEach
  public void setup() {
    itemMenu = new ItemMenu("1", "1", "Pasta", "Tomato pasta", 5.99f);
    itemMenuRepository = createPersistence();
  }

  @Test
  public void givenValidArguments_whenSave_shouldSaveItemMenu() {
    itemMenuRepository.save(itemMenu);

    assertEquals(
        itemMenu.getItemMenuId(), itemMenuRepository.get(itemMenu.getItemMenuId()).getItemMenuId());
  }

  @Test
  public void givenOneSavedItemMenu_whenGetRestaurantList_shouldReturnItemMenuListWithOneItem() {
    itemMenuRepository.save(itemMenu);

    assertEquals(1, itemMenuRepository.getItemMenuList().size());
    assertEquals(
        itemMenu.getItemMenuId(),
        itemMenuRepository.getItemMenuList().get(itemMenu.getItemMenuId()).getItemMenuId());
  }

  @Test
  public void givenValidId_whenDelete_shouldDeleteItemMenu() {
    itemMenuRepository.save(itemMenu);
    itemMenuRepository.delete(itemMenu.getItemMenuId());

    assertEquals(0, itemMenuRepository.getItemMenuList().size());
  }

  @Test
  public void givenInvalidId_whenDelete_shouldNotDeleteItemMenu() {
    itemMenuRepository.save(itemMenu);
    itemMenuRepository.delete("99");

    assertEquals(1, itemMenuRepository.getItemMenuList().size());
  }

  @Test
  public void givenValidId_whenDeleteItemMenuByRestaurantId_shouldDeleteItemMenu() {
    itemMenuRepository.save(itemMenu);
    itemMenuRepository.deleteItemMenuByRestaurantId(itemMenu.getRestaurantId());

    assertEquals(0, itemMenuRepository.getItemMenuList().size());
  }

  @Test
  public void givenInvalidId_whenDeleteItemMenuByRestaurantId_shouldNotDeleteItemMenu() {
    itemMenuRepository.save(itemMenu);
    itemMenuRepository.deleteItemMenuByRestaurantId("99");

    assertEquals(1, itemMenuRepository.getItemMenuList().size());
  }
}
