/*
keyrist ekki af óþörfu
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import vinnsla.plantmania.Planta;

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
        //keyrist aldrei
        //System.out.println("Plantaspjald tomur smidur");
    }

    public PlantaSpjald(Planta p) {
        //System.out.println("PlantaSpjald(Planta p) smidur");
        //keyrist einu sinni fyrir hvert PlantaSpjald, í upphafi keyrslu :)
        //System.out.println("Plantaspjald smidur");
        planta = p;
        LesaFXML.lesa(this, "planta-view.fxml");
        

        fxSpjald.setFxAlmenntNafn(planta.getAlmenntNafn());
        fxSpjald.setFxFlokkur(planta.getUppruni().toString().toLowerCase(Locale.ROOT));
        fxSpjald.setFxPlontuMynd(planta.getMyndaslod());
    }

    /* ekki viss u, að þetta megi?
    public PlantaSpjald(List<Planta> allarPlontur){
        for(Planta p: allarPlontur){

        }
    }
     */

    public String toString() {
        return planta.getAlmenntNafn();
    }

    public Planta getPlanta() {
        return planta;
    }
}
