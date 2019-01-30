package com.team871.modules;

import com.team871.util.data.NetNumericalDataValue;
import com.team871.util.data.NumericalDataValue;
import edu.wpi.first.networktables.NetworkTable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * @author T3Pfaffe on 1/30/2019.
 * @project DriverStation
 */
public class PidTuner extends GridPane {

    //TODO: get actual key values from PID class on robot
    private final String P_Key = "P_Val";
    private final String I_Key = "I_Val";
    private final String D_Key = "D_Val";
    private final String SETPOINT_Key = "SetPoint_Val";
    private final String ERROR_Key = "Error_Val";

    private Label mainTitle;

    private Label pTitle;
    private TextField pControl;

    private Label iTitle;
    private TextField iControl;

    private Label dTitle;
    private TextField dControl;

    private Label setPoint;
    private TextField setPointControl;

    private Label error;
    private TextField errorControl;

    private NumericalDataValue pVal;
    private NumericalDataValue iVal;
    private NumericalDataValue dVal;
    private NumericalDataValue setPointVal;
    private NumericalDataValue errorVal;

    public PidTuner() {
        mainTitle = new Label("Un initialized: ");

        pTitle = new Label("P: ");
        pControl = new TextField("0.00");
        pControl.setMaxWidth(50);

        iTitle = new Label("I: ");
        iControl = new TextField("0.00");
        iControl.setMaxWidth(50);

        dTitle = new Label("D: ");
        dControl = new TextField("0.00");
        dControl.setMaxWidth(50);

        setPoint = new Label("SetPoint: ");
        setPointControl = new TextField("0.00");
        setPointControl.setMaxWidth(50);
        setPointControl.setEditable(false);

        error = new Label("Error: ");
        errorControl = new TextField("0.00");
        errorControl.setMaxWidth(50);
        errorControl.setEditable(false);


        this.addRow(0, pTitle, pControl);
        this.addRow(1, iTitle, iControl);
        this.addRow(2, dTitle, dControl);
        this.addRow(3, setPoint, setPointControl);
        this.addRow(4, error, errorControl);
        this.setAlignment(Pos.CENTER_RIGHT);
    }

    public void initialize(NetworkTable pidObject) {

        mainTitle.setText(pidObject.toString());
        pVal = new NetNumericalDataValue(pidObject.getEntry(P_Key));
        iVal = new NetNumericalDataValue(pidObject.getEntry(I_Key));
        dVal = new NetNumericalDataValue(pidObject.getEntry(D_Key));
        setPointVal = new NetNumericalDataValue(pidObject.getEntry(SETPOINT_Key));
        errorVal = new NetNumericalDataValue(pidObject.getEntry(ERROR_Key));

        pControl.setText("" + pVal.getValue());
        iControl.setText("" + iVal.getValue());
        dControl.setText("" + dVal.getValue());

        //Updates:
        pControl.setOnAction(event -> pidObject.getEntry(P_Key).setNumber(Double.parseDouble(pControl.getText())));
        pVal.addListener((observable, old, newValue) -> pControl.setText("" + newValue.doubleValue()));

        iControl.setOnAction(event -> pidObject.getEntry(I_Key).setNumber(Double.parseDouble(iControl.getText())));
        iVal.addListener((observable, old, newValue) -> iControl.setText("" + newValue.doubleValue()));


        dControl.setOnAction(event -> pidObject.getEntry(D_Key).setNumber(Double.parseDouble(dControl.getText())));
        dVal.addListener((observable, old, newValue) -> dControl.setText("" + newValue.doubleValue()));

        setPointVal.addListener(((observable, oldValue, newValue) -> setPoint.setText("" + newValue)));

        errorVal.addListener(((observable, oldValue, newValue) -> error.setText("" + newValue)));
    }

}
