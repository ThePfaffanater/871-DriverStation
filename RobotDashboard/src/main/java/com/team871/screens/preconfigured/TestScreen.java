package com.team871.screens.preconfigured;

import com.team871.config.IDashboardConfig;
import com.team871.config.Style.ColorMode;
import com.team871.config.Style.ColorModeController;
import com.team871.modules.*;
import com.team871.screens.IScreen;
import com.team871.util.data.BinaryDataValue;
import com.team871.util.data.NumericalDataValue;
import com.team871.util.data.StringDataValue;
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
public class TestScreen implements IScreen {

    private final int MIN_WIDTH;
    private ColorMode colorMode;
    private final int MIN_HEIGHT;
    private NetworkTableInstance networkTable;

    //Driver Screen
    private GridPane driveScreenRoot;
    private GridPane gaugePane;
    private GridPane indicatorPane;
    private BinaryIndicator indicator;
    private BinaryIndicator indicator1;
    private CircleGraph batteryGauge;
    private CircleGraph motorGauge;
    private CameraBox videoFeed;

    private NumericalDataValue batteryVal;

    public TestScreen(IDashboardConfig config) {
        colorMode = config.getColorMode();
        networkTable = config.getNetworkTableInstance();
        MIN_HEIGHT = config.getInitialHeight();
        MIN_WIDTH = config.getInitialWidth();
    }


    public GridPane getScreen() {

        BinaryDataValue indicatorVal = new BinaryDataValue(true);
        indicator = new BinaryIndicator(colorMode, indicatorVal, "Test");
        indicator1 = new BinaryIndicator(colorMode, indicatorVal, "Test");

        batteryVal = new NumericalDataValue(new Double(99), 100, 0);
        batteryGauge = new CircleGraph(colorMode, batteryVal);
        batteryGauge.createBatteryRadialGraphBox();


        NumericalDataValue motorVal = new NumericalDataValue(new Double(22));
        motorGauge = new CircleGraph(colorMode, motorVal);
        motorGauge.createCustomRadialGraphBox("Motor", "m/s", 100, 0);

        //videoFeed = new CameraBox(480, 480);


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
        indicatorPane.add(indicator1, 0, 1);

        ColorModePicker colorPicker = new ColorModePicker(new ColorModeController(colorMode));
        LedController ledController = new LedController(colorMode, "LED Settings", networkTable.getTable("NULL").getEntry("NULL"), networkTable.getTable("NULL").getEntry("NULL"));
        StringDisplay stringDisplay = new StringDisplay(colorMode, new StringDataValue("This is only a test"), "Message");
        NumberDisplay numberDisplay = new NumberDisplay(colorMode, new NumericalDataValue(42.), "Number");

        driveScreenRoot = new GridPane();
        driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        driveScreenRoot.setGridLinesVisible(true);
        driveScreenRoot.add(gaugePane, 0, 0);
        driveScreenRoot.add(indicatorPane, 0, 1);
        driveScreenRoot.add(colorPicker, 0, 2);
        driveScreenRoot.add(stringDisplay, 1, 0);
        driveScreenRoot.add(numberDisplay, 1, 1);
        driveScreenRoot.add(ledController, 1, 2);

        //driveScreenRoot.add(videoFeed, 2, 0);

        driveScreenRoot.setMinSize(MIN_WIDTH, MIN_HEIGHT);

        colorMode.addListener(observable -> {
            driveScreenRoot.setBackground(new Background(new BackgroundFill(colorMode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        return driveScreenRoot;
    }

    public void update() {
        batteryVal.changeBy(-0.05);

//        indicator.update();
//        motorGauge.update();
//        batteryGauge.update();

    }

}
