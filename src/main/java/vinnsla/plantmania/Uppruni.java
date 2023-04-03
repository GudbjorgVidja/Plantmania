package vinnsla.plantmania;

public enum Uppruni {
    EYDIMORK("mynd"),
    REGNSKOGUR(""),
    SLETTUR(""),
    SALTVATN(""),
    FERSKVATN(""),
    FJALLLENDI(""),
    GRASLENDI(""),
    SKOGLENDI("");

    private String mynd;

    Uppruni(String mynd) {
        this.mynd = mynd;
    }

    public String getMynd() {
        return mynd;
    }
}
