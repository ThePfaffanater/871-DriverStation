package com.team871.util;

public class BinaryDataValue implements IData<Boolean> {

  private boolean value;

  public BinaryDataValue() {
    this(false);
  }

  public BinaryDataValue(boolean value) {
    set(value);
  }


  @Override
  public Boolean get() {
    return value;
  }

  protected void set(boolean value) {
    this.value = value;
  }

  /**
   * This is only for testing purposes and breaks encapsulation
   * so should never be utilized in real builds
   */
  @Deprecated
  public void invert() { //TODO: should probably remove but eh?
    this.value = !value;
  }

  public String toString() {
    return this.get().toString();
  }
}
