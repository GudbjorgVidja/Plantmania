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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import vinnsla.plantmania.LesaPlontur;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Notandi;
import vinnsla.plantmania.Planta;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlantController {
    @FXML
    private Plontuyfirlit fxMinarPlonturYfirlit; //mínar plöntur yfirlitið. Er eiginlega meira eins og allar plöntur yfirlit
    @FXML
    private Plontuyfirlit fxAllarPlonturYfirlit; //yfirlit yfir allar plöntur
    private UpphafController upphafController;
    private ObjectProperty<Notandi> skradurNotandi = new SimpleObjectProperty<>();

    private ObservableList<Planta> allarPlontur = FXCollections.observableArrayList();//er í vesi, geymi hér
    //ætti frekar kannski að geyma í öðrum klasa, t.d. vinnsluklasa fyrir allarPlonturYfirlit

    public void initialize() {
        upphafController = (UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA);
        skradurNotandi.setValue(upphafController.getSkradurNotandi());

        System.out.println(skradurNotandi.get());
        geraBindings();

        allarPlontur.addAll((new LesaPlontur()).getPlontur());
        //System.out.println("Buid ad lesa inn allar plontur. Staerd lista: " + allarPlontur.size()); //virkar rétt


        //binda nafn notanda við label i báðum yfirlitum.
        // Væri gott að hafa í Plontuyfirlit klasanum, en viewSwitcher er leiðinlegur við mig rn
        fxMinarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));
        fxAllarPlonturYfirlit.getNafnAfLabel().bind(new SimpleStringProperty(skradurNotandi.get().getNotendanafn()));
    }


    private void geraBindings() {
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());
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
    private void hladaOllumPlontum() {//eina notkunin er í fxml skránni, handlerinn settur á hlut

        for (Planta p : allarPlontur) {
            fxAllarPlonturYfirlit.baetaVidYfirlit(p);
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
