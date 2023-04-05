/**
 * minPlantaSpjald les inn fxml skrána minplanta-view.fxml. Þessi klasi er controllerinn
 * MinPlantaSpjald inniheldur Spjald hlut með sömu plöntu
 */
package vidmot.plantmania;

import javafx.beans.property.SimpleStringProperty;
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

    private MinPlanta minPlantan;

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

        //minPlanta vistuð í tilviksbreytu
        minPlantan = minPlanta;

        //System.out.println("AlmenntNafn: " + minPlanta.getPlanta().getAlmenntNafn());
        if (fxSpjald != null) {
            fxSpjald.setFxAlmenntNafn(minPlantan.getPlanta().getAlmenntNafn());
            fxSpjald.setFxFlokkur(minPlantan.getPlanta().getUppruni().toString().toLowerCase(Locale.ROOT));
            fxSpjald.setFxPlontuMynd(minPlantan.getPlanta().getMyndaslod());
        } else {
            System.out.println("fxSpjald is null");
        }


        //setja handlera á takkana
        fxVokva.setOnAction(this::vokvaHandler);
        fxFresta.setOnAction(this::frestaHandler);

        //setja dagsetningu á label
        //fxLabel.setText(fxLabel.getText().replace("-0", minPlanta.getNaestaVokvun().get() + ""));
        //StringProperty labelTexti = new SimpleStringProperty()
        //fxLabel.textProperty().bind(new SimpleStringProperty(minPlantan.getNaestaVokvun().asString() + " dagar"));
        fxLabel.textProperty().bind(minPlantan.getNaestaVokvun().asString().concat(new SimpleStringProperty(" dagar")));

    }

    public Spjald getFxSpjald() {
        return fxSpjald;
    }

    private void vokvaHandler(ActionEvent event) {
        System.out.println("vokva");
    }

    private void frestaHandler(ActionEvent event) {
        System.out.println("frestar um dag");
        minPlantan.setNaestaVokvun(minPlantan.getNaestaVokvun().get() + 1);
        System.out.println("naestaVokvun: " + minPlantan.getNaestaVokvun().get());
    }
}
