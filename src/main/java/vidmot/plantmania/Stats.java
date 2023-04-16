package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Planta;
import vinnsla.plantmania.enums.Ljosstyrkur;
import vinnsla.plantmania.enums.Vatnsthorf;

public class Stats extends HBox {
    @FXML
    private VBox fxVatnBox, fxLjosBox, fxHitiBox;

    @FXML
    private HBox fxBoxaHbox;

    private Planta planta;

    private MinPlanta minPlanta;

    public Stats() {
        LesaFXML.lesa(this, "stats-view.fxml");
        stillaMyndaStaerd();
        //setjaOpacity();
    }

    public Stats(Planta p) {
        planta = p;
        LesaFXML.lesa(this, "stats-view.fxml");
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

    private void setOpacity() {
        if (minPlanta.getLjosstyrkur().equals(Ljosstyrkur.HALFBEINT)) {
            fxLjosBox.getChildren().get(0).setOpacity(0.3);
        } else if (minPlanta.getLjosstyrkur().equals(Ljosstyrkur.OBEINT)) {
            fxLjosBox.getChildren().get(0).setOpacity(0.3);
            fxLjosBox.getChildren().get(1).setOpacity(0.3);
        }

        if (minPlanta.getVatnsthorf().equals(Vatnsthorf.MEDAL)) {
            fxVatnBox.getChildren().get(0).setOpacity(0.3);
        } else if (minPlanta.getVatnsthorf().equals(Vatnsthorf.LITIL) || minPlanta.getVatnsthorf().equals(Vatnsthorf.MJOG_LITIL)) {
            fxVatnBox.getChildren().get(0).setOpacity(0.3);
            fxVatnBox.getChildren().get(1).setOpacity(0.3);
        }

        if (minPlanta.getKjorhitastig().get(1) < 20) {
            fxHitiBox.getChildren().get(0).setOpacity(0.3);
        } else if (minPlanta.getKjorhitastig().get(1) < 15) {
            fxHitiBox.getChildren().get(0).setOpacity(0.3);
            fxHitiBox.getChildren().get(1).setOpacity(0.3);
        }

    }

    public void setPlanta(Planta p) {
        planta = p;
        setjaOpacity();
    }

    public void setMinPlanta(MinPlanta mp) {
        minPlanta = mp;
        setOpacity();
    }
    
}
