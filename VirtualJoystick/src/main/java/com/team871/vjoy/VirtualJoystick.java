package com.team871.vjoy;

import com.team871.jvjoyinterface.VJoy;
import com.team871.jvjoyinterface.VjdStat;

public class VirtualJoystick extends VJoy {

    public boolean connect() {

        int rID = getRID();

//		assertTrue(vJoyEnabled());
        if (!vJoyEnabled()) {
            System.out.println("vJoy driver not enabled: Failed Getting vJoy attributes.");

            return false;
        } else {
            System.out.println("Vender: " + getvJoyManufacturerString());
            System.out.println("Product: " + getvJoyProductString());
            System.out.println("Version Number: " + getvJoyVersion());
        }

//		assertTrue(driverMatch());
        if (driverMatch()) {
            System.out.println("Version of Driver Matches DLL Version {0}");
        } else {
            System.out.println("Version of Driver {0} does NOT match DLL Version {1}");

            return false;
        }

        VjdStat status = getVJDStatus(rID);
        if ((status == VjdStat.VJD_STAT_OWN) || ((status == VjdStat.VJD_STAT_FREE) && (!acquireVJD(rID)))) {
            System.out.println("Failed to acquire vJoy device number " + rID);

            return false;
        } else {
            System.out.println("Acquired: vJoy device number " + rID);
        }

        System.out.println("Number of buttons: " + getVJDButtonNumber(rID));

        return true;

    }

    public int getRID() {
        return 1;
    }

}
