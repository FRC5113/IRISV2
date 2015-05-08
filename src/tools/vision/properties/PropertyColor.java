package tools.vision.properties;

import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by Jake on 4/27/2015.
 */
public class PropertyColor extends PropertyBase {

    //Red, Green, Blue
    private double r = 255;
    private double g = 255;
    private double b = 255;

    //Alpha/Opacity
    private double a = 255;

    public double getV() {
        return v * 255;
    }

    public double getS() {
        return s * 255;
    }

    public double getH() {
        return h* 255;
    }

    public double getA() {
        return a* 255;
    }

    public double getB() {
        return b * 255;
    }

    public double getG() {
        return g * 255;
    }

    public double getR() {
        return r * 255;
    }

    //Hue, Saturation, Brightness/Value
    private double h = 1;
    private double s = 1;
    private double v = 1;

    private transient ColorPicker picker;

    public void setup()
    {
        picker = new ColorPicker();
        picker.setValue(new Color(r, g, b, a));

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
