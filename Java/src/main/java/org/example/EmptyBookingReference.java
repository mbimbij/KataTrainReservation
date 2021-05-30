package org.example;

public class EmptyBookingReference extends BookingReference {
  public static final EmptyBookingReference instance = new EmptyBookingReference();
  private EmptyBookingReference() {
    super("");
  }
}
