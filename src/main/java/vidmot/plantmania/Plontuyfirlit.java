/*
 *  todo setja mynd/texta ef yfirlitið er tómt: úbbs, engar plöntur hér. Athugaðu síurnar og reyndu aftur!
 */
package vidmot.plantmania;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.WindowEvent;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Notandi;
import vinnsla.plantmania.Planta;
import vinnsla.plantmania.enums.Uppruni;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

/**
 * Höfundur: Guðbjörg Viðja
 * Sérhæfður klasi fyrir yfirlit yfir hluti, bæði fyrir MinPlantaSpjald og PlantaSpjald hluti. Býður upp á að stjórna
 * sniði, með röðun og síu sem segir til um hvaða hlutir eru sýndir.
 * Það ætti kannksi að hafa líka vinnsluklasa fyrir þetta
 * Inniheldur flipa fyrir notanda og aðgerðir á hann
 */
public class Plontuyfirlit extends AnchorPane {
    @FXML
    private FlowPane fxFlowPane; //aðgangur í flowpane sem inniheldur spjöldin

    @FXML
    private MenuButton fxNotandi;

    @FXML
    private Menu fxSiaMenu, rodunMenu;// flokkunMenu //menuItems á MenuBar, til að stjórna sýnileika og röðun hluta

    @FXML
    private MenuItem fxSkraUt;

    @FXML
    private Label fxBanner;

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

    private Comparator<Node> yfirlitComparator;//núverandi comparator

    private BooleanProperty yfirlitTomt = new SimpleBooleanProperty(true); //ef þetta verður false: engar-minar-vidvorun tekið af

    public Plontuyfirlit() {
        LesaFXML.lesa(this, "plontuyfirlit.fxml");
        fxSkraUt.setOnAction(this::skraUtHandler);

        yfirlitComparator = naestaVokvunComparator;

        setjaMenuItemHandlera();

        stillaSiaMenuItems();

        //PlantaSpjald bætt við yfirlitið
        fyrstaHlutBaettVid.addListener((obs, o, n) -> {
            if (!o && n) {
                setRodunMenuItems();
                yfirlitComparator = almenntHeitiComparator;
                Collections.sort(ollSpjold, yfirlitComparator);
            }
        });
        tomtYfirlitVidvodun();
        
    }

    private void tomtYfirlitVidvodun() {
        fxFlowPane.getStyleClass().add("engar-minar-vidvorun");

        yfirlitTomt.addListener((obs, o, n) -> {
            if (!n) fxFlowPane.getStyleClass().removeAll("engar-minar-vidvorun");
        });

        filteredSpjold.addListener((ListChangeListener<? super Node>) change -> {
            change.next();
            if (change.wasRemoved()) {
                if (filteredSpjold.size() == 0)
                    fxFlowPane.getStyleClass().add("skoda-sia-vidvorun");//minarPlontur  getur ekki verið tómt
            } else if (change.wasAdded()) {
                fxFlowPane.getStyleClass().removeAll("skoda-sia-vidvorun");
            }

        });
    }

    /**
     * MenuItems undir sía eru upphafsstilltir, filteredList gerður, og listener settur til að passa að MenuItems sé
     * bætt við ef þarf.
     */
    private void stillaSiaMenuItems() {
        siaItems = fxSiaMenu.getItems();
        ((CheckMenuItem) fxSiaMenu.getItems().get(0)).setSelected(true);

        selectedSiaItems = new FilteredList<>(siaItems); //inniheldur valda CheckMenuItems (sem MenuItems)

        uppfaeraPredicateLista();

        Bindings.bindContent(fxFlowPane.getChildren(), filteredSpjold);
        //Bindings.bindContentBidirectional(fxFlowPane.getChildren(), filteredSpjold);//óþarfi, en virkar líka
        //todo: þarf bidirectional til að hlaða inn aftur? Eða þarf kannski að passa hvernig því er bætt við

        ollSpjold.addListener((ListChangeListener<? super Node>) change -> {
            change.next();
            if (change.wasAdded()) {
                athBaetaVidFlokk((List<Node>) change.getAddedSubList());
                Collections.sort(ollSpjold, yfirlitComparator);
                yfirlitTomt.set(false); //todo gera hér?
            }
        });
    }

    /**
     * setur predicate reglu á báða FilteredList hlutina, miðað við uppfærð skilyrði
     */
    private void uppfaeraPredicateLista() {
        Predicate<MenuItem> itemPred = smi -> (smi instanceof CheckMenuItem && ((CheckMenuItem) smi).isSelected());
        selectedSiaItems = new FilteredList<>(siaItems, itemPred);

        Predicate<Node> pred = plant -> {
            if (plant instanceof PlantaSpjald) {
                return selectedSiaItems.contains(upprunaMap.get(((PlantaSpjald) plant).getPlanta().getUppruni()));
            }
            return selectedSiaItems.contains(upprunaMap.get(((MinPlantaSpjald) plant).getMinPlanta().getUppruni()));
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

    /**
     * ef þessi aðferð keyrir þá er yfirlitið ekki tómt, og inniheldur PlantaSpjald
     * Vantar fleiri möguleika?
     * rodunMenu.getItems().add(new MenuItem("sjálfgefið"));
     */
    public void setRodunMenuItems() {
        rodunMenu.getItems().remove(2, 4);
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
        fxNotandi.setText(nafn);
    }

    public StringProperty getNafnAfLabel() {
        return fxNotandi.textProperty();
    }

    public Label getFxBanner() {
        return fxBanner;
    }

    /**
     * Raðar hlutum í yfirliti eftir því hvað er valið.
     *
     * @param event smellt á hlut undir röðun
     */
    private void rodunItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();

        if (uppruni.getText().equals("heiti A-Ö")) yfirlitComparator = almenntHeitiComparator;
        else if (uppruni.getText().equals("heiti Ö-A")) yfirlitComparator = almenntHeitiComparator.reversed();
        else if (uppruni.getText().equals("næsta vökvun")) yfirlitComparator = naestaVokvunComparator;
        else if (uppruni.getText().equals("síðast vökvað")) yfirlitComparator = naestaVokvunComparator.reversed();

        Collections.sort(ollSpjold, yfirlitComparator);
    }


    /**
     * selectedSiaItems er uppfært miðað við inntakið og sýnd spjöld svo uppfærð miðað við það.
     *
     * @param event smellt á CheckMenuItem undir sía
     */
    private void siaItemHandler(ActionEvent event) {
        MenuItem uppruni = (MenuItem) event.getSource();

        uppfaeraPredicateLista();

        uppfaeraFyrsta(uppruni);

        uppfaeraPredicateLista();
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


    /**
     * ákvað bara að nota ekki tilviksbreytu, væri það betra?
     *
     * @param event smellt á skrá út undir notandi menuButton
     */
    private void skraUtHandler(ActionEvent event) {
        PlantController pc = (PlantController) ViewSwitcher.lookup(View.ADALSIDA);
        vistaNotendaupplysingar(pc.getSkradurNotandi());
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }

    private void setjaCloseRequest() {
        //ná í stage
        //setja close request á stage


    }

    private void lokaGluggaHandler(WindowEvent event) {
        PlantController pc = (PlantController) ViewSwitcher.lookup(View.ADALSIDA);
        vistaNotendaupplysingar(pc.getSkradurNotandi());
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }

    /**
     * sækir alla notendur sem eru í skránni, finnur þann sem er skráður inn og uppfærir upplýsingar um hann með
     * því að skrifa í skrána.
     */
    public void vistaNotendaupplysingar(Notandi skradurNotandi) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        try {
            List<Notandi> notendur = objectMapper.readValue(new File("target/classes/vidmot/plantmania/notendur.json"), new TypeReference<>() {
            });
            for (Notandi n : notendur) {
                if (n.notendanafnProperty().get().equals(skradurNotandi.getNotendanafn())) {
                    n.setMinarPlontur(skradurNotandi.getMinarPlontur());
                    objectMapper.writeValue(new File("target/classes/vidmot/plantmania/notendur.json"), notendur);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }
    /* til að raða rétt eftir íslenska stafrófinu. Er ekki í notkun núna en verður bætt við seinna
        String stafrof = "A a Á á B b D d Ð ð E e É é F f G g H h I i Í í J j K k L l M m N n O o Ó ó P p R r S s T t U u Ú ú V v X x Y y Ý ý Þ þ Æ æ Ö ö";
        String[] srof = stafrof.split(" ");
     */


    /**
     * Comparator til að raða eftir almennu heiti.
     */
    private Comparator<Node> almenntHeitiComparator = (n1, n2) -> { //todo passa íslenska stafrófið
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
    //private Comparator<Node> naestaVokvunComparator = Comparator.comparingInt(n -> ((MinPlantaSpjald) n).getMinPlanta().getNaestaVokvun().get());
}
