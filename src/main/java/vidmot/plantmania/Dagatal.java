package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import vinnsla.plantmania.MinPlanta;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Sérhæfður klasi fyrir Dagatal. Hefur hnappa en virkni þeirra er útfærð í PlantController eins og er,
 * til að það sé frekar hægt að endurnýta dagatalið
 */
public class Dagatal extends AnchorPane {

    @FXML
    private Button fxTilBaka;
    @FXML
    private Button fxAfram;
    @FXML
    private Label fxDagsetning;
    @FXML
    private GridPane fxGrid;
    @FXML
    private AnchorPane fxRoot;

    private String[] vikudagar;
    private String[] manudir;
    private LocalDate syndurDagur;//dagurinn sem dagatalið sýnir. Notað til að vita hvaða mánuður er sýndur í augnablikinu
    //listi af pörum sem gefa dagsetningu og plöntu sem var vökvuð þá. inniheldur öll skipti sem einhver planta hefur verið vökvuð
    private ObservableList<Pair<MinPlanta, LocalDate>> allarPlonturOgFyrriVokvanir = FXCollections.observableArrayList();

    public Dagatal() {
        lesaFXML();

        manudir = new String[]{"Janúar", "Febrúar", "Mars", "Apríl", "Maí", "Júní", "Júlí", "Ágúst", "September", "Október", "Nóvember", "Desember"};
        syndurDagur = LocalDate.now();

        geraDagatal(LocalDate.now());
    }

    /**
     * setur upp dagatal fyrir mánuðinn sem inniheldur umbeðinn dag. Setur mánaðardaga rétt upp miðað við vikudaga
     * og setur upp og tekur af bindings fyrir viðmótshluti innan hvers dags (mánaðardagur og fjöldi vökvana labelar
     * og dropi ImageView ) eftir því sem við á.
     *
     * @param dagur - LocalDate, dagur í mánuðinum sem á að sýna
     */
    //TODO: Skipta þessu upp í fleiri aðferðir
    public void geraDagatal(LocalDate dagur) {
        ObservableList<Pair<MinPlanta, LocalDate>> vokvanirManadarins = allarPlonturOgFyrriVokvanir.filtered(p -> p.getValue().getMonth() == syndurDagur.getMonth() && p.getValue().getYear() == syndurDagur.getYear());

        int fjoldiDaga = dagur.getMonth().length(dagur.isLeapYear());
        DayOfWeek fyrstiDagurManadar = LocalDate.of(dagur.getYear(), dagur.getMonthValue(), 1).getDayOfWeek();
        fxDagsetning.setText(manudir[dagur.getMonthValue() - 1] + " - " + dagur.getYear());

        List<Integer> dagalisti = new ArrayList<>();
        for (int i = 1; i <= fjoldiDaga; i++) {
            dagalisti.add(i);
        }

        for (int i = 7; i < 49; i++) {
            if (fxGrid.getChildren().get(i) instanceof Dagur && !dagalisti.isEmpty() && !((i - 7) < fyrstiDagurManadar.ordinal())) {
                LocalDate dagurinn = LocalDate.of(syndurDagur.getYear(), syndurDagur.getMonthValue(), dagalisti.get(0));
                IntegerBinding fjoldiVokvana = Bindings.size(vokvanirManadarins.filtered(p -> p.getValue().isEqual(dagurinn)));

                ((Dagur) fxGrid.getChildren().get(i)).getFxFjoldiVokvana().textProperty().bind(fjoldiVokvana.asString());//ath að breyta þessu svo ef það er 0 sé labelinn tómur
                ((Dagur) fxGrid.getChildren().get(i)).getFxDropi().visibleProperty().bind(fjoldiVokvana.greaterThan(0));
                ((Dagur) fxGrid.getChildren().get(i)).getFxManadardagur().setText(dagalisti.get(0) + "");

                dagalisti.remove(0);
            } else {
                ((Dagur) fxGrid.getChildren().get(i)).getFxDropi().visibleProperty().unbind();
                ((Dagur) fxGrid.getChildren().get(i)).getFxDropi().setVisible(false);
                ((Dagur) fxGrid.getChildren().get(i)).getFxFjoldiVokvana().textProperty().unbind();
                ((Dagur) fxGrid.getChildren().get(i)).getFxFjoldiVokvana().setText("");
                ((Dagur) fxGrid.getChildren().get(i)).getFxManadardagur().textProperty().unbind();
                ((Dagur) fxGrid.getChildren().get(i)).getFxManadardagur().setText("");
            }
        }
    }


    /**
     * les inn fxml skrána, setur controller og rót og hleður fxmlLoadernum
     */
    private void lesaFXML() {
        FXMLLoader fxmlLoader = new
                FXMLLoader(this.getClass().getResource("dagatal-view.fxml"));
        fxmlLoader.setClassLoader(this.getClass().getClassLoader());
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    //getterar og setterar
    public LocalDate getSyndurDagur() {
        return syndurDagur;
    }

    public void setSyndurDagur(LocalDate syndurDagur) {
        this.syndurDagur = syndurDagur;
    }

    public Button getFxTilBaka() {
        return fxTilBaka;
    }

    public Button getFxAfram() {
        return fxAfram;
    }

    public GridPane getFxGrid() {
        return fxGrid;
    }

    public ObservableList<Pair<MinPlanta, LocalDate>> getAllarPlonturOgFyrriVokvanir() {
        return allarPlonturOgFyrriVokvanir;
    }
}
