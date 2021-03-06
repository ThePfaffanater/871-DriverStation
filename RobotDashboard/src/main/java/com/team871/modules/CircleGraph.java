package com.team871.modules;

import com.team871.config.ColorMode;
import com.team871.config.IUpdateable;
import com.team871.util.IData;
import com.team871.util.NumericalDataValue;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.ScaleDirection;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @author Team871-TPfaffe
 * Is a VBOX containing a one dimensional graph
 * Contains various graph templates.
 */
public class CircleGraph extends VBox implements IUpdateable {


    private Gauge gauge;
    private ColorMode colorMode;
    private IData<Double> data;

    /**
     * @param colorMode the color mode settings to be used in this Graph
     * @param data      the data this graph will read from.
     *                  sets the maximum size of this graph to 190 by default
     */
    public CircleGraph(ColorMode colorMode, IData<Double> data) {
        this(colorMode, data, 130, 40);
    }

    /**
     * @param colorMode     the color mode settings to be used in this Graph
     * @param data          the data this graph will read from.
     * @param maxSideLength the maximum size of this graph
     */
    public CircleGraph(ColorMode colorMode, IData<Double> data, double maxSideLength, double minSideLength) {
        this.colorMode = colorMode;
        this.data = data;

        this.gauge = GaugeBuilder
                .create()
                .skinType(SkinType.BAR)
                .title("Null")
                .unit("Null")
                .maxSize(maxSideLength, maxSideLength)
                .minSize(minSideLength, minSideLength)
                .scaleDirection(ScaleDirection.CLOCKWISE)
                .thresholdVisible(true)
                .thresholdColor(Color.YELLOW)
                .threshold(15)
                .checkThreshold(true)
                .minValue(0)
                .maxValue(0)
                .barColor(colorMode.getPrimaryColor())
                .unitColor(colorMode.getSecondaryColor())
                .titleColor(colorMode.getSecondaryColor())
                .valueColor(colorMode.getPrimaryColor())
                .value(0)
                .build();

        this.getChildren().addAll(gauge);
        this.setSpacing(.5);
        this.setAlignment(Pos.CENTER);
    }


    /**
     * For Creating a battery gauge/graphBox (0-100%).
     */
    public void createBatteryRadialGraphBox() {

        gauge.setTitle("Battery");
        gauge.setUnit("%");
        gauge.setScaleDirection(ScaleDirection.CLOCKWISE);
        gauge.setThresholdVisible(true);
        gauge.setThresholdColor(Color.RED);
        gauge.setThreshold(15);
        gauge.setCheckThreshold(true);
        gauge.setMinValue(0);
        gauge.setMaxValue(100);
        gauge.setValue(0);

    }

    /**
     * For Creating custom radial graph.
     *
     * @param title Name Of Gauge
     * @param unit  Unit of measurement
     */
    public void createCustomRadialGraphBox(String title, String unit) {
        Double max = Double.MAX_VALUE;
        Double min = Double.MIN_VALUE;

        if (data instanceof NumericalDataValue) { //don't worry this cast is 'safe'
            max = ((NumericalDataValue) data).getMaxValue();
            min = ((NumericalDataValue) data).getMinValue();
        }

        createCustomRadialGraphBox(title, unit, max, min);
    }

    /**
     * For Creating custom radial graph.
     * @param title Name Of Gauge
     * @param unit	Unit of measurement
     * @param maxVal Maximum Value Of Gauge
     * @param minVal Minimum Value Of Gauge
     */
    public void createCustomRadialGraphBox(String title, String unit, double maxVal, double minVal) {

        gauge.setTitle(title);
        gauge.setUnit(unit);
        gauge.setScaleDirection(ScaleDirection.CLOCKWISE);
        gauge.setThresholdVisible(true);
        gauge.setThresholdColor(Color.YELLOW);
        gauge.setThreshold(15);
        gauge.setCheckThreshold(true);
        gauge.setMinValue(minVal);
        gauge.setMaxValue(maxVal);
        gauge.setValue(0);

    }

    @Override
    public void update() {
        gauge.setBarColor(colorMode.getPrimaryColor());
        gauge.setTitleColor(colorMode.getSecondaryColor());
        gauge.setValueColor(colorMode.getSecondaryColor());
        gauge.setValue(data.get());

    }
}
