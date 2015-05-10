package tools.vision.passes.debug;

import gui.Controller;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyBool;
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
    public PropertyBool enableBlue;
    public PropertyBool enableGreen;
    public PropertyBool enableRed;


    public PassHistogram(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("BGR Debug Histogram", passes);

        bins = new PropertyInteger(256);
        bins.setNickname("Graph reference point count");
        getTreeItem().getChildren().add(bins.getTreeItem());

        enableBlue = new PropertyBool(true);
        enableBlue.setNickname("Allow blue");
        getTreeItem().getChildren().add(enableBlue.getTreeItem());

        enableGreen = new PropertyBool(true);
        enableGreen.setNickname("Allow green");
        getTreeItem().getChildren().add(enableGreen.getTreeItem());

        enableRed = new PropertyBool(true);
        enableRed.setNickname("Allow red");
        getTreeItem().getChildren().add(enableRed.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {

        if(bins != null) {
            bins.setup();
            getTreeItem().getChildren().add(bins.getTreeItem());

            enableBlue.setup();
            getTreeItem().getChildren().add(enableBlue.getTreeItem());

            enableGreen.setup();
            getTreeItem().getChildren().add(enableGreen.getTreeItem());

            enableRed.setup();
            getTreeItem().getChildren().add(enableRed.getTreeItem());
        }

        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {


        /// Separate the image in 3 places ( B, G and R )
        ArrayList<Mat> bgr_planes = new ArrayList<>();
        Core.split(mat, bgr_planes);

        MatOfFloat ranges=new MatOfFloat(0,256);
        MatOfInt histSize = new MatOfInt(bins.getValue());

        boolean uniform = true;
        boolean accumulate = false;

        Mat b_hist = new Mat();
        Mat g_hist = new Mat();
        Mat r_hist = new Mat();

        /// Compute the histograms:
        if(enableBlue.getValue())
            Imgproc.calcHist(bgr_planes, new MatOfInt(0), new Mat(), b_hist, histSize, ranges);
        if(enableGreen.getValue())
            Imgproc.calcHist(bgr_planes, new MatOfInt(1), new Mat(), g_hist, histSize, ranges);
        if(enableRed.getValue())
            Imgproc.calcHist(bgr_planes, new MatOfInt(2), new Mat(), r_hist, histSize, ranges);

        // Draw the histograms for B, G and R
        int hist_w = bins.getValue();
        int hist_h = hist_w;

        Mat histImage = new Mat( hist_h, hist_w, CvType.CV_8UC3, new Scalar( 200,200,200) );

        /// Normalize the result to [ 0, histImage.rows ]
        if(enableBlue.getValue())
            Core.normalize(b_hist, b_hist, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        if(enableGreen.getValue())
            Core.normalize(g_hist, g_hist, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        if(enableRed.getValue())
            Core.normalize(r_hist, r_hist, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());


        /// Draw for each channel
        for( int i = 1; i < bins.getValue(); i++ )
        {
            if(enableBlue.getValue())

            Imgproc.line(
                    histImage,
                    new org.opencv.core.Point( i, histImage.rows() ),
                    new org.opencv.core.Point( i, histImage.rows()-Math.round( b_hist.get(i,0)[0] )) ,
                    new Scalar( 255, 0, 0),
                    2);

            if(enableGreen.getValue())

            Imgproc.line(
                    histImage,
                    new org.opencv.core.Point( i, histImage.rows() ),
                    new org.opencv.core.Point( i, histImage.rows()-Math.round( g_hist.get(i,0)[0] )) ,
                    new Scalar( 0, 255, 0),
                    2);

            if(enableRed.getValue())

            Imgproc.line(
                    histImage,
                    new org.opencv.core.Point( i, histImage.rows() ),
                    new org.opencv.core.Point( i, histImage.rows()-Math.round( r_hist.get(i,0)[0] )) ,
                    new Scalar( 0, 0, 255),
                    2);

        }


        preview = histImage.clone();

        for(PassBase p : children.getValue())
        {
            p.process(histImage.clone());
        }

    }

}