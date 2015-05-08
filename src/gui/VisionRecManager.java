package gui;

import javafx.scene.control.*;
import org.opencv.core.Core;
import tools.Logger;
import tools.vision.passes.PassBase;
import tools.vision.passes.drawing.PassGridOverlay;
import tools.vision.passes.sources.SourceComputerCam;
import tools.vision.passes.sources.SourceImageFile;
import tools.vision.Treeable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Jake on 4/27/2015.
 * Keeps track of passes, runs passes' logic, and removes/adds new passes
 */
public class VisionRecManager {

    public ArrayList<PassBase> getPasses() {
        return passes;
    }

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

    private ListView passCreatorSourcesView;
    private ListView passCreatorFormattingView;
    private ListView passCreatorImgprocView;
    private ListView passCreatorDebugView;
    private Button addButton;
    private Button buttonRenamePass;
    private Controller controller;

    public String getCurrentlySelected() {
        return currentlySelected;
    }

    //Last selected PassBase to use for adding
    private String currentlySelected;

    public VisionRecManager(Controller c) {


        buttonRenamePass = c.getButtonRenamePass();
        setupRenameButton();

        controller = c;

        passes = new ArrayList<>(10);

        //Button setup
        this.addButton = c.getButtonCreateNewPass();
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

        //Debug view setup
        this.passCreatorDebugView = c.getPassCreatorDebugView();
        passCreatorDebugView.getItems().add("tools.vision.passes.drawing.PassGridOverlay");
        //If clicked, save the last selected item.
        passCreatorDebugView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);

        //Formatting view setup
        this.passCreatorFormattingView = c.getPassCreatorFormattingView();
        //If clicked, save the last selected item.
        passCreatorFormattingView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);

        //Source view setup
        this.passCreatorSourcesView = c.getPassCreatorSourcesView();
        passCreatorSourcesView.getItems().add("tools.vision.passes.sources.SourceComputerCam");
        passCreatorSourcesView.getItems().add("tools.vision.passes.sources.SourceImageFile");
        //If clicked, save the last selected item.
        passCreatorSourcesView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);

        //Imgproc view setup
        this.passCreatorImgprocView = c.getPassCreatorImgprocView();
        //If clicked, save the last selected item.
        passCreatorImgprocView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentlySelected = (String) newValue);

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
