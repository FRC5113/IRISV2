package tools.vision.passes.formatting;

import gui.Controller;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyDropdown;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Jake on 4/26/2015.
 * Draws a grid over top of a material image
 * Serves little purpose in recognition, mostly used for driver assistance or testing
 */
public class PassImageFormatConverter extends PassBase implements Serializable {

    private PropertyDropdown typeFrom;
    private PropertyDropdown typeTo;

    public PassImageFormatConverter(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Type Converter", passes);

        typeFrom = new PropertyDropdown(new String[] {"BGR", "HSV", "HLS", "GRAY"});
        typeFrom.setNickname("Type from");
        getTreeItem().getChildren().add(typeFrom.getTreeItem());

        typeTo = new PropertyDropdown(new String[] {"BGR", "HSV", "HLS", "GRAY"});
        typeTo.setNickname("Type to");
        getTreeItem().getChildren().add(typeTo.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {
        if(typeFrom != null) {
            typeFrom.setup();
            getTreeItem().getChildren().add(typeFrom.getTreeItem());

            typeTo.setup();
            getTreeItem().getChildren().add(typeTo.getTreeItem());
        }
        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

        Mat output = new Mat();

        //Imgproc.cvtColor(mat, output, Imgproc.COLOR_GRAY2BGR);

        String from = typeFrom.getValue();
        String to = typeTo.getValue();

        switch(from)
        {


            case "BGR":
                preview = mat;
                output = preview.clone();
                switch (to)
                {
                    case "GRAY":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2GRAY);
                        break;
                    case "HSV":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2HSV);
                        break;
                    case "HLS":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2HLS);
                        break;
                }
                break;


            case "GRAY":
                Imgproc.cvtColor(mat, preview, Imgproc.COLOR_GRAY2BGR);
                output = preview.clone();
                switch (to)
                {
                    case "HLS":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2HLS);
                        break;
                    case "HSV":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2HSV);
                        break;
                }
                break;


            case "HLS":
                Imgproc.cvtColor(mat, preview, Imgproc.COLOR_HLS2BGR);
                output = preview.clone();
                switch (to)
                {
                    case "GRAY":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2GRAY);
                        break;
                    case "HSV":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2HSV);
                        break;
                }
                break;

            case "HSV":
                Imgproc.cvtColor(mat, preview, Imgproc.COLOR_HSV2BGR);
                output = preview.clone();
                switch (to)
                {
                    case "GRAY":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2GRAY);
                        break;
                    case "HLS":
                        Imgproc.cvtColor(mat, output, Imgproc.COLOR_BGR2HLS);
                        break;
                }
                break;

        }


        for(PassBase p : children.getValue())
        {
            p.process(output.clone());
        }
    }

}
