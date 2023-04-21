package vidmot.plantmania;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import vinnsla.plantmania.Notandi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Höfundur: Sigurbjörg Erla
 * Controller fyrir upphafssíðu/innskráningarsíðu
 */
public class UpphafController {
    @FXML
    private Label fxVilla;//label fyrir villuskilaboð sem birtast ef rangt notendanafn eða lykilorð er slegið inn
    @FXML
    private PasswordField fxLykilord;//Lykilorðareitur fyrir lykilorð notandans
    @FXML
    private TextField fxNotendanafn;//Textfield fyrir notendanafn
    @FXML
    private Button fxInnskraning;//innskráningarhnappur

    private final String filename = "target/classes/vidmot/plantmania/notendur.json";//slóð á json skrá

    private Notandi skradurNotandi = new Notandi();


    /**
     * kallar á aðferðir til að lesa úr skrá og setja binding á takka
     */
    public void initialize() {
        takkaVirkni();
    }

    /**
     * Gerir binding til að óvirkja innskráningartakkann ef notendanafnsreitur eða lykilorðareitur er tómur
     */
    private void takkaVirkni() {
        fxInnskraning.disableProperty().bind(fxNotendanafn.textProperty().isEmpty().or(fxLykilord.textProperty().isEmpty()));
    }

    public Notandi getSkradurNotandi() {
        return skradurNotandi;
    }

    /**
     * setur skráðan notanda sem þann notanda í skránni sem hefur rétt notendanafn og lykilorð
     * miðað við inntakið
     */
    private void setjaSkradanNotanda() {
        List<Notandi> notendur = lesaUrSkra();
        for (Notandi n : notendur) {
            if (n.notendanafnProperty().get().equals(fxNotendanafn.textProperty().get())) {
                if (n.lykilordProperty().get().equals(fxLykilord.textProperty().get())) {
                    skradurNotandi = n;
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
        List<Notandi> notendur = lesaUrSkra();
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
     * les úr skrá og býr til java hluti, skilar útkomunni
     *
     * @return - List<Notandi> listi af öllum notendum í skrá
     */
    private List<Notandi> lesaUrSkra() {
        List<Notandi> notendur = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            notendur = objectMapper.readValue(new File(filename), new TypeReference<>() {
            });
            System.out.println(notendur);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Skrá er ekki til");
        }
        return notendur;
    }


    /**
     * Skrifar lista af hlutum í nýja skrá. Ef skráin er nú þegar til er bætt við hana
     */
    private void skrifaISkra(List<Notandi> notendur) {
        System.out.println("skrifa i skra");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            File file = new File(filename);
            if (file.createNewFile()) {
                System.out.println("Ný skrá búin til");
                objectMapper.writeValue(file, notendur);
            } else {
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
            setjaSkradanNotanda();
            System.out.println(skradurNotandi);
            ViewSwitcher.switchTo(View.ADALSIDA);
        } else {
            System.out.println("Notendanafn eða lykilorð rangt");
            fxVilla.visibleProperty().set(true);
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
        List<Notandi> notendur = lesaUrSkra();
        NyskraningDialog nyskraningDialog = new NyskraningDialog(notendur);
        Optional<Notandi> utkoma = nyskraningDialog.showAndWait();
        if (utkoma.isPresent()) {
            notendur.add(utkoma.get());
            skrifaISkra(notendur);
            skradurNotandi = utkoma.get();
            ViewSwitcher.switchTo(View.ADALSIDA);
        }
    }
}
