package org.example;

import lombok.Value;

@Value
public class Seat {
  Coach coach;
  int seatNumber;
  BookingReference bookingReference;

  public boolean isAvailable() {
    return bookingReference.isBlank();
  }

  public boolean isBooked() {
    return !bookingReference.isBlank();
  }
}