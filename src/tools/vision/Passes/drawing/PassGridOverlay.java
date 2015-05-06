package tools.vision.passes.drawing;

import gui.Controller;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import tools.vision.passes.PassBase;
import tools.vision.properties.PropertyColor;
import tools.vision.properties.PropertyInteger;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Jake on 4/26/2015.
 * Draws a grid over top of a material image
 * Serves little purpose in recognition, mostly used for driver assistance or testing
 */
public class PassGridOverlay extends PassBase implements Serializable {

    public PropertyInteger gridSpacingX;
    public PropertyInteger gridSpacingY;
    public PropertyColor gridColor;

    public PassGridOverlay(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Pass Grid Overlay", passes);

        gridSpacingX = new PropertyInteger(40);
        gridSpacingX.setNickname("Units per row");
        getTreeItem().getChildren().add(gridSpacingX.getTreeItem());

        gridSpacingY = new PropertyInteger(40);
        gridSpacingY.setNickname("Units per column");
        getTreeItem().getChildren().add(gridSpacingY.getTreeItem());

        gridColor = new PropertyColor();
        gridColor.setNickname("Grid color");
        getTreeItem().getChildren().add(gridColor.getTreeItem());
    }

    public void setup(Controller c, List<PassBase> passes)
    {
        if(gridColor != null) {
            gridSpacingX.setup();
            getTreeItem().getChildren().add(gridSpacingX.getTreeItem());

            gridSpacingY.setup();
            getTreeItem().getChildren().add(gridSpacingY.getTreeItem());

            gridColor.setup();
            getTreeItem().getChildren().add(gridColor.getTreeItem());
        }
        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

    }

    @Override
    public Image getPreviewImage() {
        return null;
    }
}
