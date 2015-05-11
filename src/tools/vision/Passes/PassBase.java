package tools.vision.passes;

import gui.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import tools.vision.Treeable;
import tools.vision.properties.PropertyChildren;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Jake on 4/26/2015.
 * Base class for vision recognition "Passes".
 * Most passes will take an input image from a parent or parents, process it, and output the processed image to a child or children.
 * <p>
 * Almost all passes should be able to return a preview image, too.
 */
public abstract class PassBase extends Treeable implements Serializable {

    protected PropertyChildren children;
    private transient static ImageView view;

    protected transient Mat preview;

    public void updateView()
    {
        view.setImage(getPreviewImage());
    }

    protected PassBase(Controller c, List<PassBase> passes) {
        super();

        setNicknameNumbered("Generic Property Base", passes);

        view = new ImageView();

        children = new PropertyChildren(this, passes);

        setup(c, passes);
    }

    public void setup(Controller c, List<PassBase> passes)
    {
        //This is like a second constructor, sort of:
        //The constructor with parameters is not called when using serialization
        //Therefore, use the constructor to add things normally, and the setup() method to add things in the loading
        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    //Similar to setNickname, but adds a number to the end if more than one pass exists already with that name.
    //For example, if there is already a "Grid Overlay" when you try to add one, it will call it "Grid Overlay 2" instead.
    protected void setNicknameNumbered(String nicknameNumbered, List<PassBase> passes) {
        boolean taken = false;
        for (PassBase b : passes) {
            if (b.getNickname().equalsIgnoreCase(nicknameNumbered)) {
                taken = true;
                break;
            }
        }

        if (taken) {
            setNicknameNumbered(nicknameNumbered, passes, 1);
        } else {
            setNickname(nicknameNumbered);
        }
    }

    //Similar to setNickname, but adds a number to the end if more than one pass exists already with that name.
    //For example, if there is already a "Grid Overlay" when you try to add one, it will call it "Grid Overlay 2" instead.
    private void setNicknameNumbered(String nicknameNumbered, List<PassBase> passes, int count) {
        boolean taken = false;
        for (PassBase b : passes) {
            if (b.getNickname().equalsIgnoreCase(nicknameNumbered + " " + count)) {
                taken = true;
                break;
            }
        }

        if (taken) {
            setNicknameNumbered(nicknameNumbered, passes, count + 1);
        } else {
            setNickname(nicknameNumbered + " " + count);
        }
    }

    public abstract void process(Mat mat);

    protected Image getPreviewImage()
    {
        if(preview != null)
            return mat2Img(preview);
        else return mat2Img(new Mat());
    }

    @Override
    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
        p.getChildren().add(view);
    }

    protected static Image mat2Img(Mat mat)
    {
        //Shamelessly stolen from some stack overflow page
        MatOfByte byteMat = new MatOfByte();
        org.opencv.imgcodecs.Imgcodecs.imencode(".bmp", mat, byteMat);

        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    public static Mat img2Mat(BufferedImage image)
    {
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer())
                .getData();
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }

}
