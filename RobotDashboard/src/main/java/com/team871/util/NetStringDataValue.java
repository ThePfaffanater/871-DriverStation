package com.team871.util;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetStringDataValue extends StringDataValue implements IData<String> {

  private NetworkTableEntry tableEntry;
  private boolean init;

  public NetStringDataValue(NetworkTableEntry tableEntry) {
    super();
    this.tableEntry = tableEntry;
    this.init = false;
  }

  @Override
  public String get() {
    if (!init) {
      initialize();
      init = true;
    }

    return super.get();
  }

  private void initialize() {
    tableEntry.addListener(event -> {
      try {
        super.set(event.value.getString());
      } catch (ClassCastException e) {
        System.out.println("TableEntry(" + tableEntry.getInfo() + ") ERROR: " + e.toString());
      }
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
  }
}
