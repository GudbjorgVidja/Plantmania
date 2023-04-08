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
import vinnsla.plantmania.enums.Uppruni;

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
        LesaFXML.lesa(this, "plontuyfirlit.fxml");

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
        

        stillaSiaMenuItems();

    }

    private void stillaFyrstaMenuItem() {

        selectedSiaItems.addListener((ListChangeListener<? super MenuItem>) change -> {
            change.next();
            if (change.wasAdded() && selectedSiaItems.size() == siaItems.size() - 1 && selectedSiaItems.get(0) != siaItems.get(0)) {
                System.out.println("allir moguleikar valdir nema fyrsti. Fyrsti verdur valinn");
                //uppfaeraPredicateLista();
            }
        });
    }

    private void stillaSiaMenuItems() {
        //síaItems inniheldur öll börn fxSiaMenu í upphafi
        siaItems = fxSiaMenu.getItems();
        ((CheckMenuItem) fxSiaMenu.getItems().get(0)).setSelected(true);

        // selectedSiaItems er filteredList af siaItems, inniheldur selected hlutina
        selectedSiaItems = new FilteredList<>(siaItems);

        uppfaeraPredicateLista();

        //Bindings.bindContent(fxFlowPane.getChildren(), filteredSpjold);
        Bindings.bindContentBidirectional(fxFlowPane.getChildren(), filteredSpjold);

        stillaFyrstaMenuItem();

        //setja listener, þ.a. ef hlut er bætt við þá sé passað að hann hafi flokk
        ollSpjold.addListener((ListChangeListener<? super Node>) change -> {
            change.next();
            if (change.wasAdded()) athBaetaVidFlokk((List<Node>) change.getAddedSubList());
        });
    }

    private void uppfaeraPredicateLista() {
        //sían uppfærð
        Predicate<MenuItem> itemPred = smi -> ((CheckMenuItem) smi).isSelected();
        selectedSiaItems.setPredicate(itemPred);//valdir hlutir
        //selectedSiaItems = new FilteredList<>(siaItems, itemPred);

        //yfirlitið uppfært
        Predicate<Node> pred = plant -> {
            if (plant instanceof PlantaSpjald) {
                return selectedSiaItems.contains(upprunaMap.get(((PlantaSpjald) plant).getPlanta().getUppruni()));
            }
            return selectedSiaItems.contains(upprunaMap.get(((MinPlantaSpjald) plant).getMinPlanta().getUppruni()));
        };
        //filteredSpjold = new FilteredList<>(ollSpjold, pred);
        filteredSpjold.setPredicate(pred);
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


    /**
     * Kallað er á aðferðina þegar nýjir hlutir bætast við yfirlitið, þ.e. PlantaSpjald eða MinPlantaSpjald.
     * Ef uppruni viðbótarinnar er ekki undir sía, þá er honum bætt við.
     *
     * @param nodes nýjar viðbætur við yfirilit
     */
    private void athBaetaVidFlokk(List<Node> nodes) {
        for (Node node : nodes) {
            Uppruni nyrUppruni = null;
            if (node instanceof MinPlantaSpjald) nyrUppruni = ((MinPlantaSpjald) node).getMinPlanta().getUppruni();
            else if (node instanceof PlantaSpjald) nyrUppruni = ((PlantaSpjald) node).getPlanta().getUppruni();


            if (!upprunaMap.containsKey(nyrUppruni) && nyrUppruni != null) {
                CheckMenuItem item = new CheckMenuItem(nyrUppruni.getStadur());
                item.setSelected(true);
                siaItems.add(item);
                item.setOnAction(this::siaItemHandler);
                upprunaMap.put(nyrUppruni, item);

                uppfaeraPredicateLista();
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

        //uppfaeraPredicateLista();
        //uppfaeraPredicateLista();


        //uppfaeraSyndSpjold();

        //todo af hverju virkar ekki að nota uppfaeraPredicateLista í staðinn fyrir þetta fyrir neðan?

        // ætti allt að gerast í setjaPredicateFilter aðferðinni
        Predicate<MenuItem> itemPred = mi -> {//þetta á að vera í sér aðferð
            return ((CheckMenuItem) mi).isSelected();
        };
        selectedSiaItems.setPredicate(itemPred);//valdir hlutir


        Predicate<Node> pred = it -> {
            if (it instanceof PlantaSpjald) {
                return selectedSiaItems.contains(upprunaMap.get(((PlantaSpjald) it).getPlanta().getUppruni()));
            }
            return selectedSiaItems.contains(upprunaMap.get(((MinPlantaSpjald) it).getMinPlanta().getUppruni()));
        };
        filteredSpjold.setPredicate(pred);


        uppfaeraPredicateLista();

        /*
        System.out.println("selectedSiaItems: " + selectedSiaItems);
        System.out.println("selectedSiaItems fjoldi: " + selectedSiaItems.size());
        System.out.println("filteredSpjold: " + filteredSpjold);
        System.out.println("filteredSpjold.size(): " + filteredSpjold.size());
        System.out.println("ollSpjold.size(): " + ollSpjold.size());
         */


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
