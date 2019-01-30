package com.team871.modules;

import com.team871.config.Style.ColorMode;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LedController extends VBox {

    private ColorMode colorMode;
    private NetworkTableEntry ledBrightnessKey;
    private NetworkTableEntry ledColorKey;
    private Label titleText;
    private Slider brightnessSlider;
    private ColorPicker colorPicker;
    private boolean init;
    private boolean lastDarkMode;

    public LedController() {
        titleText = new Label("not Initialized: ");
        brightnessSlider = new Slider();
        brightnessSlider.getStylesheets().add("slider_dark.css");
        colorPicker = new ColorPicker();
        colorPicker.setScaleX(.75);
        colorPicker.setScaleY(.75);
        colorPicker.setScaleZ(.75);

        this.getChildren().addAll(titleText, brightnessSlider, colorPicker);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(3, 3, 3, 3));
    }

    @FXML
    public void initialize() {
        titleText = new Label("not Initialized: ");
        brightnessSlider = new Slider();
        brightnessSlider.getStylesheets().add("slider_dark.css");
        colorPicker = new ColorPicker();


        this.getChildren().addAll(titleText, brightnessSlider, colorPicker);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(3, 3, 3, 3));
    }

    public void initialize(ColorMode colorMode, String title, NetworkTableEntry ledBrightnessKey, NetworkTableEntry ledColorKey) {
        this.colorMode = colorMode;
        this.ledBrightnessKey = ledBrightnessKey;
        this.ledColorKey = ledColorKey;
        this.lastDarkMode = !colorMode.isDarkMode();
        this.init = false;

        titleText.setText(title + ":");

        colorPicker.setOnAction((ActionEvent event) -> {
            ledColorKey.setString(colorPicker.getValue().toString());
            System.out.println(colorPicker.getValue().toString());
        });

        //Updates:
        brightnessSlider.valueProperty().addListener((observable, old, newValue) -> {
            if (ledBrightnessKey != null) {
                ledBrightnessKey.setNumber(newValue.intValue());
            }
        });

        colorMode.addListener(observable -> {
            titleText.setTextFill(colorMode.getSecondaryColor());

            if (lastDarkMode != colorMode.isDarkMode()) {
                if (colorMode.isDarkMode()) {
                    brightnessSlider.getStylesheets().add("slider_dark.css");
                } else {
                    brightnessSlider.getStylesheets().add("slider_light.css");
                }

                lastDarkMode = colorMode.isDarkMode();
            }
        });

        ledBrightnessKey.addListener(event -> {
            try {
                brightnessSlider.setValue(event.value.getDouble());
            } catch (ClassCastException e) {
                System.out.println("TableEntry(" + ledBrightnessKey.getInfo() + ") ERROR: " + e.toString());
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        ledColorKey.addListener(event -> {
            try {
                colorPicker.setValue(Color.valueOf(event.value.getString()));
            } catch (ClassCastException e) {
                System.out.println("TableEntry(" + ledColorKey.getInfo() + ") ERROR: " + e.toString());
            }
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    }
}
