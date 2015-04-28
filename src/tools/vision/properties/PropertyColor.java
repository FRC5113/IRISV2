package tools.vision.properties;

import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;

/**
 * Created by Jake on 4/27/2015.
 */
public class PropertyColor extends PropertyBase {

    //Red, Green, Blue
    private double r = 1;
    private double g = 1;
    private double b = 1;

    //Alpha/Opacity
    private double a = 1;

    //Hue, Saturation, Brightness/Value
    private double h = 1;
    private double s = 1;
    private double v = 1;

    private ColorPicker picker;

    public PropertyColor() {
        picker = new ColorPicker();

        picker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setValue();
            } catch (Exception e) {
            }
        });
    }

    @Override
    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
        p.getChildren().add(picker);
    }

    private void setValue() {
        r = picker.valueProperty().get().getRed();
        g = picker.valueProperty().get().getGreen();
        b = picker.valueProperty().get().getBlue();

        a = picker.valueProperty().get().getOpacity();

        h = picker.valueProperty().get().getHue();
        s = picker.valueProperty().get().getSaturation();
        v = picker.valueProperty().get().getBrightness();
    }

}
