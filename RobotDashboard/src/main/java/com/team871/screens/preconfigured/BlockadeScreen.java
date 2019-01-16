package com.team871.screens.preconfigured;

import com.team871.config.ColorMode;
import com.team871.config.IDashboardConfig;
import com.team871.config.network.BlockadeNetConfig;
import com.team871.modules.*;
import com.team871.screens.IScreen;
import com.team871.util.BinaryDataValue;
import com.team871.util.NetNumericalDataValue;
import com.team871.util.NetStringDataValue;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;


/**
 * @author Team871-TPfaffe
 * Testing dashboard for new additions.
 */
public class BlockadeScreen implements IScreen {

  private final int MIN_WIDTH;
  private final int MIN_HEIGHT;
  private NetworkTableInstance networkTable;
  private BlockadeNetConfig netConf;
  private ColorMode colorMode;

  //Driver Screen
  private GridPane driveScreenRoot;

  private VBox lifterPane;
  private BinaryIndicator cubeGrabIndicator;
  private CircleGraph cubeHeight;
  private StringDisplay liftMode;

  private VBox robotPane;
  private CircleGraph gyroHeading;
  private NumberDisplay robotTilt;
  private StringDisplay driveMode;

  private LedController ledController;


  public BlockadeScreen(IDashboardConfig config) {
    colorMode = config.getColorMode();
    networkTable = config.getNetworkTableInstance();
    netConf = new BlockadeNetConfig(networkTable);
    MIN_HEIGHT = config.getInitialHeight();
    MIN_WIDTH = config.getInitialWidth();
  }


  public GridPane getScreen() {

    lifterPane = new VBox();

    BinaryDataValue cubeStatusVal = new BinaryDataValue(true);
    cubeGrabIndicator = new BinaryIndicator(colorMode, cubeStatusVal, "Cube Status");

    NetNumericalDataValue cubeHeightVal = new NetNumericalDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.UHEIGHT_KEY));//TODO: make it so it can either read Upper & Lower Heights and combine them or have the robot send 1 data value
    cubeHeight = new CircleGraph(colorMode, cubeHeightVal);
    cubeHeight.createCustomRadialGraphBox("CUBE HEIGHT", "in", 29, -1);

    NetStringDataValue liftModeVal = new NetStringDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.LIFT_MODE_KEY));
    liftMode = new StringDisplay(colorMode, liftModeVal, "Lift Mode");

    lifterPane.getChildren().addAll(cubeHeight, liftMode, cubeGrabIndicator);
    lifterPane.setPadding(new Insets(3, 3, 3, 3));
    lifterPane.setAlignment(Pos.CENTER_LEFT);


    robotPane = new VBox();

    NetNumericalDataValue gyroHeadingVal = new NetNumericalDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.ANGLE_KEY), 360, -360, 360, 0);
    gyroHeading = new CircleGraph(colorMode, gyroHeadingVal);
    gyroHeading.createCustomRadialGraphBox("Heading", "deg\u00B0");

    NetNumericalDataValue robotTiltVal = new NetNumericalDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.TILT_KEY), 360, 0);
    robotTilt = new NumberDisplay(colorMode, robotTiltVal, "Robot Tilt");

    NetStringDataValue driveModeVal = new NetStringDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.DRIVE_MODE_KEY));
    driveMode = new StringDisplay(colorMode, driveModeVal, "Drive Mode");

    robotPane.getChildren().addAll(gyroHeading, robotTilt, driveMode);
    robotPane.setPadding(new Insets(3, 3, 3, 3));
    robotPane.setAlignment(Pos.TOP_LEFT);

    ledController = new LedController(colorMode, "LED Settings", networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.BRIGHTNESS_KEY), networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.LED_COLOR_KEY));

    CameraBox cameraBox = new CameraBox(480, 720);

    ColorModePicker colorModePicker = new ColorModePicker(colorMode);

    driveScreenRoot = new GridPane();
    driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
    driveScreenRoot.setGridLinesVisible(true);
    driveScreenRoot.add(lifterPane, 0, 0);
    driveScreenRoot.add(robotPane, 1, 0);
    driveScreenRoot.add(ledController, 2, 0);
    driveScreenRoot.add(colorModePicker, 3, 0);
    driveScreenRoot.add(cameraBox, 0, 4);
    driveScreenRoot.setMinSize(MIN_WIDTH, MIN_HEIGHT);
    return driveScreenRoot;
  }

  public void update() {
    driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

    cubeGrabIndicator.update();
    cubeHeight.update();
    liftMode.update();

    gyroHeading.update();
    robotTilt.update();
    driveMode.update();

    ledController.update();
  }

}
