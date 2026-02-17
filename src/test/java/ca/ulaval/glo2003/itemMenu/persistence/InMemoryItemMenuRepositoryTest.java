package ca.ulaval.glo2003.itemMenu.persistence;

import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class InMemoryItemMenuRepositoryTest extends ItemMenuRepositoryTest {

  @Override
  protected ItemMenuRepository createPersistence() {
    return new InMemoryItemMenuRepository();
  }
}
