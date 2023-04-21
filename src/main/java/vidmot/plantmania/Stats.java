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

/**
 * Höfundur: Guðbjörg
 * Sérhæfður klasi fyrir smá myndrænt yfirlit yfir grunnþarfir plöntu. Notast á PlantaSpjald hlut og í plöntuglugga.
 */
public class Stats extends HBox {
    @FXML
    private VBox fxVatnBox, fxLjosBox, fxHitiBox; //VBox sem innihalda imageView hluti, skali fyrir vatns-, ljós- og hitaþörf

    @FXML
    private HBox fxBoxaHbox; //HBox sem inniheldur VBoxin hér að ofan

    private final double OPACITY = 0.3;//hvaða opacity er sett á ljósari myndirnar

    private Vatnsthorf vatnsthorf;//enum gildi fyrir vatnsþörf plöntunnar
    private Ljosstyrkur ljosstyrkur;//enum gildi fyrir ljósstyrk plöntunnar
    private int kjorhitastig;//int gildi fyrir kjörhitastig plöntunnar


    public Stats() {
        LesaFXML.lesa(this, "stats-view.fxml");
        stillaMyndaStaerd();
    }

    /**
     * Stærð mynda er stillt hér.
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

    /**
     * aðferð sem athugar ljósstyrk, vatnsþörf og kjörhitastig plöntu og setur sýnileika mynda í samræmi við það
     */
    private void seturOpacity() {
        if (ljosstyrkur.equals(Ljosstyrkur.HALFBEINT)) {
            fxLjosBox.getChildren().get(0).setOpacity(OPACITY);
        } else if (ljosstyrkur.equals(Ljosstyrkur.OBEINT)) {
            fxLjosBox.getChildren().get(0).setOpacity(OPACITY);
            fxLjosBox.getChildren().get(1).setOpacity(OPACITY);
        }

        if (vatnsthorf.equals(Vatnsthorf.MEDAL)) {
            fxVatnBox.getChildren().get(0).setOpacity(OPACITY);
        } else if (vatnsthorf.equals(Vatnsthorf.LITIL) || vatnsthorf.equals(Vatnsthorf.MJOG_LITIL)) {
            fxVatnBox.getChildren().get(0).setOpacity(OPACITY);
            fxVatnBox.getChildren().get(1).setOpacity(OPACITY);
        }

        if (kjorhitastig <= 15) {
            fxHitiBox.getChildren().get(1).setOpacity(OPACITY);
        }
        if (kjorhitastig <= 20) {
            fxHitiBox.getChildren().get(0).setOpacity(OPACITY);
        }
    }

    //setter fyrir stats fyrir hlut af klasanum Planta
    public void setStats(Planta p) {
        vatnsthorf = p.getVatnsthorf();
        ljosstyrkur = p.getLjosstyrkur();
        kjorhitastig = p.getKjorhitastig().get(1);
        seturOpacity();
    }

    public void setStats(MinPlanta mp) {
        vatnsthorf = mp.getVatnsthorf();
        ljosstyrkur = mp.getLjosstyrkur();
        kjorhitastig = mp.getKjorhitastig().get(1);
        seturOpacity();
    }
}
