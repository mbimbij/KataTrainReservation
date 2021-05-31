package org.example;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.EmptyBookingReference.EMPTY_BOOKING_REFERENCE;

class TrainTest {
  @Test
  void getSeatsSortedByBookedSeatsNumberDescending() {
    // GIVEN
    BookingReference bookingReference = new BookingReference("someBookingReference");
    List<Seat> seats = Arrays.asList(
        new Seat(new Coach("A"), 1, bookingReference),
        new Seat(new Coach("A"), 1, bookingReference),
        new Seat(new Coach("A"), 1, bookingReference),
        new Seat(new Coach("A"), 1, EMPTY_BOOKING_REFERENCE),
        new Seat(new Coach("A"), 1, EMPTY_BOOKING_REFERENCE),

        new Seat(new Coach("B"), 1, bookingReference),
        new Seat(new Coach("B"), 1, EMPTY_BOOKING_REFERENCE),
        new Seat(new Coach("B"), 1, EMPTY_BOOKING_REFERENCE),
        new Seat(new Coach("B"), 1, EMPTY_BOOKING_REFERENCE),
        new Seat(new Coach("B"), 1, EMPTY_BOOKING_REFERENCE),

        new Seat(new Coach("C"), 1, bookingReference),
        new Seat(new Coach("C"), 1, bookingReference),
        new Seat(new Coach("C"), 1, bookingReference),
        new Seat(new Coach("C"), 1, bookingReference),
        new Seat(new Coach("C"), 1, bookingReference)
    );
    Train train = new Train(seats);

    // WHEN
    Map<Coach, List<Seat>> seatsGroupedByCoach = train.getSeatsGroupedByCoachSortedBySeatsNumberDescending();

    // THEN
    Set<Coach> coaches = seatsGroupedByCoach.keySet();
    assertThat(coaches).containsSequence(new Coach("C"),new Coach("A"),new Coach("B"));
  }
}