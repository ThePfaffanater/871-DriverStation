package com.team871.config.network;

import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * @author TP-Laptop on 1/5/2019.
 * @project Robotics-Dashboard-Code
 */
@SuppressWarnings("ALL")
public class BlockadeNetConfig extends AbstractNetConfig {

  public final String TABLE_KEY = "Dashboard";
  public final String UTICKS_KEY = "UpperLiftTicks";
  public final String LTICKS_KEY = "LowerLiftTicks";
  public final String UHEIGHT_KEY = "UpperLiftHeight";
  public final String LHEIGHT_KEY = "LowerLiftHeight";
  public final String XDISP_KEY = "displacementX";
  public final String YDISP_KEY = "displacementY";
  public final String BRIGHTNESS_KEY = "ledBrightness";
  public final String LED_COLOR_KEY = "ledColor";
  public final String WHITE_BALANCE_KEY = "cameraWhiteBal";
  public final String EXPOSURE_KEY = "cameraExposure";
  public final String CUBE_KEY = "cubeDetect";
  public final String LIFT_MODE_KEY = "liftMode";
  public final String LIFT_SETPOINT_KEY = "liftSetpoint";
  public final String ANGLE_KEY = "gyroAngle";
  public final String TILT_KEY = "gyroTilt";
  public final String DRIVE_MODE_KEY = "driveMode";

  public BlockadeNetConfig(NetworkTableInstance instance) {
    super(instance, "blockade_V1.00");
  }

}
