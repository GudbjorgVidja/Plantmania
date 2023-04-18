package vidmot.plantmania;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class PlantApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlantApplication.class.getResource("upphaf-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 500);
        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
        stage.setTitle("Plants in pants!");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent event) -> {
            PlantController pc = (PlantController) ViewSwitcher.lookup(View.ADALSIDA);
            if (pc != null) pc.lokaGluggaAdferd();
            stage.close();
        });

    }

    public static void main(String[] args) {
        launch();
    }
}
