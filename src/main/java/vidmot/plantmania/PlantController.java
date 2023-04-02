package vidmot.plantmania;

import javafx.fxml.FXML;

public class PlantController {
    @FXML
    private Plontuyfirlit fxPlonturYfirlit; //mínar plöntur yfirlitið

    public void initialize() {

    }


    @FXML
    protected void fxBaetaVidHandler() {
        Spjald spjald = new Spjald();
        //Rectangle spjald = new Rectangle(100, 150);
        //spjald.setFill(Color.BLUEVIOLET);
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spjald);
    }
}
