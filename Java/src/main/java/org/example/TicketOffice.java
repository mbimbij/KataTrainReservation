package org.example;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TicketOffice {

  private final IProvideTrainData trainDataProvider;
  private final IProvideBookingReference bookingReferenceProvider;

  public TicketOffice(IProvideTrainData trainDataProvider, IProvideBookingReference bookingReferences) {
    this.trainDataProvider = trainDataProvider;
    this.bookingReferenceProvider = bookingReferences;
  }

  public Reservation makeReservation(ReservationRequest reservationRequest) {
    Train train = trainDataProvider.provideTrainData();

    if (totalReservationExceeds70Percents(train, reservationRequest)) {
      return new Reservation(reservationRequest.trainId, Collections.emptyList(), EmptyBookingReference.instance.getValue());
    }

    List<Seat> bookableSeats = train.getSeats()
        .stream()
        .filter(Seat::isAvailable)
        .limit(reservationRequest.seatCount)
        .collect(Collectors.toList());
    return new Reservation(reservationRequest.trainId, bookableSeats, bookingReferenceProvider.provideBookingReference().getValue());
  }

  private boolean totalReservationExceeds70Percents(Train train, ReservationRequest reservationRequest) {
    return train.bookedSeats() + reservationRequest.seatCount > train.totalCapacity() * 0.7;
  }

}