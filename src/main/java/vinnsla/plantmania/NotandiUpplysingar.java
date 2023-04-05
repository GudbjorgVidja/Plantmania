/**
 * Þær upplýsingar sem til eru sérstaklega um þennan notanda. Er beintengt við einhvern tiltekinn notanda með erfðum.
 * <p>
 * kannski safnar þetta upplýsingum um notanda sem þarf að skrifa í skrá? veit ekki
 */
package vinnsla.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NotandiUpplysingar extends Notandi {
    private ObservableList<MinPlanta> minPlantasNotanda = FXCollections.observableArrayList();

    public void baetaVidMinPlantasNotanda(MinPlanta mp) {
        minPlantasNotanda.add(mp);
    }

    public ObservableList<MinPlanta> getMinPlantasNotanda() {
        return minPlantasNotanda;
    }

}
