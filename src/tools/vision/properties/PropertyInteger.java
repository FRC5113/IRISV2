package tools.vision.properties;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import tools.Logger;

/**
 * Created by Jake on 4/27/2015.
 */
public class PropertyInteger extends PropertyBase {

    private int value = 0;
    private transient TextField field;

    public PropertyInteger() {
        super();
    }

    public int getValue()
    {
        return value;
    }

    public void setup()
    {
        field = new TextField("" + value);


        field.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setValue();
            } catch (Exception e) {
                Logger.log("Error, not an integer value.");
                field.setText("" + value);
            }
        });
    }

    public PropertyInteger(int initial)
    {
        this();
        value = initial;
        field.setText("" + value);
    }

    @Override
    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
        p.getChildren().add(field);
    }

    private void setValue() {
        value = Integer.parseInt(field.getText());
    }
}
