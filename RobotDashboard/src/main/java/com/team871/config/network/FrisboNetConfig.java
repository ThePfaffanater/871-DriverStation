package com.team871.config.network;

import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * @author TP-Laptop on 1/5/2019.
 * @project Robotics-Dashboard-Code
 */
public class FrisboNetConfig extends AbstractNetConfig {

  public final String ANGLE_KEY = "gyroAngle";
  public final String TILT_KEY = "gyroTilt";
  public final String SHOOTER_UP_KEY = "SHOOTER_UP";
  public final String SHOOTER_DOWN_KEY = "SHOOTER_DOWN";
  public final String V_SPEED_KEY = "V_SPEED";
  public final String LIFT_UP_KEY = "LIFT_UP";
  public final String MECC_DRIVE_KEY = "MECC_DRIVE";

  public FrisboNetConfig(NetworkTableInstance instance) {
    super(instance, "frisbo_V1.00");
  }
}
