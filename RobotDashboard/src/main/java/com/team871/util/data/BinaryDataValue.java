package com.team871.util.data;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;

public class BinaryDataValue implements IData<Boolean> {


  private boolean value;

    private List<ChangeListener<? super Boolean>> changeListeners;
    private List<InvalidationListener> invalidationListeners;

  public BinaryDataValue() {
    this(false);
  }

  public BinaryDataValue(boolean value) {
      changeListeners = new ArrayList<>();
      invalidationListeners = new ArrayList<>();

    set(value);

  }


    public Boolean getValue() {
    return value;
  }

  protected void set(boolean value) {
    this.value = value;
      notifyChangeListeners(!value);
      notifyInvalidationListeners();
  }

  /**
   * This is only for testing purposes and breaks encapsulation
   * so should never be utilized in real builds
   */
  @Deprecated
  public void invert() { //TODO: should probably remove but eh?
      set(!value);
  }

  public String toString() {
      return this.getValue().toString();
  }


    private void notifyChangeListeners(boolean oldValue) {
        for (ChangeListener<? super Boolean> changeListener : changeListeners) {
            changeListener.changed(this, oldValue, this.value);
        }
    }

    private void notifyInvalidationListeners() {
        for (InvalidationListener changeListener : invalidationListeners) {
            changeListener.invalidated(this);
        }
    }


    //Listeners:
    @Override
    public void addListener(ChangeListener<? super Boolean> listener) {
        changeListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Boolean> listener) {
        removeListener(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);
    }


}
