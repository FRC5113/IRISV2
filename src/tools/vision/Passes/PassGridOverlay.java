package tools.vision.Passes;

import org.opencv.core.Mat;
import gui.Controller;
import tools.vision.properties.PropertyColor;
import tools.vision.properties.PropertyInteger;

import java.util.List;


/**
 * Created by Jake on 4/26/2015.
 * Draws a grid over top of a material image
 * Serves little purpose in recognition, mostly used for driver assistance or testing
 */
public class PassGridOverlay extends PassBase {

    PropertyInteger gridSpacingX;
    PropertyInteger gridSpacingY;
    PropertyColor gridColor;

    public PassGridOverlay(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Grid Overlay", passes);

        gridSpacingX = new PropertyInteger();
        gridSpacingX.setNickname("Units per row");
        getTreeItem().getChildren().add(gridSpacingX.getTreeItem());

        gridSpacingY = new PropertyInteger();
        gridSpacingY.setNickname("Units per column");
        getTreeItem().getChildren().add(gridSpacingY.getTreeItem());

        gridColor = new PropertyColor();
        gridColor.setNickname("Grid color");
        getTreeItem().getChildren().add(gridColor.getTreeItem());

        c.getVisionRecTreeView().getRoot().getChildren().add(getTreeItem());
    }

    @Override
    public void process(Mat mat) {

    }
}
