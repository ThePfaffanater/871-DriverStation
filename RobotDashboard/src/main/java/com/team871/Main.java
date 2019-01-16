package com.team871;

import com.team871.config.DefaultDashboardConfig;
import com.team871.config.IDashboardConfig;
import com.team871.screens.IScreen;
import com.team871.screens.preconfigured.BlockadeScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * @project Robotics Dashboard
 * @author Team871-TPfaffe
 *
 */

public class Main extends Application {


    private IDashboardConfig dashboardConfig;
    private Pane curRoot;
    private IScreen driveScreen;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void init() {
        dashboardConfig = new DefaultDashboardConfig(871);
        driveScreen = new BlockadeScreen(dashboardConfig);
        curRoot = driveScreen.getScreen();
        driveScreen.update();
    }

    @Override
    public void start(Stage window) {

        Scene scene = new Scene(curRoot);
        window.setTitle("RoboR Dashboard");
        window.setScene(scene);
        window.setResizable(false);
        window.initStyle(StageStyle.UTILITY);
        window.show();


        Task task = new Task<Void>() {//update thread.
            @Override
            public Void call() throws Exception {
                long  startT = System.currentTimeMillis();
                long  goalT  = 34;
                while (true) {
                    Platform.runLater(() -> driveScreen.update());
                    Thread.sleep (Math.max( (((goalT)-(System.currentTimeMillis()-startT))), 0));
                    startT = System.currentTimeMillis();
                }
            }
        };
        Thread th = new Thread( task);
        th.setDaemon(true);
        th.start();

        window.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            stop();
        });

    }

    @Override
    public void stop() {
        System.out.println("Main shutdown initiated");
        Platform.exit();
        System.exit(0);
      System.out.println("Goodbye :( Wait No! It did not actually shutdown!!!!");
      //this line of code should never actually run...
    }
}
