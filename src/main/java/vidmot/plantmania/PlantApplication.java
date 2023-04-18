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

        /*
        PlantController pc = (PlantController) ViewSwitcher.lookup(View.ADALSIDA);
        vistaNotendaupplysingar(pc.getSkradurNotandi());
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
        stage.setOnCloseRequest(this:: (((PlantController) ViewSwitcher.lookup(View.ADALSIDA)).lokaGluggaHandler);
         */
        //PlantController pc = (PlantController) ViewSwitcher.lookup(View.ADALSIDA);
        //stage.getScene().getWindow();
        //stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::);
        //stage.setOnCloseRequest(WindowEvent.WINDOW_CLOSE_REQUEST, pc.lokaGluggaHandler(this));
        //stage.setOnCloseRequest(WindowEvent.WINDOW_CLOSE_REQUEST, pc.lokaGluggaHandler(WindowEvent event));

        stage.setOnCloseRequest((WindowEvent event) -> {
            PlantController pc = (PlantController) ViewSwitcher.lookup(View.ADALSIDA);
            System.out.println("close request");
            System.out.println(event.getEventType());
            pc.lokaGluggaAdferd();
            stage.close();
            //if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) pc.lokaGluggaHandler(event);
        });


        //stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::nafnhandlers);


        //Stage gluggi = (Stage) fxDagatal.getScene().getWindow();
        //gluggi.setOnCloseRequest(this::lokaGluggaHandler);
    }

    public static void main(String[] args) {
        launch();
    }
}
