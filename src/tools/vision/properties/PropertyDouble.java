package tools.vision.properties;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import tools.Logger;

/**
 * Created by Jake on 4/27/2015.
 */
public class PropertyDouble extends PropertyBase {

    private double value = 0;
    private transient TextField field;

    private PropertyDouble() {
        super();
    }

    public double getValue()
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
                Logger.log("Error, not a number value.");
                field.setText("" + value);
            }
        });
    }

    public PropertyDouble(double initial)
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
        value = Double.parseDouble(field.getText());
    }
}
