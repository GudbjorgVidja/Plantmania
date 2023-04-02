package vidmot.plantmania;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Plontuyfirlit extends AnchorPane {
    public Plontuyfirlit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("plontuyfirlit.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }
    
}
