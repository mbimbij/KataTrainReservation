package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class TicketOfficeTest {

  private IProvideTrainData trainDataProvider;
  private IProvideBookingReference bookingReferenceProvider;
  private TicketOffice ticketOffice;
  private final BookingReference bookingReference = new BookingReference("someBookingReference");
  private EmptyBookingReference emptyBookingReference = EmptyBookingReference.instance;

  @BeforeEach
  void setUp() {
    initMockBookingReferenceProvider();
    initAMockTrainDataProvider_returningATrain_with1Coach_and10TotalSeats_and1SeatBooked();
    ticketOffice = new TicketOffice(trainDataProvider, bookingReferenceProvider);
  }

  private void initAMockTrainDataProvider_returningATrain_with1Coach_and10TotalSeats_and1SeatBooked() {
    Coach coach = new Coach("A");
    List<Seat> seats = Arrays.asList(
        new Seat(coach, 1, bookingReference),
        new Seat(coach, 2, emptyBookingReference),
        new Seat(coach, 3, emptyBookingReference),
        new Seat(coach, 4, emptyBookingReference),
        new Seat(coach, 5, emptyBookingReference),
        new Seat(coach, 6, emptyBookingReference),
        new Seat(coach, 7, emptyBookingReference),
        new Seat(coach, 8, emptyBookingReference),
        new Seat(coach, 9, emptyBookingReference),
        new Seat(coach, 10, emptyBookingReference)
    );
    Train train = new Train(seats);
    doReturn(train).when(trainDataProvider).provideTrainData();
  }

  private void initMockBookingReferenceProvider() {
    trainDataProvider = mock(IProvideTrainData.class);
    bookingReferenceProvider = mock(IProvideBookingReference.class);
    doReturn(bookingReference).when(bookingReferenceProvider).provideBookingReference();
  }

  @Test
  public void givenATrainWith1Coach_and10TotalSeatsAvailable_and1SeatAlreadyBooked_whenBook3Seats_thenBookingOK() {
    // GIVEN
    ReservationRequest reservationRequest = new ReservationRequest("someTrainId", 3);

    // WHEN
    Reservation reservation = ticketOffice.makeReservation(reservationRequest);

    // THEN
    assertSoftly(softAssertions -> {
      softAssertions.assertThat(reservation.bookingId).isNotBlank();
      softAssertions.assertThat(reservation.trainId).isNotBlank();
      softAssertions.assertThat(reservation.seats).hasSize(3);
    });
  }

  @Test
  public void givenATrainWith1Coach_and10TotalSeatsAvailable_and1SeatAlreadyBooked_whenBook8Seats_thenBookingKO() {
    // GIVEN
    ReservationRequest reservationRequest = new ReservationRequest("someTrainId", 8);

    // WHEN
    Reservation reservation = ticketOffice.makeReservation(reservationRequest);

    // THEN
    assertSoftly(softAssertions -> {
      softAssertions.assertThat(reservation.bookingId).isBlank();
      softAssertions.assertThat(reservation.trainId).isNotBlank();
      softAssertions.assertThat(reservation.seats).isEmpty();
    });
  }

}
