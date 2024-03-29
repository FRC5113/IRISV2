package gui;

import javafx.scene.control.*;
import tools.Logger;
import tools.vision.passes.PassBase;
import tools.vision.Treeable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Jake on 4/27/2015.
 * Keeps track of passes, runs passes' logic, and removes/adds new passes
 */
class VisionRecManager {

    public ArrayList<PassBase> getPasses() {
        return passes;
    }

    public boolean running = false;
    public boolean paused = false;
    public boolean flagTick = false;
    public long startTime = System.currentTimeMillis();

    public void setPasses(ArrayList<PassBase> passes) {
        this.passes = passes;
        controller.getVisionRecTreeView().getRoot().getChildren().clear();
        for(PassBase p : passes)
        {
            p.setup(controller, passes);
            controller.getVisionRecTreeView().getRoot().getChildren().add(p.getTreeItem());
            Logger.logln("Loaded from file: " + p.getClass().getTypeName() + " (" + p.getNickname() + ")");
        }
    }

    private ArrayList<PassBase> passes;

    private Button buttonRenamePass;
    private Button buttonRemovePass;
    private Controller controller;

    //Last selected PassBase to use for adding
    private String currentlySelected;

    public VisionRecManager(Controller c) {

        ToggleButton startStop = c.getVrStartStopToggle();
        startStop.setOnAction(event1 -> {
            running = startStop.selectedProperty().getValue();
            if(running)
            {
                startTime = System.currentTimeMillis();
                c.getVrTickButton().setDisable(false);
                c.getVrPauseButton().setDisable(false);
            }
            else
            {
                paused = false;
                c.getVrTickButton().setDisable(true);
                c.getVrPauseButton().setDisable(true);
            }
        });

        Button tickButton = c.getVrTickButton();
        tickButton.setOnAction(event2 -> flagTick = true);

        ToggleButton pauseButton = c.getVrPauseButton();
        pauseButton.setOnAction(event3 -> paused = pauseButton.selectedProperty().getValue());


        buttonRenamePass = c.getButtonRenamePass();
        buttonRemovePass = c.getButtonRemovePass();
        setupRenameButton();
        setupRemoveButton();

        controller = c;

        passes = new ArrayList<>(10);

        //Button setup
        Button addButton = c.getButtonCreateNewPass();
        //If button is clicked, get the last selected item from the accordion menus
        addButton.setOnAction(event -> {

            try {
                Class<?> classTemp = Class.forName(currentlySelected);


                Class[] parameterTypes = {Controller.class, List.class};
                Constructor constructor = classTemp.getConstructor(parameterTypes);

                Object instance = constructor.newInstance(c, passes);

                passes.add((PassBase) instance);
                c.getVisionRecTreeView().getRoot().getChildren().add(((PassBase) instance).getTreeItem());
                Logger.logln("Created new pass : " + instance + " ( " + instance.getClass().getTypeName() + " )");

            } catch (Exception e) {
                Logger.logln(e.toString());
            }

        });

        //Drawing view setup
        ListView passCreatorDrawingView = c.getPassCreatorDrawingView();
        passCreatorDrawingView.getItems().add("tools.vision.passes.drawing.PassGridOverlay");
        //If clicked, save the last selected item.
        passCreatorDrawingView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);


        //Drawing view setup
        ListView corePassCreatorView = c.getPassCreatorCoreView();
        //passCreatorDrawingView.getItems().add("tools.vision.passes.drawing.PassGridOverlay");
        //If clicked, save the last selected item.
        corePassCreatorView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);




        //
        //Debug view setup
        ListView passCreatorDebugView = c.getPassCreatorDebugView();
        passCreatorDebugView.getItems().add("tools.vision.passes.debug.PassHistogram");        //If clicked, save the last selected item.
        passCreatorDebugView.getItems().add("tools.vision.passes.debug.PassHomography");
        passCreatorDebugView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);

        //Formatting view setup
        ListView passCreatorFormattingView = c.getPassCreatorFormattingView();
        passCreatorFormattingView.getItems().add("tools.vision.passes.formatting.PassImageFormatConverter");
        //If clicked, save the last selected item.
        passCreatorFormattingView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);

        //Source view setup
        ListView passCreatorSourcesView = c.getPassCreatorSourcesView();
        passCreatorSourcesView.getItems().add("tools.vision.passes.sources.SourceComputerCam");
        passCreatorSourcesView.getItems().add("tools.vision.passes.sources.SourceImageFile");
        //If clicked, save the last selected item.
        passCreatorSourcesView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);

        //Imgproc view setup
        ListView passCreatorImgprocView = c.getPassCreatorImgprocView();
        passCreatorImgprocView.getItems().add("tools.vision.passes.imgproc.PassColorThreshold");
        passCreatorImgprocView.getItems().add("tools.vision.passes.imgproc.PassEdgeSobel");
        passCreatorImgprocView.getItems().add("tools.vision.passes.imgproc.PassBlur");
        //passCreatorImgprocView.getItems().add("tools.vision.passes.imgproc.PassErode");
        //passCreatorImgprocView.getItems().add("tools.vision.passes.imgproc.PassDilate");
        passCreatorImgprocView.getItems().add("tools.vision.passes.imgproc.PassMorphology");
        //If clicked, save the last selected item.
        passCreatorImgprocView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);

    }

    private void setupRemoveButton()
    {
        buttonRemovePass.setOnAction(event -> {

            Treeable selected = null;

            for(PassBase b : passes)
            {
                if(b.getTreeItem() == controller.getVisionRecTreeView().getSelectionModel().getSelectedItem())
                {
                    selected = b;
                    break;
                }
            }


            if(selected != null)
            {

                String name = selected.getNickname();
                Logger.logln("Removed " + name);

                passes.remove(selected);
                controller.getVisionRecTreeView().getRoot().getChildren().remove(selected.getTreeItem());
                selected = null;

                forceTreeViewRefresh(controller.getVisionRecTreeView());
            }


        });
    }

    private void setupRenameButton()
    {
        buttonRenamePass.setOnAction(event -> {

            Treeable selected = null;

            for(PassBase b : passes)
            {
                if(b.getTreeItem() == controller.getVisionRecTreeView().getSelectionModel().getSelectedItem())
                {
                    selected = b;
                    break;
                }
            }


            if(selected != null)
            {

                String oldName = selected.getNickname();
                TextInputDialog dialog = new TextInputDialog(oldName);
                dialog.setTitle("Rename pass: " + selected.getNickname());
                dialog.setHeaderText("Hit the keyboard with your hands to enter text.");
                dialog.setContentText("New pass name: ");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(selected::setNickname);

                Logger.logln("Renamed " + oldName + " to " + result.get());

                forceTreeViewRefresh(controller.getVisionRecTreeView());
            }


        });
    }

    //Because we can't call a redraw method, just refresh the items with setExpanded.
    //Not a good way of doing it, but it works.
    private void forceTreeViewRefresh(TreeView treeView) {
        treeView.getRoot().setExpanded(false);
        treeView.getRoot().setExpanded(true);
    }


}
