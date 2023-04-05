package vidmot.plantmania;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

    public Dagatal() {
        vikudagar = new String[]{"mán", "þri", "mið", "fim", "fös", "lau", "sun"};
        manudir = new String[]{"Janúar", "Febrúar", "Mars", "Apríl", "Maí", "Júní", "Júlí", "Ágúst", "September", "Október", "Nóvember", "Desember"};
        dagurinnIDag = LocalDate.now();
        syndurDagur = dagurinnIDag;

        lesaFXML();

        setjaDaga();
        geraDagatal(dagurinnIDag);
        geraEventFilter();
    }

    private void geraEventFilter() {
        fxTilBaka.addEventFilter(ActionEvent.ACTION, (Event event) -> {
            syndurDagur = syndurDagur.minusMonths(1);
            geraDagatal(syndurDagur);
            System.out.println("Til baka");
        });
        fxAfram.addEventFilter(ActionEvent.ACTION, (Event event) -> {
            syndurDagur = syndurDagur.plusMonths(1);
            geraDagatal(syndurDagur);
            System.out.println("Áfram");
        });

        fxGrid.setOnMouseClicked((MouseEvent event) -> {
            Node node = event.getPickResult().getIntersectedNode();
            Dagur dagur = null;

            if (node instanceof Dagur) {
                dagur = (Dagur) node;
            } else if (node.getParent() instanceof Dagur) {
                dagur = (Dagur) node.getParent();
            } else if (node.getParent().getParent() instanceof Dagur) {
                dagur = (Dagur) node.getParent().getParent();
            }

            if (dagur != null && !dagur.getFxManadardagur().getText().equals("")) {
                System.out.println(dagur.getFxManadardagur().getText());
                setValinnDagur(LocalDate.of(syndurDagur.getYear(), syndurDagur.getMonthValue(), Integer.parseInt(dagur.getFxManadardagur().getText())));
                //opna glugga með því sem er á þessum degi
            }
        });
    }


    private void geraDagatal(LocalDate dagur) {
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
                ((Dagur) fxGrid.getChildren().get(i)).getFxDropi().setVisible(true);
                ((Dagur) fxGrid.getChildren().get(i)).getFxFjoldiVokvana().setText(0 + "");
                ((Dagur) fxGrid.getChildren().get(i)).getFxManadardagur().setText(dagalisti.get(0) + "");
                dagalisti.remove(0);
            }
        }
    }

    private void hreinsaDaga() {
        for (int i = 7; i < 49; i++) {
            if (fxGrid.getChildren().get(i) instanceof Dagur) {
                ((Dagur) fxGrid.getChildren().get(i)).getFxDropi().setVisible(false);
                ((Dagur) fxGrid.getChildren().get(i)).getFxFjoldiVokvana().setText("");
                ((Dagur) fxGrid.getChildren().get(i)).getFxManadardagur().setText("");
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


}
