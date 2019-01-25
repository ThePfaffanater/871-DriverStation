package com.team871.util.data;

import javafx.beans.value.ObservableValue;

public interface IData<e> extends ObservableValue<e> {

  /**
   * @returns an e value of the data.
   */
  e getValue();


}
