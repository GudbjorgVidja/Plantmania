package vinnsla.plantmania.enums;

//kannski líka skuggaþolin? eða ekki
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
