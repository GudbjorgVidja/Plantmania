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
public class Notendaupplysingar {
    private ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();
    private ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir = FXCollections.observableArrayList();//þarf ekki endilega að vera í skrá? hægt að reikna út þegar forritið er opnað
    private ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir = FXCollections.observableArrayList();//ditto

    public Notendaupplysingar(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
        //finnaNaestuVokvanir();//held þetta sé óþarfi
    }

    public Notendaupplysingar() {
        //kallað á þetta fimm sinnum við upphaf keyrslu, af hverju?
        System.out.println("Notendaupplysingar smidur");
        //finnaNaestuVokvanir();
    }
    

    /**
     * setur listener á <minarPlontur>, þegar nýrri plöntu er bætt við er settur listener á hana með
     * aðferðinni vokvanirListener sem uppfærir <fyrriVokvanir>. Svo er fyrriVokvanir raðað
     * ATH: passa að það sé brugðist við þegar plöntu er eytt af listanum! Á eftir að útfæra allt tengt því tho
     */
    //TODO: Hér er settur listener á minarPlontur, og kallað á aðferð sem setur listener á stakan MinPlanta hlut sem bætt er við. Endurskrifa svo það þurfi ekki að setja líka listener á minarPlontur í aðferðinni finnaNaestuVokvanir!
    public void finnaFyrriVokvanir() {
        minarPlontur.addListener((ListChangeListener<MinPlanta>) (obs) -> {
            while (obs.next()) {
                if (obs.wasAdded()) {
                    for (int i = 0; i < obs.getAddedSize(); i++) {
                        vokvanirListener(obs.getAddedSubList().get(i));
                    }
                }
            }
            fyrriVokvanir.sort(Comparator.comparing((Pair::getValue)));
        });
    }

    /**
     * Setur listener á vökvanir fyrir staka MinPlanta til að uppfæra fyrriVokvanir listann
     *
     * @param minPlanta - MinPlanta hlutur, sú sem á að setja listener á vökvanir hjá
     */
    private void vokvanirListener(MinPlanta minPlanta) {
        minPlanta.getVokvanir().addListener((ListChangeListener<LocalDate>) (observable) -> {
            while (observable.next()) {
                if (observable.wasAdded()) {
                    for (int j = 0; j < observable.getAddedSize(); j++) {
                        fyrriVokvanir.add(new Pair<>(minPlanta, observable.getAddedSubList().get(j)));
                    }
                } else if (observable.wasRemoved()) {
                    List<Pair<MinPlanta, LocalDate>> eytt = new ArrayList<>();
                    for (int j = 0; j < observable.getRemovedSize(); j++) {
                        eytt.add(new Pair<>(minPlanta, observable.getRemoved().get(j)));
                    }
                    fyrriVokvanir.removeAll(eytt);
                }
            }
        });
    }


    /**
     * Finnur vökvanir þrjá mánuði fram í tímann.
     * Passa að hafa einhverja tilkynningu um að engar upplýsingar séu skráðar um fyrri vökvun
     * <p>
     * setur listener á minarPlontur listann, þegar nýrri plöntu er bætt við er vökvunum bætt við naestuVokvanir
     * (ath hvort það sé hægt að sleppa því?) og settur listener á planadarVokvanir í MinPlanta. Þegar planadarVokvanir
     * breytast er naestuVokvanir listinn uppfærður
     * ATH: Sameina aðeins með aðferðum til að finna síðustu vökvanir!!!
     */
    public void finnaNaestuVokvanir() {
        System.out.println("Notendaupplysingar.finnaNaestuVokvanir(): ");
        // naestuVokvanir.sort(Comparator.comparing((Pair::getValue)));
        minarPlontur.addListener((ListChangeListener<? super MinPlanta>) change -> {
            change.next();
            if (change.wasAdded()) {
                for (MinPlanta mp : change.getAddedSubList()) {
                    //ath hér
                    for (LocalDate date : mp.getPlanadarVokvanir()) {
                        naestuVokvanir.add(new Pair<>(mp, date));
                        //System.out.println("naestuVokvanir: " + naestuVokvanir);
                    }
                    mp.getPlanadarVokvanir().addListener((ListChangeListener<? super LocalDate>) breyting -> {
                        while (breyting.next()) {
                            if (breyting.wasAdded()) {
                                for (LocalDate dags : breyting.getAddedSubList()) {
                                    naestuVokvanir.add(new Pair<>(mp, dags));
                                }
                            }
                            if (breyting.wasRemoved()) {
                                List<Pair<MinPlanta, LocalDate>> eytt = new ArrayList<>();
                                for (LocalDate date : breyting.getRemoved()) {
                                    eytt.add(new Pair<>(mp, date));
                                }
                                naestuVokvanir.removeAll(eytt);
                            }
                        }
                    });
                }
                System.out.println(minarPlontur);
                System.out.println("naestuVokvanir: " + naestuVokvanir);
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
