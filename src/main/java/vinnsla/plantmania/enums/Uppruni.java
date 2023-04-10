/**
 * enum fyrir uppruna plöntu.
 * sniðugt væri að hafa hér staðinn sem streng auk myndarinnar, t.d. EYDIMORK("mynd.png", "eyðimörk").
 * Gæti líka verið stuttur texti sem birtist í plöntuglugganum.
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


    private String stadur;

    Uppruni(String stadur) {
        this.stadur = stadur;
    }

    public String getStadur() {
        return stadur;
    }
}
