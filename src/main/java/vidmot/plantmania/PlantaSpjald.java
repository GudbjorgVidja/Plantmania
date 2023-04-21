package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import vinnsla.plantmania.Planta;

/**
 * Höfundur: Guðbjörg Viðja
 * plöntuspjald fyrir einhverja plöntu, hlutur af gerð Planta. það er líka til MinPlantaSpjald.
 * PlantaSpjald er fyrir plöntu sem notandi á ekki
 */
public class PlantaSpjald extends AnchorPane {
    @FXML
    private AnchorPane fxBreytilegtSvaedi;//laga nafn, en er anchorPane sem inniheldur stats, til að geta breytt því og sýnt takka

    @FXML
    private Spjald fxSpjald;//Spjald hlutur, grunnurinn fyrir plöntuspjaldið

    @FXML
    private Stats fxStats;//hlutur af klasanum Stats, sýnir eiginleika plöntunnar með skala

    private Planta planta;//Planta vinnsluhluturinn, plantan sem spjaldið er fyrir.


    /**
     * Smiðurinn sem er notaður til að gera öll PlantaSpjald
     *
     * @param p Planta hlutur
     */
    public PlantaSpjald(Planta p) {
        planta = p;
        LesaFXML.lesa(this, "planta-view.fxml");
        stillaStaerd();
        setjaUtlitSpjalds();
    }

    /**
     * setur stats, almennt nafn, uppruna og mynd af plöntu á spjaldið
     */
    private void setjaUtlitSpjalds() {
        fxStats.setStats(planta);
        fxSpjald.setFxAlmenntNafn(planta.getAlmenntHeiti());
        fxSpjald.setFxFlokkur(planta.getUppruni().getStadur());
        fxSpjald.setFxPlontuMynd(planta.getMyndaslod());
    }

    /**
     * stillir stærðina á Vboxum með myndum
     */
    private void stillaStaerd() {
        for (Node n : fxStats.getChildren()) {
            if (n instanceof VBox) {
                ((VBox) n).setPrefHeight(27);
                ((VBox) n).setPrefWidth(36);
            }
        }
    }

    public Planta getPlanta() {
        return planta;
    }

    public String toString() {
        return planta.getAlmenntHeiti();
    }
}
