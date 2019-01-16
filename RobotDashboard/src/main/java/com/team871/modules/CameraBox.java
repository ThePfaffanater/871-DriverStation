package com.team871.modules;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaView;

public class CameraBox extends HBox {
	
	private double camHeight;
	private double camWidth;
	private CameraServerSource source;

    private MediaView mediaView;

    /**
     * @param CameraHeight in pixels
     * @param CameraWidth in pixels
     * @author Team871-TPfaffe
     */
	public CameraBox(double CameraHeight, double CameraWidth){
        Image img = new Image("noCam.png");
	    source = new CameraServerSource("http://10.8.71.2:1181/?action=stream"); //TODO: Grab source URL from netTables instead of hardcode


        ImageView display = new ImageView(img);
        display.setFitHeight(CameraHeight);
        display.setFitWidth(CameraWidth);
        getChildren().add(display);

        int FPS = 75;

        Runnable videoUpdateTask = () -> {
            long startT;

            while (true) {
                startT = System.currentTimeMillis();
                source.update();

                display.setImage(source.grabHerByThePu());

                try {
                    final long sleepMillis = (long) ((1000.0/FPS)-(System.currentTimeMillis()-startT));
                    Thread.sleep(Math.max(sleepMillis,0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread videoUpdateThread = new Thread(videoUpdateTask);
        videoUpdateThread.setDaemon(true);
        videoUpdateThread.start();
    }


}
