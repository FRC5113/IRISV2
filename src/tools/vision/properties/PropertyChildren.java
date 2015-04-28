package tools.vision.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import tools.vision.Passes.PassBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 4/27/2015.
 * Keeps track of which passes to send the processed image to after the parent pass of this property is done with its pass.
 */
public class PropertyChildren extends PropertyBase {

    private ObservableList<PassBase> children;
    private List<PassBase> passes;

    private Label childrenLabel;
    private Label unAddedLabel;

    private ListView childrenListView;
    private ListView unAddedListView;

    private Button addButton;
    private Button removeButton;

    private PassBase parentPass;


    public PropertyChildren(PassBase parent, List<PassBase> passes) {

        super();

        //Keep track of this property's parent so that way we don't add it to its own children property
        this.parentPass = parent;

        //Create the child array
        children = FXCollections.observableArrayList();

        setNickname("Children");

        //Keep a reference of the current list of passes for refreshing later on
        this.passes = passes;

        childrenLabel = new Label("Current children");
        childrenLabel.setLayoutX(20);
        childrenLabel.setLayoutY(20);

        childrenListView = new ListView<PassBase>();
        childrenListView.setLayoutY(40);
        childrenListView.setLayoutX(20);
        childrenListView.setMaxHeight(180);
        childrenListView.setMaxWidth(300);

        removeButton = new Button("Remove selected pass from children");
        removeButton.setLayoutX(20);
        removeButton.setLayoutY(230);

        //On click, remove one item from the children list and add it to the unadded list
        removeButton.setOnAction(event -> {
            if (childrenListView.getSelectionModel().getSelectedItem() != null)
                children.remove(childrenListView.getSelectionModel().getSelectedItem());
            refreshUnusedChildrenList();
        });


        unAddedLabel = new Label("Unadded Passes");
        unAddedLabel.setLayoutY(270);
        unAddedLabel.setLayoutX(20);

        unAddedListView = new ListView<PassBase>();
        unAddedListView.setLayoutY(290);
        unAddedListView.setMaxHeight(180);
        unAddedListView.setMaxWidth(300);
        unAddedListView.setLayoutX(20);

        addButton = new Button("Add selected pass to children");
        addButton.setLayoutY(490);
        addButton.setLayoutX(20);

        //On click, remove one item from the unadded list and add it to children
        addButton.setOnAction(event -> {
            if (unAddedListView.getSelectionModel().getSelectedItem() != null)
                children.add((PassBase) unAddedListView.getSelectionModel().getSelectedItem());
            refreshUnusedChildrenList();
        });

        refreshUnusedChildrenList();
    }

    private void refreshUnusedChildrenList() {

        ObservableList<PassBase> unused;
        unused = FXCollections.observableArrayList();

        List<PassBase> unusedList = new ArrayList<>(passes);
        unusedList.removeAll(children);
        unusedList.remove(parentPass);

        unused.addAll(unusedList);

        childrenListView.setItems(children);
        unAddedListView.setItems(unused);
    }

    @Override
    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
        p.getChildren().add(childrenLabel);
        p.getChildren().add(childrenListView);
        p.getChildren().add(removeButton);

        p.getChildren().add(unAddedLabel);
        p.getChildren().add(unAddedListView);
        p.getChildren().add(addButton);

        refreshUnusedChildrenList();
    }

}
