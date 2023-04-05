/**
 * hafa einhvers staðar lista af öllum plöntum, til að hafa auðveldari (og kannski hagkvæmari) aðgang að þeim á keyrslutíma
 */
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
        geraBindings();

        lesaInnAllarPlontur();
        //System.out.println("Buid ad lesa inn allar plontur. Staerd lista: " + allarPlontur.size()); //virkar rétt

        dagatalsEventFilterar();

        //binda nafn notanda við label i báðum yfirlitum.
        // Væri gott að hafa í Plontuyfirlit klasanum, en viewSwitcher er leiðinlegur við mig rn
        fxMinarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));
        fxAllarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));

        birtaNotendaPlontur();
    }

    private void lesaInnAllarPlontur() {
        //lesa inn af skrá, og setja í yfirlitið  fxAllarPlonturYfirlit
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


    private void geraBindings() {
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());
    }

    public void dagatalsEventFilterar() {
        //senda inn lista af dagsetningum pg plöntum og tengja saman
        //gæti líka haft aðferð í Dagatal bindaLista(ObservableList<Pair<MinPlanta,LocalDate>> listi) sem er svo bundið við breytuna í Dagatal
        Bindings.bindContentBidirectional(skradurNotandi.get().getNotendaupplysingar().getFyrriVokvanir(), fxDagatal.getPlonturOgVokvanir());

        //fyrir áfram og til baka takka, og svo þegar það er smellt á dag. var með þetta í Dagatal, hægt að færa til baka?
        fxDagatal.getFxTilBaka().addEventFilter(ActionEvent.ACTION, (Event event) -> {
            fxDagatal.setSyndurDagur(fxDagatal.getSyndurDagur().minusMonths(1));
            fxDagatal.geraDagatal(fxDagatal.getSyndurDagur());
        });
        fxDagatal.getFxAfram().addEventFilter(ActionEvent.ACTION, (Event event) -> {
            fxDagatal.setSyndurDagur(fxDagatal.getSyndurDagur().plusMonths(1));
            fxDagatal.geraDagatal(fxDagatal.getSyndurDagur());
        });

        fxDagatal.getFxGrid().setOnMouseClicked((MouseEvent event) -> {
            Node node = event.getPickResult().getIntersectedNode();
            Dagur dagur = null;

            while (node != null && !(node instanceof Dagur)) {
                node = node.getParent();
            }
            if (node != null) {
                dagur = (Dagur) node;
            }

            if (dagur != null && !dagur.getFxManadardagur().getText().equals("")) {
                int manadardagur = Integer.parseInt(dagur.getFxManadardagur().getText());
                fxDagatal.setValinnDagur(LocalDate.of(fxDagatal.getSyndurDagur().getYear(), fxDagatal.getSyndurDagur().getMonthValue(), manadardagur));
                if (fxDagatal.getValinnDagur().isBefore(LocalDate.now())) {
                    System.out.println("<" + fxDagatal.getValinnDagur());
                } else if (fxDagatal.getValinnDagur().isAfter(LocalDate.now())) {
                    System.out.println(">" + fxDagatal.getValinnDagur());
                } else System.out.println("=" + fxDagatal.getValinnDagur());

                dagur.getFxDropi().visibleProperty().unbind();
                dagur.getFxDropi().setVisible(true);

                ObservableList<Pair<MinPlanta, LocalDate>> plonturDagsins = skradurNotandi.get().getNotendaupplysingar().getFyrriVokvanir().filtered(p -> p.getValue().getMonth() == fxDagatal.getValinnDagur().getMonth());
                //opna glugga með plontum dagsins
            }
        });
    }


    /**
     * þegar smellt er, plönturnar úr plontur.txt sem MinPlantaSpjald hlutir
     */
    @FXML
    protected void fxBaetaVidHandler(MouseEvent event) {

        //bætir við plöntum þegar ýtt er á yfirlitið
        for (int i = 0; i < allarPlontur.size(); i++) {
            MinPlanta mp = new MinPlanta(allarPlontur.get(i));
            //MinPlantaSpjald mps = new MinPlantaSpjald(mp);
            fxMinarPlonturYfirlit.baetaVidYfirlit(mp);

            //fxMinarPlonturYfirlit.getFxFlowPane().getChildren().add(mps);
        }



        /*
        LesaPlontur l = new LesaPlontur();
        List<Planta> plontur = l.getPlontur();

        PlantaSpjald spj = new PlantaSpjald(plontur.get(0));
        fxMinarPlonturYfirlit.getFxFlowPane().getChildren().add(spj);

        spj = new PlantaSpjald(plontur.get(1));
        fxMinarPlonturYfirlit.getFxFlowPane().getChildren().add(spj);*/


        //System.out.println(event.getTarget().getClass());
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

    @FXML
    private void hladaOllumPlontum(MouseEvent event) {//eina notkunin er í fxml skránni, handlerinn settur á hlut


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
        skradurNotandi = null;
        System.out.println("skra ut");
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }

    public StringProperty getNotendanafn() {
        return new SimpleStringProperty(skradurNotandi.get().getNotendanafn());
    }
}
