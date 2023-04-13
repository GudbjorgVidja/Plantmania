package vidmot.plantmania;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 * <p>
 * EÞH - changed to include caching of controllers
 * <p>
 * Breyttum til að vista ekki í cache
 */
public class ViewSwitcher {
    //private static final Map<View, Parent> cache = new HashMap<>();
    private static final Map<View, Object> controllers = new HashMap<>();
    private static Scene scene;

    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    public static void switchTo(View view) {
        if (scene == null) {
            System.out.println("No scene was set");
            return;
        }
        try {
            FXMLLoader loader = new
                    FXMLLoader(ViewSwitcher.class.getResource(view.getFileName()));
            System.out.println("Loading from FXML");
            Parent root = loader.load();
            scene.setRoot(root);
            controllers.put(view, loader.getController());
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object lookup(View v) {
        return controllers.get(v);
    }
}
