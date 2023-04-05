/**
 * MinPlanta erfir frá Planta, sem er almenn útgáfa (planta sem við eigum ekki).
 * inniheldur lista af dagsetningum þegar plantan var vökvuð
 * <p>
 * MinPlanta ætti að vera controller fyrir fxml skrá fyrir plöntuspjald af plöntu sem við eigum.
 * Hefur þá handlera fyrir þegar ýtt er á takka á spjaldinu (vökva og fresta), og þegar ýtt er á spjaldið(upplýsingadíalogur).
 * <p>
 * á kannski að vera með observable list planaðarVökvanir, sem er listi af dagsetningum vökvana næstu þrjá mánuði?
 */

package vinnsla.plantmania;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.List;

public class MinPlanta extends Planta {
    private String nickName;
    private List<LocalDate> vokvanir;
    private StringProperty notesFraNotanda = new SimpleStringProperty();//hafa listener fyrir notes!
    private List<String> flokkar; //ath þetta aðeins
    private IntegerProperty medaltimiMilliVokvana = new SimpleIntegerProperty();
    private int thinnTimiMilliVokvana;
    private Planta planta;//var ekki málið að ef MinPlanta extends Planta þá inniheldur hún í raun sjálfkrafa Planta hlut?
    //væri kannski hægt að hafa setter sem í raun copyar öll planta gildin og setur gildi MinPlanta hlutarins eins?
    //en það væri kannski bara meira vesen


    //passa hvernig smiðurinn lítur út hér!
    public MinPlanta(Planta planta) {
        this.planta = planta;
        this.thinnTimiMilliVokvana = getAlmennurTimiMilliVokvana();
        this.nickName = "Barney";
        //this.nickName = getOllHeiti().get(1);

        //SIGURBJARGAR this.nickName = getOllHeiti().get(0);//ég sagði að latínan væri fyrst, svo almennt heiti, svo önnur
        //SIGURBJARGAR this.thinnTimiMilliVokvana = getAlmennurTimiMilliVokvana();
        //this.planta = planta; SIGURBJARGAR
    }

    public void breytaNickname(String nyttNafn) {//setNickname!
        nickName = nyttNafn;
    }

    public void baetaVidVokvun(LocalDate vokvun) {
        vokvanir.add(vokvun);
    }

    public void takaUtVokvun(LocalDate vokvun) {
        for (LocalDate l : vokvanir) {
            if (l.equals(vokvun)) {
                vokvanir.remove(vokvun);
            }
        }
    }

    public void breytaTimaMilliVokvana(int timi) {
        thinnTimiMilliVokvana = timi;
    }


    //getterar, setterar og tómur smiður fyrir json

    public MinPlanta() {

    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<LocalDate> getVokvanir() {
        return vokvanir;
    }

    public void setVokvanir(List<LocalDate> vokvanir) {
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

    public List<String> getFlokkar() {
        return flokkar;
    }

    public void setFlokkar(List<String> flokkar) {
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
        return thinnTimiMilliVokvana;
    }

    public void setThinnTimiMilliVokvana(int thinnTimiMilliVokvana) {
        this.thinnTimiMilliVokvana = thinnTimiMilliVokvana;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }
}
