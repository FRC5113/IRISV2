package tools.vision.passes.sources;

import gui.Controller;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyImageFile;
import tools.vision.properties.PropertyInteger;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Jake on 4/26/2015.
 * Draws a grid over top of a material image
 * Loads an image from a file
 */
public class SourceImageFile extends PassBase implements Serializable {

    public PropertyInteger framerate;
    public PropertyImageFile imageFile;

    public SourceImageFile(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Source Image File", passes);

        framerate = new PropertyInteger(30);
        framerate.setNickname("Goal framerate");
        getTreeItem().getChildren().add(framerate.getTreeItem());

        imageFile = new PropertyImageFile();
        imageFile.setNickname("File selection");
        getTreeItem().getChildren().add(imageFile.getTreeItem());

    }

    public void setup(Controller c, List<PassBase> passes)
    {
        if(framerate != null && imageFile != null) {
            framerate.setup();
            imageFile.setup();
                getTreeItem().getChildren().add(framerate.getTreeItem());
                getTreeItem().getChildren().add(imageFile.getTreeItem());
        }
        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

    }

    public Image getPreviewImage() {
        return imageFile.getValue();
    }

}