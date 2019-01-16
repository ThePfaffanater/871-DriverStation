package com.team871.modules;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.team871.config.ColorMode;
import com.team871.config.IUpdateable;
import edu.wpi.first.networktables.NetworkTableEntry;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LedController extends VBox implements IUpdateable {

    private ColorMode colorMode;
    private NetworkTableEntry ledBrightnessKey;
    private NetworkTableEntry ledColorKey;
    private Label titleText;
    private JFXSlider brightnessSlider;
    private JFXColorPicker colorPicker;
    private boolean init;
    private boolean lastDarkMode;

    public LedController(ColorMode colorMode, String title, NetworkTableEntry ledBrightnessKey, NetworkTableEntry ledColorKey) {
        this.colorMode = colorMode;
        this.ledBrightnessKey = ledBrightnessKey;
        this.ledColorKey = ledColorKey;
        this.lastDarkMode = !colorMode.isDarkMode();
        this.init = false;

        titleText = new Label(title + ": ");
        brightnessSlider = new JFXSlider();
        brightnessSlider.setIndicatorPosition(JFXSlider.IndicatorPosition.LEFT);
        brightnessSlider.getStylesheets().add("slider_dark.css");
        colorPicker = new JFXColorPicker();
        colorPicker.setScaleX(.75);
        colorPicker.setScaleY(.75);
        colorPicker.setScaleZ(.75);

        colorPicker.setOnAction((ActionEvent event) -> {
            ledColorKey.setString(colorPicker.getValue().toString());
            System.out.println(colorPicker.getValue().toString());
        });

        this.getChildren().addAll(titleText, brightnessSlider, colorPicker);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(3, 3, 3, 3));
    }

    @Override
    public void update() {
        if (!init) {
            initialize();
            init = true;
        }

        titleText.setTextFill(colorMode.getSecondaryColor());

        if (lastDarkMode != colorMode.isDarkMode()) {
            if (colorMode.isDarkMode())
                brightnessSlider.getStylesheets().add("slider_dark.css");
            else
                brightnessSlider.getStylesheets().add("slider_light.css");

            lastDarkMode = colorMode.isDarkMode();
        }

    }

    public void initialize() {
        brightnessSlider.valueProperty().addListener((observable, old, newValue) -> {
            if (ledBrightnessKey != null) {
                ledBrightnessKey.setNumber(newValue.intValue());
            }
        });
    }
}
