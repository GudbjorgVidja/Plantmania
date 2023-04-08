/*
 * TODO: passa að sía innihaldi bara uppruna sem eru í yfirlitinu
 */
package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Planta;
import vinnsla.plantmania.Uppruni;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * Sérhæfður klasi fyrir yfirlit yfir hluti, bæði fyrir MinPlantaSpjald og PlantaSpjald hluti. Býður upp á að stjórna
 * sniði, með röðun og síu sem segir til um hvaða hlutir eru sýndir.
 * Það ætti kannksi að hafa líka vinnsluklasa fyrir þetta
 */
public class Plontuyfirlit extends AnchorPane {
    @FXML
    private FlowPane fxFlowPane; //aðgangur í flowpane sem inniheldur spjöldin

    @FXML
    private Label notandiLabel;//label í efra hægra horni með notendanafni

    @FXML
    private Menu fxSiaMenu, flokkunMenu, rodunMenu;//menuItems á MenuBar, til að stjórna sýnileika og röðun hluta

    private ObservableList<MenuItem> checkMenuItems = FXCollections.observableArrayList();

    private ObservableList<CheckMenuItem> siaMenuItems = FXCollections.observableArrayList();

    private ObservableList<MenuItem> siaItems = FXCollections.observableArrayList();
    private FilteredList<MenuItem> filteredSiaItems;

    private HashMap<Uppruni, MenuItem> upprunaMap = new HashMap<>();//tengir saman flokkinn og checkMenuItem fyrir flokkinn

    private ObservableList<Node> syndSpjold = FXCollections.observableArrayList();//Hlutirnir í þessu yfirliti, baselistinn

    private FilteredList<Node> filteredSpjold = new FilteredList<>(syndSpjold); //filtered listinn

    public Plontuyfirlit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("plontuyfirlit.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        //TODO passa að þetta gerist ekki óþarflega oft, eða jafnvel finna aðra leið
        filteredSpjold.addListener((ListChangeListener<? super Node>) change -> {
            fxFlowPane.getChildren().clear();
            fxFlowPane.getChildren().addAll(filteredSpjold);
        });


        //siaMenuBreytingar();
        siaMenuReglur();

        //setur onAAction
        setjaMenuItemHandlera();
    }


    /**
     * setur handlera á menuItems
     */
    private void setjaMenuItemHandlera() {
        for (MenuItem item : rodunMenu.getItems()) {
            item.setOnAction(this::rodunItemHandler);
        }

        for (MenuItem item : flokkunMenu.getItems()) {
            item.setOnAction(this::flokkunItemHandler);
        }

        for (MenuItem item : fxSiaMenu.getItems()) {
            item.setOnAction(this::siaItemHandler);
        }
    }


    /**
     * --Flokkar settir á MenuItemið
     * --Flokkar og fyrsta allt sett sem merkt
     * --menuItems í viðmóti og observable Listinn siaItems bundin saman bidirectionally
     * ef allt er merkt og smellt á fyrsta: ekkert merkt
     * ef allt nema eitt er merkt og það svo merkt líka: fyrsta merkist
     * ef ekkert er merkt og smellt á fyrsta: allt merkist
     * ef allt er merkt en fyrsta ekki, og smellt á fyrsta:
     */
    private void siaMenuReglur() {
        ((CheckMenuItem) fxSiaMenu.getItems().get(0)).setSelected(true);
        Uppruni[] upprunar = Uppruni.values();
        for (Uppruni upp : upprunar) {
            CheckMenuItem item = new CheckMenuItem(upp.getStadur());
            //CheckMenuItem item = new CheckMenuItem(upp.toString().toLowerCase());
            item.setSelected(true);
            siaItems.add(item);
            upprunaMap.put(upp, item);
        }
        //fxSiaMenu.getItems().addAll(siaItems);
        Bindings.bindContentBidirectional(fxSiaMenu.getItems(), siaItems);

        filteredSiaItems = new FilteredList<>(siaItems);//inniheldur valda siaItems

        setjaPredicateFilter();


        //sleppa, ekki geta bæst við fleiri hlutir á keyrslutíma
        siaItems.addListener((ListChangeListener<? super MenuItem>) change -> {
            //uppfæra síureglur
            setjaPredicateFilter();
            System.out.println("filteredSiaItems: " + filteredSiaItems);
        });

    }

    /**
     * setur reglu á FilteredList af menuItems. FilteredSiaItems inniheldur hluti af einhverjum uppruna sem hakað er við.
     */
    private void setjaPredicateFilter() {
        //filteredSiaItems inniheldur valda flokka

        Predicate<MenuItem> itemPred = mi -> ((CheckMenuItem) mi).isSelected();
        filteredSiaItems.setPredicate(itemPred);//valdir hlutir
        //líka hægt að nota eftirfarandi:
        //filteredSiaItems.setPredicate(mi -> ((CheckMenuItem)mi).isSelected());


        //ath hvort filteredSiaItems innihaldi flokkinn fyrir uppruna plöntuspjaldsins
        Predicate<Node> pred = it -> {
            if (it instanceof PlantaSpjald) {
                return filteredSiaItems.contains(upprunaMap.get(((PlantaSpjald) it).getPlanta().getUppruni()));
            }
            return filteredSiaItems.contains(upprunaMap.get(((MinPlantaSpjald) it).getMinPlanta().getUppruni()));
        };
        filteredSpjold.setPredicate(pred);
    }

    private boolean allirFlokkarValdir() {
        for (MenuItem item : fxSiaMenu.getItems()) {
            if (!((CheckMenuItem) item).isSelected() && !item.equals(fxSiaMenu.getItems().get(0))) {
                return false;
            }
        }
        return true;
    }

    private void siaMenuBreytingar() {
        checkMenuItems.setAll(fxSiaMenu.getItems()); //checkMenuItems er uppfærð útgáfa
        System.out.println("checkmenuitems.size: " + checkMenuItems.size());

        //siaMenuItems.add((CheckMenuItem) fxSiaMenu.getItems().get(0));
        //siaItems.add(fxSiaMenu.getItems().get(0));
        //siaMenuItems.add((CheckMenuItem) fxSiaMenu.getItems().get(0));
        Uppruni[] upprunar = Uppruni.values();
        for (Uppruni upp : upprunar) {
            CheckMenuItem item = new CheckMenuItem(upp.getStadur());
            item.setSelected(true);
            //siaItems.add();
            siaMenuItems.add(item);
        }
        fxSiaMenu.getItems().addAll(siaMenuItems);




        /*
        siaMenuItems.addListener((ListChangeListener<? super CheckMenuItem>) change -> {
            //Bæta við nýjum viðbótum
        });

         */
        //Bindings.bindContentBidirectional(siaMenuItems, (List<CheckMenuItem>)fxSiaMenu.getItems());
        //Bindings.bindContent(siaMenuItems, fxSiaMenu.getItems().iterator() instanceof CheckMenuItem);


        //checkMenuItems.remove(0, 1); //inniheldur bara breytanlegu stökin

        //upprunaItemar.addAll(checkMenuItems instanceof CheckMenuItem);
        //upprunaItemar.remove(0);

        /*
        for (CheckMenuItem item : upprunaItemar) {
            System.out.println(item.getText());
        }

         */

        checkMenuItems.addListener((ListChangeListener<? super MenuItem>) change -> {
            change.next();
            if (change.wasRemoved()) fxSiaMenu.getItems().removeAll(change.getRemoved());
            if (change.wasAdded()) fxSiaMenu.getItems().addAll(change.getAddedSubList());
        });

        checkMenuItems.add(new CheckMenuItem("nýtt item"));

    }

    /**
     * PlantaSpjald hlut bætt við yfirlit
     *
     * @param planta Planta
     */
    public void baetaVidYfirlit(Planta planta) {
        PlantaSpjald spjald = new PlantaSpjald(planta);
        syndSpjold.add(spjald);
    }


    /**
     * Bætir MinPlanta hlut við yfirlitið, sem þýðir að þetta er yfirlitið í mínar plöntur flipanum.
     *
     * @param minPlanta MinPlanta hlutur
     */
    public void baetaVidYfirlit(MinPlanta minPlanta) {
        MinPlantaSpjald spjald = new MinPlantaSpjald(minPlanta);
        syndSpjold.add(spjald);

        //hafa syndirFlokkar eða það bara flokkarnir sem eru á plöntum í syndSpjold, sem eru öll möguleg spjöld (base listinn)
    }


    //      ** getterar og setterar **

    public void setNotandiLabel(String nafn) {
        notandiLabel.setText(nafn);
    }

    public StringProperty getNafnAfLabel() {
        return notandiLabel.textProperty();
    }


    /**
     * TODO passa að röðunin haldist, og að ef hlut er bætt við yfirlit þá kemur hann inn á réttum stað.
     * Raðar hlutum í yfirliti eftir því hvað er valið.
     *
     * @param event smellt á hlut undir röðun
     */
    private void rodunItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();
        System.out.println("Smellt á " + uppruni.getText());

        if (uppruni.getText().equals("almennt heiti A-Ö"))
            Collections.sort(syndSpjold, almenntHeitiComparator);
        else if (uppruni.getText().equals("almennt heiti Ö-A"))
            Collections.sort(syndSpjold, almenntHeitiComparator.reversed());
        else if (uppruni.getText().equals("fræðiheiti A-Ö"))//sleppa kannski fræðiheiti?
            Collections.sort(syndSpjold, fraediheitiComparator);
            //else if(uppruni.getText().equals("fræðiheiti Ö-A"))
        else if (uppruni.getText().equals("næsta vökvun")) Collections.sort(syndSpjold, naestaVokvunComparator);
    }

    /**
     * TODO einfalda og hreinsa upp kóðann -G
     *
     * @param event smellt á CheckMenuItem undir sía
     */
    private void siaItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();
        System.out.println("Smellt á " + uppruni.getText());

        setjaPredicateFilter();
        Predicate<MenuItem> itemPred = mi -> {
            return ((CheckMenuItem) mi).isSelected();
        };
        filteredSiaItems.setPredicate(itemPred);//valdir hlutir
        System.out.println("filteredSiaItems: " + filteredSiaItems);
        System.out.println("filteredSiaItems fjoldi: " + filteredSiaItems.size());

        Predicate<Node> pred = it -> {
            if (it instanceof PlantaSpjald) {
                return filteredSiaItems.contains(upprunaMap.get(((PlantaSpjald) it).getPlanta().getUppruni()));
            }
            return filteredSiaItems.contains(upprunaMap.get(((MinPlantaSpjald) it).getMinPlanta().getUppruni()));
        };
        filteredSpjold.setPredicate(pred);

        System.out.println("filteredSpjold: " + filteredSpjold);


    }

    //sleppa kannski flokkun til að byrja með? held það sé óþarflega flókið
    private void flokkunItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();
        System.out.println("Smellt á " + uppruni.getText());
    }

    /**
     * @return öll spjöld yfirlits
     */
    public ObservableList<Node> getSyndSpjold() {
        return syndSpjold;
    }


    //TODO: flest hér fyrir neðan er í raun vinnsla!!! Passa að gera viðeigandi vinnsluklasa og færa yfir!!!!

    /* til að raða rétt eftir íslenska stafrófinu
        String stafrof = "A a Á á B b D d Ð ð E e É é F f G g H h I i Í í J j K k L l M m N n O o Ó ó P p R r S s T t U u Ú ú V v X x Y y Ý ý Þ þ Æ æ Ö ö";
        String[] srof = stafrof.split(" ");

     */


    /**
     * Comparator til að raða eftir almennu heiti.
     * TODO passa að raða MinPlanta eftir Nickname, ekki bara almennu heiti
     */
    private Comparator<Node> almenntHeitiComparator = (n1, n2) -> {
        if (n1 instanceof PlantaSpjald) {
            return ((PlantaSpjald) n1).getPlanta().getAlmenntNafn().toLowerCase().compareTo(((PlantaSpjald) n2).getPlanta().getAlmenntNafn().toLowerCase());
        }
        return ((MinPlantaSpjald) n1).getMinPlanta().getAlmenntNafn().toLowerCase().compareTo(((MinPlantaSpjald) n2).getMinPlanta().getAlmenntNafn().toLowerCase());
    };

    /**
     * Comparator til að raða eftir fræðiheiti
     * TODO ákveða hvort við viljum hafa þetta sem möguleika, og hvort fræðiheiti eigi að sjást á yfirliti
     */
    private Comparator<Node> fraediheitiComparator = new Comparator<Node>() {
        public int compare(Node n1, Node n2) {
            if (n1 instanceof PlantaSpjald) {
                return ((PlantaSpjald) n1).getPlanta().getLatnesktNafn().toLowerCase().compareTo(((PlantaSpjald) n2).getPlanta().getAlmenntNafn().toLowerCase());
            }
            return ((MinPlantaSpjald) n1).getMinPlanta().getLatnesktNafn().toLowerCase().compareTo(((MinPlantaSpjald) n2).getMinPlanta().getAlmenntNafn().toLowerCase());

        }
    };

    /**
     * Comparator til að raða eftir næsta vökvunardegi
     */
    private Comparator<Node> naestaVokvunComparator = new Comparator<Node>() {
        public int compare(Node n1, Node n2) {
            return Integer.compare(((MinPlantaSpjald) n1).getMinPlanta().getNaestaVokvun().get(), ((MinPlantaSpjald) n2).getMinPlanta().getNaestaVokvun().get());
        }
    };
}
