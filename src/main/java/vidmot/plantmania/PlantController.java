package vidmot.plantmania;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import vinnsla.plantmania.Notandi;
import vinnsla.plantmania.Planta;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlantController {
    @FXML
    private Plontuyfirlit fxPlonturYfirlit; //mínar plöntur yfirlitið
    private UpphafController upphafController;
    private ObjectProperty<Notandi> skradurNotandi = new SimpleObjectProperty<>();

    public void initialize() {
        upphafController = (UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA);
        skradurNotandi.setValue(upphafController.getSkradurNotandi());

        System.out.println(skradurNotandi.get());
        geraBindings();
    }


    private void geraBindings() {
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());
    }


    /**
     * þegar smellt er, þá bætast við eitt spjald og plönturnar úr plontur.txt
     */
    @FXML
    protected void fxBaetaVidHandler(MouseEvent event) {
        /*Spjald spjald = new Spjald();
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spjald);


        LesaPlontur l = new LesaPlontur();
        List<Planta> plontur = l.getPlontur();

        PlantaSpjald spj = new PlantaSpjald(plontur.get(0));
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spj);

        spj = new PlantaSpjald(plontur.get(1));
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spj);*/

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

    public void skraUt(ActionEvent actionEvent) {
        skradurNotandi = null;
        System.out.println("skra ut");
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }
}
