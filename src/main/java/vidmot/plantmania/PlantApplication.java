package vidmot.plantmania;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PlantApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlantApplication.class.getResource("upphaf-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 500);
        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);//það þarf að taka eitthvað út held ég, það loadast tvisvar, en virkar
        stage.setTitle("Plants in pants!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
