package vidmot.plantmania;

import javafx.beans.binding.Bindings;
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
    private Label fxBreytaNafni, fxLatnesktNafn, fxAlmenntNafn, fxNaestaVokvun, fxThinnTimi, fxLjosthorf, fxMedaltimi;

    @FXML
    private ImageView fxMynd;

    @FXML
    private Button fxAthugasemdir, fxVokvunarsaga, fxBreytaTimaMilliVokvana;

    @FXML
    private Text fxHitastig, fxUmPlontuna, fxEinkenni, fxEitrun;

    @FXML
    private DatePicker fxDatePicker;

    private MinPlanta minPlantan;//ef glugginn er fyrir MinPlanta

    //private Planta plantan;//ef glugginn er fyrir planta, kemur etv seinna

    //ToDo: setja fxEinkenni, fxLjosthorf (styrkur og tími?), fxEitrun, fxUppruni
    //það segir að þetta komi sem overloaded method, held ég???
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
        setjaFxMedaltimi();
    }

    /**
     * setur bindingu fyrir label með meðaltíma milli vökvana. ATH: það segir alltaf dagar í fleirtölu
     */
    public void setjaFxMedaltimi() {
        fxMedaltimi.textProperty().bind(Bindings.concat("Meðaltími milli vökvana: ").concat(minPlantan.medaltimiMilliVokvanaProperty()).concat(" dagar"));
        //hafa bindingu/listener hér
    }

    /**
     * upphafsstillir label með upplýsingum um hversu langt notandi vill að líði á milli vökvana, og setur listener
     * á thinnTimiMilliVokvanaProperty í MinPlanta til að uppfæra það. Ath að dagar er alltaf í fleirtölu
     */
    public void setFxThinnTimi() {
        fxThinnTimi.setText("Þinn tími milli vökvana er " + minPlantan.getThinnTimiMilliVokvana() + " dagar");
        minPlantan.thinnTimiMilliVokvanaProperty().addListener((observable, oldValue, newValue) -> {
            fxThinnTimi.setText("Þinn tími milli vökvana er " + newValue.intValue() + " dagar");
        });
    }

    /**
     * gerir text formatter sem settur er á TextField svo það sé aðeins hægt að slá inn tölur,
     * og hún getur ekki byrjað á 0
     * ATH: láta skilaboðin birast einhvers staðar í viðmótinu þegar villa er gripin
     * ATH: á að takmarka lengd tölu til að koma í veg fyrir misnotkun??
     *
     * @param textField - Textfield reiturinn
     */
    private void geraTextFormatter(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }
            boolean logleg = false;
            try {
                Integer.parseInt(newText);
                if (!newText.startsWith("0")) {
                    logleg = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vinsamlegast sláðu inn tölu");
            }
            if (logleg) {
                return change;
            } else {
                return null;
            }
        }));
    }

    /**
     * Handler til að breyta tíma milli vökvana þegar ýtt er á hnappinn, gerir TextInputDialog, setur formatter
     * á hann, óvirkjar ok takkann við réttar aðstæður og uppfærir thinnTimiMilliVokvana í MinPlanta ef við á
     *
     * @param mouseEvent - atburðurinn
     */
    private void setjaFxBreytaTimaMilliVokvanaEventFilter(MouseEvent mouseEvent) {
        TextInputDialog timiDialog = new TextInputDialog(minPlantan.getAlmennurTimiMilliVokvana() + "");
        geraTextFormatter(timiDialog.getEditor());
        timiDialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(timiDialog.getEditor().textProperty().isEmpty());
        Optional<String> svar = timiDialog.showAndWait();
        svar.ifPresent(s -> minPlantan.setThinnTimiMilliVokvana(Integer.parseInt(s)));
    }

    /**
     * Sækir texta um plöntuna og birtir í viðmótinu
     */
    private void setFxUmPlontuna() {
        fxUmPlontuna.setText(minPlantan.getTexti());
    }

    /**
     * Sækir upplýsingar um kjörhitastig plöntu, setur í streng og birtir í viðmóti
     */
    private void setFxHitasig() {
        fxHitastig.setText("Kjörhitastig er " + minPlantan.getKjorhitastig().get(1) +
                "°C, en plantan þolir allt á milli " + minPlantan.getKjorhitastig().get(0) +
                "°C og " + minPlantan.getKjorhitastig().get(2) + "°C.");
    }

    /**
     * setur texta fyrir fxNaestaVokvun eftir því hvað er langt í hana
     *
     * @param naestaV - heiltala, dagar í næstu vökvun
     */
    private void naestaVokvunTexti(int naestaV) {
        if (naestaV == 1) {
            fxNaestaVokvun.setText("næst eftir " + minPlantan.getNaestaVokvun().get() + " dag");
        } else if (naestaV == 0) {
            fxNaestaVokvun.setText("næst í dag");
        } else {
            fxNaestaVokvun.setText("næst eftir " + minPlantan.getNaestaVokvun().get() + " daga");
        }
    }

    /**
     * upphafsstillir label fyrir næstu vökvun og setur listener sem uppfærir hann
     */
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

    /**
     * handler fyrir datePicker hlut, bætir við vökvun á plöntuna á valdri dagsetningu
     */
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
     * prentar fyrri vökvanir og planaðar vökvanir fyrir plöntuna. Opnar dialog með listview yfir vökvunardagsetningar
     * fyrir plöntuna. Hægt að eyða út fyrri vökvun
     *
     * @param event smellt á vökvunarsaga hnapp
     */
    private void vokvunarsagaHandler(ActionEvent event) {
        System.out.println("fyrri vokvanir: " + minPlantan.getVokvanir());
        System.out.println("planadar vokvanir: " + minPlantan.getPlanadarVokvanir());
        VokvanirPlontunnarDialog vokvanirPlontunnarDialog = new VokvanirPlontunnarDialog(minPlantan);
        vokvanirPlontunnarDialog.showAndWait();
    }

}
