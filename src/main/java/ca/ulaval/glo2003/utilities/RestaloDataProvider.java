package ca.ulaval.glo2003.utilities;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class RestaloDataProvider {
  public Datastore provide() {

    var mongoUrl = System.getenv("MONGO_CLUSTER_URL");
    var database = System.getenv("MONGO_DATABASE");

    return Morphia.createDatastore(MongoClients.create(mongoUrl), database);
  }
}
