/*
keyrist ekki af óþörfu
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import vinnsla.plantmania.Planta;
import vinnsla.plantmania.enums.Ljosstyrkur;
import vinnsla.plantmania.enums.Vatnsthorf;

/**
 * plöntuspjald fyrir einhverja plöntu, hlutur af gerð Planta. það er líka til MinPlantaSpjald.
 * PlantaSpjald er fyrir plöntu sem notandi á ekki
 */
public class PlantaSpjald extends AnchorPane {
    @FXML
    private AnchorPane fxBreytilegtSvaedi;//laga nafn, en er anchorPane sem inniheldur stats, til að geta breytt því og sýnt takka

    @FXML
    private Spjald fxSpjald;

    @FXML
    private VBox fxVatnBox, fxLjosBox, fxHitiBox;

    @FXML
    private HBox fxBoxaHbox;

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


        //todo er betra að gera þetta allt í einu? og fara þá einu sinni inn í fxSpjald?
        fxSpjald.setFxAlmenntNafn(planta.getAlmenntNafn());
        //fxSpjald.setFxFlokkur(planta.getUppruni().toString().toLowerCase(Locale.ROOT));
        fxSpjald.setFxFlokkur(planta.getUppruni().getStadur());
        fxSpjald.setFxPlontuMynd(planta.getMyndaslod());

        stillaMyndaStaerd();

        setjaOpacity();

    }

    /**
     * stilla stærð mynda hér, og kannski líka sýnileika? (opacity)
     */
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
