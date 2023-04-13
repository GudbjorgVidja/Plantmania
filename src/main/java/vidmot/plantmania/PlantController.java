package vidmot.plantmania;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import vinnsla.plantmania.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


//TODO: hafa einhvers staðar lista af öllum plöntum, til að hafa auðveldari (og kannski hagkvæmari) aðgang að þeim á keyrslutíma. S: þarf þess? er ekki bara skoðað út frá minPLanta?  G: jú sorry

/**
 * Controller fyrir aðalsenuna. Þar sem við notum TabPane er það sem væri annars í 5 senum eða svo í einni
 * senu, svo það er meira hér en væri annars
 */
public class PlantController {
    @FXML
    private Plontuyfirlit fxMinarPlonturYfirlit; //mínar plöntur yfirlitið.
    @FXML
    private Plontuyfirlit fxAllarPlonturYfirlit; //yfirlit yfir allar plöntur
    @FXML
    private Dagatal fxDagatal;
    private UpphafController upphafController;
    private ObjectProperty<Notandi> skradurNotandi = new SimpleObjectProperty<>();

    private Notendaupplysingar notendaupplysingar;// = new Notendaupplysingar();//setja listener fyrir þetta/hafa sem ObjectProperty?


    //TODO: væri ekki hægt að hafa þetta local þar sem þetta er notað? eða hvað
    //bara kallað á tvisvar: til að setja inn í listann og til að setja í yfirlit
    //private ObservableList<Planta> allarPlontur = FXCollections.observableArrayList();//er í vesi, geymi hér
    //ætti frekar kannski að geyma í öðrum klasa, t.d. vinnsluklasa fyrir allarPlonturYfirlit

    public void initialize() {
        upphafController = (UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA);
        skradurNotandi.setValue(upphafController.getSkradurNotandi());

        notendaupplysingar = new Notendaupplysingar(skradurNotandi.get().getMinarPlontur());
        notendaupplysingar.finnaFyrriOgSidariVokvanirListener(skradurNotandi.get().getMinarPlontur());
        System.out.println(skradurNotandi.get());
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());//af hverju ekki bara upphafsstilling?

        //fxAllarPlonturYfirlit.lesaAllarPlontur();//loadar fxml oftar
        lesaInnAllarPlontur();
        dagatalsEventFilterar();

        //binda nafn notanda við label i báðum yfirlitum.
        // Væri gott að hafa í Plontuyfirlit klasanum, en viewSwitcher er leiðinlegur við mig rn
        fxMinarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));
        fxAllarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));

        birtaNotendaPlontur();
        //notendaupplysingar.finnaFyrriOgSidariVokvanirListener(skradurNotandi.get().getMinarPlontur());
        hladaUpplysingum();

        System.out.println(skradurNotandi.get());
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());

        System.out.println(skradurNotandi);
    }

    private void hladaUpplysingum() {
        for (MinPlanta m : skradurNotandi.get().getMinarPlontur()) {
            //birtaNotendaPlontur
            fxMinarPlonturYfirlit.baetaVidYfirlit(m);

            m.sidastaVokvunListener();
            m.naestaVokvunRegla();
            m.medaltimiMilliVokvanaListener();
            //m.reiknaPlanadarVokvanir();//Þetta lætur naestuVokvanir tvítelja allar vökvanir, en útgáfan sem þetta setur inn virðist rétt
            System.out.println("MinPlanta(Planta planta) smidur");

            //naestaVokvun = (thinnTimiMilliVokvana);

        }
    }

    /**
     * lesa inn af skrá, og setja í yfirlitið  fxAllarPlonturYfirlit
     */
    private void lesaInnAllarPlontur() {
        List<Planta> lesnarPlontur = (new LesaPlontur()).getPlontur();
        for (Planta p : lesnarPlontur) {

            fxAllarPlonturYfirlit.baetaVidYfirlit(p);

            /*loadar nákvæmlega jafn oft og línan fyrir ofan
            PlantaSpjald spjald = new PlantaSpjald(p);
            fxAllarPlonturYfirlit.baetaVidYfirlit(spjald);

             */

        }

        /* //sleppa alveg allarPlontur listanum
        allarPlontur.addAll((new LesaPlontur()).getPlontur());
        for (Planta planta : allarPlontur) {
            fxAllarPlonturYfirlit.baetaVidYfirlit(planta);
        }
         */
    }

    /*private void bindaNotendaPlontur(){
        fxMinarPlonturYfirlit.getMinarPlonturYfirlit().addListener((ListChangeListener<? super Node>) change -> {
            change.next();
            if(change.wasAdded()){
                List<Node> listi = (List<Node>) change.getAddedSubList();
                for(Node node: listi){
                    if(node instanceof MinPlanta)
                }
            }
        });
    }*/

    /**
     * birtir plöntur notanda í yfirliti
     */
    private void birtaNotendaPlontur() {
        skradurNotandi.get().getMinarPlontur().addListener((ListChangeListener<? super MinPlanta>) change -> {
            change.next();
            if (change.wasAdded()) {
                for (MinPlanta mp : change.getAddedSubList()) {
                    fxMinarPlonturYfirlit.baetaVidYfirlit(mp);
                }
            }
        });
    }


    /**
     * gerir event filter sem bregst við þegar ýtt er á til baka takkann í dagatalinu
     */
    public void dagatalTilBakaRegla() {
        fxDagatal.getFxTilBaka().addEventFilter(ActionEvent.ACTION, (Event event) -> {
            fxDagatal.setSyndurDagur(fxDagatal.getSyndurDagur().minusMonths(1));
            fxDagatal.geraDagatal(fxDagatal.getSyndurDagur());
        });
    }

    /**
     * gerir event filter sem bregst við þegar ýtt er á áfram takkann í dagatalinu
     */
    public void dagatalAframRegla() {
        fxDagatal.getFxAfram().addEventFilter(ActionEvent.ACTION, (Event event) -> {
            fxDagatal.setSyndurDagur(fxDagatal.getSyndurDagur().plusMonths(1));
            fxDagatal.geraDagatal(fxDagatal.getSyndurDagur());
        });
    }

    /**
     * skilar deginum sem ýtt var á, eða deginum sem inniheldur hlut sem ýtt var á
     *
     * @return - Dagur, það sem ýtt var á
     */
    public Dagur getValinnDagur(Node node) {
        Dagur dagur = null;
        while (node != null && !(node instanceof Dagur)) {
            node = node.getParent();
        }
        if (node != null) {
            dagur = (Dagur) node;
        }
        return dagur;
    }

    /**
     * gerir bindingu milli vaktanlegra lista í Dagatal og hér. Setur event filtera fyrir takka í dagatali og
     * þegar ýtt er á dag
     */
    public void dagatalsEventFilterar() {
        Bindings.bindContentBidirectional(fxDagatal.getAllarPlonturOgFyrriVokvanir(), notendaupplysingar.getFyrriVokvanir());
        Bindings.bindContentBidirectional(fxDagatal.getAllarPlonturOgAaetladarVokvanir(), notendaupplysingar.getNaestuVokvanir());

        dagatalTilBakaRegla();
        dagatalAframRegla();

        fxDagatal.getFxGrid().setOnMouseClicked((MouseEvent event) -> {
            Dagur dagur = getValinnDagur(event.getPickResult().getIntersectedNode());

            if (dagur != null && !dagur.getFxManadardagur().getText().equals("")) {
                int manadardagur = Integer.parseInt(dagur.getFxManadardagur().getText());
                LocalDate valinDagsetning = LocalDate.of(fxDagatal.getSyndurDagur().getYear(), fxDagatal.getSyndurDagur().getMonthValue(), manadardagur);

                //TODO: ath kannski hvort naestu vokvanir fari ekki örugglega fram í tímann um 3 mánuði en ekki alltaf lengra og lengra
                System.out.println("fyrri vokvanir: " + notendaupplysingar.getFyrriVokvanir());
                System.out.println("naestu vokvanir: " + notendaupplysingar.getNaestuVokvanir());

                ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsinsLokid = notendaupplysingar.getFyrriVokvanir().filtered(p -> p.getValue().isEqual(valinDagsetning));
                ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsinsOlokid = notendaupplysingar.getNaestuVokvanir().filtered(p -> p.getValue().isEqual(valinDagsetning));

                //TODO: finna leið til að setja dagsetninguna á annað form til að prenta í dialog
                if (valinDagsetning.isBefore(LocalDate.now()) && !plonturDagsinsLokid.isEmpty()) {
                    VokvanirDagsinsDialog vokvanirDagsinsDialog = new VokvanirDagsinsDialog(plonturDagsinsLokid, "Plöntur sem hafa verið vökvaðar " + valinDagsetning);
                    vokvanirDagsinsDialog.showAndWait();
                } else if (valinDagsetning.isAfter(LocalDate.now()) && !plonturDagsinsOlokid.isEmpty()) {
                    VokvanirDagsinsDialog vokvanirDagsinsDialog = new VokvanirDagsinsDialog(plonturDagsinsOlokid, "Plöntur sem ætti að vökva " + valinDagsetning);
                    vokvanirDagsinsDialog.showAndWait();
                } else {
                    //TODO: finna betri lausn á þessu!! í staðin fyrir að sýna annað svo hitt
                    if (!plonturDagsinsLokid.isEmpty()) {
                        VokvanirDagsinsDialog vokvanirDagsinsDialog = new VokvanirDagsinsDialog(plonturDagsinsLokid, "Plöntur sem hafa verið vökvaðar " + valinDagsetning);
                        vokvanirDagsinsDialog.showAndWait();
                    }
                    if (!plonturDagsinsOlokid.isEmpty()) {
                        VokvanirDagsinsDialog vokvanirDagsinsDialog2 = new VokvanirDagsinsDialog(plonturDagsinsOlokid, "Plöntur sem ætti að vökva " + valinDagsetning);
                        vokvanirDagsinsDialog2.showAndWait();
                    }
                }

                //gerir dropann sýnilegan þegar það er ýtt á dag, taka út seinna
                //breyta frekar litnum!! og ef það er ýtt aftur er "afvalið"???
                dagur.getFxDropi().visibleProperty().unbind();
                dagur.getFxDropi().setVisible(true);
            }
        });
    }


    //hvað er þetta?
    @FXML
    /**
     * Nær í Planta hlut sem ýtt var á í yfirlitinu yfir allar plöntur
     * todo: bætir þetta plöntu við yfirlit?
     */
    private void hladaOllumPlontum(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        while (node != null && !(node instanceof PlantaSpjald)) {
            node = node.getParent();
        }
        if (node != null) {
            Planta p = ((PlantaSpjald) node).getPlanta();
            //skradurNotandi.get().getNotendaupplysingar().baetaVidPlontu(p);
            skradurNotandi.get().addaPlanta(p);
        }
    }

    /**
     * Atburðahandler. kallar á aðferð til að vista upplýsingar, setur skráðan notanda sem null og fer á upphafssíðu
     *
     * @param actionEvent - atburður
     */
    public void skraUt(ActionEvent actionEvent) {
        vistaNotendaupplysingar();
        skradurNotandi = null;
        System.out.println("skra ut");
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }

    /**
     * sækir alla notendur sem eru í skránni, finnur þann sem er skráður inn og uppfærir upplýsingar um hann með
     * því að skrifa í skrána. ATH að eins og allt annað tengt json virkar þetta ekki
     */
    private void vistaNotendaupplysingar() {
        ObjectMapper objectMapper = new ObjectMapper();
        //SimpleModule module = new SimpleModule();
        //module.addDeserializer(ObservableList.class, new ObservableListDeserializer());
        //objectMapper.registerModule(module);
        objectMapper.findAndRegisterModules();

        try {
            List<Notandi> notendur = objectMapper.readValue(new File("target/classes/vidmot/plantmania/notendur.json"), new TypeReference<>() {
            });
            for (Notandi n : notendur) {
                if (n.notendanafnProperty().get().equals(skradurNotandi.get().getNotendanafn())) {
                    n.setMinarPlontur(skradurNotandi.get().getMinarPlontur());
                    objectMapper.writeValue(new File("target/classes/vidmot/plantmania/notendur.json"), notendur);//bætti við
                }
            }
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }


    public void setSkradurNotandi(Notandi n) {
        skradurNotandi = (new SimpleObjectProperty<Notandi>(n));
        // skradurNotandi=n;
    }

    public void publicVistaUpplysingar() {
        vistaNotendaupplysingar();
    }

    public Notendaupplysingar getNotendaupplysingar() {
        return notendaupplysingar;
    }

    public void setNotendaupplysingar(Notendaupplysingar notendaupplysingar) {
        this.notendaupplysingar = notendaupplysingar;
    }
}
