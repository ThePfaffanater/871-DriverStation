package com.team871.config;

import javafx.scene.paint.Color;

public class ColorMode {
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
  }

  /**
   * @return Color Object with secondary color
   */
  public Color getSecondaryColor() {

    if (!this.darkMode) {
      this.SecColor = Color.rgb(73, 73, 73);
    } else if (this.darkMode) {
      this.SecColor = Color.rgb(237, 237, 237);
    }


    return this.SecColor;

  }

  public Color getPrimaryColor() {
    return primaryColor;
  }

  public void setPrimaryColor(Color newPrimaryColor) {
    this.primaryColor = newPrimaryColor;
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
   * will invert the color mode(dark or light).
   */
  public void changeColorMode() {
    darkMode = !darkMode;
  }

  public boolean isDarkMode() {
    return this.darkMode;
  }

}
