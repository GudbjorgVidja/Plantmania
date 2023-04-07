package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import vinnsla.plantmania.Planta;

import java.io.IOException;
import java.util.Locale;

/**
 * plöntuspjald fyrir einhverja plöntu, hlutur af gerð Planta. það er líka til MinPlantaSpjald.
 * PlantaSpjald er fyrir plöntu sem notandi á ekki
 */
public class PlantaSpjald extends AnchorPane {
    @FXML
    private AnchorPane fxBreytilegtSvaedi;//laga nafn, en er anchorPane sem inniheldur stats, til að geta breytt því og sýnt takka

    @FXML
    private Spjald fxSpjald;

    private Planta planta;//Planta vinnsluhluturinn, plantan sem spjaldið er fyrir.

    public PlantaSpjald() {

    }

    public PlantaSpjald(Planta p) {
        planta = p;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("planta-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //if (fxSpjald != null) {
        fxSpjald.setFxAlmenntNafn(planta.getAlmenntNafn());
        fxSpjald.setFxFlokkur(planta.getUppruni().toString().toLowerCase(Locale.ROOT));
        fxSpjald.setFxPlontuMynd(planta.getMyndaslod());
        //} else {
        System.out.println("fxSpjald er null");
        //}
    }

    public String toString() {
        return planta.getAlmenntNafn();
    }

    public Planta getPlanta() {
        return planta;
    }
}
