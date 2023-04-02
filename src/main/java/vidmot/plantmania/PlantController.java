package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlantController {
    @FXML
    private Plontuyfirlit fxPlonturYfirlit; //mínar plöntur yfirlitið

    public void initialize() {
        
    }


    @FXML
    protected void fxBaetaVidHandler() {
        Rectangle spjald = new Rectangle(100, 150);
        spjald.setFill(Color.BLUEVIOLET);
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spjald);
    }
}
