package ca.ulaval.glo2003.reservation.api;

import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.MissingParameterException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CreateReservationBody {
  public String date;
  public String startTime;
  public String groupSize;
  public HashMap<String, String> customer;

  public CreateReservationBody() {}

  public CreateReservationBody(
      String startTime, HashMap<String, String> customer, String groupSize, String date) {
    this.startTime = startTime;
    this.groupSize = groupSize;
    this.date = date;
    this.customer = customer;
  }

  public LocalDate getDate() {
    return LocalDate.parse(this.date);
  }

  public LocalTime getStartTime() {
    return LocalTime.parse(this.startTime);
  }

  public Integer getGroupSize() {
    return Integer.parseInt(this.groupSize);
  }

  public HashMap<String, String> getCustomer() {
    return this.customer;
  }

  public void validateCreateReservationBody() {
    if (!validateMissingParameter()) {
      throw new MissingParameterException();
    }

    if (this.customer.get("name").isBlank()
        || !validateEmail(this.customer.get("email"))
        || !validatePhoneNumber(this.customer.get("phoneNumber"))) {
      throw new InvalidParameterException();
    }

    try {
      getStartTime();
      getGroupSize();
      getDate();
    } catch (DateTimeParseException | NumberFormatException e) {
      throw new InvalidParameterException();
    }
  }

  private boolean validateMissingParameter() {
    return this.startTime != null
        && this.groupSize != null
        && this.date != null
        && this.customer != null
        && this.customer.get("name") != null
        && this.customer.get("email") != null
        && this.customer.get("phoneNumber") != null;
  }

  private boolean validateEmail(String emailAddress) {
    String regexPattern = "^(\\S+)@(\\S+)\\.(\\S+)$";
    return Pattern.compile(regexPattern).matcher(emailAddress).matches();
  }

  private boolean validatePhoneNumber(String phoneNumber) {
    String regexPattern = "^\\d{10}$";
    return Pattern.compile(regexPattern).matcher(phoneNumber).matches();
  }
}
