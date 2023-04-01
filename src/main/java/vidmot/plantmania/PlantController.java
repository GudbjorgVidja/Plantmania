package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlantController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
