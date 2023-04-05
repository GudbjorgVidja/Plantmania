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

public class Notandi {
    private StringProperty notendanafn = new SimpleStringProperty();
    private StringProperty lykilord = new SimpleStringProperty();
    private Notendaupplysingar notendaupplysingar = new Notendaupplysingar();//setja listener fyrir þetta/hafa sem ObjectProperty?

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

    public Notendaupplysingar getNotendaupplysingar() {
        return notendaupplysingar;
    }

    public void setNotendaupplysingar(Notendaupplysingar notendaupplysingar) {
        this.notendaupplysingar = notendaupplysingar;
    }

    public String toString() {
        return "Notandi{" +
                "notendanafn=" + notendanafn.get() +
                ", lykilord=" + lykilord.get() +
                ", notendaupplysingar=" + notendaupplysingar.toString() +
                '}';
    }
}
