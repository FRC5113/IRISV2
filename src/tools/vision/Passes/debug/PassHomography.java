package tools.vision.passes.debug;

import gui.Controller;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
import tools.Logger;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyBool;
import tools.vision.properties.PropertyDropdown;
import tools.vision.properties.PropertyImageFile;
import tools.vision.properties.PropertyInteger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jake on 5/17/2015.
 * One of the more complex passes
 * Still in testing, not yet for practical use
 */

public class PassHomography extends PassBase implements Serializable {

    private PropertyImageFile goal;
    private PropertyBool convertToGrayscale;

    public PassHomography(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("ORB Object Detection", passes);

        goal = new PropertyImageFile();
        goal.setNickname("Image searched for");
        getTreeItem().getChildren().add(goal.getTreeItem());

        convertToGrayscale = new PropertyBool(true);
        convertToGrayscale.setNickname("Convert to Grayscale");
        getTreeItem().getChildren().add(convertToGrayscale.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {

        if(goal != null) {
            goal.setup();
            getTreeItem().getChildren().add(goal.getTreeItem());
        }

        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    /*
    @Override
    public void process(Mat mat) {

        Mat objectImg = goal.getValue().clone();
        Mat sceneImg = mat.clone();
        Mat output = sceneImg.clone();


        MatOfKeyPoint keypoints_scene = new MatOfKeyPoint();
        MatOfKeyPoint keypoints_object = new MatOfKeyPoint();

        if(convertToGrayscale.getValue())
        {
            Imgproc.cvtColor(sceneImg, sceneImg, Imgproc.COLOR_BGR2GRAY);
            Imgproc.cvtColor(objectImg, objectImg, Imgproc.COLOR_BGR2GRAY);
        }

        FeatureDetector detector = FeatureDetector.create(FeatureDetector.BRISK);
        detector.detect(objectImg, keypoints_object);
        detector.detect(sceneImg, keypoints_scene);

        DescriptorExtractor extractor;
        extractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);

        Mat descriptor_object = new Mat();
        Mat descriptor_scene = new Mat();

        extractor.compute(objectImg, keypoints_object, descriptor_object);
        extractor.compute(sceneImg, keypoints_scene, descriptor_scene);

        DescriptorMatcher matcher;
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptor_object, descriptor_scene, matches);
        List<DMatch> matchesList = matches.toList();

        Double max_dist = 0.0;
        Double min_dist = 100.0;

        for(int i = 0; i < descriptor_object.rows(); i++){
            Double dist = (double) matchesList.get(i).distance;
            if(dist < min_dist) min_dist = dist;
            if(dist > max_dist) max_dist = dist;
        }

        System.out.println("-- Max dist : " + max_dist);
        System.out.println("-- Min dist : " + min_dist);

        //-- Draw only "good" matches (i.e. whose distance is less than 3*min_dist )
        List<DMatch> good_matches = new ArrayList<>();
        MatOfDMatch gm = new MatOfDMatch();

        for(int i = 0; i < descriptor_object.rows(); i++){
            if(matchesList.get(i).distance < 3*min_dist){
                good_matches.add(matchesList.get(i));
            }
        }

        gm.fromList(good_matches);


        Mat img_matches = new Mat();
        Features2d.drawMatches(
                objectImg,
                keypoints_object,
                sceneImg,
                keypoints_scene,
                gm,
                img_matches,
                new Scalar(255,0,0),
                new Scalar(0,0,255),
                new MatOfByte(),
                2);



        List<Point> objList = new ArrayList<>();
        List<Point> sceneList = new ArrayList<>();

        List<KeyPoint> keypoints_objectList = keypoints_object.toList();
        List<KeyPoint> keypoints_sceneList = keypoints_scene.toList();

        for(int i = 0; i<good_matches.size(); i++){
            objList.add(keypoints_objectList.get(good_matches.get(i).queryIdx).pt);
            sceneList.add(keypoints_sceneList.get(good_matches.get(i).trainIdx).pt);
        }

        MatOfPoint2f obj = new MatOfPoint2f();
        obj.fromList(objList);

        MatOfPoint2f scene = new MatOfPoint2f();
        scene.fromList(sceneList);

        Mat hg = Calib3d.findHomography(obj, scene, 8, 10);

        Mat obj_corners = new Mat(4,1,CvType.CV_32FC2);
        Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);


        obj_corners.put(0, 0, new double[]{0, 0});
        obj_corners.put(1, 0, new double[]{objectImg.cols(), 0});
        obj_corners.put(2, 0, new double[]{objectImg.cols(), objectImg.rows()});
        obj_corners.put(3, 0, new double[]{0, objectImg.rows()});


        try {
            Core.perspectiveTransform(obj_corners, scene_corners, hg);
            //org.opencv.core.Size ims = new org.opencv.core.Size(mat.cols(),mat.rows());
            //Imgproc.warpPerspective(output, output, hg, ims);
        }
        catch (CvException e)
        {
            e.printStackTrace();
        }



        if(convertToGrayscale.getValue())
        {
            Imgproc.cvtColor(sceneImg, sceneImg, Imgproc.COLOR_GRAY2BGR);
        }



        if(good_matches.size() > 0) {
            Imgproc.line(sceneImg, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(sceneImg, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(sceneImg, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(sceneImg, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);
        }
        preview = sceneImg.clone();
        for(PassBase p : children.getValue())
        {
            p.process(sceneImg.clone());
        }

    }
*/

    @Override
    public void process(Mat mat) {

        /*
        Mat img2 = goal.getValue().clone();
        Mat img1 = mat.clone();

            FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
            DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
            DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);

            //set up img1 (scene)
            Mat descriptors1 = new Mat();
            MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
            //calculate descriptor for img1
            detector.detect(img1, keypoints1);
            descriptor.compute(img1, keypoints1, descriptors1);

            //set up img2 (template)
            Mat descriptors2 = new Mat();
            MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
            //calculate descriptor for img2
            detector.detect(img2, keypoints2);
            descriptor.compute(img2, keypoints2, descriptors2);

            //match 2 images' descriptors
            MatOfDMatch matches = new MatOfDMatch();
            matcher.match(descriptors1, descriptors2,matches);

            //calculate max and min distances between keypoints
            double max_dist=0;double min_dist=99;

            List<DMatch> matchesList = matches.toList();
            for(int i=0;i<descriptors1.rows();i++)
            {
                double dist = matchesList.get(i).distance;
                if (dist<min_dist) min_dist = dist;
                if (dist>max_dist) max_dist = dist;
            }

            //set up good matches, add matches if close enough
            LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
            MatOfDMatch gm = new MatOfDMatch();
            for (int i=0;i<descriptors2.rows();i++)
            {
                if(matchesList.get(i).distance<3*min_dist)
                {
                    good_matches.addLast(matchesList.get(i));
                }
            }
            gm.fromList(good_matches);

            //put keypoints mats into lists
            List<KeyPoint> keypoints1_List = keypoints1.toList();
            List<KeyPoint> keypoints2_List = keypoints2.toList();

            //put keypoints into point2f mats so calib3d can use them to find homography
            LinkedList<Point> objList = new LinkedList<Point>();
            LinkedList<Point> sceneList = new LinkedList<Point>();
            for(int i=0;i<good_matches.size();i++)
            {
                objList.addLast(keypoints2_List.get(good_matches.get(i).trainIdx).pt);
                sceneList.addLast(keypoints1_List.get(good_matches.get(i).queryIdx).pt);
            }
            MatOfPoint2f obj = new MatOfPoint2f();
            MatOfPoint2f scene = new MatOfPoint2f();
            obj.fromList(objList);
            scene.fromList(sceneList);

            //output image
            Mat outputImg = new Mat();
            MatOfByte drawnMatches = new MatOfByte();
            Features2d.drawMatches(img1, keypoints1, img2, keypoints2, gm, outputImg);//, Scalar.all(-1), Scalar.all(-1), drawnMatches,Features2d.NOT_DRAW_SINGLE_POINTS);

            //run homography on object and scene points
        try {
            Mat H = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 5);
            Mat tmp_corners = new Mat(4, 1, CvType.CV_32FC2);
            Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

            //get corners from object
            tmp_corners.put(0, 0, new double[]{0, 0});
            tmp_corners.put(1, 0, new double[]{img2.cols(), 0});
            tmp_corners.put(2, 0, new double[]{img2.cols(), img2.rows()});
            tmp_corners.put(3, 0, new double[]{0, img2.rows()});

            Core.perspectiveTransform(tmp_corners, scene_corners, H);

            Imgproc.line(outputImg, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(outputImg, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(outputImg, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            Imgproc.line(outputImg, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(0,0)), new Scalar(0, 255, 0),4);
        }
        catch (CvException e)
        {
            e.printStackTrace();
        }

        */





        /*
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);

        //first image
        Mat img1 = mat.clone();
        Mat descriptors1 = new Mat();
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();

        detector.detect(img1, keypoints1);
        descriptor.compute(img1, keypoints1, descriptors1);

        //second image
        Mat img2 = goal.getValue().clone();
        Mat descriptors2 = new Mat();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

        detector.detect(img2, keypoints2);
        descriptor.compute(img2, keypoints2, descriptors2);

        //matcher should include 2 different image's descriptors
        MatOfDMatch  matches = new MatOfDMatch();
        matcher.match(descriptors1,descriptors2,matches);



        List<DMatch> matchesList = matches.toList();
        double max_dist =0, min_dist= 100;
        int row_count = matchesList.size();
        for(int i=0;i<row_count;i++)
        {
            double dist = matchesList.get(i).distance;
            //System.out.println("dist="+dist);
            if(dist<min_dist)min_dist = dist;
            if(dist>max_dist)max_dist = dist;


        }


        // Log.e("Max_dist,Min_dist", "Max="+max_dist+", Min="+min_dist);
        List<DMatch> good_matches = new ArrayList<DMatch>();

        double good_dist = 3*min_dist;

        for(int i =0;i<row_count; i++)
        {

            if(matchesList.get(i).distance<good_dist)
            {

                good_matches.add(matchesList.get(i));
                //Log.e("good_matches", "good_match_id="+matches.get(i).trainIdx);

            }

        }


        MatOfDMatch gm = new MatOfDMatch();
        gm.fromList(good_matches);

///////////////////////////////////////////////////////////////







        ///////////////////////////////////////////////////////////////////////////////



        //feature and connection colors
        Scalar RED = new Scalar(255,0,0);
        Scalar GREEN = new Scalar(0,255,0);
        //output image
        Mat outputImg = new Mat();
        MatOfByte drawnMatches = new MatOfByte();
        //this will draw all matches, works fine
        Features2d.drawMatches(img2, keypoints2, img1, keypoints1, gm,
                outputImg, GREEN, RED,  drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);


        Mat featuredImg = new Mat();
        Scalar kpColor = new Scalar(255,159,10);//this will be color of keypoints
        //featuredImg will be the output of first image
        Features2d.drawKeypoints(img1, keypoints1, featuredImg , kpColor, 0);
        //featuredImg will be the output of first image
        Features2d.drawKeypoints(img1, keypoints1, featuredImg , kpColor, 0);





        //////////////////////////////////////////////////////////////////////









        LinkedList<Point> objList = new LinkedList<Point>();
        LinkedList<Point> sceneList = new LinkedList<Point>();

        List<KeyPoint> keypoints_RefList = keypoints1.toList();
        List<KeyPoint> keypoints_List = keypoints2.toList();

        for (int i = 0; i < good_matches.size(); i++) {
            objList.addLast(keypoints_RefList.get(good_matches.get(i).queryIdx).pt);
            sceneList.addLast(keypoints_List.get(good_matches.get(i).trainIdx).pt);
        }

        MatOfPoint2f obj = new MatOfPoint2f();
        MatOfPoint2f scene = new MatOfPoint2f();

        obj.fromList(objList);
        scene.fromList(sceneList);

        Mat mask = new Mat();
        //  Calib3d.findHomography(obj, scene, 8, 10,  mask);
        Mat hg = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 5);

        Mat tmp_corners = new Mat(4, 1, CvType.CV_32FC2);
        Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

        //get corners from object
        tmp_corners.put(0, 0, new double[]{0, 0});
        tmp_corners.put(1, 0, new double[]{img2.cols(), 0});
        tmp_corners.put(2, 0, new double[]{img2.cols(), img2.rows()});
        tmp_corners.put(3, 0, new double[]{0, img2.rows()});

        Core.perspectiveTransform(tmp_corners, scene_corners, hg);

        Imgproc.line(outputImg, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
        Imgproc.line(outputImg, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
        Imgproc.line(outputImg, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
        Imgproc.line(outputImg, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(0,0)), new Scalar(0, 255, 0),4);


*/



        //////////////////////////////////////////////



        Mat objImg = goal.getValue().clone();
        Mat sceneImg = mat.clone();

       // Mat cornersInObjectMat = convertToMatOfType(sceneImg, CvType.CV_32FC2);
        Imgproc.cvtColor(sceneImg, sceneImg, Imgproc.COLOR_BGR2GRAY);
            Imgproc.cvtColor(objImg, objImg, Imgproc.COLOR_BGR2GRAY);



        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);

        //first image
        Mat descriptorsScene = new Mat();
        MatOfKeyPoint keypointsScene = new MatOfKeyPoint();

        detector.detect(sceneImg, keypointsScene);
        descriptor.compute(sceneImg, keypointsScene, descriptorsScene);

        //second image
        Mat descriptorsObj = new Mat();
        MatOfKeyPoint keypointsObj = new MatOfKeyPoint();

        detector.detect(objImg, keypointsObj);
        descriptor.compute(objImg, keypointsObj, descriptorsObj);


        if(!descriptorsObj.empty() && !descriptorsScene.empty()) {


            //matcher should include 2 different image's descriptors
            MatOfDMatch matches = new MatOfDMatch();
            List<DMatch> matchesList = new ArrayList<>();
            matcher.match(descriptorsObj, descriptorsScene, matches);
            matchesList = matches.toList();







            double max_dist = 0; double min_dist = 100;

            //-- Quick calculation of max and min idstance between keypoints
            for( int i = 0; i < descriptorsObj.rows(); i++)
            { double dist = matchesList.get(i).distance;
                if( dist < min_dist ) min_dist = dist;
                if( dist > max_dist ) max_dist = dist;
            }
            //printf("-- Max dist : %f \n", max_dist );
            //printf("-- Min dist : %f \n", min_dist );
            //-- Draw only good matches (i.e. whose distance is less than 3*min_dist)
            MatOfDMatch good_matches = new MatOfDMatch();
            List<DMatch> good_matches_list = new ArrayList<>();

            for( int i = 0; i < descriptorsObj.rows(); i++ )
            { if( matchesList.get(i).distance < 3*min_dist )
            { good_matches_list.add(matchesList.get(i)); }
            }

            good_matches.fromList(good_matches_list);

            MatOfByte drawnMatches = new MatOfByte();
            Features2d.drawMatches(objImg, keypointsObj, sceneImg, keypointsScene,
                    good_matches, matches, Scalar.all(-1), Scalar.all(-1),
                    drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);












            //-- localize the object
            List<Point> obj_list = new ArrayList<>();
            List<Point> scene_list = new ArrayList<>();
            MatOfPoint2f obj = new MatOfPoint2f();
            MatOfPoint2f scene = new MatOfPoint2f();


            for( int i = 0; i < good_matches_list.size(); i++) {
                //-- get the keypoints from the good matches
                obj_list.add(keypointsObj.toArray()[good_matches_list.get(i).queryIdx].pt);
                scene_list.add(keypointsScene.toArray()[good_matches_list.get(i).trainIdx].pt);
            }

            obj.fromList(obj_list);
            scene.fromList(scene_list);

            if( !obj_list.isEmpty() && !scene_list.isEmpty() && good_matches_list.size() >= 4) {



                Mat H = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 5);


                List<Point> obj_corners_list = new ArrayList<>();
                //MatOfPoint2f obj_corners = new MatOfPoint2f();

                List<Point> cornersInObject = new ArrayList<>();
                cornersInObject.add(new Point(0, 0));
                cornersInObject.add(new Point(objImg.width(), 0));
                cornersInObject.add(new Point(objImg.width(), objImg.height()));
                cornersInObject.add(new Point(0, objImg.height()));
                //Mat cornersInObjectMat = convertToMatOfType(cornersInObject, CvType.CV_32FC2);

                Mat cornersInSceneMat = new Mat(4, 1, CvType.CV_32FC2);
                //Core.perspectiveTransform(cornersInObjectMat, cornersInSceneMat, H);

                //MatOfPoint2f scene_corners = new MatOfPoint2f();



                    //List<Point> scene_corners_list = new ArrayList<>();
                    //scene_corners_list = scene_corners.toList();

                /*
                scene_corners_list.set(0, new Point(scene_corners_list.get(0).x + objImg.cols(), scene_corners_list.get(0).y + objImg.cols()));
                scene_corners_list.set(1, new Point(scene_corners_list.get(1).x + objImg.cols(), scene_corners_list.get(1).y + objImg.cols()));
                scene_corners_list.set(2, new Point(scene_corners_list.get(2).x + objImg.cols(), scene_corners_list.get(2).y + objImg.cols()));
                scene_corners_list.set(3, new Point(scene_corners_list.get(3).x + objImg.cols(), scene_corners_list.get(3).y + objImg.cols()));


                //-- Draw lines between the corners (the mapped object in the scene - image_2 )
                Imgproc.line(matches,
                        scene_corners_list.get(0),
                        scene_corners_list.get(1),
                        new Scalar(0, 255, 0), 4);

                Imgproc.line(matches,
                        scene_corners_list.get(1),
                        scene_corners_list.get(2),
                        new Scalar(0, 255, 0), 4);

                Imgproc.line(matches,
                        scene_corners_list.get(2),
                        scene_corners_list.get(3),
                        new Scalar(0, 255, 0), 4);

                Imgproc.line(matches,
                        scene_corners_list.get(3),
                        scene_corners_list.get(0),
                        new Scalar(0, 255, 0), 4);
*/

                    Imgproc.line(mat, new Point(cornersInSceneMat.get(0, 0)), new Point(cornersInSceneMat.get(1, 0)), new Scalar(0, 255, 0), 4);
                    Imgproc.line(mat, new Point(cornersInSceneMat.get(1, 0)), new Point(cornersInSceneMat.get(2, 0)), new Scalar(0, 255, 0), 4);
                    Imgproc.line(mat, new Point(cornersInSceneMat.get(2, 0)), new Point(cornersInSceneMat.get(3, 0)), new Scalar(0, 255, 0), 4);
                    Imgproc.line(mat, new Point(cornersInSceneMat.get(3, 0)), new Point(cornersInSceneMat.get(0, 0)), new Scalar(0, 255, 0), 4);


            }


            preview = mat.clone();



        }



        for(PassBase p : children.getValue())
        {
            p.process(preview.clone());
        }


    }

    public static Mat convertToMatOfType(List<Point> points, int cvType) {
        Mat mat = new Mat(4, 1, cvType);
        for (int i = 0; i < points.size(); ++i) {
            Point point = points.get(i);
            mat.put(i, 0, point.x, point.y);
        }
        return mat;
    }
}