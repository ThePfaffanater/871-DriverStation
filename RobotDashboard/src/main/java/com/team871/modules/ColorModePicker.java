package com.team871.modules;

import com.team871.config.Style.ColorModeController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

public class ColorModePicker extends HBox {

    ColorPicker colorPicker;
    RadioButton darkModeBtn;

    public ColorModePicker(ColorModeController colorModeController) {
        super();
        colorPicker = new ColorPicker();
        darkModeBtn = new RadioButton();
        darkModeBtn.setText("Light/Dark Mode");

        this.getChildren().addAll(colorPicker, darkModeBtn);

        darkModeBtn.setOnAction((ActionEvent event) -> {
            colorModeController.changeBaseColor();
        });

        colorPicker.setOnAction((ActionEvent event) -> {
            colorModeController.setPrimaryColor(colorPicker.getValue());
        });


        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(3, 3, 3, 3));

    }

}
