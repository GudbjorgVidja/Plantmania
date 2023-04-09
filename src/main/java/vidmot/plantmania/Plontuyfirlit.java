/*
 *  todo setja mynd/texta ef yfirlitið er tómt: úbbs, engar plöntur hér. Athugaðu síurnar og reyndu aftur!
 */
package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private Menu fxSiaMenu, rodunMenu;// flokkunMenu //menuItems á MenuBar, til að stjórna sýnileika og röðun hluta

    /**
     * Öll MenuItem undir sía. Inniheldur velja allt, og uppruna gildi plantna (og minnaPlantna) í yfirlitinu
     */
    private ObservableList<MenuItem> siaItems = FXCollections.observableArrayList();

    /**
     * Öll MenuItem sem hakað er við undir sía
     */
    private FilteredList<MenuItem> selectedSiaItems;

    /**
     * Tengir saman enum Uppruna gildi og CheckMenuItem fyrir gildið
     */
    private HashMap<Uppruni, MenuItem> upprunaMap = new HashMap<>();

    /**
     * Öll spjöld yfirlitsins, falin og sýnileg.
     */
    private ObservableList<Node> ollSpjold = FXCollections.observableArrayList();

    /**
     * Sýnileg spjöld í yfirlitinu að hverju sinni.
     */
    private FilteredList<Node> filteredSpjold = new FilteredList<>(ollSpjold);

    /**
     * Fylgist með þegar PlantaSpjald hlutur er fyrst settur inn, til að vita hvort röðunarmöguleikar séu réttir
     */
    private BooleanProperty fyrstaHlutBaettVid = new SimpleBooleanProperty(false);


    public Plontuyfirlit() {
        LesaFXML.lesa(this, "plontuyfirlit.fxml");

        setjaMenuItemHandlera();

        stillaSiaMenuItems();

        //PlantaSpjald bætt við yfirlitið
        fyrstaHlutBaettVid.addListener((obs, o, n) -> {
            if (!o && n) setRodunMenuItems();
        });
        //eða:  fyrstaHlutBaettVid.addListener((obs, o, n) -> (if (!o && n)  setRodunMenuItems()));
    }

    /**
     * MenuItems undir sía eru upphafsstilltir, filteredList gerður, og listener settur til að passa að MenuItems sé
     * bætt við ef þarf.
     */
    private void stillaSiaMenuItems() {
        //síaItems inniheldur öll börn fxSiaMenu í upphafi
        siaItems = fxSiaMenu.getItems();
        ((CheckMenuItem) fxSiaMenu.getItems().get(0)).setSelected(true);

        selectedSiaItems = new FilteredList<>(siaItems); //inniheldur valda CheckMenuItems (sem MenuItems)

        uppfaeraPredicateLista();

        Bindings.bindContent(fxFlowPane.getChildren(), filteredSpjold);
        //Bindings.bindContentBidirectional(fxFlowPane.getChildren(), filteredSpjold);//óþarfi, en virkar líka

        ollSpjold.addListener((ListChangeListener<? super Node>) change -> {
            change.next();
            if (change.wasAdded()) athBaetaVidFlokk((List<Node>) change.getAddedSubList());
        });
    }

    /**
     * setur predicate reglu á báða FilteredList hlutina, miðað við uppfærð skilyrði
     */
    private void uppfaeraPredicateLista() {
        //sían uppfærð
        Predicate<MenuItem> itemPred = smi -> ((CheckMenuItem) smi).isSelected();
        //selectedSiaItems.setPredicate(itemPred);//valdir hlutir
        selectedSiaItems = new FilteredList<>(siaItems, itemPred);

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

    /**
     * ef þessi aðferð keyrir þá er yfirlitið ekki tómt, og inniheldur PlantaSpjald
     * Vantar fleiri möguleika?
     */
    public void setRodunMenuItems() {
        rodunMenu.getItems().remove(2, 4);
        //rodunMenu.getItems().add(new MenuItem("sjálfgefið"));
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

        for (MenuItem item : fxSiaMenu.getItems()) {
            item.setOnAction(this::siaItemHandler);
        }
    }


    /**
     * PlantaSpjald hlut bætt við yfirlit. Notað við innlesturinn í upphafi.
     *
     * @param planta Planta
     */
    public void baetaVidYfirlit(Planta planta) {
        PlantaSpjald spjald = new PlantaSpjald(planta);
        ollSpjold.add(spjald);
        fyrstaHlutBaettVid.set(true);
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
    }


    //      ** getterar og setterar **

    public void setNotandiLabel(String nafn) {
        notandiLabel.setText(nafn);
    }

    public StringProperty getNafnAfLabel() {
        return notandiLabel.textProperty();
    }


    /**
     * todo passa að röðunin haldist, og að ef hlut er bætt við yfirlit þá kemur hann inn á réttum stað.
     * Raðar hlutum í yfirliti eftir því hvað er valið.
     *
     * @param event smellt á hlut undir röðun
     */
    private void rodunItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();
        //System.out.println("Smellt á " + uppruni.getText());

        if (uppruni.getText().equals("heiti A-Ö")) Collections.sort(ollSpjold, almenntHeitiComparator);
        else if (uppruni.getText().equals("heiti Ö-A")) Collections.sort(ollSpjold, almenntHeitiComparator.reversed());
        else if (uppruni.getText().equals("næsta vökvun")) Collections.sort(ollSpjold, naestaVokvunComparator);
        else if (uppruni.getText().equals("síðast vökvað"))
            Collections.sort(ollSpjold, naestaVokvunComparator.reversed());
    }


    /**
     * selectedSiaItems er uppfært miðað við inntakið og sýnd spjöld svo uppfærð miðað við það.
     *
     * @param event smellt á CheckMenuItem undir sía
     */
    private void siaItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();

        /*
        System.out.println("Smellt á " + uppruni.getText());
        System.out.println("selectedSiaItems.size(): " + selectedSiaItems.size());
        System.out.println("siaItems.size(): " + siaItems.size());
        System.out.println();
         */

        uppfaeraPredicateLista();

        uppfaeraFyrsta(uppruni);

        uppfaeraPredicateLista();

        /*
        System.out.println("selectedSiaItems.size(): " + selectedSiaItems.size());
        System.out.println("siaItems.size(): " + siaItems.size());
        System.out.println();
         */
    }

    /**
     * Fundið er hvort eitthvað breytist annað en það sem ýtt var á. Velja allt möguleikinn er sérstaklega athugaður, og
     * hvort allt sé rétt miðað við hann.
     *
     * @param uppruni MenuItem sem ýtt var á
     */
    private void uppfaeraFyrsta(MenuItem uppruni) {
        if (uppruni.equals(siaItems.get(0))) {
            System.out.println("smella a fyrsta :)");

            if (((CheckMenuItem) uppruni).isSelected()) {//fundið að verið var að velja fyrsta
                System.out.println("fyrsta er nu valid");
                for (MenuItem item : siaItems) {
                    if (item instanceof CheckMenuItem && !((CheckMenuItem) item).isSelected()) {
                        ((CheckMenuItem) item).setSelected(true);
                    }
                }
            } else if (!((CheckMenuItem) uppruni).isSelected()) {//fundið að verið var að afvelja fyrsta
                System.out.println("fyrsta er ekki lengur valid");
                for (MenuItem item : siaItems) {
                    if (item instanceof CheckMenuItem && ((CheckMenuItem) item).isSelected()) {
                        ((CheckMenuItem) item).setSelected(false);
                    }
                }
            }
        } else if (((CheckMenuItem) siaItems.get(0)).isSelected() && siaItems.size() > selectedSiaItems.size()) {
            ((CheckMenuItem) siaItems.get(0)).setSelected(false);
        } else if (selectedSiaItems.size() == siaItems.size() - 1 && !((CheckMenuItem) siaItems.get(0)).isSelected()) {
            ((CheckMenuItem) siaItems.get(0)).setSelected(true);
        }
    }


    //todo: eiga comparatorar (fyrir neðan) að vera í vinnslu?

    /* til að raða rétt eftir íslenska stafrófinu
        String stafrof = "A a Á á B b D d Ð ð E e É é F f G g H h I i Í í J j K k L l M m N n O o Ó ó P p R r S s T t U u Ú ú V v X x Y y Ý ý Þ þ Æ æ Ö ö";
        String[] srof = stafrof.split(" ");

     */


    /**
     * Comparator til að raða eftir almennu heiti.
     */
    private Comparator<Node> almenntHeitiComparator = (n1, n2) -> {
        if (n1 instanceof PlantaSpjald) {
            return ((PlantaSpjald) n1).getPlanta().getAlmenntNafn().toLowerCase().compareTo(((PlantaSpjald) n2).getPlanta().getAlmenntNafn().toLowerCase());
        }
        return ((MinPlantaSpjald) n1).getMinPlanta().getNickName().toLowerCase().compareTo(((MinPlantaSpjald) n2).getMinPlanta().getNickName().toLowerCase());
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
