package vidmot.plantmania;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import vinnsla.plantmania.LesaPlontur;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Notandi;
import vinnsla.plantmania.Planta;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


//TODO: hafa einhvers staðar lista af öllum plöntum, til að hafa auðveldari (og kannski hagkvæmari) aðgang að þeim á keyrslutíma

/**
 * Controller fyrir aðalsenuna. Þar sem við notum TabPane er það sem væri annars í 5 senum eða svo í einni
 * senu, svo það er meira hér en væri annars
 */
public class PlantController {
    @FXML
    private Plontuyfirlit fxMinarPlonturYfirlit; //mínar plöntur yfirlitið. Er eiginlega meira eins og allar plöntur yfirlit
    @FXML
    private Plontuyfirlit fxAllarPlonturYfirlit; //yfirlit yfir allar plöntur
    @FXML
    private Dagatal fxDagatal;
    private UpphafController upphafController;
    private ObjectProperty<Notandi> skradurNotandi = new SimpleObjectProperty<>();

    private ObservableList<Planta> allarPlontur = FXCollections.observableArrayList();//er í vesi, geymi hér
    //ætti frekar kannski að geyma í öðrum klasa, t.d. vinnsluklasa fyrir allarPlonturYfirlit

    public void initialize() {
        upphafController = (UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA);
        skradurNotandi.setValue(upphafController.getSkradurNotandi());

        System.out.println(skradurNotandi.get());
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());

        lesaInnAllarPlontur();
        //System.out.println("Buid ad lesa inn allar plontur. Staerd lista: " + allarPlontur.size()); //virkar rétt

        dagatalsEventFilterar();

        //binda nafn notanda við label i báðum yfirlitum.
        // Væri gott að hafa í Plontuyfirlit klasanum, en viewSwitcher er leiðinlegur við mig rn
        fxMinarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));
        fxAllarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));

        birtaNotendaPlontur();
        skradurNotandi.get().getNotendaupplysingar().finnaFyrriVokvanir();

        skradurNotandi.get().getNotendaupplysingar().finnaNaestuVokvanir();
        /*
        skradurNotandi.get().getNotendaupplysingar().getMinarPlontur().addListener((ListChangeListener<? super MinPlanta>) change -> {
            change.next();
            if (change.wasAdded())
                System.out.println("\n" + change.getAddedSubList() + " baett vid notendaupplysingar");
        });

         */

        //fxMinarPlonturYfirlit.getMinarPlontur.addListener() og bæta allaf sömu við
        //fxMinarPlonturYfirlit.getSyndSpjold().addListener((ListChangeListener<? super Node>) change ->{
        //});
    }

    /**
     * lesa inn af skrá, og setja í yfirlitið  fxAllarPlonturYfirlit
     */
    private void lesaInnAllarPlontur() {
        allarPlontur.addAll((new LesaPlontur()).getPlontur());
        for (Planta planta : allarPlontur) {
            fxAllarPlonturYfirlit.baetaVidYfirlit(planta);
        }
    }

    /*
    private void bindaNotendaPlontur(){
        fxMinarPlonturYfirlit.getMinarPlonturYfirlit().addListener((ListChangeListener<? super Node>) change -> {
            change.next();
            if(change.wasAdded()){

                List<Node> listi = (List<Node>) change.getAddedSubList();
                for(Node node: listi){
                    if(node instanceof MinPlanta)
                }
            }
        });

    }

     */

    /**
     * birtir plöntur notanda í yfirliti
     */
    private void birtaNotendaPlontur() {
        skradurNotandi.get().getNotendaupplysingar().getMinarPlontur().addListener((ListChangeListener<? super MinPlanta>) change -> {
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
        Bindings.bindContentBidirectional(skradurNotandi.get().getNotendaupplysingar().getFyrriVokvanir(), fxDagatal.getAllarPlonturOgFyrriVokvanir());
        Bindings.bindContentBidirectional(skradurNotandi.get().getNotendaupplysingar().getNaestuVokvanir(), fxDagatal.getAllarPlonturOgAaetladarVokvanir());
        dagatalTilBakaRegla();
        dagatalAframRegla();

        fxDagatal.getFxGrid().setOnMouseClicked((MouseEvent event) -> {
            Dagur dagur = getValinnDagur(event.getPickResult().getIntersectedNode());

            if (dagur != null && !dagur.getFxManadardagur().getText().equals("")) {
                int manadardagur = Integer.parseInt(dagur.getFxManadardagur().getText());
                LocalDate valinDagsetning = LocalDate.of(fxDagatal.getSyndurDagur().getYear(), fxDagatal.getSyndurDagur().getMonthValue(), manadardagur);

                System.out.println("fyrri vokvanir: " + skradurNotandi.get().getNotendaupplysingar().getFyrriVokvanir());
                System.out.println("naestu vokvanir: " + skradurNotandi.get().getNotendaupplysingar().getNaestuVokvanir());


                if (valinDagsetning.isBefore(LocalDate.now())) {
                    //TODO: Hér á að opnast listi yfir plöntur sem voru vökvaðar þennan dag
                } else if (valinDagsetning.isAfter(LocalDate.now())) {
                    //TODO: Hér á að opnast listi yfir plöntur sem ætti að vökva þennan dag
                } else {
                    //TODO: skoða bæði það sem er búið að vökva og á eftir að vökva
                }
                System.out.println(valinDagsetning);

                //gerir dropann sýnilegan þegar það er ýtt á dag, taka út seinna
                dagur.getFxDropi().visibleProperty().unbind();
                dagur.getFxDropi().setVisible(true);

                ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsins = skradurNotandi.get().getNotendaupplysingar().getFyrriVokvanir().filtered(p -> p.getValue().getMonth() == valinDagsetning.getMonth());
                //opna glugga með plontum dagsins
            }
        });
    }


    /**
     * þegar smellt er, plönturnar úr plontur.txt sem MinPlantaSpjald hlutir
     */
    @FXML
    protected void fxBaetaVidHandler(MouseEvent event) {
        //eintaki af öllum plöntum (gerðum) bætt við plöntur notanda
        /*
        for (int i = 0; i < allarPlontur.size(); i++) {
            MinPlanta mp = new MinPlanta(allarPlontur.get(i));
            fxMinarPlonturYfirlit.baetaVidYfirlit(mp);
        }
         */

        Plontugluggi gluggi = new Plontugluggi();
        gluggi.showAndWait();
    }

    @FXML
    private void hladaOllumPlontum(MouseEvent event) {

        Node node = event.getPickResult().getIntersectedNode();
        while (node != null && !(node instanceof PlantaSpjald)) {
            node = node.getParent();
        }
        if (node != null) {
            Planta p = ((PlantaSpjald) node).getPlanta();
            skradurNotandi.get().getNotendaupplysingar().baetaVidPlontu(p);

            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(ObservableList.class, new ObservableListDeserializer());
            objectMapper.registerModule(module);
            try {
                List<Notandi> notendur = objectMapper.readValue(new File("target/classes/vidmot/plantmania/notendur.json"), new TypeReference<>() {
                });

                for (Notandi n : notendur) {
                    if (n.getNotendanafn().equals(skradurNotandi.get().getNotendanafn())) {
                        n = skradurNotandi.get();
                        System.out.println(n);
                    }
                }
                File file = new File("target/classes/vidmot/plantmania/notendur.json");
                if (file.createNewFile()) {
                    System.out.println("Ný skrá búin til");
                    objectMapper.writeValue(file, notendur);
                } else {
                    System.out.println("skráin er til og er núna uppfærð");
                    objectMapper.writeValue(file, notendur);//bætti við
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void skraUt(ActionEvent actionEvent) {
        vistaNotendaupplysingar();
        skradurNotandi = null;
        System.out.println("skra ut");
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }

    private void vistaNotendaupplysingar() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ObservableList.class, new ObservableListDeserializer());
        objectMapper.registerModule(module);
        try {
            List<Notandi> notendur = objectMapper.readValue(new File("target/classes/vidmot/plantmania/notendur.json"), new TypeReference<>() {
            });
            for (Notandi n : notendur) {
                if (n.notendanafnProperty().get().equals(skradurNotandi.get().getNotendanafn())) {
                    n.setNotendaupplysingar(skradurNotandi.get().getNotendaupplysingar());
                    objectMapper.writeValue(new File("target/classes/vidmot/plantmania/notendur.json"), notendur);//bætti við
                }
            }
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }

    public StringProperty getNotendanafn() {
        return new SimpleStringProperty(skradurNotandi.get().getNotendanafn());
    }
}
