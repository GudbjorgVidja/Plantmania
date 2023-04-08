package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Sérhæfður klasi fyrir hvern dag í dagatali
 */
public class Dagur extends Pane {
    @FXML
    private Label fxManadardagur;
    @FXML
    private Label fxFjoldiVokvana;
    @FXML
    private ImageView fxDropi;
    @FXML
    private Label fxFjoldiVokvanaOlokid;

    /**
     * Les inn fxml skrá, setur controller og rót og hleður fxmlLoadernum
     */
    public Dagur() {
        LesaFXML.lesa(this, "dagur-view.fxml");
    }

    public Label getFxManadardagur() {
        return fxManadardagur;
    }

    public Label getFxFjoldiVokvana() {
        return fxFjoldiVokvana;
    }

    public Label getFxFjoldiVokvanaOlokid() {
        return fxFjoldiVokvanaOlokid;
    }

    public ImageView getFxDropi() {
        return fxDropi;
    }
}
