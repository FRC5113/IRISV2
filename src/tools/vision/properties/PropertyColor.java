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
        return v;
    }

    public double getS() {
        return s;
    }

    public double getH() {
        return h;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getG() {
        return g;
    }

    public double getR() {
        return r;
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
