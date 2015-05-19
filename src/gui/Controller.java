package gui;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import tools.Logger;
import tools.vision.Treeable;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyBase;

/**
 * Created by Jake on 4/26/2015.
 * This class handles most of the interactions with the UI, and keeps track of its important components
 */
public class Controller {

    public MenuItem getMenuItemLoadVR() {
        return menuItemLoadVR;
    }

    public void setMenuItemLoadVR(MenuItem menuItemLoadVR) {
        this.menuItemLoadVR = menuItemLoadVR;
    }

    public MenuItem getMenuItemSaveVR() {
        return menuItemSaveVR;
    }

    public void setMenuItemSaveVR(MenuItem menuItemSaveVR) {
        this.menuItemSaveVR = menuItemSaveVR;
    }

    @FXML
    private MenuItem menuItemLoadVR;
    @FXML
    private MenuItem menuItemSaveVR;

    @FXML
    private AreaChart resMonCompCPU;
    @FXML
    private AreaChart resMonCompRAM;

    public Button getButtonRenamePass() {
        return buttonRenamePass;
    }

    //Renames the selected pass by changing its nickname
    @FXML
    private Button buttonRenamePass;

    public Button getButtonRemovePass() {
        return buttonRemovePass;
    }

    //Removes a pass
    @FXML
    private Button buttonRemovePass;

    //Tree containing every pass and their properties
    @FXML
    private TreeView visionRecTreeView;
    //The area in which each individual property creates its settings
    @FXML
    private Pane vrPropertySettings;
    //where passes draw their preview image.
    @FXML
    private Pane vrPassPreview;
    //The following list views hold potential yet uncreated types of passes
    @FXML
    private ListView passCreatorSourcesView;
    @FXML
    private ListView passCreatorFormattingView;
    @FXML
    private ListView passCreatorImgprocView;
    @FXML
    private ListView passCreatorDebugView;

    public ListView getPassCreatorCoreView() {
        return passCreatorCoreView;
    }

    @FXML
    private ListView passCreatorCoreView;

    public ListView getPassCreatorDrawingView() {
        return passCreatorDrawingView;
    }

    @FXML
    private ListView passCreatorDrawingView;
    @FXML
    private Button buttonCreateNewPass;
    //Label used to display the amount of time that VR has been running for
    @FXML
    private Label vrUptimeClock;
    //Allows the VR system to keep all current data, but will not process more ticks automatically.
    @FXML
    private ToggleButton vrPauseButton;

    public Button getVrTickButton() {
        return vrTickButton;
    }

    public ToggleButton getVrPauseButton() {
        return vrPauseButton;
    }

    public Label getVrUptimeClock() {
        return vrUptimeClock;
    }

    //While paused or stopped, forces the system to process one more tick.
    @FXML
    private Button vrTickButton;

    public ToggleButton getVrStartStopToggle() {
        return vrStartStopToggle;
    }

    //Starts/Stops automatic processing
    @FXML
    private ToggleButton vrStartStopToggle;
    //Logger view area
    @FXML
    private TextArea logViewer;
    //Logger auto scroll checkbox
    @FXML
    private CheckBox logViewerAutoScroll;

    public ListView getPassCreatorSourcesView() {
        return passCreatorSourcesView;
    }

    public ListView getPassCreatorFormattingView() {
        return passCreatorFormattingView;
    }

    public ListView getPassCreatorImgprocView() {
        return passCreatorImgprocView;
    }

    public ListView getPassCreatorDebugView() {
        return passCreatorDebugView;
    }

    public Button getButtonCreateNewPass() {
        return buttonCreateNewPass;
    }

    public void setup() {
        visionRecTreeView.setRoot((new TreeItem<>("Vision Recognition Passes")));

        //Create settings panels on selection of new passes or properties
        visionRecTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    TreeItem selectedItem = (TreeItem) newValue;
                    try {
                        if (selectedItem.getValue() != null && selectedItem.getValue() instanceof PropertyBase) {
                            ((Treeable) selectedItem.getValue()).createSettingsPanel(vrPropertySettings);
                            ((Treeable) selectedItem.getParent().getValue()).createSettingsPanel(vrPassPreview);
                        }
                        else if (selectedItem.getValue() != null && selectedItem.getValue() instanceof PassBase) {
                            ((Treeable) selectedItem.getValue()).createSettingsPanel(vrPassPreview);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        Logger.setTextArea(logViewer, logViewerAutoScroll);
    }

    public TreeView getVisionRecTreeView() {
        return visionRecTreeView;
    }

    public AreaChart getResMonCompCPU() {
        return resMonCompCPU;
    }

    public AreaChart getResMonCompRAM() {
        return resMonCompRAM;
    }
}
