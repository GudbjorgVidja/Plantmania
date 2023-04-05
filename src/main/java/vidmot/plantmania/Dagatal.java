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
    private LocalDate dagurinnIDag;
    private LocalDate syndurDagur;
    private LocalDate valinnDagur;
    private ObservableList<Pair<MinPlanta, LocalDate>> plonturOgVokvanir = FXCollections.observableArrayList();

    public Dagatal() {
        vikudagar = new String[]{"mán", "þri", "mið", "fim", "fös", "lau", "sun"};
        manudir = new String[]{"Janúar", "Febrúar", "Mars", "Apríl", "Maí", "Júní", "Júlí", "Ágúst", "September", "Október", "Nóvember", "Desember"};
        dagurinnIDag = LocalDate.now();
        syndurDagur = dagurinnIDag;

        lesaFXML();

        setjaDaga();
        geraDagatal(dagurinnIDag);
    }

    //hér gæti eitthvað valdið vandræðum (mögulega vegna bindings)
    public void geraDagatal(LocalDate dagur) {
        ObservableList<Pair<MinPlanta, LocalDate>> vokvanirManadarins = plonturOgVokvanir.filtered(p -> p.getValue().getMonth() == syndurDagur.getMonth() && p.getValue().getYear() == syndurDagur.getYear());

        hreinsaDaga();
        int fjoldiDaga = dagur.getMonth().length(dagur.isLeapYear());
        DayOfWeek fyrstiDagurManadar = LocalDate.of(dagur.getYear(), dagur.getMonthValue(), 1).getDayOfWeek();
        fxDagsetning.setText(manudir[dagur.getMonthValue() - 1] + " - " + dagur.getYear());

        List<Integer> dagalisti = new ArrayList<>();
        for (int i = 0; i < fjoldiDaga; i++) {
            dagalisti.add(i + 1);
        }

        for (int i = 7; i < 49; i++) {
            if (fxGrid.getChildren().get(i) instanceof Dagur && !dagalisti.isEmpty() && !((i - 7) < fyrstiDagurManadar.ordinal())) {

                LocalDate dagurinn = LocalDate.of(syndurDagur.getYear(), syndurDagur.getMonthValue(), dagalisti.get(0));
                IntegerBinding count = Bindings.size(vokvanirManadarins.filtered(p -> p.getValue().isEqual(dagurinn)));
                ((Dagur) fxGrid.getChildren().get(i)).getFxFjoldiVokvana().textProperty().bind(count.asString());

                //ath hvort þetta breytist sjálfkrafa. örugglega hægt að útfæra til að það séu ekki dropar ef það er ekki mánaðardagur
                ((Dagur) fxGrid.getChildren().get(i)).getFxDropi().visibleProperty().bind(count.greaterThan(0));

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

    private void hreinsaDaga() {
        for (int i = 7; i < 49; i++) {
            if (fxGrid.getChildren().get(i) instanceof Dagur) {
                //((Dagur) fxGrid.getChildren().get(i)).getFxDropi().setVisible(false);
                //((Dagur) fxGrid.getChildren().get(i)).getFxFjoldiVokvana().setText("");
                //((Dagur) fxGrid.getChildren().get(i)).getFxManadardagur().setText("");
            }
        }
    }

    private void setjaDaga() {
        for (int i = 0; i < 7; i++) {
            if (fxGrid.getChildren().get(i) instanceof Pane) {
                Pane p = (Pane) fxGrid.getChildren().get(i);
                if (p.getChildren().get(0) instanceof Label) {
                    Label l = (Label) p.getChildren().get(0);
                    l.textProperty().set(vikudagar[i]);
                }
            }
        }
    }

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


    public void setValinnDagur(LocalDate valinnDagur) {
        this.valinnDagur = valinnDagur;
    }

    public LocalDate getSyndurDagur() {
        return syndurDagur;
    }

    public void setSyndurDagur(LocalDate syndurDagur) {
        this.syndurDagur = syndurDagur;
    }

    public Button getFxTilBaka() {
        return fxTilBaka;
    }

    public void setFxTilBaka(Button fxTilBaka) {
        this.fxTilBaka = fxTilBaka;
    }

    public Button getFxAfram() {
        return fxAfram;
    }

    public void setFxAfram(Button fxAfram) {
        this.fxAfram = fxAfram;
    }

    public Label getFxDagsetning() {
        return fxDagsetning;
    }

    public void setFxDagsetning(Label fxDagsetning) {
        this.fxDagsetning = fxDagsetning;
    }

    public GridPane getFxGrid() {
        return fxGrid;
    }

    public void setFxGrid(GridPane fxGrid) {
        this.fxGrid = fxGrid;
    }

    public LocalDate getValinnDagur() {
        return valinnDagur;
    }


    public ObservableList<Pair<MinPlanta, LocalDate>> getPlonturOgVokvanir() {
        return plonturOgVokvanir;
    }

    public void setPlonturOgVokvanir(ObservableList<Pair<MinPlanta, LocalDate>> plonturOgVokvanir) {
        this.plonturOgVokvanir = plonturOgVokvanir;
    }


}
