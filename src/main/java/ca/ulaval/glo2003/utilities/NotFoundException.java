package ca.ulaval.glo2003.utilities;

public class NotFoundException extends RuntimeException {
  public String description;

  public NotFoundException(String description) {
    this.description = description;
  }
}
