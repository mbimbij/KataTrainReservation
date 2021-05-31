package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class TicketOfficeTest {

  private IProvideTrainData trainDataProvider1Coach;
  private IProvideBookingReference bookingReferenceProvider;
  private TicketOffice ticketOffice;
  private final BookingReference bookingReference = new BookingReference("someBookingReference");
  private EmptyBookingReference emptyBookingReference = EmptyBookingReference.EMPTY_BOOKING_REFERENCE;

  @BeforeEach
  void setUp() {
    bookingReferenceProvider = initMockBookingReferenceProvider();
    trainDataProvider1Coach = initAMockTrainDataProvider_returningATrain_with1Coach_and10TotalSeats_and1SeatBooked();
    ticketOffice = new TicketOffice(trainDataProvider1Coach, bookingReferenceProvider);
  }

  private IProvideTrainData initAMockTrainDataProvider_returningATrain_with1Coach_and10TotalSeats_and1SeatBooked() {
    IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
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
    return trainDataProvider;
  }

  private IProvideBookingReference initMockBookingReferenceProvider() {
    bookingReferenceProvider = mock(IProvideBookingReference.class);
    doReturn(bookingReference).when(bookingReferenceProvider).provideBookingReference();
    return bookingReferenceProvider;
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

  @Test
  public void givenATrainWith2CoachesAandB_and10SeatsPerCoach_and1SeatBookedInCoachA_whenBook7Seats_thenSeatsBookedInCoachB() {
    // GIVEN
    IProvideTrainData iProvideTrainData = initAMockTrainDataProvider_returningATrain_with2CoachesAandB_and10SeatsPerCoach_and1SeatBookedInCoachA();
    TicketOffice ticketOffice = new TicketOffice(iProvideTrainData, bookingReferenceProvider);
    ReservationRequest reservationRequest = new ReservationRequest("someTrainId", 7);
    List<String> expectedBookedSeats = IntStream.range(1,8).mapToObj("%dB"::formatted).collect(Collectors.toList());

    // WHEN
    Reservation reservation = ticketOffice.makeReservation(reservationRequest);

    // THEN
    assertSoftly(softAssertions -> {
      softAssertions.assertThat(reservation.bookingId).isEqualTo(bookingReference.getValue());
      softAssertions.assertThat(reservation.trainId).isNotBlank();
      softAssertions.assertThat(reservation.seats)
          .extracting(seat -> "%d%s".formatted(seat.getSeatNumber(), seat.getCoach().toString()))
          .isEqualTo(expectedBookedSeats);
    });
  }

  private IProvideTrainData initAMockTrainDataProvider_returningATrain_with2CoachesAandB_and10SeatsPerCoach_and1SeatBookedInCoachA() {
    IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
    Coach coachA = new Coach("A");
    Coach coachB = new Coach("B");
    List<Seat> seats = Arrays.asList(
        new Seat(coachA, 1, bookingReference),
        new Seat(coachA, 2, emptyBookingReference),
        new Seat(coachA, 3, emptyBookingReference),
        new Seat(coachA, 4, emptyBookingReference),
        new Seat(coachA, 5, emptyBookingReference),
        new Seat(coachA, 6, emptyBookingReference),
        new Seat(coachA, 7, emptyBookingReference),
        new Seat(coachA, 8, emptyBookingReference),
        new Seat(coachA, 9, emptyBookingReference),
        new Seat(coachA, 10, emptyBookingReference),
        new Seat(coachB, 1, emptyBookingReference),
        new Seat(coachB, 2, emptyBookingReference),
        new Seat(coachB, 3, emptyBookingReference),
        new Seat(coachB, 4, emptyBookingReference),
        new Seat(coachB, 5, emptyBookingReference),
        new Seat(coachB, 6, emptyBookingReference),
        new Seat(coachB, 7, emptyBookingReference),
        new Seat(coachB, 8, emptyBookingReference),
        new Seat(coachB, 9, emptyBookingReference),
        new Seat(coachB, 10, emptyBookingReference)

    );
    Train train = new Train(seats);
    doReturn(train).when(trainDataProvider).provideTrainData();
    return trainDataProvider;
  }

  @Test
  public void givenATrainWith2CoachesAandB_and10SeatsPerCoach_and1SeatBookedInCoachA_and1SeatBookedInCoachB_whenBook7Seats_thenSeatsBookedInCoachA() {
    // GIVEN
    IProvideTrainData iProvideTrainData = initAMockTrainDataProvider_returningATrain_with2CoachesAandB_and10SeatsPerCoach_and1SeatBookedInCoachA_and1SeatBookedInCoachB();
    TicketOffice ticketOffice = new TicketOffice(iProvideTrainData, bookingReferenceProvider);
    ReservationRequest reservationRequest = new ReservationRequest("someTrainId", 7);
    List<String> expectedBookedSeats = IntStream.range(2,9).mapToObj("%dA"::formatted).collect(Collectors.toList());

    // WHEN
    Reservation reservation = ticketOffice.makeReservation(reservationRequest);

    // THEN
    assertSoftly(softAssertions -> {
      softAssertions.assertThat(reservation.bookingId).isEqualTo(bookingReference.getValue());
      softAssertions.assertThat(reservation.trainId).isNotBlank();
      softAssertions.assertThat(reservation.seats)
          .extracting(seat -> "%d%s".formatted(seat.getSeatNumber(), seat.getCoach().toString()))
          .isEqualTo(expectedBookedSeats);
    });
  }

  private IProvideTrainData initAMockTrainDataProvider_returningATrain_with2CoachesAandB_and10SeatsPerCoach_and1SeatBookedInCoachA_and1SeatBookedInCoachB() {
    IProvideTrainData trainDataProvider = mock(IProvideTrainData.class);
    Coach coachA = new Coach("A");
    Coach coachB = new Coach("B");
    List<Seat> seats = Arrays.asList(
        new Seat(coachA, 1, bookingReference),
        new Seat(coachA, 2, emptyBookingReference),
        new Seat(coachA, 3, emptyBookingReference),
        new Seat(coachA, 4, emptyBookingReference),
        new Seat(coachA, 5, emptyBookingReference),
        new Seat(coachA, 6, emptyBookingReference),
        new Seat(coachA, 7, emptyBookingReference),
        new Seat(coachA, 8, emptyBookingReference),
        new Seat(coachA, 9, emptyBookingReference),
        new Seat(coachA, 10, emptyBookingReference),
        new Seat(coachB, 1, bookingReference),
        new Seat(coachB, 2, emptyBookingReference),
        new Seat(coachB, 3, emptyBookingReference),
        new Seat(coachB, 4, emptyBookingReference),
        new Seat(coachB, 5, emptyBookingReference),
        new Seat(coachB, 6, emptyBookingReference),
        new Seat(coachB, 7, emptyBookingReference),
        new Seat(coachB, 8, emptyBookingReference),
        new Seat(coachB, 9, emptyBookingReference),
        new Seat(coachB, 10, emptyBookingReference)

    );
    Train train = new Train(seats);
    doReturn(train).when(trainDataProvider).provideTrainData();
    return trainDataProvider;
  }
}
