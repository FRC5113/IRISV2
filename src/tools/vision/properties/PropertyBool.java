package tools.vision.properties;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

/**
 * Created by Jake on 4/27/2015.
 */
public class PropertyBool extends PropertyBase {

    private boolean value = false;
    private transient ToggleButton field;

    private PropertyBool() {
        super();
    }

    public boolean getValue()
    {
        return value;
    }

    public void setup()
    {
        field = new ToggleButton("" + value);


        field.selectedProperty().addListener((observable, oldValue, newValue) -> {
            setValue();
            field.setText("" + value);
        });
    }

    public PropertyBool(boolean initial)
    {
        this();
        value = initial;
        field.setSelected(value);
        field.setText("" + value);
    }

    @Override
    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
        p.getChildren().add(field);
    }

    private void setValue() {
        value = field.selectedProperty().get();
    }
}
