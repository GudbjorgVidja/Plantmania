/**
 * Sérhæfður klasi fyrir yfirlit yfir hluti, bæði fyrir MinPlantaSpjald og PlantaSpjald hluti. Býður upp á að stjórna
 * sniði, með röðun og síu sem segir til um hvaða hlutir eru sýndir.
 * Það ætti kannksi að hafa líka vinnsluklasa fyrir þetta
 */
package vidmot.plantmania;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Planta;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class Plontuyfirlit extends AnchorPane {
    @FXML
    private FlowPane fxFlowPane; //aðgangur í flowpane sem inniheldur spjöldin

    @FXML
    private Label notandiLabel;//label í efra hægra horni með notendanafni og icon

    @FXML
    private MenuBar fxMenuBar; //til að fá aðgang að menu fyrir snið, nota getChildren();

    @FXML//sia menuið, bein tenging við viðmót
    private Menu fxSiaMenu, flokkunMenu, rodunMenu;

    private ObservableList<MenuItem> checkMenuItems = FXCollections.observableArrayList();
    //ætti kannski bara að innihalda stök 2 og lengra, þau eru þau einu sem geta breyst.

    private ObservableList<Node> syndSpjold = FXCollections.observableArrayList();//Hlutirnir í þessu yfirliti

    private ObservableList<PlantaSpjald> syndPlontuSpjold = FXCollections.observableArrayList(); //henda

    private ObservableList<MinPlantaSpjald> syndMinPlontuSpjold = FXCollections.observableArrayList();//henda

    public Plontuyfirlit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("plontuyfirlit.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        syndSpjold.addListener((ListChangeListener<? super Node>) change -> {
            fxFlowPane.getChildren().clear();
            fxFlowPane.getChildren().addAll(syndSpjold);
        });


        siaMenuBreytingar();

        //handlerar settir á menus. HAndlerarnir prenta nú bara nafn hlutarins sem var valinn
        for (MenuItem item : rodunMenu.getItems()) {
            item.setOnAction(this::rodunItemHandler);
        }

        for (MenuItem item : flokkunMenu.getItems()) {
            item.setOnAction(this::flokkunItemHandler);
        }

        for (MenuItem item : fxSiaMenu.getItems()) {
            item.setOnAction(this::siaItemHandler);
        }


        System.out.println("fxFlowPane.getChildren().getClass();" + fxFlowPane.getChildren().getClass());
        //System.out.println("fxFlowPane.getChildren().get(0).getClass()" + fxFlowPane.getChildren().get(0).getClass());

    }

    private void siaMenuBreytingar() {
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

    /**
     * @param planta Planta hlutur, almennt
     */
    public void baetaVidYfirlit(Planta planta) {
        PlantaSpjald spjald = new PlantaSpjald(planta);
        syndSpjold.add(spjald);
        //fxFlowPane.getChildren().add(spjald); var að taka
    }


    /**
     * Ef inntakið er MinPlanta þá er þetta tilvik af Plontuyfirlit MinPlantaYfirlit í þeim flipa
     *
     * @param planta MinPlanta hlutur
     */
    public void baetaVidYfirlit(MinPlanta planta) {
        MinPlantaSpjald spjald = new MinPlantaSpjald(planta);
        syndSpjold.add(spjald);
        //fxFlowPane.getChildren().add(spjald);
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

    public StringProperty getNafnAfLabel() {
        return notandiLabel.textProperty();
    }

    public MenuBar getFxMenuBar() {
        return fxMenuBar;
    }


    //handlerar fyrir þegar ýtt er á menuItem undir menu á menubar. Einn fyrir hvert menu

    private void rodunItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();
        System.out.println("Smellt á " + uppruni.getText());


        if (uppruni.getText().equals("almennt heiti A-Ö")) almenntStafrofsrod();
        else if (uppruni.getText().equals("almennt heiti Ö-A")) almenntOfugStafrofsrod();
        else if (uppruni.getText().equals("fræðiheiti A-Ö")) fraediStafrofsrod();
        //else if(uppruni.getText().equals("fræðiheiti Ö-A"))
    }

    private void siaItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();
        System.out.println("Smellt á " + uppruni.getText());
    }

    private void flokkunItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();
        System.out.println("Smellt á " + uppruni.getText());
    }


    //TODO: allt hér fyrir neðan er í raun vinnsla!!! Passa að gera viðeigandi vinnsluklasa og færa yfir!!!!

    private void almenntStafrofsrod() {
        Collections.sort(syndSpjold, this::compare);
    }

    private void almenntOfugStafrofsrod() {
        Collections.sort(syndSpjold, (a, b) -> compare(b, a));
    }

    private void fraediStafrofsrod() {
        Collections.sort(syndSpjold, fraediheitiComparator);
    }

    public int compare(Planta s1, Planta s2) {
        //if (s1.getAlmenntNafn().toLowerCase() < s1.getAlmenntNafn().toLowerCase())
        return s1.getAlmenntNafn().toLowerCase().compareTo(s2.getAlmenntNafn().toLowerCase());
    }


    //eða raða Node hlutum, og athuga gerð bara í compare aðferðinni

    public int compare(Node n1, Node n2) {
        if (n1 instanceof PlantaSpjald) {
            return ((PlantaSpjald) n1).getPlanta().getAlmenntNafn().toLowerCase().compareTo(((PlantaSpjald) n2).getPlanta().getAlmenntNafn().toLowerCase());
        }
        return ((MinPlantaSpjald) n1).getMinPlanta().getPlanta().getAlmenntNafn().toLowerCase().compareTo(((MinPlantaSpjald) n2).getMinPlanta().getPlanta().getAlmenntNafn().toLowerCase());
    }

    private Comparator<Node> fraediheitiComparator = new Comparator<Node>() {
        public int compare(Node n1, Node n2) {
            if (n1 instanceof PlantaSpjald) {
                return ((PlantaSpjald) n1).getPlanta().getLatnesktNafn().toLowerCase().compareTo(((PlantaSpjald) n2).getPlanta().getAlmenntNafn().toLowerCase());
            }
            return ((MinPlantaSpjald) n1).getMinPlanta().getPlanta().getLatnesktNafn().toLowerCase().compareTo(((MinPlantaSpjald) n2).getMinPlanta().getPlanta().getAlmenntNafn().toLowerCase());

        }
    };

}
