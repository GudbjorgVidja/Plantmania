/**
 * enum fyrir uppruna plöntu.
 * sniðugt væri að hafa hér staðinn sem streng auk myndarinnar, t.d. EYDIMORK("mynd.png", "eyðimörk").
 * Gæti líka verið stuttur texti sem birtist í plöntuglugganum.
 */
package vinnsla.plantmania.enums;

//TODO: taka út slóð fyrir mynd?
//todo: já, allavega til að byrja með
public enum Uppruni {
    EYDIMORK("eyðimörk"),
    REGNSKOGUR("regnskógur"),
    SLETTUR("sléttur"),
    SALTVATN("saltvatn"),
    FERSKVATN("ferskvatn"),
    FJALLLENDI("fjalllendi"),
    GRASLENDI("graslendi"),
    SKOGLENDI("skóglendi"),
    HITABELTI("hitabelti");

    private String mynd;

    private String stadur;

    Uppruni(String stadur) {//String mynd,
        //this.mynd = mynd;
        this.stadur = stadur;
    }

    /*
    public String getMynd() {
        return mynd;
    }

     */

    public String getStadur() {
        return stadur;
    }
}
