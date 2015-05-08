package tools;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

/**
 * Created by Jake on 4/27/2015.
 * Handles basic printing for the UI.
 */
public class Logger {

    private static TextArea textArea;
    private static String text = "";
    private static final int maxLength = 10000;
    private static CheckBox autoScrollCheckbox;

    public static void setTextArea(TextArea textAreas, CheckBox box) {
        textArea = textAreas;
        autoScrollCheckbox = box;
    }

    public static void log(String s) {
        double scrollAmount = textArea.getScrollTop();

        text += s;
        if (text.length() > maxLength) {
            text = text.substring(0, maxLength);
        }
        textArea.setText(text);

        //If autoscroll, set to bottom. Else, set to previous amount from before text was updated.
        if (autoScrollCheckbox.selectedProperty().getValue())
            textArea.setScrollTop(Double.MAX_VALUE);
        else
            textArea.setScrollTop(scrollAmount);
    }

    public static void logln(String s) {
        log(s);
        log("\n");
    }

}
