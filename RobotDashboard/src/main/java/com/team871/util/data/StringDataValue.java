package com.team871.util.data;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;

public class StringDataValue implements IData<String> {

  private String value;

  private List<ChangeListener<? super String>> changeListeners;
  private List<InvalidationListener> invalidationListeners;

  public StringDataValue() {
    this("null");
  }

  public StringDataValue(String value) {
    this.value = value;

    changeListeners = new ArrayList<>();
    invalidationListeners = new ArrayList<>();
  }


  public String getValue() {
    return value;
  }

  protected void set(String newValue) {
    String oldValue = value;
    value = newValue;
    notifyChangeListeners(oldValue);
    notifyInvalidationListeners();
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

  private void notifyChangeListeners(String oldValue) {
    for (ChangeListener<? super String> changeListener : changeListeners) {
      changeListener.changed(this, oldValue, this.value);
    }
  }

  private void notifyInvalidationListeners() {
    for (InvalidationListener changeListener : invalidationListeners) {
      changeListener.invalidated(this);
    }
  }

  @Override
  public void addListener(ChangeListener<? super String> listener) {
    changeListeners.add(listener);
  }

  @Override
  public void addListener(InvalidationListener listener) {
    invalidationListeners.add(listener);
  }

  @Override
  public void removeListener(ChangeListener<? super String> listener) {
    changeListeners.remove(listener);
  }

  @Override
  public void removeListener(InvalidationListener listener) {
    invalidationListeners.remove(listener);
  }
}
