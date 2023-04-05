/**
 * minPlantaSpjald les inn fxml skrána minplanta-view.fxml. Þessi klasi er controllerinn
 * MinPlantaSpjald inniheldur Spjald hlut með sömu plöntu
 */
package vidmot.plantmania;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import vinnsla.plantmania.MinPlanta;

import java.io.IOException;
import java.util.Locale;

public class MinPlantaSpjald extends AnchorPane {
    @FXML
    private Label fxLabel;
    @FXML
    private Button fxVokva, fxFresta;
    @FXML
    private Spjald fxSpjald;

    public MinPlantaSpjald() {//tómur smiður. Athuga hvort hann sé óþarfi
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("minplanta-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

         */
    }

    public MinPlantaSpjald(MinPlanta minPlanta) {//smiðurinn sem er notaður

        FXMLLoader loader = new FXMLLoader(getClass().getResource("minplanta-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //System.out.println("AlmenntNafn: " + minPlanta.getPlanta().getAlmenntNafn());
        if (fxSpjald != null) {
            fxSpjald.setFxAlmenntNafn(minPlanta.getPlanta().getAlmenntNafn());
            fxSpjald.setFxFlokkur(minPlanta.getPlanta().getUppruni().toString().toLowerCase(Locale.ROOT));
            fxSpjald.setFxPlontuMynd(minPlanta.getPlanta().getMyndaslod());
        } else {
            System.out.println("fxSpjald is null");
        }

        //setja handlera á takkana
        fxVokva.setOnAction(this::vokvaHandler);
        fxFresta.setOnAction(this::frestaHandler);
    }

    public Spjald getFxSpjald() {
        return fxSpjald;
    }

    private void vokvaHandler(ActionEvent event) {
        System.out.println("vokva");
    }

    private void frestaHandler(ActionEvent event) {
        System.out.println("frestar um dag");
    }
}
