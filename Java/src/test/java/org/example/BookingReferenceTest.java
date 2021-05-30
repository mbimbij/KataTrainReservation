package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BookingReferenceTest {
  @Test
  void twoBookingReferencesWithSameValue_shouldBeEqual() {
    BookingReference bookingReference1 = new BookingReference("ref");
    BookingReference bookingReference2 = new BookingReference("ref");

    Assertions.assertThat(bookingReference1).isEqualTo(bookingReference2);
  }
}