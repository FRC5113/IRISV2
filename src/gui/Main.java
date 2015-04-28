package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tools.Logger;

/*
This tool is used for vision recognition, as well as general computer and robot monitoring.
We use JavaFX and OpenCV, along with the standard FIRST libraries too.
Team 5113.
 */
public class Main extends Application {

    Controller controller;

    ResourceMonitor mon;

    VisionRecManager vrMan;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));

        controller = new Controller();
        loader.setController(controller);

        Parent root = loader.load();
        primaryStage.setTitle("FRC 5113 - IRIS Vision Recognition and Robot Monitoring Systems [Version 2.0.0b]");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();

        //Setup individual components
        controller.setup();
        vrMan = new VisionRecManager(controller);
        mon = new ResourceMonitor();

        Logger.logln("Started program!");

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(() -> {
                        mon.updateHistory();
                        mon.updateGraphs(controller.getResMonCompCPU(), controller.getResMonCompRAM());
                        controller.update();
                    });
                    Thread.sleep(1000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}