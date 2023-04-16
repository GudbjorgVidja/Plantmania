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

    /*
    @FXML
    private VBox fxVatnBox, fxLjosBox, fxHitiBox;

    @FXML
    private HBox fxBoxaHbox;

     */

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

        //stillaMyndaStaerd();

        //setjaOpacity();

    }

    private void stillaStaerd() {
        for (Node n : fxStats.getChildren()) {
            if (n instanceof VBox) {
                ((VBox) n).setPrefHeight(27);
                ((VBox) n).setPrefWidth(36);
            }
        }

    }

    /**
     * stilla stærð mynda hér, og kannski líka sýnileika? (opacity)
     */
    /*
    private void stillaMyndaStaerd() {
        for (Node v : fxBoxaHbox.getChildren()) {
            if (v instanceof VBox) {
                for (Node i : ((VBox) v).getChildren()) {
                    if (i instanceof ImageView) {
                        ((ImageView) i).setFitHeight(20);
                        ((ImageView) i).setFitWidth(20);
                    }
                }
            }
        }
    }

    //todo gera sérhæfðan klasa fyrir þetta allt, nota í PlantaSpjald og plontugluggi
    private void setjaOpacity() {
        if (planta.getLjosstyrkur().equals(Ljosstyrkur.HALFBEINT)) {
            fxLjosBox.getChildren().get(0).setOpacity(0.3);
        } else if (planta.getLjosstyrkur().equals(Ljosstyrkur.OBEINT)) {
            fxLjosBox.getChildren().get(0).setOpacity(0.3);
            fxLjosBox.getChildren().get(1).setOpacity(0.3);
        }

        if (planta.getVatnsthorf().equals(Vatnsthorf.MEDAL)) {
            fxVatnBox.getChildren().get(0).setOpacity(0.3);
        } else if (planta.getVatnsthorf().equals(Vatnsthorf.LITIL) || planta.getVatnsthorf().equals(Vatnsthorf.MJOG_LITIL)) {
            fxVatnBox.getChildren().get(0).setOpacity(0.3);
            fxVatnBox.getChildren().get(1).setOpacity(0.3);
        }

        if (planta.getKjorhitastig().get(1) < 20) {
            fxHitiBox.getChildren().get(0).setOpacity(0.3);
        } else if (planta.getKjorhitastig().get(1) < 15) {
            fxHitiBox.getChildren().get(0).setOpacity(0.3);
            fxHitiBox.getChildren().get(1).setOpacity(0.3);
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
