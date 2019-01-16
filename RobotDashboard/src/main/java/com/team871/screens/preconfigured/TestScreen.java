package com.team871.screens.preconfigured;

import com.team871.config.ColorMode;
import com.team871.config.IDashboardConfig;
import com.team871.config.IUpdateable;
import com.team871.modules.BinaryIndicator;
import com.team871.modules.CameraBox;
import com.team871.modules.CircleGraph;
import com.team871.modules.ColorModePicker;
import com.team871.util.BinaryDataValue;
import com.team871.util.NumericalDataValue;
import edu.wpi.first.networktables.NetworkTableInstance;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;


/**
 * @author Team871-TPfaffe
 * Testing dashboard for new additions.
 */
public class TestScreen implements IUpdateable {

    private final int MIN_WIDTH;
    private ColorMode colorMode;
    private final int MIN_HEIGHT;
    private NetworkTableInstance networkTable;

    //Driver Screen
    private GridPane driveScreenRoot;
    private GridPane gaugePane;
    private GridPane indicatorPane;
    private BinaryIndicator indicator;
    private CircleGraph batteryGauge;
    private CircleGraph motorGauge;
    private CameraBox videoFeed;

    private NumericalDataValue batteryVal;

    TestScreen(IDashboardConfig config) {
        colorMode = config.getColorMode();
        networkTable = config.getNetworkTableInstance();
        MIN_HEIGHT = config.getInitialHeight();
        MIN_WIDTH = config.getInitialWidth();
    }


    GridPane getDriverScreen() {

        BinaryDataValue indicatorVal = new BinaryDataValue(true);
        indicator = new BinaryIndicator(colorMode, indicatorVal, "Test");

        batteryVal = new NumericalDataValue(new Double(99), 100, 0);
        batteryGauge = new CircleGraph(colorMode, batteryVal);
        batteryGauge.createBatteryRadialGraphBox();


        NumericalDataValue motorVal = new NumericalDataValue(new Double(22));
        motorGauge = new CircleGraph(colorMode, motorVal);
        motorGauge.createCustomRadialGraphBox("Motor", "m/s", 100, 0);

        videoFeed = new CameraBox(480, 480);


        gaugePane = new GridPane();
        gaugePane.setPadding(new Insets(20));
        gaugePane.setHgap(10);
        gaugePane.setGridLinesVisible(false);
        gaugePane.setVgap(15);
        gaugePane.add(batteryGauge, 0, 0);
        gaugePane.add(motorGauge, 0, 2);


        indicatorPane = new GridPane();
        indicatorPane.setPadding(new Insets(20));
        indicatorPane.setHgap(11);
        indicatorPane.setGridLinesVisible(false);
        indicatorPane.setVgap(16);

        indicatorPane.add(indicator, 0, 0);

        ColorModePicker colorPicker = new ColorModePicker(colorMode);

        driveScreenRoot = new GridPane();
        driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        driveScreenRoot.setGridLinesVisible(true);
        driveScreenRoot.add(gaugePane, 0, 0);
        driveScreenRoot.add(indicatorPane, 0, 1);
        driveScreenRoot.add(colorPicker, 0, 2);
        driveScreenRoot.add(videoFeed, 1, 0);

        driveScreenRoot.setMinSize(MIN_WIDTH, MIN_HEIGHT);
        return driveScreenRoot;
    }

    @Override
    public void update() {
        batteryVal.changeBy(-0.05);
        driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        indicator.update();
        motorGauge.update();
        batteryGauge.update();

    }
}
