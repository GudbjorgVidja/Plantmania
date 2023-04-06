package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

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

    /**
     * Les inn fxml skrá, setur controller og rót og hleður fxmlLoadernum
     */
    public Dagur() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dagur-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Label getFxManadardagur() {
        return fxManadardagur;
    }

    public Label getFxFjoldiVokvana() {
        return fxFjoldiVokvana;
    }

    public ImageView getFxDropi() {
        return fxDropi;
    }
}
