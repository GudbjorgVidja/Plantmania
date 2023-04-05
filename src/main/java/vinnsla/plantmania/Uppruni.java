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
