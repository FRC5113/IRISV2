package tools.vision;

import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;

/**
 * Created by Jake on 4/27/2015.
 * Objects, including Passes and Properties, that can be included and display in the VR Tree View
 */
public class Treeable {

    //Name to be displayed in tree view
    private String nickname = "Generic Property Base";
    private TreeItem treeItem;
    public Treeable() {
        treeItem = new TreeItem<Treeable>(this);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public TreeItem getTreeItem() {
        return treeItem;
    }

    @Override
    public String toString() {
        return getNickname();
    }

    public String getNickname()
    {
        return nickname;
    }

    public void createSettingsPanel(Pane p) {
        p.getChildren().clear();
    }


}
