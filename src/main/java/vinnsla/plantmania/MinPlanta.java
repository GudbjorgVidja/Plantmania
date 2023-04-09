/**
 * MinPlanta erfir frá Planta, sem er almenn útgáfa (planta sem við eigum ekki).
 * inniheldur lista af dagsetningum þegar plantan var vökvuð
 * <p>
 * MinPlanta ætti að vera controller fyrir fxml skrá fyrir plöntuspjald af plöntu sem við eigum.
 * Hefur þá handlera fyrir þegar ýtt er á takka á spjaldinu (vökva og fresta), og þegar ýtt er á spjaldið(upplýsingadíalogur).
 * <p>
 * á kannski að vera með observable list planaðarVökvanir, sem er listi af dagsetningum vökvana næstu þrjá mánuði?
 * <p>
 * bæta við integerProperty hlut nidurtalning, sem telur niður að næstu vökvun og uppfærist bara ef hún breytist. Held það
 * sé betra en að reikna það út í hvert skipti.
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

//@JsonDeserialize(using = MinPlantaDeserializer.class)
public class MinPlanta extends Planta {
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

    //TODO: ertu að nota þetta eða má eyða?
    private void planadarVokvanirTestListener() { //prentar bara, gerir ekkert þannig séð
        planadarVokvanir.addListener((ListChangeListener<? super LocalDate>) change -> {
            change.next();
            if (change.wasAdded()) System.out.print("added ");
            if (change.wasRemoved()) System.out.print("removed ");
            if (change.wasPermutated()) System.out.print("permutated ");
            if (change.wasReplaced()) System.out.print("replaced "); //ef bæði removed og added true
            if (change.wasUpdated()) System.out.print("updated ");
            System.out.println();
        });
    }

    /**
     * bara kallað á úr MinPlanta smið, svo gerist bara einu sinni fyrir hverja
     */
    public void reiknaPlanadarVokvanir() {
        //planadarVokvanirTestListener(); //prentar
        LocalDate date = LocalDate.now();//þetta gefur alltaf daginn í dag, gera meira abstract með .plusDays(naestaVokvun.get())
        LocalDate eftirThrjaManudi = date.plusMonths(3);
        for (LocalDate dagur = date; dagur.isBefore(eftirThrjaManudi); dagur = dagur.plusDays(thinnTimiMilliVokvana.get())) {
            planadarVokvanir.add(dagur);
        }

        //ef tími í næstu vökvun breytist þá er öllum dagsetningum hliðrað um muninn
        naestaVokvun.addListener((obs, o, n) -> {
            if (n.intValue() > o.intValue()) {
                for (int i = 0; i < planadarVokvanir.size(); i++) {
                    planadarVokvanir.set(i, planadarVokvanir.get(i).plusDays(n.intValue() - o.intValue()));
                }
                System.out.println("planadarVokvanir uppfaersla: " + planadarVokvanir);
            } else if (n.intValue() < o.intValue()) {
                for (int i = 0; i < planadarVokvanir.size(); i++) {
                    planadarVokvanir.set(i, planadarVokvanir.get(i).minusDays(n.intValue() - o.intValue()));
                }
                System.out.println("planadarVokvanir uppfaersla: " + planadarVokvanir);
            }
        });
    }

    //ath nafnið. Setur listener á vokvanir og uppfærir sidastaVokvun
    public void sidastaVokvunListener() {
        vokvanir.addListener((ListChangeListener<LocalDate>) (observable) -> {
            if (!vokvanir.isEmpty()) {
                sidastaVokvun.set(vokvanir.get(vokvanir.size() - 1));
            } else {
                sidastaVokvun.set(null);
            }
        });
    }

    //skoða með að þurfa ekki að reikna frá grunni í hvert skipti?
    //á ekki bara að setja teljara á frestun eða eitthvað? og þegar ýtt á vökva þá er teljarinn sóttur? -G
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
     * setur listener á sidastaVokvun, ef hún breytist þá er sett binding á naestaVokvun
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

    public void breytaNickname(String nyttNafn) {//setNickname!
        nickName.set(nyttNafn);
    }

    //ekki hægt að bæta við vövkun fram í tímann! og ekki hægt að bæta við fyrir 2022?
    public void baetaVidVokvun(LocalDate vokvun) {
        if (!(vokvun.isAfter(LocalDate.now())) && !(vokvun.isBefore(LocalDate.of(2022, 1, 1)))) {
            vokvanir.add(vokvun);
            Collections.sort(vokvanir);
        }
    }

    public void takaUtVokvun(LocalDate vokvun) {
        for (LocalDate l : vokvanir) {
            if (l.equals(vokvun)) {
                vokvanir.remove(vokvun);
                return;
            }
        }
    }

    public void breytaTimaMilliVokvana(int timi) {
        thinnTimiMilliVokvana.set(timi);
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
