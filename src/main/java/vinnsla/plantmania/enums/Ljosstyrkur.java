package vinnsla.plantmania.enums;

/**
 * Enum fyrir ákjósanlegan ljósstyrk fyrir plöntuna.
 */
public enum Ljosstyrkur {
    OBEINT("óbeint"),
    HALFBEINT("hálfbeint"),
    BEINT("beint");

    private String styrkur;//strengur með gildinu

    Ljosstyrkur(String styrkur) {
        this.styrkur = styrkur;
    }

    public String getStyrkur() {
        return styrkur;
    }
}
