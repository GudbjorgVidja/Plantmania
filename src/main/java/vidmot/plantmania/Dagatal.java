package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    private LocalDate syndurDagur;//ath þetta

    public Dagatal() {
        vikudagar = new String[]{"mán", "þri", "mið", "fim", "fös", "lau", "sun"};
        manudir = new String[]{"Janúar", "Febrúar", "Mars", "Apríl", "Maí", "Júní", "Júlí", "Ágúst", "September", "Október", "Nóvember", "Desember"};
        dagurinnIDag = LocalDate.now();
        syndurDagur = dagurinnIDag;

        lesaFXML();
        geraDagatal(dagurinnIDag);
    }

    private void geraDagatal(LocalDate dagur) {
        fxGrid.getChildren().clear();
        int fjoldiDaga = dagur.getMonth().length(dagur.isLeapYear());
        DayOfWeek fyrstiDagurManadar = LocalDate.of(dagur.getYear(), dagur.getMonthValue(), 1).getDayOfWeek();

        for (int i = 0; i < 7; i++) {
            fxGrid.add(new Label(vikudagar[i]), i, 0);
        }
        fxDagsetning.setText(manudir[dagur.getMonthValue() - 1] + " - " + dagur.getYear());

        List<Integer> dagalisti = new ArrayList<>();
        for (int i = 0; i < fjoldiDaga; i++) {
            dagalisti.add(i + 1);
        }

        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (!dagalisti.isEmpty() && !(i == 1 && j < fyrstiDagurManadar.ordinal())) {
                    fxGrid.add(new Label(dagalisti.get(0) + ""), j, i);//
                    dagalisti.remove(0);
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


    @FXML
    private void tilBakaUmManudHandler() {
        System.out.println("Til baka");
        syndurDagur = syndurDagur.minusMonths(1);
        geraDagatal(syndurDagur);
    }

    @FXML
    private void aframUmManudHandler() {
        System.out.println("Afram");
        syndurDagur = syndurDagur.plusMonths(1);
        geraDagatal(syndurDagur);
    }
}
