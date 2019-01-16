package com.team871.util;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetBinaryDataValue extends BinaryDataValue implements IData<Boolean> {

  private NetworkTableEntry entry;
  private boolean init;

  /**
   * Will update to this network entry
   *
   * @param entry the value that this data is representing.
   */
  public NetBinaryDataValue(NetworkTableEntry entry) {
    super();
    this.entry = entry;
    init = false;
  }

  @Override
  public Boolean get() {
    if (!init) {
      initialize();
      init = true;
    }

    return super.get();
  }

  private void initialize() {
    entry.addListener(event -> {
      try {
        super.set(event.value.getBoolean());
      } catch (ClassCastException e) {
        System.out.println("Table Entry(" + entry.getInfo() + "): " + e.toString());
      }
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
  }
}
