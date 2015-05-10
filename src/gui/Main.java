package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;
import tools.Logger;
import tools.vision.passes.PassBase;
import tools.vision.passes.sources.SourceBase;
import tools.vision.properties.PropertyBase;

/*
This tool is used for vision recognition, as well as general computer and robot monitoring.
We use JavaFX and OpenCV, along with the standard FIRST libraries too.
Team 5113.
 */
public class Main extends Application {

    Controller controller;

    ResourceMonitor mon;

    VisionRecManager vrMan;

    Thread processThread;
    Thread fxThread;

    boolean quit = false;

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

        primaryStage.setOnCloseRequest(we ->
        {
            quit = true;
            System.exit(0);
        });

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

                while (!quit) {

                    //Non FX stuff goes here (Img processing mostly)

                    graphTimer = tryUpdateGraphs(graphTimer);

                    Platform.runLater(() -> {
                        //FX Stuff goes here

                        mon.updateGraphs(controller.getResMonCompCPU(), controller.getResMonCompRAM());
                        controller.update();

                        if(vrMan.running && !vrMan.paused) {
                            controller.getVrUptimeClock().setText("" + (System.currentTimeMillis() - vrMan.startTime) / 1000f);
                        }

                        //Set the displayed pass's view to be updated as much as possible
                        TreeItem selectedItem = (TreeItem) controller.getVisionRecTreeView().getSelectionModel().getSelectedItem();
                        try {
                            if (selectedItem != null && selectedItem.getValue() != null) {
                                if(selectedItem.getValue() instanceof PassBase)
                                    ((PassBase) selectedItem.getValue()).updateView();
                                else if(selectedItem.getValue() instanceof PropertyBase)
                                    ((PassBase) selectedItem.getParent().getValue()).updateView();
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
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        };
        fxThread = new Thread(task);
        fxThread.setDaemon(true);
        fxThread.start();

        //Thread responsible for doing the actual pass math
        processThread = new Thread(){
            public void run(){
                while (!quit) {
                    boolean allowTick = false;
                    if(vrMan.running && !vrMan.paused)
                        allowTick = true;
                    else if(vrMan.running && vrMan.paused && vrMan.flagTick)
                        allowTick = true;

                    //System.out.println(allowTick);
                    if(allowTick)
                    {
                        //System.out.println("Processing...");

                        vrMan.getPasses().stream().filter(b -> b instanceof SourceBase).forEach(b -> ((SourceBase) b).runSourceProcess());
                        vrMan.flagTick = false;
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        processThread.setDaemon(true);
        processThread.start();
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