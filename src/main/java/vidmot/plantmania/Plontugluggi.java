/**
 * skoða með að hafa eiginlega accordionPane í skrollanlega partinum. Þyrfti að vera TitledPane hlutir í VBox sem er í
 * ScrollPane, því accordion leyfir bara eitt opið í einu.
 * Nota listener til að setja min stærð eftir því hvort titledPane er opið eða lokað
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

import java.io.IOException;

public class Plontugluggi extends Dialog<String> {
    @FXML
    private Label fxBreyta;

    public Plontugluggi() {
        setDialogPane(lesaGlugga());
        setResult(null);
    }

    /*
    private void resultConverter() {
        setResultConverter();
        setResultConverter(b -> {
            if (b.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return;
            }
        });
    }

     */

    private DialogPane lesaGlugga() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(View.GLUGGI.getFileName()));
        try {
            fxmlLoader.setController(this);
            //fxmlLoader.setRoot(this);
            return fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public static void main(String[] args) {

    }
}
