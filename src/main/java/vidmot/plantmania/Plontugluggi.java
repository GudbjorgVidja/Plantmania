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
import java.util.Optional;

public class Plontugluggi extends Dialog<Void> {
    @FXML
    private Label fxBreytaNafni, fxLatnesktNafn, fxAlmenntNafn;

    @FXML
    private ImageView fxMynd;

    @FXML
    private Button fxAthugasemdir, fxVokvunarsaga;

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
        fxVokvunarsaga.setOnAction(this::vokvunarsagaHandler);

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
        TextInputDialog nafnDialog = new TextInputDialog(minPlantan.getNickName());
        //nafnDialog.getDialogPane().getButtonTypes()
        Optional<String> inntak = nafnDialog.showAndWait();
        if (inntak.isPresent()) {
            minPlantan.setNickName(inntak.get());
            //if (!minPlantan.getOllHeiti().contains(inntak.get())) {
            /*
            List<String> nyrListi = minPlantan.getOllHeiti();
            nyrListi.add(inntak.get());
            minPlantan.setOllHeiti(nyrListi);

             */
            //}
        }
    }

    /**
     * Dialogur sem inniheldur textArea opnast. Í þessu textArea eru Notes frá notanda, ef ekkert þá er það tomt.
     * Ef eitthvað er skrifað í gluggann og valið að vista það, þá vistast það í minPlanta hlutinn.
     *
     * @param event smellt á takkann breyta nótum eða eitthvað
     */
    private void athugasemdirHandler(ActionEvent event) {

        ButtonType vista = new ButtonType("vista breytingar", ButtonBar.ButtonData.OK_DONE);
        ButtonType haettaVid = new ButtonType("hætta við", ButtonBar.ButtonData.CANCEL_CLOSE);
        Dialog<String> dialogur = new Dialog<>();
        dialogur.getDialogPane().getButtonTypes().addAll(vista, haettaVid);
        //DialogPane dialogPane = new DialogPane(vista, haettaVid);
        //String eldriTexti = "eldri upplýsingar";
        TextArea textArea = new TextArea(minPlantan.getNotesFraNotanda());
        dialogur.getDialogPane().setContent(textArea);
        dialogur.setResultConverter(b -> {
            //if (b.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) return null;
            if (b.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) return textArea.getText();
            //return eldriTexti;
            //return textArea.getText();
            return null;
        });

        //dialogur.setDialogPane(dialogPane);
        Optional<String> result = dialogur.showAndWait();
        if (result.isPresent()) {//result vistar breytingar á texta
            System.out.println("utkoma ur dialog: " + result);
            minPlantan.setNotesFraNotanda(result.get());
        }
    }

    private void vokvunarsagaHandler(ActionEvent event) {
        System.out.println("fyrri vokvanir: " + minPlantan.getVokvanir());
        System.out.println("planadar vokvanir: " + minPlantan.getPlanadarVokvanir());
    }

    public static void main(String[] args) {

    }
}
