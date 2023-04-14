package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import vinnsla.plantmania.MinPlanta;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Höfundar: Guðbjörg Viðja og Sigurbjörg Erla
 * skoða með að hafa eiginlega accordionPane í skrollanlega partinum. Þyrfti að vera TitledPane hlutir í VBox sem er í
 * ScrollPane, því accordion leyfir bara eitt opið í einu.
 * Nota listener til að setja min stærð eftir því hvort titledPane er opið eða lokað.
 * <p>
 * Byrja að gera þetta bara fyrir minPlanta hlut. Einhver virkni komin
 */
public class Plontugluggi extends Dialog<Void> {
    @FXML
    private Label fxBreytaNafni, fxLatnesktNafn, fxAlmenntNafn, fxNaestaVokvun, fxThinnTimi, fxMedaltimi, fxUppruni;

    @FXML
    private ImageView fxMynd;

    @FXML
    private Button fxAthugasemdir, fxVokvunarsaga, fxBreytaTimaMilliVokvana;

    @FXML
    private Text fxHitastig, fxUmPlontuna, fxEitrun, fxNotes, fxLjosthorf;

    @FXML
    private DatePicker fxDatePicker;

    @FXML
    private FlowPane fxHeiti, fxEinkenni;

    private MinPlanta minPlantan;//ef glugginn er fyrir MinPlanta

    //private Planta plantan;//ef glugginn er fyrir planta, kemur etv seinna

    //ToDo: setja fxEinkenni, fxLjosthorf (styrkur og tími?), fxEitrun, fxUppruni
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
        setjaFxHeiti();
        setjaFxNotesBinding();
        setjaFxEitrun();
        setjaFxEinkenni();
        setjaFxUppruni();
        setjaFxLjosthorf();

        setStyleClass();
    }

    private void setStyleClass() {
        for (Node l : fxHeiti.getChildren()) {
            if (l instanceof Label) l.getStyleClass().add("rammi-label");
        }

        for (Node l : fxEinkenni.getChildren()) {
            if (l instanceof Label) l.getStyleClass().add("rammi-label");
        }
    }

    private void setjaFxLjosthorf() {
        fxLjosthorf.setText("Best er fyrir plöntuna að fá " + minPlantan.getLjosstyrkur().getStyrkur() + " sólarljós í um " + minPlantan.getLjosStundir() + " tíma á dag");

    }

    private void setjaFxUppruni() {
        fxUppruni.setText(minPlantan.getUppruni().getStadur());
    }

    public void setjaFxEitrun() {
        fxEitrun.setText(minPlantan.getEitrun().getEitrunarSkilabod());
    }

    public void setjaFxEinkenni() {
        for (String einkenni : minPlantan.getEinkenniPlontu()) {
            Label label = new Label(einkenni);
            label.getStyleClass().add("rammi-label");
            fxEinkenni.getChildren().add(label);
        }
    }

    /**
     * bindur textann í fxNotes við notesFraNotanda í MinPlanta
     */
    public void setjaFxNotesBinding() {
        fxNotes.textProperty().bind(minPlantan.notesFraNotandaProperty());
    }

    public void setjaFxHeiti() {
        for (String nafn : minPlantan.getOllHeiti()) {
            Label label = new Label(nafn);
            label.getStyleClass().add("rammi-label");
            fxHeiti.getChildren().add(label);
        }
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
     * ATH hvernig buttonTypes er skipt út
     *
     * @param mouseEvent - atburðurinn
     */
    private void setjaFxBreytaTimaMilliVokvanaEventFilter(MouseEvent mouseEvent) {
        TextInputDialog timiDialog = new TextInputDialog(minPlantan.getAlmennurTimiMilliVokvana() + "");
        geraTextFormatter(timiDialog.getEditor());
        ButtonType iLagi = new ButtonType("Í lagi", ButtonBar.ButtonData.OK_DONE);
        ButtonType haettaVid = new ButtonType("Hætta við", ButtonBar.ButtonData.CANCEL_CLOSE);
        timiDialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
        timiDialog.getDialogPane().getButtonTypes().addAll(iLagi, haettaVid);

        timiDialog.setHeaderText("Breyting á þínum tíma milli vökvana");
        timiDialog.setTitle("");

        timiDialog.getDialogPane().lookupButton(iLagi).disableProperty().bind(timiDialog.getEditor().textProperty().isEmpty());
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

        ButtonType iLagi = new ButtonType("vista breytingar", ButtonBar.ButtonData.OK_DONE);
        ButtonType haettaVid = new ButtonType("hætta við", ButtonBar.ButtonData.CANCEL_CLOSE);
        nafnDialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);

        nafnDialog.getDialogPane().getButtonTypes().addAll(iLagi, haettaVid);

        nafnDialog.setHeaderText("Breyting á nafni");
        nafnDialog.setTitle("");

        Optional<String> inntak = nafnDialog.showAndWait();
        if (inntak.isPresent()) {
            minPlantan.setNickName(inntak.get());
            System.out.println(inntak);
            //todo passa að nafn bætist við öll heiti?
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
