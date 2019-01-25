package com.team871.config;

import com.team871.config.Style.ColorMode;
import edu.wpi.first.networktables.NetworkTableInstance;

public interface IDashboardConfig {
    /**
     * @return the @config.ColorMode object containing the
     * proper secondary colors in regards to the background color
     * (i.e. light or dark modes)
     */
    ColorMode getColorMode();

    NetworkTableInstance getNetworkTableInstance();

    /**
     * @return the initial width of the dashboard window
     */
    int getInitialWidth();

    /**
     * @return the initial height of the dashboard window
     */
    int getInitialHeight();


}
