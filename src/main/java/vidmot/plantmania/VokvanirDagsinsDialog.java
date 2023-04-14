package vidmot.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import vinnsla.plantmania.MinPlanta;

import java.time.LocalDate;

/**
 * Höfundur: Sigurbjörg Erla
 * Dialog sem kemur þegar ýtt er á dag í dagatali, til að sjá hvaða plöntur á að vökva/voru vökvaðar þann daginn
 */
public class VokvanirDagsinsDialog extends Dialog<Void> {
    public VokvanirDagsinsDialog(ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, String message) {
        ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();

        for (Pair<MinPlanta, LocalDate> par : vokvanir) {
            minarPlontur.add(par.getKey());
        }

        getDialogPane().setPadding(new Insets(10));
        getDialogPane().setHeaderText(message);

        ListView<MinPlanta> listView = new ListView<>(minarPlontur);
        listView.setMaxHeight(100);
        listView.setCellFactory(new PlantaCellFactory());
        Button skoda = new Button("Skoða");

        //TODO: breyta svo listview uppfærist þegar plöntunni er breytt í plöntuglugga!! (Nickname og dagsetningar/taka út)
        skoda.addEventFilter(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            if (listView.getSelectionModel().getSelectedItem() != null) {
                Plontugluggi gluggi = new Plontugluggi(listView.getSelectionModel().getSelectedItem());//tekur inn hlutinn sem spjaldið er fyrir
                gluggi.showAndWait();
            }
        });

        getDialogPane().setContent(new VBox(listView, skoda));
        getDialogPane().getButtonTypes().setAll(ButtonType.CLOSE);
    }
}
