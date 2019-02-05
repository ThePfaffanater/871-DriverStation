package com.team871.vjoy;

import com.team871.jvjoyinterface.VJoy;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;


public class SerialVirtualJoystick {

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
    private StringBuilder message = new StringBuilder();

    public SerialVirtualJoystick(String port) throws SerialPortException {
        connect(port);
        start(port);
    }


    public void start(String port) {

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {}

            while (true) {

                if (System.currentTimeMillis() - lastRecieve > 3000) {
                    System.err.println("Lost Connection to " + port + "! Retrying Connection...");
                    try {
                        if (serialPort.isOpened()) serialPort.closePort();
                    } catch (SerialPortException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        connect(port);
                    } catch (SerialPortException e) {
                        e.printStackTrace();
                    }

                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
            }
        });
        t.setDaemon(true);
        t.start();



        stickJoy = new VirtualJoystick();
        if (!stickJoy.connect()) {
            System.err.println("Error starting vJoy virtual joystick.");
            return;
        }

    }

    private void connect(String port) throws SerialPortException {
        this.serialPort = new SerialPort(port);
            serialPort.openPort();

            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.addEventListener(this::serialEvent, SerialPort.MASK_RXCHAR);

            serialPort.writeString("connect");

            lastRecieve = System.currentTimeMillis() - 2000;

    }


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

    public void close(){

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


    }
}