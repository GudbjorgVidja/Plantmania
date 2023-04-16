package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Höfundur: Sigurbjörg Erla
 * Sérhæfður klasi fyrir hvern dag í dagatali
 */
public class Dagur extends Pane {
    @FXML
    private Label fxManadardagur;//label fyrir númer mánaðardags
    @FXML
    private Label fxFjoldiVokvana;//label fyrir fjolda vökvana sem er lokið þennan dag
    @FXML
    private ImageView fxDropi;//imageview með mynd af dropa, verður sýnt þá daga sem á að vökva eitthvað
    @FXML
    private Label fxFjoldiVokvanaOlokid;//label fyrir fjölda vökvana sem er ólokið þennan dag

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
