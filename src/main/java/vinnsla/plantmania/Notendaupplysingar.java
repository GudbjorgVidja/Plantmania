package vinnsla.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Höfundur: Sigurbjörg Erla
 * vinnsluklasi sem inniheldur upplýsingar um plöntur notanda, hvenær þær hafa verið vökvaðar og áætlaðar vökvanir
 */
public class Notendaupplysingar {
    private ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir = FXCollections.observableArrayList();//Vaktanlegur listi yfir allar vökvanir sem hafa verið gerðar fyrir allar plöntur, pör af plöntu og dagsetningu. þarf ekki endilega að vera í skrá? hægt að reikna út þegar forritið er opnað
    private ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir = FXCollections.observableArrayList();//Vaktanlegur listi yfir allar vökvanir sem eru áætlaðar fyrir allar plöntur, pör af plöntu og dagsetningu.ditto

    public Notendaupplysingar() {

    }

    /**
     * Vökvunum bætt við listana naestuVokvanir og fyrriVokvanir fyrir allar vökvanir fyrir allar plöntur notanda.
     * Listenerum bætt við til að uppfæra naestuVokvanir og fyrriVokvanir þegar vökvanir plöntu breytast
     *
     * @param minarPlontur - Vaktanlegur listi af MinPlanta, inniheldur allar plöntur notanda
     */
    public Notendaupplysingar(ObservableList<MinPlanta> minarPlontur) {
        for (MinPlanta m : minarPlontur) {
            for (LocalDate date : m.getPlanadarVokvanir()) {
                naestuVokvanir.add(new Pair<>(m, date));
            }
            for (LocalDate dags : m.getVokvanir()) {
                fyrriVokvanir.add(new Pair<>(m, dags));
            }
            vokvanalistiListener(m, fyrriVokvanir, m.getVokvanir());
            vokvanalistiListener(m, naestuVokvanir, m.getPlanadarVokvanir());
        }
    }

    /**
     * setur listener á minarPlontur, þegar nýrri plöntu er bætt við er settur listener á hana með
     * aðferðinni vokvanirListener sem uppfærir fyrriVokvanir. Svo er fyrriVokvanir raðað
     * ATH: passa að það sé brugðist við þegar plöntu er eytt af listanum! Á eftir að útfæra allt tengt því tho
     */
    public void finnaFyrriOgSidariVokvanirListener(ObservableList<MinPlanta> minarPlontur) {
        minarPlontur.addListener((ListChangeListener<MinPlanta>) (obs) -> {
            while (obs.next()) {
                if (obs.wasAdded()) {
                    for (MinPlanta minPlanta : obs.getAddedSubList()) {
                        for (LocalDate date : minPlanta.getPlanadarVokvanir()) {
                            naestuVokvanir.add(new Pair<>(minPlanta, date));
                        }
                        vokvanalistiListener(minPlanta, fyrriVokvanir, minPlanta.getVokvanir());
                        vokvanalistiListener(minPlanta, naestuVokvanir, minPlanta.getPlanadarVokvanir());
                    }
                }
            }
            fyrriVokvanir.sort(Comparator.comparing((Pair::getValue)));
        });
    }

    /**
     * Setur listener á dagsetningar, sem er observableList af dagsetningum (getur verið vokvanir eða
     * planadarVokvanir í MinPlanta), fyrir gefna plöntu
     *
     * @param minPlanta    - MinPlanta, sú sem inniheldur listann sem á að vakta
     * @param vokvanir     - ObservableList af pörum af MinPlanta og LocalDate, annað hvort naestuVokvanir eða fyrriVokvanir
     *                     tilviksbreyturnar í Notendaupplysingar (hér)
     * @param dagsetningar - ObservableList af LocalDate vökvunardagsetningum fyrir staka plöntu sem á að vakta
     */
    public void vokvanalistiListener(MinPlanta minPlanta, ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, ObservableList<LocalDate> dagsetningar) {
        dagsetningar.addListener((ListChangeListener<? super LocalDate>) breyting -> {
            while (breyting.next()) {
                if (breyting.wasAdded()) {
                    for (LocalDate dags : breyting.getAddedSubList()) {
                        vokvanir.add(new Pair<>(minPlanta, dags));
                    }
                }
                if (breyting.wasRemoved()) {
                    List<Pair<MinPlanta, LocalDate>> eytt = new ArrayList<>();
                    for (LocalDate date : breyting.getRemoved()) {
                        eytt.add(new Pair<>(minPlanta, date));
                    }
                    vokvanir.removeAll(eytt);
                }
            }
        });
    }

    public ObservableList<Pair<MinPlanta, LocalDate>> getFyrriVokvanir() {
        return fyrriVokvanir;
    }

    public ObservableList<Pair<MinPlanta, LocalDate>> getNaestuVokvanir() {
        return naestuVokvanir;
    }

    public String toString() {
        return "Notendaupplysingar{" +
                ", fyrriVokvanir=" + fyrriVokvanir +
                ", naestuVokvanir=" + naestuVokvanir +
                '}';
    }
}
