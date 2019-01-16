package com.team871.modules;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXRadioButton;
import com.team871.config.ColorMode;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ColorModePicker extends HBox {

    JFXColorPicker colorPicker;
    JFXRadioButton darkModeBtn;

    public ColorModePicker(ColorMode colorMode) {
        super();
        colorPicker = new JFXColorPicker();
        darkModeBtn = new JFXRadioButton();
        //darkModeBtn.setText("Light/Dark Mode");

        darkModeBtn.setOnAction((ActionEvent event) -> {
            colorMode.changeColorMode();
        });

        colorPicker.setOnAction((ActionEvent event) -> {
            colorMode.setPrimaryColor(colorPicker.getValue());
        });

        Label Spacer = new Label(" ");

        this.getChildren().addAll(colorPicker, Spacer, darkModeBtn);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(3, 3, 3, 3));

    }

}
