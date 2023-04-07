package vidmot.plantmania;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public enum View {
    UPPHAFSSIDA("upphaf-view.fxml"),
    ADALSIDA("mania-view.fxml"),
    GLUGGI("plontugluggi-view.fxml");
    private final String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
