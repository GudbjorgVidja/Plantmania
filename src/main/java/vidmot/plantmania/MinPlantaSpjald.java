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
 * MinPlantaSpjald inniheldur Spjald hlut með sömu plöntu. Tilheyrandi vinnsluklasi er MinPlanta.java
 */
public class MinPlantaSpjald extends AnchorPane {
    @FXML
    private Label fxLabel;//niðurtalning í næstu vökvun
    @FXML
    private Button fxVokva, fxFresta;
    @FXML
    private Spjald fxSpjald; //mynd, nafn og uppruni

    @FXML
    private AnchorPane rot;//Grunnspjaldið

    private MinPlanta minPlantan;//MinPlanta hluturinn á MinPlantaSpjald hlutnum


    public MinPlantaSpjald() {//tómur smiður. Athuga hvort hann sé óþarfi
        System.out.println("MinPlantaSpjald tomur smidur");
    }

    public MinPlantaSpjald(MinPlanta minPlanta) {//smiðurinn sem er notaður
        System.out.println("MinPlantaSpjald smidur");
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

        fxSpjald.getFxAlmenntNafn().textProperty().bind(minPlantan.nickNameProperty());
        fxSpjald.setFxFlokkur(minPlantan.getUppruni().toString().toLowerCase(Locale.ROOT));
        fxSpjald.setFxPlontuMynd(minPlantan.getMyndaslod());


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
            System.out.println("kannski ekki vokva");//frekar gera takkann óvirkan held ég rite?
        } else {
            minPlantan.baetaVidVokvun(LocalDate.now());
            System.out.println("vokva");
        }

        //System.out.println("thinnTimiMilliVokvanna: " + minPlantan.getThinnTimiMilliVokvana());
        System.out.println(minPlantan.getVokvanir().toString());//vökvanir prentaðar
    }

    public String toString() {
        return minPlantan.getAlmenntNafn();
    }

    /**
     * @param event smellt á fresta hnapp
     */
    private void frestaHandler(ActionEvent event) {
        System.out.println("frestar um dag");
        minPlantan.getNaestaVokvun().unbind(); //ný viðbót
        minPlantan.setNaestaVokvun(minPlantan.getNaestaVokvun().get() + 1);
        minPlantan.naestaVokvunRegla(); //ný viðbót
        System.out.println("naestaVokvun: " + minPlantan.getNaestaVokvun().get());
    }

    /**
     * Plöntugluggi opnast, með upplýsingum um MinPlanta hlutinn.
     *
     * @param event smellt á MinPlantaSpjald
     */
    private void opnaPlontuglugga(MouseEvent event) {
        System.out.println("Plantan sem ytt var a: " + minPlantan);
        Plontugluggi gluggi = new Plontugluggi(minPlantan);//tekur inn hlutinn sem spjaldið er fyrir

        gluggi.showAndWait();
    }
}
