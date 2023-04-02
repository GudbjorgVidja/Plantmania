package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import vinnsla.plantmania.Notandi;

public class PlantController {
    @FXML
    private Plontuyfirlit fxPlonturYfirlit; //mínar plöntur yfirlitið
    private Notandi skradurNotandi;

    public void initialize() {
        skradurNotandi = ((UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA)).getSkradurNotandi();
        System.out.println(skradurNotandi);
    }


    @FXML
    protected void fxBaetaVidHandler() {
        Rectangle spjald = new Rectangle(100, 150);
        spjald.setFill(Color.BLUEVIOLET);
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spjald);
    }
}
