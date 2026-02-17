package ca.ulaval.glo2003.itemMenu.api;

import ca.ulaval.glo2003.itemMenu.domain.ItemMenuService;
import ca.ulaval.glo2003.utilities.MissingParameterException;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;

@Path("")
public class ItemMenuResource {

  private final ItemMenuService itemMenuService;

  public ItemMenuResource(ItemMenuService itemMenuService) {
    this.itemMenuService = itemMenuService;
  }

  @POST
  @Path("restaurants/{id}/itemMenu")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createItemMenu(
      @HeaderParam("Owner") String ownerId,
      @PathParam("id") String restaurantId,
      CreateItemMenuBody body) {

    if (ownerId == null || ownerId.isBlank()) {
      throw new MissingParameterException();
    }

    body.validate();

    ItemMenuDTO itemMenu = new ItemMenuDTO(restaurantId, body);
    String location = itemMenuService.addItemMenu(ownerId, itemMenu);

    return Response.status(Response.Status.CREATED).header("Location", location).build();
  }

  @GET
  @Path("restaurants/{id}/menu")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMenu(@PathParam("id") String restaurantId) {

    ArrayList<ItemMenuDTO> items = itemMenuService.getMenu(restaurantId);
    ArrayList<JsonObject> response = new ArrayList<>();

    for (ItemMenuDTO item : items) {
      response.add(item.createResponseBody());
    }

    return Response.status(Response.Status.OK).entity(response).build();
  }

  @DELETE
  @Path("restaurants/{restaurantId}/itemMenu/{itemId}")
  public Response deleteItemMenu(
      @HeaderParam("Owner") String ownerId,
      @PathParam("restaurantId") String restaurantId,
      @PathParam("itemId") String itemMenuId) {

    if (ownerId == null || ownerId.isBlank()) {
      throw new MissingParameterException();
    }

    itemMenuService.deleteItemMenu(ownerId, restaurantId, itemMenuId);

    return Response.status((Response.Status.NO_CONTENT)).build();
  }
}
