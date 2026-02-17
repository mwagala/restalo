package ca.ulaval.glo2003.reservation.api;

import ca.ulaval.glo2003.reservation.domain.ReservationService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class ReservationResource {

  private final ReservationService reservationService;

  public ReservationResource(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @POST
  @Path("restaurants/{id}/reservations")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createReservation(
      @PathParam("id") String restaurantId, CreateReservationBody body) {

    body.validateCreateReservationBody();

    ReservationDTO reservation = new ReservationDTO(restaurantId, body);
    String location = reservationService.addReservation(reservation);

    return Response.status(Response.Status.CREATED).header("Location", location).build();
  }

  @GET
  @Path("reservations/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReservationByNumber(@PathParam("id") String reservationId) {

    ReservationDTO foundReservation = reservationService.getByNumber(reservationId);

    return Response.status(Response.Status.OK)
        .entity(foundReservation.createResponseBody(true))
        .build();
  }

  @DELETE
  @Path("reservations/{id}")
  public Response deleteReservation(@PathParam("id") String reservationId) {

    reservationService.deleteReservation(reservationId);

    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
