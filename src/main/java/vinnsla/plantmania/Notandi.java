/**
 * Upplýsingar um notandann:
 * notendanafn
 * lykilorð
 * MinPlanta hlutir
 * Breytingar á stillingum (eða kannski frekar staða stillinga)
 * flokkar (ef hægt er að bæta við flokk)
 */
package vinnsla.plantmania;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vidmot.plantmania.deserializers.NotandiDeserializer;

/**
 * vinnsluklasi sem inniheldur grunnupplýsingar um notanda
 */
@JsonDeserialize(using = NotandiDeserializer.class)
public class Notandi {
    private StringProperty notendanafn = new SimpleStringProperty();
    private StringProperty lykilord = new SimpleStringProperty();
    private ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();//vaktanlegur listi yfir plöntur (MinPlanta hlutir) í eigu notanda


    public Notandi(String notendanafn, String lykilord) {
        System.out.println("Notandi(String, String) smidur");
        this.notendanafn.set(notendanafn);
        this.lykilord.set(lykilord);
    }

    public Notandi() {
        System.out.println("Notandi() smidur");
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


    /**
     * drög að aðferð til að eyða plöntu. á eftir að skoða alla listenera
     *
     * @param minPlanta - MinPlanta hlutur sem á að eyða
     */
    public void eydaPlontu(MinPlanta minPlanta) {
        minarPlontur.remove(minPlanta);
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

    public ObservableList<MinPlanta> getMinarPlontur() {
        return minarPlontur;
    }

    public void setMinarPlontur(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
    }

    public String toString() {
        return "Notandi{" +
                "notendanafn=" + notendanafn.get() +
                ", lykilord=" + lykilord.get() +
                ", minarPlontur=" + minarPlontur +
                '}';
    }
}
