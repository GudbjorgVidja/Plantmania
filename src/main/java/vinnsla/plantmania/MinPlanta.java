package vinnsla.plantmania;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import vidmot.plantmania.deserializers.MinPlantaDeserializer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;


/**
 * Höfundur: Sigurbjörg Erla og Guðbjörgu Viðju
 * MinPlanta erfir frá Planta. Planta inniheldur grunnupplýsingar, en MinPlanta er útgáfa sem notandi á, og MinPlanta
 * inniheldur og geymir upplýsingar um bara þann tiltekna hlut.
 * Klasinn er controller, og les inn viðmót úr skránni minplanta-view.fxml.
 * MinPlanta inniheldur m.a. skráðar vökvanir aftur í tímann, auk þess að reikna út og geyma dagsetningar fyrir planaðar
 * vökvanir. Allar tilviksbreytur hér eru vaktanlegar
 */
@JsonDeserialize(using = MinPlantaDeserializer.class)
public class MinPlanta extends Planta {
    private StringProperty gaelunafn = new SimpleStringProperty();//gælunafn plöntunnar, vaktanlegt
    private ObservableList<LocalDate> vokvanir = FXCollections.observableArrayList();//vaktanlegur listi yfir allar dagsetningar sem plantan hefur verið vökvuð á
    private StringProperty athugasemdir = new SimpleStringProperty("");//athugasemdir sem notandi skrifar niður fyrir plöntuna, vaktanlegt
    private IntegerProperty medaltimiMilliVokvana = new SimpleIntegerProperty();//vaktanlegt gildi fyrir meðaltíma milli vökvana
    private IntegerProperty thinnTimiMilliVokvana = new SimpleIntegerProperty();//vaktanlegt gildi fyrir tíma sem notandi vill hafa milli vökvana
    private ObjectProperty<LocalDate> sidastaVokvun = new SimpleObjectProperty<>();//vaktanlegt gildi fyrir dagsetningu síðustu vökvunar
    private IntegerProperty naestaVokvun = new SimpleIntegerProperty();//vaktanlegt gildi fyrir fjölda daga í næstu vökvun
    private ObservableList<LocalDate> planadarVokvanir = FXCollections.observableArrayList();//vaktanlegur listi yfir áætlaðar vökvanir fyrir plöntuna næstu þrjá mánuðina


    public MinPlanta(Planta planta) {
        super(planta);
        this.gaelunafn.set(planta.getOllHeiti().get(1));
        this.thinnTimiMilliVokvana.set(planta.getAlmennurTimiMilliVokvana());
        sidastaVokvunListener();
        medaltimiMilliVokvanaListener();

        naestaVokvunRegla();
        reiknaPlanadarVokvanir();
    }

    public MinPlanta() {
        sidastaVokvunListener();
        medaltimiMilliVokvanaListener();
        naestaVokvunRegla();
    }

    /**
     * bara kallað á úr MinPlanta smið, svo gerist bara einu sinni fyrir hverja.
     * Reiknar planaðarvökvanir þrjá mánuði fram í tímann, upphafsstillir þær og setur listenera á naestaVokvun
     * og thinnTimiMilliVokvana svo planadarVokvanir uppfærist þegar annað gildið breytist
     */
    public void reiknaPlanadarVokvanir() {
        uppfaeraPlanadarVokvanir();
        setjaPlanadarVokvanirListenera();
    }

    public void setjaPlanadarVokvanirListenera() {
        naestaVokvun.addListener((obs, o, n) -> {
            uppfaeraPlanadarVokvanir();
        });

        thinnTimiMilliVokvana.addListener((observable, oldValue, newValue) -> {
            uppfaeraPlanadarVokvanir();
        });
    }

    /**
     * Setur planadarVokvanir fyrir plöntuna með réttu millibili ákveðið langt fram í tímann.
     * Tæmir planadarVokvanir svo það reiknast alltaf frá grunni
     */
    private void uppfaeraPlanadarVokvanir() {
        LocalDate dagur = LocalDate.now().plusDays(naestaVokvun.get());
        LocalDate lengdAaetlunar = LocalDate.now().plusMonths(3);

        planadarVokvanir.clear();
        for (LocalDate d = dagur; d.isBefore(lengdAaetlunar); d = d.plusDays(thinnTimiMilliVokvana.get())) {
            planadarVokvanir.add(d);
        }
    }

    /**
     * ath nafnið. Setur listener á vokvanir og uppfærir sidastaVokvun
     */
    public void sidastaVokvunListener() {
        vokvanir.addListener((ListChangeListener<LocalDate>) (observable) -> {
            if (!vokvanir.isEmpty()) {
                sidastaVokvun.set(vokvanir.get(vokvanir.size() - 1));
            } else {
                sidastaVokvun.set(null);
            }
        });
    }

    /**
     * setur listener á vokvanir til að uppfæra medaltimiMilliVokvana þegar vökvun er tekin út eða bætt við
     * S: skoða með að þurfa ekki að reikna frá grunni í hvert skipti?
     * G: á ekki bara að setja teljara á frestun eða eitthvað? og þegar ýtt á vökva þá er teljarinn sóttur?
     */
    public void medaltimiMilliVokvanaListener() {
        vokvanir.addListener((ListChangeListener<LocalDate>) (observable) -> {
            if (!vokvanir.isEmpty() && vokvanir.size() != 1) {
                int dagar = 0;
                for (int i = 0; i < vokvanir.size() - 1; i++) {
                    dagar += ChronoUnit.DAYS.between(vokvanir.get(i), vokvanir.get(i + 1));
                }
                medaltimiMilliVokvana.set(dagar / (vokvanir.size() - 1));
            } else {
                medaltimiMilliVokvana.set(0);
            }
        });
    }


    /**
     * ath hvað gerist milli daga (localDate.now() breytist!)
     * setur listener á sidastaVokvun, ef hún breytist þá er sett binding á naestaVokvun, eða bindingin tekin af og
     * naestaVokvun sett sem 0
     * ATH: tekur þetta bara þessa bindingu af, eða hefur það áhrif á fleiri!?!?!
     */
    public void naestaVokvunRegla() {
        sidastaVokvun.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {//kannski bara ef newValue != null && oldValue == null, til að endurtaka ekki að óþörfu?
                naestaVokvun.bind(thinnTimiMilliVokvana.subtract(ChronoUnit.DAYS.between(newValue, LocalDate.now())));
            } else {
                naestaVokvun.unbind();
                naestaVokvun.set(0);
            }
        });
    }

    /**
     * ath: það þarf bara að nota þetta í PlantController hladaUpplysingum, en athuga hvort það var komið
     * eitthvað sambærilegt. Ég fatta ekki alveg af hverju þetta þarf þegar það er búið að vista upplýsingar
     * en annars ekki. uppfærir naestaVokvun þegar thinnTimiMilliVokvana breytist
     */
    public void breytingAThinnTimiMilliVokvanaRegla() {
        thinnTimiMilliVokvanaProperty().addListener((observable, oldValue, newValue) -> {
            naestaVokvunProperty().unbind();
            naestaVokvunRegla();
        });
    }


    /**
     * Bætir við vökvun. ekki hægt að bæta við vökvun fram í tímann eða fyrir árið 2022
     *
     * @param vokvun - LocalDate, dagsetning vökvunar
     */
    public void baetaVidVokvun(LocalDate vokvun) {
        if (!(vokvun.isAfter(LocalDate.now())) && !(vokvun.isBefore(LocalDate.of(2022, 1, 1)))) {
            if (!vokvanir.contains(vokvun)) {
                vokvanir.add(vokvun);
                Collections.sort(vokvanir);
            }
        }
    }

    /**
     * Fjarlægir vökvun á gefinni dagsetningu úr vokvanir listanum, ef plantan var vökvuð á þeim degi
     *
     * @param vokvun - LocalDate, dagsetning vökvunar sem á að taka út
     */
    public void takaUtVokvun(LocalDate vokvun) {
        vokvanir.remove(vokvun);
    }

    //getterar og setterar
    public String getGaelunafn() {
        return gaelunafn.get();
    }

    public StringProperty gaelunafnProperty() {
        return gaelunafn;
    }

    public void setGaelunafn(String gaelunafn) {
        this.gaelunafn.set(gaelunafn);
    }

    public ObservableList<LocalDate> getVokvanir() {
        return vokvanir;
    }

    public void setVokvanir(ObservableList<LocalDate> vokvanir) {
        this.vokvanir = vokvanir;
    }

    public String getAthugasemdir() {
        return athugasemdir.get();
    }

    public StringProperty athugasemdirProperty() {
        return athugasemdir;
    }

    public void setAthugasemdir(String athugasemdir) {
        this.athugasemdir.set(athugasemdir);
    }

    public int getMedaltimiMilliVokvana() {
        return medaltimiMilliVokvana.get();
    }

    public IntegerProperty medaltimiMilliVokvanaProperty() {
        return medaltimiMilliVokvana;
    }

    public void setMedaltimiMilliVokvana(int medaltimiMilliVokvana) {
        this.medaltimiMilliVokvana.set(medaltimiMilliVokvana);
    }

    public int getThinnTimiMilliVokvana() {
        return thinnTimiMilliVokvana.get();
    }

    public IntegerProperty thinnTimiMilliVokvanaProperty() {
        return thinnTimiMilliVokvana;
    }

    public void setThinnTimiMilliVokvana(int thinnTimiMilliVokvana) {
        this.thinnTimiMilliVokvana.set(thinnTimiMilliVokvana);
    }

    public LocalDate getSidastaVokvun() {
        return sidastaVokvun.get();
    }

    public ObjectProperty<LocalDate> sidastaVokvunProperty() {
        return sidastaVokvun;
    }

    public void setSidastaVokvun(LocalDate sidastaVokvun) {
        this.sidastaVokvun.set(sidastaVokvun);
    }

    public int getNaestaVokvun() {
        return naestaVokvun.get();
    }

    public void setNaestaVokvun(int i) {
        naestaVokvun.set(i);
    }

    public ObservableList<LocalDate> getPlanadarVokvanir() {
        return planadarVokvanir;
    }


    public void setPlanadarVokvanir(ObservableList<LocalDate> planadarVokvanir) {
        this.planadarVokvanir = planadarVokvanir;
    }

    public IntegerProperty naestaVokvunProperty() {
        return naestaVokvun;
    }

    public String toString() {
        return "MinPlanta{" +
                "gaelunafn=" + gaelunafn.get() +
                ", vokvanir=" + vokvanir +
                ", athugasemdir=" + athugasemdir.get() +
                ", medaltimiMilliVokvana=" + medaltimiMilliVokvana.get() +
                ", thinnTimiMilliVokvana=" + thinnTimiMilliVokvana.get() +
                ", sidastaVokvun=" + sidastaVokvun.get() +
                ", naestaVokvun=" + naestaVokvun.get() +
                ", planadarVokvanir=" + planadarVokvanir +
                ", planta= " + super.toString() +
                '}';
    }
}
