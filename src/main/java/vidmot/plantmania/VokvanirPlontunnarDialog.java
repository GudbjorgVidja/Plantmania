package vidmot.plantmania;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import vinnsla.plantmania.MinPlanta;

import java.time.LocalDate;

/**
 * Höfundur: Sigurbjörg Erla
 * Dialog til að sýna allar dagsetningar sem ákveðin planta hefur verið vökvuð, með ListView.
 * Verður mögulega skipt út fyrir tilvik af Dagatal
 */
public class VokvanirPlontunnarDialog extends Dialog<Void> {
    public VokvanirPlontunnarDialog(MinPlanta minPlanta) {
        getDialogPane().setPadding(new Insets(10));
        getDialogPane().setHeaderText("Vökvanir fyrir þessa plöntu");

        ListView<LocalDate> listView = new ListView<>(minPlanta.getVokvanir());
        listView.setMaxHeight(100);
        listView.setCellFactory(new DagsetningCellFactory());
        Button eyda = new Button("Eyða");
        eyda.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());

        eyda.addEventFilter(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            if (listView.getSelectionModel().getSelectedItem() != null) {
                minPlanta.takaUtVokvun(listView.getSelectionModel().getSelectedItem());
            }
        });

        getDialogPane().setContent(new VBox(listView, eyda));

        getDialogPane().getButtonTypes().setAll(new ButtonType("Loka", ButtonBar.ButtonData.CANCEL_CLOSE));
    }
}
