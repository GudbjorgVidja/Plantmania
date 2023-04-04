package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import vinnsla.plantmania.LesaPlontur;
import vinnsla.plantmania.Notandi;
import vinnsla.plantmania.Planta;

import java.util.List;

public class PlantController {
    @FXML
    private Plontuyfirlit fxPlonturYfirlit; //mínar plöntur yfirlitið. Er eiginlega meira eins og allar plöntur yfirlit
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
    protected void fxBaetaVidHandler() {
        Spjald spjald = new Spjald();
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spjald);


        LesaPlontur l = new LesaPlontur();
        List<Planta> plontur = l.getPlontur();

        PlantaSpjald spj = new PlantaSpjald(plontur.get(0));
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spj);

        spj = new PlantaSpjald(plontur.get(1));
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spj);
    }

    public void skraUt(ActionEvent actionEvent) {
        skradurNotandi = null;
        System.out.println("skra ut");
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }
}
