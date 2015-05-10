package tools.vision.passes.debug;

import gui.Controller;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import tools.Logger;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyColor;
import tools.vision.properties.PropertyDouble;
import tools.vision.properties.PropertyInteger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jake on 4/26/2015.
 * Draws a grid over top of a material image
 * Serves little purpose in recognition, mostly used for driver assistance or testing
 */
public class PassHistogram extends PassBase implements Serializable {

    public PropertyInteger bins;

    public PassHistogram(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("BGR Debug Histogram", passes);

        bins = new PropertyInteger(256);
        bins.setNickname("Graph reference point count");
        getTreeItem().getChildren().add(bins.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {

        if(bins != null) {
            bins.setup();
            getTreeItem().getChildren().add(bins.getTreeItem());
        }

        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

        /*
        /// Separate the image in 3 places ( B, G and R )
        ArrayList<Mat> bgr_planes;
        Core.split(mat, bgr_planes);

        MatOfFloat ranges=new MatOfFloat(0,256);
        MatOfInt histSize = new MatOfInt(bins.getValue());

        boolean uniform = true;
        boolean accumulate = false;

        Mat b_hist, g_hist, r_hist;

        /// Compute the histograms:
        Imgproc.calcHist(bgr_planes, new MatOfInt(0), new Mat(), b_hist, histSize, ranges);
        Imgproc.calcHist(bgr_planes, new MatOfInt(0), new Mat(), g_hist, histSize, ranges);
        Imgproc.calcHist(bgr_planes, new MatOfInt(0), new Mat(), r_hist, histSize, ranges);

        // Draw the histograms for B, G and R
        int hist_w = 512; int hist_h = 512;
        int bin_w = (int) Math.round((double) (hist_w / bins.getValue()));

        Mat histImage = new Mat( hist_h, hist_w, CvType.CV_8UC3, new Scalar( 0,0,0) );

        /// Normalize the result to [ 0, histImage.rows ]
        Core.normalize(b_hist, b_hist, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        Core.normalize(g_hist, g_hist, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        Core.normalize(r_hist, r_hist, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());


        /// Draw for each channel
        for( int i = 1; i < histSize; i++ )
        {
            line( histImage, Point( bin_w*(i-1), hist_h - cvRound(b_hist.at<float>(i-1)) ) ,
            Point( bin_w*(i), hist_h - cvRound(b_hist.at<float>(i)) ),
            Scalar( 255, 0, 0), 2, 8, 0  );
            line( histImage, Point( bin_w*(i-1), hist_h - cvRound(g_hist.at<float>(i-1)) ) ,
            Point( bin_w*(i), hist_h - cvRound(g_hist.at<float>(i)) ),
            Scalar( 0, 255, 0), 2, 8, 0  );
            line( histImage, Point( bin_w*(i-1), hist_h - cvRound(r_hist.at<float>(i-1)) ) ,
            Point( bin_w*(i), hist_h - cvRound(r_hist.at<float>(i)) ),
            Scalar( 0, 0, 255), 2, 8, 0  );
        }


        preview = mat.clone();

        for(PassBase p : children.getValue())
        {
            p.process(mat.clone());
        }
        */
    }

}