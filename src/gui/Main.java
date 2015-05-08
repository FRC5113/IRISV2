package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import org.opencv.core.Core;
import tools.Logger;
import tools.vision.passes.PassBase;

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

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


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

        controller.getMenuItemSaveVR().setOnAction(t -> SaveLoadManager.save(vrMan, primaryStage.getScene()));
        controller.getMenuItemLoadVR().setOnAction(t -> SaveLoadManager.load(vrMan, primaryStage.getScene()));

        Logger.logln("Started program!");



        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                long graphTimer = System.currentTimeMillis();
                long sleepTimer = System.currentTimeMillis();

                while (true) {

                    //Non FX stuff goes here (Img processing mostly)

                    //graphTimer = tryUpdateGraphs(graphTimer);

                    Platform.runLater(() -> {
                        //FX Stuff goes here
                        //mon.updateGraphs(controller.getResMonCompCPU(), controller.getResMonCompRAM());
                        controller.update();

                        //Set the displayed pass's view to be updated as much as possible
                        TreeItem selectedItem = (TreeItem) controller.getVisionRecTreeView().getSelectionModel().getSelectedItem();
                        try {
                            if (selectedItem != null && selectedItem.getValue() != null && selectedItem.getValue() instanceof PassBase) {
                                ((PassBase) selectedItem.getValue()).updateView();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    //Make sure we don't run too fast
                    if(sleepTimer + 500 < System.currentTimeMillis())
                    {
                        sleepTimer = System.currentTimeMillis();
                    }
                    else
                    {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private long tryUpdateGraphs(long timer)
    {
        int interval = 1000;
        if(System.currentTimeMillis() - interval > timer) {
            mon.updateHistory();
            return System.currentTimeMillis();
        }
        return timer;
    }
}