/**
 * TODO: væri kannski betra að gera Spjald með MinPlanta hlut, frekar en bara Planta?
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import vinnsla.plantmania.Planta;

import java.io.IOException;

/**
 * Sérhæfður klasi, hlutur sem inniheldur mynd af plöntu, almennt heiti hennar, og uppruna. Spjald hlutur er á bæði
 * PlantaSpjald og MinPlantaSpjald.
 */
public class Spjald extends VBox {
    @FXML
    private Label fxFlokkur, fxAlmenntNafn;

    @FXML
    private ImageView fxPlontuMynd;

    private Planta plant;//Spjald inniheldur í raun Planta hlut en aldrei MinPlanta hlut

    public Spjald() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Smiður, smíðar spjald með upplýsingum frá plöntu p.
     *
     * @param p Planta, getur verið Planta hlutur innan MinPlanta
     */
    public Spjald(Planta p) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        fxFlokkur.setText(p.getUppruni().toString());
        fxAlmenntNafn.setText(p.getAlmenntNafn());
        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + p.getMyndaslod())));

    }

    public void setFxFlokkur(String nafn) {
        fxFlokkur.setText(nafn);
    }

    public void setFxAlmenntNafn(String nafn) {
        fxAlmenntNafn.setText(nafn);
    }

    public Label getFxAlmenntNafn() {
        return fxAlmenntNafn;
    }

    public void setFxPlontuMynd(String hlekkur) {
        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + hlekkur)));
    }

}
