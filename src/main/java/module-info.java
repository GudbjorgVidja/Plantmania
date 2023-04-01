module vidmot.plantmania {
    requires javafx.controls;
    requires javafx.fxml;


    opens vidmot.plantmania to javafx.fxml;
    exports vidmot.plantmania;
}
