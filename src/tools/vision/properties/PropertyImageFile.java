package tools.vision.properties;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import tools.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

/**
 * Created by Jake on 4/27/2015.
 */
public class PropertyImageFile extends PropertyBase {

    private transient Image image;
    private transient TextField url;
    private transient Button select;
    private transient ImageView view;

    private transient Pane paneRef;

    public PropertyImageFile() {
        super();
    }

    public void setup()
    {
        url = new TextField();
        url.setMinWidth(500);
        url.setEditable(false);

        view = new ImageView();
        view.setLayoutY(80);

        view.setFitHeight(600);
        view.setFitWidth(600);

        setValue(new File("res/frc5113logo.png"));

        select = new Button("Open file browser");
        select.setLayoutY(40);
        select.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            File file = fileChooser.showOpenDialog(paneRef.getScene().getWindow());
            setValue(file);
        });
    }

    private void setValue(File file)
    {
        url.setText(file.getAbsolutePath());
        image = new Image(file.toURI().toString());
        view.setImage(image);
        //System.out.println("source: " + image.toString());
    }

    @Override
    public void createSettingsPanel(Pane p) {
        paneRef = p;
        p.getChildren().clear();
        p.getChildren().add(url);
        p.getChildren().add(select);
        p.getChildren().add(view);
    }

    public Image getValue()
    {
        return image;
    }
}
