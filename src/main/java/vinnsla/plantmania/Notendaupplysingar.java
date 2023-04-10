package vinnsla.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//athuga hvernig það er greint á milli tveggja eins planta!!! það er nauðsynlegt!
//@JsonDeserialize(using = NotendaupplysingarDeserializer.class)

/**
 * vinnsluklasi sem inniheldur upplýsingar um plöntur notanda, hvenær þær hafa verið vökvaðar og áætlaðar vökvanir
 */
public class Notendaupplysingar {
    private ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();
    private ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir = FXCollections.observableArrayList();//þarf ekki endilega að vera í skrá? hægt að reikna út þegar forritið er opnað
    private ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir = FXCollections.observableArrayList();//ditto

    public Notendaupplysingar(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
    }

    public Notendaupplysingar() {
        //kallað á þetta fimm sinnum við upphaf keyrslu, af hverju?
        System.out.println("Notendaupplysingar smidur");
    }


    /**
     * setur listener á minarPlontur, þegar nýrri plöntu er bætt við er settur listener á hana með
     * aðferðinni vokvanirListener sem uppfærir fyrriVokvanir. Svo er fyrriVokvanir raðað
     * ATH: passa að það sé brugðist við þegar plöntu er eytt af listanum! Á eftir að útfæra allt tengt því tho
     */
    public void finnaFyrriVokvanir() {
        minarPlontur.addListener((ListChangeListener<MinPlanta>) (obs) -> {
            while (obs.next()) {
                if (obs.wasAdded()) {
                    for (MinPlanta minPlanta : obs.getAddedSubList()) {
                        for (LocalDate date : minPlanta.getPlanadarVokvanir()) {
                            naestuVokvanir.add(new Pair<>(minPlanta, date));
                        }
                        listaListener(minPlanta, fyrriVokvanir, minPlanta.getVokvanir());
                        listaListener(minPlanta, naestuVokvanir, minPlanta.getPlanadarVokvanir());
                    }
                }
            }
            fyrriVokvanir.sort(Comparator.comparing((Pair::getValue)));
            // naestuVokvanir.sort(Comparator.comparing((Pair::getValue)));
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
    private void listaListener(MinPlanta minPlanta, ObservableList<Pair<MinPlanta, LocalDate>> vokvanir, ObservableList<LocalDate> dagsetningar) {
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

    public ObservableList<MinPlanta> getMinarPlontur() {
        return minarPlontur;
    }

    public void setMinarPlontur(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
    }

    public ObservableList<Pair<MinPlanta, LocalDate>> getFyrriVokvanir() {
        return fyrriVokvanir;
    }

    public void setFyrriVokvanir(ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir) {
        this.fyrriVokvanir = fyrriVokvanir;
    }

    public ObservableList<Pair<MinPlanta, LocalDate>> getNaestuVokvanir() {
        return naestuVokvanir;
    }

    public void setNaestuVokvanir(ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir) {
        this.naestuVokvanir = naestuVokvanir;
    }

    /**
     * bætir við plöntu af gerðinni planta við plöntur í eigu notanda. Passar að engar tvær plöntur hafi sama
     * nickname
     * ATH: bætir alltaf við 1 fyrir aftan nafnið ef það er nú þegar til en telur ekki, svo það kemur
     * 1, 11, 111 en ekki 1, 2, 3
     *
     * @param planta - Planta af gerðinni sem notandi vill
     */
    public void baetaVidPlontu(Planta planta) {
        System.out.println("Notendaupplysingar.baetaVidPlontu(Planta)");
        MinPlanta nyPlanta = new MinPlanta(planta);
        boolean ekkertEins = false;
        for (int i = 1; !ekkertEins; i++) {
            boolean einsPlanta = false;
            for (MinPlanta minPlanta : minarPlontur) {
                if (minPlanta.getNickName().equals(nyPlanta.getNickName())) {
                    nyPlanta.setNickName(minPlanta.getNickName() + i);
                    einsPlanta = true;
                }
            }
            if (!einsPlanta) {
                ekkertEins = true;
            }
        }
        minarPlontur.add(nyPlanta);
    }

    public String toString() {
        return "Notendaupplysingar{" +
                "minarPlontur=" + minarPlontur +
                ", fyrriVokvanir=" + fyrriVokvanir +
                ", naestuVokvanir=" + naestuVokvanir +
                '}';
    }
}
