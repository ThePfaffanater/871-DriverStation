package com.team871.config.network;

/**
 * @author TP-Laptop on 1/5/2019.
 * @project Robotics-Dashboard-Code
 */

import edu.wpi.first.networktables.NetworkTableInstance;

public abstract class AbstractNetConfig {


  public final String SERVER_VERSION_KEY = "SERVER_VERSION";
  public final String CLIENT_VERSION_KEY = "CLIENT_VERSION";
  public final String TABLE_KEY = "Dashboard";


  public AbstractNetConfig(NetworkTableInstance instance, String VERSION_VAL) {
    instance.setNetworkIdentity(TABLE_KEY);

    instance.getEntry(CLIENT_VERSION_KEY).setString(VERSION_VAL);

    if (!instance.getEntry(SERVER_VERSION_KEY).getString("null").equals(VERSION_VAL)) {
      System.out.println("WARNING!! NetworkTableKey Versions are not the same. \nThis may make the robot and the dashboard incompatible.");
    }
    System.out.println();

  }


}

