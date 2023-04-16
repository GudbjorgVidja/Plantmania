package vidmot.plantmania;

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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import vinnsla.plantmania.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Höfundar: Guðbjörg Viðja og Sigurbjörg Erla
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
    @FXML
    private VBox titledPaneBoxid;

    @FXML
    private VBox vokvaBox, vandamalBox, almenntBox;

    private UpphafController upphafController;
    private ObjectProperty<Notandi> skradurNotandi = new SimpleObjectProperty<>();

    private Notendaupplysingar notendaupplysingar;

    public void initialize() {
        upphafController = (UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA);
        skradurNotandi.setValue(upphafController.getSkradurNotandi());

        notendaupplysingar = new Notendaupplysingar(skradurNotandi.get().getMinarPlontur());
        notendaupplysingar.finnaFyrriOgSidariVokvanirListener(skradurNotandi.get().getMinarPlontur());
        System.out.println(skradurNotandi.get());
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());//af hverju ekki bara upphafsstilling?

        lesaInnAllarPlontur();
        dagatalsEventFilterar();

        fxMinarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));
        fxAllarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));

        birtaNotendaPlontur();
        hladaUpplysingum();

        //todo sigurbjörg, ertu að nota þessa prentsetningu?
        System.out.println(skradurNotandi.get());
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());

        bindaMaxSizeTitledPane();
        setjaFraedsla();
    }

    private void bindaMaxSizeTitledPane() {
        for (Node node : titledPaneBoxid.getChildren()) {
            if (node instanceof TitledPane) {
                ((TitledPane) node).expandedProperty().addListener((obs, o, n) -> {
                    System.out.println(n);
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
        for (MinPlanta m : skradurNotandi.get().getMinarPlontur()) {
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
                ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsinsLokid = notendaupplysingar.getFyrriVokvanir().filtered(p -> p.getValue().isEqual(valinDagsetning));
                ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsinsOlokid = notendaupplysingar.getNaestuVokvanir().filtered(p -> p.getValue().isEqual(valinDagsetning));

                synaVokvanirDagsins(valinDagsetning, plonturDagsinsLokid, plonturDagsinsOlokid);
                //breyta litnum á reit þegar hann er valinn!! og ef það er ýtt aftur er "afvalið"??? hafa selection dæmi með style?
                //dagur.getFxDropi().visibleProperty().unbind();
                //dagur.getFxDropi().setVisible(true);
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
        String loknarVokvanir = "Plöntur sem voru vökvaðar " + valinDagsetning;
        String seinarVokvanir = "Plöntur sem hefði átt að vökva " + valinDagsetning;
        String oloknarVokvanir = "Plöntur sem ætti að vökva " + valinDagsetning;
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


    @FXML
    /**
     * Nær í Planta hlut sem ýtt var á í yfirlitinu yfir allar plöntur
     */
    private void hladaOllumPlontum(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        while (node != null && !(node instanceof PlantaSpjald)) {
            node = node.getParent();
        }
        if (node != null) {
            Planta p = ((PlantaSpjald) node).getPlanta();
            skradurNotandi.get().baetaVidPlontu(p);
            Alert a = new Alert(Alert.AlertType.NONE, "Nýrri plöntu bætt við þínar plöntur", ButtonType.OK);
            a.showAndWait();
        }
    }

    public Notandi getSkradurNotandi() {
        return skradurNotandi.get();
    }

    public Notendaupplysingar getNotendaupplysingar() {
        return notendaupplysingar;
    }

    public void setNotendaupplysingar(Notendaupplysingar notendaupplysingar) {
        this.notendaupplysingar = notendaupplysingar;
    }


    private void setjaFraedsla() {
        Fraedsla fraedsluklasi = new Fraedsla();

        List<String> titlar = new ArrayList<>(List.of("Misting", "Garðkanna", "Bottom watering", "Gradual"));
        List<String> efni = new ArrayList<>(List.of(fraedsluklasi.getMisting(), fraedsluklasi.getGardkanna(), fraedsluklasi.getBottom(), fraedsluklasi.getGradual()));
        for (int i = 0; i < vokvaBox.getChildren().size(); i += 2) {
            if (vokvaBox.getChildren().get(i) instanceof Text)
                ((Text) vokvaBox.getChildren().get(i)).setText(titlar.get(i / 2));
            if (vokvaBox.getChildren().get(i + 1) instanceof Text)
                ((Text) vokvaBox.getChildren().get(i + 1)).setText(efni.get(i / 2));
        }


        List<String> vandamal = new ArrayList<>(fraedsluklasi.getVandamalListi());
        if (vandamalBox.getChildren().get(0) instanceof Text)
            ((Text) vandamalBox.getChildren().get(0)).setText(vandamal.remove(0));

        for (int i = 0; i < vandamal.size(); i++) {
            Text texti = new Text(vandamal.get(i));
            texti.setWrappingWidth(512);
            vandamalBox.getChildren().add(texti);
        }

        List<String> almennt = fraedsluklasi.getAlmenntListi();
        for (int i = 0; i < almennt.size(); i++) {
            Text texti = new Text(almennt.get(i));
            texti.setWrappingWidth(512);
            almenntBox.getChildren().add(texti);
        }
    }
}
