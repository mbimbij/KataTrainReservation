package org.example;

public class Coach extends ValueObject<String> {
  public Coach(String value) {
    super(value);
  }

  @Override
  public String toString() {
    return getValue();
  }
}
