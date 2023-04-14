package vidmot.plantmania;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import vinnsla.plantmania.MinPlanta;

/**
 * Höfundur: Sigurbjörg Erla
 * Sérhæfður CellFactory fyrir ListView með MinPlanta hlutum, til að fá réttan texta
 * fengið héðan: https://www.baeldung.com/javafx-listview-display-custom-items
 */
public class PlantaCellFactory implements Callback<ListView<MinPlanta>, ListCell<MinPlanta>> {
    @Override
    public ListCell<MinPlanta> call(ListView<MinPlanta> param) {
        return new ListCell<>() {
            @Override
            public void updateItem(MinPlanta minPlanta, boolean empty) {
                super.updateItem(minPlanta, empty);
                if (empty || minPlanta == null) {
                    setText(null);
                } else {
                    setText(minPlanta.getNickName() + " ");
                }
            }
        };
    }
}
