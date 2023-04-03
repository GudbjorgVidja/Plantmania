/**
 * Sérhæfði klasinn spjald, grunnur fyrir plöntuspjöldin
 */
package vidmot.plantmania;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Spjald extends AnchorPane {
    public Spjald() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("planta-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
