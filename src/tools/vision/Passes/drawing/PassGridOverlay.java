package tools.vision.passes.drawing;

import gui.Controller;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
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

    private PropertyInteger gridSpacingX;
    private PropertyInteger gridSpacingY;
    private PropertyInteger gridOffsetX;
    private PropertyInteger gridOffsetY;
    private PropertyInteger gridWidth;
    private PropertyColor gridColor;

    public PassGridOverlay(Controller c, List<PassBase> passes) {

        super(c, passes);

        setNicknameNumbered("Pass Grid Overlay", passes);

        gridSpacingX = new PropertyInteger(40);
        gridSpacingX.setNickname("Units per row");
        getTreeItem().getChildren().add(gridSpacingX.getTreeItem());

        gridSpacingY = new PropertyInteger(40);
        gridSpacingY.setNickname("Units per column");
        getTreeItem().getChildren().add(gridSpacingY.getTreeItem());

        gridOffsetX = new PropertyInteger(-1);
        gridOffsetX.setNickname("X Offset");
        getTreeItem().getChildren().add(gridOffsetX.getTreeItem());

        gridOffsetY = new PropertyInteger(-1);
        gridOffsetY.setNickname("Y Offset");
        getTreeItem().getChildren().add(gridOffsetY.getTreeItem());


        gridWidth = new PropertyInteger(1);
        gridWidth.setNickname("Line Width");
        getTreeItem().getChildren().add(gridWidth.getTreeItem());

        gridColor = new PropertyColor();
        gridColor.setNickname("Line color");
        getTreeItem().getChildren().add(gridColor.getTreeItem());
    }

    public void setup(Controller c, List<PassBase> passes)
    {
        if(gridColor != null) {
            gridSpacingX.setup();
            getTreeItem().getChildren().add(gridSpacingX.getTreeItem());

            gridSpacingY.setup();
            getTreeItem().getChildren().add(gridSpacingY.getTreeItem());

            gridWidth.setup();
            getTreeItem().getChildren().add(gridWidth.getTreeItem());

            gridColor.setup();
            getTreeItem().getChildren().add(gridColor.getTreeItem());
        }
        children.setup();
        getTreeItem().getChildren().add(children.getTreeItem());
    }

    @Override
    public void process(Mat mat) {

        for(int x = 0; x < mat.width(); x += gridSpacingX.getValue())
        {
            Imgproc.line(mat, new Point(x + gridOffsetX.getValue(), 0), new Point(x + gridOffsetX.getValue(), mat.height()),
                    new Scalar(gridColor.getB(), gridColor.getG(), gridColor.getR()),
                    gridWidth.getValue());
        }

        for(int y = 0; y < mat.height(); y += gridSpacingY.getValue())
        {
            Imgproc.line(mat, new Point(0, y + gridOffsetY.getValue()), new Point(mat.width(), y + gridOffsetY.getValue()),
                    new Scalar(gridColor.getB(), gridColor.getG(), gridColor.getR()),
                    gridWidth.getValue());
        }

        preview = mat.clone();

        for(PassBase p : children.getValue())
        {
            p.process(mat.clone());
        }
    }

}
