package tools.vision.passes.imgproc;

import gui.Controller;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyDouble;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Jake on 4/26/2015.
 * Draws a grid over top of a material image
 * Serves little purpose in recognition, mostly used for driver assistance or testing
 */
public class PassEdgeSobel extends PassBase implements Serializable {

    private PropertyDouble delta;
    private PropertyDouble scale;

    public PassEdgeSobel(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Sobel Edge Detection", passes);

        delta = new PropertyDouble(0);
        delta.setNickname("Delta");
        getTreeItem().getChildren().add(delta.getTreeItem());

        scale = new PropertyDouble(1);
        scale.setNickname("Scale");
        getTreeItem().getChildren().add(scale.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {

        if(delta != null) {
            delta.setup();
            getTreeItem().getChildren().add(delta.getTreeItem());

            scale.setup();
            getTreeItem().getChildren().add(scale.getTreeItem());
        }

        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

        int ddepth = CvType.CV_16S;

        /// Convert it to gray
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

        /// Generate grad_x and grad_y
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();

        /// Gradient X
        //Scharr( src_gray, grad_x, ddepth, 1, 0, scale, delta, BORDER_DEFAULT );
        Imgproc.Sobel(mat, grad_x, ddepth, 1, 0, 3, scale.getValue(), delta.getValue(), Core.BORDER_DEFAULT);
        Core.convertScaleAbs(grad_x, abs_grad_x);

        /// Gradient Y
        //Scharr( src_gray, grad_y, ddepth, 0, 1, scale, delta, BORDER_DEFAULT );
        Imgproc.Sobel(mat, grad_y, ddepth, 0, 1, 3, scale.getValue(), delta.getValue(), Core.BORDER_DEFAULT);
        Core.convertScaleAbs(grad_y, abs_grad_y);

        /// Total Gradient (approximate)
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, mat);

        preview = new Mat();
        Imgproc.cvtColor(mat, preview, Imgproc.COLOR_GRAY2BGR);

        for(PassBase p : children.getValue())
        {
            p.process(mat.clone());
        }
    }

}