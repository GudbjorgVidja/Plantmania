/**
 * todo: væri kannski betra að gera Spjald með MinPlanta hlut, frekar en bara Planta?
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Höfundur: Guðbjörg Viðja
 * Sérhæfður klasi, hlutur sem inniheldur mynd af plöntu, almennt heiti hennar, og uppruna. Spjald hlutur er á bæði
 * PlantaSpjald og MinPlantaSpjald.
 */
public class Spjald extends VBox {
    @FXML
    private Label fxFlokkur, fxAlmenntNafn;

    @FXML
    private ImageView fxPlontuMynd;
    
    public Spjald() {
        LesaFXML.lesa(this, "spjald-view.fxml");
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
