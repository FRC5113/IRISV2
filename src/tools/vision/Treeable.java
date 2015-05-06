package tools.vision;

import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;

import java.io.Serializable;

/**
 * Created by Jake on 4/27/2015.
 * Objects, including Passes and Properties, that can be included and display in the VR Tree View
 */
public class Treeable implements Serializable {

    //Name to be displayed in tree view
    public String nickname;
    private transient TreeItem treeItem;

    public Treeable() {
        setup();
    }

    public void setup()
    {
    }

    public TreeItem getTreeItem() {
        if(treeItem != null)
            return treeItem;
        else
            treeItem = new TreeItem<>(this);
        return treeItem;
    }

    @Override
    public String toString() {
        return getNickname();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
    }


}
