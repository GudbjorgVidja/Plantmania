package vinnsla.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Notendaupplysingar {
    private ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();

    public Notendaupplysingar(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
    }

    public Notendaupplysingar() {
    }

    public ObservableList<MinPlanta> getMinarPlontur() {
        return minarPlontur;
    }

    public void setMinarPlontur(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
    }

    public void baetaVidPlontu(Planta planta) {
        minarPlontur.add(new MinPlanta(planta));
        MinPlanta ny = new MinPlanta();
        ny.setAlmenntNafn(planta.getAlmenntNafn());
        minarPlontur.add(ny);
    }

    public String toString() {
        return minarPlontur.toString();
    }
}
