package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import vinnsla.plantmania.MinPlanta;

import java.time.LocalDate;

/**
 * Höfundur: Guðbjörg Viðja
 * minPlantaSpjald les inn fxml skrána minplanta-view.fxml. Þessi klasi er controllerinn
 * MinPlantaSpjald inniheldur Spjald hlut með sömu plöntu. Tilheyrandi vinnsluklasi er MinPlanta.java
 */
public class MinPlantaSpjald extends AnchorPane {
    @FXML
    private Label fxLabel;//niðurtalning í næstu vökvun
    @FXML
    private Button fxVokva, fxFresta;//takkar til að vökva plöntu og fresta vökvun
    @FXML
    private Spjald fxSpjald; //mynd, nafn og uppruni
    @FXML
    private AnchorPane rot;//Grunnspjaldið

    private MinPlanta minPlantan;//MinPlanta hluturinn á MinPlantaSpjald hlutnum


    public MinPlantaSpjald(MinPlanta minPlanta) {
        LesaFXML.lesa(this, "minplanta-view.fxml");
        minPlantan = minPlanta;
        geraUtlitSpjalds();
        fxLabel.textProperty().bind(minPlantan.naestaVokvunProperty().asString().concat(new SimpleStringProperty(" dagar")));
        takkaRegla();
        setjaHandleraATakka();
    }

    /**
     * setur nafn plöntu, uppruna hennar og mynd á spjaldið
     */
    private void geraUtlitSpjalds() {
        fxSpjald.getFxAlmenntNafn().textProperty().bind(minPlantan.gaelunafnProperty());
        fxSpjald.setFxFlokkur(minPlantan.getUppruni().getStadur());
        fxSpjald.setFxPlontuMynd(minPlantan.getMyndaslod());
    }

    /**
     * setur eventhandlera á vökva og fresta takkana, og á spjaldið sjálft
     */
    private void setjaHandleraATakka() {
        fxVokva.setOnAction(this::vokvaHandler);
        fxFresta.setOnAction(this::frestaHandler);
        rot.setOnMouseClicked(this::opnaPlontuglugga);

        setjaFxLabelTextProperty();
    }

    private void setjaFxLabelTextProperty() {
        fxLabel.textProperty().bind(minPlantan.naestaVokvunProperty().asString().concat(new SimpleStringProperty(" dag")).concat(Bindings.when(minPlantan.naestaVokvunProperty().asString().isEqualTo("1")).then("ur").otherwise("ar")));
    }

    public Spjald getFxSpjald() {
        return fxSpjald;
    }

    public MinPlanta getMinPlanta() {
        return minPlantan;
    }

    /**
     * Skráir nýja vökvun á plöntuna fyrir daginn í dag
     *
     * @param event smellt á vökva takkann á MinPlantaSpjald hlut
     */
    private void vokvaHandler(ActionEvent event) {
        minPlantan.baetaVidVokvun(LocalDate.now());
    }

    /**
     * óvirkjar vökvunartakkann ef það eru jafn margir eða fleiri dagar í næstu vökvun og dagar milli vökvana
     */
    private void takkaRegla() {
        fxVokva.disableProperty().bind(minPlantan.naestaVokvunProperty().greaterThanOrEqualTo(minPlantan.thinnTimiMilliVokvanaProperty()));
    }

    /**
     * Frestar næstu vökvun um einn dag
     *
     * @param event smellt á fresta hnapp
     */
    private void frestaHandler(ActionEvent event) {
        minPlantan.naestaVokvunProperty().unbind();
        minPlantan.setNaestaVokvun(minPlantan.naestaVokvunProperty().get() + 1);
        minPlantan.naestaVokvunRegla();
    }

    /**
     * Opnar plöntuglugga með upplýsingum um MinPlanta hlutinn.
     *
     * @param event smellt á MinPlantaSpjald
     */
    private void opnaPlontuglugga(MouseEvent event) {
        Plontugluggi gluggi = new Plontugluggi(minPlantan);
        gluggi.showAndWait();
    }

    public String toString() {
        return minPlantan.getAlmenntHeiti();
    }
}
