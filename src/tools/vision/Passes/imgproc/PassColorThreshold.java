package tools.vision.passes.imgproc;

import gui.Controller;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyColor;
import tools.vision.properties.PropertyDropdown;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Jake on 4/26/2015.
 * Draws a grid over top of a material image
 * Serves little purpose in recognition, mostly used for driver assistance or testing
 */
public class PassColorThreshold extends PassBase implements Serializable {

    public PropertyDropdown colorChoice;
    public PropertyColor colorLow;
    public PropertyColor colorHigh;

    public PassColorThreshold(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Color Threshold", passes);

        colorChoice = new PropertyDropdown(new String[] {"BGR", "HSV"});
        colorChoice.setNickname("Color type");
        getTreeItem().getChildren().add(colorChoice.getTreeItem());

        colorLow = new PropertyColor();
        colorLow.setNickname("Low Color");
        getTreeItem().getChildren().add(colorLow.getTreeItem());

        colorHigh = new PropertyColor();
        colorHigh.setNickname("High Color");
        getTreeItem().getChildren().add(colorHigh.getTreeItem());
    }

    public void setup(Controller c, List<PassBase> passes)
    {
        if(colorChoice != null) {
            colorChoice.setup();
            getTreeItem().getChildren().add(colorChoice.getTreeItem());

            colorLow.setup();
            getTreeItem().getChildren().add(colorLow.getTreeItem());

            colorHigh.setup();
            getTreeItem().getChildren().add(colorHigh.getTreeItem());
        }
        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

        Mat temp;
        temp = mat.clone();

        switch (colorChoice.getValue())
        {


            case "BGR":
                /*Core.inRange(mat,
                        new Scalar(colorLow.getB(), colorLow.getG(), colorLow.getR()),
                        new Scalar(colorHigh.getB(), colorLow.getG(), colorHigh.getR()),
                        temp);
                */
                Core.inRange(mat,
                        new Scalar(colorLow.getB(), colorLow.getG(), colorLow.getR()),
                        new Scalar(colorHigh.getB(), colorHigh.getG(), colorHigh.getR()),
                        temp);
                break;
            case "HSV":
                Core.inRange(mat,
                        new Scalar(colorLow.getH(), colorLow.getS(), colorLow.getV()),
                        new Scalar(colorHigh.getH(), colorHigh.getS(), colorHigh.getV()),
                        temp);
                break;
        }

        preview = new Mat();

        Imgproc.cvtColor(temp, preview, Imgproc.COLOR_GRAY2BGR);

        for(PassBase p : children.getValue())
        {
            p.process(temp.clone());
        }
    }

}
