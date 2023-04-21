package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import vinnsla.plantmania.MinPlanta;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Höfundur: Sigurbjörg Erla
 * Sérhæfður klasi fyrir Dagatal. Hefur hnappa en virkni þeirra er útfærð í PlantController eins og er,
 * til að það sé frekar hægt að endurnýta dagatalið
 */
public class Dagatal extends AnchorPane {
    @FXML
    private Button fxTilBaka;//takki til að fara til baka á næsta mánuð á undan
    @FXML
    private Button fxAfram;//takki til að fara yfir á næsta mánuð á eftir
    @FXML
    private Label fxDagsetning;//labell með mánuði og ártali mánaðarins sem er sýndur
    @FXML
    private GridPane fxGrid;//gridpane sem inniheldur dagana í dagatalinu. Inniheldur labels með vikudögum og Dagur hluti

    private final String[] MANADARHEITI = new String[]{"Janúar", "Febrúar", "Mars", "Apríl", "Maí", "Júní", "Júlí",
            "Ágúst", "September", "Október", "Nóvember", "Desember"};//óbreytanlegt fylki af strengjum með mánaðarheitum

    private YearMonth syndurManudur;//nánuðurinn sem dagatalið sýnir

    //listi af pörum sem gefa dagsetningu og plöntu sem var vökvuð þá. inniheldur öll skipti sem einhver planta hefur verið vökvuð
    private ObservableList<Pair<MinPlanta, LocalDate>> allarLoknarVokvanir = FXCollections.observableArrayList();

    //listi af pörum sem gefa dagsetningu og plöntu sem ætti að vökva þá. inniheldur þrjá mánuði fram í tímann
    private ObservableList<Pair<MinPlanta, LocalDate>> allarAaetladarVokvanir = FXCollections.observableArrayList();

    //smiðurinn, les fxml, gerir fyrsta mánuðinn, sem er núverandi mánuður
    public Dagatal() {
        LesaFXML.lesa(this, "dagatal-view.fxml");
        syndurManudur = YearMonth.now();
        geraDagatal();
    }

    /**
     * setur upp dagatal fyrir mánuðinn sem inniheldur umbeðinn dag. Setur mánaðardaga rétt upp miðað við vikudaga
     * og setur upp og tekur af bindings fyrir viðmótshluti innan hvers dags (mánaðardagur og fjöldi vökvana labelar
     * og dropi ImageView ) eftir því sem við á.
     */
    public void geraDagatal() {
        ObservableList<Pair<MinPlanta, LocalDate>> vokvanirManadarinsLokid = filteraDaga(allarLoknarVokvanir);
        ObservableList<Pair<MinPlanta, LocalDate>> vokvanirManadarinsOlokid = filteraDaga(allarAaetladarVokvanir);

        DayOfWeek fyrstiDagurManadar = syndurManudur.atDay(1).getDayOfWeek();
        fxDagsetning.setText(MANADARHEITI[syndurManudur.getMonthValue() - 1] + " - " + syndurManudur.getYear());

        List<Integer> dagalisti = geraDagalista(syndurManudur);

        for (int i = 7; i < 49; i++) {
            if (fxGrid.getChildren().get(i) instanceof Dagur && !dagalisti.isEmpty() && !((i - 7) < fyrstiDagurManadar.ordinal())) {
                LocalDate dagur = syndurManudur.atDay(dagalisti.get(0));
                IntegerBinding fjoldiVokvanaLokid = Bindings.size(vokvanirManadarinsLokid.filtered(p -> p.getValue().isEqual(dagur)));
                IntegerBinding fjoldiVokvanaOlokid = Bindings.size(vokvanirManadarinsOlokid.filtered(p -> p.getValue().isEqual(dagur)));
                BooleanProperty dagurinnErLidinn = new SimpleBooleanProperty(dagur.isBefore(LocalDate.now()));

                ((Dagur) fxGrid.getChildren().get(i)).getFxFjoldiVokvanaOlokid().styleProperty().bind(Bindings.when(dagurinnErLidinn).then("-fx-text-fill: red").otherwise("-fx-text-fill: black"));
                setjaVirkanDag((Dagur) fxGrid.getChildren().get(i), fjoldiVokvanaLokid, fjoldiVokvanaOlokid);
                ((Dagur) fxGrid.getChildren().get(i)).getFxManadardagur().setText(dagalisti.get(0) + "");

                dagalisti.remove(0);
            } else {
                setjaOvirkanDag(((Dagur) fxGrid.getChildren().get(i)));
            }
        }
    }

    /**
     * Gerir lista af heiltölum fyrir hvern mánaðardag mánaðarins í dagur
     *
     * @param yearMonth - Localdate, dagsetning í mánuðinum sem á að búa til dagalista fyrir
     * @return - dagalisti, listi af mánaðardögum
     */
    private List<Integer> geraDagalista(YearMonth yearMonth) {
        int fjoldiDaga = yearMonth.getMonth().length(yearMonth.isLeapYear());
        List<Integer> dagalisti = new ArrayList<>();
        for (int i = 1; i <= fjoldiDaga; i++) {
            dagalisti.add(i);
        }
        return dagalisti;
    }

    /**
     * filterar lista yfir vökvanir til að fá bara vökvanir fyrir gefinn mánuð
     *
     * @param allarPlonturOgVokvanir - ObserableList með pörum af MinPlanta hlutum og LocalDate dagsetningum,
     *                               fyrir allar vökvanir fyrir allar plöntur, annað hvort loknar eða óloknar
     * @return Hluti gefins lista fyrir mánuðinn sem er skoðaður
     */
    private ObservableList<Pair<MinPlanta, LocalDate>> filteraDaga(ObservableList<Pair<MinPlanta, LocalDate>> allarPlonturOgVokvanir) {
        return allarPlonturOgVokvanir.filtered(p -> p.getValue().getMonth() == syndurManudur.getMonth()
                && p.getValue().getYear() == syndurManudur.getYear());
    }


    /**
     * setur bindings á gefinn virkan dag til að birta upplýsingar um vökvanir. Setur binding á labela fyrir
     * fjölda vökvana, lokinna og ólokinna, og fyrir icon
     *
     * @param dagur               - Hlutur af klasanum Dagur sem er skoðaður
     * @param fjoldiVokvanaLokid  - IntegerBinding vaktanlegt gildi fyrir fjölda lokinna vökvana fyrir gefna dagsetningu
     * @param fjoldiVokvanaOlokid - IntegerBinding vaktanlegt gildi fyrir fjölda planaðra vökvana fyrir gefna dagsetningu
     */
    private void setjaVirkanDag(Dagur dagur, IntegerBinding fjoldiVokvanaLokid, IntegerBinding fjoldiVokvanaOlokid) {
        if (dagur.getStyleClass().size() == 0) dagur.getStyleClass().add("fxDagur"); //todo setja í upphafsstillingu
        dagur.setDisable(false);

        dagur.getFxFjoldiVokvanaOlokid().textProperty().bind(
                Bindings.when(fjoldiVokvanaOlokid.isEqualTo(0)).then("")
                        .otherwise(fjoldiVokvanaOlokid.asString()));
        dagur.getFxFjoldiVokvana().textProperty().bind(
                Bindings.when(fjoldiVokvanaLokid.isEqualTo(0)).then("")
                        .otherwise(fjoldiVokvanaLokid.asString()));
        dagur.getFxDropi().visibleProperty().bind(fjoldiVokvanaOlokid.greaterThan(0));
    }

    /**
     * setur bindings á gefinn dag til að sýna að hann er ekki hluti af mánuðinum
     *
     * @param dagur - Dagur, tómur
     */
    private void setjaOvirkanDag(Dagur dagur) {
        if (dagur.getStyleClass().size() == 0) dagur.getStyleClass().add("fxDagur");
        dagur.setDisable(true);


        //todo Sigurbjörg, ég commentaði út línurnar hér að neðan og setti í staðinn transparent hluti á disabled dag y/n
        dagur.getFxFjoldiVokvanaOlokid().textProperty().unbind();
        //dagur.getFxFjoldiVokvanaOlokid().setText("");
        dagur.getFxDropi().visibleProperty().unbind();
        //dagur.getFxDropi().setVisible(false);
        dagur.getFxFjoldiVokvana().textProperty().unbind();
        //dagur.getFxFjoldiVokvana().setText("");
        dagur.getFxManadardagur().textProperty().unbind();
        //dagur.getFxManadardagur().setText("");
    }

    //getterar og setterar
    public YearMonth getSyndurManudur() {
        return syndurManudur;
    }

    public void setSyndurManudur(YearMonth syndurManudur) {
        this.syndurManudur = syndurManudur;
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

    public ObservableList<Pair<MinPlanta, LocalDate>> getAllarLoknarVokvanir() {
        return allarLoknarVokvanir;
    }

    public ObservableList<Pair<MinPlanta, LocalDate>> getAllarAaetladarVokvanir() {
        return allarAaetladarVokvanir;
    }
}
