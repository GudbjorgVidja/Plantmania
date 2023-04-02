package vidmot.plantmania;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import vinnsla.plantmania.Notandi;

import java.util.List;

public class NyskraningDialog extends Dialog<Notandi> {
    private List<Notandi> adgangarISkra;
    private TextField fxNotendanafn;
    private PasswordField fxLykilord;
    private PasswordField fxEndurtekning;
    private Notandi notandi = new Notandi();
    private BooleanProperty notandiTil = new SimpleBooleanProperty();
    private BooleanProperty lykilordRettEndurtekid = new SimpleBooleanProperty();

    public NyskraningDialog(List<Notandi> notendur) {
        this.adgangarISkra = notendur;
        geraUtlit();

        geraBinding();
        takkaRegla();
        erNotandiTilRegla();
        erLykilordRettRegla();

        setResultConverter();
    }

    private void geraUtlit() {
        fxNotendanafn = new TextField();
        fxLykilord = new PasswordField();
        fxEndurtekning = new PasswordField();
        GridPane g = new GridPane();
        g.add(new Label("Notendanafn"), 0, 0);
        g.add(fxNotendanafn, 1, 0);
        g.add(new Label("Lykilorð"), 0, 1);
        g.add(fxLykilord, 1, 1);
        g.add(new Label("endurtaka lykilorð"), 0, 2);
        g.add(fxEndurtekning, 1, 2);
        getDialogPane().setContent(g);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setHeaderText("Nýskráning");
        //initStyle(StageStyle.UNDECORATED);
        //getDialogPane().getContent().setStyle();
        //getDialogPane().setStyle("-fx-border-color: black;");
        //getDialogPane().setStyle("-fx-border-width: 5px;");
    }

    private void takkaRegla() {
        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(fxNotendanafn.textProperty().isEmpty().or(fxLykilord.textProperty().isEmpty()).or(fxEndurtekning.textProperty().isEmpty()).or(notandiTil).or(lykilordRettEndurtekid.not()));
    }

    private void erNotandiTilRegla() {
        fxNotendanafn.textProperty().addListener((observable, oldValue, newValue) -> {
            notandiTil.set(false);
            for (Notandi n : adgangarISkra) {
                if (n.notendanafnProperty().get().equals(newValue)) {
                    notandiTil.set(true);
                }
            }
        });
    }

    private void erLykilordRettRegla() {
        fxEndurtekning.textProperty().addListener((observable, oldValue, newValue) -> {
            lykilordRettEndurtekid.set(fxEndurtekning.textProperty().get().equals(fxLykilord.textProperty().get()));
        });
        fxLykilord.textProperty().addListener((observable, oldValue, newValue) -> {
            lykilordRettEndurtekid.set(fxEndurtekning.textProperty().get().equals(fxLykilord.textProperty().get()));
        });
    }

    private void geraBinding() {
        notandi.notendanafnProperty().bind(fxNotendanafn.textProperty());
        notandi.lykilordProperty().bind(fxLykilord.textProperty());
    }

    private void setResultConverter() {
        Callback<ButtonType, Notandi> personResultConverter = param -> {
            if (param == ButtonType.OK) {
                return notandi;
            } else {
                return null;
            }
        };
        setResultConverter(personResultConverter);
    }
}
