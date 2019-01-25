package com.team871.modules;

import com.team871.config.Style.ColorMode;
import com.team871.util.data.IData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class StringDisplay extends VBox {

    private ColorMode colorMode;
    private IData<String> data;
    private Label textArea;
    private Label nameDisplay;

    public StringDisplay(ColorMode colorMode, IData<String> data, String name) {
        this.colorMode = colorMode;
        this.data = data;

        nameDisplay = new Label(name + ": ");
        textArea = new Label(data.getValue());

        textArea.setTextFill(colorMode.getSecondaryColor());
        nameDisplay.setTextFill(colorMode.getSecondaryColor());

        this.getChildren().addAll(nameDisplay, textArea);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(.2);
        this.setPadding(new Insets(3, 3, 3, 3));

        //Updates:
        colorMode.addListener(observable -> {
            textArea.setTextFill(colorMode.getPrimaryColor());
            nameDisplay.setTextFill(colorMode.getSecondaryColor());
        });
    }
}
