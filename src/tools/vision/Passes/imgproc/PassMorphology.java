package tools.vision.passes.imgproc;

/**
 * Created by Jake on 5/16/2015.
 http://docs.opencv.org/doc/tutorials/imgproc/opening_closing_hats/opening_closing_hats.html#morphology-2
 */

import gui.Controller;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import tools.Logger;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyBool;
import tools.vision.properties.PropertyDropdown;
import tools.vision.properties.PropertyInteger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PassMorphology extends PassBase implements Serializable {

    private PropertyInteger size;
    private PropertyDropdown morphologyType;
    private PropertyDropdown structureType;


    public PassMorphology(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Morphology", passes);

        size = new PropertyInteger(10);
        size.setNickname("Size");
        getTreeItem().getChildren().add(size.getTreeItem());

        morphologyType = new PropertyDropdown(new String[] {"Erode", "Dilate", "Open", "Close", "Gradient", "Top Hat", "Black Hat"});
        morphologyType.setNickname("Morphology type");
        getTreeItem().getChildren().add(morphologyType.getTreeItem());

        structureType = new PropertyDropdown(new String[] {"Rect", "Cross", "Ellipse"});
        structureType.setNickname("Structure type");
        getTreeItem().getChildren().add(structureType.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {

        if(size != null) {
            size.setup();
            getTreeItem().getChildren().add(size.getTreeItem());

            morphologyType.setup();
            getTreeItem().getChildren().add(morphologyType.getTreeItem());

            structureType.setup();
            getTreeItem().getChildren().add(structureType.getTreeItem());
        }

        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

        Mat morphed = new Mat();

        int morphTypeInt = 0;
        switch(morphologyType.getValue())
        {
            case "Erode":
                morphTypeInt = Imgproc.MORPH_ERODE;
                break;
            case "Dilate":
                morphTypeInt = Imgproc.MORPH_DILATE;
                break;
            case "Open":
                morphTypeInt = Imgproc.MORPH_OPEN;
                break;
            case "Close":
                morphTypeInt = Imgproc.MORPH_CLOSE;
                break;
            case "Gradient":
                morphTypeInt = Imgproc.MORPH_GRADIENT;
                break;
            case "Top Hat":
                morphTypeInt = Imgproc.MORPH_TOPHAT;
                break;
            case "Black Hat":
                morphTypeInt = Imgproc.MORPH_BLACKHAT;
                break;
        }

        int structureTypeInt = 0;
        switch(structureType.getValue())
        {
            case "Rect":
                structureTypeInt = 0;
                break;
            case "Cross":
                structureTypeInt = 1;
                break;
            case "Ellipse":
                structureTypeInt = 2;
                break;
        }



        try {
            Imgproc.morphologyEx(mat, morphed, morphTypeInt, Imgproc.getStructuringElement(structureTypeInt, new Size(size.getValue(), size.getValue())));
        }
        catch (CvException e)
        {
            e.printStackTrace();
            Logger.logln(e.toString());
        }

        preview = morphed.clone();
        for(PassBase p : children.getValue())
        {
            p.process(morphed.clone());
        }

    }

}