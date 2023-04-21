package vidmot.plantmania;

import edu.princeton.cs.algs4.In;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;
import vinnsla.plantmania.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * Höfundar: Guðbjörg Viðja og Sigurbjörg Erla
 * Controller fyrir aðalsenuna. Þar sem við notum TabPane er það sem væri annars í 5 senum eða svo í einni
 * senu, svo það er meira hér en væri annars
 */
public class PlantController {
    @FXML
    private Yfirlit fxMinarPlonturYfirlit; //mínar plöntur yfirlitið.
    @FXML
    private Yfirlit fxAllarPlonturYfirlit; //yfirlit yfir allar plöntur
    @FXML
    private Dagatal fxDagatal;//tilvik af Dagatalinu
    @FXML
    private VBox titledPaneBoxid;//VBox sem inniheldur titledPane fyrir fræðsluefni

    private Notandi skradurNotandi = new Notandi();//Sá notandi sem er skráður inn

    private Notendaupplysingar notendaupplysingar;//Vövkunarupplýsingar fyrir þann notanda sem er skráður inn

    /**
     * Initialize, keyrir sjálfkrafa. Gerir tilvik af UpphafController og stillir skráðan notanda og upplýsingar um hann.
     * Les inn plöntur úr skrá og setur event filtera á Dagatalið. Birtir plöntur notanda o.fl.
     */
    public void initialize() {
        UpphafController upphafController = (UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA);
        skradurNotandi = upphafController.getSkradurNotandi();
        notendaupplysingar = new Notendaupplysingar(skradurNotandi.getMinarPlontur());
        notendaupplysingar.finnaFyrriOgSidariVokvanirListener(skradurNotandi.getMinarPlontur());

        lesaInnAllarPlontur();
        dagatalsEventFilterar();
        fxMinarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.getNotendanafn()));
        fxAllarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.getNotendanafn()));

        birtaNotendaPlontur();
        hladaUpplysingum();
        bindaMaxSizeTitledPane();
        geraTitledPanes();
    }

    /**
     * kallað á úr application þegar reynt er að loka glugganum
     */
    protected void lokaGluggaAdferd() {
        fxAllarPlonturYfirlit.vistaNotendaupplysingar(skradurNotandi);
    }
    
    /**
     * Setur listener á titledpane hlutina með fræðsluefninu til að breyta stærðinni þegar þeir eru sýndir/faldir
     */
    private void bindaMaxSizeTitledPane() {
        for (Node node : titledPaneBoxid.getChildren()) {
            if (node instanceof TitledPane) {
                ((TitledPane) node).expandedProperty().addListener((obs, o, n) -> {
                    if (!n) ((TitledPane) node).maxHeightProperty().set(0);
                    else ((TitledPane) node).maxHeightProperty().set(((TitledPane) node).getPrefHeight());
                });
            }
        }
    }

    /**
     * birtir plöntur notandans sem voru í skránni og setur listenera á MinPlanta hlutina
     */
    private void hladaUpplysingum() {
        for (MinPlanta m : skradurNotandi.getMinarPlontur()) {
            fxMinarPlonturYfirlit.baetaVidYfirlit(m);
            m.sidastaVokvunListener();
            m.naestaVokvunRegla();
            m.medaltimiMilliVokvanaListener();
            m.reiknaPlanadarVokvanir();
            m.breytingAThinnTimiMilliVokvanaRegla();
            m.setjaPlanadarVokvanirListenera();
        }
    }

    /**
     * lesa inn af skrá, og setja í yfirlitið  fxAllarPlonturYfirlit
     */
    private void lesaInnAllarPlontur() {
        List<Planta> lesnarPlontur = (new LesaPlontur()).getPlontur();
        for (Planta p : lesnarPlontur) {
            fxAllarPlonturYfirlit.baetaVidYfirlit(p);
        }
    }

    /**
     * birtir plöntur notanda í yfirliti
     */
    private void birtaNotendaPlontur() {
        skradurNotandi.getMinarPlontur().addListener((ListChangeListener<? super MinPlanta>) change -> {
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
            fxDagatal.setSyndurManudur(fxDagatal.getSyndurManudur().minusMonths(1));
            fxDagatal.geraDagatal();
        });
    }

    /**
     * gerir event filter sem bregst við þegar ýtt er á áfram takkann í dagatalinu
     */
    public void dagatalAframRegla() {
        fxDagatal.getFxAfram().addEventFilter(ActionEvent.ACTION, (Event event) -> {
            fxDagatal.setSyndurManudur(fxDagatal.getSyndurManudur().plusMonths(1));
            fxDagatal.geraDagatal();
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
        Bindings.bindContentBidirectional(fxDagatal.getAllarLoknarVokvanir(), notendaupplysingar.getFyrriVokvanir());
        Bindings.bindContentBidirectional(fxDagatal.getAllarAaetladarVokvanir(), notendaupplysingar.getNaestuVokvanir());
        dagatalTilBakaRegla();
        dagatalAframRegla();

        fxDagatal.getFxGrid().setOnMouseClicked((MouseEvent event) -> {
            Dagur dagur = getValinnDagur(event.getPickResult().getIntersectedNode());
            if (dagur != null && !dagur.getFxManadardagur().getText().equals("")) {
                int manadardagur = Integer.parseInt(dagur.getFxManadardagur().getText());
                LocalDate valinDagsetning = LocalDate.of(fxDagatal.getSyndurManudur().getYear(), fxDagatal.getSyndurManudur().getMonthValue(), manadardagur);
                ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsinsLokid = notendaupplysingar.getFyrriVokvanir().filtered(p -> p.getValue().isEqual(valinDagsetning));
                ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsinsOlokid = notendaupplysingar.getNaestuVokvanir().filtered(p -> p.getValue().isEqual(valinDagsetning));

                dagur.getStyleClass().add("valinnDagur");
                synaVokvanirDagsins(valinDagsetning, plonturDagsinsLokid, plonturDagsinsOlokid);
                dagur.getStyleClass().remove("valinnDagur");
            }
        });
    }


    /**
     * gerir vokvanirDagsins dialog og sýnir hann ef eitthvað var vökvað á þessum degi, eða er/var planað.
     * Setur viðfang mismunandi eftir því hvað er á deginum
     *
     * @param valinDagsetning      - LocalDate, dagsetning sem er skoðuð
     * @param plonturDagsinsLokid  - ObservableList<Pair<MinPlanta, LocalDate>>, pör af MinPlanta hlutum sem voru vökvaðar og valinni dagsetningu
     * @param plonturDagsinsOlokid -ObservableList<Pair<MinPlanta, LocalDate>>, pör af MinPlanta hlutum sem ætti að vökva og valinni dagsetningu
     */
    private void synaVokvanirDagsins(LocalDate valinDagsetning, ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsinsLokid, ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsinsOlokid) {
        String loknarVokvanir = "Plöntur sem voru vökvaðar " + valinDagsetning.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String seinarVokvanir = "Plöntur sem hefði átt að vökva " + valinDagsetning.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String oloknarVokvanir = "Plöntur sem ætti að vökva " + valinDagsetning.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        VokvanirDagsinsDialog vokvanirDagsinsDialog = null;
        if (!plonturDagsinsLokid.isEmpty() && !plonturDagsinsOlokid.isEmpty()) {
            if (valinDagsetning.isBefore(LocalDate.now())) {
                vokvanirDagsinsDialog = new VokvanirDagsinsDialog(plonturDagsinsLokid, loknarVokvanir, plonturDagsinsOlokid, seinarVokvanir);
            } else {
                vokvanirDagsinsDialog = new VokvanirDagsinsDialog(plonturDagsinsLokid, loknarVokvanir, plonturDagsinsOlokid, oloknarVokvanir);
            }
        } else if (!plonturDagsinsLokid.isEmpty()) {
            vokvanirDagsinsDialog = new VokvanirDagsinsDialog(plonturDagsinsLokid, loknarVokvanir);
        } else if (!plonturDagsinsOlokid.isEmpty()) {
            if (valinDagsetning.isBefore(LocalDate.now())) {
                vokvanirDagsinsDialog = new VokvanirDagsinsDialog(plonturDagsinsOlokid, seinarVokvanir);
            } else {
                vokvanirDagsinsDialog = new VokvanirDagsinsDialog(plonturDagsinsOlokid, oloknarVokvanir);
            }
        }
        if (vokvanirDagsinsDialog != null) {
            vokvanirDagsinsDialog.showAndWait();
        }
    }

    /**
     * Nær í Planta hlut sem ýtt var á í yfirlitinu yfir allar plöntur
     */
    public void saekjaPlontu(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        while (node != null && !(node instanceof PlantaSpjald)) {
            node = node.getParent();
        }
        if (node != null) {
            Planta p = ((PlantaSpjald) node).getPlanta();
            skradurNotandi.baetaVidPlontu(p);
            birtaBanner(fxAllarPlonturYfirlit.getFxBanner());
        }
    }

    /**
     * Birtir banner með upplýsingum í smá tíma.
     *
     * @param banner label með styleclass banner
     */
    private void birtaBanner(Label banner) {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> banner.setVisible(false));
        banner.setVisible(true);
        delay.play();
    }

    public Notandi getSkradurNotandi() {
        return skradurNotandi;
    }


    /**
     * gerir titledPanes í fræðsluflipanum. Les efnið inn úr skrá
     */
    private void geraTitledPanes() {
        In inn = new In("src/main/java/vinnsla/plantmania/fraedsla.txt");
        String alltSkjalid = inn.readAll();
        String[] paneskipting = splittaIFylki(alltSkjalid, "TITLEDPANE ");

        for (String s : paneskipting) {
            TitledPane tp = new TitledPane();

            String[] malsgreinar = splittaIFylki(s, "GREIN ");
            tp.setText(malsgreinar[0].trim());

            VBox vbox = new VBox();

            Text[] textar = geraTexta(malsgreinar);
            for (Text t : textar) {
                if (t != null) vbox.getChildren().add(t);
            }

            tp.setContent(vbox);

            vbox.getStyleClass().add("titledpanebox");
            vbox.getStylesheets().add(getClass().getResource("styling/derived-style.css").toExternalForm());

            titledPaneBoxid.getChildren().add(tp);
            bindaMaxSizeTitledPane();
        }
    }

    /**
     * Aðferðin tekur inn fylki af strengjum og gerir úr hverjum streng texta fyrir efnisgrein. Ef strengurinn byrjar á
     * TITILL þá er styleclass sett á textann.
     *
     * @param malsgreinar strengjafylki
     * @return malsgreinar nema umbreytt yfir í texta
     */
    private Text[] geraTexta(String[] malsgreinar) {
        Text[] textar = new Text[malsgreinar.length];
        for (int i = 1; i < malsgreinar.length; i++) {
            Text texti;
            if (malsgreinar[i].startsWith("TITILL")) {
                malsgreinar[i] = malsgreinar[i].replaceFirst("TITILL ", "");
                texti = new Text(malsgreinar[i].strip());
                texti.getStyleClass().add("titill");
            } else texti = new Text(malsgreinar[i].strip());
            texti.setWrappingWidth(512);
            textar[naestaLausaSaeti(textar)] = texti;
        }
        return textar;
    }

    /**
     * Finnur fyrsta lausa sætið í fylkinu, ef fylki er fullt þá skilast -1
     *
     * @param fylki Text fylki
     * @return index fyrsta null sætis
     */
    private int naestaLausaSaeti(Text[] fylki) {
        for (int i = 0; i < fylki.length; i++) {
            if (fylki[i] == null) return i;
        }
        return -1;
    }


    /**
     * Tekur inn streng og splittar í fylki hvar sem splitter kemur fyrir
     *
     * @param runa     Strengur, hlutar aðskildir með splitter
     * @param splitter Strengur, skiptir runa niður
     * @return runan skipt upp í fylki eftir splitter
     */
    private String[] splittaIFylki(String runa, String splitter) {
        if (runa.startsWith(splitter)) {
            runa = runa.replaceFirst(splitter, "");
        }
        return runa.split(splitter);
    }
}
