package vidmot.plantmania;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
 * Hefur tvo smiði til að gera birt ýmist eitt eða tvö listview
 */
public class VokvanirDagsinsDialog extends Dialog<Void> {
    private BooleanProperty ekkertValid = new SimpleBooleanProperty(true);

    /**
     * smiður fyrir VokvanirDagsinsDialog þegar það þarf tvö ListView
     *
     * @param vokvanir  - fyrri vaktanlegi listinn af pörum af MinPlanta og Localdate fyrir vökvanir dagsins
     * @param message   - Strengur með skilaboðum sem fylgja með fyrri listanum
     * @param vokvanir2 - seinni vaktanlegi listinn af pörum af MinPlanta og LocalDate fyrir vökvanir dagsins
     * @param message2  - Strengur með skilaboðum sem fylgja sem seinni listanum
     */
    public VokvanirDagsinsDialog(ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, String message, ObservableList<Pair<MinPlanta, LocalDate>> vokvanir2, String message2) {
        ObservableList<MinPlanta> minarPlontur2 = geraLista(vokvanir2);
        ObservableList<MinPlanta> minarPlontur = geraLista(vokvanir);

        getDialogPane().setPadding(new Insets(10));

        ListView<MinPlanta> listView = geraListView(minarPlontur);
        ListView<MinPlanta> listView2 = geraListView(minarPlontur2);

        geraListenera(listView, listView2);

        ekkertValid.bind(listView2.getSelectionModel().selectedItemProperty().isNull().and(listView.getSelectionModel().selectedItemProperty().isNull()));

        Button skoda = geraSkodaTakka(listView, listView2);

        getDialogPane().setContent(setjaGridPane(message, message2, listView, listView2, skoda));
        getDialogPane().getButtonTypes().setAll(new ButtonType("Loka", ButtonBar.ButtonData.CANCEL_CLOSE));
    }

    /**
     * smiður fyrir VokvanirDagsinsDialog þegar það þarf bara eitt ListView
     *
     * @param vokvanir - Vaktanlegur listi af pörum af MinPlanta og Localdate fyrir vökvanir dagsins
     * @param message  - skilaboð sem á að birta með listanum
     */
    public VokvanirDagsinsDialog(ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, String message) {
        ObservableList<MinPlanta> minarPlontur = geraLista(vokvanir);

        getDialogPane().setPadding(new Insets(10));
        Text t1 = new Text(message);
        t1.setWrappingWidth(150);
        t1.setTextAlignment(TextAlignment.CENTER);

        ListView<MinPlanta> listView = geraListView(minarPlontur);
        ekkertValid.bind(listView.getSelectionModel().selectedItemProperty().isNull());

        Button skoda = geraSkodaTakka(listView);

        VBox vBox = new VBox(t1, listView, skoda);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        getDialogPane().setContent(vBox);
        getDialogPane().getButtonTypes().setAll(new ButtonType("Loka", ButtonBar.ButtonData.CANCEL_CLOSE));
        getDialogPane().getStylesheets().add(getClass().getResource("styling/derived-style.css").toExternalForm());
    }

    /**
     * gerir útlit fyrir gridpane sem er sýndur í dialoginum þegar hann á að innihalda tvö listView
     *
     * @param message1  - Strengur með skilaboðum fyrir fyrri ListView hlutinn
     * @param message2  - Strengur með skilaboðum fyrir seinni ListView hlutinn
     * @param listView1 - Fyrri ListView hluturinn
     * @param listView2 - Seinni ListView hluturinn
     * @param skoda     - Button til að skoða hlut af listview
     * @return gridpane með listviewum, skilaboðum og takka
     */
    private GridPane setjaGridPane(String message1, String message2, ListView<MinPlanta> listView1, ListView<MinPlanta> listView2, Button skoda) {
        GridPane gridPane = new GridPane();
        Text t1 = new Text(message1);
        t1.setWrappingWidth(150);
        t1.setTextAlignment(TextAlignment.CENTER);
        Text t2 = new Text(message2);
        t2.setWrappingWidth(150);
        t2.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(skoda, HPos.CENTER);
        gridPane.add(t1, 0, 0);
        gridPane.add(t2, 1, 0);
        gridPane.add(listView1, 0, 1);
        gridPane.add(listView2, 1, 1);
        gridPane.add(skoda, 0, 2, 2, 1);
        gridPane.setHgap(30);
        gridPane.setVgap(10);
        return gridPane;
    }

    /**
     * gerir lista og kallar á aðferð til að setja listener á vokvanir sem uppfærir listann
     *
     * @return ObservableList af MinPlanta hlutum
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
     * setur listener á vökvanalistann til að hann uppfærist við breytingar
     *
     * @param vokvanir     - vaktanlegur listi með pörum af MinPlanta hlutum og LocalDate dagsetningum vökvana
     * @param minarPlontur - vaktanlegur listi af MinPlanta hlutum
     */
    private void setjaListenerAVokvanir(ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, ObservableList<MinPlanta> minarPlontur) {
        vokvanir.addListener((ListChangeListener<Pair<MinPlanta, LocalDate>>) (observable) -> {
            minarPlontur.clear();
            for (Pair<MinPlanta, LocalDate> par : vokvanir) {
                minarPlontur.add(par.getKey());
            }
        });
    }

    /**
     * gerir ListView af MinPlanta hlutum, setur hæð og breidd og cell factory
     *
     * @param minarPlontur - Vaktanlegur listi af MinPlanta hlutum sem eiga að vera í ListView
     * @return ListView af MinPlanta hlutum
     */
    private ListView<MinPlanta> geraListView(ObservableList<MinPlanta> minarPlontur) {
        ListView<MinPlanta> listView = new ListView<>(minarPlontur);
        listView.setMaxHeight(100);
        listView.setMaxWidth(150);
        listView.setCellFactory(new PlantaCellFactory());
        return listView;
    }

    /**
     * gerir takka til að skoða valda plöntu, setur binding til að óvirkja hann ef ekkert er valið og
     * setur eventfilter á hann til að bregðast við þegar ýtt er á hann
     *
     * @param listViews - eitt eða fleiri ListView með MinPlanta hluti
     * @return skoda takkinn sem var búinn til
     */
    @SafeVarargs
    private Button geraSkodaTakka(ListView<MinPlanta>... listViews) {
        Button skoda = new Button("Skoða");
        skoda.setPrefWidth(120);
        skoda.disableProperty().bind(ekkertValid);
        skoda.addEventFilter(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            for (ListView<MinPlanta> listView : listViews) {
                if (listView.getSelectionModel().getSelectedItem() != null) {
                    Plontugluggi gluggi = new Plontugluggi(listView.getSelectionModel().getSelectedItem());//tekur inn hlutinn sem spjaldið er fyrir
                    gluggi.showAndWait();
                }
            }
        });
        return skoda;
    }

    /**
     * Setur listenera á tvo ListView hluti svo að það sé bara hægt að hafa atriði úr öðrum þeirra valið í einu
     *
     * @param listView  - ListView af MinPlanta hlutum, fyrra ListViewið
     * @param listView2 - ListView af MinPlanta hlutum, seinna ListViewið
     */
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
