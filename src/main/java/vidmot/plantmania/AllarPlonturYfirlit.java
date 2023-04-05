/**
 * Plöntuyfirlitið í flipanum skoða, eða allar plöntur
 * Nota þennan klasa í *staðinn fyrir* Plontuyfirlit. Er bara fyrir Planta hluti, er ekki að pæla í MinPlanta strax
 */
package vidmot.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vinnsla.plantmania.LesaPlontur;
import vinnsla.plantmania.Planta;

import java.util.List;

public class AllarPlonturYfirlit {//extends Plontuyfirlit
    private ObservableList<PlantaSpjald> allarPlontur = FXCollections.observableArrayList();

    private ObservableList<PlantaSpjald> birtarAPlontur = FXCollections.observableArrayList();

    public AllarPlonturYfirlit() {
        LesaPlontur lestur = new LesaPlontur();
        List<Planta> p = lestur.getPlontur();
        for (Planta planta : p) {
            allarPlontur.add(new PlantaSpjald(planta));
        }
        birtarAPlontur.addAll(allarPlontur);

        //getFxFlowPane().getChildren().clear();
        //getFxFlowPane().getChildren().addAll(allarPlontur);
    }

    public ObservableList<PlantaSpjald> getBirtarAPlontur() {
        return birtarAPlontur;
    }

    public static void main(String[] args) {

    }
}
