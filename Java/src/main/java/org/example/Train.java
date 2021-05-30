package org.example;

import lombok.Value;

import java.util.List;

@Value
public class Train {
  List<Seat> seats;

  public long totalCapacity(){
    return seats.size();
  }

  public long bookedSeats(){
    return seats.stream().filter(Seat::isBooked).count();
  }
}
