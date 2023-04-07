package vidmot.plantmania;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import vinnsla.plantmania.MinPlanta;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

/**
 * minPlantaSpjald les inn fxml skrána minplanta-view.fxml. Þessi klasi er controllerinn
 * MinPlantaSpjald inniheldur Spjald hlut með sömu plöntu
 */
public class MinPlantaSpjald extends AnchorPane {
    @FXML
    private Label fxLabel;
    @FXML
    private Button fxVokva, fxFresta;
    @FXML
    private Spjald fxSpjald;

    @FXML
    private AnchorPane rot;

    private MinPlanta minPlantan;

    public MinPlantaSpjald() {//tómur smiður. Athuga hvort hann sé óþarfi

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

        if (fxSpjald != null) {
            fxSpjald.setFxAlmenntNafn(minPlantan.getAlmenntNafn());
            fxSpjald.setFxFlokkur(minPlantan.getUppruni().toString().toLowerCase(Locale.ROOT));
            fxSpjald.setFxPlontuMynd(minPlantan.getMyndaslod());
        } else {
            System.out.println("fxSpjald is null");
        }

        //setja handlera á takkana
        fxVokva.setOnAction(this::vokvaHandler);
        fxFresta.setOnAction(this::frestaHandler);
        rot.setOnMouseClicked(this::opnaPlontuglugga);

        fxLabel.textProperty().bind(minPlantan.getNaestaVokvun().asString().concat(new SimpleStringProperty(" dagar")));

    }

    public Spjald getFxSpjald() {
        return fxSpjald;
    }

    public MinPlanta getMinPlanta() {
        return minPlantan;
    }

    /**
     * kannski passa að ekki sé hægt að vökva þegar tími í næstu vökvun er meira en thinnTimiMilliVokvanna?
     *
     * @param event smellt á vökva takkann á MinPlantaSpjald hlut
     */
    private void vokvaHandler(ActionEvent event) {
        if (minPlantan.getNaestaVokvun().get() >= minPlantan.getThinnTimiMilliVokvana()) {
            System.out.println("kannski ekki vokva");
        } else {
            minPlantan.baetaVidVokvun(LocalDate.now());
            System.out.println("vokva");
        }

        //System.out.println("thinnTimiMilliVokvanna: " + minPlantan.getThinnTimiMilliVokvana());
        System.out.println(minPlantan.getVokvanir().toString());
    }

    public String toString() {
        return minPlantan.getAlmenntNafn();
    }

    private void frestaHandler(ActionEvent event) {
        System.out.println("frestar um dag");
        minPlantan.getNaestaVokvun().unbind(); //ný viðbót
        minPlantan.setNaestaVokvun(minPlantan.getNaestaVokvun().get() + 1);
        minPlantan.naestaVokvunRegla(); //ný viðbót
        System.out.println("naestaVokvun: " + minPlantan.getNaestaVokvun().get());
    }

    private void opnaPlontuglugga(MouseEvent event) {
        System.out.println("Plantan sem ytt var a: " + minPlantan);
        Plontugluggi gluggi = new Plontugluggi(minPlantan);//tekur inn hlutinn sem spjaldið er fyrir

        gluggi.showAndWait();
    }
}
