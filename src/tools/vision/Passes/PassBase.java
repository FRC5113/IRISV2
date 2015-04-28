package tools.vision.Passes;

import gui.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.opencv.core.Mat;
import tools.vision.Treeable;
import tools.vision.properties.PropertyChildren;

import java.io.File;
import java.util.List;

/**
 * Created by Jake on 4/26/2015.
 * Base class for vision recognition "Passes".
 * Most passes will take an input image from a parent or parents, process it, and output the processed image to a child or children.
 * <p>
 * Almost all passes should be able to return a preview image, too.
 */
public abstract class PassBase extends Treeable {

    protected Image lastPreviewImage;
    protected PropertyChildren children;
    private ImageView view;


    public PassBase(Controller c, List<PassBase> passes) {
        super();

        File file = new File("res/frc5113logo.png");
        lastPreviewImage = new Image(file.toURI().toString());

        view = new ImageView();
        view.setImage(getPreviewImage());

        children = new PropertyChildren(this, passes);
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

    public Image getPreviewImage() {
        return lastPreviewImage;
    }

    @Override
    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
        p.getChildren().add(view);
    }

}
