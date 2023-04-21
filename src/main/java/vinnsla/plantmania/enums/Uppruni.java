/**
 * enum fyrir uppruna plöntu.
 */
package vinnsla.plantmania.enums;


public enum Uppruni {
    EYDIMORK("eyðimörk"),
    REGNSKOGUR("regnskógur"),
    SLETTUR("sléttur"),
    SALTVATN("saltvatn"),
    FERSKVATN("ferskvatn"),
    FJALLLENDI("fjalllendi"),
    GRASLENDI("graslendi"),
    SKOGLENDI("skóglendi"),
    HITABELTI("hitabelti"),
    VOTLENDI("votlendi");


    private String stadur;//strengur með upprunastað

    Uppruni(String stadur) {
        this.stadur = stadur;
    }

    public String getStadur() {
        return stadur;
    }
}
