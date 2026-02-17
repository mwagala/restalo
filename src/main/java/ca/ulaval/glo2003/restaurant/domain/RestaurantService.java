package ca.ulaval.glo2003.restaurant.domain;

import ca.ulaval.glo2003.itemMenu.persistence.ItemMenuRepository;
import ca.ulaval.glo2003.reservation.api.ReservationDTO;
import ca.ulaval.glo2003.reservation.domain.Reservation;
import ca.ulaval.glo2003.reservation.persistence.ReservationRepository;
import ca.ulaval.glo2003.restaurant.api.DispoDTO;
import ca.ulaval.glo2003.restaurant.api.RestaurantDTO;
import ca.ulaval.glo2003.restaurant.api.RestaurantSearchDTO;
import ca.ulaval.glo2003.restaurant.persistence.RestaurantRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.glassfish.grizzly.utils.Pair;

public class RestaurantService {
  private final RestaurantRepository restaurantRepository;
  private final ReservationRepository reservationRepository;
  private final ItemMenuRepository itemMenuRepository;
  private final RestaurantValidator restaurantValidator;

  private static Integer restaurantId = 0;

  private RestaurantFactory factory;

  public RestaurantService(
      RestaurantRepository restaurantRepository,
      ReservationRepository reservationRepository,
      ItemMenuRepository itemMenuRepository,
      RestaurantValidator restaurantValidator) {

    this.restaurantRepository = restaurantRepository;
    this.reservationRepository = reservationRepository;
    this.itemMenuRepository = itemMenuRepository;
    this.restaurantValidator = restaurantValidator;
    this.factory = new RestaurantFactory();
  }

  public String addRestaurant(RestaurantDTO restaurant) {

    restaurantValidator.validateRestaurantValues(restaurant);

    String location = "http://localhost:8080/restaurants/" + (++restaurantId);
    restaurantRepository.save(factory.createRestaurant(restaurantId.toString(), restaurant));

    return location;
  }

  public ArrayList<RestaurantDTO> transformRestaurantsToRestaurantsDTO(
      ArrayList<Restaurant> restaurants) {

    ArrayList<RestaurantDTO> restaurantsDTO = new ArrayList<>();

    for (Restaurant entry : restaurants) {
      restaurantsDTO.add(new RestaurantDTO(entry));
    }

    return restaurantsDTO;
  }

  public ArrayList<RestaurantDTO> getOwnerRestaurantsList(String ownerId) {

    ArrayList<Restaurant> restaurants = restaurantRepository.getOwnerRestaurantsList(ownerId);

    return transformRestaurantsToRestaurantsDTO(restaurants);
  }

  public RestaurantDTO getById(String ownerId, String restaurantId) {

    Map<String, Restaurant> restaurantsList = restaurantRepository.getRestaurantsList();
    restaurantValidator.validateRestaurantId(restaurantId, restaurantsList);
    Restaurant restaurant = restaurantRepository.get(restaurantId);
    restaurantValidator.validateOwnerId(restaurant, ownerId);

    return new RestaurantDTO(restaurant);
  }

  public ArrayList<RestaurantDTO> searchRestaurants(RestaurantSearchDTO restaurantSearch) {

    restaurantValidator.validateHours(restaurantSearch.opened());

    ArrayList<Restaurant> restaurants =
        restaurantRepository.getSearchedRestaurants(
            new RestaurantSearch(restaurantSearch.name(), restaurantSearch.opened()));

    return transformRestaurantsToRestaurantsDTO(restaurants);
  }

  public void deleteRestaurant(String ownerId, String restaurantId) {

    Map<String, Restaurant> restaurantsList = restaurantRepository.getRestaurantsList();

    restaurantValidator.validateRestaurantId(restaurantId, restaurantsList);
    Restaurant restaurant = restaurantRepository.get(restaurantId);
    restaurantValidator.validateOwnerId(restaurant, ownerId);

    reservationRepository.deleteReservationsByRestaurantId(restaurantId);
    itemMenuRepository.deleteItemMenuByRestaurantId(restaurantId);
    restaurantRepository.delete(restaurantId);
  }

  public ArrayList<ReservationDTO> getReservationsList(String ownerId, String restaurantId) {

    Map<String, Restaurant> restaurantsList = restaurantRepository.getRestaurantsList();

    restaurantValidator.validateRestaurantId(restaurantId, restaurantsList);
    Restaurant restaurant = restaurantRepository.get(restaurantId);
    restaurantValidator.validateOwnerId(restaurant, ownerId);

    RestaurantDTO restaurantDTO = new RestaurantDTO(restaurant);
    Map<String, Reservation> reservationsList = reservationRepository.getReservationsList();
    ArrayList<ReservationDTO> reservationsDTOList = new ArrayList<>();

    for (Reservation entry : reservationsList.values()) {
      if (entry.getRestaurantId().equals(restaurantId)) {
        reservationsDTOList.add(new ReservationDTO(entry, restaurantDTO));
      }
    }

    return reservationsDTOList;
  }

  public ArrayList<ReservationDTO> searchReservations(
      ArrayList<ReservationDTO> reservationsDTOSList, String date, String customerName) {

    ArrayList<ReservationDTO> searchedReservationsList = new ArrayList<>();

    if (date != null && customerName != null) {
      filterByDateAndCustomerName(
          reservationsDTOSList, searchedReservationsList, date, customerName);

    } else if (date != null) {
      filterByDate(reservationsDTOSList, searchedReservationsList, date);

    } else if (customerName != null) {
      filterByCustomerName(reservationsDTOSList, searchedReservationsList, customerName);

    } else {
      searchedReservationsList.addAll(reservationsDTOSList);
    }

    return searchedReservationsList;
  }

  private void filterByDateAndCustomerName(
      ArrayList<ReservationDTO> reservationsDTOSList,
      ArrayList<ReservationDTO> searchedReservationsList,
      String date,
      String customerName) {

    for (ReservationDTO reservation : reservationsDTOSList) {

      if ((reservation.getDate().equals(LocalDate.parse(date)))
          && (reservation
              .getCustomer()
              .get("name")
              .toLowerCase()
              .replaceAll("\\s", "")
              .startsWith(customerName.toLowerCase().replaceAll("\\s", "")))) {

        searchedReservationsList.add(reservation);
      }
    }
  }

  private void filterByDate(
      ArrayList<ReservationDTO> reservationsDTOSList,
      ArrayList<ReservationDTO> searchedReservationsList,
      String date) {

    for (ReservationDTO reservation : reservationsDTOSList) {
      if (reservation.getDate().equals(LocalDate.parse(date))) {

        searchedReservationsList.add(reservation);
      }
    }
  }

  private void filterByCustomerName(
      ArrayList<ReservationDTO> reservationsDTOSList,
      ArrayList<ReservationDTO> searchedReservationsList,
      String customerName) {

    for (ReservationDTO reservation : reservationsDTOSList) {

      if (reservation
          .getCustomer()
          .get("name")
          .toLowerCase()
          .replaceAll("\\s", "")
          .startsWith(customerName.toLowerCase().replaceAll("\\s", ""))) {

        searchedReservationsList.add(reservation);
      }
    }
  }

  public ArrayList<DispoDTO> searchDispo(String restaurantId, String date) {

    final Integer interval = 15;

    LocalDate parsedDate = restaurantValidator.validateDate(date);
    restaurantValidator.validateRestaurantId(
        restaurantId, restaurantRepository.getRestaurantsList());

    Restaurant restaurant = restaurantRepository.get(restaurantId);


    final Integer capacity = restaurant.getCapacity();
    final Integer reservationDuration = restaurant.getReservations().get("duration");;
    Pair<Integer, Integer> schedule = getDispoSchedule(restaurantId, interval);
    ArrayList<DispoDTO> dispos =
        generateInitialDispoArray(
            schedule.getFirst(), schedule.getSecond(), parsedDate, capacity, interval);

    HashMap<String, Reservation> reservations =
        reservationRepository.getReservationByRestaurantId(restaurantId);

    for (Map.Entry<String, Reservation> entry : reservations.entrySet()) {
      setRemainingReservationPlaces(dispos, entry.getValue(), interval, reservationDuration);
    }

    return dispos;
  }

  private void setRemainingReservationPlaces(
      ArrayList<DispoDTO> dispos, Reservation reservation, Integer interval, Integer reservationDuration) {

    for (DispoDTO dispo : dispos) {

      if ((dispo
                      .getStart()
                      .isAfter(LocalDateTime.of(reservation.getDate(), reservation.getStartTime().minusMinutes(reservationDuration)))
                  || dispo
                      .getStart()
                      .isEqual(LocalDateTime.of(reservation.getDate(), reservation.getStartTime())))
              && (dispo
                  .getStart()
                  .plusMinutes(interval)
                  .isBefore(LocalDateTime.of(reservation.getDate(), reservation.getEndTime())))
          || dispo
              .getStart()
              .plusMinutes(interval)
              .isEqual(LocalDateTime.of(reservation.getDate(), reservation.getEndTime()))) {
        dispo.setRemainingPlaces(dispo.getRemainingPlaces() - reservation.getGroupSize());
      }
    }
  }

  public ArrayList<DispoDTO> generateInitialDispoArray(
      Integer openHourInMinutes,
      Integer closeHourInMinutes,
      LocalDate date,
      Integer capacity,
      Integer interval) {

    ArrayList<DispoDTO> dispos = new ArrayList<>();

    for (int i = openHourInMinutes; i <= closeHourInMinutes; i += interval) {

      int hours = i / 60;
      int minutes = i % 60;
      LocalDateTime dateTime = date.atTime(hours, minutes);

      dispos.add(new DispoDTO(dateTime, capacity));
    }

    return dispos;
  }

  public Pair<Integer, Integer> getDispoSchedule(String restaurantId, Integer interval) {

    Restaurant restaurant = restaurantRepository.get(restaurantId);
    HashMap<String, LocalTime> hours = restaurant.getHours();
    LocalTime openHour = hours.get("open");
    LocalTime closeHour = hours.get("close");
    Integer duration = restaurant.getReservations().get("duration");

    Integer firstValidHourInMinutes = openHour.getHour() * 60 + openHour.getMinute();

    if (((openHour.getMinute() + interval) % interval) != 0) {
      firstValidHourInMinutes += interval - ((openHour.getMinute() + interval) % interval);
    }

    Integer lastValidHourInMinutes =
        closeHour.getHour() * 60
            + closeHour.getMinute()
            - duration
            - ((closeHour.getMinute() + interval) % interval);

    return new Pair<>(firstValidHourInMinutes, lastValidHourInMinutes);
  }
}
