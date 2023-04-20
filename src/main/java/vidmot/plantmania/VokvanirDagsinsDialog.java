package vidmot.plantmania;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import vinnsla.plantmania.MinPlanta;

import java.time.LocalDate;

/**
 * Höfundur: Sigurbjörg Erla
 * Dialog sem kemur þegar ýtt er á dag í dagatali, til að sjá hvaða plöntur á að vökva/voru vökvaðar þann daginn
 */
public class VokvanirDagsinsDialog extends Dialog<Void> {
    private ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();//plöntur sem á að vökva þennan dag
    private ObservableList<MinPlanta> minarPlontur2 = FXCollections.observableArrayList();//plöntur sem á að vökva þennan dag
    private BooleanProperty ekkertValid = new SimpleBooleanProperty(true);

    //todo er þessi smiður líka að gera eitthvað?
    public VokvanirDagsinsDialog(ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, String message, ObservableList<Pair<MinPlanta, LocalDate>> vokvanir2, String message2) {
        minarPlontur2 = geraLista(vokvanir2);
        minarPlontur = geraLista(vokvanir);

        getDialogPane().setPadding(new Insets(10));

        ListView<MinPlanta> listView = geraListView(minarPlontur);
        ListView<MinPlanta> listView2 = geraListView(minarPlontur2);

        geraListenera(listView, listView2);

        ekkertValid.bind(listView2.getSelectionModel().selectedItemProperty().isNull().and(listView.getSelectionModel().selectedItemProperty().isNull()));

        Button skoda = geraSkodaTakka(listView, listView2);

        getDialogPane().setContent(setjaGridPane(message, message2, listView, listView2, skoda));
        getDialogPane().getButtonTypes().setAll(new ButtonType("Loka", ButtonBar.ButtonData.CANCEL_CLOSE));
    }

    private GridPane setjaGridPane(String message1, String message2, ListView<MinPlanta> listView1, ListView<MinPlanta> listView2, Button skoda) {
        GridPane gridPane = new GridPane();
        Text t1 = new Text(message1);
        t1.setWrappingWidth(150);
        t1.setTextAlignment(TextAlignment.CENTER);
        Text t2 = new Text(message2);
        t2.setWrappingWidth(150);
        t2.setTextAlignment(TextAlignment.CENTER);
        gridPane.add(t1, 0, 0);
        gridPane.add(t2, 1, 0);
        gridPane.add(listView1, 0, 1);
        gridPane.add(listView2, 1, 1);
        gridPane.add(skoda, 0, 2);
        gridPane.setHgap(30);
        gridPane.setVgap(10);
        return gridPane;
    }

    public VokvanirDagsinsDialog(ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, String message) {
        minarPlontur = geraLista(vokvanir);

        getDialogPane().setPadding(new Insets(10));
        getDialogPane().setHeaderText(message);

        ListView<MinPlanta> listView = geraListView(minarPlontur);
        ekkertValid.bind(listView.getSelectionModel().selectedItemProperty().isNull());

        Button skoda = geraSkodaTakka(listView);

        getDialogPane().setContent(new VBox(listView, skoda));
        getDialogPane().getButtonTypes().setAll(new ButtonType("Loka", ButtonBar.ButtonData.CANCEL_CLOSE));

        getDialogPane().getStylesheets().add(getClass().getResource("styling/derived-style.css").toExternalForm());
    }


    /**
     * gerir lista og kallar á aðferð til að setja listener á vokvanir sem uppfærir listann
     *
     * @return
     */
    private ObservableList<MinPlanta> geraLista(ObservableList<Pair<MinPlanta, LocalDate>> vokvanir) {
        ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();//plöntur sem á að vökva þennan dag
        for (Pair<MinPlanta, LocalDate> par : vokvanir) {
            minarPlontur.add(par.getKey());
        }
        setjaListenerAVokvanir(vokvanir, minarPlontur);
        return minarPlontur;
    }

    /**
     * setur listener á listann til að hann uppfærist við breytingar
     *
     * @param vokvanir
     */
    private void setjaListenerAVokvanir(ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, ObservableList<MinPlanta> minarPlontur) {
        vokvanir.addListener((ListChangeListener<Pair<MinPlanta, LocalDate>>) (observable) -> {
            minarPlontur.clear();
            for (Pair<MinPlanta, LocalDate> par : vokvanir) {
                minarPlontur.add(par.getKey());
            }
        });
    }

    private ListView<MinPlanta> geraListView(ObservableList<MinPlanta> minarPlontur) {
        ListView<MinPlanta> listView = new ListView<>(minarPlontur);
        listView.setMaxHeight(100);
        listView.setMaxWidth(150);
        listView.setCellFactory(new PlantaCellFactory());
        return listView;
    }

    //@SafeVarargs
    private Button geraSkodaTakka(ListView<MinPlanta>... listViews) {
        Button skoda = new Button("Skoða");
        skoda.disableProperty().bind(ekkertValid);
        skoda.addEventFilter(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            for (ListView<MinPlanta> listView : listViews) {
                if (listView.getSelectionModel().getSelectedItem() != null) {
                    Plontugluggi gluggi = new Plontugluggi(listView.getSelectionModel().getSelectedItem());//tekur inn hlutinn sem spjaldið er fyrir
                    gluggi.showAndWait();
                    //System.out.println("Her aetti gluggi ad opnast fyrir " + listView.getSelectionModel().getSelectedItem().getNickName());
                }
            }
        });
        return skoda;
    }

    private void geraListenera(ListView<MinPlanta> listView, ListView<MinPlanta> listView2) {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listView2.getSelectionModel().clearSelection();
            }
        });
        listView2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                listView.getSelectionModel().clearSelection();
            }
        });
    }
}
