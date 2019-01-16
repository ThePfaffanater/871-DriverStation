package com.team871.util;

public class StringDataValue implements IData<String> {

  private String value;

  public StringDataValue() {
    this("null");
  }

  public StringDataValue(String value) {
    this.value = value;
  }

  @Override
  public String get() {
    return value;
  }

  protected void set(String newValue) {
    this.value = newValue;
  }

  /**
   * This is only for testing purposes and breaks encapsulation
   * so should never be utilized in real builds
   *
   * @param newValue the string this object will now represent
   */
  @Deprecated
  public void changeTo(String newValue) {
    this.value = newValue;
  }

}
