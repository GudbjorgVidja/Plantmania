module vidmot.plantmania {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens vidmot.plantmania to javafx.fxml;
    exports vidmot.plantmania;
    exports vinnsla.plantmania to com.fasterxml.jackson.databind;

}
