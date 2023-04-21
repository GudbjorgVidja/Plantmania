package vidmot.plantmania;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;
import vinnsla.plantmania.MinPlanta;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Höfundar: Guðbjörg Viðja og Sigurbjörg Erla
 * Gluggi með nánari upplýsingar um MinPlanta hlut þar sem hægt er að skoða hann og gera breytingar
 */
public class Plontugluggi extends Dialog<Void> {
    @FXML
    private Label fxLatnesktNafn, fxAlmenntNafn, fxNaestaVokvun, fxThinnTimi, fxMedaltimi, fxUppruni;//label með ýmsum grunnupplýsingum

    @FXML
    private ImageView fxMynd;//imageview með mynd af plöntunni

    @FXML
    private Button fxAthugasemdir, fxVokvunarsaga, fxBreytaTimaMilliVokvana, fxBreytaNafni;//hnappar til að gera breytingar á plöntunni

    @FXML
    private Text fxHitastig, fxUmPlontuna, fxEitrun, fxNotes, fxLjosthorf;//svæði fyrir ýmsa texta með upplýsingum

    @FXML
    private DatePicker fxDatePicker;//datepicker til að bæta við vökvun aftur í tímann

    @FXML
    private FlowPane fxHeiti, fxEinkenni;//flowpane með öllum heitum; flowpane með öllum einkennum plöntunnar

    @FXML
    private Stats fxStats;//hlutur af klasanum stats, með gildin fyrir þessa plöntu

    @FXML
    private Label fxBanner;//label sem birtist með skilaboðum þegar notandi reynir að bæta við vökvun

    private MinPlanta minPlantan;//MinPlanta hluturinn sem glugginn er fyrir


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

        setjaEventHandlera();
        setjaUpplysingar();
        setjaYmsaListeneraOgBindings();
        setStyleClass();
        stillaStatsStaerd();
    }

    /**
     * upphafsstillir og setur listenera og bindings á ýmsa viðmótshluti
     */
    private void setjaYmsaListeneraOgBindings() {
        fxAlmenntNafn.textProperty().bind(minPlantan.gaelunafnProperty());
        setNaestaVokvunListener();
        setFxThinnTimi();
        setjaFxMedaltimi();
        setjaFxNotesBinding();
    }

    /**
     * kallar á ýmsar aðferðir sem setja upplýsingar á viðmótshlutum, og upphafsstillir labela án aðferða
     */
    private void setjaUpplysingar() {
        fxLatnesktNafn.setText(minPlantan.getFraediheiti());
        fxAlmenntNafn.setText(minPlantan.getGaelunafn());
        fxMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + minPlantan.getMyndaslod())));
        setFxHitasig();
        setFxUmPlontuna();
        setjaFxHeiti();
        setjaFxEitrun();
        setjaFxEinkenni();
        setjaFxUppruni();
        setjaFxLjosthorf();
        fxStats.setStats(minPlantan);
    }

    /**
     * setur event handlera á viðmótshluti
     */
    private void setjaEventHandlera() {
        fxBreytaNafni.setOnAction(this::breytaNafniHandler);
        fxAthugasemdir.setOnAction(this::athugasemdirHandler);
        fxVokvunarsaga.setOnAction(this::vokvunarsagaHandler);
        fxBreytaTimaMilliVokvana.setOnAction(this::setjaFxBreytaTimaMilliVokvanaEventFilter);
        datePickerHandler();
    }

    /**
     * stillir stærðina á Stats vboxunum
     */
    private void stillaStatsStaerd() {
        for (Node n : fxStats.getChildren()) {
            if (n instanceof VBox) {
                ((VBox) n).setPrefHeight(100);
                ((VBox) n).setPrefWidth(60);
            }
        }
    }

    /**
     * setur stílklklasa á labela í fxHeiti og fxEinkenni
     */
    private void setStyleClass() {
        for (Node l : fxHeiti.getChildren()) {
            if (l instanceof Label) l.getStyleClass().add("rammi-label");
        }
        for (Node l : fxEinkenni.getChildren()) {
            if (l instanceof Label) l.getStyleClass().add("rammi-label");
        }
    }

    private void setjaFxLjosthorf() {
        fxLjosthorf.setText("Best er fyrir plöntuna að fá " + minPlantan.getLjosstyrkur().getStyrkur() +
                " sólarljós í um " + minPlantan.getLjosStundir() + " tíma á dag");

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
        fxNotes.textProperty().bind(minPlantan.athugasemdirProperty());
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
        fxMedaltimi.textProperty().bind(Bindings.concat("Meðaltími milli vökvana: ")
                .concat(minPlantan.medaltimiMilliVokvanaProperty()).concat(" dagar"));
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
     * @param event - atburðurinn
     */
    private void setjaFxBreytaTimaMilliVokvanaEventFilter(ActionEvent event) {
        TextInputDialog timiDialog = new TextInputDialog(minPlantan.getAlmennurTimiMilliVokvana() + "");
        geraTextFormatter(timiDialog.getEditor());
        ButtonType iLagi = new ButtonType("Í lagi", ButtonBar.ButtonData.OK_DONE);
        ButtonType haettaVid = new ButtonType("Hætta við", ButtonBar.ButtonData.CANCEL_CLOSE);
        timiDialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
        timiDialog.getDialogPane().getButtonTypes().addAll(iLagi, haettaVid);

        timiDialog.getDialogPane().getStylesheets().add(getClass().getResource("styling/derived-style.css").toExternalForm());

        timiDialog.setHeaderText("Nýr dagafjöldi milli vökvana:");
        timiDialog.setTitle("Breyting á vökvunum");
        timiDialog.setGraphic(null);
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
            fxNaestaVokvun.setText("næst eftir " + minPlantan.naestaVokvunProperty().get() + " dag");
        } else if (naestaV == 0) {
            fxNaestaVokvun.setText("næst í dag");
        } else {
            fxNaestaVokvun.setText("næst eftir " + minPlantan.naestaVokvunProperty().get() + " daga");
        }
    }

    /**
     * upphafsstillir label fyrir næstu vökvun og setur listener sem uppfærir hann
     */
    private void setNaestaVokvunListener() {
        naestaVokvunTexti(minPlantan.naestaVokvunProperty().get());
        minPlantan.naestaVokvunProperty().addListener((observable, oldValue, newValue) -> {
            naestaVokvunTexti(newValue.intValue());
        });
    }

    /**
     * gerir fxmlloader fyrir gluggann, setur controller, hleður dialogpane með fxmlLoader og skilar honum
     *
     * @return dialogpane sem er hlaðið með fxmlloadernum
     */
    private DialogPane lesaGlugga() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(View.GLUGGI.getFileName()));
        try {
            fxmlLoader.setController(this);
            return fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Kallar á dialog sem breytir nickname fyrir MinPlanta hlut
     *
     * @param event smellt á litla merkið við hliðina á nafni plöntu
     */
    private void breytaNafniHandler(ActionEvent event) {
        TextInputDialog nafnDialog = new TextInputDialog(minPlantan.getGaelunafn());

        ButtonType iLagi = new ButtonType("vista breytingar", ButtonBar.ButtonData.OK_DONE);
        ButtonType haettaVid = new ButtonType("hætta við", ButtonBar.ButtonData.CANCEL_CLOSE);
        nafnDialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
        nafnDialog.getDialogPane().getButtonTypes().addAll(iLagi, haettaVid);

        nafnDialog.setHeaderText("Sláðu inn nýtt nafn");
        nafnDialog.setTitle("Breyting á nafni");
        nafnDialog.setGraphic(null);

        nafnDialog.getDialogPane().getStylesheets().add(getClass().getResource("styling/derived-style.css").toExternalForm());
        nafnDialog.getDialogPane().lookupButton(iLagi).disableProperty().bind(nafnDialog.getEditor().textProperty().isEmpty());

        Optional<String> inntak = nafnDialog.showAndWait();
        if (inntak.isPresent()) {
            minPlantan.setGaelunafn(inntak.get());
            System.out.println(inntak);
        }
    }

    /**
     * gerir form/mynstur fyrir LocalDate dagsetningu á DatePicker hlut með dateTimeFormatter. Grunnur af
     * Oracle API, DatePicker
     */
    private void geraPattern() {
        String pattern = "dd/MM/yyyy";
        fxDatePicker.setPromptText(pattern.toLowerCase());

        fxDatePicker.setConverter(new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    /**
     * handler fyrir datePicker hlut, bætir við vökvun á plöntuna á valdri dagsetningu (ekki í framtíðinni)
     */
    private void datePickerHandler() {
        geraPattern();
        fxDatePicker.setShowWeekNumbers(false);
        fxDatePicker.setOnAction(t -> {
            LocalDate date = fxDatePicker.getValue();
            if (date != null) {
                minPlantan.baetaVidVokvun(date);
                if (!date.isAfter(LocalDate.now())) {
                    fxBanner.setText("vökvun bætt við");
                    birtaBanner(fxBanner);
                } else {
                    fxBanner.setText("Ekki hægt að skrá vökvanir fram í tímann");
                    birtaBanner(fxBanner);
                }
            }
        });
    }

    /**
     * Birtir banner með upplýsingum í smá tíma. Copied úr plantController
     *
     * @param banner label með styleclass banner
     */
    private void birtaBanner(Label banner) {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> banner.setVisible(false));
        banner.setVisible(true);
        delay.play();
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

        dialogur.getDialogPane().getStylesheets().add(getClass().getResource("styling/derived-style.css").toExternalForm());

        TextArea textArea = new TextArea(minPlantan.getAthugasemdir());
        dialogur.getDialogPane().setContent(textArea);
        dialogur.setResultConverter(b -> {
            if (b.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) return textArea.getText();
            return null;
        });

        Optional<String> result = dialogur.showAndWait();
        result.ifPresent(s -> minPlantan.setAthugasemdir(s));
    }

    /**
     * Opnar dialog með listview yfir vökvunardagsetningar plöntunnar
     *
     * @param event smellt á vökvunarsaga hnapp
     */
    private void vokvunarsagaHandler(ActionEvent event) {
        VokvanirPlontunnarDialog vokvanirPlontunnarDialog = new VokvanirPlontunnarDialog(minPlantan);
        vokvanirPlontunnarDialog.showAndWait();
    }
}
