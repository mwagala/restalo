package ca.ulaval.glo2003.itemMenu.persistence;

import com.mongodb.client.MongoClients;
import dev.morphia.Morphia;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MongoItemMenuRepositoryTest extends ItemMenuRepositoryTest {
  @Container private final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

  @Override
  protected ItemMenuRepository createPersistence() {
    System.out.println(mongoDBContainer.getConnectionString());
    var mongoUrl = MongoClients.create(mongoDBContainer.getConnectionString());
    var dataStore = Morphia.createDatastore(mongoUrl, "test");
    return new MongoItemMenuRepository(dataStore);
  }
}
