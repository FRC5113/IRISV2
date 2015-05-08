package tools.vision.passes.sources;

import gui.Controller;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyInteger;

import java.util.List;

/**
 * Created by Jake on 5/7/2015.
 */
public class SourceBase extends PassBase {

    public PropertyInteger framerate;
    private transient long lastFrameTime = System.currentTimeMillis();

    public SourceBase(Controller c, List<PassBase> passes) {
        super(c, passes);

        if(preview == null)
            preview = new Mat();

        framerate = new PropertyInteger(30);
        framerate.setNickname("Goal framerate");
        getTreeItem().getChildren().add(framerate.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

    }

    public void runSourceProcess()
    {
        long timeSinceUpdate = System.currentTimeMillis() - lastFrameTime;
        if(timeSinceUpdate > (1000f / framerate.getValue()))
        {
            if(preview == null)
              preview = new Mat();

            process(preview);
            lastFrameTime = System.currentTimeMillis();
        }
    }

    @Override
    public Image getPreviewImage() {
        return null;
    }
}
