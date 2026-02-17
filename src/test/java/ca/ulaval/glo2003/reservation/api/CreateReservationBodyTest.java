package ca.ulaval.glo2003.reservation.api;

import static org.junit.jupiter.api.Assertions.*;

import ca.ulaval.glo2003.utilities.InvalidParameterException;
import ca.ulaval.glo2003.utilities.MissingParameterException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import org.junit.jupiter.api.*;

class CreateReservationBodyTest {
  private CreateReservationBody reservationBody;
  String startTime;
  HashMap<String, String> customer;
  String groupSize;
  String date;
  String restaurantId;
  String reservationId;

  @BeforeEach
  void setUp() {
    customer = new HashMap<>();
    customer.put("name", "alice");
    customer.put("phoneNumber", "4389252369");
    customer.put("email", "test@test.com");
    date = "2024-02-21";
    startTime = "21:04:00";
    groupSize = "10";
    reservationId = "3";
    restaurantId = "1";

    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);
  }

  @Test
  void givenValidParameters_whenCreateReservationBodyConstructor_shouldReturnTrue() {
    assertTrue(
        reservationBody.startTime == startTime
            && reservationBody.groupSize == groupSize
            && reservationBody.date == date
            && reservationBody.customer == customer);
  }

  @Test
  void givenInvalidDate_whenGetDate_shouldThrowException() {
    String date = "2024-13-12";
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        DateTimeParseException.class,
        () -> {
          reservationBody.getDate();
        });
  }

  @Test
  void givenValidDate_whenGetDate_shouldReturnDate() {
    assertEquals(LocalDate.of(2024, 2, 21), reservationBody.getDate());
  }

  @Test
  void givenInvalidStartTime_whenGetStartTime_shouldThrowException() {
    String startTime = "13:60:00";
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        DateTimeParseException.class,
        () -> {
          reservationBody.getStartTime();
        });
  }

  @Test
  void givenValidStartTime_whenGetStartTime_shouldReturnStartTime() {
    assertEquals(LocalTime.of(21, 4, 0), reservationBody.getStartTime());
  }

  @Test
  void givenInvalidGroupSize_whenGetGroupSize_shouldThrowException() {
    String groupSize = "a";
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        NumberFormatException.class,
        () -> {
          reservationBody.getGroupSize();
        });
  }

  @Test
  void givenValidGroupSize_whenGetGroupSize_shouldReturnGroupSize() {
    assertEquals(10, reservationBody.getGroupSize());
  }

  @Test
  void givenNullStartTime_whenValidateCreateReservationBody_shouldThrowException() {
    reservationBody = new CreateReservationBody(null, customer, groupSize, date);

    assertThrows(
        MissingParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenNullGroupSize_whenValidateCreateReservationBody_shouldThrowException() {
    reservationBody = new CreateReservationBody(startTime, customer, null, date);

    assertThrows(
        MissingParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenNullDate_whenValidateCreateReservationBody_shouldThrowException() {
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, null);

    assertThrows(
        MissingParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenNullCustomer_whenValidateCreateReservationBody_shouldThrowException() {
    reservationBody = new CreateReservationBody(startTime, null, groupSize, date);

    assertThrows(
        MissingParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenNullCustomerName_whenValidateCreateReservationBody_shouldThrowException() {
    customer = new HashMap<>();
    customer.put("phoneNumber", "4389252369");
    customer.put("email", "test@test.com");
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        MissingParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenNullCustomerEmail_whenValidateCreateReservationBody_shouldThrowException() {
    customer = new HashMap<>();
    customer.put("name", "alice");
    customer.put("phoneNumber", "4389252369");
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        MissingParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenNullCustomerPhoneNumber_whenValidateCreateReservationBody_shouldThrowException() {
    customer = new HashMap<>();
    customer.put("name", "alice");
    customer.put("email", "test@test.com");
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        MissingParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenBlankCustomerName_whenValidateCreateReservationBody_shouldThrowException() {
    customer = new HashMap<>();
    customer.put("name", "");
    customer.put("phoneNumber", "4389252369");
    customer.put("email", "test@test.com");
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenInvalidCustomerEmail_whenValidateCreateReservationBody_shouldThrowException() {
    customer = new HashMap<>();
    customer.put("name", "alice");
    customer.put("phoneNumber", "4389252369");
    customer.put("email", "test@testcom");
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenInvalidCustomerPhoneNumber_whenValidateCreateReservationBody_shouldThrowException() {
    customer = new HashMap<>();
    customer.put("name", "alice");
    customer.put("phoneNumber", "438925236");
    customer.put("email", "test@test.com");
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenInvalidStartTime_whenValidateCreateReservationBody_shouldThrowException() {
    startTime = "23:06:90";
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenInvalidGroupSize_whenValidateCreateReservationBody_shouldThrowException() {
    groupSize = "b";
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenInvalidDate_whenValidateCreateReservationBody_shouldThrowException() {
    date = "2024-04-31";
    reservationBody = new CreateReservationBody(startTime, customer, groupSize, date);

    assertThrows(
        InvalidParameterException.class,
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }

  @Test
  void givenValidCreateReservationBody_whenValidateCreateReservationBody_shouldNotThrowException() {
    assertDoesNotThrow(
        () -> {
          reservationBody.validateCreateReservationBody();
        });
  }
}
