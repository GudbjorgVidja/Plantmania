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

public class MinPlanta extends Planta {
    private StringProperty nickName = new SimpleStringProperty();
    private ObservableList<LocalDate> vokvanir = FXCollections.observableArrayList();
    private StringProperty notesFraNotanda = new SimpleStringProperty();//hafa listener fyrir notes!
    private ObservableList<String> flokkar = FXCollections.observableArrayList(); //ath þetta aðeins
    private IntegerProperty medaltimiMilliVokvana = new SimpleIntegerProperty();
    private IntegerProperty thinnTimiMilliVokvana = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> sidastaVokvun = new SimpleObjectProperty<>();
    private Planta planta;//var ekki málið að ef MinPlanta extends Planta þá inniheldur hún í raun sjálfkrafa Planta hlut?
    //væri kannski hægt að hafa setter sem í raun copyar öll planta gildin og setur gildi MinPlanta hlutarins eins?
    //en það væri kannski bara meira vesen

    private IntegerProperty naestaVokvun = new SimpleIntegerProperty(2);//setja hér niðurtalningu

    private ObservableList<LocalDate> planadarVokvanir = FXCollections.observableArrayList();

    //passa hvernig smiðurinn lítur út hér!
    public MinPlanta(Planta planta) {
        this.nickName.set(planta.getOllHeiti().get(0));
        this.thinnTimiMilliVokvana.set(planta.getAlmennurTimiMilliVokvana());
        this.planta = planta;
        sidastaVokvunListener();
        medaltimiMilliVokvanaListener();

        naestaVokvun = (thinnTimiMilliVokvana);

        naestaVokvunRegla();

        reiknaPlanadarVokvanir();

    }

    private void reiknaPlanadarVokvanir() {
        LocalDate date = LocalDate.now();
        LocalDate eftirThrjaManudi = date.plusMonths(3);
        for (LocalDate dagur = date; dagur.isBefore(eftirThrjaManudi); dagur = dagur.plusDays(thinnTimiMilliVokvana.get())) {
            planadarVokvanir.add(dagur);
        }

        naestaVokvun.addListener((obs, o, n) -> {
            if (n.intValue() > o.intValue()) {
                for (LocalDate vDay : planadarVokvanir) {
                    vDay = vDay.plusDays(n.intValue() - o.intValue());

                }
                System.out.println(planadarVokvanir);
            } else if (n.intValue() < o.intValue()) {
                for (LocalDate vDay : planadarVokvanir) {
                    vDay = vDay.plusDays(o.intValue() - n.intValue());

                }
                System.out.println(planadarVokvanir);
            }
        });
        //date.plusDays(1);
    }

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
    public void medaltimiMilliVokvanaListener() {
        vokvanir.addListener((ListChangeListener<LocalDate>) (observable) -> {
            if (!vokvanir.isEmpty() && vokvanir.size() != 1) {
                int dagar = 0;
                for (int i = 0; i < vokvanir.size() - 1; i++) {
                    //dagar += vokvanir.get(i).until(vokvanir.get(i + 1)).getDays();
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

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
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

    public String toString() {
        return "MinPlanta{" +
                "vokvanir=" + vokvanir +
                ", medaltimiMilliVokvana=" + medaltimiMilliVokvana +
                ", thinnTimiMilliVokvana=" + thinnTimiMilliVokvana +
                ", planta=" + planta +
                '}';
    }

    /*public String toString() {
        return "MinPlanta{" +
                "nickName=" + nickName.get() +
                ", vokvanir=" + vokvanir.toString() +
                ", notesFraNotanda=" + notesFraNotanda.get() +
                ", flokkar=" + flokkar.toString() +
                ", medaltimiMilliVokvana=" + medaltimiMilliVokvana.get() +
                ", thinnTimiMilliVokvana=" + thinnTimiMilliVokvana.get() +
                ", planta=" + planta +
                '}';
    }*/

    public static void main(String[] args) {
        MinPlanta planta = new MinPlanta();
        planta.thinnTimiMilliVokvana.set(18);
        planta.baetaVidVokvun(LocalDate.of(2022, 3, 18));
        planta.baetaVidVokvun(LocalDate.of(2023, 3, 14));
        planta.baetaVidVokvun(LocalDate.of(2023, 3, 22));
        planta.baetaVidVokvun(LocalDate.of(2023, 3, 10));
        planta.baetaVidVokvun(LocalDate.of(2023, 3, 26));
        planta.baetaVidVokvun(LocalDate.of(2023, 3, 30));
        planta.baetaVidVokvun(LocalDate.of(2023, 4, 3));
        planta.baetaVidVokvun(LocalDate.of(2023, 2, 2));
        planta.baetaVidVokvun(LocalDate.of(2023, 2, 14));

        System.out.println("Vokvnanir i timarod:");
        for (LocalDate v : planta.getVokvanir()) {
            System.out.println(v);
        }
        LocalDate testDagur = LocalDate.now().minusMonths(1);

        ObservableList<LocalDate> testList = planta.getVokvanir().filtered(p -> p.getMonth() == testDagur.getMonth() && p.getYear() == testDagur.getYear());

        System.out.println("Vokvnanir i timarod f. testlist:");
        for (LocalDate v : testList) {
            System.out.println(v);
        }

        planta.baetaVidVokvun(LocalDate.of(2023, 3, 11));

        System.out.println("Vokvnanir i timarod f. testlist:");
        for (LocalDate v : testList) {
            System.out.println(v);
        }
        System.out.println("Medaltimi: " + planta.getMedaltimiMilliVokvana());
        System.out.println("Dagar i naestu vokvun: " + planta.getNaestaVokvun().get());

       /* System.out.println("Medaltimi: " + planta.getMedaltimiMilliVokvana());
        System.out.println("Sidasta vokvun: " + planta.getSidastaVokvun());
        planta.baetaVidVokvun(LocalDate.of(2023, 3, 6));*/

        System.out.println("Vokvnanir i timarod:");
        for (LocalDate v : planta.getVokvanir()) {
            System.out.println(v);
        }
        System.out.println("Medaltimi: " + planta.getMedaltimiMilliVokvana());
        System.out.println("Sidasta vokvun: " + planta.getSidastaVokvun());

        System.out.println(planta.getNaestaVokvun().get());

        planta.getVokvanir().clear();

        System.out.println("Sidasta vokvun: " + planta.getSidastaVokvun());

        System.out.println(planta.getNaestaVokvun().get());
        planta.baetaVidVokvun(LocalDate.of(2023, 4, 3));
        System.out.println("Sidasta vokvun: " + planta.getSidastaVokvun());

        System.out.println(planta.getNaestaVokvun().get());

    }
}
