package vinnsla.plantmania;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vidmot.plantmania.deserializers.NotandiDeserializer;

/**
 * Höfundur: Sigurbjörg Erla
 * vinnsluklasi sem inniheldur grunnupplýsingar um notanda
 * Getterar og setterar eru neðst
 */
@JsonDeserialize(using = NotandiDeserializer.class)
public class Notandi {
    private StringProperty notendanafn = new SimpleStringProperty();//vaktanlegt gildi fyrir notendanafn
    private StringProperty lykilord = new SimpleStringProperty();//vaktanlegt gildi fyrir lykilorð
    private ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();//vaktanlegur listi yfir plöntur (MinPlanta hlutir) í eigu notanda


    //smiðir
    public Notandi(String notendanafn, String lykilord) {
        this.notendanafn.set(notendanafn);
        this.lykilord.set(lykilord);
    }

    public Notandi() {
    }


    /**
     * drög að aðferð til að eyða plöntu. á eftir að skoða alla listenera. Ekki notað í augnablikinu
     *
     * @param minPlanta - MinPlanta hlutur sem á að eyða
     */
    public void eydaPlontu(MinPlanta minPlanta) {
        minarPlontur.remove(minPlanta);
    }

    /**
     * bætir plöntu við plöntur notanda sem MinPlanta hlutur. Kallar á endurkvæmt fall til að finna laust nickname
     *
     * @param planta - Planta, gerð plöntu sem á að bæta við
     */
    public void baetaVidPlontu(Planta planta) {
        minarPlontur.add(finnaNickname(new MinPlanta(planta)));
    }

    /**
     * Ef sjálfgefið nickname fyrir plöntuna sem á að bæta við er nú þegar í notkun er kallað á endurkvæmt fall.
     * Það prófar að setja tölu fyrir aftan nafnið og finnur þannig næsta lausa nafn
     *
     * @param minPlanta - MinPlanta hlutur sem á að bæta við
     * @return - MinPlanta hlutur með nickname sem er ekki í notkun
     */
    private MinPlanta finnaNickname(MinPlanta minPlanta) {
        for (MinPlanta min : minarPlontur) {
            if (min.getGaelunafn().equals(minPlanta.getGaelunafn())) {
                minPlanta = finnaNickname(minPlanta, 1);
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
    private MinPlanta finnaNickname(MinPlanta minPlanta, int gildi) {
        String nyttNickname = minPlanta.getGaelunafn() + gildi;
        for (MinPlanta min : minarPlontur) {
            if (min.getGaelunafn().equals(nyttNickname)) {
                return finnaNickname(minPlanta, gildi + 1);
            }
        }
        minPlanta.setGaelunafn(nyttNickname);
        return minPlanta;
    }


    //getterar og setterar
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
