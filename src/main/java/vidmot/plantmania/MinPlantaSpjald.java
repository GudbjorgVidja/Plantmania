/**
 * minPlantaSpjald les inn fxml skrána minplanta-view.fxml
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import vinnsla.plantmania.MinPlanta;

import java.io.IOException;
import java.util.Locale;

public class MinPlantaSpjald extends AnchorPane {
    //hafa tilviksbreytur fyrir label og takkana tvo
    //tilviksbreyta og setter? fyrir spjald.
    @FXML
    private Spjald fxSpjald;

    public MinPlantaSpjald() {//smiðurinn
        FXMLLoader loader = new FXMLLoader(getClass().getResource("minplanta-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public MinPlantaSpjald(MinPlanta minPlanta) {//smiðurinn

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
    }

    public Spjald getFxSpjald() {
        return fxSpjald;
    }
}
