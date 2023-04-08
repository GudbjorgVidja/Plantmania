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
import vinnsla.plantmania.LesaPlontur;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Planta;
import vinnsla.plantmania.Uppruni;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

    //allir möguleikar undir sia. Bæta nýjum item handvirkt við viðmótið
    private ObservableList<MenuItem> siaItems = FXCollections.observableArrayList();//todo á fyrsta líka að vera hér inni?
    private FilteredList<MenuItem> selectedSiaItems;//passa skilgreiningu ef fyrsta er bætt við siaItems

    //tengir saman flokk(uppruna) og checkMenuItem fyrir hann
    private HashMap<Uppruni, MenuItem> upprunaMap = new HashMap<>();

    //Öll spjöld sem sett hafa verið í yfirlitið, þ.e. öll spjöld sem hægt er að sjá
    private ObservableList<Node> ollSpjold = FXCollections.observableArrayList();//Hlutirnir í þessu yfirliti, baselistinn

    //Sýnileg spjöld í yfirlitinu að hverju sinni.
    private FilteredList<Node> filteredSpjold = new FilteredList<>(ollSpjold);

    public Plontuyfirlit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("plontuyfirlit.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        /*todo ný breyting
        Bindings.bindContent(fxFlowPane.getChildren(), filteredSpjold);//flowpane inniheldur alltaf filteredSpjold

        //ef nýjum hlut er bætt við yfirlit:
        ollSpjold.addListener((ListChangeListener<? super Node>) change -> {
            change.next();
            if (change.wasAdded()) athBaetaVidFlokk((List<Node>) change.getAddedSubList());//passa að þetta sé löglegt
        });

        siaMenuReglur();

         */

        //setur onAction
        setjaMenuItemHandlera();

        //velja hvaða röðunarmöguleikar eru gefnir (mismunandi fyrir plöntur og mínar pöntur


        //todo síaItems inniheldur öll börn fxSiaMenu
        siaItems = fxSiaMenu.getItems();
        ((CheckMenuItem) fxSiaMenu.getItems().get(0)).setSelected(true);

        //todo selectedSiaItems inniheldur öll sömu stök og siaItems
        selectedSiaItems = new FilteredList<>(siaItems);
        uppfaeraSiaPred();

        //uppfaeraSyndSpjold();//upphafsstilling?
        Bindings.bindContent(fxFlowPane.getChildren(), filteredSpjold);

        //TOdo setja listener, þ.a. ef hlut er bætt við þá sé passað að hann hafi flokk
        ollSpjold.addListener((ListChangeListener<? super Node>) change -> {
            change.next();
            if (change.wasAdded()) athBaetaVidFlokk((List<Node>) change.getAddedSubList());
        });

    }

    private void uppfaeraSiaPred() {//til að selectedSiaItems innihaldi alla valda möguleika
        Predicate<MenuItem> itemPred = smi -> ((CheckMenuItem) smi).isSelected();
        selectedSiaItems.setPredicate(itemPred);//valdir hlutir
    }

    private void uppfaeraSyndSpjold() {//ef hakað við flokkinn: hlutur sýndur
        Predicate<Node> pred = it -> {
            if (it instanceof PlantaSpjald) {
                return selectedSiaItems.contains(upprunaMap.get(((PlantaSpjald) it).getPlanta().getUppruni()));
            }
            return selectedSiaItems.contains(upprunaMap.get(((MinPlantaSpjald) it).getMinPlanta().getUppruni()));
        };
        filteredSpjold.setPredicate(pred);
    }

    //todo kallað á alltaf þegar hlut er bætt við yfirlitið
    private void athBaetaVidFlokk(List<Node> nodes) {//node er viðbótin
        for (Node node : nodes) {//fyrir hvern hlut
            Uppruni nyrUppruni = null;
            if (node instanceof MinPlantaSpjald) nyrUppruni = ((MinPlantaSpjald) node).getMinPlanta().getUppruni();
            else if (node instanceof PlantaSpjald) nyrUppruni = ((PlantaSpjald) node).getPlanta().getUppruni();


            if (!upprunaMap.containsKey(nyrUppruni) && nyrUppruni != null) {
                CheckMenuItem item = new CheckMenuItem(nyrUppruni.getStadur());
                item.setSelected(true);
                //Bindings.unbindContent(fxSiaMenu.getItems().subList(1, siaItems.size()), selectedSiaItems);
                siaItems.add(item);
                item.setOnAction(this::siaItemHandler);
                upprunaMap.put(nyrUppruni, item);
                //TODO passa að predicate uppfærist
                uppfaeraSiaPred();
                uppfaeraSyndSpjold();
                //setjaPredicateFilter();
            }

        }

    }


    public void setRodunMenuItems() {
        //pæla í að setja handvirkt inn nöfn flokkanna? eftir tegund
    }

    /**
     * kallað á þetta þegar allarPlontur yfirlitið er upphafsstillt. Lesa inn héðan frekar en að gera það úr controller
     */
    public void lesaAllarPlontur() {
        //lesa inn allar plöntur
        List<Planta> allarPlontur = (new LesaPlontur()).getPlontur();


        //gera plöntuspjöld fyrir allar plöntur
        for (Planta p : allarPlontur) {
            PlantaSpjald spjald = new PlantaSpjald(p);
            ollSpjold.add(spjald);//betra að setja í lista og setja inn allt í einu?
        }

        //setja allar inn í yfirlitið
        /*
        allarPlontur.addAll((new LesaPlontur()).getPlontur());
        for (Planta planta : allarPlontur) {
            fxAllarPlonturYfirlit.baetaVidYfirlit(planta);
        }


                for (Planta planta : plontuListi) {
            PlantaSpjald spjald = new PlantaSpjald(planta);
            ollSpjold.add(spjald);
        }
         */
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
     * setur reglu á FilteredList af menuItems. FilteredSiaItems inniheldur hluti af einhverjum uppruna sem hakað er við.
     * //selectedSiaItems inniheldur valda flokka
     * Uppfærir filter reglu fyrir selectedSiaItems og filteredSpjold
     */
    private void setjaPredicateFilter() {
        Predicate<MenuItem> itemPred = mi -> ((CheckMenuItem) mi).isSelected();
        selectedSiaItems.setPredicate(itemPred);//valdir hlutir
        //líka hægt að nota eftirfarandi: selectedSiaItems.setPredicate(mi -> ((CheckMenuItem)mi).isSelected());

        //ath hvort selectedSiaItems innihaldi flokkinn fyrir uppruna plöntuspjaldsins
        Predicate<Node> pred = it -> {
            if (it instanceof PlantaSpjald) {
                return selectedSiaItems.contains(upprunaMap.get(((PlantaSpjald) it).getPlanta().getUppruni()));
            }
            return selectedSiaItems.contains(upprunaMap.get(((MinPlantaSpjald) it).getMinPlanta().getUppruni()));
        };
        filteredSpjold.setPredicate(pred);
    }

    /*
    private boolean allirFlokkarValdir() {
        //frekar bara athuga stærð filtered lista miðað við base lista
        for (MenuItem item : fxSiaMenu.getItems()) {
            if (!((CheckMenuItem) item).isSelected() && !item.equals(fxSiaMenu.getItems().get(0))) {
                return false;
            }
        }
        return true;
    }

     */


    /**
     * PlantaSpjald hlut bætt við yfirlit
     *
     * @param planta Planta
     */
    public void baetaVidYfirlit(Planta planta) {
        PlantaSpjald spjald = new PlantaSpjald(planta);
        ollSpjold.add(spjald);
    }


    public void baetaVidYfirlit(PlantaSpjald ps) {
        ollSpjold.add(ps);
    }

    public void baetaVidYfirlit(ObservableList<Planta> plontuListi) {
        for (Planta planta : plontuListi) {
            PlantaSpjald spjald = new PlantaSpjald(planta);
            ollSpjold.add(spjald);
        }
    }


    /**
     * Bætir MinPlanta hlut við yfirlitið, sem þýðir að þetta er yfirlitið í mínar plöntur flipanum.
     *
     * @param minPlanta MinPlanta hlutur
     */
    public void baetaVidYfirlit(MinPlanta minPlanta) {
        MinPlantaSpjald spjald = new MinPlantaSpjald(minPlanta);
        ollSpjold.add(spjald);

        //hafa syndirFlokkar eða það bara flokkarnir sem eru á plöntum í ollSpjold, sem eru öll möguleg spjöld (base listinn)
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
            Collections.sort(ollSpjold, almenntHeitiComparator);
        else if (uppruni.getText().equals("almennt heiti Ö-A"))
            Collections.sort(ollSpjold, almenntHeitiComparator.reversed());
        else if (uppruni.getText().equals("fræðiheiti A-Ö"))//sleppa kannski fræðiheiti?
            Collections.sort(ollSpjold, fraediheitiComparator);
            //else if(uppruni.getText().equals("fræðiheiti Ö-A"))
        else if (uppruni.getText().equals("næsta vökvun")) Collections.sort(ollSpjold, naestaVokvunComparator);
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
        // ætti allt að gerast í setjaPredicateFilter aðferðinni
        Predicate<MenuItem> itemPred = mi -> {//þetta á að vera í sér aðferð
            return ((CheckMenuItem) mi).isSelected();
        };
        selectedSiaItems.setPredicate(itemPred);//valdir hlutir

        System.out.println("selectedSiaItems: " + selectedSiaItems);
        System.out.println("selectedSiaItems fjoldi: " + selectedSiaItems.size());

        Predicate<Node> pred = it -> {
            if (it instanceof PlantaSpjald) {
                return selectedSiaItems.contains(upprunaMap.get(((PlantaSpjald) it).getPlanta().getUppruni()));
            }
            return selectedSiaItems.contains(upprunaMap.get(((MinPlantaSpjald) it).getMinPlanta().getUppruni()));
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
    public ObservableList<Node> getOllSpjold() {
        return ollSpjold;
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
/*

    //sleppa þessu alveg, kannski bara taka út
    private void siaMenuBreytingar() {
        /*
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

         */




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

        /*
        checkMenuItems.addListener((ListChangeListener<? super MenuItem>) change -> {
            change.next();
            if (change.wasRemoved()) fxSiaMenu.getItems().removeAll(change.getRemoved());
            if (change.wasAdded()) fxSiaMenu.getItems().addAll(change.getAddedSubList());
        });



//checkMenuItems.add(new CheckMenuItem("nýtt item"));

    }

 */
