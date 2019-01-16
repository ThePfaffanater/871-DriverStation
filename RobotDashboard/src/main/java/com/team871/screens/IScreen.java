package com.team871.screens;

import com.team871.config.IUpdateable;
import javafx.scene.layout.GridPane;

public interface IScreen extends IUpdateable {

  GridPane getScreen();

}
