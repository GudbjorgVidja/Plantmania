package vidmot.plantmania;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private ObjectProperty<Notandi> skradurNotandi = new SimpleObjectProperty<>();


    /**
     * kallar á aðferðir til að lesa úr skrá og setja binding á takka
     */
    public void initialize() {
        lesaUrSkra();
        takkaVirkni();
    }

    /**
     * Gerir binding til að óvirkja innskráningartakkann ef notendanafnsreitur eða lykilorðareitur er tómur
     */
    private void takkaVirkni() {
        fxInnskraning.disableProperty().bind(fxNotendanafn.textProperty().isEmpty().or(fxLykilord.textProperty().isEmpty()));
    }

    public Notandi getSkradurNotandi() {
        return skradurNotandi.get();
    }

    public ObjectProperty<Notandi> skradurNotandiProperty() {
        return skradurNotandi;
    }

    /**
     * setur skráðan notanda sem þann notanda í skránni sem hefur rétt notendanafn og lykilorð
     * miðað við inntakið
     */
    private void setjaSkradanNotanda() {
        for (Notandi n : notendur) {
            if (n.notendanafnProperty().get().equals(fxNotendanafn.textProperty().get())) {
                if (n.lykilordProperty().get().equals(fxLykilord.textProperty().get())) {
                    skradurNotandi.set(n);
                }
            }
        }
    }

    /**
     * athugar hvort inntak er gilt, þ.e.hvort notendanafn sé til og hvort lykilorð passi við það
     *
     * @return true ef inntak er ogilt, annars false
     */
    private boolean ogiltInntak() {
        for (Notandi n : notendur) {
            if (n.notendanafnProperty().get().equals(fxNotendanafn.textProperty().get())) {
                if (n.lykilordProperty().get().equals(fxLykilord.textProperty().get())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Les hluti úr skrá og býr til java hluti
     */
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

    /**
     * Skrifar lista af hlutum í nýja skrá. Ef skráin er nú þegar til er bætt við hana
     */
    private void skrifaISkra() {
        System.out.println("skrifa i skra");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File(filename);
            if (file.createNewFile())
                objectMapper.writeValue(file, notendur);
            else {
                System.out.println("skráin er til og er núna uppfærð");
                objectMapper.writeValue(file, notendur);//bætti við
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Atburðahandler til að skrá notanda inn. Ef notendanafn er til í skrá og lykilorð passar er
     * notandi skráður inn og farið í næstu senu
     *
     * @param actionEvent atburðurinn
     */
    public void skraInn(ActionEvent actionEvent) {
        if (ogiltInntak()) {
            skrifaISkra();
            setjaSkradanNotanda();
            nullStilla();
            ViewSwitcher.switchTo(View.ADALSIDA);
        } else {
            System.out.println("Notendanafn eða lykilorð rangt");
        }
    }

    /**
     * Atburðahandler til að gera nýjan aðgang. Opnar dialog þar sem notandi þarf að skrá notendanafn og lykilorð
     * fyrir nýja aðganginn, og endurtaka svo lykilorðið. Ef enginn reitur er tómur, notendanafnið er laust og
     * lykilorðið er rétt endurtekið er hægt að gera aðganginn, notandi er skráður inn og farið á næstu senu
     *
     * @param actionEvent atburðurinn
     */
    public void geraNyjanAdgang(ActionEvent actionEvent) {
        NyskraningDialog a = new NyskraningDialog(notendur);
        Optional<Notandi> utkoma = a.showAndWait();
        if (utkoma.isPresent()) {
            System.out.println("Nýr aðgangur búinn til " + utkoma.get());
            notendur.add(utkoma.get());
            skrifaISkra();
            skradurNotandi.set(utkoma.get());
            nullStilla();
            ViewSwitcher.switchTo(View.ADALSIDA);
        }
        lesaUrSkra();
    }

    public void nullStilla() {
        fxNotendanafn.textProperty().set("");
        fxLykilord.textProperty().set("");
        //notendur.clear();
    }
}
