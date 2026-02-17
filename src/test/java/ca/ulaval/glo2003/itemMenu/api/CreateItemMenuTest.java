package ca.ulaval.glo2003.itemMenu.api;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.MissingParameterException;
import org.junit.jupiter.api.Test;

public class CreateItemMenuTest {
  @Test
  public void givenValidValues_whenValidate_shouldNotThrowException() {
    CreateItemMenuBody body = new CreateItemMenuBody("Pasta", "Tomato pasta", "4.99");

    assertDoesNotThrow(body::validate);
  }

  @Test
  public void givenInvalidName_whenValidate_shouldThrowException() {
    CreateItemMenuBody bodyEmptyName = new CreateItemMenuBody("", "Tomato pasta", "4.99");
    CreateItemMenuBody bodyNullName = new CreateItemMenuBody(null, "Tomato pasta", "4.99");

    assertThrows(InvalidParameterException.class, bodyEmptyName::validate);
    assertThrows(MissingParameterException.class, bodyNullName::validate);
  }

  @Test
  public void givenInvalidDescription_whenValidate_shouldThrowException() {
    CreateItemMenuBody bodyEmptyDescription = new CreateItemMenuBody("Pasta", "", "4.99");
    CreateItemMenuBody bodyNullDescription = new CreateItemMenuBody("Pasta", null, "4.99");

    assertThrows(InvalidParameterException.class, bodyEmptyDescription::validate);
    assertThrows(MissingParameterException.class, bodyNullDescription::validate);
  }

  @Test
  public void givenInvalidPrice_whenValidate_shouldThrowException() {
    CreateItemMenuBody bodyEmptyPrice = new CreateItemMenuBody("Pasta", "Tomato pasta", "");
    CreateItemMenuBody bodyNotNumericPrice =
        new CreateItemMenuBody("Pasta", "Tomato pasta", "4a5.99");
    CreateItemMenuBody bodyNullPrice = new CreateItemMenuBody("Pasta", "Tomato pasta", null);

    assertThrows(InvalidParameterException.class, bodyEmptyPrice::validate);
    assertThrows(InvalidParameterException.class, bodyNotNumericPrice::validate);
    assertThrows(MissingParameterException.class, bodyNullPrice::validate);
  }
}
