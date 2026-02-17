package ca.ulaval.glo2003.reservation.domain;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

@Entity
public class Reservation {
  private String restaurantId;
  @Id private String reservationId;
  private LocalTime startTime;
  private Integer groupSize;
  private LocalDate date;
  private LocalTime endTime;
  public HashMap<String, String> customer;

  public Reservation(
      String restaurantId,
      String reservationId,
      LocalTime startTime,
      Integer reservationDuration,
      HashMap<String, String> customer,
      Integer groupSize,
      LocalDate date) {
    this.restaurantId = restaurantId;
    this.startTime = startTime;
    this.reservationId = reservationId;
    this.groupSize = groupSize;
    this.date = date;
    this.customer = customer;

    setValidStartAndEndTime(reservationDuration);
  }

  private void setValidStartAndEndTime(Integer reservationDuration) {
    if (this.startTime.getSecond() != 0) {
      this.startTime = LocalTime.of(this.startTime.getHour(), this.startTime.getMinute() + 1);
    }

    int startMod15Minutes = this.startTime.getMinute() % 15;

    if (startMod15Minutes != 0) {
      this.startTime = this.startTime.plusMinutes(15 - startMod15Minutes);
    }

    this.endTime = this.startTime.plusMinutes(reservationDuration);
  }

  public Reservation() {}

  public String getRestaurantId() {
    return restaurantId;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public String getReservationId() {
    return reservationId;
  }

  public Integer getGroupSize() {
    return groupSize;
  }

  public LocalDate getDate() {
    return date;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public HashMap<String, String> getCustomer() {
    return customer;
  }
}
