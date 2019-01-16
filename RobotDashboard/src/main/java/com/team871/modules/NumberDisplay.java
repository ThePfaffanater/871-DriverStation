package com.team871.modules;

import com.team871.config.ColorMode;
import com.team871.config.IUpdateable;
import com.team871.util.IData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;

public class NumberDisplay extends VBox implements IUpdateable {

    private ColorMode colorMode;
    private IData<Double> data;
    private Label textArea;
    private Label nameDisplay;
    private DecimalFormat df;

    public NumberDisplay(ColorMode colorMode, IData<Double> data, String name) {
        this.colorMode = colorMode;
        this.data = data;

        nameDisplay = new Label(name + ": ");
        textArea = new Label(new Double(round(data.get(), 2)).toString());

        textArea.setTextFill(colorMode.getSecondaryColor());
        nameDisplay.setTextFill(colorMode.getSecondaryColor());

        super.getChildren().addAll(nameDisplay, textArea);
        super.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(3, 3, 3, 3));
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void update() {
        textArea.setText(new Double(round(data.get(), 2)).toString());
        textArea.setTextFill(colorMode.getPrimaryColor());
        nameDisplay.setTextFill(colorMode.getSecondaryColor());

    }
}
