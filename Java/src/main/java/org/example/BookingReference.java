package org.example;

import org.apache.commons.lang3.StringUtils;

public class BookingReference extends ValueObject<String> {
  public BookingReference(String value) {
    super(value);
  }

  public boolean isBlank() {
    return StringUtils.isBlank(getValue());
  }
}
