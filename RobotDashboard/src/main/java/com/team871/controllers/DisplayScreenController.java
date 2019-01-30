package com.team871.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * @author T3Pfaffe on 1/30/2019.
 * @project DriverStation
 */
public class DisplayScreenController {

    @FXML
    AnchorPane background;
    @FXML
    Button debugBtn;
    @FXML
    Button driveBtn;

    @FXML
    void driveBtnAction() {
        System.out.println("hah, you thought something would happen....");
    }

    @FXML
    void debugBtnAction() {
        System.out.println("hah, you thought something would happen....");
    }
}
