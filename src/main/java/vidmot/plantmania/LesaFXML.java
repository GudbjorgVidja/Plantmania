package vidmot.plantmania;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Höfundur: Ebba Þóra Hvannberg, fengið úr öðru verkefni
 * les inn fxml skrána, setur controller og rót og hleður fxmlLoadernum
 */
public class LesaFXML {
    public static void lesa(Object controller, String fxmlSkra) {
        FXMLLoader fxmlLoader = new
                FXMLLoader(controller.getClass().getResource(fxmlSkra));
        fxmlLoader.setClassLoader(controller.getClass().getClassLoader());
        fxmlLoader.setRoot(controller);
        fxmlLoader.setController(controller);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
