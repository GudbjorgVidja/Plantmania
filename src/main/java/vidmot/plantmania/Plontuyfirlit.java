/**
 * Sérhæfður klasi fyrir yfirlit yfir mínar plöntur og allar plöntur. Á að vera tómt, en svo hlutum bætt í flowpane.
 * <p>
 * setja takka hreinsa snið? Enginn rammi, kannski bara undirstrikaður texti?
 * Hafa menuItems undir sía default flokkana, svo kki þurfi að breyta til að birta almenna yfirlitið
 * <p>
 * Uppruni flokkarnir eru upphaflegu möguleikarnir í sía
 */
package vidmot.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Planta;

import java.io.IOException;

public class Plontuyfirlit extends AnchorPane {
    @FXML
    private FlowPane fxFlowPane; //aðgangur í flowpane sem inniheldur spjöldin

    @FXML
    private Label notandiLabel;//label í efra hægra horni með notendanafni og icon

    @FXML
    private MenuBar fxMenuBar; //til að fá aðgang að menu fyrir snið, nota getChildren();

    @FXML
    private Menu fxSiaMenu;//sia menuið, bein tenging við viðmót

    private ObservableList<MenuItem> checkMenuItems = FXCollections.observableArrayList();
    //ætti kannski bara að innihalda stök 2 og lengra, þau eru þau einu sem geta breyst.

    public Plontuyfirlit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("plontuyfirlit.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        checkMenuItems.setAll(fxSiaMenu.getItems()); //checkMenuItems er uppfærð útgáfa
        //checkMenuItems.remove(0, 1); //inniheldur bara breytanlegu stökin

        checkMenuItems.addListener((ListChangeListener<? super MenuItem>) change -> {
            change.next();
            if (change.wasRemoved()) fxSiaMenu.getItems().removeAll(change.getRemoved());
            if (change.wasAdded()) fxSiaMenu.getItems().addAll(change.getAddedSubList());
        });

        checkMenuItems.add(new CheckMenuItem("nýtt item"));


        //stilla upphafsstöðu síu, og binda saman fyrsta og öll hin stök síunnar

        //setja binder eða listener þ.a. ef barnalisti sia breytist þá uppfærist viðmótið.

    }

    public void baetaVidYfirlit(Planta planta) {
        PlantaSpjald spjald = new PlantaSpjald(planta);
        fxFlowPane.getChildren().add(spjald);
    }

    public void baetaVidYfirlit(MinPlanta planta) {
        PlantaSpjald spjald = new PlantaSpjald(planta);
        fxFlowPane.getChildren().add(spjald);
    }

    //stilla upphafsstöðu síu, og binda saman fyrsta og öll hin stök síunnar
    private void stillaSia() {
        //ef allir flokkar valdir: efsta valið.
        //annars: efsta ekki valið.

        //Ef smellt á efsta og hak tekið af: setur allt sem óvalið.
        //ef smellt a efsta og hak sett á: allir flokkar valdir


        CheckMenuItem fyrstaItem = (CheckMenuItem) fxSiaMenu.getItems().get(0);

    }


    //      ** getterar og setterar **

    public FlowPane getFxFlowPane() {
        return fxFlowPane;
    }

    //nær í notendanafn af label
    public String getNotandiLabel() {
        return notandiLabel.getText();
    }

    public void setNotandiLabel(String nafn) {
        notandiLabel.setText(nafn);
    }

    public MenuBar getFxMenuBar() {
        return fxMenuBar;
    }

}
