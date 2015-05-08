package tools.vision.properties;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import tools.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jake on 5/8/2015.
 */
public class PropertyDropdown extends PropertyBase {

    private List<String> choices;
    private String chosen;
    private transient ComboBox<String> box;

    public PropertyDropdown() {
        super();
    }

    public void setup()
    {
        box = new ComboBox<>();

        box.valueProperty().addListener((ov, t, t1) -> {
            chosen = box.getSelectionModel().getSelectedItem();
        });

        if(choices != null) {
            box.getItems().addAll(choices);
            box.getSelectionModel().select(chosen);
        }
    }

    public String getValue()
    {
        return chosen;
    }

    public PropertyDropdown(String[] potential)
    {
        this();
        chosen = potential[0];
        choices = Arrays.asList(potential);

        if(choices != null) {
            box.getItems().addAll(choices);
            box.getSelectionModel().select(chosen);
        }
    }

    @Override
    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
        p.getChildren().add(box);
    }




}
