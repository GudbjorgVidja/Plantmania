/**
 * Upplýsingar um notandann:
 * notendanafn
 * lykilorð
 * MinPlanta hlutir
 * Breytingar á stillingum (eða kannski frekar staða stillinga)
 * flokkar (ef hægt er að bæta við flokk)
 */
package vinnsla.plantmania;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Notandi {
    private StringProperty notendanafn = new SimpleStringProperty();
    private StringProperty lykilord = new SimpleStringProperty();

    private ObservableList<MinPlanta> minPlantasNotanda = FXCollections.observableArrayList();

    public Notandi(String notendanafn, String lykilord) {
        this.notendanafn.set(notendanafn);
        this.lykilord.set(lykilord);
    }

    public Notandi() {
    }

    public String getNotendanafn() {
        return notendanafn.get();
    }

    public StringProperty notendanafnProperty() {
        return notendanafn;
    }

    public void setNotendanafn(String notendanafn) {
        this.notendanafn.set(notendanafn);
    }

    public String getLykilord() {
        return lykilord.get();
    }

    public StringProperty lykilordProperty() {
        return lykilord;
    }

    public void setLykilord(String lykilord) {
        this.lykilord.set(lykilord);
    }

    public void baetaVidMinPlantasNotanda(MinPlanta mp) {
        minPlantasNotanda.add(mp);
    }

    public ObservableList<MinPlanta> getMinPlantasNotanda() {
        return minPlantasNotanda;
    }

    public String toString() {
        return "Notandi{" +
                "notendanafn=" + notendanafn.get() +
                ", lykilord=" + lykilord.get() +
                '}';
    }
}
