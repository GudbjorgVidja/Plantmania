/**
 * plöntuspjald fyrir einhverja plöntu, hlutur af gerð Planta. það er líka til MinPlantaSpjald
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import vinnsla.plantmania.Planta;

import java.io.IOException;
import java.util.Locale;

public class PlantaSpjald extends AnchorPane {
    @FXML
    private AnchorPane fxBreytilegtSvaedi;//laga nafn, en er anchorPane sem inniheldur stats, til að geta breytt því og sýnt takka

    @FXML
    private ImageView fxPlontuMynd;

    @FXML
    private Label fxAlmenntNafn, fxFlokkur;

    private Planta planta;//Planta vinnsluhluturinn

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

        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + planta.getMyndaslod())));//mynd á spjaldinu rétt?

        fxAlmenntNafn.setText(planta.getAlmenntNafn());

        fxFlokkur.setText(" " + planta.getUppruni().toString().toLowerCase(Locale.ROOT) + " ");
        //String s = planta.getUppruni() + "";
        //s = s.toLowerCase(Locale.ROOT);
    }
}
