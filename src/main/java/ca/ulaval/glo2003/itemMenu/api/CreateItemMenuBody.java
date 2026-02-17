package ca.ulaval.glo2003.itemMenu.api;

import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.MissingParameterException;

public class CreateItemMenuBody {
  public String name;
  public String description;
  public String price;

  public CreateItemMenuBody() {}

  public CreateItemMenuBody(String name, String description, String price) {
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public float getPrice() {
    return (float) Math.round(Float.parseFloat(price) * 100) / 100;
  }

  public void validate() {
    if (name == null || description == null || price == null) {
      throw new MissingParameterException();
    }

    if (name.isBlank() || description.isBlank() || price.isBlank()) {
      throw new InvalidParameterException();
    }

    try {
      getPrice();
    } catch (Exception e) {
      throw new InvalidParameterException();
    }
  }
}
