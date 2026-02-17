package ca.ulaval.glo2003.restaurant.api;

import ca.ulaval.glo2003.reservation.api.ReservationDTO;
import ca.ulaval.glo2003.restaurant.domain.RestaurantService;
import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.MissingParameterException;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;

@Path("")
public class RestaurantResource {

  private final RestaurantService restaurantService;

  public RestaurantResource(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @POST
  @Path("restaurants")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createRestaurant(
      @HeaderParam("Owner") String ownerId, CreateRestaurantBody body) {

    if (ownerId == null || ownerId.isBlank()) {
      throw new MissingParameterException();
    }

    body.validateCreateRestaurantBody();

    RestaurantDTO restaurant = new RestaurantDTO(ownerId, body);
    String location = restaurantService.addRestaurant(restaurant);

    return Response.status(Response.Status.CREATED).header("Location", location).build();
  }

  @GET
  @Path("restaurants")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurantsList(@HeaderParam("Owner") String ownerId) {

    if (ownerId == null || ownerId.isBlank()) {
      throw new MissingParameterException();
    }

    ArrayList<RestaurantDTO> ownerRestaurants = restaurantService.getOwnerRestaurantsList(ownerId);
    ArrayList<JsonObject> response = new ArrayList<>();

    for (RestaurantDTO restaurant : ownerRestaurants) {
      response.add(restaurant.createResponseBody(true));
    }

    return Response.status(Response.Status.OK).entity(response).build();
  }

  @GET
  @Path("restaurants/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRestaurantById(
      @HeaderParam("Owner") String ownerId, @PathParam("id") String restaurantId) {

    if (ownerId == null || ownerId.isBlank()) {
      throw new MissingParameterException();
    }

    RestaurantDTO restaurant = restaurantService.getById(ownerId, restaurantId);

    return Response.status(Response.Status.OK).entity(restaurant.createResponseBody(true)).build();
  }

  @POST
  @Path("search/restaurants")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchRestaurants(SearchRestaurantsBody body) {

    if (body == null) {
      throw new MissingParameterException();
    }

    body.validateSearchRestaurantBody();

    RestaurantSearchDTO restaurantSearch =
        new RestaurantSearchDTO(body.getName(), body.getOpened());
    ArrayList<RestaurantDTO> restaurantsList =
        restaurantService.searchRestaurants(restaurantSearch);
    ArrayList<JsonObject> response = new ArrayList<>();

    for (RestaurantDTO restaurant : restaurantsList) {
      response.add(restaurant.createResponseBody(false));
    }

    return Response.status(Response.Status.OK).entity(response).build();
  }

  @DELETE
  @Path("restaurants/{id}")
  public Response deleteRestaurant(
      @HeaderParam("Owner") String ownerId, @PathParam("id") String restaurantId) {

    if (ownerId == null || ownerId.isBlank()) {
      throw new MissingParameterException();
    }

    restaurantService.deleteRestaurant(ownerId, restaurantId);

    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @GET
  @Path("restaurants/{id}/reservations")
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchReservation(
      @HeaderParam("Owner") String ownerId,
      @PathParam("id") String restaurantId,
      @QueryParam("date") String date,
      @QueryParam("customerName") String customerName) {

    if (ownerId == null || ownerId.isBlank()) {
      throw new MissingParameterException();

    } else if (customerName != null && (customerName.contains("\"") || customerName.isBlank())) {
      throw new InvalidParameterException();
    }

    ArrayList<JsonObject> response = new ArrayList<>();
    ArrayList<ReservationDTO> reservationsDTOSList =
        restaurantService.getReservationsList(ownerId, restaurantId);
    ArrayList<ReservationDTO> reservationsList =
        restaurantService.searchReservations(reservationsDTOSList, date, customerName);

    for (ReservationDTO reservation : reservationsList) {
      response.add(reservation.createResponseBody(false));
    }

    return Response.status(Response.Status.OK).entity(response).build();
  }

  @GET
  @Path("restaurants/{id}/availabilities")
  public Response searchDispo(
      @PathParam("id") String restaurantId, @QueryParam("date") String date) {

    if (date == null || date.isBlank()) throw new MissingParameterException();
    if (restaurantId == null || restaurantId.isBlank()) throw new MissingParameterException();

    ArrayList<DispoDTO> dispos = restaurantService.searchDispo(restaurantId, date);
    ArrayList<JsonObject> response = new ArrayList<>();

    for (DispoDTO dispo : dispos) {
      response.add(dispo.createResponseBody());
    }

    return Response.status(Response.Status.OK).entity(response).build();
  }
}
