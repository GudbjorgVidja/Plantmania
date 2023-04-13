package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import vinnsla.plantmania.Notandi;

import java.util.List;

/**
 * Klasi fyrir dialoginn sem kemur þegar notandi gerir nýjan aðgang
 */
public class NyskraningDialog extends Dialog<Notandi> {
    private List<Notandi> adgangarISkra;
    private TextField fxNotendanafn;
    private PasswordField fxLykilord;
    private PasswordField fxEndurtekning;
    private Text fxVilla;
    private BooleanProperty notandiTil = new SimpleBooleanProperty();
    private BooleanProperty lykilordRettEndurtekid = new SimpleBooleanProperty();

    /**
     * @param notendur - List<Notandi> listi yfir alla notendur sem eru til í kerfinu
     */
    public NyskraningDialog(List<Notandi> notendur) {
        this.adgangarISkra = notendur;
        geraUtlit();

        takkaRegla();
        erNotandiTilListener();
        erLykilordRettRegla();

        fxVilla.textProperty().bind(Bindings.when(notandiTil).then("Vinsamlegast veldu annað notendanafn").otherwise(""));
        setResultConverter();
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
        g.add(fxVilla, 0, 0, 2, 1);
        g.add(new Label("Notendanafn"), 0, 1);
        g.add(fxNotendanafn, 1, 1);
        g.add(new Label("Lykilorð"), 0, 2);
        g.add(fxLykilord, 1, 2);
        g.add(new Label("Endurtaka lykilorð"), 0, 3);
        g.add(fxEndurtekning, 1, 3);
        getDialogPane().setContent(g);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setHeaderText("Nýskráning");
    }

    /**
     * gerir reglu til að óvikja í lagi takkann ef reitir eru tómir, notendanafn er ekki laust
     * eða lykilorð ekki rétt endurtekið
     */
    //TODO: Hafa eitt sameiginlegt BooleanProperty fyrir alla lógíkina
    private void takkaRegla() {
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(fxNotendanafn.textProperty().
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
            if (param == ButtonType.OK) {
                return new Notandi(fxNotendanafn.getText(), fxLykilord.getText());
            } else {
                return null;
            }
        };
        setResultConverter(personResultConverter);
    }
}
