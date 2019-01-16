package com.team871.modules;

import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

public class CameraServerSource {

    private static Integer cameraQuantity;
    private int cameraIndex;

    private Image display;
    private VideoCapture vc;
    private Mat captureImg;
    private MatOfByte byteMat;


    public CameraServerSource(String sourceURL){
        if(cameraQuantity == null){
            cameraQuantity = -1;
        }
        cameraIndex = ++cameraQuantity;


        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        vc = new VideoCapture();
        vc.open(sourceURL);
        captureImg = new Mat();
        byteMat   = new MatOfByte();

        update();
        display = new Image("noCam.png");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            vc.release();
            System.out.println(" \tCamera-" + cameraIndex + " shut-down");
        }));
    }


    public Image grabHerByThePu(){
        return display;
    }

    public boolean isConnected(){
        return vc.isOpened();
    }

    /**
     *
     * @return true if a connection exists and it can update from it
     */
    public boolean update(){
        if(vc.isOpened() && vc.grab()) {
            vc.retrieve(captureImg);
            Imgcodecs.imencode(".bmp", captureImg, byteMat);
            display = new Image(new ByteArrayInputStream(byteMat.toArray()));
            return true;
        }else{
            return false;
        }
    }
}