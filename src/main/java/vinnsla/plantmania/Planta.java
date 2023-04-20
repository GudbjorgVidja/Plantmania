/**
 * mætti nefna þetta einhverPlanta ef það er þægilegra. Planta inniheldur upplýsingar um einhverja plöntu úr gagnagrunni,
 * flestum upplýsingum er ekki hægt að breyta.
 * MinPlanta extendar Planta, og inniheldur þá í raun allt í Planta og svo meira.
 * <p>
 * upplýsingar sem Planta á að innihalda:
 * Latneskt heiti
 * Almennt heiti
 * enum vatns-, ljós- og hitaþörf
 * mynd (eða öllu heldur slóð myndarinnar)
 * Stuttur texti með upplýsingum um plöntuna
 * <p>
 * <p>
 * Planta er allar grunnupplýsingarnar um plöntu.
 */
package vinnsla.plantmania;

import vinnsla.plantmania.enums.Eitrun;
import vinnsla.plantmania.enums.Ljosstyrkur;
import vinnsla.plantmania.enums.Uppruni;
import vinnsla.plantmania.enums.Vatnsthorf;

import java.util.List;

/**
 * Höfundur: Sigurbjörg Erla og Guðbjörg Viðja
 * Vinnsluklasinn Planta, inniheldur upplýsingar um ákveðna gerð af plöntu sem notandi getur svo bætt við sínar
 * plöntur sem MinPlanta og breytt henni
 */
public class Planta {
    private String fraediheiti;
    private String almenntHeiti;
    private List<String> ollHeiti;//inniheldur líka almenntHeiti
    private String myndaslod;
    private String texti;
    private Uppruni uppruni;
    private Ljosstyrkur ljosstyrkur;
    private Eitrun eitrun;
    private List<Integer> kjorhitastig;
    private Vatnsthorf vatnsthorf;
    private int almennurTimiMilliVokvana;
    private int ljosStundir;//fjöldi klukkustunda af sólarljósi á dag
    private List<String> einkenniPlontu;//t.d. ákveðin lauf... má sleppa

    public Planta(List<String> ollHeiti, String myndaslod, String texti, Uppruni uppruni, Ljosstyrkur ljosstyrkur, Eitrun eitrun, List<Integer> kjorhitastig, Vatnsthorf vatnsthorf, int almennurTimiMilliVokvana, int ljosStundir, List<String> einkenniPlontu) {
        this.fraediheiti = ollHeiti.get(0);
        this.almenntHeiti = ollHeiti.get(1);
        this.ollHeiti = ollHeiti;
        this.myndaslod = myndaslod;
        this.texti = texti;
        this.uppruni = uppruni;
        this.ljosstyrkur = ljosstyrkur;
        this.eitrun = eitrun;
        this.kjorhitastig = kjorhitastig;//hafa 3 gildi, min, kjör og max í fylki
        this.vatnsthorf = vatnsthorf;
        this.almennurTimiMilliVokvana = almennurTimiMilliVokvana;
        this.ljosStundir = ljosStundir;
        this.einkenniPlontu = einkenniPlontu;
    }

    public Planta(Planta planta) {
        this.fraediheiti = planta.fraediheiti; //eða hafa bara fraediheiti=ollHeiti.get(0)
        this.almenntHeiti = planta.almenntHeiti; // eða almenntHeiti=ollHeiti.get(1)
        this.ollHeiti = planta.ollHeiti;
        this.myndaslod = planta.myndaslod;
        this.texti = planta.texti;
        this.uppruni = planta.uppruni;
        this.ljosstyrkur = planta.ljosstyrkur;
        this.eitrun = planta.eitrun;
        this.kjorhitastig = planta.kjorhitastig;//hafa 3 gildi, min, kjör og max í fylki
        this.vatnsthorf = planta.vatnsthorf;
        this.almennurTimiMilliVokvana = planta.almennurTimiMilliVokvana;
        this.ljosStundir = planta.ljosStundir;
        this.einkenniPlontu = planta.einkenniPlontu;
    }

    public Planta() {

    }

    public String getFraediheiti() {
        return fraediheiti;
    }

    public void setFraediheiti(String fraediheiti) {
        this.fraediheiti = fraediheiti;
    }

    public String getAlmenntHeiti() {
        return almenntHeiti;
    }

    public void setAlmenntHeiti(String almenntHeiti) {
        this.almenntHeiti = almenntHeiti;
    }

    public List<String> getOllHeiti() {
        return ollHeiti;
    }

    public void setOllHeiti(List<String> ollHeiti) {
        this.ollHeiti = ollHeiti;
    }

    public String getMyndaslod() {
        return myndaslod;
    }

    public void setMyndaslod(String myndaslod) {
        this.myndaslod = myndaslod;
    }

    public String getTexti() {
        return texti;
    }

    public void setTexti(String texti) {
        this.texti = texti;
    }

    public Uppruni getUppruni() {
        return uppruni;
    }

    public void setUppruni(Uppruni uppruni) {
        this.uppruni = uppruni;
    }

    public Ljosstyrkur getLjosstyrkur() {
        return ljosstyrkur;
    }

    public void setLjosstyrkur(Ljosstyrkur ljosstyrkur) {
        this.ljosstyrkur = ljosstyrkur;
    }

    public Eitrun getEitrun() {
        return eitrun;
    }

    public void setEitrun(Eitrun eitrun) {
        this.eitrun = eitrun;
    }

    public List<Integer> getKjorhitastig() {
        return kjorhitastig;
    }

    public void setKjorhitastig(List<Integer> kjorhitastig) {
        this.kjorhitastig = kjorhitastig;
    }

    public Vatnsthorf getVatnsthorf() {
        return vatnsthorf;
    }

    public void setVatnsthorf(Vatnsthorf vatnsthorf) {
        this.vatnsthorf = vatnsthorf;
    }

    public int getAlmennurTimiMilliVokvana() {
        return almennurTimiMilliVokvana;
    }

    public void setAlmennurTimiMilliVokvana(int almennurTimiMilliVokvana) {
        this.almennurTimiMilliVokvana = almennurTimiMilliVokvana;
    }

    public int getLjosStundir() {
        return ljosStundir;
    }

    public void setLjosStundir(int ljosStundir) {
        this.ljosStundir = ljosStundir;
    }

    public List<String> getEinkenniPlontu() {
        return einkenniPlontu;
    }

    public void setEinkenniPlontu(List<String> einkenniPlontu) {
        this.einkenniPlontu = einkenniPlontu;
    }

    public String toString() {
        return "Planta{" +
                "fraediheiti='" + fraediheiti + '\'' +
                ", almenntHeiti='" + almenntHeiti + '\'' +
                ", ollHeiti=" + ollHeiti +
                ", myndaslod='" + myndaslod + '\'' +
                ", texti='" + texti + '\'' +
                ", uppruni=" + uppruni +
                ", ljosstyrkur=" + ljosstyrkur +
                ", eitrun=" + eitrun +
                ", kjorhitastig=" + kjorhitastig +
                ", vatnsthorf=" + vatnsthorf +
                ", almennurTimiMilliVokvana=" + almennurTimiMilliVokvana +
                ", ljosStundir=" + ljosStundir +
                ", einkenniPlontu=" + einkenniPlontu +
                '}';
    }
}
