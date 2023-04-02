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
 * plöntuspjald er Anchorpane, sem inniheldur fleiri hluti á við
 */
package vinnsla.plantmania;

public class Planta {
    private String latnesktNafn;
    private String almenntNafn; //má breyta í mínPlanta, annars ekki
    private String myndaslod;
    private String texti;


    public static void main(String[] args) {

    }
}
