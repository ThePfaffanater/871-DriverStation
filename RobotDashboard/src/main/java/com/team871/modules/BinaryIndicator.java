package com.team871.modules;


import com.team871.config.Style.ColorMode;
import com.team871.util.data.IData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;


public class BinaryIndicator extends VBox {

    private javafx.scene.shape.Circle Circle;
    private Label label;
    private ColorMode colorMode;
    private IData<Boolean> data;
    private String title;

    /**
     * @param colorMode colorMode settings to be used.
     * @param title     the title of the Circle Indicator
     */
    public BinaryIndicator(ColorMode colorMode, IData<Boolean> data, String title) {
        this.colorMode = colorMode;
        this.title = title;
        this.data = data;


        label = new Label(this.title + ":");
        label.setTextFill(colorMode.getSecondaryColor());
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(5, 0, 3, 0));

        Label spacer = new Label("   ");
        spacer.setFont(Font.font("Arial", 8));

        Circle = new Circle();
        Circle.setRadius(25);

        setNeutral();

        this.getChildren().addAll(label, Circle, spacer);
        this.setPadding(new Insets(3, 3, 3, 3));
        this.setAlignment(Pos.CENTER);

        //Updates:
        colorMode.addListener(observable -> {
            label.setTextFill(colorMode.getSecondaryColor());
        });

        data.addListener((observable, old, newValue) -> {
            setState(newValue);
        });


    }

    /**
     * Sets the Indicator to Off position (RED color)
     */
    private void setState(boolean on) {
        if (on)
            Circle.setFill(Color.GREEN);

        else
            Circle.setFill(Color.RED);
    }

    private void setNeutral() {
        Circle.setFill(Color.YELLOW);
    }




}

