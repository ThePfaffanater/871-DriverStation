package com.team871.modules;


import com.team871.config.ColorMode;
import com.team871.config.IUpdateable;
import com.team871.util.IData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;


public class BinaryIndicator extends VBox implements IUpdateable {

    private static javafx.scene.shape.Circle Circle;
    private static Label Label;
    ColorMode colorMode;
    IData<Boolean> data;
    private String title;

    /**
     * @param colorMode colorMode settings to be used.
     * @param title     the title of the Circle Indicator
     */
    public BinaryIndicator(ColorMode colorMode, IData<Boolean> data, String title) {
        this.colorMode = colorMode;
        this.title = title;
        this.data = data;



        Label = new Label(this.title + ":");
        Label.setTextFill(colorMode.getSecondaryColor());
        Label.setAlignment(Pos.CENTER);
        Label.setPadding(new Insets(5, 0, 3, 0));

        Label spacer = new Label("   ");
        spacer.setFont(Font.font("Arial", 8));

        Circle = new Circle();
        Circle.setRadius(25);

        setNeutral();

        this.getChildren().addAll(Label, Circle, spacer);
        this.setPadding(new Insets(3, 3, 3, 3));
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Sets the Indicator to Off position (RED color)
     */
    private void setOff() {
        Circle.setFill(Color.RED);
    }

    private void setNeutral() {
        Circle.setFill(Color.YELLOW);
    }

    /**
     * Sets the Indicator to On position (GREEN color);
     */
    private void setOn() {
        Circle.setFill(Color.GREEN);
    }

    @Override
    public void update() {
        Label.setTextFill(colorMode.getSecondaryColor());

        if (data.get()) { //for some odd reason ternary refuses to work here?
            setOn();
        } else {
            setOff();
        }

    }
}

