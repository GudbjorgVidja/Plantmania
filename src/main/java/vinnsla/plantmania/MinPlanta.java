/**
 * á kannski að vera með observable list planaðarVökvanir, sem er listi af dagsetningum vökvana næstu þrjá mánuði?
 * Það þarf líka að uppfærast þegar það líður dagur!
 */

package vinnsla.plantmania;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;


/**
 * MinPlanta erfir frá Planta. Planta inniheldur grunnupplýsingar, en MinPlanta er útgáfa sem notandi á, og MinPlanta
 * inniheldur og geymir upplýsingar um bara þann tiltekna hlut.
 * Klasinn er controller, og les inn viðmót úr skránni minplanta-view.fxml.
 * MinPlanta inniheldur m.a. skráðar vökvanir aftur í tímann, auk þess að reikna út og geyma dagsetningar fyrir planaðar
 * vökvanir.
 */
public class MinPlanta extends Planta {//@JsonDeserialize(using = MinPlantaDeserializer.class)
    private StringProperty nickName = new SimpleStringProperty();
    private ObservableList<LocalDate> vokvanir = FXCollections.observableArrayList();
    private StringProperty notesFraNotanda = new SimpleStringProperty();//hafa listener fyrir notes!
    private ObservableList<String> flokkar = FXCollections.observableArrayList(); //ath þetta aðeins
    private IntegerProperty medaltimiMilliVokvana = new SimpleIntegerProperty();//upphafsstilla?
    private IntegerProperty thinnTimiMilliVokvana = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> sidastaVokvun = new SimpleObjectProperty<>();
    private IntegerProperty naestaVokvun = new SimpleIntegerProperty();//setja hér niðurtalningu
    private ObservableList<LocalDate> planadarVokvanir = FXCollections.observableArrayList();

    //passa hvernig smiðurinn lítur út hér!
    public MinPlanta(Planta planta) {
        super(planta);
        this.nickName.set(planta.getOllHeiti().get(1));
        this.thinnTimiMilliVokvana.set(planta.getAlmennurTimiMilliVokvana());
        sidastaVokvunListener();
        medaltimiMilliVokvanaListener();

        System.out.println("MinPlanta(Planta planta) smidur");

        //naestaVokvun = (thinnTimiMilliVokvana);

        naestaVokvunRegla();
        reiknaPlanadarVokvanir();
    }

    /**
     * bara kallað á úr MinPlanta smið, svo gerist bara einu sinni fyrir hverja.
     * Reiknar planaðarvökvanir þrjá mánuði fram í tímann, upphafsstillir þær og setur listenera á naestaVokvun
     * og thinnTimiMilliVokvana svo planadarVokvanir uppfærist þegar annað gildið breytist
     */
    //TODO: skipta niður!
    public void reiknaPlanadarVokvanir() {
        //planadarVokvanirTestListener(); //prentar
        LocalDate date = LocalDate.now();//þetta gefur alltaf daginn í dag, gera meira abstract með .plusDays(naestaVokvun.get())
        LocalDate eftirThrjaManudi = date.plusMonths(3);
        for (LocalDate dagur = date; dagur.isBefore(eftirThrjaManudi); dagur = dagur.plusDays(thinnTimiMilliVokvana.get())) {
            planadarVokvanir.add(dagur);
        }

        naestaVokvun.addListener((obs, o, n) -> {
            LocalDate d = LocalDate.now().plusDays(naestaVokvun.get());
            planadarVokvanir.clear();
            for (LocalDate dagur = d; dagur.isBefore(eftirThrjaManudi); dagur = dagur.plusDays(thinnTimiMilliVokvana.get())) {
                planadarVokvanir.add(dagur);
            }
        });

        //reikna planaðar vökvanir aftur ef tími milli vökvana breytist!
        thinnTimiMilliVokvana.addListener((observable, oldValue, newValue) -> {
            LocalDate d = LocalDate.now().plusDays(naestaVokvun.get());
            planadarVokvanir.clear();
            for (LocalDate dagur = d; dagur.isBefore(eftirThrjaManudi); dagur = dagur.plusDays(thinnTimiMilliVokvana.get())) {
                planadarVokvanir.add(dagur);
            }
        });

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
     * ATH: tekur þetta bara þessa bindingu af, eða hefur það áhrif á fleiri!?!?! og verður naestaVokvun ekki
     * örugglega 0 þegar það var komin vökvun en hún tekin út aftur
     */
    public void naestaVokvunRegla() {
        sidastaVokvun.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                naestaVokvun.bind(thinnTimiMilliVokvana.subtract(ChronoUnit.DAYS.between(newValue, LocalDate.now())));
            } else {
                naestaVokvun.unbind();
                naestaVokvun.set(0);
            }
        });
    }


    /**
     * Bætir við vökvun. ekki hægt að bæta við vökvun fram í tímann eða fyrir árið 2022
     *
     * @param vokvun - LocalDate, dagsetning vökvunar
     */
    public void baetaVidVokvun(LocalDate vokvun) {
        if (!(vokvun.isAfter(LocalDate.now())) && !(vokvun.isBefore(LocalDate.of(2022, 1, 1)))) {
            vokvanir.add(vokvun);
            Collections.sort(vokvanir);
        }
    }

    /**
     * Athugar hvort plantan var vökvuð á gefnum degi, ef svo er er vökvunin tekin út
     *
     * @param vokvun - LocalDate, dagsetning vökvunar sem á að taka út
     */
    public void takaUtVokvun(LocalDate vokvun) {
        for (LocalDate l : vokvanir) {
            if (l.equals(vokvun)) {
                vokvanir.remove(vokvun);
                return;
            }
        }
    }

    //getterar, setterar og tómur smiður fyrir json
    public MinPlanta() {
        System.out.println("MinPlanta() smidur");
        sidastaVokvunListener();
        medaltimiMilliVokvanaListener();
        naestaVokvunRegla();
    }

    public String getNickName() {
        return nickName.get();
    }

    public StringProperty nickNameProperty() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName.set(nickName);
    }

    public ObservableList<LocalDate> getVokvanir() {
        return vokvanir;
    }

    public void setVokvanir(ObservableList<LocalDate> vokvanir) {
        this.vokvanir = vokvanir;
    }

    public String getNotesFraNotanda() {
        return notesFraNotanda.get();
    }

    public StringProperty notesFraNotandaProperty() {
        return notesFraNotanda;
    }

    public void setNotesFraNotanda(String notesFraNotanda) {
        this.notesFraNotanda.set(notesFraNotanda);
    }

    public ObservableList<String> getFlokkar() {
        return flokkar;
    }

    public void setFlokkar(ObservableList<String> flokkar) {
        this.flokkar = flokkar;
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

    public IntegerProperty getNaestaVokvun() {
        return naestaVokvun;
    }

    /**
     * passa að naestaVokvun sé ekki bundið, a bound value cannot be set
     *
     * @param i nýtt gildi
     */
    public void setNaestaVokvun(int i) {
        naestaVokvun.set(i);
    }

    public ObservableList<LocalDate> getPlanadarVokvanir() {
        return planadarVokvanir;
    }


    public void setPlanadarVokvanir(ObservableList<LocalDate> planadarVokvanir) {
        this.planadarVokvanir = planadarVokvanir;
    }

    public String toString() {
        return "MinPlanta{" +
                "planadarVokvanir: " + planadarVokvanir +
                ", nickName=" + nickName.get() +
                ", vokvanir=" + vokvanir.toString() +
                ", notesFraNotanda=" + notesFraNotanda.get() +
                ", flokkar=" + flokkar.toString() +
                ", medaltimiMilliVokvana=" + medaltimiMilliVokvana.get() +
                ", thinnTimiMilliVokvana=" + thinnTimiMilliVokvana.get() +
                ", naestaVokvun=" + naestaVokvun.get() +
                ", planta= " + super.toString() +
                '}';
    }

    public static void main(String[] args) {
        MinPlanta m = new MinPlanta();
        m.setThinnTimiMilliVokvana(10);
        m.baetaVidVokvun(LocalDate.now().minusDays(4));
        //m.naestaVokvun.set(3);
        m.reiknaPlanadarVokvanir();
        System.out.println(m.getNaestaVokvun().get());
        System.out.println(m.getPlanadarVokvanir());
    }
}
