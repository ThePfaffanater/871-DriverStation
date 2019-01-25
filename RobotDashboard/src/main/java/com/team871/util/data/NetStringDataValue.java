package com.team871.util.data;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetStringDataValue extends StringDataValue implements IData<String> {

  private NetworkTableEntry tableEntry;

  public NetStringDataValue(NetworkTableEntry tableEntry) {
    super();
    this.tableEntry = tableEntry;

      //Updates:
    tableEntry.addListener(event -> {
      try {
        super.set(event.value.getString());
      } catch (ClassCastException e) {
        System.out.println("TableEntry(" + tableEntry.getInfo() + ") ERROR: " + e.toString());
      }
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
  }
}
