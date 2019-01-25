package com.team871.util.data;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NetNumericalDataValue extends NumericalDataValue implements IData<Double> {

    private NetworkTableEntry tableEntry;

    /**
     * @param tableEntry the networked value that this dataValue will read from.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     * @param normalMax will normalize the dataValue to this value as maximum.
     * @param normalMin will normalize the dataValue to this value as minimum.
     */
    public NetNumericalDataValue(NetworkTableEntry tableEntry, double maxValue, double minValue, int normalMax, int normalMin){
        super(maxValue, minValue, normalMax, normalMin);

        this.tableEntry = tableEntry;
        initialize();
    }
    /**
     * @param tableEntry the networked value that this dataValue will read from.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     */
    public NetNumericalDataValue(NetworkTableEntry tableEntry, double maxValue, double minValue) {
        super(maxValue, minValue);

        this.tableEntry = tableEntry;
        initialize();
    }

    /**
     *  @param tableEntry the networked value that this dataValue will read from.
     */
    public NetNumericalDataValue(NetworkTableEntry tableEntry) {
        super();
        this.tableEntry = tableEntry;
        initialize();
    }

    private void initialize() {
        tableEntry.addListener(event -> {
            try {
                super.set(event.value.getDouble());
            } catch (ClassCastException e) {
                System.out.println("Table Entry(" + tableEntry.getInfo() + "): " + e.toString());
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }
}
