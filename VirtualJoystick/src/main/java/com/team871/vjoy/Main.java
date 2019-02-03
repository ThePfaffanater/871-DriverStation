package com.team871.vjoy;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;
import redlaboratory.jvjoyinterface.VJoy;

public class Main extends Application {

    private SerialPort serialPort;
    private VirtualJoystick stickJoy;

    private double lastA1;
    private double lastA2;
    private double lastX;
    private double lastY;
    private boolean lastC;
    private boolean lastZ;
    private boolean calibrating;
    private long lastRecieve = System.currentTimeMillis();

    private double mouseX = 0;
    private double mouseY = 0;
    private boolean mousePressed = false;


    public static void main(String[] args) {
        Application.launch(args);
    }


    public void start(Stage st) {

        st.getIcons().add(new Image(Main.class.getClassLoader().getResourceAsStream("icon.jpg")));

        st.setOnCloseRequest(e -> {
            System.out.println("Closing...");
            if (stickJoy != null) stickJoy.relinquishVJD(stickJoy.getRID());
            if (serialPort != null) {
                try {
                    serialPort.writeString("disconnect");
                    serialPort.closePort();
                } catch (SerialPortException e1) {
                    e1.printStackTrace();
                }
            }
            Platform.exit();
        });

        Pane root = new Pane();
        root.setPrefWidth(150);
        root.setPrefHeight(200);

        Label label = new Label("Select the Port to Use:");
        label.setPrefWidth(root.getPrefWidth());
        label.setPrefHeight(20);
        label.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(label);

        Button button = new Button("Continue");
        button.setDisable(true);

        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(SerialPortList.getPortNames());
        list.setItems(items);
        list.setPrefWidth(root.getPrefWidth());
        list.setPrefHeight(root.getPrefHeight() - 40 - 20);
        list.setLayoutY(20);
        list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        list.getSelectionModel().selectedItemProperty().addListener((obs, oldS, newS) -> {
            if (newS != null) {
                button.setDisable(false);
            } else {
                button.setDisable(true);
            }
        });

        root.getChildren().add(list);


        button.setPrefWidth(root.getPrefWidth());
        button.setPrefHeight(40);
        button.setLayoutY(root.getPrefHeight() - 40);
        root.getChildren().add(button);
        Scene scene = new Scene(root);

        st.setScene(scene);
        st.setResizable(false);
        st.sizeToScene();

        st.setTitle("Power Arm Virtual Joystick Monitor");

        st.show();

        button.setOnAction(e -> {
            String str = list.getSelectionModel().getSelectedItem();
            if (str != null) {
                connect(str);

                Thread t = new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                    }
                    while (true) {

                        if (System.currentTimeMillis() - lastRecieve > 3000) {
                            System.err.println("Lost Connection to " + str + "! Retrying Connection...");
                            try {
                                if (serialPort.isOpened()) serialPort.closePort();
                            } catch (SerialPortException e1) {
                                e1.printStackTrace();
                            }
                            connect(str);
                        }

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                        }
                    }
                });
                t.setDaemon(true);
                t.start();

                st.setOnCloseRequest(ev -> {
                    System.out.println("Closing...");
                    if (stickJoy != null) stickJoy.relinquishVJD(stickJoy.getRID());
                    if (serialPort != null) {
                        try {
                            serialPort.writeString("disconnect");
                            serialPort.closePort();
                        } catch (SerialPortException e1) {
                            e1.printStackTrace();
                        }
                    }
                    Platform.exit();
                });

                st.addEventHandler(MouseEvent.MOUSE_MOVED, mouseEvent -> {
                    mouseX = mouseEvent.getSceneX();
                    mouseY = mouseEvent.getSceneY();
                });

                st.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
                    mouseX = mouseEvent.getSceneX();
                    mouseY = mouseEvent.getSceneY();
                });

                st.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                    mousePressed = true;
                });
                st.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                    mousePressed = false;
                });

                Platform.runLater(() -> startGUI(st));
            }
        });

        stickJoy = new VirtualJoystick();
        if (!stickJoy.connect()) {
            System.err.println("Error starting vJoy virtual joystick.");
            return;
        }

		/*
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter the index: ");
		int index = sc.nextInt();
		sc.close();
		
		String port = portNames[index];
		
		connect(port);


        Platform.runLater(() -> startGUI(st));
        */

    }

    private void connect(String port) {
        this.serialPort = new SerialPort(port);
        try {
            serialPort.openPort();

            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.addEventListener(this::serialEvent, SerialPort.MASK_RXCHAR);

            serialPort.writeString("connect");

            lastRecieve = System.currentTimeMillis() - 2000;

        } catch (SerialPortException ex) {
            System.err.println("Exception on connect: " + ex);
        }
    }

    StringBuilder message = new StringBuilder();

    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                byte[] buffer = serialPort.readBytes();
                for (byte b : buffer) {
                    if ((b == '\r' || b == '\n') && message.length() > 0) {
                        String toProcess = message.toString();
                        //System.out.println(toProcess);
                        decode(toProcess);
                        message.setLength(0);
                    } else {
                        message.append((char) b);
                    }
                }
            } catch (SerialPortException ex) {
                System.out.println(ex);
                System.out.println("serialEvent");
            }
        }
    }

    private void decode(String msg) {
        String[] spl = msg.split("\\|");
        switch (spl[0]) {
            case "arm":

                try {
                    //calibrating = spl[5].equals("1");

                    if (!(spl[1].equals(" NAN") || spl[1].equals("-INF") || spl[1].equals("INF"))) {
                        double x = Double.parseDouble(spl[1]);
                        while (x < 0) x += 360;
                        stickJoy.setAxis((long) ((1.0 - (((x + 180) % 360 - 180 + 90)) / 180.0) * VJoy.AXIS_MAX_VALUE), stickJoy.getRID(), VJoy.HID_USAGE_RX);
                        lastA1 = x;
                    }

                    if (!(spl[2].equals(" NAN") || spl[2].equals("-INF") || spl[2].equals("INF"))) {
                        double y = -Double.parseDouble(spl[2]);
                        while (y < 0) y += 360;
                        stickJoy.setAxis((long) ((((y + 180) % 360 - 180 + 90)) / 180.0 * VJoy.AXIS_MAX_VALUE), stickJoy.getRID(), VJoy.HID_USAGE_RY);
                        lastA2 = y;
                    }


                    lastRecieve = System.currentTimeMillis();

                } catch (Exception e) {
                    System.err.println("Invalid packet:");
                    e.printStackTrace();
                }

                break;
            case "upd":

                try {
                    boolean c = spl[3].equals("1");
                    boolean z = spl[4].equals("1");
                    calibrating = spl[5].equals("1");

                    stickJoy.setBtn(c, stickJoy.getRID(), 1);
                    stickJoy.setBtn(z, stickJoy.getRID(), 2);

                    lastC = c;
                    lastZ = z;

                    if (!calibrating) {
                        if (!(spl[1].equals(" NAN") || spl[1].equals("-INF") || spl[1].equals("INF"))) {
                            double x = Double.parseDouble(spl[1]);
                            if (x > 1.0) x = 1.0;
                            if (x < -1.0) x = -1.0;
                            stickJoy.setAxis((long) ((x + 1) / 2.0 * VJoy.AXIS_MAX_VALUE), stickJoy.getRID(), VJoy.HID_USAGE_X);
                            lastX = x;
                        }

                        if (!(spl[2].equals(" NAN") || spl[2].equals("-INF") || spl[2].equals("INF"))) {
                            double y = -Double.parseDouble(spl[2]);
                            if (y > 1.0) y = 1.0;
                            if (y < -1.0) y = -1.0;
                            stickJoy.setAxis((long) ((y + 1) / 2.0 * VJoy.AXIS_MAX_VALUE), stickJoy.getRID(), VJoy.HID_USAGE_Y);
                            lastY = y;
                        }
                    }

                    lastRecieve = System.currentTimeMillis();

                } catch (Exception e) {
                    System.err.println("Invalid packet:");
                    e.printStackTrace();
                }

                break;
            default:
                System.err.println("Unknown message format: \"" + spl[0] + "\" in message \"" + msg + "\"");
                break;
        }
    }

    private void startGUI(Stage stage) {

        double prevW = stage.getWidth();
        double prevH = stage.getHeight();

        // Create the Canvas
        Canvas canvas = new Canvas(800, 400);
        canvas.setWidth(800);
        canvas.setHeight(400);

        stage.setX(stage.getX() - (canvas.getWidth() - prevW) / 2);
        stage.setY(stage.getY() - (canvas.getHeight() - prevH) / 2);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane();

        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Power Arm Virtual Joystick Monitor");

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                gc.save();

//                gc.restore();
//                if(!false) return;

                //gc.clearRect(0, 0, stage.getWidth(), stage.getHeight());
                gc.setStroke(Color.BLACK);
                gc.setFill(Color.BLACK);


                gc.setFill(Color.WHITE);
                if (System.currentTimeMillis() - lastRecieve > 3000) {
                    if (System.currentTimeMillis() % 500 >= 250) gc.setFill(new Color(1f, 0.6f, 0.6f, 1f));
                } else if (System.currentTimeMillis() - lastRecieve > 200) {
                    if (System.currentTimeMillis() % 300 >= 150) gc.setFill(new Color(1f, 0.8f, 0.8f, 1f));
                }

                gc.fillRect(0, 0, stage.getWidth(), stage.getHeight());


                //

                gc.setFill(Color.BLACK);

                gc.setTextAlign(TextAlignment.CENTER);
                gc.setFont(new Font("Arial", 30.0));
                gc.fillText("Power Arm Monitor", stage.getWidth() / 2, 30);

                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(new Font("Arial", 18.0));
                gc.setFill(Color.BLACK);
                gc.setFill((System.currentTimeMillis() - lastRecieve > 3000 && System.currentTimeMillis() % 500 < 250) ? Color.RED : Color.BLACK);
                gc.fillText((System.currentTimeMillis() - lastRecieve < 3000) ? "Connected on " + serialPort.getPortName() + "." : "Lost connection from " + serialPort.getPortName() + "!", stage.getWidth() / 2, 60);

                gc.save();
                gc.translate(stage.getWidth() / 2, 0);
                gc.translate(stage.getWidth() / 2 / 2, stage.getHeight() / 2);


                // draw arm

                int length1 = 70;
                int length2 = 70;

                int size = 20;

                double angle1 = lastA1;
                double angle2 = -lastA2;
                double angle3 = lastY * 90.0;

                boolean doMouseEasterEgg = true;
                if (doMouseEasterEgg) {

                    double relativeMouseX = mouseX - (stage.getWidth() / 2) - (stage.getWidth() / 2 / 2);
                    double relativeMouseY = mouseY - stage.getHeight() / 2;
                    double maxDist = length1 + length2 + size;


                    if (relativeMouseX * relativeMouseX + relativeMouseY * relativeMouseY < maxDist * maxDist) {

                        double targetX = relativeMouseX;
                        double targetY = relativeMouseY;
                        double distance = Math.sqrt(targetX * targetX + targetY * targetY);

                        if (distance > length1 + length2) {
                            double angle = Math.atan2(targetY, targetX);

                            targetX = (length1 + length2 - 0.01) * Math.cos(angle);
                            targetY = (length1 + length2 - 0.01) * Math.sin(angle);

                            distance = Math.sqrt(targetX * targetX + targetY * targetY);
                        }


                        angle2 = 180 + (mousePressed ? 1 : -1) * Math.toDegrees(Math.acos((length1 * length1 + length2 * length2 - distance * distance) / (2 * length1 * length2)));

                        angle1 = Math.toDegrees(Math.atan2(targetY, targetX) + (mousePressed ? 1 : -1) * Math.acos((length1 * length1 + distance * distance - length2 * length2) / (2 * length1 * distance)));

                        //angle1 = 360 - angle1;
                        //angle1 *= (mousePressed ? 1 : -1);
                        //angle2 *= -1;

                        //System.out.println(relativeMouseX + " " + relativeMouseY);
                        //System.out.println(angle1 + " " + angle2);
                    }
                }


                drawArm(gc, length1, length2, size, angle1, angle2, angle3, true, Color.TRANSPARENT, Color.TRANSPARENT);

                gc.restore();

                //

                gc.setStroke(Color.GRAY);
                gc.strokeLine(stage.getWidth() / 2, 100, stage.getWidth() / 2, stage.getHeight() - 80);

                //


                double nunchuckW = stage.getWidth() / 2;

                gc.save();
                gc.translate(nunchuckW / 2 - 50, stage.getHeight() / 2);
                //gc.scale(0.7, 0.7);


                gc.setStroke(Color.BLACK);
                gc.setGlobalAlpha(0.4);


                gc.setLineDashes(4);
                gc.strokeOval(-100, -100, 200, 200);

                gc.setLineDashes(0);
                gc.strokeOval(-5, -5, 10, 10);
                gc.setGlobalAlpha(0.2);
                gc.strokeRect(-100, -100, 200, 200);

                gc.setGlobalAlpha(1.0);
                for (int i = 0; i < 8; i++) {
                    double startAngle = Math.toRadians(i / 8.0 * 360);
                    double endAngle = Math.toRadians((i + 1) / 8.0 * 360);

                    gc.strokeLine(100 * Math.sin(startAngle), 100 * Math.cos(startAngle), 100 * Math.sin(endAngle), 100 * Math.cos(endAngle));
                }

                size = 50;

                double nx = lastX;
                double ny = lastY;

                if (calibrating) {
                    nx = Math.sin(System.currentTimeMillis() / 100.0) * 0.8;
                    ny = Math.cos(System.currentTimeMillis() / 100.0) * 0.8;
                }

                gc.setFill(new Color(0.4f, 0.4f, 0.4f, 1f));
                gc.fillOval(nx * 60 - (size * 0.7 / 2), ny * 60 - (size * 0.7 / 2), size * 0.7, size * 0.7);

                gc.setFill(new Color(0.2f, 0.2f, 0.2f, 1f));
                gc.fillOval(nx * 100 - size / 2, ny * 100 - size / 2, size, size);

                gc.restore();

                // c

                gc.save();

                gc.translate(nunchuckW / 2 + 110, stage.getHeight() / 2 - 50);

                gc.setFill(lastC ? new Color(0f, 1f, 0f, 1f) : new Color(0.7f, 0f, 0f, 1f));
                gc.fillOval(-30, -25, 60, 50);
                gc.strokeOval(-30, -25, 60, 50);

                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(new Font("Arial", 20.0));
                gc.setFill(Color.BLACK);
                gc.fillText("C", 0, 0);

                gc.restore();

                // z

                gc.save();

                gc.translate(nunchuckW / 2 + 110, stage.getHeight() / 2 - 50 + 80);

                gc.setFill(lastZ ? new Color(0f, 1f, 0f, 1f) : new Color(0.7f, 0f, 0f, 1f));
                gc.fillRect(-30, -25, 60, 50);
                gc.strokeRect(-30, -25, 60, 50);

                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(new Font("Arial", 20.0));
                gc.setFill(Color.BLACK);
                gc.fillText("Z", 0, 0);

                gc.restore();

                // calibration

                gc.save();

                gc.translate(nunchuckW / 2, stage.getHeight() / 2 + 140);

                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(new Font("Arial", 18.0));
                gc.setFill(Color.BLACK);
                gc.fillText(calibrating ? "Rotate the stick and release when finished." : "Hold C and Z to calibrate stick.", 0, 0);

                gc.restore();

                //

                stage.show();
                gc.restore();
            }
        }.start();
    }

    private void drawArm(GraphicsContext gc, int length1, int length2, int size, double angle1, double angle2, double angle3, boolean drawLimitCircles, Paint jointFill, Paint connectorFill) {
        gc.save();

        gc.setLineDashes(5);

        double endX = length1 * Math.cos(Math.toRadians(angle1)) + length2 * Math.cos(Math.toRadians(angle1 + angle2));
        double endY = length1 * Math.sin(Math.toRadians(angle1)) + length2 * Math.sin(Math.toRadians(angle1 + angle2));

        if (drawLimitCircles) {

            gc.setStroke(Color.LIGHTGRAY);
            //System.out.println("1");
            gc.strokeLine(0, 0, endX, endY);
            //System.out.println("2");

            gc.strokeOval(-length1, -length1, length1 * 2, length1 * 2);
            gc.strokeOval(-(length1 + length2), -(length1 + length2), (length1 + length2) * 2, (length1 + length2) * 2);

            gc.setLineDashes(0);
            gc.strokeOval(-(length1 + length2 + size), -(length1 + length2 + size), (length1 + length2 + size) * 2, (length1 + length2 + size) * 2);

            gc.setLineDashes(5);
            gc.translate(length1 * Math.cos(Math.toRadians(angle1)), length1 * Math.sin(Math.toRadians(angle1)));
            gc.rotate(angle1);

            gc.strokeOval(-length2, -length2, length2 * 2, length2 * 2);

            gc.translate(length2 * Math.cos(Math.toRadians(angle2)), length2 * Math.sin(Math.toRadians(angle2)));

            gc.strokeOval(-size, -size, size * 2, size * 2);
        }

        gc.restore();

        //System.out.println(lastA1);


        gc.setStroke(Color.BLACK);


        gc.setStroke(connectorFill);
        gc.setLineWidth(size);
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1)), (size / 2) * Math.sin(Math.toRadians(angle1)), (length1 - size / 2) * Math.cos(Math.toRadians(angle1)), (length1 - size / 2) * Math.sin(Math.toRadians(angle1)));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1 + 90)), (size / 2) * Math.sin(Math.toRadians(angle1 + 90)), length1 * Math.cos(Math.toRadians(angle1)) + (size / 2) * Math.cos(Math.toRadians(angle1 + 90)), length1 * Math.sin(Math.toRadians(angle1)) + (size / 2) * Math.sin(Math.toRadians(angle1 + 90)));
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1 - 90)), (size / 2) * Math.sin(Math.toRadians(angle1 - 90)), length1 * Math.cos(Math.toRadians(angle1)) + (size / 2) * Math.cos(Math.toRadians(angle1 - 90)), length1 * Math.sin(Math.toRadians(angle1)) + (size / 2) * Math.sin(Math.toRadians(angle1 - 90)));

        gc.setFill(jointFill);
        gc.fillOval(-size / 2, -size / 2, size, size);
        gc.setFill(Color.BLACK);
        gc.strokeOval(-size / 2, -size / 2, size, size);

        gc.save();
        gc.translate(length1 * Math.cos(Math.toRadians(angle1)), length1 * Math.sin(Math.toRadians(angle1)));
        gc.rotate(angle1);

        gc.setStroke(Color.BLACK);


        gc.setStroke(connectorFill);
        gc.setLineWidth(size);
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2)), (size / 2) * Math.sin(Math.toRadians(angle2)), (length2 - size / 2) * Math.cos(Math.toRadians(angle2)), (length2 - size / 2) * Math.sin(Math.toRadians(angle2)));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2 + 90)), (size / 2) * Math.sin(Math.toRadians(angle2 + 90)), length2 * Math.cos(Math.toRadians(angle2)) + (size / 2) * Math.cos(Math.toRadians(angle2 + 90)), length1 * Math.sin(Math.toRadians(angle2)) + (size / 2) * Math.sin(Math.toRadians(angle2 + 90)));
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2 - 90)), (size / 2) * Math.sin(Math.toRadians(angle2 - 90)), length2 * Math.cos(Math.toRadians(angle2)) + (size / 2) * Math.cos(Math.toRadians(angle2 - 90)), length1 * Math.sin(Math.toRadians(angle2)) + (size / 2) * Math.sin(Math.toRadians(angle2 - 90)));

        gc.setStroke(Color.BLACK);
        gc.strokeOval(-size / 2, -size / 2, size, size);
        gc.setFill(jointFill);
        gc.fillOval(-size / 2, -size / 2, size, size);

        gc.translate(length2 * Math.cos(Math.toRadians(angle2)), length2 * Math.sin(Math.toRadians(angle2)));

        gc.setStroke(Color.BLACK);
        gc.strokeOval(-size / 2, -size / 2, size, size);
        gc.setFill(jointFill);
        gc.fillOval(-size / 2, -size / 2, size, size);

        gc.rotate(-angle1 + angle3);
        gc.translate(20, 0);

        gc.strokeArc(-size / 2, -size / 2, size, size, 180 - 90, 180, ArcType.OPEN);

        //gc.strokeOval(-size / 2, -size / 2, size, size);
        gc.restore();
        //gc.restore();

    }
}