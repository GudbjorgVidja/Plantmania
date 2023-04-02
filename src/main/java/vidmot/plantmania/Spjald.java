/**
 * Sérhæfði klasinn spjald, grunnur fyrir plöntuspjöldin
 */
package vidmot.plantmania;

import javafx.fxml.FXMLLoader;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class Spjald extends Rectangle {
    public Spjald() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
