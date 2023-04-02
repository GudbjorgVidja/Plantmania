package vidmot.plantmania;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import vinnsla.plantmania.Notandi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UpphafController {
    @FXML
    private PasswordField fxLykilord;

    @FXML
    private TextField fxNotendanafn;

    @FXML
    private Button fxInnskraning;

    private static final String filename = "target/classes/vidmot/plantmania/notendur.json";

    private List<Notandi> notendur = new ArrayList<>();

    private Notandi skradurNotandi;

    public void initialize() {
        notendur.add(new Notandi("a", "a"));
        skrifaISkra();
        lesaUrSkra();
        takkaVirkni();


    }

    //innskráningarhnappur óvirkur ef reitir eru tómir
    //láta svo líka vera óvirkan ef notandi og lykilorð eru ekki í skrá
    private void takkaVirkni() {
        fxInnskraning.disableProperty().bind(fxNotendanafn.textProperty().isEmpty().or(fxLykilord.textProperty().isEmpty()));
    }

    //if fxLYkilord og fxNotenanafn eru gild, fara í næstu senu
    public void skraInn(ActionEvent actionEvent) {
        if (giltInntak()) {
            skrifaISkra();
            ViewSwitcher.switchTo(View.ADALSIDA);
        } else {
            System.out.println("Notendanafn eða lykilorð rangt");
        }
    }

    private boolean giltInntak() {
        for (Notandi n : notendur) {
            System.out.println("Athugað með notanda: " + n);
            if (n.notendanafnProperty().get().equals(fxNotendanafn.textProperty().get())) {
                if (n.lykilordProperty().get().equals(fxLykilord.textProperty().get())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void lesaUrSkra() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Gögnin sem mætti breyta t.d.
            notendur = objectMapper.readValue(
                    new File(filename),
                    new TypeReference<>() {
                    });
            System.out.println(notendur);
        } catch (IOException e) {
            System.out.println("skrá er ekki til " + e.getMessage());
        }
    }

    private void skrifaISkra() {
        System.out.println("skrifa i skra");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File(filename);
            if (file.createNewFile())
                objectMapper.writeValue(file, notendur);
            else {
                System.out.println("skráin er til og er núna uppfærð");
                objectMapper.writeValue(file, notendur);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }


    public void geraNyjanAdgang(ActionEvent actionEvent) {
        NyskraningDialog a = new NyskraningDialog(notendur);
        Optional<Notandi> aaa = a.showAndWait();
        if (aaa.isPresent()) {
            System.out.println("Nýr aðgangur búinn til " + aaa);
            notendur.add(aaa.get());
            skrifaISkra();
        }
        lesaUrSkra();
        //skipta yfir í næstu senu
    }
}
