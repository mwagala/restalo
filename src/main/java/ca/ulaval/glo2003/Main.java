package ca.ulaval.glo2003;

import ca.ulaval.glo2003.health.api.HealthResource;
import ca.ulaval.glo2003.itemMenu.api.ItemMenuResource;
import ca.ulaval.glo2003.reservation.api.ReservationResource;
import ca.ulaval.glo2003.restaurant.api.RestaurantResource;
import ca.ulaval.glo2003.utilities.*;
import io.sentry.Sentry;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {

  public static final String BASE_URI = "http://0.0.0.0:8080";

  public static HttpServer startServer(ApplicationContext applicationContext) {

    final ResourceConfig rc =
        new ResourceConfig()
            .register(new HealthResource())
            .register(new RestaurantResource(applicationContext.getRestaurantService()))
            .register(new ReservationResource(applicationContext.getReservationService()))
            .register(new ItemMenuResource(applicationContext.getItemMenuService()))
            .register(new InvalidParameterExceptionMapper())
            .register(new RuntimeExceptionMapper())
            .register(new MissingParameterExceptionMapper())
            .register(new NotFoundExceptionMapper());

    if (System.getProperty("PORT") != null && !System.getProperty("PORT").isBlank()) {
      String uri = String.format("http://0.0.0.0:%s", System.getProperty("PORT"));
      return GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
    }

    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  public static void main(String[] args) {

    final ApplicationContext appContext = new ApplicationContext();
    startServer(appContext);

    if (System.getProperty("PORT") != null && !System.getProperty("PORT").isBlank()) {
      String uri = String.format("http://0.0.0.0:%s", System.getProperty("PORT"));
      System.out.printf("Jersey app started with endpoints available at %s%n", uri);

    } else {
      System.out.printf("Jersey app started with endpoints available at %s%n", BASE_URI);
    }

    Sentry.init(
        options -> {
          var sentryDsn = System.getenv("SENTRY_DSN");
          options.setDsn(sentryDsn);
        });
  }
}
