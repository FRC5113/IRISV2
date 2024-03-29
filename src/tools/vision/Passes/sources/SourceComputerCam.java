package tools.vision.passes.sources;

import gui.Controller;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import tools.Logger;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyInteger;

import java.util.List;

/**
 * Created by Jake on 4/26/2015.
 * Returns an image taken from a computer's webcam
 */
public class SourceComputerCam extends SourceBase {

    private transient VideoCapture cvCam;
    private PropertyInteger cameraID;

    public SourceComputerCam(Controller c, List<PassBase> passes) {
        super(c, passes);

        setNicknameNumbered("Source Computer Camera", passes);

        cameraID = new PropertyInteger(0);
        cameraID.setNickname("Camera ID TODO");
        getTreeItem().getChildren().add(cameraID.getTreeItem());

    }

    public void setup()
    {
        try {
            cvCam = new VideoCapture(0);
        }
        catch (Exception e)
        {
            Logger.logln("Warning, camera not accessible");
        }

        if(cameraID != null)
        {
            cameraID.setup();
            getTreeItem().getChildren().add(cameraID.getTreeItem());

            children.setup();
            getTreeItem().getChildren().add(children.getTreeItem());
        }
    }


    @Override
    public void process(Mat mat)
    {
        if (cvCam != null && cvCam.isOpened())
        {
            Mat temp = new Mat();
            boolean successful = cvCam.read(temp);
            if(successful)
            {
                preview = temp.clone();
                for(PassBase p : children.getValue())
                {
                    p.process(temp.clone());
                }
            }
        }
        else
        {
            try {
                cvCam = new VideoCapture(0);
            }
            catch (Exception e)
            {
                Logger.logln("Warning, camera not accessible. Retrying soon...");
            }
        }
    }

    @Override
    public Image getPreviewImage()
    {
        return mat2Img(preview);
    }
}
