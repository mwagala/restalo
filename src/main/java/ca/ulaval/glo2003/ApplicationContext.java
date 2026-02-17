package ca.ulaval.glo2003;

import ca.ulaval.glo2003.itemMenu.domain.ItemMenuService;
import ca.ulaval.glo2003.itemMenu.domain.ItemMenuValidator;
import ca.ulaval.glo2003.itemMenu.persistence.InMemoryItemMenuRepository;
import ca.ulaval.glo2003.itemMenu.persistence.ItemMenuRepository;
import ca.ulaval.glo2003.itemMenu.persistence.MongoItemMenuRepository;
import ca.ulaval.glo2003.reservation.domain.ReservationService;
import ca.ulaval.glo2003.reservation.domain.ReservationValidator;
import ca.ulaval.glo2003.reservation.persistence.InMemoryReservationRepository;
import ca.ulaval.glo2003.reservation.persistence.MongoReservationRepository;
import ca.ulaval.glo2003.reservation.persistence.ReservationRepository;
import ca.ulaval.glo2003.restaurant.domain.RestaurantService;
import ca.ulaval.glo2003.restaurant.domain.RestaurantValidator;
import ca.ulaval.glo2003.restaurant.persistence.InMemoryRestaurantRepository;
import ca.ulaval.glo2003.restaurant.persistence.MongoRestaurantRepository;
import ca.ulaval.glo2003.restaurant.persistence.RestaurantRepository;
import ca.ulaval.glo2003.utilities.RestaloDataProvider;
import java.util.Objects;

public class ApplicationContext {

  private final ReservationService reservationService;

  private final RestaurantService restaurantService;

  private final ItemMenuService itemMenuService;

  public ApplicationContext() {

    RestaurantRepository restaurantRepository = null;
    ReservationRepository reservationRepository = null;
    ItemMenuRepository itemMenuRepository = null;

    if (Objects.equals(System.getProperty("persistence"), "mongo")) {
      RestaloDataProvider provider = new RestaloDataProvider();
      restaurantRepository = new MongoRestaurantRepository(provider.provide());
      reservationRepository = new MongoReservationRepository(provider.provide());
      itemMenuRepository = new MongoItemMenuRepository(provider.provide());

    } else {
      restaurantRepository = new InMemoryRestaurantRepository();
      reservationRepository = new InMemoryReservationRepository();
      itemMenuRepository = new InMemoryItemMenuRepository();
    }

    this.reservationService =
        new ReservationService(
            reservationRepository, restaurantRepository, new ReservationValidator());

    this.restaurantService =
        new RestaurantService(
            restaurantRepository,
            reservationRepository,
            itemMenuRepository,
            new RestaurantValidator());

    this.itemMenuService =
        new ItemMenuService(itemMenuRepository, restaurantRepository, new ItemMenuValidator());
  }

  public ReservationService getReservationService() {
    return reservationService;
  }

  public RestaurantService getRestaurantService() {
    return restaurantService;
  }

  public ItemMenuService getItemMenuService() {
    return itemMenuService;
  }
}
