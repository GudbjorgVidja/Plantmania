module vidmot.plantmania {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires algs4;


    opens vidmot.plantmania to javafx.fxml;
    exports vidmot.plantmania;
    exports vinnsla.plantmania to com.fasterxml.jackson.databind;
    //exports vidmot.plantmania.deserializers;
    //opens vidmot.plantmania.deserializers to javafx.fxml;

}
