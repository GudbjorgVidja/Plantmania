package vidmot.plantmania;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import vinnsla.plantmania.Notandi;

import java.util.List;

//bæta við listener sem athugar hvort notendanafn er laust
public class NyskraningDialog extends Dialog<Notandi> {

    private List<Notandi> adgangarISkra;
    private TextField fxNotendanafn;
    private PasswordField fxLykilord;
    private Notandi notandi = new Notandi();
    private BooleanProperty test = new SimpleBooleanProperty();

    public NyskraningDialog(List<Notandi> notendur) {

        this.adgangarISkra = notendur;
        fxNotendanafn = new TextField();
        fxLykilord = new PasswordField();
        geraBinding();
        setResultConverter();


        getDialogPane().setContent(new VBox(fxNotendanafn, fxLykilord));
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(fxNotendanafn.textProperty().isEmpty().or(fxLykilord.textProperty().isEmpty()));
        
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
