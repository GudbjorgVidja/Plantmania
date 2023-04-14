package vinnsla.plantmania.enums;

/**
 * Enum fyrir ákjósanlegan ljósstyrk fyrir plöntuna. Bæta kannski við skuggþolin
 */
public enum Ljosstyrkur {
    OBEINT("óbeint"),
    HALFBEINT("hálfbeint"),
    BEINT("beint");

    private String styrkur;

    Ljosstyrkur(String styrkur) {
        this.styrkur = styrkur;
    }

    public String getStyrkur() {
        return styrkur;
    }
}
