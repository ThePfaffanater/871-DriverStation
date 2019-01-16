package com.team871.screens.preconfigured;

import com.team871.config.ColorMode;
import com.team871.config.IDashboardConfig;
import com.team871.config.IUpdateable;
import com.team871.config.network.FrisboNetConfig;
import com.team871.modules.*;
import com.team871.util.NetBinaryDataValue;
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
public class FrisboScreen implements IUpdateable {

  private final int MIN_WIDTH;
  private final int MIN_HEIGHT;

  private NetworkTableInstance networkTable;
  private ColorMode colorMode;

  //Driver Screen
  private GridPane driveScreenRoot;

  private CircleGraph gyroHeading;
  private NumberDisplay robotTilt;
  private StringDisplay driveMode;
  private BinaryIndicator shooterUpDisplay;
  private BinaryIndicator liftUpDisplay;

  private FrisboNetConfig netConf;

  public FrisboScreen(IDashboardConfig config) {
    colorMode = config.getColorMode();
    networkTable = config.getNetworkTableInstance();
    netConf = new FrisboNetConfig(networkTable);
    MIN_HEIGHT = config.getInitialHeight();
    MIN_WIDTH = config.getInitialWidth();
  }

  public GridPane getScreen() {

    VBox robotPane = new VBox();

    NetNumericalDataValue gyroHeadingVal = new NetNumericalDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.ANGLE_KEY), 360, -360, 360, 0);
    gyroHeading = new CircleGraph(colorMode, gyroHeadingVal);
    gyroHeading.createCustomRadialGraphBox("Heading", "deg \u00B0");

    NetNumericalDataValue robotTiltVal = new NetNumericalDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.TILT_KEY), 360, 0);
    robotTilt = new NumberDisplay(colorMode, robotTiltVal, "Robot Tilt");

    NetStringDataValue driveModeVal = new NetStringDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.MECC_DRIVE_KEY));
    driveMode = new StringDisplay(colorMode, driveModeVal, "Drive Mode");

    NetBinaryDataValue shooterUpVal = new NetBinaryDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.SHOOTER_UP_KEY));
    shooterUpDisplay = new BinaryIndicator(colorMode, shooterUpVal, "ShooterIsUp");

    NetBinaryDataValue liftUpVal = new NetBinaryDataValue(networkTable.getTable(netConf.TABLE_KEY).getEntry(netConf.LIFT_UP_KEY));
    liftUpDisplay = new BinaryIndicator(colorMode, liftUpVal, "LifterIsUp");


    robotPane.getChildren().addAll(gyroHeading, robotTilt, driveMode, shooterUpDisplay, liftUpDisplay);
    robotPane.setPadding(new Insets(3, 3, 3, 3));
    robotPane.setAlignment(Pos.TOP_LEFT);


    driveScreenRoot = new GridPane();
    driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
    driveScreenRoot.setGridLinesVisible(true);
    driveScreenRoot.add(robotPane, 1, 0);
    driveScreenRoot.add(new ColorModePicker(colorMode), 1, 1);
    driveScreenRoot.setMinSize(MIN_WIDTH, MIN_HEIGHT);
    return driveScreenRoot;
  }

  @Override
  public void update() {
    driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

    gyroHeading.update();
    robotTilt.update();
    driveMode.update();
    liftUpDisplay.update();
    shooterUpDisplay.update();
  }

}
