package ca.ulaval.glo2003.restaurant.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.json.JsonObject;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class DispoDTOTest {
  @Test
  void givenValidDispoDTO_whenCreateResponseBody_JsonObjectShouldBeValid() {
    LocalDateTime date = LocalDateTime.of(2024, 4, 3, 8, 45, 0);
    Integer remainingPlaces = 100;
    DispoDTO dispoDTO = new DispoDTO(date, remainingPlaces);

    JsonObject responseBody = dispoDTO.createResponseBody();

    assertEquals("\"2024-04-03T08:45:00\"", responseBody.get("start").toString());
    assertEquals("100", responseBody.get("remainingPlaces").toString());
  }
}
