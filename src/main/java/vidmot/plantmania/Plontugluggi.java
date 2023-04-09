package vidmot.plantmania;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import vinnsla.plantmania.MinPlanta;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * skoða með að hafa eiginlega accordionPane í skrollanlega partinum. Þyrfti að vera TitledPane hlutir í VBox sem er í
 * ScrollPane, því accordion leyfir bara eitt opið í einu.
 * Nota listener til að setja min stærð eftir því hvort titledPane er opið eða lokað.
 * <p>
 * Byrja að gera þetta bara fyrir minPlanta hlut. Einhver virkni komin
 */
public class Plontugluggi extends Dialog<Void> {
    @FXML
    private Label fxBreytaNafni, fxLatnesktNafn, fxAlmenntNafn;

    @FXML
    private ImageView fxMynd;

    @FXML
    private Button fxAthugasemdir, fxVokvunarsaga;

    @FXML
    private Label fxNaestaVokvun, fxThinnTimi;

    @FXML
    private Text fxHitastig, fxUmPlontuna;
    @FXML
    private DatePicker fxDatePicker;
    @FXML
    private Button fxBreytaTimaMilliVokvana;
    private MinPlanta minPlantan;//ef glugginn er fyrir MinPlanta

    //private Planta plantan;//ef glugginn er fyrir planta, kemur seinna

    public Plontugluggi() {
        setDialogPane(lesaGlugga());
        ButtonType lokaTakki = new ButtonType("Loka glugga", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().add(lokaTakki);
    }

    public Plontugluggi(MinPlanta minPlanta) {
        minPlantan = minPlanta;
        setDialogPane(lesaGlugga());
        ButtonType lokaTakki = new ButtonType("Loka glugga", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().add(lokaTakki);

        fxLatnesktNafn.setText(minPlantan.getLatnesktNafn());
        fxAlmenntNafn.setText(minPlantan.getNickName());
        fxAlmenntNafn.textProperty().bind(minPlantan.nickNameProperty());
        fxMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + minPlantan.getMyndaslod())));

        fxBreytaNafni.setOnMouseClicked(this::breytaNafniHandler);
        fxAthugasemdir.setOnAction(this::athugasemdirHandler);
        fxVokvunarsaga.setOnAction(this::vokvunarsagaHandler);
        fxBreytaTimaMilliVokvana.setOnMouseClicked(this::setjaFxBreytaTimaMilliVokvanaEventFilter);

        setFxHitasig();
        datePickerHandler();
        setNaestaVokvunListener();
        setFxUmPlontuna();
        setFxThinnTimi();
    }

    //ath að þetta kemur ekki fram á dagatali eins og er, en það vantar listener í MinPlanta
    public void setFxThinnTimi() {
        fxThinnTimi.setText("Þinn tími milli vökvana er " + minPlantan.getThinnTimiMilliVokvana() + " dagar");
        minPlantan.thinnTimiMilliVokvanaProperty().addListener((observable, oldValue, newValue) -> {
            fxThinnTimi.setText("Þinn tími milli vökvana er " + newValue.intValue() + " dagar");
        });
    }

    private void geraTextFormatter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            boolean logleg = false;
            try {
                Integer.parseInt(newText);
                //if (!newText.startsWith("0")) {//ætti þetta að vera skilyrði??
                logleg = true;
                //}
            } catch (NumberFormatException e) {
                //ath að það er ekki heldur hægt að stroka út það sem var í upphafi!
                System.out.println("Vinsamlegast sláðu inn tölu");
            }
            if (logleg) {//ath newText.length > 1000 eða eitthvað, til að koma í veg fyrir misnotkun
                return change;
            } else {
                //passa að láta vita að það megi bara vera tölustafir og annað sem veldur því að ekkert bætist við
                return null;
            }
        }));
    }

    private void setjaFxBreytaTimaMilliVokvanaEventFilter(MouseEvent mouseEvent) {
        System.out.println("Thinum tima milli vokvana verdur breytt");
        TextInputDialog timiDialog = new TextInputDialog(minPlantan.getAlmennurTimiMilliVokvana() + "");
        geraTextFormatter(timiDialog.getEditor());
        timiDialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(timiDialog.getEditor().textProperty().isEmpty());
        Optional<String> svar = timiDialog.showAndWait();
        svar.ifPresent(s -> minPlantan.setThinnTimiMilliVokvana(Integer.parseInt(s)));
    }

    private void setFxUmPlontuna() {
        fxUmPlontuna.setText(minPlantan.getTexti());
    }

    private void setFxHitasig() {
        fxHitastig.setText("Kjörhitastig er " + minPlantan.getKjorhitastig().get(1) +
                "°C, en plantan þolir allt á milli " + minPlantan.getKjorhitastig().get(0) +
                "°C og " + minPlantan.getKjorhitastig().get(2) + "°C.");
    }

    private void naestaVokvunTexti(int naestaV) {
        if (naestaV == 1) {
            fxNaestaVokvun.setText("næst eftir " + minPlantan.getNaestaVokvun().get() + " dag");
        } else if (naestaV == 0) {
            fxNaestaVokvun.setText("næst í dag");
        } else {
            fxNaestaVokvun.setText("næst eftir " + minPlantan.getNaestaVokvun().get() + " daga");
        }
    }

    private void setNaestaVokvunListener() {
        naestaVokvunTexti(minPlantan.getNaestaVokvun().get());
        minPlantan.getNaestaVokvun().addListener((observable, oldValue, newValue) -> {
            naestaVokvunTexti(newValue.intValue());
        });
    }

    private DialogPane lesaGlugga() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(View.GLUGGI.getFileName()));
        try {
            fxmlLoader.setController(this);
            //fxmlLoader.setRoot(this);
            return fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Breytir nickname fyrir MinPlanta hlut
     *
     * @param event smellt á litla merkið við hliðina á nafni plöntu
     */
    private void breytaNafniHandler(MouseEvent event) {
        System.out.println("nafni verdur breytt");
        TextInputDialog nafnDialog = new TextInputDialog(minPlantan.getNickName());
        //nafnDialog.getDialogPane().getButtonTypes()
        Optional<String> inntak = nafnDialog.showAndWait();
        if (inntak.isPresent()) {
            minPlantan.setNickName(inntak.get());
            System.out.println(inntak);
            /* skoða betur, kastar villu
            if (!minPlantan.getOllHeiti().contains(inntak.get())) {
            List<String> nyrListi = minPlantan.getOllHeiti();
            nyrListi.add(inntak.get());
            minPlantan.setOllHeiti(nyrListi);
            }
            */
        }
    }

    private void datePickerHandler() {
        fxDatePicker.setOnAction(t -> {
            LocalDate date = fxDatePicker.getValue();
            System.out.println("Selected date: " + date);
            if (date != null) {
                minPlantan.baetaVidVokvun(date);
            }
        });
    }

    /**
     * Dialogur sem inniheldur textArea opnast. Í þessu textArea eru Notes frá notanda, ef ekkert þá er það tomt.
     * Ef eitthvað er skrifað í gluggann og valið að vista það, þá vistast það í minPlanta hlutinn.
     *
     * @param event smellt á takkann breyta nótum eða eitthvað
     */
    private void athugasemdirHandler(ActionEvent event) {
        ButtonType vista = new ButtonType("vista breytingar", ButtonBar.ButtonData.OK_DONE);
        ButtonType haettaVid = new ButtonType("hætta við", ButtonBar.ButtonData.CANCEL_CLOSE);
        Dialog<String> dialogur = new Dialog<>();
        dialogur.getDialogPane().getButtonTypes().addAll(vista, haettaVid);
        TextArea textArea = new TextArea(minPlantan.getNotesFraNotanda());
        dialogur.getDialogPane().setContent(textArea);
        dialogur.setResultConverter(b -> {
            if (b.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) return textArea.getText();
            return null;
        });

        //dialogur.setDialogPane(dialogPane);
        Optional<String> result = dialogur.showAndWait();
        if (result.isPresent()) {//result vistar breytingar á texta
            System.out.println("utkoma ur dialog: " + result);
            minPlantan.setNotesFraNotanda(result.get());
        }
    }

    /**
     * prentar fyrri vökvanir og planaðar vökvanir fyrir plöntuna
     *
     * @param event smellt á vökvunarsaga hnapp
     */
    private void vokvunarsagaHandler(ActionEvent event) {
        System.out.println("fyrri vokvanir: " + minPlantan.getVokvanir());
        System.out.println("planadar vokvanir: " + minPlantan.getPlanadarVokvanir());
    }

}
