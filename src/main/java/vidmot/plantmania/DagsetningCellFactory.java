package vidmot.plantmania;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Höfundur: Sigurbjörg Erla
 * Sérhæfður CellFactory fyrir ListView með LocalDate hlutum, til að fá réttan texta
 * fengið héðan: https://www.baeldung.com/javafx-listview-display-custom-items
 */
public class DagsetningCellFactory implements Callback<ListView<LocalDate>, ListCell<LocalDate>> {

    public ListCell<LocalDate> call(ListView<LocalDate> localDateListView) {
        return new ListCell<LocalDate>() {
            @Override
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                if (empty || localDate == null) {
                    setText(null);
                } else {
                    setText(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " ");
                }
            }
        };
    }
}
