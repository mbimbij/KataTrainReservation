package org.example;

public class EmptyBookingReference extends BookingReference {
  public static final EmptyBookingReference EMPTY_BOOKING_REFERENCE = new EmptyBookingReference();
  private EmptyBookingReference() {
    super("");
  }
}
