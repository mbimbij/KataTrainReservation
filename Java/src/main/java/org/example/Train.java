package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Train {
  private List<Seat> seats;

  public Train(List<Seat> seats) {
    this.seats = seats;
  }

  private long countBookedSeats(Map.Entry<Coach, List<Seat>> entry) {
    return entry.getValue().stream().filter(Seat::isBooked).count();
  }

  public long totalCapacity() {
    return seats.size();
  }

  public long bookedSeats() {
    return seats.stream().filter(Seat::isBooked).count();
  }


  public Map<Coach, List<Seat>> getSeatsGroupedByCoachSortedBySeatsNumberDescending() {
    return seats.stream()
        .collect(Collectors.groupingBy(Seat::getCoach))
        .entrySet()
        .stream()
        .sorted(Comparator.comparingLong(this::countBookedSeats).reversed())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1, e2) -> e1, LinkedHashMap::new));
  }

  List<Seat> getSeatsForReservationIfPossible(ReservationRequest reservationRequest) {
    int reservationSeatCount = reservationRequest.seatCount;

    if (totalReservationExceedsThreshold(this, reservationRequest)) {
      return Collections.emptyList();
    }

    List<Seat> bookableSeats = Collections.emptyList();
    for (List<Seat> seatsPerCoach : getSeatsGroupedByCoachSortedBySeatsNumberDescending().values()) {
      long availableSeatsCount = seatsPerCoach.stream().filter(Seat::isAvailable).count();
      if (availableSeatsCount >= reservationRequest.seatCount && coachReservationExceedsThreshold(reservationRequest, seatsPerCoach)) {
        return bookSeatsInCoach(seatsPerCoach, reservationSeatCount);
      } else if (availableSeatsCount >= reservationRequest.seatCount && bookableSeats.isEmpty()) {
        bookableSeats = seatsPerCoach;
      }
    }

    return bookSeatsInCoach(bookableSeats, reservationSeatCount);
  }

  private List<Seat> bookSeatsInCoach(List<Seat> bookableSeats, int reservationSeatCount) {
    return bookableSeats.stream().filter(Seat::isAvailable).limit(reservationSeatCount).collect(Collectors.toList());
  }

  private boolean coachReservationExceedsThreshold(ReservationRequest reservationRequest, List<Seat> seatsPerCoach) {
    long alreadyBookedSeatsCount = seatsPerCoach.stream().filter(Seat::isBooked).count();
    int totalSeatsCount = seatsPerCoach.size();
    return alreadyBookedSeatsCount + reservationRequest.seatCount <= 0.7 * totalSeatsCount;
  }

  private boolean totalReservationExceedsThreshold(Train train, ReservationRequest reservationRequest) {
    return train.bookedSeats() + reservationRequest.seatCount > train.totalCapacity() * 0.7;
  }
}
