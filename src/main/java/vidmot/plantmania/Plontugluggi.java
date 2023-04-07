/**
 * skoða með að hafa eiginlega accordionPane í skrollanlega partinum. Þyrfti að vera TitledPane hlutir í VBox sem er í
 * ScrollPane, því accordion leyfir bara eitt opið í einu.
 * Nota listener til að setja min stærð eftir því hvort titledPane er opið eða lokað.
 * <p>
 * Byrja að gera þetta bara fyrir minPlanta hlut
 */
package vidmot.plantmania;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import vinnsla.plantmania.MinPlanta;

import java.io.IOException;

public class Plontugluggi extends Dialog<Void> {
    @FXML
    private Label fxBreytaNafni, fxLatnesktNafn, fxAlmenntNafn;

    @FXML
    private ImageView fxMynd;

    @FXML
    private Button fxAthugasemdir;

    private MinPlanta minPlantan;//ef glugginn er fyrir MinPlanta

    //private Planta plantan;//ef glugginn er fyrir planta, kemur seinna

    public Plontugluggi() {
        setDialogPane(lesaGlugga());
        ButtonType lokaTakki = new ButtonType("Loka glugga", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().add(lokaTakki);
        //setResult(null);
    }

    public Plontugluggi(MinPlanta minPlanta) {
        minPlantan = minPlanta;
        setDialogPane(lesaGlugga());
        ButtonType lokaTakki = new ButtonType("Loka glugga", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().add(lokaTakki);

        fxLatnesktNafn.setText(minPlantan.getPlanta().getLatnesktNafn());
        fxAlmenntNafn.setText(minPlantan.getPlanta().getAlmenntNafn());
        fxMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + minPlantan.getPlanta().getMyndaslod())));

        fxBreytaNafni.setOnMouseClicked(this::breytaNafniHandler);
        fxAthugasemdir.setOnAction(this::athugasemdirHandler);

    }


    private void resultConverter() {
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

    private void breytaNafniHandler(MouseEvent event) {
        System.out.println("nafni verdur breytt");
    }

    private void athugasemdirHandler(ActionEvent event) {
        System.out.println("Athugasemdagluggi opnast");
    }

    public static void main(String[] args) {

    }
}
