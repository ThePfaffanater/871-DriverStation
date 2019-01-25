package com.team871.config.Style;


import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorMode implements ObservableValue<ColorMode> {


    private List<InvalidationListener> invalidationListeners;

    private Boolean darkMode = false;
    private Color backColor;
    private Color primaryColor;
    private Color SecColor;


    /**
     * @param mode true will set it to darkMode, false to LightMode
     *             the primary color will be set to a default.
     */
    public ColorMode(Boolean mode) {
        this(mode, Color.rgb(59, 115, 146));

    }

    /**
     * @param mode         true will set it to darkMode, false to LightMode
     * @param primaryColor the primary color.
     */
    public ColorMode(Boolean mode, Color primaryColor) {
        this.darkMode = mode;
        this.primaryColor = primaryColor;

        invalidationListeners = new ArrayList<>();
    }

    /**
     * @return Color Object with secondary color
     */
    public Color getSecondaryColor() {

        if (!this.darkMode) {
            this.SecColor = Color.rgb(73, 73, 73);
        } else {
            this.SecColor = Color.rgb(237, 237, 237);
        }


        return this.SecColor;

    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    /**
     * @return Color object with background color
     */
    public Color getBackgroundColor() {

        if (!this.darkMode) {
            this.backColor = Color.rgb(255, 255, 255);
        } else if (this.darkMode) {
            this.backColor = Color.rgb(39, 44, 50);
        }


        return this.backColor;

    }

    /**
     * @return whether or not it is in darkMode
     */
    public boolean isDarkMode() {
        return this.darkMode;
    }

    /**
     * @param newPrimaryColor the new primary color
     */
    protected void setPrimaryColor(Color newPrimaryColor) {
        this.primaryColor = newPrimaryColor;
        notifyInvalidationListeners();
    }

    /**
     * will invert the color mode(dark or light).
     */
    protected void changeBaseColor() {
        darkMode = !darkMode;
        notifyInvalidationListeners();
    }


    private void notifyInvalidationListeners() {
        for (InvalidationListener changeListener : invalidationListeners) {
            changeListener.invalidated(this);
        }
    }

    @Override
    public void addListener(ChangeListener<? super ColorMode> listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeListener(ChangeListener<? super ColorMode> listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);
    }

    @Override
    public ColorMode getValue() {
        return this;
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }
}
