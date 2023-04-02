package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

//bregðast við þegar það er ýtt á daginn
public class Dagur extends AnchorPane {
    @FXML
    private Label fxManadardagur;
    @FXML
    private Label fxFjoldiVokvana;

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

    public void setFxFjoldiVokvana(int fjoldiVokvana) {
        fxFjoldiVokvana.setText(fjoldiVokvana + "");
    }

    public void setFxManadardagur(int manadardagur) {
        fxManadardagur.setText(manadardagur + "");
    }

    @FXML
    private void opnaDag() {
        //opna daginn
        System.out.println("Blúbb");
    }
}
