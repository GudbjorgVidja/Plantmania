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

import java.util.List;

public class Planta {
    private String latnesktNafn;
    private String almenntNafn; //má breyta í mínPlanta, annars ekki
    private List<String> ollHeiti;//inniheldur líka almenntNafn
    private String myndaslod;
    private String texti;
    private Uppruni uppruni;
    private Ljosstyrkur ljosstyrkur;
    private Eitrun eitrun;
    private int kjorhitastig;
    private Vatnsthorf vatnsthorf;
    private int almennurTimiMilliVokvana;
    private int ljosStundir;//fjöldi klukkustunda af sólarljósi á dag
    private List<String> einkenniPlontu;//t.d. ákveðin lauf... má sleppa

    public Planta(String latnesktNafn, String almenntNafn, List<String> ollHeiti, String myndaslod, String texti, Uppruni uppruni, Ljosstyrkur ljosstyrkur, Eitrun eitrun, int kjorhitastig, Vatnsthorf vatnsthorf, int almennurTimiMilliVokvana, int ljosStundir, List<String> einkenniPlontu) {
        this.latnesktNafn = latnesktNafn; //eða hafa bara latnesktNafn=ollHeiti.get(0)
        this.almenntNafn = almenntNafn; // eða almenntNafn=ollHeiti.get(1)
        this.ollHeiti = ollHeiti;
        this.myndaslod = myndaslod;
        this.texti = texti;
        this.uppruni = uppruni;
        this.ljosstyrkur = ljosstyrkur;
        this.eitrun = eitrun;
        this.kjorhitastig = kjorhitastig;
        this.vatnsthorf = vatnsthorf;
        this.almennurTimiMilliVokvana = almennurTimiMilliVokvana;
        this.ljosStundir = ljosStundir;
        this.einkenniPlontu = einkenniPlontu;
    }

    public Planta() {

    }

    public String getLatnesktNafn() {
        return latnesktNafn;
    }

    public void setLatnesktNafn(String latnesktNafn) {
        this.latnesktNafn = latnesktNafn;
    }

    public String getAlmenntNafn() {
        return almenntNafn;
    }

    public void setAlmenntNafn(String almenntNafn) {
        this.almenntNafn = almenntNafn;
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

    public int getKjorhitastig() {
        return kjorhitastig;
    }

    public void setKjorhitastig(int kjorhitastig) {
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
                "latnesktNafn='" + latnesktNafn + '\'' +
                ", almenntNafn='" + almenntNafn + '\'' +
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
