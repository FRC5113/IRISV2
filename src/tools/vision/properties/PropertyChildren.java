package tools.vision.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import tools.vision.passes.PassBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jake on 4/27/2015.
 * Keeps track of which passes to send the processed image to after the parent pass of this property is done with its pass.
 */
public class PropertyChildren extends PropertyBase  implements Serializable {

    private transient ObservableList<PassBase> children;
    private List<PassBase> passes;
    private List<PassBase> childrenPassesSerialized;

    private transient Label childrenLabel;
    private transient Label unAddedLabel;

    private transient ListView childrenListView;
    private transient ListView unAddedListView;

    private transient Button addButton;
    private transient Button removeButton;

    private PassBase parentPass;

    public PropertyChildren(PassBase parent, List<PassBase> passes) {
        //Keep track of this property's parent so that way we don't add it to its own children property
        this.passes = passes;
        this.parentPass = parent;
        childrenPassesSerialized = new ArrayList<>(2);
        setup();
    }

    public List<PassBase> getValue()
    {
        return childrenPassesSerialized;
    }

    public void setup()
    {
        if(passes != null)
        {
            //Create the child array
            children = FXCollections.observableArrayList();

            setNickname("Children");

            childrenLabel = new Label("Current children");

            childrenListView = new ListView<PassBase>();
            childrenListView.setLayoutY(20);
            childrenListView.setMaxHeight(180);
            childrenListView.setMaxWidth(300);

            removeButton = new Button("Remove selected pass from children");
            removeButton.setLayoutY(210);

            //On click, remove one item from the children list and add it to the unadded list
            removeButton.setOnAction(event -> {
                if (childrenListView.getSelectionModel().getSelectedItem() != null)
                    childrenPassesSerialized.remove(childrenListView.getSelectionModel().getSelectedItem());
                refreshUnusedChildrenList();
            });


            unAddedLabel = new Label("Unadded Passes");
            unAddedLabel.setLayoutX(310);

            unAddedListView = new ListView<PassBase>();
            unAddedListView.setLayoutX(310);
            unAddedListView.setLayoutY(20);
            unAddedListView.setMaxHeight(180);
            unAddedListView.setMaxWidth(300);

            addButton = new Button("Add selected pass to children");
            addButton.setLayoutX(310);
            addButton.setLayoutY(210);

            //On click, remove one item from the unadded list and add it to children
            addButton.setOnAction(event -> {
                if (unAddedListView.getSelectionModel().getSelectedItem() != null)
                    childrenPassesSerialized.add((PassBase) unAddedListView.getSelectionModel().getSelectedItem());
                refreshUnusedChildrenList();
            });

            refreshUnusedChildrenList();

        }
    }

    private void refreshUnusedChildrenList() {

        ObservableList<PassBase> unused;
        unused = FXCollections.observableArrayList();

        List<PassBase> unusedList = new ArrayList<>(passes);
        unusedList.removeAll(childrenPassesSerialized);
        unusedList.remove(parentPass);
        unused.addAll(unusedList);

        children.setAll(childrenPassesSerialized);

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
