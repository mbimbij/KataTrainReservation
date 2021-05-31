package org.example;

import java.util.Collections;
import java.util.List;

import static org.example.EmptyBookingReference.EMPTY_BOOKING_REFERENCE;

public class TicketOffice {

  private final IProvideTrainData trainDataProvider;
  private final IProvideBookingReference bookingReferenceProvider;

  public TicketOffice(IProvideTrainData trainDataProvider, IProvideBookingReference bookingReferences) {
    this.trainDataProvider = trainDataProvider;
    this.bookingReferenceProvider = bookingReferences;
  }

  public Reservation makeReservation(ReservationRequest reservationRequest) {
    Train train = trainDataProvider.provideTrainData();
    List<Seat> seatsForReservation = train.getSeatsForReservationIfPossible(reservationRequest);

    if(seatsForReservation.isEmpty()){
      return new Reservation(reservationRequest.trainId, Collections.emptyList(), EMPTY_BOOKING_REFERENCE.getValue());
    }

    return new Reservation(reservationRequest.trainId, seatsForReservation, bookingReferenceProvider.provideBookingReference().getValue());
  }

}