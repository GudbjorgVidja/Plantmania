package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import vinnsla.plantmania.Notandi;

import java.util.List;

/**
 * Höfundur: Sigurbjörg Erla
 * Klasi fyrir dialoginn sem kemur þegar notandi gerir nýjan aðgang. Skilar optional Notanda
 */
public class NyskraningDialog extends Dialog<Notandi> {
    private List<Notandi> adgangarISkra;//listi yfir alla notendur í skránni
    private TextField fxNotendanafn;//textfield fyrir notendanafn
    private PasswordField fxLykilord;//passwordField fyrir lykilorð
    private PasswordField fxEndurtekning;//passwordfield fyrir endurtekningu á lykilorði
    private Text fxVilla;//text fyrir skilaboð þegar notendanafn er nú þegar í notkun
    private BooleanProperty notandiTil = new SimpleBooleanProperty();//booleanproperty sem segir til um hvort notendanafn er í notkun
    private BooleanProperty lykilordRettEndurtekid = new SimpleBooleanProperty();//booleanproperty sem segir til um hvort það standi það sama í báðum lykilorðareitum
    private ButtonType ILagi = new ButtonType("Í lagi", ButtonBar.ButtonData.OK_DONE);//buttontype fyrir í lagi takkann
    private ButtonType HaettaVid = new ButtonType("Hætta við", ButtonBar.ButtonData.CANCEL_CLOSE);//buttontype fyrir hætta við takkann

    private Image icon = new Image(getClass().getResourceAsStream("styling/icon/check.png"));//mynd af checkmark
    private ImageView notendaNafnSamthykkt = new ImageView(icon);//imageview með iconi, sést ef notendanafn er gilt
    private ImageView lykilordSamthykkt = new ImageView(icon);//imageview með iconi, sést ef inntak er gilt
    private ImageView endurtekningSamthykkt = new ImageView(icon);//imageview með iconi, sést ef lykilorð er rétt endurtekið

    /**
     * smiður
     *
     * @param notendur - List<Notandi> listi yfir alla notendur sem eru til í kerfinu
     */
    public NyskraningDialog(List<Notandi> notendur) {
        this.adgangarISkra = notendur;
        geraUtlit();

        takkaRegla();
        erNotandiTilListener();
        erLykilordRettRegla();

        setjaValidationIcon();
        setjaIconBinding();
        fxVilla.textProperty().bind(Bindings.when(notandiTil).then("Vinsamlegast veldu annað notendanafn").otherwise(""));
        setResultConverter();
    }

    /**
     * setur stærðina á myndinni í imageview hlutina
     */
    private void setjaValidationIcon() {
        notendaNafnSamthykkt.setFitHeight(15);
        notendaNafnSamthykkt.setFitWidth(15);
        lykilordSamthykkt.setFitHeight(15);
        lykilordSamthykkt.setFitWidth(15);
        endurtekningSamthykkt.setFitHeight(15);
        endurtekningSamthykkt.setFitWidth(15);
    }

    /**
     * bindur sýnileika á imageview hlutunum við það hvort inntak er gilt
     */
    private void setjaIconBinding() {
        notendaNafnSamthykkt.visibleProperty().bind(notandiTil.not().and(fxNotendanafn.textProperty().isEmpty().not()));
        lykilordSamthykkt.visibleProperty().bind(fxLykilord.textProperty().isEmpty().not());
        endurtekningSamthykkt.visibleProperty().bind(lykilordRettEndurtekid.and(fxEndurtekning.textProperty().isEmpty().not()));
    }

    /**
     * setur upp útlitið fyrir Dialoginn
     */
    private void geraUtlit() {
        fxNotendanafn = new TextField();
        fxLykilord = new PasswordField();
        fxEndurtekning = new PasswordField();
        fxVilla = new Text();
        GridPane g = new GridPane();
        g.hgapProperty().set(10);
        g.vgapProperty().set(5);
        g.add(fxVilla, 0, 0, 2, 1);
        g.add(new Label("Notendanafn"), 0, 1);
        g.add(fxNotendanafn, 1, 1);
        g.add(notendaNafnSamthykkt, 2, 1);
        g.add(new Label("Lykilorð"), 0, 2);
        g.add(fxLykilord, 1, 2);
        g.add(lykilordSamthykkt, 2, 2);
        g.add(new Label("Endurtaka lykilorð"), 0, 3);
        g.add(fxEndurtekning, 1, 3);
        g.add(endurtekningSamthykkt, 2, 3);
        getDialogPane().setContent(g);
        getDialogPane().getButtonTypes().addAll(ILagi, HaettaVid);
        getDialogPane().setHeaderText("Nýskráning");

        getDialogPane().getStylesheets().add(getClass().getResource("styling/derived-style.css").toExternalForm());
    }

    /**
     * gerir reglu til að óvikja í lagi takkann ef reitir eru tómir, notendanafn er ekki laust
     * eða lykilorð ekki rétt endurtekið
     */
    private void takkaRegla() {
        getDialogPane().lookupButton(ILagi).disableProperty().bind(fxNotendanafn.textProperty().
                isEmpty().or(fxLykilord.textProperty().isEmpty()).or(fxEndurtekning.textProperty().isEmpty()).
                or(notandiTil).or(lykilordRettEndurtekid.not()));
    }

    /**
     * gerir listener sem fylgist með notendanafnsreit og uppfærir booleanProperty eftir því hvort
     * notendanafnið er nú þegar í notkun
     */
    private void erNotandiTilListener() {
        fxNotendanafn.textProperty().addListener((observable, oldValue, newValue) -> {
            notandiTil.set(false);
            for (Notandi n : adgangarISkra) {
                if (n.notendanafnProperty().get().equals(newValue)) {
                    notandiTil.set(true);
                }
            }
        });
    }

    /**
     * gerir listenera til að fylgjast með hvort það sé sami texti í báðum lykilorðareitum
     * og uppfærir booleanproperty eftir því sem við á
     */
    private void erLykilordRettRegla() {
        fxEndurtekning.textProperty().addListener((observable, oldValue, newValue) -> {
            lykilordRettEndurtekid.set(fxEndurtekning.textProperty().get().equals(fxLykilord.textProperty().get()));
        });
        fxLykilord.textProperty().addListener((observable, oldValue, newValue) -> {
            lykilordRettEndurtekid.set(fxEndurtekning.textProperty().get().equals(fxLykilord.textProperty().get()));
        });
    }

    /**
     * gerir resultConverter til að túlka niðurstöður
     */
    private void setResultConverter() {
        Callback<ButtonType, Notandi> personResultConverter = param -> {
            if (param == ILagi) {
                return new Notandi(fxNotendanafn.getText(), fxLykilord.getText());
            } else {
                return null;
            }
        };
        setResultConverter(personResultConverter);
    }
}
