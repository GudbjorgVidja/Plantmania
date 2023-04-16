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
    private Spjald fxSpjald;

    @FXML
    private Stats fxStats;

    private Planta planta;//Planta vinnsluhluturinn, plantan sem spjaldið er fyrir.

    public PlantaSpjald() {
        //keyrist aldrei
        //System.out.println("Plantaspjald tomur smidur");
    }

    /**
     * Smiðurinn sem er notaður til að gera öll PlantaSpjald
     *
     * @param p Planta hlutur
     */
    public PlantaSpjald(Planta p) {
        planta = p;

        LesaFXML.lesa(this, "planta-view.fxml");

        fxStats.setPlanta(planta);
        stillaStaerd();
        //todo er betra að gera þetta allt í einu? og fara þá einu sinni inn í fxSpjald?
        fxSpjald.setFxAlmenntNafn(planta.getAlmenntNafn());
        fxSpjald.setFxFlokkur(planta.getUppruni().getStadur());
        fxSpjald.setFxPlontuMynd(planta.getMyndaslod());
    }

    private void stillaStaerd() {
        for (Node n : fxStats.getChildren()) {
            if (n instanceof VBox) {
                ((VBox) n).setPrefHeight(27);
                ((VBox) n).setPrefWidth(36);
            }
        }

    }

    public String toString() {
        return planta.getAlmenntNafn();
    }

    public Planta getPlanta() {
        return planta;
    }
}
