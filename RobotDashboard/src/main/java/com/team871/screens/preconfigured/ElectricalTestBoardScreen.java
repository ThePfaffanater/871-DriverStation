package com.team871.screens.preconfigured;

import com.team871.config.ColorMode;
import com.team871.config.IDashboardConfig;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.CircleGraph;
import com.team871.screens.IScreen;
import com.team871.util.NetBinaryDataValue;
import com.team871.util.NetNumericalDataValue;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

/**
 * @author TP-Laptop on 12/11/2018.
 * @project Robotics-Dashboard-Code
 */
public class ElectricalTestBoardScreen implements IScreen {

  private final int MIN_WIDTH;
  private final int MIN_HEIGHT;
  private final String TABLE_KEY = "Dashboard";
  private final String SWITCH0 = "SWITCH0";
  private final String SWITCH1 = "SWITCH1";
  private final String POT = "POTENTIOMETER";
  private final String ENCODER = "ENCODER";
  BinaryIndicator switch0Indicator;
  BinaryIndicator switch1Indicator;
  CircleGraph potIndicator;
  CircleGraph encoderIndicator;
  private NetworkTableInstance networkTable;
  private ColorMode colorMode;
  //Driver Screen
  private GridPane driveScreenRoot;
  private VBox robotPane;


  public ElectricalTestBoardScreen(IDashboardConfig config) {
    colorMode = config.getColorMode();
    networkTable = config.getNetworkTableInstance();
    MIN_HEIGHT = config.getInitialHeight();
    MIN_WIDTH = config.getInitialWidth();
  }


  public GridPane getScreen() {

    robotPane = new VBox();

    NetBinaryDataValue switch0Val = new NetBinaryDataValue(networkTable.getTable(TABLE_KEY).getEntry(SWITCH0));
    switch0Indicator = new BinaryIndicator(colorMode, switch0Val, "Switch 0");

    NetBinaryDataValue switch1Val = new NetBinaryDataValue(networkTable.getTable(TABLE_KEY).getEntry(SWITCH1));
    switch1Indicator = new BinaryIndicator(colorMode, switch1Val, "Switch 1");

    NetNumericalDataValue potVal = new NetNumericalDataValue(networkTable.getTable(TABLE_KEY).getEntry(POT));
    potIndicator = new CircleGraph(colorMode, potVal);
    potIndicator.createCustomRadialGraphBox("POT", " ");

    NetNumericalDataValue encoderVal = new NetNumericalDataValue(networkTable.getTable(TABLE_KEY).getEntry(ENCODER));
    encoderIndicator = new CircleGraph(colorMode, encoderVal);
    encoderIndicator.createCustomRadialGraphBox("Encoder", "clicks");

    robotPane.getChildren().addAll(switch0Indicator, switch1Indicator, potIndicator, encoderIndicator);
    robotPane.setPadding(new Insets(3, 3, 3, 3));
    robotPane.setAlignment(Pos.TOP_LEFT);


    driveScreenRoot = new GridPane();
    driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
    driveScreenRoot.setGridLinesVisible(true);
    driveScreenRoot.add(robotPane, 1, 0);
    driveScreenRoot.setMinSize(MIN_WIDTH, MIN_HEIGHT);
    return driveScreenRoot;
  }

  @Override
  public void update() {
    driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
    switch0Indicator.update();
    switch1Indicator.update();
    potIndicator.update();
    encoderIndicator.update();

  }

}
