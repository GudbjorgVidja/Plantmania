package vinnsla.plantmania;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import vidmot.plantmania.deserializers.NotendaupplysingarDeserializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//athuga hvernig það er greint á milli tveggja eins planta!!! það er nauðsynlegt!

/**
 * vinnsluklasi sem inniheldur upplýsingar um plöntur notanda, hvenær þær hafa verið vökvaðar og áætlaðar vökvanir
 */
@JsonDeserialize(using = NotendaupplysingarDeserializer.class)
public class Notendaupplysingar {
    private ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();//vaktanlegur listi yfir plöntur (MinPlanta hlutir) í eigu notanda
    private ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir = FXCollections.observableArrayList();//Vaktanlegur listi yfir allar vökvanir sem hafa verið gerðar fyrir allar plöntur, pör af plöntu og dagsetningu. þarf ekki endilega að vera í skrá? hægt að reikna út þegar forritið er opnað
    private ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir = FXCollections.observableArrayList();//Vaktanlegur listi yfir allar vökvanir sem eru áætlaðar fyrir allar plöntur, pör af plöntu og dagsetningu.ditto

    public Notendaupplysingar(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
    }

    public Notendaupplysingar() {
        //kallað á þetta fimm sinnum við upphaf keyrslu, af hverju? Vegna lesturs úr skrá og skrifa í skrá
        System.out.println("Notendaupplysingar smidur");
    }


    /**
     * setur listener á minarPlontur, þegar nýrri plöntu er bætt við er settur listener á hana með
     * aðferðinni vokvanirListener sem uppfærir fyrriVokvanir. Svo er fyrriVokvanir raðað
     * ATH: passa að það sé brugðist við þegar plöntu er eytt af listanum! Á eftir að útfæra allt tengt því tho
     */
    public void finnaFyrriOgSidariVokvanirListener() {
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

    //prófa að gera endurkvæmt fall til að bæta við plöntu í mínarPlontur
    public void addaPlanta(Planta planta) {
        minarPlontur.add(endurkvaemni(new MinPlanta(planta)));
    }

    /**
     * Ef sjálfgefið nickname fyrir plöntuna sem á að bæta við er nú þegar í notkun er kallað á endurkvæmt fall.
     * Það prófar að setja tölu fyrir aftan nafnið og finnur þannig næsta lausa nafn
     *
     * @param minPlanta - MinPlanta hlutur sem á að bæta við
     * @return - MinPlanta hlutur með nickname sem er ekki í notkun
     */
    private MinPlanta endurkvaemni(MinPlanta minPlanta) {
        for (MinPlanta min : minarPlontur) {
            if (min.getNickName().equals(minPlanta.getNickName())) {
                minPlanta = endurkvaemni(minPlanta, 1);
            }
        }
        return minPlanta;
    }

    /**
     * Endurkvæmt fall til að finna næsta lausa nickname fyrir plöntu með því að bæta tölu aftan við sjálfgefið
     * nicname. kallar endurkvæmt á sjálft sig þar til það finnur tölu sem er ekki í notkun nú þegar og skilar
     * MinPlanta hlutnum með því nicknami
     *
     * @param minPlanta - sjálfgefinn MinPlanta hlutur sem á að bæta við
     * @param gildi     - gildi sem á að prófa að skeyta aftan við nickname hjá minPlanta
     * @return kallar endurkvæmt á sjálft sig þar til það skilar MinPlanta plöntu með næsta lausa nickname
     */
    private MinPlanta endurkvaemni(MinPlanta minPlanta, int gildi) {
        String nyttNickname = minPlanta.getNickName() + gildi;
        for (MinPlanta min : minarPlontur) {
            if (min.getNickName().equals(nyttNickname)) {
                return endurkvaemni(minPlanta, gildi + 1);
            }
        }
        minPlanta.setNickName(nyttNickname);
        return minPlanta;
    }

    /**
     * drög að aðferð til að eyða plöntu. á eftir að skoða alla listenera
     *
     * @param minPlanta - MinPlanta hlutur sem á að eyða
     */
    public void eydaPlontu(MinPlanta minPlanta) {
        minarPlontur.remove(minPlanta);
    }

    public String toString() {
        return "Notendaupplysingar{" +
                "minarPlontur=" + minarPlontur +
                ", fyrriVokvanir=" + fyrriVokvanir +
                ", naestuVokvanir=" + naestuVokvanir +
                '}';
    }
}
