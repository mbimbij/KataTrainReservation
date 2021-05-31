package org.example;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class ValueObject<T> {
  private final T value;

  protected ValueObject(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }
}
