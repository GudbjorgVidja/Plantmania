package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import vinnsla.plantmania.Notandi;

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


    @FXML
    protected void fxBaetaVidHandler() {
        Spjald spjald = new Spjald();
        //Rectangle spjald = new Rectangle(100, 150);
        //spjald.setFill(Color.BLUEVIOLET);
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spjald);
    }

    public void skraUt(ActionEvent actionEvent) {
        skradurNotandi = null;
        System.out.println("skra ut");
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }
}
