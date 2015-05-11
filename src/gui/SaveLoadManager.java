package gui;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import tools.Logger;
import tools.vision.passes.PassBase;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jake on 5/4/2015.
 */
class SaveLoadManager {

    public static void save(VisionRecManager rec, Scene scene)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Resource File");
        File file = fileChooser.showOpenDialog(scene.getWindow());

        try
        {
            FileOutputStream fos= new FileOutputStream(file);
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(rec.getPasses().toArray(new PassBase[rec.getPasses().size()]));
            oos.close();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Logger.logln(e.toString());
        }
    }

    public static void load(VisionRecManager rec, Scene scene)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(scene.getWindow());

        try
        {


        FileInputStream fis;
        fis = new FileInputStream(file);

        ObjectInputStream ois;
        ois = new ObjectInputStream(fis);

        rec.setPasses(new ArrayList<>(Arrays.asList((PassBase[]) ois.readObject())));

        ois.close();
        fis.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Logger.logln(e.toString());
        }
    }

}
