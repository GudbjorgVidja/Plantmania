package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

//bregðast við þegar það er ýtt á daginn
public class Dagur extends Pane {
    @FXML
    private Label fxManadardagur;
    @FXML
    private Label fxFjoldiVokvana;
    @FXML
    private ImageView fxDropi;

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

    public void setFxFjoldiVokvana(int fjoldiVokvana) {
        fxFjoldiVokvana.setText(fjoldiVokvana + "");
    }

    public void setFxManadardagur(int manadardagur) {
        fxManadardagur.setText(manadardagur + "");
    }

    public ImageView getFxDropi() {
        return fxDropi;
    }
}
