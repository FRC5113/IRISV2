package tools.vision.passes.imgproc;

/**
 * Created by Jake on 5/16/2015.
 http://docs.opencv.org/doc/tutorials/imgproc/erosion_dilatation/erosion_dilatation.html#morphology-1
 */

import gui.Controller;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyBool;
import tools.vision.properties.PropertyDropdown;
import tools.vision.properties.PropertyInteger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PassErode extends PassBase implements Serializable {

    private PropertyInteger size;
    private PropertyDropdown erosionType;

    public PassErode(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Erode", passes);

        size = new PropertyInteger(10);
        size.setNickname("Size");
        getTreeItem().getChildren().add(size.getTreeItem());

        erosionType = new PropertyDropdown(new String[] {"Rect", "Cross", "Ellipse"});
        erosionType.setNickname("Erosion type");
        getTreeItem().getChildren().add(erosionType.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {

        if(size != null) {
            size.setup();
            getTreeItem().getChildren().add(size.getTreeItem());

            erosionType.setup();
            getTreeItem().getChildren().add(erosionType.getTreeItem());
        }

        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

        Mat erroded = new Mat();

        int erosion_type_int = 0;
        switch(erosionType.getValue())
        {
            case "Rect":
                erosion_type_int = 0;
                break;
            case "Cross":
                erosion_type_int = 1;
                break;
            case "Ellipse":
                erosion_type_int = 2;
                break;
        }

        Mat element = Imgproc.getStructuringElement(erosion_type_int,
                new Size(2 * size.getValue() + 1, 2 * size.getValue() + 1),
                new Point(size.getValue(), size.getValue()) );

        /// Apply the erosion operation
        Imgproc.erode(mat, erroded, element );

        preview = erroded.clone();
        for(PassBase p : children.getValue())
        {
            p.process(erroded.clone());
        }

    }

}