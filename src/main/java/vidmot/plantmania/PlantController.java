package vidmot.plantmania;

import javafx.fxml.FXML;
import vinnsla.plantmania.LesaPlontur;
import vinnsla.plantmania.Notandi;
import vinnsla.plantmania.Planta;

import java.util.List;

public class PlantController {
    @FXML
    private Plontuyfirlit fxPlonturYfirlit; //mínar plöntur yfirlitið
    private Notandi skradurNotandi;

    public void initialize() {
        skradurNotandi = ((UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA)).getSkradurNotandi();
        System.out.println(skradurNotandi);

    }


    /**
     * þegar smellt er, þá bætast við eitt spjald og plönturnar úr plontur.txt
     */
    @FXML
    protected void fxBaetaVidHandler() {
        Spjald spjald = new Spjald();
        //Rectangle spjald = new Rectangle(100, 150);
        //spjald.setFill(Color.BLUEVIOLET);
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spjald);


        LesaPlontur l = new LesaPlontur();
        List<Planta> plontur = l.getPlontur();

        PlantaSpjald spj = new PlantaSpjald(plontur.get(0));
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spj);

        spj = new PlantaSpjald(plontur.get(1));
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spj);
    }
}
