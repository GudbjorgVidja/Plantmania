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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import vinnsla.plantmania.*;

import java.io.File;
import java.io.IOException;
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

        bindaMaxSizeTitledPane();

        setjaFraedsla();
    }

    private void bindaMaxSizeTitledPane() {
        for (Node node : titledPaneBoxid.getChildren()) {
            if (node instanceof TitledPane) {
                ((TitledPane) node).expandedProperty().addListener((obs, o, n) -> {
                    System.out.println(n);
                    if (!n) ((TitledPane) node).maxHeightProperty().set(0);
                    else if (n) ((TitledPane) node).maxHeightProperty().set(((TitledPane) node).getPrefHeight());
                    //todo: eða nota Double.MAX_VALUE?
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

                //TODO: eyða þessu fljótlega
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
                        if (valinDagsetning.isBefore(LocalDate.now())) {
                            VokvanirDagsinsDialog vokvanirDagsinsDialog2 = new VokvanirDagsinsDialog(plonturDagsinsOlokid, "Plöntur sem hefði átt að vökva " + valinDagsetning);
                            vokvanirDagsinsDialog2.showAndWait();
                        } else {
                            VokvanirDagsinsDialog vokvanirDagsinsDialog2 = new VokvanirDagsinsDialog(plonturDagsinsOlokid, "Plöntur sem ætti að vökva " + valinDagsetning);
                            vokvanirDagsinsDialog2.showAndWait();
                        }
                    }
                }

                //gerir dropann sýnilegan þegar það er ýtt á dag, taka út seinna
                //breyta frekar litnum!! og ef það er ýtt aftur er "afvalið"??? hafa selection dæmi með style?
                //dagur.getFxDropi().visibleProperty().unbind();
                //dagur.getFxDropi().setVisible(true);
            }
        });
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
        objectMapper.findAndRegisterModules();

        try {
            List<Notandi> notendur = objectMapper.readValue(new File("target/classes/vidmot/plantmania/notendur.json"), new TypeReference<>() {
            });
            for (Notandi n : notendur) {
                if (n.notendanafnProperty().get().equals(skradurNotandi.get().getNotendanafn())) {
                    n.setMinarPlontur(skradurNotandi.get().getMinarPlontur());
                    objectMapper.writeValue(new File("target/classes/vidmot/plantmania/notendur.json"), notendur);
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
