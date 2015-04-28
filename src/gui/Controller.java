package gui;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import tools.Logger;
import tools.vision.Treeable;

import java.util.Optional;

/**
 * Created by Jake on 4/26/2015.
 * This class handles most of the interactions with the UI, and keeps track of its important components
 */
public class Controller {

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

    //Tree containing every pass and their properties
    @FXML
    private TreeView visionRecTreeView;
    //The area in which each individual property creates its settings, or where passes draw their preview image.
    @FXML
    private Pane vrPropertySettings;
    //The following list views hold potential yet uncreated types of passes
    @FXML
    private ListView passCreatorSourcesView;
    @FXML
    private ListView passCreatorFormattingView;
    @FXML
    private ListView passCreatorImgprocView;
    @FXML
    private ListView passCreatorDebugView;
    @FXML
    private Button buttonCreateNewPass;
    //Label used to display the amount of time that VR has been running for
    @FXML
    private Label vrUptimeClock;
    //Allows the VR system to keep all current data, but will not process more ticks automatically.
    @FXML
    private Button vrPauseButton;
    //While paused or stopped, forces the system to process one more tick.
    @FXML
    private Button vrTickButton;
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
                        if (selectedItem.getValue() != null && selectedItem.getValue() instanceof Treeable) {
                            ((Treeable) selectedItem.getValue()).createSettingsPanel(vrPropertySettings);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        Logger.setTextArea(logViewer, logViewerAutoScroll);
    }

    public void update() {
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