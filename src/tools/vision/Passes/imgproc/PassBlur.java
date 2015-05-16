package tools.vision.passes.imgproc;

/**
 * Created by Jake on 5/16/2015.
 * Blurs the image using either Normalized Block Filter, Gaussian Filter, Median Filter, or Bilateral Filter
 * http://docs.opencv.org/doc/tutorials/imgproc/gausian_median_blur_bilateral_filter/gausian_median_blur_bilateral_filter.html
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

public class PassBlur extends PassBase implements Serializable {

    private PropertyInteger kernels;
    private PropertyDropdown blurType;

    public PassBlur(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Blur tool", passes);

        kernels = new PropertyInteger(31);
        kernels.setNickname("Max Kernel Length ");
        getTreeItem().getChildren().add(kernels.getTreeItem());

        blurType = new PropertyDropdown(new String[] {"Normalized Box Filter", "Gaussian Filter", "Median Filter", "Bilateral Filter"});
        blurType.setNickname("Filter type");
        getTreeItem().getChildren().add(blurType.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {

        if(kernels != null) {
            kernels.setup();
            getTreeItem().getChildren().add(kernels.getTreeItem());

            blurType.setup();
            getTreeItem().getChildren().add(blurType.getTreeItem());
        }

        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

        Mat blurred = new Mat();


        for ( int i = 1; i < kernels.getValue(); i = i + 2 )
        {
            switch (blurType.getValue())
            {
                case "Normalized Box Filter":
                    Imgproc.blur(mat, blurred, new Size(i, i), new Point(-1, -1));
                    break;
                case "Gaussian Filter":
                    Imgproc.GaussianBlur(mat, blurred, new Size(i, i), 0, 0);
                    break;
                case "Median Filter":
                    Imgproc.medianBlur(mat, blurred, i);
                    break;
                case "Bilateral Filter":
                    Imgproc.bilateralFilter(mat, blurred, i, i * 2, i / 2);
                    break;

            }

        }

        preview = blurred.clone();
        for(PassBase p : children.getValue())
        {
            p.process(blurred.clone());
        }

    }

}