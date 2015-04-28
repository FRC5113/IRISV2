package gui;

import tools.vision.Passes.PassBase;
import tools.vision.Passes.PassGridOverlay;

import java.util.ArrayList;

/**
 * Created by Jake on 4/27/2015.
 */
public class VisionRecManager {

    private ArrayList<PassBase> passes;

    public VisionRecManager(Controller c) {
        passes = new ArrayList<PassBase>(10);

        passes.add(new PassGridOverlay(c, passes));
        passes.add(new PassGridOverlay(c, passes));
        passes.add(new PassGridOverlay(c, passes));
        passes.add(new PassGridOverlay(c, passes));

    }

}
