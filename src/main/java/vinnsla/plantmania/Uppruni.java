/**
 * enum fyrir uppruna plöntu.
 * sniðugt væri að hafa hér staðinn sem streng auk myndarinnar, t.d. EYDIMORK("mynd.png", "eyðimörk").
 * Gæti líka verið stuttur texti sem birtist í plöntuglugganum.
 */
package vinnsla.plantmania;

public enum Uppruni {
    EYDIMORK("mynd"),
    REGNSKOGUR(""),
    SLETTUR(""),
    SALTVATN(""),
    FERSKVATN(""),
    FJALLLENDI(""),
    GRASLENDI(""),
    SKOGLENDI(""),
    HITABELTI("");

    private String mynd;

    Uppruni(String mynd) {
        this.mynd = mynd;
    }

    public String getMynd() {
        return mynd;
    }
}
